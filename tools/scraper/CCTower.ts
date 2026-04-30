import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import fs from 'fs';
import path from 'path';
import { EventEmitter } from 'events';
import { exec } from 'child_process';
import { startGoogleSweep, stopGoogleSweep, isGoogleSweepActive } from './GoogleSweep';
import { GooglePlacesProvider, RETAIL_BLOCKLIST } from './lib/providers/GooglePlacesProvider';
import { executeSpider } from './core/SpiderEngine';
import { executeDetective } from './core/DetectiveEngine';
import { db, getLocalSpots, getLocalCount, updateLocalSpot, deleteLocalSpot, upsertLocalSpot, getConfig, updateConfig, getBlocklist, addBlocklist, deleteBlocklist, addBlocklistKeyword, getPipelineStats, getFieldRegistry } from './core/LocalDB';

const US_STATES = [
  'AL','AK','AZ','AR','CA','CO','CT','DE','FL','GA','HI','ID','IL','IN','IA',
  'KS','KY','LA','ME','MD','MA','MI','MN','MS','MO','MT','NE','NV','NH','NJ',
  'NM','NY','NC','ND','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VT',
  'VA','WA','WV','WI','WY'
];

let isHarvestingActive = false;
const processState = async (state: string, targets: any[], force?: boolean) => { console.log(`[DEPRECATED] OSM Harvest called for ${state}`); };
const startNationalHarvester = async (targets: any[], states: any[]) => { console.log(`[DEPRECATED] OSM Harvest started`); };
const stopNationalHarvester = async () => { console.log(`[DEPRECATED] OSM Harvest stopped`); };


 // --- Logging Infrastructure ---
const LOG_DIR = path.resolve(__dirname, './logs');
const LOG_FILE = path.join(LOG_DIR, 'fleet.log');

if (!fs.existsSync(LOG_DIR)) {
  fs.mkdirSync(LOG_DIR, { recursive: true });
}

export const logEmitter = new EventEmitter();
const originalLog = console.log;
const originalError = console.error;

function writeToLogFile(type: 'INFO' | 'ERROR', message: string) {
  const timestamp = new Date().toISOString();
  const logEntry = `[${timestamp}] [${type}] ${message}\n`;
  fs.appendFileSync(LOG_FILE, logEntry);
}

const autoTagSource = (msg: string) => {
  if (msg.includes('[Harvester]') || msg.includes('[GIS]') || msg.includes('[GHOST]') ||
      msg.includes('Golden Seed') || msg.includes('GoogleSweep') || msg.includes('[GoogleSweep]')) return 'Phase 1';
  if (msg.includes('[Operator]') || msg.includes('Google Captcha') || msg.includes('Overpass')) return 'Phase 2';
  if (msg.includes('[Indexer]')) return 'Phase 3';
  if (msg.includes('[Photographer]')) return 'Phase 5';
  return 'System';
}

console.log = (...args) => {
  const msg = args.join(' ');
  originalLog(...args);
  writeToLogFile('INFO', msg);
  logEmitter.emit('log', { type: 'INFO', source: autoTagSource(msg), message: msg });
};

console.error = (...args) => {
  const msg = args.join(' ');
  originalError(...args);
  writeToLogFile('ERROR', msg);
  logEmitter.emit('log', { type: 'ERROR', source: autoTagSource(msg), message: msg });
};

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const app = express();
app.use(cors());
app.use(express.json());

// ─── Local Photo Storage ────────────────────────────────────────────────────
// Photographer saves images here; CCTower serves them via /api/photos
const PHOTOS_DIR = path.resolve(__dirname, '../../.scraper-data/photos');
if (!fs.existsSync(PHOTOS_DIR)) {
  fs.mkdirSync(PHOTOS_DIR, { recursive: true });
}
app.use('/api/photos', express.static(PHOTOS_DIR));
// ────────────────────────────────────────────────────────────────────────────

let isRunning = false;
let isHeadless = true;
let currentTarget = 'Idle';
let enrichedCount = 0;
let errorCount = 0;
let consecutiveErrors = 0; // Tracking for circuit breaker
let lastError: string | null = null;
let isGated = false; // Circuit Breaker status

// --- Pulse Registry for Real-time Telemetry ---
interface PulseData {
  lastRunAt: string | null;
  nextRunAt: string | null;
  delayMs: number;
  ghost?: {
    userAgent: string;
    viewport: { width: number; height: number };
  }
}

const pulseRegistry: Record<string, PulseData> = {
  'Phase 3': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 4': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 5': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 6': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
};

// Active job tracker — daemons report what they are currently processing
const activeJobRegistry: Record<string, { active_job: string | null; target: string | null; updated_at: string }> = {
  'Phase 3': { active_job: null, target: null, updated_at: '' },
  'Phase 4': { active_job: null, target: null, updated_at: '' },
};

// Async sleep helper
const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

async function fetchConfig() {
  let data;
  try {
    data = getConfig();
  } catch (error) {
    console.error("Error reading scraper_config:", error);
  }
  return data || { 
    sleep_interval_ms: 10000, 
    target_facilities: [], 
    state_override: null,
    cooldown_base_ms: 300000,
    cooldown_jitter_pct: 20,
    max_consecutive_errors: 3,
    auto_resume_enabled: true,
    identity_rotation_enabled: true,
    randomize_viewport_enabled: true
  };
}

