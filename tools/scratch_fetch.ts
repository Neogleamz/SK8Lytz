import { createClient } from '@supabase/supabase-js';
import * as path from 'path';
const supabaseUrl = process.env.VITE_SUPABASE_URL || process.env.SUPABASE_URL;
const supabaseKey = process.env.VITE_SUPABASE_ANON_KEY || process.env.SUPABASE_ANON_KEY;

if (!supabaseUrl || !supabaseKey) {
  console.log('Missing env vars');
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseKey);

async function check() {
  try {
    console.log("Checking registered_devices...");
    const { data, error } = await supabase.from('registered_devices').select('*');
    console.log("registered_devices count:", data?.length, "Error:", error);

    console.log("Checking discovered_devices_telemetry...");
    const { data: data2, error: error2 } = await supabase.from('discovered_devices_telemetry').select('*');
    console.log("discovered count:", data2?.length, "Error:", error2);
  } catch (err) {
    console.error("Fetch failed:", err);
  }
}

check();
