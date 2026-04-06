-- ============================================================
-- SK8Lytz: Migration 003 - User Profiles
-- Apply in Supabase SQL Editor
-- Project: qefmeivpjyaukbwadgaz
-- ============================================================

CREATE TABLE IF NOT EXISTS public.user_profiles (
  user_id      UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  display_name TEXT,
  avatar_color TEXT NOT NULL DEFAULT '#FF8C00',
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE public.user_profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "profiles_select" ON public.user_profiles
  FOR SELECT TO authenticated USING (true);

CREATE POLICY "profiles_insert" ON public.user_profiles
  FOR INSERT TO authenticated WITH CHECK (user_id = auth.uid());

CREATE POLICY "profiles_update" ON public.user_profiles
  FOR UPDATE TO authenticated USING (user_id = auth.uid());

-- Auto-update updated_at on row change
CREATE OR REPLACE FUNCTION public.set_updated_at()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN NEW.updated_at = now(); RETURN NEW; END; $$;

CREATE TRIGGER user_profiles_updated_at
  BEFORE UPDATE ON public.user_profiles
  FOR EACH ROW EXECUTE FUNCTION public.set_updated_at();
