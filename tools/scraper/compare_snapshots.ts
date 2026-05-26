import { Database } from 'bun:sqlite';
import path from 'path';

const liveDbPath = path.resolve(__dirname, '../.scraper-data/scraper.db');
const baselineDbPath = path.resolve(__dirname, '../.scraper-data/scraper_baseline.db');

const liveDb = new Database(liveDbPath, { readonly: true });
const baseDb = new Database(baselineDbPath, { readonly: true });

const fieldsToCompare = [
  'opening_hours', 'pricing_data', 'has_adult_night', 'adult_night_schedule', 
  'adult_night_details', 'surface_type', 'surface_quality', 'has_rental', 
  'has_pro_shop', 'has_food', 'has_lights', 'has_lockers', 'has_ac', 'has_wifi', 
  'has_toilets', 'is_wheelchair_accessible', 'hosts_derby', 'capacity', 
  'operator_name', 'operator_description', 'instagram_url', 'facebook_url', 
  'tiktok_url', 'schedule_url', 'yelp_url', 'email_addresses', 'is_indoor', 
  'vibe_score', 'photos', 'candidate_photos', 'website', 'phone_number', 'city', 'state'
];

const liveSpots = liveDb.prepare('SELECT * FROM local_spots').all() as any[];
const baseSpots = baseDb.prepare('SELECT * FROM local_spots').all() as any[];

const baseMap = new Map(baseSpots.map(s => [s.id, s]));

let totalEnriched = 0;
let totalFieldsAdded = 0;
let emptyToFilledCount: Record<string, number> = {};

console.log('\n======================================================');
console.log('🔍 DATABASE DELTA: BASELINE vs LIVE');
console.log('======================================================\n');

for (const live of liveSpots) {
  const base = baseMap.get(live.id);
  if (!base) continue;

  const changes: string[] = [];

  for (const field of fieldsToCompare) {
    let oldVal = base[field];
    let newVal = live[field];

    // Normalize empty strings and nulls
    if (oldVal === '' || oldVal === '[]' || oldVal === '{}') oldVal = null;
    if (newVal === '' || newVal === '[]' || newVal === '{}') newVal = null;

    if (oldVal !== newVal) {
      // Check if it went from Empty -> Filled
      if ((oldVal === null || oldVal === undefined) && newVal !== null && newVal !== undefined) {
        changes.push(`[+] ${field}: (null) -> ${newVal}`);
        emptyToFilledCount[field] = (emptyToFilledCount[field] || 0) + 1;
        totalFieldsAdded++;
      } else if (oldVal !== null && newVal !== null) {
        changes.push(`[~] ${field}: ${oldVal} -> ${newVal}`);
        emptyToFilledCount[field] = (emptyToFilledCount[field] || 0) + 1; // Count updates too
        totalFieldsAdded++;
      }
    }
  }

  if (changes.length > 0) {
    totalEnriched++;
    console.log(`\n🎟️ ${live.name} [${live.city}, ${live.state}]`);
    console.log(`Status: ${base.verification_status} -> ${live.verification_status}`);
    changes.forEach(c => console.log(`   ${c}`));
  }
}

console.log('\n======================================================');
console.log(`📈 SUMMARY REPORT`);
console.log(`Total Records Enriched: ${totalEnriched}`);
console.log(`Total Net-New Data Points: ${totalFieldsAdded}`);
console.log('Top Fields Populated:');
Object.entries(emptyToFilledCount)
  .sort((a, b) => b[1] - a[1])
  .forEach(([field, count]) => {
    console.log(`   - ${field}: ${count} records`);
  });
console.log('======================================================\n');

liveDb.close();
baseDb.close();
