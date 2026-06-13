# Implementation Plan: SK8Lytz App Watch Sync

## Goal
Integrate the new `sk8lytz-watch-bridge` module into the existing React Native app architecture, specifically hooking it into the `SessionContext` and `DockedController` to synchronize state with the smartwatch.

## Architectural Constraints
- **State Synchronization:** The watch should only be updated when the session state changes (Start/Stop) or when significant metric thresholds are passed (e.g. Speed changes by > 0.5mph) to prevent battery drain on the watch.
- **Two-Way Binding:** If the user hits "Stop Session" on the watch, it must trigger the exact same `stopSession` flow in `SessionContext` as if they tapped the phone UI.

## Proposed Changes
### [MODIFY] `src/context/SessionContext.tsx`
- Import `WatchBridge` from `sk8lytz-watch-bridge`.
- In `startSession`, call `WatchBridge.sendStateToWatch({ status: 'ACTIVE', startTime: ... })`.
- In `stopSession`, call `WatchBridge.sendStateToWatch({ status: 'STOPPED' })`.
- Add an effect to listen for `onWatchCommandReceived` and trigger `startSession` or `stopSession` accordingly.

### [MODIFY] `src/services/SpeedTrackingService.ts`
- Inside the location update callback, throttle speed updates sent to the watch via `WatchBridge.sendMetricUpdate({ speed: newSpeed })` (e.g. only once every 3 seconds).

## Open Questions / User Review Required
> [!IMPORTANT]
> Sending Bluetooth BLE commands directly from the watch is not in scope for this phase. The watch only controls the *Session Tracking* logic (which in turn can trigger BLE commands on the phone if auto-sync is on). Does this align with the desired UX?

## Verification Plan
- Unit tests for `SessionContext` verifying the bridge is called when sessions start/stop.
- UI Smoke test with the iOS Simulator paired to a watchOS Simulator to ensure button taps on the watch propagate to the phone screen.
