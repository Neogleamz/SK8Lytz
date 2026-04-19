-- 1. Update get_next_spot_to_enrich() RPC to remove configuration bindings
-- Phase 2 daemon should process ALL queued records regardless of Phase 1 targets
CREATE OR REPLACE FUNCTION get_next_spot_to_enrich()
RETURNS SETOF skate_spots AS $$
  SELECT s.* FROM skate_spots s
  WHERE 
    (s.verification_status IN ('PENDING', 'ENRICHED') OR s.verification_status IS NULL)
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
