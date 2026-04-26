const path = require('path');
require('dotenv').config({ path: path.resolve(__dirname, '../../.env') });
const { createClient } = require('@supabase/supabase-js');

const supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL;
const supabaseKey = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY;
const supabase = createClient(supabaseUrl, supabaseKey);

async function run() {
  const payload = {
    scout_search_queries: ['roller rink', 'roller skating rink', 'skate park', 'inline skating rink'],
    target_facilities: ['roller_rink', 'skatepark', 'hybrid'],
    scout_rate_limit_rpm: 12,
    scout_gatekeeper_rules: { "must_be_establishment": true, "not_in_blocklist": true },
    
    crawl_depth: 2,
    crawl_priority_paths: ['/schedule', '/pricing', '/admission', '/about', '/events'],
    crawl_social_platforms: ['facebook', 'instagram', 'tiktok', 'youtube'],
    crawl_http_timeout_ms: 15000,
    crawl_max_retries: 3,
    crawl_gatekeeper_rules: { "must_have_website": true, "require_http_200": true },
    
    detective_model: 'Llama3.2-8b',
    detective_confidence_min: 80,
    detective_temperature: 0.1,
    ai_system_prompt: 'You are an elite data extraction agent for the SK8Lytz platform. Your goal is to extract skate spot facility information from raw HTML and text crawls into pure JSON. Do not hallucinate. If data is missing, mark as null.',
    ai_target_vectors: ['pricing', 'hours', 'surface_type', 'surface_quality', 'vibe_score', 'has_rental', 'has_pro_shop', 'has_adult_night'],
    ai_exclusion_keywords: ['ice rink', 'ice skating', 'hockey', 'curling', 'bmx', 'bikes'],
    
    photo_sources: ['google_places', 'facebook', 'instagram', 'website'],
    photo_min_count: 1,
    photo_categories: ['facade_exterior', 'skate_floor', 'arcade_zone', 'snack_bar'],
    photo_min_size_kb: 50,
    photo_vision_api: 'google_cloud_vision',
    
    publisher_auto_publish_threshold: 80,
    publisher_required_fields: ['name','lat','lng','state','facility_type','rating'],
    publisher_auto_publish_enabled: false,
    publisher_upsert_strategy: 'MERGE'
  };

  const { error } = await supabase.from('scraper_config').update(payload).eq('id', 1);
  if (error) console.error('Error updating config:', error);
  else console.log('Config successfully seeded!');

  // Seed Blocklist
  const blocklist = [
    { pattern: 'walmart', match_type: 'name', reason: 'Big box store' },
    { pattern: 'target', match_type: 'name', reason: 'Big box store' },
    { pattern: 'dicks sporting goods', match_type: 'name', reason: 'Retail' },
    { pattern: 'rei', match_type: 'name', reason: 'Retail' },
    { pattern: 'zumiez', match_type: 'name', reason: 'Retail' },
    { pattern: 'vans store', match_type: 'name', reason: 'Retail' },
  ];

  for (const item of blocklist) {
    const { error: bErr } = await supabase.from('scraper_blocklist').insert(item);
    if (bErr && !bErr.message.includes('duplicate key')) {
        console.error('Error inserting blocklist:', bErr.message);
    }
  }
  console.log('Blocklist successfully seeded!');
}

run();
