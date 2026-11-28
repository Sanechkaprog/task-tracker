package com.alexkrug.sender.dto;

public record EmailSendingRequest(
        String to,
        String subject,
        String body
) {
}
