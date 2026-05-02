import dotenv from 'dotenv';
import path from 'path';
import { GooglePlacesProvider, FacilityType, RETAIL_BLOCKLIST, injectDynamicBlocklist } from './lib/providers/GooglePlacesProvider';
import { db, getClosestLocalSpot, updateLocalSpot, upsertLocalSpot, getBlocklistKeywords, getBlocklist } from './core/LocalDB';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

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
    const dbKeywords = getBlocklistKeywords();
    const dbPatterns = getBlocklist();
    const combined = [
      ...dbKeywords.map((k: any) => k.keyword),
      ...dbPatterns.map((p: any) => p.pattern)
    ];
    if (combined.length > 0) {
      injectDynamicBlocklist(combined);
    }
  } catch (err) {
    console.error('⚠️ Failed to fetch dynamic blocklist from LocalDB:', err);
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

        // Parse city/state/zip from formatted_address
        // e.g. "201 W MacArthur Blvd, Oakland, CA 94611, USA"
        const parts = details.formatted_address?.split(',') || [];
        let derivedState: string | undefined;
        let derivedCity: string | undefined;
        let derivedZip: string | undefined;

        // Reliable US Zip Regex extraction
        if (details.formatted_address) {
          const match = details.formatted_address.match(/\b([A-Z]{2})\s+(\d{5}(?:-\d{4})?)(?:,\s*USA)?\s*$/i);
          if (match) {
            derivedState = match[1];
            derivedZip = match[2];
          }
        }

        if (parts.length >= 3) {
          derivedCity = parts[parts.length - 3].trim();       // "Oakland"
          if (!derivedState) {
            const stateZipStr = parts[parts.length - 2].trim(); // "CA 94611"
            const splitStateZip = stateZipStr.split(' ');
            if (splitStateZip.length > 0) derivedState = splitStateZip[0]; // "CA"
          }
        }
        if (!derivedState) derivedState = stateCode; // fallback

        // ── Phase 1 Quality Gate ──────────────────────────────────────────────────
        // HARD REJECT: Permanently closed venues are dead data — never seed them.
        // NOTE: google's `url` field = their Maps page URL, NOT the venue's own website.
        if (details.business_status === 'CLOSED_PERMANENTLY') {
          console.log(`  🚫 REJECTED [CLOSED_PERMANENTLY]: ${details.name}`);
          continue;
        }
        // HARD REJECT: Must have name + coordinates + full address.
        if (!details.name || !details.lat || !details.lng || !details.formatted_address) {
          console.log(`  🚫 SEEDED REJECTED: Missing name/coords/address — ${details.place_id}`);
          continue;
        }
        // SOFT WARN: Missing phone — still seeds but flagged
        if (!details.formatted_phone_number) {
          console.log(`  ⚠️  LOW_DATA: No phone number — ${details.name}`);
        }
        // NOTE: No website is OK — Phase 2 (Spider) handles social-only path

        // Build candidate_photos from Google photo references
        const googlePhotos = details.photos && details.photos.length > 0
          ? { google_refs: details.photos }
          : null;

        // metaRecord: ALL factual Google data stored in proper columns — no JSONB burial.
        // Updated on every refresh so fields stay current without downgrading pipeline status.
        const metaRecord = {
          name:                 details.name,
          lat:                  details.lat,
          lng:                  details.lng,
          city:                 derivedCity,
          state:                derivedState,
          zip:                  derivedZip,
          street_address:       details.formatted_address,
          phone_number:         details.formatted_phone_number  || null,
          website:              details.website                 || null,   // venue's own website
          google_place_id:      details.place_id,
          google_maps_url:      details.google_maps_url         || null,   // Google Maps page URL (different from website)
          business_status:      details.business_status         || 'OPERATIONAL',
          rating:               details.rating                  ?? null,
          user_ratings_total:   details.user_ratings_total      ?? null,
          opening_hours:        details.opening_hours           || null,
          operator_description: details.editorial_summary       || null,
          facility_type:        facilityType,
          last_enriched_at:     new Date().toISOString(),
          ...(googlePhotos ? { candidate_photos: googlePhotos } : {}),
          // raw_knowledge_panel: place type tags from Google (no dedicated column)
          raw_knowledge_panel: { types: details.types || null }
        };

        // freshRecord: only for brand-new inserts — Scout outputs SEEDED status
        const freshRecord = { ...metaRecord, verification_status: 'SEEDED' };

        // 1. Spatial dedup (150m radius) — catches nearby rows regardless of google_place_id
        const closestSpot = getClosestLocalSpot(details.lat, details.lng, 150);

        if (closestSpot && closestSpot.length > 0) {
          // Existing nearby row — refresh metadata, preserve pipeline status
          const existingId = closestSpot[0].spot_id;
          console.log(`  🔗 Nearby match (${closestSpot[0].distance_meters.toFixed(1)}m) — refreshing metadata.`);
          try {
            updateLocalSpot(existingId, metaRecord);
            console.log(`  💾 Refreshed: ${details.name}`);
          } catch (error: any) {
            console.error(`  ❌ Update Error:`, error.message);
          }
        } else {
          // 2. Upsert on google_place_id — new rows get SEEDED, existing get metadata refresh
          const existing = db.prepare('SELECT id FROM local_spots WHERE google_place_id = ?').get(details.place_id) as any;
          const isNew = !existing;

          try {
            if (isNew) {
               upsertLocalSpot(freshRecord);
            } else {
               updateLocalSpot(existing.id, metaRecord);
            }
            console.log(`  💾 ${isNew ? 'NEW' : 'Refreshed'} [${facilityType}]: ${details.name}`);
          } catch (error: any) {
            console.error(`  ❌ Upsert Error for ${details.name}:`, error.message);
          }
        }

        await sleep(50); // QPS cooldown between detail fetches
      }
    }
    // ---- End facility type loop ----
  }

  isGoogleSweepActive = false;
  console.log(`\n🏁 Google Places Sweep Complete! (${facilitiesToRun.join(', ')}, ${statesToRun.length} states)`);
}
