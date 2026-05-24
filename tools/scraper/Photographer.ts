/**
 * Photographer.ts — Phase 3 AI Photo Harvest Daemon (v3 Category-Intent Rebuild)
 * sharp: WebP compression at quality 82 — ~65% smaller than raw JPEG
 *
 * Strategy:
 *   Collect 6 specific assets per spot — not 20 random images.
 *   logo     → spot avatar (logo_url field, separate from photos[])
 *   exterior → outside the building
 *   interior → inside the rink
 *   floor    → skating surface (stored for future vision model analysis of floor type)
 *   pro_shop → optional, only if easy to find
 *   action   → real people skating at THIS rink (not stock photos)
 *
 * Collection order per category:
 *   Pass 1 → Own website (Puppeteer, 5-signal context capture)
 *   Pass 2 → Google Places refs (10 refs, up from 5)
 *   Pass 3 → Instagram grid (action fallback, graceful fail if blocked)
 *   Pass 4 → Yelp tab-specific (interior/pro_shop last resort only)
 *
 * Classification:
 *   Step 1 → Keyword fast-path on 5 context signals (zero cost)
 *   Step 2 → LLM text classification for ambiguous images (same text model, no VRAM swap)
 *
 * photos[] is now an array of {url, type, source, confidence, signals} objects.
 * photo_coverage field tracks per-category fill status.
 */

import dotenv from 'dotenv';
import path from 'path';
import fs from 'fs';
import fetch from 'node-fetch';
import puppeteer from 'puppeteer';
import crypto from 'crypto';
import sharp from 'sharp';
import { db, updateLocalSpot } from './core/LocalDB';

const envPaths = [
  path.resolve(__dirname, '../../.env'),
  path.resolve(process.cwd(), '.env'),
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.env',
];
for (const p of envPaths) { const r = dotenv.config({ path: p }); if (!r.error) break; }

const PHOTOS_DIR = path.resolve(__dirname, '../../.scraper-data/photos');
if (!fs.existsSync(PHOTOS_DIR)) fs.mkdirSync(PHOTOS_DIR, { recursive: true });

const PHOTO_SERVE_BASE = 'http://localhost:5999/api/photos';
const LM_STUDIO_URL = process.env.LM_STUDIO_URL || 'http://localhost:1234/v1/chat/completions';
const LM_STUDIO_MODEL = process.env.LM_STUDIO_MODEL || 'qwen2.5-7b-instruct';
const LOOP_COOLDOWN_MS = 800;
const MIN_FILE_SIZE_BYTES = 30 * 1024;  // 30KB minimum before compression
const MIN_IMG_WIDTH = 400;
const MIN_IMG_HEIGHT = 300;
const MAX_ACTION_PHOTOS = 4;
const MAX_INTERIOR_PHOTOS = 3;
const MAX_PHOTOS_TOTAL = 30;           // hard cap per spot (category fills first, then overflow for manual tagging)
const OVERFLOW_MAX = 20;               // extra unknown photos collected after categories are filled
const WEBP_QUALITY = 82;              // WebP compression quality — ~65% smaller than raw JPEG
const MAX_OUTPUT_DIMENSION = 1800;    // max width or height — downscale only, never upscale

// Stock photo domains — auto-reject action category images from these
const STOCK_PHOTO_DOMAINS = [
  'shutterstock', 'gettyimages', 'istock', 'unsplash', 'dreamstime',
  'depositphotos', 'adobestock', 'alamy', 'bigstock',
];

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

// ─── Types ────────────────────────────────────────────────────────────────────

export type PhotoCategory = 'exterior' | 'interior' | 'floor' | 'pro_shop' | 'action' | 'logo' | 'unknown';

export interface ImageCandidate {
  url: string;
  alt: string;
  filename: string;       // last path segment of URL
  parentClass: string;
  nearestHeading: string; // closest H2/H3 above the image in DOM
  pageUrl: string;        // which page this image was found on
  pageSource: string;     // 'website_gallery' | 'website_contact' | 'google_places' | 'yelp_inside' | 'instagram' etc.
  estimatedSize?: number;
  provisionalType?: PhotoCategory | null;
  typeConfidence?: number;
}

export interface SavedPhoto {
  url: string;            // local serve URL
  type: PhotoCategory;
  source: string;
  confidence: number;     // 0.0–1.0
  signals: string[];      // what triggered classification
}

export interface PhotoCoverage {
  logo: boolean;
  exterior: boolean;
  interior: boolean;
  floor: boolean;
  pro_shop: boolean;
  action: boolean;
}

// ─── Graceful Shutdown ────────────────────────────────────────────────────────

let activeBrowser: any = null;
const gracefulShutdown = async (signal: string) => {
  console.log(`[Photographer] ${signal} received — cleaning up Puppeteer...`);
  if (activeBrowser) { try { await activeBrowser.close(); } catch {} }
  process.exit(0);
};
process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));
process.on('SIGINT', () => gracefulShutdown('SIGINT'));

// ─── Logging ──────────────────────────────────────────────────────────────────

const reportPulse = (delayMs: number, active_job: string | null = null, target: string | null = null) => {
  fetch('http://localhost:5999/api/pulse', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ source: 'Photographer', delayMs, active_job, target }) }).catch(() => {});
};

let logQueue: { type: string; source: string; message: string }[] = [];
let flushTimeout: any = null;
const queueLog = (type: string, source: string, message: string) => {
  logQueue.push({ type, source, message });
  if (!flushTimeout) flushTimeout = setTimeout(flushLogQueue, 100);
};
const flushLogQueue = () => {
  flushTimeout = null;
  if (logQueue.length === 0) return;
  const batch = [...logQueue]; logQueue = [];
  fetch('http://localhost:5999/api/logs/ingest', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(batch) }).catch(() => {});
};
const log = (type: 'INFO' | 'ERROR', message: string) => {
  queueLog(type, 'Photographer', message);
  console.log(`${type === 'ERROR' ? '❌' : '📸'} [Photographer] ${message}`);
};

