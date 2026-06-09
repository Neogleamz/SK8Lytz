-- Add builder fields to sk8lytz_picks table
ALTER TABLE "public"."sk8lytz_picks" 
ADD COLUMN IF NOT EXISTS "builder_nodes" JSONB,
ADD COLUMN IF NOT EXISTS "builder_transition_type" INT2,
ADD COLUMN IF NOT EXISTS "builder_direction" INT2,
ADD COLUMN IF NOT EXISTS "builder_fill_mode" TEXT;
