import { Database } from 'bun:sqlite';
import * as path from 'path';

const dbPath = path.resolve(__dirname, 'core/scraper.db');
const db = new Database(dbPath);

const published = db.prepare('SELECT COUNT(*) as cnt FROM local_spots WHERE is_published = 1').get() as { cnt: number };
const syncing = db.prepare('SELECT COUNT(*) as cnt FROM local_spots WHERE sync_required = 1').get() as { cnt: number };

console.log(`Published: ${published.cnt}`);
console.log(`Sync Required: ${syncing.cnt}`);
