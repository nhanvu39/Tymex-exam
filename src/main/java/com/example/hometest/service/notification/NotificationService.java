package com.example.hometest.service.notification;

public interface NotificationService {
    void sendNotification(Long userId, String content) throws Exception;
}
