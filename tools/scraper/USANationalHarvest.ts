import axios from 'axios';
import fs from 'fs';
import path from 'path';
import dotenv from 'dotenv';
import { createClient } from '@supabase/supabase-js';

import { buildHoursJSON } from './lib/OSMHoursParser';
import { reverseGeocode } from './lib/NominatimAPI';
import { scrapeCulturalDetails } from './lib/GoogleScraper';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(process.env.EXPO_PUBLIC_SUPABASE_URL || '', process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '');

const OVERPASS_URL = 'https://overpass-api.de/api/interpreter';
const CACHE_DIR = path.join(__dirname, 'state_caches');
if (!fs.existsSync(CACHE_DIR)) fs.mkdirSync(CACHE_DIR);

const US_STATES = [
  'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA',
  'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
  'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
  'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
  'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY', 'DC'
];

async function sleep(ms: number) {
  return new Promise(r => setTimeout(r, ms));
}

async function processState(stateCode: string) {
  const stateCachePath = path.join(CACHE_DIR, `${stateCode}.json`);
  if (fs.existsSync(stateCachePath)) {
    console.log(`⏩ Skipping ${stateCode} - Successfully processed in a previous pass!`);
    return;
  }

  console.log(`\n🛹 [US-${stateCode}] Phase 1: Contacting OpenStreetMap Overpass Servers...`);
  
  const SAFE_QL_QUERY = `
  [out:json][timeout:300];
  area["ISO3166-2"="US-${stateCode}"]->.searchArea;
  (
    nwr["sport"~"roller_skating|skateboard"](area.searchArea);
    nwr["leisure"="skatepark"](area.searchArea);
    nwr["shop"~"skates|sports"]["sport"~"roller_skating|skateboard"](area.searchArea);
    nwr["name"~"skatepark|roller rink|skateland|skate center|skate world",i](area.searchArea);
  );
  out center;
  `;

  let elements: any[] = [];
  try {
    const response = await axios.post(OVERPASS_URL, `data=${encodeURIComponent(SAFE_QL_QUERY)}`, {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      timeout: 300000 
    });
    elements = response.data.elements || [];
    console.log(`  ✅ Found ${elements.length} raw GIS geometries in ${stateCode}`);
  } catch (err: any) {
    console.error(`❌ Overpass Failure for ${stateCode}:`, err.message);
    return; // Fast fail this state, next pipeline resumes it
  }

  if (elements.length === 0) {
    fs.writeFileSync(stateCachePath, JSON.stringify({ status: 'empty' }));
    return;
  }

  console.log(`⚙️  [US-${stateCode}] Phase 2: Sanitization & Geocoding [${elements.length} records]`);
  let validSpots: any[] = [];
  for (let i = 0; i < elements.length; i++) {
    const el = elements[i];
    const lat = el.lat || el.center?.lat;
    const lon = el.lon || el.center?.lon;
    const tags = el.tags || {};
    
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
      const geo = await reverseGeocode(lat, lon);
      if (geo) {
        if (!street_address && geo.fullAddress) street_address = geo.fullAddress;
        if (!city && geo.city) city = geo.city;
        if (!state && geo.state) state = geo.state;
        if (!zip && geo.zip) zip = geo.zip;
      }
    }

    if (!street_address) continue;

    let surface = tags.surface || 'unknown';
    if (surface.includes('wood')) surface = 'wood';
    else if (surface.includes('concrete')) surface = 'concrete';
    else if (surface.includes('asphalt')) surface = 'asphalt';

    validSpots.push({
      id: el.id.toString(),
      name, lat, lng: lon, city, state, zip, phone: tags.phone || tags['contact:phone'] || null,
      surface_type: surface, is_indoor: tags.indoor === 'yes', facility_type, street_address,
      opening_hours: buildHoursJSON(tags['opening_hours']), website: tags.website || tags['contact:website'] || null,
    });
  }

  console.log(`🕵️  [US-${stateCode}] Phase 3: Cultural Extraction (${validSpots.length} filtered spots)`);
  let enrichedSpots: any[] = [];
  for (let i = 0; i < validSpots.length; i++) {
     const spot = validSpots[i];
     const culture = await scrapeCulturalDetails(spot.name, spot.city);
     enrichedSpots.push({
         ...spot,
         has_pro_shop: culture.has_pro_shop,
         has_adult_night: culture.has_adult_night,
         vibe_rating: culture.vibe_rating,
         socials: culture.socials,
         website: spot.website || culture.fetched_website,
         is_featured: false
     });
     // Prevent aggressive ban from Google custom search headless scraper
     await sleep(1000); 
  }

  console.log(`🚀 [US-${stateCode}] Phase 4: Supabase Upsert`);
  const mappedUpload = enrichedSpots.map(s => ({
      ...s,
      // Map arbitrary OSM IDs to valid UUID formats
      id: s.id.toString().padStart(32, '0').replace(/(.{8})(.{4})(.{4})(.{4})(.{12})/, "$1-$2-$3-$4-$5")
  }));

  const { error } = await supabase.from('skate_spots').upsert(mappedUpload);
  if (error) {
      console.error(`❌ Supabase Sync Error for ${stateCode}:`, error);
  } else {
      console.log(`✅ ${stateCode} complete! Committed ${mappedUpload.length} rows.`);
      // Checkpoint Save! Prevents this state from ever running again
      fs.writeFileSync(stateCachePath, JSON.stringify(mappedUpload, null, 2));
  }
}

async function runNationalHarvester() {
  console.log('🌎 STARTING VERTICAL PIPELINE HARVEST 🌎');
  for (let i = 0; i < US_STATES.length; i++) {
     await processState(US_STATES[i]);
     if (i < US_STATES.length - 1) {
         console.log('⏳ Sleeping 15s to cool down OSM limits before next state...');
         await sleep(15000);
     }
  }
  console.log('\n🏁 USA Dataset Fully Synchronized! Ready to roll.');
}

runNationalHarvester();
