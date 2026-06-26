# Implementation Plan: fix/dashboard-flatlist-rerender

## Goal
Stabilize the device-list `renderItem` callback so the registered-fleet FlatList stops re-rendering every cell on every parent render (e.g. every BLE RSSI tick).

## Source Analysis
Reyes C2 wiring audit (docs/SESSION_LOG.md, 2026-06-25).
- Source: `src/screens/Dashboard/DashboardDeviceList.tsx:76-104` — `renderItem` is wrapped in `useCallback` with `[props]` as the dependency. `props` is a fresh object reference on every render of `useDashboardDeviceList` (called inline in DashboardScreen), so `renderItem` is recreated every render, defeating memoization. The `MemoizedDeviceItem` / FlatList sees a new `renderItem` each parent render.

## Files to Create/Modify
- `src/screens/Dashboard/DashboardDeviceList.tsx` — change the `renderItem` `useCallback` dependency from `[props]` (whole object) to the specific values the callback actually reads.

## Implementation Steps
1. Read `src/screens/Dashboard/DashboardDeviceList.tsx:60-110` in full. Enumerate exactly which fields of `props` the `renderItem` body references.
2. Replace `[props]` with a granular dep array listing only those referenced fields (e.g. `[props.connectedDevices, props.onDevicePress, ...]` — use the actual fields found in step 1). Each must be a stable reference upstream OR an acknowledged churn source; do NOT add the whole object.
3. If any referenced field is itself an unstable inline value passed from DashboardScreen, note it in the final report (do not fix upstream — out of scope), but prefer the field over the object.
4. `git diff HEAD src/screens/Dashboard/DashboardDeviceList.tsx` — confirm ONLY the dep array changed.
5. Verify: `npm run verify` passes clean.

## Out of Scope
- DashboardScreen.tsx and any upstream prop-stabilization (separate concern; would belong to a perf task, not this one).
- Other hooks in src/screens/Dashboard/.
- The autoconnect listener fix (separate task).
