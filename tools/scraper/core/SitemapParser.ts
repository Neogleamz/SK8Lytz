/**
 * SitemapParser.ts — Shared Sitemap Discovery Utility
 *
 * Used by BOTH DetectiveEngine (Phase 2) and Photographer (Phase 3).
 * Pure function — NO Puppeteer, NO database, NO side effects.
 *
 * Discovery order:
 *   1. GET /sitemap.xml
 *   2. GET /sitemap_index.xml  → flatten child sitemaps
 *   3. GET /robots.txt         → look for Sitemap: directive
 *   4. Graceful empty fallback — never throws
 *
 * Returns URLs bucketed by content type so each engine can
 * target exactly the pages it needs.
 */

export interface SitemapResult {
  /** Schedule/hours/sessions pages — highest priority for Detective */
  schedule_urls: string[];
  /** Pricing/admission/rates pages — highest priority for Detective */
  pricing_urls: string[];
  /** About/history/facility/story pages */
  about_urls: string[];
  /** Events/parties/adult-night/derby pages */
  events_urls: string[];
  /** Contact/directions/location pages */
  contact_urls: string[];
  /** Gallery/photos/media pages — primary source for Photographer */
  gallery_urls: string[];
  /** All URLs found (for fallback or debugging) */
  all_urls: string[];
}

// ─── Keyword Buckets ──────────────────────────────────────────────────────────

const BUCKET_RULES: Array<{ bucket: keyof Omit<SitemapResult, 'all_urls'>; patterns: RegExp; score: number }> = [
  // Score 10 — Required fields for Detective
  { bucket: 'schedule_urls', patterns: /schedule|hours|session|times|calendar|open.?skate|public.?skate|skate.?time|skating.?schedule/i, score: 10 },
  { bucket: 'pricing_urls',  patterns: /pric|admission|rates|ticket|fees|cost|payment|entry.?fee/i, score: 10 },
  // Score 8 — High value
  { bucket: 'events_urls',   patterns: /event|part(y|ies)|adult.?night|18.?plus|21.?plus|cosmic|derby|birthday|special/i, score: 8 },
  { bucket: 'about_urls',    patterns: /about|history|facilit|rink|story|team|our.?rink|who.?we.?are/i, score: 7 },
  // Score 6 — Optional
  { bucket: 'contact_urls',  patterns: /contact|location|direction|find.?us|get.?here|info/i, score: 6 },
  // Score 10 — Primary for Photographer
  { bucket: 'gallery_urls',  patterns: /gallerh?y|photo|media|image|picture|album|slideshow/i, score: 10 },
];

// Max URLs per bucket to prevent crawl bloat
const MAX_PER_BUCKET = 5;

// ─── Helpers ─────────────────────────────────────────────────────────────────

async function fetchText(url: string, timeoutMs = 8000): Promise<string | null> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (compatible; SK8LytzBot/2.0)' },
      signal: AbortSignal.timeout(timeoutMs),
    });
    if (!res.ok) return null;
    return await res.text();
  } catch {
    return null;
  }
}

function extractLocsFromXml(xml: string): string[] {
  const locs: string[] = [];
  const locRegex = /<loc>(.*?)<\/loc>/gi;
  let match: RegExpExecArray | null;
  while ((match = locRegex.exec(xml)) !== null) {
    const url = match[1].trim();
    if (url) locs.push(url);
  }
  return locs;
}

function extractSitemapUrlsFromIndex(xml: string): string[] {
  // Sitemap index files have <sitemap><loc>...</loc></sitemap> blocks
  const sitemapUrls: string[] = [];
  const sitemapRegex = /<sitemap>[\s\S]*?<loc>(.*?)<\/loc>[\s\S]*?<\/sitemap>/gi;
  let match: RegExpExecArray | null;
  while ((match = sitemapRegex.exec(xml)) !== null) {
    sitemapUrls.push(match[1].trim());
  }
  return sitemapUrls;
}

