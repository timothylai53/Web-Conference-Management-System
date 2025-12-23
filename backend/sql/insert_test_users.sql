-- ==========================================================
-- INSERT TEST USERS WITH DIFFERENT ROLES
-- Run this to create test accounts for login
-- ==========================================================

USE cmsdb;

-- Insert test users (if they don't exist already)
INSERT IGNORE INTO user (email, password_hash, first_name, last_name, affiliation, country, category, role, is_email_verified)
VALUES
    -- Admin user
    ('admin@scholarsync.com', 'admin123', 'Admin', 'User', 'ScholarSync', 'Malaysia', 'staff', 'Admin', TRUE),
    
    -- Regular users
    ('user@scholarsync.com', 'user123', 'Regular', 'User', 'University', 'Malaysia', 'student', 'User', TRUE),
    
    -- Author user
    ('author@scholarsync.com', 'author123', 'John', 'Author', 'Research Institute', 'Malaysia', 'academic', 'Author', TRUE),
    
    -- Reviewer user
    ('reviewer@scholarsync.com', 'reviewer123', 'Jane', 'Reviewer', 'University', 'Malaysia', 'academic', 'Reviewer', TRUE),
    
    -- Chair user
    ('chair@scholarsync.com', 'chair123', 'Dr. Robert', 'Chair', 'Conference Board', 'Malaysia', 'staff', 'Chair', TRUE);

-- Display the inserted users
SELECT user_id, email, first_name, last_name, role, is_email_verified 
FROM user 
WHERE email IN (
    'admin@scholarsync.com', 
    'user@scholarsync.com', 
    'author@scholarsync.com', 
    'reviewer@scholarsync.com', 
    'chair@scholarsync.com'
);

-- ==========================================================
-- TEST CREDENTIALS:
-- 
-- Admin Account:
--   Email: admin@scholarsync.com
--   Password: admin123
-- 
-- Regular User:
--   Email: user@scholarsync.com
--   Password: user123
-- 
-- Author:
--   Email: author@scholarsync.com
--   Password: author123
-- 
-- Reviewer:
--   Email: reviewer@scholarsync.com
--   Password: reviewer123
-- 
-- Chair:
--   Email: chair@scholarsync.com
--   Password: chair123
-- ==========================================================
