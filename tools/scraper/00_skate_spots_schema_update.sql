-- Migration to augment the skate_spots table for the US National Dataset Pipeline

ALTER TABLE skate_spots
  ADD COLUMN IF NOT EXISTS street_address TEXT,
  ADD COLUMN IF NOT EXISTS opening_hours JSONB,
  ADD COLUMN IF NOT EXISTS website TEXT,
  ADD COLUMN IF NOT EXISTS socials JSONB,
  ADD COLUMN IF NOT EXISTS facility_type TEXT,
  ADD COLUMN IF NOT EXISTS has_pro_shop BOOLEAN DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS has_adult_night BOOLEAN DEFAULT FALSE,
  ADD COLUMN IF NOT EXISTS vibe_rating TEXT;
