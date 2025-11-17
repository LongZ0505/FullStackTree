package com.longsocial.chat.repository.httpClient;

import com.longsocial.chat.dto.request.IntrospectRequest;
import com.longsocial.chat.dto.response.ApiResponse;
import com.longsocial.chat.dto.response.IntrospectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Identity-Public", url = "${app.services.identity}")
public interface IdentityIntrospect{
    @PostMapping("/auth/introspect")
    public  ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request);
}
