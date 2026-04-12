# [PLAN] refactor/state-machine-standard (Deterministic UI)

### Design Decisions & Rationale
To eliminate race conditions and "Illegal States" (e.g., syncing while disconnecting), we are transitioning from boolean-based flags to **Explicit Finite State Machines (FSM)**. This ensures that a component can only exist in one valid architectural state at a time.

## Proposed Changes

### [Component Name] State Architecture

#### [NEW] [types/AppStates.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/types/AppStates.ts)
- Define canonical Enums for core workflows:
    - `enum DeviceState { DISCONNECTED, CONNECTING, PROBING, READY, DISCONNECTING, ERROR }`
    - `enum SessionState { IDLE, RECORDING, PAUSED, SAVING }`

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DockedController.tsx)
- Refactor local state to use a single `StatusState` instead of multiple `isX` booleans.
- Implement a `transition(newState)` helper that validates the move (e.g., cannot move from `DISCONNECTED` directly to `READY`).

## Verification Plan
1. **Illegal Transition Test**: Attempt to force the state from `PROBING` to `IDLE` without a disconnect; verify the transition fails or is handled gracefully.
2. **UI Parity**: Verify that the "Sync" and "Power" controls are physically hidden/disabled when the state is not `READY`.
3. **Complexity Reduction Audit**: Compare the number of hooks and conditional renders before and after the refactor; target a 30% reduction in "logical branching."
