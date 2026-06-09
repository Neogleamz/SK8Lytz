const { Client } = require('pg');
require('dotenv').config();

async function run() {
  const connectionString = `postgresql://postgres:${process.env.SUPABASE_DB_PASSWORD}@db.qefmeivpjyaukbwadgaz.supabase.co:5432/postgres`;
  
  const client = new Client({ connectionString });
  
  try {
    await client.connect();
    console.log("Connected to Supabase PostgreSQL.");

    const query = `
      -- 1. Create a helper function to check if the current user is an admin
      -- This uses a security definer function to avoid infinite recursion if user_profiles also has RLS
      CREATE OR REPLACE FUNCTION public.is_admin()
      RETURNS boolean AS $$
      DECLARE
        _role text;
      BEGIN
        SELECT role INTO _role FROM public.user_profiles WHERE user_id = auth.uid() LIMIT 1;
        RETURN _role = 'admin';
      END;
      $$ LANGUAGE plpgsql SECURITY DEFINER;

      -- 2. Update registered_devices SELECT policy
      DROP POLICY IF EXISTS "devices_owner_select" ON public.registered_devices;
      CREATE POLICY "devices_owner_select" ON public.registered_devices
      FOR SELECT TO authenticated
      USING (user_id = auth.uid() OR public.is_admin());

      -- 3. Update crew_sessions SELECT policy (assuming it has RLS)
      DROP POLICY IF EXISTS "crew_sessions_select" ON public.crew_sessions;
      DROP POLICY IF EXISTS "crew_owner_select" ON public.crew_sessions;
      -- Best effort: recreate as permissive for admins
      CREATE POLICY "crew_owner_select" ON public.crew_sessions
      FOR SELECT TO authenticated
      USING (leader_user_id = auth.uid() OR public.is_admin());

      -- 4. Update skate_sessions SELECT policy
      DROP POLICY IF EXISTS "skate_owner_select" ON public.skate_sessions;
      CREATE POLICY "skate_owner_select" ON public.skate_sessions
      FOR SELECT TO authenticated
      USING (user_id = auth.uid() OR public.is_admin());
    `;

    await client.query(query);
    console.log("RLS policies updated successfully to allow Admins full SELECT access!");
  } catch (err) {
    console.error("Error updating RLS:", err);
  } finally {
    await client.end();
  }
}

run();
