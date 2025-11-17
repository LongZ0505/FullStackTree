package com.group.post.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes=
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        var auth=servletRequestAttributes.getRequest().getHeader("Authorization");
        log.info("Authentication: {}",auth);
        if(StringUtils.hasText(auth)){
            requestTemplate.header("Authorization",auth);
        }
    }
}
