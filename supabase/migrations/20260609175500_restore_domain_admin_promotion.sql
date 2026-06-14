-- ============================================================
-- MIGRATION: Restore domain-based admin auto-promotion
-- Date: 2026-06-09
-- ============================================================

-- Restore the domain wildcard auto-promotion trigger for sk8lytz.com and neogleamz.com
-- as requested to ensure all team members get automatic admin access.
CREATE OR REPLACE FUNCTION public.handle_auto_promotion()
RETURNS TRIGGER
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = ''
AS $$
BEGIN
    IF RIGHT(NEW.email, 12) = '@sk8lytz.com' OR RIGHT(NEW.email, 14) = '@neogleamz.com' THEN
        INSERT INTO public.user_profiles (user_id, role)
        VALUES (NEW.id, 'admin')
        ON CONFLICT (user_id) DO UPDATE
        SET role = 'admin', updated_at = now();
    END IF;

    RETURN NEW;
END;
$$;
