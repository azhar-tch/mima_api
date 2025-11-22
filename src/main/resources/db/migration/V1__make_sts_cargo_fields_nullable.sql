-- Make cargo-related fields nullable in stsoperation table
-- These fields are optional when planning an STS operation

ALTER TABLE stsoperation
MODIFY COLUMN cargo_type VARCHAR(200) NULL,
MODIFY COLUMN quantity_transferred DOUBLE NULL,
MODIFY COLUMN unit VARCHAR(20) NULL;
