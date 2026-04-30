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
  console.log('[Indexer v4] 🧠 Booting AI Detective (ENRICHED → DEEP_CRAWLED)...');
  console.log('[Indexer v4] ℹ️  Core AI logic delegated to core/DetectiveEngine.ts');

  while (true) {
    try {
      // Fetch active region config
      const configRes = await fetch('http://localhost:5999/api/priority-states')
        .then(r => r.json())
        .catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];

      let query = `SELECT * FROM local_spots WHERE verification_status = 'ENRICHED'`;
      if (priorityStates.length > 0) {
        query += ` AND state IN (${priorityStates.map((s: string) => `'${s}'`).join(',')})`;
      }
      query += ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`;

      const spots = db.prepare(query).all() as any[];

      if (!spots || spots.length === 0) {
        const delay = 15000;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }

      const target = spots[0];
      console.log(`\n🧠 [Detective] Analyzing: ${target.name} (${target.city}, ${target.state})`);
      reportPulse(0, undefined, target.name, target.website || null);

      // Mark in-flight immediately to prevent duplicate picks
      updateLocalSpot(target.id, {
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      });

      // ── Fetch global AI config and headless preference ─────────────────
      const statusRes = await fetch('http://localhost:5999/status').then(r => r.json()).catch(() => ({ isHeadless: true }));
      const configResGlobal = await fetch('http://localhost:5999/config').then(r => r.json()).catch(() => ({ config: {} }));
      const aiConfig = configResGlobal.config || {};

      let candidateLinks: Record<string, string> = {};
      try {
        candidateLinks = typeof target.candidate_links === 'string' ? JSON.parse(target.candidate_links) : (target.candidate_links || {});
      } catch (e) {
        // ignore parse error
      }

      // ── Delegate to DetectiveEngine ─────────────────────────────────────
      const result = await executeDetective(
        target,
        candidateLinks,
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

      // Handle STALLED (quality gate failed but not toxicity)
      if (!result.passedQualityGate) {
        console.error(`   ⚠️  STALLED: Quality score ${result.qualityScore}/17 — insufficient data extracted.`);
        updateLocalSpot(target.id, {
          verification_status: 'STALLED',
          is_deep_crawled: false,
          retry_count: (target.retry_count || 0) + 1,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: Object.keys(result.aiMetadata).length > 0 ? result.aiMetadata : aiMetadata,
          ...(result.candidatePhotos ? { candidate_photos: result.candidatePhotos } : {})
        });
        continue;
      }

      // ── ✅ Write all fields to DB ─────────────────────────────────────────
      const { mappedFields } = result;
      try {
        updateLocalSpot(target.id, {
          is_indoor:                  mappedFields.is_indoor,
          operator_description:       mappedFields.operator_description,
          operator_name:              mappedFields.operator_name,
          instagram_url:              mappedFields.instagram_url,
          facebook_url:               mappedFields.facebook_url,
          tiktok_url:                 mappedFields.tiktok_url,
          schedule_url:               mappedFields.schedule_url,
          opening_hours:              mappedFields.opening_hours,
          adult_night_schedule:       mappedFields.adult_night_schedule,
          has_adult_night:            mappedFields.has_adult_night,
          adult_night_details:        mappedFields.adult_night_details,
          special_events:             mappedFields.special_events,
          pricing_data:               mappedFields.pricing_data,
          has_fee:                    mappedFields.has_fee,
          surface_type:               mappedFields.surface_type,
          surface_quality:            mappedFields.surface_quality,
          vibe_score:                 mappedFields.vibe_score,
          capacity:                   mappedFields.capacity,
          has_rental:                 mappedFields.has_rental,
          has_pro_shop:               mappedFields.has_pro_shop,
          has_food:                   mappedFields.has_food,
          has_lights:                 mappedFields.has_lights,
          has_lockers:                mappedFields.has_lockers,
          has_ac:                     mappedFields.has_ac,
          has_wifi:                   mappedFields.has_wifi,
          has_toilets:                mappedFields.has_toilets,
          is_wheelchair_accessible:   mappedFields.is_wheelchair_accessible,
          hosts_derby:                mappedFields.hosts_derby,
          cultural_metadata:          mappedFields.cultural_metadata,
          ...(result.candidatePhotos ? { candidate_photos: result.candidatePhotos } : {}),
          ai_metadata: mappedFields.ai_metadata,
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          retry_count: 0,
          last_attempted_at: new Date().toISOString()
        });
      } catch (updateError: any) {
        console.error('[Indexer] DB write failed after sanitization:', updateError.message);
        updateLocalSpot(target.id, {
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: mappedFields.ai_metadata,
          ...(result.candidatePhotos ? { candidate_photos: result.candidatePhotos } : {})
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
