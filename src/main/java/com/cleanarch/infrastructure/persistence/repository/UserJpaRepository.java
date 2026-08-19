package com.cleanarch.infrastructure.persistence.repository;

import com.cleanarch.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * LAYER 3: INFRASTRUCTURE - Persistence
 * 
 * Spring Data JPA Repository - Framework-specific interface
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
