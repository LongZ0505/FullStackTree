package com.longsocial.follow.controller;

import com.longsocial.follow.dto.ApiResponse;
import com.longsocial.follow.dto.request.RelationDeleteRequest;
import com.longsocial.follow.dto.request.RelationRequest;
import com.longsocial.follow.dto.request.UserNodeRequest;
import com.longsocial.follow.dto.response.RelationResponse;
import com.longsocial.follow.dto.response.TreeResponse;
import com.longsocial.follow.dto.response.UserNodeResponse;
import com.longsocial.follow.dto.response.UserResponse;
import com.longsocial.follow.service.UserNodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/userNode")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserNodeController {
    UserNodeService userNodeService;
    @PostMapping("/createNode")
    public ApiResponse<UserNodeResponse> createNode(@RequestBody UserNodeRequest request){
        log.info(request.toString());
        return ApiResponse.<UserNodeResponse>builder()
                .result(userNodeService.createUserNode(request))
                .build();
    }
    @PutMapping("/userNode/{id}")
    public  ApiResponse<?> updateInfor(@PathVariable String id,@RequestBody UserNodeRequest request){
        return ApiResponse.builder()
                .result(userNodeService.updateUserNode(id,request))
                .build();
    }
    @GetMapping("/getNode/{userId}")
    public ApiResponse<UserResponse> getNode(@PathVariable("userId") String request){
        log.info(request.toString());
        return ApiResponse.<UserResponse>builder()
                .result(userNodeService.getNode(request))
                .build();
    }
    @DeleteMapping("/userNode/{id}")
    public ApiResponse<?> deleteNode(@PathVariable("id") String id){
        log.info(id);
        userNodeService.deleteNode(id);
        return ApiResponse.builder()
                .result("Node has been deleted")
                .build();
    }
    @PostMapping("/relation")
    public ApiResponse<RelationResponse> createRelation(@RequestBody RelationRequest request) {
        log.info(request.toString());
        return ApiResponse.<RelationResponse>builder()
                .result(userNodeService.creationRelation(request))
                .build();
    }


    @GetMapping("/allUserNode")
    public ApiResponse<TreeResponse>
    getAllUserNode(){
        return ApiResponse.<TreeResponse>builder()
                .result(userNodeService.getAllUserNode())
                .build();
    }
    @GetMapping("/allNode")
    public  ApiResponse<List<UserNodeResponse>> getAllNodes(){
        return ApiResponse.<List<UserNodeResponse>>builder()
                .result(userNodeService.getAllNodes())
                .build();
    }
    @DeleteMapping("/ralations/{relationId}")
    public ApiResponse<?> deleteRelation(@PathVariable("relationId") long relationId){
        log.info("id: ",relationId);
        userNodeService.deleteRelation(relationId);
        return ApiResponse.builder()
                .result("Deleted relation")
                .build();
    }
    @GetMapping("/query/{query}")
    public ApiResponse<List<UserResponse>> queryUser(@PathVariable("query") String query) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userNodeService.queryUser(query))
                .build() ;
    }
}
