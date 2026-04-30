import { createClient } from '@supabase/supabase-js';
import { upsertLocalSpot } from './LocalDB';
import dotenv from 'dotenv';
import path from 'path';
import fs from 'fs';
import fetch from 'node-fetch';

const envPaths = [
  path.resolve(__dirname, '../../../.env'), // worktree root
  'C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.env', // absolute fallback to master
];
for (const p of envPaths) {
  const result = dotenv.config({ path: p });
  if (!result.error && process.env.EXPO_PUBLIC_SUPABASE_URL) break;
}

if (!process.env.EXPO_PUBLIC_SUPABASE_URL) {
  throw new Error("Missing SUPABASE URL");
}

const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL || '',
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || ''
);

const LOCAL_BUCKET_DIR = path.resolve(__dirname, '../../.scraper-data/bucket');
if (!fs.existsSync(LOCAL_BUCKET_DIR)) {
  fs.mkdirSync(LOCAL_BUCKET_DIR, { recursive: true });
}

const downloadAndSaveImage = async (url: string, localFilename: string): Promise<string | null> => {
  try {
    const res = await fetch(url);
    if (!res.ok) return null;
    const buffer = await res.buffer();
    const filePath = path.join(LOCAL_BUCKET_DIR, localFilename);
    fs.writeFileSync(filePath, buffer);
    return `/local-bucket/${localFilename}`; // relative path for local HTTP server
  } catch (err) {
    console.error(`Failed to download ${url}:`, err);
    return null;
  }
};

const runMigration = async () => {
  console.log('🚀 Starting Local SQLite Migration...');

  // 1. Fetch unpublished spots (or ALL if we want a full mirror)
  // Let's just fetch all where is_published = false or null
  let count = 0;
  let page = 0;
  const PAGE_SIZE = 500;

  while (true) {
    const { data, error } = await supabase
      .from('skate_spots')
      .select('*')
      .or('is_published.eq.false,is_published.is.null')
      .range(page * PAGE_SIZE, (page + 1) * PAGE_SIZE - 1);

    if (error) {
      console.error('Error fetching from Supabase:', error);
      break;
    }

    if (!data || data.length === 0) {
      break;
    }

    for (const spot of data) {
      console.log(`Processing [${spot.id}] ${spot.name}...`);
      
      // Handle photos array (final CDN URLs)
      if (spot.photos && Array.isArray(spot.photos)) {
        const newPhotos = [];
        for (let i = 0; i < spot.photos.length; i++) {
          const url = spot.photos[i];
          if (typeof url === 'string' && url.includes('supabase.co')) {
            const ext = url.split('.').pop() || 'jpg';
            const filename = `${spot.id}_photo_${i}.${ext}`;
            const localUrl = await downloadAndSaveImage(url, filename);
            newPhotos.push(localUrl || url);
          } else {
            newPhotos.push(url);
          }
        }
        spot.photos = newPhotos;
      }

      // We can do the same for candidate_photos if needed, but candidates are often external URLs (og_image, etc.)
      // which the Photographer daemon will process anyway. For now, we just migrate the raw candidate links.

      upsertLocalSpot(spot);
      count++;
    }

    page++;
    console.log(`Migrated ${count} records so far...`);
  }

  console.log(`✅ Migration complete! Total records pulled: ${count}`);
};

runMigration().catch(console.error);
