package com.cleanarch.infrastructure.messaging.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * LAYER 3: INFRASTRUCTURE - Kafka Publisher
 * 
 * Generic Kafka message publisher for sending messages to Kafka topics.
 * This adapter handles the technical details of publishing to Kafka.
 * 
 * Usage:
 * - Inject this component in your use cases or controllers
 * - Call publish(topic, key, message) to send messages
 * - Messages are automatically serialized to JSON
 */
@Component
public class KafkaPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publish a message to a Kafka topic asynchronously
     * 
     * @param topic   The Kafka topic name
     * @param key     The message key (can be null)
     * @param message The message payload
     * @return CompletableFuture with the result
     */
    public CompletableFuture<SendResult<String, Object>> publish(String topic, String key, Object message) {
        logger.info("Publishing message to topic: {} with key: {}", topic, key);
        
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Message published successfully to topic: {} | partition: {} | offset: {}",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to publish message to topic: {} | error: {}", topic, ex.getMessage(), ex);
            }
        });
        
        return future;
    }

    /**
     * Publish a message to a Kafka topic (without key)
     * 
     * @param topic   The Kafka topic name
     * @param message The message payload
     * @return CompletableFuture with the result
     */
    public CompletableFuture<SendResult<String, Object>> publish(String topic, Object message) {
        return publish(topic, null, message);
    }

    /**
     * Publish a message to a Kafka topic and wait for completion (blocking)
     * Use this carefully as it blocks the thread
     * 
     * @param topic   The Kafka topic name
     * @param key     The message key (can be null)
     * @param message The message payload
     */
    public void publishSync(String topic, String key, Object message) {
        try {
            logger.info("Publishing message synchronously to topic: {} with key: {}", topic, key);
            SendResult<String, Object> result = kafkaTemplate.send(topic, key, message).get();
            logger.info("Message published successfully to topic: {} | partition: {} | offset: {}",
                    topic,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        } catch (Exception e) {
            logger.error("Failed to publish message synchronously to topic: {} | error: {}", topic, e.getMessage(), e);
            throw new RuntimeException("Failed to publish message to Kafka", e);
        }
    }
}
