import { db } from './core/LocalDB';

const query = `
  UPDATE local_spots 
  SET 
    verification_status = 'SEEDED', 
    is_deep_crawled = 0, 
    last_attempted_at = NULL, 
    retry_count = 0, 
    pricing_data = NULL, 
    has_rental = NULL, 
    has_fee = NULL 
  WHERE name LIKE '%Cyr%'
`;

const res = db.prepare(query).run();
console.log(`✅ Reset ${res.changes} Cyr Arena record(s) back to SEEDED.`);
process.exit(0);
