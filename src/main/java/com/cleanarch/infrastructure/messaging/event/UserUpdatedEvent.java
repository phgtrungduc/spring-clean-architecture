package com.cleanarch.infrastructure.messaging.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * LAYER 3: INFRASTRUCTURE - User Updated Event
 * 
 * Event message sent to Kafka when a user is updated.
 * This is a DTO used for messaging infrastructure.
 */
public class UserUpdatedEvent {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // Default constructor for JSON deserialization
    public UserUpdatedEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = "USER_UPDATED";
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with user data
    public UserUpdatedEvent(String userId, String email, String fullName, boolean active) {
        this();
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserUpdatedEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", active=" + active +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
