package com.pennywise.controller;

import com.pennywise.model.Goal;
import com.pennywise.service.GoalService;
import com.pennywise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Exposes endpoints under /api/goals for:
 * - POST /api/goals: create or update the authenticated user’s saving goal.
 * - GET /api/goals: fetch the authenticated user’s current goal (if any).
 *
 * Both endpoints are protected by JWT, since /api/** is locked down in
 * SecurityConfig.
 */
@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;
    private final UserService userService;

    @Autowired
    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
    }

    /**
     * GET /api/goals
     * Return the user’s existing Goal (200 OK + JSON) or 404 (not found).
     *
     * @param userDetails injected by Spring from the JWT filter (username is the
     *                    email)
     */
    @GetMapping
    public ResponseEntity<GoalResponse> getGoal(
            @AuthenticationPrincipal UserDetails userDetails) {
        // 1) Convert the email (username) → UUID
        UUID userId = userService.findByEmail(userDetails.getUsername())
                .map(u -> u.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2) Ask the service for the current Goal
        Optional<Goal> existing = goalService.getGoalForUser(userId);

        if (existing.isEmpty()) {
            // 3a) If no goal exists for this user, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 3b) If it exists, wrap it in a GoalResponse and send 200 OK
        Goal g = existing.get();
        GoalResponse resp = new GoalResponse(
                g.getId(),
                g.getTargetAmount(),
                g.getCategoryFocus(),
                g.getStartDate(),
                g.getEndDate());
        return ResponseEntity.ok(resp);
    }

    /**
     * POST /api/goals
     * Create or update the user’s savings goal. If this user already has a goal,
     * we overwrite it; otherwise, we create a fresh row.
     *
     * @param userDetails injected by Spring from the JWT filter
     * @param request     JSON body mapped to GoalRequest
     * @return 201 Created (if brand-new) or 200 OK (if updated), with the saved
     *         GoalResponse JSON
     */
    @PostMapping
    public ResponseEntity<GoalResponse> createOrUpdateGoal(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody GoalRequest request) {
        // 1) Convert email → userId
        UUID userId = userService.findByEmail(userDetails.getUsername())
                .map(u -> u.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2) Delegate to the service
        Goal saved = goalService.createOrUpdateGoal(
                userId,
                request.getTargetAmount(),
                request.getCategoryFocus(),
                request.getStartDate(),
                request.getEndDate());

        // 3) Wrap saved Goal in a Response DTO
        GoalResponse resp = new GoalResponse(
                saved.getId(),
                saved.getTargetAmount(),
                saved.getCategoryFocus(),
                saved.getStartDate(),
                saved.getEndDate());

        // 4) Decide HTTP code: if this was newly created vs. updated.
        // Simpler: always return 200 OK, or optionally check if existed:
        boolean existed = goalService.getGoalForUser(userId).isPresent();
        HttpStatus status = existed ? HttpStatus.OK : HttpStatus.CREATED;

        return ResponseEntity.status(status).body(resp);
    }
}
