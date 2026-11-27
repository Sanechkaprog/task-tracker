package com.alexkrug.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TaskEditDto (
        @Size(max = 250, message = "Titles`s length must be less than 250 symbols")
        @NotBlank(message = "Title can`t be blank")
        String title,

        @Size(max = 2000, message = "Description`s length must be less than 2000 symbols")
        @NotBlank(message = "Description can`t be blank")
        String description,

        @NotEmpty
        Boolean completed
){
}
