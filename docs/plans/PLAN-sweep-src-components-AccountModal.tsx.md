# Implementation Plan: sweep-src-components-AccountModal.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-AccountModal.tsx` domain cluster.

## Batch & Wave
- **Wave:** 7
- **Prerequisite:** Wave 6 fully merged

## Proposed Changes
### [MODIFY] AccountModal.tsx
- Line 1: Fix `R-23` violation. File size is 31397 bytes, exceeding the 30KB monolith limit defined in Rule S4/R-23. The component contains user profile rendering, edit profile form, and logout functionality which can be easily separated. (Suggested: Extract sub-sections like the profile form, session stats display, and log display sub-panes into separate component files under src/components/account/.)
- Line 194: Fix `R-09` violation. PII key 'crewName' passed with unscrubbed value 'crew.name' in AppLogger call. (Suggested: Wrap resource name in scrubPII(crew.name) or log the unique ID instead of name.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
