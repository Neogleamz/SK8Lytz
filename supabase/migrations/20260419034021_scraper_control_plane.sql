-- 1. Update skate_spots with Auto-Healing columns
ALTER TABLE skate_spots
ADD COLUMN IF NOT EXISTS last_attempted_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
ADD COLUMN IF NOT EXISTS verification_status TEXT DEFAULT 'PENDING' CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED'));

-- 2. Create scraper_config table
CREATE TABLE IF NOT EXISTS scraper_config (
    id SERIAL PRIMARY KEY,
    state_override TEXT,           -- 'CA', 'TX', NULL
    target_facilities TEXT[],      -- ['roller_rink', 'skate_park']
    sleep_interval_ms INTEGER DEFAULT 5000,
    is_active BOOLEAN DEFAULT false,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Initialize default row if missing
INSERT INTO scraper_config (id, is_active, target_facilities) 
VALUES (1, false, ARRAY['roller_rink', 'skatepark'])
ON CONFLICT (id) DO NOTHING;

-- 3. Rewrite get_next_spot_to_enrich RPC
CREATE OR REPLACE FUNCTION get_next_spot_to_enrich()
RETURNS SETOF skate_spots AS $$
  SELECT s.* FROM skate_spots s
  CROSS JOIN (
    SELECT state_override, target_facilities 
    FROM scraper_config 
    WHERE id = 1
  ) c
  WHERE 
    (s.verification_status = 'PENDING' OR s.verification_status IS NULL)
    AND (c.state_override IS NULL OR s.state = c.state_override)
    AND (
      c.target_facilities IS NULL 
      OR array_length(c.target_facilities, 1) IS NULL 
      OR array_length(c.target_facilities, 1) = 0 
      OR s.facility_type = ANY(c.target_facilities)
    )
  ORDER BY
    CASE
      WHEN s.facility_type = 'roller_rink' THEN 1
      WHEN s.facility_type = 'hybrid' THEN 2
      WHEN s.facility_type = 'pro_shop' THEN 3
      WHEN s.facility_type = 'skatepark' THEN 4
      ELSE 5
    END ASC,
    s.id ASC
  LIMIT 1;
$$ LANGUAGE sql;
