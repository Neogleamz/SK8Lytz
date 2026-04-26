-- Migration to support real-time unified pipeline dashboard telemetry
ALTER TABLE scraper_config 
ADD COLUMN IF NOT EXISTS daemon_telemetry JSONB DEFAULT '{}'::jsonb;

-- Example structure of daemon_telemetry:
-- {
--   "scout": { "active_job": "id", "target": "Query: Kansas", "in_q": [], "rejected": [], "success": [] },
--   "spider": { "active_job": "uuid", "target": "Skate City", "in_q": [], "rejected": [], "success": [] },
--   "detective": { ... },
--   "photographer": { ... },
--   "publisher": { ... }
-- }
