# [FIX] TypeScript Debt Audit

## Goal
Resolve pre-existing TS errors across the codebase (dead state vars, type drift, missing imports).

## Proposed Changes
- Global `npx tsc` sweep.
- Remove unused variables/imports (Boy Scout Rule).
- Fix `any` types in legacy `BLEService`.

## Verification Plan
- Zero compilation errors.
