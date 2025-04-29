package com.antonio.taskmanager.dto;

import java.time.LocalDate;
import com.antonio.taskmanager.enums.Priority;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskPatchDTO {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Boolean completed;
}
