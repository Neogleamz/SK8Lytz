import { createClient } from '@supabase/supabase-js'; import { readFileSync } from 'fs';
const {SUPABASE_URL, SUPABASE_ANON_KEY} = JSON.parse(readFileSync('src/services/config.json', 'utf8') || '{}');
console.log(SUPABASE_URL);
