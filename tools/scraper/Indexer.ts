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

      const links = await page.evaluate(() => {
         const anchors = Array.from(document.querySelectorAll('a'));
         return anchors.map(a => a.href.toLowerCase()).filter(Boolean);
      });

      let instagram_url = target.instagram_url || null;
      let facebook_url = target.facebook_url || null;
      let tiktok_url = target.tiktok_url || null;
      let schedule_url = target.schedule_url || null;

      links.forEach(href => {
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
      
      await browser.close();

      console.log(`   Result: IG[${instagram_url ? 'Y' : 'N'}] FB[${facebook_url ? 'Y' : 'N'}] TK[${tiktok_url ? 'Y' : 'N'}] SCHED[${schedule_url ? 'Y' : 'N'}]`);

      // Update to INDEXED
      const { error: updateError } = await supabase.from('skate_spots').update({
        instagram_url,
        facebook_url,
        tiktok_url,
        schedule_url,
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
