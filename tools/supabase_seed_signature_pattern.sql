-- Seed SK8Lytz Signature Pattern into global picks
DELETE FROM sk8lytz_picks WHERE name = 'SK8Lytz Signature';

INSERT INTO sk8lytz_picks (
  name,
  custom_name,
  mode,
  pattern_id,
  speed,
  brightness,
  fixed_color_mode,
  fixed_fg_color,
  fixed_bg_color,
  is_active,
  sort_order
)
VALUES (
  'SK8Lytz Signature',
  null,
  'custom',
  44, 
  80,
  100,
  'FG_BG',
  '#00008B', 
  '#FF8C00', 
  true,
  1
);
