package com.antonio.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.taskmanager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.antonio.taskmanager.dto.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Endpoints for creating and managing tasks")
public class TaskController {
    private final TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        logger.info("Received request to create task: {}", taskRequestDTO);

        TaskResponseDTO responseDTO = taskService.createTask(taskRequestDTO);
        logger.debug("Task created successfully: {}", responseDTO);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all tasks for current user", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping()
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        logger.info("Received request to get all tasks");
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get tasks by id for the current user", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskByID(@PathVariable UUID id) {
        logger.info("Received request to get task by ID");
        TaskResponseDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Update a task for current user", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable UUID id,
            @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        logger.info("Received request to update task with ID: {}", id);
        TaskResponseDTO task = taskService.updateTask(id, taskRequestDTO);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Delete a task for current user", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        logger.info("Received request to delete task with ID: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Patch a task for current user", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(@PathVariable UUID id, @RequestBody TaskPatchDTO patchDTO) {
        logger.info("Received PATCH request to update task with ID: {}", id);
        TaskResponseDTO task = taskService.patchTask(id, patchDTO);
        return ResponseEntity.ok(task);
    }
}
