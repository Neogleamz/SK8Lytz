/**
 * DetectiveEngine.ts — Phase 2 AI Detective (Sitemap-First, Multi-Source)
 * v2 Refactor: sitemap navigation + Yelp/Facebook/Google pre-crawl + tiered LLM extraction
 * ZERO DB WRITES — pure function called by Indexer.ts and SniperBench.
 */

import http from 'http';
import puppeteer from 'puppeteer';
import Tesseract from 'tesseract.js';
// Bypass Bun ESM resolution bug for pdf-parse
const pdfParse = require('pdf-parse');
import { parseSitemap } from './SitemapParser';
import { HeuristicsEngine } from './HeuristicsEngine';
import { getFieldRegistry } from './LocalDB';

// ─── Shared Sanitizers (re-exported for Indexer) ─────────────────────────────

export const safeNum = (v: any): number | null => {
  if (v == null) return null;
  const n = Number(v);
  if (!isNaN(n)) return n;
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
    if (['yes','true','1','y','available','offered'].includes(lower)) return true;
    if (['no','false','0','n','none'].includes(lower)) return false;
    if (['null','unknown','not available','n/a','not mentioned','not specified'].includes(lower)) return null;
    const AFFIRMATIVE = /\b(yes|allowed|accessible|available|offered|provided|included|compliant|ada|handicap|equipped|open)\b/i;
    const NEGATIVE = /\b(no|not|unavailable|inaccessible|closed|denied|none|lacking|without)\b/i;
    if (AFFIRMATIVE.test(lower) && !NEGATIVE.test(lower)) return true;
    if (NEGATIVE.test(lower)) return false;
    if (/partial/i.test(lower)) return true;
  }
  return null;
};

function repairJSON(raw: string): any {
  let s = raw.trim().replace(/^```json\s*/i,'').replace(/```\s*$/,'').trim();
  try { return JSON.parse(s); } catch {}
  let braces = 0, brackets = 0, inString = false, escaped = false;
  for (let i = 0; i < s.length; i++) {
    const c = s[i];
    if (escaped) { escaped = false; continue; }
    if (c === '\\') { escaped = true; continue; }
    if (c === '"') { inString = !inString; continue; }
    if (inString) continue;
    if (c === '{') braces++; if (c === '}') braces--;
    if (c === '[') brackets++; if (c === ']') brackets--;
  }
  if (inString) s += '"';
  while (brackets > 0) { s += ']'; brackets--; }
  while (braces > 0) { s += '}'; braces--; }
  try { return JSON.parse(s); } catch { return {}; }
}

const SURFACE_KEYWORD_MAP: [string, string][] = [
  ['maple','wood'],['hardwood','wood'],['wood','wood'],['rotacast','wood'],['laminate','wood'],
  ['concrete','concrete'],['cement','concrete'],
  ['asphalt','asphalt'],['tarmac','asphalt'],
  ['sport court','sport_court'],['polyurethane','sport_court'],['synthetic','sport_court'],['rubber','sport_court'],
];
export const safeSurface = (v: any): string | null => {
  if (!v || typeof v !== 'string') return null;
  const lower = v.toLowerCase().trim();
  for (const [kw, val] of SURFACE_KEYWORD_MAP) { if (lower.includes(kw)) return val; }
  return 'unknown';
};

export const normalizeHours = (rawHours: any): string[] | null => {
  if (!rawHours) return null;

  // 1. If it's already an array of strings, return it
  if (Array.isArray(rawHours)) {
    return rawHours.map(String).filter(Boolean);
  }

  // 2. If it's a string, see if it is a JSON string
  if (typeof rawHours === 'string') {
    const trimmed = rawHours.trim();
    if ((trimmed.startsWith('[') && trimmed.endsWith(']')) || (trimmed.startsWith('{') && trimmed.endsWith('}'))) {
      try {
        const parsed = JSON.parse(trimmed);
        return normalizeHours(parsed);
      } catch {}
    }
    // If it's a plain text string with commas or newlines, split it into an array
    const separators = /[\n,;]|\s{2,}/;
    const split = trimmed.split(separators).map(s => s.trim()).filter(Boolean);
    if (split.length >= 2) return split;
    return [trimmed];
  }

  // 3. If it's a JSON object (like {"Monday": "Closed", "Tuesday": "6:00-9:00 PM"}), convert to string array
  if (typeof rawHours === 'object') {
    // If it's a Google Places opening_hours object with weekday_text
    if (Array.isArray(rawHours.weekday_text)) {
      return rawHours.weekday_text.map(String).filter(Boolean);
    }
    
    const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    const result: string[] = [];
    let hasDay = false;
    for (const d of days) {
      if (rawHours[d] !== undefined || rawHours[d.slice(0, 3)] !== undefined) {
        const val = rawHours[d] !== undefined ? rawHours[d] : rawHours[d.slice(0, 3)];
        result.push(`${d}: ${val}`);
        hasDay = true;
      }
    }
    if (hasDay) return result;

    // Fallback: convert any key-value object to string
    const resultFallback: string[] = [];
    for (const [k, v] of Object.entries(rawHours)) {
      if (v !== undefined && v !== null) {
        resultFallback.push(`${k}: ${v}`);
      }
    }
    if (resultFallback.length > 0) return resultFallback;
  }

  return null;
};

export const normalizePricing = (rawPricing: any): Record<string, number | null> => {
  const norm: Record<string, number | null> = { adult: null, child: null, senior: null, spectator: null, skate_rental: null };
  if (!rawPricing) return norm;
  
  if (typeof rawPricing === 'string') {
    const trimmed = rawPricing.trim();
    if (trimmed.startsWith('{') && trimmed.endsWith('}')) {
      try {
        rawPricing = JSON.parse(trimmed);
      } catch {}
    }
  }

  if (typeof rawPricing === 'object' && !Array.isArray(rawPricing)) {
    const keys = ['adult', 'child', 'senior', 'spectator', 'skate_rental'];
    let matchesExact = true;
    for (const k of keys) {
      if (rawPricing[k] !== undefined) {
        norm[k] = safeNum(rawPricing[k]);
      } else {
        matchesExact = false;
      }
    }
    if (matchesExact) return norm;

    // Otherwise, map dynamically
    for (const [k, v] of Object.entries(rawPricing)) {
      const lower = k.toLowerCase();
      const num = safeNum(v);
      if (lower.includes('adult')) norm.adult = num;
      else if (lower.includes('child') || lower.includes('kid')) norm.child = num;
      else if (lower.includes('senior')) norm.senior = num;
      else if (lower.includes('spectator') || lower.includes('observer')) norm.spectator = num;
      else if (lower.includes('rental') || lower.includes('skate_rental')) norm.skate_rental = num;
    }
  } else if (typeof rawPricing === 'string') {
    // Fallback regex parsing if it returned a flat string
    const adultMatch = rawPricing.match(/adult[^\d$]*(\d+\.?\d*)/i);
    if (adultMatch) norm.adult = Number(adultMatch[1]);
    const childMatch = rawPricing.match(/(child|kid)[^\d$]*(\d+\.?\d*)/i);
    if (childMatch) norm.child = Number(childMatch[2]);
    const rentalMatch = rawPricing.match(/(rental|skate[^\d$]*rental)[^\d$]*(\d+\.?\d*)/i);
    if (rentalMatch) norm.skate_rental = Number(rentalMatch[2]);
  }
  return norm;
};

// ─── Email Extraction Helper ──────────────────────────────────────────────────

const EMAIL_DOMAIN_BLOCKLIST = [
  'example.com', 'sentry.io', 'wordpress.org', 'w3.org', 'schema.org',
  'wixpress.com', 'squarespace.com', 'googleapis.com', 'googleusercontent.com',
  'gstatic.com', 'cloudflare.com', 'jquery.com', 'jsdelivr.net',
  'facebook.com', 'twitter.com', 'instagram.com', 'tiktok.com'
];

function extractEmails(text: string): string[] {
  const regex = /[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}/g;
  const matches = (text.match(regex) || []).map(e => e.toLowerCase().trim());
  return [...new Set(matches)].filter(e => {
    if (EMAIL_DOMAIN_BLOCKLIST.some(b => e.endsWith('@' + b) || e.endsWith('.' + b))) return false;
    if (/\.(png|jpg|jpeg|gif|webp|svg|css|js)$/i.test(e)) return false;
    if (e.split('@')[0].length < 2) return false;
    return true;
  });
}

// ─── Types ────────────────────────────────────────────────────────────────────

export interface DetectiveResult {
  aiMetadata: Record<string, any>;
  mappedFields: Record<string, any>;
  combinedText: string;
  qualityScore: number;
  passedQualityGate: boolean;
  candidatePhotos: Record<string, any> | null;
  socialLinks: { instagram_url: string|null; facebook_url: string|null; tiktok_url: string|null; schedule_url: string|null };
  flyerUrls: string[];
  /** Per-field confidence map: { field_name: { source, confidence, extracted_at } } */
  fieldConfidence: Record<string, { source: string; confidence: number; extracted_at: string }>;
}

// ─── Constants ────────────────────────────────────────────────────────────────

const FIELD_DESCRIPTIONS: Record<string, any> = {
  // ── Pass 1A: Session Hours (SOLO — most complex extraction) ──
  opening_hours: 'JSON object mapping each day of the week to session times in 12-hour format. Split sessions use comma. Example: {"Monday": "3:00 PM - 5:00 PM", "Friday": "7:00 PM - 11:00 PM", "Saturday": "1:00 PM - 4:00 PM, 7:00 PM - 11:00 PM"}. Use "Closed" ONLY if explicitly stated. Use null for days not mentioned. DO NOT guess.',

  // ── Pass 1B: Pricing & Fees ──
  pricing_data: {
    adult: 'number or null — dollar amount (e.g. 12.00). null if not listed.',
    child: 'number or null — dollar amount. null if not listed.',
    senior: 'number or null — dollar amount. null if not listed.',
    spectator: 'number or null — non-skating/supervising fee. null if not listed.',
    skate_rental: 'number or null — rental fee. null if not listed.'
  },
  has_fee: 'true if admission fees charged. false ONLY if explicitly stated as free. null if no pricing info found.',
  has_rental: 'true if skate rentals available. Keywords: "skate rental", "rent skates", "$X rental". null if not mentioned.',
  price_range: '"$" (under $8), "$$" ($8-$15), "$$$" ($15-$25), "$$$$" (over $25) based on adult admission. null if no pricing.',

  // ── Pass 1C: Adult Night ──
  has_adult_night: 'true if dedicated adult-only sessions (18+ or 21+). Keywords: "adult night", "grown-up skate", "18+", "21+". DO NOT confuse regular evening sessions with adult-only events.',
  adult_night_schedule: 'JSON: {day: time_range} for adult-only sessions. Example: {"Friday": "9:00 PM - 12:00 AM"}. null if none.',
  adult_night_details: 'Age requirements, dress code, drink policy, music genre, cover charge. Example: "21+ only, $15 cover includes rental, DJ plays R&B". null if no details.',

  // ── Pass 2A: Floor & Vibe ──
  surface_type: 'Exact value: "wood", "maple", "concrete", "asphalt", "sport_court", "synthetic", "vinyl", or "unknown". null if not mentioned.',
  surface_quality: '3-7 word description of floor condition. Examples: "super smooth freshly refinished maple", "sticky needs resurfacing". null if not mentioned.',
  vibe_score: 'Integer 0-100. 0=hostile, 25=boring, 50=average, 75=fun/welcoming, 100=legendary. Based on review sentiment. null if insufficient data.',

  // ── Pass 2B: Amenities ──
  is_indoor: 'true if fully enclosed indoor rink. false if outdoor/open-air. Keywords: "indoor", "outdoor", "open-air".',
  has_pro_shop: 'true if on-site shop sells gear/skates (not rentals). Keywords: "pro shop", "skate shop", "we sell", "retail".',
  has_food: 'true if food service on-site. Keywords: "snack bar", "concessions", "pizza", "cafe", "we serve", menu items.',
  has_lights: 'true if special lighting effects. Keywords: "glow skate", "cosmic skating", "black light", "laser show", "LED lights".',
  has_lockers: 'true if lockers/cubbies for personal items. Keywords: "lockers", "cubbies", "storage".',
  has_ac: 'true if air conditioning mentioned. Keywords: "air conditioned", "A/C", "climate controlled".',
  has_wifi: 'true if guest WiFi available. Keywords: "free wifi", "guest wifi", "wireless internet".',
  has_toilets: 'true if restrooms mentioned. Keywords: "restrooms", "bathrooms", "toilets".',
  capacity: 'Integer — max skaters at one time. Keywords: "capacity", "holds up to", "max skaters", "accommodates". null if not mentioned.',

  // ── Pass 2C: Identity & Culture ──
  is_wheelchair_accessible: 'true if ADA/wheelchair accessible. Keywords: "ADA", "wheelchair", "handicap accessible", "ramp".',
  hosts_derby: 'true if roller derby league at this rink. Keywords: "roller derby", "derby league", "bout", "flat track".',
  special_events: 'Array of unique recurring events/packages. Examples: ["Birthday Packages", "Cosmic Glow Fridays", "Lock-In Overnights"]. [] if none. Do NOT include regular sessions.',
  operator_name: 'Owner/operator name. Examples: "Smith Family Entertainment", "Apex Skating LLC". null if not named.',
  operator_description: '1-2 sentences about operator history. null if not found.',
  cultural_metadata: 'Cultural significance or community role. null if not mentioned.',

  // ── Pass 2D: Contacts & Socials ──
  instagram_url: 'Full Instagram URL from page links. Must start with https://instagram.com/ or https://www.instagram.com/. null if not found. DO NOT fabricate.',
  facebook_url: 'Full Facebook URL from page links. Must start with https://facebook.com/ or https://www.facebook.com/. null if not found. DO NOT fabricate.',
  tiktok_url: 'Full TikTok URL from page links. Must start with https://tiktok.com/ or https://www.tiktok.com/. null if not found. DO NOT fabricate.',
  schedule_url: 'Direct URL to schedule/calendar page. null if none exists. DO NOT fabricate.',
  yelp_url: 'Full Yelp URL. Must start with https://www.yelp.com/biz/. null if not found. DO NOT fabricate.',
  logo_url: 'Direct URL to logo image (.png/.jpg/.svg/.webp). Check og:image, favicon, header. null if not found. DO NOT return stock photos.',
  email_addresses: 'Array of all contact emails. Extract from mailto: links and text. [] if none found.'
};

