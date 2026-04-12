# [PLAN] perf/optimistic-ble-updates (The Ghost Standard)

### Design Decisions & Rationale
We are implementing **Optimistic UI Updates** to mask the physical latency of Bluetooth LE. When a user interacts with a control (e.g., Power Toggle), the UI will respond instantly, assuming success. A "Ghost" background worker will handle the actual command transmission and perform "State Reconciliation" if the hardware fails to acknowledge.

## Proposed Changes

### [Component Name] BLE Command Ghost

#### [MODIFY] [ZenggeController.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ZenggeController.ts)
- Implement `optimisticWrite`: A wrapper that immediately updates local state before calling the BLE queue.
- **Rollback Mechanism**: If the Promise-based BLE write fails after retries, trigger a `STATE_RECONCILE` event.

### [Component Name] UI Bridge

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DockedController.tsx)
- Connect to the `STATE_RECONCILE` event to snap the UI back to the physical hardware state if a command is dropped.
- Implement subtle **Intermission Haptics**: A light buzz on tap (Optimistic) followed by a "Success" micro-haptic (Confirmed) OR an "Error" haptic (Reconciled).

## Verification Plan
1. **Latency Masking**: Use the app while far from hardware; verify the UI toggle moves instantly even if the BLE command takes 500ms to time out.
2. **Reconciliation Test**: Toggle power while Bluetooth is disabled. Verify the UI snaps back to "Off" after the failed attempt.
3. **Loop Integrity**: Ensure rapid-fire commands (dragging a slider) don't trigger multiple cascading rollbacks.
