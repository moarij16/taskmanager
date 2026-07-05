package com.example.taskmanager.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.taskmanager.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// Loads a User by email for Spring Security's authentication process.
// Kept as its own bean (not a @Bean method inside SecurityConfig) to avoid a
// circular dependency: JwtAuthFilter needs a UserDetailsService, and
// SecurityConfig needs JwtAuthFilter injected to wire it into the filter chain.
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
