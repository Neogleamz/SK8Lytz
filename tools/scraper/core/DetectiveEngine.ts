/**
 * DetectiveEngine.ts — Phase 2 AI Detective (Sitemap-First, Multi-Source)
 * v2 Refactor: sitemap navigation + Yelp/Facebook/Google pre-crawl + tiered LLM extraction
 * ZERO DB WRITES — pure function called by Indexer.ts and SniperBench.
 */

import puppeteer from 'puppeteer';
import Tesseract from 'tesseract.js';
import pdfParse from 'pdf-parse';
import { parseSitemap } from './SitemapParser';

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

// ─── Pre-Crawl External Source Fetchers (no Puppeteer) ───────────────────────

async function fetchExternalText(url: string, label: string, onProgress?: (msg: string) => void): Promise<string> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36' },
      signal: AbortSignal.timeout(10000),
    });
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
    const bodyText = html.replace(/<[^>]+>/g,' ').replace(/\s{2,}/g,' ').trim().slice(0, 8000);
    let result = `[${label}: ${url}]\n`;
    if (jsonLdMatches.length) result += `JSON-LD: ${jsonLdMatches.join('\n')}\n`;
    if (ogTitle) result += `Title: ${ogTitle}\n`;
    if (ogImage) result += `og:image: ${ogImage}\n`;
    result += bodyText;
    onProgress?.(`[Detective] ✓ ${label}: ${bodyText.length} chars, ${jsonLdMatches.length} JSON-LD blocks`);
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
  passLabel: string
): Promise<Record<string, any>> {
  const LM_STUDIO_URL = 'http://localhost:1234/v1/chat/completions';
  const payload = { model, messages: [{ role:'system', content: systemMessage },{ role:'user', content: userMessage }], temperature: 0.1, stream: false };
  for (let attempt = 1; attempt <= 2; attempt++) {
    try {
      const fetchFn = require('node-fetch');
      const res = await fetchFn(LM_STUDIO_URL, { 
        method:'POST', 
        headers:{'Content-Type':'application/json'}, 
        body: JSON.stringify(payload),
        signal: AbortSignal.timeout(120000)
      });
      if (!res.ok) throw new Error(`LM Studio: ${res.statusText}`);
      const data = await res.json();
      const raw = data.choices?.[0]?.message?.content || '{}';
      try {
        const parsed = JSON.parse(raw.replace(/```json/g,'').replace(/```/g,'').trim());
        onProgress(`[Detective] ✓ LM Studio ${passLabel} complete. Keys: ${Object.keys(parsed).length}`);
        return parsed;
      } catch {
        onProgress(`[Detective] ⚠️ JSON repair needed (${passLabel})...`);
        return repairJSON(raw);
      }
    } catch (err: any) {
      onProgress(`[Detective] ✗ LM Studio attempt ${attempt}/2 failed: ${err.message}`);
      if (attempt < 2) { onProgress(`[Detective] ⏳ Retrying in 3s...`); await sleep(3000); }
    }
  }
  return {};
}


// ─── Page Crawl Helper ───────────────────────────────────────────────────────

