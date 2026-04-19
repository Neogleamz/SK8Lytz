-- Add Gold Standard columns to skate_spots
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS phone_number TEXT;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS is_published BOOLEAN DEFAULT false;

-- Add index for map performance
CREATE INDEX IF NOT EXISTS idx_skate_spots_is_published ON skate_spots(is_published) WHERE is_published = true;

-- Update RLS (if any) to ensure the scraper and dashboard can manage these
-- Assuming the existing scraper/anon roles already have access to skate_spots.
