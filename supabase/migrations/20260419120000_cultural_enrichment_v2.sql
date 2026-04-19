-- Obsidian Tier: Advanced Cultural Profiling
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS surface_quality TEXT;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS vibe_score FLOAT;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS cultural_metadata JSONB DEFAULT '{}';

COMMENT ON COLUMN skate_spots.surface_quality IS 'Detected surface type: Buttery, Smooth, Crusty, Rough, etc.';
COMMENT ON COLUMN skate_spots.vibe_score IS 'Weighted sentiment score from user reviews (0-5).';
