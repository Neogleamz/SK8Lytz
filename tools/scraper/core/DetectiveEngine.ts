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

async function fetchExternalText(url: string, label: string): Promise<string> {
  try {
    const res = await fetch(url, {
      headers: { 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36' },
      signal: AbortSignal.timeout(10000),
    });
    if (!res.ok) return '';
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
    return result;
  } catch { return ''; }
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
      const res = await fetchFn(LM_STUDIO_URL, { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payload) });
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

async function crawlPage(page: any, url: string, onProgress: (m: string) => void): Promise<{ text:string; jsonLd:string; ogImage:string|null; images:Array<{src:string;alt:string;parentClass:string}>; iframes:string[]; links:Array<{href:string;text:string}>; }> {
  const empty = { text:'', jsonLd:'', ogImage:null, images:[], iframes:[], links:[] };
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
      document.querySelectorAll('nav,footer,script,style,header,iframe,noscript').forEach(el=>el.remove());
      const text=document.body?.innerText?.replace(/\n+/g,' ').replace(/\s{2,}/g,' ').trim()||'';
      return {ogImage,jsonLd,iframes,images,links,text};
    }).catch(()=>({ogImage:null,jsonLd:'',iframes:[],images:[],links:[],text:''}));
    return {text:d.text,jsonLd:d.jsonLd,ogImage:d.ogImage,images:d.images,iframes:d.iframes,links:d.links};
  } catch { onProgress(`[Detective] Nav failed: ${url}`); return empty; }
}

// ─── Main Detective Function ─────────────────────────────────────────────────

