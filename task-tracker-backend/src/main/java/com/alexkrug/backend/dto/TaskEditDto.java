package com.alexkrug.backend.dto;

public record TaskEditDto (
        String title,
        String description,
        Boolean completed
){
}
