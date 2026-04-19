import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

async function inject() {
  console.log("Injecting config row...");
  const { data, error } = await supabase.from('scraper_config').upsert({
    id: 1,
    state_override: [],
    target_facilities: ['skatepark', 'roller_rink', 'pro_shop'],
    sleep_interval_ms: 10000
  });
  console.log("Result:", error?.message || 'Success');
}
inject();
