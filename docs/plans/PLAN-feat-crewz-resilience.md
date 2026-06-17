# Implementation Plan: Crewz Mode Resilience (Phase 1, 3, 4)

## Goal Description
Implement architectural adjustments to the Crewz domain based on product feedback to ensure highly resilient, persistent background group sessions that do not drop when the phone is pocketed.

1. Explicitly gate Crewz Mode behind Location and Data Sharing permissions.
2. **Phase 1 (Heartbeats):** Implement a gentle Keep-Alive ping for Leaders to ensure long-running sessions are not prematurely pruned by the `cleanupExpiredSessions` cron job. We will NOT implement aggressive host migration or auto-kicks.
3. **Phase 3 (Payload Compression):** Convert the heavy JSON `scene_update` broadcasts into compact binary payloads mapping directly to the `ZENGGE_PROTOCOL_BIBLE` opcodes to drastically reduce latency and cloud bandwidth.
4. **Phase 4 (Global Foreground Service):** Extract the Notifee foreground notification out of the isolated `SessionMachine` and make it a global persistent service that starts on app boot. This ensures the app acts as a true "Phone-as-Gateway", staying alive in the pocket to maintain BLE, Supabase, and Watch Sync continuously.

## Files to Create/Modify

### [MODIFY] docs/SK8Lytz_App_Master_Reference.md
- Add an explicit carve-out in the Offline-First Mandate stating that Crewz Mode is an Online-Only exception.
- Document that Crewz Mode requires Location and Data Sharing permissions.

### [NEW] tools/knowledge-base/patterns/offline-first.md
- Add a dedicated section for "Deliberate Exceptions" and list Crewz Mode as relying entirely on Cloud pub/sub (Supabase Realtime).

### [MODIFY] src/components/crew/CrewLandingScreen.tsx
- Integrate `PermissionService.checkPermission('LOCATION')` before allowing the user to create or join a session.
- Add an explicit `OfflineMode` guard. If the app is offline, the Crewz Hub will display a dedicated Empty State.

### [MODIFY] src/services/CrewService/CrewRealtime.ts
- Add a `startHeartbeat()` method for the Leader that fires a lightweight `last_active_at = now()` update to the `crew_sessions` table every 60 seconds.
- Refactor `broadcastScene` to `broadcastPayload`. Instead of broadcasting massive JSON objects, broadcast compiled byte arrays.

### [MODIFY] src/hooks/useCrewSession.ts
- Update the `onScene` callback to `onPayload`.
- When Crew members receive the `payload`, pass the byte array directly into their local `BleWriteQueue`.

### [NEW] src/services/GlobalForegroundService.ts
- Create a global singleton service to manage the Notifee Foreground Service.
- It will start automatically on app launch (once permissions are granted) and run persistently, preventing the OS from suspending the app or dropping BLE/Supabase connections.

### [MODIFY] src/services/session/NotificationService.ts
- Remove the scoped Notifee foreground service from `src/services/session/NotificationService.ts` to prevent conflicts with the new GlobalForegroundService.

## Verification Plan
- Run `npm run verify` to check type safety.
- Verify the global foreground service launches on app boot and requests necessary permissions.
- Verify background payload broadcasts are successful and compact.
