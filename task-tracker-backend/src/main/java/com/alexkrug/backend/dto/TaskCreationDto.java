package com.alexkrug.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreationDto(
        @NotBlank(message = "Title can`t be blank")
        @Size(max = 250, message = "Title`s length must be less than 250 symbols")
        String title,

        @NotBlank(message = "Description can`t be blank")
        @Size(max = 2000, message = "Description`s length must be less than 2000")
        String description
) {
}
