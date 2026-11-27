package com.alexkrug.backend.dto;

public record TaskDto(
        String title,
        String description,
        String createdAt,
        Boolean completed,
        String completedAt
) {
}
