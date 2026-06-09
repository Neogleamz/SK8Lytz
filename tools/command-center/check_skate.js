import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

// Load .env from the root SK8Lytz folder
dotenv.config({ path: path.resolve(process.cwd(), '../.env') });

const supabaseUrl = process.env.VITE_SUPABASE_URL || process.env.SUPABASE_URL || 'https://nysrtvqlsydvokxdbvty.supabase.co';
const supabaseKey = process.env.VITE_SUPABASE_ANON_KEY || process.env.SUPABASE_ANON_KEY;

if (!supabaseKey) {
  console.log("No supabase key found!");
  process.exit(1);
}

const supabase = createClient(supabaseUrl, supabaseKey);

async function check() {
  const { data: sess } = await supabase.from('skate_sessions').select('id, location_coords');
  let validCount = 0;
  
  if (!sess) {
    console.log("No sessions found or query failed.");
    return;
  }

  sess.forEach(s => {
    let isValid = false;
    if (s.location_coords) {
       // Try parsing
       try {
           const parsed = typeof s.location_coords === 'string' ? JSON.parse(s.location_coords) : s.location_coords;
           if (parsed && typeof parsed.lat !== 'undefined' && typeof parsed.lng !== 'undefined') {
               isValid = true;
           } else if (parsed && typeof parsed.latitude !== 'undefined' && typeof parsed.longitude !== 'undefined') {
               isValid = true;
           }
       } catch (e) {
           // parseStringLocation fallback
           const str = s.location_coords.toString().trim();
           if (str.includes(',')) {
               const parts = str.split(',');
               if (parts.length >= 2 && !isNaN(parseFloat(parts[0])) && !isNaN(parseFloat(parts[1]))) {
                   isValid = true;
               }
           }
       }
    }
    if (isValid) validCount++;
  });
  console.log(`Total sessions: ${sess.length}`);
  console.log(`Sessions with valid coordinates: ${validCount}`);
  console.log(`First 3 coordinates:`);
  console.log(sess.slice(0, 3).map(s => s.location_coords));
}

check();
