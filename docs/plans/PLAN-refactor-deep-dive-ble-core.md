# Implementation Plan: refactor/deep-dive-ble-core

## Goal
Fix stale closures in `useBLEAutoRecovery.ts`, missing offline queues in `useBLEScanner.ts`, and `any` casts in `useBLESweeper.ts`.

## Source of Truth
`src/hooks/ble/useBLEAutoRecovery.ts`

## Proposed Changes

### BLE_CORE
- **[MODIFY]** `src/hooks/ble/useBLEAutoRecovery.ts`: Fix line 218 by adding `useCallback` to interval handlers to prevent stale closures. Ensure dependencies are accurate or use `useRef` to track latest state.
- **[MODIFY]** `src/hooks/ble/useBLEScanner.ts`: Implement offline persistence for discovered devices using `AsyncStorage`. Wrap in `try/catch`.
- **[MODIFY]** `src/hooks/ble/useBLESweeper.ts`: Remove `any` casts in Interrogator hardware queries. Apply proper characteristic response types.

## Verification Plan
1. **Automated Tests**: Run `npm run verify` to ensure TypeScript compilation succeeds.
2. **Manual Verification**: Run BLE tests to ensure auto-reconnect loops correctly after a simulated disconnect and telemetry is preserved.
