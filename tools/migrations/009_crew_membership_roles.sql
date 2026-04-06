-- Migration 009: Multi-owner support via crew_memberships.role
-- Run once in Supabase SQL editor.

-- 1. Add role column
ALTER TABLE public.crew_memberships
  ADD COLUMN IF NOT EXISTS role TEXT NOT NULL DEFAULT 'member'
    CHECK (role IN ('owner', 'member'));

-- 2. Back-fill existing owners
UPDATE public.crew_memberships cm
SET    role = 'owner'
FROM   public.crews c
WHERE  cm.crew_id = c.id
  AND  cm.user_id = c.owner_id;

-- 3. Performance index
CREATE INDEX IF NOT EXISTS idx_crew_memberships_role
  ON public.crew_memberships (crew_id, role);

-- 4. RLS: owners can update member roles
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_policies
    WHERE tablename = 'crew_memberships'
      AND policyname = 'Owners can manage member roles'
  ) THEN
    EXECUTE $policy$
      CREATE POLICY "Owners can manage member roles"
        ON public.crew_memberships
        FOR UPDATE
        USING (
          EXISTS (
            SELECT 1 FROM public.crew_memberships AS cm2
            WHERE cm2.crew_id = crew_memberships.crew_id
              AND cm2.user_id = auth.uid()
              AND cm2.role = 'owner'
          )
        )
    $policy$;
  END IF;
END;
$$;
