# Add SK8Lytz Picks (Cloud Database Seeding)

## Goal
Seed the `sk8lytz_picks` Supabase table with 6 native hardware patterns (including Rainbow effects and various math-synthesized patterns) so they appear globally in all users' apps as curated favorites.

## User Review Required
Review the 6 selected patterns and their colors below. These are native `PatternEngine` effects.

## Proposed Changes

### `tools/sql/seed_sk8lytz_picks.sql`
- **[NEW]** Create a SQL script containing the `INSERT INTO sk8lytz_picks` commands.

```sql
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
```

### Execution Steps
1. Once the script is generated, the agent will execute it against the Supabase database using the MCP `execute_sql` tool.
2. We do NOT need to modify the React Native source code, as `useCuratedPicks.ts` is already wired to fetch this table.

## Verification Plan
1. Start the Dev Server and open the SK8Lytz App.
2. Navigate to the Favorites/Programs tab and verify the "SK8Lytz Picks" section appears and shows the 6 new cards.
3. Tap "Toxic Ooze" and verify the preview accurately switches to green pulsing mode.
