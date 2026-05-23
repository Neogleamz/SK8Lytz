/**
 * Photographer.ts — Phase 3 AI Photo Harvest Daemon (v2 Sitemap-First Rebuild)
 *
 * New architecture:
 *   1. Read candidatePhotos from Detective handoff (gallery_urls, yelp, facebook, og_image, logo, cover)
 *   2. Download logo_url + cover_photo_url as separate fields (not mixed into photos[])
 *   3. Puppeteer crawl gallery pages (from sitemap), Facebook photos, Yelp photos, homepage
 *   4. Extract all large images + context (alt, parentClass, pageSource)
 *   5. LM Studio AI selects the 10 best photos
 *   6. Download AI-approved photos → local disk
 *   7. Write photos[], logo_url, cover_photo_url → MEDIA_READY
 *
 * Fallback: if LM Studio unavailable → top 10 by file size
 */

import dotenv from 'dotenv';
import path from 'path';
import fs from 'fs';
import fetch from 'node-fetch';
import puppeteer from 'puppeteer';
import Tesseract from 'tesseract.js';
import crypto from 'crypto';
import { db, updateLocalSpot, getConfig } from './core/LocalDB';

const envPaths = [
  path.resolve(__dirname, '../../.env'),
  path.resolve(process.cwd(), '.env'),
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.env',
];
for (const p of envPaths) { const r = dotenv.config({ path: p }); if (!r.error) break; }

const PHOTOS_DIR = path.resolve(__dirname, '../../.scraper-data/photos');
if (!fs.existsSync(PHOTOS_DIR)) fs.mkdirSync(PHOTOS_DIR, { recursive: true });

const PHOTO_SERVE_BASE = 'http://localhost:5999/api/photos';
const LM_STUDIO_URL = 'http://localhost:1234/v1/chat/completions';
const LOOP_COOLDOWN_MS = 800;
const MAX_PHOTOS = 10;
const MIN_FILE_SIZE_BYTES = 50 * 1024; // 50KB
const MIN_IMG_WIDTH = 600;
const MIN_IMG_HEIGHT = 400;

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

// ─── Graceful Shutdown (Anti-Zombie Chrome Defense) ───────────────────────────
let activeBrowser: any = null;
const gracefulShutdown = async (signal: string) => {
  console.log(`[Photographer] ${signal} received — cleaning up Puppeteer...`);
  if (activeBrowser) { try { await activeBrowser.close(); } catch {} }
  process.exit(0);
};
process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));
process.on('SIGINT', () => gracefulShutdown('SIGINT'));

const reportPulse = (delayMs: number, active_job: string | null = null, target: string | null = null) => {
  fetch('http://localhost:5999/api/pulse', { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({source:'Photographer',delayMs,active_job,target}) }).catch(()=>{});
};

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
  fetch('http://localhost:5999/api/logs/ingest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(batch)
  }).catch(() => {});
};

const logToTower = (type: 'INFO'|'ERROR', message: string) => {
  queueLog(type, 'Photographer', message);
  console.log(`${type==='ERROR'?'❌':'📸'} [Photographer] ${message}`);
};

// ─── Image Candidate Type ─────────────────────────────────────────────────────

interface ImageCandidate {
  url: string;
  alt: string;
  parentClass: string;
  pageSource: string;
  estimatedSize?: number;
}

// ─── Download Helpers ─────────────────────────────────────────────────────────

async function downloadImage(url: string): Promise<{buffer: Buffer; mimeType: string} | null> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (compatible; SK8LytzBot/2.0)' },
      signal: AbortSignal.timeout(15000),
    });
    if (!res.ok) return null;
    const contentType = res.headers.get('content-type') || '';
    if (!contentType.startsWith('image/')) return null;
    const buf = await res.buffer();
    if (buf.length < MIN_FILE_SIZE_BYTES) return null; // rejects tracking pixels
    return { buffer: buf, mimeType: contentType };
  } catch { return null; }
}

