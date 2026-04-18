ALTER TABLE skate_spots
ADD COLUMN IF NOT EXISTS last_enriched_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
ADD COLUMN IF NOT EXISTS socials JSONB DEFAULT '{}'::jsonb;

CREATE OR REPLACE FUNCTION get_next_spot_to_enrich()
RETURNS SETOF skate_spots AS $$
BEGIN
  RETURN QUERY
  SELECT * FROM skate_spots
  WHERE last_enriched_at IS NULL OR last_enriched_at < NOW() - INTERVAL '30 days'
  ORDER BY
    CASE
      WHEN facility_type = 'roller_rink' THEN 1
      WHEN facility_type = 'hybrid' THEN 2
      WHEN facility_type = 'pro_shop' THEN 3
      WHEN facility_type = 'skatepark' THEN 4
      ELSE 5
    END ASC,
    id ASC
  LIMIT 1;
END;
$$ LANGUAGE plpgsql;