function scoreAndBucket(urls: string[]): Omit<SitemapResult, 'all_urls'> {
  const buckets: Omit<SitemapResult, 'all_urls'> = {
    schedule_urls: [],
    pricing_urls: [],
    about_urls: [],
    events_urls: [],
    contact_urls: [],
    gallery_urls: [],
  };

  // Score each URL against all bucket rules, assign to best match
  for (const url of urls) {
    // Only test the path portion (after the domain) to avoid false matches on domain names
    let path = url;
    try { path = new URL(url).pathname + new URL(url).search; } catch {}

    let bestBucket: keyof typeof buckets | null = null;
    let bestScore = 0;

    for (const rule of BUCKET_RULES) {
      if (rule.patterns.test(path) && rule.score > bestScore) {
        bestBucket = rule.bucket;
        bestScore = rule.score;
      }
    }

    if (bestBucket && buckets[bestBucket].length < MAX_PER_BUCKET) {
      buckets[bestBucket].push(url);
    }
  }

  return buckets;
}

// ─── Main Export ──────────────────────────────────────────────────────────────

/**
 * Discover and bucket all URLs from a website's sitemap.
 * Returns empty buckets (never throws) if sitemap is unavailable.
 */
export async function parseSitemap(websiteUrl: string): Promise<SitemapResult> {
  const empty: SitemapResult = {
    schedule_urls: [], pricing_urls: [], about_urls: [],
    events_urls: [], contact_urls: [], gallery_urls: [], all_urls: []
  };

  if (!websiteUrl) return empty;

  let origin = '';
  try {
    origin = new URL(websiteUrl).origin;
  } catch {
    return empty;
  }

  let allLocs: string[] = [];

  // ── Attempt 1: /sitemap.xml ───────────────────────────────────────────────
  const sitemapXml = await fetchText(`${origin}/sitemap.xml`);
  if (sitemapXml) {
    // Check if this is a sitemap index (contains <sitemap> elements)
    const isSitemapIndex = /<sitemap>/i.test(sitemapXml);
    if (isSitemapIndex) {
      // Flatten: fetch each child sitemap and extract its locs
      const childUrls = extractSitemapUrlsFromIndex(sitemapXml);
      for (const childUrl of childUrls.slice(0, 5)) { // max 5 child sitemaps
        const childXml = await fetchText(childUrl);
        if (childXml) allLocs.push(...extractLocsFromXml(childXml));
      }
    } else {
      allLocs = extractLocsFromXml(sitemapXml);
    }
  }

  // ── Attempt 2: /sitemap_index.xml (if attempt 1 found nothing) ───────────
  if (allLocs.length === 0) {
    const indexXml = await fetchText(`${origin}/sitemap_index.xml`);
    if (indexXml) {
      const childUrls = extractSitemapUrlsFromIndex(indexXml);
      for (const childUrl of childUrls.slice(0, 5)) {
        const childXml = await fetchText(childUrl);
        if (childXml) allLocs.push(...extractLocsFromXml(childXml));
      }
      if (allLocs.length === 0) {
        allLocs = extractLocsFromXml(indexXml);
      }
    }
  }

  // ── Attempt 3: robots.txt → Sitemap: directive ───────────────────────────
  if (allLocs.length === 0) {
    const robotsTxt = await fetchText(`${origin}/robots.txt`);
    if (robotsTxt) {
      const sitemapDirectiveMatch = robotsTxt.match(/^Sitemap:\s*(.+)$/mi);
      if (sitemapDirectiveMatch) {
        const directiveUrl = sitemapDirectiveMatch[1].trim();
        const directiveXml = await fetchText(directiveUrl);
        if (directiveXml) allLocs = extractLocsFromXml(directiveXml);
      }
    }
  }

  // De-duplicate
  const uniqueLocs = [...new Set(allLocs)];

  if (uniqueLocs.length === 0) return empty;

  const buckets = scoreAndBucket(uniqueLocs);

  return {
    ...buckets,
    all_urls: uniqueLocs,
  };
}
