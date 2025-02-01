package com.example.bookservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BookProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookEvent(String message) {
        System.out.println("Publishing to Kafka: " + message);
        kafkaTemplate.send("book-events", message);
    }
}