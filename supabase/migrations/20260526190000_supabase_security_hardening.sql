-- HARDENING SWEEP: PL/pgSQL Function Search Path Hardening
-- Lock down all SK8Lytz administrative and trigger functions to the public schema to prevent mutable search path escalation.
ALTER FUNCTION public.admin_add_hardware_blacklist(p_mac_address text, p_reason text) SET search_path = public;
ALTER FUNCTION public.admin_export_user_data(p_target_user_id uuid) SET search_path = public;
ALTER FUNCTION public.admin_get_global_telemetry() SET search_path = public;
ALTER FUNCTION public.admin_remove_hardware_blacklist(p_mac_address text) SET search_path = public;
ALTER FUNCTION public.admin_revoke_admin_role(p_target_user_id uuid) SET search_path = public;
ALTER FUNCTION public.admin_revoke_sessions(p_target_user_id uuid) SET search_path = public, auth;
ALTER FUNCTION public.handle_auto_promotion() SET search_path = public;
ALTER FUNCTION public.handle_new_user() SET search_path = public;

-- Secure legacy/ETL scraper helper functions
ALTER FUNCTION public.get_databank_coverage() SET search_path = public;
ALTER FUNCTION public.get_next_spot_for_indexer(text[]) SET search_path = public;
ALTER FUNCTION public.get_next_spot_for_operator(text[]) SET search_path = public;

-- HARDENING SWEEP: Row Level Security Hardening
-- Finding 2: Fix broken SELECT policy on discovered_devices_telemetry for admins
DROP POLICY IF EXISTS "Only admins can select telemetry" ON public.discovered_devices_telemetry;
CREATE POLICY "Only admins can select telemetry" ON public.discovered_devices_telemetry
  FOR SELECT
  TO authenticated
  USING (
    EXISTS (
      SELECT 1 FROM public.user_profiles
      WHERE user_profiles.user_id = auth.uid()
        AND (role = 'admin'::public.user_role OR role = 'moderator'::public.user_role)
    )
  );

-- Finding 3: Drop the permissive authenticated_full_access policy on sk8lytz_app_settings
DROP POLICY IF EXISTS "authenticated_full_access" ON public.sk8lytz_app_settings;

-- Finding 5: Drop anonymous write policies on skate_spots (Indexer/Publisher uses service_role key)
DROP POLICY IF EXISTS "Allow public insert for scraper" ON public.skate_spots;
DROP POLICY IF EXISTS "Allow public update for scraper" ON public.skate_spots;

