package com.alexkrug.backend.dto.request;


public record EmailSendingRequest (
        String to,
        String subject,
        String body
) {
}
