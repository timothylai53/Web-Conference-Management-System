-- ==========================================================
-- INSERT TEST USERS WITH DIFFERENT ROLES
-- Run this to create test accounts for login
-- ==========================================================

USE cmsdb;

-- Insert test users (if they don't exist already)
INSERT IGNORE INTO user (email, password_hash, first_name, last_name, affiliation, country, category, is_email_verified)
VALUES
    -- Admin user
    ('admin@scholarsync.com', 'admin123', 'Admin', 'User', 'ScholarSync', 'Malaysia', 'staff', TRUE),
    
    -- Regular users
    ('user@scholarsync.com', 'user123', 'Regular', 'User', 'University', 'Malaysia', 'student', TRUE),
    
    -- Author user
    ('author@scholarsync.com', 'author123', 'John', 'Author', 'Research Institute', 'Malaysia', 'academic', TRUE),
    
    -- Reviewer user
    ('reviewer@scholarsync.com', 'reviewer123', 'Jane', 'Reviewer', 'University', 'Malaysia', 'academic', TRUE),
    
    -- Chair user
    ('chair@scholarsync.com', 'chair123', 'Dr. Robert', 'Chair', 'Conference Board', 'Malaysia', 'staff', TRUE);

-- Assign roles to test users
-- Get user IDs and assign appropriate roles
INSERT IGNORE INTO user_role (user_id, role_id)
SELECT u.user_id, r.role_id
FROM user u, role r
WHERE (u.email = 'admin@scholarsync.com' AND r.role_name = 'Admin')
   OR (u.email = 'author@scholarsync.com' AND r.role_name = 'Author')
   OR (u.email = 'reviewer@scholarsync.com' AND r.role_name = 'Reviewer')
   OR (u.email = 'chair@scholarsync.com' AND r.role_name = 'Chair');

-- Add reviewer@scholarsync.com to the reviewer table
-- This is needed for the review-setup page to show this reviewer
INSERT IGNORE INTO reviewer (user_id, expertise_area, max_papers)
SELECT u.user_id, 'Software Engineering, Web Development', 5
FROM user u
WHERE u.email = 'reviewer@scholarsync.com';

-- Display the inserted users with their roles
SELECT u.user_id, u.email, u.first_name, u.last_name, r.role_name, u.is_email_verified 
FROM user u 
LEFT JOIN user_role ur ON u.user_id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.role_id
WHERE u.email IN (
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
