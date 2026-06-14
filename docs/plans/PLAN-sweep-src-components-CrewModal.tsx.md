# Implementation Plan: sweep-src-components-CrewModal.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-CrewModal.tsx` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] CrewModal.tsx
- Line 121: Fix `R-07` violation. CrewModal instantiates a new context value reference inline on every render cycle, triggering rendering sweeps in all descendant modal subcomponents. (Suggested: Wrap the context value instantiation inside a React.useMemo hook, adding stable dependencies.)
- Line 105: Fix `R-04` violation. AppLogger.warn call is missing telemetry context parameters: payload_size, ssi. Rule R-04 mandate that payload_size and ssi must be provided for all telemetry entries. (Suggested: AppLogger.warn(message, { ...context, payload_size: 0, ssi: 0 });)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
