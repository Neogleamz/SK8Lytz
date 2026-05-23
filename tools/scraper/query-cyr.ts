import { db } from './core/LocalDB';
const spots = db.prepare("SELECT id, name, verification_status, pricing_data, has_rental, is_deep_crawled FROM local_spots WHERE name LIKE '%Cyr%'").all();
console.log('All Cyr matching rows:');
console.log(JSON.stringify(spots, null, 2));
process.exit(0);
