const Database = require('better-sqlite3');
const db = new Database('tools/.scraper-data/scraper.db');

const r = db.prepare("UPDATE local_spots SET verification_status='SEEDED', photos=NULL, candidate_photos=NULL, last_attempted_at=NULL WHERE verification_status='STALLED'").run();
console.log('STALLED reset:', r.changes);

const counts = db.prepare("SELECT verification_status, COUNT(*) as cnt FROM local_spots GROUP BY verification_status ORDER BY cnt DESC").all();
console.log(JSON.stringify(counts, null, 2));
db.close();
