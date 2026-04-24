-- Seed native math patterns as SK8Lytz Picks
INSERT INTO public.sk8lytz_picks (
    name, custom_name, mode, color, pattern_id, speed, brightness, 
    fixed_color_mode, fixed_fg_color, fixed_bg_color, fixed_hue, 
    is_active, sort_order
) VALUES 
-- 1. True Rainbow Flow
('True Rainbow Flow', 'True Rainbow Flow', 'MULTIMODE', '#FF0000', 19, 80, 100, 'GENERATIVE', '#FF0000', '#000000', 0, true, 1),
-- 2. Cyberpunk Shift
('Cyberpunk Shift', 'Cyberpunk Shift', 'MULTIMODE', '#00F0FF', 22, 90, 100, 'FG_BG', '#00F0FF', '#FF00FF', 180, true, 2),
-- 3. Neon Pulse
('Neon Pulse', 'Neon Pulse', 'MULTIMODE', '#FF00FF', 40, 70, 100, 'FG_BG', '#FF00FF', '#000000', 300, true, 3),
-- 4. Fire Flame
('Fire Flame', 'Fire Flame', 'MULTIMODE', '#FF4500', 39, 100, 100, 'FG_BG', '#FF4500', '#000000', 15, true, 4),
-- 5. Toxic Ooze (Smooth Breath)
('Toxic Ooze', 'Toxic Ooze', 'MULTIMODE', '#39FF14', 17, 40, 100, 'FG_BG', '#39FF14', '#000000', 120, true, 5),
-- 6. Cyan Comet
('Cyan Comet', 'Cyan Comet', 'MULTIMODE', '#00FFFF', 8, 85, 100, 'FG_BG', '#00FFFF', '#000000', 180, true, 6);
