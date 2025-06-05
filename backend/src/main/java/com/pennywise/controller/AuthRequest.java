package com.pennywise.controller;

public class AuthRequest {
    private String email;
    private String password;

    public AuthRequest() {
    } // needed for JSON deserialization

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
