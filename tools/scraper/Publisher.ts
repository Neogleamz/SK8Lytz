/**
 * Publisher.ts — Phase 5 Sync-to-Production Daemon
 *
 * This daemon acts as a one-way replication engine. It monitors the local_spots
 * table for records where \`is_published = 1\` and \`sync_required = 1\`.
 * It then securely pushes these vetted records to the remote Supabase \`skate_spots\`
 * production table, and clears the \`sync_required\` flag.
 *
 * It also handles unpublishing: if a spot has \`is_published = 0\` and \`sync_required = 1\`,
 * it deletes the record from the remote Supabase database.
 */

import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import fs from 'fs';
import fetch from 'node-fetch';
import { db, getSpotsToSync, markSpotSynced, updateLocalSpot, getFieldRegistry } from './core/LocalDB';

// Load .env
const envPaths = [
  path.resolve(__dirname, '../../.env'),
  path.resolve(process.cwd(), '.env'),
  path.resolve(process.cwd(), '../../.env'),
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.env',
];

for (const p of envPaths) {
  const result = dotenv.config({ path: p });
  if (!result.error && process.env.EXPO_PUBLIC_SUPABASE_URL) break;
}

if (!process.env.EXPO_PUBLIC_SUPABASE_URL) {
  console.error('[Publisher] ❌ Failed to load .env — EXPO_PUBLIC_SUPABASE_URL is missing.');
  process.exit(1);
}

// ALWAYS use service role key for the publisher to bypass RLS policies during bulk syncs
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '',
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

const COOLDOWN_MS = 5000;

// ─── Telemetry ──────────────────────────────────────────────────────────────
const _log = console.log;
const _err = console.error;

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

const pushLog = (type: 'INFO' | 'ERROR', message: string) => {
  queueLog(type, 'Publisher', message);
};

const reportPulse = (delayMs: number, target?: string | null) => {
  fetch('http://localhost:5999/api/pulse', { 
    method: 'POST', 
    headers: { 'Content-Type': 'application/json' }, 
    body: JSON.stringify({ source: 'Publisher', delayMs, active_job: 'Syncing', target }) 
  }).catch(() => {});
};

console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

