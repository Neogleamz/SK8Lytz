# Implementation Plan: AccountModal-Extraction-Spike

## Goal Description

Extract presentation and style logic from `AccountModal.tsx` to resolve the S4/R-23 Monolith File limit violation (>30KB), and fix the remaining PII telemetry violation.

## Proposed Changes

### src/components/account/

#### [NEW] AccountModalSkeleton.tsx
- Extract the `AccountModalSkeleton` component (currently lines 70-114 of `AccountModal.tsx`).
- It is a purely presentational component with no internal state, relying only on `Animated` and `Spacing`.

#### [NEW] AccountModalStyles.ts
- Extract the massive `createStyles` block (currently lines 548-670 of `AccountModal.tsx`).
- Export `createStyles` so `AccountModal.tsx` can import it and pass the `Colors` palette in.

### src/components/

#### [MODIFY] AccountModal.tsx
- Remove `AccountModalSkeleton` and import it from `./account/AccountModalSkeleton`.
- Remove `createStyles` and import it from `./account/AccountModalStyles`.
- Line 194: Fix `R-09` PII violation. `AppLogger.log('CREW_PERMANENT_DELETED', { crewId: crew.id, crewName: crew.name });` -> Wrap `crew.name` in `scrubPII`.

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
