-- Migration: Drop unused active_calories column from skate_sessions
-- Reason: The app exclusively uses the 'calories' column for health integration data.

ALTER TABLE skate_sessions DROP COLUMN IF EXISTS active_calories;
