package com.example.taskmanager.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Request body for the registration endpoint (e.g. POST /api/auth/register).
// Validated via @Valid in the controller — constraint violations short-circuit
// into a 400 response before any service/repository code runs.
//
// @NoArgsConstructor is required here: @Builder generates its own internal
// all-args constructor, which stops @Data from auto-generating a no-args one.
// Jackson needs that no-args constructor to deserialize the request body.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    private String password;
}
