package com.cleanarch.infrastructure.web.dto;

/**
 * LAYER 3: INFRASTRUCTURE
 * 
 * User Response DTO - Data structure for API response
 */
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String createdAt;
    private String status;

    public UserResponse() {
    }

    public UserResponse(String id, String email, String fullName, String createdAt, String status) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
        this.status = status;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
