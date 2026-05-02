const fs = require('fs');
const path = require('path');
const Database = require('better-sqlite3');
const dotenv = require('dotenv');

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const MAPS_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY;

if (!MAPS_KEY) {
  console.error("No Google Maps API Key found.");
  process.exit(1);
}

const db = new Database(path.resolve(__dirname, '../.scraper-data/scraper.db'));

const records = db.prepare(`SELECT id, name, google_place_id FROM local_spots WHERE google_place_id IS NOT NULL AND candidate_photos IS NULL AND verification_status = 'SEEDED'`).all();

console.log(`Found ${records.length} records needing restoration.`);

async function run() {
  for (const record of records) {
    try {
      const url = `https://maps.googleapis.com/maps/api/place/details/json?place_id=${record.google_place_id}&fields=photos&key=${MAPS_KEY}`;
      const res = await fetch(url);
      const json = await res.json();
      
      if (json.result && json.result.photos) {
        const refs = json.result.photos.map(p => p.photo_reference);
        const cp = JSON.stringify({ google_refs: refs });
        db.prepare('UPDATE local_spots SET candidate_photos = ? WHERE id = ?').run(cp, record.id);
        console.log(`Restored ${refs.length} photo refs for ${record.name}`);
      } else {
        console.log(`No photos found in Google for ${record.name}`);
      }
    } catch (e) {
      console.error(`Error on ${record.name}:`, e.message);
    }
  }
}

run();
