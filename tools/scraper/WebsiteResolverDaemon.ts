import dotenv from 'dotenv';
import path from 'path';
import http from 'http';
import { db, updateLocalSpot } from './core/LocalDB';
import { WebsiteResolver } from './core/WebsiteResolver';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

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
  
  const postData = JSON.stringify(batch);
  try {
    const req = http.request({
      hostname: 'localhost',
      port: 5999,
      path: '/api/logs/ingest',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(postData)
      },
      timeout: 3000
    }, (res) => {
      res.resume();
    });
    req.on('error', () => {});
    req.write(postData);
    req.end();
  } catch {}
};

console.log = (...args) => { 
  _log(...args); 
  queueLog('INFO', 'Phase 2', args.join(' ')); 
};

console.error = (...args) => { 
  _err(...args); 
  queueLog('ERROR', 'Phase 2', args.join(' ')); 
};

const reportPulse = (delayMs: number, targetName: string | null = null, targetUrl: string | null = null) => {
  const postData = JSON.stringify({ source: 'Phase 2', delayMs, active_job: 'RESOLVING_WEBSITES', target: targetName });
  try {
    const req = http.request({
      hostname: 'localhost',
      port: 5999,
      path: '/api/pulse',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(postData)
      },
      timeout: 3000
    }, (res) => { res.resume(); });
    req.on('error', () => {});
    req.write(postData);
    req.end();
  } catch {}
};

async function runWebsiteResolver() {
  console.log('[WebsiteResolver] 🧠 Booting Website Resolver Daemon (PENDING_WEBSITE -> SEEDED)...');

  while (true) {
    try {
      // Pull next spot that needs its website resolved
      const spot = db.prepare(`
        SELECT * FROM local_spots 
        WHERE verification_status = 'PENDING_WEBSITE' 
        ORDER BY last_attempted_at ASC NULLS FIRST 
        LIMIT 1
      `).get() as any;

      if (!spot) {
        const idleDelay = 10000; // Poll every 10 seconds if queue is empty
        reportPulse(idleDelay);
        await sleep(idleDelay);
        continue;
      }

      console.log(`\n🧠 [WebsiteResolver] Found website-less record: "${spot.name}" (${spot.city || ''}, ${spot.state || ''})`);
      reportPulse(0, spot.name);

      // Instantly touch record to prevent duplicate processing
      updateLocalSpot(spot.id, {
        last_attempted_at: new Date().toISOString(),
        retry_count: (spot.retry_count || 0) + 1
      });

      // Call core WebsiteResolver engine
      const res = await WebsiteResolver.resolveWebsite(spot);

      if (res.success && res.website) {
        console.log(`✅ [WebsiteResolver] Website successfully resolved! URL: ${res.website}`);
        updateLocalSpot(spot.id, {
          website: res.website,
          verification_status: 'SEEDED',
          retry_count: 0,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: JSON.stringify({ 
            website_resolver: {
              source: res.source,
              score: res.score,
              reason: res.reason,
              resolved_at: new Date().toISOString()
            }
          })
        });
      } else {
        console.warn(`⚠️ [WebsiteResolver] Could not resolve high-confidence website: ${res.reason}`);
        updateLocalSpot(spot.id, {
          verification_status: 'WEBSITE_STALLED',
          last_attempted_at: new Date().toISOString(),
          ai_metadata: JSON.stringify({ 
            website_resolver: {
              failed_reason: res.reason,
              attempted_at: new Date().toISOString()
            }
          })
        });
      }

      // Add a rate-limit friendly pause of 5 seconds between queries
      const delay = 5000 + Math.random() * 2000;
      reportPulse(delay);
      await sleep(delay);

    } catch (err: any) {
      console.error(`[WebsiteResolver Daemon Error] ${err.message}`);
      const errDelay = 15000;
      reportPulse(errDelay);
      await sleep(errDelay);
    }
  }
}

runWebsiteResolver();
