/**
 * pipeline-test-snapshot.ts
 * Full-field before/after pipeline quality comparison.
 * Dynamically reads ALL columns from local_spots schema — never goes stale.
 *
 * Usage:
 *   bun run pipeline-test-snapshot.ts before   → snapshot next 10 in Phase 2 queue
 *   bun run pipeline-test-snapshot.ts after    → compare vs baseline + verdict
 */
import { Database } from 'bun:sqlite';
import path from 'path';
import fs from 'fs';

const MODE = process.argv[2] as 'before' | 'after';
if (MODE !== 'before' && MODE !== 'after') {
  console.error('Usage: bun run pipeline-test-snapshot.ts [before|after]');
  process.exit(1);
}

const DB_PATH      = path.resolve(__dirname, '../.scraper-data/scraper.db');
const SNAPSHOT_PATH = path.resolve(__dirname, '../.scraper-data/test-batch-baseline.json');

const db = new Database(DB_PATH);
db.exec('PRAGMA busy_timeout = 10000; PRAGMA journal_mode = WAL;');

// ── Columns to skip — bulky blobs, system ops, or always-filled coords ────────
const SKIP_COLS = new Set([
  // Bulk blobs — too large, not quality indicators
  'raw_data', 'candidate_photos', 'candidate_links', 'ai_metadata',
  'dom_images', 'flyer_urls', 'field_confidence', 'raw_knowledge_panel',
  // System / operational
  'id', 'created_at', 'updated_at', 'last_attempted_at', 'last_enriched_at',
  'last_synced_at', 'retry_count', 'sync_required', 'updated_by',
  'is_deep_crawled', 'is_published', 'is_verified', 'is_featured',
  'pipeline_status', 'supabase_id', 'source',
  // Always filled at seed time (Phase 1 guaranteed)
  'lat', 'lng', 'name', 'city', 'state', 'zip', 'street_address',
  'google_place_id', 'google_maps_url', 'business_status',
  'rating', 'user_ratings_total', 'verification_status',
  'facility_type', 'has_adult_night',
]);

// ── Dynamically resolve tracked fields from schema ────────────────────────────
type ColInfo = { name: string };
const allCols = (db.prepare('PRAGMA table_info(local_spots)').all() as ColInfo[]).map(c => c.name);
const TRACKED = allCols.filter(c => !SKIP_COLS.has(c));

// ── Empty-value detector (handles all the nasty edge cases) ──────────────────
const EMPTY_PRICING = '{"adult":null,"child":null,"senior":null,"spectator":null,"skate_rental":null}';
function isEmpty(v: unknown): boolean {
  if (v === null || v === undefined || v === '' || v === 'null') return true;
  if (typeof v === 'string') {
    const t = v.trim();
    if (t === '{}' || t === '[]' || t === EMPTY_PRICING) return true;
    try {
      const p = JSON.parse(t);
      if (Array.isArray(p) && p.length === 0) return true;
      if (typeof p === 'object' && p !== null && Object.values(p).every(x => x === null)) return true;
    } catch { /* not JSON */ }
  }
  return false;
}

type SpotRow = Record<string, unknown>;

function scoreSpot(spot: SpotRow) {
  const nullFields: string[] = [];
  let filled = 0;
  for (const f of TRACKED) {
    if (isEmpty(spot[f])) nullFields.push(f);
    else filled++;
  }
  return { filled, total: TRACKED.length, nullFields };
}

