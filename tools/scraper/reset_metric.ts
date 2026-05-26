import { Database } from 'bun:sqlite';
import path from 'path';

const dbPath = path.resolve(__dirname, '../.scraper-data/scraper.db');
const db = new Database(dbPath);

const res = db.prepare(`UPDATE local_spots SET is_deep_crawled = 0 WHERE verification_status IN ('SEEDED', 'PENDING_WEBSITE', 'WEBSITE_STALLED', 'REJECTED', 'ON_HOLD')`).run();
console.log('Fixed ' + res.changes + ' records to clear is_deep_crawled flag.');
db.close();
