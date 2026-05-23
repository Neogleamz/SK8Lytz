const { db } = require('./core/LocalDB');
try {
  const spot = db.query("SELECT id, name, website, state, verification_status, last_attempted_at, is_deep_crawled, retry_count, candidate_photos, photos FROM local_spots WHERE id = 'b3d0147d-3284-4035-b0bf-30a772a10779'").get();
  console.log('Skate City Spot details:', JSON.stringify(spot, null, 2));
} catch (e) {
  console.error('Error querying:', e);
}
