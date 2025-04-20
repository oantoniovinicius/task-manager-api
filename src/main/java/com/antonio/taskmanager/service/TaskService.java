package com.antonio.taskmanager.service;

import org.springframework.stereotype.Service;

import com.antonio.taskmanager.dto.*;
import com.antonio.taskmanager.repository.TaskRepository;

import jakarta.validation.Valid;

import com.antonio.taskmanager.entity.Task;
import com.antonio.taskmanager.mapper.TaskMapper;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService (TaskRepository taskRepository, TaskMapper taskMapper){ 
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDTO createTask (@Valid TaskRequestDTO dto){
        Task task = taskMapper.toEntity(dto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask); 
    }
}   
