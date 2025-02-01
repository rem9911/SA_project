package com.example.notificationservice.service;

import org.springframework.stereotype.Component;

@Component
public class SMSNotification implements Observer {

    @Override
    public void update(String message) {
        System.out.println("SMS Notification: " + message);
    }
}