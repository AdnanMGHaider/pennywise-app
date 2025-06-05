package com.pennywise.repository;

import com.pennywise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for User entities.
 * By extending JpaRepository<User, UUID>, we inherit:
 * • findAll(), findById(UUID), save(User), delete(User), etc.
 * 
 * The custom method signature Optional<User> findByEmail(String email)
 * tells Spring Data to generate a query like:
 * SELECT * FROM users WHERE email = :email
 */

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find a User by their unique email address.
     * Returns an Optional containing the User if found, or empty if not.
     *
     * At runtime, Spring Data parses this method name and automatically
     * creates the SQL: SELECT * FROM users WHERE email = ?-;
     */

    Optional<User> findByEmail(String email);
}
