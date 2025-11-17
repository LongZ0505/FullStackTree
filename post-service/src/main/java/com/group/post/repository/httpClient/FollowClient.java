package com.group.post.repository.httpClient;

import com.group.post.configuration.AuthenticationRequestInterceptor;
import com.group.post.dto.response.ApiResponse;
import com.group.post.dto.response.UserNodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "follow-client", url = "${app.services.follow}",
        configuration = AuthenticationRequestInterceptor.class)
public interface FollowClient {
    @GetMapping(value = "/follows/getFollowees/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UserNodeResponse>> getFollowees(@PathVariable("userId") Integer request);
}
