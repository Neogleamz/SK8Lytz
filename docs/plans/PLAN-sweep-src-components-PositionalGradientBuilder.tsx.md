# Implementation Plan: sweep-src-components-PositionalGradientBuilder.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-PositionalGradientBuilder.tsx` domain cluster.

## Batch & Wave
- **Wave:** 2
- **Prerequisite:** Wave 1 fully merged

## Proposed Changes
### [MODIFY] PositionalGradientBuilder.tsx
- Line 123: Fix `R-25` violation. Gradient pin selector dots and custom behavior toggle controls lack screen reader accessibility support. (Suggested: Assign accessibilityRole="button" and custom accessibilityLabel indicating color hex and position relative to the gradient layout.)
- Line 56: Fix `R-19` violation. Hardcoded chipset-specific reference to ZenggeProtocol.setMultiColor in a UI component, bypassing the Hardware Abstraction Layer (HAL). (Suggested: Refactor to use a generic protocol adapter method or pass the constructed payload down via the parent component using a polymorphic IControllerProtocol adapter interface.)
- Line 57: Fix `R-06` violation. Generic console.warn log of BLE write failure instead of invoking the system telemetry AppLogger. (Suggested: Import and use AppLogger.error to log BLE write failures with appropriate metadata (e.g., payload size, speed).)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
