-- Add foreign key reference to skate_spots inside skate_sessions
ALTER TABLE skate_sessions ADD COLUMN IF NOT EXISTS skate_spot_id UUID REFERENCES skate_spots(id) ON DELETE SET NULL;

-- Enable indexing for performance since we'll be querying sessions by spot frequently in the future
CREATE INDEX IF NOT EXISTS idx_skate_sessions_skate_spot_id ON skate_sessions(skate_spot_id);
