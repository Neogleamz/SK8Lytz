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

// ─── Telemetry Hook to CCTower ─────────────────────────────────────────────
const _log = console.log;
const _err = console.error;

const pushLog = (type: 'INFO' | 'ERROR', message: string) => {
  fetch('http://localhost:5999/api/logs/ingest', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ type, source: 'Phase 3', message })
  }).catch(() => {});
};

const reportPulse = (delayMs: number, ghost?: any) => {
  fetch('http://localhost:5999/api/pulse', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source: 'Phase 3', delayMs, ghost })
  }).catch(() => {});
};

console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

// ─── Constants ─────────────────────────────────────────────────────────────
const DAYS = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'] as const;
type DayKey = typeof DAYS[number];

// ─── Main Indexer Loop ──────────────────────────────────────────────────────

async function runIndexer() {
  console.log('[Indexer v2] 🕵️ Booting Detective — Hours/Pricing/Events Engine...');

  while (true) {
    try {
      // Fetch active region config — ensures priority state changes take effect immediately
      const configRes = await fetch('http://localhost:5999/api/priority-states').then(r => r.json()).catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_indexer', { priority_states: priorityStates });
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        const delay = 15000;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }

      const target = spots[0];
      console.log(`\n🕵️ [Indexer v2] Analyzing: ${target.name} (${target.city}, ${target.state})`);

      // ── No website: mark as crawled and skip ──────────────────────────────
      if (!target.website) {
        console.log(`   ⚠️ No website. Marking crawled and skipping.`);
        await supabase.from('skate_spots').update({
          is_deep_crawled: true,
          last_attempted_at: new Date().toISOString()
        }).eq('id', target.id);
        continue;
      }

      // ── Fetch current configurations ───────────────────────────────────
      const statusRes = await fetch('http://localhost:5999/status')
        .then(r => r.json())
        .catch(() => ({ isHeadless: true }));
        
      const configResGlobal = await fetch('http://localhost:5999/config')
        .then(r => r.json())
        .catch(() => ({ config: {} }));
      const aiConfig = configResGlobal.config || {};

      const identity = GHOST.generateIdentity();

      const browser = await puppeteer.launch({
        headless: statusRes.isHeadless ? 'new' : false,
        protocolTimeout: 60000,
        args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
      });

      const page = await browser.newPage();
      await page.setUserAgent(identity.userAgent);
      await page.setViewport(identity.viewport);

      // ── Navigate with networkidle2 for JS-rendered content ───────────────
      console.log(`   [Crawl] → ${target.website}`);
      try {
        await page.goto(target.website, {
          waitUntil: 'networkidle2',
          timeout: 30000
        });
      } catch {
        // Fallback to domcontentloaded if networkidle2 times out
        try {
          await page.goto(target.website, { waitUntil: 'domcontentloaded', timeout: 15000 });
        } catch {
          console.error(`   ✗ Navigation failed for ${target.website}`);
          await browser.close();
          await supabase.from('skate_spots').update({
            is_deep_crawled: true,
            retry_count: (target.retry_count || 0) + 1,
            last_attempted_at: new Date().toISOString()
          }).eq('id', target.id);
          continue;
        }
      }

      await sleep(1500);

      // ── DOM Extraction & Cleanup for LLM ───────────────────────────────────
      const pageData = await page.evaluate(() => {
        const anchors = Array.from(document.querySelectorAll('a'));
        const links = anchors.map(a => a.href.toLowerCase()).filter(Boolean);
        
        // Clean DOM
        document.querySelectorAll('nav, footer, script, style, header, iframe, noscript').forEach(el => el.remove());
        const cleanText = document.body.innerText.replace(/\n+/g, ' ').replace(/\s{2,}/g, ' ').trim();
        
        return { links, cleanText };
      });

      await browser.close();

      const text = pageData.cleanText;

      // ── AI Toxicity Bouncer (Dynamic Exclusions) ───────────────────────────
      const exclusions = aiConfig.ai_exclusion_keywords || [];
      const toxicityReason = exclusions.find((kw: string) => text.toLowerCase().includes(kw.toLowerCase()));
      if (toxicityReason) {
        console.log(`   🚫 AI HEALER ABORT: Exclusion keyword hit [${toxicityReason}]`);
        pushLog('INFO', `Phase 2 Healer rejected ${target.name}: Keyword "${toxicityReason}"`);
        await supabase.from('skate_spots').update({
          is_deep_crawled: true,
          verification_status: 'REJECTED',
          last_attempted_at: new Date().toISOString()
        }).eq('id', target.id);
        continue;
      }

      // ── Llama-3 Detective Extraction ──────────────────────────────────────
      const systemPrompt = aiConfig.ai_system_prompt || 'Extract JSON data accurately.';
      const targetVectors = aiConfig.ai_target_vectors || [];
      const schema = targetVectors.reduce((acc: any, vec: any) => {
        acc[vec.key] = vec.type;
        return acc;
      }, {});

      const prompt = `${systemPrompt}\n\nSchema:\n${JSON.stringify(schema, null, 2)}\n\nWebsite Text:\n${text}`;
      
      console.log(`   🧠 Invoking Ollama Detective (llama3)...`);
      let aiMetadata: any = {};
      try {
        const fetch = require('node-fetch');
        const response = await fetch('http://localhost:11434/api/generate', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            model: 'llama3.1',
            prompt: prompt,
            format: 'json',
            stream: false
          })
        });
        if (!response.ok) throw new Error('Ollama HTTP error');
        const aiData = await response.json();
        aiMetadata = JSON.parse(aiData.response);
      } catch (err: any) {
         console.error('   ✗ Ollama Extraction Failed:', err.message);
         // Proceed with empty metadata; we still get photos and links
      }

      // Map AI Metadata back to structured columns if they match
      const opening_hours = aiMetadata.opening_hours || target.opening_hours || null;
      const has_adult_night = aiMetadata.has_adult_night || target.has_adult_night || false;
      const adult_night_details = aiMetadata.adult_night_details || target.adult_night_details || null;
      const adultNightSchedule = aiMetadata.adult_night_schedule || target.adult_night_schedule || null;
      const special_events = aiMetadata.special_events || target.special_events || null;
      const pricing_data = aiMetadata.pricing_data || target.pricing_data || null;

      // ── Social Links ──────────────────────────────────────────────────────
      let instagram_url = target.instagram_url || null;
      let facebook_url = target.facebook_url || null;
      let tiktok_url = target.tiktok_url || null;
      let schedule_url = target.schedule_url || null;

      pageData.links.forEach((href: string) => {
        if (!instagram_url && href.includes('instagram.com') && !href.includes('/explore') && !href.includes('/p/')) {
          instagram_url = href;
        }
        if (!facebook_url && href.includes('facebook.com') && !href.includes('sharer')) {
          facebook_url = href;
        }
        if (!tiktok_url && href.includes('tiktok.com') && !href.includes('music')) {
          tiktok_url = href;
        }
        if (!schedule_url && href.includes('.pdf') && (href.includes('sched') || href.includes('hours') || href.includes('calendar'))) {
          schedule_url = href;
        }
        if (!schedule_url && (href.includes('/schedule') || href.includes('/calendar') || href.includes('/hours'))) {
          schedule_url = href;
        }
      });

      // ── Photo Candidates (free harvest — no API cost) ─────────────────────
      // Only collect if we don't already have photos
      let candidate_photos: Record<string, any> | null = target.candidate_photos || null;
      if (!target.photos && !candidate_photos) {
        const candidateMap: Record<string, any> = {};

        // Tier 1: OG image — site's intentional hero photo
        const ogImage = await page.evaluate(() =>
          document.querySelector('meta[property="og:image"]')?.getAttribute('content') || null
        ).catch(() => null);
        if (ogImage && !ogImage.includes('placeholder') && !ogImage.includes('default')) {
          candidateMap.og_image = ogImage;
        }

        // Tier 2: DOM large images (naturalWidth >= 400, naturalHeight >= 300)
        const domImages = await page.evaluate(() => {
          return Array.from(document.querySelectorAll('img'))
            .filter((img: any) => {
              const w = img.naturalWidth || img.width || 0;
              const h = img.naturalHeight || img.height || 0;
              const src = (img.src || '').toLowerCase();
              // Exclude icons, logos, tracking pixels, placeholder gifs
              return w >= 400 && h >= 300 &&
                !src.includes('logo') && !src.includes('icon') &&
                !src.includes('pixel') && !src.includes('gif') &&
                (src.startsWith('http') || src.startsWith('//'));
            })
            .map((img: any) => img.src)
            .slice(0, 3);
        }).catch(() => [] as string[]);
        if (domImages.length > 0) candidateMap.dom_images = domImages;

        // Tier 3: Street View Static (deterministic from lat/lng — free tier 25k/month)
        const MAPS_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY || '';
        if (target.lat && target.lng && MAPS_KEY) {
          candidateMap.street_view_url = `https://maps.googleapis.com/maps/api/streetview?size=800x600&location=${target.lat},${target.lng}&heading=auto&fov=80&key=${MAPS_KEY}`;
        }

        // Tier 4: Facebook cover photo via OG (lightweight fetch — no browser)
        if (facebook_url && facebook_url.includes('facebook.com')) {
          try {
            const fbRes = await fetch(facebook_url, {
              headers: { 'User-Agent': 'facebookexternalhit/1.1' },
              signal: AbortSignal.timeout(5000)
            });
            const fbHtml = await fbRes.text();
            const ogMatch = fbHtml.match(/<meta[^>]+property=["']og:image["'][^>]+content=["']([^"']+)["']/);
            if (ogMatch?.[1]) candidateMap.facebook_og = ogMatch[1];
          } catch { /* Facebook fetch failed — non-critical */ }
        }

        if (Object.keys(candidateMap).length > 0) {
          candidate_photos = candidateMap;
          console.log(`   📸 Photo candidates collected: ${Object.keys(candidateMap).join(', ')}`);
        }
      }

      // ── Log Result ────────────────────────────────────────────────────────
      const hoursFound = Object.keys(opening_hours || {}).length;
      const scheduleFound = adultNightSchedule ? Object.keys(adultNightSchedule).length : 0;
      const eventsFound = special_events?.length || 0;
      const pricingFound = Object.keys(pricing_data || {}).length;
      console.log(`   ✓ Hours[${hoursFound}/7] AdultNight[${has_adult_night ? 'Y' : 'N'}, ${scheduleFound} days] Events[${eventsFound}] Pricing[${pricingFound}] Socials[IG:${instagram_url ? 'Y' : 'N'} FB:${facebook_url ? 'Y' : 'N'}] Photos[${candidate_photos ? Object.keys(candidate_photos).length : 0} candidates]`);

      // ── Write to DB ───────────────────────────────────────────────────────
      const { error: updateError } = await supabase.from('skate_spots').update({
        // Social
        instagram_url,
        facebook_url,
        tiktok_url,
        schedule_url,
        // Hours
        opening_hours,
        // Adult Night
        has_adult_night,
        adult_night_details,
        adult_night_schedule: adultNightSchedule,
        // Events & Pricing
        special_events,
        pricing_data,
        // Photos (candidates written here; Photographer daemon downloads + uploads)
        ...(candidate_photos ? { candidate_photos } : {}),
        
        // AI Metadata Dump
        ai_metadata: Object.keys(aiMetadata).length > 0 ? aiMetadata : (target.ai_metadata || null),

        // Pipeline flags
        // IMPORTANT: Never downgrade ENRICHED. ENRICHED + deep_crawled = still ENRICHED.
        // Only promote IDENTITY_ESTABLISHED to INDEXED after deep crawl.
        verification_status: target.verification_status === 'ENRICHED' ? 'ENRICHED' : 'INDEXED',
        is_deep_crawled: true,
        retry_count: 0,
        last_attempted_at: new Date().toISOString()
      }).eq('id', target.id);

      if (updateError) throw updateError;

      const delay = await GHOST.getAdaptiveDelay('GOOGLE');
      reportPulse(delay, identity);
      await sleep(delay);

    } catch (err: any) {
      console.error('[Indexer Error]', err.message);
      const delay = 30000;
      reportPulse(delay);
      await sleep(delay);
    }
  }
}

runIndexer();
