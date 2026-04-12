# Plan: `feat/pull-to-refresh-scanner`

### Design Decisions & Rationale
The dashboard scanner slab currently has no way to re-trigger BLE discovery without navigating away. A standard React Native `RefreshControl` mounted on the outer `ScrollView` of the dashboard is the lowest-friction approach — it uses native pull gesture semantics and feeds directly into `scanForPeripherals()` from `useBLE`.

---

## Proposed Changes

### Target File: `src/screens/DashboardScreen.tsx`

#### [MODIFY] Scanner section ScrollView
- Import `RefreshControl` from `react-native`.
- Add `refreshing={isScanning}` and `onRefresh={() => { clearDeviceState(); scanForPeripherals(); }}` props to the outermost `ScrollView` that wraps the device fleet + scanner slab.

#### [MODIFY] Refresh side effect
- On pull: clear the `allDevices` state and `pendingRegistrations` from `useBLE` before starting the fresh scan so stale cards don't linger.
- Keep already-connected devices in `connectedDevices` — only the *discovery* list is cleared.

---

## Open Questions
- **Q:** Should a pull-to-refresh also trigger a Supabase `registered_devices` re-fetch from the cloud, or just a local BLE scan? (Recommendation: both — scan AND refetch cloud list)

## Verification Plan
1. Open the dashboard with no devices connected.
2. Pull down from the top of the hardware fleet slab.
3. Verify the scanner animation activates and new devices appear.
