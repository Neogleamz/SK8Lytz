import puppeteer from 'puppeteer';
import { GHOST } from './GHOST';

export interface ScrapedDetails {
  has_pro_shop: boolean;
  has_adult_night: boolean;
  vibe_rating: number | null;
  vibe_score: number | null;
  surface_quality: string | null;
  socials: Record<string, string>;
  fetched_website: string | null;
  phone_number: string | null;
}

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

// Fallback logic to scan Google and business websites for cultural requirements
export async function scrapeCulturalDetails(
  searchQuery: string, 
  isHeadless: boolean = true,
  rotationEnabled: boolean = true,
  randomizeViewport: boolean = true
): Promise<ScrapedDetails> {
  const result: ScrapedDetails = {
    has_pro_shop: false,
    has_adult_night: false,
    vibe_rating: null,
    vibe_score: null,
    surface_quality: null,
    socials: {},
    fetched_website: null,
    phone_number: null
  };

  const detectSurface = (text: string): string | null => {
    const lower = text.toLowerCase();
    if (lower.includes('buttery') || lower.includes('polished concrete')) return 'Buttery';
    if (lower.includes('smooth')) return 'Smooth';
    if (lower.includes('crusty’') || lower.includes('old asphalt')) return 'Crusty';
    if (lower.includes('rough') || lower.includes('potholes')) return 'Rough';
    if (lower.includes('concrete')) return 'Concrete';
    if (lower.includes('asphalt')) return 'Asphalt';
    if (lower.includes('wood') || lower.includes('masonite')) return 'Wood';
    return null;
  };

  try {
    const identity = GHOST.generateIdentity();
    console.log(`    🆔 Identity: ${identity.userAgent.slice(0, 45)}... [${identity.viewport.width}x${identity.viewport.height}]`);

    const browser = await puppeteer.launch({ 
      headless: isHeadless ? 'new' : false,
      args: [
        '--disable-blink-features=AutomationControlled',
        '--no-sandbox',
        '--window-size=' + identity.viewport.width + ',' + identity.viewport.height
      ]
    });
    
    const page = await browser.newPage();
    
    // Apply Identity Masking
    await page.setUserAgent(identity.userAgent);
    await page.setViewport(identity.viewport);
    await page.setExtraHTTPHeaders({
      'Accept-Language': identity.languages.join(',')
    });

    // Deep Script Spoofing
    await page.evaluateOnNewDocument(() => {
      // Hide WebDriver
      Object.defineProperty(navigator, 'webdriver', { get: () => undefined });
      
      // Fix Plugins
      Object.defineProperty(navigator, 'plugins', { get: () => [1, 2, 3, 4, 5] });
      
      // Mask Chrome property
      (window as any).chrome = { runtime: {} };
    });

    // Simulate a highly targeted local search
    const query = encodeURIComponent(`${searchQuery} reviews 18+ adult pro shop`);
    console.log(`    🔍 Step 1: Querying Google for [${searchQuery}]...`);
    await page.goto(`https://www.google.com/search?q=${query}`, { waitUntil: 'domcontentloaded' });
    
    // Give time to avoid bot detection
    await sleep(2000); 

    const bodyText = await page.evaluate(() => document.body.innerText.toLowerCase());
    
    // Check for explicit Bot Gates or CAPTCHAs
    const isBotGated = await page.evaluate(() => {
      const text = document.body.innerText.toLowerCase();
      return text.includes('google.com/sorry/index') || 
             text.includes('our systems have detected unusual traffic') ||
             text.includes('verify you are a human') ||
             text.includes('captcha') ||
             !!document.querySelector('#challenge-running') || // Cloudflare
             !!document.querySelector('.g-recaptcha');
    });

    if (isBotGated) {
      console.log('🚨 SECURITY ALERT: Crawler identified as bot. Gating protocol detected.');
      await browser.close();
      throw new Error('BOT_GATED');
    }

    
    // --- Step 2: Website Resolution ---
    console.log(`    🌐 Step 2: Attempting to resolve official website...`);
    const website = await page.evaluate(() => {
      // Look for the "Website" button in Google's Knowledge Graph or top results
      const anchors = Array.from(document.querySelectorAll('a'));
      const webBtn = anchors.find(a => a.innerText.toLowerCase() === 'website' || (a.getAttribute('aria-label') || '').toLowerCase().includes('website'));
      if (webBtn) return webBtn.getAttribute('href');
      
      // Fallback: look for likely official domains in the top 3 results
      // (Simple logic: first non-google/directory link)
      return null;
    });

    if (website && !website.includes('google.com')) {
       result.fetched_website = website;
       console.log(`    ✅ Step 2 Success: Resolved [${website.slice(0, 30)}...]`);
     } else {
        console.log(`    ⚠️  Step 2: No direct website button found in Knowledge Graph.`);
     }
 
     // --- Step 4: Phone Resolution ---
     console.log(`    📞 Step 4: Attempting to resolve phone number...`);
     const phone = await page.evaluate(() => {
       // Look for the "Call" button or phone span in Google's Knowledge Graph
       const selectors = [
         '[data-local-attribute="d_ph"]',
         '[data-dtype="d_ph"]',
         'span[role="link"] > span', // Often contains phone in mobile/compact views
         'a[href^="tel:"]'
       ];
       
       for (const sel of selectors) {
         const el = document.querySelector(sel);
         if (el) {
           const text = (el as any).innerText || el.getAttribute('href') || '';
           const clean = text.replace('tel:', '').trim();
           if (/\d/.test(clean)) return clean;
         }
       }
       return null;
     });
 
     if (phone) {
        result.phone_number = phone;
        console.log(`    ✅ Step 4 Success: Resolved [${phone}]`);
     } else {
        console.log(`    ⚠️  Step 4: No phone number detected.`);
     }
 
     // --- Step 3: Analysis ---
    console.log(`    🎸 Step 3: Analyzing metadata for 18+ / Pro Shop markers...`);
    
    // Niche DOM extraction rules
    if (bodyText.includes('adult night') || bodyText.includes('18+') || bodyText.includes('21+')) {
      result.has_adult_night = true;
      console.log('    ✨ Found Cultural Marker: Adult Night / 18+ verified.');
    }

    if (bodyText.includes('pro shop') || bodyText.includes('skate shop')) {
      result.has_pro_shop = true;
      console.log('    ✨ Found Cultural Marker: On-site Pro Shop availability.');
    }

    // Extract Vibe from Stars
    const starRating = await page.evaluate(() => {
      const ratingEl = document.querySelector('.Aq14fc'); // Google Panel Rating class
      return ratingEl ? parseFloat(ratingEl.textContent || '0') : null;
    });

    if (starRating) {
      result.vibe_rating = starRating;
      result.vibe_score = starRating;
      console.log(`    [Google] Extracted Community Vibe Rating: ${starRating}/5.0`);
    }

    const mainSurface = detectSurface(bodyText);
    if (mainSurface) {
      result.surface_quality = mainSurface;
      console.log(`    ✨ Cultural Discovery [Surface]: ${mainSurface}`);
    }

    // Attempt to salvage socials from results
    if (bodyText.includes('instagram.com/')) {
      const idx = bodyText.indexOf('instagram.com/');
      const handle = bodyText.slice(idx, idx + 40).split(/[ \n"']/)[0];
      result.socials['instagram'] = `https://${handle}`;
    }

    // --- YELP ENRICHMENT PROTOCOL ---
    const yelpLink = await page.evaluate(() => {
      const links = Array.from(document.querySelectorAll('a'));
      const yelpNode = links.find(a => a.href.includes('yelp.com/biz/'));
      return yelpNode ? yelpNode.href : null;
    });

    if (yelpLink) {
      try {
        console.log(`    [Google] Located Yelp business listing. Navigating for deep-dive...`);
        await page.goto(yelpLink, { waitUntil: 'domcontentloaded', timeout: 15000 });
        await sleep(1500);
        const yelpText = await page.evaluate(() => document.body.innerText.toLowerCase());
        
        // Salvage missing Vibe Rating from Yelp
        if (!result.vibe_rating) {
           const yelpRating = await page.evaluate(() => {
              const el = document.querySelector('[aria-label$=" star rating"]');
              return el ? parseFloat(el.getAttribute('aria-label') || '0') : null;
           });
           if (yelpRating) {
             result.vibe_rating = yelpRating;
             result.vibe_score = yelpRating;
             console.log(`    [Yelp] Successfully salvaged Vibe Rating from user reviews: ${yelpRating}/5.0`);
           }
        }

        // Deep-dive Yelp reviews for cultural rules
        if (yelpText.includes('adult night') || yelpText.includes('18+') || yelpText.includes('adults only')) {
           result.has_adult_night = true;
           console.log('    [Yelp] Verified Adult Night existence via raw user review text.');
        }
        if (yelpText.includes('pro shop') || yelpText.includes('skates for sale')) {
           result.has_pro_shop = true;
           console.log('    [Yelp] Confirmed in-house Pro Shop mentions in reviews.');
        }

        // Surface Discovery via Yelp Reviews
        const yelpSurface = detectSurface(yelpText);
        if (yelpSurface && !result.surface_quality) {
           result.surface_quality = yelpSurface;
           console.log(`    ✨ Cultural Discovery [Surface]: ${yelpSurface} (via Yelp)`);
        }
        
        result.socials['yelp'] = yelpLink;
      } catch (yelpErr) {
        // Skip silently if Yelp blocks headless Chromium
      }
    }

    await browser.close();
    return result;
  } catch (err: any) {
    console.error(`Puppeteer Error for query [${searchQuery}]:`, err.message);
    return result;
  }
}
