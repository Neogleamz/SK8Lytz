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

// ─── Graceful Shutdown (Anti-Zombie Chrome Defense) ───────────────────────────
const gracefulShutdown = (signal: string) => {
  console.log(`[Indexer] ${signal} received — shutting down gracefully...`);
  process.exit(0);
};
process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));
process.on('SIGINT', () => gracefulShutdown('SIGINT'));

import http from 'http';

function safePost(urlStr: string, body: any): Promise<void> {
  return new Promise<void>((resolve) => {
    try {
      const url = new URL(urlStr);
      const postData = JSON.stringify(body);
      const req = http.request({
        hostname: url.hostname,
        port: url.port || 80,
        path: url.pathname,
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Content-Length': Buffer.byteLength(postData)
        },
        timeout: 5000
      }, (res) => {
        res.resume();
        resolve();
      });
      req.on('error', () => resolve());
      req.on('timeout', () => { req.destroy(); resolve(); });
      req.write(postData);
      req.end();
    } catch {
      resolve();
    }
  });
}

function safeGet(urlStr: string, fallback: any = {}): Promise<any> {
  return new Promise<any>((resolve) => {
    try {
      const url = new URL(urlStr);
      const req = http.request({
        hostname: url.hostname,
        port: url.port || 80,
        path: url.pathname,
        method: 'GET',
        timeout: 5000
      }, (res) => {
        let data = '';
        res.on('data', (chunk) => { data += chunk; });
        res.on('end', () => {
          try {
            resolve(JSON.parse(data));
          } catch {
            resolve(fallback);
          }
        });
      });
      req.on('error', () => resolve(fallback));
      req.on('timeout', () => { req.destroy(); resolve(fallback); });
      req.end();
    } catch {
      resolve(fallback);
    }
  });
}

// ─── Telemetry Hook to CCTower ─────────────────────────────────────────────
const _log = console.log;
const _err = console.error;

let logQueue: { type: string; source: string; message: string }[] = [];
let flushTimeout: any = null;

const queueLog = (type: string, source: string, message: string) => {
  logQueue.push({ type, source, message });
  if (!flushTimeout) {
    flushTimeout = setTimeout(flushLogQueue, 100);
  }
};

const flushLogQueue = () => {
  flushTimeout = null;
  if (logQueue.length === 0) return;
  const batch = [...logQueue];
  logQueue = [];
  safePost('http://localhost:5999/api/logs/ingest', batch).catch(() => {});
};

const pushLog = (type: 'INFO' | 'ERROR', message: string) => {
  queueLog(type, 'Phase 2', message);
};

const reportPulse = (delayMs: number, ghost?: any, active_job?: string | null, target_url?: string | null) => {
  safePost('http://localhost:5999/api/pulse', { source: 'Phase 2', delayMs, ghost, active_job, target: target_url });
};

