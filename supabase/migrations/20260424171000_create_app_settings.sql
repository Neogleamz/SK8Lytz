-- Create the dedicated table for global app settings and controls
CREATE TABLE IF NOT EXISTS public.sk8lytz_app_settings (
    setting_key TEXT PRIMARY KEY,
    setting_value JSONB NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    updated_by UUID REFERENCES auth.users(id) ON DELETE SET NULL
);

-- Enable Row Level Security
ALTER TABLE public.sk8lytz_app_settings ENABLE ROW LEVEL SECURITY;

-- Clean up any existing policies during iteration
DROP POLICY IF EXISTS "Anyone can view app settings" ON public.sk8lytz_app_settings;
DROP POLICY IF EXISTS "Admins can update app settings" ON public.sk8lytz_app_settings;

-- Policy: SELECT
-- We allow all authenticated users to read settings, so UI features can lock/hide based on governance.
CREATE POLICY "Anyone can view app settings"
    ON public.sk8lytz_app_settings
    FOR SELECT
    USING (auth.role() = 'authenticated');

-- Policy: ALL (INSERT/UPDATE/DELETE)
-- Only profiles with the 'admin' role can mutate the app settings.
CREATE POLICY "Admins can update app settings"
    ON public.sk8lytz_app_settings
    FOR ALL
    USING (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin'
    )
    WITH CHECK (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin'
    );
