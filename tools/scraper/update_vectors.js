const path = require('path');
require('dotenv').config({ path: path.resolve(__dirname, '../../.env') });
const { createClient } = require('@supabase/supabase-js');

const supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL;
const supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY;
const supabase = createClient(supabaseUrl, supabaseKey);

async function run() {
  const payload = {
    ai_target_vectors: [
      { key: 'pricing', prompt: 'Extract admission prices, rental fees, and any available package deals. Separate adult, child, and spectator pricing if available.' },
      { key: 'hours', prompt: 'Extract the complete weekly schedule for public skating sessions. Ignore private party times unless explicitly open to public.' },
      { key: 'surface_type', prompt: 'Identify the material of the skating floor (e.g., hardwood, maple, concrete, rotacast, synthetic).' },
      { key: 'surface_quality', prompt: 'Assess the condition or quality of the skating floor based on descriptions or reviews (e.g., smooth, well-maintained, slippery, sticky, warped).' },
      { key: 'vibe_score', prompt: 'Determine the overall atmosphere and cultural vibe (e.g., family-friendly, adult-night focused, strictly-rhythm, legacy-retro, modern).' },
      { key: 'has_rental', prompt: 'Does the facility offer skate rentals? Note if they specifically mention inline or quad rentals.' },
      { key: 'has_pro_shop', prompt: 'Does the facility have an on-site pro shop that sells skates, wheels, or bearings?' },
      { key: 'has_adult_night', prompt: 'Does the facility host 18+ or 21+ adult-only skate nights? Specify the day if found.' }
    ]
  };

  const { error } = await supabase.from('scraper_config').update(payload).eq('id', 1);
  if (error) console.error('Error updating config:', error);
  else console.log('Vectors updated!');
}
run();
