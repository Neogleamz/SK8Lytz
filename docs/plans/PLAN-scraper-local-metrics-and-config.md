# Migrate Final Scraper Tables & Repair Pipeline Metrics

The recent scraper migration to local SQLite missed a few secondary dependencies that still query the production Supabase database. This causes broken metrics in the dashboard and retains a cloud dependency for config/blocklisting.

## Objective
1. Migrate `scraper_config`, `scraper_blocklist`, and `scraper_blocklist_keywords` from Supabase to `LocalDB` (SQLite).
2. Refactor the `CCTower.ts` `/api/pipeline-stats` endpoint to compute pipeline stage metrics locally instead of relying on a Supabase RPC function.

## Data Migration Strategy

As requested, we will pull down all existing cloud data before deprecating the Supabase tables.
We will create a one-off `migrate_supabase_to_local.ts` script that will:
1. Connect to Supabase to fetch all rows from `scraper_config`, `scraper_blocklist`, and `scraper_blocklist_keywords`.
2. Insert those rows into the newly created tables in `LocalDB.ts`.

## Proposed Changes

### `LocalDB.ts`
- **[MODIFY] `tools/scraper/core/LocalDB.ts`**
  - **Schema Updates**: Add CREATE TABLE statements for `scraper_config` (single row config), `scraper_blocklist`, and `scraper_blocklist_keywords`.
  - **CRUD Methods**: Add `getConfig()`, `updateConfig()`, `getBlocklist()`, `addBlocklist()`, `deleteBlocklist()`, `getBlocklistKeywords()`, `addBlocklistKeyword()`.
  - **Metrics Method**: Add `getPipelineStats(states?: string[])` to execute a local `db.prepare` aggregate query. It will use `SUM(CASE WHEN verification_status = 'X' THEN 1 ELSE 0 END)` to mirror the previous RPC output so the dashboard UI requires zero changes.

### `CCTower.ts`
- **[MODIFY] `tools/scraper/CCTower.ts`**
  - Refactor `GET /config` and `POST /config` to use `LocalDB.getConfig() / updateConfig()`.
  - Refactor `GET /api/scraper/blocklist`, `POST /api/scraper/blocklist`, and `DELETE /api/scraper/blocklist/:id` to use `LocalDB` methods.
  - Refactor `DELETE /api/skate_spots/:id` to use `LocalDB.addBlocklistKeyword()`.
  - Refactor `GET /api/pipeline-stats` to call `LocalDB.getPipelineStats()` instead of `supabase.rpc('get_pipeline_stats')`.

### `GoogleSweep.ts`
- **[MODIFY] `tools/scraper/GoogleSweep.ts`**
  - Remove Supabase import.
  - Refactor line 61 to fetch keywords using `LocalDB.getBlocklistKeywords()` instead of querying Supabase.

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` from the root directory to verify type safety across the scraper stack.

### Manual Verification
- Restart the scraper stack (`pm2 restart CCTower`).
- Open the local scraper dashboard and verify that the "Pipeline Stats" table populates correctly with the numbers matching the SQLite database states.
- Test adding a spot to the blocklist via the UI and verify it saves to the local SQLite database.