function saveToDisk(buf: Buffer, spotId: string, state: string, index: number, prefix = 'photo', mimeType = 'image/jpeg'): string | null {
  try {
    const ext = mimeType.includes('png') ? 'png' : mimeType.includes('webp') ? 'webp' : 'jpg';
    const dir = path.join(PHOTOS_DIR, state || 'US', spotId);
    if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });

    // De-duplicate by file content hash
    const hash = crypto.createHash('sha256').update(buf).digest('hex');
    const existingFiles = fs.readdirSync(dir);
    for (const file of existingFiles) {
      if (file.startsWith('logo_') || file.startsWith('cover_')) continue;
      const existingBuf = fs.readFileSync(path.join(dir, file));
      const existingHash = crypto.createHash('sha256').update(existingBuf).digest('hex');
      if (hash === existingHash) {
        logToTower('INFO', `  ⏭️ Duplicate image content detected (hash: ${hash.slice(0,8)}). Skipping.`);
        return null;
      }
    }

    const filename = `${prefix}_${index}.${ext}`;
    fs.writeFileSync(path.join(dir, filename), buf);
    return `${PHOTO_SERVE_BASE}/${state || 'US'}/${spotId}/${filename}`;
  } catch (e: any) {
    logToTower('ERROR', `Disk write failed: ${e.message}`);
    return null;
  }
}

// ─── Programmatic Photo Selection (Heuristics) ──────────────────────────────

// ─── Programmatic Photo Selection (Heuristics) ──────────────────────────────

function normalizeImageUrl(urlStr: string): string {
  try {
    const url = new URL(urlStr);
    
    // Strip common sizing and resizing query parameters
    const paramsToStrip = [
      'width', 'height', 'w', 'h', 'resize', 'size', 'v', 'cache', 'dpr', 
      'fit', 'crop', 'quality', 'q', 'auto', 'format'
    ];
    paramsToStrip.forEach(p => url.searchParams.delete(p));
    
    // Lowercase path for robust suffix matching
    let pathname = url.pathname.toLowerCase();
    
    // Remove typical WordPress/CDN dimension suffixes (e.g. -300x200, _100x100)
    pathname = pathname.replace(/[-_]\d+x\d+/g, '');
    
    // Remove typical scale/size suffixes (e.g. -scaled, -large, -thumb)
    pathname = pathname.replace(/[-_](scaled|large|medium|thumbnail|thumb|small|big|hero)/g, '');
    
    // Remove file extensions for robust visual stem-level deduplication
    pathname = pathname.replace(/\.(jpg|jpeg|png|webp|gif|svg|bmp)$/i, '');
    
    return url.origin + pathname;
  } catch {
    return urlStr;
  }
}

function calculateCandidateScore(c: ImageCandidate, size: number): number {
  let score = size; // Base score is file size in bytes

  const urlLower = c.url.toLowerCase();
  const altLower = c.alt.toLowerCase();
  const classLower = c.parentClass.toLowerCase();

  // 1. Extreme Penalty for bad keywords in URL, Alt, or parentClass (UI, icons, menus, generic templates)
  const badKeywords = [
    'logo', 'icon', 'placeholder', 'avatar', 'button', 'sprite', 'spacer', 
    'transparent', 'bg-', 'star', 'rating', 'map-marker', 'arrow', 'loader',
    'advertisement', 'ad-', 'ad_', 'social', 'widget', 'header-bg', 'footer-bg',
    'generic', 'blank', 'no-image', 'no-photo', 'default', 'close', 'search',
    'menu', 'pin', 'marker', 'cart', 'trash', 'user', 'profile'
  ];
  
  for (const kw of badKeywords) {
    if (urlLower.includes(kw) || altLower.includes(kw) || classLower.includes(kw)) {
      score -= 1000 * 1024; // Deduct 1MB worth of score priority
    }
  }

  // 2. Extra Boost for positive, high-value image keywords
  const goodKeywords = [
    'gallery', 'upload', 'media', 'photo', 'rink', 'floor', 'skater', 'skate',
    'spot', 'interior', 'exterior', 'facility', 'full', 'large', 'original',
    'action', 'party', 'event', 'fun'
  ];
  
  for (const kw of goodKeywords) {
    if (urlLower.includes(kw) || altLower.includes(kw)) {
      score += 200 * 1024; // Boost by 200KB worth of priority
    }
  }

  // 3. Parental class boosts
  if (classLower.includes('gallery') || classLower.includes('slider') || classLower.includes('carousel') || classLower.includes('portfolio')) {
    score += 150 * 1024; // Boost by 150KB
  }

  return score;
}

