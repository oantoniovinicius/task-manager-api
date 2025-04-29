package com.antonio.taskmanager.dto;

import java.util.UUID;

import com.antonio.taskmanager.enums.Priority;
import lombok.Data;

@Data
public class TaskResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private String dueDate;
    private Priority priority;
    private String createdAt;
    private String updatedAt;
}
