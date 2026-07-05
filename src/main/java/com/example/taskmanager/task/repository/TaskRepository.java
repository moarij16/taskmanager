package com.example.taskmanager.task.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanager.task.model.Task;
import com.example.taskmanager.task.model.TaskStatus;
import com.example.taskmanager.user.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Paginated list of one user's tasks — pairs with app.page.size in application.properties.
    Page<Task> findByOwner(User owner, Pageable pageable);

    // Filter a user's tasks down to one status (e.g. show only DONE tasks).
    List<Task> findByOwnerAndStatus(User owner, TaskStatus status);

}
