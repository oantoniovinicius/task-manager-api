package com.antonio.taskmanager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        // if the user sends a request with invalid data (like a null title), this
        // method will be called
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
                // a map to store the field erros and the message
                Map<String, String> errors = new HashMap<>();

                // loop through the field errors and add them to the map
                // ex.getBindingResult().getFieldErrors() returns a list of field errors
                // each field error has a field name and a default message
                for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }

                logger.warn("Validation error: {}", errors);

                // return a 400 status (bad request) with the errors map
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ValidationErrorResponse(
                                                HttpStatus.BAD_REQUEST.value(),
                                                "Validation failed for one or more fields.",
                                                errors,
                                                request.getRequestURI()));
        }

        // if the user send an invalid URL request, that'll be handled outside the JSON
        // response
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex,
                        HttpServletRequest request) {
                logger.warn("Constraint violation: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.BAD_REQUEST.value(),
                                                "Constraint Violation",
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(TaskNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request) {
                logger.warn("Task not found: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.NOT_FOUND.value(),
                                                "Not Found",
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
                logger.warn("User not found: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.NOT_FOUND.value(),
                                                "Not Found",
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex,
                        HttpServletRequest request) {
                logger.warn("User already exists: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.CONFLICT.value(),
                                                "Conflict",
                                                ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
                logger.warn("Access denied: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.FORBIDDEN.value(),
                                                "Forbidden",
                                                "You do not have permission to perform this action.",
                                                request.getRequestURI()));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest request) {
                logger.warn("Bad credentials: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.UNAUTHORIZED.value(),
                                                "Unauthorized",
                                                "Invalid email or password.",
                                                request.getRequestURI()));
        }

        // if an exception don't get caught by any of the previous methods, this one
        // will handle it
        // works like a plan B
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
                logger.error("Unexpected error", ex);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(
                                                LocalDateTime.now(),
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "Internal Server Error",
                                                "Something went wrong. Please try again later.",
                                                request.getRequestURI()));
        }
}
