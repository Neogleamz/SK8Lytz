-- Migration: Normalize MAC Addresses and Deduplicate
-- This script safely uppercases all registered device MACs in the database,
-- and drops duplicates caused by the previous case-sensitive race condition.

-- 1. Create a temporary table to hold the deduplicated data
CREATE TEMP TABLE deduped_devices AS
SELECT DISTINCT ON (user_id, UPPER(device_mac)) *
FROM registered_devices
ORDER BY user_id, UPPER(device_mac), updated_at DESC;

-- 2. Delete ALL existing records to clear the slate of case-mismatches
DELETE FROM registered_devices;

-- 3. Re-insert the deduplicated records with normalized UPPERCASE MACs
INSERT INTO registered_devices
SELECT * FROM deduped_devices;

-- 4. Update the actual rows to ensure device_mac is strictly uppercase
UPDATE registered_devices
SET device_mac = UPPER(device_mac);

-- 5. Drop the old constraint which was case sensitive
ALTER TABLE registered_devices DROP CONSTRAINT IF EXISTS unique_user_device_mac;

-- 6. Add a case-insensitive unique constraint to prevent this forever
CREATE UNIQUE INDEX unique_user_device_mac_idx ON registered_devices (user_id, UPPER(device_mac));
