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

// ─── Extraction Engine ─────────────────────────────────────────────────────

/**
 * Parses a flat body text for structured Mon–Sun hours.
 * Returns a partial record; only fills days not already populated.
 */
function parseHoursFromText(text: string): Partial<Record<DayKey, string>> {
  const hours: Partial<Record<DayKey, string>> = {};
  for (const day of DAYS) {
    // Match: "Monday 4:00 PM - 9:00 PM", "Mon: 4pm-9pm", "Monday: Closed"
    const regex = new RegExp(
      `\\b${day.substring(0, 3)}(?:${day.substring(3)})?[:\\s.]+([\\d]{1,2}(?::[0-9]{2})?\\s*(?:am|pm)[\\s\\-–to]+[\\d]{1,2}(?::[0-9]{2})?\\s*(?:am|pm)|closed|open 24 hours?)`,
      'i'
    );
    const match = regex.exec(text);
    if (match) hours[day] = match[1].trim().toLowerCase();
  }
  return hours;
}

/**
 * Parses <table> and <dl> elements in the DOM for structured hours.
 * Returns stringified JSON to pass back through page.evaluate().
 */
function buildTableHoursScript(): string {
  return `
    (() => {
      const hours = {};
      const dayMap = {
        mon: 'monday', tue: 'tuesday', wed: 'wednesday', thu: 'thursday',
        fri: 'friday', sat: 'saturday', sun: 'sunday',
        monday: 'monday', tuesday: 'tuesday', wednesday: 'wednesday',
        thursday: 'thursday', friday: 'friday', saturday: 'saturday', sunday: 'sunday'
      };
      const allDays = ['monday','tuesday','wednesday','thursday','friday','saturday','sunday'];

      // Strategy 1: <table> rows — col 0 = day, col 1 = hours
      document.querySelectorAll('table tr').forEach(row => {
        const cells = Array.from(row.querySelectorAll('td, th')).map(c => c.innerText.trim().toLowerCase());
        if (cells.length >= 2) {
          const dayKey = Object.keys(dayMap).find(k => cells[0].startsWith(k));
          if (dayKey && !hours[dayMap[dayKey]]) {
            hours[dayMap[dayKey]] = cells[1];
          }
        }
      });

      // Strategy 2: <dl> format — <dt>Monday</dt><dd>4pm-9pm</dd>
      document.querySelectorAll('dl').forEach(dl => {
        const dts = Array.from(dl.querySelectorAll('dt'));
        const dds = Array.from(dl.querySelectorAll('dd'));
        dts.forEach((dt, i) => {
          const txt = dt.innerText.trim().toLowerCase();
          const dayKey = Object.keys(dayMap).find(k => txt.startsWith(k));
          if (dayKey && dds[i] && !hours[dayMap[dayKey]]) {
            hours[dayMap[dayKey]] = dds[i].innerText.trim().toLowerCase();
          }
        });
      });

      // Strategy 3: Google-style spans with aria-label or data-day
      document.querySelectorAll('[aria-label]').forEach(el => {
        const label = el.getAttribute('aria-label')?.toLowerCase() || '';
        const dayKey = Object.keys(dayMap).find(k => label.startsWith(k));
        if (dayKey && !hours[dayMap[dayKey]]) {
          hours[dayMap[dayKey]] = el.innerText.trim().toLowerCase();
        }
      });

      return JSON.stringify(hours);
    })()
  `;
}

/**
 * Detects which specific days have adult/18+ night sessions.
 * Returns a map of day → time string.
 */
