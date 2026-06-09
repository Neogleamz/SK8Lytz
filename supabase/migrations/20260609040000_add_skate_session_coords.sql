-- Add GPS Coordinate fields to skate_sessions for historical mapping
ALTER TABLE public.skate_sessions 
ADD COLUMN IF NOT EXISTS location_coords JSONB,
ADD COLUMN IF NOT EXISTS start_coords JSONB,
ADD COLUMN IF NOT EXISTS end_coords JSONB,
ADD COLUMN IF NOT EXISTS path_coords JSONB;
