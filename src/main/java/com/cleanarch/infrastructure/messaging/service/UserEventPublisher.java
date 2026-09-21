package com.cleanarch.infrastructure.messaging.service;

import com.cleanarch.infrastructure.messaging.event.UserCreatedEvent;
import com.cleanarch.infrastructure.messaging.event.UserUpdatedEvent;
import com.cleanarch.infrastructure.messaging.publisher.KafkaPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * LAYER 3: INFRASTRUCTURE - User Event Publisher Service
 * 
 * Service that publishes user-related events to Kafka.
 * This demonstrates how to use the KafkaPublisher in your application.
 * 
 * Usage:
 * - Inject this service in your controllers or use cases
 * - Call the appropriate publish method when events occur
 * 
 * Example integration with CreateUserUseCase:
 * - After successfully creating a user, call publishUserCreated()
 * - The event will be published to Kafka for other services to consume
 */
@Service
public class UserEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);

    private static final String USER_CREATED_TOPIC = "user-created";
    private static final String USER_UPDATED_TOPIC = "user-updated";
    private static final String USER_EVENTS_TOPIC = "user-events";

    private final KafkaPublisher kafkaPublisher;

    public UserEventPublisher(KafkaPublisher kafkaPublisher) {
        this.kafkaPublisher = kafkaPublisher;
    }

    /**
     * Publish user created event to Kafka
     * 
     * @param userId    The user ID
     * @param email     The user email
     * @param fullName  The user full name
     * @param createdAt The creation timestamp
     * @param active    Whether the user is active
     */
    public void publishUserCreated(String userId, String email, String fullName, LocalDateTime createdAt, boolean active) {
        try {
            UserCreatedEvent event = new UserCreatedEvent(userId, email, fullName, createdAt, active);
            
            logger.info("Publishing user created event for user: {}", userId);
            
            // Publish to specific topic
            kafkaPublisher.publish(USER_CREATED_TOPIC, userId, event);
            
            // Also publish to generic user events topic
            kafkaPublisher.publish(USER_EVENTS_TOPIC, userId, event);
            
        } catch (Exception e) {
            logger.error("Failed to publish user created event for user: {} | error: {}", 
                    userId, e.getMessage(), e);
            // Don't throw exception - event publishing should not fail the main operation
        }
    }

    /**
     * Publish user updated event to Kafka
     * 
     * @param userId   The user ID
     * @param email    The user email
     * @param fullName The user full name
     * @param active   Whether the user is active
     */
    public void publishUserUpdated(String userId, String email, String fullName, boolean active) {
        try {
            UserUpdatedEvent event = new UserUpdatedEvent(userId, email, fullName, active);
            
            logger.info("Publishing user updated event for user: {}", userId);
            
            // Publish to specific topic
            kafkaPublisher.publish(USER_UPDATED_TOPIC, userId, event);
            
            // Also publish to generic user events topic
            kafkaPublisher.publish(USER_EVENTS_TOPIC, userId, event);
            
        } catch (Exception e) {
            logger.error("Failed to publish user updated event for user: {} | error: {}", 
                    userId, e.getMessage(), e);
        }
    }

    /**
     * Publish a custom event to Kafka
     * 
     * @param topic   The Kafka topic
     * @param key     The message key
     * @param message The message payload
     */
    public void publishCustomEvent(String topic, String key, Object message) {
        try {
            logger.info("Publishing custom event to topic: {} with key: {}", topic, key);
            kafkaPublisher.publish(topic, key, message);
        } catch (Exception e) {
            logger.error("Failed to publish custom event to topic: {} | error: {}", 
                    topic, e.getMessage(), e);
        }
    }
}
