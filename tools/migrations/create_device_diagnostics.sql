-- ============================================================
-- SK8Lytz Device Diagnostics Table
-- Run this in Supabase SQL Editor: https://supabase.com/dashboard/project/qefmeivpjyaukbwadgaz/sql
-- ============================================================

CREATE TABLE IF NOT EXISTS public.device_diagnostics (
  id            BIGSERIAL PRIMARY KEY,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  session_id    TEXT,
  device_id     TEXT NOT NULL,
  device_name   TEXT,
  payload_hex   TEXT NOT NULL,
  payload_bytes INTEGER NOT NULL,
  byte_0        INTEGER,   -- first byte (0x63 = hw settings, 0x00 = V2 wrapper, etc.)
  byte_2        INTEGER,   -- byte[2]: 0x80 = V2 protocol
  parsed_ok     BOOLEAN DEFAULT FALSE,
  points        INTEGER,
  ic_type       INTEGER,
  ic_name       TEXT,
  color_sorting INTEGER,
  color_order   TEXT,
  notes         TEXT
);

-- RLS: allow anon inserts and reads (no auth required for diagnostics)
ALTER TABLE public.device_diagnostics ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "allow_anon_insert" ON public.device_diagnostics;
CREATE POLICY "allow_anon_insert" ON public.device_diagnostics
  FOR INSERT TO anon WITH CHECK (true);

DROP POLICY IF EXISTS "allow_anon_select" ON public.device_diagnostics;
CREATE POLICY "allow_anon_select" ON public.device_diagnostics
  FOR SELECT TO anon USING (true);

CREATE INDEX IF NOT EXISTS idx_dd_device_id   ON public.device_diagnostics(device_id);
CREATE INDEX IF NOT EXISTS idx_dd_created_at  ON public.device_diagnostics(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_dd_session_id  ON public.device_diagnostics(session_id);
