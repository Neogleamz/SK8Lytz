import dotenv from 'dotenv';
import path from 'path';
import { createClient } from '@supabase/supabase-js';
import { scrapeCulturalDetails } from './lib/GoogleScraper';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

// Sleep for 4 minutes between queries to stay extremely stealthy
const SLEEP_INTERVAL_MS = 4 * 60 * 1000; 

async function sleep(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function runEnrichmentCycle() {
  console.log(`\n🔍 [${new Date().toISOString()}] Waking up Enrichment Daemon...`);

  // 1. Fetch next spot using our priority queue RPC
  const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_to_enrich');
  
  if (rpcError) {
    console.error('❌ Failed to fetch next spot from Queue:', rpcError.message);
    await sleep(60000); // Back off 1 minute on DB failure
    return; 
  }

  if (!spots || spots.length === 0) {
    console.log('🏁 No spots need enrichment right now! Sleeping for a long cycle (30m)...');
    await sleep(30 * 60 * 1000);
    return;
  }

  const targetSpot = spots[0];
  console.log(`🎯 Targeted: [${targetSpot.facility_type?.toUpperCase() || 'UNKNOWN'}] ${targetSpot.name} in ${targetSpot.city || 'Unknown'}, ${targetSpot.state || 'Unknown'}`);

  // 2. Perform the heavy/slow Puppeteer Scrape
  console.log(`   🎭 Launching Puppeteer Scraper...`);
  const culturalData = await scrapeCulturalDetails(targetSpot.name, targetSpot.city || '');
  
  // 3. Update the database record
  console.log(`   💾 Merging enriched data into Supabase...`);
  const { error: updateError } = await supabase
    .from('skate_spots')
    .update({
      has_pro_shop: targetSpot.has_pro_shop || culturalData.has_pro_shop, // Keep existing mapping if already true
      has_adult_night: culturalData.has_adult_night,
      vibe_rating: culturalData.vibe_rating,
      socials: culturalData.socials,
      website: targetSpot.website || culturalData.fetched_website,
      last_enriched_at: new Date().toISOString()
    })
    .eq('id', targetSpot.id);

  if (updateError) {
    console.error(`❌ Failed to update ${targetSpot.name}:`, updateError.message);
  } else {
    console.log(`✅ Enrichment saved! Found Rating: ${culturalData.vibe_rating || 'N/A'}, Adult Night: ${culturalData.has_adult_night}`);
  }

  // 4. Sleep to prevent bans
  console.log(`😴 Cycle complete. Sleeping for ${SLEEP_INTERVAL_MS / 1000 / 60} minutes...`);
  await sleep(SLEEP_INTERVAL_MS);
}

async function startDaemon() {
  console.log('🤖 Starting SK8Lytz Cultural Enrichment Daemon');
  console.log(`⚙️  Configured Poll Interval: ${SLEEP_INTERVAL_MS / 1000 / 60}m per query`);
  
  while (true) {
    await runEnrichmentCycle();
  }
}

startDaemon();
