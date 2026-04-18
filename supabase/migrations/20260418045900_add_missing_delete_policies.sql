-- Migration: Add missing DELETE policies for devices and groups
-- This resolves the "ghost devices in account manager" split-brain issue
-- where the app swallowed RLS silent-fails during device deregistration.

BEGIN;

-- 1. Add missing DELETE policy to registered_devices
DROP POLICY IF EXISTS "devices_owner_delete" ON "public"."registered_devices";
CREATE POLICY "devices_owner_delete" ON "public"."registered_devices"
FOR DELETE TO authenticated
USING ((select auth.uid()) = user_id);

-- 2. Ensure registered_groups also has a complete suite of RLS policies if it didn't already
ALTER TABLE "public"."registered_groups" ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "groups_owner_select" ON "public"."registered_groups";
CREATE POLICY "groups_owner_select" ON "public"."registered_groups"
FOR SELECT TO authenticated
USING ((select auth.uid()) = user_id);

DROP POLICY IF EXISTS "groups_owner_insert" ON "public"."registered_groups";
CREATE POLICY "groups_owner_insert" ON "public"."registered_groups"
FOR INSERT TO authenticated
WITH CHECK ((select auth.uid()) = user_id);

DROP POLICY IF EXISTS "groups_owner_update" ON "public"."registered_groups";
CREATE POLICY "groups_owner_update" ON "public"."registered_groups"
FOR UPDATE TO authenticated
USING ((select auth.uid()) = user_id);

DROP POLICY IF EXISTS "groups_owner_delete" ON "public"."registered_groups";
CREATE POLICY "groups_owner_delete" ON "public"."registered_groups"
FOR DELETE TO authenticated
USING ((select auth.uid()) = user_id);

COMMIT;
