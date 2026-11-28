package com.alexkrug.sender.service;

import com.alexkrug.sender.dto.EmailSendingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_FROM}")
    private String sender;

    public void send(EmailSendingRequest message) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom(sender);
        email.setTo(message.to());
        email.setSubject(message.subject());
        email.setText(message.body());

        mailSender.send(email);
    }
}