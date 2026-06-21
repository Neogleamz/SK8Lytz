# Implementation Plan: C17 — Boolean Trap to FSM Migration

## Goal
Replace scattered boolean states with string union types or FSM patterns.

## Rules Addressed
- R-18: Boolean Traps (6 findings)

## Files to Create/Modify
- `src/components/auth/AuthFormSignUp.tsx`
- `src/components/CrewModal.tsx`
- `src/hooks/useAccountOverview.ts`

## Implementation Steps
1. View each file. Identify boolean state clusters (isLoading + isError + isSuccess).
2. Replace with: type FormState = 'idle' | 'loading' | 'error' | 'success'.
3. Update all consumers to use FSM state.
4. Verify: git diff shows boolean removal and FSM addition.
5. Run npm run verify.

## Out of Scope
- DashboardScreen.tsx (C2), useDashboardGroups.ts (C9)

// SKIPPED: src/components/auth/AuthFormSignUp.tsx - Already migrated to string union FSM in prior sweep
// SKIPPED: src/components/CrewModal.tsx - Already migrated to string union FSM in prior sweep
// SKIPPED: src/hooks/useAccountOverview.ts - Already migrated to string union FSM in prior sweep
