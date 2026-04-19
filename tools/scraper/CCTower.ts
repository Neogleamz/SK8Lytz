import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import fs from 'fs';
import path from 'path';
import { EventEmitter } from 'events';
import { createClient } from '@supabase/supabase-js';
import { exec } from 'child_process';
import { US_STATES, processState, startNationalHarvester, stopNationalHarvester, isHarvestingActive } from './USANationalHarvest';


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

console.log = (...args) => {
  const msg = args.join(' ');
  originalLog(...args);
  writeToLogFile('INFO', msg);
  logEmitter.emit('log', { type: 'INFO', message: msg });
};

console.error = (...args) => {
  const msg = args.join(' ');
  originalError(...args);
  writeToLogFile('ERROR', msg);
  logEmitter.emit('log', { type: 'ERROR', message: msg });
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
  exec('pm2 jlist', async (err, stdout) => {
    let running = false;
    let operatorStatus = 'Offline';
    let indexerStatus = 'Offline';

    if (!err && stdout) {
      try {
        const pm2List = JSON.parse(stdout);
        const operator = pm2List.find((p: any) => p.name === 'scraper-operator');
        const indexer = pm2List.find((p: any) => p.name === 'scraper-indexer');
        if (operator?.pm2_env?.status === 'online' || indexer?.pm2_env?.status === 'online') {
            running = true;
        }
        operatorStatus = operator?.pm2_env?.status || 'Offline';
        indexerStatus = indexer?.pm2_env?.status || 'Offline';
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
      
    // New counts for indexer metrics
    const { count: totalIdentified } = await supabase
      .from('skate_spots')
      .select('*', { count: 'exact', head: true })
      .in('verification_status', ['IDENTITY_ESTABLISHED', 'INDEXED', 'ENRICHED', 'VERIFIED']);

    res.json({
      isRunning: running,
      isHarvestingActive,
      isHeadless,
      currentTarget: `Operator: ${operatorStatus} | Indexer: ${indexerStatus}`,
      processedCount: totalProcessed || 0,
      enrichedCount: totalIdentified || 0, // Using identified to reflect Phase 2/3 progress
      verifiedCount: totalVerified || 0,
      errorCount,
      consecutiveErrors,
      isGated,
      lastError
    });
  });
});

app.post('/start', (req, res) => {
  console.log('Orchestrating micro-scrapers start...');
  exec('pm2 start scraper-operator scraper-indexer', (err, stdout, stderr) => {
     if (err) {
        console.error('Failed to start scrapers cluster:', err);
        return res.status(500).json({ success: false, message: 'Start failed', error: err.message });
     }
     isRunning = true;
     res.json({ success: true, message: 'Scrapers cluster started' });
  });
});

app.post('/stop', (req, res) => {
  console.log('Orchestrating micro-scrapers stop...');
  exec('pm2 stop scraper-operator scraper-indexer', (err, stdout, stderr) => {
     if (err) {
        console.error('Failed to stop scrapers cluster:', err);
        return res.status(500).json({ success: false, message: 'Stop failed', error: err.message });
     }
     isRunning = false;
     res.json({ success: true, message: 'Scrapers cluster stopped' });
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

app.get('/api/spots', async (req, res) => {
  const { status, limit = 50, offset = 0, sortCol = 'last_attempted_at', sortDir = 'desc', search = '' } = req.query;
  let query = supabase.from('skate_spots').select('*', { count: 'exact' });
  
  if (status && status !== 'ALL') {
    if (status === 'UNVERIFIED') {
       query = query.is('verification_status', null);
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
  const { error } = await supabase
    .from('skate_spots')
    .update({ is_published: true })
    .eq('verification_status', 'VERIFIED')
    .not('website', 'is', null);

  if (error) return res.status(500).json({ error: error.message });
  res.json({ success: true, message: 'Bulk promotion successful' });
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

app.post('/api/harvest/start-all', async (req, res) => {
  const { target_facilities, target_states } = req.body;
  if (isHarvestingActive) return res.json({ success: false, message: 'Already running' });
  startNationalHarvester(target_facilities || [], target_states || []).catch(e => console.error(e));
  res.json({ success: true, message: 'Harvest sequence started' });
});

app.post('/api/harvest/stop-all', async (req, res) => {
  if (!isHarvestingActive) return res.json({ success: false, message: 'Not running' });
  await stopNationalHarvester();
  res.json({ success: true, message: 'National Harvest stopping' });
});

app.get('/api/queue', async (req, res) => {
  let query = supabase.from('skate_spots').select('*')
    .or('verification_status.eq.PENDING,verification_status.eq.ENRICHED,verification_status.is.null');

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

app.listen(5999, () => {
  console.log('📡 CCTower API listening on port 5999');
});
