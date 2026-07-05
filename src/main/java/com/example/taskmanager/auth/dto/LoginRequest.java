package com.example.taskmanager.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Request body for the login endpoint (e.g. POST /api/auth/login).
// No @Email format check here — login only needs to know the field isn't
// empty; an unrecognized email fails authentication anyway, not validation.
@Data
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
