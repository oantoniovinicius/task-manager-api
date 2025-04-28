package com.antonio.taskmanager.service;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import com.antonio.taskmanager.dto.*;
import com.antonio.taskmanager.repository.TaskRepository;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById (UUID id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        logger.info("Task found with ID: {}", task.getId());
        return taskMapper.toDTO(task);
    }

    public TaskResponseDTO updateTask (UUID id, @Valid TaskRequestDTO dto){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        logger.info("Task found with ID: {}", task.getId());

        if(dto.getTitle() != null){
            task.setTitle(dto.getTitle());
        }
        if(dto.getDescription() != null){
            task.setDescription(dto.getDescription());
        }
        if(dto.getDueDate() != null){
            task.setDueDate(dto.getDueDate());
        }
        if(dto.getPriority() != null){
            task.setPriority(dto.getPriority());
        }
        
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        logger.info("Task updated with ID: {}", updatedTask.getId());

        return taskMapper.toDTO(updatedTask);
    }
}   