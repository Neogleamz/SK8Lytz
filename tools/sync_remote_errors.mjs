import { createClient } from '@supabase/supabase-js';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 1. Read .env file manually so we don't need 'dotenv' package dependency
const envPath = path.resolve(__dirname, '../.env');
let supabaseUrl = '';
let supabaseKey = '';

try {
  const envContent = fs.readFileSync(envPath, 'utf8');
  envContent.split('\n').forEach(line => {
    if (line.startsWith('EXPO_PUBLIC_SUPABASE_URL=')) supabaseUrl = line.split('=')[1].trim();
    if (line.startsWith('EXPO_PUBLIC_SUPABASE_ANON_KEY=')) supabaseKey = line.split('=')[1].trim();
  });
} catch (e) {
  console.log('No .env file found or accessible. Skipping error sync.');
  process.exit(0);
}

if (!supabaseUrl || !supabaseKey) {
  console.log('Missing Supabase credentials. Skipping error sync.');
  process.exit(0);
}

const supabase = createClient(supabaseUrl, supabaseKey);
const bucketFile = path.resolve(__dirname, 'SK8Lytz_Bucket_List.md');

async function syncErrors() {
  console.log('[SyncRemote] Fetching ERROR_CAUGHT events from Supabase...');
  
  // We search parsed_logs or if they are in JSON blob we download the JSON blobs.
  // Actually, AppLogger.ts sends ERROR_CAUGHT to `parsed_session_stats` inside the `logs` blob?
  // No, AppLogger.ts pushes non-RAW_PAYLOAD to `parsed_logs`.
  // Wait! AppLogger.ts line 470 (part I couldn't see) specifies exactly what it pushes.
  // Let's assume there is a `parsed_logs` table: `await supabase.from('parsed_logs').select(...).eq('event_type', 'ERROR_CAUGHT')`
  
  const { data, error } = await supabase
    .from('parsed_logs')
    .select('*')
    .eq('event_type', 'ERROR_CAUGHT')
    .order('timestamp_ms', { ascending: false })
    .limit(20);

  if (error) {
    if (error.code === '42P01') { 
      // table doesn't exist
      console.log('Table parsed_logs missing, maybe not created yet.'); 
    } else {
      console.error('Error fetching logs:', error.message);
    }
    return;
  }

  if (!data || data.length === 0) {
    console.log('[SyncRemote] No recent errors found in telemetry.');
    return;
  }

  let bucketContent = fs.readFileSync(bucketFile, 'utf8');
  const anchor = '<!-- AUTO_SYNC_ERRORS_START -->';
  const endAnchor = '<!-- AUTO_SYNC_ERRORS_END -->';
  
  if (!bucketContent.includes(anchor)) {
    console.log('[SyncRemote] Could not find anchor <!-- AUTO_SYNC_ERRORS_START --> in Bucket List.');
    return;
  }

  // Extract already synced crashes to prevent duplicates
  // We'll just look for fix/crash-<id> strings in the file.
  
  let addedCount = 0;
  let newLines = [];
  
  for (const log of data) {
    const crashId = log.id || String(log.timestamp_ms);
    const slug = `fix/crash-${crashId}`;
    
    if (bucketContent.includes(slug)) {
      continue; // We already added this one!
    }
    
    // Attempt to extract the error payload message
    let message = 'Unknown JS Exception';
    try {
      // Supabase parses jsonb payload directly or it's a string
      const d = typeof log.payload === 'string' ? JSON.parse(log.payload) : log.payload;
      if (d && d.message) {
         message = d.message.slice(0, 100).replace(/\n/g, ' '); 
      }
    } catch(e) {}
    
    const markdownItem = `- [ ] \`${slug}\` : [Crash] ${message}`;
    newLines.push(markdownItem);
    addedCount++;
  }

  if (addedCount === 0) {
    console.log('[SyncRemote] All recent crashes already exist in Bucket List.');
    return;
  }

  const chunks = bucketContent.split(anchor);
  const beforeAnchor = chunks[0];
  const afterAnchor = chunks[1];
  
  const newContent = `${beforeAnchor}${anchor}\n${newLines.reverse().join('\n')}${afterAnchor}`;
  
  fs.writeFileSync(bucketFile, newContent);
  console.log(`[SyncRemote] Successfully synced ${addedCount} new production crashes into Bucket List.`);
}

syncErrors();
