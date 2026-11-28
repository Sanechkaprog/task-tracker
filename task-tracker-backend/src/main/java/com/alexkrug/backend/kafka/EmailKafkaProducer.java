package com.alexkrug.backend.kafka;

import com.alexkrug.backend.dto.request.EmailSendingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailKafkaProducer {
    @Value("${KAFKA_TOPIC_NAME}")
    private String topicEmailSending;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendWelcomeEmailTo(String email) {
        EmailSendingRequest emailSendingRequest = new EmailSendingRequest(
                email,
                "WELCOME_EMAIL",
                "Welcome to Task-Tracker!"
        );

        CompletableFuture.supplyAsync(() -> kafkaTemplate.send(topicEmailSending, emailSendingRequest).join())
                .orTimeout(10, TimeUnit.SECONDS)
                .handle((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send welcome email request to Kafka for: {}", email, ex.getCause());
                    } else {
                        log.info("Sent welcome email request to Kafka: {}", emailSendingRequest);
                    }
                    return null;
                });

    }
}
