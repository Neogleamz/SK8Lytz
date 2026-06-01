/**
 * requeue-photoless.ts — One-shot script
 *
 * Finds all MEDIA_READY spots that have a website URL but no downloaded photos,
 * and resets them to DEEP_CRAWLED so the Photographer will crawl their websites.
 *
 * Safe: only touches spots with NO photos. All enriched fields are preserved.
 */
const Database = require('better-sqlite3');
const path = require('path');

const DB_PATH = path.resolve(__dirname, '../.scraper-data/scraper.db');
const db = new Database(DB_PATH);

// Preview what we'll touch
const preview = db.prepare(`
  SELECT COUNT(*) as cnt FROM local_spots
  WHERE verification_status = 'MEDIA_READY'
    AND website IS NOT NULL AND website != ''
    AND (photos IS NULL OR photos = '[]' OR photos = '' OR photos = 'null')
`).get();

console.log(`\n📋 Found ${preview.cnt} MEDIA_READY spots with a website but no photos.`);
console.log(`   These will be reset to DEEP_CRAWLED for the Photographer to crawl.\n`);

if (preview.cnt === 0) {
  console.log('Nothing to do!');
  process.exit(0);
}

// Sample a few before committing
const samples = db.prepare(`
  SELECT name, state, website FROM local_spots
  WHERE verification_status = 'MEDIA_READY'
    AND website IS NOT NULL AND website != ''
    AND (photos IS NULL OR photos = '[]' OR photos = '' OR photos = 'null')
  LIMIT 12
`).all();

console.log('Sample spots being re-queued:');
samples.forEach((r: any) => console.log(`  [${r.state}] ${r.name} → ${r.website}`));
console.log('  ...\n');

// Execute the reset — preserve all enriched data, just change status + reset retry counter
const result = db.prepare(`
  UPDATE local_spots
  SET
    verification_status = 'DEEP_CRAWLED',
    is_deep_crawled = 1,
    retry_count = 0,
    last_attempted_at = NULL
  WHERE verification_status = 'MEDIA_READY'
    AND website IS NOT NULL AND website != ''
    AND (photos IS NULL OR photos = '[]' OR photos = '' OR photos = 'null')
`).run();

console.log(`✅ Reset ${result.changes} spots to DEEP_CRAWLED.`);
console.log(`   The Photographer will now pick them up and crawl their websites.\n`);

// Count spots with no website — need manual upload
const noWebsite = db.prepare(`
  SELECT COUNT(*) as cnt FROM local_spots
  WHERE verification_status = 'MEDIA_READY'
    AND (website IS NULL OR website = '')
    AND (photos IS NULL OR photos = '[]' OR photos = '' OR photos = 'null')
`).get();

if (noWebsite.cnt > 0) {
  console.log(`⚠️  ${noWebsite.cnt} MEDIA_READY spots have NO website and NO photos.`);
  console.log(`   These need manual photo upload from the dashboard.\n`);
}

db.close();
console.log('✨ Done.');
process.exit(0);
