# Implementation Plan: sweep-src-components-ConnectionStrengthBadge.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-ConnectionStrengthBadge.tsx` domain cluster.

## Batch & Wave
- **Wave:** 2
- **Prerequisite:** Wave 1 fully merged

## Proposed Changes
### [MODIFY] ConnectionStrengthBadge.tsx
- Line 47: Fix `R-25` violation. Signal strength status graphic contains no accessibility label or role description, making it unreadable to screen readers. (Suggested: Add 'accessible={true}', 'accessibilityRole="image"', and 'accessibilityLabel={`Signal strength: ${label}`}' using the calculated label from the RSSI tier.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
