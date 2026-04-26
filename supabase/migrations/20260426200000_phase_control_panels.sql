-- Phase Control Panel Migration
-- Adds per-phase operational settings to scraper_config (singleton row id=1)
-- and creates a blocklist table for Phase 1 Scout management.

-- ── PHASE 1: Scout ──────────────────────────────────────────────────────────
ALTER TABLE scraper_config
  ADD COLUMN IF NOT EXISTS scout_search_queries       TEXT[]   DEFAULT ARRAY['roller rink', 'roller skating rink', 'skate park', 'inline skating rink'],
  ADD COLUMN IF NOT EXISTS scout_rate_limit_rpm       INTEGER  DEFAULT 12,
  ADD COLUMN IF NOT EXISTS scout_gatekeeper_rules     JSONB    DEFAULT '{
    "must_be_establishment": true,
    "not_in_blocklist": true,
    "allowed_facility_types": ["roller_rink", "skatepark", "hybrid", "inline_rink"]
  }'::jsonb;

-- ── PHASE 2: Crawl ───────────────────────────────────────────────────────────
ALTER TABLE scraper_config
  ADD COLUMN IF NOT EXISTS crawl_depth               INTEGER  DEFAULT 2,
  ADD COLUMN IF NOT EXISTS crawl_priority_paths      TEXT[]   DEFAULT ARRAY['/schedule', '/pricing', '/admission', '/about', '/events'],
  ADD COLUMN IF NOT EXISTS crawl_social_platforms    TEXT[]   DEFAULT ARRAY['facebook', 'instagram', 'tiktok', 'youtube', 'twitter'],
  ADD COLUMN IF NOT EXISTS crawl_http_timeout_ms     INTEGER  DEFAULT 15000,
  ADD COLUMN IF NOT EXISTS crawl_max_retries         INTEGER  DEFAULT 3,
  ADD COLUMN IF NOT EXISTS crawl_gatekeeper_rules    JSONB    DEFAULT '{
    "must_have_website": true,
    "require_http_200": true,
    "skip_already_crawled": false
  }'::jsonb;

-- ── PHASE 3: Detective ───────────────────────────────────────────────────────
ALTER TABLE scraper_config
  ADD COLUMN IF NOT EXISTS detective_model           TEXT     DEFAULT 'Llama3.2-8b',
  ADD COLUMN IF NOT EXISTS detective_confidence_min  INTEGER  DEFAULT 80,
  ADD COLUMN IF NOT EXISTS detective_temperature     NUMERIC  DEFAULT 0.1,
  ADD COLUMN IF NOT EXISTS detective_max_tokens      INTEGER  DEFAULT 2048,
  ADD COLUMN IF NOT EXISTS detective_gatekeeper_rules JSONB   DEFAULT '{
    "requires_pricing_or_schedule": true,
    "min_confidence_pct": 80
  }'::jsonb;

-- ── PHASE 4: Photographer ────────────────────────────────────────────────────
ALTER TABLE scraper_config
  ADD COLUMN IF NOT EXISTS photo_sources             TEXT[]   DEFAULT ARRAY['google_places', 'facebook', 'instagram', 'website'],
  ADD COLUMN IF NOT EXISTS photo_min_count           INTEGER  DEFAULT 1,
  ADD COLUMN IF NOT EXISTS photo_categories          TEXT[]   DEFAULT ARRAY['facade_exterior', 'skate_floor', 'arcade_zone', 'snack_bar', 'amenities', 'event_flyers', 'specials'],
  ADD COLUMN IF NOT EXISTS photo_min_size_kb         INTEGER  DEFAULT 50,
  ADD COLUMN IF NOT EXISTS photo_vision_api          TEXT     DEFAULT 'google_cloud_vision',
  ADD COLUMN IF NOT EXISTS photo_gatekeeper_rules    JSONB    DEFAULT '{
    "min_valid_photos": 1,
    "min_resolution_px": 400
  }'::jsonb;

-- ── PHASE 5: Publisher ───────────────────────────────────────────────────────
ALTER TABLE scraper_config
  ADD COLUMN IF NOT EXISTS publisher_auto_publish_threshold INTEGER DEFAULT 80,
  ADD COLUMN IF NOT EXISTS publisher_required_fields        TEXT[]  DEFAULT ARRAY['name','lat','lng','state','facility_type','rating'],
  ADD COLUMN IF NOT EXISTS publisher_auto_publish_enabled   BOOLEAN DEFAULT false,
  ADD COLUMN IF NOT EXISTS publisher_upsert_strategy        TEXT    DEFAULT 'MERGE',
  ADD COLUMN IF NOT EXISTS publisher_webhook_url            TEXT    DEFAULT NULL,
  ADD COLUMN IF NOT EXISTS publisher_gatekeeper_rules       JSONB   DEFAULT '{
    "must_have_photo": true,
    "must_have_surface_type": false,
    "must_have_rating": true,
    "must_have_hours_or_schedule": false
  }'::jsonb;

-- ── BLOCKLIST TABLE (Phase 1 Scout) ─────────────────────────────────────────
CREATE TABLE IF NOT EXISTS scraper_blocklist (
  id          SERIAL PRIMARY KEY,
  pattern     TEXT        NOT NULL,          -- e.g. "tony's skate shop", "iceskate.com"
  match_type  TEXT        NOT NULL DEFAULT 'name'  CHECK (match_type IN ('name', 'domain', 'phone', 'place_id')),
  reason      TEXT,                          -- optional operator note
  added_by    TEXT        DEFAULT 'operator',
  created_at  TIMESTAMPTZ DEFAULT NOW()
);

-- Allow anon dashboard access (same policy pattern as scraper_config)
ALTER TABLE scraper_blocklist ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Allow anon all on scraper_blocklist" ON scraper_blocklist;
CREATE POLICY "Allow anon all on scraper_blocklist" ON scraper_blocklist
  FOR ALL USING (TRUE) WITH CHECK (TRUE);

-- Seed a few example blocklist entries
INSERT INTO scraper_blocklist (pattern, match_type, reason) VALUES
  ('skate shop', 'name', 'Auto-exclusion: retail shop, not a venue'),
  ('ice rink', 'name', 'Auto-exclusion: ice skating, not roller'),
  ('bmx park', 'name', 'Auto-exclusion: BMX, not roller/inline')
ON CONFLICT DO NOTHING;
