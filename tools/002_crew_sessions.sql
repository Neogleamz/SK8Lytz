-- ============================================================
-- SK8Lytz: Migration 002 - Crew Sessions
-- Apply this in the Supabase SQL Editor
-- Project: qefmeivpjyaukbwadgaz
-- ============================================================

-- Crew Sessions table
CREATE TABLE IF NOT EXISTS public.crew_sessions (
  id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  invite_code    TEXT UNIQUE NOT NULL DEFAULT upper(substring(replace(gen_random_uuid()::text,'-',''),1,6)),
  name           TEXT NOT NULL DEFAULT 'Crew Session',
  leader_user_id UUID REFERENCES auth.users(id) ON DELETE SET NULL,
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
  expires_at     TIMESTAMPTZ NOT NULL DEFAULT now() + interval '12 hours',
  is_active      BOOLEAN NOT NULL DEFAULT true,
  -- Stores the most recent scene snapshot for late-arriving members to sync immediately
  last_scene     JSONB DEFAULT NULL
);

-- Index for the active sessions browse query
CREATE INDEX IF NOT EXISTS idx_crew_sessions_active ON public.crew_sessions (is_active, expires_at DESC);

-- Crew Members table
CREATE TABLE IF NOT EXISTS public.crew_members (
  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id   UUID NOT NULL REFERENCES public.crew_sessions(id) ON DELETE CASCADE,
  user_id      UUID REFERENCES auth.users(id) ON DELETE CASCADE,
  display_name TEXT,
  joined_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (session_id, user_id)
);

-- Enable RLS
ALTER TABLE public.crew_sessions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.crew_members  ENABLE ROW LEVEL SECURITY;

-- crew_sessions policies
CREATE POLICY "crew_sessions_select" ON public.crew_sessions
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "crew_sessions_insert" ON public.crew_sessions
  FOR INSERT TO authenticated WITH CHECK (leader_user_id = auth.uid());

CREATE POLICY "crew_sessions_update" ON public.crew_sessions
  FOR UPDATE TO authenticated USING (leader_user_id = auth.uid());

-- crew_members policies
CREATE POLICY "crew_members_select" ON public.crew_members
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "crew_members_insert" ON public.crew_members
  FOR INSERT TO authenticated WITH CHECK (user_id = auth.uid());

CREATE POLICY "crew_members_delete" ON public.crew_members
  FOR DELETE TO authenticated USING (user_id = auth.uid());
