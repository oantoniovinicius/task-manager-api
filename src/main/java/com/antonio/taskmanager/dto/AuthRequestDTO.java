package com.antonio.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @Email(message = "The email must be a valid email address")
    @NotBlank(message = "The email is required and cannot be empty")
    private String email;

    @NotBlank(message = "The password is required and cannot be empty")
    private String password;
}
