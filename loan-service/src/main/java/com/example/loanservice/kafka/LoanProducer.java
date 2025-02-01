package com.example.loanservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class LoanProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public LoanProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLoanEvent(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("loan-events", message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Publishing to Kafka succeeded: " + message);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("Failed to publish message: " + message + ". Error: " + ex.getMessage());
                kafkaTemplate.send("loan-events-dlq", message);
            }
        });
    }
}
