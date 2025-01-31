package com.example.notificationservice.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(String message) {
        // Simule l'envoi d'une notification
        System.out.println("Sending notification: " + message);
    }

}
