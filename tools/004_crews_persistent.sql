-- ============================================================
-- SK8Lytz: Migration 004 - Persistent Crews + Memberships
-- Apply in Supabase SQL Editor
-- Project: qefmeivpjyaukbwadgaz
-- ============================================================

-- Permanent crew entities (long-lived, unlike ephemeral crew_sessions)
CREATE TABLE IF NOT EXISTS public.crews (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  name        TEXT NOT NULL,
  owner_id    UUID REFERENCES auth.users(id) ON DELETE SET NULL,
  invite_code TEXT UNIQUE NOT NULL DEFAULT upper(substring(replace(gen_random_uuid()::text,'-',''),1,6)),
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Permanent crew membership (user follows a crew)
CREATE TABLE IF NOT EXISTS public.crew_memberships (
  id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  crew_id   UUID NOT NULL REFERENCES public.crews(id) ON DELETE CASCADE,
  user_id   UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  joined_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (crew_id, user_id)
);

-- Link ephemeral sessions back to a permanent crew (optional)
ALTER TABLE public.crew_sessions
  ADD COLUMN IF NOT EXISTS crew_id UUID REFERENCES public.crews(id) ON DELETE SET NULL;

-- RLS
ALTER TABLE public.crews           ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.crew_memberships ENABLE ROW LEVEL SECURITY;

-- crews policies
CREATE POLICY "crews_select" ON public.crews
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "crews_insert" ON public.crews
  FOR INSERT TO authenticated WITH CHECK (owner_id = auth.uid());

CREATE POLICY "crews_update" ON public.crews
  FOR UPDATE TO authenticated USING (owner_id = auth.uid());

CREATE POLICY "crews_delete" ON public.crews
  FOR DELETE TO authenticated USING (owner_id = auth.uid());

-- crew_memberships policies
CREATE POLICY "memberships_select" ON public.crew_memberships
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "memberships_insert" ON public.crew_memberships
  FOR INSERT TO authenticated WITH CHECK (user_id = auth.uid());

CREATE POLICY "memberships_delete" ON public.crew_memberships
  FOR DELETE TO authenticated USING (user_id = auth.uid());

-- Index for quick crew session lookup by crew_id
CREATE INDEX IF NOT EXISTS idx_crew_sessions_crew_id ON public.crew_sessions (crew_id);
