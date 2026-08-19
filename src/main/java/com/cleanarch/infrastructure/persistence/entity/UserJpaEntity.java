package com.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * LAYER 3: INFRASTRUCTURE - Persistence
 * 
 * JPA Entity - Database mapping (NOT the domain entity!)
 * This is framework-specific and stays in the infrastructure layer
 */
@Entity
@Table(name = "USERS")
public class UserJpaEntity {
    @Id
    @Column(name = "ID", length = 50)
    private String id;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "FULL_NAME", nullable = false, length = 255)
    private String fullName;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    // No-arg constructor for JPA
    public UserJpaEntity() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
