/**
 * SitemapParser.ts — Shared Sitemap Discovery + Content Fingerprinting Utility
 *
 * Used by BOTH DetectiveEngine (Phase 2) and Photographer (Phase 3).
 * Pure function — NO Puppeteer, NO database, NO side effects.
 *
 * Discovery order:
 *   1. GET /sitemap.xml
 *   2. GET /sitemap_index.xml  → flatten child sitemaps
 *   3. GET /robots.txt         → look for Sitemap: directive
 *   4. Full 2-level shallow spider with content fingerprinting
 *   5. Graceful empty fallback — never throws
 *
 * Content Fingerprinting:
 *   Each discovered URL is classified using 4 signal layers:
 *     - URL path keywords
 *     - Anchor text (how other pages link TO this page)
 *     - Page <title> tag
 *     - <h1>/<h2>/<h3> headings on the page
 *
 *   MULTI-LABEL: A URL can appear in MULTIPLE buckets simultaneously.
 *   A "Plan Your Visit" page with hours + pricing + directions ends up
 *   in schedule_urls AND pricing_urls AND contact_urls.
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
  // Score 10 - High Priority Events
  { bucket: 'events_urls',   patterns: /adult.?night|18.?plus|21.?plus|cosmic|derby|upcoming.?events|special.?events/i, score: 10 },
  { bucket: 'events_urls',   patterns: /event/i, score: 9 },
  // Score 8 — High value
  { bucket: 'events_urls',   patterns: /part(y|ies)|birthday|special/i, score: 7 },
  { bucket: 'about_urls',    patterns: /about|history|facilit|rink|story|team|our.?rink|who.?we.?are/i, score: 7 },
  // Score 10 — Required for email capture
  { bucket: 'contact_urls',  patterns: /contact|location|direction|find.?us|get.?here|info|email|reach.?us|connect/i, score: 10 },
  // Score 10 — Primary for Photographer
  { bucket: 'gallery_urls',  patterns: /gallerh?y|photo|media|image|picture|album|slideshow/i, score: 10 },
];

// Max URLs per bucket to prevent crawl bloat
const MAX_PER_BUCKET = 5;

// ─── Content Fingerprint Types ────────────────────────────────────────────────

interface PageFingerprint {
  url: string;
  /** Anchor text(s) from pages that link TO this URL */
  anchorTexts: string[];
  /** <title> tag content, if fetched */
  title: string;
  /** All <h1>, <h2>, <h3> heading text, if fetched */
  headings: string[];
  /** Number of large images on the page (gallery signal) */
  imageCount: number;
}

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
  const sitemapUrls: string[] = [];
  const sitemapRegex = /<sitemap>[\s\S]*?<loc>(.*?)<\/loc>[\s\S]*?<\/sitemap>/gi;
  let match: RegExpExecArray | null;
  while ((match = sitemapRegex.exec(xml)) !== null) {
    sitemapUrls.push(match[1].trim());
  }
  return sitemapUrls;
}

/** Extract lightweight content signals from raw HTML */
function extractPageMeta(html: string): { title: string; headings: string[]; imageCount: number } {
  // <title> tag
  const titleMatch = html.match(/<title[^>]*>([^<]+)<\/title>/i);
  const title = (titleMatch?.[1] || '').trim();

  // All <h1>, <h2>, <h3> headings
  const headings: string[] = [];
  const headingRegex = /<h[1-3][^>]*>([^<]*(?:<[^/][^>]*>[^<]*)*)<\/h[1-3]>/gi;
  let hMatch: RegExpExecArray | null;
  while ((hMatch = headingRegex.exec(html)) !== null) {
    // Strip any inner tags from heading text
    const text = hMatch[1].replace(/<[^>]+>/g, '').trim();
    if (text.length > 1 && text.length < 200) headings.push(text);
  }

  // Count images with src attributes (gallery signal)
  const imgMatches = html.match(/<img\s+[^>]*src=["'][^"']+["']/gi);
  const imageCount = imgMatches ? imgMatches.length : 0;

  return { title, headings, imageCount };
}

// ─── Multi-Label Content-Aware Scoring ────────────────────────────────────────

