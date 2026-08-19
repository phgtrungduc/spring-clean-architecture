package com.cleanarch.domain.port;

import com.cleanarch.domain.model.User;
import com.cleanarch.domain.valueobject.Email;

import java.util.Optional;

/**
 * LAYER 1: DOMAIN
 * 
 * Repository Interface (Port) - Data access contract
 * Defines what the domain needs from persistence
 * Implementation will be in Infrastructure layer
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(Email email);
    Optional<User> findById(String userId);
    boolean existsByEmail(Email email);
}
