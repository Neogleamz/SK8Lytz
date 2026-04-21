import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import fs from 'fs';
import path from 'path';
import { EventEmitter } from 'events';
import { createClient } from '@supabase/supabase-js';
import { exec } from 'child_process';
import { US_STATES, processState, startNationalHarvester, stopNationalHarvester, isHarvestingActive } from './USANationalHarvest';
import { startGoogleSweep, stopGoogleSweep, isGoogleSweepActive } from './GoogleSweep';


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
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

const app = express();
app.use(cors());
app.use(express.json());

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
  'Phase 1': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 2': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 3': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 4': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 5': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 6': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
};

// Async sleep helper
const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

async function fetchConfig() {
  const { data } = await supabase.from('scraper_config').select('*').eq('id', 1).single();
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
    let operatorStatus = 'Offline';
    let indexerStatus = 'Offline';
    let photographerStatus = 'Offline';

    if (!err && stdout) {
      try {
        const pm2List = JSON.parse(stdout);
        const operator = pm2List.find((p: any) => p.name === 'scraper-operator');
        const indexer = pm2List.find((p: any) => p.name === 'scraper-indexer');
        const photographer = pm2List.find((p: any) => p.name === 'scraper-photographer');
        if (operator?.pm2_env?.status === 'online' || indexer?.pm2_env?.status === 'online') {
            running = true;
        }
        operatorStatus = operator?.pm2_env?.status || 'Offline';
        indexerStatus = indexer?.pm2_env?.status || 'Offline';
        photographerStatus = photographer?.pm2_env?.status || 'Offline';
      } catch(e) {}
    }

    const { count: totalProcessed } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .not('last_attempted_at', 'is', null);

    const { count: totalEnriched } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .in('verification_status', ['ENRICHED', 'VERIFIED']);

    const { count: totalVerified } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .eq('verification_status', 'VERIFIED');
      
    const { count: pendingCount } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .or('verification_status.eq.PENDING,verification_status.is.null');

    const { count: identityCount } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .eq('verification_status', 'IDENTITY_ESTABLISHED');

    const { count: indexedCount } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .eq('is_deep_crawled', true);

    const { count: enrichedCount } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .eq('verification_status', 'ENRICHED');
      
    const { count: mediaReadyCount } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .eq('verification_status', 'MEDIA_READY');

    // Photographer input pool: candidate_photos collected, photos not yet downloaded
    const { count: candidatesCount } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .not('candidate_photos', 'is', null)
      .is('photos', null);

    res.json({
      isRunning: running,
      isHarvestingActive,
      isHeadless,
      currentTarget: `Operator: ${operatorStatus} | Indexer: ${indexerStatus} | Photographer: ${photographerStatus}`,
      isGoogleSweepActive,
      processedCount: totalProcessed || 0,
      enrichedCount: enrichedCount || 0,
      mediaReadyCount: mediaReadyCount || 0,
      candidatesReadyCount: candidatesCount || 0,
      verifiedCount: totalVerified || 0,
      
      // New Micro-Scraper Metrics
      pendingCount: pendingCount || 0,
      identityCount: identityCount || 0,
      indexedCount: indexedCount || 0,
      
      errorCount,
      consecutiveErrors,
      isGated,
      lastError,
      pulseRegistry
    });
  });
});

app.post('/api/pulse', (req, res) => {
  const { source, delayMs, ghost } = req.body;
  if (pulseRegistry[source]) {
    pulseRegistry[source] = {
      lastRunAt: new Date().toISOString(),
      nextRunAt: new Date(Date.now() + delayMs).toISOString(),
      delayMs,
      ghost
    };
  }
  res.json({ success: true });
});

app.post('/start', (req, res) => {
  const { daemons } = req.body as { daemons?: string[] };
  const target = (daemons && daemons.length > 0)
    ? daemons.map(d => `scraper-${d}`).join(',')
    : 'scraper-operator,scraper-indexer,scraper-photographer';
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
  const { daemons } = req.body as { daemons?: string[] };
  const target = (daemons && daemons.length > 0)
    ? daemons.map(d => `scraper-${d}`).join(' ')
    : 'scraper-operator scraper-indexer scraper-photographer';
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
  console.log(`Commanding daemon start: scraper-${name}`);
  exec(`pm2 start scraper-${name}`, { cwd: __dirname, windowsHide: true }, (err, stdout, stderr) => {
     if (err) {
        console.error(`Failed to start ${name}:`, err);
        return res.status(500).json({ success: false, message: `Failed to start ${name}` });
     }
     res.json({ success: true, message: `${name} daemon started` });
  });
});

app.post('/api/daemons/:name/stop', (req, res) => {
  const { name } = req.params;
  console.log(`Commanding daemon stop: scraper-${name}`);
  exec(`pm2 stop scraper-${name}`, { cwd: __dirname, windowsHide: true }, (err, stdout, stderr) => {
     if (err) {
        console.error(`Failed to stop ${name}:`, err);
        return res.status(500).json({ success: false, message: `Failed to stop ${name}` });
     }
     res.json({ success: true, message: `${name} daemon stopped` });
  });
});

