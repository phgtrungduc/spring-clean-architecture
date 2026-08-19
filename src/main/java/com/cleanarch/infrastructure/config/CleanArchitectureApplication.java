package com.cleanarch.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * LAYER 3: INFRASTRUCTURE - Configuration
 * 
 * Spring Boot Application - Entry point
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.cleanarch")
@EnableJpaRepositories(basePackages = "com.cleanarch.infrastructure.persistence.repository")
@EntityScan(basePackages = "com.cleanarch.infrastructure.persistence.entity")
public class CleanArchitectureApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleanArchitectureApplication.class, args);
    }
}
