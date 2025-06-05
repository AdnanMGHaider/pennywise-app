package com.pennywise.security;

import com.pennywise.model.User;
import com.pennywise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implements Spring Security’s UserDetailsService by delegating to UserService.
 * When Spring Security needs to authenticate a username (email), it calls
 * loadUserByUsername().
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Load a UserDetails by email (our “username”). If not found, throw
     * UsernameNotFoundException.
     * We wrap our User entity in Spring’s UserDetails for authentication checks.
     */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));

        // Spring’s User object: fields are (username, password, grantedAuthorities)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(Collections.emptyList()) // no roles/authorities yet
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}