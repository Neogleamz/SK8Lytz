
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
   const { data, error } = await supabase.from('skate_spots').select('name').ilike('name', '%sporting%');
   console.log(data);
}
run();

