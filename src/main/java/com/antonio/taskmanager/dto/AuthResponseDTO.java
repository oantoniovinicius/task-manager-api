package com.antonio.taskmanager.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private UUID id;
    private String email;
    private String username;
    private String role;
}
