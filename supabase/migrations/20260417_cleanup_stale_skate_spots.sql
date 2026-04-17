-- Cleanup legacy skate spots that predated the Nationwide Harvesting Engine
-- Any spots lacking fundamental schema definitions are deleted to prevent map ghosting
DELETE FROM skate_spots
WHERE facility_type IS NULL 
   OR street_address IS NULL;
