package com.souldevec.security.controllers;

import com.souldevec.security.dtos.CreateTaskDto;
import com.souldevec.security.dtos.TaskResponseDto;
import com.souldevec.security.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody CreateTaskDto createTaskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication.getName();
        return ResponseEntity.ok(taskService.createTask(createTaskDto, creatorUsername));
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponseDto>> getMyTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.getTasksForUser(username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDto> markTaskAsCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markTaskAsCompleted(id));
    }
}
