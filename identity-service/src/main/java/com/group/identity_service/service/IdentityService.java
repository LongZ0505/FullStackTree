package com.group.identity_service.service;

import com.group.identity_service.constant.Role;
import com.group.identity_service.dto.ApiResponse;
import com.group.identity_service.dto.request.CreationRequest;
import com.group.identity_service.dto.request.UserNodeRequest;
import com.group.identity_service.dto.response.CreationResponse;
import com.group.identity_service.exception.AppException;
import com.group.identity_service.exception.ErrorCode;
import com.group.identity_service.mapper.IdentityMapper;
import com.group.identity_service.repository.IdentityRepository;
import com.group.identity_service.repository.client.UserNodeClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class IdentityService {
    IdentityRepository identityRepository;
    IdentityMapper identityMapper;
    PasswordEncoder passwordEncoder;
    UserNodeClient userNodeClient;

    public CreationResponse Register(CreationRequest request) {
        log.info(request.getUserName());
        var us = identityRepository.findByUserName(request.getUserName());
        if (us.isPresent()) throw new AppException(ErrorCode.USER_EXISTED);
        var user = identityMapper.toUser(request);
        user.setRole(Role.USER.toString());
        log.info("test : {}", user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var u = identityRepository.save(user);
        //clean
        var response = userNodeClient.createNode(UserNodeRequest.builder()
                .userId(u.getId())
                .name(request.getName())
                .dob(request.getDob())
                .sex(request.isSex())
                .build());
        log.info("response: {}", response);
        return identityMapper.toUserResponse(user);
    }
}
