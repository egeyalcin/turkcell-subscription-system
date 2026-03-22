package com.turkcell.subscriptionservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(LocalizedException.class)
    public ResponseEntity<String> handleLocalizedException(LocalizedException ex, HttpServletRequest request) {
        Locale locale = request.getLocale();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage(locale));
    }

    @ExceptionHandler(SubscriptionProcessException.class)
    public ResponseEntity<String> handleSubscriptionNotExistsException(SubscriptionProcessException ex, HttpServletRequest request) {
        Locale locale = request.getLocale();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage(locale));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}