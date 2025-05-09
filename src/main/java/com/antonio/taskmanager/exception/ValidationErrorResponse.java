package com.antonio.taskmanager.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors, String path) {
        super(LocalDateTime.now(), status, "Validation Error", message, path);
        this.errors = errors;
    }
}