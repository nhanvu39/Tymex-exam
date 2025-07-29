package com.example.hometest.controller.notification;

import com.example.hometest.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send/{userId}")
    public ResponseEntity sendNotification(@PathVariable Long userId, @RequestParam String message) throws Exception {
        notificationService.sendNotification(userId, message);
        return ResponseEntity.ok().build();
    }
}
