-- Add Google Places specific tracked fields to skate_spots
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS google_place_id TEXT UNIQUE;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS rating NUMERIC;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS user_ratings_total INTEGER;

-- Ensure an index on the google_place_id for fast lookups during the scraping process to prevent duplicates
CREATE INDEX IF NOT EXISTS idx_skate_spots_google_place_id ON skate_spots(google_place_id) WHERE google_place_id IS NOT NULL;
