# Implementation Plan: extract-and-sweep-CrewService.ts

## Goal
Extract the monolithic `src/services/CrewService.ts` (>31KB) into a modular `src/services/crewService/` directory to resolve the S4 limit violation, segregating session lifecycle, Supabase Realtime channels, and telemetry tracking.

## Files to Create/Modify
- [NEW] `src/services/crewService/index.ts`
- [NEW] `src/services/crewService/types.ts`
- [NEW] `src/services/crewService/CrewSessionManager.ts`
- [NEW] `src/services/crewService/CrewRealtime.ts`
- [NEW] `src/services/crewService/CrewAutoRejoin.ts`
- [NEW] `src/services/crewService/CrewService.ts`
- [DELETE] `src/services/CrewService.ts`

## Proposed Changes

### 1. Types Module
- **File:** `src/services/crewService/types.ts`
- **Action:** Move `CrewSession`, `CrewMember`, `CrewScenePayload`, `CrewRole`, and `SessionTelemetryData` interfaces.

### 2. Session Manager
- **File:** `src/services/crewService/CrewSessionManager.ts`
- **Action:** Extract `createSession`, `cleanupLegacySessions`, `cleanupExpiredSessions`, `joinSession`, and `endSession` DB logic.

### 3. Realtime Module
- **File:** `src/services/crewService/CrewRealtime.ts`
- **Action:** Extract the Supabase `RealtimeChannel` subscription and broadcast logic (`subscribeAsLeader`, `subscribeAsMember`, `broadcastScene`).

### 4. Auto-Rejoin Module
- **File:** `src/services/crewService/CrewAutoRejoin.ts`
- **Action:** Extract `tryAutoRejoin` and `AsyncStorage` persistence logic (`STORAGE_LAST_SESSION_ID`).

### 5. Core Service
- **File:** `src/services/crewService/CrewService.ts`
- **Action:** Combine the modules behind the existing `crewService` singleton instance.

### 6. Global Imports
- **Action:** Update all imports referencing `src/services/CrewService` to point to the new directory.

## Out of Scope
- Modifying the UI components in `src/components/crew/`.

## Verification
- **Test:** Run `npm run verify` to ensure zero compilation or structural test errors.
