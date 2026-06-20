# Implementation Plan: C10 — AsyncStorage Key Registry Sync

## Goal
Centralize all AsyncStorage keys to storageKeys.ts, remove undocumented inline keys.

## Rules Addressed
- R-24: AsyncStorage Key Collisions (7 findings)

## Files to Create/Modify
- `src/constants/storageKeys.ts` — Add missing key constants
- `src/hooks/useProductCatalog.ts` — Import keys from storageKeys
- `src/components/admin/tools/tabs/Sk8LytzProgrammer.tsx` — Import keys from storageKeys

## Implementation Steps
1. Grep for AsyncStorage.getItem/setItem with inline string keys.
2. Add missing keys to storageKeys.ts.
3. Replace inline strings with imported constants.
4. Verify: git diff shows only key centralization.
5. Run npm run verify.

## Out of Scope
- DashboardScreen.tsx (C2)