/**
 * Score page fingerprints against ALL bucket rules, MULTI-LABEL.
 * A single URL can appear in multiple buckets if its content matches.
 *
 * 4 signal layers, weighted:
 *   - URL path:     1x (weakest — opaque slugs like /page-3 have no signal)
 *   - Anchor text:  2x (what other pages call this page)
 *   - Page title:   3x (what the site owner named the page)
 *   - Headings:     3x (what sections are ON the page — catches mixed pages)
 */
function scoreAndBucketFingerprints(fingerprints: PageFingerprint[]): Omit<SitemapResult, 'all_urls'> {
  const buckets: Omit<SitemapResult, 'all_urls'> = {
    schedule_urls: [], pricing_urls: [], about_urls: [],
    events_urls: [], contact_urls: [], gallery_urls: []
  };

  const scoredBuckets: Record<keyof typeof buckets, Array<{url: string; score: number}>> = {
    schedule_urls: [], pricing_urls: [], about_urls: [],
    events_urls: [], contact_urls: [], gallery_urls: []
  };

  // Track which URLs already added to which bucket (dedup)
  const seen: Record<keyof typeof buckets, Set<string>> = {
    schedule_urls: new Set(), pricing_urls: new Set(), about_urls: new Set(),
    events_urls: new Set(), contact_urls: new Set(), gallery_urls: new Set()
  };

  for (const fp of fingerprints) {
    // Build the combined text signals
    let urlPath = fp.url;
    try { urlPath = new URL(fp.url).pathname; } catch {}
    const anchorText = fp.anchorTexts.join(' ');
    const headingsText = fp.headings.join(' ');

    // Test EACH bucket rule against ALL signal layers
    for (const rule of BUCKET_RULES) {
      let totalScore = 0;

      // Layer 1: URL path (1x weight)
      if (rule.patterns.test(urlPath)) totalScore += rule.score * 1;

      // Layer 2: Anchor text (2x weight)
      if (anchorText && rule.patterns.test(anchorText)) totalScore += rule.score * 2;

      // Layer 3: Page title (3x weight)
      if (fp.title && rule.patterns.test(fp.title)) totalScore += rule.score * 3;

      // Layer 4: Headings (3x weight — catches mixed pages with multiple h1/h2)
      if (headingsText && rule.patterns.test(headingsText)) totalScore += rule.score * 3;

      // Threshold: need at least 1 signal match
      if (totalScore > 0 && !seen[rule.bucket].has(fp.url)) {
        // Bonus: gallery pages with many images get a boost
        if (rule.bucket === 'gallery_urls' && fp.imageCount >= 5) {
          totalScore += 15;
        }

        scoredBuckets[rule.bucket].push({ url: fp.url, score: totalScore });
        seen[rule.bucket].add(fp.url);
      }
    }
  }

  // Sort by score descending and truncate
  for (const key of Object.keys(scoredBuckets) as Array<keyof typeof buckets>) {
    buckets[key] = scoredBuckets[key]
      .sort((a, b) => b.score - a.score)
      .slice(0, MAX_PER_BUCKET)
      .map(x => x.url);
  }

  return buckets;
}

/**
 * Legacy fallback: score URLs by path only (for sitemap.xml/robots.txt URLs
 * that we haven't fetched HTML for). Still multi-label.
 */
