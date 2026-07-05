package com.example.taskmanager.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.auth.dto.AuthResponse;
import com.example.taskmanager.auth.dto.LoginRequest;
import com.example.taskmanager.auth.dto.RegisterRequest;
import com.example.taskmanager.auth.service.AuthService;
import com.example.taskmanager.common.dto.ApiResponse;
import com.example.taskmanager.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// REST entry points for the auth flows implemented in AuthService: register,
// login, refresh, and reading the currently authenticated user.
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthService authService;

    // Public — listed in SecurityConfig's PUBLIC_URLS ("/api/auth/**").
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)   // 201
        .body(ApiResponse.builder()
            .success(true)
            .message("User registered successfully")
            .build()
        );
    }

    // Public — credential check happens inside AuthService via AuthenticationManager.
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Public — the refresh token itself (not a session) is the proof of identity here.
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestHeader("Authorization") String authHeader) {
        // Guard against a missing/malformed header before slicing it, otherwise
        // a bad request throws StringIndexOutOfBoundsException instead of a 401.
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String refreshToken = authHeader.substring(BEARER_PREFIX.length());
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    // Authenticated — JwtAuthFilter must have already populated the SecurityContext
    // for @AuthenticationPrincipal to resolve to the logged-in User.
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok((User) userDetails);
    }
}