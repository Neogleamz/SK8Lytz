-- Add detailed telemetry preservation columns to registered_devices
ALTER TABLE public.registered_devices
ADD COLUMN factory_name text NULL,
ADD COLUMN manufacturer_data text NULL,
ADD COLUMN ble_version integer NULL;
