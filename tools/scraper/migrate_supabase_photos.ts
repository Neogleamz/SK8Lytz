import dotenv from 'dotenv';
import path from 'path';
import fs from 'fs';
import fetch from 'node-fetch';
import { createClient } from '@supabase/supabase-js';
import { db, updateLocalSpot } from './core/LocalDB';

// Load .env
const envPaths = [
  path.resolve(__dirname, '../../.env'),
  path.resolve(process.cwd(), '.env'),
  path.resolve(process.cwd(), '../../.env'),
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.env',
];

for (const p of envPaths) {
  const result = dotenv.config({ path: p });
  if (!result.error && process.env.EXPO_PUBLIC_SUPABASE_URL) break;
}

const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '',
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

const PHOTOS_DIR = path.resolve(__dirname, '../../.scraper-data/photos');
const PHOTO_SERVE_BASE = 'http://localhost:5999/api/photos';

async function downloadImage(url: string): Promise<Buffer | null> {
  try {
    const res = await fetch(url);
    if (!res.ok) return null;
    return await res.buffer();
  } catch {
    return null;
  }
}

async function run() {
  console.log('🚀 Starting Supabase Photo Migration...');

  // 1. Fetch from remote Supabase
  const { data: spots, error } = await supabase
    .from('skate_spots')
    .select('id, name, state, photos')
    .not('photos', 'is', null);

  if (error) {
    console.error('❌ Failed to fetch from Supabase:', error);
    process.exit(1);
  }

  console.log(`Found ${spots?.length || 0} spots with remote photos in Supabase.`);

  let totalDownloaded = 0;

  for (const spot of spots || []) {
    if (!spot.photos || spot.photos.length === 0) continue;
    
    // Make sure it exists locally
    const localSpot = db.prepare('SELECT id, photos FROM local_spots WHERE id = ?').get(spot.id) as any;
    if (!localSpot) {
      console.warn(`⚠️ Skipping ${spot.name} - Not found in local_spots`);
      continue;
    }

    console.log(`\n📸 Processing: ${spot.name} (${spot.photos.length} photos)`);
    
    const localUrls: string[] = [];
    const state = spot.state || 'US';

    for (let i = 0; i < spot.photos.length; i++) {
      const remoteUrl = spot.photos[i];
      if (!remoteUrl.includes('supabase.co')) {
        // It's already local or something else
        localUrls.push(remoteUrl);
        continue;
      }

      console.log(`   ⬇️  Downloading photo ${i}...`);
      const buffer = await downloadImage(remoteUrl);
      if (!buffer) {
        console.error(`   ❌ Failed to download ${remoteUrl}`);
        continue;
      }

      const stateDir = path.join(PHOTOS_DIR, state, spot.id);
      if (!fs.existsSync(stateDir)) {
        fs.mkdirSync(stateDir, { recursive: true });
      }

      // Supabase public URLs are usually jpg
      const ext = remoteUrl.toLowerCase().includes('png') ? 'png' : 'jpg';
      const filename = `photo_${i}.${ext}`;
      const filePath = path.join(stateDir, filename);

      fs.writeFileSync(filePath, buffer);
      
      const localUrl = `${PHOTO_SERVE_BASE}/${state}/${spot.id}/${filename}`;
      localUrls.push(localUrl);
      totalDownloaded++;
      console.log(`   ✅ Saved -> ${localUrl} (${(buffer.length / 1024).toFixed(1)} KB)`);
    }

    if (localUrls.length > 0) {
      updateLocalSpot(spot.id, {
        photos: localUrls,
        // Optional: you can set verification_status to MEDIA_READY if you want
        // but we'll just update the photos array directly.
      });
      console.log(`✨ Updated local DB for ${spot.name}`);
    }
  }

  console.log(`\n🎉 Migration complete! Downloaded ${totalDownloaded} photos to local disk.`);
}

run();
