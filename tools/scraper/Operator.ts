import puppeteer from 'puppeteer';
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';
import { GHOST } from './lib/GHOST';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });
const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '', 
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

const sleep = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Telemetry Hook to CCTower
const _log = console.log;
const _err = console.error;

const pushLog = (type: 'INFO'|'ERROR', message: string) => {
   fetch('http://localhost:5999/api/logs/ingest', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ type, source: 'Phase 2', message })
   }).catch(() => {});
};

const reportPulse = (delayMs: number, ghost?: any) => {
   fetch('http://localhost:5999/api/pulse', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ source: 'Phase 2', delayMs, ghost })
   }).catch(() => {});
};

console.log = (...args) => {
   _log(...args);
   pushLog('INFO', args.join(' '));
};
console.error = (...args) => {
   _err(...args);
   pushLog('ERROR', args.join(' '));
};

async function runOperator() {
  console.log('[Operator] Booting Identity & Contact Scraper...');
  
  while (true) {
    let target: any = null;
    let browser: any = null;
    let delay = await GHOST.getAdaptiveDelay('GOOGLE');

    try {
      // Fetch active region config before each pick — ensures real-time priority state changes are respected
      const configRes = await fetch('http://localhost:5999/api/priority-states').then(r => r.json()).catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_operator', { priority_states: priorityStates });
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        delay = 30000; // Longer sleep if queue empty
        continue;
      }

      target = spots[0];
      console.log(`\n🕵️ [Operator] Targeted: ${target.name} in ${target.city}, ${target.state}`);
      
      // PRE-FLIGHT: Update last_attempted_at immediately to "bury" it in the queue 
      // so we don't repeat the same rink if we crash or get gated.
      await supabase.from('skate_spots').update({
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      }).eq('id', target.id);

      const searchQuery = target.street_address 
          ? `${target.name} ${target.street_address} ${target.city} ${target.state}`
          : `${target.name} ${target.city} ${target.state}`;

      const statusRes = await fetch("http://localhost:5999/status").then(r => r.json()).catch(() => ({ isHeadless: true }));
      const identity = GHOST.generateIdentity();
      
      browser = await puppeteer.launch({ 
        headless: statusRes.isHeadless ? true : false,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
      });
      
      const page = await browser.newPage();
      await page.setUserAgent(identity.userAgent);
      await page.setViewport(identity.viewport);
      
      const queryUrl = `https://www.google.com/search?q=${encodeURIComponent(searchQuery)}`;
      console.log(`[Google] Navigating...`);
      await page.goto(queryUrl, { waitUntil: 'domcontentloaded', timeout: 45000 });
      await sleep(2000);

      // Check for gate
      const isBotGated = await page.evaluate(() => {
        const text = document.body.innerText.toLowerCase();
        return text.includes('verify you are a human') || document.querySelector('.g-recaptcha') || text.includes('unusual traffic');
      });

      if (isBotGated) {
          console.error(`[Operator] 🚨 Google Captcha hit on ${target.name}. Moving to GHOST_PAUSED.`);
          await supabase.from('skate_spots').update({ 
              verification_status: 'GHOST_PAUSED',
              last_error: 'GOOGLE_CAPTCHA_GATED' 
          }).eq('id', target.id);
          
          delay = delay * 5; // 5x Penalty Delay
          continue;
      }

      // Step 2: Website Resolution
      const website = await page.evaluate(() => {
        const anchors = Array.from(document.querySelectorAll('a'));
        const webBtn = anchors.find(a => a.innerText.toLowerCase() === 'website' || (a.getAttribute('aria-label') || '').toLowerCase().includes('website'));
        return webBtn ? webBtn.getAttribute('href') : null;
      });

      // Step 4: Phone Resolution
      const phone = await page.evaluate(() => {
        const sel = document.querySelector('a[data-number], [data-dtype="d_ph"] span, a[href^="tel:"]');
        if (sel) {
          const raw = (sel as HTMLElement).innerText || sel.getAttribute('href') || '/';
          const clean = raw.replace('tel:', '').trim();
          if (/\d/.test(clean)) return clean;
        }
        return null;
      });
      
      console.log(`   Result: Website [${website ? 'OK' : 'MISSING'}] Phone [${phone ? 'OK' : 'MISSING'}]`);

      // Update to IDENTITY_ESTABLISHED
      const { error: updateError } = await supabase.from('skate_spots').update({
        website: target.website || (website && !website.includes('google.com') ? website : null),
        phone_number: target.phone_number || phone,
        verification_status: 'IDENTITY_ESTABLISHED'
      }).eq('id', target.id);

      if (updateError) throw updateError;
      
      reportPulse(delay, identity);

    } catch (err: any) {
      console.error('[Operator Error]', err.message);
      if (target) {
          await supabase.from('skate_spots').update({ 
               last_error: err.message.slice(0, 200) 
          }).eq('id', target.id);
      }
      delay = Math.max(delay, 45000); // 45s minimum on error
    } finally {
      if (browser) {
          try { await browser.close(); } catch(e) {}
      }
      // UNIVERSAL STEALTH GUARD: The loop physically cannot restart without sleeping.
      console.log(`⏳ [GHOST] Adaptive Stealth Cooldown: ${Math.round(delay/1000)}s...`);
      reportPulse(delay);
      await sleep(delay);
    }
  }
}

runOperator();
