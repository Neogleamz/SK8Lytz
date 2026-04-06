-- ============================================================
-- SK8Lytz: Migration 005 - Push Notification Tokens
-- Apply in Supabase SQL Editor
-- Project: qefmeivpjyaukbwadgaz
-- ============================================================

CREATE TABLE IF NOT EXISTS public.push_tokens (
  id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id    UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  token      TEXT NOT NULL,
  platform   TEXT NOT NULL CHECK (platform IN ('ios', 'android', 'web')),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (user_id, token)
);

ALTER TABLE public.push_tokens ENABLE ROW LEVEL SECURITY;

CREATE POLICY "push_tokens_select" ON public.push_tokens
  FOR SELECT TO authenticated USING (user_id = auth.uid());

CREATE POLICY "push_tokens_insert" ON public.push_tokens
  FOR INSERT TO authenticated WITH CHECK (user_id = auth.uid());

CREATE POLICY "push_tokens_update" ON public.push_tokens
  FOR UPDATE TO authenticated USING (user_id = auth.uid());

CREATE POLICY "push_tokens_delete" ON public.push_tokens
  FOR DELETE TO authenticated USING (user_id = auth.uid());

-- Index for fast token lookup by user
CREATE INDEX IF NOT EXISTS idx_push_tokens_user_id ON public.push_tokens (user_id);
