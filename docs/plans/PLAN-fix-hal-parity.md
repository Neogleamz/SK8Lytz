# Implementation Plan: Fix HAL Parity Split Brain

Provide a brief description of the problem, any background context, and what the change accomplishes.
The UI components (`UniversalTacticalSliders` and `BuilderPanel`) are bypassing the `useProtocolDispatch` HAL and injecting hardcoded `ZenggeProtocol` payloads directly into `writeToDevice`. This creates a split-brain parity violation: if a mixed protocol group (Zengge + BanlanX) is active, the UI visualizer animates perfectly, but the BanlanX device receives Zengge byte structures and falls completely out of sync.

## User Review Required

> [!IMPORTANT]
> This requires refactoring the inner dispatch closures of these components to consume `useProtocolDispatch` methods instead of direct `writeToDevice` with raw protocols.

## Proposed Changes

### UI Components

#### [MODIFY] [UniversalTacticalSliders.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalTacticalSliders.tsx)
- Replace all instances of `ZenggeProtocol.setMultiColor` inside `writeToDevice` calls with the `setMultiColor` dispatcher from `useProtocolDispatch`.
- Remove the `ZenggeProtocol` import.

#### [MODIFY] [BuilderPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/BuilderPanel.tsx)
- Replace `ZenggeProtocol.setMultiColor` inside `writeToDevice` with the `setMultiColor` dispatcher from `useProtocolDispatch`.
- Remove the `ZenggeProtocol` import.

#### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Ensure that the props passed to `UniversalTacticalSliders` and `BuilderPanel` reflect the correct dispatcher function signatures (e.g., passing `sendColor` instead of `writeToDevice` where applicable).

## Out of Scope
- Fixing `ZenggeProtocol` references inside the `admin/tools/` oracle tabs. The admin oracle is explicitly designed to send raw protocol bytes for diagnostics and is exempt from HAL parity.

## Verification Plan
### Automated Tests
- `npm run verify` to ensure no type regressions with the new prop signatures.

### Manual Verification
- Render the `DockedController` and verify that sliding the tactical sliders triggers `_dispatchToDevices` (which iterates over the group and resolves the correct adapter) instead of calling `useBLE`'s `writeToDevice` directly with raw arrays.
