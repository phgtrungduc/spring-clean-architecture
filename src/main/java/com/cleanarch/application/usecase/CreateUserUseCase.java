package com.cleanarch.application.usecase;

import com.cleanarch.application.port.in.CreateUserCommand;
import com.cleanarch.application.port.in.CreateUserResult;
import com.cleanarch.application.port.out.UserRepositoryPort;
import com.cleanarch.domain.model.User;
import com.cleanarch.domain.valueobject.Email;

/**
 * LAYER 2: APPLICATION (Use Cases)
 * 
 * CreateUserUseCase - Application-specific business logic
 * Orchestrates the flow of data to and from domain entities
 */
public class CreateUserUseCase {
    private final UserRepositoryPort userRepository;

    public CreateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public CreateUserResult execute(CreateUserCommand command) {
        // 1. Create value objects
        Email email = new Email(command.getEmail());
        
        // 2. Business Rule: Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email " + email.getValue() + " already exists");
        }

        // 3. Create User entity (business logic is in the entity)
        User user = new User(email, command.getFullName());

        // 4. Save user via repository port
        User savedUser = userRepository.save(user);

        // 5. Return result
        return new CreateUserResult(
            savedUser.getId().getValue(),
            savedUser.getEmail().getValue(),
            savedUser.getFullName(),
            savedUser.getCreatedAt(),
            savedUser.isActive()
        );
    }
}
