package com.alexkrug.backend.dto;

public record CredentialsRequest (
        String username,
        String password,
        String email
) {
}
