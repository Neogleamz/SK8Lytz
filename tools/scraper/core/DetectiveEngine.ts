/**
 * DetectiveEngine.ts — Pure AI Detective (Deep Crawl + LM Studio Extraction)
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
import pdfParse from 'pdf-parse';

// ─── Re-export shared type sanitizers (used by Indexer thin wrapper too) ─────

export const safeNum = (v: any): number | null => {
  if (v == null) return null;
  const n = Number(v);
  if (!isNaN(n)) return n;
  // Secondary parser: extract leading number from mixed strings like "7 - family-friendly"
  if (typeof v === 'string') {
    const match = v.match(/^\s*(\d+\.?\d*)/);
    if (match) return Number(match[1]);
  }
  return null;
};

export const safeBool = (v: any): boolean | null => {
  if (v == null) return null;
  if (typeof v === 'boolean') return v;
  if (typeof v === 'number') return v !== 0;
  if (typeof v === 'string') {
    const lower = v.toLowerCase().trim();
    // Exact matches first
    if (['yes', 'true', '1', 'y', 'available', 'offered'].includes(lower)) return true;
    if (['no', 'false', '0', 'n', 'none'].includes(lower)) return false;
    if (['null', 'unknown', 'not available', 'n/a', 'not mentioned', 'not specified'].includes(lower)) return null;
    // Affirmative language detection (catches prose like "Wheelchairs are allowed on the rink floor.")
    const AFFIRMATIVE = /\b(yes|allowed|accessible|available|offered|provided|included|compliant|ada|handicap|equipped|open)\b/i;
    const NEGATIVE = /\b(no|not|unavailable|inaccessible|closed|denied|none|lacking|without)\b/i;
    if (AFFIRMATIVE.test(lower) && !NEGATIVE.test(lower)) return true;
    if (NEGATIVE.test(lower)) return false;
    // Ambiguous phrases like "partially accessible" → still true (it IS accessible)
    if (/partial/i.test(lower)) return true;
  }
  return null;
};

/**
 * Attempt to repair truncated JSON from LLM output.
 * Handles unterminated strings, missing closing brackets/braces.
 */
function repairJSON(raw: string): any {
  let s = raw.trim();
  // Strip markdown fences
  s = s.replace(/^```json\s*/i, '').replace(/```\s*$/, '').trim();
  // Try direct parse first
  try { return JSON.parse(s); } catch {}
  // Count brackets/braces
  let braces = 0, brackets = 0, inString = false, escaped = false;
  for (let i = 0; i < s.length; i++) {
    const c = s[i];
    if (escaped) { escaped = false; continue; }
    if (c === '\\') { escaped = true; continue; }
    if (c === '"') { inString = !inString; continue; }
    if (inString) continue;
    if (c === '{') braces++;
    if (c === '}') braces--;
    if (c === '[') brackets++;
    if (c === ']') brackets--;
  }
  // Close unterminated string
  if (inString) s += '"';
  // Close missing brackets/braces
  while (brackets > 0) { s += ']'; brackets--; }
  while (braces > 0) { s += '}'; braces--; }
  try { return JSON.parse(s); } catch { return {}; }
}

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
  aiMetadata: Record<string, any>;
  mappedFields: Record<string, any>;
  combinedText: string;
  qualityScore: number;
  passedQualityGate: boolean;
  candidatePhotos: Record<string, any> | null;
  socialLinks: { instagram_url: string | null; facebook_url: string | null; tiktok_url: string | null; schedule_url: string | null };
  flyerUrls: string[];
}

// ─── Constants & Utilities ───────────────────────────────────────────────────

const MAX_PAGES_PER_RECORD = 8;
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

async function autoScroll(page: any) {
  await page.evaluate(async () => {
    await new Promise((resolve) => {
      let totalHeight = 0;
      const distance = 100;
      const timer = setInterval(() => {
        const scrollHeight = document.body.scrollHeight;
        window.scrollBy(0, distance);
        totalHeight += distance;
        if (totalHeight >= scrollHeight - window.innerHeight) {
          clearInterval(timer);
          resolve(true);
        }
      }, 100);
    });
  });
}

