package com.devteria.notification.controller;


import com.devteria.notification.dto.request.NotificationRequest;
import com.devteria.notification.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;

    @KafkaListener(topics = "notification")
    public void listenNotificationDelivery(String message){
        log.info("Message received: {}", message);
        notificationService.createNotification(NotificationRequest.builder().content(message).build());
    }

}
