-- ═══════════════════════════════════════════════════════════
-- SK8Lytz — Host Device Telemetry Columns Migration
-- Run in Supabase SQL Editor:
-- https://supabase.com/dashboard/project/qefmeivpjyaukbwadgaz/sql
-- ═══════════════════════════════════════════════════════════

ALTER TABLE parsed_session_stats
  -- Device Identity
  ADD COLUMN IF NOT EXISTS device_brand          TEXT,
  ADD COLUMN IF NOT EXISTS device_model          TEXT,
  ADD COLUMN IF NOT EXISTS device_manufacturer   TEXT,
  ADD COLUMN IF NOT EXISTS device_model_id       TEXT,
  ADD COLUMN IF NOT EXISTS device_name           TEXT,
  ADD COLUMN IF NOT EXISTS device_type           TEXT,
  ADD COLUMN IF NOT EXISTS device_year_class     INTEGER,
  ADD COLUMN IF NOT EXISTS is_physical_device    BOOLEAN,
  -- OS
  ADD COLUMN IF NOT EXISTS os_name               TEXT,
  ADD COLUMN IF NOT EXISTS os_version            TEXT,
  ADD COLUMN IF NOT EXISTS os_build_id           TEXT,
  ADD COLUMN IF NOT EXISTS platform_api_level    INTEGER,
  -- Hardware
  ADD COLUMN IF NOT EXISTS total_memory_mb       INTEGER,
  -- Power
  ADD COLUMN IF NOT EXISTS battery_state         TEXT,
  -- Full device info blob (all expo-device fields)
  ADD COLUMN IF NOT EXISTS host_device_info      JSONB DEFAULT '{}';

-- Verify
SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'parsed_session_stats'
ORDER BY ordinal_position;