export async function executeDetective(
  spotContext: any, aiConfig: any, isHeadless: boolean, onProgress: (msg:string)=>void=()=>{}
): Promise<DetectiveResult> {
  let combinedText=''; const flyerUrls:string[]=[]; let ogImage:string|null=null;
  const domImages:Array<{src:string;alt:string;parentClass:string}>=[];
  let aiMetadata:Record<string,any>={};
  const detectiveModel=aiConfig.detective_model||'local-model';
  let sitemap:any={schedule_urls:[],pricing_urls:[],about_urls:[],events_urls:[],contact_urls:[],gallery_urls:[],all_urls:[]};
  let yelpData:any={text:'',photos_url:null,og_image:null};
  let fbData:any={text:'',cover_photo:null,photos_url:null};

  if (!spotContext.website) {
    onProgress('[Detective] No website.'); combinedText=`Facility: ${spotContext.name}. ${spotContext.city}, ${spotContext.state}. No website.`;
  } else if (isSocialCrawlBlocked(spotContext.website)) {
    onProgress('[Detective] Social-only.'); combinedText=`Facility: ${spotContext.name}. ${spotContext.city}, ${spotContext.state}. No traditional website.`;
  } else {
    onProgress('[Detective] Fetching sitemap...');
    sitemap=await parseSitemap(spotContext.website);
    onProgress(`[Detective] schedule(${sitemap.schedule_urls.length}) pricing(${sitemap.pricing_urls.length}) gallery(${sitemap.gallery_urls.length})`);
    onProgress('[Detective] Fetching Yelp...');
    yelpData=await fetchYelpData(spotContext.name,spotContext.city,spotContext.state,spotContext.yelp_url);
    if(yelpData.text) combinedText+='\n\n'+yelpData.text;
    await sleep(EXTERNAL_SOURCE_DELAY);
    onProgress('[Detective] Fetching Facebook...');
    fbData=await fetchFacebookData(spotContext.facebook_url||null);
    if(fbData.text) combinedText+='\n\n'+fbData.text;
    await sleep(EXTERNAL_SOURCE_DELAY);
    onProgress('[Detective] Fetching Google Places...');
    const gt=await fetchGooglePlacesWeb(spotContext.google_place_id||null);
    if(gt) combinedText+='\n\n'+gt;

    let browser:any=null;
    try {
      browser=await puppeteer.launch({headless:isHeadless?'new':false,protocolTimeout:60000,args:['--no-sandbox','--disable-setuid-sandbox','--disable-dev-shm-usage']});
      const page=await browser.newPage();
      await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/124.0.0.0 Safari/537.36');
      await page.setViewport({width:1280,height:800});
      const targeted=[...new Set([...sitemap.schedule_urls.slice(0,3),...sitemap.pricing_urls.slice(0,3),...sitemap.about_urls.slice(0,2),...sitemap.events_urls.slice(0,2),...sitemap.contact_urls.slice(0,1)])];
      const crawlUrls=[spotContext.website,...targeted].slice(0,MAX_PAGES_PER_RECORD);
      const PAGE_SCORE_RULES=[{pattern:/hours|schedule|session|times|calendar|events|open.?skate/i,score:10},{pattern:/adult.?night|18\+|21\+/i,score:10},{pattern:/pricing|price|admission|rates|tickets/i,score:9},{pattern:/about|story|history|facility|rink/i,score:8},{pattern:/location|directions|contact/i,score:6}];
      let allLinks:Array<{href:string;text:string}>=[];
      let hostname='';try{hostname=new URL(spotContext.website).hostname;}catch{}
      for(const url of crawlUrls){
        onProgress(`[Detective] -> ${url}`);
        const pg=await crawlPage(page,url,onProgress);
        if(pg.jsonLd) combinedText+=`\n\n[JSON-LD: ${url}]\n${pg.jsonLd}`;
        if(pg.text) combinedText+=`\n\n[PAGE: ${url}]\n${pg.text}`;
        if(!ogImage&&pg.ogImage&&!pg.ogImage.includes('placeholder')) ogImage=pg.ogImage;
        domImages.push(...pg.images.slice(0,5));
        flyerUrls.push(...pg.images.filter(i=>/schedule|pricing|flyer/i.test(i.src+i.alt)).map(i=>i.src));
        allLinks.push(...pg.links);
        for(const iframe of pg.iframes){const ig=await crawlPage(page,iframe,onProgress);if(ig.text)combinedText+=`\n\n[IFRAME:${iframe}]\n${ig.text}`;}
      }
      if(targeted.length===0){
        const internal=allLinks.filter(l=>{try{return new URL(l.href).hostname===hostname;}catch{return false;}});
        const scored=internal.map(l=>{let s=0;for(const r of PAGE_SCORE_RULES){if(r.pattern.test(l.href)||r.pattern.test(l.text))s=Math.max(s,r.score);}return{...l,score:s};}).filter(l=>l.score>0).sort((a,b)=>b.score-a.score);
        for(const l of [...new Set(scored.map(s=>s.href))].slice(0,5)){
          if(combinedText.includes(`[PAGE: ${l}]`)) continue;
          const pg=await crawlPage(page,l,onProgress);
          if(pg.text) combinedText+=`\n\n[PAGE: ${l}]\n${pg.text}`;
          domImages.push(...pg.images.slice(0,3));
        }
      }
      for(const fUrl of [...new Set(flyerUrls)].slice(0,5)){
        if(combinedText.includes(`[OCR from Flyer Image: ${fUrl}]`)) continue;
        try{const{data:{text}}=await Tesseract.recognize(fUrl,'eng');if(text?.length>20)combinedText+=`\n\n[OCR from Flyer Image: ${fUrl}]\n${text}`;}catch{}
      }
    } finally{if(browser){try{await browser.close();}catch{}}}
  }

  const userVectors=aiConfig.ai_target_vectors||[];
  const userSchema=userVectors.reduce((acc:any,vec:any)=>{acc[vec.key]=vec.prompt||vec.type;return acc;},{});
  const REQUIRED_SCHEMA={hours:'Complete weekly public skating schedule {day: time_range}.',pricing:'All admission fees {adult,child,senior,spectator,skate_rental}.',has_fee:'boolean',has_adult_night:'boolean',adult_night_schedule:'If adult nights: {day:time_range}. Null if none.'};
  const FULL_SCHEMA:Record<string,string>={surface_type:'Floor: wood/maple/concrete/asphalt/sport_court/synthetic.',surface_quality:'Condition 3-5 words.',vibe_score:'0-100.',is_indoor:'boolean',has_rental:'boolean',has_pro_shop:'boolean',has_food:'boolean',has_lights:'boolean',has_lockers:'boolean',has_ac:'boolean',has_wifi:'boolean',has_toilets:'boolean',wheelchair:'boolean',derby:'boolean',capacity:'integer.',special_events:'Array.',operator_name:'Owner name.',operator_description:'1-2 sentences.',cultural_meta:'Significance or null.',adult_night_details:'Details or null.',instagram_url:'URL or null.',facebook_url:'URL or null.',tiktok_url:'URL or null.',schedule_url:'URL or null.',yelp_url:'URL or null.',price_range:'$ to $$$$ or null.',logo_url:'Logo URL or null.',...userSchema};
  const exclusionKw=aiConfig.ai_exclusion_keywords||[];
  const usp=aiConfig.ai_system_prompt||'';
  const buildSystem=(schema:Record<string,string>,ctx?:string)=>{
    let s=`You are a data extraction agent for [${spotContext.name}] in [${spotContext.city}].\nONLY this location. null for missing. Valid JSON only. true/false/null booleans.\n`;
    if(usp) s+=usp+'\n';
    if(exclusionKw.length) s+=`TOXICITY: If PRIMARY business matches [${exclusionKw.join(',')}] return {"TOXICITY_ABORT":true}.\n`;
    if(ctx) s+=`PASS 1 CONTEXT: ${ctx}\n`;
    return s+`SCHEMA:\n${JSON.stringify(schema,null,2)}`;
  };

  if(combinedText.trim().length>=50){
    const slice=combinedText.slice(-40000);
    onProgress('[Detective] LM Studio Pass 1...');
    const pass1=await callLMStudio(buildSystem(REQUIRED_SCHEMA),`Website Text:\n${slice}`,detectiveModel,onProgress,'Pass1');
    if(pass1.TOXICITY_ABORT===true) return{aiMetadata:{TOXICITY_ABORT:true},mappedFields:{_simulated_status:'REJECTED'},combinedText,qualityScore:0,passedQualityGate:false,candidatePhotos:null,socialLinks:{instagram_url:null,facebook_url:null,tiktok_url:null,schedule_url:null},flyerUrls};
    onProgress('[Detective] LM Studio Pass 2...');
    const pass2=await callLMStudio(buildSystem(FULL_SCHEMA,`Hours:${JSON.stringify(pass1.hours)} Pricing:${JSON.stringify(pass1.pricing)}`),`Website Text:\n${slice}`,detectiveModel,onProgress,'Pass2');
    aiMetadata={...pass2,...pass1};
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
  }
  if(Object.keys(candidatePhotos).length===0) candidatePhotos=null as any;

  const qFields=[opening_hours,pricing_data,operator_name,operator_description,surface_type,surface_quality,vibe_score,is_indoor,capacity,has_fee,has_rental,has_pro_shop,has_food,has_lights,has_ac,has_lockers,has_toilets,has_wifi,is_wheelchair_accessible,has_adult_night,adult_night_details,adultNightSchedule,special_events,hosts_derby,instagram_url,facebook_url,tiktok_url,cultural_metadata];
  const qualityScore=qFields.filter(f=>f!==null&&f!==undefined&&f!==false).length;
  const hasHighValueField=(opening_hours&&Object.keys(opening_hours).length>0)||(pricing_data&&Object.keys(pricing_data).length>0);
  const passedQualityGate=qualityScore>=3&&!!hasHighValueField;
  onProgress(`[Detective] Quality[${qualityScore}/${qFields.length}]`);
  onProgress(passedQualityGate?'[Detective] Quality gate passed.':'[Detective] Low quality — emitting DEEP_CRAWLED anyway.');

  const mappedFields={is_indoor,operator_description,operator_name,instagram_url,facebook_url,tiktok_url,schedule_url,opening_hours,adult_night_schedule:adultNightSchedule,has_adult_night,adult_night_details,special_events,pricing_data,has_fee,surface_type,surface_quality,vibe_score,capacity,has_rental,has_pro_shop,has_food,has_lights,has_lockers,has_ac,has_wifi,has_toilets,is_wheelchair_accessible,hosts_derby,cultural_metadata,yelp_url,price_range,logo_url,candidate_photos:candidatePhotos,ai_metadata:Object.keys(aiMetadata).length>0?aiMetadata:null,_simulated_status:passedQualityGate?'DEEP_CRAWLED':'LOW_QUALITY'};
  return {aiMetadata,mappedFields,combinedText,qualityScore,passedQualityGate,candidatePhotos,socialLinks:{instagram_url,facebook_url,tiktok_url,schedule_url},flyerUrls};
}