function parseAdultNightSchedule(text: string): Partial<Record<DayKey, string>> | null {
  const schedule: Partial<Record<DayKey, string>> = {};

  // Look for adult night mentions near day names
  // e.g. "Friday Adult Night 9pm-midnight", "18+ Saturdays 10pm-1am"
  const adultNightRegex = new RegExp(
    `(${DAYS.map(d => d.substring(0, 3)).join('|')}${DAYS.map(d => d).join('|')})` +
    `.{0,40}` +
    `(?:18\\s*\\+|21\\s*\\+|adult[s]?|18 and older).{0,60}` +
    `([\\d]{1,2}(?::[0-9]{2})?\\s*(?:am|pm).{0,20}[\\d]{1,2}(?::[0-9]{2})?\\s*(?:am|pm|midnight|noon))`,
    'gi'
  );

  // Reverse pattern: "Adult Night every Friday 9pm-11pm"
  const reverseRegex = new RegExp(
    `(?:18\\s*\\+|21\\s*\\+|adult[s]?\\s+night).{0,60}` +
    `(${DAYS.join('|')}).{0,40}` +
    `([\\d]{1,2}(?::[0-9]{2})?\\s*(?:am|pm).{0,20}[\\d]{1,2}(?::[0-9]{2})?\\s*(?:am|pm|midnight|noon))`,
    'gi'
  );

  const dayMap: Record<string, DayKey> = {
    mon: 'monday', tue: 'tuesday', wed: 'wednesday', thu: 'thursday',
    fri: 'friday', sat: 'saturday', sun: 'sunday',
    monday: 'monday', tuesday: 'tuesday', wednesday: 'wednesday',
    thursday: 'thursday', friday: 'friday', saturday: 'saturday', sunday: 'sunday'
  };

  let m: RegExpExecArray | null;
  while ((m = adultNightRegex.exec(text)) !== null) {
    const dayKey = Object.keys(dayMap).find(k => m![1].toLowerCase().startsWith(k));
    if (dayKey) schedule[dayMap[dayKey]] = m[2].trim();
  }
  while ((m = reverseRegex.exec(text)) !== null) {
    const dayKey = Object.keys(dayMap).find(k => m![1].toLowerCase().startsWith(k));
    if (dayKey && !schedule[dayMap[dayKey]]) schedule[dayMap[dayKey]] = m[2].trim();
  }

  return Object.keys(schedule).length > 0 ? schedule : null;
}

/**
 * Extracts recurring special events from body text.
 * Returns an array of event description strings.
 */
function parseSpecialEvents(text: string): string[] | null {
  const events: string[] = [];
  const eventPatterns = [
    // "Cosmic/Galaxy/Glow skate every Friday"
    /(?:cosmic|galaxy|glow|neon|blacklight|disco|80s|theme|retro|holiday)\s+(?:skate|night|session|skating).{0,60}(?:every|each)?\s+(?:monday|tuesday|wednesday|thursday|friday|saturday|sunday)/gi,
    // "Every Friday: Cosmic Skate"
    /every\s+(?:monday|tuesday|wednesday|thursday|friday|saturday|sunday)[,:\s]+.{5,60}?(?:skate|rink|night|session)/gi,
    // "Birthday parties available"
    /birthday\s+(?:parties|party)\s+(?:available|offered|welcome|packages?)/gi,
    // "Private party rental"
    /private\s+(?:party|event|rental|reservation)\s+(?:available|offered|packages?)/gi,
    // "Family skate night"
    /family\s+(?:skate|skating)\s+(?:night|session|day)/gi,
    // "Couples skate"
    /couples?\s+skate/gi,
    // "Roller derby"
    /roller\s+derby/gi,
  ];

  for (const pattern of eventPatterns) {
    let m: RegExpExecArray | null;
    while ((m = pattern.exec(text)) !== null) {
      const snippet = m[0].trim().replace(/\s+/g, ' ');
      if (!events.some(e => e.toLowerCase().includes(snippet.toLowerCase().substring(0, 20)))) {
        events.push(snippet);
      }
    }
  }

  return events.length > 0 ? events : null;
}

/**
 * Extracts structured pricing from body text.
 * Returns an object or null.
 */
function parsePricing(text: string): Record<string, string> | null {
  const pricing: Record<string, string> = {};

  const patterns: [string, RegExp][] = [
    ['admission', /(?:admission|general|entry|skate fee)[:\s]+\$?([\d.]+)/gi],
    ['child_admission', /(?:child|children|kids?)\s+(?:admission|entry|skate)[:\s]+\$?([\d.]+)/gi],
    ['skate_rental', /(?:skate|blade|boot)\s+rental[:\s]+\$?([\d.]+)/gi],
    ['adult_admission', /adult\s+(?:admission|entry|skate)[:\s]+\$?([\d.]+)/gi],
    ['helmet_rental', /helmet\s+rental[:\s]+\$?([\d.]+)/gi],
  ];

  for (const [key, regex] of patterns) {
    const m = regex.exec(text);
    if (m) pricing[key] = `$${m[1]}`;
  }

  // Fallback: grab any price mentions near "admission" or "skate"
  if (Object.keys(pricing).length === 0) {
    const fallback = /(?:admission|skate rental|price|entry).{0,60}\$(\d+(?:\.\d{2})?)/gi;
    let m: RegExpExecArray | null;
    let count = 0;
    while ((m = fallback.exec(text)) !== null && count < 5) {
      pricing[`price_${count++}`] = `$${m[1]}`;
    }
  }

  return Object.keys(pricing).length > 0 ? pricing : null;
}

