-- Create a secure RPC function to allow users to completely delete their own account and all associated data
-- This is required to comply with Apple App Store and Google Play privacy requirements.

CREATE OR REPLACE FUNCTION public.delete_account()
RETURNS void
LANGUAGE plpgsql
SECURITY DEFINER -- Runs as elevated privileges so it can delete from auth.users
AS $$
BEGIN
  -- Double check the caller is an authenticated user
  IF auth.uid() IS NULL THEN
    RAISE EXCEPTION 'Not authenticated';
  END IF;

  -- Delete the user from auth.users.
  -- Because auth.users is at the root of the dependency tree,
  -- and profiles/crews/telemetry have ON DELETE CASCADE established,
  -- this single DELETE statement will cascade through the entire database
  -- erasing all traces of the user automatically.
  DELETE FROM auth.users WHERE id = auth.uid();
END;
$$;
