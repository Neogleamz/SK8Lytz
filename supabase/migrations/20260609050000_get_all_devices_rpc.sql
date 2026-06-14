-- RPC to allow Admins to bypass RLS and fetch all registered devices for the Command Center
CREATE OR REPLACE FUNCTION get_all_registered_devices()
RETURNS SETOF registered_devices
LANGUAGE sql
SECURITY DEFINER
SET search_path = public
AS $$
  -- Only allow if the user is an admin
  SELECT * FROM registered_devices
  WHERE EXISTS (
    SELECT 1 FROM public.user_profiles
    WHERE user_id = auth.uid() AND role = 'admin'
  );
$$;