app.get('/config', async (req, res) => {
  const { data, error } = await supabase.from('scraper_config').select('*').eq('id', 1).single();
  if (error) return res.status(500).json({ error: error.message });
  res.json({ config: data });
});

app.post('/config', async (req, res) => {
  const { 
    state_override, 
    target_facilities, 
    sleep_interval_ms,
    cooldown_base_ms,
    cooldown_jitter_pct,
    max_consecutive_errors,
    auto_resume_enabled,
    identity_rotation_enabled,
    randomize_viewport_enabled 
  } = req.body;

  const { error } = await supabase.from('scraper_config').update({
    state_override,
    target_facilities,
    sleep_interval_ms,
    cooldown_base_ms,
    cooldown_jitter_pct,
    max_consecutive_errors,
    auto_resume_enabled,
    identity_rotation_enabled,
    randomize_viewport_enabled
  }).eq('id', 1);

  if (error) {
    return res.status(500).json({ error: error.message });
  }
  res.json({ success: true, message: 'Config updated' });
});

// --- Priority States API (Global Active Region) ---
// state_override = [] means nationwide (no priority filter)

app.get('/api/priority-states', async (req, res) => {
  const { data, error } = await supabase.from('scraper_config').select('state_override').eq('id', 1).single();
  if (error) return res.status(500).json({ error: error.message });
  res.json({ priority_states: data?.state_override || [] });
});

app.post('/api/priority-states', async (req, res) => {
  const { states } = req.body;
  if (!Array.isArray(states)) return res.status(400).json({ error: 'states must be an array of 2-letter codes' });
  const valid = states.filter((s: string) => typeof s === 'string' && s.length === 2).map((s: string) => s.toUpperCase());
  const { error } = await supabase.from('scraper_config').update({ state_override: valid }).eq('id', 1);
  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true, priority_states: valid, message: valid.length === 0 ? 'Nationwide (no filter)' : `Active region: ${valid.join(', ')}` });
});

app.delete('/api/priority-states', async (req, res) => {
  const { error } = await supabase.from('scraper_config').update({ state_override: [] }).eq('id', 1);
  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true, priority_states: [], message: 'Region cleared -- nationwide mode active' });
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

  const { data, error } = await supabase.from('skate_spots').select('state');
  const counts: Record<string, number> = {};
  if (!error && data) {
    data.forEach((row: any) => {
      const st = row.state;
      if (st) counts[st] = (counts[st] || 0) + 1;
    });
  }

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
  const { status, limit = 50, offset = 0, sortCol = 'last_attempted_at', sortDir = 'desc', search = '' } = req.query;
  let query = supabase.from('skate_spots').select('*', { count: 'exact' });
    if (status && status !== 'ALL') {
      if (status === 'UNVERIFIED' || status === 'PENDING') {
         query = query.or('verification_status.eq.PENDING,verification_status.is.null');
      } else {
         query = query.eq('verification_status', status);
      }
    }

  if (search) {
     query = query.or(`name.ilike.%${search}%,city.ilike.%${search}%,state.ilike.%${search}%`);
  }
  
  query = query.order(String(sortCol), { ascending: sortDir === 'asc', nullsFirst: false })
               .range(Number(offset), Number(offset) + Number(limit) - 1);
               
  const { data, error, count } = await query;
  if (error) return res.status(500).json({ error: error.message });
  res.json({ spots: data, total: count });
});

app.put('/api/spots/:id', async (req, res) => {
  const { id } = req.params;
  const updates = req.body;
  const { error } = await supabase.from('skate_spots').update(updates).eq('id', id);
  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true });
});

app.post('/api/promote-all', async (req, res) => {
  // Include MEDIA_READY -- the photographer pipeline's final output status
  const { error } = await supabase
    .from('skate_spots')
    .update({ is_published: true })
    .or('verification_status.eq.VERIFIED,verification_status.eq.ENRICHED,verification_status.eq.MEDIA_READY');

  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true, message: 'Bulk promotion successful' });
});

// State-scoped publish: promote all eligible records in a single state
app.post('/api/promote-state/:state', async (req, res) => {
  const { state } = req.params;
  if (!state || state.length !== 2) return res.status(400).json({ error: 'Invalid state abbreviation' });

  const { error, count } = await supabase
    .from('skate_spots')
    .update({ is_published: true })
    .eq('state', state.toUpperCase())
    .or('verification_status.eq.VERIFIED,verification_status.eq.ENRICHED,verification_status.eq.MEDIA_READY')
    .select('id', { count: 'exact', head: true });

  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true, state: state.toUpperCase(), promoted: count ?? 0 });
});

// State-scoped unpublish: retract all records in a single state
app.post('/api/unpublish-state/:state', async (req, res) => {
  const { state } = req.params;
  if (!state || state.length !== 2) return res.status(400).json({ error: 'Invalid state abbreviation' });

  const { error, count } = await supabase
    .from('skate_spots')
    .update({ is_published: false })
    .eq('state', state.toUpperCase())
    .select('id', { count: 'exact', head: true });

  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true, state: state.toUpperCase(), unpublished: count ?? 0 });
});

