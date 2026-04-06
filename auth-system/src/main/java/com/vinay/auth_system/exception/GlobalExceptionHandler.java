package com.vinay.auth_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredential(BadCredentialsException badCredentialsException){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", "Invalid email or password",
                             "type", "error"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException runtimeException){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", runtimeException.getMessage()));
    }
}