// ─── Category Keyword Maps ────────────────────────────────────────────────────

const CATEGORY_KEYWORDS: Record<PhotoCategory, { strong: string[]; medium: string[] }> = {
  floor: {
    strong: ['floor', 'surface', 'hardwood', 'hardwood-floor', 'skating-surface', 'rink-floor', 'skating-floor', 'wood-floor'],
    medium: ['rink', 'skate', 'smooth', 'maple', 'poly', 'sport-court', 'concrete-floor'],
  },
  exterior: {
    strong: ['exterior', 'outside', 'outside-view', 'building', 'entrance', 'front', 'parking', 'facade', 'frontage', 'storefront'],
    medium: ['arrive', 'location', 'directions', 'contact', 'find-us', 'parking-lot', 'building-exterior'],
  },
  interior: {
    strong: ['interior', 'inside', 'inside-view', 'inside-the-rink', 'facility', 'lobby', 'inside-view'],
    medium: ['arena', 'center', 'complex', 'concourse', 'hall', 'main-floor', 'our-rink', 'the-rink'],
  },
  pro_shop: {
    strong: ['pro-shop', 'proshop', 'skate-shop', 'shop', 'merchandise', 'gear', 'retail', 'skates-for-sale'],
    medium: ['equipment', 'accessories', 'wheels', 'boots', 'store'],
  },
  action: {
    strong: ['skaters', 'skating', 'action', 'crowd', 'people-skating', 'glow-skate', 'cosmic', 'fun', 'skate-night'],
    medium: ['birthday', 'party', 'event', 'kids', 'family', 'friends', 'night', 'session', 'open-skate'],
  },
  logo: {
    strong: ['logo', 'brand', 'icon', 'apple-touch-icon', 'favicon'],
    medium: ['header-logo', 'site-logo', 'brand-logo'],
  },
  unknown: { strong: [], medium: [] },
};

// ─── Classification: Keyword Fast-Path ───────────────────────────────────────

function inferPhotoTypeFromKeywords(candidate: ImageCandidate): { type: PhotoCategory; confidence: number; signals: string[] } {
  const scores: Record<PhotoCategory, number> = {
    floor: 0, exterior: 0, interior: 0, pro_shop: 0, action: 0, logo: 0, unknown: 0,
  };
  const matchedSignals: string[] = [];

  // Reject stock photos for action category
  if (STOCK_PHOTO_DOMAINS.some(d => candidate.url.includes(d))) {
    return { type: 'unknown', confidence: 1.0, signals: ['stock:domain-rejected'] };
  }

  const signalTexts: Array<{ label: string; text: string; weight: number }> = [
    { label: 'filename', text: candidate.filename.toLowerCase(), weight: 3 },
    { label: 'alt',      text: candidate.alt.toLowerCase(),      weight: 3 },
    { label: 'heading',  text: candidate.nearestHeading.toLowerCase(), weight: 2 },
    { label: 'class',    text: candidate.parentClass.toLowerCase(), weight: 1 },
    { label: 'page',     text: candidate.pageUrl.toLowerCase(),   weight: 2 },
  ];

  for (const [cat, kws] of Object.entries(CATEGORY_KEYWORDS) as Array<[PhotoCategory, { strong: string[]; medium: string[] }]>) {
    if (cat === 'unknown') continue;
    for (const sig of signalTexts) {
      for (const kw of kws.strong) {
        if (sig.text.includes(kw)) {
          scores[cat] += sig.weight * 2;
          matchedSignals.push(`${sig.label}:${kw}`);
        }
      }
      for (const kw of kws.medium) {
        if (sig.text.includes(kw)) {
          scores[cat] += sig.weight;
          matchedSignals.push(`${sig.label}:${kw}`);
        }
      }
    }
  }

  // Source-based boosts (pageSource tells us a lot)
  if (candidate.pageSource === 'google_refs') { scores.interior += 2; scores.action += 2; scores.floor += 2; }
  if (candidate.pageSource === 'yelp_inside') { scores.interior += 5; }
  if (candidate.pageSource === 'yelp_food')   { scores.pro_shop += 2; }
  if (candidate.pageSource === 'instagram')   { scores.action += 4; }
  if (candidate.pageSource === 'website_contact') { scores.exterior += 3; }
  if (candidate.pageSource === 'website_og') { scores.floor += 3; scores.interior += 2; }

  const sorted = (Object.entries(scores) as Array<[PhotoCategory, number]>)
    .filter(([, v]) => v > 0)
    .sort((a, b) => b[1] - a[1]);

  if (sorted.length === 0) return { type: 'unknown', confidence: 0.0, signals: [] };

  const [bestCat, bestScore] = sorted[0];
  const secondScore = sorted[1]?.[1] ?? 0;
  const confidence = bestScore >= 6 ? 0.95 : bestScore >= 4 ? 0.80 : bestScore >= 2 ? 0.65 : 0.40;
  const isAmbiguous = secondScore > 0 && bestScore - secondScore < 2;

  return {
    type: isAmbiguous && confidence < 0.7 ? 'unknown' : bestCat,
    confidence,
    signals: [...new Set(matchedSignals)].slice(0, 5),
  };
}

// ─── Classification: LLM Text Pass (for ambiguous images) ────────────────────