// ─── Main Indexer Loop ──────────────────────────────────────────────────────

async function runIndexer() {
  console.log('[Indexer v2] 🕵️ Booting Detective — Hours/Pricing/Events Engine...');

  while (true) {
    try {
      const { data: spots, error: rpcError } = await supabase.rpc('get_next_spot_for_indexer');
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

      // ── Fetch current headless setting ───────────────────────────────────
      const statusRes = await fetch('http://localhost:5999/status')
        .then(r => r.json())
        .catch(() => ({ isHeadless: true }));

      const identity = GHOST.generateIdentity();

      const browser = await puppeteer.launch({
        headless: statusRes.isHeadless ? 'new' : false,
        args: ['--no-sandbox', '--disable-setuid-sandbox']
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

      // ── DOM Extraction ────────────────────────────────────────────────────
      const pageData = await page.evaluate(() => {
        const anchors = Array.from(document.querySelectorAll('a'));
        return {
          links: anchors.map(a => a.href.toLowerCase()).filter(Boolean),
          bodyText: document.body.innerText.replace(/\n+/g, ' ').replace(/\s{2,}/g, ' ')
        };
      });

      // Strategy: parse table-structured hours from DOM
      let tableHours: Partial<Record<DayKey, string>> = {};
      try {
        const tableHoursJson = await page.evaluate(new Function(`return ${buildTableHoursScript()}`) as any);
        tableHours = JSON.parse(tableHoursJson as string) || {};
      } catch { /* DOM may not have table hours */ }

      await browser.close();

      const text = pageData.bodyText;

      // ── Social Links ──────────────────────────────────────────────────────
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
        // PDF schedule → store as schedule_url
        if (!schedule_url && href.includes('.pdf') && (href.includes('sched') || href.includes('hours') || href.includes('calendar'))) {
          schedule_url = href;
        }
        if (!schedule_url && (href.includes('/schedule') || href.includes('/calendar') || href.includes('/hours'))) {
          schedule_url = href;
        }
      });

      // ── Hours: Table parse (primary) → Text regex (fallback) ─────────────
      // Only fill hours the Google Places data didn't already provide
      const existingHours: Partial<Record<DayKey, string>> = target.opening_hours || {};
      const textHours = parseHoursFromText(text);

      const mergedHours: Partial<Record<DayKey, string>> = { ...textHours, ...tableHours };
      const finalHours: Partial<Record<DayKey, string>> = {};

      for (const day of DAYS) {
        // Google/existing data takes priority; fill gaps from website
        if (existingHours[day]) {
          finalHours[day] = existingHours[day];
        } else if (mergedHours[day]) {
          finalHours[day] = mergedHours[day];
        }
      }

      const opening_hours = Object.keys(finalHours).length > 0 ? finalHours : (target.opening_hours || null);

      // ── Adult Night: Detect presence and per-day schedule ─────────────────
      const adultNightSchedule = parseAdultNightSchedule(text);
      let has_adult_night = target.has_adult_night || false;
      let adult_night_details = target.adult_night_details || null;

      // Simple presence check (fallback)
      const adultRegex = /(\b18\s*\+|\b18\s*and\s*older|\b21\s*\+|\b(?:adult|adults)\s*(?:skate|only|night))/gi;
      const adultMatch = adultRegex.exec(text);
      if (adultMatch || adultNightSchedule) {
        has_adult_night = true;
        if (!adult_night_details && adultMatch) {
          const ctxStart = Math.max(0, adultMatch.index - 30);
          adult_night_details = text.substring(ctxStart, adultMatch.index + 100).trim();
        }
      }

      // ── Special Events ────────────────────────────────────────────────────
      const newEvents = parseSpecialEvents(text);
      const existingEvents: string[] = target.special_events || [];
      const special_events = newEvents
        ? [...new Set([...existingEvents, ...newEvents])]
        : (existingEvents.length > 0 ? existingEvents : null);

      // ── Pricing ───────────────────────────────────────────────────────────
      const pricing_data = parsePricing(text) || target.pricing_data || null;

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
      const eventsFound = newEvents?.length || 0;
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