app.get('/status', async (req, res) => {
  // Let's use PM2 to check if the scrapers are actually running online
  exec('pm2 jlist', { windowsHide: true }, async (err, stdout) => {
    let running = false;
    let indexerStatus = 'Offline';
    let photographerStatus = 'Offline';
    let publisherStatus = 'Offline';

    if (!err && stdout) {
      try {
        const pm2List = JSON.parse(stdout);
        const indexer = pm2List.find((p: any) => p.name === 'scraper-indexer');
        const photographer = pm2List.find((p: any) => p.name === 'scraper-photographer');
        const publisher = pm2List.find((p: any) => p.name === 'scraper-publisher');
        if (indexer?.pm2_env?.status === 'online' || publisher?.pm2_env?.status === 'online') {
            running = true;
        }
        indexerStatus = indexer?.pm2_env?.status || 'Offline';
        photographerStatus = photographer?.pm2_env?.status || 'Offline';
        publisherStatus = publisher?.pm2_env?.status || 'Offline';
      } catch(e) {}
    }

    const totalCount = db.prepare('SELECT COUNT(*) as cnt FROM local_spots').get().cnt;
    const totalProcessed = db.prepare('SELECT COUNT(*) as cnt FROM local_spots WHERE last_attempted_at IS NOT NULL').get().cnt;
    const publishedCount = db.prepare('SELECT COUNT(*) as cnt FROM local_spots WHERE is_published = 1').get().cnt;
    const pendingCount = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'PENDING' OR verification_status IS NULL`).get().cnt;
    const seededCount = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'SEEDED'`).get().cnt;
    const enrichedCount = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'ENRICHED'`).get().cnt;
    const deepCrawledCount = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'DEEP_CRAWLED'`).get().cnt;
    const mediaReadyCount = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'MEDIA_READY'`).get().cnt;
    const candidatesCount = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE candidate_photos IS NOT NULL AND photos IS NULL`).get().cnt;

    res.json({
      isRunning: running,
      isHarvestingActive,
      isHeadless,
      currentTarget: `Indexer: ${indexerStatus} | Photographer: ${photographerStatus}`,
      isGoogleSweepActive,
      totalCount: totalCount || 0,          // COUNT(*) — true total seeded
      processedCount: totalProcessed || 0,
      enrichedCount: enrichedCount || 0,
      mediaReadyCount: mediaReadyCount || 0,
      candidatesReadyCount: candidatesCount || 0,
      publishedCount: publishedCount || 0,
      
      // Pipeline stage counts (matches real verification_status values)
      pendingCount: pendingCount || 0,
      seededCount: seededCount || 0,
      deepCrawledCount: deepCrawledCount || 0,
      
      errorCount,
      consecutiveErrors,
      isGated,
      lastError,
      pulseRegistry
    });
  });
});

app.post('/api/pulse', (req, res) => {
  const { source, delayMs, ghost, active_job, target } = req.body;
  if (pulseRegistry[source]) {
    pulseRegistry[source] = {
      lastRunAt: new Date().toISOString(),
      nextRunAt: new Date(Date.now() + delayMs).toISOString(),
      delayMs,
      ghost
    };
  }
  // Track active job for telemetry display
  if (activeJobRegistry[source] !== undefined) {
    activeJobRegistry[source] = {
      active_job: active_job || null,
      target: target || null,
      updated_at: new Date().toISOString()
    };
  }
  res.json({ success: true });
});

app.post('/start', (req, res) => {
  const { daemons } = (req.body || {}) as { daemons?: string[] };
  const target = (daemons && daemons.length > 0)
    ? daemons.map(d => `scraper-${d}`).join(',')
    : 'scraper-indexer,scraper-photographer,scraper-publisher';
  console.log(`Orchestrating start: ${target}`);
  exec(`pm2 start ecosystem.config.js --only ${target}`, { cwd: __dirname, windowsHide: true }, (err) => {
     if (err) {
        console.error('Failed to start scrapers cluster:', err);
        return res.status(500).json({ success: false, message: 'Start failed', error: err.message });
     }
     isRunning = true;
     res.json({ success: true, message: `Started: ${target}` });
  });
});

app.post('/stop', (req, res) => {
  const { daemons } = (req.body || {}) as { daemons?: string[] };
  const target = (daemons && daemons.length > 0)
    ? daemons.map(d => `scraper-${d}`).join(' ')
    : 'scraper-indexer scraper-photographer scraper-publisher';
  console.log(`Orchestrating stop: ${target}`);
  exec(`pm2 stop ${target}`, { cwd: __dirname, windowsHide: true }, (err) => {
     if (err) {
        console.error('Failed to stop scrapers cluster:', err);
        return res.status(500).json({ success: false, message: 'Stop failed', error: err.message });
     }
     isRunning = false;
     res.json({ success: true, message: `Stopped: ${target}` });
  });
});

app.post('/api/daemons/:name/start', (req, res) => {
  const { name } = req.params;
  const target = `scraper-${name}`;
  console.log(`Commanding daemon start: ${target}`);
  exec(`pm2 start ${target}`, { cwd: __dirname, windowsHide: true }, (err, stdout, stderr) => {
     if (err) {
        console.error(`Failed to start ${name}:`, err);
        return res.status(500).json({ success: false, message: `Failed to start ${name}` });
     }
     res.json({ success: true, message: `${name} daemon started` });
  });
});

