package com.alexkrug.sender.kafka;

import com.alexkrug.sender.dto.EmailSendingRequest;
import com.alexkrug.sender.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailKafkaListener {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${KAFKA_TOPIC_NAME}",
            groupId = "email-sender-group"
    )

    public void consume(EmailSendingRequest message) {

        log.info(
                "Received email message for {}",
                message.to()
        );

        emailService.send(message);

        log.info(
                "Email successfully sent to {}",
                message.to()
        );
    }
}