package com.antonio.taskmanager.mapper;

import org.springframework.stereotype.Component;
import com.antonio.taskmanager.dto.UserResponseDTO;
import com.antonio.taskmanager.entity.User;

@Component
public class UserMapper {
    public UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
