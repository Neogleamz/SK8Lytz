-- 1. Hardware Blacklist Table
CREATE TABLE IF NOT EXISTS public.hardware_blacklist (
    mac_address TEXT PRIMARY KEY,
    reason TEXT NOT NULL,
    added_by UUID NOT NULL REFERENCES auth.users(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

ALTER TABLE public.hardware_blacklist ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Admins can view hardware blacklist" ON public.hardware_blacklist
    FOR SELECT USING (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin'
    );
CREATE POLICY "Admins can manage hardware blacklist" ON public.hardware_blacklist
    FOR ALL USING (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin'
    );

-- 2. Feature Flags Table
CREATE TABLE IF NOT EXISTS public.feature_flags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    flag_key TEXT NOT NULL,
    target_user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    is_enabled BOOLEAN DEFAULT false NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL,
    UNIQUE(flag_key, target_user_id) -- If target_user_id is NULL, it's global
);

ALTER TABLE public.feature_flags ENABLE ROW LEVEL SECURITY;

-- Everyone can view feature flags (since clients need to fetch them)
CREATE POLICY "Anyone can view feature flags" ON public.feature_flags
    FOR SELECT USING (true);
    
-- Only Admins can manage them
CREATE POLICY "Admins can manage feature flags" ON public.feature_flags
    FOR ALL USING (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin'
    );

-- 3. Revoke Sessions RPC (Security Definer to bypass RLS on auth.sessions)
CREATE OR REPLACE FUNCTION public.admin_revoke_sessions(p_target_user_id UUID)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
BEGIN
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role != 'admin' THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin role';
    END IF;

    -- Delete all active sessions for the user
    DELETE FROM auth.sessions WHERE user_id = p_target_user_id;

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action) 
    VALUES (auth.uid(), p_target_user_id, 'REVOKE_SESSIONS');
END;
$$;

-- 4. Export User Data Archive RPC
CREATE OR REPLACE FUNCTION public.admin_export_user_data(p_target_user_id UUID)
RETURNS JSONB
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
    result JSONB;
BEGIN
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role != 'admin' THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin role';
    END IF;

    -- Aggregate user profile, registered fleet, and crew memberships
    SELECT jsonb_build_object(
        'profile', (SELECT row_to_json(up) FROM public.user_profiles up WHERE user_id = p_target_user_id),
        'fleet', COALESCE((SELECT jsonb_agg(row_to_json(f)) FROM public.user_devices f WHERE user_id = p_target_user_id), '[]'::jsonb),
        'crews', COALESCE((SELECT jsonb_agg(row_to_json(cm)) FROM public.crew_members cm WHERE user_id = p_target_user_id), '[]'::jsonb)
    ) INTO result;

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action) 
    VALUES (auth.uid(), p_target_user_id, 'EXPORT_USER_DATA');

    RETURN result;
END;
$$;

-- 5. Hardware Blacklist RPCs
CREATE OR REPLACE FUNCTION public.admin_add_hardware_blacklist(p_mac_address TEXT, p_reason TEXT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
BEGIN
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role != 'admin' THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin role';
    END IF;

    INSERT INTO public.hardware_blacklist (mac_address, reason, added_by)
    VALUES (upper(p_mac_address), p_reason, auth.uid())
    ON CONFLICT (mac_address) DO UPDATE SET reason = p_reason, added_by = auth.uid();

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, action, reason) 
    VALUES (auth.uid(), 'ADD_HARDWARE_BLACKLIST', upper(p_mac_address) || ': ' || p_reason);
END;
$$;

CREATE OR REPLACE FUNCTION public.admin_remove_hardware_blacklist(p_mac_address TEXT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
BEGIN
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role != 'admin' THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin role';
    END IF;

    DELETE FROM public.hardware_blacklist WHERE mac_address = upper(p_mac_address);

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, action, reason) 
    VALUES (auth.uid(), 'REMOVE_HARDWARE_BLACKLIST', upper(p_mac_address));
END;
$$;
