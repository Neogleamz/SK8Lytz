import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import { GooglePlacesProvider } from './lib/providers/GooglePlacesProvider';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

export let isGoogleSweepActive = false;

export async function stopGoogleSweep() {
  isGoogleSweepActive = false;
  console.log('🛑 Halting Google Sweep after current batch.');
}

async function sleep(ms: number) {
  return new Promise(r => setTimeout(r, ms));
}

// Convert state abbreviation to full name for better Google Search matches
const STATE_NAMES: Record<string, string> = {
  'AL': 'Alabama', 'AK': 'Alaska', 'AZ': 'Arizona', 'AR': 'Arkansas', 'CA': 'California', 'CO': 'Colorado', 'CT': 'Connecticut', 'DE': 'Delaware', 'FL': 'Florida', 'GA': 'Georgia',
  'HI': 'Hawaii', 'ID': 'Idaho', 'IL': 'Illinois', 'IN': 'Indiana', 'IA': 'Iowa', 'KS': 'Kansas', 'KY': 'Kentucky', 'LA': 'Louisiana', 'ME': 'Maine', 'MD': 'Maryland',
  'MA': 'Massachusetts', 'MI': 'Michigan', 'MN': 'Minnesota', 'MS': 'Mississippi', 'MO': 'Missouri', 'MT': 'Montana', 'NE': 'Nebraska', 'NV': 'Nevada', 'NH': 'New Hampshire', 'NJ': 'New Jersey',
  'NM': 'New Mexico', 'NY': 'New York', 'NC': 'North Carolina', 'ND': 'North Dakota', 'OH': 'Ohio', 'OK': 'Oklahoma', 'OR': 'Oregon', 'PA': 'Pennsylvania', 'RI': 'Rhode Island', 'SC': 'South Carolina',
  'SD': 'South Dakota', 'TN': 'Tennessee', 'TX': 'Texas', 'UT': 'Utah', 'VT': 'Vermont', 'VA': 'Virginia', 'WA': 'Washington', 'WV': 'West Virginia', 'WI': 'Wisconsin', 'WY': 'Wyoming', 'DC': 'Washington DC'
};

const RETAIL_BLOCKLIST = [
  "dick's sporting goods", "academy sports", "play it again sports", 
  "cargo largo", "target", "walmart", "dunham's", "big 5", 
  "scheels", "rei", "bass pro", "cabela", "sportsman"
];

