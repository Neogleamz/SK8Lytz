-- 1. Add pricing_data jsonb column to hold heuristic extraction snippets
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS pricing_data JSONB;

-- 2. Add special_events jsonb column to hold schedule/special event findings
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS special_events JSONB;
