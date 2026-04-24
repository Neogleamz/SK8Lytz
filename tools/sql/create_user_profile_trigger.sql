-- Creates the essential database trigger that generates a user_profile when a new user signs up.

-- 1. Create the function that does the work
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
DECLARE
  generated_username TEXT;
  default_color TEXT;
BEGIN
  -- Extract metadata provided during signup
  generated_username := NEW.raw_user_meta_data->>'username';
  
  -- Fallback if no username was provided
  IF generated_username IS NULL OR generated_username = '' THEN
    generated_username := 'skater_' || substr(NEW.id::text, 1, 6);
  END IF;

  -- Default avatar color
  default_color := '#FFAA00';

  -- Insert the profile
  INSERT INTO public.user_profiles (
    user_id,
    username,
    display_name,
    avatar_color,
    accepted_eula_version,
    role,
    is_banned
  ) VALUES (
    NEW.id,
    generated_username,
    COALESCE(NEW.raw_user_meta_data->>'display_name', generated_username),
    default_color,
    (NEW.raw_user_meta_data->>'accepted_eula_version')::int,
    'user',
    false
  );

  RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- 2. Bind the trigger to auth.users
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE PROCEDURE public.handle_new_user();
