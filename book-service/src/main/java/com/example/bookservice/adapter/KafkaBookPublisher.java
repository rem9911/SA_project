package com.example.bookservice.adapter;

import com.example.bookservice.kafka.BookProducer;
import org.springframework.stereotype.Component;

@Component("kafkaPublisher")
public class KafkaBookPublisher implements MessagePublisher {

    private final BookProducer bookProducer;

    public KafkaBookPublisher(BookProducer bookProducer) {
        this.bookProducer = bookProducer;
    }

    @Override
    public void sendMessage(String message) {
        bookProducer.sendBookEvent("📢 Kafka: " + message);
    }
}