/**
 * Execute the Phase 3 Detective against a spot's candidate_links.
 */
export async function executeDetective(
  spotContext: any,
  aiConfig: any,
  isHeadless: boolean,
  onProgress: (msg: string) => void = () => {}
): Promise<DetectiveResult> {

  let browser: any = null;
  let combinedText = '';
  const allLinks: {href: string, text: string}[] = [];
  let ogImage: string | null = null;
  const domImages: string[] = [];
  const flyerUrls: string[] = [];
  let aiMetadata: Record<string, any> = {};
  // Guard: if no website at all, skip crawling entirely
  if (!spotContext.website) {
    onProgress(`[Detective] ⚠️ No website URL — running AI on metadata only.`);
    combinedText = `Facility name: ${spotContext.name}. Location: ${spotContext.city}, ${spotContext.state}. ` +
      `Type: ${spotContext.facility_type || 'roller rink'}. ` +
      (spotContext.phone ? `Phone: ${spotContext.phone}. ` : '') +
      `Note: No website available for this facility.`;
  } else {

  const socialOnlyRecord = isSocialCrawlBlocked(spotContext.website);

  if (socialOnlyRecord) {
    onProgress(`[Detective] ⚠️ Website is a social media URL (${spotContext.website}) — running AI on metadata only.`);
    combinedText = `Facility name: ${spotContext.name}. Location: ${spotContext.city}, ${spotContext.state}. ` +
      `Type: ${spotContext.facility_type || 'roller rink'}. ` +
      (spotContext.phone ? `Phone: ${spotContext.phone}. ` : '') +
      `Note: No traditional website available.`;
  } else {
    onProgress(`[Detective] 🕷️ Crawling root website: ${spotContext.website}`);
    try {
      browser = await puppeteer.launch({
        headless: isHeadless ? 'new' : false,
        protocolTimeout: 60000,
        args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage']
      });
      const page = await browser.newPage();
      await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36');
      await page.setViewport({ width: 1280, height: 800 });

      // Pass 1 variables
      const crawlQueue = [spotContext.website];
      let passCount = 1;

      while (passCount <= 2) {
        onProgress(`[Detective] 🔄 Beginning Crawl Pass ${passCount}...`);
        
        // Crawl all URLs in queue
        while (crawlQueue.length > 0 && combinedText.length < 50000) {
          const url = crawlQueue.shift()!;
          onProgress(`[Detective] → ${url}`);

          // PDF Trap
          if (url.toLowerCase().endsWith('.pdf')) {
            onProgress(`[Detective] 📄 PDF detected — running pdf-parse: ${url}`);
            try {
              const fetchFn = require('node-fetch');
              const pdfRes = await fetchFn(url);
              const pdfBuffer = await pdfRes.buffer();
              const pdfData = await pdfParse(pdfBuffer);
              combinedText += `\n\n[PDF DOCUMENT: ${url}]\n${pdfData.text}`;
              onProgress(`[Detective] ↳ Extracted ${pdfData.text.length} chars from PDF`);
            } catch (e: any) {
              onProgress(`[Detective] ✗ PDF extraction failed: ${e.message}`);
            }
            continue;
          }

          // Image-Trap
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
          
          // Lazy load sweep
          onProgress(`[Detective] 📜 Auto-scrolling to trigger lazy-loaded media...`);
          await autoScroll(page);

          const isPriorityUrl = url.includes('schedule') || url.includes('pricing') || url.includes('hours') || 
            url.includes('admission') || url.includes('session') || url.includes('skating') || 
            url.includes('public-skating') || url.includes('calendar') || url.includes('events') || 
            url.includes('open-skate') || url.includes('times') || url.includes('rates') || 
            url.includes('info') || url.includes('tickets') || url.includes('booking');

          const pageData = await page.evaluate((isPriority: boolean) => {
            const ogMeta = document.querySelector('meta[property="og:image"]')?.getAttribute('content') || null;
            
            // 1. Extract JSON-LD Business Schema
            const jsonLd = Array.from(document.querySelectorAll('script[type="application/ld+json"]'))
              .map((el: any) => el.innerText).join('\n');

            // 2. Extract iframes before removal
            const iframes = Array.from(document.querySelectorAll('iframe'))
              .map((el: any) => el.src || '')
              .filter((src: string) => src.includes('calendar') || src.includes('ticket') || src.includes('centeredge') || src.includes('roller'));

            // 3. Extract Images & Flag Unrestricted OCR
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
                // Unrestricted OCR logic: if it's a priority page, EVERY large image is a flyer.
                const isFlyer = isPriority || src.includes('schedule') || src.includes('pricing') || src.includes('flyer') || alt.includes('schedule') || alt.includes('pricing');
                return { src, isFlyer };
              });
              
            // 4. Semantic Links
            const links = Array.from(document.querySelectorAll('a'))
              .map((a: any) => ({
                href: (a.href || '').toLowerCase(),
                text: (a.innerText || '').toLowerCase()
              }))
              .filter(l => l.href && (l.href.startsWith('http') || l.href.startsWith('//')));
              
            // 5. Clean DOM
            document.querySelectorAll('nav, footer, script, style, header, iframe, noscript').forEach(el => el.remove());
            const cleanText = document.body.innerText.replace(/\n+/g, ' ').replace(/\s{2,}/g, ' ').trim();
            
            return { ogMeta, imgs, links, cleanText, jsonLd, iframes };
          }, isPriorityUrl).catch(() => ({ ogMeta: null, imgs: [], links: [], cleanText: '', jsonLd: '', iframes: [] }));

          if (pageData.jsonLd) combinedText += `\n\n[HIDDEN JSON-LD SCHEMA: ${url}]\n${pageData.jsonLd}`;
          if (pageData.cleanText) combinedText += `\n\n[PAGE: ${url}]\n${pageData.cleanText}`;
          
          allLinks.push(...pageData.links);
          if (!ogImage && pageData.ogMeta && !pageData.ogMeta.includes('placeholder')) ogImage = pageData.ogMeta;

          const pageImages = pageData.imgs.map((i: any) => i.src);
          domImages.push(...pageImages.slice(0, 2));
          
          const trappedFlyers = pageData.imgs.filter((i: any) => i.isFlyer).map((i: any) => i.src);
          if (trappedFlyers.length > 0) flyerUrls.push(...trappedFlyers);
          
          if (pageData.iframes.length > 0) {
             onProgress(`[Detective] 🎟️ Found ${pageData.iframes.length} ticketing/calendar iframes.`);
             crawlQueue.push(...pageData.iframes); // Scrape the iframes!
          }
        }

        // ── DOM Internal Link Navigation ─────────────────────────────────────────
        let hostname = '';
        try { hostname = new URL(spotContext.website).hostname; } catch { hostname = ''; }
        
        const internalLinks = allLinks.filter((l: any) => {
          if (!l.href || l.href.startsWith('mailto:') || l.href.startsWith('tel:') || l.href.startsWith('javascript:')) return false;
          // Only crawl internal links or ticketing iframes
          try {
            const u = new URL(l.href);
            return u.hostname === hostname || u.hostname.includes(hostname.replace('www.', '')) || l.href.includes('centeredge') || l.href.includes('roller');
          } catch { return false; }
        });

        // Expanded Dictionary
        const PAGE_SCORE_RULES = [
          { pattern: /hours|schedule|session|times|calendar|events|open.?skate/i, score: 10 },
          { pattern: /adult.?night|18\+|21\+/i,                                  score: 10 },
          { pattern: /pricing|price|admission|rates|tickets|booking/i,            score: 9  },
          { pattern: /about|story|history|facility|rink/i,                        score: 8  },
          { pattern: /location|directions|contact|info/i,                         score: 6  },
        ];

        type ScoredLink = { href: string; score: number };
        const scored: ScoredLink[] = [];
        for (const link of internalLinks) {
          let bestScore = 0;
          for (const rule of PAGE_SCORE_RULES) {
            // Semantic scoring: Test both URL and the surrounding <a> text!
            if (rule.pattern.test(link.href) || rule.pattern.test(link.text)) {
              if (rule.score > bestScore) bestScore = rule.score;
            }
          }
          if (bestScore > 0) scored.push({ href: link.href, score: bestScore });
        }

        scored.sort((a, b) => b.score - a.score);
        
        // Pass 1 grabs 3 deep links. Pass 2 grabs 5 additional ones.
        const linksToGrab = passCount === 1 ? 3 : 5;
        const subUrls = [...new Set(scored.map(s => s.href))].slice(0, linksToGrab);
        
        for (const url of subUrls) {
          // Skip if already in text (naively check if URL is in combinedText, though a weak check, it stops duplicate crawls)
          if (combinedText.includes(`[PAGE: ${url}]`)) continue;
          
          onProgress(`[Detective] → Deep Dive: ${url}`);
          try {
            await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 15000 });
            await autoScroll(page);
            await new Promise(r => setTimeout(r, 1000));
            const subData = await page.evaluate(() => {
              // Extract JSON-LD and Unrestricted OCR again for deep dives
              const jsonLd = Array.from(document.querySelectorAll('script[type="application/ld+json"]')).map((el: any) => el.innerText).join('\n');
              const iframes = Array.from(document.querySelectorAll('iframe'))
                  .map((el: any) => el.src || '')
                  .filter((src: string) => src.includes('calendar') || src.includes('ticket') || src.includes('centeredge') || src.includes('roller'));
              const flyers = Array.from(document.querySelectorAll('img')).filter((img: any) => {
                  const w = img.naturalWidth || img.width || 0;
                  const h = img.naturalHeight || img.height || 0;
                  return w >= 400 && h >= 300 && !img.src.includes('logo') && !img.src.includes('icon');
              }).map((img: any) => img.src);
              
              document.querySelectorAll('nav, footer, script, style, header, iframe, noscript').forEach(el => el.remove());
              const cleanText = document.body.innerText.replace(/\n+/g, ' ').replace(/\s{2,}/g, ' ').trim();
              return { cleanText, jsonLd, iframes, flyers };
            }).catch(() => ({ cleanText: '', jsonLd: '', iframes: [], flyers: [] }));
            
            if (subData.jsonLd) combinedText += `\n\n[HIDDEN JSON-LD SCHEMA: ${url}]\n${subData.jsonLd}`;
            if (subData.cleanText) combinedText += `\n\n[PAGE: ${url}]\n${subData.cleanText}`;
            if (subData.iframes.length > 0) crawlQueue.push(...subData.iframes);
            if (subData.flyers.length > 0) flyerUrls.push(...subData.flyers);
          } catch {
            onProgress(`[Detective] ✗ Deep dive failed: ${url}`);
          }
        }

        // OCR Pass
        if (flyerUrls.length > 0) {
          const uniqueFlyers = [...new Set(flyerUrls)].slice(0, 5); // Increased from 3 to 5
          onProgress(`[Detective] 👁️ Trapped ${uniqueFlyers.length} potential flyer images. Running OCR...`);
          for (const fUrl of uniqueFlyers) {
            // Check if already OCR'd
            if (combinedText.includes(`[OCR from Flyer Image: ${fUrl}]`)) continue;
            
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
        
        // ── LM Studio AI Extraction (OpenAI-Compatible) ─────────────────
        const userSystemPrompt = aiConfig.ai_system_prompt || '';
        const userVectors = aiConfig.ai_target_vectors || [];
        const userSchema = userVectors.reduce((acc: any, vec: any) => {
          acc[vec.key] = vec.prompt || vec.type;
          return acc;
        }, {});

        // Built-in comprehensive vector schema — covers ALL 35+ DB fields
        const BUILTIN_VECTORS: Record<string, string> = {
          hours: 'Extract the COMPLETE weekly schedule for public skating sessions as an object mapping days to time ranges. Example: {"Monday":"7pm-10pm","Tuesday":"Closed","Wednesday":"6pm-9pm"}. Ignore private party times.',
          pricing: 'Extract ALL admission prices as an object. Include adult, child, senior, spectator, and skate rental fees. Example: {"adult":"$10","child_under_12":"$7","spectator":"$3","skate_rental":"$5"}',
          surface_type: 'Identify the skating floor material. Valid values: wood, maple, hardwood, concrete, asphalt, sport_court, synthetic. Return null if unknown.',
          surface_quality: 'Assess floor condition from descriptions or reviews (e.g., smooth, well-maintained, slippery, sticky, warped). Return null if not mentioned.',
          vibe_score: 'Rate the facility atmosphere 1-10 based on website tone. 1=rundown, 5=family-friendly, 8=vibrant-community, 10=premium-nightlife.',
          is_indoor: 'Is this facility indoors? Return true, false, or null.',
          has_fee: 'Does the facility charge an admission fee? Return true, false, or null.',
          has_rental: 'Does the facility offer skate rentals? Note if they mention inline vs quad.',
          has_pro_shop: 'Does the facility have an on-site pro shop that sells skates, wheels, or bearings?',
          has_food: 'Does the facility have a snack bar, concession stand, or food service?',
          has_lights: 'Does the facility have special lighting effects (disco lights, LED, blacklight)?',
          has_lockers: 'Does the facility offer lockers or cubbies for personal items?',
          has_ac: 'Is the facility air-conditioned? Return true, false, or null.',
          has_wifi: 'Does the facility offer free WiFi? Return true, false, or null.',
          has_toilets: 'Does the facility have restrooms/bathrooms? Return true, false, or null.',
          is_wheelchair_accessible: 'Is the facility wheelchair accessible or ADA compliant?',
          has_adult_night: 'Does the facility host 18+ or 21+ adult-only skate nights?',
          adult_night_details: 'If adult nights exist, describe them (age requirement, theme, DJ, etc). Return null if no adult night.',
          adult_night_schedule: 'If adult nights exist, map which days they occur. Example: {"Friday":"9pm-12am","Saturday":"10pm-1am"}. Return null if none.',
          hosts_derby: 'Does this facility host roller derby teams or events?',
          capacity: 'What is the maximum occupancy or skater capacity? Return a number or null.',
          special_events: 'List any recurring special events (birthday parties, cosmic skate, DJ nights, etc.) as an array of strings.',
          operator_name: 'Who owns or operates this facility? Return the business or person name, or null.',
          operator_description: 'A 1-2 sentence description of this facility based on website content. Be factual, not promotional.',
          cultural_metadata: 'Any cultural or community significance (historic rink, Black-owned, LGBTQ-friendly, legendary DJ, etc). Return null if none.',
          instagram_url: 'Extract the facility Instagram URL if found. Return full URL or null.',
          facebook_url: 'Extract the facility Facebook URL if found. Return full URL or null.',
          tiktok_url: 'Extract the facility TikTok URL if found. Return full URL or null.',
          schedule_url: 'Extract the URL of the dedicated schedule/hours page if found. Return full URL or null.',
        };

        // Merge: user-config vectors override built-in ones by key
        const mergedSchema = { ...BUILTIN_VECTORS, ...userSchema };

        let systemMessage = '';
        if (spotContext.name && spotContext.city) {
          systemMessage += `You are a data extraction agent for roller skating facilities. You are analyzing website content for [${spotContext.name}] in [${spotContext.city}]. `;
          systemMessage += `The text below may contain data for multiple franchise locations. ONLY extract data for [${spotContext.name}]. Ignore all other cities.\n\n`;
        }

        // Append the user's custom system prompt if one exists in config
        if (userSystemPrompt) {
          systemMessage += userSystemPrompt + '\n\n';
        }

        // Anti-hallucination rules
        systemMessage += `RULES:\n`;
        systemMessage += `1. If you cannot find evidence for a field in the provided text, return null for that field. Do NOT guess or infer.\n`;
        systemMessage += `2. Return ONLY a valid JSON object matching the schema below. No markdown, no code blocks, no commentary.\n`;
        systemMessage += `3. For boolean fields, return true, false, or null (not strings).\n`;
        systemMessage += `4. For URL fields, return the full URL starting with http/https, or null.\n`;
        
        if (aiConfig.ai_exclusion_keywords && aiConfig.ai_exclusion_keywords.length > 0) {
          systemMessage += `5. TOXICITY BOUNCER: If the PRIMARY BUSINESS of this facility is any of the following: [${aiConfig.ai_exclusion_keywords.join(', ')}], you MUST return EXACTLY {"TOXICITY_ABORT": true} and nothing else. However, if they merely mention these items (e.g., selling scooter wheels, banning skateboards), DO NOT abort.\n`;
        }
        
        systemMessage += `\nSCHEMA (extract ALL of these fields):\n${JSON.stringify(mergedSchema, null, 2)}`;

        const userMessage = `Website/Image Text:\n${combinedText.slice(-30000)}`;
        const detectiveModel = aiConfig.detective_model || 'local-model';
        onProgress(`[Detective] 🧠 Invoking LM Studio (${detectiveModel}) - Pass ${passCount}...`);

        if (combinedText.trim().length < 50) {
          onProgress(`[Detective] ⚠️ Content length too short (${combinedText.length} chars). Skipping LM Studio to prevent Bad Request.`);
          break;
        }

        const LM_STUDIO_URL = 'http://localhost:1234/v1/chat/completions';
        const lmPayload = {
          model: detectiveModel,
          messages: [
            { role: 'system', content: systemMessage },
            { role: 'user', content: userMessage }
          ],
          temperature: 0.1,
          stream: false
        };

        // Retry loop: 1 retry with 3s backoff on failure (cold model load, timeout, etc.)
        const MAX_LM_RETRIES = 2;
        for (let attempt = 1; attempt <= MAX_LM_RETRIES; attempt++) {
          try {
            const fetchFn = require('node-fetch');
            const response = await fetchFn(LM_STUDIO_URL, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(lmPayload)
            });
            if (!response.ok) throw new Error(`LM Studio API failed: ${response.statusText}`);
            const aiData = await response.json();
            const contentString = aiData.choices?.[0]?.message?.content || '{}';
            // Primary parse with markdown stripping
            const jsonStr = contentString.replace(/```json/g, '').replace(/```/g, '').trim();
            try {
              aiMetadata = JSON.parse(jsonStr);
            } catch {
              // JSON repair fallback for truncated output
              onProgress(`[Detective] ⚠️ JSON parse failed — attempting repair...`);
              aiMetadata = repairJSON(contentString);
            }
            const fillRate = Object.values(aiMetadata).filter(v => v !== null && v !== undefined).length;
            onProgress(`[Detective] ✓ LM Studio extraction complete (Pass ${passCount}, Attempt ${attempt}). Keys: ${Object.keys(aiMetadata).length}, Fill: ${fillRate}`);
            break; // Success — exit retry loop
          } catch (err: any) {
            onProgress(`[Detective] ✗ LM Studio attempt ${attempt}/${MAX_LM_RETRIES} failed: ${err.message}`);
            if (attempt < MAX_LM_RETRIES) {
              onProgress(`[Detective] ⏳ Retrying in 3s...`);
              await new Promise(r => setTimeout(r, 3000));
            }
          }
        }

        // ── Missing Data Fallback Check ────────────────────────────────
        const hours = aiMetadata.hours || aiMetadata.opening_hours || {};
        const pricing = aiMetadata.pricing || aiMetadata.pricing_data || {};
        
        if (passCount === 1) {
          const hasHours = Object.keys(hours).length > 0;
          const hasPricing = Object.keys(pricing).length > 0;
          if (!hasHours || !hasPricing) {
            onProgress(`[Detective] ⚠️ Missing critical data (Hours: ${hasHours}, Pricing: ${hasPricing}). Triggering DEEP PASS fallback loop.`);
            passCount = 2; // trigger deep pass
            continue;
          } else {
            break; // Stop loop, we got everything!
          }
        } else {
          break; // Done with pass 2
        }
      }

      await browser.close();
      browser = null;

    } finally {
      if (browser) { try { await browser.close(); } catch (_) {} browser = null; }
    }
    }
  } // end: website null guard

  // ── AI Toxicity Bouncer (Smart Evaluation) ──────────────────────────────
  if (aiMetadata.TOXICITY_ABORT === true) {
    onProgress(`[Detective] 🚫 HEALER ABORT: AI determined this facility matches an exclusion keyword primary business.`);
    return {
      aiMetadata: { TOXICITY_ABORT: true }, 
      mappedFields: { _simulated_status: 'REJECTED' }, 
      combinedText,
      qualityScore: 0, passedQualityGate: false, candidatePhotos: null, toxicity_abort: true,
      socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null },
      flyerUrls
    };
  }

  // ── Map AI output → typed DB columns ────────────────────────────────────
  let opening_hours          = aiMetadata.hours            || aiMetadata.opening_hours        || spotContext.opening_hours        || null;
  const pricing_data         = aiMetadata.pricing           || aiMetadata.pricing_data         || spotContext.pricing_data         || null;
  const surface_type         = safeSurface(aiMetadata.surface_type   || spotContext.surface_type);
  const surface_quality      = aiMetadata.surface_quality   || spotContext.surface_quality || null;
  const vibe_score           = safeNum(aiMetadata.vibe_score          ?? spotContext.vibe_score);
  const is_indoor            = safeBool(aiMetadata.is_indoor           ?? spotContext.is_indoor);
  const has_fee              = safeBool(aiMetadata.has_fee             ?? spotContext.has_fee);
  const has_adult_night      = safeBool(aiMetadata.has_adult_night     ?? spotContext.has_adult_night) ?? false;
  let adult_night_details    = aiMetadata.adult_night_details || spotContext.adult_night_details || null;
  let adultNightSchedule     = aiMetadata.adult_night_schedule || spotContext.adult_night_schedule || null;

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
  const operator_name            = aiMetadata.operator_name || spotContext.operator_name || null;

  // ── Social Links & Schedules (Native AI Vectors) ─────────────────────────
  const instagram_url = aiMetadata.instagram_url || spotContext.instagram_url || null;
  const facebook_url  = aiMetadata.facebook_url  || spotContext.facebook_url  || null;
  const tiktok_url    = aiMetadata.tiktok_url    || spotContext.tiktok_url    || null;
  const schedule_url  = aiMetadata.schedule_url  || spotContext.schedule_url  || null;

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
    if (Object.keys(candidateMap).length > 0) {
      candidatePhotos = candidateMap;
      onProgress(`[Detective] 📸 Photo candidates: ${Object.keys(candidateMap).join(', ')}`);
    }
  }

  // ── Google Places Fallback for Hours ─────────────────────────────────────
  // If the site blocked us (403) or the AI failed to extract hours, fall back to Places API
  if ((!opening_hours || Object.keys(opening_hours).length === 0) && spotContext.google_place_id) {
    const MAPS_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_API_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY || '';
    if (MAPS_KEY) {
      try {
        onProgress(`[Detective] 🕒 Hours missing. Fetching fallback from Google Places API...`);
        const fetchFn = require('node-fetch');
        const placesUrl = `https://maps.googleapis.com/maps/api/place/details/json?place_id=${spotContext.google_place_id}&fields=opening_hours&key=${MAPS_KEY}`;
        const placeRes = await fetchFn(placesUrl);
        if (placeRes.ok) {
          const placeData = await placeRes.json();
          if (placeData.result?.opening_hours?.weekday_text) {
            opening_hours = {};
            placeData.result.opening_hours.weekday_text.forEach((dayStr: string) => {
              const [day, ...rest] = dayStr.split(/:\s*/);
              if (day && rest.length > 0) opening_hours[day] = rest.join(':');
            });
            onProgress(`[Detective] ✓ Recovered hours from Google Places API.`);
          }
        }
      } catch (err: any) {
        onProgress(`[Detective] ✗ Google Places fallback failed: ${err.message}`);
      }
    }
  }

  // ── Quality Gate ─────────────────────────────────────────────────────────
  // Full-spectrum quality check: counts ALL AI-extracted fields (28 total)
  const qualityFields = [
    // Schedule & Pricing (high-value)
    opening_hours, pricing_data,
    // Identity
    operator_name, operator_description,
    // Facility attributes
    surface_type, surface_quality, vibe_score, is_indoor, capacity,
    // Amenities (boolean flags)
    has_fee, has_rental, has_pro_shop, has_food, has_lights,
    has_ac, has_lockers, has_toilets, has_wifi, is_wheelchair_accessible,
    // Events & Nightlife
    has_adult_night, adult_night_details, adultNightSchedule, special_events, hosts_derby,
    // Social & Cultural
    instagram_url, facebook_url, tiktok_url, cultural_metadata,
  ];
  const QUALITY_TOTAL = qualityFields.length; // 28
  const qualityScore = qualityFields.filter(f => f !== null && f !== undefined && f !== false).length;
  // Require at least 1 high-value field (hours OR pricing) AND minimum score of 3
  const hasHighValueField = (opening_hours && Object.keys(opening_hours).length > 0) ||
                            (pricing_data && Object.keys(pricing_data).length > 0);
  const passedQualityGate = qualityScore >= 3 && hasHighValueField;

  const hoursFound   = Object.keys(opening_hours || {}).length;
  const pricingFound = Object.keys(pricing_data  || {}).length;
  const eventsFound  = Array.isArray(special_events) ? special_events.length : 0;
  const schedFound   = adultNightSchedule ? Object.keys(adultNightSchedule).length : 0;

  onProgress(`[Detective] 📊 Quality[${qualityScore}/${QUALITY_TOTAL}] Hours[${hoursFound}/7] Pricing[${pricingFound}] AdultNight[${has_adult_night ? 'Y' : 'N'}, ${schedFound}d] Events[${eventsFound}] Socials[IG:${instagram_url ? 'Y' : 'N'} FB:${facebook_url ? 'Y' : 'N'}] Photos[${candidatePhotos ? Object.keys(candidatePhotos).length : 0}]`);
  if (!passedQualityGate) {
    onProgress(`[Detective] ⚠️ STALLED: Quality score ${qualityScore}/${QUALITY_TOTAL}${!hasHighValueField ? ' — missing hours & pricing (high-value gate)' : ''} — insufficient data extracted.`);
  } else {
    onProgress(`[Detective] ✅ Quality gate passed — record would be marked DEEP_CRAWLED in production.`);
  }

  const mappedFields = {
    is_indoor, operator_description, operator_name, instagram_url, facebook_url, tiktok_url, schedule_url,
    opening_hours, adult_night_schedule: adultNightSchedule,
    has_adult_night, adult_night_details, special_events, pricing_data, has_fee,
    surface_type, surface_quality, vibe_score, capacity, has_rental, has_pro_shop, has_food,
    has_lights, has_lockers, has_ac, has_wifi, has_toilets, is_wheelchair_accessible, hosts_derby,
    cultural_metadata, candidate_photos: candidatePhotos,
    ai_metadata: Object.keys(aiMetadata).length > 0 ? aiMetadata : null,
    _simulated_status: passedQualityGate ? 'DEEP_CRAWLED' : 'STALLED',
  };

  return {
    aiMetadata, mappedFields, combinedText, qualityScore, passedQualityGate,
    candidatePhotos, socialLinks: { instagram_url, facebook_url, tiktok_url, schedule_url },
    flyerUrls,
  };
}
