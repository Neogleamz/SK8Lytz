/**
 * Photographer.ts — Phase 4 Media Harvest Daemon
 *
 * Reads candidate_photos collected by the Indexer, downloads each image URL binary,
 * saves to local disk under .scraper-data/photos/{state}/{spotId}/, and writes
 * local HTTP URLs back to the `photos` column. Sets verification_status = 'MEDIA_READY'.
 *
 * Photos are served by CCTower at: http://localhost:5999/api/photos/{state}/{spotId}/{filename}
 *
 * Zero Puppeteer footprint. Zero cloud storage costs.
 * Photo sources: OG image, DOM large images, Street View Static (free), Facebook OG.
 */
import dotenv from 'dotenv';
import path from 'path';
import fs from 'fs';
import fetch from 'node-fetch';
import { db, updateLocalSpot } from './core/LocalDB';

// Load .env
const envPaths = [
  path.resolve(__dirname, '../../.env'),
  path.resolve(process.cwd(), '.env'),
  path.resolve(process.cwd(), '../../.env'),
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.env',
];
for (const p of envPaths) {
  const result = dotenv.config({ path: p });
  if (!result.error) break;
}

// Local photo storage root — sibling to scraper.db
const PHOTOS_DIR = path.resolve(__dirname, '../../.scraper-data/photos');
if (!fs.existsSync(PHOTOS_DIR)) {
  fs.mkdirSync(PHOTOS_DIR, { recursive: true });
}

// CCTower serves photos at this base URL
const PHOTO_SERVE_BASE = 'http://localhost:5999/api/photos';

const COOLDOWN_MS = 800;
const MAX_PHOTOS_PER_SPOT = 10;

const reportPulse = (delayMs: number) => {
  fetch('http://localhost:5999/api/pulse', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source: 'Photographer', delayMs })
  }).catch(() => {});
};

const logToTower = (type: 'INFO' | 'ERROR', message: string) => {
  fetch('http://localhost:5999/api/logs/ingest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ type, source: 'Photographer', message })
  }).catch(() => {});
  const prefix = type === 'ERROR' ? '❌' : '📸';
  console.log(`${prefix} [Photographer] ${message}`);
};

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

/**
 * Attempt to download a URL and return its buffer.
 * Returns null if the download fails or response is not an image.
 */
async function downloadImage(url: string): Promise<Buffer | null> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (compatible; SK8LytzBot/1.0)' },
      signal: AbortSignal.timeout(10000)
    });
    if (!res.ok) return null;

    const contentType = res.headers.get('content-type') || '';
    if (!contentType.startsWith('image/') && !contentType.includes('jpeg') && !contentType.includes('png') && !contentType.includes('webp')) {
      return null;
    }

    const buf = await res.buffer();
    // Sanity check: must be at least 10KB (filters tracking pixels)
    if (buf.length < 10240) return null;
    return buf;
  } catch {
    return null;
  }
}

/**
 * Save a Buffer to local disk under .scraper-data/photos/{state}/{spotId}/.
 * Returns the local HTTP URL served by CCTower, or null on failure.
 */
function saveToLocalDisk(
  buffer: Buffer,
  spotId: string,
  state: string,
  photoIndex: number,
  mimeType = 'image/jpeg'
): string | null {
  try {
    const ext = mimeType.includes('png') ? 'png' : mimeType.includes('webp') ? 'webp' : 'jpg';
    const stateDir = path.join(PHOTOS_DIR, state || 'US', spotId);
    if (!fs.existsSync(stateDir)) {
      fs.mkdirSync(stateDir, { recursive: true });
    }
    const filename = `photo_${photoIndex}.${ext}`;
    const filePath = path.join(stateDir, filename);
    fs.writeFileSync(filePath, buffer);
    // Return CCTower-served URL
    return `${PHOTO_SERVE_BASE}/${state || 'US'}/${spotId}/${filename}`;
  } catch (err: any) {
    logToTower('ERROR', `Disk write failed for spot ${spotId} photo ${photoIndex}: ${err.message}`);
    return null;
  }
}

/**
 * Main processing loop — runs continuously until process is killed.
 */
