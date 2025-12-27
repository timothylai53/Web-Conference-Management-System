-- ==========================================================
-- Database Initialization Master Script
-- This file runs automatically when Docker container starts
-- Scripts are executed in alphabetical order
-- ==========================================================

-- This file ensures the database exists
-- The actual schema and data are in separate files:
-- 1. cms_create.sql - Creates all tables
-- 2. cms_data.sql - Inserts sample data
-- 3. insert_test_users.sql - Additional test users
-- 4. add_conference_id_to_paper.sql - Schema updates

-- Note: MySQL in Docker runs all .sql files in /docker-entrypoint-initdb.d
-- in alphabetical order when the container is first created
