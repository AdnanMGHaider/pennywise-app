package com.pennywise.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // Foreign key to users.id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "category_focus", nullable = false)
    private String categoryFocus;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    // JPA requires a no-args constructor
    protected Goal() {
    }

    // Convenience constructor for creating/updating a goal
    public Goal(UUID userId,
            BigDecimal targetAmount,
            String categoryFocus,
            Instant startDate,
            Instant endDate) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.targetAmount = targetAmount;
        this.categoryFocus = categoryFocus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters (no setters for id; you can add setters for other fields if you want
    // mutable updates)
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public String getCategoryFocus() {
        return categoryFocus;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    // If you want to support updating an existing Goal in-place, add setters:
    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public void setCategoryFocus(String categoryFocus) {
        this.categoryFocus = categoryFocus;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
