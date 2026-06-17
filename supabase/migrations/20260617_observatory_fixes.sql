-- 1. Fix schema drift on label_designs
ALTER TABLE public.label_designs ADD COLUMN IF NOT EXISTS product_name text;

-- 2. Fix smallint overflow on telemetry tables by upgrading to integer
ALTER TABLE public.discovered_devices_telemetry ALTER COLUMN product_id TYPE integer;
ALTER TABLE public.discovered_devices_telemetry ALTER COLUMN firmware_ver TYPE integer;
ALTER TABLE public.discovered_devices_telemetry ALTER COLUMN ble_version TYPE integer;
ALTER TABLE public.discovered_devices_telemetry ALTER COLUMN led_version TYPE integer;
