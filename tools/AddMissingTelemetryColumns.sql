-- ═══════════════════════════════════════════════════════════
-- SK8Lytz Telemetry Tables — Missing Columns Migration
-- Run this in Supabase SQL Editor:
-- https://supabase.com/dashboard/project/qefmeivpjyaukbwadgaz/sql
-- ═══════════════════════════════════════════════════════════

-- Add missing columns to parsed_session_stats
ALTER TABLE parsed_session_stats
  ADD COLUMN IF NOT EXISTS total_storage_estimate BIGINT DEFAULT 0,
  ADD COLUMN IF NOT EXISTS last_app_opened_time   BIGINT DEFAULT 0,
  ADD COLUMN IF NOT EXISTS primary_ble_mac        TEXT,
  ADD COLUMN IF NOT EXISTS mode_usage             JSONB DEFAULT '{}',
  ADD COLUMN IF NOT EXISTS color_usage            JSONB DEFAULT '{}';

-- Add missing hardware columns to parsed_session_devices
ALTER TABLE parsed_session_devices
  ADD COLUMN IF NOT EXISTS firmware_ver   JSONB,
  ADD COLUMN IF NOT EXISTS ic_type        TEXT,
  ADD COLUMN IF NOT EXISTS led_points     INTEGER,
  ADD COLUMN IF NOT EXISTS segments       INTEGER,
  ADD COLUMN IF NOT EXISTS color_sorting  TEXT;

-- Verify results
SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name IN ('parsed_session_stats', 'parsed_session_devices')
ORDER BY table_name, ordinal_position;
