/**
 * reset-records.ts — Bulk reset utility for SK8Lytz scraper pipeline
 * 
 * Objectives:
 * 1. Convert all 'null'/'NULL' strings in database columns to real SQL NULLs.
 * 2. Reset all non-REJECTED spots to 'SEEDED' verification_status.
 * 3. Clear pipeline_status and is_deep_crawled flags to trigger re-processing.
 */

const Database = require('better-sqlite3');
const path = require('path');

const DB_PATH = path.resolve(__dirname, '../.scraper-data/scraper.db');
const db = new Database(DB_PATH);

console.log('🚀 Starting Database Sanitization & Pipeline Reset...');

// 1. Sanitize 'null' strings in all relevant columns
const columns = [
  'is_indoor', 'operator_description', 'operator_name', 'instagram_url', 'facebook_url',
  'tiktok_url', 'schedule_url', 'opening_hours', 'adult_night_schedule',
  'adult_night_details', 'special_events', 'pricing_data', 'surface_type',
  'surface_quality', 'cultural_metadata', 'yelp_url', 'price_range', 'logo_url', 'cover_photo_url'
];

db.transaction(() => {
  let sanitizedCount = 0;
  for (const col of columns) {
    const res = db.prepare(`UPDATE local_spots SET ${col} = NULL WHERE ${col} = 'null' OR ${col} = 'NULL' OR ${col} = 'None' OR ${col} = 'N/A'`).run();
    sanitizedCount += res.changes;
  }
  console.log(`✅ Sanitized ${sanitizedCount} string-null fields across ${columns.length} columns.`);

  // 2. Bulk Reset Pipeline
  const res = db.prepare(`
    UPDATE local_spots 
    SET 
      verification_status = 'SEEDED', 
      pipeline_status = '', 
      is_deep_crawled = 0,
      last_attempted_at = NULL
    WHERE verification_status != 'REJECTED'
  `).run();

  console.log(`✅ Reset ${res.changes} records to SEEDED status.`);
})();

console.log('✨ All operations completed successfully.');
process.exit(0);
