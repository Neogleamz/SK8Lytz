import axios from 'axios';
import fs from 'fs';
import path from 'path';
import dotenv from 'dotenv';
import { createClient } from '@supabase/supabase-js';

import { buildHoursJSON } from './lib/OSMHoursParser';
import { reverseGeocode } from './lib/NominatimAPI';
import { GHOST } from './lib/GHOST';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(process.env.EXPO_PUBLIC_SUPABASE_URL || '', process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '');

const OVERPASS_URL = 'https://overpass-api.de/api/interpreter';
const CACHE_DIR = path.join(__dirname, 'state_caches');
if (!fs.existsSync(CACHE_DIR)) fs.mkdirSync(CACHE_DIR);

export const US_STATES = [
  'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA',
  'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
  'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
  'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
  'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY', 'DC'
];

async function sleep(ms: number) {
  return new Promise(r => setTimeout(r, ms));
}

const reportPulse = (delayMs: number, ghost?: any) => {
    fetch('http://localhost:5999/api/pulse', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify({ source: 'Phase 1', delayMs, ghost })
    }).catch(() => {});
 };

export let isHarvestingActive = false;

export async function stopNationalHarvester() {
  isHarvestingActive = false;
  console.log('🛑 Master Harvester signal received: Halting pipeline after current cycle.');
}

export async function startNationalHarvester(targetFacilities: string[] = [], targetStates: string[] = []) {
  if (isHarvestingActive) return;
  isHarvestingActive = true;
  console.log(`🌎 STARTING VERTICAL PIPELINE HARVEST 🌎 (States: ${targetStates.join(',') || 'ALL'})`);
  const statesToRun = targetStates.length > 0 ? targetStates : US_STATES;
  
  let statesProcessed = 0;
  for (let i = 0; i < statesToRun.length; i++) {
     if (!isHarvestingActive) {
       console.log('⛔ Harvester pipeline cleanly aborted by C&C signal.');
       break;
     }

     // Report starting pulse
     reportPulse(0, GHOST.generateIdentity());

     const didWork = await processState(statesToRun[i], targetFacilities);
     if (didWork) statesProcessed++;

     if (i < statesToRun.length - 1 && isHarvestingActive) {
         const waitTime = await GHOST.getAdaptiveDelay('OSM');
         console.log(`⏳ Global GHOST: Cooling down OSM limits (${Math.round(waitTime/1000)}s)...`);
         
         const identity = GHOST.generateIdentity();
         reportPulse(waitTime, identity);
         await sleep(waitTime);
     }
  }
  if (isHarvestingActive) {
    if (statesProcessed > 0) {
      console.log(`\n🏁 USA Dataset Sweep Complete! ${statesProcessed} states updated.`);
    } else {
      console.log(`\n🏁 USA Dataset is up to date.`);
    }
    isHarvestingActive = false;
  }
}

