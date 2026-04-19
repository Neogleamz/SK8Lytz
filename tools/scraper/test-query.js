require('dotenv').config({ path: '../../.env' });
const { createClient } = require('@supabase/supabase-js');

const supabase = createClient(
  process.env.EXPO_PUBLIC_SUPABASE_URL,
  process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY
);

async function run() {
  const { data, error } = await supabase
    .from('skate_spots')
    .select('state, facility_type, verification_status')
    .in('verification_status', ['PENDING', 'ENRICHED', null]);
    
  if (error) {
    console.error(error);
    return;
  }
  
  const stats = data.reduce((acc, x) => { 
    const k = x.state + ' | ' + x.facility_type; 
    acc[k] = (acc[k] || 0) + 1; 
    return acc; 
  }, {});
  
  console.log(JSON.stringify(stats, null, 2));

  // Let's also check if 'IA' has any records
  const iaSpots = data.filter(s => s.state === 'IA');
  console.log('IA Spots:', iaSpots.length);
}

run();
