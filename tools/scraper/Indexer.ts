/**
 * Indexer.ts — Phase 3 Detective Daemon (Thin DB Wrapper)
 *
 * This is now a thin orchestrator. All AI extraction logic lives in:
 *   tools/scraper/core/DetectiveEngine.ts
 *
 * This daemon:
 *   1. Picks the next ENRICHED spot from the DB via RPC.
 *   2. Fetches global AI config from CCTower.
 *   3. Calls executeDetective() from DetectiveEngine.
 *   4. Maps the result to DB columns and writes to the DB.
 *   5. Advances the verification_status to DEEP_CRAWLED or STALLED.
 *
 * ⚠️  DB writes happen here — NOT inside DetectiveEngine.
 */

import dotenv from 'dotenv';
import path from 'path';
import { executeDetective } from './core/DetectiveEngine';
import { db, updateLocalSpot } from './core/LocalDB';

if (process.env.SCRAPER_REGISTER_ONLY === 'true') {
  console.log('[Indexer] PM2 registration mode: exiting immediately.');
  process.exit(0);
}

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// ─── Telemetry Hook to CCTower ─────────────────────────────────────────────
const _log = console.log;
const _err = console.error;

const pushLog = (type: 'INFO' | 'ERROR', message: string) => {
  fetch('http://localhost:5999/api/logs/ingest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ type, source: 'Phase 3', message })
  }).catch(() => {});
};

const reportPulse = (delayMs: number, ghost?: any, active_job?: string | null, target_url?: string | null) => {
  fetch('http://localhost:5999/api/pulse', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source: 'Phase 3', delayMs, ghost, active_job, target: target_url })
  }).catch(() => {});
};

console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