const onStream = (text: string) => {
  queueLog('LLM_STREAM', 'Phase 2', text);
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
      const statusRes = await safeGet('http://localhost:5999/status', { isHeadless: true });
      const configResGlobal = await safeGet('http://localhost:5999/config', { config: {} });
      const aiConfig = configResGlobal.config || {};
      const priorityStates = aiConfig.state_override || [];
      const targetModel = aiConfig.detective_model || 'local-model';

      // ── Pre-flight LM Studio check & auto-start gating ─────────────────
      let lmsOk = false;
      try {
        const lmsStatusRes = await safeGet('http://localhost:5999/api/llm/status', { serverStatus: 'OFF', loadedModels: [] });
        const isServerOn = lmsStatusRes.serverStatus === 'ON';
        const isModelLoaded = lmsStatusRes.loadedModels.includes(targetModel);
        
        // Relax strict gating: let LM Studio JIT load the model if it's off.
        if (isServerOn) {
          lmsOk = true;
        } else {
          console.warn(`[Indexer] ⚠️ LM Studio gatekeeper warning — Server: ${lmsStatusRes.serverStatus}, Model Loaded: ${isModelLoaded ? 'YES' : 'NO'} (${targetModel})`);
          
          if (lmsStatusRes.serverStatus === 'OFF') {
            console.log('[Indexer] Attempting to auto-start LM Studio server...');
            await safePost('http://localhost:5999/api/llm/server/start', {});
          }
          
          if (isServerOn && !isModelLoaded) {
            console.log(`[Indexer] Attempting to auto-load ${targetModel} model...`);
            await safePost('http://localhost:5999/api/llm/model/load', { modelKey: targetModel });
          }
          
          console.log('[Indexer] Waiting 15 seconds for LM Studio to settle...');
          await sleep(15000);
          
          const lmsStatusRecheck = await safeGet('http://localhost:5999/api/llm/status', { serverStatus: 'OFF', loadedModels: [] });
          if (lmsStatusRecheck.serverStatus === 'ON' && lmsStatusRecheck.loadedModels.includes(targetModel)) {
            lmsOk = true;
          }
        }
      } catch (err: any) {
        console.error('[Indexer] LM Studio status pre-flight check crashed:', err.message);
      }

      if (!lmsOk) {
        console.error(`[Indexer] ⛔ PIPELINE GATED: LM Studio is offline. Retrying in 30 seconds...`);
        await safePost('http://localhost:5999/api/pulse', { 
          source: 'Phase 2', 
          delayMs: 30000, 
          active_job: 'GATED: LM Studio Offline',
          target: `GATED: ${targetModel} missing`
        });
        
        await sleep(30000);
        continue;
      }

      let target: any = null;

      // sniper_target_id isolation
      if (aiConfig.sniper_target_id) {
        target = db.prepare(`SELECT * FROM local_spots WHERE id = ?`).get(aiConfig.sniper_target_id);
        if (target && !['SEEDED', 'PENDING'].includes(target.verification_status)) {
          // If we are in sniper mode, we only process it if it's SEEDED. 
          // If it's already past SEEDED, we stop and wait (unless it's being reset)
          target = null;
        }
      }

      if (!target) {
        let query = `SELECT * FROM local_spots WHERE verification_status IN ('SEEDED', 'PENDING')`;
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
      let streamBuffer = '';
      let streamTimeout: any = null;
      const flushStream = () => {
        if (streamBuffer) {
          queueLog('LLM_STREAM', 'Phase 2', streamBuffer);
          streamBuffer = '';
        }
        streamTimeout = null;
      };

      const result = await executeDetective(
        target,
        aiConfig,
        statusRes.isHeadless,
        (msg: string) => console.log(`   ${msg}`),
        (text: string) => {
          streamBuffer += text;
          if (!streamTimeout) {
            streamTimeout = setTimeout(flushStream, 100);
          }
        }
      );

      // Clean up final remaining stream chunks at the end
      if (streamTimeout) {
        clearTimeout(streamTimeout);
      }
      flushStream();

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
        // Merge confidence with existing (preserve user_manual overrides)
        let existingConf: any = {};
        try { existingConf = typeof target.field_confidence === 'string' ? JSON.parse(target.field_confidence) : (target.field_confidence || {}); } catch {}
        const mergedConf = { ...existingConf };
        for (const [k, v] of Object.entries(result.fieldConfidence)) {
          if (!mergedConf[k] || mergedConf[k].source !== 'user_manual') mergedConf[k] = v;
        }
        updateLocalSpot(target.id, {
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          retry_count: (target.retry_count || 0) + 1,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: JSON.stringify({ ...(Object.keys(result.aiMetadata).length > 0 ? result.aiMetadata : (aiMetadata || {})), quality_note: `Low quality score: ${result.qualityScore}/17` }),
          ...(result.candidatePhotos ? { candidate_photos: JSON.stringify(result.candidatePhotos) } : {}),
          field_confidence: JSON.stringify(mergedConf)
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
        'hosts_derby', 'cultural_metadata', 'yelp_url', 'price_range', 'logo_url', 'cover_photo_url',
        'email_addresses'
      ];

      for (const key of fieldsToCheck) {
        const newVal = result.mappedFields ? result.mappedFields[key] : undefined;
        const oldVal = target[key];
        
        const isEmpty = (v: any) => v === null || v === undefined || v === '' || v === 'null' || v === 'NULL' || v === '{}' || v === '[]';
        const sanitize = (v: any) => isEmpty(v) ? null : (typeof v === 'object' ? JSON.stringify(v) : v);

        if (!isEmpty(newVal)) {
          // Fresh AI crawl wins
          finalUpdates[key] = sanitize(newVal);
        }
        // ── Additive-Only: if AI returned nothing, DON'T write anything ──
        // The existing value in the DB stays untouched.
      }

      // Merge confidence with existing (preserve user_manual overrides)
      let existingConf: any = {};
      try { existingConf = typeof target.field_confidence === 'string' ? JSON.parse(target.field_confidence) : (target.field_confidence || {}); } catch {}
      const mergedConf = { ...existingConf };
      for (const [k, v] of Object.entries(result.fieldConfidence)) {
        if (!mergedConf[k] || mergedConf[k].source !== 'user_manual') mergedConf[k] = v;
      }

      try {
        updateLocalSpot(target.id, {
          ...finalUpdates,
          ...(result.candidatePhotos ? { candidate_photos: JSON.stringify(result.candidatePhotos) } : {}),
          ...(result.mappedFields?.ai_metadata ? { ai_metadata: JSON.stringify(result.mappedFields.ai_metadata) } : {}),
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          retry_count: 0,
          last_attempted_at: new Date().toISOString(),
          pipeline_status: '',
          field_confidence: JSON.stringify(mergedConf)
        });
      } catch (updateError: any) {
        console.error('[Indexer] DB write failed after sanitization:', updateError.message);
        updateLocalSpot(target.id, {
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          last_attempted_at: new Date().toISOString(),
          pipeline_status: '',
          ...(result.mappedFields?.ai_metadata ? { ai_metadata: JSON.stringify(result.mappedFields.ai_metadata) } : {}),
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
