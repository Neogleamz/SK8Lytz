-- Nuclear Option: Purge Users for Testing
-- This script deletes specific users to start clean, explicitly cleaning up all their data.

DO $$
DECLARE
    user_email_1 TEXT := 'magma@example.com'; -- Replace with actual email
    user_email_2 TEXT := 'andyledford.al@example.com'; -- Replace with actual email
    uid UUID;
BEGIN
    FOR uid IN SELECT id FROM auth.users WHERE email ILIKE '%' || user_email_1 || '%' OR email ILIKE '%' || user_email_2 || '%' LOOP
        -- Explicitly delete child records to guarantee no orphaned "crap"
        -- and to prevent Foreign Key constraint errors if CASCADE is missing.
        DELETE FROM public.crew_members WHERE user_id = uid;
        DELETE FROM public.crews WHERE owner_id = uid;
        DELETE FROM public.skate_sessions WHERE user_id = uid;
        DELETE FROM public.crew_sessions WHERE leader_id = uid;
        DELETE FROM public.registered_devices WHERE owner_id = uid;
        DELETE FROM public.scenes WHERE user_id = uid;
        
        -- Delete the profile
        DELETE FROM public.profiles WHERE id = uid;
        
        -- Finally delete the auth user
        DELETE FROM auth.users WHERE id = uid;
    END LOOP;
END $$;

-- If you truly want to wipe EVERYONE and start from 0:
/*
TRUNCATE TABLE public.profiles CASCADE;
DELETE FROM auth.users;
*/
