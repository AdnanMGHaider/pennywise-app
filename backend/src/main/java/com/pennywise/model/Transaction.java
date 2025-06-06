package com.pennywise.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    // Store the user’s UUID (foreign key to users.id)
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String merchant;

    // Optional latitude and longitude (nullable for now)
    @Column
    private Double lat;

    @Column
    private Double lng;

    @Column(length = 512)
    private String description;

    // JPA requires a no-args constructor
    protected Transaction() {
    }

    // Convenience constructor
    public Transaction(UUID userId, Instant date, BigDecimal amount, String category,
            String merchant, Double lat, Double lng, String description) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.merchant = merchant;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    // Getters (no setters for immutability except where needed)
    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getMerchant() {
        return merchant;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
    }
}