app.post('/api/daemons/:name/stop', (req, res) => {
  const { name } = req.params;
  const target = `scraper-${name}`;
  console.log(`Commanding daemon stop: ${target}`);
  exec(`pm2 stop ${target}`, { cwd: __dirname, windowsHide: true }, (err, stdout, stderr) => {
     if (err) {
        console.error(`Failed to stop ${name}:`, err);
        return res.status(500).json({ success: false, message: `Failed to stop ${name}` });
     }
     res.json({ success: true, message: `${name} daemon stopped` });
  });
});
app.get('/api/pipeline/telemetry', async (req, res) => {
  try {
    // Build live telemetry from DB queues + in-memory active job registry
    // in_q = next 3 spots waiting in each phase queue (DB truth)
    const p1 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'PENDING' OR verification_status IS NULL ORDER BY created_at ASC LIMIT 3`).all();
    const p3 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'SEEDED' AND website IS NOT NULL AND website != '' ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 3`).all();
    const p4 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' AND candidate_photos IS NOT NULL AND photos IS NULL ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 3`).all();
    const p6 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'MEDIA_READY' AND is_published = 0 ORDER BY created_at DESC LIMIT 3`).all();

    const names = (rows: any[]) => rows.map((s: any) => s.name);
    const isAlive = (phase: string) => {
      const p = pulseRegistry[phase];
      if (!p?.lastRunAt) return false;
      return (Date.now() - new Date(p.lastRunAt).getTime()) < 600000; // alive if pulsed in last 10 min
    };
    const aj = (phase: string) => activeJobRegistry[phase]?.active_job || null;
    const tgt = (phase: string) => activeJobRegistry[phase]?.target || null;

    res.json({
      detective:  { active_job: aj('Phase 3'), target: tgt('Phase 3'), in_q: names(p3), pulse: pulseRegistry['Phase 3'], alive: isAlive('Phase 3') },
      photographer: { active_job: aj('Phase 4'), target: tgt('Phase 4'), in_q: names(p4), pulse: pulseRegistry['Phase 4'], alive: isAlive('Phase 4') },
      publisher:  { active_job: null, target: null, in_q: names(p6), pulse: pulseRegistry['Phase 6'], alive: false },
    });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.get('/config', async (req, res) => {
  try {
    const config = getConfig();
    res.json({ config });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/config', async (req, res) => {
  const payload = { ...req.body };
  delete payload.id;
  delete payload.daemon_telemetry;
  delete payload.updated_at;

  try {
    updateConfig(payload);
    res.json({ success: true, message: 'Config updated' });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

// --- Sniper Bench End-to-End Pipeline Tracing ---
app.post('/api/sniper/seed', async (req, res) => {
  const { url, spot_name, spot_city } = req.body;
  if (!url) return res.status(400).json({ error: 'URL is required' });
  if (!spot_name || !spot_city) return res.status(400).json({ error: 'spot_name and spot_city are required for Google Places lookup' });

  try {
    // ── Step 1: Text Search to find place_id ────────────────────────────────
    console.log(`[Sniper] Searching Google Places for: "${spot_name} ${spot_city}"`);
    const placeIds = await GooglePlacesProvider.searchRegion(`${spot_name} ${spot_city}`, 'roller_rink');

    if (!placeIds || placeIds.length === 0) {
      // Fallback: seed with just URL + basic info if Google can't find the place
      console.warn(`[Sniper] No Google Places result found for "${spot_name} ${spot_city}" — seeding with URL only`);
      return res.status(404).json({ error: `No Google Places result found for "${spot_name} ${spot_city}". Try adjusting the name or city.` });
    }

    // ── Step 2: Fetch full Place Details for top result ─────────────────────
    const placeId = placeIds[0];
    console.log(`[Sniper] Fetching Place Details for place_id: ${placeId}`);
    const details = await GooglePlacesProvider.getPlaceDetails(placeId);

    if (!details) throw new Error('Place Details returned null for place_id: ' + placeId);
    if (!details.lat || !details.lng) throw new Error('Place Details missing coordinates');

    // Retail blocklist check
    const lowerName = details.name.toLowerCase();
    if (RETAIL_BLOCKLIST.some(block => lowerName.includes(block))) {
      return res.status(400).json({ error: `Place "${details.name}" is on the retail blocklist — not a valid skate facility` });
    }

    // ── Step 3: Parse address components (same logic as GoogleSweep) ─────────
    const parts = details.formatted_address?.split(',') || [];
    let derivedState: string | null = null;
    let derivedCity: string | null = null;
    let derivedZip: string | null = null;
    if (parts.length >= 3) {
      const stateZipStr = parts[parts.length - 2].trim();
      derivedCity = parts[parts.length - 3].trim();
      const splitStateZip = stateZipStr.split(' ');
      if (splitStateZip.length > 0) derivedState = splitStateZip[0];
      if (splitStateZip.length > 1) derivedZip = splitStateZip[1];
    }

    // ── Step 4: Build metaRecord identical to GoogleSweep ────────────────────
    const googlePhotos = details.photos && details.photos.length > 0
      ? { google_refs: details.photos }
      : null;

    const metaRecord: Record<string, any> = {
      name:                 details.name,
      lat:                  details.lat,
      lng:                  details.lng,
      city:                 derivedCity || spot_city,
      state:                derivedState || null,
      zip:                  derivedZip || null,
      street_address:       details.formatted_address || null,
      phone_number:         details.formatted_phone_number || null,
      website:              url || details.website || null, // user-provided URL takes priority
      google_place_id:      details.place_id,
      google_maps_url:      details.google_maps_url || null,
      business_status:      details.business_status || 'OPERATIONAL',
      rating:               details.rating ?? null,
      user_ratings_total:   details.user_ratings_total ?? null,
      opening_hours:        details.opening_hours || null,
      operator_description: details.editorial_summary || null,
      facility_type:        'roller_rink',
      last_enriched_at:     new Date().toISOString(),
      raw_knowledge_panel:  { types: details.types || null },
      verification_status:  'SEEDED',
      retry_count:          -999, // Push to front of queue
      last_attempted_at:    new Date(0).toISOString(),
      ...(googlePhotos ? { candidate_photos: googlePhotos } : {}),
    };

    // ── Step 5: Upsert (same dedup logic as GoogleSweep) ─────────────────────
    let id;
    try {
      // Check if place_id exists to update or insert
      const existing = db.prepare('SELECT id FROM local_spots WHERE google_place_id = ?').get(metaRecord.google_place_id) as any;
      if (existing) {
        id = existing.id;
        updateLocalSpot(id, metaRecord);
      } else {
        id = upsertLocalSpot(metaRecord);
      }
    } catch (error: any) {
      console.error('[Sniper] Seed Upsert Error:', error.message);
      return res.status(500).json({ error: error.message });
    }

    console.log(`[Sniper] ✅ Seeded full Phase-1 local record ID: ${id} for "${details.name}" → ${url}`);
    res.json({ success: true, spot_id: id, place_name: details.name, address: details.formatted_address });

  } catch (err: any) {
    console.error('[Sniper] Seed Error:', err.message);
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/sniper/poll/:id', async (req, res) => {
  try {
    const spot = db.prepare('SELECT * FROM local_spots WHERE id = ?').get(req.params.id);
    res.json({ success: true, spot });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// --- AI Detective Sandbox ---
app.post('/api/sandbox', async (req, res) => {
  const { url, ai_system_prompt, ai_target_vectors, spot_name, spot_city } = req.body;
  if (!url || !ai_system_prompt || !ai_target_vectors) {
    return res.status(400).json({ error: 'url, ai_system_prompt, and ai_target_vectors are required' });
  }

  try {
    let cleanText = '';
    // Image-Trap OCR logic
    if (url.match(/\.(jpeg|jpg|gif|png)$/i)) {
      console.log(`[Image-Trap] Detected image URL, running OCR on ${url}`);
      const Tesseract = require('tesseract.js');
      const { data: { text } } = await Tesseract.recognize(url, 'eng');
      cleanText = text.replace(/\s+/g, ' ').trim();
    } else {
      // Clean DOM and extract text for standard webpages
      const puppeteer = require('puppeteer');
      const browser = await puppeteer.launch({ headless: true });
      const page = await browser.newPage();
      await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 });

      // --- Smart City Spider Logic ---
      if (spot_city) {
        try {
          const links = await page.evaluate(() => {
            return Array.from(document.querySelectorAll('a')).map(a => ({
              text: (a.innerText || '').toLowerCase(),
              href: (a.href || '').toLowerCase()
            }));
          });
          
          const currentHostname = new URL(url).hostname;
          const internalLinks = links.filter(l => {
            try {
              const linkUrl = new URL(l.href);
              return linkUrl.hostname === currentHostname || linkUrl.hostname.includes(currentHostname.replace('www.',''));
            } catch(e) { return false; }
          });

          const targetCityStr = spot_city.toLowerCase();
          let match = internalLinks.find(l => l.text.includes(targetCityStr) || l.href.includes(targetCityStr));
          
          if (!match) {
            match = internalLinks.find(l => l.text.includes('hours') || l.text.includes('location') || l.text.includes('schedule') || l.text.includes('calendar'));
          }

          if (match && match.href && !match.href.startsWith('mailto:') && !match.href.startsWith('tel:')) {
            console.log(`[Sandbox Spider] Hopping to subpage: ${match.href}`);
            await page.goto(match.href, { waitUntil: 'domcontentloaded', timeout: 30000 });
          }
        } catch (spiderErr) {
          console.error('[Sandbox Spider] Heuristic hop failed, defaulting to root:', spiderErr);
        }
      }
      // -------------------------------

      let ocrText = '';
      try {
        console.log(`[Brute Force OCR] Taking full page screenshot for ${url}`);
        const screenshotBuffer = await page.screenshot({ fullPage: true, encoding: 'base64' });
        const Tesseract = require('tesseract.js');
        const { data: { text } } = await Tesseract.recognize(Buffer.from(screenshotBuffer, 'base64'), 'eng');
        ocrText = text.replace(/\s+/g, ' ').trim();
        console.log(`[Brute Force OCR] Extracted ${ocrText.length} characters.`);
      } catch (ocrErr) {
        console.error('[Brute Force OCR] Failed to process screenshot:', ocrErr);
      }

      cleanText = await page.evaluate(() => {
        document.querySelectorAll('nav, footer, script, style, header, iframe, noscript').forEach(el => el.remove());
        return document.body.innerText.replace(/\s+/g, ' ').trim();
      });
      await browser.close();

      if (ocrText && ocrText.length > 20) {
          cleanText = cleanText + '\n\n--- VISUAL OCR TEXT (FROM SCREENSHOT) ---\n\n' + ocrText;
      }
    }

    // Construct the Schema
    const schema = ai_target_vectors.reduce((acc: any, vec: any) => {
      acc[vec.key] = vec.type;
      return acc;
    }, {});

    let contextHeader = "";
    if (spot_name && spot_city) {
      contextHeader = `You are analyzing data for a roller rink. The specific location you are targeting is [${spot_name}] located in [${spot_city}]. This text may contain data for multiple franchise locations. Ignore all other cities. ONLY extract the hours, pricing, and adult nights for the [${spot_name}] location.\n\n`;
    }

    const systemPrompt = `${contextHeader}${ai_system_prompt}\n\nYou MUST return a valid JSON object exactly matching this schema:\n${JSON.stringify(schema, null, 2)}\n\nDo not include any conversational text or markdown code blocks. Just the raw JSON.`;

    // Hit Local LM Studio (OpenAI Compatible)
    const fetch = require('node-fetch');
    const targetModel = req.body.detective_model || 'local-model';
    const response = await fetch('http://localhost:1234/v1/chat/completions', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        model: targetModel,
        messages: [
          { role: 'system', content: systemPrompt },
          { role: 'user', content: `Website/Image Text:\n${cleanText}` }
        ],
        temperature: 0.1,
        stream: false
      })
    });

    if (!response.ok) {
      throw new Error(`LM Studio API failed: ${response.statusText}`);
    }

    const aiData = await response.json();
    let parsedJson = {};
    try {
      const contentString = aiData.choices[0].message.content;
      // Strip out markdown code blocks if the model ignored instructions
      const jsonStr = contentString.replace(/```json/g, '').replace(/```/g, '').trim();
      parsedJson = JSON.parse(jsonStr);
    } catch(e) {
      parsedJson = { error: 'Failed to parse JSON', raw: aiData.choices?.[0]?.message?.content || aiData };
    }

    res.json({ success: true, cleanText, aiResponse: parsedJson });
  } catch (err: any) {
    console.error('Sandbox error:', err);
    res.status(500).json({ error: err.message });
  }
});

// --- Priority States API (Global Active Region) ---
// state_override = [] means nationwide (no priority filter)

app.get('/api/priority-states', async (req, res) => {
  try {
    const config = getConfig();
    res.json({ priority_states: config.state_override || [] });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/api/priority-states', async (req, res) => {
  const { states } = req.body;
  if (!Array.isArray(states)) return res.status(400).json({ error: 'states must be an array of 2-letter codes' });
  const valid = states.filter((s: string) => typeof s === 'string' && s.length === 2).map((s: string) => s.toUpperCase());
  try {
    updateConfig({ state_override: valid });
    res.json({ success: true, priority_states: valid, message: valid.length === 0 ? 'Nationwide (no filter)' : `Active region: ${valid.join(', ')}` });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.delete('/api/priority-states', async (req, res) => {
  try {
    updateConfig({ state_override: [] });
    res.json({ success: true, priority_states: [], message: 'Region cleared -- nationwide mode active' });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

// --- Phase 1: Harvest & Data Grid Routes ---

app.get('/api/harvest/status', async (req, res) => {
  const cacheDir = path.join(__dirname, 'state_caches');
  const seededStates: string[] = [];
  if (fs.existsSync(cacheDir)) {
    const files = fs.readdirSync(cacheDir);
    files.forEach(f => {
      if (f.endsWith('.json')) seededStates.push(f.replace('.json', ''));
    });
  }

  const data = db.prepare('SELECT state FROM local_spots WHERE state IS NOT NULL').all();
  const counts: Record<string, number> = {};
  data.forEach((row: any) => {
    const st = row.state;
    if (st) counts[st] = (counts[st] || 0) + 1;
  });

  res.json({ seededStates, stateCounts: counts, allStates: US_STATES });
});

app.post('/api/harvest', async (req, res) => {
  const { state, target_facilities } = req.body;
  if (!state || !US_STATES.includes(state)) return res.status(400).json({ error: 'Invalid state code' });
  
  processState(state, target_facilities || []).catch(e => console.error(e));
  res.json({ success: true, message: `Started harvest for US-${state}` });
});

app.post('/api/harvest/force', async (req, res) => {
  const { state } = req.body;
  if (!state) return res.status(400).json({ error: 'State code required' });
  
  const { processState } = require('./USANationalHarvest');
  processState(state, [], true).catch(e => console.error(e));
  res.json({ success: true, message: `Forced re-harvest started for ${state}` });
});

app.post('/api/discover', async (req, res) => {
  const { stateFull } = req.body;
  if (!stateFull) return res.status(400).json({ error: 'State name required' });
  
  const { runDirectDiscovery } = require('./Discoverer');
  runDirectDiscovery(stateFull).catch(e => console.error(e));
  res.json({ success: true, message: `Started direct discovery for ${stateFull}` });
});

app.get('/api/spots', async (req, res) => {
  try {
    const spots = getLocalSpots(req.query);
    const count = getLocalCount(req.query);
    res.json({ spots, total: count });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.put('/api/spots/:id', async (req, res) => {
  const { id } = req.params;
  const updates = req.body;
  try {
    updateLocalSpot(id, updates);
    res.json({ success: true });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/api/promote-all', async (req, res) => {
  try {
    db.prepare(`UPDATE local_spots SET is_published = 1 WHERE verification_status IN ('VERIFIED', 'ENRICHED', 'MEDIA_READY')`).run();
    res.json({ success: true, message: 'Bulk promotion successful' });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

// State-scoped publish: promote all eligible records in a single state
app.post('/api/promote-state/:state', async (req, res) => {
  const { state } = req.params;
  if (!state || state.length !== 2) return res.status(400).json({ error: 'Invalid state abbreviation' });

  try {
    const info = db.prepare(`UPDATE local_spots SET is_published = 1 WHERE state = ? AND verification_status IN ('VERIFIED', 'ENRICHED', 'MEDIA_READY')`).run(state.toUpperCase());
    res.json({ success: true, state: state.toUpperCase(), promoted: info.changes });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

// State-scoped unpublish: retract all records in a single state
app.post('/api/unpublish-state/:state', async (req, res) => {
  const { state } = req.params;
  if (!state || state.length !== 2) return res.status(400).json({ error: 'Invalid state abbreviation' });

  try {
    const info = db.prepare(`UPDATE local_spots SET is_published = 0 WHERE state = ?`).run(state.toUpperCase());
    res.json({ success: true, state: state.toUpperCase(), unpublished: info.changes });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.delete('/api/spots/:id', async (req, res) => {
  const { id } = req.params;
  try {
    deleteLocalSpot(id);
    res.json({ success: true });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

// --- NEW OMNI-DAEMON ROUTES ---

app.post('/api/headless', (req, res) => {
  isHeadless = req.body.isHeadless;
  res.json({ success: true, isHeadless });
});

app.get('/api/logs/stream', (req, res) => {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  res.flushHeaders?.();

  const listener = (data: any) => res.write(`data: ${JSON.stringify(data)}\n\n`);
  logEmitter.on('log', listener);
  req.on('close', () => logEmitter.off('log', listener));
});

// External Webhook to push logs into the live SSE stream from PM2 Daemons
app.post('/api/logs/ingest', (req, res) => {
  const { type, source, message } = req.body;
  if (!message) return res.status(400).json({ error: 'Message payload required' });
  
  // Format for internal tracking and logging
  const msgContext = source ? `[${source}] ${message}` : message;
  writeToLogFile(type || 'INFO', msgContext);
  
  // Emit to SSE clients immediately
  logEmitter.emit('log', { type: type || 'INFO', source: source || 'UNKNOWN', message });
  
  res.sendStatus(200);
});

app.post('/api/harvest/start-all', async (req, res) => {
  const { target_facilities, target_states, provider } = req.body;
  
  if (provider === 'google') {
    if (isGoogleSweepActive) return res.json({ success: false, message: 'Google Sweep Already running' });
    startGoogleSweep(target_states || [], target_facilities || []).catch(e => console.error(e));
    return res.json({ success: true, message: 'Google Places sweep started' });
  }

  // Fallback / OSM Mode
  if (isHarvestingActive) return res.json({ success: false, message: 'OSM Harvest Already running' });
  startNationalHarvester(target_facilities || [], target_states || []).catch(e => console.error(e));
  res.json({ success: true, message: 'OSM Harvest sequence started' });
});

app.post('/api/harvest/stop-all', async (req, res) => {
  const { provider } = req.body;
  if (provider === 'google') {
      if (!isGoogleSweepActive) return res.json({ success: false, message: 'Google Sweep Not running' });
      await stopGoogleSweep();
      return res.json({ success: true, message: 'Google Sweep stopping' });
  }

  if (!isHarvestingActive) return res.json({ success: false, message: 'OSM Harvest Not running' });
  await stopNationalHarvester();
  res.json({ success: true, message: 'National Harvest stopping' });
});

// ─── Pipeline Stats: server-side aggregation via RPC (no row-cap) ────────────
app.get('/api/pipeline-stats', async (req, res) => {
  const statesRaw = (req.query.states as string) || '';
  const states = statesRaw ? statesRaw.split(',').map(s => s.trim().toUpperCase()).filter(Boolean) : [];

  try {
    const statsData = getPipelineStats(states);
    const summary = { ...statsData, state: states.length > 0 ? states.join('+') : 'ALL' };
    res.json({ stats: [summary], summary, active_states: states });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});


app.get('/api/recent-spots', async (req, res) => {

  const statesRaw = (req.query.states as string) || '';
  const states = statesRaw ? statesRaw.split(',').map(s => s.trim().toUpperCase()).filter(Boolean) : [];

  let query = 'SELECT * FROM local_spots';
  if (states.length > 0) {
    query += ` WHERE state IN (${states.map(s => `'${s}'`).join(',')})`;
  }
  query += ` ORDER BY created_at DESC NULLS LAST LIMIT 10`;

  try {
    const data = db.prepare(query).all();
    res.json({ spots: data });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.get('/api/field-registry', async (req, res) => {
  try {
    const fields = getFieldRegistry();
    res.json({ fields });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});


app.get('/api/queue', async (req, res) => {
  const { phase } = req.query;
  const statesRaw = (req.query.states as string) || '';
  const states = statesRaw ? statesRaw.split(',').map(s => s.trim().toUpperCase()).filter(Boolean) : [];

  let query = 'SELECT * FROM local_spots WHERE 1=1';
  if (phase === 'phase1') query += ` AND verification_status = 'SEEDED'`;
  else if (phase === 'phase2') query += ` AND verification_status = 'SEEDED' AND website IS NOT NULL AND website != ''`;
  else if (phase === 'phase3') query += ` AND verification_status = 'ENRICHED' AND candidate_links IS NOT NULL`;
  else if (phase === 'phase4') query += ` AND verification_status = 'DEEP_CRAWLED' AND candidate_photos IS NOT NULL AND photos IS NULL`;
  else if (phase === 'phase6') query += ` AND verification_status = 'MEDIA_READY' AND is_published = 0`;
  else if (phase === 'spider-recent') query += ` AND verification_status = 'ENRICHED' AND candidate_links IS NOT NULL`;
  else if (phase === 'detective-recent') query += ` AND verification_status = 'DEEP_CRAWLED'`;
  else query += ` AND (verification_status IN ('SEEDED','ENRICHED','DEEP_CRAWLED') OR verification_status IS NULL)`;

  if (states.length > 0) {
    query += ` AND state IN (${states.map(s => `'${s}'`).join(',')})`;
  }

  const sortAsc = phase !== 'spider-recent';
  query += sortAsc ? ` ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 10` : ` ORDER BY last_attempted_at DESC NULLS LAST LIMIT 10`;

  try {
    const data = db.prepare(query).all();
    res.json({ spots: data, active_states: states });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// --- Scraper Blocklist & Spot Deletion ---

app.put('/api/skate_spots/:id', async (req, res) => {
  const { id } = req.params;
  try {
    updateLocalSpot(id, req.body);
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/skate_spots/:id', async (req, res) => {
  const { id } = req.params;
  const { blacklist } = req.query;

  try {
    if (blacklist === 'true') {
      const spot = db.prepare('SELECT name FROM local_spots WHERE id = ?').get(id) as any;
      if (spot && spot.name) {
        // Strip common suffixes and trim for the blocklist keyword
        let keyword = spot.name.toLowerCase().trim();
        // Insert into blocklist
        addBlocklistKeyword(keyword);
        console.log( `Blacklisted keyword extracted from spot: "${keyword}"`);
      }
    }
    
    deleteLocalSpot(id);
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/skate_spots/:id/restart', async (req, res) => {
  const { id } = req.params;
  try {
    updateLocalSpot(id, {
      verification_status: 'SEEDED',
      last_attempted_at: null,
      candidate_links: null,
      candidate_photos: null,
      photos: null,
      opening_hours: null,
      pricing_data: null
    });
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/skate_spots/:id/freeze', async (req, res) => {
  const { id } = req.params;
  try {
    updateLocalSpot(id, { verification_status: 'ON_HOLD' });
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/scraper/blocklist', async (req, res) => {
  try {
    const keywords = getBlocklist();
    res.json({ keywords });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/api/scraper/blocklist', async (req, res) => {
  const { keyword, match_type = 'name', reason = '' } = req.body;
  if (!keyword) return res.status(400).json({ error: 'Missing keyword' });
  const kw = keyword.toLowerCase().trim();
  
  try {
    addBlocklist(kw, match_type, reason);
    
    // Execute SQL Guillotine on local DB
    const info = db.prepare(`DELETE FROM local_spots WHERE name LIKE ?`).run(`%${kw}%`);
      
    console.log( `Added "${kw}" to blocklist and purged ${info.changes} matching local records.`);
    res.json({ success: true, count: info.changes });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/scraper/blocklist/:id', async (req, res) => {
  const { id } = req.params;
  try {
    deleteBlocklist(id);
    res.json({ success: true });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.get('/api/logs/history', (req, res) => {
  if (!fs.existsSync(LOG_FILE)) return res.json({ history: [] });
  const content = fs.readFileSync(LOG_FILE, 'utf8');
  const lines = content.split('\n').filter(Boolean).slice(-150);
  res.json({ history: lines });
});

app.get('/api/stats/coverage', async (req, res) => {
  try {
    const data = db.prepare(`
      SELECT 
        state, 
        COUNT(*) as total, 
        SUM(CASE WHEN verification_status = 'ENRICHED' THEN 1 ELSE 0 END) as enriched,
        SUM(CASE WHEN is_published = 1 THEN 1 ELSE 0 END) as published
      FROM local_spots
      WHERE state IS NOT NULL
      GROUP BY state
      ORDER BY total DESC
    `).all();
    res.json({ stats: data });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// --- Databank Coverage: state x verification_status + is_published counts ---
app.get('/api/stats/databank-coverage', async (req, res) => {
  try {
    const rows = db.prepare(`
      SELECT 
        state,
        COUNT(*) as total,
        SUM(CASE WHEN is_published = 1 THEN 1 ELSE 0 END) as published,
        SUM(CASE WHEN verification_status = 'PENDING' THEN 1 ELSE 0 END) as PENDING,
        SUM(CASE WHEN verification_status = 'ENRICHED' THEN 1 ELSE 0 END) as ENRICHED,
        SUM(CASE WHEN verification_status = 'IDENTITY_ESTABLISHED' THEN 1 ELSE 0 END) as IDENTITY_ESTABLISHED,
        SUM(CASE WHEN verification_status = 'INDEXED' THEN 1 ELSE 0 END) as INDEXED,
        SUM(CASE WHEN verification_status = 'MEDIA_READY' THEN 1 ELSE 0 END) as MEDIA_READY
      FROM local_spots
      WHERE state IS NOT NULL
      GROUP BY state
      ORDER BY state ASC
    `).all();

    res.json({ rows });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// --- Image Proxy: pipes external photo URLs server-side to bypass referrer/CORS restrictions ---
// Used by the dashboard so Street View Static and other CDN images render from localhost.
app.get('/api/img-proxy', async (req, res) => {
  const url = req.query.url as string;
  if (!url || !url.startsWith('http')) {
    res.status(400).send('Missing or invalid url param');
    return;
  }
  try {
    const nodeFetch = (await import('node-fetch')).default as any;
    const upstream = await nodeFetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (compatible; SK8LytzBot/1.0)' },
      signal: AbortSignal.timeout(8000)
    });
    if (!upstream.ok) { res.status(upstream.status).send('Upstream error'); return; }
    const ct = upstream.headers.get('content-type') || 'image/jpeg';
    res.set('Content-Type', ct);
    res.set('Cache-Control', 'public, max-age=86400');
    (upstream.body as any).pipe(res);
  } catch (err: any) {
    res.status(502).send('Proxy fetch error: ' + err.message);
  }
});

// ─── Sniper Bench: Isolated SSE Streaming Pipeline ──────────────────────────
// This endpoint runs a full Spider + Detective execution in memory against a
// single spot_id WITHOUT modifying any database records. Results stream to the
// caller via Server-Sent Events so the UI can show real-time progress.
app.get('/api/sniper/stream', async (req, res) => {
  const { spot_id } = req.query;
  if (!spot_id) {
    res.status(400).json({ error: 'spot_id query param is required' });
    return;
  }

  // ── Set SSE headers ────────────────────────────────────────────────────────
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.flushHeaders?.();

  const send = (type: 'log' | 'result' | 'error' | 'done', payload: any) => {
    res.write(`data: ${JSON.stringify({ type, payload })}\n\n`);
  };

  try {
    // ── Fetch spot record (read-only) ──────────────────────────────────────
    send('log', '[Sniper] 🎯 Fetching spot record from local database...');
    const spot = db.prepare('SELECT * FROM local_spots WHERE id = ?').get(String(spot_id));
    const fetchError = !spot ? new Error('Spot not found') : null;

    if (fetchError || !spot) {
      send('error', `Failed to fetch spot: ${fetchError?.message || 'Not found'}`);
      res.end();
      return;
    }

    send('log', `[Sniper] ✓ Loaded: "${spot.name}" (${spot.city}, ${spot.state})`);
    send('log', `[Sniper] 🌐 Website: ${spot.website || '(none)'}`);
    send('log', `[Sniper] 📋 Current status: ${spot.verification_status || 'UNKNOWN'}`);
    send('log', '[Sniper] ─────────────────────────────────────────');

    // ── Fetch global AI config ─────────────────────────────────────────────
    const configRes = await fetch('http://localhost:5999/config').then(r => r.json()).catch(() => ({ config: {} }));
    const aiConfig = configRes.config || {};

    // ── Phase 2: Spider ────────────────────────────────────────────────────
    send('log', '[Sniper] === PHASE 2: SPIDER ===');
    const spiderResult = await executeSpider(
      spot.website || '',
      true, // always headless in Sniper
      (msg) => send('log', msg)
    );
    send('log', '[Sniper] ─────────────────────────────────────────');

    // Use existing candidate_links if spider was blocked (social-only)
    const candidateLinks = spiderResult.isSocialOnly
      ? (spot.candidate_links || {})
      : (spiderResult.candidateLinks || spot.candidate_links || {});

    // ── Phase 3: Detective ─────────────────────────────────────────────────
    send('log', '[Sniper] === PHASE 3: DETECTIVE ===');
    const detectiveResult = await executeDetective(
      spot,
      candidateLinks,
      aiConfig,
      true, // always headless in Sniper
      (msg) => send('log', msg)
    );
    send('log', '[Sniper] ─────────────────────────────────────────');

    // ── Stream final result payload ────────────────────────────────────────
    send('log', '[Sniper] ✅ Isolated pipeline complete — NO database changes made.');
    send('result', {
      spot_id: String(spot_id),
      spot_name: spot.name,
      spiderResult: {
        candidateLinks: spiderResult.candidateLinks,
        isSocialOnly: spiderResult.isSocialOnly,
        summary: spiderResult.summary,
      },
      detectiveResult: {
        qualityScore: detectiveResult.qualityScore,
        passedQualityGate: detectiveResult.passedQualityGate,
        simulatedStatus: detectiveResult.mappedFields._simulated_status,
        aiMetadata: detectiveResult.aiMetadata,
        mappedFields: detectiveResult.mappedFields,
        socialLinks: detectiveResult.socialLinks,
        candidatePhotos: detectiveResult.candidatePhotos,
        flyerUrls: detectiveResult.flyerUrls,
      }
    });
    send('done', true);
  } catch (err: any) {
    send('error', `Sniper pipeline crashed: ${err.message}`);
    send('done', true);
  } finally {
    res.end();
  }
});

app.listen(5999, () => {
  console.log('[CCTower] API listening on port 5999');
});
