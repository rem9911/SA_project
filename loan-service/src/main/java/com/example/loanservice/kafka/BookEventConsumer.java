package com.example.loanservice.kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookEventConsumer {

    @KafkaListener(topics = "book-events", groupId = "library-group")
    public void consumeBookEvent(String message) {
        System.out.println("Received Kafka message: " + message);
        // Logique pour traiter l'événement (exemple : notifier un utilisateur)
    }
}
