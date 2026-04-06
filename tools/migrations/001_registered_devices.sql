-- SK8Lytz — registered_devices migration
-- Run this in the Supabase SQL Editor for project: qefmeivpjyaukbwadgaz
-- https://supabase.com/dashboard/project/qefmeivpjyaukbwadgaz/sql

CREATE TABLE IF NOT EXISTS registered_devices (
  id                 uuid DEFAULT gen_random_uuid() PRIMARY KEY,
  user_id            uuid REFERENCES auth.users(id) ON DELETE CASCADE NOT NULL,

  -- Primary BLE identifier
  device_mac         text NOT NULL,

  -- Secondary fingerprint (MAC randomization fallback)
  firmware_ver       int,
  led_version        int,
  product_id         int,

  -- User-assigned identity
  device_name        text NOT NULL,
  product_type       text NOT NULL CHECK (product_type IN ('HALOZ', 'SOULZ')),
  position           text CHECK (position IN ('Left', 'Right')),
  group_name         text NOT NULL,

  -- Hardware config from BLE probe (0x63 response)
  led_points         int,
  segments           int,
  ic_type            text,
  color_sorting      text,

  -- Registration metadata
  rssi_at_register   int,
  is_pending_sync    boolean DEFAULT false,
  registered_at      timestamptz DEFAULT now(),
  updated_at         timestamptz DEFAULT now(),

  UNIQUE(user_id, device_mac)
);

-- Auto-update updated_at on any row change
CREATE OR REPLACE FUNCTION update_registered_devices_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_registered_devices_updated_at
  BEFORE UPDATE ON registered_devices
  FOR EACH ROW EXECUTE FUNCTION update_registered_devices_updated_at();

-- Row Level Security
ALTER TABLE registered_devices ENABLE ROW LEVEL SECURITY;

-- Users fully manage their own rows
CREATE POLICY "users_manage_own_devices"
  ON registered_devices
  FOR ALL
  USING (auth.uid() = user_id)
  WITH CHECK (auth.uid() = user_id);

-- Any authenticated user can SELECT to check if a device_mac is claimed
-- (needed for the claim flow in DeviceSettingsModal)
CREATE POLICY "authenticated_check_device_claimed"
  ON registered_devices
  FOR SELECT
  TO authenticated
  USING (true);

-- Performance indexes
CREATE INDEX IF NOT EXISTS idx_registered_devices_device_mac ON registered_devices (device_mac);
CREATE INDEX IF NOT EXISTS idx_registered_devices_user_id ON registered_devices (user_id);
