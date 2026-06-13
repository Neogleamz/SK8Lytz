# [FEAT] USA Skate Spots Dataset Pipeline

The goal is to automatically generate and maintain a massive, zero-cost, nationwide dataset of Roller Rinks and Skateparks using OpenStreetMap's Overpass Data framework, completely bypassing expensive Google Places API fees or scraping rate-limits.

## Key Changes: Schema Expansion
Based on new requirements, we must augment the `skate_spots` Supabase database. We need to add 7 new columns to support complete operating details, links, and cultural metrics.

### 1. Address Requirements (Strictly Enforced)
- `street_address` (String) — Extracted from OSM's `addr:street` and `addr:housenumber`. 
- **Requirement**: If a raw coordinate lacks an address, the script will trigger a **Reverse-Geocoder API** (Nominatim). If the physical address cannot be resolved, the spot is instantly **DROPPED** from the dataset.

### 2. Operating Hours (Mon-Sun JSON)
- `opening_hours` (JSON) — Extracted from OSM's native `opening_hours` tag.
- **Requirement**: The ETL script will use a parser to convert complex OSM formats (`Mo-Fr 08:00-20:00`) into a normalized 7-day JSON matrix.

### 3. Web & Social Presence (Optional, High-Priority)
- `website` (String) 
- `socials` (JSON) — Extracted from `contact:instagram` or `contact:facebook`, or scraped from the company website.
- **Requirement**: Retained as optional to prevent dropping 85% of public outdoor skateparks. However, the DOM scraper will treat finding this data as a critical priority. 

### 4. Categorization & Business Details
- `facility_type` (String) — Differentiates between `'roller_rink'`, `'skatepark'`, or `'hybrid'`. Deduced automatically based on which OSM query triggered the spot, or via DOM analysis.
- `has_pro_shop` (Boolean) — Does this rink have an on-site shop?
- `has_adult_night` (Boolean) — Does this rink host 18+/21+ adult sessions?
- `vibe_rating` (Float/String) — An aggregate metric describing the spot's atmosphere.
- **Requirement**: Since OpenStreetMap does not natively track these niche metrics, we will resurrect the `GoogleShadowDOM.ts` crawler. The pipeline will pass the rink's name and city into Puppeteer, load the Google Search Knowledge Panel, and extract the vibe/shop details.

## Proposed Strategy: Osmosis Pipeline

### Phase 1: High-Yield Overpass Data Harvest
We will create a multi-threaded OSM scraper `tools/scraper/USANationalHarvest.ts` that runs two highly optimized QL queries:
1. **Rinks**: `area["ISO3166-1"="US"][admin_level=2]->.searchArea;  nwr["sport"~"roller_skating"](area.searchArea);`
2. **Parks**: `area["ISO3166-1"="US"][admin_level=2]->.searchArea;  nwr["leisure"="skatepark"](area.searchArea);`

### Phase 2: Sanitization and Reverse-Geocoding
The harvester will stream the raw GeoJSON/OSM output to a specialized JSON sanitizer.
- Attempt to extract exact address fields from OSM tags.
- Execute **Nominatim Reverse Geocoding** for missing addresses. Drop failures.
- Run the **OSM Hours Parser** to build the 7-day JSON object.

### Phase 3: Cultural DOM Enrichment & Social Retrieval (Headless)
For every valid Skate Spot that passes address validation:
- Launch headless Chrome/Puppeteer via `tools/scraper/GoogleShadowDOM.ts`.
- Perform targeted Google searches to infer `has_pro_shop`, `has_adult_night`, and generate the `vibe_rating`.
- Attempt to scrape the website URL from Google. Use the website to crawl for Instagram/Facebook links.

### Phase 4: Bulk Supabase Upload chunking
The script will slice the fully enriched dataset into staggered 50-row `upsert` batches to bypass Edge Function timeouts safely.

> [!WARNING]
> **Headless Scraper Time Sink**: Firing headless Chrome to do DOM analysis for 5,000+ US spots will take heavily extended time to prevent Google from IP blocking us (estimated 5-10 seconds per spot = ~10+ hours to run the full script locally). The script will save its progress to `db_progress_cache.json` so you can safely stop and resume the harvest.
