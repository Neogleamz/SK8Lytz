-- 1. Add evasion and circuit breaker columns to scraper_config
ALTER TABLE scraper_config ADD COLUMN IF NOT EXISTS cooldown_base_ms INT DEFAULT 300000;
ALTER TABLE scraper_config ADD COLUMN IF NOT EXISTS cooldown_jitter_pct INT DEFAULT 20;
ALTER TABLE scraper_config ADD COLUMN IF NOT EXISTS max_consecutive_errors INT DEFAULT 3;
ALTER TABLE scraper_config ADD COLUMN IF NOT EXISTS auto_resume_enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE scraper_config ADD COLUMN IF NOT EXISTS identity_rotation_enabled BOOLEAN DEFAULT TRUE;
ALTER TABLE scraper_config ADD COLUMN IF NOT EXISTS randomize_viewport_enabled BOOLEAN DEFAULT TRUE;

-- 2. Open up RLS for the scraper_config table since we are in a local control plane environment
-- Allowing the anon key (local dashboard) to manage this single configuration row
ALTER TABLE scraper_config ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Allow anon all on scraper_config" ON scraper_config;
CREATE POLICY "Allow anon all on scraper_config" ON scraper_config
    FOR ALL
    USING (TRUE)
    WITH CHECK (TRUE);

-- 3. Ensure the singleton configuration row exists
INSERT INTO scraper_config (id, state_override, target_facilities)
VALUES (1, ARRAY[]::TEXT[], ARRAY['skatepark', 'roller_rink', 'pro_shop']::TEXT[])
ON CONFLICT (id) DO NOTHING;

-- 4. Update the RPC to be more resilient to empty config rows
DROP FUNCTION IF EXISTS get_next_spot_to_enrich();

CREATE OR REPLACE FUNCTION get_next_spot_to_enrich()
RETURNS TABLE (
    id UUID,
    name TEXT,
    city TEXT,
    state TEXT,
    facility_type TEXT,
    website TEXT
) LANGUAGE sql AS $$
    SELECT 
        s.id, s.name, s.city, s.state, s.facility_type, s.website
    FROM skate_spots s
    WHERE 
        (s.verification_status = 'PENDING' OR s.verification_status IS NULL)
        AND (
            NOT EXISTS (SELECT 1 FROM scraper_config c WHERE c.id = 1 AND array_length(c.state_override, 1) > 0)
            OR s.state = ANY (SELECT UNNEST(c.state_override) FROM scraper_config c WHERE c.id = 1)
        )
        AND (
            NOT EXISTS (SELECT 1 FROM scraper_config c WHERE c.id = 1 AND array_length(c.target_facilities, 1) > 0)
            OR s.facility_type = ANY (SELECT UNNEST(c.target_facilities) FROM scraper_config c WHERE c.id = 1)
        )
    ORDER BY s.last_attempted_at ASC NULLS FIRST
    LIMIT 1;
$$;
