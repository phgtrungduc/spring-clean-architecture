package com.cleanarch.infrastructure.web.dto;

/**
 * LAYER 3: INFRASTRUCTURE
 * 
 * Web Request DTO - Specific to REST API
 */
public class CreateUserRequest {
    private String email;
    private String fullName;

    // No-arg constructor for Jackson
    public CreateUserRequest() {
    }

    public CreateUserRequest(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
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
}
