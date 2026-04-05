-- Supabase Cloud Scenes Migration
-- Run this in your Supabase SQL Editor

-- 1. Create the table
CREATE TABLE public.shared_scenes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    author_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    author_username TEXT NOT NULL,
    name TEXT NOT NULL,
    scene_payload JSONB NOT NULL,
    downloads INTEGER DEFAULT 0,
    upvotes INTEGER DEFAULT 0,
    is_public BOOLEAN DEFAULT false
);

-- 2. Enable Row Level Security (RLS)
ALTER TABLE public.shared_scenes ENABLE ROW LEVEL SECURITY;

-- 3. Policy: Allow anyone (even anonymous if you want, or just authenticated) to read public scenes
CREATE POLICY "Public scenes are widely viewable"
ON public.shared_scenes
FOR SELECT
USING (is_public = true);

-- 4. Policy: Allow authenticated users to insert their own scenes
CREATE POLICY "Users can create scenes"
ON public.shared_scenes
FOR INSERT
TO authenticated
WITH CHECK (auth.uid() = author_id);

-- 5. Policy: Allow authenticated users to view their own private scenes
CREATE POLICY "Users can view own private scenes"
ON public.shared_scenes
FOR SELECT
TO authenticated
USING (auth.uid() = author_id);

-- 6. Policy: Allow authenticated users to update their own scenes
CREATE POLICY "Users can update own scenes"
ON public.shared_scenes
FOR UPDATE
TO authenticated
USING (auth.uid() = author_id)
WITH CHECK (auth.uid() = author_id);

-- 7. Policy: Allow authenticated users to delete their own scenes
CREATE POLICY "Users can delete own scenes"
ON public.shared_scenes
FOR DELETE
TO authenticated
USING (auth.uid() = author_id);

-- 8. Function: Securely increment upvotes without needing broad UPDATE permissions
CREATE OR REPLACE FUNCTION increment_scene_upvote(scene_id UUID)
RETURNS void AS $$
BEGIN
  UPDATE public.shared_scenes
  SET upvotes = upvotes + 1
  WHERE id = scene_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- 9. Function: Securely increment downloads
CREATE OR REPLACE FUNCTION increment_scene_download(scene_id UUID)
RETURNS void AS $$
BEGIN
  UPDATE public.shared_scenes
  SET downloads = downloads + 1
  WHERE id = scene_id;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;
