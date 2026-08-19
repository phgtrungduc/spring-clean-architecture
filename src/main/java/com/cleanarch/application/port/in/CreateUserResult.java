package com.cleanarch.application.port.in;

import java.time.LocalDateTime;

/**
 * LAYER 2: APPLICATION
 * 
 * Result - Output data structure from CreateUser use case
 */
public class CreateUserResult {
    private final String userId;
    private final String email;
    private final String fullName;
    private final LocalDateTime createdAt;
    private final boolean active;

    public CreateUserResult(String userId, String email, String fullName, 
                           LocalDateTime createdAt, boolean active) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.active = active;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }
}
