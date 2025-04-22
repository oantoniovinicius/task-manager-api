package com.antonio.taskmanager.service;

import org.springframework.stereotype.Service;

import com.antonio.taskmanager.dto.*;
import com.antonio.taskmanager.repository.TaskRepository;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.antonio.taskmanager.entity.Task;
import com.antonio.taskmanager.mapper.TaskMapper;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);


    public TaskService (TaskRepository taskRepository, TaskMapper taskMapper){ 
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDTO createTask (@Valid TaskRequestDTO dto){
        logger.info("Creating a new task with title: {}", dto.getTitle());

        Task task = taskMapper.toEntity(dto);
        logger.debug("Entity created: {}", task);

        Task savedTask = taskRepository.save(task);
        logger.info("Task saved with ID: {}", savedTask.getId());
        
        return taskMapper.toDTO(savedTask); 
    }
}   
