import puppeteer from 'puppeteer';
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import { GHOST } from './lib/GHOST';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '',
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// ─── Telemetry ──────────────────────────────────────────────────────────────
const _log = console.log;
const _err = console.error;
const pushLog = (type: 'INFO'|'ERROR', message: string) => {
  fetch('http://localhost:5999/api/logs/ingest', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ type, source: 'Phase 2', message }) }).catch(() => {});
};
const reportPulse = (delayMs: number, ghost?: any, active_job?: string | null, target?: string | null) => {
  fetch('http://localhost:5999/api/pulse', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ source: 'Phase 2', delayMs, ghost, active_job, target }) }).catch(() => {});
};
console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

// ─── Link scoring: which subpages are most valuable for the Detective ────────
const PAGE_SCORE_RULES: { pattern: RegExp; key: string; score: number }[] = [
  { pattern: /hours|schedule|session/i,         key: 'hours',   score: 10 },
  { pattern: /adult.?night|18\+|21\+/i,         key: 'adult',   score: 10 },
  { pattern: /pricing|price|admission|rates/i,  key: 'pricing', score: 9  },
  { pattern: /events?|calendar|upcoming/i,      key: 'events',  score: 8  },
  { pattern: /location|directions|contact/i,    key: 'contact', score: 6  },
  { pattern: /about|info|faq/i,                 key: 'about',   score: 4  },
];
const MAX_CANDIDATE_LINKS = 8;

// ─── Pipeline: SEEDED → (Spider) → ENRICHED ─────────────────────────────────
async function runOperator() {
  console.log('[Operator v2] 🕷️  Booting Website Link Spider (SEEDED → ENRICHED)...');

  while (true) {
    let target: any = null;
    let browser: any = null;
    let delay = 8000 + Math.random() * 7000; // 8–15s — small business sites, no Google-level stealth needed

    try {
      const configRes = await fetch('http://localhost:5999/api/priority-states').then(r => r.json()).catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];

      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_operator', { priority_states: priorityStates });
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        reportPulse(30000);
        await sleep(30000);
        continue;
      }

      target = spots[0];
      console.log(`\n🕷️  [Spider] ${target.name} (${target.city}, ${target.state})`);
      // Report active job to CCTower telemetry immediately
      reportPulse(0, undefined, target.name, target.website || null);

      // Pre-flight burial — prevents re-pick on crash
      await supabase.from('skate_spots').update({
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      }).eq('id', target.id);

      // GATE: no website → REJECTED (pipeline dead end)
      if (!target.website || target.website.trim() === '') {
        console.log(`   ⚠️  No website. REJECTED.`);
        await supabase.from('skate_spots').update({ verification_status: 'REJECTED' }).eq('id', target.id);
        await sleep(2000);
        continue;
      }

      // Launch browser
      const statusRes = await fetch('http://localhost:5999/status').then(r => r.json()).catch(() => ({ isHeadless: true }));
      const identity = GHOST.generateIdentity();
      browser = await puppeteer.launch({
        headless: statusRes.isHeadless ? 'new' : false,
        args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
      });
      const page = await browser.newPage();
      await page.setUserAgent(identity.userAgent);
      await page.setViewport(identity.viewport);

      console.log(`   [Spider] → ${target.website}`);
      try {
        await page.goto(target.website, { waitUntil: 'domcontentloaded', timeout: 30000 });
      } catch {
        console.error(`   ✗ Navigation failed`);
        await browser.close(); browser = null;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }
      await sleep(1500);

      // Collect + score internal links
      let hostname = '';
      try { hostname = new URL(target.website).hostname; } catch { hostname = ''; }

      const rawLinks = await page.evaluate(() =>
        Array.from(document.querySelectorAll('a')).map(a => ({
          href: (a.href || '').toLowerCase().trim(),
          text: (a.innerText || a.getAttribute('aria-label') || '').toLowerCase().trim()
        }))
      ).catch(() => [] as { href: string; text: string }[]);

      await browser.close(); browser = null;

      const internalLinks = rawLinks.filter((l: any) => {
        if (!l.href || l.href.startsWith('mailto:') || l.href.startsWith('tel:') || l.href.startsWith('javascript:')) return false;
        try { const u = new URL(l.href); return u.hostname === hostname || u.hostname.includes(hostname.replace('www.','')); } catch { return false; }
      });

      type ScoredLink = { href: string; key: string; score: number };
      const scored: ScoredLink[] = [];
      for (const link of internalLinks) {
        let bestScore = 0, bestKey = 'page';
        for (const rule of PAGE_SCORE_RULES) {
          if (rule.score > bestScore && (rule.pattern.test(link.href) || rule.pattern.test(link.text))) {
            bestScore = rule.score; bestKey = rule.key;
          }
        }
        if (bestScore > 0) scored.push({ href: link.href, key: bestKey, score: bestScore });
      }

      scored.sort((a, b) => b.score - a.score);
      const seen = new Set<string>();
      const byKey = new Map<string, ScoredLink>();
      for (const s of scored) {
        if (seen.has(s.href)) continue;
        seen.add(s.href);
        if (!byKey.has(s.key)) byKey.set(s.key, s);
      }

      // Build candidate_links — always include root
      const candidateLinks: Record<string, string> = { root: target.website };
      let count = 1;
      for (const [key, link] of byKey.entries()) {
        if (count >= MAX_CANDIDATE_LINKS) break;
        candidateLinks[key] = link.href;
        count++;
      }

      console.log(`   ✓ [Spider] ${Object.keys(candidateLinks).length} links: [${Object.keys(candidateLinks).join(', ')}]`);

      // SEEDED → ENRICHED
      const { error: updateError } = await supabase.from('skate_spots').update({
        candidate_links: candidateLinks,
        verification_status: 'ENRICHED'
      }).eq('id', target.id);
      if (updateError) throw updateError;

      reportPulse(delay, identity);

    } catch (err: any) {
      console.error('[Operator Error]', err.message);
      delay = Math.max(delay, 45000);
    } finally {
      if (browser) { try { await (browser as any).close(); } catch (e) {} }
      console.log(`⏳ [GHOST] Cooldown: ${Math.round(delay/1000)}s`);
      reportPulse(delay, undefined, null, null); // clear active job during cooldown
      await sleep(delay);
    }
  }
}

runOperator();
