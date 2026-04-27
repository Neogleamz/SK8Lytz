/**
 * SpiderEngine.ts — Pure Website Link Spider
 *
 * Contains the full Phase 2 (Operator) crawling logic extracted into a stateless,
 * database-free module. The production Operator.ts daemon calls executeSpider()
 * and then performs its own DB writes. The Sniper Bench calls executeSpider()
 * and streams the result via SSE without touching the database.
 *
 * ⚠️  ZERO DB WRITES IN THIS FILE — it is a pure function.
 */

import puppeteer from 'puppeteer';

// ─── Social media domains — crawling these wastes time and yields no useful data ─
const SOCIAL_DOMAINS = [
  'facebook.com', 'instagram.com', 'twitter.com', 'x.com',
  'tiktok.com', 'youtube.com', 'yelp.com', 'google.com', 'linkedin.com', 'snapchat.com'
];

export function isSocialMediaUrl(url: string): string | null {
  try {
    const h = new URL(url).hostname.replace('www.', '');
    return SOCIAL_DOMAINS.find(d => h === d || h.endsWith('.' + d)) || null;
  } catch { return null; }
}

// ─── Link scoring: which subpages are most valuable for the Detective ────────
export const PAGE_SCORE_RULES: { pattern: RegExp; key: string; score: number }[] = [
  { pattern: /hours|schedule|session/i,         key: 'hours',   score: 10 },
  { pattern: /adult.?night|18\+|21\+/i,         key: 'adult',   score: 10 },
  { pattern: /pricing|price|admission|rates/i,  key: 'pricing', score: 9  },
  { pattern: /events?|calendar|upcoming/i,      key: 'events',  score: 8  },
  { pattern: /location|directions|contact/i,    key: 'contact', score: 6  },
  { pattern: /about|info|faq/i,                 key: 'about',   score: 4  },
];

const MAX_CANDIDATE_LINKS = 8;

export interface SpiderResult {
  /** Candidate links map: { root, hours?, pricing?, ... } */
  candidateLinks: Record<string, string>;
  /** The socialDomain if the website IS a social URL (skipped crawl) */
  socialDomain: string | null;
  /** True if the website was skipped due to being a social-only URL */
  isSocialOnly: boolean;
  /** Summary log of what happened */
  summary: string;
}

/**
 * Execute the Phase 2 Spider against a given website URL.
 *
 * @param website     - The root URL to crawl.
 * @param isHeadless  - Whether to launch Puppeteer headless.
 * @param onProgress  - Callback that receives log lines in real-time (for SSE streaming).
 */
export async function executeSpider(
  website: string,
  isHeadless: boolean,
  onProgress: (msg: string) => void = () => {}
): Promise<SpiderResult> {

  // GATE: no website
  if (!website || website.trim() === '') {
    onProgress('[Spider] ⚠️ No website URL provided — cannot crawl.');
    return { candidateLinks: {}, socialDomain: null, isSocialOnly: false, summary: 'No website URL.' };
  }

  // GATE: website IS a social media URL
  const socialDomain = isSocialMediaUrl(website);
  if (socialDomain) {
    onProgress(`[Spider] ⚠️ Website is ${socialDomain} — storing social handle, skipping browser crawl.`);
    return {
      candidateLinks: { _social_only: true } as any,
      socialDomain,
      isSocialOnly: true,
      summary: `Social-only URL (${socialDomain}). No crawl performed.`
    };
  }

  let browser: any = null;
  try {
    onProgress(`[Spider] 🕷️ Launching browser → ${website}`);
    browser = await puppeteer.launch({
      headless: isHeadless ? 'new' : false,
      args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
    });

    const page = await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36');
    await page.setViewport({ width: 1280, height: 800 });

    try {
      await page.goto(website, { waitUntil: 'domcontentloaded', timeout: 30000 });
    } catch {
      onProgress('[Spider] ✗ Navigation failed — site unreachable or timed out.');
      return { candidateLinks: {}, socialDomain: null, isSocialOnly: false, summary: 'Navigation failed.' };
    }

    await new Promise(r => setTimeout(r, 1500));

    // Collect + score internal links
    let hostname = '';
    try { hostname = new URL(website).hostname; } catch { hostname = ''; }

    const rawLinks = await page.evaluate(() =>
      Array.from(document.querySelectorAll('a')).map((a: any) => ({
        href: (a.href || '').toLowerCase().trim(),
        text: (a.innerText || a.getAttribute('aria-label') || '').toLowerCase().trim()
      }))
    ).catch(() => [] as { href: string; text: string }[]);

    await browser.close();
    browser = null;

    const internalLinks = rawLinks.filter((l: any) => {
      if (!l.href || l.href.startsWith('mailto:') || l.href.startsWith('tel:') || l.href.startsWith('javascript:')) return false;
      try {
        const u = new URL(l.href);
        return u.hostname === hostname || u.hostname.includes(hostname.replace('www.', ''));
      } catch { return false; }
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
    const candidateLinks: Record<string, string> = { root: website };
    let count = 1;
    for (const [key, link] of byKey.entries()) {
      if (count >= MAX_CANDIDATE_LINKS) break;
      candidateLinks[key] = link.href;
      count++;
    }

    const linkCount = Object.keys(candidateLinks).length;
    const subpages = linkCount - 1;
    const summary = `Mapped ${linkCount} links: [${Object.keys(candidateLinks).join(', ')}]`;
    onProgress(`[Spider] ✓ ${summary}`);
    if (subpages === 0) {
      onProgress('[Spider] ⚠️ LOW YIELD: Only root URL found — Detective will have limited data.');
    }

    return { candidateLinks, socialDomain: null, isSocialOnly: false, summary };

  } finally {
    if (browser) {
      try { await browser.close(); } catch (_) {}
    }
  }
}
