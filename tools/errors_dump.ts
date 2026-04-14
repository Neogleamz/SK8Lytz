import { createClient } from '@supabase/supabase-js';
import * as dotenv from 'dotenv';
dotenv.config();

const url = process.env.EXPO_PUBLIC_SUPABASE_URL || '';
const key = process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '';

if (!url || !key) {
  console.error("Missing URL or KEY in .env");
  process.exit(1);
}

const supabase = createClient(url, key);

async function dump() {
  const { data, error } = await supabase
    .from('telemetry_errors')
    .select('*')
    .order('created_at', { ascending: false })
    .limit(3);

  if (error) {
    console.error("Supabase Error:", error);
  } else {
    console.log(JSON.stringify(data, null, 2));
  }
}

dump();
