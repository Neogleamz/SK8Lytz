# Implementation Plan

## Task: sweep-group-sync
**Slug:** sweep-group-sync
**Wave:** [WAVE:4] — Prerequisite: Wave 3 fully merged
**Size:** [Feast] — 10 files
**Risk:** [H-RISK] — GroupRepository and CrewService are data-layer singletons; type safety here affects all crew features
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_GROUP_SYNC_findings.json`
**Prerequisite:** Wave 3 fully merged

## Goal
Fix 22 findings in the group sync and crew layer. Critical: eliminate 4 `as any` type assertions in `GroupRepository` and `CrewService` where database rows are cast without shape validation — these are data corruption vectors. Fix the stale closure re-entrancy bug in `useCrewProximityRadar`. Add proper 4-state UI matrices to `CrewLandingScreen` and `CrewJoinScreen`. PII-scrub 3 raw user data leaks in crew telemetry logs. Fix 6 missing AppLogger telemetry context fields.

## Decision Log
- **`GroupRepository` `as any` (CONFIRMED — R-08 HIGH)**: `GroupRepository.ts` casts retrieved DB rows with `as any` at L27, L62, and L227 to bypass compiler validation. These must be replaced with typed Supabase-generated types from `src/types/supabase.ts` or explicit interfaces.
- **`CrewService` `as any` (R-08 HIGH)**: L712 casts the Supabase query object directly to `any` to call `.execute()`. This is a Supabase client API misuse — use the correct typed query builder chain.
- **Stale closure in `useCrewProximityRadar` (CONFIRMED — R-12 HIGH)**: `crewService.isNearby` is a non-reactive singleton method referenced inside a `useEffect` dependency array. The effect never re-runs when the proximity state changes. Must restructure to use a reactive state value.
- **PII leaks (R-09 MEDIUM)**: `CrewService` logs `leaderName` in plain text, `CrewProfileService` logs raw search queries (usernames/display names), `useCrewManage` logs raw user search text. All must be replaced with `'[REDACTED]'`.
- **4-state matrix (R-14 HIGH)**: `CrewLandingScreen` renders the public active sessions list with no loading, error, or empty states. `CrewJoinScreen` does the same. Users see a blank screen with no feedback.

## Files to Create/Modify

### [MODIFY] src/services/GroupRepository.ts
- L27: Replace `as any` method resolution with typed interface (R-08)
- L62: Replace `as any` property assignment with typed device config type (R-08)
- L227: Replace `any[]` row cast with `Database['public']['Tables']['groups']['Row'][]` from supabase types (R-08)
- L276: Add `instanceof Error` check in catch block (R-06)
- L322: Add `instanceof Error` check in catch block (R-06)
- L46, L276, L322: Fix AppLogger calls — add `payload_size` and `ssi` context (R-04)

### [MODIFY] src/services/CrewService.ts
- L712: Replace `as any` on Supabase query — use correct typed `.select()` chain (R-08)
- L125: Fix `AppLogger.error` — add `payload_size` and `ssi` context (R-04)
- L372: Replace `leaderName` in AppLogger with `'[REDACTED]'` (R-09)

### [MODIFY] src/services/CrewProfileService.ts
- L593: Replace raw search query text in AppLogger warning with `'[REDACTED]'` (R-09)

### [MODIFY] src/hooks/useCrewHub.ts
- L19: Fix `AppLogger.warn` — add `payload_size` and `ssi` context (R-04)
- L124: Fix `AppLogger.warn` — replace double string params with context object (R-04)

### [MODIFY] src/hooks/useCrewManage.ts
- L92: Replace raw user search query in AppLogger with `'[REDACTED]'` (R-09)

### [MODIFY] src/hooks/useCrewProximityRadar.ts
- L131: Fix stale closure — replace non-reactive `crewService.isNearby` dep with a reactive state value from the proximity radar result (R-12)

### [MODIFY] src/hooks/useCrewSession.ts
- L79: Flip write order — write to local state/cache first, then sync to Supabase asynchronously (R-05)

### [MODIFY] src/components/CrewMemberDashboard.tsx
- L177: Replace `as unknown as CrewMemberRow[]` with properly typed Supabase row type (R-08)
- L190: Fix `AppLogger.warn` — add `payload_size` and `ssi` context (R-04)
- L163: Add abort guard to `loadMembers` inside `useEffect`/`setInterval` — check if mounted before updating state (R-26)

### [MODIFY] src/components/crew/CrewLandingScreen.tsx
- L87: Add full 4-state UI matrix (loading spinner, error view, empty view with CTA, success list) for public active sessions (R-14)

### [MODIFY] src/components/crew/CrewJoinScreen.tsx
- L40: Add loading and empty states to the active sessions list render (R-14)

## Out of Scope
- `CrewService.ts` monolith extraction (~31KB) — separate task
- No BLE or protocol layer changes

## Verification Plan
- `npm run verify` — TSC must pass; all `as any` casts in GroupRepository must be eliminated
- `grep 'as any\|as unknown as' src/services/GroupRepository.ts src/services/CrewService.ts` must return zero matches
