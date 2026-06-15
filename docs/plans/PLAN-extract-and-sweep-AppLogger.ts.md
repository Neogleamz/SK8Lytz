# Implementation Plan: extract-and-sweep-AppLogger.ts

## Goal
Extract the monolithic `src/services/AppLogger.ts` (>34KB) into a modular `src/services/appLogger/` directory to resolve the S4 limit violation, while maintaining identical external API and addressing all deep-dive audit telemetry violations.

## Files to Create/Modify
- [NEW] `src/services/appLogger/index.ts`
- [NEW] `src/services/appLogger/types.ts`
- [NEW] `src/services/appLogger/AppLoggerStorage.ts`
- [NEW] `src/services/appLogger/AppLoggerCloud.ts`
- [NEW] `src/services/appLogger/AppLoggerService.ts`
- [DELETE] `src/services/AppLogger.ts`

## Proposed Changes

### 1. Types Module
- **File:** `src/services/appLogger/types.ts`
- **Action:** Move `EventType` and `LogEntry` types into this module. Ensure no runtime dependencies are dragged along.

### 2. Storage Module
- **File:** `src/services/appLogger/AppLoggerStorage.ts`
- **Action:** Extract AsyncStorage interactions (buffer rotation, batch-write accumulators, legacy key migrations) into a focused module. 

### 3. Cloud Module
- **File:** `src/services/appLogger/AppLoggerCloud.ts`
- **Action:** Extract Supabase interactions (Fast-Lane error ingestion, crash telemetry, `telemetry_snapshots` pushing).
- **Audit Fix:** Refactor `userId` handling to avoid direct `supabase.auth.getUser()` calls in the service tier, fixing the R-15 violation.

### 4. Core Service
- **File:** `src/services/appLogger/AppLoggerService.ts`
- **Action:** Combine the storage and cloud modules behind the existing `AppLogger` public interface (`log`, `debug`, `info`, `warn`, `error`). Maintain the singleton pattern and exact public method signatures.

### 5. Entry Point & Cleanup
- **File:** `src/services/appLogger/index.ts`
- **Action:** Export the singleton instance as `AppLogger`, along with the types.
- **File:** `src/services/AppLogger.ts`
- **Action:** Delete the original monolith file.

## Out of Scope
- Modifying `DeviceRepository` or `CrewService` imports (this happens in Wave 7). Wait, deleting `AppLogger.ts` will break Wave 7 if we don't alias it or fix the imports globally.
  - **Correction for this task:** We must ALSO update all imports across the codebase that reference `src/services/AppLogger.ts` to `src/services/appLogger/index.ts` or simply let the bundler resolve `src/services/appLogger`. Wait, the AST tool showed it's heavily imported.
  - *Addendum to Files to Modify:* Any file importing `src/services/AppLogger` will be updated to `src/services/appLogger`. The developer agent MUST run a global `grep_search` and replace all import paths to point to the new directory.

## Verification
- **Test:** Run `npm run verify` to confirm TSC handles the import path changes perfectly.
- **Test:** Verify `AppLogger` singleton can still be called in `DashboardScreen` without runtime errors.
