package com.cleanarch.domain.model;

import com.cleanarch.domain.valueobject.Email;
import com.cleanarch.domain.valueobject.UserId;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * LAYER 1: DOMAIN (Core business logic)
 * 
 * User Entity - Pure business object, no framework dependencies
 * Contains core business logic and rules
 */
public class User {
    private final UserId id;
    private final Email email;
    private final String fullName;
    private final LocalDateTime createdAt;
    private boolean active;

    // Constructor for new user
    public User(Email email, String fullName) {
        this.id = new UserId(UUID.randomUUID().toString());
        this.email = email;
        this.fullName = validateFullName(fullName);
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    // Constructor for existing user (from database)
    public User(UserId id, Email email, String fullName, LocalDateTime createdAt, boolean active) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.active = active;
    }

    // Business Rule: Full name must not be empty and must have at least 2 characters
    private String validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (fullName.trim().length() < 2) {
            throw new IllegalArgumentException("Full name must have at least 2 characters");
        }
        return fullName.trim();
    }

    // Business Rule: Deactivate user
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("User is already inactive");
        }
        this.active = false;
    }

    // Business Rule: Activate user
    public void activate() {
        if (this.active) {
            throw new IllegalStateException("User is already active");
        }
        this.active = true;
    }

    // Getters
    public UserId getId() {
        return id;
    }

    public Email getEmail() {
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