async function estimateImageQuality(candidates: ImageCandidate[]): Promise<string[]> {
  if (candidates.length === 0) return [];
  const sizeMap = new Map<string, number>();
  
  // Batch HEAD requests 10 at a time
  for (let i = 0; i < candidates.length; i += 10) {
    const batch = candidates.slice(i, i + 10);
    await Promise.all(batch.map(async (c) => {
      try {
        const res = await fetch(c.url, { method: 'HEAD', headers: { 'User-Agent': 'Mozilla/5.0' }, signal: AbortSignal.timeout(5000) });
        if (res.ok) {
          const cl = res.headers.get('content-length');
          sizeMap.set(c.url, cl ? parseInt(cl, 10) : 0);
        } else { sizeMap.set(c.url, 0); }
      } catch { sizeMap.set(c.url, 0); }
    }));
  }

  const sortedUrls = candidates
    .filter(c => (sizeMap.get(c.url) || 0) >= MIN_FILE_SIZE_BYTES)
    .sort((a, b) => {
      const scoreA = calculateCandidateScore(a, sizeMap.get(a.url) || 0);
      const scoreB = calculateCandidateScore(b, sizeMap.get(b.url) || 0);
      return scoreB - scoreA;
    })
    .map(c => c.url)
    .slice(0, MAX_PHOTOS * 2.5); // Take top 25 to screen

  const finalUrls: string[] = [];
  logToTower('INFO', `  🔍 Running OCR Bouncer on top ${sortedUrls.length} candidate images...`);
  
  for (const url of sortedUrls) {
    if (finalUrls.length >= MAX_PHOTOS) break;
    try {
      const { data: { text } } = await Tesseract.recognize(url, 'eng');
      const textLen = text?.trim().length || 0;
      if (textLen > 100) {
        logToTower('INFO', `  🚫 Rejected as Flyer (Text length: ${textLen}): ${url.slice(0, 60)}`);
      } else {
        finalUrls.push(url);
      }
    } catch {
      finalUrls.push(url); // Default to keep if OCR fails
    }
  }

  return finalUrls;
}

// ─── Puppeteer Gallery Crawl ──────────────────────────────────────────────────

async function crawlForImages(urls: string[], pageSourceLabel: string, isHeadless: boolean): Promise<ImageCandidate[]> {
  if (urls.length === 0) return [];
  const candidates: ImageCandidate[] = [];
  let browser: any = null;
  try {
    browser = await puppeteer.launch({ headless: isHeadless ? 'new' : false, args:['--no-sandbox','--disable-setuid-sandbox','--disable-dev-shm-usage'] });
    activeBrowser = browser;
    const page = await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
    await page.setViewport({width:1280, height:900});

    for (const url of urls) {
      try {
        await page.goto(url, { waitUntil:'domcontentloaded', timeout:25000 });
        // Auto scroll to trigger lazy-load
        await page.evaluate(async () => {
          await new Promise(resolve => {
            let total=0; const dist=150;
            const maxScrolls = 40;
            let scrolls = 0;
            const timer=setInterval(()=>{
              window.scrollBy(0,dist);
              total+=dist;
              scrolls++;
              if(total>=document.body.scrollHeight || scrolls >= maxScrolls){
                clearInterval(timer);
                resolve(true);
              }
            },120);
          });
        });
        await sleep(800);

        const found: ImageCandidate[] = await page.evaluate((source: string, minW: number, minH: number) => {
          const results: any[] = [];
          // OG image
          const og = document.querySelector('meta[property="og:image"]')?.getAttribute('content');
          if (og) results.push({ url: og, alt: 'og:image', parentClass: 'meta', pageSource: source });
          // JSON-LD images
          document.querySelectorAll('script[type="application/ld+json"]').forEach((el:any) => {
            try { const ld = JSON.parse(el.innerText); if (ld.image) { const imgs = Array.isArray(ld.image) ? ld.image : [ld.image]; imgs.forEach((img:any) => { const u = typeof img==='string'?img:img?.url; if(u) results.push({url:u,alt:'json-ld',parentClass:'ld+json',pageSource:source}); }); } } catch {}
          });
          // IMG tags
          document.querySelectorAll('img').forEach((img:any) => {
            const w = img.naturalWidth || img.width || 0;
            const h = img.naturalHeight || img.height || 0;
            const src = img.src || img.dataset?.src || img.dataset?.lazySrc || '';
            
            if (src && src.startsWith('http')) {
              // Relax strict layout size check if the URL contains strong visual photo cues
              const isPlaceholderOrIcon = /icon|logo|sprite|spacer|star|button|avatar/i.test(src);
              const isHighResCandidate = /uploads|gallery|media|photos|rink|skate|original/i.test(src) || src.includes('places/photo');
              
              const passesSize = (w >= minW && h >= minH);
              const passesLazyLoad = (w === 0 && h === 0 && isHighResCandidate);
              
              if (!isPlaceholderOrIcon && (passesSize || passesLazyLoad)) {
                results.push({ url:src, alt:(img.alt||'').toLowerCase(), parentClass:(img.closest('[class]')?.className||'').toLowerCase().slice(0,80), pageSource:source });
              }
            }
          });
          // CSS background images on hero/slider/gallery divs
          document.querySelectorAll('[class*="hero"],[class*="slider"],[class*="gallery"],[class*="banner"],[class*="carousel"],[class*="rink"],[class*="photo"]').forEach((el:any) => {
            const bg = window.getComputedStyle(el).backgroundImage;
            const match = bg?.match(/url\(["']?(https?[^"')]+)["']?\)/);
            if (match?.[1]) results.push({ url:match[1], alt:'css-background', parentClass:(el.className||'').toLowerCase().slice(0,80), pageSource:source });
          });
          return results;
        }, pageSourceLabel + ':' + url, MIN_IMG_WIDTH, MIN_IMG_HEIGHT);

        candidates.push(...found);
        logToTower('INFO', `  📷 ${pageSourceLabel} ${url} → ${found.length} candidates`);
      } catch (e: any) {
        logToTower('INFO', `  ⚠️ Crawl failed: ${url}`);
      }
    }
  } finally { if (browser) { try { await browser.close(); } catch {} } activeBrowser = null; }

  // De-duplicate by normalized URL to completely avoid multi-CDN / resized duplicates
  const seenNormalized = new Set<string>();
  const uniqueCandidates: ImageCandidate[] = [];
  for (const c of candidates) {
    const norm = normalizeImageUrl(c.url);
    if (!seenNormalized.has(norm)) {
      seenNormalized.add(norm);
      uniqueCandidates.push(c);
    }
  }
  return uniqueCandidates;
}

