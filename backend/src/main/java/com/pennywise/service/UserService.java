package com.pennywise.service;

import com.pennywise.model.User;
import com.pennywise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for User-related operations.
 * Encapsulates:
 * • password hashing,
 * • user lookup by email,
 * • user registration.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a brand-new user.
     * Steps:
     * 1. Check if email already exists → throw an exception if so.
     * 2. Hash the raw password using BCrypt.
     * 3. Create a new User entity (UUID + hashed password + timestamp).
     * 4. Save via userRepository.save().
     *
     * @param email       raw email string from request
     * @param rawPassword raw password from request
     * @return the newly saved User entity
     * @throws IllegalArgumentException if email is already taken
     */
    public User registerUser(String email, String rawPassword) {
        // 1. Prevent duplicate registration
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + email);
        }

        // 2. Hash the raw password
        String hashed = passwordEncoder.encode(rawPassword);

        // 3. Create a new User instance (id + createdAt set in User’s constructor)
        User newUser = new User(email, hashed);

        // 4. Persist to DB
        return userRepository.save(newUser);
    }

    /**
     * Look up a user by email.
     * Useful during login to fetch the stored password hash and compare.
     *
     * @param email the email to search
     * @return an Optional<User> (empty if not found)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * (Optional) Fetch a user by their UUID.
     *
     * @param id the user’s UUID
     * @return an Optional<User> (empty if not found)
     */
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
