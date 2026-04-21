import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import { GooglePlacesProvider, FacilityType, RETAIL_BLOCKLIST, injectDynamicBlocklist } from './lib/providers/GooglePlacesProvider';

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
  'AL': 'Alabama', 'AK': 'Alaska', 'AZ': 'Arizona', 'AR': 'Arkansas', 'CA': 'California',
  'CO': 'Colorado', 'CT': 'Connecticut', 'DE': 'Delaware', 'FL': 'Florida', 'GA': 'Georgia',
  'HI': 'Hawaii', 'ID': 'Idaho', 'IL': 'Illinois', 'IN': 'Indiana', 'IA': 'Iowa',
  'KS': 'Kansas', 'KY': 'Kentucky', 'LA': 'Louisiana', 'ME': 'Maine', 'MD': 'Maryland',
  'MA': 'Massachusetts', 'MI': 'Michigan', 'MN': 'Minnesota', 'MS': 'Mississippi', 'MO': 'Missouri',
  'MT': 'Montana', 'NE': 'Nebraska', 'NV': 'Nevada', 'NH': 'New Hampshire', 'NJ': 'New Jersey',
  'NM': 'New Mexico', 'NY': 'New York', 'NC': 'North Carolina', 'ND': 'North Dakota', 'OH': 'Ohio',
  'OK': 'Oklahoma', 'OR': 'Oregon', 'PA': 'Pennsylvania', 'RI': 'Rhode Island', 'SC': 'South Carolina',
  'SD': 'South Dakota', 'TN': 'Tennessee', 'TX': 'Texas', 'UT': 'Utah', 'VT': 'Vermont',
  'VA': 'Virginia', 'WA': 'Washington', 'WV': 'West Virginia', 'WI': 'Wisconsin', 'WY': 'Wyoming',
  'DC': 'Washington DC'
};

// RETAIL_BLOCKLIST is imported from GooglePlacesProvider — single source of truth.

/**
 * Sweeps Google Places for the given facility types and states.
 * @param targetStates     2-letter state codes. Empty = all states.
 * @param targetFacilities Facility types to sweep. Empty = roller_rink only.
 */
export async function startGoogleSweep(
  targetStates: string[] = [],
  targetFacilities: string[] = []
) {
  if (isGoogleSweepActive) return;
  isGoogleSweepActive = true;

  // Resolve which facility types to sweep. Default: roller_rink only.
  const ALL_FACILITY_TYPES: FacilityType[] = ['roller_rink', 'skate_shop', 'skate_park'];
  const facilitiesToRun: FacilityType[] = targetFacilities.length > 0
    ? ALL_FACILITY_TYPES.filter(ft => targetFacilities.includes(ft))
    : ['roller_rink'];

  // --- Dynamic Blocklist Injection ---
  try {
    const { data: dbKeywords } = await supabase.from('scraper_blocklist_keywords').select('keyword');
    if (dbKeywords && dbKeywords.length > 0) {
      injectDynamicBlocklist(dbKeywords.map(k => k.keyword));
    }
  } catch (err) {
    console.error('⚠️ Failed to fetch dynamic blocklist from Supabase:', err);
  }

  const statesToRun = targetStates.length > 0 ? targetStates : Object.keys(STATE_NAMES);

  console.log(`\n🌐 GOOGLE PLACES SWEEP STARTING`);
  console.log(`   Facilities : ${facilitiesToRun.join(', ')}`);
  console.log(`   States     : ${statesToRun.join(', ') || 'ALL (${statesToRun.length})'}\n`);

  for (const stateCode of statesToRun) {
    if (!isGoogleSweepActive) {
      console.log('⛔ Google Sweep cleanly aborted.');
      break;
    }

    const stateFull = STATE_NAMES[stateCode] || stateCode;

    // ---- Iterate over each facility type for this state ----
    for (const facilityType of facilitiesToRun) {
      if (!isGoogleSweepActive) break;

      console.log(`\n📡 [${stateCode}] Sweeping: ${facilityType}`);

      const placeIds = await GooglePlacesProvider.searchRegion(stateFull, facilityType);
      console.log(`  ✅ [${stateCode}/${facilityType}] Found ${placeIds.length} candidates`);

      for (let i = 0; i < placeIds.length; i++) {
        if (!isGoogleSweepActive) break;

        const placeId = placeIds[i];
        console.log(`  ⏳ Fetching details: ${placeId} (${i + 1}/${placeIds.length})`);

        const details = await GooglePlacesProvider.getPlaceDetails(placeId);
        if (!details) continue;

        // Retail blocklist check
        const lowerName = details.name.toLowerCase();
        if (RETAIL_BLOCKLIST.some(block => lowerName.includes(block))) {
          console.log(`  🚫 Blocked: ${details.name}`);
          continue;
        }

        // Parse city/state from formatted_address
        // e.g. "201 W MacArthur Blvd, Oakland, CA 94611, USA"
        const parts = details.formatted_address?.split(',') || [];
        let derivedState: string | undefined;
        let derivedCity: string | undefined;

        if (parts.length >= 3) {
          const stateZipStr = parts[parts.length - 2].trim(); // "CA 94611"
          derivedCity = parts[parts.length - 3].trim();       // "Oakland"
          const splitStateZip = stateZipStr.split(' ');
          if (splitStateZip.length > 0) derivedState = splitStateZip[0]; // "CA"
        }
        if (!derivedState) derivedState = stateCode; // fallback

        // metaRecord: factual Google data ONLY — no pipeline status.
        // Used on all update/conflict paths so MEDIA_READY is never downgraded to ENRICHED.
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
          facility_type: facilityType,           // ← correct per-type value
          last_enriched_at: new Date().toISOString()
        };

        // freshRecord: only for brand-new inserts — adds initial ENRICHED status
        const freshRecord = { ...metaRecord, verification_status: 'ENRICHED' };

        // 1. Spatial dedup (150m radius) — catches nearby rows regardless of google_place_id
        const { data: closestSpot } = await supabase.rpc('get_closest_skate_spot', {
          p_lat: details.lat,
          p_lng: details.lng,
          p_radius_meters: 150
        });

        if (closestSpot && closestSpot.length > 0) {
          // Existing nearby row — refresh metadata, preserve pipeline status
          const existingId = closestSpot[0].spot_id;
          console.log(`  🔗 Nearby match (${closestSpot[0].distance_meters.toFixed(1)}m) — refreshing metadata.`);
          const { error } = await supabase.from('skate_spots').update(metaRecord).eq('id', existingId);
          if (error) console.error(`  ❌ Update Error:`, error.message);
          else console.log(`  💾 Refreshed: ${details.name}`);
        } else {
          // 2. Upsert on google_place_id — new rows get ENRICHED, existing get metadata refresh
          const isNew = !(await supabase.from('skate_spots')
            .select('id', { count: 'exact', head: true })
            .eq('google_place_id', details.place_id)
            .then(r => r.count && r.count > 0));

          const { error } = await supabase.from('skate_spots').upsert(
            isNew ? freshRecord : metaRecord,
            { onConflict: 'google_place_id', ignoreDuplicates: false }
          );

          if (error) console.error(`  ❌ Upsert Error for ${details.name}:`, error.message);
          else console.log(`  💾 ${isNew ? 'NEW' : 'Refreshed'} [${facilityType}]: ${details.name}`);
        }

        await sleep(500); // QPS cooldown between detail fetches
      }
    }
    // ---- End facility type loop ----
  }

  isGoogleSweepActive = false;
  console.log(`\n🏁 Google Places Sweep Complete! (${facilitiesToRun.join(', ')}, ${statesToRun.length} states)`);
}
