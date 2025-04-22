package com.antonio.taskmanager.dto;

import java.time.LocalDate;

import com.antonio.taskmanager.enums.Priority;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TaskRequestDTO {

    @NotBlank(message = "The task title is required and cannot be empty")
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @Future
    private LocalDate dueDate;

    @NotNull
    private Priority priority;

    public boolean isCompleted() {
        return false;
    }
}
