package com.cleanarch.infrastructure.config;

import com.cleanarch.application.port.out.UserRepositoryPort;
import com.cleanarch.application.usecase.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LAYER 3: INFRASTRUCTURE - Configuration
 * 
 * Use Case Configuration - Wiring dependencies with Spring
 * This is where we connect the layers using Dependency Injection
 */
@Configuration
public class UseCaseConfiguration {

    /**
     * Create the CreateUserUseCase bean
     */
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepositoryPort userRepository) {
        return new CreateUserUseCase(userRepository);
    }
}
