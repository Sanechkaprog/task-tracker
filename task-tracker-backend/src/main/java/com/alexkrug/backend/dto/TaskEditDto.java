package com.alexkrug.backend.dto;
import jakarta.validation.constraints.Size;

public record TaskEditDto (
        @Size(max = 250, message = "Titles`s length must be less than 250 symbols")
        String title,

        @Size(max = 2000, message = "Description`s length must be less than 2000 symbols")
        String description,

        Boolean completed
){
}
