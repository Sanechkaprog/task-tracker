package com.alexkrug.backend.dto.response;

public record TaskDto(
        Long taskId,
        String title,
        String description,
        String createdAt,
        Boolean completed,
        String completedAt
) {
}
