package com.devteria.notification.service;

import com.devteria.notification.dto.request.NotificationRequest;
import com.devteria.notification.dto.response.NotificationResponse;
import com.devteria.notification.entity.Notification;
import com.devteria.notification.repository.httpclient.NotificationRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;

    public void createNotification(NotificationRequest request) {
        notificationRepository.save(Notification.builder()
                .content(request.getContent())
                .createAt(LocalDate.now())
                .build());
    }

    public List<NotificationResponse> getNotification() {
        return notificationRepository.findAllByOrderByCreateAtDesc().stream().map(notification ->
                        NotificationResponse.builder().content(
                                        notification.getContent())
                                .createAt(notification.getCreateAt())
                                .id(notification.getId())
                                .build())
                .toList();
    }

}
