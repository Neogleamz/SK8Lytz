import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
dotenv.config();

const supabaseUrl = process.env.VITE_SUPABASE_URL || 'https://nysrtvqlsydvokxdbvty.supabase.co';
const supabaseKey = process.env.VITE_SUPABASE_ANON_KEY;

const supabase = createClient(supabaseUrl, supabaseKey);

async function check() {
  const { data: devs } = await supabase.from('registered_devices').select('id, last_lat, last_long, last_lng');
  console.log('Devices:', devs);
  
  const { data: users } = await supabase.from('user_profiles').select('user_id, last_known_lat, last_known_lng');
  console.log('Users:', users);
}

check();