const REQUIRED_SCHEMA = {
  hours: FIELD_DESCRIPTIONS.opening_hours,
  pricing: FIELD_DESCRIPTIONS.pricing_data,
};

const FIELD_PASS_MAP: Record<string, string> = {
  // Pass 1A: Session Hours (SOLO)
  opening_hours: 'pass1A',

  // Pass 1B: Pricing & Fees
  pricing_data: 'pass1B',
  has_fee: 'pass1B',
  has_rental: 'pass1B',
  price_range: 'pass1B',

  // Pass 1C: Adult Night
  has_adult_night: 'pass1C',
  adult_night_schedule: 'pass1C',
  adult_night_details: 'pass1C',

  // Pass 2A: Floor & Vibe
  surface_type: 'pass2A',
  surface_quality: 'pass2A',
  vibe_score: 'pass2A',

  // Pass 2B: Amenities
  is_indoor: 'pass2B',
  has_pro_shop: 'pass2B',
  has_food: 'pass2B',
  has_lights: 'pass2B',
  has_lockers: 'pass2B',
  has_ac: 'pass2B',
  has_wifi: 'pass2B',
  has_toilets: 'pass2B',
  capacity: 'pass2B',

  // Pass 2C: Identity & Culture
  is_wheelchair_accessible: 'pass2C',
  hosts_derby: 'pass2C',
  special_events: 'pass2C',
  operator_name: 'pass2C',
  operator_description: 'pass2C',
  cultural_metadata: 'pass2C',

  // Pass 2D: Contacts & Socials
  instagram_url: 'pass2D',
  facebook_url: 'pass2D',
  tiktok_url: 'pass2D',
  schedule_url: 'pass2D',
  yelp_url: 'pass2D',
  logo_url: 'pass2D',
  email_addresses: 'pass2D'
};

function buildPassSchema(fields: any[], spotContext: any, confMap: Record<string, any>, threshold = 0.60, maxTier = 10, configVectorMap: Record<string, string> = {}, scope: string = 'gap-fill'): Record<string, any> {
  const schema: Record<string, any> = {};
  for (const f of fields) {
    const key = f.field_name;
    const value = spotContext[key];
    const conf = confMap[key];
    
    // Skip fields belonging to higher priority groups/tiers than the selected maxTier
    if (f.priority_group !== undefined && f.priority_group > maxTier) {
      continue;
    }
    
    const isEmpty = value === null || value === undefined || value === '' || value === 'null' || value === 'NULL' || value === '{}' || value === '[]';
    const isLowConfidence = !conf || conf.confidence < threshold;
    const isTierOverride = scope.startsWith('tier-');
    
    if (key === 'opening_hours') {
      console.log(`[DEBUG buildPassSchema] opening_hours check: isEmpty=${isEmpty}, isLowConfidence=${isLowConfidence}, isTierOverride=${isTierOverride}, priority=${f.priority_group}, maxTier=${maxTier}`);
    }
    
    if (isEmpty || isLowConfidence || isTierOverride) {
      // Priority: DB ai_target_vector → hardcoded FIELD_DESCRIPTIONS → generic fallback
      const vectorPrompt = configVectorMap[key] || FIELD_DESCRIPTIONS[key] || `${f.display_label} (${f.data_type} or null).`;
      if (key === 'opening_hours') {
        schema['hours'] = vectorPrompt;
      } else if (key === 'pricing_data') {
        schema['pricing'] = vectorPrompt;
      } else {
        schema[key] = vectorPrompt;
      }
    }
  }
  return schema;
}

const MAX_PAGES_PER_RECORD = 8;
const EXTERNAL_SOURCE_DELAY = 5000; // 5s between external fetches per user spec


const SOCIAL_CRAWL_BLOCKLIST = ['facebook.com','instagram.com','twitter.com','x.com','tiktok.com','youtube.com','yelp.com','google.com','linkedin.com'];

function isSocialCrawlBlocked(url: string): boolean {
  try {
    const h = new URL(url).hostname.replace('www.','');
    return SOCIAL_CRAWL_BLOCKLIST.some(d => h === d || h.endsWith('.'+d));
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
        if (totalHeight >= scrollHeight - window.innerHeight) { clearInterval(timer); resolve(true); }
      }, 100);
    });
  });
}

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

/**
 * Adaptive Sitemap Sibling Location Bouncer
 * Analyzes discovered URLs to identify multi-location branch structures,
 * matches the target branch against the active spot's city/name,
 * prunes all sibling location paths, and extracts any target location-specific landing URL to prioritize.
 */
function filterMultiLocationUrls(
  sitemap: any,
  spotContext: any,
  onProgress: (msg: string) => void
): string | null {
  if (!spotContext.website) return null;
  
  let baseHost = '';
  let origin = '';
  try {
    const parsed = new URL(spotContext.website);
    origin = parsed.origin;
    baseHost = parsed.hostname.toLowerCase().replace('www.', '');
  } catch {
    return null;
  }
  
  const targetCity = (spotContext.city || '').toLowerCase().replace(/[^a-z0-9]/g, '').trim();
  const targetName = (spotContext.name || '').toLowerCase().replace(/[^a-z0-9]/g, '').trim();
  
  if (!targetCity) return null;
  
  const COMMON_GLOBAL_PATHS = new Set([
    'about', 'contact', 'pricing', 'schedule', 'gallery', 'home', 'careers', 'parties', 'events', 
    'privacy', 'terms', 'faq', 'services', 'news', 'blog', 'location', 'locations', 'hours', 
    'find-us', 'contact-us', 'book', 'booking', 'shop', 'store', 'cart', 'checkout', 'gift-cards', 
    'register', 'sign-up', 'login', 'admin', 'wp-admin', 'wp-content', 'assets', 'images', 'uploads', 
    'api', 'v1', 'v2', 'index', 'main', 'default', 'error', '404', 'search', 'facility', 'rink', 'arena',
    'birthday', 'birthdays', 'class', 'classes', 'lesson', 'lessons', 'session', 'sessions', 'open-skate',
    'public-skate', 'admission', 'rates', 'tickets', 'fundraisers', 'fundraising', 'stem', 'field-trips',
    'group-events', 'private-parties', 'skate-rental', 'pro-shop', 'concessions', 'cafe', 'food', 'menu',
    'adult-night', 'adults', 'kids', 'family', 'teen-night', 'schedule-events', 'calendar', 'announcements'
  ]);

  const candidateBranches = new Set<string>();
  const allUrls = sitemap.all_urls || [];
  
  for (const urlStr of allUrls) {
    try {
      const url = new URL(urlStr);
      if (url.hostname.toLowerCase().replace('www.', '') !== baseHost) continue;
      
      const segments = url.pathname.split('/').filter(Boolean);
      if (segments.length > 0) {
        const topSegment = segments[0].toLowerCase();
        if (
          /^[a-z0-9-]+$/.test(topSegment) && 
          topSegment.length >= 3 && 
          !COMMON_GLOBAL_PATHS.has(topSegment)
        ) {
          candidateBranches.add(topSegment);
        }
      }
    } catch {}
  }
  
  if (candidateBranches.size <= 1) {
    return null;
  }
  
  let targetBranchSegment = '';
  for (const branch of candidateBranches) {
    const cleanBranch = branch.replace(/-/g, '');
    if (
      targetCity.includes(cleanBranch) || 
      cleanBranch.includes(targetCity) ||
      targetName.includes(cleanBranch)
    ) {
      targetBranchSegment = branch;
      break;
    }
  }
  
  if (!targetBranchSegment) {
    onProgress(`[Detective-Bouncer] ⚠️ Multi-location candidates detected (${Array.from(candidateBranches).join(', ')}), but none matched target city/name "${spotContext.city}/${spotContext.name}". Bouncer inactive.`);
    return null;
  }
  
  const siblingBranches = Array.from(candidateBranches).filter(b => b !== targetBranchSegment);
  onProgress(`[Detective-Bouncer] 🛡️ Multi-location detected for "${spotContext.name}". Target branch: "${targetBranchSegment}". Sibling branches to bounce: ${siblingBranches.join(', ')}`);
  
  const pruneList = (urls: string[]): string[] => {
    if (!urls) return [];
    return urls.filter(urlStr => {
      try {
        const url = new URL(urlStr);
        if (url.hostname.toLowerCase().replace('www.', '') !== baseHost) return true;
        
        const pathLower = url.pathname.toLowerCase();
        for (const sibling of siblingBranches) {
          const pattern = new RegExp(`(^|/|-)` + sibling + `($|/|-)`);
          if (pattern.test(pathLower)) {
            return false;
          }
        }
        return true;
      } catch {
        return true;
      }
    });
  };
  
  const beforeTotal = sitemap.all_urls.length;
  sitemap.all_urls = pruneList(sitemap.all_urls);
  if (sitemap.schedule_urls) sitemap.schedule_urls = pruneList(sitemap.schedule_urls);
  if (sitemap.pricing_urls) sitemap.pricing_urls = pruneList(sitemap.pricing_urls);
  if (sitemap.contact_urls) sitemap.contact_urls = pruneList(sitemap.contact_urls);
  if (sitemap.gallery_urls) sitemap.gallery_urls = pruneList(sitemap.gallery_urls);
  if (sitemap.about_urls) sitemap.about_urls = pruneList(sitemap.about_urls);
  if (sitemap.events_urls) sitemap.events_urls = pruneList(sitemap.events_urls);
  
  const bouncedCount = beforeTotal - sitemap.all_urls.length;
  if (bouncedCount > 0) {
    onProgress(`[Detective-Bouncer] 🛡️ Bounced ${bouncedCount} sibling branch URLs from crawl queue.`);
  }

  // Look for target branch specific location landing URL
  const targetBranchUrl = `${origin}/${targetBranchSegment}`;
  const foundTargetUrl = sitemap.all_urls.find((u: string) => {
    try {
      const parsed = new URL(u);
      return parsed.pathname.toLowerCase().replace(/\/$/, '') === '/' + targetBranchSegment;
    } catch {
      return false;
    }
  });

  return foundTargetUrl || targetBranchUrl;
}

