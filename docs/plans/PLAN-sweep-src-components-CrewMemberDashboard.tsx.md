# Implementation Plan: sweep-src-components-CrewMemberDashboard.tsx

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-components-CrewMemberDashboard.tsx` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] CrewMemberDashboard.tsx
- Line 178: Fix `R-11` violation. Database query to retrieve crew members does not extract or check returned 'error' object from Supabase, leading to silent failure when loading members. (Suggested: Extract error, check its existence, and display/throw the error details.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
