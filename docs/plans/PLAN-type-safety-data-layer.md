# Implementation Plan — `refactor/type-safety-data-layer`

## Goal
Fix `as unknown as` type laundering and `payload: any` in the 6 core data layer service files, replacing unsafe double-casts with proper type guards or correctly typed Supabase queries.

## Source of Truth
- `artifacts/deepdive_raw/DOMAIN_DATA_LAYER_findings.json` — 27 findings
- `artifacts/deepdive_raw/R-08_findings.json` — data layer entries
- `artifacts/system_audit_report.md` §R-08 Data Layer cluster
- `src/types/supabase.ts` — generated Supabase types (source of truth for DB shape)

## Out of Scope
- UI component `any` casts (separate CLUSTER-03 task)
- Changing service business logic
- `src/types/supabase.ts` itself (auto-generated, exempt from R-23)

---

## Step 1 — Fix `src/services/ScenesService.ts` (6 instances)
**Lines:** 44 (`payload: any`), 63, 100, 221, 242 (`as unknown as SceneStep[]`), 375 (`as unknown as Database[...]`)

- **Step 1a:** Define `ScenePayload` type based on what `publishScene` receives. Replace `payload: any` on line 110.
- **Step 1b:** Lines 63 + 100: Use Supabase's generated return type directly instead of `as unknown as ICloudScene[]`. Either add `.returns<ICloudScene>()` to the query, or write a type guard `isICloudScene(row: unknown): row is ICloudScene`.
- **Step 1c:** Lines 221 + 242: Replace `p.nodes as unknown as SceneStep[]` with a proper type guard that validates the array shape.
- **Step 1d:** Line 375: Fix the `upsert` cast by constructing the insert object with explicit typed fields.
- **Verify:** `grep "as unknown as\|: any" src/services/ScenesService.ts` returns zero.

## Step 2 — Fix `src/services/GradientsService.ts` (3 instances)
**Lines:** 40, 55 (`as unknown as CustomBuilderPreset[]`), 109 (`as unknown as Database[...]`)

- **Action:** Same pattern as ScenesService — add `.returns<CustomBuilderPreset>()` or write a type guard. Fix the upsert cast with explicit typed fields.
- **Verify:** Zero `as unknown as` in GradientsService.

## Step 3 — Fix `src/services/SpeedTrackingService.ts` (2 instances)
**Lines:** 332 (`as unknown as SkateSessionRow[]`), 401 (`as unknown as AggRow[]`)

- **Action:** Define `SkateSessionRow` and `AggRow` from the Supabase generated types. Replace casts with proper `select` query typing.
- **Verify:** Zero type laundering in SpeedTrackingService.

## Step 4 — Fix `src/services/DeviceRepository.ts` (3 instances)
**Lines:** 515, 516 (`cloud as unknown as RegisteredDevice`), 523 (`any` in comment context), 781 (`any` in comment context)

- **Action:** Lines 515-516: The cloud→RegisteredDevice cast suggests `cloud` has an incompatible type. Trace the return type of whatever populates `cloud` and align `RegisteredDevice` with the Supabase column types.
- **Verify:** Zero `as unknown as` in the merge/sync logic section.

## Step 5 — Fix `src/services/supabaseClient.ts:64`
- **Action:** The `as unknown as ReturnType<typeof createClient<Database>>` cast suggests the client is conditionally constructed. Fix by typing the conditional branches correctly.
- **Verify:** Zero `as unknown as` in supabaseClient.

## Step 6 — Fix `src/hooks/useFavorites.ts:161` and `src/hooks/useCuratedPicks.ts:62`
- **Action:** Replace `payload as unknown as Database[...]` and `data.map((row: any)` with proper typed queries.
- **Verify:** Zero `as unknown as` and zero `: any` in these hooks.

## Step 7 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors. Existing errors count unchanged or improved.
