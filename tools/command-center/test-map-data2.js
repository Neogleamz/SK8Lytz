import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

// Load .env from tools/command-center
dotenv.config({ path: path.resolve(process.cwd(), '.env') });

const supabaseUrl = process.env.VITE_SUPABASE_URL;
const supabaseKey = process.env.VITE_SUPABASE_ANON_KEY;

if (!supabaseUrl || !supabaseKey) {
  console.error("Missing supabase credentials");
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseKey);

async function check() {
  console.log("Checking DB...");
  const { data: dev, error: e1 } = await supabase.from('registered_devices').select('id, user_id, last_lat, last_lng, device_mac').limit(5);
  console.log("Devices Error:", e1?.message);
  console.log("Devices:", JSON.stringify(dev, null, 2));

  const { data: sess, error: e2 } = await supabase.from('crew_sessions').select('id, leader_user_id, location_coords').not('location_coords', 'is', null).limit(5);
  console.log("Sessions Error:", e2?.message);
  console.log("Sessions:", JSON.stringify(sess, null, 2));

  const { data: tele, error: e3 } = await supabase.from('discovered_devices_telemetry').select('device_mac, location').not('location', 'is', null).limit(5);
  console.log("Telemetry Error:", e3?.message);
  console.log("Telemetry:", JSON.stringify(tele, null, 2));
}

check();
