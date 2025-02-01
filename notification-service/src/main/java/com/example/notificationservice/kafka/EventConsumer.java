package com.example.notificationservice.kafka;

import com.example.notificationservice.service.EmailNotification;
import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.SMSNotification;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventConsumer {
    private final List<String> notifications = new ArrayList<>();
    private final NotificationService notificationService;

    public EventConsumer(NotificationService notificationService,
                         EmailNotification emailNotification,
                         SMSNotification smsNotification) {
        this.notificationService = notificationService;

        this.notificationService.addObserver(emailNotification);
        this.notificationService.addObserver(smsNotification);
    }

    @KafkaListener(topics = "book-events", groupId = "notification-group")
    public void consumeBookEvents(String message) {
        System.out.println("Received book event: " + message);
        notifications.add("Book event: " + message);
        notificationService.notifyObservers("Kafka message " + message + " consumed successfully.");
    }

    @KafkaListener(topics = "loan-events", groupId = "notification-group")
    public void consumeLoanEvents(String message) {
        System.out.println("Received loan event: " + message);
        notifications.add("Loan event: " + message);
        notificationService.notifyObservers("Kafka message " + message + " consumed successfully.");
    }

    public List<String> getNotifications() {
        return notifications;
    }
}