async function crawlPage(page: any, url: string, onProgress: (m: string) => void): Promise<{ text:string; jsonLd:string; ogImage:string|null; images:Array<{src:string;alt:string;parentClass:string}>; iframes:string[]; links:Array<{href:string;text:string}>; mailtos:string[]; fullText:string; }> {
  const empty = { text:'', jsonLd:'', ogImage:null, images:[], iframes:[], links:[], mailtos:[], fullText:'' };
  if (url.toLowerCase().endsWith('.pdf')) {
    try { const fetchFn = require('node-fetch'); const buf = await (await fetchFn(url)).buffer(); const pdfData = await pdfParse(buf); return { ...empty, text:`[PDF: ${url}]\n${pdfData.text}` }; } catch { return empty; }
  }
  if (/\.(png|jpg|jpeg|gif|webp)(\?.*)?$/i.test(url)) {
    try { const { data:{text} } = await Tesseract.recognize(url,'eng'); return { ...empty, text:text?.length>20?`[OCR Image: ${url}]\n${text}`:'' }; } catch { return empty; }
  }
  try {
    await page.goto(url,{waitUntil:'domcontentloaded',timeout:30000}).catch(()=>page.goto(url,{waitUntil:'domcontentloaded',timeout:15000}));
    await autoScroll(page);
    const d = await page.evaluate(()=>{
      const ogImage=document.querySelector('meta[property="og:image"]')?.getAttribute('content')||null;
      const jsonLd=Array.from(document.querySelectorAll('script[type="application/ld+json"]')).map((e:any)=>e.innerText).join('\n');
      const iframes=Array.from(document.querySelectorAll('iframe')).map((e:any)=>e.src||'').filter((s:string)=>s.includes('calendar')||s.includes('ticket')||s.includes('centeredge')||s.includes('roller'));
      const images=Array.from(document.querySelectorAll('img')).filter((i:any)=>{const w=i.naturalWidth||i.width||0,h=i.naturalHeight||i.height||0;return w>=400&&h>=300&&i.src&&(i.src.startsWith('http')||i.src.startsWith('//'));}).map((i:any)=>({src:i.src,alt:(i.alt||'').toLowerCase(),parentClass:(i.parentElement?.className||'').toLowerCase()}));
      const links=Array.from(document.querySelectorAll('a')).map((a:any)=>({href:(a.href||'').toLowerCase(),text:(a.innerText||'').toLowerCase()})).filter((l:any)=>l.href&&(l.href.startsWith('http')||l.href.startsWith('//')));
      const mailtos=Array.from(document.querySelectorAll('a[href^="mailto:"]')).map((a:any)=>a.href.replace('mailto:','').split('?')[0].trim().toLowerCase()).filter((e:string)=>e&&e.includes('@'));
      const fullText=document.body?.innerText?.replace(/\n+/g,' ').replace(/\s{2,}/g,' ').trim()||'';
      document.querySelectorAll('nav,footer,script,style,header,iframe,noscript').forEach(el=>el.remove());
      const text=document.body?.innerText?.replace(/\n+/g,' ').replace(/\s{2,}/g,' ').trim()||'';
      return {ogImage,jsonLd,iframes,images,links,mailtos,text,fullText};
    }).catch(()=>({ogImage:null,jsonLd:'',iframes:[],images:[],links:[],mailtos:[],text:'',fullText:''}));
    return {text:d.text,jsonLd:d.jsonLd,ogImage:d.ogImage,images:d.images,iframes:d.iframes,links:d.links,mailtos:d.mailtos,fullText:d.fullText};
  } catch { onProgress(`[Detective] Nav failed: ${url}`); return empty; }
}

// ─── Main Detective Function ─────────────────────────────────────────────────

