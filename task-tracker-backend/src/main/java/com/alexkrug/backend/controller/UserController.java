package com.alexkrug.backend.controller;

import com.alexkrug.backend.domain.User;
import com.alexkrug.backend.dto.CredentialsRequest;
import com.alexkrug.backend.dto.SuccessJwtResponse;
import com.alexkrug.backend.dto.UserDto;
import com.alexkrug.backend.exception.EmailExistException;
import com.alexkrug.backend.exception.UserAlreadyRegisteredException;
import com.alexkrug.backend.mapper.ServiceMapper;
import com.alexkrug.backend.security.UserSecurity;
import com.alexkrug.backend.security.jwt.JwtUtils;
import com.alexkrug.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final ServiceMapper serviceMapper;
    @PostMapping
    public ResponseEntity<SuccessJwtResponse> signUp(@Valid @RequestBody CredentialsRequest credentials) {
        String username = credentials.username();
        String password = credentials.password();
        String email = credentials.email();

        if (userService.isUserRegistered(username)) {
            throw new UserAlreadyRegisteredException("User already registered");
        }

        if (userService.isEmailExist(email)) {
            throw new EmailExistException("User with this email already registered");
        }

        User user = userService.saveUser(username, password, email);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                new UserSecurity(user),
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
                .body(new SuccessJwtResponse(token));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(@Valid @AuthenticationPrincipal UserSecurity userSecurity) {
        User user = userService.getUser(userSecurity.getUsername());
        return ResponseEntity
                .ok()
                .body(serviceMapper.userToDto(user));
    }

}
