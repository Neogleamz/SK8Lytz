import fs from 'fs';
import path from 'path';
import puppeteer from 'puppeteer';

// Basic delay function to prevent rate-limiting bans
const delay = (ms: number) => new Promise(res => setTimeout(res, ms));

async function scrapeGoogleDOM() {
  const seedPath = path.join(__dirname, 'seed.json');
  if (!fs.existsSync(seedPath)) {
    console.error('❌ seed.json not found! Run SeedOverpass.ts first.');
    return;
  }

  const spots = JSON.parse(fs.readFileSync(seedPath, 'utf8'));
  console.log(`🕵️‍♂️ Booting Headless Chrome to scrape ${spots.length} spots...`);

  // Launch headless browser
  const browser = await puppeteer.launch({ 
    headless: true,
    args: ['--no-sandbox', '--disable-setuid-sandbox', '--window-size=1280,800']
  });
  
  const enrichedData = [];

  for (const spot of spots) {
    const page = await browser.newPage();
    try {
      const query = `${spot.name} ${spot.city} ${spot.state || ''}`;
      console.log(`\n🔍 Searching: ${query}`);
      
      const searchUrl = `https://www.google.com/search?q=${encodeURIComponent(query)}`;
      
      // Navigate to Google
      await page.goto(searchUrl, { waitUntil: 'networkidle2' });
      
      // Data Harvesting Target Selectors (The "Knowledge Panel" on the right side)
      // Note: Google changes class names frequently, so we rely on attributes or distinct structures
      
      // Attempt to extract Rating
      const rating = await page.evaluate(() => {
        const ratingEl = document.querySelector('span[aria-hidden="true"]:not([class=""])');
        return ratingEl ? ratingEl.textContent?.trim() : null;
      });

      // Attempt to extract Address
      const address = await page.evaluate(() => {
        const addrLinks = Array.from(document.querySelectorAll('a')).filter(a => a.href.includes('maps/place'));
        return addrLinks.length > 0 ? addrLinks[0].textContent : null;
      });

      // Attempt to extract Phone Number
      const phone = await page.evaluate(() => {
        const spans = Array.from(document.querySelectorAll('span'));
        const phoneSpan = spans.find(s => s.textContent?.match(/^\+?1?\s*\(?-?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$/));
        return phoneSpan ? phoneSpan.textContent : null;
      });

      // Save enriched object
      enrichedData.push({
        ...spot,
        enriched: {
          google_rating: rating,
          full_address: address,
          phone: phone,
          scraped_at: new Date().toISOString()
        }
      });
      console.log(`✅ Extracted: ⭐${rating || '?'} | 📍${address || '?'} | 📞${phone || '?'}`);

    } catch (err: any) {
      console.error(`❌ Failed on ${spot.name}:`, err.message);
    } finally {
      await page.close();
      // Random delay to avoid IP ban (2-5 seconds)
      const waitTime = Math.floor(Math.random() * 3000) + 2000;
      await delay(waitTime);
    }
  }

  await browser.close();

  // Save the enriched data
  const outputPath = path.join(__dirname, 'enriched.json');
  fs.writeFileSync(outputPath, JSON.stringify(enrichedData, null, 2), 'utf8');
  console.log(`\n💾 Saved highly-enriched JSON heist to ${outputPath}`);
}

// Execute if run directly
if (require.main === module) {
  scrapeGoogleDOM();
}
