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

const reportPulse = (delayMs: number, ghost?: any, active_job?: string | null, target_url?: string | null) => {
  fetch('http://localhost:5999/api/pulse', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ source: 'Phase 3', delayMs, ghost, active_job, target: target_url })
  }).catch(() => {});
};

console.log = (...args) => { _log(...args); pushLog('INFO', args.join(' ')); };
console.error = (...args) => { _err(...args); pushLog('ERROR', args.join(' ')); };

// ─── AI Output Type Sanitizers ────────────────────────────────────────────
// The LLM returns free-text; these coerce to Postgres-safe types or null.

/** Parse a number from AI output. Returns null if unparseable (e.g. "family-friendly"). */
const safeNum = (v: any): number | null => {
  if (v == null) return null;
  const n = Number(v);
  return isNaN(n) ? null : n;
};

/** Parse a boolean from AI output. Returns null for ambiguous free-text. */
const safeBool = (v: any): boolean | null => {
  if (v == null) return null;
  if (typeof v === 'boolean') return v;
  if (typeof v === 'string') {
    const lower = v.toLowerCase().trim();
    if (['yes', 'true', '1', 'y', 'available', 'offered'].includes(lower)) return true;
    if (['no', 'false', '0', 'n', 'null', 'none', 'unknown', 'not available', 'n/a'].includes(lower)) return false;
  }
  // Free-text sentences (e.g. "Wheelchairs are allowed...") → null, not a crash
  return null;
};

/**
 * Map AI surface string to a valid skate_spot_surface enum value.
 * DB enum: wood | concrete | asphalt | sport_court | unknown
 */
const SURFACE_KEYWORD_MAP: [string, string][] = [
  ['maple', 'wood'], ['hardwood', 'wood'], ['wood', 'wood'], ['rotacast', 'wood'],
  ['roll-on', 'wood'], ['laminate', 'wood'],
  ['concrete', 'concrete'], ['cement', 'concrete'],
  ['asphalt', 'asphalt'], ['tarmac', 'asphalt'],
  ['sport court', 'sport_court'], ['sport_court', 'sport_court'],
  ['polyurethane', 'sport_court'], ['synthetic', 'sport_court'], ['rubber', 'sport_court'],
];
const safeSurface = (v: any): string | null => {
  if (!v || typeof v !== 'string') return null;
  const lower = v.toLowerCase().trim();
  for (const [keyword, enumVal] of SURFACE_KEYWORD_MAP) {
    if (lower.includes(keyword)) return enumVal;
  }
  return 'unknown'; // AI returned something but we can't map it — valid enum, not a crash
};

// ─── Constants ──────────────────────────────────────────────────────────────
const DAYS = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'] as const;
type DayKey = typeof DAYS[number];
const MAX_PAGES_PER_RECORD = 4;

