-- ==========================================================
-- REVIEWER APPLICATION SYSTEM
-- Table to store reviewer applications pending admin approval
-- ==========================================================

USE cmsdb;

CREATE TABLE IF NOT EXISTS reviewer_application (
    application_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    affiliation VARCHAR(255),
    country VARCHAR(100),
    orcid VARCHAR(50),
    expertise_area VARCHAR(255) NOT NULL,
    max_papers INT DEFAULT 5,
    bio TEXT,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    reviewed_by BIGINT,
    rejection_reason TEXT,
    FOREIGN KEY (reviewed_by) REFERENCES user(user_id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_email (email),
    INDEX idx_submitted (submitted_at)
) ENGINE=InnoDB;
