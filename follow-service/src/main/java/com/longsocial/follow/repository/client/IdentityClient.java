package com.longsocial.follow.repository.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.longsocial.follow.configuration.AuthenticationRequestInterceptor;
import com.longsocial.follow.dto.ApiResponse;
import com.longsocial.follow.dto.response.UserNodeResponse;
import com.longsocial.follow.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "identity-client", url = "${app.services.url}",
        configuration = AuthenticationRequestInterceptor.class)
public interface IdentityClient {
    @GetMapping(value = "/users/getAll/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<UserResponse>> getAllUser(
            @PathVariable("userId") Integer userId);

    @GetMapping(value = "/users/userId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") Integer userId);
}
