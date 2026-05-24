/**
 * bulk-reset-to-seeded.ts
 * One-shot operational script — resets pipeline status for Phase 2+3 re-processing.
 *
 * RESETS:  DEEP_CRAWLED, MEDIA_READY, PUBLISHED → SEEDED
 * SKIPS:   REJECTED, STALLED, ON_HOLD, WEBSITE_STALLED, PENDING_WEBSITE (never touched)
 * PHOTOS:  NEVER TOUCHED. photos, photo_coverage, logo_url are sacred and untouched.
 */
import { Database } from 'bun:sqlite';
import path from 'path';

const DB_PATH = path.resolve(__dirname, '../../.scraper-data/scraper.db');
const db = new Database(DB_PATH);

db.exec('PRAGMA busy_timeout = 10000;');
db.exec('PRAGMA journal_mode = WAL;');

// Snapshot before
const before = db.prepare(`
  SELECT verification_status, COUNT(*) as cnt
  FROM local_spots
  WHERE verification_status IN ('DEEP_CRAWLED', 'MEDIA_READY', 'PUBLISHED', 'SEEDED')
  GROUP BY verification_status
  ORDER BY cnt DESC
`).all() as { verification_status: string; cnt: number }[];

console.log('\n📊 BEFORE RESET:');
for (const row of before) {
  console.log(`  ${row.verification_status}: ${row.cnt}`);
}

// Execute reset — photos/photo_coverage/logo_url NEVER touched
const result = db.prepare(`
  UPDATE local_spots
  SET
    verification_status = 'SEEDED',
    is_deep_crawled     = 0,
    is_published        = 0,
    retry_count         = 0,
    last_attempted_at   = NULL
  WHERE verification_status IN ('DEEP_CRAWLED', 'MEDIA_READY', 'PUBLISHED')
`).run();

console.log(`\n✅ RESET COMPLETE: ${result.changes} spots → SEEDED`);
console.log('   photos, photo_coverage, logo_url: UNTOUCHED ✅');
console.log('   All enriched data fields: UNTOUCHED ✅');
console.log('   REJECTED / STALLED / ON_HOLD / WEBSITE_STALLED: UNTOUCHED ✅');

// Snapshot after
const after = db.prepare(`
  SELECT verification_status, COUNT(*) as cnt
  FROM local_spots
  WHERE verification_status IN ('DEEP_CRAWLED', 'MEDIA_READY', 'PUBLISHED', 'SEEDED')
  GROUP BY verification_status
  ORDER BY cnt DESC
`).all() as { verification_status: string; cnt: number }[];

console.log('\n📊 AFTER RESET:');
for (const row of after) {
  console.log(`  ${row.verification_status}: ${row.cnt}`);
}

const seededNow = (after.find(r => r.verification_status === 'SEEDED')?.cnt || 0);
console.log(`\n🚀 Phase 2 (Detective) queue size: ${seededNow} spots ready`);
console.log('   Phase 3 (Photographer) will pick them up as they complete Phase 2.\n');

db.close();
