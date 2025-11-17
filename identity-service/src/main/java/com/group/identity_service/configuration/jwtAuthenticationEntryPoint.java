package com.group.identity_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.identity_service.dto.ApiResponse;
import com.group.identity_service.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.mapper.Mapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.awt.*;
import java.io.IOException;

public class jwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var erroCode=ErrorCode.UNAUTHENTICATED;
        response.setStatus(erroCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiResponse<?> result=ApiResponse.builder()
                .code(erroCode.getCode())
                .message(erroCode.getMessage())
                .build();
        ObjectMapper mapper=new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(result));
        response.flushBuffer();
    }
}
