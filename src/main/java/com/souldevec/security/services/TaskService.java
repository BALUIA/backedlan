package com.souldevec.security.services;

import com.souldevec.security.dtos.CreateTaskDto;
import com.souldevec.security.dtos.TaskResponseDto;
import com.souldevec.security.entities.Task;
import com.souldevec.security.entities.User;
import com.souldevec.security.enums.TaskStatus;
import com.souldevec.security.repositories.TaskRepository;
import com.souldevec.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskResponseDto createTask(CreateTaskDto createTaskDto, String creatorUsername) {
        User creadoPor = userRepository.findByUserName(creatorUsername)
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));

        User asignadoA = userRepository.findById(createTaskDto.getAssignedToUserId())
                .orElseThrow(() -> new RuntimeException("Usuario asignado no encontrado"));

        Task task = new Task();
        task.setDescription(createTaskDto.getDescription());
        task.setStatus(TaskStatus.PENDIENTE);
        task.setFechaCreacion(LocalDateTime.now());
        task.setCreadoPor(creadoPor);
        task.setAsignadoA(asignadoA);

        Task savedTask = taskRepository.save(task);
        return mapToTaskResponseDto(savedTask);
    }

    public TaskResponseDto markTaskAsCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        task.setStatus(TaskStatus.REALIZADO);
        task.setFechaRealizacion(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return mapToTaskResponseDto(updatedTask);
    }

    public List<TaskResponseDto> getTasksForUser(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return taskRepository.findByAsignadoAOrderByFechaCreacionDesc(user).stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAllByOrderByFechaCreacionDesc().stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    private TaskResponseDto mapToTaskResponseDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setFechaCreacion(task.getFechaCreacion());
        dto.setFechaRealizacion(task.getFechaRealizacion());
        dto.setAsignadoA(task.getAsignadoA().getUserName());
        dto.setCreadoPor(task.getCreadoPor().getUserName());
        return dto;
    }
}