export async function executeDetective(
  spotContext: any, aiConfig: any, isHeadless: boolean, onProgress: (msg:string)=>void=()=>{}
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

  try {
    // ── Always fetch external sources regardless of website type ──
    const isSocialOnly = spotContext.website && isSocialCrawlBlocked(spotContext.website);
    const hasWebsite = !!spotContext.website;

    // Yelp + Google Places fetched for ALL records (Fix #4: social-only still gets data)
    onProgress('[Detective] Fetching Yelp...');
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

    if (!hasWebsite) {
      onProgress('[Detective] No website — using external sources only.');
      coreText = `Facility: ${spotContext.name}. ${spotContext.city}, ${spotContext.state}.\n` + externalText;
      amenityText = coreText;
    } else if (isSocialOnly) {
      onProgress('[Detective] Social-only website — using external sources only.');
      coreText = `Facility: ${spotContext.name}. ${spotContext.city}, ${spotContext.state}. Social media only.\n` + externalText;
      amenityText = coreText;
    } else {
    onProgress('[Detective] Fetching sitemap...');
    sitemap=await parseSitemap(spotContext.website);
    onProgress(`[Detective] 🕷️ Sitemap: schedule(${sitemap.schedule_urls.length}) pricing(${sitemap.pricing_urls.length}) contact(${sitemap.contact_urls.length}) gallery(${sitemap.gallery_urls.length}) about(${sitemap.about_urls.length}) total(${sitemap.all_urls.length})`);

    browser=await puppeteer.launch({headless:isHeadless?'new':false,protocolTimeout:60000,args:['--no-sandbox','--disable-setuid-sandbox','--disable-dev-shm-usage']});
    const page=await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
      await page.setViewport({width:1280,height:800});
      // Fix #7: Include gallery_urls in targeted crawl (SitemapParser spider handles discovery)
      const targeted=[...new Set([...sitemap.schedule_urls.slice(0,3),...sitemap.pricing_urls.slice(0,3),...sitemap.about_urls.slice(0,2),...sitemap.events_urls.slice(0,2),...sitemap.contact_urls.slice(0,3),...sitemap.gallery_urls.slice(0,2)])];
      const crawlUrls=[spotContext.website,...targeted].slice(0,MAX_PAGES_PER_RECORD);
      const PAGE_SCORE_RULES=[{pattern:/hours|schedule|session|times|calendar|events|open.?skate/i,score:10},{pattern:/adult.?night|18\+|21\+/i,score:10},{pattern:/pricing|price|admission|rates|tickets/i,score:9},{pattern:/about|story|history|facility|rink/i,score:8},{pattern:/contact|location|directions|email|info/i,score:10}];
      
      let hostname='';try{hostname=new URL(spotContext.website).hostname;}catch{}
      for(const url of crawlUrls){
        onProgress(`[Detective] -> ${url}`);
        const pg=await crawlPage(page,url,onProgress);
        const isOpsUrl = url === spotContext.website || /schedule|pricing|admission|rates|calendar|events/i.test(url);
        const isAmenityUrl = url === spotContext.website || /about|story|history|facility|rink/i.test(url);

        // Fix #1: Targeted pages go into priorityText (prepended), NOT appended to external noise
        if(pg.jsonLd) {
          const ld = `\n\n[JSON-LD: ${url}]\n${pg.jsonLd}`;
          if(isOpsUrl) priorityText += ld;
          if(isAmenityUrl) amenityText += ld;
        }
        if(pg.text) {
          const txt = `\n\n[PAGE: ${url}]\n${pg.text}`;
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
            const igTxt = `\n\n[IFRAME:${iframe}]\n${ig.text}`;
            coreText+=igTxt; amenityText+=igTxt;
          }
        }
      }
      if(targeted.length===0){
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
      for(const fUrl of [...new Set(flyerUrls)].slice(0,5)){
        if(coreText.includes(`[OCR from Flyer Image: ${fUrl}]`)) continue;
        try{
          const{data:{text}}=await Tesseract.recognize(fUrl,'eng');
          if(text?.length>20) {
            const ocrTxt = `[OCR from Flyer Image: ${fUrl}]\n${text}\n\n`;
            coreText = ocrTxt + coreText;
            amenityText = ocrTxt + amenityText;
            onProgress(`[Detective] Pre-pended OCR text from flyer: ${fUrl}`);
          }
        }catch{}
      }
    // Fix #1: Assemble final text — priority pages FIRST, external noise LAST
    coreText = priorityText + '\n\n' + externalText;
    amenityText = priorityText + '\n\n' + externalText;
    }
  } catch (err: any) {
    onProgress(`[Detective] Pre-crawl phase error: ${err.message}`);
    // Ensure coreText has at least external data if crawl crashed
    if (!coreText && externalText) { coreText = externalText; amenityText = externalText; }
  }

  try {
  const userVectors=aiConfig.ai_target_vectors||[];
  const userSchema=userVectors.reduce((acc:any,vec:any)=>{acc[vec.key]=vec.prompt||vec.type;return acc;},{});
  const REQUIRED_SCHEMA={hours:'Complete weekly public skating schedule for ALL 7 DAYS {Monday: time_range, Tuesday: time_range, ...}. Include every day even if closed.',pricing:'All admission fees {adult,child,senior,spectator,skate_rental}.',has_fee:'boolean or null — return null if no pricing/fee information was found. DO NOT assume free.',has_adult_night:'boolean or null — return null if no adult night information was found.',adult_night_schedule:'If adult nights: {day:time_range}. Null if none.'};
  const FULL_SCHEMA:Record<string,string>={surface_type:'Floor: wood/maple/concrete/asphalt/sport_court/synthetic.',surface_quality:'Condition 3-5 words.',vibe_score:'0-100.',is_indoor:'boolean',has_rental:'boolean',has_pro_shop:'boolean',has_food:'boolean',has_lights:'boolean',has_lockers:'boolean',has_ac:'boolean',has_wifi:'boolean',has_toilets:'boolean',wheelchair:'boolean',derby:'boolean',capacity:'integer.',special_events:'Array.',operator_name:'Owner name.',operator_description:'1-2 sentences.',cultural_meta:'Significance or null.',adult_night_details:'Details or null.',instagram_url:'URL or null.',facebook_url:'URL or null.',tiktok_url:'URL or null.',schedule_url:'URL or null.',yelp_url:'URL or null.',price_range:'$ to $$$$ or null.',logo_url:'Logo URL or null.',...userSchema};
  const exclusionKw=aiConfig.ai_exclusion_keywords||[];
  const usp=aiConfig.ai_system_prompt||'';
  const buildSystem=(schema:Record<string,string>,ctx?:string)=>{
    let s=`You are a data extraction agent for [${spotContext.name}] in [${spotContext.city}].\nONLY this location. Valid JSON only.\n`;
    s+=`CRITICAL BOOLEAN RULE: For ALL boolean fields, you MUST return null if the information was not explicitly found in the text. Do NOT assume false. Do NOT infer. A missing fee schedule does NOT mean admission is free. A missing amenity mention does NOT mean it is absent. Return null to indicate unknown.\n`;
    if(usp) s+=usp+'\n';
    if(exclusionKw.length) s+=`TOXICITY: If PRIMARY business matches [${exclusionKw.join(',')}] return {"TOXICITY_ABORT":true}.\n`;
    if(ctx) s+=`PASS 1 CONTEXT: ${ctx}\n`;
    return s+`SCHEMA:\n${JSON.stringify(schema,null,2)}`;
  };

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

  if(coreText.trim().length>=50){
    const cSlice=coreText.slice(0, 24000);
    const aSlice=amenityText.slice(0, 24000);
    combinedText = `[OPS CORE]\n${cSlice}\n\n[AMENITIES]\n${aSlice}`;
    
    onProgress('[Detective] LM Studio Pass 1 (Ops: hours/pricing/adult-night)...');
    const pass1=await callLMStudio(buildSystem(REQUIRED_SCHEMA),`Website Text:\n${cSlice}`,detectiveModel,onProgress,'Pass1');
    if(pass1.TOXICITY_ABORT===true) return{aiMetadata:{TOXICITY_ABORT:true},mappedFields:{_simulated_status:'REJECTED'},combinedText,qualityScore:0,passedQualityGate:false,candidatePhotos:null,socialLinks:{instagram_url:null,facebook_url:null,tiktok_url:null,schedule_url:null},flyerUrls};
    
    // ── ESCALATION PROTOCOL ──
    const needsEscalation = !pass1.hours || !pass1.pricing || (pass1.has_adult_night === true && !pass1.adult_night_schedule);
    let escalationRan = false;
    if (needsEscalation && browser) {
      onProgress('[Detective] 🚨 ESCALATION PROTOCOL: Missing hours/pricing/adult-night. Activating Hound Dog & OCR.');
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
            const { data: { text } } = await Tesseract.recognize(screenshotBuffer, 'eng');
            if (text?.trim().length > 50) {
              const ocrTxt = `\n\n[ESCALATION OCR from ${targetUrl}]\n${text}\n\n`;
              coreText = ocrTxt + coreText;
              amenityText = ocrTxt + amenityText;
            }
          } catch(e:any) {
            onProgress(`[Detective] OCR failed: ${e.message}`);
          }
        }
        
        onProgress('[Detective] 🚨 Re-running LM Studio Pass 1 (Ops) with OCR context...');
        const cSlice2 = coreText.slice(0, 24000);
        const pass1_retry = await callLMStudio(buildSystem(REQUIRED_SCHEMA),`Website Text:\n${cSlice2}`,detectiveModel,onProgress,'Pass1-Retry');
        if (pass1_retry.hours) pass1.hours = pass1_retry.hours;
        if (pass1_retry.pricing) pass1.pricing = pass1_retry.pricing;
        if (pass1_retry.has_adult_night !== undefined) pass1.has_adult_night = pass1_retry.has_adult_night;
        if (pass1_retry.adult_night_schedule) pass1.adult_night_schedule = pass1_retry.adult_night_schedule;
      } else {
        onProgress('[Detective] 🚨 No undiscovered priority links found for escalation.');
      }
    }

    // Fix #9: Merged Pass 2+3 into single combined pass (Amenities + Vibe + Social)
    const COMBINED_SCHEMA = {
      surface_type:'Floor: wood/maple/concrete/asphalt/sport_court/synthetic.',
      surface_quality:'Condition 3-5 words.', vibe_score:'0-100.',
      is_indoor:'boolean', has_rental:'boolean', has_pro_shop:'boolean', has_food:'boolean', 
      has_lights:'boolean', has_lockers:'boolean', has_ac:'boolean', has_wifi:'boolean', 
      has_toilets:'boolean', wheelchair:'boolean', capacity:'integer.',
      derby:'boolean', special_events:'Array.', 
      operator_name:'Owner name.', operator_description:'1-2 sentences.', cultural_meta:'Significance or null.', 
      adult_night_details:'Details or null.', instagram_url:'URL or null.', facebook_url:'URL or null.', 
      tiktok_url:'URL or null.', schedule_url:'URL or null.', yelp_url:'URL or null.', price_range:'$ to $$$$ or null.', 
      logo_url:'Logo URL or null.', email_addresses:'Array of ALL contact email addresses found for this venue. Include info, events, parties, management, booking — every unique email. Return [] if none found.', ...userSchema
    };
    // Fix #11: Use enriched text if escalation ran
    const pass2Slice = escalationRan ? amenityText.slice(0, 24000) : aSlice;
    onProgress('[Detective] LM Studio Pass 2 (Amenities + Vibe + Social)...');
    const pass2=await callLMStudio(buildSystem(COMBINED_SCHEMA),`Website Text:\n${pass2Slice}`,detectiveModel,onProgress,'Pass2-Combined');

    // Fix #2: JSON-LD fields fill gaps — LLM wins, JSON-LD is fallback
    aiMetadata={...jsonLdFields,...pass2,...pass1};
  } else onProgress('[Detective] Content too short. Skipping LM Studio.');

  const opening_hours=aiMetadata.hours||aiMetadata.opening_hours||spotContext.opening_hours||null;
  const pricing_data=aiMetadata.pricing||aiMetadata.pricing_data||spotContext.pricing_data||null;
  const surface_type=safeSurface(aiMetadata.surface_type||spotContext.surface_type);
  const surface_quality=aiMetadata.surface_quality||spotContext.surface_quality||null;
  const vibe_score=safeNum(aiMetadata.vibe_score??spotContext.vibe_score);
  const is_indoor=safeBool(aiMetadata.is_indoor??spotContext.is_indoor);
  const has_fee=safeBool(aiMetadata.has_fee??spotContext.has_fee);
  const has_adult_night=safeBool(aiMetadata.has_adult_night??spotContext.has_adult_night)??false;
  const adult_night_details=has_adult_night?aiMetadata.adult_night_details||spotContext.adult_night_details||null:null;
  const adultNightSchedule=has_adult_night?aiMetadata.adult_night_schedule||spotContext.adult_night_schedule||null:null;
  const special_events=aiMetadata.special_events||spotContext.special_events||null;
  const capacity=safeNum(aiMetadata.capacity??spotContext.capacity);
  const has_rental=safeBool(aiMetadata.has_rental??spotContext.has_rental);
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
  const regexEmails = extractEmails(coreText + '\n' + amenityText);
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

  // LLM Pass 1 fields (ops: hours, pricing, adult night) — confidence 0.70
  const pass1Fields = ['opening_hours', 'pricing_data', 'has_fee', 'has_adult_night', 'adult_night_schedule', 'adult_night_details'];
  for (const f of pass1Fields) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass1', 0.70);
  }

  // LLM Pass 2 fields (amenities, vibe, social) — confidence 0.60
  const pass2Fields = ['surface_type','surface_quality','vibe_score','is_indoor','has_rental','has_pro_shop',
    'has_food','has_lights','has_lockers','has_ac','has_wifi','has_toilets','is_wheelchair_accessible',
    'hosts_derby','capacity','special_events','operator_name','operator_description','cultural_metadata',
    'instagram_url','facebook_url','tiktok_url','schedule_url','yelp_url','price_range','logo_url'];
  for (const f of pass2Fields) {
    if (!fieldConfidence[f]) tag(f, 'llm_pass2', 0.60);
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
