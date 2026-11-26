package com.alexkrug.backend.handler;

import com.alexkrug.backend.dto.ErrorResponse;
import com.alexkrug.backend.exception.EmailExistException;
import com.alexkrug.backend.exception.UserAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;
import java.util.List;

@RestControllerAdvice
public class BackendExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsException() {
        return new ResponseEntity<>(
                new ErrorResponse("Invalid username or password"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> registeredUser(UserAlreadyRegisteredException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<ErrorResponse> registeredUser(EmailExistException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> patternConstraints(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errorResponses = ex
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(violation -> new ErrorResponse(violation.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(
                errorResponses,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> internalServerErrors() {
        return new ResponseEntity<>(
                new ErrorResponse("Internal server error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
