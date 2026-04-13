# [PLAN] refactor/state-machine-standard (Deterministic UI)

### Design Decisions & Rationale

To eliminate race conditions and "Illegal States" (e.g., syncing while disconnecting), we are transitioning from boolean-based flags to **Explicit Finite State Machines (FSM)**. This ensures that a component can only exist in one valid architectural state at a time. The BLE connection lifecycle and dashboard routing are being heavily refactored. The FSMs will be unified in `src/types/dashboard.types.ts` as the canonical source.

## Proposed Changes

### Domain Types

#### [MODIFY] [dashboard.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/dashboard.types.ts)

- [NEW] Add `BleConnectionState = 'IDLE' | 'SCANNING' | 'CONNECTING' | 'PROBING' | 'READY' | 'DISCONNECTING' | 'ERROR'`
- [NEW] Add `DashboardViewState = 'LOADING_REGS' | 'SETUP_WIZARD' | 'DASHBOARD' | 'CREW_HUB' | 'OFFLINE'`

### BLE Hardware Services

#### [MODIFY] [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)

- [DELETE] `isScanning`, `isScanProbing`, `isConnecting` (scattered booleans)
- [NEW] Implement `const [bleState, setBleState] = useState<BleConnectionState>('IDLE')`
- [NEW] Expose `bleState` to Dashboard.

### UI Layer

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)

- [DELETE] Scattered booleans `isCheckingRegistrations`, `isSetupWizardVisible`, `isOfflineMode`, `isDisconnecting`. (Note: `isActuallyConnected` remains purely as a derived variable, not independent state).
- [NEW] Implement `const [viewState, setViewState] = useState<DashboardViewState>('LOADING_REGS')`
- [MODIFY] Update the final `return` block to use `switch (viewState)` for safe, deterministic screen routing.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)

- [MODIFY] Adjust incoming props (e.g., `isDisconnecting`) to consume the new `bleState` FSM from parent. Disable controls during `'DISCONNECTING'` or non-`'READY'` states safely.

## Verification Plan

1. **Illegal Transition Test**: Attempt to force the state from `PROBING` to `IDLE` without a disconnect; verify the transition fails or is handled gracefully.
2. **UI Parity**: Verify that the "Sync" and "Power" controls are physically hidden/disabled when the state is not `READY`.
3. **Complexity Reduction Audit**: Compare the number of hooks and conditional renders before and after the refactor; target a 30% reduction in "logical branching."
