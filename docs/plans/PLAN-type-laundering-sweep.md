# Implementation Plan

## Task: fix/type-laundering-sweep
**Cluster:** B — Type Safety & `as unknown as X` Laundering  
**Batch:** `[BATCH:type-safety-sweep]`  
**Rule Violated:** R-08  
**Severity:** HIGH × 9, MEDIUM × 14, LOW × 5  
**Risk:** M-RISK  
**Size:** Feast (28 confirmed prod casts, 22+ files)

## Cited Truth
- `src/services/ble/ConnectService.ts:121` — `as unknown as Device`  
- `src/hooks/ble/useBLEScanner.ts:116` — `as unknown as TelemetryInsert[]`  
- `src/screens/DashboardScreen.tsx:265` — `as unknown as DisplayDevice`  
- `src/services/ble/BleMachine.ts:28` — `as unknown as { type: string; output? }`  
- `src/services/CrewService.ts:329` — `as unknown as CrewSession[]`  
- `src/services/LocationService.ts:158,166` — `as unknown as DB_CrewSession[]`  
- `src/components/CommunityModal.tsx:138` — `as unknown as Record<string, any>`  
- Full list: grep `as unknown as` in `src/` excluding `__tests__/` and `__mocks__/`

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster B, lines 146–168  
- R-08 Raw: `artifacts/deepdive_raw/R-08_findings.json`  
- Agent Behavior: `.agents/rules/agent-behavior.md` Rule 1 (No `any` Cast Law)

## Problem
28 `as unknown as X` type laundering casts in production code bypass TypeScript's structural guarantees. In the BLE domain, an incorrectly typed `DeviceConfig` or `BLEPeripheral` will produce malformed payloads dispatched to hardware at runtime with no compile-time protection.

## Triage Categories

### Category 1 — BLE Data Path (HIGH — 9 casts)
These casts are in code that directly produces BLE writes. Priority: fix first.

| File | Line | Cast | Fix Strategy |
|---|---|---|---|
| `ConnectService.ts:121` | `as unknown as Device` | Add `isDev(peripheral)` type guard function |
| `useBLEScanner.ts:116` | `as unknown as TelemetryInsert[]` | Cast to correct DB Insert type; verify field map |
| `useBLEScanner.ts:319-322` | Virtual device object casts | Wrap in `satisfies Device` or fix partial type |
| `DashboardScreen.tsx:264-265` | `as unknown as DisplayDevice` | Merge cfg/d at type level; no cast needed |
| `BleMachine.ts:28` | XState event cast | Add typed `DoneInvokeEvent<{devices: Device[]}>` union |
| `useBLE.ts` | `peripheral as unknown as BLEPeripheral` | Add type guard `isPeripheral()` in `src/types/` |

### Category 2 — Supabase Data Shape (MEDIUM — 8 casts)
Supabase `.data` results being cast instead of properly typed.

| File | Line | Fix Strategy |
|---|---|---|
| `CrewService.ts:329` | `as unknown as CrewSession[]` | Map raw Supabase rows to typed objects explicitly |
| `LocationService.ts:158,166` | `as unknown as DB_CrewSession[]` | Add `mapToCrewSession()` mapper function |
| `GradientsService.ts:114` | nodes cast | Use DB generated type directly |
| `GlobalAnalyticsPanel.tsx:22` | `as unknown as GlobalAnalyticsSummary` | Add Zod schema or explicit runtime check |

### Category 3 — CSS/Style Workarounds (LOW — 11 casts)
These cast `{ boxShadow, userSelect, pointerEvents }` web CSS props to RN ViewStyle. These are legitimate cross-platform workarounds.

Fix strategy: Extract to a shared utility:
```ts
// src/utils/webStyles.ts
export const webStyle = (style: Record<string, unknown>): object => style;
```
Replace `{ touchAction: 'none' } as unknown as ViewStyle` with `webStyle({ touchAction: 'none' })`. No cast needed. Apply to all 11 instances.

## Implementation Steps

### Step 1 — Create type guards (no file changes yet)
Create `src/types/bleGuards.ts`:
- `isDevice(obj: unknown): obj is Device` — validate id, name, rssi present
- `isPeripheral(obj: unknown): obj is BLEPeripheral` — validate MAC, serviceUUIDs

### Step 2 — Fix BLE data path casts (HIGH)
For each HIGH cast: view_file the exact line → replace cast with type guard or typed mapper → run tsc.

### Step 3 — Fix Supabase casts (MEDIUM)
Add `mapToCrewSession(raw: unknown): CrewSession` mapper to `CrewService.ts`. Apply to all Supabase query result casts.

### Step 4 — Fix web style casts (LOW)
Create `src/utils/webStyles.ts` webStyle util. Find and replace all 11 cross-platform CSS casts.

### Step 5 — Lint enforcement
Verify `@typescript-eslint/no-explicit-any` is active in `.eslintrc.js`. Add `@typescript-eslint/no-unsafe-type-assertion` rule if not present.

## Verification Plan
- `npm run verify` (TSC no-emit must show 0 new errors, 0 `as any` in diff)
- `grep "as unknown as" src/ --include="*.ts" --include="*.tsx" -r | grep -v "__tests__"` → must return 0 prod hits
- `grep "as any" src/ --include="*.ts" --include="*.tsx" -r | grep -v "__tests__"` → must return 0 prod hits

## Rollback
- Category 3 (web style casts) are purely cosmetic — if webStyle util breaks RN build, revert and use explicit type intersection instead
