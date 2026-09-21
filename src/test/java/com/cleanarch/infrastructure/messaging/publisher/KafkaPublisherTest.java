package com.cleanarch.infrastructure.messaging.publisher;

import com.cleanarch.infrastructure.messaging.event.UserCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for Kafka Publisher
 * 
 * NOTE: This test requires Embedded Kafka dependency in pom.xml:
 * <dependency>
 *     <groupId>org.springframework.kafka</groupId>
 *     <artifactId>spring-kafka-test</artifactId>
 *     <scope>test</scope>
 * </dependency>
 * 
 * To run this test:
 * 1. Add spring-kafka-test dependency
 * 2. Run: mvn test -Dtest=KafkaPublisherTest
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class KafkaPublisherTest {

    @Autowired
    private KafkaPublisher kafkaPublisher;

    @Test
    void shouldPublishMessageToKafka() throws Exception {
        // Given
        String topic = "test-topic";
        String key = UUID.randomUUID().toString();
        UserCreatedEvent event = new UserCreatedEvent(
            UUID.randomUUID().toString(),
            "test@example.com",
            "Test User",
            LocalDateTime.now(),
            true
        );

        // When
        var future = kafkaPublisher.publish(topic, key, event);

        // Then
        var result = future.get(); // Wait for completion
        assertThat(result).isNotNull();
        assertThat(result.getRecordMetadata()).isNotNull();
        assertThat(result.getRecordMetadata().topic()).isEqualTo(topic);
    }

    @Test
    void shouldPublishMessageWithoutKey() throws Exception {
        // Given
        String topic = "test-topic-no-key";
        String message = "Hello Kafka!";

        // When
        var future = kafkaPublisher.publish(topic, message);

        // Then
        var result = future.get();
        assertThat(result).isNotNull();
        assertThat(result.getRecordMetadata().topic()).isEqualTo(topic);
    }
}
