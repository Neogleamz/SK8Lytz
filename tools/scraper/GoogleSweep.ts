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
            // Attempt to derive clean city/state from formatted_address if possible.
            // Formatted address example: "201 W MacArthur Blvd, Oakland, CA 94611, USA"
            const parts = details.formatted_address?.split(',') || [];
            let derivedCity = "Unknown";
            let derivedState: string | undefined = stateCode;
            
            if (parts.length >= 3) {
                // Heuristic parsing
                const stateZipStr = parts[parts.length - 2].trim(); // "CA 94611"
                const cityStr = parts[parts.length - 3].trim(); // "Oakland"
                derivedCity = cityStr;
            }

            const record = {
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
                verification_status: 'ENRICHED', // Golden Seed is implicitly higher trust
                last_enriched_at: new Date().toISOString()
            };

            // 1. Check for spatial duplicates (e.g. from OSM Phase 1)
            const { data: closestSpot } = await supabase.rpc('get_closest_skate_spot', {
                p_lat: details.lat,
                p_lng: details.lng,
                p_radius_meters: 150 // 150 meters to be safe
            });

            if (closestSpot && closestSpot.length > 0) {
                const existingId = closestSpot[0].spot_id;
                console.log(`  🔗 Found nearby Phase 1 spot (${closestSpot[0].distance_meters.toFixed(1)}m away). Upgrading row to ENRICHED.`);
                const { error } = await supabase.from('skate_spots').update(record).eq('id', existingId);
                if (error) console.error(`  ❌ Supabase Update Error:`, error.message);
                else console.log(`  💾 Updated Golden Seed: ${details.name}`);
            } else {
                // If it's brand new, or already created by Google with the same google_place_id
                const { error } = await supabase.from('skate_spots').upsert(record, { 
                    onConflict: 'google_place_id', 
                    ignoreDuplicates: false 
                });

                if (error) console.error(`  ❌ Supabase Upsert Error for ${details.name}:`, error.message);
                else console.log(`  💾 Saved NEW Golden Seed: ${details.name}`);
            }
        }
        
        // Cooldown between detail queries to avoid QPS spike
        await sleep(500);
    }
  }

  isGoogleSweepActive = false;
  console.log(`\n🏁 USA Google Golden Seed Sweep Complete!`);
}
