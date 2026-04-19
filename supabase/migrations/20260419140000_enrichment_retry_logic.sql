-- 1. Add retry_count to track enrichment attempts
ALTER TABLE skate_spots ADD COLUMN IF NOT EXISTS retry_count INTEGER DEFAULT 0;

-- 2. Update verification_status to include 'ENRICHED'
-- First, drop the old constraint if it exists (might have a system name, we'll try to target by typical naming or just re-apply)
DO $$
BEGIN
    ALTER TABLE skate_spots DROP CONSTRAINT IF EXISTS skate_spots_verification_status_check;
EXCEPTION
    WHEN undefined_object THEN
        NULL;
END $$;

ALTER TABLE skate_spots ADD CONSTRAINT skate_spots_verification_status_check 
CHECK (verification_status IN ('PENDING', 'ENRICHED', 'VERIFIED', 'REJECTED'));

-- 3. Update get_next_spot_to_enrich() RPC for round-robin cycle and status awareness
CREATE OR REPLACE FUNCTION get_next_spot_to_enrich()
RETURNS SETOF skate_spots AS $$
  SELECT s.* FROM skate_spots s
  CROSS JOIN (
    SELECT state_override, target_facilities 
    FROM scraper_config 
    WHERE id = 1
  ) c
  WHERE 
    (s.verification_status IN ('PENDING', 'ENRICHED') OR s.verification_status IS NULL)
    AND (
      c.state_override IS NULL 
      OR array_length(c.state_override, 1) IS NULL 
      OR array_length(c.state_override, 1) = 0 
      OR s.state = ANY(c.state_override)
    )
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
    s.last_attempted_at ASC NULLS FIRST, -- ROUND ROBIN CYCLE
    s.id ASC
  LIMIT 1;
$$ LANGUAGE sql;
