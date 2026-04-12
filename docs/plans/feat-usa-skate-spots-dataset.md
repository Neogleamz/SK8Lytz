# Plan: `feat/usa-skate-spots-dataset`

### Design Decisions & Rationale
We build and seed a Supabase `skate_spots` table with US rink/park data aggregated from Google Places API (for discovery) and manually verified by a Neogleamz curator. The dataset powers both the "Find a Place to Skate" map overlay and Geofence triggers. Google Places is the source-of-truth for unverified data; the Supabase `skate_spots` table is the source-of-truth for verified premium entries.

---

## Proposed Changes

### [NEW] Supabase Table: `skate_spots` (migration)
| Column | Type |
|---|---|
| `id` | uuid (PK) |
| `name` | text |
| `address` | text |
| `latitude` | float8 |
| `longitude` | float8 |
| `verified` | boolean (default false) |
| `surface_type` | text (e.g. 'hardwood', 'concrete', 'sport-court') |
| `has_adult_nights` | boolean |
| `hours_json` | jsonb |
| `geofence_radius_m` | int (default 150) |
| `created_at` | timestamptz |

### [NEW] `tools/seed-skate-spots.mjs`
- Node script to search Google Places for "roller skating rink" across all 50 US states and insert results into Supabase `skate_spots` with `verified: false`.

### [MODIFY] `src/services/SkateSpotsService.ts`
- Add `getNearbySpots(lat, lng, radiusMi)` — fetches from `skate_spots` with PostGIS `ST_DWithin` or a manual distance formula.

---

## Open Questions
- **Q:** Do we need Google Places API access? (Yes — requires a Maps API key in `.env.example`.)
- **Q:** What is the plan for ongoing data quality — community "claim and complete" flow?

## Verification Plan
1. Run seed script and verify rows appear in Supabase `skate_spots`.
2. Open SkateMapScreen and verify nearby verified/unverified spots appear as pins.
