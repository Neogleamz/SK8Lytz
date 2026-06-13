# Offline-First Scene Sync Queue

We are upgrading `ScenesService.ts` to be 100% Offline-First, and introducing a background worker to ensure all pending local writes (scenes and telemetry) automatically sync to Supabase when network connectivity is restored.

## User Review Required

- **Silent Queueing**: When you click "Publish to Community" offline, the app will no longer throw an error alert. Instead, it will say "Queued for Publishing" and invisibly push it to the cloud when you reconnect. Is this UX acceptable?
- **Telemetry Flush**: I discovered that telemetry logs are currently only being uploaded manually via the Admin Tools panel. I will wire them into the new background sync worker so telemetry flows automatically.

## Proposed Changes

### Layer 1: Core Service

#### [MODIFY] [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts)
- Add a new `SceneSyncJob` interface and an `AsyncStorage` queue (`@Sk8lytz_Scene_Sync_Queue`).
- **`saveScene()`**: Save to local cache immediately. Instead of failing on a Supabase upsert, push an `upsert_user_scene` job to the queue.
- **`publishScene()`**: Push a `publish_community_scene` job to the queue instead of direct failing. Return `true` immediately to the UI.
- **`deleteSavedScene()`**: Delete locally immediately, push a `delete_user_scene` job.
- **`flushSyncQueue()`**: Read the queue. Attempt to process all jobs against Supabase. On success, remove from the queue. On failure (offline), leave them in the queue.

### Layer 2: Background Worker

#### [NEW] [useOfflineSyncWorker.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/cloud/useOfflineSyncWorker.ts)
- Create a new background loop hook.
- Run every 60 seconds (using `setInterval`).
- Attempt to call `ScenesService.flushSyncQueue()`.
- Attempt to call `AppLogger.uploadLogsToSupabase()`.
- Catch all errors silently to prevent UI disruption.

#### [MODIFY] [App.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)
- Inject `useOfflineSyncWorker()` at the root level so the sync loop runs continuously as long as the app is open in the foreground.

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` to ensure no typing regressions in `ScenesService.ts`.

### Manual Verification
1. Build the app locally.
2. Turn off WiFi/Cellular on the phone.
3. Open Builder, create a new scene, tap "Save Private".
4. Verify the scene appears in the local list immediately.
5. Turn WiFi back on.
6. Wait 60 seconds.
7. Verify the scene appears in the Supabase `user_saved_presets` table automatically.
