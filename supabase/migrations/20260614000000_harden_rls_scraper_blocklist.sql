-- Replace overly permissive anonymous RLS SELECT on scraper_blocklist
-- with authenticated-only policy
DROP POLICY IF EXISTS "Allow anon all on scraper_blocklist" ON public.scraper_blocklist;

CREATE POLICY "Allow authenticated read on scraper_blocklist" 
  ON public.scraper_blocklist
  FOR SELECT 
  TO authenticated 
  USING (true);
