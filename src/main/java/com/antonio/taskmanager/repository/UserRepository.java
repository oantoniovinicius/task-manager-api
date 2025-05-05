package com.antonio.taskmanager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonio.taskmanager.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    // This interface will automatically provide CRUD operations for the User entity
    // You can add custom query methods here if needed
    Optional<User> findByEmail(String email);
}
