/**
 * fix-phantom-flags.ts
 * One-shot fix: clears is_deep_crawled + is_published phantom flags
 * on all spots where verification_status was reset to SEEDED or PENDING_WEBSITE
 * but the flags were not cleared by the prior bulk reset operation.
 */
import { Database } from 'bun:sqlite';
import path from 'path';

const DB_PATH = path.resolve(__dirname, '../.scraper-data/scraper.db');
const db = new Database(DB_PATH);
db.exec('PRAGMA busy_timeout = 10000; PRAGMA journal_mode = WAL;');

// Count first so we know the scope
const before = db.prepare(`
  SELECT COUNT(*) as cnt FROM local_spots
  WHERE verification_status IN ('SEEDED', 'PENDING_WEBSITE')
  AND (is_deep_crawled = 1 OR is_published = 1)
`).get() as { cnt: number };

console.log(`\n🔍 Found ${before.cnt} spots with phantom flags (SEEDED/PENDING_WEBSITE but is_deep_crawled=1 or is_published=1)`);

const result = db.prepare(`
  UPDATE local_spots
  SET
    is_deep_crawled = 0,
    is_published    = 0
  WHERE verification_status IN ('SEEDED', 'PENDING_WEBSITE')
  AND (is_deep_crawled = 1 OR is_published = 1)
`).run();

console.log(`✅ Cleared phantom flags on ${result.changes} spots`);
console.log('   These spots will no longer appear in the Phase 2 detective-recent out-queue');
console.log('   They remain correctly queued as SEEDED → entering Phase 2 Detective\n');

db.close();
