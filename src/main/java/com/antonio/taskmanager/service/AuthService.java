package com.antonio.taskmanager.service;

import org.springframework.stereotype.Service;

import com.antonio.taskmanager.enums.Role;
import com.antonio.taskmanager.mapper.UserMapper;
import com.antonio.taskmanager.dto.AuthRequestDTO;
import com.antonio.taskmanager.dto.AuthResponseDTO;
import com.antonio.taskmanager.dto.UserResponseDTO;

import com.antonio.taskmanager.entity.User;
import com.antonio.taskmanager.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponseDTO register(AuthRequestDTO request) {
        // creating new user
        User user = User.builder().username(request.getEmail().split("@")[0]).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();

        userRepository.save(user);

        // generating JWT token
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getEmail(), user.getUsername(),
                user.getRole().name());
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        // find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // generate JWT token
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getEmail(), user.getUsername(),
                user.getRole().name());
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public User getCurrentUser() { // get the current user who logged in
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
