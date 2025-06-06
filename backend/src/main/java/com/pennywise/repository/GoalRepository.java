package com.pennywise.repository;

import com.pennywise.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the Goal entity.
 * Extends JpaRepository<Goal, UUID> so we automatically get:
 * - save(…), findById(…), findAll(…), delete(…) etc.
 *
 * We add one custom finder: findByUserId(UUID).
 * That lets us ask “give me the Goal row for this particular user,” which
 * is exactly what we need when a user wants to see or update their savings
 * goal.
 */
public interface GoalRepository extends JpaRepository<Goal, UUID> {

    /**
     * Look up a Goal record by the user’s UUID.
     * Returns Optional.empty() if that user has not set a goal yet.
     */
    Optional<Goal> findByUserId(UUID userId);
}
