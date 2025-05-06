package com.antonio.taskmanager.security;

import com.antonio.taskmanager.entity.User;
import com.antonio.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .map(user -> new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
            ))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}