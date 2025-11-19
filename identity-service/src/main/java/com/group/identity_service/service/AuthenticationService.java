package com.group.identity_service.service;

import com.group.identity_service.dto.request.IntrospectRequest;
import com.group.identity_service.dto.request.LoginRequest;
import com.group.identity_service.dto.request.RefreshRequest;
import com.group.identity_service.dto.response.AuthenticationResponse;
import com.group.identity_service.dto.response.IntrospectResponse;
import com.group.identity_service.dto.response.UserNodeResponse;
import com.group.identity_service.entity.InvalidToken;
import com.group.identity_service.exception.AppException;
import com.group.identity_service.exception.ErrorCode;
import com.group.identity_service.repository.IdentityRepository;
import com.group.identity_service.repository.InvalidTokenRepository;
import com.group.identity_service.repository.client.UserNodeClient;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {
    @NonFinal
    @org.springframework.beans.factory.annotation.Value("${jwt.signerKey}")
    protected String SECRET;
    @NonFinal
    @org.springframework.beans.factory.annotation.Value("${jwt.valid-duration}")
    protected long EXPIRE_TIME;
    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected long REFRESH_TIME;
    InvalidTokenRepository invalidTokenRepository;
    IdentityRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserNodeClient userNodeClient;
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var valid = true;
        log.info("toe:{}",request.getToken());
        var check = verifier(request.getToken(), false);
        if (Objects.isNull(check)) {
            valid = false;
        }
        return IntrospectResponse.builder()
                .isValid(valid)
                .userId(check.getJWTClaimsSet().getSubject())
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) throws JOSEException {
        String emailOrUserName = request.getUsername();
        log.info(emailOrUserName);
        var user = userRepository.findByUserName(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!result) throw new AppException(ErrorCode.UNAUTHENTICATED);
        String token = generateToken(user.getId(), user.getUserName());
        log.info(token);
        return AuthenticationResponse.builder().token(token).user(UserNodeResponse.builder()
                        .id(user.getId())
                        .name(userNodeClient.getNode(user.getId()).getResult().getName())
                .build()).build();
    }

    public String generateToken(String id, String userName) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512 );
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(id)
                .claim("userName", userName)
                .jwtID(UUID.randomUUID().toString())
                .issuer("com.long")
                .expirationTime(new Date(Instant.now().plus(EXPIRE_TIME, ChronoUnit.SECONDS).toEpochMilli()))
                .issueTime(new Date())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        jwsObject.sign(new MACSigner(SECRET.getBytes()));
        return jwsObject.serialize();
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifier(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidatedToken =
                InvalidToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidTokenRepository.save(invalidatedToken);

        var userId = signedJWT.getJWTClaimsSet().getSubject();

        var user =
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user.getId(), user.getUserName());

        return AuthenticationResponse.builder().token(token).build();
    }

    public SignedJWT verifier(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(SECRET.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        var check = (isRefresh)
                ? new Date((signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                .plus(REFRESH_TIME, ChronoUnit.SECONDS).toEpochMilli()))
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(jwsVerifier);
        if (!verified || !check.after(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }
}
