package com.antonio.taskmanager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        // if the user sends a request with invalid data (like a null title), this
        // method will be called
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
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
                                .body(errors);
        }

        // if the user send an invalid URL request, that'll be handled outside the JSON
        // response
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex) {
                logger.warn("Constraint Violation: {}", ex.getMessage());

                return ResponseEntity
                                .badRequest()
                                .body(new ErrorResponse(
                                                HttpStatus.BAD_REQUEST.value(),
                                                ex.getMessage(),
                                                LocalDateTime.now()));
        }

        @ExceptionHandler(TaskNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
                logger.warn("Task not found: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(
                                                HttpStatus.NOT_FOUND.value(),
                                                ex.getMessage(),
                                                LocalDateTime.now()));
        }

        // if an exception don't get caught by any of the previous methods, this one
        // will handle it
        // works like a plan B
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
                logger.error("Unexpected error.", ex);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ErrorResponse(
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                "Something went wrong. Please try again later.",
                                                LocalDateTime.now()));
        }
}
