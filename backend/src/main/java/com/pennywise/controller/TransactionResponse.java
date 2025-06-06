package com.pennywise.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Outgoing JSON for a transaction record.
 */
public class TransactionResponse {
    private UUID id;
    private Instant date;
    private BigDecimal amount;
    private String category;
    private String merchant;
    private Double lat;
    private Double lng;
    private String description;

    public TransactionResponse() {
    }

    public TransactionResponse(UUID id, Instant date, BigDecimal amount,
            String category, String merchant,
            Double lat, Double lng, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.merchant = merchant;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
