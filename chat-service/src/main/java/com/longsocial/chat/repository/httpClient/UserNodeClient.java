package com.longsocial.chat.repository.httpClient;

import com.longsocial.chat.dto.response.ApiResponse;
import com.longsocial.chat.dto.response.UserNodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserNodeClient", url = "${app.services.follow}")
public interface UserNodeClient {
    @GetMapping("/userNode/getNode/{userId}")
    public ApiResponse<UserNodeResponse> getNode(@PathVariable("userId") String request);
}
