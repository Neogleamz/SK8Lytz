ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS notif_preferences JSONB DEFAULT '{}';
