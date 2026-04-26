-- Migration to add AI Detective configuration columns to scraper_config and skate_spots

-- 1. Add AI configuration columns to scraper_config
ALTER TABLE scraper_config 
ADD COLUMN IF NOT EXISTS ai_system_prompt text DEFAULT 'You are a data extraction API for a roller skating app. Read the provided website text and extract the requested fields. If a value is missing, return null. Do not guess. You must return valid JSON.',
ADD COLUMN IF NOT EXISTS ai_exclusion_keywords text[] DEFAULT ARRAY['bicycle', 'ice hockey', 'rv', 'camper', 'ice rink', 'bmx'],
ADD COLUMN IF NOT EXISTS ai_target_vectors jsonb DEFAULT '[
  { "key": "hours", "type": "JSON", "instruction": "Extract the weekly operating schedule" },
  { "key": "pricing_data", "type": "JSON", "instruction": "Extract admission and rental prices" },
  { "key": "has_adult_night", "type": "boolean", "instruction": "True if they explicitly mention 18+, 21+, or adult-only nights" },
  { "key": "has_pro_shop", "type": "boolean", "instruction": "True if they sell roller skates or gear" },
  { "key": "special_events", "type": "array", "instruction": "List of special theme nights or events" }
]'::jsonb;

-- 2. Add AI metadata fallback to skate_spots for dynamic vectors
ALTER TABLE skate_spots
ADD COLUMN IF NOT EXISTS ai_metadata jsonb DEFAULT '{}'::jsonb;