export async function startGoogleSweep(targetStates: string[] = []) {
  if (isGoogleSweepActive) return;
  isGoogleSweepActive = true;
  
  console.log(`🌎 STARTING GOOGLE PLACES GOLDEN SEED 🌎 (States: ${targetStates.join(',') || 'ALL'})`);
  
  const statesToRun = targetStates.length > 0 ? targetStates : Object.keys(STATE_NAMES);

  for (const stateCode of statesToRun) {
    if (!isGoogleSweepActive) {
      console.log('⛔ Google Sweep cleanly aborted.');
      break;
    }

    const stateFull = STATE_NAMES[stateCode] || stateCode;
    console.log(`\n🛹 [US-${stateCode}] Phase 1: Polling Google Places exact match...`);
    
    // Grab highly relevant IDs
    const placeIds = await GooglePlacesProvider.searchRegion(stateFull);
    console.log(`  ✅ [${stateCode}] Found ${placeIds.length} roller-skating relevant Place APIs`);

    for (let i = 0; i < placeIds.length; i++) {
        if (!isGoogleSweepActive) break;
        
        const placeId = placeIds[i];
        console.log(`  ⏳ Fetching High-Fidelity Details: ${placeId} (${i+1}/${placeIds.length})`);
        
        const details = await GooglePlacesProvider.getPlaceDetails(placeId);
        if (details) {
            const lowerName = details.name.toLowerCase();
            if (RETAIL_BLOCKLIST.some(block => lowerName.includes(block))) {
                console.log(`  🚫 Blocklisted Retailer Skipped: ${details.name}`);
                continue;
            }

            // Attempt to derive clean city/state from formatted_address if possible.
            // Formatted address example: "201 W MacArthur Blvd, Oakland, CA 94611, USA"
            const parts = details.formatted_address?.split(',') || [];
            let derivedState: string | undefined = undefined;
            
            if (parts.length >= 3) {
                // Heuristic parsing
                const stateZipStr = parts[parts.length - 2].trim(); // "CA 94611"
                const cityStr = parts[parts.length - 3].trim(); // "Oakland"
                derivedCity = cityStr;
                
                // Usually formatted like "CA 94611" or "TX 75001"
                const splitStateZip = stateZipStr.split(' ');
                if (splitStateZip.length > 0) {
                    derivedState = splitStateZip[0]; // e.g. "CA"
                }
            }
            
            // Fallback back to target state if it entirely failed to parse
            if (!derivedState) derivedState = stateCode;

            // metaRecord: Google-sourced factual data ONLY — no pipeline status.
            // This is used for all UPDATE/upsert-on-conflict paths to ensure we never
            // downgrade an existing MEDIA_READY / VERIFIED record back to ENRICHED.
            const metaRecord = {
                name: details.name,
                lat: details.lat,
                lng: details.lng,
                city: derivedCity,
                state: derivedState,
                street_address: details.formatted_address,
                phone_number: details.formatted_phone_number,
                website: details.website,
                google_place_id: details.place_id,
                rating: details.rating,
                user_ratings_total: details.user_ratings_total,
                opening_hours: details.opening_hours,
                facility_type: 'roller_rink',
                last_enriched_at: new Date().toISOString()
            };

            // freshRecord: used ONLY for brand-new inserts (no existing row found).
            // Sets the initial pipeline status to ENRICHED on first-time ingestion.
            const freshRecord = { ...metaRecord, verification_status: 'ENRICHED' };

            // 1. Check for spatial duplicates (nearby OSM row or prior Google row)
            const { data: closestSpot } = await supabase.rpc('get_closest_skate_spot', {
                p_lat: details.lat,
                p_lng: details.lng,
                p_radius_meters: 150 // 150 meters to be safe
            });

            if (closestSpot && closestSpot.length > 0) {
                // Existing row found nearby — refresh Google metadata, preserve pipeline status
                const existingId = closestSpot[0].spot_id;
                console.log(`  🔗 Found nearby spot (${closestSpot[0].distance_meters.toFixed(1)}m away). Refreshing metadata, preserving status.`);
                const { error } = await supabase.from('skate_spots').update(metaRecord).eq('id', existingId);
                if (error) console.error(`  ❌ Supabase Update Error:`, error.message);
                else console.log(`  💾 Updated Golden Seed: ${details.name}`);
            } else {
                // No spatial match — upsert on google_place_id.
                // On conflict (same place_id): update metadata only (metaRecord, no status).
                // On fresh insert: set initial status to ENRICHED (freshRecord).
                // Supabase upsert with ignoreDuplicates:false updates all provided columns on conflict.
                // We use metaRecord here so a re-seed never resets status on an existing google_place_id row.
                const isNew = !(await supabase.from('skate_spots')
                    .select('id', { count: 'exact', head: true })
                    .eq('google_place_id', details.place_id)
                    .then(r => r.count && r.count > 0));

                const { error } = await supabase.from('skate_spots').upsert(
                    isNew ? freshRecord : metaRecord,
                    { onConflict: 'google_place_id', ignoreDuplicates: false }
                );

                if (error) console.error(`  ❌ Supabase Upsert Error for ${details.name}:`, error.message);
                else console.log(`  💾 ${isNew ? 'NEW' : 'Refreshed'} Golden Seed: ${details.name}`);
            }
        }
        
        // Cooldown between detail queries to avoid QPS spike
        await sleep(500);
    }
  }

  isGoogleSweepActive = false;
  console.log(`\n🏁 USA Google Golden Seed Sweep Complete!`);
}
