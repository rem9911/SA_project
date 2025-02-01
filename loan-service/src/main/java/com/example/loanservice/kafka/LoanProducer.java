package com.example.loanservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoanProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public LoanProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLoanEvent(String message) {
        kafkaTemplate.send("loan-events", message);
    }
}