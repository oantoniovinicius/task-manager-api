package com.antonio.taskmanager.dto;

import java.time.LocalDate;

import com.antonio.taskmanager.enums.Priority;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "The task title is required and cannot be empty")
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @Schema(description = "Must be a future date", example = "2025-12-31")
    @Future
    private LocalDate dueDate;

    @NotNull
    private Priority priority;

    @NotNull
    private Boolean completed;
}
