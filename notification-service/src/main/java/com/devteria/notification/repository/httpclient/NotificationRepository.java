package com.devteria.notification.repository.httpclient;

import com.devteria.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findAllByOrderByCreateAtDesc();
}
