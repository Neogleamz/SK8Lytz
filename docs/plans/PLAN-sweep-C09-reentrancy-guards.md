# Implementation Plan: C9 — Re-Entrancy Guard Sweep

## Goal
Add isProcessingRef.current boolean guards to all async UI handlers missing them.

## Rules Addressed
- R-26: Re-Entrancy Races (6 findings)

## Files to Create/Modify
- `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`
- `src/hooks/useDashboardGroups.ts`
- `src/components/admin/AdminPicksScheduler.tsx`
- `src/hooks/useAdminTelemetry.ts`

## Implementation Steps
1. View each file. Find async handlers without re-entrancy guards.
2. Add: const isProcessingRef = useRef(false); at top.
3. Add guard: if (isProcessingRef.current) return; isProcessingRef.current = true; try { ... } finally { isProcessingRef.current = false; }
4. Verify: git diff shows only guard additions.
5. Run npm run verify.

## Out of Scope
- DashboardScreen.tsx (C2)
