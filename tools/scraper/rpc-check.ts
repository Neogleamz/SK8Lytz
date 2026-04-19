import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

async function check() {
  console.log("Checking RPC result...");
  const { data, error } = await supabase.rpc('get_next_spot_to_enrich');
  console.log("RPC Data:", data);
  console.log("RPC Error:", error);
}
check();
