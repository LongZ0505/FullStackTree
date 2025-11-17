package com.group.identity_service.controller;

import com.group.identity_service.dto.ApiResponse;
import com.group.identity_service.dto.request.IntrospectRequest;
import com.group.identity_service.dto.request.LoginRequest;
import com.group.identity_service.dto.request.RefreshRequest;
import com.group.identity_service.dto.response.AuthenticationResponse;
import com.group.identity_service.dto.response.IntrospectResponse;
import com.group.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {
    AuthenticationService service;
    @PostMapping("/login")
    public  ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request) throws JOSEException {
        log.info("okl",request.getUsername());
        return ApiResponse.<AuthenticationResponse>builder()
                .result(service.login(request))
                .build();
    }
//    @PostMapping("/logout")
//    public  ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
//        service.logout(request);
//        return ApiResponse.<Void>builder()
//                .build();
//    }
    @PostMapping("/refresh")
    public  ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(service.refreshToken(request))
                .build();
    }
    @PostMapping("/introspect")
    public  ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(service.introspect(request))
                .build();
    }
    // password send email
    @PostMapping("/password")
    public  ApiResponse<IntrospectResponse> forgotPassword(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(service.introspect(request))
                .build();
    }
}
