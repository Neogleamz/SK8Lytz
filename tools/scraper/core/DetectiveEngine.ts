/**
 * DetectiveEngine.ts — Pure AI Detective (Deep Crawl + Ollama Extraction)
 *
 * Contains the full Phase 3 (Indexer) deep-crawl and AI extraction logic extracted
 * into a stateless, database-free module. The production Indexer.ts daemon calls
 * executeDetective() and then performs its own DB writes. The Sniper Bench calls
 * executeDetective() and streams the result via SSE without touching the database.
 *
 * ⚠️  ZERO DB WRITES IN THIS FILE — it is a pure function.
 */

import puppeteer from 'puppeteer';
import Tesseract from 'tesseract.js';

// ─── Re-export shared type sanitizers (used by Indexer thin wrapper too) ─────

/** Parse a number from AI output. Returns null if unparseable. */
export const safeNum = (v: any): number | null => {
  if (v == null) return null;
  const n = Number(v);
  return isNaN(n) ? null : n;
};

/** Parse a boolean from AI output. Returns null for ambiguous free-text. */
export const safeBool = (v: any): boolean | null => {
  if (v == null) return null;
  if (typeof v === 'boolean') return v;
  if (typeof v === 'string') {
    const lower = v.toLowerCase().trim();
    if (['yes', 'true', '1', 'y', 'available', 'offered'].includes(lower)) return true;
    if (['no', 'false', '0', 'n'].includes(lower)) return false;
    if (['null', 'none', 'unknown', 'not available', 'n/a'].includes(lower)) return null;
  }
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
export const safeSurface = (v: any): string | null => {
  if (!v || typeof v !== 'string') return null;
  const lower = v.toLowerCase().trim();
  for (const [keyword, enumVal] of SURFACE_KEYWORD_MAP) {
    if (lower.includes(keyword)) return enumVal;
  }
  return 'unknown';
};

// ─── Types ───────────────────────────────────────────────────────────────────

export interface DetectiveResult {
  /** Raw AI extraction output (the full JSON the LLM returned) */
  aiMetadata: Record<string, any>;
  /** All mapped, sanitized DB-ready fields */
  mappedFields: Record<string, any>;
  /** Combined page text that was fed to the AI */
  combinedText: string;
  /** Quality score (0-17) — how many fields the AI successfully extracted */
  qualityScore: number;
  /** Whether the record passed the quality gate (>= 2) */
  passedQualityGate: boolean;
  /** Photo candidates discovered during crawl */
  candidatePhotos: Record<string, any> | null;
  /** Social links discovered during crawl */
  socialLinks: { instagram_url: string | null; facebook_url: string | null; tiktok_url: string | null; schedule_url: string | null };
  /** Flyer image URLs that were OCR'd */
  flyerUrls: string[];
}

// ─── Constants ───────────────────────────────────────────────────────────────
const MAX_PAGES_PER_RECORD = 4;
const PAGE_PRIORITY = ['hours', 'adult', 'pricing', 'events', 'contact', 'about', 'root'];
const SOCIAL_CRAWL_BLOCKLIST = [
  'facebook.com', 'instagram.com', 'twitter.com', 'x.com',
  'tiktok.com', 'youtube.com', 'yelp.com', 'google.com', 'linkedin.com'
];

function isSocialCrawlBlocked(url: string): boolean {
  try {
    const h = new URL(url).hostname.replace('www.', '');
    return SOCIAL_CRAWL_BLOCKLIST.some(d => h === d || h.endsWith('.' + d));
  } catch { return false; }
}

/**
 * Execute the Phase 3 Detective against a spot's candidate_links.
 *
 * @param spotContext   - The spot record from the DB (name, city, state, website, etc.)
 * @param candidateLinks - The candidate_links JSONB map produced by the Spider.
 * @param aiConfig      - The global AI config fetched from CCTower /config.
 * @param isHeadless    - Whether to launch Puppeteer headless.
 * @param onProgress    - Callback that receives log lines in real-time (for SSE streaming).
 */
export async function executeDetective(
  spotContext: any,
  candidateLinks: Record<string, string>,
  aiConfig: any,
  isHeadless: boolean,
  onProgress: (msg: string) => void = () => {}
): Promise<DetectiveResult> {

  let browser: any = null;
  let combinedText = '';
  const allLinks: string[] = [];
  let ogImage: string | null = null;
  const domImages: string[] = [];
  const flyerUrls: string[] = [];

  // ── Build ordered list of URLs to visit ──────────────────────────────────
  const urlsToVisit: string[] = [];
  for (const key of PAGE_PRIORITY) {
    if (candidateLinks[key] && !urlsToVisit.includes(candidateLinks[key])) {
      urlsToVisit.push(candidateLinks[key]);
      if (urlsToVisit.length >= MAX_PAGES_PER_RECORD) break;
    }
  }
  if (urlsToVisit.length === 0 && spotContext.website) {
    urlsToVisit.push(spotContext.website);
  }

  const filteredUrls = urlsToVisit.filter(u => !isSocialCrawlBlocked(u));
  const socialOnlyRecord = filteredUrls.length === 0;

  if (socialOnlyRecord) {
    onProgress('[Detective] ⚠️ No crawlable URLs (social-only record) — running AI on metadata only.');
    combinedText = `Facility name: ${spotContext.name}. Location: ${spotContext.city}, ${spotContext.state}. ` +
      `Type: ${spotContext.facility_type || 'roller rink'}. ` +
      (spotContext.phone ? `Phone: ${spotContext.phone}. ` : '') +
      (spotContext.facebook_url ? `Facebook: ${spotContext.facebook_url}. ` : '') +
      `Note: No website available — extract what you can from facility type and name.`;
  } else {
    onProgress(`[Detective] 🕷️ Visiting ${filteredUrls.length} candidate pages...`);
    try {
      browser = await puppeteer.launch({
        headless: isHeadless ? 'new' : false,
        protocolTimeout: 60000,
        args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
      });
      const page = await browser.newPage();
      await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36');
      await page.setViewport({ width: 1280, height: 800 });

      for (const url of filteredUrls) {
        onProgress(`[Detective] → ${url}`);

        // Image-Trap Short-Circuit
        const isDirectImageUrl = /\.(png|jpg|jpeg|gif|webp)(\?.*)?$/i.test(url);
        if (isDirectImageUrl) {
          onProgress(`[Detective] 👁️ Direct image URL detected — running Tesseract: ${url}`);
          try {
            const { data: { text } } = await Tesseract.recognize(url, 'eng');
            if (text && text.trim().length > 20) {
              combinedText += `\n\n[OCR from Direct Image: ${url}]\n${text}`;
              onProgress(`[Detective] ↳ OCR extracted ${text.length} chars from direct image`);
            } else {
              onProgress(`[Detective] ↳ OCR yielded no usable text from ${url}`);
            }
          } catch (ocrErr: any) {
            onProgress(`[Detective] ✗ Direct image OCR failed: ${ocrErr.message}`);
          }
          continue;
        }

        try {
          await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 30000 });
        } catch {
          try {
            await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 15000 });
          } catch {
            onProgress(`[Detective] ✗ Navigation failed: ${url}`);
            continue;
          }
        }
        await new Promise(r => setTimeout(r, 1000));

        const isPriorityUrl = url.includes('schedule') || url.includes('pricing') || url.includes('hours') || url.includes('admission');
        const pageData = await page.evaluate((isPriority: boolean) => {
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
            .map((img: any) => {
              const src = img.src;
              const alt = (img.alt || '').toLowerCase();
              const isFlyer = isPriority || src.includes('schedule') || src.includes('pricing') || src.includes('flyer') || alt.includes('schedule') || alt.includes('pricing');
              return { src, isFlyer };
            });
          const links = Array.from(document.querySelectorAll('a'))
            .map((a: any) => (a.href || '').toLowerCase())
            .filter(Boolean);
          document.querySelectorAll('nav, footer, script, style, header, iframe, noscript').forEach(el => el.remove());
          const cleanText = document.body.innerText.replace(/\n+/g, ' ').replace(/\s{2,}/g, ' ').trim();
          return { ogMeta, imgs, links, cleanText };
        }, isPriorityUrl).catch(() => ({ ogMeta: null, imgs: [], links: [], cleanText: '' }));

        if (pageData.cleanText) combinedText += `\n\n[PAGE: ${url}]\n${pageData.cleanText}`;
        allLinks.push(...pageData.links);
        if (!ogImage && pageData.ogMeta && !pageData.ogMeta.includes('placeholder')) ogImage = pageData.ogMeta;

        const pageImages = pageData.imgs.map((i: any) => i.src);
        domImages.push(...pageImages.slice(0, 2));
        const trappedFlyers = pageData.imgs.filter((i: any) => i.isFlyer).map((i: any) => i.src);
        if (trappedFlyers.length > 0) flyerUrls.push(...trappedFlyers);
      }

      await browser.close();
      browser = null;

      // OCR Pass
      if (flyerUrls.length > 0) {
        const uniqueFlyers = [...new Set(flyerUrls)].slice(0, 3);
        onProgress(`[Detective] 👁️ Trapped ${uniqueFlyers.length} potential flyer images. Running OCR...`);
        for (const fUrl of uniqueFlyers) {
          try {
            const { data: { text } } = await Tesseract.recognize(fUrl, 'eng');
            if (text && text.length > 20) {
              combinedText += `\n\n[OCR from Flyer Image: ${fUrl}]\n${text}`;
              onProgress(`[Detective] ↳ OCR extracted ${text.length} chars from ${fUrl}`);
            }
          } catch (err: any) {
            onProgress(`[Detective] ✗ OCR failed for ${fUrl}: ${err.message}`);
          }
        }
      }
    } finally {
      if (browser) { try { await browser.close(); } catch (_) {} browser = null; }
    }
  }

  // ── AI Toxicity Bouncer ──────────────────────────────────────────────────
  const exclusions = aiConfig.ai_exclusion_keywords || [];
  const toxicityReason = exclusions.find((kw: string) =>
    combinedText.toLowerCase().includes(kw.toLowerCase())
  );
  if (toxicityReason) {
    onProgress(`[Detective] 🚫 HEALER ABORT: Exclusion keyword [${toxicityReason}] found in content.`);
    const emptyResult: DetectiveResult = {
      aiMetadata: {}, mappedFields: {}, combinedText,
      qualityScore: 0, passedQualityGate: false, candidatePhotos: null,
      socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null },
      flyerUrls
    };
    return emptyResult;
  }

  // ── Llama-3 Detective Extraction ─────────────────────────────────────────
  const systemPrompt = aiConfig.ai_system_prompt || 'Extract JSON data accurately.';
  const targetVectors = aiConfig.ai_target_vectors || [];
  const schema = targetVectors.reduce((acc: any, vec: any) => {
    acc[vec.key] = vec.prompt || vec.type;
    return acc;
  }, {});

  let contextHeader = '';
  if (spotContext.name && spotContext.city) {
    contextHeader = `You are analyzing website content for a skate facility. The specific location is [${spotContext.name}] in [${spotContext.city}]. The text below may contain info for multiple franchise locations. ONLY extract data for [${spotContext.name}]. Ignore all other cities.\n\n`;
  }

  const prompt = `${contextHeader}${systemPrompt}\n\nSchema:\n${JSON.stringify(schema, null, 2)}\n\nWebsite Text:\n${combinedText.slice(0, 25000)}`;
  const detectiveModel = aiConfig.detective_model || 'llama3.1';
  onProgress(`[Detective] 🧠 Invoking Ollama (${detectiveModel})...`);

  let aiMetadata: Record<string, any> = {};
  try {
    // eslint-disable-next-line @typescript-eslint/no-var-requires
    const fetchFn = require('node-fetch');
    const response = await fetchFn('http://localhost:11434/api/generate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ model: detectiveModel, prompt, format: 'json', stream: false, options: { num_ctx: 8192 } })
    });
    if (!response.ok) throw new Error('Ollama HTTP error');
    const aiData = await response.json();
    aiMetadata = JSON.parse(aiData.response);
    onProgress('[Detective] ✓ Ollama extraction complete.');
  } catch (err: any) {
    onProgress(`[Detective] ✗ Ollama extraction failed: ${err.message}`);
  }

  // ── Map AI output → typed DB columns ────────────────────────────────────
  const opening_hours        = aiMetadata.hours            || aiMetadata.opening_hours        || spotContext.opening_hours        || null;
  const pricing_data         = aiMetadata.pricing           || aiMetadata.pricing_data         || spotContext.pricing_data         || null;
  const surface_type         = safeSurface(aiMetadata.surface_type   || spotContext.surface_type);
  const surface_quality      = aiMetadata.surface_quality   || spotContext.surface_quality || null;
  const vibe_score           = safeNum(aiMetadata.vibe_score          ?? spotContext.vibe_score);
  const is_indoor            = safeBool(aiMetadata.is_indoor           ?? spotContext.is_indoor);
  const has_fee              = safeBool(aiMetadata.has_fee             ?? spotContext.has_fee);
  const has_adult_night      = safeBool(aiMetadata.has_adult_night     ?? spotContext.has_adult_night) ?? false;
  let adult_night_details    = aiMetadata.adult_night_details || spotContext.adult_night_details || null;
  let adultNightSchedule     = aiMetadata.adult_night_schedule || spotContext.adult_night_schedule || null;

  // Anti-hallucination guard: Nullify details if AI says adult night doesn't exist
  if (has_adult_night === false) {
    adult_night_details = null;
    adultNightSchedule = null;
  }

  const special_events       = aiMetadata.special_events      || spotContext.special_events      || null;
  const rawCapacity          = aiMetadata.capacity ?? spotContext.capacity;
  const capacity             = rawCapacity != null && !isNaN(parseInt(String(rawCapacity))) ? parseInt(String(rawCapacity)) : null;
  const has_rental           = safeBool(aiMetadata.has_rental          ?? spotContext.has_rental);
  const has_pro_shop         = safeBool(aiMetadata.has_pro_shop        ?? spotContext.has_pro_shop);
  const has_food             = safeBool(aiMetadata.has_food            ?? spotContext.has_food);
  const has_lights           = safeBool(aiMetadata.has_lights          ?? spotContext.has_lights);
  const has_lockers          = safeBool(aiMetadata.has_lockers         ?? spotContext.has_lockers);
  const has_ac               = safeBool(aiMetadata.has_ac              ?? spotContext.has_ac);
  const has_wifi             = safeBool(aiMetadata.has_wifi            ?? spotContext.has_wifi);
  const has_toilets          = safeBool(aiMetadata.has_toilets         ?? spotContext.has_toilets);
  const is_wheelchair_accessible = safeBool(aiMetadata.wheelchair ?? aiMetadata.is_wheelchair_accessible ?? spotContext.is_wheelchair_accessible);
  const hosts_derby              = safeBool(aiMetadata.derby    ?? aiMetadata.hosts_derby              ?? spotContext.hosts_derby);
  const cultural_metadata        = aiMetadata.cultural_meta    || aiMetadata.cultural_metadata         || spotContext.cultural_metadata || null;
  const operator_description     = aiMetadata.operator_description || spotContext.operator_description  || null;

  // ── Social Links (from collected hrefs) ──────────────────────────────────
  let instagram_url = spotContext.instagram_url || null;
  let facebook_url  = spotContext.facebook_url  || null;
  let tiktok_url    = spotContext.tiktok_url    || null;
  let schedule_url  = spotContext.schedule_url  || null;

  for (const href of allLinks) {
    if (!instagram_url && href.includes('instagram.com') && !href.includes('/explore') && !href.includes('/p/')) instagram_url = href;
    if (!facebook_url  && href.includes('facebook.com')  && !href.includes('sharer')) facebook_url = href;
    if (!tiktok_url    && href.includes('tiktok.com')    && !href.includes('music'))  tiktok_url = href;
    if (!schedule_url  && href.includes('.pdf') && (href.includes('sched') || href.includes('hours') || href.includes('calendar'))) schedule_url = href;
    if (!schedule_url  && (href.includes('/schedule') || href.includes('/calendar') || href.includes('/hours'))) schedule_url = href;
  }

  // ── Photo Candidates ─────────────────────────────────────────────────────
  let candidatePhotos: Record<string, any> | null = spotContext.candidate_photos || null;
  if (!spotContext.photos && !candidatePhotos) {
    const candidateMap: Record<string, any> = {};
    if (ogImage) candidateMap.og_image = ogImage;
    if (domImages.length > 0) candidateMap.dom_images = [...new Set(domImages)].slice(0, 3);
    const MAPS_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY || '';
    if (spotContext.lat && spotContext.lng && MAPS_KEY) {
      candidateMap.street_view_url = `https://maps.googleapis.com/maps/api/streetview?size=800x600&location=${spotContext.lat},${spotContext.lng}&heading=auto&fov=80&key=${MAPS_KEY}`;
    }
    if (facebook_url && facebook_url.includes('facebook.com')) {
      try {
        const fetchFn = require('node-fetch');
        const fbRes = await fetchFn(facebook_url, { headers: { 'User-Agent': 'facebookexternalhit/1.1' }, signal: AbortSignal.timeout(5000) });
        const fbHtml = await fbRes.text();
        const ogMatch = fbHtml.match(/<meta[^>]+property=["']og:image["'][^>]+content=["']([^"']+)["']/);
        if (ogMatch?.[1]) candidateMap.facebook_og = ogMatch[1];
      } catch { /* non-critical */ }
    }
    if (Object.keys(candidateMap).length > 0) {
      candidatePhotos = candidateMap;
      onProgress(`[Detective] 📸 Photo candidates: ${Object.keys(candidateMap).join(', ')}`);
    }
  }

  // ── Quality Gate ─────────────────────────────────────────────────────────
  const qualityFields = [
    opening_hours, pricing_data, surface_type, vibe_score,
    has_fee, has_rental, has_pro_shop, has_food, has_lights,
    has_ac, has_lockers, has_toilets, has_wifi,
    has_adult_night, special_events, operator_description, cultural_metadata
  ];
  const qualityScore = qualityFields.filter(f => f !== null && f !== undefined && f !== false).length;
  const passedQualityGate = qualityScore >= 2;

  const hoursFound   = Object.keys(opening_hours || {}).length;
  const pricingFound = Object.keys(pricing_data  || {}).length;
  const eventsFound  = Array.isArray(special_events) ? special_events.length : 0;
  const schedFound   = adultNightSchedule ? Object.keys(adultNightSchedule).length : 0;

  onProgress(`[Detective] 📊 Quality[${qualityScore}/17] Hours[${hoursFound}/7] Pricing[${pricingFound}] AdultNight[${has_adult_night ? 'Y' : 'N'}, ${schedFound}d] Events[${eventsFound}] Socials[IG:${instagram_url ? 'Y' : 'N'} FB:${facebook_url ? 'Y' : 'N'}] Photos[${candidatePhotos ? Object.keys(candidatePhotos).length : 0}]`);
  if (!passedQualityGate) {
    onProgress(`[Detective] ⚠️ Quality score ${qualityScore}/17 — below gate threshold of 2. Record would be STALLED in production.`);
  } else {
    onProgress(`[Detective] ✅ Quality gate passed — record would be marked DEEP_CRAWLED in production.`);
  }

  const mappedFields = {
    is_indoor, operator_description, instagram_url, facebook_url, tiktok_url, schedule_url,
    opening_hours, adult_night_schedule: adultNightSchedule,
    has_adult_night, adult_night_details, special_events, pricing_data, has_fee,
    surface_type, surface_quality, vibe_score, capacity, has_rental, has_pro_shop, has_food,
    has_lights, has_lockers, has_ac, has_wifi, has_toilets, is_wheelchair_accessible, hosts_derby,
    cultural_metadata, candidate_photos: candidatePhotos,
    ai_metadata: Object.keys(aiMetadata).length > 0 ? aiMetadata : null,
    // What the status *would be* if written to DB
    _simulated_status: passedQualityGate ? 'DEEP_CRAWLED' : 'STALLED',
  };

  return {
    aiMetadata,
    mappedFields,
    combinedText,
    qualityScore,
    passedQualityGate,
    candidatePhotos,
    socialLinks: { instagram_url, facebook_url, tiktok_url, schedule_url },
    flyerUrls,
  };
}
