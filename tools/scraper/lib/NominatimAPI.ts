import axios from 'axios';
import fs from 'fs';
import path from 'path';

// OSM policy limits Nominatim calls to strictly 1 request per second.
const NOMINATIM_RATE_LIMIT_MS = 1500;
const CACHE_FILE = path.join(__dirname, '..', 'nominatim_cache.json');

let geocodeCache: Record<string, string> = {};

if (fs.existsSync(CACHE_FILE)) {
  geocodeCache = JSON.parse(fs.readFileSync(CACHE_FILE, 'utf-8'));
}

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export async function reverseGeocode(lat: number, lon: number): Promise<string | null> {
  const coordinateKey = `${lat.toFixed(5)},${lon.toFixed(5)}`;
  
  if (geocodeCache[coordinateKey]) {
    return geocodeCache[coordinateKey] === 'NULL_ADDRESS' ? null : geocodeCache[coordinateKey];
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
        'User-Agent': 'SK8Lytz-DataSet-Compiler/1.0 (contact@sk8lytz.com)'
      }
    });

    const data = response.data;
    if (data && data.address) {
      const road = data.address.road || data.address.pedestrian || data.address.footway || '';
      const houseNumber = data.address.house_number || '';
      
      let fullAddress = '';
      if (houseNumber && road) {
        fullAddress = `${houseNumber} ${road}`;
      } else if (road) {
        fullAddress = road; // some public parks just have the street name
      } else {
        // Fallback or missing address
      }

      if (fullAddress) {
        geocodeCache[coordinateKey] = fullAddress;
        fs.writeFileSync(CACHE_FILE, JSON.stringify(geocodeCache, null, 2));
        return fullAddress;
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
