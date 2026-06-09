-- Add is_enabled and description to sk8lytz_app_settings
ALTER TABLE public.sk8lytz_app_settings
ADD COLUMN IF NOT EXISTS is_enabled BOOLEAN DEFAULT true,
ADD COLUMN IF NOT EXISTS description TEXT;

-- Seed new unified visibility keys
INSERT INTO public.sk8lytz_app_settings (setting_key, setting_value, is_enabled, description)
VALUES 
    ('visibility_street_mode', '"visible_all"'::jsonb, true, 'Controls the visibility of Street Mode: visible_all, online_only, hidden_all'),
    ('visibility_maps_tab', '"visible_all"'::jsonb, true, 'Controls the visibility of the Maps Tab: visible_all, online_only, hidden_all'),
    ('visibility_crew_hub', '"visible_all"'::jsonb, true, 'Controls the visibility of the Crew Hub: visible_all, online_only, hidden_all'),
    ('visibility_community_hub', '"visible_all"'::jsonb, true, 'Controls the visibility of the Community Hub: visible_all, online_only, hidden_all')
ON CONFLICT (setting_key) DO UPDATE 
SET 
    is_enabled = EXCLUDED.is_enabled,
    description = EXCLUDED.description;

-- Clean up legacy keys that are now unified
DELETE FROM public.sk8lytz_app_settings 
WHERE setting_key IN (
    'global_maps_locked',
    'global_crew_hub_locked',
    'global_community_hub_locked',
    'offline_maps_hidden',
    'offline_crew_hub_hidden',
    'offline_community_hub_hidden'
);
