package com.longsocial.gateway.configuration;

import com.longsocial.gateway.dto.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longsocial.gateway.repository.httpClient.IdentityClient;
import com.longsocial.gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

import java.awt.desktop.PreferencesEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationGateWay implements GlobalFilter, Ordered {
    @NonFinal
    private String[] PUBLIC_ENTRYPOINT= {"/identity/.*","/userNode","/follow/.*"};
    IdentityService identityService;
    ObjectMapper objectMapper;
    @Value("${app.api-prefix}")
    @NonFinal
    String prefix;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEntry(exchange.getRequest())) return chain.filter(exchange);
        var lstToken =exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(lstToken)) return  unauthenticated(exchange.getResponse());
        var token = lstToken.getFirst();
        identityService.introspect(token).flatMap(introspectResponseApiResponse ->
        {
           if(introspectResponseApiResponse.getResult().isValid())
               return chain.filter(exchange);
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
        return  chain.filter(exchange);
    }
    public boolean isPublicEntry(ServerHttpRequest request){
        return Arrays.stream(PUBLIC_ENTRYPOINT).anyMatch(s
                -> request.getURI().getPath().matches(prefix+s));
    }
    public Mono<Void> unauthenticated(ServerHttpResponse response)  {
        ApiResponse<?> apiResponse= ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE ,MediaType.APPLICATION_JSON_VALUE);
        try {
            String body = objectMapper.writeValueAsString(apiResponse);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public int getOrder() {
        return -1;
    }
}
