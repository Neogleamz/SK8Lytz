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
      await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36');
    try {
      const query = `${spot.name} ${spot.city} ${spot.state || ''}`;
      console.log(`\n🔍 Searching: ${query}`);
      
      const searchUrl = `https://www.google.com/search?q=${encodeURIComponent(query)}&hl=en`; // Force english
      
      // Navigate to Google
      await page.goto(searchUrl, { waitUntil: 'networkidle2' });
      
      // Check for Google Consent Popup (Accept all button)
      try {
         const acceptBtn = await page.$('button[id="L2AGLb"], button[aria-label="Accept all"]');
         if (acceptBtn) {
            await acceptBtn.click();
            await delay(1500); // give it time to remove the overlay
         }
      } catch(e) {}
      
      // Data Harvesting Target Selectors (The "Knowledge Panel" on the right side)
      // Note: Google changes class names frequently, so we rely on attributes or distinct structures
      
      // Stealth/Consent catch:
      await page.screenshot({ path: path.join(__dirname, 'debug_google.png') });
      const html = await page.content();
      fs.writeFileSync(path.join(__dirname, 'debug_google.html'), html);
      
      const rating = await page.evaluate(() => {
        const els = Array.from(document.querySelectorAll('span, div'));
        const found = els.find(s => s.textContent && s.textContent.trim().match(/^[1-5]\.\d$/) && s.children.length === 0);
        return found ? found.textContent?.trim() : null;
      });

      const address = await page.evaluate(() => {
        const d = document.querySelector('div[data-attrid="kc:/location/location:address"]');
        if (d) return d.textContent?.replace('Address:', '').trim();
        
        // Fallback: search for elements containing "Address:"
        const allSpans = Array.from(document.querySelectorAll('span, div'));
        const addrNode = allSpans.find(n => n.textContent?.includes('Address:') && n.textContent.length > 8 && n.children.length === 0);
        if (addrNode) return addrNode.textContent?.replace('Address:', '').trim();
        return null;
      });

      const phone = await page.evaluate(() => {
         const p = document.querySelector('div[data-attrid="kc:/collection/knowledge_panels/has_phone"]') || document.querySelector('span[aria-label^="Call"]');
         if (p) return p.textContent?.replace('Phone:', '').trim();
         
         const allSpans = Array.from(document.querySelectorAll('span'));
         const phoneNode = allSpans.find(n => n.textContent?.match(/^\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$/));
         return phoneNode ? phoneNode.textContent : null;
      });

      const hasProshop = await page.evaluate(() => {
        const text = document.body.innerText.toLowerCase();
        return text.includes('skate shop') || text.includes('pro shop') || text.includes('skate store') || text.includes('skate rentals');
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
