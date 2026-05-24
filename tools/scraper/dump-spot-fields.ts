import { Database } from 'bun:sqlite';
import path from 'path';

const db = new Database(path.resolve(__dirname, '../.scraper-data/scraper.db'));
db.exec('PRAGMA busy_timeout=10000;');

const SKIP = new Set(['raw_data','candidate_photos','candidate_links','ai_metadata','dom_images','flyer_urls','field_confidence']);

const names = ['SOCO Skateland', 'Roller City'];

for (const name of names) {
  const spot = db.prepare(`SELECT * FROM local_spots WHERE name LIKE ?`).get(`%${name}%`) as Record<string, unknown>;
  if (!spot) { console.log(`\n❌ ${name} — NOT FOUND`); continue; }

  console.log(`\n${'═'.repeat(70)}`);
  console.log(`📋 ${spot.name} (${spot.city}, ${spot.state}) — ${spot.verification_status}`);
  console.log('═'.repeat(70));

  let filled = 0, nulls = 0;
  for (const [k, v] of Object.entries(spot)) {
    if (SKIP.has(k)) continue;
    const isEmpty = v === null || v === undefined || v === '' ||
      v === '{}' || v === '[]' || v === 'null' ||
      (typeof v === 'string' && v === '{"adult":null,"child":null,"senior":null,"spectator":null,"skate_rental":null}');
    const marker = isEmpty ? '  NULL' : '✅    ';
    if (isEmpty) nulls++; else filled++;
    const display = v === null ? '' : typeof v === 'string' && v.length > 80 ? v.slice(0, 80) + '…' : String(v);
    console.log(`  ${marker}  ${k.padEnd(32)} ${display}`);
  }
  console.log(`\n  Fields filled: ${filled}  |  NULLs: ${nulls}  |  Fill rate: ${Math.round(filled/(filled+nulls)*100)}%`);
}

db.close();
