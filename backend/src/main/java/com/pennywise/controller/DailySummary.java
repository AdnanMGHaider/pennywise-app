package com.pennywise.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Holds one day and the total amount spent on that day.
 * Drives the dashboard’s daily spending chart.
 */
public class DailySummary {
    private LocalDate date;
    private BigDecimal totalAmount;

    // Required by Jackson/JPA for object creation via reflection
    public DailySummary() {
    }

    // Used by our JPQL/SQL query to map each row into a DailySummary
    public DailySummary(LocalDate date, BigDecimal totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