async function runPhotographerLoop() {
  logToTower('INFO', '🚀 Photographer daemon starting — Phase 4 Media Harvest active (local storage)');

  let consecutiveIdle = 0;

  while (true) {
    // Fetch active region — priority states processed first
    const configRes = await fetch('http://localhost:5999/api/priority-states').then(r => r.json()).catch(() => ({ priority_states: [] }));
    const priorityStates: string[] = configRes.priority_states || [];

    // Build query with priority state ordering
    let query = `SELECT id, name, state, candidate_photos, photos, verification_status FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' AND (photos IS NULL OR photos = '[]' OR photos = '')`;
    if (priorityStates.length > 0) {
      query += ` AND state IN (${priorityStates.map((s: string) => `'${s}'`).join(',')})`;
    }
    query += ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 1`;

    let target: any = null;
    let queryError = null;
    try {
      target = db.prepare(query).get() as any;
    } catch (e: any) {
      queryError = e;
    }

    if (queryError || !target) {
      consecutiveIdle++;
      if (consecutiveIdle === 1) {
        logToTower('INFO', '⏸️ Queue is empty — all candidates photographed. Idling...');
      }
      reportPulse(30000);
      await sleep(30000);
      continue;
    }

    consecutiveIdle = 0;
    logToTower('INFO', `📷 Processing: ${target.name} (${target.state}) [${target.id}]`);

    let candidates: any = {};
    try {
      candidates = typeof target.candidate_photos === 'string' ? JSON.parse(target.candidate_photos) : target.candidate_photos;
      if (!candidates) candidates = {};
    } catch (e) {}

    const localUrls: string[] = [];

    // Build ordered list of photo URLs to attempt
    const urlCandidates: Array<{ key: string; url: string }> = [];
    
    // 1. Google Places Photos (highest quality hero candidates)
    if (candidates.google_refs?.length && process.env.GOOGLE_MAPS_API_KEY) {
      candidates.google_refs.forEach((ref: string, i: number) => {
        const url = `https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photo_reference=${ref}&key=${process.env.GOOGLE_MAPS_API_KEY}`;
        urlCandidates.push({ key: `google_${i}`, url });
      });
    }

    // 2. Fallbacks: Website OG, parsed DOM images, Facebook, Street View
    if (candidates.og_image) urlCandidates.push({ key: 'og_image', url: candidates.og_image });
    if (candidates.dom_images?.length) {
      candidates.dom_images.forEach((url: string, i: number) =>
        urlCandidates.push({ key: `dom_${i}`, url })
      );
    }
    if (candidates.facebook_og) urlCandidates.push({ key: 'facebook_og', url: candidates.facebook_og });
    if (candidates.street_view_url) urlCandidates.push({ key: 'street_view', url: candidates.street_view_url });

    let photoIndex = 0;
    for (const candidate of urlCandidates) {
      if (photoIndex >= MAX_PHOTOS_PER_SPOT) break;

      const buffer = await downloadImage(candidate.url);
      if (!buffer) {
        logToTower('INFO', `   ⚠️ Skipped [${candidate.key}] — not downloadable`);
        continue;
      }

      const localUrl = saveToLocalDisk(buffer, target.id, target.state || 'US', photoIndex);
      if (localUrl) {
        localUrls.push(localUrl);
        logToTower('INFO', `   ✅ [${candidate.key}] → saved locally (${(buffer.length / 1024).toFixed(1)} KB)`);
        photoIndex++;
      }
    }

    // Store local HTTP URLs — served by CCTower /api/photos static handler
    const finalPhotos: string[] | null = localUrls.length > 0 ? localUrls : null;

    if (finalPhotos) {
      updateLocalSpot(target.id, {
        photos: finalPhotos,
        verification_status: 'MEDIA_READY',
        last_attempted_at: new Date().toISOString()
      });

      logToTower('INFO', `✨ ${target.name} → MEDIA_READY (${finalPhotos.length} photos saved to disk)`);
    } else {
      // Null out candidate_photos so this record is skipped on future runs, but promote to MEDIA_READY so it doesn't get stuck.
      updateLocalSpot(target.id, {
        candidate_photos: null,
        verification_status: 'MEDIA_READY',
        last_attempted_at: new Date().toISOString()
      });
      logToTower('INFO', `⚠️ ${target.name} → no photos obtainable, cleared candidates and promoted to MEDIA_READY`);
    }

    reportPulse(COOLDOWN_MS);
    await sleep(COOLDOWN_MS);
  }
}

runPhotographerLoop().catch(err => {
  console.error('[Photographer] Fatal error:', err);
  process.exit(1);
});
