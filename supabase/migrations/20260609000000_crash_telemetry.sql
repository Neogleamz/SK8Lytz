-- Create the crash_telemetry table for the Flight Recorder
CREATE TABLE IF NOT EXISTS public.crash_telemetry (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE SET NULL,
    error_signature TEXT NOT NULL,
    stack_trace TEXT,
    breadcrumbs JSONB DEFAULT '[]'::jsonb,
    environment_state JSONB DEFAULT '{}'::jsonb,
    severity TEXT DEFAULT 'ERROR' CHECK (severity IN ('FATAL', 'ERROR', 'WARN', 'INFO')),
    status TEXT DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'INVESTIGATING', 'RESOLVED', 'IGNORED')),
    app_version TEXT,
    created_at TIMESTAMPTZ DEFAULT now(),
    resolved_at TIMESTAMPTZ,
    resolved_by UUID REFERENCES auth.users(id) ON DELETE SET NULL
);

-- Add an index to speed up live querying by status and severity
CREATE INDEX IF NOT EXISTS idx_crash_telemetry_status ON public.crash_telemetry(status);
CREATE INDEX IF NOT EXISTS idx_crash_telemetry_created_at ON public.crash_telemetry(created_at DESC);

-- Enable RLS
ALTER TABLE public.crash_telemetry ENABLE ROW LEVEL SECURITY;

-- Policy: Anyone authenticated can insert their own crash reports
CREATE POLICY "Users can insert their own crash reports"
    ON public.crash_telemetry
    FOR INSERT
    TO authenticated
    WITH CHECK (auth.uid() = user_id OR user_id IS NULL);

-- Policy: Service Role or Admin can view and update all crash reports
CREATE POLICY "Admins can view and manage crash reports"
    ON public.crash_telemetry
    FOR ALL
    TO authenticated
    USING (
        EXISTS (
            SELECT 1 FROM public.user_profiles
            WHERE public.user_profiles.user_id = auth.uid() AND public.user_profiles.role = 'admin'
        )
    );

-- Enable Realtime for live dashboard streaming
ALTER PUBLICATION supabase_realtime ADD TABLE crash_telemetry;
