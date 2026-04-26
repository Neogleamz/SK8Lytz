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

// ─── Telemetry Hook to CCTower ──────────────────────────────────────────────
const _log = console.log;
const _err = console.error;

const pushLog = (type: 'INFO' | 'ERROR', message: string) => {
  fetch('http://localhost:5999/api/logs/ingest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ type, source: 'Phase 2', message })
  }).catch(() => {});
};

const reportPulse = (delayMs: number, ghost?: any) => {
  fetch('http://localhost:5999/api/pulse', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source: 'Phase 2', delayMs, ghost })
  }).catch(() => {});
};

console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

// ─── Page Scoring: Keywords that indicate high-value subpages ───────────────
// The Operator scores internal links by their URL/text content.
// High-scoring pages get prioritised into candidate_links for the Detective.
const PAGE_SCORE_RULES: { pattern: RegExp; key: string; score: number }[] = [
  { pattern: /hours|schedule|session/i,  key: 'hours',    score: 10 },
  { pattern: /adult.?night|18\+|21\+/i,  key: 'adult',    score: 10 },
  { pattern: /pricing|price|admission|rates|cost/i, key: 'pricing', score: 9 },
  { pattern: /events?|calendar|upcoming/i, key: 'events', score: 8 },
  { pattern: /location|directions|map|contact/i, key: 'contact', score: 6 },
  { pattern: /about|info|faq/i,           key: 'about',   score: 4 },
];

const MAX_CANDIDATE_LINKS = 8; // Max URLs to store per record

// ─── Main Operator Loop ─────────────────────────────────────────────────────
async function runOperator() {
  console.log('[Operator v2] 🕷️  Booting Website Link Spider...');

  while (true) {
    let target: any = null;
    let browser: any = null;
    let delay = await GHOST.getAdaptiveDelay('GOOGLE');

    try {
      // Fetch active region config — hard state filter applied at RPC level
      const configRes = await fetch('http://localhost:5999/api/priority-states')
        .then(r => r.json())
        .catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];

      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_operator', {
        priority_states: priorityStates
      });
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        delay = 30000;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }

      target = spots[0];
      console.log(`\n🕷️  [Operator] Spidering: ${target.name} (${target.city}, ${target.state})`);

      // PRE-FLIGHT: Bury record in queue immediately to prevent duplicate picks
      await supabase.from('skate_spots').update({
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      }).eq('id', target.id);

      // ─── GATE: No website → mark MISSING_WEBSITE and skip ─────────────────
      if (!target.website || target.website.trim() === '') {
        console.log(`   ⚠️  No website URL. Marking MISSING_WEBSITE.`);
        await supabase.from('skate_spots').update({
          verification_status: 'MISSING_WEBSITE',
          is_deep_crawled: true
        }).eq('id', target.id);
        reportPulse(delay);
        await sleep(2000); // Short pause — no browser needed
        continue;
      }

      // ─── SPIDER: Crawl website, collect and score internal links ───────────
      const statusRes = await fetch('http://localhost:5999/status')
        .then(r => r.json())
        .catch(() => ({ isHeadless: true }));

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
        console.error(`   ✗ Navigation failed for ${target.website}`);
        await browser.close();
        browser = null;
        // Keep ENRICHED status — will retry on next loop with buried last_attempted_at
        reportPulse(delay);
        await sleep(delay);
        continue;
      }

      await sleep(1500);

      // ─── Collect all internal links from the page ──────────────────────────
      let hostname = '';
      try { hostname = new URL(target.website).hostname; } catch { hostname = ''; }

      const rawLinks = await page.evaluate(() => {
        return Array.from(document.querySelectorAll('a')).map(a => ({
          href: (a.href || '').toLowerCase().trim(),
          text: (a.innerText || a.getAttribute('aria-label') || '').toLowerCase().trim()
        }));
      }).catch(() => [] as { href: string; text: string }[]);

      await browser.close();
      browser = null;

      // Filter to internal links only — no mailto, tel, anchors, external domains
      const internalLinks = rawLinks.filter(l => {
        if (!l.href || l.href.startsWith('mailto:') || l.href.startsWith('tel:') || l.href.startsWith('javascript:')) return false;
        try {
          const url = new URL(l.href);
          return url.hostname === hostname || url.hostname.includes(hostname.replace('www.', ''));
        } catch { return false; }
      });

      // ─── Score each internal link ──────────────────────────────────────────
      type ScoredLink = { href: string; key: string; score: number };
      const scored: ScoredLink[] = [];

      for (const link of internalLinks) {
        let bestScore = 0;
        let bestKey = 'page';
        for (const rule of PAGE_SCORE_RULES) {
          if (rule.pattern.test(link.href) || rule.pattern.test(link.text)) {
            if (rule.score > bestScore) {
              bestScore = rule.score;
              bestKey = rule.key;
            }
          }
        }
        if (bestScore > 0) {
          scored.push({ href: link.href, key: bestKey, score: bestScore });
        }
      }

      // Deduplicate by href, keep highest score per key category
      const seen = new Set<string>();
      const byKey = new Map<string, ScoredLink>();
      scored.sort((a, b) => b.score - a.score);
      for (const s of scored) {
        if (seen.has(s.href)) continue;
        seen.add(s.href);
        if (!byKey.has(s.key)) byKey.set(s.key, s);
      }

      // Build candidate_links object — always include root
      const candidateLinks: Record<string, string> = { root: target.website };
      let count = 1;
      for (const [key, link] of byKey.entries()) {
        if (count >= MAX_CANDIDATE_LINKS) break;
        candidateLinks[key] = link.href;
        count++;
      }

      const linkSummary = Object.keys(candidateLinks).join(', ');
      console.log(`   ✓ [Spider] Collected ${Object.keys(candidateLinks).length} candidate links: [${linkSummary}]`);

      // ─── Write candidate_links + promote to IDENTITY_ESTABLISHED ──────────
      const { error: updateError } = await supabase.from('skate_spots').update({
        candidate_links: candidateLinks,
        verification_status: 'IDENTITY_ESTABLISHED'
      }).eq('id', target.id);

      if (updateError) throw updateError;

      reportPulse(delay, identity);

    } catch (err: any) {
      console.error('[Operator Error]', err.message);
      if (target) {
        console.error(`   ↳ Failed on: ${target?.name} — ${err.message.slice(0, 200)}`);
      }
      delay = Math.max(delay, 45000);
    } finally {
      if (browser) {
        try { await (browser as any).close(); } catch (e) {}
      }
      console.log(`⏳ [GHOST] Stealth Cooldown: ${Math.round(delay / 1000)}s...`);
      reportPulse(delay);
      await sleep(delay);
    }
  }
}

runOperator();
