import axios from 'axios';
import { reverseGeocode } from './lib/NominatimAPI';
import { scrapeCulturalDetails } from './lib/GoogleScraper';
import { buildHoursJSON } from './lib/OSMHoursParser';

// Tiny bounding box query for a small city area (e.g. Venice Beach Skatepark)
const OSM_TEST_QUERY = `
[out:json][timeout:25];
nw["leisure"="skatepark"](33.95,-118.5,34.0,-118.4);
out center;
`;

async function runTest() {
  console.log('🧪 Starting Micro-Harvest Dry Run...');
  
  const response = await axios.post('https://overpass-api.de/api/interpreter', `data=${encodeURIComponent(OSM_TEST_QUERY)}`, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  });

  const elements = response.data.elements;
  console.log(`✅ Fetched ${elements.length} skateparks for testing.`);

  if (elements.length > 0) {
    const el = elements[0];
    const lat = el.lat || el.center?.lat;
    const lon = el.lon || el.center?.lon;
    const tags = el.tags || {};
    
    console.log(`\n🔍 Found: ${tags.name || 'Unnamed Spot'}`);
    
    console.log(`🗺️ Reverse Geocoding...`);
    const address = await reverseGeocode(lat, lon);
    console.log(`   Address: ${address}`);
    
    console.log(`⏰ Parsing Hours...`);
    console.log(`   JSON:`, buildHoursJSON(tags['opening_hours']));
    
    console.log(`🕵️ Headless Cultural Scrape...`);
    const culture = await scrapeCulturalDetails(tags.name || 'Skatepark', address);
    console.log(`   Culture Profile:`, culture);
  }
}

runTest();
