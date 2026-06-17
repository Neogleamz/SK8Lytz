# Implementation Plan: fix/ble-core-type-safety

> **Slug:** `fix/ble-core-type-safety`  
> **Risk:** H-RISK | **Size:** Meal | **Rule:** R-08 + S3  
> **Source Analysis:** [system_audit_report.md — CLUSTER 4](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md)

## Goal

Eliminate all `any` type casts from BLE core production services (BleMachine, ConnectService, HeartbeatService, RecoveryService) and fix the Device/DisplayDevice type hierarchy in DashboardScreen to remove 9 `as unknown as` casts. Zero new `any` casts introduced.

## Source Analysis Link

| File | Lines | Issue |
|---|---|---|
| [BleMachine.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L18) | 18 | `handleOrganicDisconnect: (error: any, ...)` |
| [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L47) | 47, 50 | `({ event }: any)` and `(p: any)` |
| [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L27) | 27, 240, 249 | `(error: any)` in three spots |
| [HeartbeatService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts#L20) | 20 | `fromCallback<any, ...>` |
| [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L33) | 33, 36, 42, 46 | `Map<string, any>`, `(error: any, ...)` twice, `fromCallback<any, ...>` |
| [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L251) | 251,252,527,842-844,981,1111,1157,1273 | 9 `as unknown as` casts from Device→DisplayDevice mismatch |

## Files to Create/Modify

1. `src/services/ble/BleMachine.types.ts` — Fix `handleOrganicDisconnect` param type
2. `src/services/ble/BleMachine.ts` — Type `setTargetMacs` event param and map callback
3. `src/services/ble/ConnectService.ts` — Type disconnect callback and monitor params
4. `src/services/ble/HeartbeatService.ts` — Type `fromCallback` generic
5. `src/services/ble/RecoveryService.ts` — Type Map generic and function signatures
6. `src/screens/DashboardScreen.tsx` — Fix Device→DisplayDevice cast sites
7. `src/types/dashboard.types.ts` — Widen `DisplayDevice` to extend from `Device` (structural)

## Steps

### Step 1 — Ground the `handleOrganicDisconnect` signature

The `onDeviceDisconnected` callback receives `(error: BleError | null, device: Device | null)`.  
Source: `react-native-ble-plx/src/index.d.ts:1231`

However, RecoveryService (lines 124–132) constructs a serialized error object `{ message, stack, name, status }` and passes that instead. The correct type for the error param across all callers is `BleError | { message: string; stack?: string; name: string; status: unknown } | null`.

1. **BleMachine.types.ts:18** — Change `error: any` → `error: BleError | null`.  
   Source: [BleMachine.types.ts:18](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L18)
2. **ConnectService.ts:27** — Same change in `ConnectServiceInput` interface.  
   Source: [ConnectService.ts:27](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L27)
3. **RecoveryService.ts:36** — Change `error: any` → `error: BleError | null` in `RecoveryInput`.  
   Source: [RecoveryService.ts:36](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L36)

> **Verify:** `grep -rn "error: any" src/services/ble/` returns zero hits in these four files.

### Step 2 — Type the `setTargetMacs` assign action

- **BleMachine.ts:47** — Replace `({ event }: any)` with explicit union destructure typed against `BleMachineEvent`.  
  Source: [BleMachine.ts:47](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L47)
- **BleMachine.ts:50** — Replace `(p: any)` with `(p: Device)` — the `peripherals` field is `Device[]` per `BleMachineEvent` RESTORE_PERIPHERALS event (line 46 of types).  
  Source: [BleMachine.ts:50](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L50), [BleMachine.types.ts:46](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L46)

> **Verify:** `git diff` shows only the targeted lines changed. TSC passes.

### Step 3 — Type ConnectService callbacks

- **ConnectService.ts:240** — `(error: any)` in `onDeviceDisconnected` callback → `(error: BleError | null, device: Device | null)` per BLE PLX signature.  
  Source: [ConnectService.ts:240](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L240), `react-native-ble-plx/src/index.d.ts:1231`
- **ConnectService.ts:249** — `(error: any, characteristic: any)` in `monitorCharacteristicForService` → `(error: BleError | null, characteristic: Characteristic | null)`. Both types already imported at line 6.  
  Source: [ConnectService.ts:249](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L249)

> **Verify:** `git diff HEAD ConnectService.ts` — only lines 240, 249 changed.

### Step 4 — Type HeartbeatService `fromCallback` generic

- **HeartbeatService.ts:20** — Replace `fromCallback<any, HeartbeatServiceInput>` with `fromCallback<BleMachineEvent, HeartbeatServiceInput>`. The `sendBack` emits `HEARTBEAT_FAIL` (line 78), which is a `BleMachineEvent`.  
  Source: [HeartbeatService.ts:20](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts#L20), [HeartbeatService.ts:78](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts#L78)

> **Verify:** TSC passes; `sendBack({ type: 'HEARTBEAT_FAIL', ... })` type-checks against `BleMachineEvent`.

### Step 5 — Type RecoveryService generics and signatures

- **RecoveryService.ts:33** — `Map<string, any>` → `Map<string, IControllerProtocol>`. Add import for `IControllerProtocol`.  
  Source: [RecoveryService.ts:33](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L33)
- **RecoveryService.ts:42** — `handleNotification: (error: any, characteristic: any, ...)` → `(error: BleError | null, characteristic: Characteristic | null, ...)`. Add `BleError, Characteristic` to the import.  
  Source: [RecoveryService.ts:42](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L42)
- **RecoveryService.ts:46** — `fromCallback<any, RecoveryInput>` → `fromCallback<BleMachineEvent, RecoveryInput>`. The `sendBack` emits `RECOVERY_COMPLETE | RECOVERY_FAIL | RECOVERY_PERMANENTLY_FAILED`, all `BleMachineEvent` variants.  
  Source: [RecoveryService.ts:46](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L46)

> **Verify:** `grep -rn ": any" src/services/ble/RecoveryService.ts` returns zero hits.

### Step 6 — Fix Device/DisplayDevice type hierarchy in DashboardScreen

**Root cause:** `DashboardScreen` holds state as `Device[]` (from BLE PLX) but downstream hooks expect `DisplayDevice[]`. Since `DisplayDevice` structurally overlaps `Device` (both have `id`, `name`, `rssi`, `mtu` + index signature), the fix is to change the DashboardScreen's state type to `Device[]` consistently and update the downstream hook interfaces to accept `Device[]` where they only read BLE identity fields.

**Approach — make DashboardScreen pass `Device[]` directly, update hook interfaces to accept `Device[]`:**

1. **`src/types/dashboard.types.ts`** — Add `import type { Device } from 'react-native-ble-plx'` and change `DisplayDevice` to `export interface DisplayDevice extends Device { ... }` (keeping only the additional config fields). This makes every `Device` structurally assignable to contexts that spread it with config data.  
   Source: [dashboard.types.ts:263](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/dashboard.types.ts#L263)

2. **DashboardScreen.tsx** — Remove all 9 `as unknown as` casts at lines 251, 252, 527, 842–844, 981, 1111, 1157. With `DisplayDevice extends Device`, these become assignable without casts.  
   Source: [DashboardScreen.tsx:251](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L251)

3. **DashboardScreen.tsx:1273** — `ledgerState?: any` in `MemoizedDeviceItemProps` → `ledgerState?: DevicePatternState | undefined`.  
   Source: [DashboardScreen.tsx:1273](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L1273)

> **Verify:** `npm run verify` passes. Zero `as unknown as` casts remain in DashboardScreen except any pre-existing non-Device ones. Zero `any` in the 5 BLE service files.

## Verification Plan

1. `npm run verify` — TSC + Jest + AST + TypeSafety all pass
2. `grep -rn ": any\b" src/services/ble/BleMachine.types.ts src/services/ble/BleMachine.ts src/services/ble/ConnectService.ts src/services/ble/HeartbeatService.ts src/services/ble/RecoveryService.ts` → **0 hits**
3. `grep -rn "as unknown as" src/screens/DashboardScreen.tsx` → **0 hits** for Device↔DisplayDevice casts (line 1157 DeviceSettings cast may remain if structurally necessary — document reason)
4. `git diff HEAD --stat` — only the 7 listed files modified

## Out of Scope

- `__tests__/` files — `any` casts in mocks are acceptable per synthesis rules
- UI logic changes — no behavior changes in any component
- Protocol changes — no BLE payload or opcode modifications
- `useDashboardController.tsx` — already typed with `DisplayDevice[]`; will benefit from extends fix without changes
- `useHardwareNotifications.ts` line 527 cast — separate concern (BLEDeviceMinimal), not part of this type hierarchy fix
- Monolith extraction of DashboardScreen — S4 out of scope
