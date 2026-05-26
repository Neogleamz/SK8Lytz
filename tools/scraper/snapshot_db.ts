import { Database } from 'bun:sqlite';
import fs from 'fs';
import path from 'path';

const dbPath = path.resolve(__dirname, '../.scraper-data/scraper.db');
const backupPath = path.resolve(__dirname, '../.scraper-data/scraper_baseline.db');

if (fs.existsSync(backupPath)) fs.unlinkSync(backupPath);

const db = new Database(dbPath);
db.exec('PRAGMA busy_timeout = 10000;');

console.log('Creating atomic snapshot of entire database...');
db.exec(`VACUUM INTO '${backupPath}'`);
db.close();

const stats = fs.statSync(backupPath);
console.log(`✅ Full database snapshot created: scraper_baseline.db (${Math.round(stats.size / 1024 / 1024)} MB)`);
