import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import fs from 'fs';
import path from 'path';
import { EventEmitter } from 'events';
import { exec, spawn, ChildProcess } from 'child_process';
import { startGoogleSweep, stopGoogleSweep, isGoogleSweepActive } from './GoogleSweep';
import { GooglePlacesProvider, RETAIL_BLOCKLIST } from './lib/providers/GooglePlacesProvider';
import { executeDetective } from './core/DetectiveEngine';
import { HeuristicsEngine } from './core/HeuristicsEngine';
import { db, getLocalSpots, getLocalCount, updateLocalSpot, deleteLocalSpot, upsertLocalSpot, getConfig, updateConfig, getBlocklist, addBlocklist, deleteBlocklist, addBlocklistKeyword, getPipelineStats, getFieldRegistry, upsertFieldRegistryItem, logFieldCorrection, getCorrectionStats } from './core/LocalDB';

// Identify this as the CCTower process for LocalDB initialization
process.env.IS_CCTOWER = 'true';

// Guard against PM2 environment variable serialization bugs
process.env.SCRAPER_REGISTER_ONLY = 'false';

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
  if (msg.includes('[Operator]') || msg.includes('Google Captcha') || msg.includes('Overpass') || msg.includes('[HEURISTIC]') || msg.includes('[WebsiteResolver]') || msg.includes('[WEBSITE_RESOLVER]')) return 'Phase 2';
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

// ─── Local Bucket Storage (Supabase Migrated Photos) ─────────────────────────
const BUCKET_DIR = path.resolve(__dirname, '../../.scraper-data/bucket');
if (!fs.existsSync(BUCKET_DIR)) {
  fs.mkdirSync(BUCKET_DIR, { recursive: true });
}
app.use('/local-bucket', express.static(BUCKET_DIR));
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
  'Phase 1': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 2': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 3': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 4': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 5': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
  'Phase 6': { lastRunAt: null, nextRunAt: null, delayMs: 0 },
};

// Active job tracker — daemons report what they are currently processing
const activeJobRegistry: Record<string, { active_job: string | null; target: string | null; updated_at: string }> = {
  'Phase 2': { active_job: null, target: null, updated_at: '' },
  'Phase 3': { active_job: null, target: null, updated_at: '' },
  'Phase 4': { active_job: null, target: null, updated_at: '' },
  'Phase 6': { active_job: null, target: null, updated_at: '' },
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
  let running = activeDaemons.size > 0;
  let indexerStatus = activeDaemons.has('scraper-indexer') ? 'online' : 'Offline';
  let resolverStatus = activeDaemons.has('scraper-website-resolver') ? 'online' : 'Offline';
  let photographerStatus = activeDaemons.has('scraper-photographer') ? 'online' : 'Offline';
  let publisherStatus = activeDaemons.has('scraper-publisher') ? 'online' : 'Offline';

  const totalCount = (db.prepare('SELECT COUNT(*) as cnt FROM local_spots').get() as { cnt: number }).cnt;
  const totalProcessed = (db.prepare('SELECT COUNT(*) as cnt FROM local_spots WHERE last_attempted_at IS NOT NULL').get() as { cnt: number }).cnt;
  const publishedCount = (db.prepare('SELECT COUNT(*) as cnt FROM local_spots WHERE is_published = 1').get() as { cnt: number }).cnt;
  const pendingCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'PENDING' OR verification_status IS NULL`).get() as { cnt: number }).cnt;
  const seededCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'SEEDED'`).get() as { cnt: number }).cnt;
  const enrichedCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'ENRICHED'`).get() as { cnt: number }).cnt;
  const deepCrawledCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'DEEP_CRAWLED'`).get() as { cnt: number }).cnt;
  const mediaReadyCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'MEDIA_READY'`).get() as { cnt: number }).cnt;
  const candidatesCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE candidate_photos IS NOT NULL AND photos IS NULL`).get() as { cnt: number }).cnt;
  
  const pendingWebsiteCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'PENDING_WEBSITE'`).get() as { cnt: number }).cnt;
  const stalledWebsiteCount = (db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'WEBSITE_STALLED'`).get() as { cnt: number }).cnt;

  res.json({
    isRunning: running,
    isHarvestingActive,
    isHeadless,
    currentTarget: `Website Resolver: ${resolverStatus} | Indexer: ${indexerStatus} | Photographer: ${photographerStatus} | Publisher: ${publisherStatus}`,
    isGoogleSweepActive,
    totalCount: totalCount || 0,
    processedCount: totalProcessed || 0,
    enrichedCount: enrichedCount || 0,
    mediaReadyCount: mediaReadyCount || 0,
    candidatesReadyCount: candidatesCount || 0,
    publishedCount: publishedCount || 0,
    pendingCount: pendingCount || 0,
    seededCount: seededCount || 0,
    deepCrawledCount: deepCrawledCount || 0,
    pendingWebsiteCount: pendingWebsiteCount || 0,
    stalledWebsiteCount: stalledWebsiteCount || 0,
    errorCount,
    consecutiveErrors,
    isGated,
    lastError,
    pulseRegistry,
    activeJobRegistry,
    activeDaemons: Array.from(activeDaemons.keys()),
    lmsStatus: cachedLmsStatus,
    gpuTelemetry: cachedGpuTelemetry
  });
});