export async function processState(stateCode: string, targetFacilities: string[] = [], forceResult: boolean = false): Promise<boolean> {
  const stateCachePath = path.join(CACHE_DIR, `${stateCode}.json`);
  if (fs.existsSync(stateCachePath) && !forceResult) {
    return false;
  }

  const identity = GHOST.generateIdentity();
  console.log(`\n🛹 [US-${stateCode}] Phase 1: Contacting OpenStreetMap Overpass Servers...`);
  console.log(`    🆔 Identity: ${identity.userAgent.slice(0, 45)}... [Global GHOST]`);

  const includeAll = targetFacilities.length === 0;
  let conditions = '';
  if (includeAll || targetFacilities.includes('roller_rink')) {
    conditions += `    nwr["sport"="roller_skating"](area.searchArea);\n`;
    // New Wide-Net Heuristics for under-tagged rinks (like Winnwood)
    conditions += `    nwr["leisure"="sports_centre"]["name"~"Skate|Rink|Roll",i](area.searchArea);\n`;
    conditions += `    nwr["building"]["name"~"Skate Center|Rink|Roll",i](area.searchArea);\n`;
  }
  if (includeAll || targetFacilities.includes('skatepark')) {
    conditions += `    nwr["sport"="skateboard"](area.searchArea);\n`;
    conditions += `    nwr["leisure"="skatepark"](area.searchArea);\n`;
    conditions += `    nwr["name"~"Skatepark",i](area.searchArea);\n`;
  }
  
  const SAFE_QL_QUERY = `
  [out:json][timeout:300];
  area["ISO3166-2"="US-${stateCode}"]->.searchArea;
  (
${conditions}  );
  out body center;
  `;

  let elements: any[] = [];
  let attempts = 0;
  while (attempts < 3) {
    try {
      const response = await axios.post(OVERPASS_URL, `data=${encodeURIComponent(SAFE_QL_QUERY)}`, {
        headers: { 
          'Content-Type': 'application/x-www-form-urlencoded',
          'User-Agent': identity.userAgent,
          'Referer': 'https://sk8lytz.com/'
        },
        timeout: 300000 
      });
      elements = response.data.elements || [];
      console.log(`  ✅ [${stateCode}] Found ${elements.length} raw GIS geometries`);
      break;
    } catch (err: any) {
      attempts++;
      if (err.response?.status === 429 || err.response?.status === 504 || err.response?.status === 406) {
        console.log(`⚠️ Overpass Service Pressure (${err.response?.status}). Retrying check in 10s... (${attempts}/3)`);
        await sleep(10000);
        continue;
      }
      console.error(`❌ Overpass Failure for ${stateCode}:`, err.message);
      return false;
    }
  }

  if (elements.length === 0) {
    fs.writeFileSync(stateCachePath, JSON.stringify({ status: 'empty' }));
    return true;
  }

  console.log(`⚙️  [US-${stateCode}] Phase 2: Sanitization & Geocoding [${elements.length} records]`);
  let validSpots: any[] = [];
  for (let i = 0; i < elements.length; i++) {
    const el = elements[i];
    const lat = el.lat || el.center?.lat;
    const lon = el.lon || el.center?.lon;
    const tags = el.tags || {};
    
    // Stealth Throttle: Don't hammer Nominatim/OSM
    const delay = await GHOST.getAdaptiveDelay('NOMINATIM');
    await sleep(delay);

    let facility_type = 'unknown';
    const isSkatepark = tags['leisure'] === 'skatepark' || tags.name?.toLowerCase().includes('skatepark');
    const isRollerRink = tags['sport']?.includes('roller_skating') || tags.name?.toLowerCase().match(/rink|skateland|skate center/);
    const isProShop = tags['shop']?.includes('skate') || tags['shop']?.includes('sports') || tags.name?.toLowerCase().includes('skate shop');
    
    if (isProShop) facility_type = 'pro_shop';
    else if (isSkatepark && isRollerRink) facility_type = 'hybrid';
    else if (isSkatepark) facility_type = 'skatepark';
    else if (isRollerRink) facility_type = 'roller_rink';
    
    let name = tags.name || tags.brand || null;
    if (!name) {
      if (facility_type === 'skatepark') name = 'Unknown Public Skatepark';
      else if (facility_type === 'pro_shop') name = 'Independent Skate Shop';
      else name = 'Community Roller Rink';
    }

    let street_address = tags['addr:housenumber'] && tags['addr:street'] 
      ? `${tags['addr:housenumber']} ${tags['addr:street']}` : null;
    let city = tags['addr:city'] || null;
    let state = tags['addr:state'] || null;
    let zip = tags['addr:postcode'] || null;

    if (!street_address || !city || !state || !zip) {
      // Bypassing expensive reverse geocode for broad-net pulse to avoid stalls.
      // Phase 2 (Operator) will enrich these later.
      /*
      const geo = await reverseGeocode(lat, lon);
      if (geo) {
        if (!street_address && geo.fullAddress) street_address = geo.fullAddress;
        if (!city && geo.city) city = geo.city;
        if (!state && geo.state) state = geo.state;
        if (!zip && geo.zip) zip = geo.zip;
      }
      */
    }

    if (i % 10 === 0) console.log(`  ⏳ [${stateCode}] Geocoding progress: ${i}/${elements.length}...`);

    if (!street_address && !tags.name && !lat) continue;

    let surface = tags.surface || 'unknown';
    if (surface.includes('wood')) surface = 'wood';
    else if (surface.includes('concrete')) surface = 'concrete';
    else if (surface.includes('asphalt')) surface = 'asphalt';

    const has_lights = tags.lit === 'yes';
    const has_fee = tags.fee === 'yes';
    const has_rental = tags['skates:rental'] === 'yes';
    const is_wheelchair_accessible = tags.wheelchair === 'yes';
    const has_wifi = tags.internet_access === 'wlan' || tags.internet_access === 'yes';
    const has_toilets = tags.toilets === 'yes';
    const operator_name = tags.operator || null;
    const operator_description = tags.description || null;

    const has_food = tags.food === 'yes' || tags.snack_bar === 'yes' || tags.fast_food === 'yes';
    const has_ac = tags.air_conditioning === 'yes';
    const has_lockers = tags.lockers === 'yes';
    const capacity = tags.capacity ? parseInt(tags.capacity, 10) : null;
    const hosts_derby = tags.roller_derby === 'yes' || (tags.club && tags.club.includes('derby')) || null;

    validSpots.push({
      id: el.id.toString(),
      name, lat, lng: lon, city, state, zip, phone: tags.phone || tags['contact:phone'] || null,
      surface_type: surface, is_indoor: tags.indoor === 'yes', facility_type, street_address,
      opening_hours: buildHoursJSON(tags['opening_hours']), website: tags.website || tags['contact:website'] || null,
      has_lights, has_fee, has_rental, is_wheelchair_accessible, has_wifi, has_toilets, operator_name, operator_description,
      has_food, has_ac, has_lockers, capacity, hosts_derby,
      verification_status: 'PENDING'
    });
  }

  console.log(`🚀 [US-${stateCode}] Phase 3: Supabase Upsert (Raw Skeletons)`);
  const mappedUpload = validSpots.map(s => ({
      ...s,
      id: s.id.toString().padStart(32, '0').replace(/(.{8})(.{4})(.{4})(.{4})(.{12})/, "$1-$2-$3-$4-$5")
  }));

  // IMPORTANT: Use ignoreDuplicates to prevent overwriting existing progress
  const { error } = await supabase.from('skate_spots').upsert(mappedUpload, { 
    onConflict: 'id',
    ignoreDuplicates: true 
  });
  
  if (error) {
      console.error(`❌ Supabase Sync Error for ${stateCode}:`, error);
      return false;
  } else {
      console.log(`✅ ${stateCode} complete! Committed ${mappedUpload.length} entries (Skipped processed duplicates).`);
      fs.writeFileSync(stateCachePath, JSON.stringify(mappedUpload, null, 2));
      return true;
  }
}
