-- Add fixed_direction field to sk8lytz_picks table
ALTER TABLE "public"."sk8lytz_picks" 
ADD COLUMN IF NOT EXISTS "fixed_direction" INT2;
