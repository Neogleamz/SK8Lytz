-- ============================================================
-- MIGRATION: Fix admin revoke RPC + auto-promotion security hole
-- Date: 2026-05-12
-- ============================================================

-- 1. Create a proper SECURITY DEFINER RPC for revoking admin role.
--    The previous implementation did a direct client-side UPDATE which RLS blocks.
CREATE OR REPLACE FUNCTION public.admin_revoke_admin_role(p_target_user_id UUID)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
    target_role public.user_role;
BEGIN
    -- Verify caller is admin
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role != 'admin' THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin role';
    END IF;

    -- Prevent self-demotion
    IF p_target_user_id = auth.uid() THEN
        RAISE EXCEPTION 'Cannot revoke your own admin privileges';
    END IF;

    -- Verify target is actually an admin
    SELECT role INTO target_role FROM public.user_profiles WHERE user_id = p_target_user_id;
    IF target_role != 'admin' THEN
        RAISE EXCEPTION 'Target user is not an admin';
    END IF;

    -- Demote the user
    UPDATE public.user_profiles
    SET role = 'user', updated_at = now()
    WHERE user_id = p_target_user_id;

    -- Audit log
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action, reason)
    VALUES (auth.uid(), p_target_user_id, 'REVOKE_ADMIN_ROLE', 'Demoted via Admin Roster Panel');
END;
$$;

-- 2. SECURITY FIX: Replace the dangerous domain-wildcard auto-promotion trigger
--    with a hardcoded exact-email allowlist.
--    The old trigger promoted anyone signing up with @sk8lytz.com, @neogleamz.com,
--    or @ledfordconsult.com — meaning ANY email from those domains became admin.
--    This is how skater_**** accounts got promoted.
CREATE OR REPLACE FUNCTION public.handle_auto_promotion()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    admin_email_list TEXT[];
BEGIN
    -- Hardcoded exact allowlist. Only these specific addresses auto-promote.
    -- Add/remove emails here as needed. No wildcards. No domains.
    admin_email_list := ARRAY[
        -- 'you@yourdomain.com'  -- Add your real admin email here
    ];

    IF NEW.email = ANY(admin_email_list) THEN
        INSERT INTO public.user_profiles (user_id, role)
        VALUES (NEW.id, 'admin')
        ON CONFLICT (user_id) DO UPDATE
        SET role = 'admin', updated_at = now();
    END IF;

    RETURN NEW;
END;
$$;

-- Trigger already exists — just replacing the function is sufficient.
-- (The CREATE OR REPLACE above updates the function body in-place.)
