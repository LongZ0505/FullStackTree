package com.group.identity_service.repository.client;



import com.group.identity_service.configuration.AuthenticationRequestInterceptor;
import com.group.identity_service.dto.ApiResponse;
import com.group.identity_service.dto.request.UserNodeRequest;
import com.group.identity_service.dto.response.UserNodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserNode-client", url = "${app.services.follow}",
        configuration = AuthenticationRequestInterceptor.class)
public interface UserNodeClient {
    @PostMapping(value = "/internal/createNode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserNodeResponse> createNode
            (@RequestBody UserNodeRequest request);

    @PutMapping(value = "/internal/updateUserNode", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateInfor(@RequestBody UserNodeRequest request);

}
