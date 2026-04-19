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
    try {
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_operator');
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        // Queue empty
        const delay = 10000;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }

      const target = spots[0];
      console.log(`\n🕵️ [Operator] Targeted: ${target.name} in ${target.city}, ${target.state}`);

      const searchQuery = target.street_address 
          ? `${target.name} ${target.street_address} ${target.city} ${target.state}`
          : `${target.name} ${target.city} ${target.state}`;

      const statusRes = await fetch("http://localhost:5999/status").then(r => r.json()).catch(() => ({ isHeadless: true }));
      
      const identity = GHOST.generateIdentity();
      
      const browser = await puppeteer.launch({ 
        headless: statusRes.isHeadless ? true : false,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
      });
      
      const page = await browser.newPage();
      await page.setUserAgent(identity.userAgent);
      await page.setViewport(identity.viewport);
      
      const queryUrl = `https://www.google.com/search?q=${encodeURIComponent(searchQuery)}`;
      
      console.log(`[Google] Navigating...`);
      await page.goto(queryUrl, { waitUntil: 'domcontentloaded' });
      await sleep(1500);

      // Check for gate
      const isBotGated = await page.evaluate(() => {
        const text = document.body.innerText.toLowerCase();
        return text.includes('verify you are a human') || document.querySelector('.g-recaptcha');
      });

      if (isBotGated) {
          console.error('[Operator] Google Captcha hit. Sleeping for 45s.');
          await browser.close();
          const delay = 45000;
          reportPulse(delay);
          await sleep(delay);
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
      
      await browser.close();

      console.log(`   Result: Website [${website ? 'OK' : 'MISSING'}] Phone [${phone ? 'OK' : 'MISSING'}]`);

      // Update to IDENTITY_ESTABLISHED
      const { error: updateError } = await supabase.from('skate_spots').update({
        website: target.website || (website && !website.includes('google.com') ? website : null),
        phone_number: target.phone_number || phone,
        verification_status: 'IDENTITY_ESTABLISHED',
        retry_count: (target.retry_count || 0) + 1,
        last_attempted_at: new Date().toISOString()
      }).eq('id', target.id);

      if (updateError) throw updateError;
      
      const delay = await GHOST.getAdaptiveDelay('GOOGLE');
      reportPulse(delay, identity);
      await sleep(delay);

    } catch (err: any) {
      console.error('[Operator Error]', err.message);
      const delay = 30000;
      reportPulse(delay);
      await sleep(delay);
    }
  }
}

runOperator();
