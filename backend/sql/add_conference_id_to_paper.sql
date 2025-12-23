-- ==========================================================
-- MIGRATION: Add conference_id column to paper table
-- Run this on your existing database to add conference support
-- ==========================================================

USE cmsdb;

-- Add conference_id column to paper table
ALTER TABLE paper
ADD COLUMN conference_id BIGINT AFTER track_id;

-- Add foreign key constraint
ALTER TABLE paper
ADD CONSTRAINT fk_paper_conference
FOREIGN KEY (conference_id) REFERENCES conference(conference_id);

-- Optional: Update existing papers with a default conference_id
-- UPDATE paper SET conference_id = 1 WHERE conference_id IS NULL;

SELECT 'Migration completed: conference_id column added to paper table' AS status;
