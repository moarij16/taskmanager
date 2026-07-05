package com.example.taskmanager.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskmanager.auth.dto.AuthResponse;
import com.example.taskmanager.auth.dto.LoginRequest;
import com.example.taskmanager.auth.dto.RegisterRequest;
import com.example.taskmanager.common.JwtUtil;
import com.example.taskmanager.user.model.Role;
import com.example.taskmanager.user.model.User;
import com.example.taskmanager.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// Orchestrates the auth flows behind AuthController: registering a new user,
// authenticating login credentials, and rotating access tokens off a refresh
// token. Each flow ends the same way — issue a fresh access/refresh token pair
// wrapped in an AuthResponse — since the client only ever needs those tokens back.
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Creates a new user with a hashed password and default USER role,
    // then immediately logs them in by issuing a token pair.
    public AuthResponse register(RegisterRequest request) {
        // Check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Build user
        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(jwtExpiration)
            .email(user.getEmail())
            .role(user.getRole().name())
            .build();
    }

    // Delegates credential checking to Spring Security's AuthenticationManager
    // (which uses the userDetailsService + passwordEncoder beans from SecurityConfig),
    // then issues a fresh token pair once authentication succeeds.
    public AuthResponse login(LoginRequest request) {
        // Spring Security validates credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        // If we reach here — credentials valid
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(jwtExpiration)
            .email(user.getEmail())
            .role(user.getRole().name())
            .build();
    }

    // Exchanges a still-valid refresh token for a new access token, without
    // requiring the user to log in again. The refresh token itself is reused
    // as-is rather than rotated.
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtUtil.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);

        return AuthResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(refreshToken) // keep same refresh token
            .expiresIn(jwtExpiration)
            .email(user.getEmail())
            .role(user.getRole().name())
            .build();
    }
}