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
      
      // Extract Knowledge Panel Data resiliently via text traversing
      const rating = await page.evaluate(() => {
        // Find element holding star rating e.g. 4.6
        const els = Array.from(document.querySelectorAll('span'));
        const found = els.find(s => s.textContent?.match(/^[1-5]\.\d$/));
        return found ? found.textContent : null;
      });

      const address = await page.evaluate(() => {
        const d = document.querySelector('div[data-attrid="kc:/location/location:address"]');
        return d ? d.textContent?.replace('Address: ', '').replace('Address:', '').trim() : null;
      });

      const phone = await page.evaluate(() => {
         const p = document.querySelector('div[data-attrid="kc:/collection/knowledge_panels/has_phone"]');
         return p ? p.textContent?.replace('Phone: ', '').replace('Phone:', '').trim() : null;
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
