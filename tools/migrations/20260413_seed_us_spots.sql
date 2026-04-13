-- Maintenance Sweep #2: Seed US Skate Spots
-- Iconic US Roller Rinks for initial Map Grounding

INSERT INTO public.skate_spots (name, lat, lng, surface_type, is_indoor, source, is_verified)
VALUES 
    ('Oaks Park Roller Rink', 45.4718, -122.6614, 'wood', true, 'native_seed', true),
    ('Skateland Northridge', 34.2355, -118.5282, 'wood', true, 'native_seed', true),
    ('RollerJam USA', 40.5286, -74.1919, 'wood', true, 'native_seed', true),
    ('Cascades Skating Fun Center', 33.7485, -84.4842, 'wood', true, 'native_seed', true),
    ('Lynwood Sport Center', 41.5246, -87.5348, 'wood', true, 'native_seed', true),
    ('The Rink Fitness & Family Fun Center', 41.7371, -87.6041, 'wood', true, 'native_seed', true),
    ('Skateland Indianapolis', 39.8661, -86.0441, 'wood', true, 'native_seed', true),
    ('Orbit Skate Center', 42.1121, -88.0345, 'wood', true, 'native_seed', true),
    ('Riedell Way Rink', 44.5623, -92.5332, 'wood', true, 'native_seed', true),
    ('Moonlight Rollerway', 34.1425, -118.2551, 'wood', true, 'native_seed', true),
    ('World of Wheels Superior', 46.7208, -92.1041, 'wood', true, 'native_seed', true),
    ('Roller King Roseville', 38.7521, -121.2884, 'wood', true, 'native_seed', true),
    ('Crystal Palace Las Vegas', 36.1441, -115.2415, 'wood', true, 'native_seed', true),
    ('Anacostia Roller Rink', 38.8781, -76.9741, 'concrete', true, 'native_seed', true),
    ('Skate Zone Lake Worth', 26.6181, -80.0841, 'wood', true, 'native_seed', true),
    ('Peninsula Roller Rink', 37.0641, -76.4741, 'wood', true, 'native_seed', true),
    ('Great Skate Roseville', 42.5041, -82.9341, 'wood', true, 'native_seed', true),
    ('United Skates Clovis', 36.8241, -119.7041, 'wood', true, 'native_seed', true),
    ('Fun Plex Mt Laurel', 39.9541, -74.8841, 'sport_court', true, 'native_seed', true),
    ('Skate-O-Rama Grand Junction', 39.0641, -108.5541, 'wood', true, 'native_seed', true)
ON CONFLICT (name) DO NOTHING; -- Assuming name is unique for seeding purposes, otherwise check by Lat/Lng