function scoreAndBucket(urls: string[]): Omit<SitemapResult, 'all_urls'> {
  return scoreAndBucketFingerprints(
    urls.map(url => ({ url, anchorTexts: [], title: '', headings: [], imageCount: 0 }))
  );
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
    const isSitemapIndex = /<sitemap>/i.test(sitemapXml);
    if (isSitemapIndex) {
      const childUrls = extractSitemapUrlsFromIndex(sitemapXml);
      for (const childUrl of childUrls.slice(0, 5)) {
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

  // ── Attempts 1-3 found sitemap URLs → path-only scoring ──────────────────
  if (allLocs.length > 0) {
    const uniqueLocs = [...new Set(allLocs)];
    const buckets = scoreAndBucket(uniqueLocs);
    return { ...buckets, all_urls: uniqueLocs };
  }

  // ── Attempt 4: Full spider + content fingerprinting ──────────────────────
  // When no sitemap exists, spider the site and classify pages using
  // multi-layer content signals (URL path + anchor text + title + headings).
  // Most rink sites have 10-20 pages — we can fingerprint every single one.

  const MAX_SPIDER_URLS = 40;
  const fingerprints: Map<string, PageFingerprint> = new Map();

  /** Resolve and normalize a URL, return null if invalid/off-origin */
  const resolveUrl = (href: string): string | null => {
    if (!href || href.startsWith('javascript:') || href.startsWith('mailto:') || href.startsWith('tel:')) return null;
    if (href.startsWith('/')) href = origin + href;
    else if (!href.startsWith('http')) href = origin + '/' + href;
    try { if (new URL(href).origin !== origin) return null; } catch { return null; }
    // Normalize: strip trailing slash, query params, fragments
    return href.replace(/\/+$/, '').split('?')[0].split('#')[0] || null;
  };

  /** Extract all same-origin links WITH their anchor text from HTML */
  const extractLinksWithAnchors = (html: string): Array<{ href: string; anchorText: string }> => {
    const linkRegex = /<a\s+[^>]*href=["']([^"'#]+)["'][^>]*>([\s\S]*?)<\/a>/gi;
    let m: RegExpExecArray | null;
    const links: Array<{ href: string; anchorText: string }> = [];
    while ((m = linkRegex.exec(html)) !== null) {
      const resolved = resolveUrl(m[1]);
      if (!resolved) continue;
      // Strip HTML tags from anchor text
      const anchorText = m[2].replace(/<[^>]+>/g, '').replace(/\s+/g, ' ').trim();
      links.push({ href: resolved, anchorText });
    }
    return links;
  };

  /** Get or create a fingerprint entry for a URL */
  const getFingerprint = (url: string): PageFingerprint => {
    if (!fingerprints.has(url)) {
      fingerprints.set(url, { url, anchorTexts: [], title: '', headings: [], imageCount: 0 });
    }
    return fingerprints.get(url)!;
  };

  // Level 1: Fetch homepage, discover all top-level links with anchor text
  const homepageHtml = await fetchText(origin);
  if (homepageHtml) {
    // Fingerprint the homepage itself
    const homeFp = getFingerprint(origin);
    const homeMeta = extractPageMeta(homepageHtml);
    homeFp.title = homeMeta.title;
    homeFp.headings = homeMeta.headings;
    homeFp.imageCount = homeMeta.imageCount;

    // Extract all links with their anchor text
    const level1Links = extractLinksWithAnchors(homepageHtml);
    const level1Urls: string[] = [];
    for (const { href, anchorText } of level1Links) {
      if (fingerprints.size >= MAX_SPIDER_URLS) break;
      const fp = getFingerprint(href);
      if (anchorText && !fp.anchorTexts.includes(anchorText)) {
        fp.anchorTexts.push(anchorText);
      }
      if (!level1Urls.includes(href)) level1Urls.push(href);
    }

    // Level 2: Fetch each discovered page — extract title, headings, images, AND sub-links
    for (const pageUrl of level1Urls.slice(0, 25)) {
      if (fingerprints.size >= MAX_SPIDER_URLS) break;
      if (pageUrl === origin) continue;

      const pageHtml = await fetchText(pageUrl, 5000);
      if (!pageHtml) continue;

      // Fingerprint this page with its actual content
      const pageFp = getFingerprint(pageUrl);
      const pageMeta = extractPageMeta(pageHtml);
      pageFp.title = pageMeta.title;
      pageFp.headings = pageMeta.headings;
      pageFp.imageCount = pageMeta.imageCount;

      // Extract sub-links (Level 2 discovery) with anchor text
      const level2Links = extractLinksWithAnchors(pageHtml);
      for (const { href, anchorText } of level2Links) {
        if (fingerprints.size >= MAX_SPIDER_URLS) break;
        const subFp = getFingerprint(href);
        if (anchorText && !subFp.anchorTexts.includes(anchorText)) {
          subFp.anchorTexts.push(anchorText);
        }
      }
    }
  }

  if (fingerprints.size === 0) return empty;

  const allFingerprints = [...fingerprints.values()];
  const buckets = scoreAndBucketFingerprints(allFingerprints);

  return {
    ...buckets,
    all_urls: allFingerprints.map(fp => fp.url),
  };
}
