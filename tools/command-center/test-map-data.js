import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
dotenv.config();

const supabaseUrl = process.env.VITE_SUPABASE_URL || 'https://xxx.supabase.co';
const supabaseKey = process.env.VITE_SUPABASE_ANON_KEY || 'xxx';

const supabase = createClient(supabaseUrl, supabaseKey);

async function check() {
  const { data: devices, error: devError } = await supabase.from('registered_devices').select('id, user_id, last_lat, last_lng, device_mac');
  console.log("Devices:", devices?.slice(0, 5));

  const { data: sessions } = await supabase.from('crew_sessions').select('id, leader_user_id, location_coords').not('location_coords', 'is', null).limit(5);
  console.log("Sessions with location:", sessions);

  const { data: tele } = await supabase.from('discovered_devices_telemetry').select('device_mac, location').not('location', 'is', null).limit(5);
  console.log("Telemetry with location:", tele);
}

check();
