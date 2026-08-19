package com.cleanarch.infrastructure.persistence.adapter;

import com.cleanarch.application.port.out.UserRepositoryPort;
import com.cleanarch.domain.model.User;
import com.cleanarch.domain.valueobject.Email;
import com.cleanarch.domain.valueobject.UserId;
import com.cleanarch.infrastructure.persistence.entity.UserJpaEntity;
import com.cleanarch.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * LAYER 3: INFRASTRUCTURE - Persistence Adapter
 * 
 * UserRepositoryAdapter - Implements UserRepositoryPort
 * Converts between Domain entities and JPA entities
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        // Convert Domain User -> JPA Entity
        UserJpaEntity jpaEntity = toJpaEntity(user);
        
        // Save to database
        UserJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        
        // Convert back: JPA Entity -> Domain User
        return toDomainUser(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
            .map(this::toDomainUser);
    }

    @Override
    public Optional<User> findById(String userId) {
        return jpaRepository.findById(userId)
            .map(this::toDomainUser);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

    // Mapper: Domain -> JPA Entity
    private UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId().getValue());
        entity.setEmail(user.getEmail().getValue());
        entity.setFullName(user.getFullName());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setActive(user.isActive());
        return entity;
    }

    // Mapper: JPA Entity -> Domain
    private User toDomainUser(UserJpaEntity entity) {
        return new User(
            new UserId(entity.getId()),
            new Email(entity.getEmail()),
            entity.getFullName(),
            entity.getCreatedAt(),
            entity.isActive()
        );
    }
}
