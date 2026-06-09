-- =========================================================================
-- Supabase Migration: Device Groups Many-to-Many Architecture
-- =========================================================================

-- 1. Create the device_group_members junction table
CREATE TABLE IF NOT EXISTS public.device_group_members (
    device_id TEXT REFERENCES public.registered_devices(id) ON DELETE CASCADE,
    group_id TEXT REFERENCES public.registered_groups(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    PRIMARY KEY (device_id, group_id)
);

-- Enable RLS on the junction table
ALTER TABLE public.device_group_members ENABLE ROW LEVEL SECURITY;

-- 2. Create RLS Policies for the junction table
CREATE POLICY "Users can manage their own device group memberships"
ON public.device_group_members
FOR ALL
USING (
  EXISTS (
    SELECT 1 FROM public.registered_devices rd 
    WHERE rd.id = device_group_members.device_id AND rd.user_id = auth.uid()
  )
)
WITH CHECK (
  EXISTS (
    SELECT 1 FROM public.registered_devices rd 
    WHERE rd.id = device_group_members.device_id AND rd.user_id = auth.uid()
  )
);

-- 3. Replace the `upsert_group_with_devices` RPC to support the junction table
CREATE OR REPLACE FUNCTION public.upsert_group_with_devices(
    p_group_id TEXT,
    p_group_name TEXT,
    p_type TEXT,
    p_device_ids TEXT[]
)
RETURNS VOID
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
BEGIN
    -- 1. Upsert the group in `registered_groups`
    INSERT INTO public.registered_groups (id, group_name, type, user_id)
    VALUES (p_group_id, p_group_name, p_type, auth.uid())
    ON CONFLICT (id) DO UPDATE SET group_name = EXCLUDED.group_name, type = EXCLUDED.type;

    -- 2. Delete existing memberships for this group
    DELETE FROM public.device_group_members WHERE group_id = p_group_id;

    -- 3. Insert new memberships for this group
    IF array_length(p_device_ids, 1) > 0 THEN
        INSERT INTO public.device_group_members (group_id, device_id)
        SELECT p_group_id, unnest(p_device_ids);
    END IF;
END;
$$;
