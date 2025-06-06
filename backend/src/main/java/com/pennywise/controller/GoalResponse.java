package com.pennywise.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents the JSON that we send back when a user fetches or creates/updates
 * a goal.
 * Example JSON:
 * {
 * "id": "550e8400-e29b-41d4-a716-446655440000",
 * "targetAmount": 1000.00,
 * "categoryFocus": "Travel",
 * "startDate": "2025-06-01T00:00:00Z",
 * "endDate": "2025-12-01T00:00:00Z"
 * }
 */
public class GoalResponse {
    private UUID id;
    private BigDecimal targetAmount;
    private String categoryFocus;
    private Instant startDate;
    private Instant endDate;

    public GoalResponse() {
    }

    public GoalResponse(UUID id,
            BigDecimal targetAmount,
            String categoryFocus,
            Instant startDate,
            Instant endDate) {
        this.id = id;
        this.targetAmount = targetAmount;
        this.categoryFocus = categoryFocus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getCategoryFocus() {
        return categoryFocus;
    }

    public void setCategoryFocus(String categoryFocus) {
        this.categoryFocus = categoryFocus;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
