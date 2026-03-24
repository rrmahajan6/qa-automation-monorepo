/**
 * Sample SQL Queries for Test Data Setup
 * Execute these during global setup
 */

-- Create test tables (if not exist)
CREATE TABLE IF NOT EXISTS test_users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'user',
    created_by VARCHAR(50) DEFAULT 'automation',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS test_orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES test_users(id),
    total_amount DECIMAL(10, 2),
    status VARCHAR(50) DEFAULT 'pending',
    created_by VARCHAR(50) DEFAULT 'automation',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS test_sessions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES test_users(id),
    token VARCHAR(500),
    expires_at TIMESTAMP,
    created_by VARCHAR(50) DEFAULT 'automation',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert seed data
INSERT INTO test_users (username, email, password, role, created_by)
VALUES 
    ('testuser', 'testuser@example.com', 'hashed_password_here', 'user', 'automation'),
    ('admin', 'admin@example.com', 'hashed_password_here', 'admin', 'automation')
ON CONFLICT (email) DO NOTHING;
