package com.antonio.taskmanager.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.antonio.taskmanager.dto.TaskRequestDTO;
import com.antonio.taskmanager.dto.TaskResponseDTO;
import com.antonio.taskmanager.entity.Task;

@Component
public class TaskMapper {
    public TaskResponseDTO toDTO(Task task){
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
    
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setTitle(task.getTitle());
        taskResponseDTO.setDescription(
            task.getDescription() != null ? task.getDescription() : "No description"
        );
        taskResponseDTO.setCompleted(task.isCompleted());
        taskResponseDTO.setPriority(task.getPriority());
        if (task.getDueDate() != null) {
            taskResponseDTO.setDueDate(
                task.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }
        if(task.getDueDate() != null) {
            taskResponseDTO.setCreatedAt(
                task.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            ); 
        }

        return taskResponseDTO;
    }    

    public Task toEntity(TaskRequestDTO dto){
        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority());

        return task;
    }
}
