import axios from 'axios';
import fs from 'fs';
import path from 'path';
import { GHOST } from './GHOST';

export interface GeocodeResult {
  fullAddress: string | null;
  city: string | null;
  state: string | null;
  zip: string | null;
}

// OSM policy limits Nominatim calls to strictly 1 request per second.
const NOMINATIM_RATE_LIMIT_MS = 1500;
const CACHE_FILE = path.join(__dirname, '..', 'nominatim_cache.json');

let geocodeCache: Record<string, GeocodeResult | 'NULL_ADDRESS'> = {};

if (fs.existsSync(CACHE_FILE)) {
  try {
    geocodeCache = JSON.parse(fs.readFileSync(CACHE_FILE, 'utf-8'));
  } catch (e) {
    geocodeCache = {};
  }
}

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export async function reverseGeocode(lat: number, lon: number): Promise<GeocodeResult | null> {
  const coordinateKey = `${lat.toFixed(5)},${lon.toFixed(5)}`;
  
  if (geocodeCache[coordinateKey]) {
    const cached = geocodeCache[coordinateKey];
    if (cached === 'NULL_ADDRESS') return null;
    
    // Auto-migrate legacy cache formats (string -> object)
    if (typeof cached === 'string') {
      return { fullAddress: cached, city: null, state: null, zip: null };
    }
    return cached;
  }

  try {
    await sleep(NOMINATIM_RATE_LIMIT_MS);
    const response = await axios.get('https://nominatim.openstreetmap.org/reverse', {
      params: {
        lat,
        lon,
        format: 'json',
        addressdetails: 1,
        'accept-language': 'en'
      },
      headers: {
        'User-Agent': GHOST.generateIdentity().userAgent
      }
    });

    const data = response.data;
    if (data && data.address) {
      const road = data.address.road || data.address.pedestrian || data.address.footway || '';
      const houseNumber = data.address.house_number || '';
      
      // Attempt to extract city/state/zip
      const city = data.address.city || data.address.town || data.address.village || data.address.municipality || null;
      const state = data.address.state || null;
      const zip = data.address.postcode || null;
      
      let fullAddress = '';
      if (houseNumber && road) {
        fullAddress = `${houseNumber} ${road}`;
      } else if (road) {
        fullAddress = road; // some public parks just have the street name
      } else {
        // Fallback or missing address
      }

      const result: GeocodeResult = {
        fullAddress: fullAddress || null,
        city,
        state,
        zip
      };

      if (fullAddress || city || state || zip) {
        geocodeCache[coordinateKey] = result;
        fs.writeFileSync(CACHE_FILE, JSON.stringify(geocodeCache, null, 2));
        return result;
      }
    }
    
    // Explicitly cache negative lookups to prevent punishing the API
    geocodeCache[coordinateKey] = 'NULL_ADDRESS';
    fs.writeFileSync(CACHE_FILE, JSON.stringify(geocodeCache, null, 2));
    return null;

  } catch (err: any) {
    console.error(`Nominatim API Error for [${lat}, ${lon}]:`, err.message);
    return null;
  }
}
