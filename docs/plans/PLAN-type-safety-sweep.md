# Implementation Plan

## PLAN-type-safety-sweep — `any` Bypass / Type Laundering
*Source: `/deepdive-code-hunt` fleet | Rules: R-08 | Date: 2026-06-10*

### Problem
48 `any` casts and `as unknown as` type laundering patterns across 36 files. These bypass TypeScript's type system, eroding the compiler's ability to catch data contract violations. Concentrated in hooks, dashboard components, and crew screens.

### Source of Truth
- `artifacts/system_audit_report.md` — CLUSTER-01
- `src/types/supabase.ts` — generated DB types for proper row shapes

### Affected Files (Priority Groups)

**Group A — Protocol/BLE Hooks (H-RISK):**
- `src/hooks/useHardwareNotifications.ts` — 4 `any` usages on cfg/prev params → type with `HardwareConfig`
- `src/hooks/useControllerDispatch.ts:35` — `any` on hwSettings → `Record<string, unknown>`
- `src/hooks/useDockedControllerState.ts:159` — `any` on spatial segments → `Segment[]`
- `src/hooks/useDashboardController.tsx:17` — `any` on crewSession → typed interface
- `src/services/supabaseClient.ts:78` — `as unknown as` on offline mock → implement interface

**Group B — Dashboard Components:**
- `src/screens/DashboardScreen.tsx:628,1201` — two `any` casts
- `src/components/dashboard/DashboardTelemetryHero.tsx:10` — `forwardRef` untyped
- `src/components/dashboard/MySkatesSlab.tsx:21` — `any[]` devices
- `src/components/dashboard/RegisteredFleetSlab.tsx:15` — `any[]` registered devices
- `src/components/dashboard/SkateGroupCard.tsx:18` — `any` user profile

**Group C — Crew Screens:**
- `src/components/crew/CrewCreateScreen.tsx:122` — `(s: any)` → `(s: SkateSpot)`
- `src/components/crew/CrewControllerScreen.tsx:25` — `Record<string, any>` → `Record<string, unknown>`
- `src/components/crew/CrewJoinScreen.tsx:36` — explicit `any`
- `src/components/crew/CrewScheduleScreen.tsx:134,146` — two explicit `any`

**Group D — Admin Tools:**
- `src/components/admin/tools/GlobalAnalyticsPanel.tsx:26` — `as unknown` laundering
- `src/components/admin/tools/Sk8LytzProgrammer.tsx:501` — `as unknown` laundering
- `src/components/CommunityModal.tsx:66,147` — two `as unknown` laundering sites

**Group E — Utils/Services:**
- `src/utils/FlightRecorder.ts:5,12` — `any` → `unknown` / `Record<string, unknown>`
- `src/hooks/useStreetMode.ts:32,38` — two `any` casts on hwSettings/deviceContext
- `src/services/LocationService.ts:158,178` — two `any` on map row args
- `src/hooks/useProductCatalog.ts:28` — `Record<string, any>` → `Record<string, unknown>`
- `src/hooks/useProtocolBuilder.ts:24` — `any` on val param → `string | number`
- `src/hooks/useMapFilters.ts:58` — `any` on spots → `SkateSpot[]`
- `src/hooks/useSkateStats.ts:24` — missing type in catch block
- `src/hooks/useDeviceStateLedger.ts:48` — `any` on global object
- `src/services/HealthSyncService.ts:48` — `Record<string, any>`

### Implementation Steps
1. Fix Group A first (BLE/protocol domain — highest impact)
2. Fix Group B (dashboard — user-visible, medium risk)
3. Fix Group C (crew screens — isolated UI layer)
4. Fix Group D (admin tools — lowest blast radius)
5. Fix Group E (utils — wide but shallow fixes)
6. Run `npx tsc --noEmit` after each group to verify no regressions

### Verify
- `npm run verify` — zero new TS errors
- Before/after: `grep -rn "as any\|: any\|as unknown as" src/ --include="*.ts" --include="*.tsx" | wc -l` should decrease