async function runPublisher() {
  if (process.env.SCRAPER_REGISTER_ONLY === 'true') {
    console.log('[Publisher] PM2 registration mode: exiting immediately.');
    process.exit(0);
  }
  console.log('[Publisher] 🚀 Booting Sync-to-Production Daemon...');

  while (true) {
    try {
      // Fetch up to 50 spots that need syncing
      const spotsToSync = getSpotsToSync(50);

      if (spotsToSync.length === 0) {
        reportPulse(COOLDOWN_MS * 2);
        await sleep(COOLDOWN_MS * 2);
        continue;
      }

      console.log(`[Publisher] Found ${spotsToSync.length} spot(s) pending sync.`);

      const fields = getFieldRegistry();
      const requiredFields = fields.filter(f => f.is_hard_gate === 1).map(f => f.field_name);

      for (const spot of spotsToSync) {
        reportPulse(0, spot.name);

        if (spot.is_published) {
          // Guillotine Check: Verify all required fields are present
          let missingRequired = false;
          let missingFieldName = '';
          for (const rf of requiredFields) {
            const val = spot[rf];
            if (val === null || val === undefined || val === '' || 
               (Array.isArray(val) && val.length === 0) || 
               (typeof val === 'string' && val === '[]')) {
              missingRequired = true;
              missingFieldName = rf;
              break;
            }
          }

          if (missingRequired) {
            console.error(`[Publisher] 🛑 GUILLOTINE: Rejected [${spot.id}] ${spot.name} (Missing required field: ${missingFieldName})`);
            // updateLocalSpot(spot.id, { is_published: 0, pipeline_status: 'REJECTED', verification_status: 'REJECTED' });
            markSpotSynced(spot.id);
            continue;
          }

          // Clean payload for Supabase insertion (exclude local-only columns if any)
          const payload = { ...spot };
          delete payload.sync_required; // Don't push local sync flag
          delete payload.raw_data; // Exclude raw backup JSON

          // Automatically strip all local-only database columns that aren't in Supabase production
          const allowedKeys = new Set([
            'id', 'name', 'lat', 'lng', 'city', 'state', 'zip', 'street_address', 'phone_number', 'website',
            'google_place_id', 'google_maps_url', 'business_status', 'rating', 'user_ratings_total',
            'opening_hours', 'operator_description', 'facility_type', 'is_published', 'verification_status',
            'has_adult_night', 'has_pro_shop', 'has_proshop', 'is_deep_crawled', 'raw_knowledge_panel',
            'photos', 'candidate_photos', 'candidate_links', 'ai_metadata', 'last_attempted_at',
            'last_enriched_at', 'retry_count', 'created_at', 'surface_type', 'is_indoor', 'adult_night_details',
            'source', 'is_verified', 'updated_at', 'updated_by', 'is_featured', 'has_lights', 'has_fee',
            'operator_name', 'has_rental', 'is_wheelchair_accessible', 'has_wifi', 'has_toilets', 'has_food',
            'has_ac', 'has_lockers', 'capacity', 'hosts_derby', 'surface_quality', 'vibe_score', 'vibe_rating',
            'cultural_metadata', 'instagram_url', 'facebook_url', 'tiktok_url', 'schedule_url', 'pricing_data',
            'special_events', 'adult_night_schedule', 'email_addresses', 'address', 'phone', 'socials'
          ]);

          Object.keys(payload).forEach(key => {
            if (!allowedKeys.has(key)) {
              delete payload[key];
            }
          });

          // Serialize JSON columns properly for Supabase JSONB
          ['opening_hours', 'raw_knowledge_panel', 'photos', 'candidate_photos', 'candidate_links', 'ai_metadata', 'email_addresses'].forEach(field => {
            if (payload[field] && typeof payload[field] === 'string') {
              try { payload[field] = JSON.parse(payload[field]); } catch (e) {}
            }
          });

          // Upload local photos to Supabase Storage before upsert
          if (payload.photos && Array.isArray(payload.photos)) {
            const remotePhotos: string[] = [];
            for (let i = 0; i < payload.photos.length; i++) {
              const photoUrl = payload.photos[i];
              
              const isLocalPhoto = typeof photoUrl === 'string' && photoUrl.startsWith('http://localhost:5999/api/photos');
              const isLocalBucket = typeof photoUrl === 'string' && photoUrl.startsWith('/local-bucket/');

              if (isLocalPhoto || isLocalBucket) {
                let localPath = '';
                let filename = '';
                let storagePath = '';

                if (isLocalPhoto) {
                  const urlParts = photoUrl.split('/');
                  filename = urlParts.pop() || '';
                  const id = urlParts.pop() || '';
                  const state = urlParts.pop() || '';
                  if (state && id && filename) {
                    localPath = path.resolve(__dirname, '../../.scraper-data/photos', state, id, filename);
                    storagePath = `${state}/${id}/${filename}`;
                  }
                } else if (isLocalBucket) {
                  const urlParts = photoUrl.split('/');
                  filename = urlParts.pop() || '';
                  if (filename) {
                    localPath = path.resolve(__dirname, '../../.scraper-data/bucket', filename);
                    storagePath = `${spot.state || 'US'}/${spot.id}/${filename}`;
                  }
                }

                if (localPath && fs.existsSync(localPath)) {
                  const fileBuffer = fs.readFileSync(localPath);
                  const ext = filename.split('.').pop() || 'jpg';
                  const contentType = ext === 'png' ? 'image/png' : ext === 'webp' ? 'image/webp' : 'image/jpeg';
                  
                  console.log(`[Publisher] ⬆️  Uploading ${filename}...`);
                  const { data, error } = await supabase.storage.from('spot-photos').upload(storagePath, fileBuffer, {
                    contentType,
                    upsert: true
                  });
                  
                  if (error) {
                    console.error(`[Publisher] ❌ Failed to upload photo ${filename}: ${error.message}`);
                    remotePhotos.push(photoUrl);
                  } else {
                    const { data: publicUrlData } = supabase.storage.from('spot-photos').getPublicUrl(storagePath);
                    if (publicUrlData && publicUrlData.publicUrl) {
                      remotePhotos.push(publicUrlData.publicUrl);
                      console.log(`[Publisher] ☁️  Uploaded -> ${publicUrlData.publicUrl}`);
                    } else {
                      remotePhotos.push(photoUrl);
                    }
                  }
                } else {
                  console.warn(`[Publisher] ⚠️ Local photo not found on disk: ${localPath}`);
                  remotePhotos.push(photoUrl);
                }
              } else {
                remotePhotos.push(photoUrl);
              }
            }
            payload.photos = remotePhotos;
          }

          const { error } = await supabase.from('skate_spots').upsert(payload, { onConflict: 'id' });

          if (error) {
            console.error(`[Publisher] ❌ Failed to upsert [${spot.id}] ${spot.name}: ${error.message}`);
            if (error.message.includes('invalid input syntax for type uuid')) {
              // updateLocalSpot(spot.id, { is_published: 0, pipeline_status: 'REJECTED', verification_status: 'REJECTED' });
              markSpotSynced(spot.id);
            }
          } else {
            console.log(`[Publisher] ✅ Synced to Production: ${spot.name}`);
            markSpotSynced(spot.id);
          }
        } else {
          // Spot is unpublished — remove it from production
          const { error } = await supabase.from('skate_spots').delete().eq('id', spot.id);
          
          if (error) {
            console.error(`[Publisher] ❌ Failed to retract [${spot.id}] ${spot.name}: ${error.message}`);
            if (error.message.includes('invalid input syntax for type uuid')) {
              // updateLocalSpot(spot.id, { is_published: 0, pipeline_status: 'REJECTED', verification_status: 'REJECTED' });
              markSpotSynced(spot.id);
            }
          } else {
            console.log(`[Publisher] 🗑️  Retracted from Production: ${spot.name}`);
            markSpotSynced(spot.id);
          }
        }
        
        await sleep(500); // Throttling Supabase API requests
      }

    } catch (err: any) {
      console.error('[Publisher Error]', err.message);
    } finally {
      reportPulse(COOLDOWN_MS);
      await sleep(COOLDOWN_MS);
    }
  }
}

runPublisher();
