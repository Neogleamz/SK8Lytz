-- Convert state_override from TEXT to TEXT[] to allow targeting multiple states at once
ALTER TABLE scraper_config 
ALTER COLUMN state_override TYPE TEXT[] USING 
    CASE 
        WHEN state_override IS NULL OR state_override = '' THEN ARRAY[]::TEXT[]
        ELSE ARRAY[state_override]
    END;

-- Set default value to empty array if null
UPDATE scraper_config SET state_override = ARRAY[]::TEXT[] WHERE state_override IS NULL;

-- Rewrite the RPC
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
    s.id ASC
  LIMIT 1;
$$ LANGUAGE sql;
