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

      const hasProshop = await page.evaluate(() => {
        const text = document.body.innerText.toLowerCase();
        return text.includes('skate shop') || text.includes('pro shop') || text.includes('skate store');
      });

      // Save enriched object
      enrichedData.push({
        ...spot,
        enriched: {
          google_rating: rating,
          full_address: address,
          phone: phone,
          has_proshop: hasProshop,
          scraped_at: new Date().toISOString()
        }
      });
      console.log(`✅ Extracted: ⭐${rating || '?'} | 📍${address || '?'} | 📞${phone || '?'} | 🛒${hasProshop ? 'YES' : 'NO'}`);

      // Incremental Save: Write to disk immediately so background crashes don't lose data
      const outputPath = path.join(__dirname, 'enriched.json');
      fs.writeFileSync(outputPath, JSON.stringify(enrichedData, null, 2), 'utf8');

    } catch (err: any) {
      console.error(`❌ Failed on ${spot.name}:`, err.message);
    } finally {
      await page.close();
      // Stealth Mode: Random delay between 30 to 90 seconds to fly completely under radar
      const waitTime = Math.floor(Math.random() * 60000) + 30000;
      console.log(`⏳ Sleeping for ${Math.floor(waitTime/1000)}s...`);
      await delay(waitTime);
    }
  }

  await browser.close();
  console.log(`\n💾 Saved highly-enriched JSON heist to ${path.join(__dirname, 'enriched.json')}`);
}

// Execute if run directly
if (require.main === module) {
  scrapeGoogleDOM();
}
