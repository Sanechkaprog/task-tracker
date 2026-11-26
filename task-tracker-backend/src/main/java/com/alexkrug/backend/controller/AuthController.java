package com.alexkrug.backend.controller;

import com.alexkrug.backend.dto.AuthRequest;
import com.alexkrug.backend.dto.SuccessJwtResponse;
import com.alexkrug.backend.security.jwt.JwtUtils;
import com.alexkrug.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
@Validated
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("login")
    public ResponseEntity<SuccessJwtResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        String username = authRequest.username();
        String password = authRequest.password();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        ResponseCookie cookie = ResponseCookie
                .from("jwt", jwtToken)
                .path("/")
                .maxAge(Duration.ofMillis(jwtUtils.getJwtExpirationMs()))
                .httpOnly(true)
                .build();


        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SuccessJwtResponse(jwtToken));

    }
}
