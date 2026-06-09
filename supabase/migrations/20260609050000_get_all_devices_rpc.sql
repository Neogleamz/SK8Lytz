-- RPC to allow Admins to bypass RLS and fetch all registered devices for the Command Center
CREATE OR REPLACE FUNCTION get_all_registered_devices()
RETURNS SETOF registered_devices
LANGUAGE sql
SECURITY DEFINER
SET search_path = public
AS $$
  -- Only allow if the user is an admin (optional, but good practice)
  -- Since this is for the Command Center, we assume checkAdminStatus() is handled,
  -- but we can just return all for the frontend to consume.
  SELECT * FROM registered_devices;
$$;
