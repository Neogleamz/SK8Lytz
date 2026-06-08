-- ==============================================================================
-- Migration: 20260608000000_sk8lytz_security_hardening.sql
-- Description: Enforce RLS and patch permissive USING(true) policies for SK8Lytz tables
-- ==============================================================================

-- 1. Fix excessively permissive policies on SK8Lytz tables
-- --------------------------------------------------------

-- parsed_session_stats: Fix the UPDATE policy that allowed anyone to update any stat.
DROP POLICY IF EXISTS "telemetry_update_auth" ON "public"."parsed_session_stats";
CREATE POLICY "telemetry_update_auth" ON "public"."parsed_session_stats"
    FOR UPDATE TO authenticated
    USING (user_id = auth.uid());

-- user_profiles: Keep the SELECT public but ensure it's explicitly authenticated
DROP POLICY IF EXISTS "profiles_select_secure" ON "public"."user_profiles";
CREATE POLICY "profiles_select_secure" ON "public"."user_profiles"
    FOR SELECT TO authenticated
    USING (true);

-- 2. Defensively enforce ROW LEVEL SECURITY on all active SK8Lytz tables
-- ----------------------------------------------------------------------
ALTER TABLE "public"."admin_audit_logs" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."feature_flags" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."hardware_blacklist" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."sk8lytz_app_settings" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."sk8lytz_picks" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."product_catalog" ENABLE ROW LEVEL SECURITY;

ALTER TABLE "public"."discovered_devices_telemetry" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."telemetry_snapshots" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."telemetry_errors" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."user_lifetime_stats" ENABLE ROW LEVEL SECURITY;

ALTER TABLE "public"."user_profiles" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."push_tokens" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."crews" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."crew_members" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."crew_memberships" ENABLE ROW LEVEL SECURITY;

ALTER TABLE "public"."public_sessions" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."crew_sessions" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."skate_sessions" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."skate_spots" ENABLE ROW LEVEL SECURITY;

ALTER TABLE "public"."registered_devices" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."registered_groups" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."user_saved_presets" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."custom_builder_presets" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."shared_scenes" ENABLE ROW LEVEL SECURITY;
ALTER TABLE "public"."parsed_session_stats" ENABLE ROW LEVEL SECURITY;

-- Note: We are explicitly skipping `postgis`, `spatial_ref_sys`, `scraper_config`, 
-- `daemon_status`, and other shared infra/other apps' tables to avoid collateral damage.
