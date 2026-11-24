package com.alexkrug.backend.controller;

import com.alexkrug.backend.dto.CredentialsRequest;
import com.alexkrug.backend.dto.SuccessRegistrationResponse;
import com.alexkrug.backend.exception.UserAlreadyRegisteredException;
import com.alexkrug.backend.security.jwt.JwtUtils;
import com.alexkrug.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<SuccessRegistrationResponse> signUp(@RequestBody CredentialsRequest credentials) {
        String username = credentials.username();
        String password = credentials.password();

        if (userService.isUserRegistered(username)) {
            throw new UserAlreadyRegisteredException("User already registered");
        }
    }
}
