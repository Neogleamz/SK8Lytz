# Implementation Plan: C16 — Circular Dependency Resolution

## Goal
Break import cycles in appLogger chain and CrewService internal modules.

## Rules Addressed
- R-29: Circular Dependencies (4 cycles)

## Files to Create/Modify
- `src/services/appLogger/index.ts`
- `src/services/appLogger/AppLoggerService.ts`
- `src/services/AppSettingsService.ts`
- `src/services/CrewService/CrewService.ts`
- `src/services/CrewService/CrewAutoRejoin.ts`
- `src/services/CrewService/CrewRealtime.ts`

## Implementation Steps
1. Map import graph for appLogger chain. Break cycle with lazy import or interface extraction.
2. Map CrewService internal cycles. Use dependency injection or barrel export restructuring.
3. Verify: no circular deps detected by madge or manual trace.
4. Run npm run verify.

## Out of Scope
- AppLoggerCloud.ts (C5)