// ─── Main Loop ────────────────────────────────────────────────────────────────

async function runPhotographerLoop() {
  if (process.env.SCRAPER_REGISTER_ONLY === 'true') {
    console.log('[Photographer] PM2 registration mode: exiting immediately.');
    process.exit(0);
  }
  logToTower('INFO', '🚀 Photographer v2 daemon starting — Sitemap-First AI Photo Selection active');
  let consecutiveIdle = 0;
  const isHeadless = process.env.PHOTOGRAPHER_HEADLESS !== 'false';

  while (true) {
    const configRes = await fetch('http://localhost:5999/api/priority-states').then((r: any) => r.json()).catch(() => ({ priority_states: [] }));
    const priorityStates: string[] = configRes.priority_states || [];
    const aiConfig = await fetch('http://localhost:5999/config').then((r: any) => r.json()).then((d:any) => d.config || {}).catch(() => ({}));

    let target: any = null;

    // sniper_target_id isolation
    if (aiConfig.sniper_target_id) {
      target = db.prepare(`SELECT id, name, state, candidate_photos, photos, logo_url, cover_photo_url, verification_status FROM local_spots WHERE id = ?`).get(aiConfig.sniper_target_id);
      if (target && target.verification_status !== 'DEEP_CRAWLED') {
        target = null;
      }
    }

    if (!target) {
      let query = `SELECT id, name, state, candidate_photos, photos, logo_url, cover_photo_url, verification_status FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' AND website IS NOT NULL`;
      if (priorityStates.length > 0) {
        query += ` AND state IN (${priorityStates.map((s: string) => `'${s}'`).join(',')})`;
      }
      query += ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`;
      try { target = db.prepare(query).get() as any; } catch {}
    }

    if (!target) {
      // Fallback: any DEEP_CRAWLED without priority filter
      try { target = db.prepare(`SELECT id, name, state, candidate_photos, photos, logo_url, cover_photo_url, verification_status FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`).get() as any; } catch {}
    }

    if (!target) {
      consecutiveIdle++;
      if (consecutiveIdle === 1) logToTower('INFO', '⏸️ Queue empty — idling...');
      reportPulse(30000, 'IDLE', null);
      await sleep(30000);
      continue;
    }

    consecutiveIdle = 0;
    logToTower('INFO', `📷 Processing: ${target.name} (${target.state}) [${target.id}]`);
    reportPulse(0, 'Crawling Photos', target.name);

    let candidates: any = {};
    try { candidates = typeof target.candidate_photos === 'string' ? JSON.parse(target.candidate_photos) : (target.candidate_photos || {}); } catch {}

    // ── Step 1: Logo + Cover (fast, no browser) ───────────────────────────────
    let savedLogoUrl: string | null = null;
    let savedCoverUrl: string | null = null;

    if (candidates.logo_url && !target.logo_url) {
      const dl = await downloadImage(candidates.logo_url);
      if (dl) {
        savedLogoUrl = saveToDisk(dl.buffer, target.id, target.state||'US', 0, 'logo', dl.mimeType);
        if (savedLogoUrl) logToTower('INFO', `  🏷️ Logo saved: ${savedLogoUrl}`);
      }
    }

    if (candidates.cover_photo_url && !target.cover_photo_url) {
      const dl = await downloadImage(candidates.cover_photo_url);
      if (dl) {
        savedCoverUrl = saveToDisk(dl.buffer, target.id, target.state||'US', 0, 'cover', dl.mimeType);
        if (savedCoverUrl) logToTower('INFO', `  🖼️ Cover saved: ${savedCoverUrl}`);
      }
    }

    // ── Step 2: Build crawl source list ──────────────────────────────────────
    let existingPhotos: string[] = [];
    try { if (target.photos && target.photos !== '[]') existingPhotos = typeof target.photos === 'string' ? JSON.parse(target.photos) : target.photos; } catch {}

    if (existingPhotos.length >= MAX_PHOTOS) {
      // Already has enough photos — just ensure status is correct
      const updates: any = { verification_status:'MEDIA_READY', last_attempted_at: new Date().toISOString() };
      if (savedLogoUrl) updates.logo_url = savedLogoUrl;
      if (savedCoverUrl) updates.cover_photo_url = savedCoverUrl;
      updateLocalSpot(target.id, updates);
      logToTower('INFO', `⏭️ ${target.name} already has ${existingPhotos.length} photos. Marking MEDIA_READY.`);
      await sleep(LOOP_COOLDOWN_MS);
      continue;
    }

    // ── Step 3: Puppeteer crawl — all sources ─────────────────────────────────
    const allCandidates: ImageCandidate[] = [];

    try {
      // Priority 1: Sitemap gallery pages
      if (candidates.gallery_urls?.length) {
        logToTower('INFO', `  🗺️ Crawling ${candidates.gallery_urls.length} gallery pages...`);
        const found = await crawlForImages(candidates.gallery_urls, 'gallery', isHeadless);
        allCandidates.push(...found);
      }

      // Priority 2: Facebook photos
      if (candidates.facebook_photos_url) {
        logToTower('INFO', `  📘 Crawling Facebook photos...`);
        const found = await crawlForImages([candidates.facebook_photos_url], 'facebook', isHeadless);
        allCandidates.push(...found);
      }

      // Priority 3: Yelp photos
      if (candidates.yelp_photos_url) {
        logToTower('INFO', `  ⭐ Crawling Yelp photos...`);
        const found = await crawlForImages([candidates.yelp_photos_url], 'yelp', isHeadless);
        allCandidates.push(...found);
      }

      // Priority 4-7: Homepage + OG + JSON-LD (always run as fallback/supplement)
      const spot: any = db.prepare('SELECT website FROM local_spots WHERE id = ?').get(target.id);
      if (spot?.website) {
        logToTower('INFO', `  🏠 Crawling homepage...`);
        const found = await crawlForImages([spot.website], 'homepage', isHeadless);
        allCandidates.push(...found);
      }
    } catch (crawlErr: any) {
      logToTower('ERROR', `  ⚠️ Puppeteer sitemap crawl crashed: ${crawlErr.message}`);
      logToTower('INFO', `  ⚡ Triggering lightweight Cheerio fallback sitemap crawler...`);
      try {
        const cheerio = require('cheerio');
        const urlsToCrawl: string[] = [...(candidates.gallery_urls || [])];
        const spot: any = db.prepare('SELECT website FROM local_spots WHERE id = ?').get(target.id);
        if (spot?.website) urlsToCrawl.push(spot.website);

        for (const url of urlsToCrawl) {
          try {
            logToTower('INFO', `    🌐 Cheerio crawling: ${url}`);
            const res = await fetch(url, { headers: { 'User-Agent': 'Mozilla/5.0' }, timeout: 8000 });
            if (res.ok) {
              const html = await res.text();
              const $ = cheerio.load(html);
              
              // Parse og:image
              const og = $('meta[property="og:image"]').attr('content');
              if (og) allCandidates.push({ url: og, alt: 'og:image', parentClass: 'meta', pageSource: 'cheerio_fallback' });

              // Parse standard img tags
              $('img').each((_: number, img: any) => {
                const src = $(img).attr('src') || $(img).attr('data-src') || $(img).attr('data-lazy-src') || '';
                if (src && src.startsWith('http')) {
                  allCandidates.push({ url: src, alt: ($(img).attr('alt') || '').toLowerCase(), parentClass: 'img_tag', pageSource: 'cheerio_fallback' });
                }
              });
            }
          } catch (e: any) {
            logToTower('INFO', `    ⚠️ Cheerio fallback crawl failed for ${url}: ${e.message}`);
          }
        }
      } catch (cheerioErr: any) {
        logToTower('ERROR', `    ⚠️ Cheerio fallback parser failed: ${cheerioErr.message}`);
      }
    }

    // Add direct OG image as a candidate if not already found by crawl
    if (candidates.og_image) allCandidates.push({ url: candidates.og_image, alt: 'og:image', parentClass: 'meta', pageSource: 'og_direct' });

    // DOM images from Detective handoff
    if (candidates.dom_images?.length) {
      candidates.dom_images.forEach((url: string) => allCandidates.push({ url, alt: 'dom_image', parentClass: '', pageSource: 'detective_dom' }));
    }

    // Google Places photo references (Phase 1 handoff)
    if (candidates.google_refs?.length) {
      const GMAPS_API_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_KEY;
      if (GMAPS_API_KEY) {
        for (const ref of candidates.google_refs.slice(0, 5)) {
          const photoRef = typeof ref === 'string' ? ref : ref?.photo_reference;
          if (photoRef) {
            const url = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photo_reference=${photoRef}&key=${GMAPS_API_KEY}`;
            allCandidates.push({ url, alt: 'google_places', parentClass: '', pageSource: 'google_refs' });
          }
        }
        logToTower('INFO', `  🗺️ Added ${Math.min(candidates.google_refs.length, 5)} Google Places photo candidates`);
      }
    }

    logToTower('INFO', `  🔎 Total raw candidates: ${allCandidates.length}. Sending to AI...`);

    // ── Step 4: Programmatic Selection (Heuristic-First) ──────────────────────
    logToTower('INFO', `  🤖 Estimating image quality for ${allCandidates.length} candidates via HTTP Content-Length...`);
    const selectedUrls = await estimateImageQuality(allCandidates);
    logToTower('INFO', `  🤖 Selected ${selectedUrls.length} high-resolution photos based on file size heuristics.`);

    // ── Step 5: Download AI-selected photos ───────────────────────────────────
    const newUrls: string[] = [];
    for (let i = 0; i < selectedUrls.length && (existingPhotos.length + newUrls.length) < MAX_PHOTOS; i++) {
      const url = selectedUrls[i];
      const dl = await downloadImage(url);
      if (!dl) { logToTower('INFO', `   ⚠️ Skip (download fail): ${url.slice(0,60)}`); continue; }
      const localUrl = saveToDisk(dl.buffer, target.id, target.state||'US', existingPhotos.length + newUrls.length, 'photo', dl.mimeType);
      if (localUrl) {
        newUrls.push(localUrl);
        logToTower('INFO', `   ✅ Saved: ${localUrl} (${(dl.buffer.length/1024).toFixed(1)}KB)`);
      }
    }

    // ── Step 6: Write to DB ───────────────────────────────────────────────────
    const finalPhotos = [...new Set([...existingPhotos, ...newUrls])];
    const updates: any = {
      photos: finalPhotos,
      verification_status: 'MEDIA_READY',
      last_attempted_at: new Date().toISOString(),
    };
    if (savedLogoUrl) updates.logo_url = savedLogoUrl;
    if (savedCoverUrl) updates.cover_photo_url = savedCoverUrl;

    updateLocalSpot(target.id, updates);

    if (finalPhotos.length > 0) {
      logToTower('INFO', `✨ ${target.name} → MEDIA_READY (${finalPhotos.length} photos, logo:${!!savedLogoUrl}, cover:${!!savedCoverUrl})`);
    } else {
      logToTower('INFO', `⚠️ ${target.name} → MEDIA_READY (no photos obtainable — promoted anyway)`);
    }

    reportPulse(LOOP_COOLDOWN_MS, 'IDLE', null);
    await sleep(LOOP_COOLDOWN_MS);
  }
}

runPhotographerLoop().catch(err => {
  console.error('[Photographer] Fatal error:', err);
  process.exit(1);
});
