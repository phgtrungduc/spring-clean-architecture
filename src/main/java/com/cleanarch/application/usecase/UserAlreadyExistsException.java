package com.cleanarch.application.usecase;

/**
 * LAYER 2: APPLICATION
 * 
 * Application-specific exception
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
