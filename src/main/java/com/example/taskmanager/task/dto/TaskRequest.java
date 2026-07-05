package com.example.taskmanager.task.dto;

import java.time.LocalDate;

import com.example.taskmanager.task.model.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Request body for creating/updating a task (e.g. POST /api/tasks).
// Deliberately excludes "status" and "owner" — those are server-controlled,
// not something a client should be able to set directly.
//
// @NoArgsConstructor is required alongside @Data + @Builder: @Builder generates
// its own internal all-args constructor, which stops @Data from generating a
// no-args one — and Jackson needs the no-args constructor to deserialize this.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDate dueDate;
}
