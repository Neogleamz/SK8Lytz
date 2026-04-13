-- Hardening SK8Lytz Infrastructure Migration
-- Optimized for Performance and Security (Stability-First)

BEGIN;

-- 1. Indexing missing foreign keys for performance
CREATE INDEX IF NOT EXISTS "idx_skate_sessions_crew_session_id" ON "public"."skate_sessions" ("crew_session_id");
CREATE INDEX IF NOT EXISTS "idx_skate_spots_updated_by" ON "public"."skate_spots" ("updated_by");
CREATE INDEX IF NOT EXISTS "idx_crew_members_session_id" ON "public"."crew_members" ("session_id");
CREATE INDEX IF NOT EXISTS "idx_crew_memberships_crew_id" ON "public"."crew_memberships" ("crew_id");

-- 2. Transition User Profiles to secure authenticated access only
DROP POLICY IF EXISTS "profiles_select" ON "public"."user_profiles";
CREATE POLICY "profiles_select_secure" ON "public"."user_profiles"
FOR SELECT TO authenticated
USING (true); -- Public profiles are okay for discovery but restricted in select to authenticated

DROP POLICY IF EXISTS "profiles_insert" ON "public"."user_profiles";
CREATE POLICY "profiles_insert_secure" ON "public"."user_profiles"
FOR INSERT TO authenticated
WITH CHECK ((select auth.uid()) = user_id);

DROP POLICY IF EXISTS "profiles_update" ON "public"."user_profiles";
CREATE POLICY "profiles_update_secure" ON "public"."user_profiles"
FOR UPDATE TO authenticated
USING ((select auth.uid()) = user_id);

-- 3. Telemetry Hardening: Restriction to Authenticated Users
-- Prevents public bot spam while allowing app telemetry
ALTER TABLE "public"."parsed_session_stats" ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Allow public insert" ON "public"."parsed_session_stats";
CREATE POLICY "telemetry_insert_auth" ON "public"."parsed_session_stats"
FOR INSERT TO authenticated
WITH CHECK (true);

DROP POLICY IF EXISTS "Allow public update on session_id conflict" ON "public"."parsed_session_stats";
CREATE POLICY "telemetry_update_auth" ON "public"."parsed_session_stats"
FOR UPDATE TO authenticated
USING (true);

-- 4. Secure Skate Sessions
DROP POLICY IF EXISTS "Enable all access for authenticated users" ON "public"."skate_sessions";
CREATE POLICY "skate_sessions_owner_access" ON "public"."skate_sessions"
FOR ALL TO authenticated
USING ((select auth.uid()) = user_id);

-- 5. Optimization: Standardize (select auth.uid()) across existing core policies
-- This prevents per-row function re-execution

-- Registered Devices
DROP POLICY IF EXISTS "Users can see their own devices" ON "public"."registered_devices";
CREATE POLICY "devices_owner_select" ON "public"."registered_devices"
FOR SELECT TO authenticated
USING ((select auth.uid()) = user_id);

DROP POLICY IF EXISTS "Users can register their own devices" ON "public"."registered_devices";
CREATE POLICY "devices_owner_insert" ON "public"."registered_devices"
FOR INSERT TO authenticated
WITH CHECK ((select auth.uid()) = user_id);

DROP POLICY IF EXISTS "Users can update their own devices" ON "public"."registered_devices";
CREATE POLICY "devices_owner_update" ON "public"."registered_devices"
FOR UPDATE TO authenticated
USING ((select auth.uid()) = user_id);

COMMIT;
