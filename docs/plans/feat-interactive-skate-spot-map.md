# Implementation Plan: Interactive Skate Spot Map & DOM Crawler

Build a massively detailed, interactive global map of roller skating rinks, powered by a 100% free "Zero-API" backend extraction pipeline that heuristically scrapes rich data (hours, photos, vibes, pricing) and serves it through a clustered React Native Map.

## User Review Required

> [!CAUTION]
> **Data Scraping Legality & TOS**
> We are using an Open-Source seed (OSM), but scraping Google Maps DOM and Facebook DOM violates their Terms of Service if done commercially or at massive scale. This architecture relies on running the scraping script strictly on your local machine, rate-limiting the requests, and doing it as a one-time educational extraction (ETL) run, rather than a persistent cloud service. Please confirm you understand this constraint.

> [!IMPORTANT]
> **Dependency Approvals**
> The UI phase requires `react-native-map-clustering` and `supercluster` (already in `package.json`).
> The Scraper phase requires setting up a standalone Node project in a `tools/scraper` directory using `puppeteer` and `cheerio`. 

## Proposed Changes

---

### Phase 1: The Zero-API Crawler (Local Backend)

We aren't polluting the React Native app code with scraping logic. The scraper will be a standalone administrative node script run locally.

#### [NEW] `tools/scraper/package.json`
- Initialize a local Node script environment independent of Expo.
- Install `puppeteer`, `cheerio`, `axios`, and `@supabase/supabase-js`.

#### [NEW] `tools/scraper/SeedOverpass.ts`
- Performs zero-cost Axios calls to the OpenStreetMap Overpass API for `leisure=ice_rink` and `sport=roller_skating`.
- Saves base data (Lat/Lng, Name) to a local `seed.json`.
 
#### [NEW] `tools/scraper/GoogleShadowDOM.ts`
- Launches a headless Puppeteer browser.
- Reads `seed.json`, and sequentially searches Google Maps.
- Extracts DOM elements:
  - `<div class="hours">` matrix
  - Text from the Rating spans and Phone spans.
  - Intercepts network requests to grab the high-res cover image URL.
- Randomizes delays (`sleep(2000 - 5000)`) to avoid shadowbans.

#### [NEW] `tools/scraper/SocialVibeCrawler.ts`
- Uses DuckDuckGo search to locate the rink's Facebook page.
- Scans recent posts for `Regex(/18\+|21\+|adult night/i)`.
- Compiles the final enriched JSON and pushes to Supabase.

---

### Phase 2: Database Submersion (Supabase)

#### [NEW] Supabase Migration `add_skate_spots_table`
Execute an MCP database migration to hold our harvested data permanently.
- `id` (UUID, PK)
- `name` (Text)
- `lat` (Float8)
- `lng` (Float8)
- `hours` (JSONB)
- `phone` (Text)
- `cover_photo_url` (Text)
- `has_adult_night` (Boolean)
- `surface_type` (Enum: 'wood', 'concrete', 'asphalt', 'sport_court', 'unknown')
- `vibe_rating` (Float8)

---

### Phase 3: Display (React Native UI)

#### [NEW] `src/screens/SkateMapScreen.tsx`
- Utilizes `<MapView>` wrapped in the `react-native-map-clustering` provider to prevent Android crashes.
- Only executes a single backend call: `supabase.from('skate_spots').select('*')`. No commercial API calls are ever made on the client.

#### [NEW] `src/components/SkateSpotBottomSheet.tsx`
- The slide-up UI panel that handles the 4-State Matrix:
  - *Loading*: Skeleton outlines.
  - *Success*: High-res image header, rating, `has_adult_night` badge, and wood/concrete icon.
  - *Empty/Unverified*: "Suggest an Edit" CTA.

## Verification Plan

### Automated Tests
- Run the scraper against 5 test cities locally, verifying that Google does not throw Captchas on the Puppeteer browser.
- Run `tsc --noEmit` on the app frontend to ensure Supabase type generation correctly mirrors the new table.

### Manual Verification
- Deploy the app to simulator or Web.
- Open the Skate Map; verify clustering works.
- Tap a cluster, tap a rink, and verify the slide-up modal smoothly renders the image URL fetched by the scraper without lag.
