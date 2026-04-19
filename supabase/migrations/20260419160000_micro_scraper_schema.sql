-- 1. Add new social media and routing columns to skate_spots representing the Indexer phase
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS instagram_url TEXT;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS facebook_url TEXT;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS tiktok_url TEXT;
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS schedule_url TEXT;

-- 2. Expand and replace the verification_status check constraint to support the new micro-scraper pipeline
DO $$
BEGIN
    ALTER TABLE skate_spots DROP CONSTRAINT IF EXISTS skate_spots_verification_status_check;
EXCEPTION
    WHEN undefined_object THEN
        NULL;
END $$;

ALTER TABLE skate_spots ADD CONSTRAINT skate_spots_verification_status_check 
CHECK (verification_status IN (
  'PENDING', 
  'IDENTITY_ESTABLISHED', 
  'INDEXED', 
  'ENRICHED', 
  'VERIFIED', 
  'REJECTED', 
  'DEPRECATED',
  'GOLD_STANDARD'
));

-- 3. Update the RPCs so they feed specific pipeline phases

-- The Operator targets PENDING
CREATE OR REPLACE FUNCTION get_next_spot_for_operator()
RETURNS SETOF skate_spots AS $$
  SELECT s.* FROM skate_spots s
  WHERE 
    (s.verification_status = 'PENDING' OR s.verification_status IS NULL)
  ORDER BY
    CASE
      WHEN s.facility_type = 'roller_rink' THEN 1
      WHEN s.facility_type = 'hybrid' THEN 2
      WHEN s.facility_type = 'pro_shop' THEN 3
      WHEN s.facility_type = 'skatepark' THEN 4
      ELSE 5
    END ASC,
    s.last_attempted_at ASC NULLS FIRST,
    s.id ASC
  LIMIT 1;
$$ LANGUAGE sql;

-- The Indexer targets IDENTITY_ESTABLISHED
CREATE OR REPLACE FUNCTION get_next_spot_for_indexer()
RETURNS SETOF skate_spots AS $$
  SELECT s.* FROM skate_spots s
  WHERE 
    s.verification_status = 'IDENTITY_ESTABLISHED'
  ORDER BY
    CASE
      WHEN s.facility_type = 'roller_rink' THEN 1
      WHEN s.facility_type = 'hybrid' THEN 2
      WHEN s.facility_type = 'pro_shop' THEN 3
      WHEN s.facility_type = 'skatepark' THEN 4
      ELSE 5
    END ASC,
    s.last_attempted_at ASC NULLS FIRST,
    s.id ASC
  LIMIT 1;
$$ LANGUAGE sql;
