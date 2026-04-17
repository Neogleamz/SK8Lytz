import puppeteer from 'puppeteer';

export interface ScrapedDetails {
  has_pro_shop: boolean;
  has_adult_night: boolean;
  vibe_rating: number | null;
  socials: Record<string, string>;
  fetched_website: string | null;
}

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

// Fallback logic to scan Google and business websites for cultural requirements
export async function scrapeCulturalDetails(spotName: string, city: string | null): Promise<ScrapedDetails> {
  const result: ScrapedDetails = {
    has_pro_shop: false,
    has_adult_night: false,
    vibe_rating: null,
    socials: {},
    fetched_website: null
  };

  try {
    const browser = await puppeteer.launch({ headless: 'new' });
    const page = await browser.newPage();
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/115.0.0.0 Safari/537.36');
    
    // Simulate a highly targeted local search
    const query = encodeURIComponent(`${spotName} ${city || ''} reviews 18+ adult pro shop`);
    await page.goto(`https://www.google.com/search?q=${query}`, { waitUntil: 'domcontentloaded' });
    
    // Give time to avoid bot detection
    await sleep(2000); 

    const bodyText = await page.evaluate(() => document.body.innerText.toLowerCase());
    
    // Niche DOM extraction rules
    if (bodyText.includes('adult night') || bodyText.includes('18+') || bodyText.includes('21+')) {
      result.has_adult_night = true;
    }

    if (bodyText.includes('pro shop') || bodyText.includes('skate shop')) {
      result.has_pro_shop = true;
    }

    // Extract Vibe from Stars
    const starRating = await page.evaluate(() => {
      const ratingEl = document.querySelector('.Aq14fc'); // Google Panel Rating class
      return ratingEl ? parseFloat(ratingEl.textContent || '0') : null;
    });

    if (starRating) {
      result.vibe_rating = starRating;
    }

    // Attempt to salvage socials from results
    if (bodyText.includes('instagram.com/')) {
      const idx = bodyText.indexOf('instagram.com/');
      const handle = bodyText.slice(idx, idx + 40).split(/[ \n"']/)[0];
      result.socials['instagram'] = `https://${handle}`;
    }

    await browser.close();
    return result;
  } catch (err: any) {
    console.error(`Puppeteer Error for ${spotName}:`, err.message);
    return result;
  }
}
