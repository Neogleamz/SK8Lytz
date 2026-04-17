import axios from 'axios';
import fs from 'fs';
import path from 'path';
import dotenv from 'dotenv';
import { createClient } from '@supabase/supabase-js';

import { buildHoursJSON } from './lib/OSMHoursParser';
import { reverseGeocode } from './lib/NominatimAPI';
import { scrapeCulturalDetails } from './lib/GoogleScraper';

dotenv.config({ path: '../../.env' });
const supabase = createClient(process.env.EXPO_PUBLIC_SUPABASE_URL || '', process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '');

// Supabase and Puppeteer imports will go here for Phases 2 & 3
const OVERPASS_URL = 'https://overpass-api.de/api/interpreter';
const CACHE_FILE = path.join(__dirname, 'osm_raw_cache.json');

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

export async function harvestOpenStreetMap() {
  console.log('🛹 Phase 1: Contacting OpenStreetMap Overpass Servers for State-by-State Harvest...');
  
  if (fs.existsSync(CACHE_FILE)) {
    console.log('⚠️  Raw OSM data cached found! Skipping Overpass query to save bandwidth.');
    return JSON.parse(fs.readFileSync(CACHE_FILE, 'utf-8'));
  }

  let allElements: any[] = [];
  
  for (let i = 0; i < US_STATES.length; i++) {
    const s = US_STATES[i];
    console.log(`[${i+1}/${US_STATES.length}] Requesting data for US-${s}...`);
    
    const SAFE_QL_QUERY = `
    [out:json][timeout:300];
    area["ISO3166-2"="US-${s}"]->.searchArea;
    (
      nwr["sport"~"roller_skating|skateboard"](area.searchArea);
      nwr["leisure"="skatepark"](area.searchArea);
      nwr["shop"~"skates|sports"]["sport"~"roller_skating|skateboard"](area.searchArea);
      nwr["name"~"skatepark|roller rink|skateland|skate center|skate world",i](area.searchArea);
    );
    out center;
    `;

    try {
      const response = await axios.post(OVERPASS_URL, `data=${encodeURIComponent(SAFE_QL_QUERY)}`, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        timeout: 300000 
      });

      const elements = response.data.elements;
      if (elements) {
        allElements = allElements.concat(elements);
        console.log(`  ✅ Found ${elements.length} spots in ${s}`);
      }
      
      // Strict 15 second pace between states to NEVER hit 429 Rate Limits
      if (i < US_STATES.length - 1) {
        await sleep(15000); 
      }
    } catch (err: any) {
      console.error(`❌ Overpass Failure for ${s}:`, err.message);
      // If one state fails, we wait longer and continue to the next
      await sleep(30000); 
    }
  }
  
  console.log(`\n✅ Harvested ${allElements.length} TOTAL raw GIS geometries across the USA.`);
  fs.writeFileSync(CACHE_FILE, JSON.stringify(allElements, null, 2));
  return allElements;
}

import { buildHoursJSON } from './lib/OSMHoursParser';
import { reverseGeocode } from './lib/NominatimAPI';

