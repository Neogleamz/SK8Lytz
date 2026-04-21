/**
 * Photographer.ts — Phase 4 Media Harvest Daemon
 *
 * Reads candidate_photos collected by the Indexer, downloads each image URL binary,
 * uploads to Supabase Storage (spot-photos bucket), and writes the final CDN URL array
 * to the `photos` column. Sets verification_status = 'MEDIA_READY'.
 *
 * Zero Puppeteer footprint. Zero new API costs.
 * Photo sources: OG image, DOM large images, Street View Static (free), Facebook OG.
 */
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import fetch from 'node-fetch';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '',
  process.env.SUPABASE_SERVICE_ROLE_KEY || ''
);

const STORAGE_BUCKET = 'spot-photos';
const COOLDOWN_MS = 800; // ms between records
const MAX_PHOTOS_PER_SPOT = 4; // OG + up to 3 DOM images

const reportPulse = (delayMs: number) => {
  fetch('http://localhost:5999/api/pulse', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source: 'Phase 5', delayMs })
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
 * Upload a Buffer to Supabase Storage.
 * Returns the public CDN URL or null on failure.
 */
async function uploadToStorage(
  buffer: Buffer,
  spotId: string,
  state: string,
  photoIndex: number,
  mimeType = 'image/jpeg'
): Promise<string | null> {
  const ext = mimeType.includes('png') ? 'png' : mimeType.includes('webp') ? 'webp' : 'jpg';
  const filePath = `${state || 'US'}/${spotId}/photo_${photoIndex}.${ext}`;

  const { error } = await supabase.storage
    .from(STORAGE_BUCKET)
    .upload(filePath, buffer, {
      contentType: mimeType,
      upsert: true
    });

  if (error) {
    logToTower('ERROR', `Storage upload failed [${filePath}]: ${error.message}`);
    return null;
  }

  const { data: publicData } = supabase.storage
    .from(STORAGE_BUCKET)
    .getPublicUrl(filePath);

  return publicData?.publicUrl || null;
}

/**
 * Main processing loop — runs continuously until process is killed.
 */
async function runPhotographerLoop() {
  logToTower('INFO', '🚀 Photographer daemon starting — Phase 4 Media Harvest active');

  let consecutiveIdle = 0;

  while (true) {
    // Query: next spot with candidates not yet having final photos
    const { data: target, error: queryError } = await supabase
      .from('skate_spots')
      .select('id, name, state, candidate_photos, photos, verification_status')
      .not('candidate_photos', 'is', null)
      .is('photos', null)
      .in('verification_status', ['ENRICHED', 'IDENTITY_ESTABLISHED', 'INDEXED'])
      .order('last_attempted_at', { ascending: true, nullsFirst: true })
      .limit(1)
      .single();

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

    const candidates = target.candidate_photos as Record<string, any>;
    const cdnUrls: string[] = [];

    // Build ordered list of photo URLs to attempt
    const urlCandidates: Array<{ key: string; url: string }> = [];
    if (candidates.og_image) urlCandidates.push({ key: 'og_image', url: candidates.og_image });
    if (candidates.dom_images?.length) {
      candidates.dom_images.forEach((url: string, i: number) =>
        urlCandidates.push({ key: `dom_${i}`, url })
      );
    }
    if (candidates.facebook_og) urlCandidates.push({ key: 'facebook_og', url: candidates.facebook_og });
    // Street View is always last — guaranteed fallback
    if (candidates.street_view_url) urlCandidates.push({ key: 'street_view', url: candidates.street_view_url });

    let photoIndex = 0;
    for (const candidate of urlCandidates) {
      if (photoIndex >= MAX_PHOTOS_PER_SPOT) break;

      const buffer = await downloadImage(candidate.url);
      if (!buffer) {
        logToTower('INFO', `   ⚠️ Skipped [${candidate.key}] — not downloadable`);
        continue;
      }

      const cdnUrl = await uploadToStorage(buffer, target.id, target.state || 'US', photoIndex);
      if (cdnUrl) {
        cdnUrls.push(cdnUrl);
        logToTower('INFO', `   ✅ [${candidate.key}] → ${cdnUrl.slice(0, 60)}...`);
        photoIndex++;
      }
    }

    // Street View guaranteed fallback if nothing downloaded
    if (cdnUrls.length === 0 && candidates.street_view_url && !urlCandidates.find(u => u.key === 'street_view')) {
      cdnUrls.push(candidates.street_view_url); // Store URL directly as fallback (no download needed)
    }

    // Always store at least the Street View URL directly if photos is empty
    const finalPhotos = cdnUrls.length > 0 ? cdnUrls : (candidates.street_view_url ? [candidates.street_view_url] : null);

    if (finalPhotos) {
      await supabase.from('skate_spots').update({
        photos: finalPhotos,
        verification_status: 'MEDIA_READY',
        last_attempted_at: new Date().toISOString()
      }).eq('id', target.id);

      logToTower('INFO', `✨ ${target.name} → MEDIA_READY (${finalPhotos.length} photos)`);
    } else {
      // Null out candidate_photos to skip this record next run
      await supabase.from('skate_spots').update({
        candidate_photos: null,
        last_attempted_at: new Date().toISOString()
      }).eq('id', target.id);
      logToTower('INFO', `⚠️ ${target.name} → no photos downloadable, cleared candidates`);
    }

    reportPulse(COOLDOWN_MS);
    await sleep(COOLDOWN_MS);
  }
}

runPhotographerLoop().catch(err => {
  console.error('[Photographer] Fatal error:', err);
  process.exit(1);
});
