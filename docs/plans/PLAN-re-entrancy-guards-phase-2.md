# Implementation Plan: fix/re-entrancy-guards-phase-2

> **Task**: Add re-entrancy guards to R-26 instances NOT covered in phase 1.
> **Source of Truth**: `artifacts/deepdive_raw/R-26_findings.json` (Entries 2–5)

## User Review Required
> [!IMPORTANT]
> **DashboardScreen Monolith Extraction Rule (S4)**: Rule S4 explicitly forbids editing files larger than 30KB without extracting them first. `src/screens/DashboardScreen.tsx` is currently 50KB. Therefore, the `checkNewDevice` re-entrancy guard fix in `DashboardScreen.tsx` MUST be skipped in this phase. It will be addressed directly after the `refactor/god-object-split` task breaks down the file.

## Open Questions
None. The re-entrancy guard pattern is straightforward (`useRef` boolean flag for `useEffect` loops, and `useRef` boolean flag for event listeners).

## Proposed Changes

### `src/context/SessionContext.tsx`

#### [MODIFY] `src/context/SessionContext.tsx`
- **`checkAutoPause`**: Add `isCheckingAutoPause` (`useRef(false)`) outside the `useEffect` to prevent overlapping AsyncStorage reads and overlapping pause commands when `gpsSpeed` changes rapidly.
- **`syncSessionState`**: Add `isSyncingSessionState` (`useRef(false)`) outside the `useEffect` to prevent overlapping AsyncStorage executions when the `AppState` listener triggers multiple times in rapid succession.

### `src/hooks/useHealthTelemetry.ts`

#### [MODIFY] `src/hooks/useHealthTelemetry.ts`
- **`pollHealthData`**: Add `isPollingRef` (`useRef(false)`) to prevent overlapping health polling cycles if HealthKit/HealthConnect hangs or takes longer than the 15s `setInterval`.

### `src/hooks/useCrewProximityRadar.ts`

#### [MODIFY] `src/hooks/useCrewProximityRadar.ts`
- **`scan`**: Add `isScanningRef` (`useRef(false)`) to prevent overlapping API and location calls if the `crewService.isInCrew` dependency flips rapidly while a scan is still in flight.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure strict TypeSafety and Jest test suite passes.
- Specifically monitor `PASS src/hooks/__tests__/useHealthTelemetry.test.ts` and `PASS src/context/__tests__/SessionContext.test.tsx` (if they exist, otherwise global suite).

### Manual Verification
- Simulate rapid GPS speed fluctuations (via simulator) and verify only one `checkAutoPause` block runs concurrently.
- Verify that `pollHealthData` does not stack up promises if the HealthKit bridge slows down.
