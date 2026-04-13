# [L-RISK] [Snack] [BATCH] Maintenance Sweep #2

## Goal
Execute a dual-snack maintenance sweep to sanitize Crew Hub screens and seed the US Skate Spots map with curated data.

## Proposed Changes

### [CHORE] Crew Hub Comment Cleanup
Remove redundant placeholder comments from the core screens.

- `src/components/crew/CrewManageScreen.tsx` (Line 16)
- `src/components/crew/CrewDetailScreen.tsx` (Line 15)
- `src/components/crew/CrewCreateScreen.tsx` (Line 16)

---

### [FEAT] Curated US Skate Spots
Seed the `skate_spots` table with iconic US Rinks.

- **Migration**: `tools/migrations/20260413_seed_us_spots.sql`
- **Data**: 20+ records (Oaks Park, Skateland, RollerJam USA, etc.)

## Verification
- `npx tsc --noEmit` (Code safety)
- `SELECT COUNT(*) FROM skate_spots` (Data verification)
- Manual check of `SkateMapScreen` markers.