app.post('/api/pulse', (req, res) => {
  let { source, delayMs, ghost, active_job, target } = req.body;
  if (source === 'Phase 2') source = 'Phase 2';
  if (source === 'Photographer' || source === 'photographer') source = 'Phase 4';
  if (source === 'Publisher' || source === 'publisher') source = 'Phase 6';
  if (source === 'Indexer' || source === 'indexer') source = 'Phase 3';
  if (source === 'Scout' || source === 'scout') source = 'Phase 1';

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

const activeDaemons = new Map<string, ChildProcess>();

app.post('/start', (req, res) => {
  const scriptMap: Record<string, string> = {
    'website-resolver': 'WebsiteResolverDaemon.ts',
    'indexer': 'Indexer.ts',
    'photographer': 'Photographer.ts',
    'publisher': 'Publisher.ts'
  };

  const started: string[] = [];
  const errors: string[] = [];

  for (const [name, scriptName] of Object.entries(scriptMap)) {
    const target = `scraper-${name}`;
    if (activeDaemons.has(target)) {
      continue;
    }

    try {
      const outLog = fs.openSync(path.join(LOG_DIR, `${name}-out.log`), 'a');
      const errLog = fs.openSync(path.join(LOG_DIR, `${name}-error.log`), 'a');

      const child = spawn('bun', [scriptName], {
        cwd: __dirname,
        stdio: ['ignore', outLog, errLog],
        detached: false,
        env: { ...process.env, SCRAPER_REGISTER_ONLY: 'false' }
      });

      child.on('exit', () => {
        activeDaemons.delete(target);
        console.log(`Daemon ${target} exited`);
      });

      activeDaemons.set(target, child);
      started.push(name);
    } catch (err: any) {
      console.error(`Failed to spawn ${name} in bulk start:`, err);
      errors.push(name);
    }
  }

  isRunning = activeDaemons.size > 0;

  if (errors.length > 0) {
    res.status(500).json({
      success: false,
      message: `Bulk start complete with errors. Started: ${started.join(', ') || 'none'}. Failed to start: ${errors.join(', ')}`
    });
  } else {
    res.json({
      success: true,
      message: started.length > 0 
        ? `Bulk start completed. Launched: ${started.join(', ')}`
        : `All daemons are already running.`
    });
  }
});

app.post('/stop', (req, res) => {
  // Bulk stop
  activeDaemons.forEach((child, target) => {
    child.kill('SIGTERM');
    activeDaemons.delete(target);
  });
  isRunning = false;
  res.json({ success: true, message: `All local daemons stopped` });
});

app.post('/api/daemons/:name/start', (req, res) => {
  const { name } = req.params;
  const target = `scraper-${name}`;
  
  if (activeDaemons.has(target)) {
    return res.status(400).json({ success: false, message: `${name} is already running` });
  }

  console.log(`Commanding daemon start: ${target}`);
  
  const scriptMap: Record<string, string> = {
    'website-resolver': 'WebsiteResolverDaemon.ts',
    'indexer': 'Indexer.ts',
    'photographer': 'Photographer.ts',
    'publisher': 'Publisher.ts'
  };
  
  const scriptName = scriptMap[name];
  if (!scriptName) return res.status(400).json({ success: false, message: 'Invalid daemon' });

  try {
    const outLog = fs.openSync(path.join(LOG_DIR, `${name}-out.log`), 'a');
    const errLog = fs.openSync(path.join(LOG_DIR, `${name}-error.log`), 'a');

    const child = spawn('bun', [scriptName], {
      cwd: __dirname,
      stdio: ['ignore', outLog, errLog],
      detached: false,
      env: { ...process.env, SCRAPER_REGISTER_ONLY: 'false' }
    });

    child.on('exit', () => {
      activeDaemons.delete(target);
      console.log(`Daemon ${target} exited`);
    });

    activeDaemons.set(target, child);
    isRunning = true;
    res.json({ success: true, message: `${name} daemon started natively` });
  } catch (err: any) {
    console.error(`Failed to spawn ${name}:`, err);
    res.status(500).json({ success: false, message: `Failed to spawn ${name}` });
  }
});

app.post('/api/daemons/:name/stop', (req, res) => {
  const { name } = req.params;
  const target = `scraper-${name}`;
  console.log(`Commanding daemon stop: ${target}`);
  
  const child = activeDaemons.get(target);
  if (child) {
    child.kill('SIGTERM');
    activeDaemons.delete(target);
    res.json({ success: true, message: `${name} daemon stopped` });
  } else {
    res.json({ success: true, message: `${name} stopped or not running` });
  }
});

// ─── LM Studio Integration Status State & Helper ────────────────────────────

interface LmsStatus {
  serverStatus: 'ON' | 'OFF' | 'MISSING';
  port: number;
  loadedModels: string[];
  availableModels: { key: string; arch: string; size: string; loaded: boolean }[];
  lastUpdated: string;
}

let cachedLmsStatus: LmsStatus = {
  serverStatus: 'OFF',
  port: 1234,
  loadedModels: [],
  availableModels: [],
  lastUpdated: new Date().toISOString()
};

// ─── Host Telemetry (LibreHardwareMonitor Bridge) ────────────────────────────

interface HostTelemetry {
  vramUsedMB: number;
  vramTotalMB: number;
  vramPercent: number;
  gpuUtilPercent: number;
  cpuUtilPercent: number;
  ramUsedGB: number;
  ramTotalGB: number;
  ramPercent: number;
  dockerMemory?: {
    usedMB: number;
    limitMB: number;
    percent: number;
  };
  lastUpdated: string;
}

let cachedGpuTelemetry: HostTelemetry = {
  vramUsedMB: 0, vramTotalMB: 8192, vramPercent: 0,
  gpuUtilPercent: 0,
  cpuUtilPercent: 0,
  ramUsedGB: 0, ramTotalGB: 16, ramPercent: 0,
  dockerMemory: { usedMB: 0, limitMB: 4096, percent: 0 },
  lastUpdated: new Date().toISOString()
};

interface LhmNode {
  Text: string;
  Value?: string;
  ImageURL?: string;
  Children?: LhmNode[];
}

// Find a hardware component node in the sensor tree (e.g. CPU, GPU, Generic Memory)
function findLhmHardwareNode(node: LhmNode, imageKeyword: string): LhmNode | null {
  if (node.ImageURL?.toLowerCase().includes(imageKeyword.toLowerCase())) {
    return node;
  }
  if (node.Children) {
    for (const child of node.Children) {
      const found = findLhmHardwareNode(child, imageKeyword);
      if (found) return found;
    }
  }
  return null;
}

// Recursively find the value of a specific named sensor under a hardware component
function findLhmSensorValue(node: LhmNode, sensorName: string): string | null {
  if (node.Text.toLowerCase().trim() === sensorName.toLowerCase().trim() && node.Value !== undefined) {
    return node.Value;
  }
  if (node.Children) {
    for (const child of node.Children) {
      const found = findLhmSensorValue(child, sensorName);
      if (found) return found;
    }
  }
  return null;
}

function getDockerMemoryStats(): { usedMB: number; limitMB: number; percent: number } {
  let usedBytes = 0;
  let limitBytes = 0;

  try {
    // Check cgroup v2 first
    if (fs.existsSync('/sys/fs/cgroup/memory.current')) {
      usedBytes = parseInt(fs.readFileSync('/sys/fs/cgroup/memory.current', 'utf8').trim()) || 0;
      const maxStr = fs.readFileSync('/sys/fs/cgroup/memory.max', 'utf8').trim();
      if (maxStr && maxStr !== 'max') {
        limitBytes = parseInt(maxStr) || 0;
      }
    } else if (fs.existsSync('/sys/fs/cgroup/memory/memory.usage_in_bytes')) {
      // Fallback to cgroup v1
      usedBytes = parseInt(fs.readFileSync('/sys/fs/cgroup/memory/memory.usage_in_bytes', 'utf8').trim()) || 0;
      const limitStr = fs.readFileSync('/sys/fs/cgroup/memory/memory.limit_in_bytes', 'utf8').trim();
      if (limitStr) {
        limitBytes = parseInt(limitStr) || 0;
      }
    }
  } catch (e) {
    // Fail silently
  }

  const usedMB = Math.round(usedBytes / (1024 * 1024)) || 0;
  let limitMB = Math.round(limitBytes / (1024 * 1024)) || 0;
  if (limitMB <= 0 || limitMB > 262144) {
    limitMB = 4096; // 4GB fallback
  }

  return {
    usedMB,
    limitMB,
    percent: Math.min(100, Math.round((usedMB / limitMB) * 100)) || 0
  };
}

let lmsConnectionWarningLogged = false;

async function updateHostTelemetry(): Promise<void> {
  try {
    const fetchFn = require('node-fetch');
    const res = await fetchFn('http://host.docker.internal:8085/data.json', { signal: AbortSignal.timeout(3000) });
    if (!res.ok) throw new Error(`LHM HTTP status ${res.status}`);
    
    const tree = (await res.json()) as LhmNode;
    if (!tree) return;

    lmsConnectionWarningLogged = false; // Reset warning state upon successful query

    // 1. Parse CPU load
    let cpuUtilPercent = 0;
    const cpuNode = findLhmHardwareNode(tree, 'cpu.png');
    if (cpuNode) {
      const valStr = findLhmSensorValue(cpuNode, 'CPU Total');
      if (valStr) cpuUtilPercent = parseFloat(valStr) || 0;
    }

    // 2. Parse Generic Memory (System RAM)
    let ramPercent = 0;
    let ramUsedGB = 0;
    let ramTotalGB = 16;
    const ramNode = findLhmHardwareNode(tree, 'ram.png');
    if (ramNode) {
      const pctStr = findLhmSensorValue(ramNode, 'Memory');
      const usedStr = findLhmSensorValue(ramNode, 'Memory Used');
      const availStr = findLhmSensorValue(ramNode, 'Memory Available');
      
      if (pctStr) ramPercent = parseFloat(pctStr) || 0;
      if (usedStr) ramUsedGB = parseFloat(usedStr) || 0;
      if (usedStr && availStr) {
        const availGB = parseFloat(availStr) || 0;
        ramTotalGB = Math.round((ramUsedGB + availGB) * 10) / 10;
      }
    }

    // 3. Parse GPU load & VRAM telemetry (supports nvidia, amd, intel)
    let gpuUtilPercent = 0;
    let vramUsedMB = 0;
    let vramTotalMB = 8192;
    
    const gpuNode = findLhmHardwareNode(tree, 'nvidia.png') || 
                    findLhmHardwareNode(tree, 'amd.png') || 
                    findLhmHardwareNode(tree, 'ati.png') || 
                    findLhmHardwareNode(tree, 'intel.png');
                    
    if (gpuNode) {
      const utilStr = findLhmSensorValue(gpuNode, 'GPU Core');
      const vramUsedStr = findLhmSensorValue(gpuNode, 'GPU Memory Used') || findLhmSensorValue(gpuNode, 'GPU Memory');
      const vramFreeStr = findLhmSensorValue(gpuNode, 'GPU Memory Free');
      const vramTotalStr = findLhmSensorValue(gpuNode, 'GPU Memory Total');

      if (utilStr) gpuUtilPercent = parseFloat(utilStr) || 0;
      if (vramUsedStr) vramUsedMB = parseFloat(vramUsedStr) || 0;
      if (vramTotalStr) {
        vramTotalMB = parseFloat(vramTotalStr) || 8192;
      } else if (vramUsedStr && vramFreeStr) {
        const freeMB = parseFloat(vramFreeStr) || 0;
        vramTotalMB = Math.round(vramUsedMB + freeMB);
      }
    }

    cachedGpuTelemetry = {
      vramUsedMB: Math.round(vramUsedMB),
      vramTotalMB: Math.round(vramTotalMB),
      vramPercent: Math.round((vramUsedMB / vramTotalMB) * 100) || 0,
      gpuUtilPercent: Math.round(gpuUtilPercent * 10) / 10,
      cpuUtilPercent: Math.round(cpuUtilPercent * 10) / 10,
      ramUsedGB: Math.round(ramUsedGB * 10) / 10,
      ramTotalGB,
      ramPercent: Math.round(ramPercent),
      dockerMemory: getDockerMemoryStats(),
      lastUpdated: new Date().toISOString()
    };
  } catch (err: any) {
    if (!lmsConnectionWarningLogged) {
      console.warn(`[CCTower] ⚠️ Host telemetry unavailable. Start LibreHardwareMonitor on Windows and enable Options -> Remote Web Server on port 8085.`);
      lmsConnectionWarningLogged = true; // Log only once to avoid spamming the terminal logs
    }
  }
}

// Poll host hardware telemetry every 6 seconds
setInterval(updateHostTelemetry, 6000);
updateHostTelemetry();

async function updateLmsStatus(): Promise<void> {
  const hosts = ['host.docker.internal', 'localhost', '127.0.0.1'];
  const port = 1234;
  let probedHost: string | null = null;
  let fetchedModels: string[] = [];

  for (const host of hosts) {
    try {
      const fetchFn = require('node-fetch');
      const res = await fetchFn(`http://${host}:${port}/v1/models`, { signal: AbortSignal.timeout(2000) });
      if (res.ok) {
        const data = (await res.json()) as { data?: { id: string }[] };
        if (data && Array.isArray(data.data)) {
          fetchedModels = data.data.map((m: { id: string }) => m.id);
          probedHost = host;
          break;
        }
      }
    } catch (e) {}
  }

  if (probedHost) {
    process.env.LM_STUDIO_HOST = probedHost;
    
    // In Docker, we rely on aiConfig to know which model is targeted
    let currentDetective = 'local-model';
    try {
      const cfg = getConfig();
      if (cfg && cfg.detective_model) currentDetective = cfg.detective_model;
    } catch (e) {}

    const availableModels = fetchedModels.map(m => ({ 
      key: m, 
      arch: 'unknown', 
      size: 'unknown', 
      loaded: m === currentDetective 
    }));
    
    // Provide a clean UI by marking only the active targeted model as "loaded"
    const loadedModels = fetchedModels.includes(currentDetective) ? [currentDetective] : [];

    cachedLmsStatus = {
      serverStatus: 'ON',
      port,
      loadedModels,
      availableModels,
      lastUpdated: new Date().toISOString()
    };
    return;
  }

  cachedLmsStatus = {
    serverStatus: 'OFF',
    port: 1234,
    loadedModels: [],
    availableModels: [],
    lastUpdated: new Date().toISOString()
  };
}

// ─── LM Studio Integration Endpoints ────────────────────────────────────────

app.get('/api/llm/status', (req, res) => {
  res.json(cachedLmsStatus);
});

// ─── Heuristics Registry Endpoints ───────────────────────────────────────────
app.get('/api/heuristics', (req, res) => {
  try {
    const ledger = HeuristicsEngine.load();
    res.json({ success: true, ledger });
  } catch (error: any) {
    res.status(500).json({ success: false, error: error.message });
  }
});

app.post('/api/heuristics', (req, res) => {
  try {
    HeuristicsEngine.save(req.body);
    res.json({ success: true });
  } catch (error: any) {
    res.status(500).json({ success: false, error: error.message });
  }
});

// ─── Website Resolver Endpoints ──────────────────────────────────────────────
app.post('/api/website-resolver/trigger', (req, res) => {
  const target = 'scraper-website-resolver';
  if (activeDaemons.has(target)) {
    return res.json({ success: true, message: 'Website Resolver is already active and running.' });
  }
  try {
    const outLog = fs.openSync(path.join(LOG_DIR, `website-resolver-out.log`), 'a');
    const errLog = fs.openSync(path.join(LOG_DIR, `website-resolver-error.log`), 'a');
    const child = spawn('bun', ['WebsiteResolverDaemon.ts'], {
      cwd: __dirname,
      stdio: ['ignore', outLog, errLog],
      detached: false,
      env: { ...process.env, SCRAPER_REGISTER_ONLY: 'false' }
    });
    child.on('exit', () => {
      activeDaemons.delete(target);
      console.log(`Daemon ${target} exited`);
    });
    activeDaemons.set(target, child);
    res.json({ success: true, message: 'Website Resolver daemon launched successfully!' });
  } catch (err: any) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post('/api/website-resolver/reset-missing', (req, res) => {
  try {
    const query = `
      UPDATE local_spots 
      SET 
        verification_status = 'PENDING_WEBSITE',
        retry_count = 0,
        last_attempted_at = NULL
      WHERE 
        (website IS NULL OR website = '') 
        AND (verification_status IN ('SEEDED', 'PENDING', 'STALLED', 'WEBSITE_STALLED') OR verification_status IS NULL)
    `;
    const result = db.prepare(query).run();
    res.json({ success: true, count: result.changes });
  } catch (err: any) {
    res.status(500).json({ success: false, error: err.message });
  }
});

app.post('/api/llm/server/start', (req, res) => {
  res.json({ success: false, message: 'Cannot start LM Studio from inside Docker. Please launch LM Studio manually on your host machine.' });
});

app.post('/api/llm/server/stop', (req, res) => {
  res.json({ success: false, message: 'Cannot stop LM Studio from inside Docker.' });
});

app.post('/api/llm/model/load', async (req, res) => {
  const { modelKey, setAsDetective } = req.body;
  const targetModel = modelKey || 'llama-3.2-3b-instruct';
  console.log(`[CCTower] Setting target LLM model: ${targetModel}`);
  
  // Auto-update detective_model in global config
  if (setAsDetective !== false) {
    try {
      updateConfig({ detective_model: targetModel });
      console.log(`[CCTower] Auto-set detective_model → ${targetModel}`);
    } catch (e: any) {
      console.error(`[CCTower] Failed to auto-set detective_model:`, e.message);
    }
  }

  // Fire a JIT warmup request to LM Studio
  try {
    const fetchFn = require('node-fetch');
    const host = process.env.LM_STUDIO_HOST || 'host.docker.internal';
    const port = cachedLmsStatus.port || 1234;
    fetchFn(`http://${host}:${port}/v1/chat/completions`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ model: targetModel, messages: [{ role: 'user', content: 'wake up' }], max_tokens: 1 })
    }).catch(() => {});
  } catch (e) {}

  await updateLmsStatus();
  res.json({ success: true, message: `Set target model to ${targetModel} (JIT loading applied)`, cachedLmsStatus, detective_model: targetModel });
});

app.post('/api/llm/model/unload', async (req, res) => {
  res.json({ success: true, message: 'Model unloading is managed via LM Studio JIT.' });
});

// ─── Model VRAM Estimate (pre-load check) ────────────────────────────────────
app.post('/api/llm/model/estimate', (req, res) => {
  res.json({
    success: false,
    modelKey: req.body.modelKey,
    estimatedGpuMB: null,
    estimatedTotalMB: null,
    verdict: 'Docker cannot estimate',
    currentVramUsedMB: cachedGpuTelemetry.vramUsedMB,
    currentVramTotalMB: cachedGpuTelemetry.vramTotalMB,
    headroomMB: null
  });
});

// ─── GPU Telemetry Endpoint ──────────────────────────────────────────────────
app.get('/api/gpu/stats', (req, res) => {
  res.json(cachedGpuTelemetry);
});

// ─── Scraper Watchdog Service (Self-Healing Loop) ───────────────────────────
async function runWatchdogCheck(): Promise<void> {
  try {
    const config = getConfig() || {};
    
    // Check if the global Watchdog Auto-Heal setting is enabled (defaults to true)
    const watchdogEnabled = config.watchdog_enabled !== false;
    if (!watchdogEnabled) return;

    const timeoutSec = config.watchdog_heartbeat_timeout_s || 60;
    const watchdogPhases = config.watchdog_phases || {
      'website-resolver': true,
      'indexer': true,
      'photographer': true,
      'publisher': true
    };

    const daemonToPulseMap: Record<string, string> = {
      'website-resolver': 'Phase 2',
      'indexer': 'Phase 3',
      'photographer': 'Phase 4',
      'publisher': 'Phase 6'
    };

    const scriptMap: Record<string, string> = {
      'website-resolver': 'WebsiteResolverDaemon.ts',
      'indexer': 'Indexer.ts',
      'photographer': 'Photographer.ts',
      'publisher': 'Publisher.ts'
    };

    const now = Date.now();

    for (const [daemonName, shouldProtect] of Object.entries(watchdogPhases)) {
      if (!shouldProtect) {
        // If user disabled it, ensure it's stopped if it is running
        const target = `scraper-${daemonName}`;
        if (activeDaemons.has(target)) {
          console.log(`[Watchdog] 🛡️ Policy disabled for ${daemonName}. Shutting down daemon...`);
          const child = activeDaemons.get(target);
          if (child) {
            child.kill('SIGTERM');
            activeDaemons.delete(target);
          }
        }
        continue;
      }

      // Check heartbeat
      const pulseKey = daemonToPulseMap[daemonName];
      if (!pulseKey) continue;

      const lastPulse = pulseRegistry[pulseKey];
      const target = `scraper-${daemonName}`;

      let needsRestart = false;

      if (!lastPulse || !lastPulse.lastRunAt) {
        // Protected phase is enabled but has never run/pulsed. Auto-start it JIT!
        needsRestart = true;
      } else {
        const lastPulseTime = new Date(lastPulse.lastRunAt).getTime();
        const secondsSinceLastPulse = (now - lastPulseTime) / 1000;
        if (secondsSinceLastPulse > timeoutSec) {
          needsRestart = true;
          console.warn(`[Watchdog] ⚠️ Daemon ${daemonName} missed heartbeat! Silence duration: ${Math.round(secondsSinceLastPulse)}s (limit: ${timeoutSec}s)`);
        }
      }

      if (needsRestart) {
        console.log(`[Watchdog] 🛡️ Auto-healing / Auto-starting daemon: ${target}`);
        
        // Kill existing if any (zombie cleanup)
        if (activeDaemons.has(target)) {
          try {
            const child = activeDaemons.get(target);
            if (child) child.kill('SIGKILL');
          } catch {}
          activeDaemons.delete(target);
        }

        // Spawn a fresh child process
        const scriptName = scriptMap[daemonName];
        if (scriptName) {
          try {
            const outLog = fs.openSync(path.join(LOG_DIR, `${daemonName}-out.log`), 'a');
            const errLog = fs.openSync(path.join(LOG_DIR, `${daemonName}-error.log`), 'a');

            // Stagger spawn slightly to prevent DB write race conditions on boot
            await sleep(2000);

            const child = spawn('bun', [scriptName], {
              cwd: __dirname,
              stdio: ['ignore', outLog, errLog],
              detached: false,
              env: { ...process.env, SCRAPER_REGISTER_ONLY: 'false' }
            });

            child.on('exit', () => {
              activeDaemons.delete(target);
              console.log(`[Watchdog] Daemon ${target} exited`);
            });

            activeDaemons.set(target, child);
            isRunning = true;
            
            // Log to CCTower fleet logger so it prints in the timeline terminal!
            console.log(`[Watchdog] 🛡️ Auto-healed and successfully restarted failed ${daemonName} loop!`);
          } catch (err: any) {
            console.error(`[Watchdog] Failed to auto-heal ${daemonName}:`, err.message);
          }
        }
      }
    }
  } catch (e: any) {
    console.error(`[Watchdog] Error in loop:`, e.message);
  }
}

// Spin up the Watchdog check cycle every 15 seconds
setInterval(runWatchdogCheck, 15000);
runWatchdogCheck();


// Initial background polling for LM Studio
setInterval(updateLmsStatus, 30000);
updateLmsStatus();

app.get('/api/pipeline/telemetry', async (req, res) => {
  try {
    // Build live telemetry from DB queues + in-memory active job registry
    // in_q = next 3 spots waiting in each phase queue (DB truth)
    const p1 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'PENDING' OR verification_status IS NULL ORDER BY created_at ASC LIMIT 3`).all();
    const pW = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'PENDING_WEBSITE' ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 3`).all();
    const p3 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'SEEDED' ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 3`).all();
    const p4 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'DEEP_CRAWLED' AND photos IS NULL ORDER BY last_attempted_at ASC NULLS FIRST LIMIT 3`).all();
    const p6 = db.prepare(`SELECT name FROM local_spots WHERE verification_status = 'MEDIA_READY' AND is_published = 0 ORDER BY created_at DESC LIMIT 3`).all();

    const names = (rows: any[]) => rows.map((s: any) => s.name);
    const isAlive = (phase: string) => {
      const p = pulseRegistry[phase];
      if (!p?.lastRunAt) return false;
      return (Date.now() - new Date(p.lastRunAt).getTime()) < 600000; // alive if pulsed in last 10 min
    };
    const aj = (phase: string) => activeJobRegistry[phase]?.active_job || null;
    const tgt = (phase: string) => activeJobRegistry[phase]?.target || null;
    const getRecord = (name: string | null) => name ? db.prepare(`SELECT * FROM local_spots WHERE name = ? LIMIT 1`).get(name) : null;

    res.json({
      scout:      { active_job: aj('Phase 1'), target: tgt('Phase 1'), active_record: getRecord(tgt('Phase 1')), in_q: names(p1), pulse: pulseRegistry['Phase 1'], alive: isAlive('Phase 1') },
      resolver:   { active_job: aj('Phase 2'), target: tgt('Phase 2'), active_record: getRecord(tgt('Phase 2')), in_q: names(pW), pulse: pulseRegistry['Phase 2'], alive: isAlive('Phase 2') },
      detective:  { active_job: aj('Phase 3'), target: tgt('Phase 3'), active_record: getRecord(aj('Phase 3')), in_q: names(p3), pulse: pulseRegistry['Phase 3'], alive: isAlive('Phase 3') },
      photographer: { active_job: aj('Phase 4'), target: tgt('Phase 4'), active_record: getRecord(tgt('Phase 4')), in_q: names(p4), pulse: pulseRegistry['Phase 4'], alive: isAlive('Phase 4') },
      publisher:  { active_job: aj('Phase 6'), target: tgt('Phase 6'), active_record: getRecord(tgt('Phase 6')), in_q: names(p6), pulse: pulseRegistry['Phase 6'], alive: isAlive('Phase 6') },
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

    // JIT hot-load the model if the detective_model was updated
    if (payload.detective_model) {
      console.log(`[CCTower] Detective model changed to: ${payload.detective_model}. Hot-loading via JIT...`);
      try {
        const fetchFn = require('node-fetch');
        const host = process.env.LM_STUDIO_HOST || 'host.docker.internal';
        const port = cachedLmsStatus.port || 1234;
        fetchFn(`http://${host}:${port}/v1/chat/completions`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ model: payload.detective_model, messages: [{ role: 'user', content: 'wake up' }], max_tokens: 1 })
        }).catch(() => {});
      } catch (e) {}
      await updateLmsStatus();
    }

    res.json({ success: true, message: 'Config updated' });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/api/config/scope', async (req, res) => {
  const { scrape_scope } = req.body;
  if (!scrape_scope) return res.status(400).json({ error: 'scrape_scope is required' });
  try {
    updateConfig({ scrape_scope });
    res.json({ success: true, scrape_scope, message: `Scrape scope updated to ${scrape_scope}` });
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
      verification_status:  (url || details.website) ? 'SEEDED' : 'PENDING_WEBSITE',
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

// ─── NEW SNIPER & SEARCH ENDPOINTS ──────────────────────────────────────────

app.get('/api/spots/search', (req, res) => {
  const { q } = req.query;
  if (!q) return res.json({ spots: [] });
  try {
    const spots = db.prepare(`
      SELECT id, name, city, state, verification_status 
      FROM local_spots 
      WHERE name LIKE ? OR city LIKE ? OR id = ?
      LIMIT 10
    `).all(`%${q}%`, `%${q}%`, q);
    res.json({ spots });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/sniper', async (req, res) => {
  const { target_id } = req.body;
  if (!target_id) return res.status(400).json({ error: 'target_id is required' });
  try {
    const config = getConfig();
    updateConfig({ ...config, sniper_target_id: target_id });
    res.json({ success: true, sniper_target_id: target_id });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/sniper', async (req, res) => {
  try {
    const config = getConfig();
    updateConfig({ ...config, sniper_target_id: null });
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/spots/:id/reset', async (req, res) => {
  const { id } = req.params;
  try {
    const spot = db.prepare('SELECT website, candidate_links FROM local_spots WHERE id = ?').get(id) as any;
    const hasWebsite = (spot?.website && spot.website.trim() !== '') || (() => {
      try {
        const cl = JSON.parse(spot?.candidate_links || '{}');
        return !!(cl.website && cl.website.trim() !== '');
      } catch {
        return false;
      }
    })();
    const nextStatus = hasWebsite ? 'SEEDED' : 'PENDING_WEBSITE';

    // Purge AI fields and reset status
    db.prepare(`
      UPDATE local_spots 
      SET 
        verification_status = ?, 
        last_attempted_at = NULL,
        is_deep_crawled = 0,
        ai_metadata = NULL,
        opening_hours = NULL,
        pricing_data = NULL,
        surface_type = NULL,
        surface_quality = NULL,
        candidate_photos = NULL,
        candidate_links = NULL
      WHERE id = ?
    `).run(nextStatus, id);
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

// ────────────────────────────────────────────────────────────────────────────

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
      try {
        const nodeFetch = require('node-fetch');
        const fetchRes = await nodeFetch(url, { signal: AbortSignal.timeout(10000) });

        if (fetchRes.ok) {
          const buf = Buffer.from(await fetchRes.arrayBuffer());
          // Validate dimensions before handing to Leptonica
          const isValidImg = buf.length >= 2048 && (() => {
            if (buf[0] === 0xFF && buf[1] === 0xD8 && buf[2] === 0xFF) {
              for (let i = 2; i < buf.length - 9; i++) {
                if (buf[i] === 0xFF && (buf[i+1] === 0xC0 || buf[i+1] === 0xC2)) {
                  return ((buf[i+7] << 8)|buf[i+8]) >= 16 && ((buf[i+5] << 8)|buf[i+6]) >= 16;
                }
              }
              return false;
            }
            if (buf[0] === 0x89 && buf[1] === 0x50 && buf.length >= 24) {
              return ((buf[16]<<24)|(buf[17]<<16)|(buf[18]<<8)|buf[19]) >= 16;
            }
            return false;
          })();
          if (isValidImg) {
            const Tesseract = require('tesseract.js');
            const { data: { text } } = await Tesseract.recognize(buf, 'eng');
            cleanText = text.replace(/\s+/g, ' ').trim();
          } else {
            console.log(`[Image-Trap] Skipping OCR: image too small or invalid format`);
          }
        }
      } catch (ocrErr: any) {
        console.error('[Image-Trap] OCR failed:', ocrErr.message);
      }
    } else {
      // Clean DOM and extract text for standard webpages
      const puppeteer = require('puppeteer');
      const browser = await puppeteer.launch({ headless: true });
      const page = await browser.newPage();
      await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 });

      // --- Smart City Spider Logic ---
      if (spot_city) {
        try {
          const links = (await page.evaluate(() => {
            return Array.from(document.querySelectorAll('a')).map(a => ({
              text: (a.innerText || '').toLowerCase(),
              href: (a.href || '').toLowerCase()
            }));
          })) as { text: string; href: string }[];
          
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
        const screenshotBuffer = Buffer.from(await page.screenshot({ fullPage: true, encoding: 'base64' }) as string, 'base64');
        if (screenshotBuffer && screenshotBuffer.length >= 2048) {
          const Tesseract = require('tesseract.js');
          const { data: { text } } = await Tesseract.recognize(screenshotBuffer, 'eng');
          ocrText = text.replace(/\s+/g, ' ').trim();
          console.log(`[Brute Force OCR] Extracted ${ocrText.length} characters.`);
        } else {
          console.log('[Brute Force OCR] Screenshot buffer too small, skipping OCR.');
        }
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

    // Construct the Schema (handle dashboard {key, prompt} and legacy {key, type} formats)
    const schema = ai_target_vectors.reduce((acc: any, vec: any) => {
      if (typeof vec === 'string') return acc;
      acc[vec.key] = vec.prompt || vec.type || `${vec.key} (extract or null)`;
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
  
  processState(state, target_facilities || []).catch(console.error);
  res.json({ success: true, message: `Started harvest for US-${state}` });
});

app.post('/api/harvest/force', async (req, res) => {
  const { state } = req.body;
  if (!state) return res.status(400).json({ error: 'State code required' });
  
  const { processState } = require('./USANationalHarvest');
  processState(state, [], true).catch(console.error);
  res.json({ success: true, message: `Forced re-harvest started for ${state}` });
});

app.post('/api/discover', async (req, res) => {
  const { stateFull } = req.body;
  if (!stateFull) return res.status(400).json({ error: 'State name required' });
  
  const { runDirectDiscovery } = require('./Discoverer');
  runDirectDiscovery(stateFull).catch(console.error);
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
    // Log field corrections for training data (before applying updates)
    const existing = db.prepare('SELECT * FROM local_spots WHERE id = ?').get(id) as any;
    if (existing) {
      const trackableFields = [
        'name', 'phone_number', 'website', 'street_address', 'opening_hours',
        'pricing_data', 'has_adult_night', 'adult_night_schedule', 'adult_night_details',
        'surface_type', 'surface_quality', 'has_rental', 'has_pro_shop', 'has_food',
        'has_lights', 'has_lockers', 'has_ac', 'has_wifi', 'has_toilets',
        'is_wheelchair_accessible', 'hosts_derby', 'capacity', 'operator_name',
        'operator_description', 'instagram_url', 'facebook_url', 'tiktok_url',
        'schedule_url', 'yelp_url', 'email_addresses', 'is_indoor', 'vibe_score'
      ];
      for (const field of trackableFields) {
        if (field in updates && updates[field] !== existing[field]) {
          logFieldCorrection(id, field, existing[field], updates[field]);
        }
      }
    }
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

// ── Field Correction Stats (AI Quality Monitoring) ────────────────────────────
app.get('/api/correction-stats', async (req, res) => {
  try {
    const stats = getCorrectionStats();
    const totalCorrections = db.prepare('SELECT COUNT(*) as cnt FROM field_corrections').get() as any;
    res.json({
      total_corrections: totalCorrections?.cnt || 0,
      by_field_and_source: stats
    });
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
  const payload = req.body;
  if (!payload) return res.status(400).json({ error: 'Payload required' });

  const logs = Array.isArray(payload) ? payload : [payload];
  
  for (const log of logs) {
    const { type, source, message } = log;
    if (!message) continue;
    
    // Format for internal tracking and logging
    const msgContext = source ? `[${source}] ${message}` : message;
    writeToLogFile(type || 'INFO', msgContext);
    
    // Emit to SSE clients immediately
    logEmitter.emit('log', { type: type || 'INFO', source: source || 'UNKNOWN', message });
  }
  
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

app.put('/api/field-registry/:id', async (req, res) => {
  try {
    const { importance_level, priority_group, is_hard_gate, visual_glow } = req.body;
    
    const existing = getFieldRegistry().find(f => f.id === req.params.id);
    if (!existing) return res.status(404).json({ error: 'Field not found' });
    
    const updated = {
      ...existing,
      importance_level: importance_level !== undefined ? importance_level : existing.importance_level,
      priority_group: priority_group !== undefined ? priority_group : existing.priority_group,
      is_hard_gate: is_hard_gate !== undefined ? is_hard_gate : existing.is_hard_gate,
      visual_glow: visual_glow !== undefined ? visual_glow : existing.visual_glow
    };
    
    upsertFieldRegistryItem(updated);
    res.json({ success: true, field: updated });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.post('/api/field-registry/reset', async (req, res) => {
  try {
    db.exec(`
      -- Seed any missing Phase 2 fields that may not exist in legacy DBs
      INSERT OR IGNORE INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, importance_level, priority_group, is_hard_gate, visual_glow)
      VALUES ('price_range', 'price_range', 2, 'Price Range', 'text', 445, 0, 10, 0, 0);
      INSERT OR IGNORE INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, importance_level, priority_group, is_hard_gate, visual_glow)
      VALUES ('yelp_url', 'yelp_url', 2, 'Yelp URL', 'text', 750, 0, 10, 0, 0);
      INSERT OR IGNORE INTO pipeline_field_registry (id, field_name, phase_id, display_label, data_type, sort_order, importance_level, priority_group, is_hard_gate, visual_glow)
      VALUES ('logo_url', 'logo_url', 2, 'Logo URL', 'text', 755, 0, 10, 0, 0);

      UPDATE pipeline_field_registry SET priority_group = 10, is_hard_gate = 0, visual_glow = 0;
      -- Tier 0: Phase 1 Seeded Data (NOT detective-extracted)
      UPDATE pipeline_field_registry SET priority_group = 0, is_hard_gate = 1, visual_glow = 0 WHERE field_name = 'name';
      UPDATE pipeline_field_registry SET priority_group = 0, is_hard_gate = 1, visual_glow = 0 WHERE field_name IN ('street_address', 'lat', 'lng', 'city', 'state', 'zip');
      UPDATE pipeline_field_registry SET priority_group = 0, is_hard_gate = 0, visual_glow = 0 WHERE field_name = 'phone_number';
      -- Tier 1: 🕐 Session Hours (Pass 1A)
      UPDATE pipeline_field_registry SET priority_group = 1, is_hard_gate = 1, visual_glow = 1 WHERE field_name = 'opening_hours';
      -- Tier 2: 💰 Pricing & Fees (Pass 1B)
      UPDATE pipeline_field_registry SET priority_group = 2, is_hard_gate = 1, visual_glow = 1 WHERE field_name IN ('pricing_data', 'has_fee', 'has_rental', 'price_range');
      -- Tier 3: 🌙 Adult Night (Pass 1C)
      UPDATE pipeline_field_registry SET priority_group = 3, is_hard_gate = 0, visual_glow = 1 WHERE field_name IN ('has_adult_night', 'adult_night_schedule', 'adult_night_details');
      -- Tier 4: 🛹 Floor & Vibe (Pass 2A)
      UPDATE pipeline_field_registry SET priority_group = 4, is_hard_gate = 0, visual_glow = 1 WHERE field_name IN ('surface_type', 'surface_quality', 'vibe_score');
      -- Tier 5: 🏢 Amenities (Pass 2B)
      UPDATE pipeline_field_registry SET priority_group = 5, is_hard_gate = 0, visual_glow = 0 WHERE field_name IN (
        'is_indoor', 'has_pro_shop', 'has_food', 'has_lights', 'has_lockers', 'has_ac', 'has_wifi', 'has_toilets', 'capacity'
      );
      -- Tier 6: 🎭 Identity & Culture (Pass 2C)
      UPDATE pipeline_field_registry SET priority_group = 6, is_hard_gate = 0, visual_glow = 0 WHERE field_name IN (
        'is_wheelchair_accessible', 'hosts_derby', 'special_events', 'operator_name', 'operator_description', 'cultural_metadata'
      );
      -- Tier 7: 📱 Contacts & Socials (Pass 2D)
      UPDATE pipeline_field_registry SET priority_group = 7, is_hard_gate = 0, visual_glow = 0 WHERE field_name IN (
        'email_addresses', 'instagram_url', 'facebook_url', 'tiktok_url', 'schedule_url', 'yelp_url', 'logo_url'
      );
    `);
    res.json({ success: true, message: 'Field registry reset to system defaults successfully.' });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});


// ── Bulk Reset Records ────────────────────────────────────────────────────────
// Resets all matching records back to a target status phase (SEEDED, DEEP_CRAWLED, or MEDIA_READY).
// Respects the global state[] and facility_types[] filters.
// Body: { states: string[], facility_types: string[], target_statuses: string[], reset_to: string }
app.post('/api/bulk-reset-to-seeded', (req, res) => {
  try {
    const { 
      states = [], 
      facility_types = [], 
      target_statuses = ['DEEP_CRAWLED', 'MEDIA_READY', 'STALLED', 'REJECTED'],
      reset_to = 'SEEDED'
    } = req.body;

    if (!Array.isArray(target_statuses) || target_statuses.length === 0) {
      return res.status(400).json({ error: 'target_statuses must be a non-empty array' });
    }

    let whereClauses = [`verification_status IN (${target_statuses.map(() => '?').join(',')})`];
    const params: any[] = [...target_statuses];

    if (states.length > 0) {
      whereClauses.push(`state IN (${states.map(() => '?').join(',')})`);
      params.push(...states.map((s: string) => s.toUpperCase()));
    }

    if (facility_types.length > 0) {
      whereClauses.push(`facility_type IN (${facility_types.map(() => '?').join(',')})`);
      params.push(...facility_types);
    }

    const where = whereClauses.join(' AND ');
    // First count so we can report
    const countRes = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE ${where}`).get(...params) as any;
    // Execute reset
    const result = db.prepare(`
      UPDATE local_spots 
      SET 
        verification_status = CASE 
          WHEN ? = 'DEEP_CRAWLED' THEN 'DEEP_CRAWLED'
          WHEN ? = 'MEDIA_READY' THEN 'MEDIA_READY'
          WHEN ? = 'PENDING_WEBSITE' THEN 'PENDING_WEBSITE'
          ELSE CASE 
            WHEN (website IS NOT NULL AND website != '') 
              OR (candidate_links IS NOT NULL AND json_extract(candidate_links, '$.website') IS NOT NULL AND json_extract(candidate_links, '$.website') != '')
            THEN 'SEEDED' 
            ELSE 'PENDING_WEBSITE' 
          END
        END, 
        retry_count = 0, 
        last_attempted_at = NULL 
      WHERE ${where}
    `).run(reset_to, reset_to, reset_to, ...params);

    res.json({
      success: true,
      reset_count: result.changes,
      total_matched: countRes?.cnt ?? 0,
      filters: { states, facility_types, target_statuses, reset_to }
    });
  } catch (error: any) {
    res.status(500).json({ error: error.message });
  }
});

app.get('/api/queue', async (req, res) => {
  const { phase } = req.query;
  const statesRaw = (req.query.states as string) || '';
  const states = statesRaw ? statesRaw.split(',').map(s => s.trim().toUpperCase()).filter(Boolean) : [];

  let query = 'SELECT * FROM local_spots WHERE 1=1';
  if (phase === 'pending_website') query += ` AND verification_status = 'PENDING_WEBSITE'`;
  else if (phase === 'stalled_website') query += ` AND verification_status = 'WEBSITE_STALLED'`;
  else if (phase === 'phase1') query += ` AND verification_status = 'SEEDED'`;
  else if (phase === 'phase2') query += ` AND verification_status = 'SEEDED'`; // Detective input
  else if (phase === 'phase3') query += ` AND verification_status = 'DEEP_CRAWLED' AND photos IS NULL`; // Photographer input
  else if (phase === 'phase4') query += ` AND verification_status = 'MEDIA_READY' AND is_published = 0`; // Publisher input
  // phase6 removed (dead) — was old Publisher queue key
  // spider-recent removed (dead) — Spider phase eliminated
  else if (phase === 'detective-recent') query += ` AND is_deep_crawled = 1`;
  else if (phase === 'published') query += ` AND is_published = 1`;
  else query += ` AND (verification_status IN ('SEEDED','DEEP_CRAWLED','MEDIA_READY','PENDING_WEBSITE','WEBSITE_STALLED') OR verification_status IS NULL)`;

  if (states.length > 0) {
    query += ` AND state IN (${states.map(s => `'${s}'`).join(',')})`;
  }

  const sortAsc = phase !== 'detective-recent';
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
    const body = { ...req.body };
    // Co-update verification_status when publishing/unpublishing so the
    // status badge on DatabankCard stays in sync with the LIVE toggle.
    if ('is_published' in body) {
      const publishing = body.is_published === true || body.is_published === 1;
      if (publishing && !body.verification_status) {
        body.verification_status = 'PUBLISHED';
      } else if (!publishing && !body.verification_status) {
        // Revert to MEDIA_READY on unpublish (spot has photos, just pulled back)
        body.verification_status = 'MEDIA_READY';
      }
    }
    updateLocalSpot(id, body);
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});


  // Photo Management Endpoints
app.post('/api/skate_spots/:id/photos/upload', async (req, res) => {
  const { id } = req.params;
  const { image } = req.body;
  try {
    if (!image || !image.startsWith('data:image/')) return res.status(400).json({ error: 'Invalid image payload' });
    const spot = getLocalSpots().find((s: any) => String(s.id) === String(id));
    if (!spot) return res.status(404).json({ error: 'Not found' });
    
    const matches = image.match(/^data:image\/([A-Za-z-+\/]+);base64,(.+)$/);
    if (!matches || matches.length !== 3) return res.status(400).json({ error: 'Invalid base64 string' });
    
    const ext = matches[1] === 'jpeg' ? 'jpg' : matches[1];
    const buffer = Buffer.from(matches[2], 'base64');
    
    const filename = `manual_${id}_${Date.now()}.${ext}`;
    fs.writeFileSync(path.join(PHOTOS_DIR, filename), buffer);
    
    const photoUrl = `http://localhost:5999/api/photos/${filename}`;
    
    let photos = spot.photos;
    if (typeof photos === 'string') try { photos = JSON.parse(photos); } catch { photos = []; }
    if (!Array.isArray(photos)) photos = [];
    
    photos.unshift(photoUrl); // Make it the hero immediately
    updateLocalSpot(id, { photos: JSON.stringify(photos) });
    res.json({ success: true, url: photoUrl });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/skate_spots/:id/photos/hero', async (req, res) => {
  const { id } = req.params;
  const { photoIndex } = req.body;
  try {
    const spot = getLocalSpots().find((s: any) => String(s.id) === String(id));
    if (!spot) return res.status(404).json({ error: 'Not found' });
    let photos = spot.photos;
    if (typeof photos === 'string') try { photos = JSON.parse(photos); } catch { photos = []; }
    if (!Array.isArray(photos) || photos.length <= photoIndex) return res.status(400).json({ error: 'Invalid index' });
    
    const hero = photos.splice(photoIndex, 1)[0];
    photos.unshift(hero);
    updateLocalSpot(id, { photos: JSON.stringify(photos) });
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/skate_spots/:id/photos/:index', async (req, res) => {
  const { id, index } = req.params;
  const idx = parseInt(index, 10);
  try {
    const spot = getLocalSpots().find((s: any) => String(s.id) === String(id));
    if (!spot) return res.status(404).json({ error: 'Not found' });
    let photos = spot.photos;
    if (typeof photos === 'string') try { photos = JSON.parse(photos); } catch { photos = []; }
    if (!Array.isArray(photos) || photos.length <= idx) return res.status(400).json({ error: 'Invalid index' });
    
    photos.splice(idx, 1);
    updateLocalSpot(id, { photos: JSON.stringify(photos) });
    res.json({ success: true });
  } catch (err: any) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/skate_spots/:id/photos/tag', async (req, res) => {
  const { id } = req.params;
  const { photoIndex, fieldType } = req.body;
  try {
    const spot = getLocalSpots().find((s: any) => String(s.id) === String(id));
    if (!spot) return res.status(404).json({ error: 'Not found' });
    let photos = spot.photos;
    if (typeof photos === 'string') try { photos = JSON.parse(photos); } catch { photos = []; }
    if (!Array.isArray(photos) || photos.length <= photoIndex) return res.status(400).json({ error: 'Invalid index' });
    
    const photoUrl = typeof photos[photoIndex] === 'string' ? photos[photoIndex] : photos[photoIndex]?.url;
    if (!photoUrl) return res.status(400).json({ error: 'Invalid photo url' });

    updateLocalSpot(id, { [fieldType]: photoUrl });
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
    // ── Additive-Only: NEVER wipe enrichment data on restart ──
    // Only reset pipeline status so the record re-enters the Detective queue.
    // Photos, hours, pricing, and all manually curated data are SACRED.
    const spot = db.prepare('SELECT website, candidate_links FROM local_spots WHERE id = ?').get(id) as any;
    const hasWebsite = (spot?.website && spot.website.trim() !== '') || (() => {
      try {
        const cl = JSON.parse(spot?.candidate_links || '{}');
        return !!(cl.website && cl.website.trim() !== '');
      } catch {
        return false;
      }
    })();
    const nextStatus = hasWebsite ? 'SEEDED' : 'PENDING_WEBSITE';

    updateLocalSpot(id, {
      verification_status: nextStatus,
      last_attempted_at: null,
      retry_count: 0,
      is_deep_crawled: false,
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
    // 1. Write to legacy blocklist table (for backwards compatibility)
    addBlocklist(kw, match_type, reason);
    
    // 2. Sync to ai_exclusion_keywords (SINGLE SOURCE OF TRUTH — used by Phase 1 + Phase 2)
    const currentCfg = getConfig();
    const existingKw: string[] = currentCfg.ai_exclusion_keywords || [];
    if (!existingKw.map((k: string) => k.toLowerCase()).includes(kw)) {
      updateConfig({ ai_exclusion_keywords: [...existingKw, kw] });
    }
    
    // 3. Execute SQL Guillotine — purge matching spots from DB immediately
    const info = db.prepare(`UPDATE local_spots SET verification_status = 'REJECTED', is_published = 0 WHERE name LIKE ? COLLATE NOCASE`).run(`%${kw}%`);
      
    console.log(`[Guillotine] ☠️  Added "${kw}" to unified blocklist — purged ${info.changes} matching spots.`);
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

// ── Rejection Stats (Toxicity Bouncer Audit Log) ────────────────────────────
app.get('/api/rejected-stats', (req, res) => {
  try {
    const totalRow = db.prepare(`SELECT COUNT(*) as cnt FROM local_spots WHERE verification_status = 'REJECTED'`).get() as any;
    const recent = db.prepare(`
      SELECT name, city, state, ai_metadata, last_attempted_at
      FROM local_spots WHERE verification_status = 'REJECTED' 
      ORDER BY last_attempted_at DESC LIMIT 15
    `).all() as any[];
    const recentParsed = recent.map((r: any) => {
      let reason = 'manual_guillotine';
      try {
        const meta = typeof r.ai_metadata === 'string' ? JSON.parse(r.ai_metadata) : (r.ai_metadata || {});
        reason = meta.rejection_reason || meta.reason || (meta.TOXICITY_ABORT ? 'toxicity_abort' : 'manual_guillotine');
      } catch {}
      return { name: r.name, city: r.city, state: r.state, reason, rejected_at: r.last_attempted_at };
    });
    const breakdown: Record<string, number> = {};
    for (const r of recentParsed) { breakdown[r.reason] = (breakdown[r.reason] || 0) + 1; }
    res.json({ total: totalRow.cnt, recent: recentParsed, breakdown });
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
        SUM(CASE WHEN verification_status = 'MEDIA_READY' THEN 1 ELSE 0 END) as MEDIA_READY, SUM(CASE WHEN verification_status = 'DEEP_CRAWLED' THEN 1 ELSE 0 END) as DEEP_CRAWLED, SUM(CASE WHEN verification_status = 'SEEDED' THEN 1 ELSE 0 END) as SEEDED,
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
        SUM(CASE WHEN verification_status = 'SEEDED' THEN 1 ELSE 0 END) as SEEDED,
        SUM(CASE WHEN verification_status = 'DEEP_CRAWLED' THEN 1 ELSE 0 END) as DEEP_CRAWLED,
        SUM(CASE WHEN verification_status = 'MEDIA_READY' THEN 1 ELSE 0 END) as MEDIA_READY, SUM(CASE WHEN verification_status = 'DEPRECATED' THEN 1 ELSE 0 END) as DEPRECATED, SUM(CASE WHEN verification_status = 'REJECTED' THEN 1 ELSE 0 END) as REJECTED
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
app.get(['/api/img-proxy', '/api/proxy-image'], async (req, res) => {
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
    const spot = db.prepare('SELECT * FROM local_spots WHERE id = ?').get(String(spot_id)) as any;
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

    // ── Phase 3: Detective ─────────────────────────────────────────────────
    send('log', '[Sniper] === PHASE 3: DETECTIVE ===');
    const detectiveResult = await executeDetective(
      spot,
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
        candidateLinks: null,
        isSocialOnly: false,
        summary: 'Phase 2 Deprecated',
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

// ─── Sniper Bench: Apply Simulation to DB ──────────────────────────────────
// This endpoint takes the payload generated by the Sniper Bench simulation
// and destructively overwrites the local_spots database record, advancing its state.
app.post('/api/sniper/apply', async (req, res) => {
  const { spot_id, detectiveResult } = req.body;
  if (!spot_id || !detectiveResult) {
    return res.status(400).json({ error: 'spot_id and detectiveResult are required' });
  }

  try {
    const spot = db.prepare('SELECT id FROM local_spots WHERE id = ?').get(String(spot_id));
    if (!spot) {
      return res.status(404).json({ error: 'Spot not found' });
    }

    const { mappedFields, socialLinks, candidatePhotos, flyerUrls, aiMetadata, simulatedStatus } = detectiveResult;

    // ── Additive-Only: filter out null/empty values to prevent data destruction ──
    const safeFields: Record<string, any> = {};
    if (mappedFields) {
      for (const [key, val] of Object.entries(mappedFields)) {
        if (key === '_simulated_status') continue; // skip internal marker
        if (val != null && val !== '' && val !== '[]' && val !== '{}' && val !== 'null') {
          safeFields[key] = val;
        }
      }
    }

    const updatePayload: Record<string, any> = {
      ...safeFields,
      ...(socialLinks ? { social_links: JSON.stringify(socialLinks) } : {}),
      ...(candidatePhotos ? { candidate_photos: JSON.stringify(candidatePhotos) } : {}),
      ...(flyerUrls ? { flyer_urls: JSON.stringify(flyerUrls) } : {}),
      ...(aiMetadata ? { ai_metadata: JSON.stringify(aiMetadata) } : {}),
      verification_status: simulatedStatus || 'MEDIA_READY',
      last_attempted_at: new Date().toISOString(),
    };

    updateLocalSpot(String(spot_id), updatePayload);

    console.log(`[Sniper API] ✅ Destructive overwrite applied to spot_id: ${spot_id} -> Status: ${updatePayload.verification_status}`);
    res.json({ success: true, spot_id, status: updatePayload.verification_status });
  } catch (err: any) {
    console.error(`[Sniper API] Failed to apply changes: ${err.message}`);
    res.status(500).json({ error: err.message });
  }
});

// ─── STARTUP BACKGROUND SERVICES (Replacements for PM2) ───────────
try {
  console.log('[CCTower] Booting Discord Bridge...');
  const discordOut = fs.openSync(path.join(LOG_DIR, `discord-out.log`), 'a');
  const discordChild = spawn('bun', ['run', 'index.js'], {
    cwd: path.resolve(__dirname, '../discord-bridge'),
    stdio: ['ignore', discordOut, discordOut],
    detached: false
  });
  
  console.log('[CCTower] Booting Vite Dashboard...');
  const dashOut = fs.openSync(path.join(LOG_DIR, `dashboard-out.log`), 'a');
  const dashChild = spawn('bun', ['run', 'dev', '--host', '0.0.0.0', '--port', '5998'], {
    cwd: path.resolve(__dirname, '../scraper-dashboard'),
    stdio: ['ignore', dashOut, dashOut],
    detached: false
  });

  process.on('SIGTERM', () => {
    discordChild.kill('SIGTERM');
    dashChild.kill('SIGTERM');
    process.exit(0);
  });
} catch (e: any) {
  console.error('[CCTower] Failed to boot auxiliary services:', e.message);
}

const PORT = process.env.CCTOWER_PORT || 5999;
app.listen(PORT, () => {
  console.log(`[CCTower] Master Control Node online at port ${PORT}`);
  
  updateLmsStatus().then(() => {
    console.log('[CCTower] Initial LM Studio status checked:', cachedLmsStatus.serverStatus);
  });
  
  setInterval(() => {
    updateLmsStatus().catch(err => {
      console.error('[CCTower] Error polling LM Studio status:', err);
    });
  }, 10000);
});

