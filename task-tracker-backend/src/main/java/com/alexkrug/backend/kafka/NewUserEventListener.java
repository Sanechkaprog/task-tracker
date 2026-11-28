package com.alexkrug.backend.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewUserEventListener {
    private final EmailKafkaProducer producer;
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlerUserCreated(String username) {
        producer.sendWelcomeEmailTo(username);
        log.info("EMAIL SENT");
    }
}
