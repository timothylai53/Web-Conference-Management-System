-- ==========================================================
-- MIGRATION: Add conference_id column to paper table (IF NOT EXISTS)
-- This file is safe to run multiple times
-- ==========================================================

USE cmsdb;

-- Check if conference_id column exists before adding it
SET @column_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = 'cmsdb'
    AND TABLE_NAME = 'paper'
    AND COLUMN_NAME = 'conference_id'
);

-- Only add conference_id if it doesn't exist
SET @sql = IF(@column_exists = 0,
    'ALTER TABLE paper ADD COLUMN conference_id BIGINT AFTER track_id',
    'SELECT "Column conference_id already exists" AS status'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Add foreign key constraint only if needed
SET @fk_exists = (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = 'cmsdb'
    AND TABLE_NAME = 'paper'
    AND CONSTRAINT_NAME = 'fk_paper_conference'
);

SET @sql_fk = IF(@fk_exists = 0 AND @column_exists = 0,
    'ALTER TABLE paper ADD CONSTRAINT fk_paper_conference FOREIGN KEY (conference_id) REFERENCES conference(conference_id)',
    'SELECT "Foreign key already exists or column was not added" AS status'
);

PREPARE stmt FROM @sql_fk;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT 'Migration check completed' AS status;

