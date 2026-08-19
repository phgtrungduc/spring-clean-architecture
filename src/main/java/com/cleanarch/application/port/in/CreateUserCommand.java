package com.cleanarch.application.port.in;

/**
 * LAYER 2: APPLICATION
 * 
 * Command - Input data structure for CreateUser use case
 */
public class CreateUserCommand {
    private final String email;
    private final String fullName;

    public CreateUserCommand(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
