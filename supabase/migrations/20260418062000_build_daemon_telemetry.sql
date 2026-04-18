-- Migration: 20260418062000_build_daemon_telemetry.sql
-- Description: Creates the singleton daemon_status table for tracing ETL pipeline metrics.

CREATE TABLE public.daemon_status (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status TEXT NOT NULL DEFAULT 'OFFLINE',
    current_target TEXT,
    total_enriched INT NOT NULL DEFAULT 0,
    total_denials INT NOT NULL DEFAULT 0,
    last_heartbeat TIMESTAMP WITH TIME ZONE,
    last_error TEXT,
    CONSTRAINT single_row_enforcer CHECK (id = '00000000-0000-0000-0000-000000000000')
);

-- Turn on RLS
ALTER TABLE public.daemon_status ENABLE ROW LEVEL SECURITY;

-- Allow public read access (for the Admin Tools hook to poll)
CREATE POLICY "Allow public read access to daemon_status"
    ON public.daemon_status
    FOR SELECT
    TO public
    USING (true);

-- Provide the singleton row immediately
INSERT INTO public.daemon_status (id, status, total_enriched, total_denials) 
VALUES ('00000000-0000-0000-0000-000000000000', 'OFFLINE', 0, 0);
