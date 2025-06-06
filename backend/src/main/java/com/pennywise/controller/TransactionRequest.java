package com.pennywise.controller;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Incoming JSON body when creating a new transaction.
 * Example:
 * {
 * "date": "2025-06-05T20:00:00Z",
 * "amount": 12.34,
 * "category": "Food",
 * "merchant": "Coffee Shop",
 * "lat": 43.65, // optional
 * "lng": -79.38, // optional
 * "description": "Latte and snack"
 * }
 */
public class TransactionRequest {
    private Instant date;
    private BigDecimal amount;
    private String category;
    private String merchant;
    private Double lat; // nullable
    private Double lng; // nullable
    private String description; // nullable

    public TransactionRequest() {
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