// ─── Pre-Crawl External Source Fetchers (no Puppeteer) ───────────────────────

async function fetchExternalText(url: string, label: string, onProgress?: (msg: string) => void): Promise<string> {
  const controller = new AbortController();
  const timeout = setTimeout(() => controller.abort(), 10000);
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36' },
      signal: controller.signal,
    });
    clearTimeout(timeout);
    if (!res.ok) {
      onProgress?.(`[Detective] ⚠️ ${label} fetch failed: HTTP ${res.status} ${res.statusText}`);
      return '';
    }
    const html = await res.text();
    // Extract JSON-LD blocks
    const jsonLdMatches: string[] = [];
    const jsonLdRegex = /<script[^>]*type="application\/ld\+json"[^>]*>([\s\S]*?)<\/script>/gi;
    let m: RegExpExecArray | null;
    while ((m = jsonLdRegex.exec(html)) !== null) jsonLdMatches.push(m[1]);
    // Extract OG tags
    const ogImage = html.match(/<meta[^>]*property="og:image"[^>]*content="([^"]+)"/i)?.[1] || '';
    const ogTitle = html.match(/<meta[^>]*property="og:title"[^>]*content="([^"]+)"/i)?.[1] || '';
    // Strip tags for body text (simple)
    const fullBodyText = html.replace(/<[^>]+>/g, ' ').replace(/\s{2,}/g, ' ').trim();
    const bodyText = fullBodyText.slice(0, 1500);
    
    // Feature: Review Text Mining Regex Engine
    // Extract sentences from full body text, splitting by punctuation or line breaks
    const sentences = fullBodyText.split(/[.!?]+|\n+/)
      .map(s => s.trim())
      .filter(s => s.length > 10 && s.length < 300);
    const keywords = ['adult', '18+', '21+', 'price', 'admission', '$', 'cost', 'fee', 'schedule', 'hours', 'session', 'dj', 'music'];
    const matchedSnippets = sentences.filter(s => {
      const lower = s.toLowerCase();
      return keywords.some(k => {
        if (k.length <= 3) {
          // Escape '+' and enforce word boundaries for short key terms
          const regex = new RegExp(`\\b${k.replace('+', '\\+')}\\b`, 'i');
          return regex.test(lower);
        }
        return lower.includes(k);
      });
    });
    const uniqueSnippets = [...new Set(matchedSnippets)].slice(0, 30);

    let result = `[${label}: ${url}]\n`;
    if (jsonLdMatches.length) result += `JSON-LD: ${jsonLdMatches.join('\n')}\n`;
    if (ogTitle) result += `Title: ${ogTitle}\n`;
    if (ogImage) result += `og:image: ${ogImage}\n`;
    if (uniqueSnippets.length > 0) result += `MINED SNIPPETS:\n- ${uniqueSnippets.join('\n- ')}\n\n`;
    result += bodyText;
    onProgress?.(`[Detective] ✓ ${label}: ${bodyText.length} chars, ${jsonLdMatches.length} JSON-LD blocks, ${uniqueSnippets.length} snippets mined`);
    return result;
  } catch (err: any) {
    onProgress?.(`[Detective] ⚠️ ${label} fetch error: ${err.message}`);
    return '';
  }
}

async function fetchYelpData(name: string, city: string, state: string, existingYelpUrl?: string): Promise<{ text: string; photos_url: string | null; og_image: string | null }> {
  const url = existingYelpUrl || `https://www.yelp.com/search?find_desc=${encodeURIComponent(name)}&find_loc=${encodeURIComponent(`${city} ${state}`)}`;
  const text = await fetchExternalText(url, 'YELP DATA');
  const photosUrl = existingYelpUrl ? `${existingYelpUrl.replace(/\?.*$/,'')}` : null;
  const ogImageMatch = text.match(/og:image: ([^\n]+)/);
  return { text, photos_url: photosUrl ? `${photosUrl}/photos` : null, og_image: ogImageMatch?.[1] || null };
}

async function fetchYelpReviewsWithPuppeteer(browser: any, name: string, city: string, state: string, existingYelpUrl?: string, onProgress?: (msg: string) => void): Promise<string> {
  const url = existingYelpUrl || `https://www.yelp.com/search?find_desc=${encodeURIComponent(name)}&find_loc=${encodeURIComponent(`${city} ${state}`)}`;
  onProgress?.(`[Yelp-Miner] Navigating to Yelp: ${url.slice(0, 60)}...`);
  
  const page = await browser.newPage();
  try {
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
    await page.goto(url, { waitUntil: 'domcontentloaded', timeout: 25000 }).catch(()=>{});
    await sleep(2000);
    
    const isSearchPage = url.includes('/search?');
    if (isSearchPage) {
      const bizLink = await page.evaluate(() => {
        const anchors = Array.from(document.querySelectorAll('a'));
        const link = anchors.find(a => a.href && a.href.includes('/biz/'));
        return link ? link.href : null;
      });
      if (bizLink) {
        onProgress?.(`[Yelp-Miner] Found Yelp business link: ${bizLink}`);
        await page.goto(bizLink, { waitUntil: 'domcontentloaded', timeout: 25000 }).catch(()=>{});
        await sleep(2000);
      } else {
        onProgress?.(`[Yelp-Miner] No business listing found in Yelp search.`);
        await page.close();
        return '';
      }
    }
    
    const rawText = await page.evaluate(() => {
      const comments = Array.from(document.querySelectorAll('[data-testid="comment"], p[class*="comment"], span[class*="raw"]'));
      if (comments.length > 0) {
        return comments.map(c => (c as any).innerText).join('\n\n');
      }
      return document.body.innerText;
    });
    
    await page.close();
    
    const cleaned = rawText
      .split('\n\n')
      .map((r: string) => r.trim())
      .filter((r: string) => r.length > 30 && r.length < 1000)
      .slice(0, 5);
      
    if (cleaned.length > 0) {
      onProgress?.(`[Yelp-Miner] ✓ Successfully mined ${cleaned.length} reviews from Yelp!`);
      return `[YELP REVIEWS]\n- ${cleaned.join('\n- ')}\n`;
    }
    return '';
  } catch (e: any) {
    onProgress?.(`[Yelp-Miner] ⚠️ Scrape failed: ${e.message}`);
    try { await page.close(); } catch {}
    return '';
  }
}


async function fetchFacebookData(facebookUrl: string | null): Promise<{ text: string; cover_photo: string | null; photos_url: string | null }> {
  if (!facebookUrl) return { text: '', cover_photo: null, photos_url: null };
  const text = await fetchExternalText(facebookUrl, 'FACEBOOK DATA');
  const ogImageMatch = text.match(/og:image: ([^\n]+)/);
  return {
    text,
    cover_photo: ogImageMatch?.[1] || null,
    photos_url: facebookUrl.replace(/\/$/, '') + '/photos',
  };
}

async function fetchGooglePlacesWeb(googlePlaceId: string | null): Promise<string> {
  if (!googlePlaceId) return '';
  const url = `https://www.google.com/maps/place/?q=place_id:${googlePlaceId}`;
  return fetchExternalText(url, 'GOOGLE PLACES');
}

// ─── LM Studio Call ───────────────────────────────────────────────────────────

async function callLMStudio(
  systemMessage: string,
  userMessage: string,
  model: string,
  onProgress: (msg: string) => void,
  passLabel: string,
  onStream?: (text: string) => void
): Promise<Record<string, any>> {
  const host = process.env.LM_STUDIO_HOST || 'host.docker.internal';
  const LM_STUDIO_URL = `http://${host}:1234/v1/chat/completions`;
  console.log(`[Detective] 🔗 Querying LM Studio URL: ${LM_STUDIO_URL} (Model: ${model})`);
  
  if (onStream) {
    onStream(`\n\n--- [${passLabel} LLM PASS START] ---\nQuerying model ${model}...\n`);
  }
  
  const payload = { 
    model, 
    messages: [
      { role:'system', content: systemMessage },
      { role:'user', content: userMessage }
    ], 
    temperature: 0.1, 
    stream: true
  };
  
  for (let attempt = 1; attempt <= 2; attempt++) {
    try {
      const url = new URL(LM_STUDIO_URL);
      const postData = JSON.stringify(payload);
      
      const fullText = await new Promise<string>((resolve, reject) => {
        const req = http.request({
          hostname: url.hostname,
          port: url.port || 80,
          path: url.pathname,
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Content-Length': Buffer.byteLength(postData)
          },
          timeout: 300000 // 5 minutes timeout
        }, (res: http.IncomingMessage) => {
          if (res.statusCode && (res.statusCode < 200 || res.statusCode >= 300)) {
            let errBody = '';
            res.on('data', (chunk) => { errBody += chunk.toString(); });
            res.on('end', () => {
              reject(new Error(`HTTP status ${res.statusCode}: ${res.statusMessage} - Body: ${errBody}`));
            });
            return;
          }

          let buffer = '';
          let accumulated = '';
          
          res.on('data', (chunk: any) => {
            const chunkStr = chunk.toString();
            buffer += chunkStr;
            
            let lines = buffer.split('\n');
            buffer = lines.pop() || ''; // keep last partial line in buffer
            
            for (const line of lines) {
              const cleanLine = line.trim();
              if (!cleanLine) continue;
              if (cleanLine.startsWith('data: ')) {
                const dataStr = cleanLine.slice(6).trim();
                if (dataStr === '[DONE]') continue;
                try {
                  const parsed = JSON.parse(dataStr);
                  const content = parsed.choices?.[0]?.delta?.content || '';
                  if (content) {
                    accumulated += content;
                    if (onStream) {
                      onStream(content);
                    }
                  }
                } catch (e) {
                  // partial stream line parse error is fine
                }
              }
            }
          });
          
          res.on('end', () => {
            if (buffer.startsWith('data: ')) {
              const dataStr = buffer.slice(6).trim();
              if (dataStr !== '[DONE]') {
                try {
                  const parsed = JSON.parse(dataStr);
                  const content = parsed.choices?.[0]?.delta?.content || '';
                  if (content) {
                    accumulated += content;
                    if (onStream) onStream(content);
                  }
                } catch (e) {}
              }
            }
            resolve(accumulated);
          });
        });
        
        req.on('error', (err: Error) => reject(err));
        req.on('timeout', () => {
          req.destroy();
          reject(new Error('Request timed out after 5 minutes'));
        });
        
        req.write(postData);
        req.end();
      });
      
      try {
        const parsed = JSON.parse(fullText.replace(/```json/g,'').replace(/```/g,'').trim());
        onProgress(`[Detective] ✓ LM Studio ${passLabel} complete. Keys: ${Object.keys(parsed).length}`);
        return parsed;
      } catch {
        onProgress(`[Detective] ⚠️ JSON repair needed (${passLabel})...`);
        return repairJSON(fullText);
      }
    } catch (err: any) {
      onProgress(`[Detective] ✗ LM Studio attempt ${attempt}/2 failed: ${err.message}`);
      if (attempt < 2) { onProgress(`[Detective] ⏳ Retrying in 3s...`); await sleep(3000); }
    }
  }
  return {};
}

const MIN_OCR_DIMENSION = 16; // Leptonica hard minimum is 3px; we use 16 as safe floor

