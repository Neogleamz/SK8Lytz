import axios from 'axios';
import fs from 'fs';
import path from 'path';

const OVERPASS_API_URL = 'https://overpass-api.de/api/interpreter';

// We perform a bounding box search for a smaller region (e.g., West Coast / California Area)
// to prevent 504 Gateway Timeouts from the public Overpass API during testing.
// Bounding box for CA: (32.5, -124.5, 42.0, -114.0)
const OVERPASS_QUERY = `
  [out:json][timeout:90];
  (
    node["sport"="roller_skating"](32.5, -124.5, 42.0, -114.0);
    way["sport"="roller_skating"](32.5, -124.5, 42.0, -114.0);
    relation["sport"="roller_skating"](32.5, -124.5, 42.0, -114.0);
  );
  out center;
`;

export interface OverpassElement {
  type: string;
  id: number;
  lat?: number;
  lon?: number;
  center?: { lat: number; lon: number };
  tags: {
    name?: string;
    'addr:city'?: string;
    'addr:state'?: string;
    'addr:street'?: string;
    'addr:postcode'?: string;
    website?: string;
    phone?: string;
    [key: string]: string | undefined;
  };
}

async function seedOverpass() {
  console.log('🌍 Connecting to OpenStreetMap Overpass API...');
  
  try {
    const response = await axios.post(
      OVERPASS_API_URL, 
      `data=${encodeURIComponent(OVERPASS_QUERY)}`,
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    );

    const elements: OverpassElement[] = response.data.elements;
    
    // Filter and sanitize the raw OSM nodes
    const validSpots = elements
      .filter(e => e.tags && e.tags.name) // Must have a name to be useful
      .map(e => {
        // 'way' and 'relation' types return a center coordinate from our query
        const lat = e.lat ?? e.center?.lat;
        const lon = e.lon ?? e.center?.lon;
        
        return {
          osm_id: e.id,
          name: e.tags.name,
          lat,
          lng: lon,
          city: e.tags['addr:city'] || null,
          state: e.tags['addr:state'] || null,
          website: e.tags.website || null,
          phone: e.tags.phone || null,
          raw_tags: e.tags
        };
      });

    console.log(`✅ Extracted ${validSpots.length} verified named skating spots.`);
    
    // Save to local seed JSON
    const outputPath = path.join(__dirname, 'seed.json');
    fs.writeFileSync(outputPath, JSON.stringify(validSpots, null, 2), 'utf8');
    
    console.log(`💾 Saved seed data to ${outputPath}`);
    
  } catch (err: any) {
    console.error('❌ Failed to fetch from Overpass:', err.message);
  }
}

// Execute if run directly
if (require.main === module) {
  seedOverpass();
}