// ─── Main Indexer Loop ──────────────────────────────────────────────────────
async function runIndexer() {
  console.log('[Indexer v4] 🧠 Booting AI Detective (SEEDED → DEEP_CRAWLED)...');
  console.log('[Indexer v4] ℹ️  Core AI logic delegated to core/DetectiveEngine.ts');

  while (true) {
    try {
      // ── Fetch global AI config and headless preference ─────────────────
      const statusRes = await fetch('http://localhost:5999/status').then(r => r.json()).catch(() => ({ isHeadless: true }));
      const configResGlobal = await fetch('http://localhost:5999/config').then(r => r.json()).catch(() => ({ config: {} }));
      const aiConfig = configResGlobal.config || {};
      const priorityStates = aiConfig.state_override || [];

      let target: any = null;

      // sniper_target_id isolation
      if (aiConfig.sniper_target_id) {
        target = db.prepare(`SELECT * FROM local_spots WHERE id = ?`).get(aiConfig.sniper_target_id);
        if (target && target.verification_status !== 'SEEDED') {
          // If we are in sniper mode, we only process it if it's SEEDED. 
          // If it's already past SEEDED, we stop and wait (unless it's being reset)
          target = null;
        }
      }

      if (!target) {
        let query = `SELECT * FROM local_spots WHERE verification_status = 'SEEDED'`;
        if (priorityStates.length > 0) {
          query += ` AND state IN (${priorityStates.map((s: string) => `'${s}'`).join(',')})`;
        }
        query += ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`;
        target = db.prepare(query).get();
      }

      if (!target) {
        const delay = 15000;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }
      console.log(`\n🧠 [Detective] Analyzing: ${target.name} (${target.city}, ${target.state})`);
      reportPulse(0, undefined, target.name, target.website || null);

      // Mark in-flight immediately to prevent duplicate picks
      updateLocalSpot(target.id, {
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      });

      // ── Delegate to DetectiveEngine ─────────────────────────────────────

      // ── Delegate to DetectiveEngine ─────────────────────────────────────
      const result = await executeDetective(
        target,
        aiConfig,
        statusRes.isHeadless,
        (msg: string) => console.log(`   ${msg}`)
      );

      // Handle toxicity abort (quality gate = 0, passedQualityGate = false from toxicity)
      const wasToxicityAborted = !result.passedQualityGate && result.qualityScore === 0 && Object.keys(result.aiMetadata).length === 0;
      if (wasToxicityAborted) {
        updateLocalSpot(target.id, {
          is_deep_crawled: true,
          verification_status: 'REJECTED',
          last_attempted_at: new Date().toISOString()
        });
        continue;
      }

      let aiMetadata: any = null;
      try {
        aiMetadata = typeof target.ai_metadata === 'string' ? JSON.parse(target.ai_metadata) : (target.ai_metadata || null);
      } catch (e) {}

      // Handle quality gate failure — still emit DEEP_CRAWLED so Photographer gets a shot.
      // Low quality is flagged via ai_metadata.quality_note, not by blocking the pipeline.
      if (!result.passedQualityGate) {
        console.error(`   ⚠️  Low quality (${result.qualityScore}/17) → promoting to DEEP_CRAWLED for Photographer.`);
        updateLocalSpot(target.id, {
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          retry_count: (target.retry_count || 0) + 1,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: JSON.stringify({ ...(Object.keys(result.aiMetadata).length > 0 ? result.aiMetadata : (aiMetadata || {})), quality_note: `Low quality score: ${result.qualityScore}/17` }),
          ...(result.candidatePhotos ? { candidate_photos: JSON.stringify(result.candidatePhotos) } : {})
        });
        continue;
      }

      // ── ✅ Write all fields to DB (Non-Destructive Merge) ───────────────
      const finalUpdates: any = {};
      
      const fieldsToCheck = [
        'is_indoor', 'operator_description', 'operator_name', 'instagram_url', 'facebook_url',
        'tiktok_url', 'schedule_url', 'opening_hours', 'adult_night_schedule', 'has_adult_night',
        'adult_night_details', 'special_events', 'pricing_data', 'has_fee', 'surface_type',
        'surface_quality', 'vibe_score', 'capacity', 'has_rental', 'has_pro_shop', 'has_food',
        'has_lights', 'has_lockers', 'has_ac', 'has_wifi', 'has_toilets', 'is_wheelchair_accessible',
        'hosts_derby', 'cultural_metadata', 'yelp_url', 'price_range', 'logo_url', 'cover_photo_url'
      ];

      for (const key of fieldsToCheck) {
        const newVal = result.mappedFields ? result.mappedFields[key] : undefined;
        const oldVal = target[key];
        
        const isEmpty = (v: any) => v === null || v === undefined || v === '' || v === 'null' || v === 'NULL' || v === '{}' || v === '[]';
        const sanitize = (v: any) => isEmpty(v) ? null : (typeof v === 'object' ? JSON.stringify(v) : v);

        if (!isEmpty(newVal)) {
          // Fresh AI crawl wins
          finalUpdates[key] = sanitize(newVal);
        } else if (!isEmpty(oldVal)) {
          // Keep existing good data, but sanitize it in case it was a "null" string
          finalUpdates[key] = sanitize(oldVal);
        } else {
          finalUpdates[key] = null;
        }
      }

      try {
        updateLocalSpot(target.id, {
          ...finalUpdates,
          ...(result.candidatePhotos ? { candidate_photos: JSON.stringify(result.candidatePhotos) } : {}),
          ai_metadata: result.mappedFields?.ai_metadata ? JSON.stringify(result.mappedFields.ai_metadata) : null,
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          retry_count: 0,
          last_attempted_at: new Date().toISOString(),
          pipeline_status: ''
        });
      } catch (updateError: any) {
        console.error('[Indexer] DB write failed after sanitization:', updateError.message);
        updateLocalSpot(target.id, {
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          last_attempted_at: new Date().toISOString(),
          pipeline_status: '',
          ai_metadata: result.mappedFields?.ai_metadata ? JSON.stringify(result.mappedFields.ai_metadata) : null,
          ...(result.candidatePhotos ? { candidate_photos: JSON.stringify(result.candidatePhotos) } : {})
        });
      }

      const delay = 2000 + Math.random() * 2000;
      reportPulse(delay);
      await sleep(delay);

    } catch (err: any) {
      console.error('[Indexer Error]', err.message);
      const delay = 30000;
      reportPulse(delay);
      await sleep(delay);
    }
  }
}

runIndexer();
