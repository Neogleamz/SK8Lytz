# Implementation Plan

## Goal
Execute a comprehensive Burn-Down Batch to eradicate the 14 injected `any` casts, finalize the split-brain XState migration, and enforce the `AuthContext` across 16 bypassed hooks and services.

## Source of Truth
- `src/context/AuthContext.tsx:9-10` — Strict directive prohibiting services from calling `supabase.auth` and mandating hooks to use `useAuth()`.
- `.agents/rules/CONSTITUTION.md` — Rule 1: No `any` Cast Law.

## Proposed Changes

### [Phase 1: Eradicate Constitutional Violations (14 `any` Casts)]
#### [MODIFY] `src/context/AuthContext.tsx`
Remove `(e: any)` casts in exception handlers; replace with `(e: unknown)` and proper type guards.
#### [MODIFY] `src/services/CrewService.ts` and `src/services/AuthProfileService.ts`
Remove `catch (e: any)` casts; enforce strict error checking.
#### [MODIFY] `src/services/AuthProfileService.ts`
Type the `notif_preferences` field in `updateProfile` strictly as `Record<string, boolean>` instead of `any`.
#### [MODIFY] `src/hooks/useBLE.ts`, `src/hooks/useDashboardAutoConnect.ts`, `src/hooks/ble/useBLEScanner.ts`
Eradicate the `bleGateRef: React.MutableRefObject<any> // MIGRATION-SHIM`. Delete the shim entirely to force true XState compliance.

---

### [Phase 2: Finalize Split-Brain XState FSM Migration]
#### [DELETE] `src/services/ble/BleStateMachine.ts`
Purge the legacy state machine file from the codebase completely.
#### [MODIFY] `src/types/ble.types.ts` and `src/services/BleLifecycleManager.ts`
Redirect types and lifecycle imports to `src/services/ble/BleMachine.ts`.
#### [MODIFY] `src/hooks/useBLE.ts`
- Remove the native `connectedDevices` React state (`useState`).
- Refactor `useBLE` to expose `bleSnapshot.context.connectedDevices` directly.
- Replace `setConnectedDevices` prop drilling with direct `bleMachineActor.send({ type: 'UPDATE_CONNECTED_DEVICES' })` dispatch.
#### [MODIFY] Subsidiary BLE Hooks (`useBLEScanner.ts`, `useBLEAutoRecovery.ts`)
Refactor to receive `bleMachineActor` or `bleSnapshot` instead of relying on faked `bleGateRef` and parallel `useState` string refs.

---

### [Phase 3: AuthContext Global Enforcement]
#### [MODIFY] 8 Rogue React Hooks
Files: `useFavorites.ts`, `useCrewSession.ts`, `useDashboardCrew.ts`, `useDashboardAutoConnect.ts`, `useGradients.ts`, `useProductManager.ts`, `useScenes.ts`, `SkaterStatsPanel.tsx`.
Action: Delete all `supabase.auth.getUser()` and `supabase.auth.getSession()` network calls. Replace with `const { user, session } = useAuth()`.
#### [MODIFY] 8 Rogue Services
Files: `SpeedTrackingService.ts`, `CrewProfileService.ts`, `CrewService.ts`, `DeviceRepository.ts`, `GroupRepository.ts`, `LocationService.ts`, `PushTokenService.ts`, `ScenesService.ts`.
Action: Delete `supabase.auth.getUser()` calls. Modify the function signatures to require `userId: string` or `user: User` as arguments, forcing the React Hooks to pass the context down.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure zero TypeScript errors without using `as any` or `@ts-ignore`.
- Run Jest test suite to ensure the refactored BLE and Auth hooks haven't broken the test coverage.

### Manual Verification
- **XState Check:** Verify that scanning, connecting, and dropping a skate properly transitions the FSM state without relying on local `useState` fallbacks.
- **Auth Check:** Verify that no rogue `supabase.auth.getUser()` calls remain outside `AuthContext.tsx` by running `grep -r "supabase.auth.getUser" src/`.
