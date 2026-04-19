require('dotenv').config({ path: '../../.env' });
const { createClient } = require('@supabase/supabase-js');

const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL,
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY
);

async function migrateData() {
  console.log('Fetching spots that need migration...');
  
  // Find spots that are PENDING but have data.
  const { data, error } = await supabase
    .from('skate_spots')
    .select('id, website, phone_number, vibe_score, has_pro_shop, has_adult_night')
    .eq('verification_status', 'PENDING')
    .not('last_enriched_at', 'is', null);
    
  if (error) {
    console.error('Error fetching:', error);
    return;
  }
  
  console.log(`Found ${data.length} potential spots to migrate.`);
  
  let migratedCount = 0;
  for (const spot of data) {
      if (spot.website || spot.phone_number || spot.vibe_score || spot.has_pro_shop || spot.has_adult_night) {
          const { error: updateError } = await supabase.from('skate_spots').update({
              verification_status: 'ENRICHED'
          }).eq('id', spot.id);
          
          if (updateError) {
              console.error(`Error updating spot ${spot.id}:`, updateError);
          } else {
              migratedCount++;
          }
      }
  }
  
  console.log(`Migration complete. Status 'ENRICHED' applied to ${migratedCount} spots.`);
}

migrateData();
