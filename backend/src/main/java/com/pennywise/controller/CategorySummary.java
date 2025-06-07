package com.pennywise.controller;

import java.math.BigDecimal;

/**
 * Holds one category and the total amount spent in that category.
 * Used to drive the front-end’s pie chart of spend by category.
 */
public class CategorySummary {
    private String category;
    private BigDecimal totalAmount;

    // No-args constructor for Jackson/ JPA
    public CategorySummary() {
    }

    // Constructor used by JPA @Query “new” syntax
    public CategorySummary(String category, BigDecimal totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
