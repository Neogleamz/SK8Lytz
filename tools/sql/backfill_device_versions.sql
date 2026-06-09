-- =================================================================================
-- Script: backfill_device_versions.sql
-- Description: Backfills missing ble_version, led_version, and firmware_ver in 
--              the registered_devices table using the latest telemetry data from 
--              discovered_devices_telemetry.
-- =================================================================================

BEGIN;

WITH latest_telemetry AS (
  SELECT DISTINCT ON (device_mac) 
    device_mac, 
    ble_version, 
    led_version, 
    firmware_ver
  FROM public.discovered_devices_telemetry
  WHERE ble_version IS NOT NULL 
     OR led_version IS NOT NULL 
     OR firmware_ver IS NOT NULL
  ORDER BY device_mac, discovered_at DESC
)
UPDATE public.registered_devices rd
SET 
  ble_version = COALESCE(rd.ble_version, lt.ble_version),
  led_version = COALESCE(rd.led_version, lt.led_version),
  firmware_ver = COALESCE(rd.firmware_ver, lt.firmware_ver)
FROM latest_telemetry lt
WHERE rd.device_mac = lt.device_mac
  AND (
    rd.ble_version IS NULL AND lt.ble_version IS NOT NULL OR
    rd.led_version IS NULL AND lt.led_version IS NOT NULL OR
    rd.firmware_ver IS NULL AND lt.firmware_ver IS NOT NULL
  );

COMMIT;
