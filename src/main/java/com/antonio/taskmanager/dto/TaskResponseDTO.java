package com.antonio.taskmanager.dto;

import java.util.UUID;

import jakarta.annotation.Priority;

public class TaskResponseDTO {
    private UUID id;
    private String title;
    private boolean completed;
    private String dueDate;
    private Priority priority;
    private String createdAt;
}