function isValidJpegOrPng(buf: Buffer): boolean {
  if (!buf || buf.length < 24) return false;
  // JPEG magic bytes: FF D8 FF — parse width/height from SOF0/SOF2 marker
  if (buf[0] === 0xFF && buf[1] === 0xD8 && buf[2] === 0xFF) {
    // Scan for SOF marker (0xFF 0xC0 or 0xFF 0xC2)
    for (let i = 2; i < buf.length - 9; i++) {
      if (buf[i] === 0xFF && (buf[i + 1] === 0xC0 || buf[i + 1] === 0xC2)) {
        const h = (buf[i + 5] << 8) | buf[i + 6];
        const w = (buf[i + 7] << 8) | buf[i + 8];
        return w >= MIN_OCR_DIMENSION && h >= MIN_OCR_DIMENSION;
      }
    }
    return false; // No SOF found — reject
  }
  // PNG magic bytes: 89 50 4E 47 — width/height at bytes 16-23 in IHDR chunk
  if (buf[0] === 0x89 && buf[1] === 0x50 && buf[2] === 0x4E && buf[3] === 0x47) {
    if (buf.length < 24) return false;
    const w = (buf[16] << 24) | (buf[17] << 16) | (buf[18] << 8) | buf[19];
    const h = (buf[20] << 24) | (buf[21] << 16) | (buf[22] << 8) | buf[23];
    return w >= MIN_OCR_DIMENSION && h >= MIN_OCR_DIMENSION;
  }
  return false;
}

async function downloadImageBuffer(url: string): Promise<Buffer | null> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (compatible; SK8LytzBot/2.0)' },
      signal: AbortSignal.timeout(10000),
    });
    if (!res.ok) return null;
    const contentType = res.headers.get('content-type') || '';
    if (!contentType.startsWith('image/')) return null;
    const arrayBuffer = await res.arrayBuffer();
    return Buffer.from(arrayBuffer);
  } catch {
    return null;
  }
}

// ─── Page Crawl Helper ───────────────────────────────────────────────────────

async function crawlPage(page: any, url: string, onProgress: (m: string) => void): Promise<{ text:string; jsonLd:string; ogImage:string|null; images:Array<{src:string;alt:string;parentClass:string}>; iframes:string[]; links:Array<{href:string;text:string}>; mailtos:string[]; fullText:string; }> {
  const empty = { text:'', jsonLd:'', ogImage:null, images:[], iframes:[], links:[], mailtos:[], fullText:'' };
  if (url.toLowerCase().endsWith('.pdf')) {
    try { const fetchFn = require('node-fetch'); const buf = await (await fetchFn(url)).buffer(); const pdfData = await (pdfParse as any)(buf); return { ...empty, text:`[PDF: ${url}]\n${pdfData.text}` }; } catch { return empty; }
  }
  if (/\.(png|jpg|jpeg)(\?.*)?$/i.test(url)) {
    try {
      const buf = await downloadImageBuffer(url);
      // Dimension + magic byte guard prevents Leptonica worker crashes
      if (buf && buf.length >= 2048 && isValidJpegOrPng(buf)) {
        try {
          const { data:{text} } = await Tesseract.recognize(buf,'eng');
          return { ...empty, text:text?.length>20?`[OCR Image: ${url}]\n${text}`:'' };
        } catch { /* Tesseract worker crash — swallow and continue */ }
      }
      return empty;
    } catch { return empty; }
  }
  try {
    await page.goto(url,{waitUntil:'domcontentloaded',timeout:30000}).catch(()=>page.goto(url,{waitUntil:'domcontentloaded',timeout:15000}));
    await autoScroll(page);
    const d = await page.evaluate(()=>{
      const decodeCfEmail = (encoded: string): string | null => {
        try {
          let email = "";
          const key = parseInt(encoded.substring(0, 2), 16);
          for (let i = 2; i < encoded.length; i += 2) {
            email += String.fromCharCode(parseInt(encoded.substring(i, i + 2), 16) ^ key);
          }
          return email.trim().toLowerCase();
        } catch {
          return null;
        }
      };

      const ogImage=document.querySelector('meta[property="og:image"]')?.getAttribute('content')||null;
      const jsonLd=Array.from(document.querySelectorAll('script[type="application/ld+json"]')).map((e:any)=>e.innerText).join('\n');
      const iframes=Array.from(document.querySelectorAll('iframe')).map((e:any)=>e.src||'').filter((s:string)=>s.includes('calendar')||s.includes('ticket')||s.includes('centeredge')||s.includes('roller'));
      const images=Array.from(document.querySelectorAll('img')).filter((i:any)=>{const w=i.naturalWidth||i.width||0,h=i.naturalHeight||i.height||0;return w>=400&&h>=300&&i.src&&(i.src.startsWith('http')||i.src.startsWith('//'));}).map((i:any)=>({src:i.src,alt:(i.alt||'').toLowerCase(),parentClass:(i.parentElement?.className||'').toLowerCase()}));
      const links=Array.from(document.querySelectorAll('a')).map((a:any)=>({href:(a.href||'').toLowerCase(),text:(a.innerText||'').toLowerCase()})).filter((l:any)=>l.href&&(l.href.startsWith('http')||l.href.startsWith('//')));
      
      const mailtos=Array.from(document.querySelectorAll('a[href^="mailto:"]')).map((a:any)=>a.href.replace('mailto:','').split('?')[0].trim().toLowerCase()).filter((e:string)=>e&&e.includes('@'));
      
      // Extract Cloudflare protected emails
      document.querySelectorAll('[data-cfemail]').forEach((el: any) => {
        const enc = el.getAttribute('data-cfemail');
        if (enc) {
          const dec = decodeCfEmail(enc);
          if (dec && dec.includes('@') && !mailtos.includes(dec)) mailtos.push(dec);
        }
      });
      document.querySelectorAll('a[href*="email-protection"]').forEach((el: any) => {
        const href = el.href || '';
        const hashIndex = href.indexOf('#');
        if (hashIndex !== -1) {
          const enc = href.substring(hashIndex + 1);
          const dec = decodeCfEmail(enc);
          if (dec && dec.includes('@') && !mailtos.includes(dec)) mailtos.push(dec);
        }
      });

      const fullText=document.body?.innerText?.trim()||'';
      document.querySelectorAll('nav,footer,script,style,header,iframe,noscript').forEach(el=>el.remove());
      const text=document.body?.innerText?.trim()||'';
      return {ogImage,jsonLd,iframes,images,links,mailtos,text,fullText};
    }).catch(()=>({ogImage:null,jsonLd:'',iframes:[],images:[],links:[],mailtos:[],text:'',fullText:''}));
    return {text:d.text,jsonLd:d.jsonLd,ogImage:d.ogImage,images:d.images,iframes:d.iframes,links:d.links,mailtos:d.mailtos,fullText:d.fullText};
  } catch { onProgress(`[Detective] Nav failed: ${url}`); return empty; }
}

// ─── Web Text Condensation (RAG-Lite Boilerplate Filter) ────────────────────
function condenseWebText(rawText: string): string {
  if (!rawText) return '';
  const lines = rawText.split('\n');
  const keptSegments: string[] = [];

  const highValuePatterns = [
    /hour|schedule|session|time|calendar|events|open.?skate/i,
    /adult.?night|18\+|21\+/i,
    /pricing|price|admission|rates|ticket|fee|cost|dollar/i,
    /about|story|history|facility|rink|floor|surface|wood/i,
    /contact|location|directions|email|info|phone|address/i,
    /rental|pro.?shop|food|snack|arcade|party|birthday/i,
    /locker|bathroom|restroom|toilet|wifi|wi-fi|wheelchair|accessible|parking/i
  ];

  const skipKeywords = [
    /copyright/i, /all rights reserved/i, /privacy policy/i, /cookie policy/i,
    /terms of (use|service)/i, /designed by/i, /powered by/i, /skip to/i,
    /menu toggle/i, /cart/i, /checkout/i
  ];

  for (const line of lines) {
    const s = line.trim();
    if (!s || s.length < 5 || s.length > 500) continue;

    // Discard typical layout and cookie policy boilerplate
    if (skipKeywords.some(pat => pat.test(s))) continue;

    // Keep if matches high-value rink data patterns
    const isHighValue = highValuePatterns.some(pat => pat.test(s));
    
    // Also keep if it looks like a time/price line (contains numbers + day name or am/pm/$)
    const hasNumbers = /\d+/.test(s);
    const hasTimeOrPrice = /(am|pm|\$|monday|tuesday|wednesday|thursday|friday|saturday|sunday)/i.test(s);
    
    if (isHighValue || (hasNumbers && hasTimeOrPrice)) {
      keptSegments.push(s);
    }
  }

  // Fallback to basic slicing if page was exceptionally sparse to avoid data loss
  if (keptSegments.length < 5) {
    return rawText.replace(/\s+/g, ' ').slice(0, 4000);
  }

  return keptSegments.join('\n');
}

// ─── Main Detective Function ─────────────────────────────────────────────────