async function runPipeline() {
  const elements = await harvestOpenStreetMap();
  console.log(`\n⚙️ Phase 2: Sanitization & Geocoding [${elements.length} records]`);
  
  let validSpots: any[] = [];
  
  for (let i = 0; i < elements.length; i++) {
    const el = elements[i];
    const lat = el.lat || el.center?.lat;
    const lon = el.lon || el.center?.lon;
    const tags = el.tags || {};
    
    // Facility Type Classification
    let facility_type = 'unknown';
    const isSkatepark = tags['leisure'] === 'skatepark' || tags.name?.toLowerCase().includes('skatepark');
    const isRollerRink = tags['sport']?.includes('roller_skating') || tags.name?.toLowerCase().match(/rink|skateland|skate center/);
    const isProShop = tags['shop']?.includes('skate') || tags['shop']?.includes('sports') || tags.name?.toLowerCase().includes('skate shop');
    
    if (isProShop) facility_type = 'pro_shop';
    else if (isSkatepark && isRollerRink) facility_type = 'hybrid';
    else if (isSkatepark) facility_type = 'skatepark';
    else if (isRollerRink) facility_type = 'roller_rink';
    
    // Name Parsing
    let name = tags.name || tags.brand || null;
    if (!name) {
      if (facility_type === 'skatepark') name = 'Unknown Public Skatepark';
      else if (facility_type === 'pro_shop') name = 'Independent Skate Shop';
      else name = 'Community Roller Rink';
    }

    // Address extraction & geocoding fallback
    let street_address = tags['addr:housenumber'] && tags['addr:street'] 
      ? `${tags['addr:housenumber']} ${tags['addr:street']}` : null;
    let city = tags['addr:city'] || null;
    let state = tags['addr:state'] || null;
    let zip = tags['addr:postcode'] || null;
    let fallbackFullAddress = null;

    if (!street_address) {
      console.log(`[${i+1}/${elements.length}] Reverse Geocoding ${name}...`);
      fallbackFullAddress = await reverseGeocode(lat, lon);
      if (!fallbackFullAddress) {
        console.log(`  -> Dropping: Could not reverse-geocode address.`);
        continue; // Strictly enforce Addresses!
      }
      street_address = fallbackFullAddress;
    }

    const opening_hours = buildHoursJSON(tags['opening_hours']);
    
    let surface = tags.surface || 'unknown';
    if (surface.includes('wood')) surface = 'wood';
    else if (surface.includes('concrete')) surface = 'concrete';
    else if (surface.includes('asphalt')) surface = 'asphalt';

    validSpots.push({
      id: el.id,
      name,
      lat,
      lng: lon,
      city, // Note: Nominatim sometimes provides these, but we strictly map full address to street_address
      state,
      zip,
      phone: tags.phone || tags['contact:phone'] || null,
      surface_type: surface,
      is_indoor: tags.indoor === 'yes',
      facility_type,
      street_address,
      opening_hours,
      website: tags.website || tags['contact:website'] || null,
    });
  }
  
  console.log(`\n🎉 Phase 2 Complete. Yield: ${validSpots.length}/${elements.length} spots survived address enforcement.`);
  fs.writeFileSync(path.join(__dirname, 'phase2_cache.json'), JSON.stringify(validSpots, null, 2));

  console.log(`\n🕵️ Phase 3: Headless Cultural Extraction`);
  let enrichedSpots: any[] = [];
  
  // To avoid running 5000 scrapes in testing, we use cache if available.
  const enrichmentCacheFile = path.join(__dirname, 'phase3_cache.json');
  if (fs.existsSync(enrichmentCacheFile)) {
    console.log('⚠️ Loading enriched spots from Phase 3 cache!');
    enrichedSpots = JSON.parse(fs.readFileSync(enrichmentCacheFile, 'utf-8'));
  } else {
    for (let i = 0; i < validSpots.length; i++) {
        const spot = validSpots[i];
        console.log(`[${i+1}/${validSpots.length}] Inferring cultural profile for: ${spot.name}...`);
        const culture = await scrapeCulturalDetails(spot.name, spot.city);
        
        enrichedSpots.push({
            ...spot,
            has_pro_shop: culture.has_pro_shop,
            has_adult_night: culture.has_adult_night,
            vibe_rating: culture.vibe_rating,
            socials: culture.socials,
            website: spot.website || culture.fetched_website // Fallback to scrape if OSM misses it
        });
        
        // Cache incrementally!
        fs.writeFileSync(enrichmentCacheFile, JSON.stringify(enrichedSpots, null, 2));
    }
  }

  console.log(`\n🚀 Phase 4: Bulk Supabase Upsert Sequence`);
  const BATCH_SIZE = 50;
  for (let i = 0; i < enrichedSpots.length; i += BATCH_SIZE) {
    const batch = enrichedSpots.slice(i, i + BATCH_SIZE);
    console.log(`Uploading Batch ${Math.floor(i/BATCH_SIZE) + 1} (${batch.length} spots)...`);
    
    // Fix OSM IDs to match DB UUID
    const mappedUpload = batch.map(s => ({
        ...s,
        id: s.id.toString().padStart(32, '0').replace(/(.{8})(.{4})(.{4})(.{4})(.{12})/, "$1-$2-$3-$4-$5")
    }));

    const { error } = await supabase.from('skate_spots').upsert(mappedUpload);
    if (error) {
        console.error(`❌ Supabase Sync Error on chunk ${i}:`, error);
    }
  }

  console.log('\n🏁 USA Dataset Fully Synchronized & Enriched! Ready to roll.');
}

runPipeline();
