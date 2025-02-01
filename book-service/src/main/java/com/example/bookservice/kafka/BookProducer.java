package com.example.bookservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.logging.LoggerService;


@Service
public class BookProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final LoggerService logger = LoggerService.getInstance();


    public BookProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookEvent(String message) {
        logger.info("Publishing to Kafka: " + message);
        kafkaTemplate.send("book-events", message);
    }
}