app.delete('/api/spots/:id', async (req, res) => {
  const { id } = req.params;
  const { error } = await supabase.from('skate_spots').delete().eq('id', id);
  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true });
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

app.get('/api/recent-spots', async (req, res) => {
  const { data, error } = await supabase
    .from('skate_spots')
    .select('*')
    .order('created_at', { ascending: false, nullsFirst: true })
    .limit(10);
    
  if (error) return res.status(500).json({ error: error.message });
  res.json({ spots: data });
});

app.get('/api/queue', async (req, res) => {
  const { phase } = req.query;
  
  let query = supabase.from('skate_spots').select('*');
  if (phase === 'phase1') {
     query = query.or('verification_status.eq.PENDING,verification_status.is.null');
  } else if (phase === 'phase2') {
     query = query.eq('verification_status', 'PENDING');
  } else if (phase === 'phase3') {
     // Mirror get_next_spot_for_indexer RPC: ENRICHED or IDENTITY_ESTABLISHED, not yet crawled, has website
     query = query
       .or('verification_status.eq.IDENTITY_ESTABLISHED,verification_status.eq.ENRICHED')
       .eq('is_deep_crawled', false)
       .not('website', 'is', null)
       .neq('website', '');
  } else if (phase === 'phase4') {
     // Photographer queue: records with candidates that haven't been downloaded yet
     query = query
       .not('candidate_photos', 'is', null)
       .is('photos', null);
  } else if (phase === 'phase5') {
     // Publisher queue: records with photos ready to go live
     query = query.eq('verification_status', 'MEDIA_READY');
  } else {
     query = query.or('verification_status.eq.PENDING,verification_status.eq.IDENTITY_ESTABLISHED,verification_status.eq.INDEXED,verification_status.eq.ENRICHED,verification_status.is.null');
  }

  const { data, error } = await query
    .order('last_attempted_at', { ascending: true, nullsFirst: true })
    .limit(10);
    
  if (error) return res.status(500).json({ error: error.message });
  res.json({ spots: data });
});

app.get('/api/logs/history', (req, res) => {
  if (!fs.existsSync(LOG_FILE)) return res.json({ history: [] });
  const content = fs.readFileSync(LOG_FILE, 'utf8');
  const lines = content.split('\n').filter(Boolean).slice(-150);
  res.json({ history: lines });
});

app.get('/api/stats/coverage', async (req, res) => {
  try {
    const { data, error } = await supabase.rpc('get_state_enrichment_stats');
    if (error) {
      const { data: rawData, error: rawError } = await supabase
        .from('skate_spots')
        .select('state, verification_status')
        .then(result => {
           if (result.error) return result;
           const stats: Record<string, any> = {};
           result.data.forEach((row: any) => {
             if (!stats[row.state]) stats[row.state] = { state: row.state, total: 0, enriched: 0, verified: 0 };
             stats[row.state].total++;
             if (row.verification_status === 'ENRICHED') stats[row.state].enriched++;
             if (row.verification_status === 'VERIFIED') stats[row.state].verified++;
           });
           return { data: Object.values(stats).sort((a: any,b: any) => b.total - a.total), error: null };
        });
      if (rawError) throw rawError;
      return res.json({ stats: rawData });
    }
    res.json({ stats: data });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// --- Databank Coverage: state x verification_status + is_published counts ---
app.get('/api/stats/databank-coverage', async (req, res) => {
  try {
    const { data, error } = await supabase
      .from('skate_spots')
      .select('state, verification_status, is_published');

    if (error) throw error;

    // Group client-side: per state, count by status AND count published
    const grouped: Record<string, Record<string, number> & { published: number }> = {};
    (data || []).forEach((row: any) => {
      const st = row.state || 'UNKNOWN';
      const vs = row.verification_status || 'PENDING';
      if (!grouped[st]) grouped[st] = { published: 0 };
      grouped[st][vs] = (grouped[st][vs] || 0) + 1;
      if (row.is_published) grouped[st].published = (grouped[st].published || 0) + 1;
    });

    const rows = Object.entries(grouped).map(([state, statuses]) => ({
      state,
      ...statuses,
      total: Object.values(statuses)
        .filter((v): v is number => typeof v === 'number')
        .reduce((a, b) => a + b, 0) - (statuses.published || 0), // total = record count, not double-counting published
    }));

    // Fix: total should be count of records, not including the published counter
    const fixedRows = (data || []).reduce((acc: Record<string, any>, row: any) => {
      const st = row.state || 'UNKNOWN';
      if (!acc[st]) acc[st] = { state: st, published: 0, total: 0 };
      acc[st].total++;
      acc[st][row.verification_status || 'PENDING'] = (acc[st][row.verification_status || 'PENDING'] || 0) + 1;
      if (row.is_published) acc[st].published++;
      return acc;
    }, {});

    res.json({ rows: Object.values(fixedRows) });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.listen(5999, () => {
  console.log('[CCTower] API listening on port 5999');
});
