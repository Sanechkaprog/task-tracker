package com.alexkrug.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CredentialsRequest (

        @NotBlank(message = "Username mustn`t be blank")
        @Size(max = 50, message = "Max permissible length is 50 symbols")
        String username,

        @NotBlank(message = "Password mustn`t be blank")
        @Size(max = 300, message = "Max permissible length is 300 symbols")
        String password,

        @Email(message = "Email must be like test@gmail.com")
        @NotBlank
        String email
) {
}
