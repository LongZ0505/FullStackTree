package com.longsocial.chat.service;

import com.longsocial.chat.dto.request.IntrospectRequest;
import com.longsocial.chat.dto.response.IntrospectResponse;
import com.longsocial.chat.repository.httpClient.IdentityClient;
import com.longsocial.chat.repository.httpClient.IdentityIntrospect;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class IdentityService {
    IdentityIntrospect identityIntrospect;
    public IntrospectResponse introspect(IntrospectRequest request){
        return identityIntrospect.introspect(request).getResult();
    }
}