// ─────────────────────────────────────────────────────────────────────────────
if (MODE === 'before') {
  const spots = db.prepare(`
    SELECT * FROM local_spots
    WHERE verification_status IN ('SEEDED', 'PENDING_WEBSITE')
    AND verification_status NOT IN ('REJECTED', 'ON_HOLD')
    ORDER BY last_attempted_at ASC NULLS FIRST
    LIMIT 10
  `).all() as SpotRow[];

  if (spots.length === 0) {
    console.log('⚠️  No SEEDED spots in queue. Pipeline may already be running or empty.');
    process.exit(0);
  }

  const baseline = spots.map(spot => ({
    id: spot.id,
    name: spot.name,
    city: spot.city,
    state: spot.state,
    verification_status: spot.verification_status,
    score: scoreSpot(spot),
    fields: TRACKED.reduce((acc, f) => { acc[f] = spot[f] ?? null; return acc; }, {} as Record<string, unknown>)
  }));

  fs.writeFileSync(SNAPSHOT_PATH, JSON.stringify({
    capturedAt: new Date().toISOString(),
    trackedFields: TRACKED,
    spots: baseline
  }, null, 2));

  console.log(`\n📸 BEFORE SNAPSHOT — ${new Date().toLocaleTimeString()}`);
  console.log(`   Tracking ${TRACKED.length} data-quality fields (schema-dynamic)`);
  console.log(`   Captured ${baseline.length} spots from Phase 2 queue\n`);
  console.log('─'.repeat(80));
  console.log(`${'SPOT NAME'.padEnd(36)} ${'ST'.padEnd(4)} ${'FILLED'.padEnd(14)} TOP NULL FIELDS`);
  console.log('─'.repeat(80));

  for (const s of baseline) {
    const pct = Math.round((s.score.filled / s.score.total) * 100);
    const topNulls = s.score.nullFields.slice(0, 4).join(', ');
    console.log(
      `${String(s.name).slice(0, 35).padEnd(36)} ${String(s.state).padEnd(4)} ` +
      `${`${s.score.filled}/${s.score.total} (${pct}%)`.padEnd(14)} ${topNulls}${s.score.nullFields.length > 4 ? `… +${s.score.nullFields.length - 4}` : ''}`
    );
  }

  const avgFilled = baseline.reduce((a, s) => a + s.score.filled, 0) / baseline.length;
  const avgPct = Math.round((avgFilled / TRACKED.length) * 100);
  console.log('─'.repeat(80));
  console.log(`\n   Avg fill rate: ${avgFilled.toFixed(1)} / ${TRACKED.length} fields (${avgPct}%)`);
  console.log(`   Baseline saved → ${SNAPSHOT_PATH}\n`);
  console.log('✅ Start the pipeline. Run "after" when 10 spots complete Phase 2+3.\n');

// ─────────────────────────────────────────────────────────────────────────────
} else {
  if (!fs.existsSync(SNAPSHOT_PATH)) {
    console.error('❌ No baseline found. Run "before" first.');
    process.exit(1);
  }

  const baseline = JSON.parse(fs.readFileSync(SNAPSHOT_PATH, 'utf-8'));
  const ids = baseline.spots.map((s: SpotRow) => s.id) as string[];
  const ph = ids.map(() => '?').join(',');
  const currentSpots = db.prepare(`SELECT * FROM local_spots WHERE id IN (${ph})`).all(...ids) as SpotRow[];
  const cur = new Map(currentSpots.map(s => [s.id, s]));

  console.log(`\n📊 PIPELINE TEST — Full Field Before vs After`);
  console.log(`   Baseline:  ${baseline.capturedAt}`);
  console.log(`   Analyzed:  ${new Date().toISOString()}`);
  console.log(`   Tracking:  ${TRACKED.length} fields\n`);
  console.log('─'.repeat(96));
  console.log(`${'SPOT'.padEnd(32)} ${'STATUS'.padEnd(14)} ${'BEFORE'.padEnd(12)} ${'AFTER'.padEnd(12)} ${'Δ'.padEnd(6)} NEWLY FILLED`);
  console.log('─'.repeat(96));

  let totBefore = 0, totAfter = 0, improved = 0;
  const fieldGains: Record<string, number> = {};
  const fieldLosses: Record<string, number> = {};

  for (const bSpot of baseline.spots) {
    const current = cur.get(bSpot.id);
    if (!current) { console.log(`${'  (not found)'.padEnd(32)} ${String(bSpot.name).slice(0,30)}`); continue; }

    const before = bSpot.score as { filled: number; total: number; nullFields: string[] };
    const after  = scoreSpot(current);
    const delta  = after.filled - before.filled;
    if (delta > 0) improved++;
    totBefore += before.filled;
    totAfter  += after.filled;

    const newlyFilled  = before.nullFields.filter(f => !after.nullFields.includes(f));
    const newlyCleared = after.nullFields.filter(f => !before.nullFields.includes(f));
    newlyFilled.forEach(f  => { fieldGains[f]  = (fieldGains[f]  || 0) + 1; });
    newlyCleared.forEach(f => { fieldLosses[f] = (fieldLosses[f] || 0) + 1; });

    const dStr   = delta > 0 ? `+${delta}` : delta < 0 ? `${delta}` : '=';
    const status = String(current.verification_status || '?');
    console.log(
      `${String(bSpot.name).slice(0, 31).padEnd(32)} ${status.padEnd(14)} ` +
      `${`${before.filled}/${before.total}`.padEnd(12)} ` +
      `${`${after.filled}/${after.total}`.padEnd(12)} ` +
      `${dStr.padEnd(6)} ${newlyFilled.slice(0, 5).join(', ')}${newlyFilled.length > 5 ? `… +${newlyFilled.length - 5}` : ''}`
    );
  }

  const n        = baseline.spots.length;
  const avgBef   = (totBefore / n).toFixed(1);
  const avgAft   = (totAfter  / n).toFixed(1);
  const pctBef   = Math.round((totBefore / (n * TRACKED.length)) * 100);
  const pctAft   = Math.round((totAfter  / (n * TRACKED.length)) * 100);
  const improv   = pctAft - pctBef;

  console.log('─'.repeat(96));
  console.log(`\n   BEFORE avg : ${avgBef}/${TRACKED.length} filled (${pctBef}%)`);
  console.log(`   AFTER  avg : ${avgAft}/${TRACKED.length} filled (${pctAft}%)`);
  console.log(`   Δ avg/spot : +${((totAfter - totBefore) / n).toFixed(1)} fields`);
  console.log(`   Improved   : ${improved}/${n} spots gained new data\n`);

  if (Object.keys(fieldGains).length > 0) {
    console.log('   🏆 Top fields newly filled by pipeline:');
    Object.entries(fieldGains).sort(([,a],[,b]) => b-a).slice(0, 10)
      .forEach(([f, c]) => console.log(`      ✅  ${f.padEnd(32)} ${c}/${n} spots`));
  }

  if (Object.keys(fieldLosses).length > 0) {
    console.log('\n   ⚠️  Fields that became NULL (data regression):');
    Object.entries(fieldLosses).sort(([,a],[,b]) => b-a)
      .forEach(([f, c]) => console.log(`      ❌  ${f.padEnd(32)} ${c}/${n} spots`));
  }

  // Verdict
  console.log('\n' + '═'.repeat(96));
  if (improv >= 15)
    console.log(`   ✅ GREENLIGHT — ${improv}pp improvement (${pctBef}% → ${pctAft}%). Unleash to all records.`);
  else if (improv >= 5)
    console.log(`   ⚠️  MARGINAL  — ${improv}pp improvement. Investigate top NULL fields before full batch.`);
  else if (improv >= 0)
    console.log(`   🔍 MONITOR   — ${improv}pp improvement. Check rejected spots + field extraction quality.`);
  else
    console.log(`   ❌ REGRESSION — ${improv}pp. Data quality DECLINED. Hold and investigate.`);
  console.log('═'.repeat(96) + '\n');
}

db.close();