export async function executeDetective(
  spotContext: any, aiConfig: any, isHeadless: boolean, onProgress: (msg:string)=>void=()=>{}, onStream?: (msg:string)=>void
): Promise<DetectiveResult> {
  let coreText=''; let amenityText=''; let combinedText=''; const flyerUrls:string[]=[]; let ogImage:string|null=null;
  const domImages:Array<{src:string;alt:string;parentClass:string}>=[];
  let aiMetadata:Record<string,any>={};
  const detectiveModel=aiConfig.detective_model||'local-model';
  let sitemap:any={schedule_urls:[],pricing_urls:[],about_urls:[],events_urls:[],contact_urls:[],gallery_urls:[],all_urls:[]};
  let yelpData:any={text:'',photos_url:null,og_image:null};
  let fbData:any={text:'',cover_photo:null,photos_url:null};
  let allLinks:Array<{href:string;text:string}>=[];
  const allMailtos:string[]=[];
  let browser:any=null;

  let pass1: any = null;
  let pass2: any = null;
  let escalationRan = false;
  let earlyTerminated = false;

  const exclusionKw = aiConfig.ai_exclusion_keywords || [];
  const usp = aiConfig.ai_system_prompt || '';

  // Build lookup map from DB-stored ai_target_vectors: [{key, type}] → {key: type}
  const configVectorMap: Record<string, string> = {};
  if (Array.isArray(aiConfig.ai_target_vectors)) {
    for (const vec of aiConfig.ai_target_vectors) {
      if (typeof vec === 'string') {
        // Legacy flat string format: just the field name, no prompt
        continue;
      }
      // Dashboard format: {key, prompt} or sandbox format: {key, type}
      const fieldKey = vec.key;
      const promptText = vec.prompt || vec.type;
      if (fieldKey && promptText) configVectorMap[fieldKey] = promptText;
    }
    if (Object.keys(configVectorMap).length > 0) {
      onProgress(`[Detective] 📋 Loaded ${Object.keys(configVectorMap).length} AI target vectors from config DB`);
    }
  }
  const buildSystem = (schema: Record<string, any>, ctx?: string) => {
    let s = `You are a data extraction agent for [${spotContext.name}] in [${spotContext.city}].\nONLY this location. Valid JSON only.\n`;
    s += `CRITICAL BOOLEAN RULE: For ALL boolean fields, you MUST return null if the information was not explicitly found in the text. Do NOT assume false. Do NOT infer. A missing fee schedule does NOT mean admission is free. A missing amenity mention does NOT mean it is absent. Return null to indicate unknown.\n`;
    if (usp) s += usp + '\n';
    if (exclusionKw.length) s += `TOXICITY: If PRIMARY business matches [${exclusionKw.join(',')}] return {"TOXICITY_ABORT":true}.\n`;
    if (ctx) s += `PASS 1 CONTEXT: ${ctx}\n`;
    return s + `SCHEMA:\n${JSON.stringify(schema, null, 2)}`;
  };



  // ── Fix #8: Extract Google Places photo references from raw_data ──
  let googlePhotoRefs: string[] = [];
  try {
    const rawData = typeof spotContext.raw_data === 'string' ? JSON.parse(spotContext.raw_data) : (spotContext.raw_data || {});
    if (rawData.photos?.length) {
      const GMAPS_KEY = process.env.EXPO_PUBLIC_GOOGLE_MAPS_KEY;
      if (GMAPS_KEY) {
        googlePhotoRefs = rawData.photos.slice(0, 5)
          .map((p: any) => p.photo_reference || p)
          .filter((ref: any) => typeof ref === 'string' && ref.length > 10)
          .map((ref: string) => `https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photo_reference=${ref}&key=${GMAPS_KEY}`);
        if (googlePhotoRefs.length > 0) onProgress(`[Detective] 🗺️ Extracted ${googlePhotoRefs.length} Google Places photo refs from raw_data`);
      }
    }
  } catch {}

  // ── Separate text buckets for priority ordering (Fix #1) ──
  let externalText = ''; // Yelp, Facebook, Google Places (low priority — appended LAST)
  let priorityText = ''; // Targeted sitemap pages (high priority — prepended FIRST)

  const isSocialOnly = spotContext.website && isSocialCrawlBlocked(spotContext.website);
  const hasWebsite = !!spotContext.website;
  let hasCrawledWebsite = false;

  try {
    if (hasWebsite && !isSocialOnly) {
      hasCrawledWebsite = true;
      onProgress('[Detective] Fetching sitemap...');
    sitemap=await parseSitemap(spotContext.website);
    onProgress(`[Detective] 🕷️ Discovered sitemap URLs: schedule(${sitemap.schedule_urls.length}) pricing(${sitemap.pricing_urls.length}) contact(${sitemap.contact_urls.length}) gallery(${sitemap.gallery_urls.length}) about(${sitemap.about_urls.length}) total(${sitemap.all_urls.length})`);

    // Run the Adaptive Sitemap Sibling Location Bouncer
    const targetLocationUrl = filterMultiLocationUrls(sitemap, spotContext, onProgress);

    browser=await puppeteer.launch({headless:isHeadless?'new':false,protocolTimeout:60000,args:['--no-sandbox','--disable-setuid-sandbox','--disable-dev-shm-usage']});
    const page=await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
      await page.setViewport({width:1280,height:800});
      // Fix #7: Include gallery_urls in targeted crawl (SitemapParser spider handles discovery)
      let crawlUrls: string[] = [];
      let targeted: string[] = [];
      if (sitemap.all_urls && sitemap.all_urls.length > 0 && sitemap.all_urls.length <= 15) {
        // Small site optimization: crawl ALL unique discovered pages to guarantee 100% data extraction completeness
        if (targetLocationUrl && targetLocationUrl !== spotContext.website) {
          crawlUrls = [...new Set([targetLocationUrl, spotContext.website, ...sitemap.all_urls])];
          onProgress(`[Detective-Bouncer] 🎯 Prioritizing specific location landing page: ${targetLocationUrl}`);
        } else {
          crawlUrls = [...new Set([spotContext.website, ...sitemap.all_urls])];
        }
        onProgress(`[Detective] 🕷️ Small site detected (${sitemap.all_urls.length} pages). Crawling ALL unique URLs.`);
      } else {
        targeted=[...new Set([...sitemap.schedule_urls.slice(0,3),...sitemap.pricing_urls.slice(0,3),...sitemap.about_urls.slice(0,2),...sitemap.events_urls.slice(0,2),...sitemap.contact_urls.slice(0,3),...sitemap.gallery_urls.slice(0,2)])];
        if (targetLocationUrl && targetLocationUrl !== spotContext.website) {
          crawlUrls = [targetLocationUrl, spotContext.website, ...targeted].slice(0, MAX_PAGES_PER_RECORD);
          onProgress(`[Detective-Bouncer] 🎯 Prioritizing specific location landing page: ${targetLocationUrl}`);
        } else {
          crawlUrls=[spotContext.website,...targeted].slice(0,MAX_PAGES_PER_RECORD);
        }
      }
      const PAGE_SCORE_RULES=[{pattern:/hours|schedule|session|times|calendar|events|open.?skate/i,score:10},{pattern:/adult.?night|18\+|21\+/i,score:10},{pattern:/pricing|price|admission|rates|tickets/i,score:9},{pattern:/about|story|history|facility|rink/i,score:8},{pattern:/contact|location|directions|email|info/i,score:10}];
      
      let hostname='';try{hostname=new URL(spotContext.website).hostname;}catch{}
      for(const url of crawlUrls){
        onProgress(`[Detective] -> ${url}`);
        const pg=await crawlPage(page,url,onProgress);
        let isOpsUrl = url === spotContext.website || /schedule|pricing|admission|rates|calendar|events|hours|session|times/i.test(url);
        let isAmenityUrl = url === spotContext.website || /about|story|history|facility|rink|pro.?shop|shop|concession|cafe|food/i.test(url);
        if (!isOpsUrl && !isAmenityUrl) {
          // Uncategorized crawled pages: include in both segments to prevent data loss
          isOpsUrl = true;
          isAmenityUrl = true;
        }

        // Fix #1: Targeted pages go into priorityText (prepended), NOT appended to external noise
        if(pg.jsonLd) {
          const ld = `\n\n[JSON-LD: ${url}]\n${pg.jsonLd}`;
          if(isOpsUrl) priorityText += ld;
          if(isAmenityUrl) amenityText += ld;
        }
        if(pg.text) {
          const txt = `\n\n[PAGE: ${url}]\n${condenseWebText(pg.text)}`;
          if(isOpsUrl) priorityText += txt;
          if(isAmenityUrl) amenityText += txt;
        }
        if(!ogImage&&pg.ogImage&&!pg.ogImage.includes('placeholder')) ogImage=pg.ogImage;
        domImages.push(...pg.images.slice(0,5));
        flyerUrls.push(...pg.images.filter(i=>/schedule|pricing|flyer/i.test(i.src+i.alt)).map(i=>i.src));
        allLinks.push(...pg.links);
        allMailtos.push(...pg.mailtos);
        for(const iframe of pg.iframes){
          const ig=await crawlPage(page,iframe,onProgress);
          if(ig.text) {
            const igTxt = `\n\n[IFRAME:${iframe}]\n${condenseWebText(ig.text)}`;
            coreText+=igTxt; amenityText+=igTxt;
          }
        }

        // Check for early termination after crawling the website homepage
        try {
          const ledger = HeuristicsEngine.load();
          if (ledger.early_termination_enabled && url === spotContext.website && priorityText.trim().length >= 50) {
            // Pre-gate: only burn an LLM call if homepage text actually contains time + price signals
            const hasTimeSignal = /\b(monday|tuesday|wednesday|thursday|friday|saturday|sunday|am|pm|\d{1,2}:\d{2})\b/i.test(priorityText);
            const hasPriceSignal = /(\$\d|admission|pricing|rates|fee|\bfree\b)/i.test(priorityText);
            if (!hasTimeSignal || !hasPriceSignal) {
              onProgress(`[HEURISTIC] ⏭️ Skipping Early-Check: homepage lacks time/price regex signals (time:${hasTimeSignal}, price:${hasPriceSignal}).`);
            } else {
            onProgress(`[HEURISTIC] 🧬 Checking early termination conditions on homepage...`);
            const testPass1 = await callLMStudio(
              buildSystem(REQUIRED_SCHEMA),
              `Website Text:\n${priorityText.slice(0, 12000)}`,
              detectiveModel,
              onProgress,
              'Early-Check',
              onStream
            );
            
            const hasHours = testPass1.hours && (
              typeof testPass1.hours === 'object' || 
              (typeof testPass1.hours === 'string' && testPass1.hours.toLowerCase().includes('monday'))
            );
            const hasPricing = testPass1.pricing && (
              testPass1.pricing.adult !== null || 
              testPass1.pricing.child !== null || 
              testPass1.pricing.skate_rental !== null
            );
            
            if (hasHours && hasPricing) {
              pass1 = testPass1;
              earlyTerminated = true;
              const currentIdx = crawlUrls.indexOf(url);
              if (currentIdx !== -1) {
                const remaining = crawlUrls.slice(currentIdx + 1);
                const amenitiesOnly = remaining.filter(u => /about|story|history|facility|rink|pro.?shop|shop|concession|cafe|food/i.test(u));
                crawlUrls.splice(currentIdx + 1, crawlUrls.length - (currentIdx + 1), ...amenitiesOnly);
                onProgress(`[HEURISTIC] 🎯 Early termination triggered! Hours & Pricing resolved. Kept ${amenitiesOnly.length} amenity/about pages, skipped ${remaining.length - amenitiesOnly.length} schedule/pricing/events pages.`);
              } else {
                break;
              }
            } else {
              onProgress(`[HEURISTIC] ❌ Early termination conditions not met (hours: ${!!hasHours}, pricing: ${!!hasPricing}). Continuing crawl.`);
            }
            } // close regex pre-gate else
          }
        } catch (e: any) {
          onProgress(`[HEURISTIC] ⚠️ Error during early termination check: ${e.message}`);
        }
      }
      if(targeted.length===0 && !earlyTerminated){
        const internal=allLinks.filter(l=>{try{return new URL(l.href).hostname===hostname;}catch{return false;}});
        const scored=internal.map(l=>{let s=0;for(const r of PAGE_SCORE_RULES){if(r.pattern.test(l.href)||r.pattern.test(l.text))s=Math.max(s,r.score);}return{...l,score:s};}).filter(l=>l.score>0).sort((a,b)=>b.score-a.score);
        for(const l of [...new Set(scored.map(s=>s.href))].slice(0,5)){
          if(coreText.includes(`[PAGE: ${l}]`)) continue;
          const pg=await crawlPage(page,l,onProgress);
          if(pg.text) {
            const txt = `\n\n[PAGE: ${l}]\n${pg.text}`;
            coreText+=txt; amenityText+=txt;
          }
          domImages.push(...pg.images.slice(0,3));
        }
      }

      // Fix #1: Merge ops text with iframe text for Pass 1. amenityText retains its accumulated about/facility text for Pass 2.
      coreText = priorityText + '\n' + coreText;
    } else {
      onProgress('[Detective] No website or Social-only. Will rely entirely on Phase 3 Enrichment.');
    }
  } catch (err: any) {
    onProgress(`[Detective] Pre-crawl phase error: ${err.message}`);
  }

  try {

  const sanitize=(obj:any):any=>{
    if(!obj) return obj;
    if(Array.isArray(obj)) return obj.map(sanitize);
    if(typeof obj==='object'){
      const n:any={};
      for(const k in obj){
        let v=obj[k];
        if(v==='null'||v==='NULL'||v==='None'||v==='N/A') v=null;
        n[k]=sanitize(v);
      }
      return n;
    }
    if(obj==='null'||obj==='NULL'||obj==='None'||obj==='N/A') return null;
    return obj;
  };

  // ── Fix #2: Parse JSON-LD structured data directly ──
  const jsonLdFields: Record<string, any> = {};
  const jsonLdRegex = /\[JSON-LD:[^\]]*\]\n([\s\S]*?)(?=\n\n|$)/g;
  let jsonLdMatch: RegExpExecArray | null;
  while ((jsonLdMatch = jsonLdRegex.exec(coreText + '\n' + amenityText)) !== null) {
    try {
      const parsed = JSON.parse(jsonLdMatch[1].trim());
      const items = Array.isArray(parsed) ? parsed : [parsed];
      for (const item of items) {
        if (item.telephone && !jsonLdFields.phone_number) jsonLdFields.phone_number = item.telephone;
        if (item.priceRange && !jsonLdFields.price_range) jsonLdFields.price_range = item.priceRange;
        if (item.image && !jsonLdFields.logo_url) jsonLdFields.logo_url = typeof item.image === 'string' ? item.image : item.image?.url;
        if (item.openingHoursSpecification && !jsonLdFields.hours) {
          const hoursMap: Record<string, string> = {};
          for (const spec of (Array.isArray(item.openingHoursSpecification) ? item.openingHoursSpecification : [item.openingHoursSpecification])) {
            const days = Array.isArray(spec.dayOfWeek) ? spec.dayOfWeek : [spec.dayOfWeek];
            for (const day of days) {
              const dayName = typeof day === 'string' ? day.replace('https://schema.org/', '').replace('http://schema.org/', '') : day;
              if (dayName && spec.opens && spec.closes) hoursMap[dayName] = `${spec.opens}-${spec.closes}`;
            }
          }
          if (Object.keys(hoursMap).length > 0) jsonLdFields.hours = hoursMap;
        }
        if (item.address) {
          if (item.address.streetAddress && !jsonLdFields.street_address) jsonLdFields.street_address = item.address.streetAddress;
        }
      }
    } catch {}
  }
  if (Object.keys(jsonLdFields).length > 0) onProgress(`[Detective] 📋 JSON-LD direct parse: ${Object.keys(jsonLdFields).join(', ')}`);

  if (!pass1) pass1 = {};
  if (!pass2) pass2 = {};

  // ── Gap Analysis & JIT Pass Scheduler ──
  const scope = aiConfig.scrape_scope || 'gap-fill';
  let maxTier = 10;
  if (scope.startsWith('tier-')) {
    const parsed = parseInt(scope.split('-')[1]);
    if (!isNaN(parsed)) maxTier = parsed;
  }
  let skipPass1A = false;
  let skipPass1B = false;
  let skipPass1C = false;
  let skip2A = false;
  let skip2B = false;
  let skip2C = false;
  let skip2D = false;

  // Load existing confidence map
  let confMap: Record<string, any> = {};
  try {
    if (spotContext.field_confidence) {
      confMap = typeof spotContext.field_confidence === 'string' ? JSON.parse(spotContext.field_confidence) : spotContext.field_confidence;
    }
  } catch {}

  // Query SQLite registry for all fields dynamically
  const allFields = getFieldRegistry();

  // Map fields into their respective JIT passes (7 passes)
  const pass1ARegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass1A');
  const pass1BRegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass1B');
  const pass1CRegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass1C');
  const pass2ARegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass2A');
  const pass2BRegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass2B');
  const pass2CRegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass2C');
  const pass2DRegFields = allFields.filter(f => FIELD_PASS_MAP[f.field_name] === 'pass2D');

  // Build minimal schemas dynamically for gaps only!
  const schemaPass1A = buildPassSchema(pass1ARegFields, spotContext, confMap, 0.70, maxTier, configVectorMap, scope);
  const schemaPass1B = buildPassSchema(pass1BRegFields, spotContext, confMap, 0.70, maxTier, configVectorMap, scope);
  const schemaPass1C = buildPassSchema(pass1CRegFields, spotContext, confMap, 0.70, maxTier, configVectorMap, scope);
  const schemaPass2A = buildPassSchema(pass2ARegFields, spotContext, confMap, 0.60, maxTier, configVectorMap, scope);
  const schemaPass2B = buildPassSchema(pass2BRegFields, spotContext, confMap, 0.60, maxTier, configVectorMap, scope);
  const schemaPass2C = buildPassSchema(pass2CRegFields, spotContext, confMap, 0.60, maxTier, configVectorMap, scope);
  const schemaPass2D = buildPassSchema(pass2DRegFields, spotContext, confMap, 0.60, maxTier, configVectorMap, scope);

  if (scope === 'hours') {
    skipPass1B = skipPass1C = skip2A = skip2B = skip2C = skip2D = true;
    skipPass1A = Object.keys(schemaPass1A).length === 0;
  } else if (scope === 'pricing') {
    skipPass1A = skipPass1C = skip2A = skip2B = skip2C = skip2D = true;
    skipPass1B = Object.keys(schemaPass1B).length === 0;
  } else if (scope === 'amenities') {
    skipPass1A = skipPass1B = skipPass1C = true;
    skip2A = Object.keys(schemaPass2A).length === 0;
    skip2B = Object.keys(schemaPass2B).length === 0;
    skip2C = Object.keys(schemaPass2C).length === 0;
    skip2D = Object.keys(schemaPass2D).length === 0;
  } else {
    // scope === 'gap-fill' (default) or 'tier-N'
    skipPass1A = Object.keys(schemaPass1A).length === 0;
    skipPass1B = Object.keys(schemaPass1B).length === 0;
    skipPass1C = Object.keys(schemaPass1C).length === 0;
    skip2A = Object.keys(schemaPass2A).length === 0;
    skip2B = Object.keys(schemaPass2B).length === 0;
    skip2C = Object.keys(schemaPass2C).length === 0;
    skip2D = Object.keys(schemaPass2D).length === 0;
  }

  const safeParseJson = (str: any) => {
    try { return typeof str === 'string' ? JSON.parse(str) : str; } catch { return null; }
  };

  // Populate pass1 from DB when all 3 sub-passes are skipped
  if (skipPass1A && skipPass1B && skipPass1C) {
    pass1 = {
      hours: safeParseJson(spotContext.opening_hours),
      pricing: safeParseJson(spotContext.pricing_data),
      has_fee: spotContext.has_fee !== null ? !!spotContext.has_fee : null,
      has_rental: spotContext.has_rental !== null ? !!spotContext.has_rental : null,
      price_range: spotContext.price_range || null,
      has_adult_night: spotContext.has_adult_night !== null ? !!spotContext.has_adult_night : null,
      adult_night_schedule: safeParseJson(spotContext.adult_night_schedule),
      adult_night_details: spotContext.adult_night_details || null
    };
  } else if (skipPass1A) {
    // Partial skip: hours already resolved but pricing/adult night need extraction
    pass1.hours = safeParseJson(spotContext.opening_hours);
  }

  // ── Pre-LLM Toxicity Bouncer (Dynamic — uses unified ai_exclusion_keywords) ──
  const isTierOverride = scope.startsWith('tier-');
  let cSlice = coreText;
  let aSlice = amenityText;

  if (coreText.trim().length >= 50 && hasCrawledWebsite && !isTierOverride) {
    // 🧬 Heuristics Semantic DOM Slicing
    onProgress(`[HEURISTIC] 🧬 Applying semantic slicing to extraction text...`);
    cSlice = HeuristicsEngine.getSemanticSlice(coreText).slice.slice(0, 12000);
    aSlice = HeuristicsEngine.getSemanticSlice(amenityText).slice.slice(0, 12000);
  } else {
    if (isTierOverride) onProgress(`[HEURISTIC] ⏭️ Bypassing semantic slicing (Tier Override Active)`);
    cSlice = coreText.slice(0, 15000);
    aSlice = amenityText.slice(0, 15000);
  }
  
  combinedText = `[OPS CORE]\n${cSlice}\n\n[AMENITIES]${aSlice}`;

  if (exclusionKw.length > 0) {
    const textLower = combinedText.toLowerCase();
    const toxicHit = exclusionKw.find((kw: string) => {
      try {
        return new RegExp(`\\b${kw.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}\\b`, 'i').test(textLower);
      } catch {
        return textLower.includes(kw.toLowerCase());
      }
    });
    if (toxicHit) {
      onProgress(`[Detective] ☠️ TOXICITY ABORT: matched "${toxicHit}" — skipping all LLM passes`);
      return {
        aiMetadata: { TOXICITY_ABORT: true, reason: toxicHit },
        mappedFields: { _simulated_status: 'REJECTED' },
        combinedText: `[TOXICITY REJECTED: "${toxicHit}"]`,
        qualityScore: 0,
        passedQualityGate: false,
        candidatePhotos: null,
        socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null },
        flyerUrls,
        fieldConfidence: {}
      };
    }
  }



  if (coreText.trim().length >= 50 && hasCrawledWebsite) {
    // ── Pass 1A: Session Hours (SOLO — highest-value extraction) ──
    if (!earlyTerminated && !skipPass1A) {
      onProgress('[Detective] 🕐 LM Studio Pass 1A (Session Hours)...');
      const pass1A = await callLMStudio(buildSystem(schemaPass1A),`Website Text:\n${cSlice}`,detectiveModel,onProgress,'Pass1A-Hours',onStream);
      if(pass1A.TOXICITY_ABORT===true) return{aiMetadata:{TOXICITY_ABORT:true},mappedFields:{_simulated_status:'REJECTED'},combinedText,qualityScore:0,passedQualityGate:false,candidatePhotos:null,socialLinks:{instagram_url:null,facebook_url:null,tiktok_url:null,schedule_url:null},flyerUrls,fieldConfidence:{}};
      if (pass1A.hours) pass1.hours = pass1A.hours;
    } else if (earlyTerminated) {
      onProgress('[HEURISTIC] 🎯 Bypassing Pass 1A. Reusing early-terminated homepage results!');
    } else {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 1A (Hours): Reusing resolved database values!');
    }

    // ── Pass 1B: Pricing & Fees ──
    if (!earlyTerminated && !skipPass1B) {
      onProgress('[Detective] 💰 LM Studio Pass 1B (Pricing & Fees)...');
      const pass1B = await callLMStudio(buildSystem(schemaPass1B),`Website Text:\n${cSlice}`,detectiveModel,onProgress,'Pass1B-Pricing',onStream);
      if (pass1B.pricing) pass1.pricing = pass1B.pricing;
      if (pass1B.has_fee !== undefined) pass1.has_fee = pass1B.has_fee;
      if (pass1B.has_rental !== undefined) pass1.has_rental = pass1B.has_rental;
      if (pass1B.price_range) pass1.price_range = pass1B.price_range;
    } else if (!earlyTerminated) {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 1B (Pricing): Reusing resolved database values!');
    }

    // ── Pass 1C: Adult Night ──
    if (!earlyTerminated && !skipPass1C) {
      onProgress('[Detective] 🌙 LM Studio Pass 1C (Adult Night)...');
      const pass1C = await callLMStudio(buildSystem(schemaPass1C),`Website Text:\n${cSlice}`,detectiveModel,onProgress,'Pass1C-AdultNight',onStream);
      if (pass1C.has_adult_night !== undefined) pass1.has_adult_night = pass1C.has_adult_night;
      if (pass1C.adult_night_schedule) pass1.adult_night_schedule = pass1C.adult_night_schedule;
      if (pass1C.adult_night_details) pass1.adult_night_details = pass1C.adult_night_details;
    } else if (!earlyTerminated) {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 1C (Adult Night): Reusing resolved database values!');
    }
    
    // ── ESCALATION PROTOCOL ──
    const needsEscalation = !earlyTerminated && (!skipPass1A || !skipPass1B) && (!pass1.hours || !pass1.pricing);
    if (needsEscalation) {
      onProgress('[Detective] 🚨 ESCALATION PROTOCOL: Missing hours/pricing. Activating Hound Dog & OCR.');
      
      // 1. Gated Flyer OCR (runs only during escalation!)
      if (flyerUrls.length > 0) {
        onProgress(`[Detective] 🚨 Running flyer OCR on ${Math.min(5, flyerUrls.length)} discovered flyers...`);
        for (const fUrl of [...new Set(flyerUrls)].slice(0, 5)) {
          try {
            const cleanUrl = fUrl.split('?')[0].toLowerCase();
            const isOcrCompatible = cleanUrl.endsWith('.jpg') || cleanUrl.endsWith('.jpeg') || cleanUrl.endsWith('.png');
            if (isOcrCompatible) {
              const buf = await downloadImageBuffer(fUrl);
              // Dimension + magic byte guard prevents Leptonica worker crashes
              if (buf && buf.length >= 2048 && isValidJpegOrPng(buf)) {
                try {
                  const { data: { text } } = await Tesseract.recognize(buf, 'eng');
                  if (text && text.trim().length > 20) {
                    const ocrTxt = `\n\n[OCR from Flyer Image: ${fUrl}]\n${text}\n\n`;
                    coreText = ocrTxt + coreText;
                    amenityText = ocrTxt + amenityText;
                    onProgress(`[Detective] ✓ Extracted flyer OCR: ${fUrl}`);
                    escalationRan = true;
                  }
                } catch { /* Tesseract worker crash — swallow and continue */ }
              } else {
                onProgress(`[Detective] Skipping flyer OCR: Image too small or invalid format: ${fUrl}`);
              }
            }
          } catch (e: any) {
            onProgress(`[Detective] Flyer OCR failed for ${fUrl}: ${e.message}`);
          }
        }
      }

      if (browser) {
        const ESCALATION_RULES = [/hours/i, /schedule/i, /pricing/i, /rates/i, /admission/i, /calendar/i, /adult.?night/i, /18\+/i, /21\+/i];
        const undiscovered = allLinks.filter(l => ESCALATION_RULES.some(r => r.test(l.href) || r.test(l.text)))
                                     .map(l => l.href)
                                     .filter(href => !coreText.includes(`[PAGE: ${href}]`));
        
        const targets = [...new Set(undiscovered)].slice(0, 2);
        if (targets.length > 0) {
          escalationRan = true;
          const pages = await browser.pages();
          const escPage = pages.length > 0 ? pages[0] : await browser.newPage();
          
          for (const targetUrl of targets) {
            onProgress(`[Detective] 🚨 Escalation deep crawl -> ${targetUrl}`);
            await escPage.goto(targetUrl, { waitUntil: 'domcontentloaded', timeout: 25000 }).catch(()=>{});
            await autoScroll(escPage);
            
            try {
              onProgress(`[Detective] 📸 Visual Snapshot taken. Running Tesseract OCR...`);
              const screenshotBuffer = await escPage.screenshot({ fullPage: true, type: 'jpeg', quality: 80 });
              if (screenshotBuffer && screenshotBuffer.length >= 2048 && isValidJpegOrPng(screenshotBuffer)) {
                try {
                  const { data: { text } } = await Tesseract.recognize(screenshotBuffer, 'eng');
                  if (text && text.trim().length > 50) {
                    const ocrTxt = `\n\n[ESCALATION OCR from ${targetUrl}]\n${text}\n\n`;
                    coreText = ocrTxt + coreText;
                    amenityText = ocrTxt + amenityText;
                  }
                } catch { /* Tesseract worker crash — swallow and continue */ }
              } else {
                onProgress(`[Detective] Skipping screenshot OCR: buffer too small or invalid.`);
              }
            } catch(e:any) {
              onProgress(`[Detective] Screenshot capture failed: ${e.message}`);
            }
          }
        } else {
          onProgress('[Detective] 🚨 No undiscovered priority links found for escalation.');
        }
      }

      if (escalationRan) {
        const cSlice2 = coreText.slice(0, 12000);
        // Retry only the sub-passes that failed
        if (!pass1.hours) {
          onProgress('[Detective] 🚨 Re-running Pass 1A (Hours) with OCR context...');
          const retry1A = await callLMStudio(buildSystem(schemaPass1A),`Website Text:\n${cSlice2}`,detectiveModel,onProgress,'Pass1A-Retry',onStream);
          if (retry1A.hours) pass1.hours = retry1A.hours;
        }
        if (!pass1.pricing) {
          onProgress('[Detective] 🚨 Re-running Pass 1B (Pricing) with OCR context...');
          const retry1B = await callLMStudio(buildSystem(schemaPass1B),`Website Text:\n${cSlice2}`,detectiveModel,onProgress,'Pass1B-Retry',onStream);
          if (retry1B.pricing) pass1.pricing = retry1B.pricing;
          if (retry1B.has_fee !== undefined) pass1.has_fee = retry1B.has_fee;
          if (retry1B.has_rental !== undefined) pass1.has_rental = retry1B.has_rental;
        }
      }
    }

    // Fix #9: Split monolithic Pass 2 into 4 highly focused sub-passes
    // Fix #11: Use enriched text if escalation ran
    const pass2Slice = escalationRan ? amenityText.slice(0, 12000) : aSlice;

    // Fetch Yelp Reviews with Puppeteer if browser is active and we need Pass 2A or Pass 2B
    let yelpReviews = '';
    if (browser && (!skip2A || !skip2B)) {
      try {
        yelpReviews = await fetchYelpReviewsWithPuppeteer(browser, spotContext.name, spotContext.city, spotContext.state, spotContext.yelp_url || undefined, onProgress);
      } catch (e: any) {
        onProgress(`[Yelp-Miner] ⚠️ Review extraction error: ${e.message}`);
      }
    }

    let pass2A: any = null;
    if (skip2A) {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 2A (Floor & Physics): Reusing resolved database values!');
      pass2A = {
        surface_type: spotContext.surface_type || null,
        surface_quality: spotContext.surface_quality || null,
        vibe_score: spotContext.vibe_score !== null ? Number(spotContext.vibe_score) : null
      };
    } else {
      onProgress('[Detective] LM Studio Pass 2A (Floor & Physics)...');
      const pass2ASlice = yelpReviews ? `${yelpReviews}\n\n${pass2Slice}` : pass2Slice;
      pass2A = await callLMStudio(buildSystem(schemaPass2A), `Website Text & Reviews:\n${pass2ASlice}`, detectiveModel, onProgress, 'Pass2A', onStream);
      if (pass2A.TOXICITY_ABORT === true) return { aiMetadata: { TOXICITY_ABORT: true }, mappedFields: { _simulated_status: 'REJECTED' }, combinedText, qualityScore: 0, passedQualityGate: false, candidatePhotos: null, socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null }, flyerUrls, fieldConfidence: {} };
    }

    let pass2B: any = null;
    if (skip2B) {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 2B (Amenities & Services): Reusing resolved database values!');
      pass2B = {
        is_indoor: spotContext.is_indoor !== null ? !!spotContext.is_indoor : null,
        has_rental: spotContext.has_rental !== null ? !!spotContext.has_rental : null,
        has_pro_shop: spotContext.has_pro_shop !== null ? !!spotContext.has_pro_shop : null,
        has_food: spotContext.has_food !== null ? !!spotContext.has_food : null,
        has_lights: spotContext.has_lights !== null ? !!spotContext.has_lights : null,
        has_lockers: spotContext.has_lockers !== null ? !!spotContext.has_lockers : null,
        has_ac: spotContext.has_ac !== null ? !!spotContext.has_ac : null,
        has_wifi: spotContext.has_wifi !== null ? !!spotContext.has_wifi : null,
        has_toilets: spotContext.has_toilets !== null ? !!spotContext.has_toilets : null,
        capacity: spotContext.capacity !== null ? Number(spotContext.capacity) : null
      };
    } else {
      onProgress('[Detective] LM Studio Pass 2B (Amenities & Services)...');
      const pass2BSlice = yelpReviews ? `${yelpReviews}\n\n${pass2Slice}` : pass2Slice;
      pass2B = await callLMStudio(buildSystem(schemaPass2B), `Website Text & Reviews:\n${pass2BSlice}`, detectiveModel, onProgress, 'Pass2B', onStream);
      if (pass2B.TOXICITY_ABORT === true) return { aiMetadata: { TOXICITY_ABORT: true }, mappedFields: { _simulated_status: 'REJECTED' }, combinedText, qualityScore: 0, passedQualityGate: false, candidatePhotos: null, socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null }, flyerUrls, fieldConfidence: {} };
    }

    let pass2C: any = null;
    if (skip2C) {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 2C (Accessibility & Culture): Reusing resolved database values!');
      const safeParseJson = (str: any) => {
        try { return typeof str === 'string' ? JSON.parse(str) : str; } catch { return null; }
      };
      pass2C = {
        is_wheelchair_accessible: spotContext.is_wheelchair_accessible !== null ? !!spotContext.is_wheelchair_accessible : null,
        hosts_derby: spotContext.hosts_derby !== null ? !!spotContext.hosts_derby : null,
        special_events: safeParseJson(spotContext.special_events) || null,
        operator_name: spotContext.operator_name || null,
        operator_description: spotContext.operator_description || null,
        cultural_metadata: safeParseJson(spotContext.cultural_metadata) || spotContext.cultural_metadata || null,
        adult_night_details: spotContext.adult_night_details || null
      };
    } else {
      onProgress('[Detective] LM Studio Pass 2C (Accessibility & Culture)...');
      pass2C = await callLMStudio(buildSystem(schemaPass2C), `Website Text:\n${pass2Slice}`, detectiveModel, onProgress, 'Pass2C', onStream);
      if (pass2C.TOXICITY_ABORT === true) return { aiMetadata: { TOXICITY_ABORT: true }, mappedFields: { _simulated_status: 'REJECTED' }, combinedText, qualityScore: 0, passedQualityGate: false, candidatePhotos: null, socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null }, flyerUrls, fieldConfidence: {} };
    }

    let pass2D: any = null;
    if (skip2D) {
      onProgress('[JIT-Scheduler] 🎯 Bypassing Pass 2D (Socials & Contacts): Reusing resolved database values!');
      const safeParseJson = (str: any) => {
        try { return typeof str === 'string' ? JSON.parse(str) : str; } catch { return null; }
      };
      pass2D = {
        instagram_url: spotContext.instagram_url || null,
        facebook_url: spotContext.facebook_url || null,
        tiktok_url: spotContext.tiktok_url || null,
        schedule_url: spotContext.schedule_url || null,
        yelp_url: spotContext.yelp_url || null,
        price_range: spotContext.price_range || null,
        logo_url: spotContext.logo_url || null,
        email_addresses: safeParseJson(spotContext.email_addresses) || null
      };
    } else {
      onProgress('[Detective] LM Studio Pass 2D (Socials & Contacts)...');
      pass2D = await callLMStudio(buildSystem(schemaPass2D), `Website Text:\n${pass2Slice}`, detectiveModel, onProgress, 'Pass2D', onStream);
      if (pass2D.TOXICITY_ABORT === true) return { aiMetadata: { TOXICITY_ABORT: true }, mappedFields: { _simulated_status: 'REJECTED' }, combinedText, qualityScore: 0, passedQualityGate: false, candidatePhotos: null, socialLinks: { instagram_url: null, facebook_url: null, tiktok_url: null, schedule_url: null }, flyerUrls, fieldConfidence: {} };
    }

    pass2 = { ...pass2A, ...pass2B, ...pass2C, ...pass2D };
  } else {
    onProgress('[Detective] Content too short or skipped. Bypassing Website LLM Passes.');
  }

  // ── PHASE 3: EXTERNAL ENRICHMENT ──
  const needsEnrichment = !hasWebsite || isSocialOnly || !pass1.hours || !pass1.pricing || (pass1.has_adult_night === true && !pass1.adult_night_schedule);

  if (needsEnrichment) {
    onProgress('[Detective] 🚨 ENRICHMENT PHASE: Fetching Yelp, Facebook, and Google Places...');
    yelpData = await fetchYelpData(spotContext.name, spotContext.city, spotContext.state, spotContext.yelp_url);
    if (yelpData.text) externalText += '\n\n' + yelpData.text;
    await sleep(EXTERNAL_SOURCE_DELAY);

    onProgress('[Detective] Fetching Facebook...');
    fbData = await fetchFacebookData(spotContext.facebook_url || null);
    if (fbData.text) externalText += '\n\n' + fbData.text;
    await sleep(EXTERNAL_SOURCE_DELAY);

    onProgress('[Detective] Fetching Google Places...');
    const gt = await fetchGooglePlacesWeb(spotContext.google_place_id || null);
    if (gt) externalText += '\n\n' + gt;

    if (externalText.trim().length > 50) {
      onProgress('[Detective] LM Studio Pass 3 (Enrichment Fallback)...');
      const eSlice = externalText.slice(0, 4000);
      const schemaPass3 = { ...schemaPass1A, ...schemaPass1B, ...schemaPass1C, ...schemaPass2A, ...schemaPass2B, ...schemaPass2C, ...schemaPass2D };
      const pass3 = await callLMStudio(buildSystem(schemaPass3), `External Fallback Text:\n${eSlice}`, detectiveModel, onProgress, 'Pass3-Enrichment', onStream);
      
      // Merge without overwriting official website data
      for (const k in pass3) {
        if (pass1[k] === undefined || pass1[k] === null) pass1[k] = pass3[k];
        if (pass2[k] === undefined || pass2[k] === null) pass2[k] = pass3[k];
      }
      combinedText += `\n\n[EXTERNAL FALLBACKS]\n${eSlice}`;
    }
  }

  // Fix #2: JSON-LD fields fill gaps — LLM wins, JSON-LD is fallback
  aiMetadata = { ...jsonLdFields, ...pass2, ...pass1 };

  const opening_hours=normalizeHours(aiMetadata.hours||aiMetadata.opening_hours||spotContext.opening_hours||null);
  const pricing_data=normalizePricing(aiMetadata.pricing||aiMetadata.pricing_data||spotContext.pricing_data);
  const surface_type=safeSurface(aiMetadata.surface_type||spotContext.surface_type);
  const surface_quality=aiMetadata.surface_quality||spotContext.surface_quality||null;
  const vibe_score=safeNum(aiMetadata.vibe_score??spotContext.vibe_score);
  const is_indoor=safeBool(aiMetadata.is_indoor??spotContext.is_indoor);
  let has_fee=safeBool(aiMetadata.has_fee??spotContext.has_fee);
  if (has_fee === null && pricing_data && (typeof pricing_data.adult === 'number' || typeof pricing_data.child === 'number')) {
    has_fee = true;
  }
  const has_adult_night=safeBool(aiMetadata.has_adult_night??spotContext.has_adult_night)??false;
  const adult_night_details=has_adult_night?aiMetadata.adult_night_details||spotContext.adult_night_details||null:null;
  const adultNightSchedule=has_adult_night?aiMetadata.adult_night_schedule||spotContext.adult_night_schedule||null:null;
  const special_events=aiMetadata.special_events||spotContext.special_events||null;
  const capacity=safeNum(aiMetadata.capacity??spotContext.capacity);
  let has_rental=safeBool(aiMetadata.has_rental??spotContext.has_rental);
  if (has_rental === null && pricing_data && typeof pricing_data.skate_rental === 'number') {
    has_rental = true;
  }
  const has_pro_shop=safeBool(aiMetadata.has_pro_shop??spotContext.has_pro_shop);
  const has_food=safeBool(aiMetadata.has_food??spotContext.has_food);
  const has_lights=safeBool(aiMetadata.has_lights??spotContext.has_lights);
  const has_lockers=safeBool(aiMetadata.has_lockers??spotContext.has_lockers);
  const has_ac=safeBool(aiMetadata.has_ac??spotContext.has_ac);
  const has_wifi=safeBool(aiMetadata.has_wifi??spotContext.has_wifi);
  const has_toilets=safeBool(aiMetadata.has_toilets??spotContext.has_toilets);
  const is_wheelchair_accessible=safeBool(aiMetadata.wheelchair??aiMetadata.is_wheelchair_accessible??spotContext.is_wheelchair_accessible);
  const hosts_derby=safeBool(aiMetadata.derby??aiMetadata.hosts_derby??spotContext.hosts_derby);
  const cultural_metadata=aiMetadata.cultural_meta||aiMetadata.cultural_metadata||spotContext.cultural_metadata||null;
  const operator_description=aiMetadata.operator_description||spotContext.operator_description||null;
  const operator_name=aiMetadata.operator_name||spotContext.operator_name||null;
  const instagram_url=aiMetadata.instagram_url||spotContext.instagram_url||null;
  const facebook_url=aiMetadata.facebook_url||spotContext.facebook_url||null;
  const tiktok_url=aiMetadata.tiktok_url||spotContext.tiktok_url||null;
  const schedule_url=aiMetadata.schedule_url||spotContext.schedule_url||null;
  const yelp_url=aiMetadata.yelp_url||spotContext.yelp_url||null;
  const price_range=aiMetadata.price_range||null;
  const logo_url=aiMetadata.logo_url||null;

  // ── 3-Layer Email Merge ────────────────────────────────────────────────────
  const regexEmails = extractEmails(coreText + '\n' + amenityText + '\n' + externalText + '\n' + combinedText);
  const aiEmails: string[] = Array.isArray(aiMetadata.email_addresses)
    ? aiMetadata.email_addresses.map((e: string) => e.toLowerCase().trim())
    : (typeof aiMetadata.email_addresses === 'string' ? [aiMetadata.email_addresses.toLowerCase().trim()] : []);
  const existingEmails: string[] = Array.isArray(spotContext.email_addresses)
    ? spotContext.email_addresses
    : (typeof spotContext.email_addresses === 'string' ? (() => { try { return JSON.parse(spotContext.email_addresses); } catch { return []; } })() : []);
  const email_addresses = [...new Set([
    ...allMailtos,
    ...regexEmails,
    ...aiEmails,
    ...existingEmails,
  ])].filter(e => e && e.includes('@')).sort();
  if (email_addresses.length > 0) onProgress(`[Detective] 📧 Captured ${email_addresses.length} email(s): ${email_addresses.join(', ')}`);

  let candidatePhotos:Record<string,any>={};
  try{candidatePhotos=typeof spotContext.candidate_photos==='string'?JSON.parse(spotContext.candidate_photos):(spotContext.candidate_photos||{});}catch{}
  if(!spotContext.photos){
    if(ogImage&&!candidatePhotos.og_image) candidatePhotos.og_image=ogImage;
    if(domImages.length) candidatePhotos.dom_images=[...new Set(domImages.map((i:any)=>i.src))].slice(0,10);
    if(sitemap.gallery_urls?.length) candidatePhotos.gallery_urls=sitemap.gallery_urls;
    if(fbData.photos_url) candidatePhotos.facebook_photos_url=fbData.photos_url;
    if(fbData.cover_photo) candidatePhotos.cover_photo_url=fbData.cover_photo;
    if(yelpData.photos_url) candidatePhotos.yelp_photos_url=yelpData.photos_url;
    if(logo_url) candidatePhotos.logo_url=logo_url;
    // Fix #8: Inject Google Places photo references
    if(googlePhotoRefs.length > 0) candidatePhotos.google_refs = googlePhotoRefs;
  }
  if(Object.keys(candidatePhotos).length===0) candidatePhotos=null as any;

  const qFields=[opening_hours,pricing_data,operator_name,operator_description,surface_type,surface_quality,vibe_score,is_indoor,capacity,has_fee,has_rental,has_pro_shop,has_food,has_lights,has_ac,has_lockers,has_toilets,has_wifi,is_wheelchair_accessible,has_adult_night,adult_night_details,adultNightSchedule,special_events,hosts_derby,instagram_url,facebook_url,tiktok_url,cultural_metadata];
  // Fix #3: false is VALID data (e.g. has_rental:false = confirmed no rental), don't penalize it
  const qualityScore=qFields.filter(f=>f!==null&&f!==undefined).length;
  // FIX: Truthy check — LM can return hours as a string OR object; Object.keys() throws on strings
  const hasHighValueField=!!(opening_hours)||!!(pricing_data);
  const passedQualityGate=qualityScore>=3&&hasHighValueField;
  onProgress(`[Detective] Quality[${qualityScore}/${qFields.length}]`);
  onProgress(passedQualityGate?'[Detective] Quality gate passed.':'[Detective] Low quality — emitting DEEP_CRAWLED anyway.');

  const mappedFieldsRaw={is_indoor,operator_description,operator_name,instagram_url,facebook_url,tiktok_url,schedule_url,opening_hours,adult_night_schedule:adultNightSchedule,has_adult_night,adult_night_details,special_events,pricing_data,has_fee,surface_type,surface_quality,vibe_score,capacity,has_rental,has_pro_shop,has_food,has_lights,has_lockers,has_ac,has_wifi,has_toilets,is_wheelchair_accessible,hosts_derby,cultural_metadata,yelp_url,price_range,logo_url,email_addresses:email_addresses.length>0?email_addresses:null,candidate_photos:candidatePhotos,ai_metadata:Object.keys(aiMetadata).length>0?aiMetadata:null,_simulated_status:passedQualityGate?'DEEP_CRAWLED':'LOW_QUALITY'};
  const mappedFields=sanitize(mappedFieldsRaw);
  
  // ── Build per-field confidence map ──────────────────────────────────────────
  const now = new Date().toISOString();
  const fieldConfidence: Record<string, { source: string; confidence: number; extracted_at: string }> = {};

  const tag = (field: string, source: string, confidence: number) => {
    if (mappedFields[field] !== null && mappedFields[field] !== undefined) {
      fieldConfidence[field] = { source, confidence, extracted_at: now };
    }
  };

  // JSON-LD parsed fields (highest automated confidence: 0.90)
  const jsonLdFieldNames = Object.keys(jsonLdFields);
  const JSON_LD_FIELD_MAP: Record<string, string> = {
    hours: 'opening_hours', phone_number: 'phone_number', price_range: 'price_range',
    logo_url: 'logo_url', street_address: 'street_address'
  };
  for (const jf of jsonLdFieldNames) {
    const mapped = JSON_LD_FIELD_MAP[jf] || jf;
    // Only tag as json_ld if the final value actually came from JSON-LD (not overridden by LLM)
    if (aiMetadata[jf] === jsonLdFields[jf]) tag(mapped, 'json_ld', 0.90);
  }

  // LLM Pass 1A: Session Hours — confidence 0.70
  if (!fieldConfidence['opening_hours']) tag('opening_hours', 'llm_pass1A', 0.70);

  // LLM Pass 1B: Pricing & Fees — confidence 0.70
  for (const f of ['pricing_data', 'has_fee', 'has_rental', 'price_range']) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass1B', 0.70);
  }

  // LLM Pass 1C: Adult Night — confidence 0.70
  for (const f of ['has_adult_night', 'adult_night_schedule', 'adult_night_details']) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass1C', 0.70);
  }

  // LLM Pass 2A: Floor & Vibe — confidence 0.60
  for (const f of ['surface_type','surface_quality','vibe_score']) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass2A', 0.60);
  }

  // LLM Pass 2B: Amenities — confidence 0.60
  for (const f of ['is_indoor','has_pro_shop','has_food','has_lights','has_lockers','has_ac','has_wifi','has_toilets','capacity']) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass2B', 0.60);
  }

  // LLM Pass 2C: Identity & Culture — confidence 0.60
  for (const f of ['is_wheelchair_accessible','hosts_derby','special_events','operator_name','operator_description','cultural_metadata']) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass2C', 0.60);
  }

  // LLM Pass 2D: Contacts & Socials — confidence 0.60
  for (const f of ['instagram_url','facebook_url','tiktok_url','schedule_url','yelp_url','logo_url']) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass2D', 0.60);
  }

  // Regex/mailto extraction (high confidence: 0.80)
  if (email_addresses.length > 0 && !fieldConfidence['email_addresses']) {
    tag('email_addresses', 'regex_extraction', 0.80);
  }

  const confSummary = Object.entries(fieldConfidence).reduce((acc, [, v]) => {
    acc[v.source] = (acc[v.source] || 0) + 1; return acc;
  }, {} as Record<string, number>);
  onProgress(`[Detective] 🎯 Confidence: ${Object.entries(confSummary).map(([s,c]) => `${s}(${c})`).join(' ')}`);

  return {aiMetadata,mappedFields,combinedText,qualityScore,passedQualityGate,candidatePhotos,socialLinks:{instagram_url,facebook_url,tiktok_url,schedule_url},flyerUrls,fieldConfidence};
  } finally {
    if(browser){try{await browser.close();}catch{}}
  }
}
