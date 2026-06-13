# Goal Description

Break down `DashboardScreen.tsx` (50KB) and `DockedController.tsx` (65KB) into smaller, manageable sub-components and custom hooks. The extreme hook density (153 and 128 hooks respectively) violates our monolith defense rules and makes future UI features brittle due to merge conflicts and state entanglement.

## User Review Required

> [!WARNING]
> This is a high-risk structural refactor. The goal is 100% logic parity with no regressions. We will not be introducing new features during this split. Please confirm the extraction boundaries before I start slicing these files up.

## Proposed Changes

### DashboardScreen.tsx Extraction

We will extract the inline rendering and sprawling modal definitions from `DashboardScreen.tsx` to reduce its footprint to purely layout and top-level domain hook orchestration.

#### [NEW] src/components/dashboard/DashboardDeviceList.tsx
Extract the `renderItem` (which renders `DeviceItem` and handles `onPress`/`onLongPress`) and the `FlatList` layout. 

#### [NEW] src/components/dashboard/DashboardModalsLayer.tsx
Extract all bottom-level modals (GroupSettingsModal, DeviceSettingsModal, AdminToolsModal, AccountModal, SupportModal) into a single unified layer component that receives visibility flags and callbacks from the parent.

#### [MODIFY] src/screens/DashboardScreen.tsx
Remove the inline rendering logic and modals. Import and mount the new `DashboardDeviceList` and `DashboardModalsLayer` components.

---

### DockedController.tsx Extraction

`DockedController` handles UI layout + BLE persistence sync + permission states. We will separate the non-visual hooks from the layout.

#### [NEW] src/hooks/useDockedPermissions.ts
Extract the reactive permission gating (`checkPermission`, AppState listeners for camera/location) that determines `hiddenModes`.

#### [NEW] src/hooks/useDockedBLESync.ts
Extract the ledger replay logic (the `isActuallyPairedOrConnected` useEffects) and ghost reconcile wiring (`useOptimisticBLE` configuration) into a dedicated hook.

#### [MODIFY] src/components/DockedController.tsx
Remove the heavy inline `useEffect` and `useCallback` chains. Delegate to the new hooks.

## Verification Plan

### Automated Tests
- Run `npm run verify` to ensure no Type errors or Jest failures are introduced by the component splitting.

### Manual Verification
- We will rely on the `npm run verify` suite's strict TypeScript checks to validate that prop passing and ref forwarding remain sound.
- Since we are not changing underlying logic, a clean `tsc` output is a strong indicator of success.
