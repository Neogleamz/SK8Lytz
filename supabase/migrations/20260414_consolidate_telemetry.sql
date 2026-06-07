CREATE TABLE telemetry_snapshots (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  created_at TIMESTAMPTZ DEFAULT now(),
  session_id TEXT NOT NULL,
  device_id TEXT,
  event_type TEXT NOT NULL,
  metadata JSONB DEFAULT '{}'::jsonb
);

CREATE INDEX idx_telemetry_snapshots_metadata ON telemetry_snapshots USING GIN (metadata);

ALTER TABLE telemetry_snapshots ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Enable insert for authenticated users and anon" 
ON telemetry_snapshots FOR INSERT 
TO authenticated, anon 
WITH CHECK (true);


DROP TABLE IF EXISTS parsed_logs CASCADE;
DROP TABLE IF EXISTS parsed_mode_usage CASCADE;
DROP TABLE IF EXISTS parsed_pattern_usage CASCADE;
DROP TABLE IF EXISTS parsed_color_usage CASCADE;
DROP TABLE IF EXISTS parsed_session_devices CASCADE;
DROP TABLE IF EXISTS device_diagnostics CASCADE;
