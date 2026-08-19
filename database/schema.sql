-- Oracle Database Schema for Clean Architecture Demo

-- Drop table if exists (for development only)
-- DROP TABLE USERS CASCADE CONSTRAINTS;

-- Create USERS table
CREATE TABLE USERS (
    ID VARCHAR2(50) PRIMARY KEY,
    EMAIL VARCHAR2(255) NOT NULL UNIQUE,
    FULL_NAME VARCHAR2(255) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL,
    ACTIVE NUMBER(1) NOT NULL CHECK (ACTIVE IN (0, 1))
);

-- Create index on email for faster lookup
CREATE INDEX idx_users_email ON USERS(EMAIL);

-- Comments for documentation
COMMENT ON TABLE USERS IS 'Users table for Clean Architecture demo';
COMMENT ON COLUMN USERS.ID IS 'Unique user identifier (UUID)';
COMMENT ON COLUMN USERS.EMAIL IS 'User email address (unique)';
COMMENT ON COLUMN USERS.FULL_NAME IS 'User full name';
COMMENT ON COLUMN USERS.CREATED_AT IS 'User creation timestamp';
COMMENT ON COLUMN USERS.ACTIVE IS 'User active status (1=active, 0=inactive)';

-- Sample data (optional, for testing)
-- INSERT INTO USERS (ID, EMAIL, FULL_NAME, CREATED_AT, ACTIVE) 
-- VALUES ('test-uuid-1', 'test@example.com', 'Test User', SYSTIMESTAMP, 1);

COMMIT;
