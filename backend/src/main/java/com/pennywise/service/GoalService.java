package com.pennywise.service;

import com.pennywise.model.Goal;
import com.pennywise.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for all “savings goal” business logic.
 * 
 * Responsibilities:
 * 1. Determine whether to create a new Goal row or update an existing one.
 * 2. Fetch a user’s current goal (if any).
 * 3. Delegate actual data‐persistence to GoalRepository.
 */
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserService userService;
    // (we’ll use UserService if, in a future step, you want to resolve email→UUID
    // here,
    // but for now, controller will pass in the UUID directly.)

    @Autowired
    public GoalService(GoalRepository goalRepository, UserService userService) {
        this.goalRepository = goalRepository;
        this.userService = userService;
    }

    /**
     * Create a new Goal row for this user or update the existing one.
     *
     * @param userId        UUID of the currently authenticated user
     * @param targetAmount  How much they want to save (e.g. 1000.00)
     * @param categoryFocus The category this goal is focused on (e.g. "Travel")
     * @param startDate     When the goal begins (Instant, e.g. now())
     * @param endDate       When the goal ends (Instant)
     * @return the saved Goal entity (new or updated)
     */
    public Goal createOrUpdateGoal(
            UUID userId,
            BigDecimal targetAmount,
            String categoryFocus,
            Instant startDate,
            Instant endDate) {
        // 1) Look up if a Goal already exists for this userId
        Optional<Goal> existingGoalOpt = goalRepository.findByUserId(userId);

        if (existingGoalOpt.isPresent()) {
            // 2a) If already present, update its fields in-place and save
            Goal existing = existingGoalOpt.get();
            existing.setTargetAmount(targetAmount);
            existing.setCategoryFocus(categoryFocus);
            existing.setStartDate(startDate);
            existing.setEndDate(endDate);

            return goalRepository.save(existing);
            // save(...) does an UPDATE since the entity already has an ID
        } else {
            // 2b) If no goal exists yet, create a brand-new Goal entity
            Goal newGoal = new Goal(userId, targetAmount, categoryFocus, startDate, endDate);
            return goalRepository.save(newGoal);
            // save(...) does an INSERT into the goals table
        }
    }

    /**
     * Fetch the current user’s Goal, if any.
     *
     * @param userId UUID of the authenticated user
     * @return Optional<Goal> (empty if user has not created a goal yet)
     */
    public Optional<Goal> getGoalForUser(UUID userId) {
        return goalRepository.findByUserId(userId);
    }
}
