import puppeteer from 'puppeteer';
import * as cheerio from 'cheerio';
import fs from 'fs';
import dotenv from 'dotenv';
import path from 'path';
import { upsertLocalSpot } from './core/LocalDB';

dotenv.config({ path: '../../.env' });

const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

async function run() {
  if (!fs.existsSync('seed.json')) {
    console.error('seed.json not found! run `npm run seed` first.');
    return;
  }

  const raw = fs.readFileSync('seed.json', 'utf8');
  const spots = JSON.parse(raw);

  console.log(`Starting enrichment for ${spots.length} spots...`);

  const browser = await puppeteer.launch({ headless: 'new' });
  const page = await browser.newPage();
  await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36');

  let processed = 0;

  for (const s of spots) {
    console.log(`[${processed + 1}/${spots.length}] Synthesizing data for ${s.name}...`);

    let safeSurface = 'unknown';
    if (['wood', 'concrete', 'asphalt', 'sport_court', 'unknown'].includes(s.surface_type)) {
      safeSurface = s.surface_type;
    }

    try {
      upsertLocalSpot({
        id: s.id.toString().padStart(32, '0').replace(/(.{8})(.{4})(.{4})(.{4})(.{12})/, "$1-$2-$3-$4-$5"),
        name: s.name,
        lat: s.lat,
        lng: s.lng,
        city: s.city,
        state: s.state,
        zip: s.zip,
        phone_number: s.phone,
        surface_type: safeSurface,
        is_indoor: s.is_indoor,
        is_verified: true,
        source: 'osm',
        verification_status: 'SEEDED'
      });
      console.log(`  -> Inserted ${s.name}!`);
    } catch (err: any) {
      console.error(`  -> LocalDB Upsert Error:`, err.message);
    }

    processed++;
    await sleep(200);
  }

  await browser.close();
  console.log('Enrichment and LocalDB Upsert Complete!');
}

run();
