-- Nuclear Option: Purge Telemetry Errors and Snapshots
-- This script completely wipes the telemetry logging tables to start fresh for testing.

TRUNCATE TABLE public.telemetry_errors;
TRUNCATE TABLE public.telemetry_snapshots;

-- Note: TRUNCATE is faster than DELETE and resets any auto-incrementing counters (though these use UUIDs).
