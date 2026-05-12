-- Migration: Add Health Sync Telemetry to skate_sessions
-- Created: 2026-05-12

ALTER TABLE "public"."skate_sessions"
ADD COLUMN IF NOT EXISTS "avg_bpm" integer,
ADD COLUMN IF NOT EXISTS "peak_bpm" integer,
ADD COLUMN IF NOT EXISTS "active_calories" integer;

-- Update the comments for schema clarity
COMMENT ON COLUMN "public"."skate_sessions"."avg_bpm" IS 'Average Heart Rate (BPM) recorded during the session via Health Integration';
COMMENT ON COLUMN "public"."skate_sessions"."peak_bpm" IS 'Peak Heart Rate (BPM) recorded during the session via Health Integration';
COMMENT ON COLUMN "public"."skate_sessions"."active_calories" IS 'Active energy burned (kcal) during the session via Health Integration';
