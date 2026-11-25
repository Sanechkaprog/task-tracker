package com.alexkrug.backend.controller;

import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.CredentialsRequest;
import com.alexkrug.backend.dto.SuccessRegistrationResponse;
import com.alexkrug.backend.dto.UserDto;
import com.alexkrug.backend.exception.UserAlreadyRegisteredException;
import com.alexkrug.backend.mapper.ServiceMapper;
import com.alexkrug.backend.security.UserSecurity;
import com.alexkrug.backend.security.jwt.JwtUtils;
import com.alexkrug.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;

@Slf4j
@Controller
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final ServiceMapper serviceMapper;
    @PostMapping
    public ResponseEntity<SuccessRegistrationResponse> signUp(@RequestBody CredentialsRequest credentials) {
        String username = credentials.username();
        String password = credentials.password();
        String email = credentials.email();

        if (userService.isUserRegistered(username)) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        User user = userService.saveUser(username, password, email);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                username,
                null,
                new UserSecurity(user).getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);

        ResponseCookie cookie = ResponseCookie
                .from("jwt", token)
                .path("/")
                .maxAge(Duration.ofMillis(jwtUtils.getJwtExpirationMs()))
                .httpOnly(true)
                .build();


        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new SuccessRegistrationResponse(token));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(@CookieValue("jwt") String jwt) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userService.getUser(username);
        return ResponseEntity
                .ok()
                .body(serviceMapper.userToDto(user));
    }

}
