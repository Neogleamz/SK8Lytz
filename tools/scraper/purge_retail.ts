import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import * as url from 'url';

const __dirname = url.fileURLToPath(new URL('.', import.meta.url));
dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

async function run() {
  const RETAIL_BLOCKLIST = [
    "dick's sporting goods", "academy sports", "play it again sports", 
    "cargo largo", "target", "walmart", "dunham's", "big 5", 
    "scheels", "rei", "bass pro", "cabela", "sportsman"
  ];
  
  let totalDeleted = 0;
  for(const brand of RETAIL_BLOCKLIST) {
     const { data, error } = await supabase.from('skate_spots').delete().ilike('name', `%${brand}%`).select();
     if (error) {
        console.error('Error deleting', brand, error.message);
     } else {
        if (data && data.length > 0) {
            console.log(`Deleted ${data.length} records matching '${brand}'`);
            totalDeleted += data.length;
        }
     }
  }
  console.log('Total Retail Stores Purged:', totalDeleted);
}
run();
