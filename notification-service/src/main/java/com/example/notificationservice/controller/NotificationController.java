package com.example.notificationservice.controller;

import com.example.notificationservice.kafka.EventConsumer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    /*private final List<String> notifications = new ArrayList<>();

    @PostMapping
    public void addNotification(@RequestBody String notification) {
        notifications.add(notification);
    }

    @GetMapping
    public List<String> getNotifications() {
        return notifications;
    }*/
    private final EventConsumer eventConsumer;

    public NotificationController(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @GetMapping
    public List<String> getNotifications() {
        return eventConsumer.getNotifications();
    }
}
