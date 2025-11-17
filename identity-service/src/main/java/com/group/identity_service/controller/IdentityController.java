package com.group.identity_service.controller;

import com.group.identity_service.dto.ApiResponse;
import com.group.identity_service.dto.request.CreationRequest;
import com.group.identity_service.dto.response.CreationResponse;
import com.group.identity_service.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class IdentityController {
    IdentityService identityService;
    @PostMapping("/register")
    public ApiResponse<CreationResponse> register(@RequestBody CreationRequest request){
        return ApiResponse.<CreationResponse>builder()
                .result(identityService.Register(request))
                .build();
    }
}
