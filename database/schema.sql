-- PostgreSQL Schema for Clean Architecture Demo

-- Drop table if exists (for development only)
-- DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE INDEX idx_users_email ON users (email);

COMMENT ON TABLE users IS 'Users table for Clean Architecture demo';
COMMENT ON COLUMN users.id IS 'Unique user identifier (UUID)';
COMMENT ON COLUMN users.email IS 'User email address (unique)';
COMMENT ON COLUMN users.full_name IS 'User full name';
COMMENT ON COLUMN users.created_at IS 'User creation timestamp';
COMMENT ON COLUMN users.active IS 'User active status';

-- Sample data (optional, for testing)
-- INSERT INTO users (id, email, full_name, created_at, active)
-- VALUES ('test-uuid-1', 'test@example.com', 'Test User', NOW(), TRUE);
