import fs from 'fs';
import path from 'path';
import { createClient } from '@supabase/supabase-js';
import dotenv from 'dotenv';
dotenv.config();

// Ensure you create a .env file locally with these values before running
const SUPABASE_URL = process.env.EXPO_PUBLIC_SUPABASE_URL || '';
const SUPABASE_SERVICE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || '';

if (!SUPABASE_URL || !SUPABASE_SERVICE_KEY) {
  console.log("⚠️ Missing Supabase env variables. Skipping database ingestion test.");
  process.exit(0);
}

const supabase = createClient(SUPABASE_URL, SUPABASE_SERVICE_KEY);

async function pushToDatabase() {
  const enrichedPath = path.join(__dirname, 'enriched.json');
  if (!fs.existsSync(enrichedPath)) {
    console.error('❌ enriched.json not found!');
    return;
  }

  const spots = JSON.parse(fs.readFileSync(enrichedPath, 'utf8'));
  console.log(`📤 Pushing ${spots.length} highly enriched skate spots to Supabase...`);

  for (const spot of spots) {
      // Attempt to extract ZIP from the address (basic 5-digit regex at the end of the string)
      let parsedZip = null;
      if (spot.enriched.full_address) {
        const zipMatch = spot.enriched.full_address.match(/\b\d{5}\b/);
        if (zipMatch) parsedZip = zipMatch[0];
      }

      const { error } = await supabase
        .from('skate_spots')
        .upsert({
          name: spot.name,
          lat: spot.lat,
          lng: spot.lng,
          city: spot.city,
          state: spot.state,
          zip: parsedZip,
          phone: spot.enriched.phone || null,
          address: spot.enriched.full_address || null,
          has_proshop: spot.enriched.has_proshop || false,
          vibe_rating: spot.enriched.google_rating ? parseFloat(spot.enriched.google_rating) : null,
          source: 'native_crawler',
          is_verified: false
        }, { onConflict: 'name' }); // Assuming name prevents exact dupes in this POC

    if (error) {
      console.error(`❌ DB Insert failed for ${spot.name}:`, error.message);
    } else {
      console.log(`✅ Emplaced: ${spot.name}`);
    }
  }

  console.log('🎉 Heist Complete. Data submerged.');
}

if (require.main === module) {
  pushToDatabase();
}
