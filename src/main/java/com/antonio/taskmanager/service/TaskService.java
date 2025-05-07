package com.antonio.taskmanager.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.antonio.taskmanager.dto.*;
import com.antonio.taskmanager.repository.TaskRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.antonio.taskmanager.entity.Task;
import com.antonio.taskmanager.entity.User;
import com.antonio.taskmanager.enums.Role;
import com.antonio.taskmanager.mapper.TaskMapper;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskResponseDTO createTask(@Valid TaskRequestDTO dto) {
        logger.info("Creating a new task with title: {}", dto.getTitle());

        Task task = taskMapper.toEntity(dto);
        logger.debug("Entity created: {}", task);

        User currentUser = authService.getCurrentUser();
        task.setUser(currentUser);

        Task savedTask = taskRepository.save(task);
        logger.info("Task saved with ID: {}", savedTask.getId());

        return taskMapper.toDTO(savedTask);
    }

    public List<TaskResponseDTO> getAllTasks() {
        User currentUser = authService.getCurrentUser();

        List<Task> tasks;
        if (currentUser.getRole() == Role.ADMIN) { // admin can see all tasks
            tasks = taskRepository.findAll();
        } else { // regular user can only see their own tasks
            tasks = taskRepository.findAllByUser(currentUser);
            logger.info("Tasks found for user: {}", currentUser.getId());
        }

        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        logger.info("Task found with ID: {}", task.getId());

        User currentUser = authService.getCurrentUser(); // get the current user
        if (!task.getUser().getId().equals(currentUser.getId())) { // this line checks if the task belongs to the
                                                                   // current user
            throw new RuntimeException("You do not have permission to access this task");
        }

        return taskMapper.toDTO(task);
    }

    public TaskResponseDTO updateTask(UUID id, @Valid TaskRequestDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        logger.info("Task found with ID: {}", task.getId());

        User currentUser = authService.getCurrentUser();
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this task");
        }

        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getDueDate() != null) {
            task.setDueDate(dto.getDueDate());
        }
        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        }

        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        logger.info("Task updated with ID: {}", updatedTask.getId());

        return taskMapper.toDTO(updatedTask);
    }

    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        logger.info("Task found with ID: {}", task.getId());

        User currentUser = authService.getCurrentUser();
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this task");
        }

        taskRepository.delete(task);
        logger.info("Task deleted with ID: {}", task.getId());
    }

    public TaskResponseDTO patchTask(UUID id, @Valid TaskPatchDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
        logger.info("Task found with ID: {}", task.getId());

        User currentUser = authService.getCurrentUser();
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have access to this task");
        }

        if (dto.getTitle() != null)
            task.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            task.setDescription(dto.getDescription());
        if (dto.getDueDate() != null)
            task.setDueDate(dto.getDueDate());
        if (dto.getPriority() != null)
            task.setPriority(dto.getPriority());

        task.setUpdatedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        logger.info("Task updated with ID: {}", updatedTask.getId());

        return taskMapper.toDTO(updatedTask);
    }
}