async function inferPhotoTypeLLM(candidate: ImageCandidate): Promise<{ type: PhotoCategory; confidence: number } | null> {
  try {
    const prompt = `You are classifying images for a roller skating rink venue database.
Based ONLY on the following context signals (not the image itself), classify this image into exactly ONE category.

Filename: ${candidate.filename}
Alt text: ${candidate.alt || 'none'}
Nearest page heading: ${candidate.nearestHeading || 'none'}
Parent CSS class: ${candidate.parentClass || 'none'}
Page URL: ${candidate.pageUrl || 'none'}
Source: ${candidate.pageSource}

Categories:
- floor: The skating surface / rink floor (wood, concrete, sport court)
- exterior: Outside the building, entrance, parking lot, building facade
- interior: Inside the rink, lobby, general indoor views
- pro_shop: Skate shop, merchandise, gear for sale
- action: People skating, crowds having fun AT THIS specific rink
- unknown: Cannot determine from context

Respond with ONLY valid JSON. Example: {"type":"floor","confidence":0.85}`;

    const res = await fetch(LM_STUDIO_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        model: LM_STUDIO_MODEL,
        messages: [{ role: 'user', content: prompt }],
        temperature: 0.1,
        max_tokens: 60,
      }),
      signal: AbortSignal.timeout(12000),
    });
    if (!res.ok) return null;
    const data = await res.json() as any;
    const text = data?.choices?.[0]?.message?.content?.trim() || '';
    const match = text.match(/\{[^}]+\}/);
    if (!match) return null;
    const parsed = JSON.parse(match[0]);
    const validTypes: PhotoCategory[] = ['floor', 'exterior', 'interior', 'pro_shop', 'action', 'unknown'];
    if (!validTypes.includes(parsed.type)) return null;
    return { type: parsed.type as PhotoCategory, confidence: parseFloat(parsed.confidence) || 0.5 };
  } catch {
    return null;
  }
}

// ─── Combined Classification ──────────────────────────────────────────────────

async function classifyImage(candidate: ImageCandidate): Promise<{ type: PhotoCategory; confidence: number; signals: string[] }> {
  const kwResult = inferPhotoTypeFromKeywords(candidate);

  // If keyword fast-path is confident enough → skip LLM
  if (kwResult.confidence >= 0.75 || kwResult.type === 'unknown' && kwResult.signals.includes('stock:domain-rejected')) {
    return kwResult;
  }

  // Otherwise try LLM for ambiguous cases
  const llmResult = await inferPhotoTypeLLM(candidate);
  if (llmResult && llmResult.confidence > kwResult.confidence) {
    return { type: llmResult.type, confidence: llmResult.confidence, signals: [...kwResult.signals, 'llm:text-classifier'] };
  }

  return kwResult;
}

// ─── Download Helpers ─────────────────────────────────────────────────────────

async function downloadImage(url: string): Promise<{ buffer: Buffer; mimeType: string } | null> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (compatible; SK8LytzBot/3.0)' },
      signal: AbortSignal.timeout(15000),
    });
    if (!res.ok) return null;
    const contentType = res.headers.get('content-type') || '';
    if (!contentType.startsWith('image/')) return null;
    const buf = await res.buffer();
    if (buf.length < MIN_FILE_SIZE_BYTES) return null;
    return { buffer: buf, mimeType: contentType };
  } catch { return null; }
}

/**
 * Compress image to WebP via sharp and save to disk.
 * - Converts any format (JPEG/PNG/GIF) to WebP at WEBP_QUALITY (82)
 * - Downscales if width or height exceeds MAX_OUTPUT_DIMENSION (never upscales)
 * - De-duplicates by SHA-256 content hash of the ORIGINAL buffer
 * - Returns the local serve URL or null on failure/duplicate
 */
async function compressAndSave(buf: Buffer, spotId: string, state: string, filename: string): Promise<string | null> {
  try {
    const dir = path.join(PHOTOS_DIR, state || 'US', spotId);
    if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });

    // De-duplicate by original content hash (before compression)
    const hash = crypto.createHash('sha256').update(buf).digest('hex');
    const existingFiles = fs.readdirSync(dir);
    for (const file of existingFiles) {
      try {
        // Compare hashes stored in companion .hash files for speed
        const hashFile = path.join(dir, file + '.hash');
        if (fs.existsSync(hashFile) && fs.readFileSync(hashFile, 'utf8').trim() === hash) {
          return null; // duplicate
        }
      } catch {}
    }

    // Compress: resize if too large, convert to WebP
    const compressed = await sharp(buf)
      .resize(MAX_OUTPUT_DIMENSION, MAX_OUTPUT_DIMENSION, {
        fit: 'inside',
        withoutEnlargement: true, // never upscale
      })
      .webp({ quality: WEBP_QUALITY, effort: 4 })
      .toBuffer();

    const finalFilename = `${filename}.webp`;
    const finalPath = path.join(dir, finalFilename);
    fs.writeFileSync(finalPath, compressed);
    // Write companion hash file for fast dedup on future runs
    fs.writeFileSync(finalPath + '.hash', hash);

    const savedKB = Math.round(compressed.length / 1024);
    const originalKB = Math.round(buf.length / 1024);
    log('INFO', `  🗜️ ${filename}: ${originalKB}KB → ${savedKB}KB WebP (${Math.round((1 - savedKB / originalKB) * 100)}% saved)`);

    return `${PHOTO_SERVE_BASE}/${state || 'US'}/${spotId}/${finalFilename}`;
  } catch (e: any) {
    log('ERROR', `compressAndSave failed for ${filename}: ${e.message}`);
    return null;
  }
}

// ─── Logo Extraction ──────────────────────────────────────────────────────────

