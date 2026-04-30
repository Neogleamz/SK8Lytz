/**
 * Operator.ts — Phase 2 Spider Daemon (Thin DB Wrapper)
 *
 * This is now a thin orchestrator. All crawling logic lives in:
 *   tools/scraper/core/SpiderEngine.ts
 *
 * This daemon:
 *   1. Picks the next SEEDED spot from the DB via RPC.
 *   2. Calls executeSpider() from SpiderEngine.
 *   3. Writes the resulting candidate_links back to the DB and advances
 *      the verification_status to ENRICHED.
 *
 * ⚠️  DB writes happen here — NOT inside SpiderEngine.
 */

import dotenv from 'dotenv';
import path from 'path';
import { executeSpider, isSocialMediaUrl } from './core/SpiderEngine';
import { db, updateLocalSpot } from './core/LocalDB';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// ─── Telemetry ──────────────────────────────────────────────────────────────
const _log = console.log;
const _err = console.error;
const pushLog = (type: 'INFO' | 'ERROR', message: string) => {
  fetch('http://localhost:5999/api/logs/ingest', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ type, source: 'Phase 2', message }) }).catch(() => {});
};
const reportPulse = (delayMs: number, ghost?: any, active_job?: string | null, target?: string | null) => {
  fetch('http://localhost:5999/api/pulse', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ source: 'Phase 2', delayMs, ghost, active_job, target }) }).catch(() => {});
};
console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

// ─── Pipeline: SEEDED → (Spider) → ENRICHED ─────────────────────────────────
async function runOperator() {
  console.log('[Operator v3] 🕷️  Booting Website Link Spider (SEEDED → ENRICHED)...');
  console.log('[Operator v3] ℹ️  Core spider logic delegated to core/SpiderEngine.ts');

  while (true) {
    let target: any = null;
    let delay = 2000 + Math.random() * 2000;

    try {
      const configRes = await fetch('http://localhost:5999/api/priority-states').then(r => r.json()).catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];

      let query = `SELECT * FROM local_spots WHERE verification_status = 'SEEDED' AND website IS NOT NULL AND website != ''`;
      if (priorityStates.length > 0) {
        query += ` AND state IN (${priorityStates.map((s: string) => `'${s}'`).join(',')})`;
      }
      query += ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`;

      const spots = db.prepare(query).all() as any[];

      if (!spots || spots.length === 0) {
        reportPulse(30000);
        await sleep(30000);
        continue;
      }

      target = spots[0];
      console.log(`\n🕷️  [Spider] ${target.name} (${target.city}, ${target.state})`);
      reportPulse(0, undefined, target.name, target.website || null);

      // Pre-flight burial — prevents re-pick on crash
      updateLocalSpot(target.id, {
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      });

      // GATE: no website → REJECTED (pipeline dead end)
      if (!target.website || target.website.trim() === '') {
        console.log(`   ⚠️  No website. REJECTED.`);
        updateLocalSpot(target.id, { verification_status: 'REJECTED' });
        await sleep(2000);
        continue;
      }

      // Fetch headless preference from CCTower
      const statusRes = await fetch('http://localhost:5999/status').then(r => r.json()).catch(() => ({ isHeadless: true }));

      // ── Delegate to SpiderEngine ────────────────────────────────────────
      const result = await executeSpider(
        target.website,
        statusRes.isHeadless,
        (msg: string) => console.log(`   ${msg}`)
      );

      // Handle social-only record
      if (result.isSocialOnly) {
        const socialUpdate: Record<string, any> = {
          candidate_links: result.candidateLinks,
          verification_status: 'ENRICHED',
        };
        if (result.socialDomain === 'facebook.com') socialUpdate.facebook_url = target.website;
        if (result.socialDomain === 'instagram.com') socialUpdate.instagram_url = target.website;
        if (result.socialDomain === 'tiktok.com') socialUpdate.tiktok_url = target.website;
        updateLocalSpot(target.id, socialUpdate);
        await sleep(1000);
        continue;
      }

      // Handle empty result (navigation failed)
      if (!result.candidateLinks || !result.candidateLinks.root) {
        console.error(`   ✗ STALLED: Spider returned no root URL — cannot advance to Detective.`);
        updateLocalSpot(target.id, {
          verification_status: 'STALLED',
          retry_count: (target.retry_count || 0) + 1,
          last_attempted_at: new Date().toISOString()
        });
        reportPulse(delay);
        continue;
      }

      // ✅ Write result to DB — advance to ENRICHED
      updateLocalSpot(target.id, {
        candidate_links: result.candidateLinks,
        verification_status: 'ENRICHED'
      });

      reportPulse(delay);

    } catch (err: any) {
      console.error('[Operator Error]', err.message);
      delay = Math.max(delay, 45000);
    } finally {
      console.log(`⏳ [GHOST] Cooldown: ${Math.round(delay / 1000)}s`);
      reportPulse(delay, undefined, null, null);
      await sleep(delay);
    }
  }
}

runOperator();
