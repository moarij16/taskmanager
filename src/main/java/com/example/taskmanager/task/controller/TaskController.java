package com.example.taskmanager.task.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.task.dto.TaskRequest;
import com.example.taskmanager.task.model.Task;
import com.example.taskmanager.task.service.TaskService;
import com.example.taskmanager.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// REST entry points for task management. Not listed in SecurityConfig's
// PUBLIC_URLS, so every route here requires a valid access token — JwtAuthFilter
// populates @AuthenticationPrincipal with the calling User.
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Example endpoint: creates a task scoped to whoever is authenticated.
    // Returns the Task entity directly for now (same pattern AuthController.getCurrentUser
    // uses) — swap for a TaskResponse DTO once that's built, to avoid exposing the entity.
    @PostMapping
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal User owner) {
        Task task = taskService.createTask(request, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
}
