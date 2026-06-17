-- ==============================================================================
-- Migration: 20260617010000_sk8lytz_picks_admin_policy.sql
-- Description: Add RLS policies allowing admins to manage sk8lytz_picks
-- ==============================================================================

CREATE POLICY "Admins can manage picks" ON public.sk8lytz_picks
    FOR ALL TO authenticated USING (
        EXISTS (
            SELECT 1 FROM public.user_profiles
            WHERE user_id = auth.uid() AND role = 'admin'
        )
    ) WITH CHECK (
        EXISTS (
            SELECT 1 FROM public.user_profiles
            WHERE user_id = auth.uid() AND role = 'admin'
        )
    );
