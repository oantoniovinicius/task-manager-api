package com.antonio.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.taskmanager.service.TaskService;

import jakarta.validation.Valid;

import com.antonio.taskmanager.dto.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO responseDTO = taskService.createTask(taskRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
