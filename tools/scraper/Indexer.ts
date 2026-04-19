import puppeteer from 'puppeteer';
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
import path from 'path';

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
      body: JSON.stringify({ type, source: 'Phase 3', message })
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

async function runIndexer() {
  console.log('[Indexer] Booting Social & Node Discovery Scraper...');
  
  while (true) {
    try {
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_indexer');
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        // Queue empty
        await sleep(10000);
        continue;
      }

      const target = spots[0];
      console.log(`\n🕸️ [Indexer] Analyzing: ${target.name}`);

      // If they don't have a website, we can't index it this way. Skip to INDEXED status immediately.
      if (!target.website) {
         console.log(`   ⚠️ No website on file. Skipping social index. `);
         await supabase.from('skate_spots').update({
             verification_status: 'INDEXED',
             last_attempted_at: new Date().toISOString()
         }).eq('id', target.id);
         continue;
      }

      const statusRes = await fetch("http://localhost:5999/status").then(r => r.json()).catch(() => ({ isHeadless: true }));
      const browser = await puppeteer.launch({ 
        headless: statusRes.isHeadless ? true : false,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
      });
      
      const page = await browser.newPage();
      
      console.log(`[Crawl] Navigating to ${target.website}`);
      await page.goto(target.website, { waitUntil: 'domcontentloaded', timeout: 25000 });
      await sleep(2000);

      const pageData = await page.evaluate(() => {
         const anchors = Array.from(document.querySelectorAll('a'));
         return {
            links: anchors.map(a => a.href.toLowerCase()).filter(Boolean),
            bodyText: document.body.innerText.replace(/\n+/g, ' ') // Flatten newlines
         };
      });

      let instagram_url = target.instagram_url || null;
      let facebook_url = target.facebook_url || null;
      let tiktok_url = target.tiktok_url || null;
      let schedule_url = target.schedule_url || null;

      pageData.links.forEach(href => {
        if (!instagram_url && href.includes('instagram.com') && !href.includes('/explore') && !href.includes('/p/')) {
           instagram_url = href;
        }
        if (!facebook_url && href.includes('facebook.com') && !href.includes('sharer')) {
           facebook_url = href;
        }
        if (!tiktok_url && href.includes('tiktok.com') && !href.includes('music')) {
           tiktok_url = href;
        }
        if (!schedule_url && (href.includes('/schedule') || href.includes('/calendar') || href.includes('/hours'))) {
           schedule_url = href;
        }
      });

      // -------------------------------------------------------------
      // HEURISTIC SPIDER ENGINE (Context Extraction without LLM)
      // -------------------------------------------------------------
      const text = pageData.bodyText;
      let has_adult_night = target.has_adult_night || false;
      let adult_night_details = target.adult_night_details || null;
      let pricing_data: string[] | null = null;
      let opening_hours: any = target.opening_hours || {};

      // 1. ADULT NIGHT HEURISTIC
      const adultRegex = /(\b18\s*\+|\b18\s*and\s*older|\b21\s*\+|\b(?:adult|adults)\s*(?:skate|only|night))/gi;
      const adultMatch = adultRegex.exec(text);
      if (adultMatch) {
         has_adult_night = true;
         // Extract surrounding 60 characters for context
         const ctxStart = Math.max(0, adultMatch.index - 30);
         adult_night_details = text.substring(ctxStart, adultMatch.index + 80).trim();
      }

      // 2. PRICING HEURISTIC
      const pricingSet = new Set<string>();
      // Look for $ amounts within 40 characters of pricing keywords
      const priceRegex = /(?:admission|skate rental|price|entry).{0,50}\$\d+/gi;
      let pMatch;
      while ((pMatch = priceRegex.exec(text)) !== null) {
         pricingSet.add(pMatch[0].trim());
      }
      if (pricingSet.size > 0) pricing_data = Array.from(pricingSet);

      // 3. OPERATING HOURS HEURISTIC
      // Extract days followed by times
      const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
      days.forEach(day => {
         // Look for "Monday 4:00 PM - 9:00 PM" loosely
         const dayRegex = new RegExp(`\\b${day}\\b.{0,20}(\\d{1,2}(?::\\d{2})?\\s*(?:am|pm)?\\s*(?:-|to)\\s*\\d{1,2}(?::\\d{2})?\\s*(?:am|pm)?|closed)`, 'i');
         const dMatch = dayRegex.exec(text);
         if (dMatch && !opening_hours[day]) {
            opening_hours[day] = dMatch[1].trim();
         }
      });
      if (Object.keys(opening_hours).length === 0) opening_hours = null;
      
      await browser.close();

      console.log(`   Result: IG[${instagram_url ? 'Y' : 'N'}] FB[${facebook_url ? 'Y' : 'N'}] TK[${tiktok_url ? 'Y' : 'N'}]; Adult:[${has_adult_night ? 'Y' : 'N'}] Prices:[${pricing_data ? pricing_data.length : 0}] Hours:[${opening_hours ? Object.keys(opening_hours).length : 0}]`);

      // Update to INDEXED
      const { error: updateError } = await supabase.from('skate_spots').update({
        instagram_url,
        facebook_url,
        tiktok_url,
        schedule_url,
        has_adult_night,
        adult_night_details,
        pricing_data,
        opening_hours,
        verification_status: 'INDEXED',
        retry_count: 0, // Reset retries for the next pipeline phase
        last_attempted_at: new Date().toISOString()
      }).eq('id', target.id);

      if (updateError) throw updateError;
      
      await sleep(5000);

    } catch (err: any) {
      console.error('[Indexer Error]', err.message);
      // Wait a bit before retrying on error
      await sleep(30000);
    }
  }
}

runIndexer();
