package com.pennywise.controller;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents the JSON body that a client sends when setting or updating a
 * savings goal.
 * Example JSON:
 * {
 * "targetAmount": 1000.00,
 * "categoryFocus": "Travel",
 * "startDate": "2025-06-01T00:00:00Z",
 * "endDate": "2025-12-01T00:00:00Z"
 * }
 */
public class GoalRequest {
    private BigDecimal targetAmount;
    private String categoryFocus;
    private Instant startDate;
    private Instant endDate;

    public GoalRequest() {
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
