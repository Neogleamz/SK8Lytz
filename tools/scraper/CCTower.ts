import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import fs from 'fs';
import path from 'path';
import { EventEmitter } from 'events';
import { createClient } from '@supabase/supabase-js';
import { scrapeCulturalDetails } from './lib/GoogleScraper';
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

async function runDaemonLoop() {
  while (isRunning) {
    try {
      const config = await fetchConfig();
      const sleepInterval = config.sleep_interval_ms || 10000;
      
      currentTarget = 'Querying Queue...';
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_to_enrich');
      
      if (rpcError) throw new Error('RPC Failed: ' + rpcError.message);
      
      if (!spots || spots.length === 0) {
        currentTarget = 'Idle - Queue Empty';
        consecutiveErrors = 0; // Reset errors on clean idle
        isGated = false;
        await sleep(sleepInterval);
        continue;
      }
      
      const target = spots[0];
      currentTarget = `[${target.facility_type}] ${target.name} (${target.city}, ${target.state})`;
      
      // Update targeted row so another worker doesn't pick it up
      await supabase.from('skate_spots').update({ 
        last_attempted_at: new Date().toISOString() 
      }).eq('id', target.id);
      
      console.log(`\n🎯 Targeting: ${currentTarget}`);
      
      // We pass the exact address/coordinates to the scraper to satisfy "exact address" requirement from the plan
      const searchQuery = target.street_address 
          ? `${target.name} ${target.street_address} ${target.city} ${target.state}`
          : `${target.name} ${target.city} ${target.state}`;
          
      // Graceful stop check before spin up
      if (!isRunning) {
         currentTarget = 'Offline';
         break;
      }

      const culturalData = await scrapeCulturalDetails(
        searchQuery, 
        isHeadless,
        config.identity_rotation_enabled ?? true,
        config.randomize_viewport_enabled ?? true
      );
      
      const newRetryCount = (target.retry_count || 0) + 1;
      const hasGoldStandard = culturalData.fetched_website && culturalData.phone_number;
      
      let verificationStatus = target.verification_status;

      if (hasGoldStandard) {
        verificationStatus = 'VERIFIED';
      } else if (newRetryCount >= 10) {
        verificationStatus = 'REJECTED';
      } else if (
        culturalData.fetched_website || 
        culturalData.phone_number || 
        culturalData.has_adult_night || 
        culturalData.vibe_rating || 
        culturalData.has_pro_shop
      ) {
        verificationStatus = 'ENRICHED'; // Got something, but not everything
      } else {
        verificationStatus = 'PENDING'; // Still nothing
      }
      
      // Write back validation status
      console.log(`   💾 Validation: ${verificationStatus} ${hasGoldStandard ? '(Gold Standard Met)' : `(Attempt ${newRetryCount}/10)`}`);
      
      const wins = [];
      if (culturalData.fetched_website) wins.push('Website');
      if (culturalData.has_pro_shop) wins.push('Pro-Shop');
      if (culturalData.has_adult_night) wins.push('Adult-Night');
      if (culturalData.vibe_rating) wins.push('Vibe-Score');
      if (culturalData.phone_number) wins.push('Phone');

      console.log(`   ✨ Enrichment Summary: [${wins.length ? wins.join(', ') : 'No new markers found'}]`);
      
      const { error: updateError } = await supabase
        .from('skate_spots')
        .update({
          has_pro_shop: target.has_pro_shop || culturalData.has_pro_shop,
          has_adult_night: culturalData.has_adult_night,
          vibe_rating: culturalData.vibe_rating,
          socials: culturalData.socials,
          website: target.website || culturalData.fetched_website,
          phone_number: culturalData.phone_number,
          surface_quality: culturalData.surface_quality,
          vibe_score: culturalData.vibe_score,
          verification_status: verificationStatus,
          retry_count: newRetryCount,
          cultural_metadata: {
             ...(target.cultural_metadata || {}),
             last_deep_scan: new Date().toISOString(),
             surface_raw: culturalData.surface_quality,
             vibe_raw: culturalData.vibe_score
          },
          last_enriched_at: new Date().toISOString(),
          last_attempted_at: new Date().toISOString()
        })
        .eq('id', target.id);
        
      if (updateError) throw updateError;
      
      enrichedCount++;
      consecutiveErrors = 0; // Success reset
      isGated = false;
      lastError = null;
      currentTarget = 'Cooldown Phase';
      await sleep(sleepInterval);


    } catch (err: any) {
      errorCount++;
      consecutiveErrors++;
      lastError = err.message;
      
      if (err.message === 'BOT_GATED') {
        const config = await fetchConfig();
        const cooldownBase = config.cooldown_base_ms || 300000;
        const jitterPct = config.cooldown_jitter_pct || 20;
        
        // Exponential Backoff: base * (2 ^ (errors - 1))
        const backoff = cooldownBase * Math.pow(2, Math.min(consecutiveErrors - 1, 4));
        const jitterAmt = backoff * (jitterPct / 100) * (Math.random() * 2 - 1);
        const totalWait = Math.max(backoff + jitterAmt, 10000);

        currentTarget = `GATED - PAUSING (${Math.round(totalWait/1000)}s)`;
        console.error(`🛑 [SECURITY] Bot Gate detected! Cooling down for ${Math.round(totalWait/1000)}s...`);
        
        if (consecutiveErrors >= (config.max_consecutive_errors || 3)) {
          isGated = true;
          currentTarget = 'CIRCUIT BREAKER - GATED';
          console.error('⛔ [CRITICAL] Strike threshold reached. Halting engine to prevent blacklist.');
          if (!config.auto_resume_enabled) isRunning = false;
        }
        
        await sleep(totalWait);
      } else {
        console.error('❌ Daemon Error:', err.message);
        currentTarget = 'Error - Backing off';
        await sleep(30000); // Backoff on error
      }
    }
  }
  
  currentTarget = 'Offline';
}

// Routes
app.get('/status', async (req, res) => {
  const { count: totalEnriched } = await supabase
    .from('skate_spots')
    .select('*', { count: 'exact', head: true })
    .in('verification_status', ['ENRICHED', 'VERIFIED']);

  const { count: totalVerified } = await supabase
    .from('skate_spots')
    .select('*', { count: 'exact', head: true })
    .eq('verification_status', 'VERIFIED');

  res.json({
    isRunning,
    isHarvestingActive,
    isHeadless,
    currentTarget,
    enrichedCount: totalEnriched || 0,
    verifiedCount: totalVerified || 0,
    errorCount,
    consecutiveErrors,
    isGated,
    lastError
  });
});

app.post('/start', (req, res) => {
  if (!isRunning) {
    isRunning = true;
    runDaemonLoop();
    res.json({ success: true, message: 'Daemon started' });
  } else {
    res.json({ success: false, message: 'Already running' });
  }
});

app.post('/stop', (req, res) => {
  if (isRunning) {
    isRunning = false;
    currentTarget = 'Stopping...';
    res.json({ success: true, message: 'Daemon stopping' });
  } else {
    res.json({ success: false, message: 'Daemon is not running' });
  }
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
