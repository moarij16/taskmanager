package com.example.taskmanager.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Used during authentication: email is the login identifier (see User.getUsername()).
    Optional<User> findByEmail(String email);

    // Used during registration to check for duplicates without loading the full entity.
    boolean existsByEmail(String email);
}