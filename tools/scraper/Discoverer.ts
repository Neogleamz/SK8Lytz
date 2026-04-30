import puppeteer from 'puppeteer';
import dotenv from 'dotenv';
import path from 'path';
import { GHOST } from './lib/GHOST';
import { upsertLocalSpot } from './core/LocalDB';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

export async function runDirectDiscovery(stateFull: string) {
    console.log(`📡 [Phase 0] Starting Omni-Net Discovery for ${stateFull}...`);
    
    const identity = GHOST.generateIdentity();
    const browser = await puppeteer.launch({
        headless: 'new',
        args: ['--no-sandbox', '--disable-setuid-sandbox']
    });

    try {
        const page = await browser.newPage();
        await page.setUserAgent(identity.userAgent);
        await page.setViewport(identity.viewport);

        console.log(`🔍 [Discoverer] Searching Google Maps for Skating Rinks in ${stateFull}...`);
        
        // Use Google Maps Search URL directly
        const query = encodeURIComponent(`Skating Rink in ${stateFull}`);
        await page.goto(`https://www.google.com/maps/search/${query}`, { waitUntil: 'networkidle2' });

        // Scroll to load all results (Heuristic: Google Maps loads in batches)
        for (let i = 0; i < 5; i++) {
            await page.evaluate(() => {
                const scrollable = document.querySelector('div[role="feed"]');
                if (scrollable) scrollable.scrollTop = scrollable.scrollHeight;
            });
            await sleep(2000);
        }

        const listings = await page.evaluate(() => {
            const results: any[] = [];
            const cards = document.querySelectorAll('div[role="article"]');
            cards.forEach(card => {
                const name = card.querySelector('div.fontHeadlineSmall')?.textContent || '';
                const address = card.querySelector('div.fontBodyMedium:nth-child(2)')?.textContent || '';
                if (name && address) {
                    results.push({ name, address });
                }
            });
            return results;
        });

        console.log(`✅ [Discoverer] Found ${listings.length} candidate leads for ${stateFull}`);

        for (const lead of listings) {
             // Basic Sanity Check
             if (!lead.name.toLowerCase().match(/rink|skate|skateland/)) continue;

             // Push to LocalDB as PENDING (ignore duplicates on name/address)
             try {
               upsertLocalSpot({
                 name: lead.name,
                 street_address: lead.address,
                 state: stateFull.split(' ').pop() || '',
                 verification_status: 'PENDING'
               });
               console.log(`   ➕ Lead Added: ${lead.name}`);
             } catch (err: any) {
               console.log(`   ⚠️ Skipped (duplicate): ${lead.name}`);
             }
        }

    } catch (err: any) {
        console.error(`❌ [Discoverer] Failure for ${stateFull}:`, err.message);
    } finally {
        await browser.close();
    }
}
