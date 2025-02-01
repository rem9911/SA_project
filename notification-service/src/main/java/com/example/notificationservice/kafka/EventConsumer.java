package com.example.notificationservice.kafka;

import com.example.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventConsumer {

    /*private final NotificationService notificationService;

    public EventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "book-events", groupId = "notification-group")
    public void consumeBookEvents(String message) {
        System.out.println("Received book event: " + message);
        notificationService.sendNotification("Book event: " + message);
    }

    @KafkaListener(topics = "loan-events", groupId = "notification-group")
    public void consumeLoanEvents(String message) {
        System.out.println("Received loan event: " + message);
        notificationService.sendNotification("Loan event: " + message);
    }*/
    private final List<String> notifications = new ArrayList<>();

    @KafkaListener(topics = "book-events", groupId = "notification-group")
    public void consumeBookEvents(String message) {
        System.out.println("Received book event: " + message);
        notifications.add("Book event: " + message);
    }

    @KafkaListener(topics = "loan-events", groupId = "notification-group")
    publ