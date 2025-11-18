package com.group.identity_service.repository.client;



import com.group.identity_service.configuration.AuthenticationRequestInterceptor;
import com.group.identity_service.dto.ApiResponse;
import com.group.identity_service.dto.request.UserNodeRequest;
import com.group.identity_service.dto.response.UserNodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "UserNode-client", url = "${app.services.follow}",
        configuration = AuthenticationRequestInterceptor.class)
public interface UserNodeClient {
    @PostMapping(value = "/internal/createNode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserNodeResponse> createNode
            (@RequestBody UserNodeRequest request);

    @GetMapping(value = "/userNode/getNode/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserNodeResponse> getNode(@PathVariable("userId") String userId);

}
