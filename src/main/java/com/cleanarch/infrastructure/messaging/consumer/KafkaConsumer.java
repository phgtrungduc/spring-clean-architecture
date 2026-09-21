package com.cleanarch.infrastructure.messaging.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * LAYER 3: INFRASTRUCTURE - Kafka Consumer
 * 
 * Generic Kafka message consumer that listens to configured topics.
 * This is a sample consumer - customize based on your business needs.
 * 
 * Features:
 * - Listens to multiple topics
 * - Manual acknowledgment for better control
 * - Error handling and logging
 * - JSON deserialization
 * 
 * To customize:
 * 1. Change the topics in @KafkaListener annotation
 * 2. Modify the message processing logic
 * 3. Add business logic or call use cases
 */
@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * Listen to user-events topic
     * 
     * Configuration:
     * - topics: List of topics to listen to
     * - groupId: Consumer group ID
     * - containerFactory: The listener container factory bean name
     * 
     * To enable manual acknowledgment, add parameter: Acknowledgment ack
     * Then call ack.acknowledge() after successful processing
     */
    @KafkaListener(
        topics = {"user-events", "user-created", "user-updated"},
        groupId = "${spring.kafka.consumer.group-id:clean-arch-group}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeUserEvents(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            logger.info("Consuming message from topic: {} | partition: {} | offset: {} | key: {}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key());

            // Get the message payload
            Object message = record.value();
            logger.info("Message payload: {}", message);

            // TODO: Process the message
            // Example: Call a use case, update database, send notification, etc.
            processMessage(record.topic(), record.key(), message);

            // Manually acknowledge the message after successful processing
            if (ack != null) {
                ack.acknowledge();
                logger.info("Message acknowledged successfully");
            }

        } catch (Exception e) {
            logger.error("Error processing message from topic: {} | offset: {} | error: {}",
                    record.topic(),
                    record.offset(),
                    e.getMessage(),
                    e);
            
            // TODO: Implement error handling strategy
            // Options:
            // 1. Don't acknowledge - message will be reprocessed
            // 2. Send to dead letter queue
            // 3. Log and continue (acknowledge anyway)
            
            // For now, we'll acknowledge to avoid infinite retries
            if (ack != null) {
                ack.acknowledge();
            }
        }
    }

    /**
     * Example: Listen to a different topic with different configuration
     */
    @KafkaListener(
        topics = {"order-events"},
        groupId = "order-consumer-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderEvents(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            logger.info("Consuming order event | topic: {} | key: {} | offset: {}",
                    record.topic(),
                    record.key(),
                    record.offset());

            Object message = record.value();
            logger.info("Order message: {}", message);

            // Process order-specific logic
            processOrderMessage(message);

            if (ack != null) {
                ack.acknowledge();
            }

        } catch (Exception e) {
            logger.error("Error processing order event: {}", e.getMessage(), e);
            if (ack != null) {
                ack.acknowledge();
            }
        }
    }

    /**
     * Process the consumed message
     * Override or customize this method based on your business logic
     */
    private void processMessage(String topic, String key, Object message) {
        logger.info("Processing message from topic: {}", topic);
        
        // TODO: Implement your business logic here
        // Examples:
        // 1. Parse message to domain object
        // 2. Call use case
        // 3. Update database
        // 4. Send notification
        // 5. Trigger workflow
        
        switch (topic) {
            case "user-created":
                handleUserCreated(message);
                break;
            case "user-updated":
                handleUserUpdated(message);
                break;
            case "user-events":
                handleGenericUserEvent(message);
                break;
            default:
                logger.warn("Unknown topic: {}", topic);
        }
    }

    /**
     * Process order messages
     */
    private void processOrderMessage(Object message) {
        logger.info("Processing order message: {}", message);
        // TODO: Implement order processing logic
    }

    /**
     * Handle user created event
     */
    private void handleUserCreated(Object message) {
        logger.info("Handling user created event: {}", message);
        // TODO: Implement logic for user created event
        // Example: Send welcome email, create user profile, etc.
    }

    /**
     * Handle user updated event
     */
    private void handleUserUpdated(Object message) {
        logger.info("Handling user updated event: {}", message);
        // TODO: Implement logic for user updated event
        // Example: Update cache, sync with other services, etc.
    }

    /**
     * Handle generic user event
     */
    private void handleGenericUserEvent(Object message) {
        logger.info("Handling generic user event: {}", message);
        // TODO: Implement generic user event logic
    }
}
