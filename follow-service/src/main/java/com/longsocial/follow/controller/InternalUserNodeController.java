package com.longsocial.follow.controller;

import com.longsocial.follow.dto.ApiResponse;
import com.longsocial.follow.dto.request.UserNodeRequest;
import com.longsocial.follow.dto.response.UserNodeResponse;
import com.longsocial.follow.service.UserNodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapping;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/internal")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserNodeController {
    UserNodeService userNodeService;
    @PostMapping("/createNode")
    public ApiResponse<UserNodeResponse> createNode(@RequestBody UserNodeRequest request){
        log.info(request.toString());
        return ApiResponse.<UserNodeResponse>builder()
                .result(userNodeService.createUserNode(request))
                .build();
    }
//    @PutMapping("/updateUserNode")
//    public  ApiResponse<?> updateInfor(@RequestBody UserNodeRequest request){
//        return ApiResponse.builder()
//                .result(userNodeService.updateUserNode(request))
//                .build();
//    }
}
