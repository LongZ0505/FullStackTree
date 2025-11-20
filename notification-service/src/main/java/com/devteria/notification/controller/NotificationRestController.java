package com.devteria.notification.controller;

import com.devteria.notification.dto.ApiResponse;
import com.devteria.notification.dto.response.NotificationResponse;
import com.devteria.notification.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationRestController {
    NotificationService notificationService;

    @GetMapping("/allNotifications")
    ApiResponse<List<NotificationResponse>> getALl(){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotification())
                .build();
    }
}
