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

async function runOperator() {
  console.log('[Operator] Booting Identity & Contact Scraper...');
  
  while (true) {
    try {
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_operator');
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        // Queue empty
        await sleep(10000);
        continue;
      }

      const target = spots[0];
      console.log(`\n🕵️ [Operator] Targeted: ${target.name} in ${target.city}, ${target.state}`);

      const searchQuery = target.street_address 
          ? `${target.name} ${target.street_address} ${target.city} ${target.state}`
          : `${target.name} ${target.city} ${target.state}`;

      const browser = await puppeteer.launch({ 
        headless: 'new',
        args: ['--no-sandbox', '--disable-setuid-sandbox']
      });
      
      const page = await browser.newPage();
      
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
          await sleep(45000);
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
      
      await sleep(5000);

    } catch (err: any) {
      console.error('[Operator Error]', err.message);
      await sleep(30000);
    }
  }
}

runOperator();
