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
import { db, getSpotsToSync, markSpotSynced, updateLocalSpot, getFieldRegistry, getConfig } from './core/LocalDB';
import { validateAndCleanField } from './core/ValidatorEngine';
import { analyzePhotoWithVision } from './core/VisionLLM';
import type { VisionLLMOptions } from './core/VisionLLM';

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
      // ── Phase Interlocking (Wait for other phases to finish) ──
      try {
        const statusRes = await fetch('http://localhost:5999/status').then((r: any) => r.json());
        if (statusRes) {
          const { scout, resolver, detective, photographer } = statusRes;
          // We check if any of the active scraping phases are running.
          // Note: resolver is Phase 2, detective is Phase 3, photographer is Phase 4, scout is Phase 1
          const anyActive = scout?.alive || resolver?.alive || detective?.alive || photographer?.alive;
          if (anyActive) {
            console.log('[Publisher] ⏳ Waiting for other phases to finish before starting the Vision Pass...');
            reportPulse(10000, 'WAITING', 'Other phases are active');
            await sleep(10000);
            continue;
          }
        }
      } catch (err: any) {
        console.error(`[Publisher] Failed to check master status: ${err.message}`);
      }

      // Fetch up to 50 spots that need syncing
      const spotsToSync = getSpotsToSync(50);

      if (spotsToSync.length === 0) {
        reportPulse(COOLDOWN_MS * 2);
        await sleep(COOLDOWN_MS * 2);
        continue;
      }

      console.log(`[Publisher] Found ${spotsToSync.length} spot(s) pending sync.`);

      const fields = getFieldRegistry();
      const requiredFields = fields.filter(f => f.is_hard_gate === 1);

      // ── Fetch Publisher Config from DB ──
      const pubConfig = getConfig();
      const visionEnabled = pubConfig.publisher_vision_enabled !== false;
      const visionPrompt = pubConfig.publisher_vision_prompt || 'You are a photo analyst for a roller skating rink directory. Analyze this image and classify it.\n\nRespond with ONLY valid JSON:\n{\n  "category": "exterior|interior|floor|pro_shop|action|logo|flyer|reject",\n  "score": 1-10\n}';
      const visionModelId = pubConfig.publisher_vision_model || 'qwen2.5-vl-7b-instruct';
      const visionOpts: VisionLLMOptions = {
        model: visionModelId,
        temperature: pubConfig.publisher_vision_temperature ?? 0.1,
        host: process.env.LM_STUDIO_HOST || 'localhost',
      };
      const visionMinScore = pubConfig.publisher_vision_min_score ?? 3;
      const requiredPhotoTags: string[] = pubConfig.publisher_required_photo_tags || [];
      const detectiveModel = pubConfig.detective_model || 'qwen2.5-7b-instruct-1m';

      // ── AUTO MODEL SWAP: Load Vision Model ──
      // JIT-load the vision model into LM Studio before processing photos.
      // LM Studio auto-unloads the previous model when VRAM is constrained.
      let visionModelReady = false;
      if (visionEnabled) {
        const lmsHost = process.env.LM_STUDIO_HOST || 'localhost';
        const lmsPort = 1234;
        console.log(`[Publisher] 🔄 AUTO-SWAP: Loading vision model "${visionModelId}" into LM Studio...`);
        reportPulse(0, 'LOADING', `Swapping to vision model: ${visionModelId}`);

        try {
          // Send a JIT warmup request — LM Studio will auto-load the model
          const warmupRes = await fetch(`http://${lmsHost}:${lmsPort}/v1/chat/completions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              model: visionModelId,
              messages: [{ role: 'user', content: [{ type: 'text', text: 'Respond with: READY' }] }],
              max_tokens: 5,
              temperature: 0,
            }),
          });

          if (warmupRes.ok) {
            const warmupData: any = await warmupRes.json();
            const reply = warmupData?.choices?.[0]?.message?.content || '';
            console.log(`[Publisher] ✅ Vision model loaded and responding: "${reply.trim().substring(0, 30)}"`);
            visionModelReady = true;
          } else {
            const errText = await warmupRes.text();
            console.error(`[Publisher] ⚠️ Vision model warmup failed (${warmupRes.status}): ${errText.substring(0, 200)}`);
            console.log('[Publisher] ⏭️ Skipping Vision Pass — model not available. Proceeding with Guillotine only.');
          }
        } catch (swapErr: any) {
          console.error(`[Publisher] ❌ Failed to connect to LM Studio for model swap: ${swapErr.message}`);
          console.log('[Publisher] ⏭️ Skipping Vision Pass — LM Studio unreachable. Proceeding with Guillotine only.');
        }
      }

      for (const spot of spotsToSync) {
        reportPulse(0, spot.name);

        // ═══════════════════════════════════════════════════════════
        // PASS 1: VISION LLM — Photo Curation, Tagging & Scoring
        // ═══════════════════════════════════════════════════════════
        if (visionModelReady) {
          let photos: any[] = [];
          try {
            photos = typeof spot.photos === 'string' ? JSON.parse(spot.photos) : (spot.photos || []);
          } catch { photos = []; }

          if (photos.length > 0) {
            console.log(`[Publisher] 👁️ Vision Pass: Analyzing ${photos.length} photo(s) for [${spot.id}] ${spot.name}`);
            const analyzedPhotos: any[] = [];

            for (const photo of photos) {
              const photoUrl = typeof photo === 'string' ? photo : photo?.url;
              if (!photoUrl) { analyzedPhotos.push(photo); continue; }

              // Read the local file as base64
              const isLocal = photoUrl.startsWith('http://localhost:5999/api/photos');
              if (!isLocal) {
                // Remote/already-uploaded photo — keep as-is, can't analyze without downloading
                analyzedPhotos.push(photo);
                continue;
              }

              try {
                const urlParts = photoUrl.split('/');
                const filename = urlParts.pop() || '';
                const id = urlParts.pop() || '';
                const state = urlParts.pop() || '';
                const localPath = path.resolve(__dirname, '../../.scraper-data/photos', state, id, filename);

                if (!fs.existsSync(localPath)) {
                  analyzedPhotos.push(photo);
                  continue;
                }

                const imgBuf = fs.readFileSync(localPath);
                const b64 = imgBuf.toString('base64');

                const result = await analyzePhotoWithVision(b64, visionPrompt, visionOpts);

                if (!result.valid) {
                  console.log(`[Publisher]   🚫 Rejected: ${filename} (category: reject)`);
                  continue; // Drop this photo entirely
                }

                if (result.score !== undefined && result.score < visionMinScore) {
                  console.log(`[Publisher]   📉 Dropped low-score: ${filename} (score: ${result.score}, min: ${visionMinScore})`);
                  continue; // Drop this photo
                }

                // Build the enriched photo object
                const enrichedPhoto = typeof photo === 'string'
                  ? { url: photo, type: result.category || 'unknown', source: 'vision', confidence: 1.0, signals: ['vision_llm'], visionScore: result.score || 5 }
                  : { ...photo, type: result.category || photo.type || 'unknown', visionScore: result.score || 5, signals: [...(photo.signals || []), 'vision_llm'] };

                console.log(`[Publisher]   ✅ ${filename} → ${result.category} (score: ${result.score})`);
                analyzedPhotos.push(enrichedPhoto);
              } catch (photoErr: any) {
                console.warn(`[Publisher]   ⚠️ Vision analysis failed for photo: ${photoErr.message}`);
                analyzedPhotos.push(photo); // Keep the photo on error
              }
            }

            // Sort by visionScore descending — best photos first
            analyzedPhotos.sort((a, b) => {
              const scoreA = (typeof a === 'object' ? a.visionScore : 0) || 0;
              const scoreB = (typeof b === 'object' ? b.visionScore : 0) || 0;
              return scoreB - scoreA;
            });

            // Persist the curated, sorted photos back to local DB
            spot.photos = JSON.stringify(analyzedPhotos);
            updateLocalSpot(spot.id, { photos: spot.photos });
            console.log(`[Publisher] 👁️ Vision Pass complete: ${analyzedPhotos.length}/${photos.length} photos kept for [${spot.id}]`);
          }
        }

        // ═══════════════════════════════════════════════════════════
        // PASS 2: GUILLOTINE — Required Photo Tags Check
        // ═══════════════════════════════════════════════════════════
        if (requiredPhotoTags.length > 0) {
          let photos: any[] = [];
          try {
            photos = typeof spot.photos === 'string' ? JSON.parse(spot.photos) : (spot.photos || []);
          } catch { photos = []; }

          const presentTags = new Set(photos.map((p: any) => typeof p === 'object' ? p.type : 'unknown'));
          const missingTags = requiredPhotoTags.filter(tag => !presentTags.has(tag));

          if (missingTags.length > 0) {
            console.error(`[Publisher] 🛑 GUILLOTINE: Rejected [${spot.id}] ${spot.name} — missing required photo tags: ${missingTags.join(', ')}`);
            markSpotSynced(spot.id);
            continue;
          }
        }

        if (spot.is_published) {
          // Guillotine Check: Verify all required fields are present and valid
          let missingRequired = false;
          let missingFieldName = '';
          let validationError = '';
          let cleanUpdates: any = {};

          for (const rf of requiredFields) {
            const rawVal = spot[rf.field_name];
            
            const vResult = validateAndCleanField(rawVal, rf.validation_rule || 'NONE');
            if (!vResult.valid) {
              missingRequired = true;
              missingFieldName = rf.field_name;
              validationError = vResult.reason || 'Invalid format';
              break;
            } else if (vResult.cleanValue !== rawVal && vResult.cleanValue !== undefined) {
              // The validator cleaned the value, queue it for local DB update
              cleanUpdates[rf.field_name] = vResult.cleanValue;
              spot[rf.field_name] = vResult.cleanValue; // Update local memory object before Supabase push
            }
          }

          if (missingRequired) {
            console.error(`[Publisher] 🛑 GUILLOTINE: Rejected [${spot.id}] ${spot.name} (${missingFieldName} - ${validationError})`);
            // updateLocalSpot(spot.id, { is_published: 0, pipeline_status: 'REJECTED', verification_status: 'REJECTED' });
            markSpotSynced(spot.id);
            continue;
          }

          // If the validator cleaned any fields, persist them back to LocalDB
          if (Object.keys(cleanUpdates).length > 0) {
            console.log(`[Publisher] 🧹 Auto-Formatting applied for [${spot.id}]:`, Object.keys(cleanUpdates));
            updateLocalSpot(spot.id, cleanUpdates);
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

      // ── AUTO MODEL SWAP: Restore Detective Model ──
      // After all spots are processed, swap back to the text model so
      // Detective/Indexer can resume on the next cycle.
      if (visionModelReady) {
        const lmsHost = process.env.LM_STUDIO_HOST || 'localhost';
        const lmsPort = 1234;
        console.log(`[Publisher] 🔄 AUTO-SWAP: Restoring detective model "${detectiveModel}"...`);
        try {
          await fetch(`http://${lmsHost}:${lmsPort}/v1/chat/completions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              model: detectiveModel,
              messages: [{ role: 'user', content: 'wake up' }],
              max_tokens: 1,
            }),
          });
          console.log(`[Publisher] ✅ Detective model "${detectiveModel}" restored.`);
        } catch (restoreErr: any) {
          console.warn(`[Publisher] ⚠️ Failed to restore detective model: ${restoreErr.message}. You may need to reload it manually in LM Studio.`);
        }
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
