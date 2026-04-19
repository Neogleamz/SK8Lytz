import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '',
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

async function run() {
  console.log('Purging skateparks...');
  const { data, error } = await supabase
    .from('skate_spots')
    .delete()
    .in('facility_type', ['skatepark', 'hybrid']);
    
  if (error) {
    console.error('Delete error:', error);
  } else {
    console.log('Success deleting skateparks.');
  }

  const { error: confErr } = await supabase
    .from('scraper_config')
    .update({ target_facilities: ['roller_rink', 'pro_shop'] })
    .eq('id', 1);

  if (confErr) {
    console.error('Config update error:', confErr);
  } else {
    console.log('Success updating target_facilities config.');
  }
}

run();
