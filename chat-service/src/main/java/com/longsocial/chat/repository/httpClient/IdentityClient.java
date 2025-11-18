package com.longsocial.chat.repository.httpClient;

import com.longsocial.chat.configuration.AuthenticationRequestInterceptor;
import com.longsocial.chat.dto.request.IntrospectRequest;
import com.longsocial.chat.dto.response.ApiResponse;
import com.longsocial.chat.dto.response.IntrospectResponse;
import com.longsocial.chat.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Identity-Service", url = "${app.services.identity}",
        configuration = AuthenticationRequestInterceptor.class)
public interface IdentityClient {
    @GetMapping(value = "/users/userId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> getUserById(@PathVariable("userId") String userId);

}
