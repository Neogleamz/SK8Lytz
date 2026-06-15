# Implementation Plan: extract-and-sweep-DeviceRepository.ts

## Goal
Extract the monolithic `src/services/DeviceRepository.ts` (>39KB) into a modular `src/services/deviceRepository/` directory to resolve the S4 limit violation, segregating local storage, cloud sync, and state management.

## Files to Create/Modify
- [NEW] `src/services/deviceRepository/index.ts`
- [NEW] `src/services/deviceRepository/types.ts`
- [NEW] `src/services/deviceRepository/DeviceStorage.ts`
- [NEW] `src/services/deviceRepository/DeviceCloudSync.ts`
- [NEW] `src/services/deviceRepository/DeviceStateManagement.ts`
- [NEW] `src/services/deviceRepository/DeviceRepositoryService.ts`
- [DELETE] `src/services/DeviceRepository.ts`

## Proposed Changes

### 1. Types Module
- **File:** `src/services/deviceRepository/types.ts`
- **Action:** Move `DeviceRepositorySnapshot` and local Supabase insert type aliases (`DeviceInsertRow`, `GroupInsert`).

### 2. Storage Module
- **File:** `src/services/deviceRepository/DeviceStorage.ts`
- **Action:** Handle direct AsyncStorage reading/writing for `DEVICES_KEY`, `CONFIGS_KEY`, `TOMBSTONE_KEY`, and `PENDING_KEY`.

### 3. State Management Module
- **File:** `src/services/deviceRepository/DeviceStateManagement.ts`
- **Action:** Isolate the in-memory state arrays (`devices`, `configs`, `tombstones`), the `listeners` Set, and the `useSyncExternalStore` subscription logic.

### 4. Cloud Sync Module
- **File:** `src/services/deviceRepository/DeviceCloudSync.ts`
- **Action:** Extract the complex `syncFromCloud` merge logic, product ID confirmation, and claim-check logic.

### 5. Core Repository
- **File:** `src/services/deviceRepository/DeviceRepositoryService.ts`
- **Action:** Act as the facade coordinating storage, state, and cloud sync. Maintain the exact singleton instance interface.

### 6. Global Imports
- **Action:** Update all imports referencing `src/services/DeviceRepository` to point to the new directory.

## Out of Scope
- Refactoring `GroupRepository` internals.
- Modifying React hook signatures that consume the repository.

## Verification
- **Test:** Run `npm run verify` to confirm TSC handles the import path changes.
- **Test:** Verify the `useRegistration` hook still successfully returns the devices list.
