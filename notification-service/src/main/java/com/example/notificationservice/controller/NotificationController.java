package com.example.notificationservice.controller;

import com.example.notificationservice.kafka.EventConsumer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EventConsumer eventConsumer;

    public NotificationController(EventConsumer eventConsumer) {
        this.eventConsumer = eventConsumer;
    }

    @Operation(summary = "Retrieve notifications from the Notification Service",
            description = "Fetches a list of notifications received via Kafka events.")
    @GetMapping
    public List<String> getNotifications() {
        return eventConsumer.getNotifications();
    }
}
