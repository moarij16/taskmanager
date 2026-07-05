package com.example.taskmanager.task.service;

import org.springframework.stereotype.Service;

import com.example.taskmanager.task.dto.TaskRequest;
import com.example.taskmanager.task.model.Task;
import com.example.taskmanager.task.model.TaskStatus;
import com.example.taskmanager.task.repository.TaskRepository;
import com.example.taskmanager.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    // Example: builds a Task from the incoming request, scoped to whichever
    // user is creating it. New tasks always start as TODO — status isn't
    // client-settable at creation time (see TaskRequest's comment on why).
    public Task createTask(TaskRequest request, User owner) {
        Task task = Task.builder()
            .name(request.getName())
            .description(request.getDescription())
            .priority(request.getPriority())
            .dueDate(request.getDueDate())
            .status(TaskStatus.TODO)
            .owner(owner)
            .build();

        return taskRepository.save(task);
    }
}
