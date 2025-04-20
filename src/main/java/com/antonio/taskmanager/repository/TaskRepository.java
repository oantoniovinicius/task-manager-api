package com.antonio.taskmanager.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.antonio.taskmanager.entity.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    // This interface will automatically provide CRUD operations for the Task entity
    // You can add custom query methods here if needed
}
