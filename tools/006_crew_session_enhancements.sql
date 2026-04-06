-- ============================================================
-- SK8Lytz: Migration 006 - Crew Session Enhancements
-- Adds: location, status, scheduled_at, ended_at, username lookup
-- Apply in Supabase SQL Editor
-- Project: qefmeivpjyaukbwadgaz
-- ============================================================

-- Enhance crew_sessions with location + lifecycle status
ALTER TABLE public.crew_sessions
  ADD COLUMN IF NOT EXISTS status TEXT NOT NULL DEFAULT 'active'
    CHECK (status IN ('scheduled', 'active', 'ended')),
  ADD COLUMN IF NOT EXISTS scheduled_at  TIMESTAMPTZ,
  ADD COLUMN IF NOT EXISTS ended_at      TIMESTAMPTZ,
  ADD COLUMN IF NOT EXISTS location_label TEXT,
  ADD COLUMN IF NOT EXISTS location_coords JSONB;  -- { lat, lng }

-- Index for scheduled session discovery
CREATE INDEX IF NOT EXISTS idx_crew_sessions_scheduled
  ON public.crew_sessions (status, scheduled_at)
  WHERE status = 'scheduled';

-- Add username column to user_profiles for lookup
ALTER TABLE public.user_profiles
  ADD COLUMN IF NOT EXISTS username TEXT UNIQUE;

CREATE INDEX IF NOT EXISTS idx_user_profiles_username ON public.user_profiles (lower(username));

-- SECURITY DEFINER function: look up email by username (for login with username)
-- Runs as postgres superuser so it can read auth.users
CREATE OR REPLACE FUNCTION public.get_email_by_username(p_username TEXT)
RETURNS TEXT
LANGUAGE sql
SECURITY DEFINER
SET search_path = public
AS $$
  SELECT email FROM auth.users
  WHERE raw_user_meta_data->>'username' = p_username
     OR lower(raw_user_meta_data->>'username') = lower(p_username)
  LIMIT 1;
$$;

-- Grant anon role access (needed for pre-auth lookup)
GRANT EXECUTE ON FUNCTION public.get_email_by_username(TEXT) TO anon;
GRANT EXECUTE ON FUNCTION public.get_email_by_username(TEXT) TO authenticated;
