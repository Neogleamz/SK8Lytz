import { db } from './core/LocalDB';

const spots = db.prepare("SELECT id, name, verification_status, pricing_data, has_rental, is_deep_crawled FROM local_spots WHERE name LIKE '%Beechmont%'").all();
console.log('All Beechmont matching rows:');
console.log(JSON.stringify(spots, null, 2));
