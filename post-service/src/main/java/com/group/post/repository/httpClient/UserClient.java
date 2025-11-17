package com.group.post.repository.httpClient;

import com.group.post.configuration.AuthenticationRequestInterceptor;
import com.group.post.dto.response.ApiResponse;
import com.group.post.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "Identity-Service", url = "${app.services.identity}",
        configuration = AuthenticationRequestInterceptor.class)
public interface UserClient {
    @GetMapping(value = "/users/userId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") Integer userId);

    @GetMapping(value = "/users/userName/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> getUserByUserName(@PathVariable("userName") String userName);

    @GetMapping(value = "/users/getExcludingSelf/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UserResponse>> getAllExcludingSelf(@RequestParam("limit") Integer limit,
                                                               @PathVariable("userId") Integer userId);

    @GetMapping(value = "/users/random/{num}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UserResponse>> getRandomUsers(@PathVariable("num") Integer num);
}
