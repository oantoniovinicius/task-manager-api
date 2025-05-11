package com.antonio.taskmanager.service;

import com.antonio.taskmanager.enums.Role;
import com.antonio.taskmanager.exception.UserAlreadyExistsException;
import com.antonio.taskmanager.exception.UserNotFoundException;
import com.antonio.taskmanager.dto.AuthRequestDTO;
import com.antonio.taskmanager.dto.AuthResponseDTO;
import com.antonio.taskmanager.entity.User;
import com.antonio.taskmanager.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO register(AuthRequestDTO request) {
        // check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("An account with email " + request.getEmail() + " already exists");
        }

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
                .orElseThrow(() -> new UserNotFoundException("User with email " + request.getEmail() + " not found"));

        // verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        // generate JWT token
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getEmail(), user.getUsername(),
                user.getRole().name());
    }
}