// ─── Main Indexer Loop ──────────────────────────────────────────────────────
async function runIndexer() {
  console.log('[Indexer v3] 🧠 Booting AI Detective — using Spider-provided candidate_links...');

  while (true) {
    let browser: any = null;
    try {
      // Fetch active region config — hard state filter applied at RPC level
      const configRes = await fetch('http://localhost:5999/api/priority-states')
        .then(r => r.json())
        .catch(() => ({ priority_states: [] }));
      const priorityStates = configRes.priority_states || [];

      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_indexer', {
        priority_states: priorityStates
      });
      if (rpcError) throw new Error('RPC Failed/' + rpcError.message);

      if (!spots || spots.length === 0) {
        const delay = 15000;
        reportPulse(delay);
        await sleep(delay);
        continue;
      }

      const target = spots[0];
      console.log(`\n🧠 [Detective] Analyzing: ${target.name} (${target.city}, ${target.state})`);
      // Report active job to CCTower telemetry immediately
      reportPulse(0, undefined, target.name, target.website || null);

      // Mark in-flight immediately to prevent duplicate picks
      await supabase.from('skate_spots').update({
        last_attempted_at: new Date().toISOString(),
        retry_count: (target.retry_count || 0) + 1
      }).eq('id', target.id);

      // ── Fetch global AI config ─────────────────────────────────────────────
      const statusRes = await fetch('http://localhost:5999/status')
        .then(r => r.json())
        .catch(() => ({ isHeadless: true }));

      const configResGlobal = await fetch('http://localhost:5999/config')
        .then(r => r.json())
        .catch(() => ({ config: {} }));
      const aiConfig = configResGlobal.config || {};


      // ── Use candidate_links from Spider (Phase 2) ──────────────────────────
      // candidate_links is a JSONB object: { root: url, hours: url, pricing: url, ... }
      // Priority: hours > adult > pricing > events > contact > about > root
      const candidateLinks: Record<string, string> = target.candidate_links || {};
      const PAGE_PRIORITY = ['hours', 'adult', 'pricing', 'events', 'contact', 'about', 'root'];

      // Build ordered list of URLs to visit (deduplicated, max MAX_PAGES_PER_RECORD)
      const urlsToVisit: string[] = [];
      for (const key of PAGE_PRIORITY) {
        if (candidateLinks[key] && !urlsToVisit.includes(candidateLinks[key])) {
          urlsToVisit.push(candidateLinks[key]);
          if (urlsToVisit.length >= MAX_PAGES_PER_RECORD) break;
        }
      }
      // Fallback: use website root if no candidate_links were collected
      if (urlsToVisit.length === 0 && target.website) {
        urlsToVisit.push(target.website);
      }

      // ── Filter out social media URLs — they hit login walls, yield no text ──
      const SOCIAL_CRAWL_BLOCKLIST = ['facebook.com', 'instagram.com', 'twitter.com', 'x.com', 'tiktok.com', 'youtube.com', 'yelp.com', 'google.com', 'linkedin.com'];
      const isSocialCrawlBlocked = (url: string) => {
        try { const h = new URL(url).hostname.replace('www.', ''); return SOCIAL_CRAWL_BLOCKLIST.some(d => h === d || h.endsWith('.' + d)); } catch { return false; }
      };
      const filteredUrls = urlsToVisit.filter(u => !isSocialCrawlBlocked(u));
      const socialOnlyRecord = filteredUrls.length === 0;

      if (socialOnlyRecord) {
        console.log(`   ⚠️  [Detective] No crawlable URLs (social-only record) — running AI on metadata only.`);
      }

      console.log(`   [Detective] Visiting ${filteredUrls.length} candidate pages${socialOnlyRecord ? ' (SKIP — social only)' : ''}...`);

      // ── Visit candidate pages OR fallback to metadata-only for social records ──
      let combinedText = '';
      const allLinks: string[] = [];
      let ogImage: string | null = null;
      const domImages: string[] = [];

      if (!socialOnlyRecord) {
        // Launch browser only when we have real URLs to visit
        browser = await puppeteer.launch({
          headless: statusRes.isHeadless ? 'new' : false,
          protocolTimeout: 60000,
          args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
        });
        const page = await browser.newPage();
        await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36');
        await page.setViewport({ width: 1280, height: 800 });

        for (const url of filteredUrls) {
          console.log(`   → ${url}`);
          try {
            await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 });
          } catch {
            try {
              await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 15000 });
            } catch {
              console.error(`   ✗ Navigation failed: ${url}`);
              continue;
            }
          }
          await sleep(1000);

          const pageData = await page.evaluate(() => {
            const ogMeta = document.querySelector('meta[property="og:image"]')?.getAttribute('content') || null;
            const imgs = Array.from(document.querySelectorAll('img'))
              .filter((img: any) => {
                const w = img.naturalWidth || img.width || 0;
                const h = img.naturalHeight || img.height || 0;
                const src = (img.src || '').toLowerCase();
                return w >= 400 && h >= 300 &&
                  !src.includes('logo') && !src.includes('icon') &&
                  !src.includes('pixel') && !src.includes('gif') &&
                  (src.startsWith('http') || src.startsWith('//'));
              })
              .map((img: any) => img.src)
              .slice(0, 3);
            const links = Array.from(document.querySelectorAll('a'))
              .map(a => (a.href || '').toLowerCase())
              .filter(Boolean);
            document.querySelectorAll('nav, footer, script, style, header, iframe, noscript').forEach(el => el.remove());
            const cleanText = document.body.innerText.replace(/\n+/g, ' ').replace(/\s{2,}/g, ' ').trim();
            return { ogMeta, imgs, links, cleanText };
          }).catch(() => ({ ogMeta: null, imgs: [], links: [], cleanText: '' }));

          if (pageData.cleanText) {
            combinedText += `\n\n[PAGE: ${url}]\n${pageData.cleanText}`;
          }
          allLinks.push(...pageData.links);
          if (!ogImage && pageData.ogMeta && !pageData.ogMeta.includes('placeholder')) {
            ogImage = pageData.ogMeta;
          }
          domImages.push(...pageData.imgs.slice(0, 2));
        }

        await browser.close();
        browser = null;
      } else {
        // Social-only record — give AI basic metadata context to work with
        combinedText = `Facility name: ${target.name}. Location: ${target.city}, ${target.state}. ` +
          `Type: ${target.facility_type || 'roller rink'}. ` +
          (target.phone ? `Phone: ${target.phone}. ` : '') +
          (target.facebook_url ? `Facebook: ${target.facebook_url}. ` : '') +
          `Note: No website available — extract what you can from facility type and name.`;
      }


      // ── AI Toxicity Bouncer ────────────────────────────────────────────────
      const exclusions = aiConfig.ai_exclusion_keywords || [];
      const toxicityReason = exclusions.find((kw: string) =>
        combinedText.toLowerCase().includes(kw.toLowerCase())
      );
      if (toxicityReason) {
        console.log(`   🚫 HEALER ABORT: Exclusion keyword [${toxicityReason}]`);
        await supabase.from('skate_spots').update({
          is_deep_crawled: true,
          verification_status: 'REJECTED',
          last_attempted_at: new Date().toISOString()
        }).eq('id', target.id);
        continue;
      }

      // ── Llama-3 Detective Extraction ───────────────────────────────────────
      const systemPrompt = aiConfig.ai_system_prompt || 'Extract JSON data accurately.';
      const targetVectors = aiConfig.ai_target_vectors || [];
      const schema = targetVectors.reduce((acc: any, vec: any) => {
        acc[vec.key] = vec.prompt || vec.type;
        return acc;
      }, {});

      let contextHeader = '';
      if (target.name && target.city) {
        contextHeader = `You are analyzing website content for a skate facility. The specific location is [${target.name}] in [${target.city}]. The text below may contain info for multiple franchise locations. ONLY extract data for [${target.name}]. Ignore all other cities.\n\n`;
      }

      const prompt = `${contextHeader}${systemPrompt}\n\nSchema:\n${JSON.stringify(schema, null, 2)}\n\nWebsite Text:\n${combinedText.slice(0, 12000)}`;

      const detectiveModel = aiConfig.detective_model || 'llama3.1';
      console.log(`   🧠 Invoking Ollama Detective (${detectiveModel})...`);

      let aiMetadata: any = {};
      try {
        const fetch = require('node-fetch');
        const response = await fetch('http://localhost:11434/api/generate', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            model: detectiveModel,
            prompt,
            format: 'json',
            stream: false
          })
        });
        if (!response.ok) throw new Error('Ollama HTTP error');
        const aiData = await response.json();
        aiMetadata = JSON.parse(aiData.response);
      } catch (err: any) {
        console.error('   ✗ Ollama Extraction Failed:', err.message);
        // Non-fatal — proceed with socials and photo candidates
      }

      // ── Map AI output → typed DB columns (sanitized to prevent Postgres type errors) ──
      const opening_hours        = aiMetadata.hours             || aiMetadata.opening_hours        || target.opening_hours        || null;
      const pricing_data         = aiMetadata.pricing            || aiMetadata.pricing_data         || target.pricing_data         || null;
      const surface_type         = safeSurface(aiMetadata.surface_type    || target.surface_type);
      const surface_quality      = aiMetadata.surface_quality    || target.surface_quality || null;
      const vibe_score           = safeNum(aiMetadata.vibe_score           ?? target.vibe_score);
      const is_indoor            = safeBool(aiMetadata.is_indoor            ?? target.is_indoor);
      const has_fee              = safeBool(aiMetadata.has_fee              ?? target.has_fee);
      const has_adult_night      = safeBool(aiMetadata.has_adult_night      ?? target.has_adult_night) ?? false;
      const adult_night_details  = aiMetadata.adult_night_details  || target.adult_night_details  || null;
      const adultNightSchedule   = aiMetadata.adult_night_schedule || target.adult_night_schedule || null;
      const special_events       = aiMetadata.special_events       || target.special_events       || null;
      // capacity is integer in DB — use parseInt, not parseFloat
      const rawCapacity          = aiMetadata.capacity ?? target.capacity;
      const capacity             = rawCapacity != null && !isNaN(parseInt(String(rawCapacity))) ? parseInt(String(rawCapacity)) : null;
      const has_rental           = safeBool(aiMetadata.has_rental           ?? target.has_rental);
      const has_pro_shop         = safeBool(aiMetadata.has_pro_shop         ?? target.has_pro_shop);
      const has_food             = safeBool(aiMetadata.has_food             ?? target.has_food);
      const has_lights           = safeBool(aiMetadata.has_lights           ?? target.has_lights);
      const has_lockers          = safeBool(aiMetadata.has_lockers          ?? target.has_lockers);
      const has_ac               = safeBool(aiMetadata.has_ac               ?? target.has_ac);
      const has_wifi             = safeBool(aiMetadata.has_wifi             ?? target.has_wifi);
      const has_toilets          = safeBool(aiMetadata.has_toilets          ?? target.has_toilets);
      const is_wheelchair_accessible = safeBool(aiMetadata.wheelchair ?? aiMetadata.is_wheelchair_accessible ?? target.is_wheelchair_accessible);
      const hosts_derby              = safeBool(aiMetadata.derby     ?? aiMetadata.hosts_derby              ?? target.hosts_derby);
      const cultural_metadata        = aiMetadata.cultural_meta     || aiMetadata.cultural_metadata         || target.cultural_metadata || null;
      const operator_description     = aiMetadata.operator_description || target.operator_description       || null;

      // ── Social Links (from collected hrefs) ────────────────────────────────
      let instagram_url = target.instagram_url || null;
      let facebook_url  = target.facebook_url  || null;
      let tiktok_url    = target.tiktok_url    || null;
      let schedule_url  = target.schedule_url  || null;

      for (const href of allLinks) {
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
      }

      // ── Photo Candidates ───────────────────────────────────────────────────
      let candidate_photos: Record<string, any> | null = target.candidate_photos || null;
      if (!target.photos && !candidate_photos) {
        const candidateMap: Record<string, any> = {};

        if (ogImage) candidateMap.og_image = ogImage;
        if (domImages.length > 0) candidateMap.dom_images = [...new Set(domImages)].slice(0, 3);

        // Street View Static (free tier — 25k/month)
        const MAPS_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY || '';
        if (target.lat && target.lng && MAPS_KEY) {
          candidateMap.street_view_url = `https://maps.googleapis.com/maps/api/streetview?size=800x600&location=${target.lat},${target.lng}&heading=auto&fov=80&key=${MAPS_KEY}`;
        }

        // Facebook cover OG (lightweight fetch)
        if (facebook_url && facebook_url.includes('facebook.com')) {
          try {
            const fbRes = await fetch(facebook_url, {
              headers: { 'User-Agent': 'facebookexternalhit/1.1' },
              signal: AbortSignal.timeout(5000)
            });
            const fbHtml = await fbRes.text();
            const ogMatch = fbHtml.match(/<meta[^>]+property=["']og:image["'][^>]+content=["']([^"']+)["']/);
            if (ogMatch?.[1]) candidateMap.facebook_og = ogMatch[1];
          } catch { /* non-critical */ }
        }

        if (Object.keys(candidateMap).length > 0) {
          candidate_photos = candidateMap;
          console.log(`   📸 Photo candidates: ${Object.keys(candidateMap).join(', ')}`);
        }
      }

      // ── Quality Gate — count populated fields before advancing ──────────────
      // Each non-null key field counts as 1 point.
      // DEEP_CRAWLED requires >= 2 points so we know the AI actually extracted something.
      // Records with 0-1 points are marked STALLED — visible on dashboard, not re-queued blindly.
      const qualityFields = [
        opening_hours, pricing_data, surface_type, vibe_score,
        has_fee, has_rental, has_pro_shop, has_food, has_lights,
        has_ac, has_lockers, has_toilets, has_wifi,
        has_adult_night, special_events, operator_description, cultural_metadata
      ];
      const qualityScore = qualityFields.filter(f => f !== null && f !== undefined && f !== false).length;
      const hoursFound   = Object.keys(opening_hours || {}).length;
      const pricingFound = Object.keys(pricing_data  || {}).length;
      const eventsFound  = Array.isArray(special_events) ? special_events.length : 0;
      const schedFound   = adultNightSchedule ? Object.keys(adultNightSchedule).length : 0;

      console.log(`   📊 Quality[${qualityScore}/17] Hours[${hoursFound}/7] Pricing[${pricingFound}] AdultNight[${has_adult_night ? 'Y' : 'N'}, ${schedFound}d] Events[${eventsFound}] Socials[IG:${instagram_url ? 'Y' : 'N'} FB:${facebook_url ? 'Y' : 'N'}] Photos[${candidate_photos ? Object.keys(candidate_photos).length : 0}]`);

      if (qualityScore < 2) {
        // AI ran but extracted nothing meaningful — mark STALLED so it is visible on dashboard
        console.error(`   ⚠️  STALLED: Quality score ${qualityScore}/17 — insufficient data extracted. Marking STALLED.`);
        await supabase.from('skate_spots').update({
          verification_status: 'STALLED',
          is_deep_crawled: false,
          retry_count: (target.retry_count || 0) + 1,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: Object.keys(aiMetadata).length > 0 ? aiMetadata : (target.ai_metadata || null),
          ...(candidate_photos ? { candidate_photos } : {})
        }).eq('id', target.id);
        continue;
      }

      // ── Write all fields to DB ─────────────────────────────────────────────
      const { error: updateError } = await supabase.from('skate_spots').update({
        // Identity enrichment
        is_indoor,
        operator_description,
        // Social
        instagram_url,
        facebook_url,
        tiktok_url,
        schedule_url,
        // Hours & Schedule
        opening_hours,
        adult_night_schedule: adultNightSchedule,
        // Adult Night
        has_adult_night,
        adult_night_details,
        // Events & Pricing
        special_events,
        pricing_data,
        has_fee,
        // Facility attributes (AI extracted)
        surface_type,
        surface_quality,
        vibe_score,
        capacity,
        has_rental,
        has_pro_shop,
        has_food,
        has_lights,
        has_lockers,
        has_ac,
        has_wifi,
        has_toilets,
        is_wheelchair_accessible,
        hosts_derby,
        cultural_metadata,
        // Photos
        ...(candidate_photos ? { candidate_photos } : {}),
        // AI raw dump
        ai_metadata: Object.keys(aiMetadata).length > 0 ? aiMetadata : (target.ai_metadata || null),
        // ✅ Pipeline advance — only reached if qualityScore >= 2
        verification_status: 'DEEP_CRAWLED',
        is_deep_crawled: true,
        retry_count: 0,
        last_attempted_at: new Date().toISOString()
      }).eq('id', target.id);

      if (updateError) {
        console.error('[Indexer] DB write failed after sanitization:', updateError.message);
        // Still advance — raw AI data is in ai_metadata, don't re-queue
        await supabase.from('skate_spots').update({
          verification_status: 'DEEP_CRAWLED',
          is_deep_crawled: true,
          last_attempted_at: new Date().toISOString(),
          ai_metadata: Object.keys(aiMetadata).length > 0 ? aiMetadata : null,
          ...(candidate_photos ? { candidate_photos } : {})
        }).eq('id', target.id);
      }

      const delay = 2000 + Math.random() * 2000;
      reportPulse(delay);
      await sleep(delay);

    } catch (err: any) {
      console.error('[Indexer Error]', err.message);
      const delay = 30000;
      reportPulse(delay);
      await sleep(delay);
    } finally {
      if (browser) {
        try { await (browser as any).close(); } catch (e) {}
      }
    }
  }
}

runIndexer();
