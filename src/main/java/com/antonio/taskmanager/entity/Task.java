package com.antonio.taskmanager.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.antonio.taskmanager.dto.TaskResponseDTO;

import jakarta.annotation.Priority;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Task { 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = true, length = 500)
    private String description;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = true)
    private LocalDate dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    public TaskResponseDTO toDTO(Task task){
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
    
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(task.getTitle());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setCreatedAt(task.getCreatedAt().toString());
        taskResponseDTO.setCompleted(task.isCompleted());
        taskResponseDTO.setDueDate(task.getDueDate().toString());
        taskResponseDTO.setPriority(task.getPriority());

        return taskResponseDTO;
    }
}
