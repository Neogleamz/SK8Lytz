-- 1. Custom Enum for Roles
DO $$ BEGIN
    CREATE TYPE public.user_role AS ENUM ('user', 'moderator', 'admin');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- 2. Schema modifications to user_profiles
ALTER TABLE public.user_profiles 
  ADD COLUMN IF NOT EXISTS role public.user_role DEFAULT 'user'::public.user_role NOT NULL,
  ADD COLUMN IF NOT EXISTS is_banned BOOLEAN DEFAULT false NOT NULL,
  ADD COLUMN IF NOT EXISTS ban_reason TEXT;

-- 3. Audit Log Table
CREATE TABLE IF NOT EXISTS public.admin_audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    admin_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE SET NULL,
    target_user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    action TEXT NOT NULL,
    reason TEXT,
    created_at TIMESTAMPTZ DEFAULT now() NOT NULL
);

-- RLS for Audit Log (Only Admins can view/insert)
ALTER TABLE public.admin_audit_logs ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Admins can view audit logs" ON public.admin_audit_logs;
DROP POLICY IF EXISTS "Admins can insert audit logs" ON public.admin_audit_logs;

CREATE POLICY "Admins can view audit logs" ON public.admin_audit_logs
    FOR SELECT USING (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin'
    );
CREATE POLICY "Admins can insert audit logs" ON public.admin_audit_logs
    FOR INSERT WITH CHECK (
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'admin' OR
        (SELECT role FROM public.user_profiles WHERE user_id = auth.uid()) = 'moderator'
    );

-- 4. RPCs (Security Definer)

-- 4a. Ban User
CREATE OR REPLACE FUNCTION public.admin_ban_user(p_target_user_id UUID, p_reason TEXT)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
BEGIN
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role NOT IN ('admin', 'moderator') THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin or moderator role';
    END IF;

    -- Update profile
    UPDATE public.user_profiles 
    SET is_banned = true, ban_reason = p_reason, updated_at = now()
    WHERE user_id = p_target_user_id;

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action, reason) 
    VALUES (auth.uid(), p_target_user_id, 'BAN_USER', p_reason);
END;
$$;

-- 4b. Revoke Ban
CREATE OR REPLACE FUNCTION public.admin_revoke_ban(p_target_user_id UUID)
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
DECLARE
    caller_role public.user_role;
BEGIN
    SELECT role INTO caller_role FROM public.user_profiles WHERE user_id = auth.uid();
    IF caller_role NOT IN ('admin', 'moderator') THEN
        RAISE EXCEPTION 'Unauthorized: Requires admin or moderator role';
    END IF;

    -- Update profile
    UPDATE public.user_profiles 
    SET is_banned = false, ban_reason = NULL, updated_at = now()
    WHERE user_id = p_target_user_id;

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action) 
    VALUES (auth.uid(), p_target_user_id, 'REVOKE_BAN');
END;
$$;

-- 4c. Force Password Reset (Lockout)
CREATE OR REPLACE FUNCTION public.admin_force_password_reset(p_target_user_id UUID)
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

    -- Randomize the password hash in auth.users so the user is locked out.
    -- They must now use 'Forgot Password' to regain access natively.
    UPDATE auth.users 
    SET encrypted_password = crypt(gen_random_uuid()::text, gen_salt('bf'))
    WHERE id = p_target_user_id;

    -- Terminate active sessions (optional depending on your Auth settings, but we force logout state usually by scrambling hash)

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action) 
    VALUES (auth.uid(), p_target_user_id, 'FORCE_PASSWORD_RESET');
END;
$$;

-- 4d. Soft Delete User
CREATE OR REPLACE FUNCTION public.admin_soft_delete_user(p_target_user_id UUID)
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

    -- Supabase doesn't natively support deleted_at on auth.users easily without custom schema.
    -- We can ban them and prefix their email to free it up, OR we can just delete them directly if we assume 'hard purge'.
    -- But since we want soft-delete, we will add 'deleted_at' to user_profiles.
    
    -- NOTE: To keep things simple since you already have `delete_account()` for hard-delete, 
    -- we'll soft-delete by banning and scrambling email so they can't login, 
    -- but keeping data intact for 30 days pending cron sweep.
    
    UPDATE auth.users 
    SET email = 'deleted_' || id || '_' || email,
        encrypted_password = crypt(gen_random_uuid()::text, gen_salt('bf'))
    WHERE id = p_target_user_id;

    UPDATE public.user_profiles 
    SET is_banned = true, ban_reason = 'Soft Deleted Pending Purge', updated_at = now()
    WHERE user_id = p_target_user_id;

    -- Log action
    INSERT INTO public.admin_audit_logs (admin_id, target_user_id, action) 
    VALUES (auth.uid(), p_target_user_id, 'SOFT_DELETE_USER');
END;
$$;

-- 5. Auto Promotion Trigger
CREATE OR REPLACE FUNCTION public.handle_auto_promotion()
RETURNS TRIGGER 
LANGUAGE plpgsql
SECURITY DEFINER
AS $$
BEGIN
    -- This trigger fires AFTER INSERT on auth.users.
    -- Notice we must wait for the user_profiles row to exist.
    -- Often user_profiles is created by ANOTHER trigger on auth.users.
    -- We will just do a direct UPDATE on user_profiles here, but if the row doesn't exist yet, it won't affect it. 
    -- Assuming your profile creation trigger fires FIRST or concurrently. Let's make sure by handling it in a resilient way. 
    
    IF NEW.email LIKE '%@sk8lytz.com' OR NEW.email LIKE '%@neogleamz.com' OR NEW.email LIKE '%@ledfordconsult.com' THEN
        -- Upsert to be safe, ensuring a profile exists with the admin role.
        INSERT INTO public.user_profiles (user_id, role)
        VALUES (NEW.id, 'admin')
        ON CONFLICT (user_id) DO UPDATE 
        SET role = 'admin', updated_at = now();
    END IF;
    
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trigger_auto_promotion ON auth.users;
CREATE TRIGGER trigger_auto_promotion
AFTER INSERT ON auth.users
FOR EACH ROW EXECUTE PROCEDURE public.handle_auto_promotion();
