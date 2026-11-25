package com.alexkrug.backend.dto;

public record AuthRequest (
        String username,
        String password
) {
}