async function extractLogo(websiteUrl: string, spotId: string, state: string): Promise<string | null> {
  let browser: any = null;
  try {
    browser = await puppeteer.launch({ headless: 'new', args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage'] });
    activeBrowser = browser;
    const page = await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
    await page.goto(websiteUrl, { waitUntil: 'domcontentloaded', timeout: 20000 });

    const logoUrl: string | null = await page.evaluate(() => {
      // Priority 1: apple-touch-icon — designed for square icon display, perfect for avatars
      const touch = document.querySelector('link[rel="apple-touch-icon"]') as HTMLLinkElement;
      if (touch?.href) return touch.href;

      // Priority 2: large PNG favicon
      const icons = Array.from(document.querySelectorAll('link[rel~="icon"]')) as HTMLLinkElement[];
      const large = icons
        .filter(i => i.sizes?.value?.match(/\d+x\d+/))
        .sort((a, b) => {
          const sizeA = parseInt(a.sizes.value.split('x')[0]) || 0;
          const sizeB = parseInt(b.sizes.value.split('x')[0]) || 0;
          return sizeB - sizeA;
        })[0];
      if (large?.href) return large.href;

      // Priority 3: og:logo meta
      const ogLogo = document.querySelector('meta[property="og:logo"]') as HTMLMetaElement;
      if (ogLogo?.content) return ogLogo.content;

      // Priority 4: JSON-LD logo
      for (const el of Array.from(document.querySelectorAll('script[type="application/ld+json"]'))) {
        try {
          const ld = JSON.parse((el as HTMLElement).innerText);
          const logo = ld?.logo || ld?.organization?.logo || ld?.publisher?.logo;
          if (logo) return typeof logo === 'string' ? logo : logo?.url || null;
        } catch {}
      }

      // Priority 5: img with class/alt containing "logo" and decent size
      const imgs = Array.from(document.querySelectorAll('img')) as HTMLImageElement[];
      const logoImg = imgs.find(i =>
        (i.alt?.toLowerCase().includes('logo') || (i.className || '').toLowerCase().includes('logo')) &&
        (i.naturalWidth || i.width || 0) >= 80
      );
      if (logoImg?.src) return logoImg.src;

      // Priority 6: any favicon as fallback
      const anyFav = document.querySelector('link[rel~="icon"]') as HTMLLinkElement;
      return anyFav?.href || null;
    });

    if (!logoUrl) return null;

    const dl = await downloadImage(logoUrl);
    if (!dl) return null;
    return await compressAndSave(dl.buffer, spotId, state, 'logo');
  } catch (e: any) {
    log('INFO', `Logo extraction failed: ${e.message}`);
    return null;
  } finally {
    if (browser) { try { await browser.close(); } catch {} }
    activeBrowser = null;
  }
}

// ─── Puppeteer Category-Aware Crawl ──────────────────────────────────────────

async function crawlPageForImages(urls: string[], pageSourceLabel: string): Promise<ImageCandidate[]> {
  if (urls.length === 0) return [];
  const candidates: ImageCandidate[] = [];
  let browser: any = null;
  try {
    browser = await puppeteer.launch({ headless: 'new', args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage'] });
    activeBrowser = browser;
    const page = await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
    await page.setViewport({ width: 1280, height: 900 });

    for (const url of urls) {
      try {
        await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 25000 });
        // Auto-scroll to trigger lazy-load
        await page.evaluate(async () => {
          await new Promise(resolve => {
            let total = 0; const dist = 150; let scrolls = 0;
            const timer = setInterval(() => {
              window.scrollBy(0, dist); total += dist; scrolls++;
              if (total >= document.body.scrollHeight || scrolls >= 40) { clearInterval(timer); resolve(true); }
            }, 120);
          });
        });
        await sleep(600);

        const found: ImageCandidate[] = await page.evaluate((source: string, pageUrlStr: string, minW: number, minH: number) => {
          const getFilename = (urlStr: string): string => {
            try { return decodeURIComponent(new URL(urlStr).pathname.split('/').pop() || '').replace(/[-_]/g, ' ').replace(/\.\w+$/, ''); }
            catch { return ''; }
          };
          const getNearestHeading = (el: Element): string => {
            let cur: Element | null = el.parentElement;
            while (cur && cur !== document.body) {
              const h = cur.querySelector('h1,h2,h3,h4');
              if (h) return (h.textContent || '').trim().slice(0, 80);
              // also look at preceding siblings
              let prev = cur.previousElementSibling;
              while (prev) {
                if (prev.tagName?.match(/^H[1-4]$/)) return (prev.textContent || '').trim().slice(0, 80);
                prev = prev.previousElementSibling;
              }
              cur = cur.parentElement;
            }
            return '';
          };

          const results: any[] = [];

          // OG image — often the rink's best hero shot (floor or interior)
          const og = document.querySelector('meta[property="og:image"]')?.getAttribute('content');
          if (og) results.push({ url: og, alt: 'og:image', filename: getFilename(og), parentClass: 'meta', nearestHeading: '', pageUrl: pageUrlStr, pageSource: source.includes('homepage') ? 'website_og' : source });

          // JSON-LD images
          document.querySelectorAll('script[type="application/ld+json"]').forEach((el: any) => {
            try {
              const ld = JSON.parse(el.innerText);
              const imgs = Array.isArray(ld.image) ? ld.image : (ld.image ? [ld.image] : []);
              imgs.forEach((img: any) => {
                const u = typeof img === 'string' ? img : img?.url;
                if (u) results.push({ url: u, alt: 'json-ld', filename: getFilename(u), parentClass: 'ld+json', nearestHeading: '', pageUrl: pageUrlStr, pageSource: source });
              });
            } catch {}
          });

          // IMG tags with 5-signal context capture
          document.querySelectorAll('img').forEach((img: any) => {
            const w = img.naturalWidth || img.width || 0;
            const h = img.naturalHeight || img.height || 0;
            const src = img.src || img.dataset?.src || img.dataset?.lazySrc || '';
            if (!src || !src.startsWith('http')) return;

            const isIcon = /icon|logo|sprite|spacer|star|button|avatar|thumbnail.*small|px[^a-z]|1x1|tracking/i.test(src);
            if (isIcon) return;

            const isHighRes = /uploads|gallery|media|photos|rink|skate|original|images\/.*\.(jpg|jpeg|png|webp)/i.test(src) ||
              src.includes('places/photo') || src.includes('wixstatic.com/media') || src.includes('squarespace-cdn.com') ||
              src.includes('googleusercontent.com');

            const passesSize = w >= minW && h >= minH;
            const passesLazy = w === 0 && h === 0 && isHighRes;

            if (passesSize || passesLazy) {
              results.push({
                url: src,
                alt: (img.alt || '').toLowerCase().slice(0, 120),
                filename: getFilename(src),
                parentClass: (img.closest('[class]')?.className || '').toLowerCase().slice(0, 80),
                nearestHeading: getNearestHeading(img),
                pageUrl: pageUrlStr,
                pageSource: source,
              });
            }
          });

          // CSS background images on hero/slider/gallery/rink containers
          document.querySelectorAll('[class*="hero"],[class*="slider"],[class*="gallery"],[class*="banner"],[class*="carousel"],[class*="rink"],[class*="photo"],[class*="interior"],[class*="exterior"],[class*="floor"]').forEach((el: any) => {
            const bg = window.getComputedStyle(el).backgroundImage;
            const match = bg?.match(/url\(["']?(https?[^"')]+)["']?\)/);
            if (match?.[1]) {
              results.push({
                url: match[1],
                alt: 'css-background',
                filename: getFilename(match[1]),
                parentClass: (el.className || '').toLowerCase().slice(0, 80),
                nearestHeading: getNearestHeading(el),
                pageUrl: pageUrlStr,
                pageSource: source,
              });
            }
          });

          return results;
        }, pageSourceLabel + ':' + url, url, MIN_IMG_WIDTH, MIN_IMG_HEIGHT);

        candidates.push(...found);
        log('INFO', `  📷 ${pageSourceLabel} ${url.slice(0, 60)} → ${found.length} candidates`);
      } catch (e: any) {
        log('INFO', `  ⚠️ Crawl failed for ${url.slice(0, 60)}: ${e.message}`);
      }
    }
  } finally {
    if (browser) { try { await browser.close(); } catch {} }
    activeBrowser = null;
  }

  // Normalize URL dedup (strip CDN resize params)
  const seenNorm = new Set<string>();
  const unique: ImageCandidate[] = [];
  for (const c of candidates) {
    try {
      const u = new URL(c.url);
      ['width', 'height', 'w', 'h', 'resize', 'size', 'v', 'cache', 'dpr', 'fit', 'crop', 'quality', 'q', 'auto', 'format'].forEach(p => u.searchParams.delete(p));
      const norm = u.origin + u.pathname.toLowerCase().replace(/[-_]\d+x\d+/g, '').replace(/[-_](scaled|large|medium|thumbnail|thumb|small|big|hero)/g, '').replace(/\.(jpg|jpeg|png|webp|gif|svg)$/i, '');
      if (!seenNorm.has(norm)) { seenNorm.add(norm); unique.push(c); }
    } catch { unique.push(c); }
  }
  return unique;
}

// ─── Instagram Grid Scrape (action fallback) ──────────────────────────────────

async function scrapeInstagramGrid(instagramUrl: string): Promise<ImageCandidate[]> {
  let browser: any = null;
  try {
    browser = await puppeteer.launch({ headless: 'new', args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage'] });
    activeBrowser = browser;
    const page = await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
    await page.goto(instagramUrl, { waitUntil: 'domcontentloaded', timeout: 20000 });
    await sleep(2000);

    // Check if we hit a login wall
    const isBlocked = await page.evaluate(() =>
      !!(document.querySelector('[data-testid="login-form"]') || document.querySelector('form[id="loginForm"]'))
    );
    if (isBlocked) { log('INFO', '  📵 Instagram login wall — skipping gracefully'); return []; }

    const images: ImageCandidate[] = await page.evaluate((source: string, pageUrlStr: string) => {
      const results: any[] = [];
      // Instagram grid images are in article > img or _aagu class
      document.querySelectorAll('article img, ._aagu img, main img').forEach((img: any) => {
        const src = img.src || img.dataset?.src || '';
        if (src && src.startsWith('http') && !src.includes('profile_pic') && (img.naturalWidth || img.width || 0) >= 100) {
          results.push({
            url: src,
            alt: (img.alt || '').toLowerCase().slice(0, 120),
            filename: 'instagram-photo',
            parentClass: 'instagram-grid',
            nearestHeading: '',
            pageUrl: pageUrlStr,
            pageSource: source,
          });
        }
      });
      return results.slice(0, 9); // Max 9 grid posts
    }, 'instagram', instagramUrl);

    log('INFO', `  📸 Instagram grid → ${images.length} candidates`);
    return images;
  } catch (e: any) {
    log('INFO', `  📵 Instagram scrape failed (graceful): ${e.message}`);
    return [];
  } finally {
    if (browser) { try { await browser.close(); } catch {} }
    activeBrowser = null;
  }
}

// ─── Yelp Tab-Specific Scrape ─────────────────────────────────────────────────

async function scrapeYelpTab(yelpUrl: string, tab: 'inside' | 'food'): Promise<ImageCandidate[]> {
  const tabUrl = `${yelpUrl.replace(/\/$/, '')}/photos?tab=${tab}`;
  try {
    return await crawlPageForImages([tabUrl], `yelp_${tab}`);
  } catch { return []; }
}

// ─── Main Collection Loop ─────────────────────────────────────────────────────

async function runPhotographerLoop() {
  if (process.env.SCRAPER_REGISTER_ONLY === 'true') {
    console.log('[Photographer] PM2 registration mode: exiting immediately.');
    process.exit(0);
  }
  log('INFO', '🚀 Photographer v3 daemon starting — Category-Intent Photo Collection active');
  let consecutiveIdle = 0;

  while (true) {
    const configRes = await fetch('http://localhost:5999/api/priority-states').then((r: any) => r.json()).catch(() => ({ priority_states: [] }));
    const priorityStates: string[] = configRes.priority_states || [];
    const aiConfig = await fetch('http://localhost:5999/config').then((r: any) => r.json()).then((d: any) => d.config || {}).catch(() => ({}));

    let target: any = null;

    if (aiConfig.sniper_target_id) {
      target = db.prepare(`SELECT id, name, state, website, candidate_photos, photos, logo_url, cover_photo_url, instagram_url, yelp_url, verification_status, retry_count, photo_coverage FROM local_spots WHERE id = ?`).get(aiConfig.sniper_target_id);
      if (target && target.verification_status !== 'DEEP_CRAWLED') target = null;
    }

    if (!target) {
      let query = `SELECT id, name, state, website, candidate_photos, photos, logo_url, cover_photo_url, instagram_url, yelp_url, verification_status, retry_count, photo_coverage FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' AND website IS NOT NULL`;
      if (priorityStates.length > 0) query += ` AND state IN (${priorityStates.map((s: string) => `'${s}'`).join(',')})`;
      query += ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`;
      try { target = db.prepare(query).get() as any; } catch {}
    }

    if (!target) {
      try { target = db.prepare(`SELECT id, name, state, website, candidate_photos, photos, logo_url, cover_photo_url, instagram_url, yelp_url, verification_status, retry_count, photo_coverage FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`).get() as any; } catch {}
    }

    if (!target) {
      consecutiveIdle++;
      if (consecutiveIdle === 1) log('INFO', '⏸️ Queue empty — idling...');
      reportPulse(30000, 'IDLE', null);
      await sleep(30000);
      continue;
    }

    consecutiveIdle = 0;

    // Crash gate: never silently promote an unprocessed record to MEDIA_READY.
    // A spot that crashed 3x has no photos — promoting it bypasses Phase 3 entirely.
    // Set STALLED so it's visible in the dashboard and can be manually reviewed/restarted.
    if ((target.retry_count || 0) >= 3) {
      log('ERROR', `🚫 ${target.name} failed ${target.retry_count}x — marking STALLED (not promoted to MEDIA_READY)`);
      updateLocalSpot(target.id, { verification_status: 'STALLED', last_attempted_at: new Date().toISOString() });
      await sleep(LOOP_COOLDOWN_MS);
      continue;
    }

    // Optimistic touch gate — push to back of queue if we crash
    updateLocalSpot(target.id, { last_attempted_at: new Date().toISOString(), retry_count: (target.retry_count || 0) + 1 });

    log('INFO', `📷 Processing: ${target.name} (${target.state}) [${target.id}]`);
    reportPulse(0, 'Collecting Photos', target.name);

    let candidates: any = {};
    try { candidates = typeof target.candidate_photos === 'string' ? JSON.parse(target.candidate_photos) : (target.candidate_photos || {}); } catch {}

    // Parse existing photos (may be old string[] format or new object[] format)
    let existingPhotos: SavedPhoto[] = [];
    try {
      const raw = typeof target.photos === 'string' ? JSON.parse(target.photos) : (target.photos || []);
      if (Array.isArray(raw)) {
        existingPhotos = raw.map((p: any) => typeof p === 'string' ? { url: p, type: 'unknown' as PhotoCategory, source: 'legacy', confidence: 0.5, signals: [] } : p);
      }
    } catch {}

    // Parse existing coverage
    let coverage: PhotoCoverage = { logo: !!target.logo_url, exterior: false, interior: false, floor: false, pro_shop: false, action: false };
    try {
      const existing = typeof target.photo_coverage === 'string' ? JSON.parse(target.photo_coverage) : (target.photo_coverage || {});
      coverage = { ...coverage, ...existing };
    } catch {}

    // Count existing photos by category
    const photosByType: Record<PhotoCategory, SavedPhoto[]> = { exterior: [], interior: [], floor: [], pro_shop: [], action: [], logo: [], unknown: [] };
    for (const p of existingPhotos) { photosByType[p.type || 'unknown']?.push(p); }

    const savedPhotos: SavedPhoto[] = [...existingPhotos];
    const websiteUrl = target.website || '';
    let allWebsiteCandidates: ImageCandidate[] = []; // hoisted for overflow pass

    // ── LOGO (Priority 0) ─────────────────────────────────────────────────────
    if (!target.logo_url && !coverage.logo && websiteUrl) {
      log('INFO', '  🏷️ Extracting logo...');
      const logoUrl = await extractLogo(websiteUrl, target.id, target.state || 'US');
      if (logoUrl) {
        coverage.logo = true;
        log('INFO', `  ✅ Logo saved: ${logoUrl}`);
        updateLocalSpot(target.id, { logo_url: logoUrl });
      }
    }

    // ── PASS 1: Own Website ────────────────────────────────────────────────────
    if (websiteUrl) {
      const needsFloor    = !coverage.floor    || photosByType.floor.length < 1;
      const needsExterior = !coverage.exterior || photosByType.exterior.length < 1;
      const needsInterior = !coverage.interior || photosByType.interior.length < MAX_INTERIOR_PHOTOS;
      const needsProShop  = !coverage.pro_shop;
      const needsAction   = !coverage.action   || photosByType.action.length < MAX_ACTION_PHOTOS;

      if (needsFloor || needsExterior || needsInterior || needsProShop || needsAction) {
        log('INFO', '  🌐 Pass 1: Website crawl...');

        // Build targeted page list based on what we still need
        const pagesToCrawl: string[] = [websiteUrl]; // homepage always (og:image → floor/interior)

        // Find category-specific pages from candidate_links
        const candidateLinks: Record<string, string> = candidates.candidate_links || {};
        const allLinks: string[] = candidates.gallery_urls || [];

        // Contact page → exterior
        if (needsExterior) {
          const contactUrl = Object.values(candidateLinks).find((u: string) => /contact|find-us|location|directions/i.test(u))
            || allLinks.find(u => /contact|find-us|location|directions/i.test(u));
          if (contactUrl && contactUrl !== websiteUrl) pagesToCrawl.push(contactUrl);
        }

        // Gallery/facility/about pages → floor, interior
        for (const u of allLinks.slice(0, 3)) {
          if (/gallery|photo|facility|about|our-rink|inside|interior|floor/i.test(u) && !pagesToCrawl.includes(u)) {
            pagesToCrawl.push(u);
          }
        }

        // Pro-shop page → pro_shop
        if (needsProShop) {
          const shopUrl = Object.values(candidateLinks).find((u: string) => /pro.?shop|shop|gear|merchandise|store/i.test(u))
            || allLinks.find(u => /pro.?shop|shop|gear/i.test(u));
          if (shopUrl && !pagesToCrawl.includes(shopUrl)) pagesToCrawl.push(shopUrl);
        }

        const websiteCandidates = await crawlPageForImages(pagesToCrawl.slice(0, 5), 'website');
        allWebsiteCandidates = websiteCandidates; // save for overflow pass

        // Also add OG image as explicit candidate
        if (candidates.og_image) {
          websiteCandidates.push({ url: candidates.og_image, alt: 'og:image', filename: 'og-image', parentClass: 'meta', nearestHeading: '', pageUrl: websiteUrl, pageSource: 'website_og' });
        }

        // Classify and collect
        for (const candidate of websiteCandidates) {
          const { type, confidence, signals } = await classifyImage(candidate);
          if (type === 'unknown') continue;

          const currentCount = photosByType[type]?.length || 0;
          const maxForType = type === 'action' ? MAX_ACTION_PHOTOS : type === 'interior' ? MAX_INTERIOR_PHOTOS : 1;
          if (currentCount >= maxForType) continue;
          if (type === 'exterior' && coverage.exterior) continue;
          if (type === 'floor' && coverage.floor) continue;
          if (type === 'pro_shop' && coverage.pro_shop) continue;

          const dl = await downloadImage(candidate.url);
          if (!dl) continue;

          const index = savedPhotos.filter(p => p.type === type).length;
          const localUrl = await compressAndSave(dl.buffer, target.id, target.state || 'US', `${type}_${index}`);
          if (!localUrl) continue;

          const saved: SavedPhoto = { url: localUrl, type, source: candidate.pageSource, confidence, signals };
          savedPhotos.push(saved);
          photosByType[type] = [...(photosByType[type] || []), saved];
          if (type !== 'action' && type !== 'interior') coverage[type as keyof PhotoCoverage] = true;
          if (type === 'interior' && photosByType.interior.length >= 1) coverage.interior = true;
          if (type === 'action' && photosByType.action.length >= 1) coverage.action = true;
          log('INFO', `  ✅ Saved ${type} (confidence: ${confidence.toFixed(2)}, signals: ${signals.join(', ')})`);
        }
      }
    }

    // ── PASS 2: Google Places refs (up to 10, for unfilled categories) ─────────
    const stillNeedsAny = !coverage.floor || !coverage.exterior || !coverage.interior || !coverage.action;
    if (stillNeedsAny && candidates.google_refs?.length) {
      log('INFO', '  🗺️ Pass 2: Google Places refs...');
      const GMAPS_API_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_KEY;
      if (GMAPS_API_KEY) {
        for (const ref of candidates.google_refs.slice(0, 10)) {
          if (savedPhotos.length >= MAX_PHOTOS_TOTAL) break;
          const photoRef = typeof ref === 'string' ? ref : ref?.photo_reference;
          if (!photoRef) continue;
          const photoUrl = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photo_reference=${photoRef}&key=${GMAPS_API_KEY}`;

          const candidate: ImageCandidate = { url: photoUrl, alt: 'google-places', filename: 'google-places-photo', parentClass: '', nearestHeading: '', pageUrl: 'google', pageSource: 'google_refs' };
          const { type, confidence, signals } = await classifyImage(candidate);
          const resolvedType = type === 'unknown' ? 'interior' : type; // Google Places = real venue photos, tag as interior if uncertain

          const maxForType = resolvedType === 'action' ? MAX_ACTION_PHOTOS : resolvedType === 'interior' ? MAX_INTERIOR_PHOTOS : 1;
          const currentCount = photosByType[resolvedType]?.length || 0;
          if (currentCount >= maxForType) continue;

          const dl = await downloadImage(photoUrl);
          if (!dl) continue;
          const index = savedPhotos.filter(p => p.type === resolvedType).length;
          const localUrl = await compressAndSave(dl.buffer, target.id, target.state || 'US', `${resolvedType}_gp_${index}`);
          if (!localUrl) continue;

          const saved: SavedPhoto = { url: localUrl, type: resolvedType, source: 'google_places', confidence, signals };
          savedPhotos.push(saved);
          photosByType[resolvedType] = [...(photosByType[resolvedType] || []), saved];
          if (resolvedType !== 'action' && resolvedType !== 'interior') coverage[resolvedType as keyof PhotoCoverage] = true;
          if (resolvedType === 'interior' && photosByType.interior.length >= 1) coverage.interior = true;
          if (resolvedType === 'action' && photosByType.action.length >= 1) coverage.action = true;
          log('INFO', `  ✅ Google Places → ${resolvedType}`);
        }
      }
    }

    // ── PASS 3: Instagram grid (action fallback) ────────────────────────────────
    const needsAction = !coverage.action || photosByType.action.length < MAX_ACTION_PHOTOS;
    const instagramUrl = target.instagram_url || candidates.instagram_url;
    if (needsAction && instagramUrl) {
      log('INFO', '  📸 Pass 3: Instagram grid (action fallback)...');
      const igCandidates = await scrapeInstagramGrid(instagramUrl);
      for (const candidate of igCandidates) {
        if (photosByType.action.length >= MAX_ACTION_PHOTOS) break;
        const dl = await downloadImage(candidate.url);
        if (!dl) continue;
        const index = photosByType.action.length;
        const localUrl = await compressAndSave(dl.buffer, target.id, target.state || 'US', `action_ig_${index}`);
        if (!localUrl) continue;
        const saved: SavedPhoto = { url: localUrl, type: 'action', source: 'instagram', confidence: 0.80, signals: ['source:instagram-grid'] };
        savedPhotos.push(saved);
        photosByType.action.push(saved);
        coverage.action = true;
        log('INFO', `  ✅ Instagram → action`);
      }
    }

    // ── PASS 4: Yelp tab-specific (last resort) ────────────────────────────────
    const yelpUrl = target.yelp_url || candidates.yelp_url;
    if (yelpUrl) {
      if (!coverage.interior || photosByType.interior.length < 1) {
        log('INFO', '  ⭐ Pass 4: Yelp inside tab...');
        const yelpInside = await scrapeYelpTab(yelpUrl, 'inside');
        for (const c of yelpInside.slice(0, 2)) {
          if (photosByType.interior.length >= MAX_INTERIOR_PHOTOS) break;
          const dl = await downloadImage(c.url);
          if (!dl) continue;
          const idx = photosByType.interior.length;
          const localUrl = await compressAndSave(dl.buffer, target.id, target.state || 'US', `interior_yelp_${idx}`);
          if (!localUrl) continue;
          const saved: SavedPhoto = { url: localUrl, type: 'interior', source: 'yelp_inside', confidence: 0.88, signals: ['source:yelp-inside-tab'] };
          savedPhotos.push(saved); photosByType.interior.push(saved); coverage.interior = true;
          log('INFO', `  ✅ Yelp inside → interior`);
        }
      }
    }

    // ── OVERFLOW PASS: collect remaining website candidates as 'unknown' for manual tagging ──
    // After filling priority categories, save ALL remaining website images tagged as 'unknown'.
    // These show in the dashboard TAG queue for manual classification.
    if (allWebsiteCandidates.length > 0 && savedPhotos.length < MAX_PHOTOS_TOTAL) {
      const savedUrls = new Set(savedPhotos.map(p => p.url));
      const overflowCandidates = allWebsiteCandidates.filter(c => {
        // Skip already-saved and stock photos
        if (STOCK_PHOTO_DOMAINS.some(d => c.url.includes(d))) return false;
        return !savedUrls.has(c.url);
      });
      log('INFO', `  📦 Overflow pass: up to ${Math.min(overflowCandidates.length, OVERFLOW_MAX)} of ${overflowCandidates.length} remaining candidates for manual tagging...`);
      let overflowCount = 0;
      for (const candidate of overflowCandidates) {
        if (savedPhotos.length >= MAX_PHOTOS_TOTAL || overflowCount >= OVERFLOW_MAX) break;
        const dl = await downloadImage(candidate.url);
        if (!dl) continue;
        const idx = photosByType.unknown.length;
        const localUrl = await compressAndSave(dl.buffer, target.id, target.state || 'US', `overflow_${idx}`);
        if (!localUrl) continue;
        const saved: SavedPhoto = { url: localUrl, type: 'unknown', source: candidate.pageSource, confidence: 0.0, signals: [candidate.alt || candidate.filename].filter(Boolean).slice(0, 3) };
        savedPhotos.push(saved);
        photosByType.unknown.push(saved);
        overflowCount++;
      }
      if (overflowCount > 0) log('INFO', `  📦 Overflow: +${overflowCount} unknown photos ready for manual tagging`);
    }

    // ── Write results ──────────────────────────────────────────────────────────
    const finalCoverage: PhotoCoverage = {
      logo: !!target.logo_url || coverage.logo,
      exterior: photosByType.exterior.length > 0,
      interior: photosByType.interior.length > 0,
      floor: photosByType.floor.length > 0,
      pro_shop: photosByType.pro_shop.length > 0,
      action: photosByType.action.length > 0,
    };

    const finalPhotos = savedPhotos.filter(p => p.type !== 'logo');
    updateLocalSpot(target.id, {
      photos: finalPhotos,
      photo_coverage: finalCoverage,
      verification_status: 'MEDIA_READY',
      last_attempted_at: new Date().toISOString(),
    });

    const covSummary = Object.entries(finalCoverage).filter(([, v]) => v).map(([k]) => k).join(', ') || 'none';
    log('INFO', `✨ ${target.name} → MEDIA_READY | Coverage: [${covSummary}] | ${finalPhotos.length} photos`);
    reportPulse(LOOP_COOLDOWN_MS, 'IDLE', null);
    await sleep(LOOP_COOLDOWN_MS);
  }
}

process.on('unhandledRejection', (reason) => { console.error('[Photographer] Unhandled Rejection:', reason); });
process.on('uncaughtException', (error) => { console.error('[Photographer] Uncaught Exception:', error); });

runPhotographerLoop().catch(err => {
  console.error('[Photographer] Fatal error:', err);
  process.exit(1);
});
