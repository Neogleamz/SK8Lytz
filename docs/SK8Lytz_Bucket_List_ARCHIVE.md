### âš¡ [BATCH:deep-dive-remediation] â€” `deep-dive-remediation-batch` â€” ðŸŸ¢ Completed

- [x] **`fix/auth-context-bypass`** - Merged 304b4d1f: Bypassed auth context in services.
  - **Tags:** `[âœ… DONE]` `[âœ… VERIFIED]` `[Services]` `[M-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]`
  - **Plan:** ðŸ“Ž [PLAN-auth-context-bypass.md](docs/plans/PLAN-auth-context-bypass.md)
  - **Source of Truth:** ðŸ“– `src/services/CrewProfileService.ts` | `src/services/GroupRepository.ts` | `src/services/ScenesService.ts` | Audit: `R-15_findings.json`
  - **Goal:** Remove all 10 direct `supabase.auth.getUser()` / `getSession()` calls from service layer. Require callers to pass `userId` as a parameter sourced from AuthContext.
  - **Details:** 8 methods in CrewProfileService + GroupRepository.deleteGroup + ScenesService.flushSyncQueue all bypass AuthContext. Silent stale auth possible if token expires between calls.
- [x] **`qa/r06-r08-type-and-error-safety`**
- [x] **`qa/r11-r12-r16-async-and-closures`**
- [x] **`qa/r20-os-variance-parity-and-config`**
- [x] **`qa/r09-pii-scrubbing-leaks`**

### âš¡ [BATCH:error-handling-sweep] â€” ðŸŸ¢ Completed

- [x] **`fix/type-safety-any-cast-phase1`**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[UI]` `[M-RISK]` `[Banquet]` `[ðŸ¤– PRO-MED]`
  - **Details:** Phase 1: Fix structural `supabase as unknown as { rpc: any }` chain casts (2 files) + ProductManager interface `any[]` (6 violations) + `createStyles(Colors: any)` sweep. 294 total any-cast violations found. Phase 1 targeted the highest-structural-risk 30.

### âš¡ [BATCH:dep-diet-sweep] â€” ðŸŸ¢ Completed

- [x] **`chore/dead-dependency-prune`**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[Build]` `[L-RISK]` `[Snack]` `[ðŸ¤– PRO-LOW]`
  - **Details:** Removed 7 fully unutilized dependencies: `string-similarity`, `supercluster`, `jpeg-js`, `expo-speech`, `expo-image-manipulator`, `expo-blur`, and `react-native-nitro-image`. `react-native-vision-camera-worklets` and `react-native-nitro-modules` were retained as they inject necessary type definitions for Frame objects.

# SK8Lytz Master Bucket List Archive

This document contains the archive of all successfully completed and merged tasks, sprints, and epic batches within the SK8Lytz app ecosystem.

---

## ðŸ“¦ ARCHIVED SPRINT LOGS

### Sprint: v3.9.1 â€” 2026-06-07 (ble-gatt-hardening)

### [BATCH:ble-gatt-hardening] (Complete)
- **Prerequisite**: None
- **Active Tasks**: 3 tasks

- [x] **`fix/ble-gatt-queue-hardening`** (Merged: 1f22f260)
  - **Source of Truth:** ðŸ“– `src/services/BleWriteDispatcher.ts:141`, `src/services/BleConnectionManager.ts:252`, `src/hooks/ble/useBLEHeartbeat.ts:109` | Audit: `R-01_findings.json`, `R-13_findings.json`
  - **Goal:** Serialize all multi-device BLE GATT operations. Close the BleWriteQueue conditional bypass. Replace all `Promise.all(devices.map(...))` write paths with sequential `for...of` loops.
  - **Details:** R-01 found a conditional bypass in BleConnectionManager that allows direct `writeOp()` calls without queue protection. R-13 found 6 concurrent `Promise.all` write/disconnect patterns. Combined effect is GATT 133 error storms on multi-device operations. 7 files, 7 surgical edits.

- [x] **`fix/ble-pixel-buffer-clamp`** (Merged: 7156f1d4)
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[BLE]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-HIGH]`
  - **Plan:** ðŸ“Ž [PLAN-BLE-PIXEL-BUFFER-CLAMP.md](docs/plans/PLAN-BLE-PIXEL-BUFFER-CLAMP.md)
  - **Source of Truth:** ðŸ“– `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx:178`, `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx:73` | Audit: `R-10_findings.json`
  - **Goal:** Enforce 12-pixel minimum for all `0x59` Static Colorful dispatches in diagnostic lab. Add `Math.max(12, pts)` guard to 5 diagnostic lab files.
  - **Details:** Hardware safety rule â€” payloads below 10 pixels cause physical EEPROM buffer lockouts on 0xA3 chipset. 5 files, 5 one-line guards.

- [x] **`fix/ble-jitter-backoff`** (Merged: 5f895783)
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[BLE]` `[M-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]`
  - **Plan:** ðŸ“Ž [PLAN-BLE-JITTER-BACKOFF.md](docs/plans/PLAN-BLE-JITTER-BACKOFF.md)
  - **Source of Truth:** ðŸ“– `src/hooks/useBLE.ts:131`, `src/hooks/useDashboardAutoConnect.ts:169`, `src/services/BleConnectionManager.ts:150` | Audit: `R-03_findings.json`
  - **Goal:** Add Â±500ms jitter to all BLE reconnect retry timers to decoheres simultaneous multi-device reconnect stampedes.
  - **Details:** 3 fixed-interval retry paths with no jitter. Group reconnect after BLE drop causes synchronized retry burst every 1000ms, amplifying GATT 133 collisions. 1 utility function + 3 surgical edits.

---

### Sprint: v3.9.1 â€” 2026-06-07 (ble-p3-polish)

### [BATCH:ble-p3-polish] (Complete)
- **Prerequisite**: `[BATCH:ble-p2-architecture]` merged âœ…
- **Active Tasks**: `ble/partial-group-connectivity-ui`, `ble/predictive-reconnection`

- [x] **`ble/partial-group-connectivity-ui`**
  - **Tags:** `[âœ… DONE]` `[UI]` `[L-RISK]` `[Snack]` `[BATCH:ble-p3-polish]`
  - **Plan:** ðŸ“Ž `PLAN-ble-partial-group-connectivity-ui.md`
  - **Outcome:** Replaced header connection string with interactive skate icons per group. Merged @ 9034fb44.
  - **Source of Truth:** ðŸ“– [DashboardScreen.tsx:652-664] â€” auto-close guard; ghosted devices silently skipped by writeToDevice

- [x] **`ble/predictive-reconnection`** (CANCELLED)
  - **Tags:** `[âŒ ICED]` `[CORE]` `[L-RISK]` `[Feast]` `[BATCH:ble-p3-polish]`
  - **Outcome:** Cancelled by user. Decided not to implement predictive reconnection to save complexity.


### Sprint: v3.7.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-06-01 (camera-mode-white-only)

- [x] **`fix/camera-mode-white-only`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Resolved white-only camera capture bug by utilizing useResizer from react-native-vision-camera-resizer to properly downscale frames on the GPU to a 50x50 RGB buffer. Merged `283774f7`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-camera-mode-white-only.md](./plans/PLAN-camera-mode-white-only.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx#L96) Ãƒâ€šÃ‚Â§onFrame
  - **Goal:** Fix the camera frame processor crash that results in only capturing white colors.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Disposed GPUFrame inside finally block to prevent VRAM memory leaks. Set minimum channel offset of 3 to eliminate potential infinite loop hazards. Prioritized port 8081 for CDP console sniffer.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-29 (grouping-architecture-overhaul)

### [BATCH:grouping-architecture-overhaul] (Active) ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ Parallel-Safe
- **Prerequisite**: None
- **Active Tasks**: `feat/group-many-to-many`, `feat/group-mixed-state`, `fix/skatepark-hijack`, `fix/infinite-blob`, `fix/local-cloud-split-brain`

- [x] **`feat/group-many-to-many`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Migrated device group membership to junction table. Merged `7e34ba7`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Feast]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-group-many-to-many.md](./plans/PLAN-group-many-to-many.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
  - **Goal:** Resolve The "Mutually Exclusive" Flaw (1-to-1 Mapping).
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Refactored `DeviceSettings` and Supabase schema to support a many-to-many relationship using array-based group mapping.

- [x] **`feat/group-mixed-state`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added mixed state rendering and tap-to-unify UI. Merged `21636ec`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-group-mixed-state.md](./plans/PLAN-group-mixed-state.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
  - **Goal:** Resolve Lack of "Mixed State" Aggregation.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Updated group UI cards to intelligently aggregate states using green/red status dots and introduced "Mixed - Tap to Sync" visual cue.

- [x] **`fix/skatepark-hijack`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added RSSI gating to background discovery. Merged `378366a7`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-skatepark-hijack.md](./plans/PLAN-skatepark-hijack.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L323)
  - **Goal:** Resolve The "Skatepark Hijack" (Proximity Blindness).
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Introduced RSSI proximity gating and explicit user confirmation steps in `runAutoProvisioning` to prevent hijacking unregistered devices in public. Built an admin slider to adjust the threshold.

- [x] **`fix/infinite-blob`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Implemented while loop to auto-increment group names and prevent appending. Merged `a48aaebe`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-infinite-blob.md](./plans/PLAN-infinite-blob.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L363)
  - **Goal:** Resolve The "Infinite Blob" (Accidental Mega-Groups).
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Replaced `findIndex` with an auto-incrementing `while` loop so that multiple pairs of the same product type are placed into "My SK8Lytz HALOZ", "My SK8Lytz HALOZ 2", etc.

- [x] **`fix/local-cloud-split-brain`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Fixed the UUID lookup failure in `saveGroupTransactional` by queuing cloud sync if devices are pending. Merged `d9bf414`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-local-cloud-split-brain.md](./plans/PLAN-local-cloud-split-brain.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [DeviceRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts#L488)
  - **Goal:** Resolve The Local/Cloud Split-Brain (MAC vs DB ID).
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Implemented `hasPendingDevices` guard to bypass the RPC and delegate group mapping to `_queuePendingGroupSync` to prevent silent FK violations.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-27 (ble-hci)

### [BATCH:ble-hci] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ (Complete)


- [x] **`spike/0x40-chunked-framing-hci-verify`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Verified `writeChunked` 0x40 frame format on hardware. Merged `74ec886d`.
  - **Tags:** `[ÃƒÂ°Ã…Â¸Ã¢â‚¬Â¢Ã‚ÂµÃƒÂ¯Ã‚Â¸Ã‚Â SPIKE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:ble-hci]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-chunked-ble-framing-0x51.md](./plans/PLAN-chunked-ble-framing-0x51.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L706) Ãƒâ€šÃ‚Â§writeChunked and [ZENGGE_PROTOCOL_BIBLE.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L189) Ãƒâ€šÃ‚Â§0x51 Custom Scene
  - **Goal:** HCI-sniff verify the `writeChunked` 0x40 fragmentation frame format on real HALOZ/SOULZ hardware before wiring the Scene Builder UI to production chunked writes.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Validated `writeChunked` behavior on physical hardware via ADB HCI sniff, matched byte frames, and updated ZENGGE_PROTOCOL_BIBLE.md.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-27 (dependency-diet)

### [BATCH:dependency-diet] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ (Complete)

- [x] **`spike/major-dep-upgrades`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Upgraded React to 19.2.6 and @types/react to 19.2.15. Merged `89825d9c`.
  - **Tags:** `[ÃƒÂ°Ã…Â¸Ã¢â‚¬Â¢Ã‚ÂµÃƒÂ¯Ã‚Â¸Ã‚Â SPIKE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Feast]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:dependency-diet]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-spike-major-dep-upgrades.md](./plans/PLAN-spike-major-dep-upgrades.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json#L1)
  - **Goal:** Evaluate breaking changes for React and core dependencies.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Upgraded `react` and `react-dom` to 19.2.6, and `@types/react` to 19.2.15 while locking `react-native`, `typescript`, and `@react-native-async-storage/async-storage` to their stable versions to prevent BLE thread instability. Verified via full QA suite and merged.

---


### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-27 (monolith-cleanup)

### [BATCH:monolith-cleanup] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ (Complete)

- [x] **`refactor/split-monolith-files`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Monolith partitioned cleanly into 4 helper services + thin composition hook. Merged `48d35783`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:monolith-cleanup]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-refactor-split-monolith-files.md](./plans/PLAN-refactor-split-monolith-files.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [agent-behavior.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/agent-behavior.md#L15) Ãƒâ€šÃ‚Â§Look Before You Leap
  - **Goal:** Break down monolithic files > 30KB discovered during audit.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Surgically extracted `BleConnectionManager`, `BleWriteDispatcher`, `BlePingService`, and `BleLifecycleManager` into separate stateless modules, and refactored `useBLE.ts` as a clean, thin orchestrator hook. Passed all unit, compiler, browser, and static quality checks.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-27 (ble-and-camera-hardening)

### [BATCH:ble-hardening] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¹ (Complete)

- [x] **`fix/sweeper-gatt-discovery-skip`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Merged `971af30c`. Hoisted GATT discovery above cache lookup in interrogateDevice.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:ble-hardening]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-fix-sweeper-gatt-discovery-skip.md](./plans/PLAN-fix-sweeper-gatt-discovery-skip.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useBLESweeper.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts#L250) Ãƒâ€šÃ‚Â§interrogateDevice L250-268
  - **Goal:** Fix the third and final copy of the GATT discovery-on-cache-hit bug.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Hoisted `discoverAllServicesAndCharacteristics()` above the cache lookup block in interrogateDevice to ensure native handle maps populate before the `0x63` hardware settings query.

- [x] **`refactor/ble-session-factory`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Merged `ffa980c8`. Extracted connectÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢discoverÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢resolve adapter sequence.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:ble-hardening]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-refactor-ble-session-factory.md](./plans/PLAN-refactor-ble-session-factory.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L670) Ãƒâ€šÃ‚Â§handshakeDevice
  - **Goal:** Extract connectÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢discoverÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢resolve sequence from 4 callsites to eliminate Shotgun Surgery.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Created `BleSessionFactory` to own the invariant sequence `connect ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ discoverAll ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ resolveAdapter ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ return { conn, adapter }` with AbortSignal support, purging ~120 duplicate lines from useBLE, useBLESweeper, and useBLEAutoRecovery.

- [x] **`refactor/ble-typed-fsm-gate`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Merged `f8b1c07a`. Replaced raw string gates with compile-enforced FSM.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:ble-hardening]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-refactor-ble-typed-fsm-gate.md](./plans/PLAN-refactor-ble-typed-fsm-gate.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L61) Ãƒâ€šÃ‚Â§bleGateRef
  - **Goal:** Replace raw string union refs with typed state machine.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Created `BleStateMachine` FSM with valid transition checks and state event listeners. Replaced `bleGateRef` usages in useBLE, useBLESweeper, useBLEAutoRecovery, and useDashboardAutoConnect to use `.tag` checks and strict transitions, completely avoiding split-brain states and GATT collisions.

### [BATCH:camera-hardening] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ (Complete)

- [x] **`fix/camera-tracker-5hz-sideeffect`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Merged `459319ad`. Decoupled 5Hz updates using shared React Ref.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:camera-hardening]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-fix-camera-tracker-5hz-sideeffect.md](./plans/PLAN-fix-camera-tracker-5hz-sideeffect.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx#L103) Ãƒâ€šÃ‚Â§dispatchSniperColor
  - **Goal:** Remove 5Hz BLE side-effects.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Destructured `liveColorRef` in `CameraTrackerProps` and updated it at 5Hz inside the frame processor, bypassing parent re-renders. Replaced the 5Hz callback inside `CameraPanel` with a no-op, reading from `liveColorRef.current` strictly on Shutter Capture. Passed the verification QA suite cleanly.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-27 (camera-v2)

### [BATCH:camera-v2] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ (Complete)

- [x] **`feat/camera-vibe-catcher-v2`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Complete Camera Vibe Catcher v2 redesign using VisionCamera v5 and K-Means. Merged `24cb371`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[H-RISK]` `[Feast]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:camera-v2]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-camera-vibe-catcher-v2.md](./plans/PLAN-camera-vibe-catcher-v2.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [Image.nitro.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/node_modules/react-native-nitro-image/src/specs/Image.nitro.ts#L138) Ãƒâ€šÃ‚Â§crop() API, [PreviewView.nitro.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/node_modules/react-native-vision-camera/src/specs/views/PreviewView.nitro.ts#L112) Ãƒâ€šÃ‚Â§takeSnapshot @platform Android, [useDockedControllerState.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDockedControllerState.ts#L8) Ãƒâ€šÃ‚Â§BuilderNode interface
  - **Goal:** Complete camera mode restart: replace broken `takeSnapshot()` + Nitro Image pipeline with cross-platform `useFrameProcessor` + `vision-camera-resize-plugin`. Add SNIPER (center-pixel solid color) and VIBE (K-Means 3-color palette ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ auto-Builder payload) dual sub-modes.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Migrated to the VisionCamera v5 API utilizing the GPU-backed `useFrameOutput` and `vision-camera-resize-plugin` (50x50 RGB scaling). Added SNIPER center-pixel sample targeting the 25x25 center element and VIBE K-Means (k=3, 5 iterations) clustering to automatically map dominant swatches to FG/BG/ACCENT. Wired static and flow custom spatial patterns to `0x59` BLE commands with padded 12-pixel buffer overflow protection. Files changed: CameraTracker.tsx, CameraPanel.tsx, DockedController.tsx, package.json, kMeansPalette.ts, and kMeansPalette.test.ts. Passed all local and E2E QA checks cleanly.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-27 (permission-gating)

### [BATCH:permission-gating] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ¢Ã…Â¡Ã‚Â¡ (Complete)

- [x] **`feat/strict-permission-gating`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Fixed 3 permission bugs + reactive dock icon gating. Merged `51104a3`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:permission-gating]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-strict-permission-gating.md](./plans/PLAN-strict-permission-gating.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ `PermissionService.ts`, `DockedDock.tsx`, `DockedController.tsx`, `app.json`
  - **Goal:** Fix 3 permission bugs + enforce strict reactive UI gating for CAMERA/STREET dock icons.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â BUG-1: Added ACTIVITY_RECOGNITION to app.json android.permissions. BUG-2: Gated CAMERA mode behind checkPermission + openGlobalPermissionsModal. BUG-3: Gated STREET mode behind LOCATION check. Added reactive dock visibility via PERMISSION_STATUS_CHANGED_EVENT + AppState listener. Favorite restore for CAMERA falls back to MULTIMODE if denied. Files changed: app.json, PermissionService.ts, DockedController.tsx, DockedDock.tsx.

- [x] **`feat/offline-guest-gating`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Gated cloud publishing and community scenes behind offline mode. Merged `ccf4f2f`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:permission-gating]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-offline-guest-gating.md](./plans/PLAN-offline-guest-gating.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ `Supabase` Session State and UI Layouts
  - **Goal:** Implement an "Offline/Guest" mode that selectively hides cloud-dependent UI features.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Propagated `isOfflineMode` through `DashboardScreen`, `DockedController`, `QuickPresetModal`, and `CommunityModal` to hide all cloud-based features and default to local "My Saves" appropriately. Passed QA Suite verification.

---

### Sprint: v3.6.3 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-26 (camera-color-swatch-redesign)

- [x] **`feat/camera-color-swatch-redesign`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Platform-split color analysis (Android TextureView + iOS worklet), center crop, neutral white snap, and 5-swatch UI palette. Merged `7c254f60`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:camera-color-swatch-redesign]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-camera-color-swatch-redesign.md](./plans/PLAN-camera-color-swatch-redesign.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L306) Ãƒâ€šÃ‚Â§0x59 Static Colorful Pixel Array
  - **Goal:** Split Android & iOS camera tracking, implement center crop & neutral snap gate, and build a 5-swatch tactile UI.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Successfully split the CameraTracker hook into Android (TextureView takeSnapshot) and iOS (worklet useFrameOutput) platforms, resolving the Android react-native-worklets crash. Implemented a 32x32 center crop with 1x1 resize to average color over reticle rather than whole frame. Added a delta < 0.15 neutral white snapping gate to stop noisy green/blue drift. Completely redesigned CameraPanel to support a scrollable row of up to 5 captured tactile color swatches. Passed all verifiable checks cleanly.

---

### Sprint: v3.6.2 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-26 (music-mode-fix)

- [x] **`fix/music-mode-payload-cap`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Capped magnitude stream to 150 and disabled built-in mic when App mic active. Merged `99550a0`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:music-mode-fix]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity-ide/brain/3f09133f-0b00-4051-bb43-79e27aa0f099/implementation_plan.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L773) Ãƒâ€šÃ‚Â§Music Mode and [MusicModeFragment.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/com/zengge/wifi/activity/NewSymphony/fragment/MusicModeFragment.java#L595)
  - **Goal:** Fix the `isOn` microphone toggle routing and scale magnitude limits to 150 to stop ambient visualizer pattern cycling.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Capped real-time App mic magnitude stream to 150 (down from 255) to prevent physical controller saturation/visualizer lockout, and corrected the 0x73 'isOn' byte routing to dynamically toggle off the hardware microphone in App Mic mode.

---

### Sprint: v3.9.0 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-26 (rf-remote)

- [x] **`spike/rf-remote-2.4g-settings`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Corrected RF remote state opcodes and integrated DeviceSettingsModal into DashboardScreen. Merged `f94a0b5d`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:rf-remote]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-spike-rf-remote-2.4g-settings.md](./plans/PLAN-spike-rf-remote-2.4g-settings.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [ZENGGE_PROTOCOL_BIBLE.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L1) Ãƒâ€šÃ‚Â§RF Remote Settings
  - **Goal:** Reverse engineer and implement the 2.4G RF Remote Settings (Allow All, Don't Allow, Paired Only, Clear Pairing).
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Fixed inverted `ALLOW_ALL` (0x01), `ALLOW_NONE` (0x02), and `ALLOW_PAIRED` (0x03) byte mappings in `ZenggeProtocol.ts`, updated parsing mappings, added comprehensive unit tests, and fully imported/rendered the settings modal in `DashboardScreen.tsx` so long-pressing a device card displays the settings modal end-to-end.

---

### Sprint: v3.8.0 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-26 (supabase-security)

- [x] **`chore/audit-supabase-security`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Hardened PL/pgSQL function search paths and RLS policies on skate_spots, sk8lytz_app_settings, and telemetry. Merged `539dc791`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:supabase-security]`
  - **Plan:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â½ [PLAN-chore-audit-supabase-security.md](./plans/PLAN-chore-audit-supabase-security.md)
  - **Source of Truth:** ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬â€œ [SK8Lytz_App_Master_Reference.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md#L309) Ãƒâ€šÃ‚Â§Admin Tools Hub
  - **Goal:** Fix Supabase RLS policies and mutable search paths.
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Hardened PL/pgSQL function search paths, dropped permissive RLS overrides on sk8lytz_app_settings, removed public write exposures on skate_spots, and fixed the broken admin select filter on discovered_devices_telemetry. Excluded inventory product_catalog from scope.

---

### Sprint: v3.7.0 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-26 (tooling + chores + camera + music)

- [x] **`fix(tooling)/fortress-gatekeeper-divergence`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Patched gatekeeper to not destroy branches on failed --ff-only merge. Merged `053ed333`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added rebase-before-merge step + exit code capture + `continue` on failure. Branch and worktree are now PRESERVED when merge fails. Victory Snapshot VS-001 written to `safety-protocol.md`.

- [x] **`fix/missing-telemetry-script`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Implemented `tools/sync_remote_errors.mjs`. Merged `57d04a80`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:telemetry-sync]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Native Node 18+ fetch, zero deps, queries Supabase `telemetry_errors`, prints triage table + top 5 errors. CLI flags: `--hours`, `--limit`, `--json`. Offline-safe. Windows UV_HANDLE crash fixed via `process.exitCode`.

- [x] **`chore/audit-npm-vulnerabilities`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â npm audit returned 0 vulnerabilities. Merged `6b20619f`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:vulnerability-fix]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Zero vulnerabilities found. Bucket list entry was stale. No package changes required.

- [x] **`spike/rearchitect-camera-mode`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Platform-split fix: Android TextureView + iOS worklet. Merged `939a5262`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[H-RISK]` `[Feast]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 6 previous fixes all silently failed. Root causes: `takeSnapshot()` is `@platform Android` only; `implementationMode='performance'` (SurfaceView) doesn't support it; bare `catch{}` ate all errors; iOS had no valid fallback. Fix: Android uses TextureView + `takeSnapshot()`. iOS uses `useFrameOutput` worklet + `runOnJS`.

- [x] **`fix/camera-mode-warmup-reset`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Stabilized callback identity cascade and resolved Android warmup timer reset loop (stuck-on-pink bug). Merged `dc643b27`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Wrote a stable callback wrapper for `onColorDetected` inside a `useRef` inside `CameraTracker.tsx` to stop warmup loops and leaks.

- [x] **`fix/music-mode-ui-flex`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Wired ColorSwatch FG/BG pickers in MusicPanel. Merged `2e6363fa`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `ColorSwatch` wired + `overflow:'hidden'` clip removed.

- [x] **`fix/music-mode-color-inversion`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Fixed APP mic deactivation in music mode. Merged `a303d409`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `src === 'DEVICE'` as isOn flag silently killed APP mic path.

- [x] **`fix/music-mode-pattern-mapping`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Fixed colorMode, modeType passthrough, effectId clamp. Merged `4e41f7b8`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ READY]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 3 bugs across MusicDictionary, ZenggeAdapter, ZenggeProtocol.

---

### Sprint: v3.6.0 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-05-26

- [x] **`fix/health-telemetry-autostart`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Auto-started HealthKit/HealthConnect bridges before polling. Merged `03096cf3`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Injected `AppleHealthKit.initHealthKit()` (iOS) and `initialize()` (Android Health Connect) at the top of each `pollHealthData()` cycle.

- [x] **`spike/appstate-ble-reconnect`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Autonomous recovery on appstate wake. Merged `f518d38f`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ THINK]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added a 1500ms OS stack wakeup delay to `retriggerAutoConnect` and implemented a native `isDeviceConnected` audit in `useBLE.ts` to clear stale split-brain connections when the app wakes up.

- [x] **`feat/dashboard-pull-to-refresh`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Add pull-to-refresh to dashboard for manual BLE sweep. Merged `f8c0b2bd`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Wrapped `DashboardScreen` main `ScrollView` with `RefreshControl`. Triggered `retriggerAutoConnectRef.current()` on pull.

- [x] **`feat/crew-hub-radius-refresh`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added 10mi radius option. Merged `7aa002c5`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added 10 miles to the "Live Near You" map radius dropdown.

- [x] **`fix/crew-hub-collapsed-padding`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Fixed CrewHubSlab vertical padding state. Merged `7aa002c5`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Collapse padding when collapsed.

- [x] **`feat/crew-hub-mini-map`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added MapViewCluster inside slab. Merged `7aa002c5`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added native mini-map static view inside collapsed slab.

- [x] **`chore/remove-test-category`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Removed Test category from engine and UI. Merged `7aa002c5`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:pro-effects-cleanup]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Stripped all `group: 'Test'` entries.

- [x] **`feat/universal-slider-labels`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Refactored TacticalSlider labels inline. Merged `7aa002c5`.
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ DONE]` `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:universal-sliders]`
  - **Details:** COMPLETE ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Added `label` prop to `TacticalSlider.tsx`.

---

### Sprint: v3.5.2 and prior

- [x] **`fix/detective-social-only-sites`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Yelp/Places search on social-only sites. Merged auto-cleaned.
- [x] **`fix/detective-google-photo-refs`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Harvest photo references from Places. Merged auto-cleaned.
- [x] **`fix/detective-escalation-all-passes`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Re-run LLM passes on enriched text. Merged auto-cleaned.
- [x] **`spike/detective-model-upgrade-7b`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 7B/8B model evaluation (GDDR6 VRAM optimization). Merged auto-cleaned.
- [x] **`feat/user-correction-feedback-loop`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Log PUT spots feedback loop. Merged auto-cleaned.
- [x] **`feat/review-text-mining`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Regex mine reviews for structured data. Merged auto-cleaned.
- [x] **`hotfix/data-integrity-guards`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Preserved data upsert guards. Merged `1289dae4`.
- [x] **`chore/test-usecontrollerdispatch`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Mocked dispatch testing suite. Merged `7ff122d7`.
- [x] **`fix/gatt-conn-133-exception`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Android GATT 133 retries/teardown. Merged `df7a3c40`.
- [x] **`feat/detox-e2e-automation`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Detox E2E tests for React Native. Merged `94e52cd8`.
- [x] **`chore/consolidate-rules-workflows`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Rules consolidation (behavior and safety). Merged `17971a6`.
- [x] **`chore/align-git-workflows`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Unified attestation gating on git workflows. Merged `8bdea378`.
- [x] **`chore/verifiable-attestation`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Verifiable QA check-runner with cryptographic attestations. Merged `46f95b12`.
- [x] **`chore/worktree-junction-tsc`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Enable Directory Junctions for worktrees. Merged `214d4249`.
- [x] **`test/component-smoke`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Static parser checks for container views relative imports. Merged `214d4249`.
- [x] **`chore/eslint-import-gate`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ESLint Flat Config + unused-import gates. Merged `214d4249`.
- [x] **`fix/split-brain-hygiene`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Unified shared favorites and deleted dead stubs. Merged `15db163e`.
- [x] **`fix/ble-split-brain`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Unified single BLEProvider context. Merged `6b8e6917`.
- [x] **`fix/two-way-health-sync`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Push workout data natively to HealthKit/HealthConnect. Merged `b75f3f52`.
- [x] **`fix/telemetry-hardening-v2`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Consolidated AppLogger telemetry channels. Merged `6ef76e05`.
- [x] **`feat/babel-syntax-gate`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Pre-commit static AST syntax validator. Merged `6ef76e05`.
- [x] **`fix/black-hole-errors`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â CENTRALIZED production error visibility in BLE. Merged `f63a44c6`.
- [x] **`feat/geofence-rink-sync`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â social radar check-in geofence. Merged `c18bae6`.
- [x] **`feat/health-sync-integration`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â skating health telemetry sync. Merged `9168b2e`.
- [x] **`feat/scene-offline-sync-queue`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â offline AsyncStorage sync queues. Merged `fe99fb3f`.
- [x] **`spike/ios-android-parity-audit`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Android takeSnapshot TextureView + iOS worklet parity. Merged `f0516ac9`.


---

### Sprint: v3.9.1 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-06-06 (session-integrity)

### [BATCH:session-integrity] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¹ Sequential (Complete)

- [x] **`fix/session-watch-stale-closure`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Added `endSessionRef` stable-ref forwarder to `SessionContext.tsx`. `notifee.onBackgroundEvent`, `AppState` listener, and 10s STOPPED watchdog all call through the stable wrapper, eliminating the stale-closure data-loss bug.

- [x] **`fix/session-appstate-deps-loop`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Removed `sessionPhase` from the `AppState` listener `useEffect` dependency array in `SessionContext.tsx`. Listener now registered once on mount ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â eliminates double-registration and racing double-pause on background.

- [x] **`fix/session-autopause-starttime`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Removed the redundant `WatchBridge.syncSessionState` call in `SessionContext.tsx` auto-resume path. `useGlobalTelemetry` already pushes the correctly shifted anchor ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â the SessionContext push was overwriting it with `new Date()` (wrong).

- [x] **`fix/session-paused-persistence`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:session-integrity]`
  - **Outcome:** `@sk8lytz_session_active` AsyncStorage key upgraded from `'true'`/`'false'` to `JSON.stringify({ state, pausedAt })`. On crash-recovery, PAUSED state restored correctly. Backward compat: legacy `'true'`/`'false'` values handled as `'active'`/`'idle'`.

- [x] **`fix/session-background-end-data-loss`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** `notifee.onBackgroundEvent` in `index.ts` now calls `WatchBridge.syncSessionState({ status: 'STOPPED' })` + sets `@sk8lytz_pending_bg_end` flag. On next foreground, `SessionContext` detects pending flag and runs full `commitSession()` with cached telemetry. Eliminated the silent total-data-loss path.

- [x] **`fix/session-idle-race-summary`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Added `'ENDING'` to `SessionPhase` type. `endSession()` sets `ENDING` first (keeps FGS alive), awaits SUMMARY push to watch, then sets `IDLE`. Updated `isSkateSessionActive` derivation and `useGlobalTelemetry` type guard.

- [x] **`fix/session-watch-contract-audit`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:session-integrity]`
  - **Outcome:** Documentation-only. Audited `WatchConnectivityManager.swift` L81-117 and `WearableCommunicationService.kt` L125-130 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â both handle all 4 states. Added JSDoc contract comment to `WatchSessionState` type confirming native compliance.

---

### Sprint: v3.9.1 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-06-06 (ble-connection-resilience)

### [BATCH:ble-connection-resilience] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¹ Sequential (Complete)

- [x] **`fix/ble-gate-silent-invalid-transition`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `AppLogger.error()` + `__DEV__` throw on invalid transitions in `BleStateMachine.ts`. Added `forceTransitionTo()` escape hatch. Added `SCANNING ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ DISCONNECTING` as valid transition. `setGate()` now checks return value. Error-recovery catch blocks use `forceTransitionTo()`. 3 new Jest tests.

- [x] **`fix/ble-state-ref-lag`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Created `setConnectedDevicesSync()` wrapper that updates both `connectedDevicesRef.current` AND calls `setConnectedDevices()` atomically. Replaced all callsites. Removed the sync `useEffect`. 1-frame lag eliminated.

- [x] **`fix/ble-disconnect-stale-closure`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `handleOrganicDisconnectRef` stable-ref forwarder. Passed stable wrapper to `useBLEAutoRecovery` and `BleConnectionManager`. Ref always updated on every render ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â stale closure eliminated.

- [x] **`fix/ble-autoconnect-drain-permanent`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `failedAutoConnectRef: Map<string, { attempts, lastAttempt }>`. On failure: re-queues with backoff (1s ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ 4s ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ 12s). After 3 failures: ejects permanently. `retriggerAutoConnect()` clears failed map too.

- [x] **`fix/ble-ghost-state-flicker`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Removed pre-dispatch ghost-clear from Group Dropout Coordinator. Ghost state now cleared exclusively in `.then()` success callback after `connectToDevices` resolves. Devices remain visually dimmed until confirmed reconnected.

- [x] **`fix/ble-gatt-mutex-hotreload`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `_generation` counter + `_hotReloadCleanup()` to `useBLEGattMutex.ts`. On Hot Reload: aborts current holder, resets lock, increments generation. `acquireGattLock` races against 200ms timeout for orphaned promises. Stall reduced from 15s to ~200ms.

- [x] **`fix/ble-autoconnect-single-group`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `[69f65537] Unified batch completed successfully`
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Replaced `groups[0]` single-group selection with `Set<string>` MAC aggregation across ALL groups. Both cloud and offline paths collect all unique device MACs. `retriggerAutoConnect()` also clears `autoConnectRetriesRef`.

---

### Sprint: v3.9.1 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 2026-06-06 (account-critical)

### [BATCH:account-critical] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¹ Sequential (Complete)

- [x] **`fix/offline-session-persistence-queue`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â merged `76067e15` | C-01 CLOSED
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH]` `[BATCH:account-critical]`
  - **Outcome:** `SpeedTrackingService.saveSession()` queues offline sessions to `@SK8Lytz_PendingSession_Queue`. `flushPendingSessionQueue()` with re-entrancy guard wired into `useOfflineSyncWorker` 60s loop. User sees Alert instead of silent data loss. 4 Jest tests. 129/129 passing.

- [x] **`fix/offline-eula-bypass`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â merged `66fc95cf` | M-07 CLOSED
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** `ComplianceGate.tsx` offline bypass removed. First offline launch shows full `EulaModal`. Acceptance writes versioned JSON to `@Sk8lytz_offline_eula_accepted`. Subsequent launches pass immediately. Authenticated path unchanged.

- [x] **`fix/session-expiry-ux-message`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â merged `72ea48a9` | M-02 CLOSED
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** `App.tsx` `init()` detects expired token via `@Sk8lytz_auth_last_email` after null `getSession()`. Amber banner on `AuthScreen`: "Your session expired. Please sign in again." Clears on `SIGNED_IN`. No banner on fresh install.

- [x] **`fix/crew-delete-rpc`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â merged `d0cf72ee` | M-05 CLOSED
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CLOUD]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** `AccountModal.tsx` `handleDeleteCrew` was calling `leavePermanentCrew` (membership-only removal). Fixed to call `profileService.deleteCrew()` ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â hard-deletes crew + cascades memberships + broadcasts `session_ended`. Service layer was already correct; only the UI handler was wrong.

- [x] **`fix/offline-device-userid-stamp`** ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â NO-OP | M-06 CLOSED (defect does not exist)
  - **Tags:** `[ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ VERIFIED]` `[CLOUD]` `[M-RISK]` `[Snack]` `[ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** Audit finding was incorrect. `DeviceRepository._flushPendingSync(userId)` already receives `userId` from auth-gated `syncFromCloud()`. `dbRow.user_id = userId` at L704 stamps correctly at flush time. No null path exists.



### [BATCH:account-hardening] Ã¢â‚¬â€ Ã¢Å“â€¦ Completed 2026-06-06

- [x] **`refactor/auth-context-provider`** Ã¢â‚¬â€ merged 64daf01d | C-02 CLOSED
  - **Tags:** [Ã¢Å“â€¦ VERIFIED] [UI] [M-RISK] [Meal] [Ã°Å¸Â¤â€“ PRO-HIGH] [BATCH:account-hardening]
  - **Outcome:** Extracted auth state from App.tsx into AuthContext. Replaced independent getUser() calls across services and hooks with useAuth().user to prevent race conditions and redundant lookups.

- [x] **`fix/auth-tokens-secure-store`** Ã¢â‚¬â€ merged 738ba170 | M-01 CLOSED
  - **Tags:** [Ã¢Å“â€¦ VERIFIED] [CLOUD] [M-RISK] [Snack] [Ã°Å¸Â¤â€“ FLASH] [BATCH:account-hardening]
  - **Outcome:** Replaced AsyncStorage with expo-secure-store for Supabase auth token storage adapter. Added migration logic to move existing tokens on first launch.

- [x] **`fix/password-change-reauth`** Ã¢â‚¬â€ merged 363b9808 | M-03 CLOSED
  - **Tags:** [Ã¢Å“â€¦ VERIFIED] [CLOUD] [M-RISK] [Snack] [Ã°Å¸Â¤â€“ FLASH] [BATCH:account-hardening]
  - **Outcome:** Added "Current Password" verification to AccountModal.tsx before allowing a password update. signInWithPassword gate added before updateUser.

- [x] **`feat/notif-prefs-cloud-sync`** Ã¢â‚¬â€ merged 60067804 | M-04 CLOSED
  - **Tags:** [Ã¢Å“â€¦ VERIFIED] [CLOUD] [M-RISK] [Snack] [Ã°Å¸Â¤â€“ FLASH] [BATCH:account-hardening]
  - **Outcome:** Added notif_preferences JSONB column. Updated AuthProfileService.updateProfile and useAccountOverview to sync local preferences to the cloud and merge them on load.

*End of Archive.*
### [BATCH:ble-p2-xstate-fsm] Ã¢â‚¬â€ Ã°Å¸â€œâ€¹ Sequential (touches all BLE state files)

- [x] **le/xstate-fsm-migration** Ã¢â‚¬â€ migrated BLE engine to XState v5 FSM. Merged \5cdeb702\.
  - **Tags:** [Ã°Å¸â€¢ÂµÃ¯Â¸Â SPIKE] [Ã¢Å“â€¦ VERIFIED] [CORE] [H-RISK] [Feast] [Ã°Å¸Â¤â€“ THINK] [BATCH:ble-p2-xstate-fsm]
  - **Plan:** Ã°Å¸â€œÅ½ PLAN-ble-xstate-fsm-migration.md
  - **Source of Truth:** Ã°Å¸â€œâ€“ [BleStateMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleStateMachine.ts)
  - **Goal:** Evaluate migrating BLE state management from scattered refs/effects/tagged unions to XState statecharts. Per-device FSMs composed into group-level summaries. Invalid states become structurally impossible.
  - **Details:** COMPLETE Ã¢â‚¬â€ Migrated BLE system to XState v5 (useMachine). Created BleMachine.ts and BleMachine.types.ts. Replaced BleStateMachine class. Added shim to leGateRef to prevent regression and satisfy type checking. Verified via 
pm run verify which includes QA tests.

### [BATCH:ble-p3-polish] Ã¢â‚¬â€ Ã¢Å¡Â¡ Parallel (Completed Tasks)

- [x] **`ble/connection-health-heartbeat`** Ã¢â‚¬â€ `pingConnectedDevice` hook + 7 Jest tests. Merged `84e21bb3`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[LAB]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:ble-p3-polish]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-ble-connection-health-heartbeat.md](file:///C:/Users/Magma/.gemini/antigravity/brain/acebf202-b9db-4779-8e51-e3ed33ab835d/PLAN-ble-connection-health-heartbeat.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `useBLE.ts` Ã¢â‚¬â€ no periodic connection liveness check; stale GATT handles survive for minutes on Samsung Galaxy A-series
  - **Goal:** Add a lightweight BLE ping every 45-60s to connected devices to verify the connection is actually alive. If ping fails, preemptively trigger recovery instead of waiting for the next write to fail.
  - **Details:** COMPLETE Ã¢â‚¬â€ Created `src/hooks/ble/useBLEHeartbeat.ts` with `pingConnectedDevice()` (pure exported fn, testable) and `useBLEHeartbeat` (thin setInterval orchestrator). Wired into `useBLE.ts`. 7 tests in `useBLEHeartbeat.test.ts` cover Zengge happy path, BanlanX fallback, empty-packets fallback, GATT 133 error, code 8 error, cancel-throws safety, success no-op. Also fixed: `verifiable-check-runner.js` junction relink idempotency + `jest.config.js` `transformIgnorePatterns` expanded for `expo-*` packages. Needs physical device smoke test to confirm stale link recovery fires correctly in the field.

- [x] **`ble/post-connect-rssi-monitoring`** Ã¢â‚¬â€ `useBLERSSIMonitor` + live rssiMap on device cards. Merged `fd635db8`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[LAB]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-p3-polish]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-ble-post-connect-rssi-monitoring.md](file:///C:/Users/Magma/.gemini/antigravity/brain/acebf202-b9db-4779-8e51-e3ed33ab835d/PLAN-ble-post-connect-rssi-monitoring.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `useBLE.ts` Ã¢â‚¬â€ RSSI checked only during scan, never after connection established
  - **Goal:** Poll RSSI every 30s on connected devices. If RSSI drops below -75 dBm, show a "weak connection" warning badge on the device card. If it drops below -82 dBm, preemptively disconnect and reconnect.
  - **Details:** COMPLETE Ã¢â‚¬â€ Created `src/hooks/ble/useBLERSSIMonitor.ts` with `readDeviceRSSI()` (pure testable fn) and `useBLERSSIMonitor()` (30s polling hook, returns `Record<string,number>`). Created `src/components/ConnectionStrengthBadge.tsx` (3-bar pure-View signal icon, no SVG dep). Wired `rssiMap` into `BluetoothLowEnergyApi` + `useBLE.ts` return. In `DashboardScreen.tsx` `renderItem`, live `rssiMap[mac]` overrides stale scan-time `device.rssi` Ã¢â‚¬â€ existing wifi icon auto-updates. Preemptive reconnect guard uses verified `ghostedDeviceIds.includes(mac)`. 9 tests. Needs physical device smoke test to confirm badge updates within 30s of signal degradation.


#### Batch Strategy Table Ã¢â‚¬â€ Deep-Dive Code Audit (2026-06-07)

---

*(account-hardening batch complete Ã¢â‚¬â€ archived @ Sprint v3.9.1)*
*(account-critical batch complete Ã¢â‚¬â€ archived @ Sprint v3.9.1)*

## Ã°Å¸Å¡Â§ ACTIVE SPRINT

---

> Ã¢Å“â€¦ All triage items from this audit have been completed and archived in `tools/SK8Lytz_Bucket_List_ARCHIVE.md` under Sprint v3.9.1 (2026-06-06).
> 3 research agents Ãƒâ€” 30+ files Ãƒâ€” every line read. 14 issues identified across session lifecycle and BLE connection resilience.
> Ã°Å¸â€œÅ  **Source Analysis**: [Connection & Session Architecture Audit (2026-06-06)](file:///C:/Users/Magma/.gemini/antigravity/brain/25ac1742-4218-4218-91d4-cea42835db9b/analysis_results.md)

## Ã°Å¸Å¡â€˜ TRIAGE QUEUE (Bugs & Hotfixes)

---

> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.
> Ã¢Å¡Â Ã¯Â¸Â AI AGENT DIRECTIVES (THE CONSTITUTION)

# SK8Lytz Master Bucket List



#### Batch Strategy Table Ã¢â‚¬â€ Deep-Dive Code Audit (2026-06-07)

> Identified violations from the Deep-Dive Code Audit (Rule 16 + Offline Telemetry).

| Batch | Type | Tasks | File Overlap | Prerequisite |
|-------|------|-------|-------------|-------------|
| `[BATCH:deep-dive-regressions]` | Ã°Å¸â€œâ€¹ Sequential | 1 | Touches 25+ files | None |

---

### Ã¢Å¡Â¡ [BATCH:deep-dive-regressions] Ã¢â‚¬â€ `refactor/deep-dive-regressions` Ã¢â‚¬â€ Ã°Å¸â€Â´ Critical (Active)
> **Worktree**: `refactor/deep-dive-regressions` Ã‚Â· **Type**: Sequential Ã‚Â· **Prerequisite**: None
> **Source Analysis**: Ã°Å¸â€œÅ  [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã¢â‚¬â€ 16-agent deep dive exposed numerous Rule 16 violations (missing try/catch, `any` casts, inline functions) and critical offline telemetry dropping.

- [x] **`refactor/deep-dive-telemetry`** Ã¢â‚¬â€ merged @ 256d3257 (Added robust setTimeout debounce to AppLogger and wrapped I/O in try/catch)
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[DATA]` `[Ã¢Å¡Â Ã¯Â¸Â H-RISK]` `[Ã°Å¸Â¥Â© Feast]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix offline telemetry drops in `DATA_LAYER`, `SESSION_TRACKING`, and `AppLogger.ts`.
  - **Decision Log:** Critical I/O operations lack try/catch wrappers, causing silent failures when the app goes offline. Replaced faulty manual debounce with true setTimeout buffer and forced offline persists.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã‚Â· Plan: [PLAN-refactor-deep-dive-telemetry.md](docs/plans/PLAN-refactor-deep-dive-telemetry.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `src/services/AppLogger.ts`

- [x] **`refactor/deep-dive-type-safety`** Ã¢â‚¬â€ merged @ 9ca523d3 (Eliminated `any` casts in AccountModal and CrewTab, enforced strict types via `React.Dispatch<React.SetStateAction<...>>`)
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[CORE]` `[Ã¢Å¡Â Ã¯Â¸Â H-RISK]` `[Ã°Å¸Â¥Â© Feast]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Eradicate all `any` casts from `NOTIFICATIONS_&_ROUTING`, `GROUP_SYNC`, `HARDWARE_PROTOCOLS`, and `IDENTITY`.
  - **Decision Log:** Pervasive use of `any` casts bypasses TypeScript safety during crashes, violating Rule 16.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã‚Â· Plan: [PLAN-refactor-deep-dive-type-safety.md](docs/plans/PLAN-refactor-deep-dive-type-safety.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `src/components/crew/CrewLandingScreen.tsx` + `CONSTITUTION.md`

- [x] **`refactor/deep-dive-perf`** Ã¢â‚¬â€ merged @ e72ff390 (Extracted inline styles to StyleSheet.create and moved inline mappings and renderItem to useCallback/useMemo across UI components)
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[UI]` `[Ã¢Å¡Â Ã¯Â¸Â M-RISK]` `[Ã°Å¸ÂÂ± Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Resolve performance leaks caused by inline functions and styles in FlatLists across `UI_CONTROLS` and `GROUP_SYNC`.
  - **Decision Log:** Widespread inline styles/functions cause severe re-render thrashing.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã‚Â· Plan: [PLAN-refactor-deep-dive-perf.md](docs/plans/PLAN-refactor-deep-dive-perf.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `src/components/DockedController.tsx`

- [x] **`refactor/deep-dive-ble-core`** Ã¢â‚¬â€ merged @ 0718bb3b (Fixed stale closures in AutoRecovery, added offline AsyncStorage telemetry queues, removed any casts in useBLESweeper)
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[CORE]` `[Ã¢Å¡Â Ã¯Â¸Â H-RISK]` `[Ã°Å¸Â¥Â© Feast]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix stale closures in `useBLEAutoRecovery.ts`, missing offline queues in `useBLEScanner.ts`, and `any` casts in `useBLESweeper.ts`.
  - **Decision Log:** BLE components have critical stale closures and lack offline persistence for discovered devices.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã‚Â· Plan: [PLAN-refactor-deep-dive-ble-core.md](docs/plans/PLAN-refactor-deep-dive-ble-core.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `src/hooks/ble/useBLEAutoRecovery.ts`

- [x] **`refactor/deep-dive-os-permissions`** Ã¢â‚¬â€ merged @ 14dff9da (Fixed Android 14+ FOREGROUND_SERVICE location flags in AndroidManifest.xml and wrapped AsyncStorage permissions telemetry)
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[NATIVE]` `[Ã¢Å¡Â Ã¯Â¸Â M-RISK]` `[Ã°Å¸ÂÂ± Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix missing Android 14+ FOREGROUND_SERVICE flags and conflicting location permissions in `AndroidManifest.xml`.
  - **Decision Log:** Missing OS-level foreground service definitions for Android 14+ will cause background execution crashes.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã‚Â· Plan: [PLAN-refactor-deep-dive-os-permissions.md](docs/plans/PLAN-refactor-deep-dive-os-permissions.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `android/app/src/main/AndroidManifest.xml`

- [x] **`refactor/deep-dive-native-cloud`** Ã¢â‚¬â€ merged @ c03b83e5 (Fixed telemetry overwrites in WatchConnectivity, buffered WearMessageSender, added try/catch to edge functions, and safely casted SQL JSON metrics)
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€ INFERRED]` `[CLOUD]` `[Ã¢Å¡Â Ã¯Â¸Â H-RISK]` `[Ã°Å¸Â¥Â© Feast]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix telemetry overwrites in WatchConnectivityManager, dropouts in WearMessageSender, and missing try/catch in Supabase Edge Functions.
  - **Decision Log:** Native watch targets and cloud edge functions drop data due to unhandled exceptions and payload overwriting.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Ã‚Â· Plan: [PLAN-refactor-deep-dive-native-cloud.md](docs/plans/PLAN-refactor-deep-dive-native-cloud.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `targets/watch/WatchConnectivityManager.swift`

---



### Ã°Å¸â€Â´ Tier 1: Critical System Integrity

#### Batch Strategy Table Ã¢â‚¬â€ Constitutional Audit Burn-Down

> Identified violations from the Deep-Dive System Audit (2026-06-06).

| Batch | Type | Tasks | File Overlap | Prerequisite |
|-------|------|-------|-------------|-------------|
| `[BATCH:burn-down-audit-failures]` | Ã°Å¸â€œâ€¹ Sequential | 1 | Touches 25+ files (BLE & Auth) | None |

---

### Ã¢Å¡Â¡ [BATCH:burn-down-audit-failures] Ã¢â‚¬â€ `refactor/burn-down-audit-failures` Ã¢â‚¬â€ Ã°Å¸â€Â´ Critical
> **Worktree**: `refactor/burn-down-audit-failures` Ã‚Â· **Type**: Sequential Ã‚Â· **Prerequisite**: None
> **Source Analysis**: Ã°Å¸â€œÅ  [audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/25ac1742-4218-4218-91d4-cea42835db9b/audit_report.md) Ã¢â‚¬â€ 3-agent deep dive exposed 14 new `any` casts, faked XState shims, and 16 AuthContext bypasses.

- [/] **`refactor/burn-down-audit-failures`**
  - **Tags:** `[Ã°Å¸Å¡Â§ IN PROGRESS]` `[Ã°Å¸Â¤â€ INFERRED]` `[CORE]` `[Ã¢Å¡Â Ã¯Â¸Â H-RISK]` `[Ã°Å¸Â¥Â© Feast]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:burn-down-audit-failures]`
  - **Goal:** Eradicate 14 injected `any` casts, finalize the split-brain XState migration, and enforce AuthContext globally across 16 bypassed hooks/services.
  - **Decision Log:** Deep dive audit by 3 subagents found 14 injected `any` casts, split-brain XState migration, and 16 bypassed AuthContext hooks/services. Pushed on 2026-06-06 as 'completed' but failed constitutional laws.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/25ac1742-4218-4218-91d4-cea42835db9b/audit_report.md) Ã‚Â· Plan: [PLAN-refactor-burn-down-audit-failures.md](docs/plans/PLAN-refactor-burn-down-audit-failures.md)
    Key finding: "XState migration faked via `any` shims. 16 files bypass AuthContext. 14 new `any` casts violated Rule 1."
    Rejected alternative: "Leaving them as Tech Debt. This violates core rules and causes cascading type failures."
  - **Source of Truth:** Ã°Å¸â€œâ€“ `src/context/AuthContext.tsx:9-10` + `CONSTITUTION.md` Rule 1.
  - **Details:** 3-phase burn down: 1. Purge `any` casts. 2. Rip out `BleStateMachine.ts` and fake `bleGateRef` shims. 3. Delete `supabase.auth.getUser` from 8 hooks and 8 services, forcing context/prop passing.

---



### [BATCH:ble-p1-ios-platform] â€” ? Parallel (zero file overlap)

> ?? **P2 â€” Planned Improvements.** iOS-specific fixes.

- **Prerequisite**: None
- **Active Tasks**: le/ios-state-restoration

- [x] **le/ios-state-restoration** (Merged @ f6af517d â€” Implemented react-native-ble-plx restoreStateIdentifier for silent iOS CoreBluetooth connection recovery)
  - **Tags:** [?? NEEDS PLAN] [?? INFERRED] [LAB] [M-RISK] [Meal] [?? THINK] [BATCH:ble-p1-ios-platform]
  - **Plan:** ?? PLAN-ble-ios-state-restoration.md *(pending)*
  - **Source of Truth:** ?? Apple [State Preservation and Restoration docs](https://developer.apple.com/library/archive/documentation/NetworkingInternetWeb/Conceptual/CoreBluetooth_concepts/CoreBluetoothBackgroundProcessingForIOSApps/PerformingTasksWhileYourAppIsInTheBackground.html)
  - **Goal:** Implement Core Bluetooth State Preservation and Restoration via a small native module (~30 lines Swift) so iOS can restore pending BLE connections when the app is terminated and relaunched.
  - **Details:** Issue iOS-02. If iOS kills the app while in Music/Street mode, BLE connections are silently dropped with no recovery. Core Bluetooth provides CBCentralManagerOptionRestoreIdentifierKey â€” the system saves pending connections and restores them on relaunch. Nanoleaf and LIFX both implement this. Requires: register restore identifier in native BLE init, implement centralManager:willRestoreState: delegate, bridge restored peripheral IDs back to JS via eact-native-ble-plx configuration or a small custom native module.

---




- [x] **`qa/fix-ble-queue-and-gatt-collisions`** ?? Merged in e465d08a
  - **Tags:** `[?? TRIAGE]` `[? VERIFIED]` `[NATIVE]` `[H-RISK]` `[Meal]` `[?? THINK]`
  - **Plan:** ?? [PLAN-fix-ble-queue-and-gatt-collisions.md](docs/plans/PLAN-fix-ble-queue-and-gatt-collisions.md)
  - **Source of Truth:** ?? `system_audit_report.md`
  - **Goal:** Fix R-01 (useBLEAutoRecovery queue bypass), R-13 (Promise.all GATT collisions), R-03 (Missing jitter), R-10 (Sequential group writes), R-16 (Hardcoded delays).

- [x] **`qa/fix-split-brain-and-offline-first`** ?? Merged in 5e077e68
  - **Tags:** `[?? TRIAGE]` `[? VERIFIED]` `[CLOUD]` `[H-RISK]` `[Feast]` `[?? THINK]`
  - **Plan:** ?? [PLAN-fix-split-brain-and-offline-first.md](docs/plans/PLAN-fix-split-brain-and-offline-first.md)
  - **Source of Truth:** ?? `system_audit_report.md`
  - **Goal:** Unify telemetry hooks, fix useBLEScanner/useBLESweeper duality, kill useControllerPersistence in favor of useDeviceStateLedger. Fix R-05 blocking network calls in useProductCatalog/ScenesService.


- [x] **`qa/fix-error-handling-and-io-safety`** ðŸš€ Merged in 86edaf43
  - **Tags:** `[ðŸ”´ TRIAGE]` `[âœ… VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[ðŸ¤– FAST]`
  - **Plan:** ðŸ“Ž [PLAN-fix-error-handling-and-io-safety.md](docs/plans/PLAN-fix-error-handling-and-io-safety.md)
  - **Source of Truth:** ðŸ“– `system_audit_report.md`
  - **Goal:** Standardize `e instanceof Error` unwrapping across the app (R-06). Wrap unhandled async networks in `try/catch` (R-11). Eliminate `any` casts (R-08).

- [x] **`qa/fix-os-parity-and-build-config`** ðŸš€ Merged in 86edaf43
  - **Tags:** `[ðŸ”´ TRIAGE]` `[âœ… VERIFIED]` `[NATIVE]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-HIGH]`
  - **Plan:** ðŸ“Ž [PLAN-fix-os-parity-and-build-config.md](docs/plans/PLAN-fix-os-parity-and-build-config.md)
  - **Source of Truth:** ðŸ“– `system_audit_report.md`
  - **Goal:** Fix R-20 missing Foreground Service Types and iOS Location usage descriptions in `app.config.js`. Scrub API keys. Fix shadowColor/elevation parity. 
 # # #   [ B A T C H : s p l i t - b r a i n - e r a d i c a t i o n ]   -   C R I T I C A L   |   C O M P L E T E D  
 -   [ x ]   f i x / s p l i t - b r a i n - e r a d i c a t i o n   ( 6 5 2 8 2 c 2 7 )   -   E l i m i n a t e d   3   c o n f i r m e d   s p l i t - b r a i n   s t a t e   d u p l i c a t i o n   p a t t e r n s   v i a   C r e w S e r v i c e   E v e n t E m i t t e r   p u b / s u b ,   s i n g l e   s p e e d   q u e u e ,   a n d   p r o t o c o l   i n s t a n c e s .  
     -   T a g s :   [ V E R I F I E D ]   [ B L E ]   [ H - R I S K ]   [ B a n q u e t ]   [ T H I N K ]  
 

- [x] **`fix/pii-scrub-telemetry`**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[Services]` `[H-RISK]` `[Meal]` `[ðŸ§  PRO-MED]`
  - **Plan:** ðŸ”— [PLAN-PII-SCRUB-TELEMETRY.md](docs/plans/PLAN-PII-SCRUB-TELEMETRY.md)
  - **Source of Truth:** ðŸ“ `src/hooks/useCrewSession.ts:107`, `src/hooks/useBLE.ts:444`, `src/hooks/useDashboardAutoConnect.ts:222`, `src/hooks/useDeviceStateLedger.ts:162`, `src/screens/DashboardScreen.tsx:434` | Audit: `R-09_findings.json`
  - **Goal:** Remove raw MAC addresses and user display_names from all AppLogger telemetry calls. Implement `scrubPII()` hash helper. 49 total call sites (5 primary + 44 sweep).
  - **Details:** GDPR compliance risk. MAC addresses and display names are leaking into cloud telemetry logs. Primary fix: implement `scrubPII()` deterministic hash, apply to 5 verified high-severity sites, then grep-sweep remaining 44.

- [x] **`fix/stale-closure-intervals`**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[Hooks]` `[H-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]`
  - **Plan:** ðŸ“Ž [PLAN-STALE-CLOSURE-INTERVALS.md](docs/plans/PLAN-STALE-CLOSURE-INTERVALS.md)
  - **Source of Truth:** ðŸ“– `src/hooks/cloud/useOfflineSyncWorker.ts:36`, `src/hooks/useDashboardAutoConnect.ts:169`, `src/hooks/useTelemetryLedger.ts:169`, `src/hooks/ble/useBLEHeartbeat.ts:105` | Audit: `R-12_findings.json`, `R-26_findings.json`
  - **Goal:** Fix 2 stale closure intervals that capture null user/session (preventing offline sync from ever running). Add `_isRunning` boolean ref guards to 6 async interval callbacks to prevent concurrent execution and duplicate DB writes.
  - **Details:** `useOfflineSyncWorker` captures stale `user=null` in empty-dep `useEffect` â€” offline queue never flushes post-login. R-26 found 7 intervals firing async callbacks concurrently, causing double DB inserts, duplicate GATT reads, and duplicate notifee calls on slow networks.
### [BATCH:offline-first-sweep] ?? LOW | (Archived)
- [x] **`feat/offline-first-cache-layer`** (Merged in aa5ad615)
  - **Goal:** Added AsyncStorage cache-first layer to 6 Supabase fetch paths. Cache serves immediately; network sync runs in background. Fixed offline experience for hardware_blacklist, app settings, gradients, scenes, and skate spots.


- [x] **`fix/async-error-hardening`** ðŸš€ Merged in 027bc694
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[Services]` `[M-RISK]` `[Banquet]` `[ðŸ¤– PRO-LOW]`
  - **Plan:** ðŸ“Ž [PLAN-ASYNC-ERROR-HARDENING.md](docs/plans/PLAN-ASYNC-ERROR-HARDENING.md)
  - **Source of Truth:** ðŸ“– `src/services/CrewProfileService.ts:31-461`, `src/services/CrewService.ts:76-597`, `src/services/DeviceRepository.ts:140-724` | Audit: `R-11_findings.json` (120+ findings), `R-06_findings.json` (72+ findings)
  - **Goal:** Wrap 120+ naked `await` operations in try/catch and fix 72+ catch blocks missing `e instanceof Error` unwrapping. ~192 total surgical edits across ~30 files.
  - **Details:** `CrewProfileService.ts` alone has 55 naked awaits. `DeviceRepository.ts` has 8 untyped catch blocks. These are all LOW-cognitive-load, high-volume pattern fixes ideal for a lesser model to execute with this plan as the SoT.

- [x] **`fix/gatt-race-conditions`** (Merged @ accf781c) ðŸš€ Merged in accf781c
  - **Tags:** `[âšª READY]` `[âœ… VERIFIED]` `[BLE]` `[H-RISK]` `[Meal]` `[ðŸ¤– THINK]`
  - **Plan:** ðŸ“Ž [PLAN-gatt-race-conditions.md](docs/plans/PLAN-gatt-race-conditions.md)
  - **Source of Truth:** ðŸ“– `src/hooks/ble/useBLEBatterySweep.ts:135` | `src/hooks/useBLE.ts:410` | `src/services/BleConnectionManager.ts:47` | `src/hooks/useDashboardGroups.ts:235` | `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx:178` | `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx:73` | Audit: `R-26_findings.json` + `R-10_findings.json`
  - **Goal:** Fix 4 re-entrancy race conditions (burstTimer overwrite, pingDevice sweeper race, GATT lock pre-check, double-tap power toggle) and 2 DiagLab `Promise.all` concurrent write collisions â†’ GATT 133.
  - **Details:** GATT 133 errors on Android are directly caused by these races. Rapid battery sweep, rapid ping, and DiagLab group commands all trigger the issue today.

- [x] **`fix/flatlist-perf-sweep`** - Merged 5fcdb090: Injected removeClippedSubviews and initialNumToRender performance props to all flatlists. ðŸš€ Merged in 5fcdb090
  - **Tags:** `[âšª READY]` `[âœ… VERIFIED]` `[UI]` `[L-RISK]` `[Meal]` `[ðŸ¤– PRO-LOW]` `[BATCH]`
  - **Plan:** ðŸ“Ž `(generate on `/start-task`)`
  - **Source of Truth:** ðŸ“– `src/components/docked/FavoritesPanel.tsx:80,94` | `src/components/patterns/GradientLibraryTab.tsx:118,121,125` | `src/components/patterns/PatternPickerTab.tsx:151,153,154` | `src/components/CommunityModal.tsx:258,262` | `src/components/VerticalPatternDrum.tsx:101,115,116` | `src/components/admin/tools/tabs/DiagnosticLabSnifferTab.tsx:27,28,29` | Audit: `R-07_findings.json â†’ PERF-R07-001 through PERF-R07-016`
  - **Goal:** Extract 16 inline FlatList props (`keyExtractor`, `renderItem`, `contentContainerStyle`, `columnWrapperStyle`, `getItemLayout`, `ListEmptyComponent`) to stable `useCallback`/`StyleSheet.create` references across 6 components.
  - **Details:** Every inline prop re-creates a new object/function reference on each render, causing FlatList to re-render its entire item list. Pattern picker and gradient library performance is directly affected.

- [x] **`fix/error-handling-sweep`** - Merged 165f7be7: Applied instanceof Error type safety checks to catch blocks. ðŸš€ Merged in 165f7be7
  - **Tags:** `[âšª READY]` `[âœ… VERIFIED]` `[Services]` `[L-RISK]` `[Meal]` `[ðŸ¤– PRO-LOW]` `[BATCH]`
  - **Plan:** ðŸ“Ž `(generate on /start-task)`
  - **Source of Truth:** ðŸ“– `src/services/supabaseClient.ts:21,29` | `src/utils/migrateAuthTokens.ts:26` | `src/components/CameraTracker.tsx:149` | `src/components/DockedController.tsx:435,467` | `src/hooks/ble/useBLEAutoRecovery.ts:474` | `src/hooks/useCrewManage.ts:62` | `src/services/SpeedTrackingService.ts:149,278` | `src/services/BleWriteQueue.ts:181` | `src/context/SessionContext.tsx:353,386` | Audit: `R-06_findings.json` + `DOMAIN_NATIVE_&_WATCH_findings.json`
  - **Goal:** Replace 17 raw `console.error(e)` and bare `String(e)` error log patterns with the standard `e instanceof Error ? e.message : String(e)` pattern. Replace 1 silent swallow `catch (e) {}` with `AppLogger.warn`.
  - **Details:** 4 HIGH severity `console.error(rawError)` calls can log [object Object] instead of message. Standard error pattern is enforced codebase-wide but these 17 instances were missed.

- [x] **`fix/os-permissions-config`** - Merged: Added missing iOS descriptors and removed redundant Android intents.
  - **Tags:** `[âšª READY]` `[âœ… VERIFIED]` `[BUILD]` `[M-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]`
  - **Plan:** ðŸ“Ž [PLAN-os-permissions-config.md](docs/plans/PLAN-os-permissions-config.md)
  - **Source of Truth:** ðŸ“– `app.config.js:17,21` | `android/app/src/main/AndroidManifest.xml:49` | Audit: `DOMAIN_OS_PERMISSIONS_findings.json â†’ OS_PERM-001, OS_PERM-002, OS_PERM-003`
  - **Goal:** Add missing `NSHealthUpdateUsageDescription` (iOS crash fix), correct inaccurate `NSLocationWhenInUseUsageDescription` (App Store rejection fix), remove 4 duplicate AndroidManifest intent-filter entries.
  - **Details:** Missing `NSHealthUpdateUsageDescription` will crash iOS when Health permissions are requested. Inaccurate location description will trigger App Store review rejection. Both are pre-release blockers.

- [x] **`fix/animated-loop-leak-sweep`** - Merged: Fixed 6 component animation leak bugs with loop.stop() cleanup.
  - **Tags:** `[âšª READY]` `[âœ… VERIFIED]` `[UI]` `[L-RISK]` `[Meal]` `[ðŸ¤– PRO-LOW]` `[BATCH]`
  - **Plan:** ðŸ“Ž [PLAN-animated-loop-leak-sweep.md](docs/plans/PLAN-animated-loop-leak-sweep.md)
  - **Source of Truth:** ðŸ“– `src/components/AccountModal.tsx:84` | `src/components/CrewMemberDashboard.tsx:103` | `src/components/ProductVisualizer.tsx:77` | `src/components/patterns/PatternCard.tsx:37` | `src/components/MarqueeText.tsx:17` | `src/components/CommunityModal.tsx:33` | Audit: `R-22_findings.json â†’ MEM-001 through MEM-006`
  - **Goal:** Add `return () => loop.stop()` cleanup to 6 `Animated.loop().start()` useEffect calls that currently have no cleanup. Prevents background animation loops after component unmount.
  - **Details:** Identical single-line fix pattern across 6 files. Unified Batch Override authorized. CPU leak on every modal close/card unmount.

- [x] **`fix/pii-scrub-sweep`** ðŸš€ Merged in 1ecea5d6
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[AUTH]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Remove user account identifiers (email, display name, user IDs) from 4 AppLogger call sites. MAC addresses explicitly excluded â€” local-only telemetry, BLE controller MACs are not user-linkable.
  - **Decision Log:** R-09 found 23 raw-ID instances but the indefensible subset is 4: `UserManagementPanel.tsx:222` (full profile object with email+name), `CrewService.ts:375` (userId), `useCrewSession.ts:98` (userId), `DeviceRepository.ts:358` (user.id). MAC address scrubbing excluded by user decision 2026-06-08 â€” AppLogger is local-only and BLE controller MACs are not personally linkable. Evidence: `R-09_findings.json` (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-pii-scrub-sweep.md](docs/plans/PLAN-pii-scrub-sweep.md)
    Key finding: "4 indefensible PII leaks: 1 full profile object (email+name), 3 raw user IDs. The 19 MAC address instances are excluded per local-only telemetry decision."
    Rejected alternative: "Scrubbing all 23 instances including MACs â€” rejected 2026-06-08, AppLogger is local-only and BLE controller MACs are not user-linkable in practice."
  - **Source of Truth:** ðŸ“– [R-09_findings.json](artifacts/deepdive_raw/R-09_findings.json) Â· `src/components/admin/tools/UserManagementPanel.tsx:222` Â· `src/services/CrewService.ts:375` Â· `src/hooks/useCrewSession.ts:98` Â· `src/services/DeviceRepository.ts:358`
  - **Details:** 4 files, ~4 line changes total. `UserManagementPanel.tsx:222` â€” replace full `data` object log with `{ count: data.length }`. The 3 userId refs â€” remove from log payload (crew/session IDs provide sufficient debug context). Post-fix grep: `grep -rn "AppLogger" src/ | grep -E "(email|display_name|user\.id|userId)" | grep -v "MAC"` must return zero.

- [x] **`fix/re-entrancy-guards`** ðŸš€ Merged in bf1d1629
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[AUTH]` `[BLE]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Add `isActive` / `isRunningRef` re-entrancy guards to 5 confirmed async functions called from `useEffect` or event listeners without protection, preventing state corruption on rapid re-renders or concurrent deep links.
  - **Decision Log:** R-26 + DOMAIN_IDENTITY confirmed `useRegistration.ts:81`, `SkaterStatsPanel.tsx:24`, `AuthContext.tsx:102` (handleDeepLink) call async functions without guards â€” on rapid navigation or back-to-back deep links, multiple concurrent calls race each other. Evidence: `R-26_findings.json`, `DOMAIN_IDENTITY_findings.json` (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-re-entrancy-guards.md](docs/plans/PLAN-re-entrancy-guards.md)
    Key finding: "3 CONFIRMED instances in auth/identity layer. handleDeepLink in AuthContext is the highest risk â€” rapid deep links can corrupt auth state."
    Rejected alternative: "AbortController â€” rejected because these are not fetch calls; a simple boolean ref is cleaner and zero-dependency."
  - **Source of Truth:** ðŸ“– [R-26_findings.json](artifacts/deepdive_raw/R-26_findings.json) Â· [DOMAIN_IDENTITY_findings.json](artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json)
  - **Details:** Pattern: `let isActive = true; return () => { isActive = false; }` in useEffect. For event listeners: `useRef(false)` guard checked at function entry, cleared in finally. 3 files, ~15 lines of change total.

- [x] **`fix/auth-context-bypass`** ðŸš€ Merged in ac739bc6
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[AUTH]` `[M-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Route all 4 direct `supabase.auth.*` calls in UI components through `AuthContext` methods, enabling centralized auth testing and removing the scattered auth bypass smell.
  - **Decision Log:** R-15 + DOMAIN_IDENTITY confirmed `AuthFormSignIn.tsx:73`, `AuthFormSignUp.tsx:106`, `AuthFormForgotPassword.tsx:38`, `useDashboardProfile.ts:113` all bypass the `AuthContext` abstraction and call Supabase directly. This makes auth behavior untestable and creates split-brain risk if auth session handling changes. Evidence: `DOMAIN_IDENTITY_findings.json` lines 122â€“148 (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-auth-context-bypass-fix.md](docs/plans/PLAN-auth-context-bypass-fix.md)
    Key finding: "4 UI components bypass AuthContext for signIn, signUp, resetPassword, signOut. AuthContext already exists â€” these calls just aren't routed through it."
    Rejected alternative: "Creating a separate AuthService class â€” rejected because AuthContext already serves this purpose; adding another abstraction layer is unnecessary complexity."
  - **Source of Truth:** ðŸ“– [DOMAIN_IDENTITY_findings.json](artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json) Â· `src/context/AuthContext.tsx`
  - **Details:** May require adding `resetPassword` and `signUp` as exported methods on AuthContext if not already present. 4 files, small change each.

- [x] **`refactor/ble-delay-constants`** ðŸš€ Merged in d647af14
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[BLE]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Extract all hardcoded millisecond delay values in the BLE pipeline into named constants in `src/constants/bleTimingConstants.ts`, making timing tunable without hunting magic numbers.
  - **Decision Log:** R-16 sniper found ~85 setTimeout usages codebase-wide; after scrubbing UI debounce and tests, ~30 HIGH-risk instances remain in the BLE write pipeline with hardcoded ms values (`50`, `100`, `200`, `250`, `400`, `600`). These are undocumented and impossible to tune safely. Evidence: `R-16_findings.json` BLE service entries (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-ble-delay-constants.md](docs/plans/PLAN-ble-delay-constants.md)
    Key finding: "`BleConnectionManager.ts`, `BleWriteDispatcher.ts`, `BleSessionFactory.ts`, `BlePingService.ts`, `useBLEAutoRecovery.ts`, `BleLifecycleManager.ts` all contain magic ms numbers in BLE pipeline delays."
    Rejected alternative: "Configurable runtime delays â€” rejected because these are protocol-level timing constraints that should be compile-time constants, not runtime settings."
  - **Source of Truth:** ðŸ“– [R-16_findings.json](artifacts/deepdive_raw/R-16_findings.json) Â· `tools/ZENGGE_PROTOCOL_BIBLE.md`
  - **Details:** Create `BLE_TIMING` const object. 6 files touched. No logic changes â€” purely naming. Low execution risk despite H-RISK domain tag.
- [x] **\ix/error-handling-standardization\** ï¿½ Merged a171835: Standardized ~150 catch blocks with instanceof Error unwrapping.
  - **Tags:** \[? READY]\ \[?? INFERRED]\ \[Services]\ \[L-RISK]\ \[Feast]\ \[?? PRO-MED]\ \[BATCH:deepdive-synthesis-2026-06-08]\
  - **Goal:** Add \instanceof Error\ unwrapping to all ~190 catch blocks that pass raw \e: unknown\ directly to AppLogger, replacing \[object Object]\ telemetry with readable error messages.
  - **Decision Log:** R-06 sniper found 2130 raw entries (~190 unique) of \catch (e: unknown)\ blocks that log \e\ directly without unwrapping. This produces \[object Object]\ in production telemetry, making debugging impossible. The pattern \e instanceof Error ? e.message : String(e)\ is already used correctly in ~60% of the codebase ï¿½ the other 40% needs to catch up. Evidence: \R-06_findings.json\ (2026-06-08).


- [x] **`refactor/boolean-fsm-admin-tools`** ðŸš€ Merged in 07f94b36
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[UI]` `[L-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Collapse 11 independent boolean visibility states in `AdminToolsModal.tsx` into a single `activePanel: AdminPanel | null` union type, making panel navigation deterministic and eliminating impossible states.
  - **Decision Log:** R-18 sniper found `AdminToolsModal.tsx` declares 11 separate `isXVisible` booleans (lines 65â€“75). With 11 booleans there are 2^11 = 2048 possible states â€” only 12 are valid. This is a correctness hazard and a cognitive load tax. Evidence: `R-18_findings.json` lines 59â€“121 (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-boolean-fsm-admin-tools.md](docs/plans/PLAN-boolean-fsm-admin-tools.md)
    Key finding: "11 booleans â†’ 1 union type. DashboardScreen has 5 booleans but is a monolith (H-RISK) â€” AdminToolsModal is the safe Snack entry point for this pattern."
    Rejected alternative: "Applying FSM to all 16 affected files at once â€” rejected as too broad; start with AdminToolsModal as proof of pattern."
  - **Source of Truth:** ðŸ“– [R-18_findings.json](artifacts/deepdive_raw/R-18_findings.json) Â· `src/components/admin/AdminToolsModal.tsx:65-75`
  - **Details:** 1 file. Define `AdminPanel` union type, replace 11 useState with 1, update all call sites and conditional renders. ~30 lines changed.

- [x] **`fix/state-matrix-error-ui`** ðŸš€ Merged in 6e5e2601
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[UI]` `[M-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Add explicit Error UI state to ~18 data-driven components that silently render stale/empty data on fetch failure, completing the 4-state matrix (Loading, Error, Empty, Success).
  - **Decision Log:** R-14 + DOMAIN_IDENTITY confirmed `SkaterStatsPanel.tsx` swallows `fetchStats` errors and renders zero-value stats silently. `useScenes.ts` and `useGradients.ts` have no error state returned to consumers. Per Offline-First mandate (agent-behavior.md Â§4), all data views MUST implement the full 4-state matrix. Evidence: `R-14_findings.json`, `DOMAIN_IDENTITY_findings.json:116` (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-state-matrix-error-ui.md](docs/plans/PLAN-state-matrix-error-ui.md)
    Key finding: "~18 components missing error state. P0: SkaterStatsPanel (silently shows zeros), useScenes (empty list on failure), useGradients (no error feedback)."
    Rejected alternative: "Global error boundary only â€” rejected because per-component error states provide granular UX (retry button, specific message) that a generic boundary cannot."
  - **Source of Truth:** ðŸ“– [R-14_findings.json](artifacts/deepdive_raw/R-14_findings.json) Â· `agent-behavior.md Â§4`
  - **Details:** Add `error: string | null` state + retry callback to each affected hook. Consumers render `<ErrorState />`. Use existing error component if one exists.
### [BATCH:deepdive-synthesis-2026-06-08] â€” Priority-Ordered Sweep | From `/deepdive-code-synthesis`

> **Worktree**: Sequential execution â€” one task at a time. **Type**: Sequential. **Prerequisite**: None.
> **Source Analysis**: ðŸ“Š [system_audit_report.md](artifacts/system_audit_report.md) â€” 48-agent deep-dive, ~875 unique verified findings across 26 anti-pattern rules.

#### ~~ðŸ”´ CRITICAL~~ âŒ VOIDED â€” FALSE POSITIVE

- [x] ~~**`feat/pattern-engine-protocol-fix`**~~ â€” **CANCELLED: AUDIT AGENT MISINFORMATION**
  - **Tags:** `[âŒ VOIDED]` `[FALSE-POSITIVE]` `[BLE]` `[H-RISK]` `[Snack]`
  - **Plan:** ðŸ“Ž [PLAN-pattern-engine-protocol-fix.md](docs/plans/PLAN-pattern-engine-protocol-fix.md) *(INVALID â€” do not execute)*
  - **Void Reason (2026-06-08):** The audit agent (DOMAIN_PATTERN_ENGINE sniper) confused `setCustomModeExtendedCompact` with `setCustomModeExtended`. These are entirely different methods. `setCustomModeExtendedCompact` emits a **21-byte compact 0x51 packet** (direct GATT write, no chunking) â€” the 10th byte is the **direction flag**, intentionally included. Switching to `setCustomModeCompact` would **break direction support** for all 0x51 patterns. Hardware is functioning correctly. The `dir` field is required, not dead code. Evidence: `ZenggeProtocol.ts:736-771` (method JSDoc + implementation), `PatternEngine.ts:244` comment: *"Use the 10-byte unpadded compact payload so the direction flag is respected!"*

---

#### ðŸ”´ P1 â€” CRITICAL Security

- [x] **`fix/pii-scrubber-hardening`** â€” Merged 2924dce6: Expanded PII key coverage to location/auth patterns via substring matching, added array recursion to obfuscate, renamed LocationService labelâ†’address, removed hardcoded Maps API key from AndroidManifest.


---

#### ðŸŸ  P2 â€” High Correctness




---

#### ðŸŸ¡ P3 â€” Medium Priority / Architecture Hygiene


- [x] **`refactor/storage-key-registry-v2`** @ a2fdee53 - Standardized AsyncStorage keys across codebase and updated Master Reference.
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[Services]` `[M-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Fix 4 raw AsyncStorage string usages bypassing constants, and document 6+ undocumented keys in Master Reference Â§A.2, completing the storage key registry started by `fix/async-storage-key-registry`.
  - **Decision Log:** R-24 sniper found `AdminToolsModal.tsx:104` uses `'@Sk8lytz_device_configs'` as a raw string while `DeviceRepository.ts` uses `CONFIGS_KEY` constant for the same key. Same split in `useBLEScanner.ts`, `useAccountOverview.ts`, `AuthContext.tsx`. 6 keys used but absent from Master Reference Â§A.2. Prior merge (b707386d) fixed `ng_` namespace keys but missed these. Evidence: `R-24_findings.json` (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-storage-key-registry-v2.md](docs/plans/PLAN-storage-key-registry-v2.md)
    Key finding: "4 raw string key violations, 6 undocumented keys. Prior merge missed these secondary violations."
    Rejected alternative: "Automated key validation at runtime â€” rejected as over-engineering; static constants enforced at import time is sufficient."
  - **Source of Truth:** ðŸ“– [R-24_findings.json](artifacts/deepdive_raw/R-24_findings.json) Â· `src/constants/storageKeys.ts` Â· `tools/SK8Lytz_App_Master_Reference.md`
  - **Details:** 4 raw string fixes + Master Reference Â§A.2 update. Must update docs per VS-003 rule before running gatekeeper.



---

#### ðŸ”µ P4 â€” Type Safety Sweeps (Large Scope)

- [x] **`refactor/type-safety-data-layer`** @ 76ac0911 â€” Replaced all double-casts with `.returns<T>()` and strict guards
  - **Tags:** `[âœ… COMPLETED]` `[ðŸ¤” INFERRED]` `[Services]` `[M-RISK]` `[Meal]` `[ðŸ¤– THINK]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Fix `as unknown as` type laundering and `payload: any` in 6 core data layer service files by replacing unsafe double-casts with proper type guards or correctly typed Supabase queries.
  - **Decision Log:** DOMAIN_DATA_LAYER + R-08 confirmed 20+ type laundering instances. Fixed via strict Supabase interface alignment.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-type-safety-data-layer.md](docs/plans/PLAN-type-safety-data-layer.md)
  - **Source of Truth:** ðŸ“– [DOMAIN_DATA_LAYER_findings.json](artifacts/deepdive_raw/DOMAIN_DATA_LAYER_findings.json) Â· `src/types/supabase.ts`
  - **Details:** Prerequisite for `refactor/type-safety-ui-layer` is now cleared.

- [x] **`refactor/type-safety-ui-layer`** @ 38d792dd - Resolved prop `any` types across UI layer and stabilized display device mapping.
  - **Tags:** `[âœ… COMPLETED]` `[ðŸ¤” INFERRED]` `[UI]` `[M-RISK]` `[Feast]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Fix `: any` prop type annotations across Dashboard UI, Admin Diagnostic Lab tabs, and Docked components â€” replacing prop `any` with explicit types from the domain model.
  - **Decision Log:** R-08 sniper found ~380 total `any` instances across 253 files. UI layer accounts for ~180 of these, primarily in Admin Diagnostic Lab tab components (25) and Dashboard slabs (15). These are prop interfaces lacking domain type imports. Blocked by data layer task â€” service types must be clean first to avoid cascading errors. Evidence: `R-08_findings.json` (2026-06-08).
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-type-safety-ui-layer.md](docs/plans/PLAN-type-safety-ui-layer.md)
    Key finding: "UI layer `any` casts are primarily prop interface gaps â€” import the right type from `src/types/` and the fix is trivial."
    Rejected alternative: "One giant sweep of all 380 instances â€” rejected as too high collision risk. Sequential: data layer â†’ UI layer."
  - **Source of Truth:** ðŸ“– [R-08_findings.json](artifacts/deepdive_raw/R-08_findings.json) Â· `src/types/`
  - **Details:** Execute in 4 sub-batches (Diagnostic Lab tabs â†’ Dashboard slabs â†’ Docked â†’ Remaining). Commit + TSC check after each sub-batch. **Blocked by `refactor/type-safety-data-layer`.**

---


- [x] **`fix/audit-fixes-ble-signal`** `[MERGE: 54cc1111]`
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[BLE]` `[M-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:audit-fixes-ble-signal]`
  - **Goal:** Fix 5 BLE signal/state bugs: fire FSM `CONNECT_SUCCESS`/`DISCONNECT_COMPLETE`, fix RSSI stale prune dep, wire `onWeakSignal`, correct power opcode `0xCCâ†’0x71`, remove duplicate `bleGateState` dep.
  - **Decision Log (2026-06-09):** BleMachine defines `CONNECT_SUCCESS` and `DISCONNECT_COMPLETE` events that are never fired by `BleConnectionManager` or `BleLifecycleManager`. FSM transitions `IDLEâ†’CONNECTINGâ†’IDLE` only â€” `READY` and `DISCONNECTING` states are unreachable. `derivedBleState` in `useBLE.ts:602-606` compensates by reading `connectedDevices.length` directly, masking the bug today. `BleWriteQueue.ts:52` maps `0xCC` as power opcode â€” Zengge's actual power opcode is `0x71` (ZenggeProtocol.ts:829). Power writes dispatched as `normal` priority instead of `critical`. `useBLERSSIMonitor.ts:134` prune depends on `[bleManager]` which never changes â€” stale RSSI persists post-disconnect.
  - **Analysis:** ðŸ“Š Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) Â· Plan: [PLAN-audit-fixes-ble-signal.md](docs/plans/PLAN-audit-fixes-ble-signal.md)
    Key finding: "FSM READY state unreachable â€” any future FSM subscriber will never see CONNECTED. Power opcode 0xCC never emitted â€” power writes run at normal not critical priority."
    Rejected alternative: "Removing the READY/DISCONNECTING states from BleMachine entirely â€” rejected because they are part of the documented architecture and future UI consumers (connection animations) will need them."
  - **Source of Truth:** ðŸ“– [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) Â· [BleConnectionManager.ts:295-310](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L295-L310) Â· [BleWriteQueue.ts:52](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts#L52) Â· [ZenggeProtocol.ts:829](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts#L829) Â· [useBLERSSIMonitor.ts:120-134](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLERSSIMonitor.ts#L120-L134) Â· [useBLE.ts:466-476,667,673](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L466-L476)
  - **Details:** 5 atomic surgical fixes. `bleSend` must be added to `BleConnectionRequest` type â€” check first if already there. `connectedDeviceIds: string[]` added to `UseBLERSSIMonitorParams` â€” passed from useBLE call site. Changing `0xCCâ†’0x71` promotes power writes from `normalâ†’critical` â€” this is **intentionally correct** (power should preempt pattern writes). `onWeakSignal` wired to AppLogger only, not auto-recovery â€” weak signal is informational not critical.

- [x] **`fix/audit-fixes-scanner`** 6d5f9130 - Fixed scanner typescript issues, added double-start guard, and surfaced low battery warning to UI.
  - **Tags:** `[âœ… MERGED]` `[âœ… VERIFIED]` `[BLE]` `[M-RISK]` `[Meal]` `[ðŸ§  PRO-MED]` `[BATCH:audit-fixes-scanner]`
  - **Goal:** Type `BleLifecycleManager` signature, surface battery-constrained warning to UI, add scan double-start guard, remove `as unknown as any` cast in DashboardScreen.
  - **Decision Log (2026-06-09):** `BleLifecycleManager.ts:11-18` is fully `any`-typed â€” `cancelAllRecoveries()` call on `autoRecovery: any` is not TS-verified. `useBLEBatterySweep.ts:69-72`: battery < 15% causes silent sweeper shutdown with no UI feedback â€” 1500ms auto-connect races battery check and waits for devices that never appear. `useBLEScanner.ts:299` calls `startDeviceScan` without checking if scan is already active (no double-start guard). `DashboardScreen.tsx:1134` has `as unknown as any` cast â€” bypasses type system in the most complex file in the codebase.
  - **Analysis:** ðŸ“Š Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) Â· Plan: [PLAN-audit-fixes-scanner.md](docs/plans/PLAN-audit-fixes-scanner.md)
    Key finding: "Battery PAUSED path silently returns without notifying UI â€” user sees 0 devices and no explanation. BleLifecycleManager.ts is fully any-typed â€” `cancelAllRecoveries()` TS-unverified."
    Rejected alternative: "Showing a modal instead of a banner for battery warning â€” rejected because a modal blocks the UI and users need to still navigate while constrained."
  - **Source of Truth:** ðŸ“– [BleLifecycleManager.ts:11-18](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleLifecycleManager.ts#L11-L18) Â· [useBLEBatterySweep.ts:65-72](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts#L65-L72) Â· [useBLEScanner.ts:299](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L299) Â· [DashboardScreen.tsx:1134](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L1134) Â· [IControllerProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/IControllerProtocol.ts)
  - **Details:** God Object lock applies to DashboardScreen â€” ONLY touch line 1134 (cast fix) and the 2-line banner conditional. No other changes in DashboardScreen. `BleLifecycleManager` type fixes require `BleManager` import from `react-native-ble-plx` â€” check if already imported before adding. Battery banner must use existing StyleSheet pattern from DashboardScreen.

- [x] **`fix/re-entrancy-guards-phase-2`** `[MERGE: 39490c68]` â€” Added useRef boolean gates to checkAutoPause, syncSessionState, pollHealthData, and scan.
  - **Tags:** `[âšª TRIAGE]` `[ðŸ¤” INFERRED]` `[Services]` `[H-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]`
  - **Goal:** Add re-entrancy guards to 4 remaining R-26 instances NOT covered in phase 1: `SessionContext.tsx` (checkAutoPause GPS flood + syncSessionState AppState race), `DashboardScreen.tsx` (checkNewDevice â€” monolith, extract first), `useHealthTelemetry.ts` (setInterval pattern), `useCrewProximityRadar.ts` (partially guarded).
  - **Decision Log (2026-06-08):** These were found in R-26 during fix/re-entrancy-guards execution. DashboardScreen is blocked by monolith extraction (>30KB rule). SessionContext is high-risk â€” requires spike to understand GPS speed dependency chain before touching. Filed to TRIAGE per zero-collateral-damage rule.
  - **Source of Truth:** ðŸ“– [R-26_findings.json](artifacts/deepdive_raw/R-26_findings.json) entries 2â€“5

- [x] **`fix/triage-ble-buffer-lockout`** `[MERGE: 3b9eca9f]`
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[BLE]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-HIGH]` `[BATCH:triage-sweep]`
  - **Goal:** Enforce 12-pixel minimum length constraint for 0x59 payloads to prevent A3 chipset EEPROM buffer lockouts.
  - **Decision Log (2026-06-08):** Flagged during deep-dive synthesis: payloads below 10 pixels lock the A3 buffer.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-triage-ble-buffer-lockout.md](docs/plans/PLAN-triage-ble-buffer-lockout.md)
    Key finding: "BleWriteDispatcher.ts fails to enforce a minimum length constraint for 0x59 payloads."
    Rejected alternative: "Modifying the firmware â€” rejected as it's outside our control; client must pad."
  - **Source of Truth:** ðŸ“– `.agents/rules/agent-behavior.md` Rule 10
  - **Details:** Must pad 0x59 payloads < 12 pixels.

- [x] **`cloud/triage-cloud-security`** `[MERGE: e38ca42f]`
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:triage-sweep]`
  - **Goal:** Fix IDOR/Privilege Escalation edge function vulnerability and Search Path Hijacking in migrations.
  - **Decision Log (2026-06-08):** Flagged during deep-dive synthesis: SECURITY DEFINER functions missing path resets.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-triage-cloud-security.md](docs/plans/PLAN-triage-cloud-security.md)
    Key finding: "Multiple SECURITY DEFINER functions omit SET search_path = public."
    Rejected alternative: "Dropping the functions â€” rejected because they are required for admin operations."
  - **Source of Truth:** ðŸ“– `tools/SK8Lytz_App_Master_Reference.md` Â§ Supabase Security Rules
  - **Details:** Fix migrations and edge function authorization checks.

- [x] **`refactor/triage-type-safety`**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[UI]` `[L-RISK]` `[Batch]` `[ðŸ¤– PRO-MED]` `[BATCH:triage-sweep]`
  - **Goal:** Replace dangerous `any` casts with `unknown` or specific interfaces across UI and BLE hooks.
  - **Decision Log (2026-06-08):** Flagged during deep-dive synthesis: multiple direct `any` bypasses in core components.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](artifacts/system_audit_report.md) Â· Plan: [PLAN-triage-type-safety.md](docs/plans/PLAN-triage-type-safety.md)
    Key finding: "Strict type violations (any) used extensively to bypass TS compilation errors."
    Rejected alternative: "Using @ts-ignore â€” rejected as it violates the same rule."
  - **Source of Truth:** ðŸ“– `.agents/rules/prime-directive.md` S3
  - **Details:** Surgical replacements only. Do not refactor actual logic.

- [x] **`fix/wizard-ftue-scan`** `[MERGE: 54cc1111]`
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[BLE]` `[H-RISK]` `[Snack]` `[ðŸ¤– PRO-HIGH]`
  - **Goal:** Fix the async sweeper race that leaves new users stuck on "Scanning for devices..." forever in the Hardware Setup Wizard â€” app is non-functional for all new installs.
  - **Decision Log (2026-06-09):** Confirmed Strike 2 on this bug. Previous Fix 1 patched 5sâ†’10s timeout. Previous Fix 2 adjusted RSSI. Neither fixed the root cause: `startSweeper()` is async (calls `Battery.getBatteryLevelAsync()`), so `isSweeperActive` is still `false` when the wizard synchronously calls `scanForPeripherals()`. Wizard hits the raw 5s scan â†’ hard stop â†’ no retry on step 1 â†’ infinite "Scanning..." state. Industry standard (Govee, LIFX, Hue, Sonos): discovery is a service-level concern, wizard is a passive subscriber. Fix routes FTUE through `startSweeper()` directly, bypassing the race.
  - **Analysis:** ðŸ“Š Source: Direct read 2026-06-09 Â· Plan: [PLAN-wizard-ftue-scan.md](docs/plans/PLAN-wizard-ftue-scan.md)
    Key finding: "`scanForPeripherals()` checks `isSweeperActive` synchronously but `startSweeper()` resolves async (battery check). Race = 5s raw scan + hard stop + no step-1 keepAlive retry = wizard stuck forever."
    Rejected alternatives: "Increase timeout (Fix 1 â€” failed). Adjust RSSI (Fix 2 â€” failed). Both missed the async race root cause."
  - **Source of Truth:** ðŸ“– [useBLEScanner.ts:291-307](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L291-L307) Â· [useBLEBatterySweep.ts:65](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts#L65) Â· [HardwareSetupWizardScreen.tsx:113](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L113)
  - **Details:** 2-file surgical fix. `useBLEScanner.ts`: add `registeredMacs.length === 0` FTUE branch that calls `startSweeper()` and returns immediately â€” eliminates the race entirely. `HardwareSetupWizardScreen.tsx`: change `step === 2` to `(step === 1 || step === 2)` in keepAlive gate â€” belt-and-suspenders fallback ensures retry fires even when stuck on step 1. **âš ï¸ Three-Strike rule active â€” verify `startSweeper()` is idempotent in Step 3 of the plan BEFORE writing any code.**

- [x] **`fix/audit-fixes-auth`** - merged @ e732f8f (Offline rpc crash fixed)
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[Auth]` `[H-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:audit-fixes-auth]`
  - **Goal:** Fix 4 confirmed auth path bugs: offline `rpc` crash (HIGH), silent profile error swallowing (MEDIUM), dead `safeErr` variables (LOW), duplicated ternary dead code (LOW).
  - **Decision Log (2026-06-09):** Offline stub at `supabaseClient.ts:48-64` has no `rpc` method. `AuthFormSignIn.tsx:57` calls `supabase.rpc('get_email_by_username', ...)` for username login â€” throws `TypeError: supabase.rpc is not a function` in offline mode. Confirmed by reading both files in the 2026-06-09 audit. Highest priority auth finding.
  - **Analysis:** ðŸ“Š Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) Â· Plan: [PLAN-audit-fixes-auth.md](docs/plans/PLAN-audit-fixes-auth.md)
    Key finding: "Offline stub missing `rpc` method â€” username login crashes in offline mode (H1). AuthProfileService.ts:79 comment acknowledges missing AppLogger call (M5)."
    Rejected alternative: "Null-checking the rpc call site â€” rejected because every future offline rpc call would also need a guard; fixing the stub is the single-source fix."
  - **Source of Truth:** ðŸ“– [supabaseClient.ts:48-64](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/supabaseClient.ts#L48-L64) Â· [AuthFormSignIn.tsx:57](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignIn.tsx#L57) Â· [AuthProfileService.ts:77-82](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts#L77-L82) Â· [SessionContext.tsx:44,323,401](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L44)
  - **Details:** 4 atomic fixes in one worktree. Step 1 (H1 â€” add `rpc` stub) is the only user-facing crash risk. Steps 2-4 are Boy Scout cleanups. No changes outside auth domain. `DashboardScreen.tsx` is explicitly out of scope (God Object lock).

- [x] **`fix/audit-fixes-ble-protocol`** @ 3af6b482 - UI dispatches now route through the proper device adapter
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[BLE]` `[H-RISK]` `[Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:audit-fixes-ble-protocol]`
  - **Goal:** Route pattern dispatch through the per-device `IControllerProtocol` adapter so BanlanX devices in mixed groups receive correct protocol bytes instead of Zengge 0x59 packets.
  - **Decision Log (2026-06-09):** `useControllerDispatch.ts:17-21` imports and calls `ZenggeProtocol.setMultiColor()` directly. Comment at lines 10-11 explicitly bans calling `useProtocolDispatch()` (orphan BLE instance risk). `PatternEngine.ts:192` also calls Zengge static. BLE Payload Dispatch auditor confirmed: solo BanlanX âœ…, solo Zengge âœ…, mixed group âŒ. Fix routes through `adapterMapRef` (already populated at connection time by `createGattSession` â€” same map used by BleWriteDispatcher.ts:143).
  - **Analysis:** ðŸ“Š Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) Â· Plan: [PLAN-audit-fixes-ble-protocol.md](docs/plans/PLAN-audit-fixes-ble-protocol.md)
    Key finding: "Two parallel dispatch chains exist. Chain B (DockedController â†’ useControllerDispatch) bypasses IControllerProtocol adapter entirely â€” BanlanX in mixed groups receives Zengge 0x59 packets."
    Rejected alternative: "Calling useProtocolDispatch() from useControllerDispatch â€” banned by comment at useControllerDispatch.ts:10-11 (creates orphan useBLE instance). Rejected."
  - **Source of Truth:** ðŸ“– [useControllerDispatch.ts:17-21](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts#L17-L21) Â· [PatternEngine.ts:192](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/PatternEngine.ts#L192) Â· [IControllerProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/IControllerProtocol.ts) Â· [BleWriteDispatcher.ts:143](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts#L143)
  - **Details:** Adds `getAdapterForDevice` + `primaryDeviceId` to `UseControllerDispatchParams`. Adds optional `protocol` param to `buildPatternPayload` with Zengge as default (zero regression for existing solo-Zengge users). `DockedController.tsx` changes are surgical â€” only the `useControllerDispatch` call site (lines 551-560). God Object lock still applies â€” no other changes in that file.

- [x] **`feat/restore-virtual-skates`** â€” merged @ 0aca27c1 (restored virtual skates DI)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[BLE]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ¤– PRO-HIGH]` `[BATCH:virtual-skates]`
  - **Goal:** Restore the Dev Sandbox Virtual Skates by injecting Mock devices into the BLE Scanner engine.
  - **Decision Log:** The UI toggle correctly sets the DEMO flag, but the scanner engine currently drops the connection loop because it never intercepts the physical scan. This restores local testing velocity without actual hardware.
  - **Analysis:** ðŸ“Š Plan: [PLAN-restore-virtual-skates.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/plans/PLAN-restore-virtual-skates.md)
    Key finding: "The previous implementation set STORAGE_DEMO_HALO/SOUL but the scanner never read them. We will unify under STORAGE_DEMO_MODE."
    Rejected alternative: "Mocking the BleManager library completely via jest.mock â€” rejected because it breaks the iOS build and doesn't run in the actual Expo Go app."
  - **Source of Truth:** ðŸ“– `src/hooks/ble/useBLEScanner.ts`
  - **Details:** Must strictly enforce `__DEV__` gating. Use Dependency Injection in the scanner to yield fake Device objects instead of triggering the Bluetooth radio.

- [x] **`bug/split-brain-telemetry-drop`**
  - **Tags:** `[âšª TRIAGE]` `[âŒ UNVERIFIED]` `[BLE]` `[M-RISK]` `[Snack]` `[ðŸ¤– PRO-HIGH]`
  - **Goal:** Fix `DeviceRepository.saveDevice` to properly capture and persist `ble_version`, `factory_name`, and `manufacturer_data` to `registered_devices` during the registration flow instead of hardcoding `null`.
  - **Decision Log (2026-06-09):** The global admin dashboard revealed that `registered_devices` drops BLE chipset version data. A spike confirmed that the BLE scanner captures it, but `DeviceRepository.ts` strips it.
  - **Plan:** ðŸ“Ž [PLAN-split-brain-telemetry-drop.md](docs/plans/PLAN-split-brain-telemetry-drop.md)
  - **Source of Truth:** ðŸ“– `src/services/DeviceRepository.ts`
  - **Details:** Revert dashboard join workarounds and fix the root cause in the mobile app persistence layer.

- [x] **`fix/db-hygiene-batch`**
  - **Tags:** `[ðŸš§ ACTIVE]` `[âœ… VERIFIED]` `[CLOUD]` `[M-RISK]` `[Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:db-hygiene]`
  - **Goal:** Fix 5 orphaned database fields (presets updated_at, scanner is_claimed, remove active_calories, crew role mapping, crew avatar URL).
  - **Decision Log:** See PLAN-db-hygiene-batch.md for BLE deduplication logic and health metrics calories mapping rules.
  - **Source of Truth:** ðŸ“– `src/types/supabase.ts`
  - **Details:** Updating payloads across multiple services to achieve 1:1 parity with database schema.

- [x] **`fix/db-telemetry-drift`** 2026-06-09 merged dual-write and drift fix
  - **Tags:** `[ðŸ“¦ ARCHIVE]` `[âœ… VERIFIED]` `[CLOUD/BLE]` `[H-RISK]` `[Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:telemetry-drift]`
  - **Goal:** Dual-write crash logs, fix location data black hole, and fix solo lifetime stats drift.
  - **Decision Log:** Dual-write pattern approved to safely hydrate crash_telemetry without breaking legacy dashboards.
  - **Source of Truth:** ðŸ“– `src/services/AppLogger.ts`, `src/services/SpeedTrackingService.ts`
  - **Details:** Worktree active. See PLAN-db-telemetry-fix.md for payload mapping.

- [x] **`fix/eeprom-product-confirm`**
  - **Tags:** `[ðŸ”¥ ON DECK]` `[âŒ UNVERIFIED]` `[BLE/DB]` `[M-RISK]` `[Snack]` `[ðŸ¤– PRO-LOW]` `[BATCH:db-hygiene]`
  - **Goal:** Ensure `product_id_confirmed_at` is populated correctly.
  - **Decision Log:** EEPROM 0x63 responses successfully identify product IDs but fail to persist the confirmation back to registered_devices. This task wires that persistence.
  - **Source of Truth:** ðŸ“– `src/hooks/useControllerDispatch.ts`, `src/services/DeviceRepository.ts`

- [x] **`feat/map-relational-drilldown`** ðŸš€ Merged in 2188ff2a
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[UI/CLOUD]` `[M-RISK]` `[Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:fleet-map-ops]`
  - **Goal:** Transform the Command Center map into a lazy-loaded relational inspector with spider-web vectors, Crew Zones, and Supabase Realtime telemetry firehose.
  - **Decision Log:** Brainstorming session confirmed the user wants interactive relational drill-downs connecting users to hardware/crews visually, plus live moving telemetry via WebSockets. Historical polylines were rejected as too invasive.
  - **Analysis:** ðŸ“Š Plan: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/26f16420-6723-4436-a1a4-3544dd1b501f/implementation_plan.md)
    Key finding: "We must use lazy-loaded Supabase queries on click to prevent overwhelming the browser with the full relational graph."
    Rejected alternative: "Showing historical skate paths (polylines) â€” rejected due to privacy/invasiveness."
  - **Source of Truth:** ðŸ“– `src/types/supabase.ts` & `MapWidget.tsx`
  - **Details:** Must implement Supabase `.channel()` for real-time `telemetry_snapshots` inserts to move pins without refresh.

- [x] **`feat/live-debugger-suite`**
  - **Tags:** `[? READY]` `[? VERIFIED]` `[UI/DB]` `[M-RISK]` `[Meal]` `[?? PRO-MED]` `[BATCH:live-debugger-suite]`
  - **Goal:** Evolve the Live Debugger into a 3-tab Diagnostic Suite (Live Stream, Crash Autopsy, Non-Fatal Diagnostics) with alerting and grouping.
  - **Decision Log:** The initial Live Debugger is a basic feed; user requires a robust interface to triage, group, and resolve errors (90-day retention, simple DB status updates) using crash and error telemetry.
  - **Analysis:** ?? Plan: [PLAN-live-debugger-suite.md](../../docs/plans/PLAN-live-debugger-suite.md)
  - **Source of Truth:** ?? `src/components/widgets/LiveDebuggerWidget.tsx`, `crash_telemetry`, `telemetry_errors`
  - **Details:** Requires creating a Supabase View `view_crash_aggregates`, updating `LiveDebuggerWidget.tsx` with AG Grid, and adding specific highlighting for spike thresholds (>10 crashes/hour).

- [x] **`feat/auto-factory-tagging`**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[BLE]` `[âœ… L-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:ble-discovery]`
  - **Goal:** Add BLE signature fingerprinting to `useBLEScanner.ts` to automatically populate the `factory_name` property during discovery.
  - **Decision Log:** User noted `manufacturer_data` is a raw Base64 string and asked for "Zengge" or "BanlanX" mapping. We need to infer `factory_name` natively on the scanner layer.
  - **Analysis:** ðŸ“Š Plan: [PLAN-auto-factory-tagging.md](../../docs/plans/PLAN-auto-factory-tagging.md)
    Key finding: "Zengge and BanlanX emit specific service UUIDs (FFE5 vs FFE0+0x5053) which we can map to factory_name."
    Rejected alternative: "Storing the raw base64 string in factory_name â€” rejected because it is not human-readable and violates DB schema intent."
  - **Source of Truth:** ðŸ“– `src/hooks/ble/useBLEScanner.ts`
  - **Details:** Must parse `device.serviceUUIDs` and `device.manufacturerData` to determine the brand before yielding to the UI state.
### âš¡ [BATCH:fleet-map-ops] â€” `feat/map-relational-drilldown` â€” [âœ… READY]
> **Worktree**: `feat/map-relational-drilldown` Â· **Type**: Standalone Â· **Prerequisite**: None
> **Source Analysis**: ðŸ“Š [SESSION_LOG.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SESSION_LOG.md) â€” Map transitioning to a relational inspector with realtime telemetry.

- [x] **`feat/docker-web-demo`**
  - **Tags:** `[ðŸš§ ACTIVE]` `[âœ… VERIFIED]` `[TOOLING]` `[L-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]` `[BATCH:docker-web-demo]`
  - **Goal:** Containerize the Expo Web Demo using Docker to resolve manual boot friction.
  - **Decision Log:** User experienced persistent friction restarting the web demo manually on port 8081. Transitioning to a Docker service within the existing `docker-compose.yml` stack to match Command Center/Scraper behavior.
  - **Source of Truth:** ðŸ“– `docker-compose.yml`, `Dockerfile.web` (to be created)

- [x] **`refactor/ble-p3-connect-service`** â€” merged `e92c63c6`
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[BLE]` `[M-RISK]` `[Meal]` `[ðŸ§  HIGH]` `[BATCH:ble-xstate-pipeline]`
  - **Goal:** Move GATT connection pipeline into a `fromPromise` XState service invoked by CONNECTING state. `BleConnectionManager.ts` deleted. Machine owns full connect lifecycle.
  - **Decision Log (2026-06-10):** Audit found 7 call sites of `connectToDevices` in `DashboardScreen.tsx` (lines 543, 574, 596, 611, 645, 845, 1195) â€” all routing through `BleConnectionManager.ts:42`. Manual `CONNECT_SUCCESS`/`CONNECT_FAIL` bleSend calls require the caller to correctly bracket the state machine, which it doesn't always do under async pressure. XState `invoke.onDone`/`onError` is atomic.
  - **Analysis:** ðŸ“Š Source: [BLE_AUDIT_REPORT.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_REPORT.md) Â§3 Async Path Map Â· Plan: [PLAN-ble-p3-connect-service.md](./plans/PLAN-ble-p3-connect-service.md)
    Key finding: "Device Connection entry trigger `BleConnectionManager.ts:42` â€” exit cleanup `BleConnectionManager.ts:88` â€” no abort signal propagated to intermediate GATT awaits"
    Rejected alternative: "Fix cancellation in BleConnectionManager â€” rejected because the root problem is that the machine does not own the lifecycle; fixing the manager leaves the structural gap"
  - **Source of Truth:** ðŸ“– [BleConnectionManager.ts:42-88](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L42) Â· [BleSessionFactory.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleSessionFactory.ts)
  - **Details:** Pure GATT helpers (GATT 133 retry, MTU negotiation, handshake) move to `ConnectService.ts` or `BleSessionFactory.ts`. Double-tap gating is now free â€” machine in CONNECTING ignores CONNECT_REQUEST.

- [x] **fix/ble-t2-static-guards** â€” Add BLE architecture invariant guards to verifiable-check-runner ðŸš€ Merged in bdcfd9f8
  - **Tags:** [âœ… READY] [âœ… VERIFIED] [TOOLING] [L-RISK] [Snack] [ðŸ¤– PRO-LOW] [BATCH:ble-test-hardening]
  - **Goal:** Add 2 new static checks to `tools/verifiable-check-runner.js`: (1) detect `bleManager.startDeviceScan()` calls outside BleMachine.ts, (2) detect `onOrganicDisconnect` missing from useBLE.ts machine input.
  - **Decision Log (2026-06-10):** The dual-scan bug and the organic disconnect bug were both invisible to the test suite. Static AST/grep guards at the gate level catch these at commit time, not at runtime during a skate session.
  - **Analysis:** ðŸ“Š Source: [ble_test_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a20aff2e-be9b-43b9-b217-269aa20e5f8a/ble_test_gap_analysis.md) â€” "Missing Static git gate" section
  - **Plan:** ðŸ“Ž [PLAN-ble-t2-static-guards.md](docs/plans/PLAN-ble-t2-static-guards.md)
  - **Source of Truth:** ðŸ“– `tools/verifiable-check-runner.js` Â· `src/services/ble/BleMachine.ts` Â· `src/hooks/useBLE.ts`
  - **Details:** Both guards must pass on current codebase AND fail when the pattern is violated. Snack-sized â€” pure JS additions to existing check runner, no new files needed.

- [x] **test/ble-t1-machine-tests** â€” BleMachine state machine unit tests ðŸš€ Merged in 2b1226a8
  - **Tags:** [âœ… READY] [âœ… VERIFIED] [BLE] [M-RISK] [Meal] [ðŸ§  MED] [BATCH:ble-test-hardening]
  - **Goal:** Write comprehensive unit tests for all 6 BleMachine state transitions + the organic disconnect â†’ RECOVERY_START wiring. This test is the regression guard for the HIGH-severity silent bug fixed in 2276ac8a.
  - **Decision Log (2026-06-10):** Audit found zero tests for BleMachine.ts. The organic disconnect fix has no guard â€” any future refactor of useBLE.ts input construction silently breaks recovery again with a green test suite. Filed as P0.
  - **Analysis:** ðŸ“Š Source: [BLE_AUDIT_2/01_bleMachine.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/01_bleMachine.md) Â· [SESSION_LOG.md DECISION 2026-06-10T08:38](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SESSION_LOG.md)
  - **Plan:** ðŸ“Ž [PLAN-ble-t1-machine-tests.md](docs/plans/PLAN-ble-t1-machine-tests.md)
  - **Source of Truth:** ðŸ“– `src/services/ble/BleMachine.ts` Â· `src/hooks/useBLE.ts:L182-187`
  - **Details:** 18 test cases across 3 groups: state transitions, organic disconnect regression guard, context assertions. Use XState `createActor` with mocked service stubs.

- [x] **test/ble-t3-connect-service-tests** â€” ConnectService unit tests ðŸš€ Merged in 43377f8c
  - **Tags:** [âœ… READY] [âœ… VERIFIED] [BLE] [M-RISK] [Meal] [ðŸ§  MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for ConnectService.ts covering group connect, GATT 133 retry, stale device flush, MTU negotiation, adapter mapping, notification registration, and the onOrganicDisconnect disconnect subscription.
  - **Decision Log (2026-06-10):** Post-migration audit confirmed zero tests for ConnectService. This is the most complex actor (288 lines, 3 retry loops, MTU negotiation, multi-device sequential flow). Any regression is invisible until hardware fails in the field.
  - **Analysis:** ðŸ“Š Source: [BLE_AUDIT_2/02_connectService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/02_connectService.md)
  - **Plan:** ðŸ“Ž [PLAN-ble-t3-connect-service-tests.md](docs/plans/PLAN-ble-t3-connect-service-tests.md)
  - **Source of Truth:** ðŸ“– `src/services/ble/ConnectService.ts` Â· `tools/BLE_AUDIT_2/02_connectService.md`
  - **Details:** 18 test cases across 7 groups. Mock `bleManager`, `createGattSession`, and capture `onDeviceDisconnected` callback to verify `onOrganicDisconnect` fires.

- [x] **test/ble-t4-recovery-service-tests** â€” RecoveryService unit tests
  - **Tags:** [âœ… READY] [âœ… VERIFIED] [BLE] [M-RISK] [Meal] [ðŸ§  MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for RecoveryService.ts covering Phase 1/2/3 backoff, clearWriteQueue regression guard, RECOVERY_COMPLETE/FAIL events, and re-registration of notifications + adapter after reconnect.
  - **Decision Log (2026-06-10):** The clearWriteQueue() fix (2276ac8a) also has no regression guard. The 3-phase recovery loop is the most complex async flow in the codebase â€” zero tests means any breakage is silent.
  - **Analysis:** ðŸ“Š Source: [BLE_AUDIT_2/03_recoveryService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/03_recoveryService.md)
  - **Plan:** ðŸ“Ž [PLAN-ble-t4-recovery-service-tests.md](docs/plans/PLAN-ble-t4-recovery-service-tests.md)
  - **Source of Truth:** ðŸ“– `src/services/ble/RecoveryService.ts` Â· `tools/BLE_AUDIT_2/03_recoveryService.md`
  - **Details:** 13 test cases across 5 groups. Use `jest.useFakeTimers()` to fast-forward backoff delays. Assert `clearWriteQueue` called as first action.

- [x] **test/ble-t5-heartbeat-service-tests** â€” HeartbeatService unit tests
  - **Tags:** [âœ… READY] [âœ… VERIFIED] [BLE] [L-RISK] [Meal] [ðŸ§  MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for HeartbeatService.ts covering 45s tick, exact 0x63 opcode bytes, HEARTBEAT_FAIL event, RSSI fallback path, multi-device sequential loop, and clearInterval cleanup.
  - **Decision Log (2026-06-10):** HeartbeatService is the liveness detector for connected devices. A broken heartbeat means dead skates that appear connected. Zero tests.
  - **Analysis:** ðŸ“Š Source: [BLE_AUDIT_2/04_heartbeatService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/04_heartbeatService.md)
  - **Plan:** ðŸ“Ž [PLAN-ble-t5-heartbeat-service-tests.md](docs/plans/PLAN-ble-t5-heartbeat-service-tests.md)
  - **Source of Truth:** ðŸ“– `src/services/ble/HeartbeatService.ts` Â· `tools/BLE_AUDIT_2/04_heartbeatService.md`
  - **Details:** 12 test cases across 6 groups. Verify exact opcode bytes [0x63, 0x12, 0x21, 0x0F, 0xA5] from audit. Use fake timers.

- [x] **test/ble-t6-interrogator-service-tests** â€” InterrogatorService unit tests
  - **Tags:** [âœ… READY] [âœ… VERIFIED] [BLE] [L-RISK] [Meal] [ðŸ§  MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for InterrogatorService.ts covering probe lifecycle, cancelDeviceConnection in finally (both success and failure paths), FTUE vs standard queue delay, AsyncStorage parse error resilience.
  - **Decision Log (2026-06-10):** InterrogatorService reads hardware EEPROM on first contact â€” wrong behavior here means wrong LED counts and broken color patterns for the user's lifetime. Zero tests.
  - **Analysis:** ðŸ“Š Source: [BLE_AUDIT_2/06_interrogatorService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/06_interrogatorService.md)
  - **Plan:** ðŸ“Ž [PLAN-ble-t6-interrogator-service-tests.md](docs/plans/PLAN-ble-t6-interrogator-service-tests.md)
  - **Source of Truth:** ðŸ“– `src/services/ble/InterrogatorService.ts` Â· `tools/BLE_AUDIT_2/06_interrogatorService.md`
  - **Details:** 12 test cases across 5 groups. Mock AsyncStorage, createGattSession, enqueueWrite. Verify `cancelDeviceConnection` called in finally on both success AND failure paths.

- [x] **fix/street-mode-accelerometer** ðŸš€ Merged in 92a3b893
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [CORE] [M-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:group-concurrency-sweep]
  - **Goal:** Fix accelerometer race condition missing `useRef` flag for street mode triggers.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster F).
  - **Plan:** ðŸ“Ž [PLAN-street-mode-accelerometer.md](docs/plans/PLAN-street-mode-accelerometer.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-10)

- [x] **fix/session-notifee-listener** ðŸš€ Merged in 92a3b893
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [CORE] [M-RISK] [Meal] [ðŸ¤– PRO-MED] [BATCH:group-concurrency-sweep]
  - **Goal:** Implement cleanup pattern for Notifee event listeners to prevent duplicate background alerts.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster F).
  - **Plan:** ðŸ“Ž [PLAN-session-notifee-listener.md](docs/plans/PLAN-session-notifee-listener.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-10)

- [x] **fix/memory-leak-hardware-notifications**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [CORE] [L-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:memory-leak-sweep]
  - **Goal:** Add cleanup function for BLEContext hardware notifications to prevent listener duplication.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster D).
  - **Plan:** ðŸ“Ž [PLAN-memory-leak-hardware-notifications.md](docs/plans/PLAN-memory-leak-hardware-notifications.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-17)

- [x] **fix/memory-leak-scanner-timers**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [CORE] [L-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:memory-leak-sweep]
  - **Goal:** Clear timeout refs on unmount to prevent setting state on unmounted scanner hooks.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster D).
  - **Plan:** ðŸ“Ž [PLAN-memory-leak-scanner-timers.md](docs/plans/PLAN-memory-leak-scanner-timers.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-12, R-17)

- [x] **fix/telemetry-ledger-global-timer**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [TELEMETRY] [M-RISK] [Meal] [ðŸ¤– PRO-HIGH] [BATCH:memory-leak-sweep]
  - **Goal:** Refactor shared globalFlushTimer to use an instance counter pattern.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster D).
  - **Plan:** ðŸ“Ž [PLAN-telemetry-ledger-global-timer.md](docs/plans/PLAN-telemetry-ledger-global-timer.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-17)

- [x] **fix/pii-logger-scrubber**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [SECURITY] [H-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:pii-scrub-sweep]
  - **Goal:** Implement automatic PII/location redaction in AppLogger before telemetry dispatch.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster A). High severity.
  - **Plan:** ðŸ“Ž [PLAN-pii-logger-scrubber.md](docs/plans/PLAN-pii-logger-scrubber.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-09)

- [x] **fix/ble-backoff-jitter**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [BLE] [M-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:error-handling-sweep]
  - **Goal:** Add randomized jitter to BLE reconnection backoffs to prevent broadcast storm collisions.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster E).
  - **Plan:** ðŸ“Ž [PLAN-ble-backoff-jitter.md](docs/plans/PLAN-ble-backoff-jitter.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-06)

- [x] **fix/ble-recovery-max-attempts**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [BLE] [M-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:error-handling-sweep]
  - **Goal:** Implement max reconnection attempts logic to prevent infinite looping.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster E).
  - **Plan:** ðŸ“Ž [PLAN-ble-recovery-max-attempts.md](docs/plans/PLAN-ble-recovery-max-attempts.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-06)

- [x] **fix/heartbeat-gatt-guard**
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [BLE] [M-RISK] [Meal] [ðŸ¤– PRO-HIGH] [BATCH:error-handling-sweep]
  - **Goal:** Prevent heartbeat dispatches from crashing the BLE Queue if GATT is unavailable.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster E).
  - **Plan:** ðŸ“Ž [PLAN-heartbeat-gatt-guard.md](docs/plans/PLAN-heartbeat-gatt-guard.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-06)

- [x] **fix/promise-io-safety-critical** ðŸš€ Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [IO] [L-RISK] [Snack] [ðŸ¤– PRO-MED] [BATCH:promise-io-safety-sweep]
  - **Goal:** Fix 5 critical fire-and-forget Promises missing `.catch` blocks.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster C).
  - **Plan:** ðŸ“Ž [PLAN-promise-io-safety-critical.md](docs/plans/PLAN-promise-io-safety-critical.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-11)

- [x] **fix/promise-io-safety-medium** ðŸš€ Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [IO] [L-RISK] [Meal] [ðŸ¤– PRO-MED] [BATCH:promise-io-safety-sweep]
  - **Goal:** Fix 7 medium severity fire-and-forget Promises in AsyncStorage writes.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster C).
  - **Plan:** ðŸ“Ž [PLAN-promise-io-safety-medium.md](docs/plans/PLAN-promise-io-safety-medium.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-11)

- [x] **fix/type-laundering-sweep** ðŸš€ Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [TYPES] [M-RISK] [Feast] [ðŸ¤– PRO-MED] [BATCH:type-safety-sweep]
  - **Goal:** Eliminate 28 instances of `as unknown as` casting across the codebase.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster B).
  - **Plan:** ðŸ“Ž [PLAN-type-laundering-sweep.md](docs/plans/PLAN-type-laundering-sweep.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-08)

- [x] **fix/ble-programmer-gatt-delays** ðŸš€ Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [BLE] [M-RISK] [Meal] [ðŸ¤– PRO-HIGH] [BATCH:hardcoded-delay-sweep]
  - **Goal:** Replace hardcoded static GATT settle delays with reactive patterns or configurable constants.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster G).
  - **Plan:** ðŸ“Ž [PLAN-ble-programmer-gatt-delays.md](docs/plans/PLAN-ble-programmer-gatt-delays.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-16)

- [x] **fix/auth-context-bypass** ðŸš€ Merged in 2213d4cc8db8c6b1ae8b21ccef4f23a1b738f83e
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [SECURITY] [H-RISK] [Meal] [ðŸ¤– PRO-HIGH] [BATCH:auth-context-sweep]
  - **Goal:** Harden global navigation guards against AsyncStorage tampering for isOfflineMode bypass.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster K). Critical security gap.
  - **Plan:** ðŸ“Ž [PLAN-auth-context-bypass.md](docs/plans/PLAN-auth-context-bypass.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-15)

- [x] **fix/device-settings-probe-fsm** ðŸš€ Merged in 2213d4cc8db8c6b1ae8b21ccef4f23a1b738f83e
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [UI] [M-RISK] [Meal] [ðŸ¤– PRO-MED] [BATCH:boolean-fsm-sweep]
  - **Goal:** Replace binary isProbing flag with granular ProbeStatus union type in DeviceSettingsModal.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster I).
  - **Plan:** ðŸ“Ž [PLAN-device-settings-probe-fsm.md](docs/plans/PLAN-device-settings-probe-fsm.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-18)

- [x] **fix/hal-enclosure-oracle-tab** ðŸš€ Merged in 2213d4cc8db8c6b1ae8b21ccef4f23a1b738f83e
  - **Tags:** [âšª READY] [âœ… VERIFIED] [BLE] [L-RISK] [Snack] [CHORE] [BATCH:hal-enclosure-sweep]
  - **Plan:** ðŸ“Ž [PLAN-hal-enclosure-oracle-tab.md](docs/plans/PLAN-hal-enclosure-oracle-tab.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (Cluster H, R-19)
  - **Goal:** Extract raw BLE byte arrays from DiagnosticLabOracleTab.tsx into ZenggeProtocol.ts methods. Exclude the 0x73 hypothesis test payloads.
  - **Rationale for Demotion (2026-06-08):** Oracle Lab functions independently. However, 2026-06-10 deepdive-code-synthesis flagged this as a HAL Enclosure violation (R-19) for the 0x58/0x57/0x56 scene blocks. Plan updated and task formalized.

- [x] **fix/ui-state-matrix** ðŸš€ Merged in 83feb803e4511fad99933de527feee45d384a3b9
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [UI] [M-RISK] [Feast] [ðŸ¤– PRO-HIGH] [BATCH:state-matrix-sweep]
  - **Goal:** Implement missing Error and Empty states across 9 specific UI screens.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster J).
  - **Plan:** ðŸ“Ž [PLAN-ui-state-matrix.md](docs/plans/PLAN-ui-state-matrix.md)
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-14)

- [x] **feat/observatory-schema** ðŸš€ Merged in 83feb803e4511fad99933de527feee45d384a3b9
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Establish the TypeScript UnifiedErrorRecord schema and BaseCollector structure.
  - **Decision Log:** Foundation required to normalize 12 disparate error sources into a single queryable pipeline.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-a1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "A unified schema is required before any deduplication or routing can occur."
    Rejected alternative: "Storing raw payloads â€” rejected because it makes exact-match dedup impossible."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§4 Unified Error Schema
  - **Details:** Must support fingerprints, breadcrumbs, cross-reference matches, and urgency scores.

- [x] **feat/observatory-local-collectors** ðŸš€ Merged in 83feb803e4511fad99933de527feee45d384a3b9
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build local file-reading and grep-scanning collectors (Sources 7-12) to parse known issues, logs, and AST output.
  - **Decision Log:** We need to capture institutional memory (KNOWN_ISSUES, SESSION_LOG) and static analysis signals to detect regressions.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-a2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Local grep and file parsing provide zero-dependency signals for regression detection."
    Rejected alternative: "N/A"
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§3 (Sources 7-12)
  - **Details:** Zero external dependencies. Wraps `grep -rn "console.error" src/` avoiding `__DEV__` lines.

- [x] **feat/observatory-build-collectors** ðŸš€ Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build the build-time collectors for Jest, TSC, and Web Console errors (Sources 3-5).
  - **Decision Log:** Unit test failures and TSC compilation errors must be auto-triaged just like production errors.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-a3](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Build pipeline errors provide high-confidence signals with exact file:line references."
    Rejected alternative: "Manual test verification â€” rejected to enforce auto-triage of regressions."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§3 (Sources 3-5)
  - **Details:** Must parse JSON outputs from Jest and map TSC stdout to the Unified schema.

- [x] **feat/observatory-report-generator** ðŸš€ Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Generate the daily markdown triage report for user review.
  - **Decision Log:** Tasks require human approval. The report is the presentation layer for the user's "Ship it" gate.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-d2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Report must sort by max urgency score to focus attention on CRITICAL and HIGH items."
    Rejected alternative: "N/A"
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§8
  - **Details:** Writes to `tools/observatory/reports/YYYY-MM-DD/report.md`.

- [x] **feat/observatory-workflow** ðŸš€ Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[CORE]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Write the `/self-heal` markdown workflow and deprecate `/audit-codebase`.
  - **Decision Log:** This brings the system to life, tying River, Blake, Reyes, Jordan, and Alex into a unified 5-phase execution map.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-e1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "A dedicated workflow file ensures consistent execution and persona handoffs."
    Rejected alternative: "Keeping audit-codebase â€” rejected per user decision C to consolidate."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§8, Â§10.4
  - **Details:** Must include the Step 0 institutional memory check. Deprecates `audit-codebase.md`.

- [x] **feat/observatory-auto-heal-library** ðŸš€ Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[CORE]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Implement the AH-001 through AH-009 auto-heal pattern library.
  - **Decision Log:** We must automate fixes for known, low-risk, repetitive errors to free up human cycles.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-e2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Auto-heals must NEVER apply to BLE or CORE domains."
    Rejected alternative: "Auto-committing patches â€” rejected; user must approve all proposals."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§7
  - **Details:** Flags tasks with `[ðŸ”§ AUTO-HEAL PROPOSED]`.

- [x] **feat/observatory-tests** ðŸš€ Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Write Jest unit tests for the collection, dedup, scoring, and task generation engines.
  - **Decision Log:** The observatory is critical infrastructure and must be protected by the `npm run verify` pipeline.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-f1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Test coverage ensures dedup and scoring logic remains stable."
    Rejected alternative: "N/A"
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§11
  - **Details:** Add at least 3 test suites (`dedup.test.js`, `scoring.test.js`, `task_generator.test.js`).

- [x] **fix/auth-context-fsm** ðŸš€ Merged in 461e16d6f591884c9e0f3ed222f340600c4f704c
  - **Tags:** [âšª TRIAGE] [âœ… VERIFIED] [CORE] [H-RISK] [Feast] [ðŸ¤– PRO-HIGH] [BATCH:boolean-fsm-sweep]
  - **Goal:** Refactor AuthContext from 3 overlapping boolean flags into a strict Finite State Machine.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster I). Tagged Feast and H-RISK. Sent to Roadmap to prevent disrupting current sprint.
  - **Plan:** TBD
  - **Source of Truth:** ðŸ“– artifacts/system_audit_report.md (R-18)

- [x] **feat/observatory-remote-collectors**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[CLOUD]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build collectors to fetch Supabase `telemetry_errors` and MMKV-backed `crash_telemetry` (Sources 1-2).
  - **Decision Log:** We opted for a beefier homegrown CrashReporter (Option D) and need to collect those live production crashes.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-b1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "MMKV persistence survives native crashes; we must pull these breadcrumbs into the observatory."
    Rejected alternative: "Sentry/Crashlytics SDKs â€” rejected to adhere to the Anti-Bloat Protocol."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§3 (Sources 1-2)
  - **Details:** Extends existing `tools/sync_remote_errors.mjs`.

- [x] **feat/observatory-device-collector**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[CORE]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build an ADB logcat parser to detect native device crashes (Source 6).
  - **Decision Log:** React Native JS boundaries cannot catch ANR/OOMs; ADB logcat is the only visibility into native deaths.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-b2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "ADB parsing provides a zero-dependency safety net for native crashes."
    Rejected alternative: "N/A"
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§3 (Source 6)
  - **Details:** Gracefully outputs 0 findings if no ADB device is connected.

- [x] **feat/observatory-dedup-engine**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âš ï¸ H-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Implement the 4-pass deduplication pipeline and the composite urgency scoring algorithm (0-100).
  - **Decision Log:** 12 sources will create massive noise and duplicate reports. A robust 4-pass dedup is required to cluster root causes.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-c1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Fuzzy matching and root cause clustering are critical for reducing noise."
    Rejected alternative: "Simple exact match â€” rejected because the same error can trigger slightly different line numbers."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§5.1, Â§5.2
  - **Details:** Must support Exact Match, Fuzzy Match, Root Cause, and False Positive Scrubbing.

- [x] **feat/observatory-crossref-engine**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Cross-reference deduplicated errors against KNOWN_ISSUES and FRICTION_LEDGER for regression detection.
  - **Decision Log:** We must never re-derive known fixes or fail to identify regressions of previously resolved patterns.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-c2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Checking errors against institutional memory creates the self-reinforcing knowledge loop."
    Rejected alternative: "N/A"
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§5.3, Â§5.4
  - **Details:** Increases urgency by 30 if an error fingerprint matches a resolved VS-XXX pattern.

- [x] **feat/observatory-task-generator**
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[TELEMETRY]` `[âš ï¸ H-RISK]` `[ðŸ± Meal]` `[ðŸ¤– MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Generate strict kanban task entries with automated tags, goals, and Decision Logs from error clusters.
  - **Decision Log:** The action layer must seamlessly integrate into Casey's kanban flow without breaking formatting rules.
  - **Analysis:** ðŸ“Š Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) Â· Plan: [PLAN-observatory-pipeline.md#task-d1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Auto-generated tasks must follow strict multi-line schema to avoid breaking parsers."
    Rejected alternative: "Direct Bucket List insertion â€” rejected; tasks must be presented for user review first."
  - **Source of Truth:** ðŸ“– `self_healing_audit_system.md` Â§6.1, Â§6.2
  - **Details:** Output string must strictly match SK8Lytz Kanban Schema.

- [x] **chore/exception-masking-sweep** ðŸš€ Merged in 559dcaaf
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[CORE]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix 17 empty catch blocks and `String(e)` misuses across 12 files. Protocol files (`ZenggeProtocol.ts`, `BanlanxAdapter.ts`) are silently swallowing errors that should be logged. Apply canonical `e instanceof Error ? e.message : String(e)` pattern throughout.
  - **Decision Log (2026-06-10):** Fleet found 3 silent catches in `ZenggeProtocol.ts:18,192,393` â€” protocol-level errors are swallowed with zero visibility. `BanlanxAdapter.ts:95` same pattern. `SessionContext.tsx:366,399` uses raw `String(err)` instead of proper unwrapping.
  - **Analysis:** ðŸ“Š Source: `artifacts/system_audit_report.md` Â· CLUSTER-04 (17 findings, R-06)
    Key finding: "Protocol-level silent catches are the highest priority â€” they hide BLE command failures with zero observability."
    Rejected alternative: "Bare re-throw â€” rejected, crashes the caller instead of gracefully logging."
  - **Plan:** ðŸ“Ž [PLAN-exception-masking-sweep.md](docs/plans/PLAN-exception-masking-sweep.md)
  - **Source of Truth:** ðŸ“– `src/protocols/ZenggeProtocol.ts:18,192,393` Â· `src/protocols/BanlanxAdapter.ts:95` Â· `src/context/SessionContext.tsx:366,399`

- [x] **chore/promise-safety-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[CORE]` `[âš ï¸ H-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Add try/catch and `.catch()` handlers to 23 async operations across 13 files â€” including the hardware wizard flow, session context notification setup, AdminTelemetry export, and all AsyncStorage fire-and-forget calls.
  - **Decision Log (2026-06-10):** Fleet confirmed `HardwareSetupWizardScreen.tsx:64,601` has async flows with no catch â€” any BLE rejection silently crashes the wizard. `setupNotification()` is called fire-and-forget with internal awaits. `PushTokenService` makes two Supabase writes with zero error handling.
  - **Analysis:** ðŸ“Š Source: `artifacts/system_audit_report.md` Â· CLUSTER-02 (23 findings, R-11)
    Key finding: "8 HIGH severity unhandled rejections in wizard, session, and telemetry flows â€” crash risk on BLE/network failure."
    Rejected alternative: "Global unhandledRejection handler â€” rejected, masks root causes and violates surgical principle."
  - **Plan:** ðŸ“Ž [PLAN-promise-safety-sweep.md](docs/plans/PLAN-promise-safety-sweep.md)
  - **Source of Truth:** ðŸ“– `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:64,601` Â· `src/context/SessionContext.tsx:240` Â· `src/hooks/useAdminTelemetry.ts:49,55` Â· `src/services/PushTokenService.ts:22,36`

- [x] **chore/fsm-boolean-trap-sweep** â€” âœ… Merged @ bd3a0435. Refactored 18 target files to use strict status string unions ('idle' | 'loading' | 'success' | 'error'), eliminating impossible race states. Deferred Hook Zones in DashboardScreen/DockedController to avoid collateral damage per surgical rules.
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[UI]` `[âš ï¸ M-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deepdive-sweep]` `[WAVE:3]`
  - **Goal:** Replace scattered boolean flag triplets (`isLoading + isError + isSuccess`) in 18 files with single FSM string union state (`'idle' | 'loading' | 'error' | 'success'`).
  - **Decision Log (2026-06-10):** Fleet found 18 files with boolean traps including root screens (`DashboardScreen.tsx:178`) and critical flows (`HardwareSetupWizardScreen.tsx:53-55`, `AuthFormSignUp.tsx:37`). Scattered booleans allow `isLoading && isSuccess` to be simultaneously true â€” a logically impossible race state.
  - **Analysis:** ðŸ“Š Source: `artifacts/system_audit_report.md` Â· CLUSTER-03 (18 findings, R-18)
    Key finding: "DashboardScreen, DockedController, and the Hardware Wizard all have boolean traps â€” the three highest-traffic screens."
    Rejected alternative: "useReducer for every component â€” rejected as over-engineering for simple 3-state flows."
  - **Plan:** ðŸ“Ž [PLAN-fsm-boolean-trap-sweep.md](docs/plans/PLAN-fsm-boolean-trap-sweep.md)
  - **Source of Truth:** ðŸ“– `src/screens/DashboardScreen.tsx:178` Â· `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:53-55` Â· `src/components/auth/AuthFormSignUp.tsx:37` Â· `artifacts/system_audit_report.md CLUSTER-03`

- [x] **chore/state-matrix-sweep** ðŸš€ Merged in 0374dc4c
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[UI]` `[âš ï¸ M-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Expose missing `isLoading`/`error` states from hooks (R-14). Audit and normalize all AsyncStorage keys to consistent namespace (R-24). Replace hardcoded `setTimeout` in BLE + splash flows with proper lifecycle events (R-16).
  - **Decision Log (2026-06-10):** Fleet confirmed `useCuratedPicks.ts:112` catches Supabase errors and logs them but never exposes `error` state to consumers â€” the UI renders stale data silently. `FavoritesPanel` declares `picksLoading` prop but ignores it. `DashboardScreen.tsx:197` uses `setTimeout` where `InteractionManager` should be used.
  - **Analysis:** ðŸ“Š Source: `artifacts/system_audit_report.md` Â· CLUSTER-08 (21 findings, R-14+R-24+R-16)
    Key finding: "`useCuratedPicks` hiding errors means admins never see failed spot fetches â€” offline-first promise broken."
    Rejected alternative: "Global error boundary only â€” rejected, doesn't expose the error to the specific UI widget that needs it."
  - **Plan:** ðŸ“Ž [PLAN-state-matrix-sweep.md](docs/plans/PLAN-state-matrix-sweep.md)
  - **Source of Truth:** ðŸ“– `src/hooks/useCuratedPicks.ts:112` Â· `src/components/docked/FavoritesPanel.tsx:100` Â· `src/screens/DashboardScreen.tsx:197` Â· `src/constants/storageKeys.ts`

- [x] **chore/misc-guardrail-sweep** ðŸš€ Merged in 2a682c5b
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[CORE]` `[âš ï¸ M-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-MED]` `[BATCH:deepdive-sweep]` `[WAVE:5]`
  - **Goal:** Fix 27 findings across 8 minor guardrails: PII MAC leaks (R-09), Android BT permission flag (R-20), memory leaks (R-22), re-entrancy races (R-26), context depth (R-27), event listener leak (R-17), stale closure (R-12), split-brain duplication (R-21).
  - **Decision Log (2026-06-10):** Highest urgency within this cluster is `AndroidManifest.xml:8` â€” `BLUETOOTH_SCAN` missing `neverForLocation` forces Android 12+ users to grant Location permission just to find their skates. Second priority: MAC PII leak in `DeviceRepository.ts:793`. These two are 1-line fixes with outsized impact.
  - **Analysis:** ðŸ“Š Source: `artifacts/system_audit_report.md` Â· CLUSTER-09 (27 findings)
    Key finding: "Missing `neverForLocation` on BLUETOOTH_SCAN is a UX regression â€” users will see an unexpected Location permission prompt on fresh installs."
    Rejected alternative: "Accept the Location permission â€” rejected, violates user trust and App Store privacy guidelines."
  - **Plan:** ðŸ“Ž [PLAN-misc-guardrail-sweep.md](docs/plans/PLAN-misc-guardrail-sweep.md)
  - **Source of Truth:** ðŸ“– `android/app/src/main/AndroidManifest.xml:8` Â· `src/services/DeviceRepository.ts:793` Â· `src/hooks/useDashboardCrew.ts:70` Â· `artifacts/system_audit_report.md CLUSTER-09`

- [x] **chore/type-safety-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[CORE]` `[âš ï¸ M-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deepdive-sweep]` `[WAVE:6]`
  - **Goal:** Eliminate 48 `any` casts and `as unknown as` type laundering patterns across 36 files. Highest concentration in hooks, dashboard components, admin tools, and crew screens.
  - **Decision Log (2026-06-10):** Fleet flagged 30 HIGH severity `any` usages. `useHardwareNotifications.ts` alone has 4. `supabaseClient.ts:78` uses `as unknown as` to bypass the mock type â€” must implement the interface properly. `CrewCreateScreen.tsx:122` uses `(s: any)` on a spot filter with a known `SkateSpot` type available.
  - **Analysis:** ðŸ“Š Source: `artifacts/system_audit_report.md` Â· CLUSTER-01 (48 findings, R-08)
    Key finding: "36 files affected â€” this is the widest surface area finding in the entire audit."
    Rejected alternative: "Suppress with @ts-ignore â€” strictly forbidden per No `any` Cast Law in agent-behavior.md."
  - **Plan:** ðŸ“Ž [PLAN-type-safety-sweep.md](docs/plans/PLAN-type-safety-sweep.md)
  - **Source of Truth:** ðŸ“– `src/types/supabase.ts` Â· `src/hooks/useHardwareNotifications.ts` Â· `src/components/crew/CrewCreateScreen.tsx:122` Â· `artifacts/system_audit_report.md CLUSTER-01`

- [x] **chore/anti-bloatprotocolr-21-sweep** (merged: c750de0f)
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all Anti-Bloat Protocol / R-21 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating Anti-Bloat Protocol / R-21. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-anti-bloatprotocolr-21-sweep.md](docs/plans/PLAN-anti-bloatprotocolr-21-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule Anti-Bloat Protocol / R-21
  - **Prerequisite:** None

- [x] **chore/r-17-sweep** (merged: 06ed1ab7)
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-17 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-17. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-17-sweep.md](docs/plans/PLAN-r-17-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-17
  - **Prerequisite:** None

- [x] **chore/r-04-sweep** (merged: c39f38f5)
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-04 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-04. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-04-sweep.md](docs/plans/PLAN-r-04-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-04
  - **Prerequisite:** None

- [x] **chore/r-10-sweep** (merged: 61315662)
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-10 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-10. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-10-sweep.md](docs/plans/PLAN-r-10-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-10
  - **Prerequisite:** None

- [x] **chore/anti-bloatprotocol-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all Anti-Bloat Protocol violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating Anti-Bloat Protocol. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-anti-bloatprotocol-sweep.md](docs/plans/PLAN-anti-bloatprotocol-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule Anti-Bloat Protocol
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-03-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-03 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-03. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-03-sweep.md](docs/plans/PLAN-r-03-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-03
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-05-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-05 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-05. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-05-sweep.md](docs/plans/PLAN-r-05-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-05
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-08-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-08 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-08. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-08-sweep.md](docs/plans/PLAN-r-08-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-08
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-22-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-22 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-22. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-22-sweep.md](docs/plans/PLAN-r-22-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-22
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/unknown-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all Unknown violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating Unknown. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-unknown-sweep.md](docs/plans/PLAN-unknown-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule Unknown
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-18-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:3]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-18 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-18. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-18-sweep.md](docs/plans/PLAN-r-18-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-18
  - **Prerequisite:** Wave 2 fully merged

- [x] **chore/r-13-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:3]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-13 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-13. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-13-sweep.md](docs/plans/PLAN-r-13-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-13
  - **Prerequisite:** Wave 2 fully merged

- [x] **chore/r-23-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:4]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-23 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-23. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-23-sweep.md](docs/plans/PLAN-r-23-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-23
  - **Prerequisite:** Wave 3 fully merged

- [x] **chore/r-21-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:4]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-21 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-21. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-21-sweep.md](docs/plans/PLAN-r-21-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-21
  - **Prerequisite:** Wave 3 fully merged

- [x] **chore/r-26-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:5]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-26 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-26. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-26-sweep.md](docs/plans/PLAN-r-26-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-26
  - **Prerequisite:** Wave 4 fully merged

- [x] **chore/r-09-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:5]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-09 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-09. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-09-sweep.md](docs/plans/PLAN-r-09-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-09
  - **Prerequisite:** Wave 4 fully merged

- [x] **chore/r-24-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-24 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-24. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-24-sweep.md](docs/plans/PLAN-r-24-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-24
  - **Prerequisite:** Wave 5 fully merged

- [x] **chore/r-06-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[H-RISK]` `[Meal]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-06 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-06. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-06-sweep.md](docs/plans/PLAN-r-06-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-06
  - **Prerequisite:** Wave 5 fully merged

- [x] **chore/r-14-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-14 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-14. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-14-sweep.md](docs/plans/PLAN-r-14-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-14
  - **Prerequisite:** Wave 5 fully merged

- [x] **chore/r-15-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:7]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-15 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-15. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-15-sweep.md](docs/plans/PLAN-r-15-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-15
  - **Prerequisite:** Wave 6 fully merged

- [x] **chore/r-07-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Meal]` `[WAVE:7]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-07 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-07. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-07-sweep.md](docs/plans/PLAN-r-07-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-07
  - **Prerequisite:** Wave 6 fully merged

- [x] **chore/r-11-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:8]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-11 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-11. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-11-sweep.md](docs/plans/PLAN-r-11-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-11
  - **Prerequisite:** Wave 7 fully merged

- [x] **chore/r-27-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:8]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-27 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-27. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-27-sweep.md](docs/plans/PLAN-r-27-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-27
  - **Prerequisite:** Wave 7 fully merged

- [x] **chore/r-20-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:9]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-20 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-20. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-20-sweep.md](docs/plans/PLAN-r-20-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-20
  - **Prerequisite:** Wave 8 fully merged

- [x] **chore/r-16-sweep**
  - **Tags:** `[âšª TRIAGE]` `[âœ… VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:10]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-16 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-16. Grouped by collision graph into parallel-safe batches.
  - **Plan:** ðŸ“Ž [PLAN-r-16-sweep.md](docs/plans/PLAN-r-16-sweep.md)
  - **Source of Truth:** ðŸ“– `artifacts/system_audit_report.md` Â· Rule R-16
  - **Prerequisite:** Wave 9 fully merged

- [x] **`feat/deepdive-docs-holistic-sync`** ðŸš€ Merged in 64e6826d
  - **Tags:** `[âœ… READY]` `[TOOLING]` `[L-RISK]` `[Snack]` `[ðŸ¤– PRO-MED]`
  - **Goal:** Enhance /deepdive-docs to automatically maintain high-level non-developer documentation.
  - **Decision Log:** High-level diagrams and user journeys will slowly drift out of date if they aren't synced dynamically alongside the Master Reference when code changes.
  - **Analysis:** ðŸ“Š Plan: [PLAN-deepdive-docs-holistic-sync.md](../../docs/plans/PLAN-deepdive-docs-holistic-sync.md)
    Key finding: "The Cartographers must flag changes impacting User Journey flows, C4 Context boundaries, or Hardware state machines."
    Rejected alternative: "Manually patching the documentation was rejected in favor of automatic synthesis."
  - **Source of Truth:** ðŸ“– [.agents/workflows/deepdive-docs.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-docs.md)
  - **Details:** Must not break non-destructive archival protocols.

- [x] **chore/sweep-services-core**
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 94 findings in domain services-core. Every file in the plan must appear in the diff.
  - **Source of Truth:** docs/plans/PLAN-sweep-services-core.md

- [x] **chore/sweep-components-admin** ðŸš€ Merged in d9585164
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 35 findings in domain components-admin. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-components-admin.md

- [x] **chore/sweep-components-auth** ðŸš€ Merged in 54b1cea5
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Meal]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 13 findings in domain components-auth. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-components-auth.md

- [x] **chore/sweep-services-ble** ðŸš€ Merged in 414097f8
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[H-RISK]` `[Meal]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 11 findings in domain services-ble. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-services-ble.md

- [x] **chore/sweep-supabase** ðŸš€ Merged in a561ee25
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 3 findings in domain supabase. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-supabase.md

- [x] **chore/sweep-utils** ðŸš€ Merged in 7a827677
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 8 findings in domain utils. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-utils.md

- [x] **chore/sweep-hooks-core** ðŸš€ Merged in b3d43808
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:3]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 118 findings in domain hooks-core. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 2 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-hooks-core.md
  - **Decision Log:** Eradicate all core hook findings identified in the DeepDive audit (system_audit_report.md) to enforce type safety, error boundaries, and re-entrancy safety.

- [x] **chore/sweep-root** ðŸš€ Merged in 9bdeb129
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:4]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 23 findings in domain root. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 3 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-root.md
  - **Decision Log:** Eradicate all root domain findings identified in the DeepDive audit (system_audit_report.md) to enforce type safety, remove unused/bloated client-side Node.js dependencies, handle unhandled platform/native promise re-entrancies, and ensure platform UI shadow fallbacks.

- [x] **chore/sweep-components-ui**
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:5]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 96 findings in domain components-ui. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 4 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-components-ui.md
  - **Decision Log:** Eradicate all components-ui domain findings identified in the DeepDive audit (system_audit_report.md) to improve layout safety, eliminate type laundering, and enforce error boundary protection.

- [x] **chore/sweep-hooks-ble**
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[H-RISK]` `[Feast]` `[WAVE:5]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 18 findings in domain hooks-ble. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 4 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-hooks-ble.md
  - **Decision Log:** Resolve all hooks-ble domain findings identified in the DeepDive audit (system_audit_report.md) to fix memory leaks, type safety issues, and connection state bugs in BLE hooks.

- [x] **chore/sweep-context**
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[H-RISK]` `[Meal]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 15 findings in domain context. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 5 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-context.md
  - **Decision Log:** Eradicate all context domain findings identified in the DeepDive audit (system_audit_report.md) to fix memory leaks, unhandled promises, and state naming in React contexts.

- [x] **chore/sweep-screens**
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:7]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 32 findings in domain screens. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 6 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-screens.md
  - **Decision Log:** Eradicate all screens domain findings identified in the DeepDive audit (system_audit_report.md) to ensure proper type safety, remove unused variables, and standardize lifecycle callbacks.

- [x] **`spike/wear-os-bridge-field`**
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[ðŸ§ª LAB]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]` `[BATCH:session-xstate-engine]` `[WAVE:0]`
  - **Goal:** Confirm exact field name for session distance in WatchBridge payload so Wave 1 + Wear OS fix use the correct key.
  - **Decision Log:** `SessionCommitService.ts` and the Wear OS distance bug fix both read from a WatchBridge message field â€” field name must be verified before writing a single line of code that uses it.
  - **Analysis:** ðŸ“Š Source: [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) Â· Plan: [PLAN-spike-wear-os-bridge-field.md](./plans/PLAN-spike-wear-os-bridge-field.md)
    Key finding: "Wear OS DashboardScreen.kt shows hardcoded 0.0 distance â€” field name must be from WatchBridge payload type"
    Rejected alternative: "Assume field name is `distance` â€” rejected, assumption violates P1"
  - **Source of Truth:** ðŸ“– [android/sk8lytzWear/](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/) + [targets/watch/](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/) + `sk8lytz-watch-bridge` package types
  - **Details:** Read-only spike. Output: one `[DECISION]` entry in SESSION_LOG with the confirmed field name. No code changes. `Prerequisite: none.`

- [x] **`feat/session-services-layer`** ðŸš€ Merged in b9c7baa9
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[ðŸ§ª LAB]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  HIGH]` `[BATCH:session-xstate-engine]` `[WAVE:1]`
  - **Goal:** Create all 9 new files in `src/services/session/` and `src/components/session/` â€” the full XState session engine layer â€” without touching any existing file.
  - **Decision Log:** 10 confirmed session sync bugs (watch desync, auto-pause race, notification drift, stats wrong counter) all trace to one root cause: no single XState state authority for session lifecycle. Confirmed 2026-06-11 via full `SessionContext.tsx` + `useGlobalTelemetry.ts` + `useHealthTelemetry.ts` audit.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-feat-session-services-layer.md](./plans/PLAN-feat-session-services-layer.md)
    Key finding: "`BleMachine.ts` pattern (`setup({ actors, actions })` + `fromCallback` services + `fromPromise` commit) is already proven in this codebase â€” zero new patterns required"
    Rejected alternative: "Custom pub/sub event bus â€” rejected because XState v5 already installed and BleMachine proves pattern works"
  - **Source of Truth:** ðŸ“– [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) + [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts) + [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts) + [SessionContext.tsx:285â€“359](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L285-L359)
  - **Details:** Creates: `SessionMachine.types.ts`, `SessionMachine.ts`, `SensorService.ts`, `AutoPauseService.ts`, `HealthService.ts`, `SessionCommitService.ts`, `NotificationService.ts`, `SessionBridge.ts`, `../components/session/SessionPhaseBadge.tsx`. Zero existing file modifications. Jest test stubs required. `Prerequisite: Wave 0 fully merged â€” distance field name confirmed in SESSION_LOG.`

- [x] **`refactor/session-context-xstate`** ðŸš€ Merged in 4df46b81
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âš ï¸ H-RISK]` `[ðŸ± Meal]` `[ðŸ§  HIGH]` `[BATCH:session-xstate-engine]` `[WAVE:2]`
  - **Goal:** Rewrite `SessionContext.tsx` to be a thin `useMachine(sessionMachine)` wrapper with identical public `useSession()` API â€” zero changes to any consumer.
  - **Decision Log:** Current `SessionContext.tsx` is 474 lines of fragmented React state FSM + 6 chained useEffects. Every session phase transition requires synchronizing 4 independent systems manually. Wave 1 moves all logic into the machine â€” this wave wires it.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-refactor-session-context-xstate.md](./plans/PLAN-refactor-session-context-xstate.md)
    Key finding: "`useSession()` return shape at `SessionContext.tsx:463` â€” `{ isSkateSessionActive, sessionPhase, startSession, endSession, telemetry, health }` â€” must remain identical"
    Rejected alternative: "Change useSession() return shape â€” rejected because 14+ consumers would break"
  - **Source of Truth:** ðŸ“– [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx) + [useBLE.ts:177](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L177)
  - **Details:** Single file rewrite. Keeps: `SessionContext` createContext, storage key constants, `persistSessionPhase`, `useSession()` export. Removes: all useState FSM, all manual useEffect chains. Adds: `useMachine`, `SessionBridge.register`, crash recovery via `STORAGE_PENDING_BG_END`, watch listeners via `SessionBridge`. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`

- [x] **`refactor/delete-legacy-hooks`** ðŸš€ Merged in c8e30287
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[ðŸ§ª LAB]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]` `[BATCH:session-xstate-engine]` `[WAVE:3]`
  - **Goal:** Delete `useGlobalTelemetry.ts` + `useHealthTelemetry.ts` (now orphaned after Wave 2) and register the Notifee background event handler in app root.
  - **Decision Log:** AST confirmed both files have `imported_by: [SessionContext.tsx]` only. After Wave 2 removes those imports, both files are dead code. Background Notifee handler must be registered at app root (outside React tree) for notification action buttons to work when app is backgrounded.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-refactor-delete-legacy-hooks.md](./plans/PLAN-refactor-delete-legacy-hooks.md)
    Key finding: "AST output: `useGlobalTelemetry.ts imported_by: [SessionContext.tsx]` and `useHealthTelemetry.ts imported_by: [SessionContext.tsx]` â€” confirmed safe to delete after Wave 2"
    Rejected alternative: "Keep as deprecated stubs â€” rejected because dead code is risk and confusion"
  - **Source of Truth:** ðŸ“– [useGlobalTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts) + [useHealthTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHealthTelemetry.ts) + [SessionContext.tsx:441â€“448](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L441-L448)
  - **Details:** Parallel-safe with Wave 3B and 3C â€” no shared files. Files touched: 2 deletions + `App.tsx` (1 new block). Must grep for remaining imports before deleting. `Prerequisite: Wave 2 fully merged into master before this worktree is created.`

- [x] **`feat/session-phase-badge-ui`** ðŸš€ Merged in 481839b5
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  MEDIUM]` `[BATCH:session-xstate-engine]` `[WAVE:3]`
  - **Goal:** Integrate `SessionPhaseBadge` into `DashboardTelemetryHero` and `LiveTelemetryHUD` so users see `â— RECORDING` / `â¸ PAUSED` / `âº SAVING...` near the session timer.
  - **Decision Log:** User explicitly requested phase indicator below/near timers in session â€” `DashboardTelemetryHero` (HUD) and `LiveTelemetryHUD` (controller pill) are the two display surfaces confirmed by prop chain audit at `DockedController.tsx:1080â€“1084`. `SessionPhaseBadge` component created in Wave 1.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-feat-session-phase-badge-ui.md](./plans/PLAN-feat-session-phase-badge-ui.md)
    Key finding: "`sessionPhase` already destructured from `useSession()` in `DashboardScreen.tsx:486` â€” zero new data fetching required"
    Rejected alternative: "Add `useSession()` call inside DashboardTelemetryHero directly â€” rejected because it bypasses the existing prop contract and creates a second context read"
  - **Source of Truth:** ðŸ“– [DashboardTelemetryHero.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx) + [DockedController.tsx:1080](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1080) + [DashboardScreen.tsx:486](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L486)
  - **Details:** Parallel-safe with Wave 3A and 3C. Files: `DashboardTelemetryHero.tsx` (badge below TIME pill), `LiveTelemetryHUD.tsx` (rightmost pill slot), `DockedController.tsx` (1 prop addition line only â€” S4 monolith flag active, surgical only). StreetPanel badge done in Wave 3C. `Prerequisite: Wave 2 fully merged into master before this worktree is created.`

- [x] **`fix/session-bug-fixes`** ðŸš€ Merged in 481839b5
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  MEDIUM]` `[BATCH:session-xstate-engine]` `[WAVE:3]`
  - **Goal:** Fix 3 isolated bugs: StreetPanel dual-source-of-truth data + badge, AccountTabStats wrong sessions counter, Wear OS hardcoded 0.0 mi distance.
  - **Decision Log:** (1) `StreetPanel.tsx:80â€“81` reads `crewService.sessionTelemetry` instead of the drilled-in `sessionPeakSpeed`/`sessionDistanceMiles` props â€” confirmed by code read. (2) `AccountTabStats.tsx:49` uses `history?.length` (crew history count, not skate sessions) â€” confirmed source-of-truth mismatch. (3) Wear OS stop confirmation screen shows hardcoded `0.0 mi` â€” confirmed from prior audit.
  - **Analysis:** ðŸ“Š Source: [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) Â· Plan: [PLAN-fix-session-bug-fixes.md](./plans/PLAN-fix-session-bug-fixes.md)
    Key finding: "`DockedController.tsx:1249â€“1258` confirms `sessionPeakSpeed` and `sessionDistanceMiles` are already drilled to StreetPanel â€” the fix is replacing the wrong data source, not adding new props"
    Rejected alternative: "Fix via crewService changes â€” rejected because crewService is the wrong data source entirely"
  - **Source of Truth:** ðŸ“– [StreetPanel.tsx:80â€“81](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/StreetPanel.tsx#L80-L81) + [AccountTabStats.tsx:49](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/AccountTabStats.tsx#L49) + [DockedController.tsx:1249](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1249)
  - **Details:** Parallel-safe with Wave 3A and 3B. Files: `StreetPanel.tsx` (2-line data fix + badge integration + 1 prop addition), `AccountTabStats.tsx` (1-line counter fix), `android/sk8lytzWear/` KT file (hardcoded distance fix â€” field name from Wave 0 SESSION_LOG entry). `Prerequisite: Wave 2 fully merged into master. Wave 0 SESSION_LOG entry must contain confirmed distance field name.`

- [x] **`docs/cartographer-rebuild-and-harden`**
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âœ… L-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  HIGH]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Run the full 21-node cartographer fleet, update all Tier-3 satellite docs unconditionally, inject 3 missing ADRs, and harden 3 workflow files so Phase 4 can never be silently skipped again.
  - **Decision Log:** Post-Wave 1+2 audit confirmed: Master Reference missing 9 new session service files + rewritten SessionContext.tsx; Phase 4 of `/deepdive-docs` was skipped on `5aa3aa68` run because it is conditional on flags sub-agents optionally emit; 3 [DECISION] entries from 2026-06-11 never promoted to ADR; State_Charts_UX.md has no sessionMachine chart; User_Journey_Maps.md Journey 3 shows outdated linear watch flow.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-docs-cartographer-rebuild-and-harden.md](./plans/PLAN-docs-cartographer-rebuild-and-harden.md)
    Key finding: "`git log --oneline -3 -- tools/State_Charts_UX.md` shows last update `5aa3aa68` (yesterday) â€” Phase 4 ran but produced no changes because [IMPACTS_STATE_CHART] flag was conditional"
    Rejected alternative: "Delta sync of 4 domains only â€” rejected because Phase 4 was skipped entirely so all satellite docs need first real sync"
  - **Source of Truth:** ðŸ“– [deepdive-docs.md:L121-L129](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-docs.md#L121-L129) Â§Phase 4 + [SESSION_LOG.md:L40-L52](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SESSION_LOG.md#L40-L52) Â§3 missing ADRs
  - **Details:** Phase A = 21-node cartographer fleet â†’ Phase 3 injection â†’ Phase 4 unconditional satellite update â†’ Phase 5 ADR sync. Phase B = 3 surgical workflow edits (deepdive-docs, start-task, wind-down). Zero TypeScript source file changes. Full rebuild justified: Domain 12 (SESSION_TRACKING) entirely different after Wave 2 rewrite.

- [x] **`docs/test-plan-session-machine`**
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  MEDIUM]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Add a "Session Machine Test Coverage (XState v5)" section to `tools/SK8Lytz_TEST_PLAN.md` documenting the 28-suite / 218-test coverage added in Waves 1+2, including known gaps.
  - **Decision Log:** Wave 1 (`b9c7baa9`) + Wave 2 (`4df46b81`) merged a complete sessionMachine implementation with 10+ new test cases. `SK8Lytz_TEST_PLAN.md` (39KB) has no session machine section â€” last update was `a3973b94` (pre-XState). Without documenting what is covered and what is NOT covered (the gaps), future QA runs have no baseline to work from.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-docs-test-plan-session-machine.md](./plans/PLAN-docs-test-plan-session-machine.md)
    Key finding: "`git log --oneline -1 -- tools/SK8Lytz_TEST_PLAN.md` â†’ `a3973b94` (pre-Wave 1) â€” test plan 2 merges behind"
    Rejected alternative: "Write new tests instead â€” rejected; user explicitly said `/intake` for test plan documentation, not new test authoring. Test authoring is a separate `/tdd` task."
  - **Source of Truth:** ðŸ“– [SK8Lytz_TEST_PLAN.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_TEST_PLAN.md) + merge commits `b9c7baa9` (W1) + `4df46b81` (W2)
  - **Details:** Must read actual test files before documenting coverage â€” do not document from memory. Document both covered states/edges AND known gaps (network failure during COMMITTED, concurrent END triggers). Parallel-safe with all other batch tasks.

- [x] **`docs/xstate-v5-kb-capture`**
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Read `BleMachine.ts` + `sessionMachine.ts` live implementations and write a KB entry for the XState v5 patterns SK8Lytz actually uses, so future agents never re-derive them.
  - **Decision Log:** KB INDEX has no XState entry (confirmed by search â€” 2026-06-11). Two XState v5 state machines now run in the app (`BleMachine.ts` + `sessionMachine.ts` from Wave 1). Without a KB entry, every cold-start session agent re-derives patterns from documentation or guesswork. Rule 13 requires KB entry before asserting facts about external libraries.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-docs-xstate-v5-kb-capture.md](./plans/PLAN-docs-xstate-v5-kb-capture.md)
    Key finding: "`grep 'xstate' tools/knowledge-base/INDEX.md` â†’ no results. Zero KB coverage for XState v5 despite two active state machines in production."
    Rejected alternative: "Full XState v5 API docs â€” rejected; scope is patterns-we-use only to keep KB focused (user confirmed Q1)"
  - **Source of Truth:** ðŸ“– [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) + [sessionMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/sessionMachine.ts) + [knowledge-base/INDEX.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/knowledge-base/INDEX.md)
  - **Details:** Scope = `createMachine`, `useMachine`, `assign`, guard syntax, `SessionBridge` actor pattern. Staleness window: 180 days. Feeds into Master Reference Â§3 State Machine Library. Parallel-safe with all other batch tasks.

- [x] **`fix/industry-benchmarks-dedup`**
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[â˜ï¸ CLOUD]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Remove the duplicate second copy of content in `tools/INDUSTRY_BENCHMARKS.md` â€” the full file content appears twice due to a prior double-write bug.
  - **Decision Log:** `view_file tools/INDUSTRY_BENCHMARKS.md` confirmed (2026-06-11): lines 1-49 identical to lines 50-99 verbatim. A double-write during a prior context-compiler run wrote the header + 3 benchmark entries twice. Lines 100-125 are unique (High-Density Grouping, Hardware Mocks, DB PII Encryption) and must be preserved.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Â· Plan: [PLAN-fix-industry-benchmarks-dedup.md](./plans/PLAN-fix-industry-benchmarks-dedup.md)
    Key finding: "File is 125 lines but unique content is only ~75 lines â€” 50-line duplicate section from a prior double-write"
    Rejected alternative: "Leave as-is â€” rejected; duplication confuses agents and the second header block (`# Industry Benchmarks`) causes parsing errors in synthesis workflows"
  - **Source of Truth:** ðŸ“– [INDUSTRY_BENCHMARKS.md:L1-L125](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/INDUSTRY_BENCHMARKS.md)
  - **Details:** 2-minute surgical fix. Read full file, identify exact duplicate range, remove second copy, commit. No content loss â€” only duplicate removal.

- [x] **`fix/session-machine-actor-types`** â€” *Merged to master @ 8f482d06*
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[ðŸ§ª LAB]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  MEDIUM]` `[BATCH:session-xstate-hardening]` `[WAVE:1A]`
  - **Goal:** Type all 4 `fromCallback<any>` actor calls to `SessionMachineEvent`, remove 10s `syncWatchStopped` delay, fix ENDING notification buttons.
  - **Decision Log:** Post-merge audit 2026-06-11 found `fromCallback<any, ...>` on AutoPauseService L9, SensorService L18, HealthService L12, NotificationService L18 â€” suppresses type checking on sendBack events. syncWatchStopped 10s delay creates race if new session starts within window. ENDING phase shows contextually wrong action buttons.
  - **Analysis:** ðŸ“Š Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) Â· Plan: [PLAN-fix-session-machine-actor-types.md](./plans/PLAN-fix-session-machine-actor-types.md)
    Key finding: "4 actors emit untyped sendBack events; 1 machine action has a 10s race window; ENDING notification shows wrong buttons"
    Rejected alternative: "Add // @ts-ignore to suppress" â€” violates No any Cast Law (S3)
  - **Source of Truth:** ðŸ“– [SessionMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SessionMachine.ts#L127-L131) `syncWatchStopped` Â· [AutoPauseService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/AutoPauseService.ts#L9) Â· [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts#L42-L71)
  - **Details:** All 4 actor files share `SessionMachine.ts` as their parent consumer â€” AST confirmed these must run in one worktree. `SessionMachineEvent` type exists in `SessionMachine.types.ts` and is the correct sendBack type. The ENDING fix adds an early-return branch before the isPaused branch in NotificationService.

- [x] **`fix/ble-connection-hang`** ðŸš€ Merged in de974879
  - **Tags:** `[âœ… READY]` `[ðŸ§ª LAB]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]`
  - **Goal:** Fix the connection hang when opening the controller screen on already connected devices.
  - **Decision Log:** physical device gets stuck on blue screen when opening controller because connectedDevices([mac]) passes MAC instead of service UUIDs, causing connectToDevice to be called redundantly and hang.
  - **Analysis:** ðŸ“Š Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/002ab10f-9595-41ac-89ac-a7516dd02366/implementation_plan.md) Â· Plan: [PLAN-fix-ble-connection-hang.md](./plans/PLAN-fix-ble-connection-hang.md)
    Key finding: "passing MAC to connectedDevices returns [] forcing redundant connectToDevice call which hangs BLE stack"
    Rejected alternative: "bypassing isDeviceConnected check â€” rejected because we need connection-state check to prevent redundant connection attempts"
  - **Source of Truth:** ðŸ“– [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L149) Â§1
  - **Details:** Must run verify after changes.

- [x] **`fix/hardware-setup-identification`**
  - **Tags:** `[âœ… READY]` `[âœ… UNVERIFIED]` `[UI]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  LOW]` `[BATCH:hardware-setup]`
  - **Goal:** Fix setup wizard color swap logic, blink button matching colors, and correctly persist left/right custom device names.
  - **Decision Log:** Setup wizard had flawed color swap assignment causing both skates to get same color on step 2, blink buttons defaulted to cyan instead of red/green, and custom names did not persist to DB.
  - **Analysis:** ðŸ“Š Plan: [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md)
    Key finding: "position assignment overrides itself if both names default to 'left' substring; `custom_name` missing from payload."
  - **Source of Truth:** ðŸ“– [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** Wave assignment: [WAVE:1] â€” verified by AST analysis. No import overlap with active batches. Merged in c9c64b88 â€” Fixed left/right assignment bug and name logic.

- [x] **`feat/hardware-setup-brand-colors`**
  - **Tags:** `[âœ… READY]` `[âœ… UNVERIFIED]` `[UI]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]` `[BATCH:hardware-setup]`
  - **Goal:** Update Hardware Setup Wizard to use Neogleamz brand colors (Blue and Orange) instead of generic Red and Green.
  - **Decision Log:** The user requested swapping the generic red/green identification colors to the official brand colors 1B4279 and F79320.
  - **Analysis:** ðŸ“Š Plan: [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md)
    Key finding: "Hardware identity flashing should match the brand aesthetic for a premium feel."
  - **Source of Truth:** ðŸ“– [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** Wave assignment: [WAVE:1] â€” shares import tree with `fix/hardware-setup-identification`. Will execute in unified batch worktree. Merged in c9c64b88 â€” Applied brand colors to setup wizard.

- [x] **`fix/global-header-spacing`**
  - **Tags:** `[âœ… READY]` `[âœ… UNVERIFIED]` `[UI]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  LOW]` `[BATCH:hardware-setup]`
  - **Goal:** Eliminate double padding blank space at the top of the app header.
  - **Decision Log:** SafeAreaProvider was added to App.tsx, but DashboardScreen retained SafeAreaView while DashboardHeader also used insetTop, causing double top padding.
  - **Analysis:** ðŸ“Š Plan: [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md)
    Key finding: "DashboardScreen nested SafeAreaView causes double padding."
  - **Source of Truth:** ðŸ“– [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
  - **Details:** Wave assignment: [WAVE:1] â€” verified by AST analysis. Shares import tree with hardware setup batch. Merged in c9c64b88 â€” Removed SafeAreaView to fix double padding.

- [x] **`fix/app-safe-area-boundaries`** - Merged @ 1122bb39 (Migrated to react-native-safe-area-context)
  - **Tags:** `[âœ… READY]` `[âœ… UNVERIFIED]` `[UI]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  LOW]` `[BATCH:safe-area-migration]` `[WAVE:1]`
  - **Goal:** Migrate core React Native SafeAreaView imports to react-native-safe-area-context.
  - **Decision Log:** SafeAreaView from react-native is deprecated and fails to apply notch/header padding on Android devices properly.
  - **Analysis:** ðŸ“Š Plan: [PLAN-fix-app-safe-area-boundaries.md](./plans/PLAN-fix-app-safe-area-boundaries.md)
    Key finding: "Core SafeAreaView does not respect Android notification bar insets correctly."
  - **Source of Truth:** ðŸ“– [GlobalErrorBoundary.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/GlobalErrorBoundary.tsx#L2) Â· [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L11)
  - **Details:** Replaces the deprecated import in the two remaining files. Drops into existing UI with no layout shifts other than correct notch spacing.

- [x] **`fix/admin-modal-safe-areas`** [merge: efe231b2] ðŸš€ Merged in efe231b2
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[UI]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  LOW]` `[BATCH:admin-modal-safe-areas]` `[WAVE:1]`
  - **Goal:** Migrate remaining legacy SafeAreaView imports in Admin/Account Modals to react-native-safe-area-context.
  - **Decision Log:** Codebase audit revealed 8 admin panels and EulaModal still use core SafeAreaView, and AccountModal uses an unpadded absolute `top: 16`.
  - **Analysis:** ðŸ“Š Source: N/A Â· Plan: [PLAN-fix-admin-modal-safe-areas.md](./plans/PLAN-fix-admin-modal-safe-areas.md)
    Key finding: "Legacy SafeAreaView allows notch bleeding on Android."
    Rejected alternative: "Ignore since they are admin tools" â€” breaks UX for power users and EulaModal affects all users.
  - **Source of Truth:** ðŸ“– [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
  - **Details:** Simple import swap across 8 files and one dynamic padding change.

- [x] **`feat/harden-ble-regression-shields`** ðŸš€ Merged in fd3df999
  - **Tags:** `[âœ… READY]` `[ðŸ§ª LAB]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  MEDIUM]`
  - **Goal:** Build automated Jest regression tests and codify permanent rules to lock down setup wizard scanning and group connection state.
  - **Decision Log:** FTUE onboarding race conditions and setup wizard scanning deadlocks cost 12 hours of debugging across multiple sessions; codifying regression tests and strict rules prevents future AI/developer regressions.
  - **Analysis:** ðŸ“Š Source: [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L345) and [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L561) Â· Plan: [PLAN-harden-ble-regression-shields.md](./plans/PLAN-harden-ble-regression-shields.md)
    Key finding: Idempotent sweeper triggers and Next button state checks are critical to FTUE onboarding stability.
    Rejected alternative: Relying solely on textual instructions in session summaries, which inevitably drift or get ignored by future models.
  - **Source of Truth:** ðŸ“– [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L345) L345-355 Â· [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L561) L561-571 Â· [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L282) L282
  - **Details:** Non-production changes only. Adds tests in `ConnectService.test.ts` and `HardwareSetupWizardScreen.test.tsx`, plus a new `useBLEScanner.test.ts`. Codifies rules in `21_GUARDRAILS.md` and `prime-directive.md`.

- [x] **`chore/sweep-cloud-supabase`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Harden 6 SECURITY DEFINER PostgreSQL functions with `SET search_path = ''` to eliminate SQL injection surface, fix email domain validation bypass, restrict scraper_blocklist RLS, and add error handling to the Deno edge function.
  - **Decision Log:** deepdive fleet confirmed 6 unguarded `SECURITY DEFINER` functions in Supabase migrations â€” without `SET search_path`, a caller can inject a malicious schema and execute arbitrary SQL under elevated privilege. The email LIKE `%@sk8lytz.com` pattern is bypassable with `x@sk8lytz.com.evil.com`.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-cloud-supabase.md](./plans/PLAN-sweep-cloud-supabase.md) â€” Key finding: "6 SECURITY DEFINER RPCs without SET search_path â€” SQL injection vectors (2 agents confirmed)" â€” Rejected: "App-layer validation only" â€” does not protect against DB-level schema injection
  - **Source of Truth:** [20260414_account_deletion_rpc.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260414_account_deletion_rpc.sql) + 5 additional migration files listed in PLAN


- [x] **`chore/sweep-devops-tooling`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[DEVOPS]` `[H-RISK]` `[Snack]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix gatekeeper rebase failure detection ($LASTEXITCODE), prevent regression healer from committing to master, fix auto-archiver slug regex collision, and replace raw `npx tsc`/`npx jest` in husky pre-commit hook.
  - **Decision Log:** Fleet confirmed `fortress-gatekeeper.ps1` does not check `$LASTEXITCODE` after `git rebase` â€” a failed rebase is silently ignored, leaving master in a corrupted merge state. `regression_healer.py` has no branch guard and can commit directly to master.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-devops-tooling.md](./plans/PLAN-sweep-devops-tooling.md) â€” Key finding: "Gatekeeper git rebase failure is unchecked; regression healer can commit to master" â€” Rejected: "Manual review step only" â€” silent failure mode requires a process guard
  - **Source of Truth:** [fortress-gatekeeper.ps1](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/fortress-gatekeeper.ps1#L93) Â· [regression_healer.py](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/sentinel/regression_healer.py#L188)


- [x] **`chore/sweep-protocol-core`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[BLE]` `[H-RISK]` `[Snack]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Resolve split-brain 0x40 chunking between ZenggeAdapter and BleWriteDispatcher, fix incorrect TransitionType mapping, and remove hardcoded 54-pixel max in streamPixelFrame.
  - **Decision Log:** 2 independent fleet agents confirmed ZenggeAdapter.prepareForTransmission and BleWriteDispatcher implement conflicting chunking logic â€” the controller receives double-chunked payloads, causing corrupted LED state. Per Protocol Bible: chunking belongs to the dispatcher only.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-protocol-core.md](./plans/PLAN-sweep-protocol-core.md) â€” Key finding: "Split-brain 0x40 chunking confirmed by 2 agents â€” double-chunked payloads corrupt LED state" â€” Rejected: "Move chunking to adapter" â€” Protocol Bible explicitly assigns chunking to the write dispatcher
  - **Source of Truth:** [ZenggeAdapter.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts#L260) Â· [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)


- [x] **`chore/sweep-ui-screens-dashboard`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[H-RISK]` `[Meal]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Eliminate `as any`/`any[]` props from 5 dashboard sub-components, fix Animated.Value memory leak in render cycle, fix Platform.OS ternary missing Web case, and fix power-toggle loop missing queue serialization.
  - **Decision Log:** Fleet found 6 dashboard components with untyped any props in the primary render path. Animated.Value instantiated in CrewHubSlab.tsx:181 render body accumulates instances on every render causing memory growth over long skating sessions.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-screens-dashboard.md](./plans/PLAN-sweep-ui-screens-dashboard.md) â€” Key finding: "Animated.Value memory leak in render; 5 components with any-typed props in Dashboard render path" â€” Rejected: "@ts-ignore suppression" â€” banned by The No any Cast Law
  - **Source of Truth:** [CrewHubSlab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/CrewHubSlab.tsx#L181) Â· [DashboardTelemetryHero.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx#L12)


- [x] **`chore/sweep-ui-visualizer-patterns`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Meal]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix floating unawaited promise in UnifiedPatternPicker, remove duplicate hexToRgb, fix web-only CSS props crashing on native in NeonHueStrip, and extract inline FlatList callbacks to stable useCallback refs.
  - **Decision Log:** Fleet confirmed writeToDeviceRef.current(payload) in UnifiedPatternPicker returns a Promise never caught â€” BLE write failures silently dropped. NeonHueStrip passes touchAction/userSelect to a native View, which crashes silently on iOS/Android.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-visualizer-patterns.md](./plans/PLAN-sweep-ui-visualizer-patterns.md) â€” Key finding: "Floating promise in UnifiedPatternPicker:62; NeonHueStrip web-only props crash on native" â€” Rejected: "Return void from writeToDevice" â€” masks errors; proper async error handling required
  - **Source of Truth:** [UnifiedPatternPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx#L62) Â· [NeonHueStrip.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/NeonHueStrip.tsx#L99)


- [x] **`chore/sweep-os-permissions-manifests`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[NATIVE]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Move hardcoded Google Maps API key from AndroidManifest.xml to build-time env var, fix HealthKit activity type mismatch, and trim excessive foregroundServiceType declaration.
  - **Decision Log:** Fleet flagged Google Maps API key hardcoded in AndroidManifest.xml:29 as plaintext â€” committed to git history and exposed to all repo contributors. Android 14+ strictly enforces foregroundServiceType matching; the over-broad declaration risks Play Store rejection.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-os-permissions-manifests.md](./plans/PLAN-sweep-os-permissions-manifests.md) â€” Key finding: "Google Maps API key hardcoded in AndroidManifest.xml â€” PII/secret leak committed to git" â€” Rejected: "Add to .gitignore only" â€” key already in history; must rotate + move to env var
  - **Source of Truth:** [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml#L29) Â· [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js#L15)


- [x] **`chore/sweep-native-watch`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[NATIVE]` `[M-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix @Published property mutation on WCSession background queue (iOS crash risk), fix non-atomic SharedPreferences write in Wear OS, and align exercise type to INLINE_SKATING.
  - **Decision Log:** Fleet confirmed WatchConnectivityManager.swift:105 mutates @Published properties from the WCSession background delegate queue â€” SwiftUI rendering on background threads causes crashes on iOS 17+. WearMessageSender.kt:85 non-atomic SharedPreferences write causes data corruption under concurrent health delivery.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-native-watch.md](./plans/PLAN-sweep-native-watch.md) â€” Key finding: "@Published modified on WCSession background queue â€” guaranteed crash on iOS 17+" â€” Rejected: "@MainActor attribute only" â€” WCSession delegates are not MainActor-isolated; must use explicit DispatchQueue.main.async
  - **Source of Truth:** [WatchConnectivityManager.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift#L105) Â· [WearMessageSender.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt#L85)


- [x] **`chore/sweep-admin-telemetry`** ðŸš€ Merged in aa782643
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Meal]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Extract all inline keyExtractor/renderItem callbacks in admin FlatLists to stable useCallback refs, add 4-state UI matrices to 3 admin panels, and fix AppLogger telemetry context structure in 2 files.
  - **Decision Log:** Fleet found inline arrow functions for keyExtractor in every admin panel FlatList â€” these defeat FlatList virtualization causing full re-renders on every state update. AdminRosterPanel, HardwareBlacklistPanel, and FeatureFlagsPanel show blank screens on fetch failure with no error feedback.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-admin-telemetry.md](./plans/PLAN-sweep-admin-telemetry.md) â€” Key finding: "6 admin FlatLists with inline keyExtractor defeat virtualization; 3 panels missing error/empty states" â€” Rejected: "Memoize entire list component" â€” stable callback refs are the targeted correct fix
  - **Source of Truth:** [AdminRosterPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminRosterPanel.tsx#L178) Â· [HardwareBlacklistPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/HardwareBlacklistPanel.tsx#L255)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-ble-core-dispatch`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[BLE]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Replace Promise.all concurrent BLE writes with serialized sequential dispatch, add re-entrancy guards to processQueue and battery sweep, and PII-scrub 9 raw MAC address leaks from telemetry logs.
  - **Decision Log:** 2 independent agents confirmed BleWriteDispatcher.ts:164 and :228 use Promise.all for concurrent characteristic writes â€” the Zengge controller has a single GATT characteristic; parallel writes cause GATT collisions and undefined controller state. Protocol Bible mandates strictly sequential delivery.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ble-core-dispatch.md](./plans/PLAN-sweep-ble-core-dispatch.md) â€” Key finding: "Promise.all at BleWriteDispatcher:164,:228 â€” concurrent GATT writes violate sequential write contract (2 agents confirmed)" â€” Rejected: "Retry on GATT collision" â€” serialization is the correct fix; retry masks root cause
  - **Source of Truth:** [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts#L164) Â· [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-storage-keys`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[STORAGE]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Fix 3 AsyncStorage key collision bugs, consolidate all hardcoded @Sk8lytz_* key literals into the central STORAGE_KEYS registry, and flip AppSettingsService to write local cache first (offline-first mandate).
  - **Decision Log:** Fleet confirmed useFavorites and QuickPresetModal resolve favorites keys independently with conflicting logic â€” reads/writes from different code paths silently overwrite each other. DashboardScreen hardcodes '@Sk8lytz_Favorites' bypassing the registry; if the key is ever renamed the Dashboard silently reads nothing.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-storage-keys.md](./plans/PLAN-sweep-storage-keys.md) â€” Key finding: "@Sk8lytz_Favorites hardcoded in DashboardScreen:648; AppSettingsService blocks local cache behind network â€” violates offline-first mandate" â€” Rejected: "Document keys" â€” doesn't prevent future renames from silently breaking Dashboard
  - **Source of Truth:** [useFavorites.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useFavorites.ts#L33) Â· [AppSettingsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppSettingsService.ts#L91)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-ui-modals-shared`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Snack]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Replace static color imports with useTheme() hook in 3 modal components, fix any-typed props in CommunityModal and MarqueeText, and break the circular dependency in account/types.ts.
  - **Decision Log:** Fleet confirmed DeviceSettingsModal and GroupSettingsModal import colors statically, bypassing useTheme() â€” these components are invisible to dark mode/theme switching. The account/types.ts circular import chain causes unpredictable module resolution order on hot reload.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-modals-shared.md](./plans/PLAN-sweep-ui-modals-shared.md) â€” Key finding: "DeviceSettingsModal and GroupSettingsModal ignore dark mode â€” static color import bypasses useTheme()" â€” Rejected: "Pass colors as props" â€” props threading for theme is an antipattern; hook consumption is correct
  - **Source of Truth:** [DeviceSettingsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceSettingsModal.tsx#L7) Â· [account/types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/types.ts)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-identity-auth`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[AUTH]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:3]`
  - **Goal:** Fix notif_preferences: any in core profile type, remove direct supabase.auth.getUser() from AppLogger, and fix 5 AppLogger telemetry context structure errors in auth layer.
  - **Decision Log:** Fleet confirmed ProfileService.types.ts:21 declares notif_preferences: any â€” this core type field poisons every component that consumes the profile type. AppLogger.ts:674 fires a live supabase.auth.getUser() network call on every log flush cycle, adding latency and failing silently when offline.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-identity-auth.md](./plans/PLAN-sweep-identity-auth.md) â€” Key finding: "notif_preferences: any in core type; AppLogger fires live auth network call on every log flush" â€” Rejected: "Cast to unknown instead of any" â€” still loses type information; proper interface required
  - **Source of Truth:** [ProfileService.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ProfileService.types.ts#L21) Â· [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts#L674)
  - **Details:** Prerequisite: Wave 2 fully merged into master before this worktree is created.


- [x] **`chore/sweep-group-sync`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[CLOUD]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Eliminate 4 as-any type casts in GroupRepository and CrewService DB access paths, fix stale closure in useCrewProximityRadar, add 4-state UI to Crew screens, and PII-scrub 3 raw user data leaks.
  - **Decision Log:** Fleet confirmed 4x as-any on GroupRepository + CrewService Supabase row access â€” bypasses shape validation on DB rows, causing runtime crashes when schema evolves. useCrewProximityRadar:131 captures crewService.isNearby as non-reactive, causing proximity radar to never update after initial mount.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-group-sync.md](./plans/PLAN-sweep-group-sync.md) â€” Key finding: "4x as-any on DB row access in GroupRepository/CrewService; stale closure in useCrewProximityRadar stops proximity radar after mount" â€” Rejected: "Runtime schema validation library" â€” overweight; Supabase-generated types already present in project
  - **Source of Truth:** [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts#L27) Â· [useCrewProximityRadar.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewProximityRadar.ts#L131)
  - **Details:** Prerequisite: Wave 3 fully merged into master before this worktree is created.


- [x] **`chore/sweep-session-context`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[SERVICES]` `[H-RISK]` `[Meal]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Add isFlushInProgress re-entrancy guards to 3 flushSyncQueue functions, add try/catch to 3 unawaited AsyncStorage calls in SessionContext, and register 6 undocumented storage keys into the registry.
  - **Decision Log:** Fleet confirmed SpeedTrackingService, ScenesService, and GradientsService all have the same re-entrancy bug â€” concurrent callers double-upload the queue then both clear it, silently deleting pending session data that one caller never successfully POSTed. This is an active data loss bug.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-session-context.md](./plans/PLAN-sweep-session-context.md) â€” Key finding: "3x flushSyncQueue with no re-entrancy guard â€” concurrent callers corrupt queue and silently delete pending session data (2 agents confirmed)" â€” Rejected: "Move flush to singleton scheduler" â€” boolean ref guard solves problem with zero new dependencies
  - **Source of Truth:** [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts#L243) Â· [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts#L258) Â· [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts#L161)
  - **Details:** Prerequisite: Wave 3 fully merged into master before this worktree is created.


- [x] **`chore/sweep-shared-utils`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[SERVICES]` `[M-RISK]` `[Snack]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:5]`
  - **Goal:** Deduplicate isValidEmail to a canonical src/utils/validation.ts, add accessibility props to CustomSlider and DeviceItem, fix PositionalGradientBuilder error handling, and add platform guard to LocationService.
  - **Decision Log:** Fleet found isValidEmail duplicated across 3+ auth forms â€” any future change requires updating all copies in sync. CustomSlider uses PanResponder with zero accessibility props â€” completely invisible to screen readers, violating App Store accessibility guidelines.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-shared-utils.md](./plans/PLAN-sweep-shared-utils.md) â€” Key finding: "isValidEmail duplicated 3+ times; CustomSlider has zero accessibility props â€” invisible to screen readers" â€” Rejected: "Shared comment" â€” comments don't prevent drift; canonical module import is correct
  - **Source of Truth:** [CustomSlider.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomSlider.tsx#L102) Â· [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts#L81)
  - **Details:** Prerequisite: Wave 4 fully merged into master before this worktree is created.


- [x] **`chore/sweep-ui-docked-controller`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:6]`
  - **Goal:** Wrap 5+ unawaited writeToDevice calls in UniversalSlidersFooter with async error handlers, fix stale closure in useStreetMode accelerometer listener, and stabilize MemoizedSk8lytzController dependencies.
  - **Decision Log:** Fleet confirmed writeToDevice called fire-and-forget in 5+ locations in UniversalSlidersFooter.tsx â€” BLE write failures silently swallowed with no user feedback. useStreetMode:188 captures deviceContext at listener registration â€” after device reconnect, the listener holds a stale reference and sends to the wrong device.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-docked-controller.md](./plans/PLAN-sweep-ui-docked-controller.md) â€” Key finding: "5+ unawaited writeToDevice in UniversalSlidersFooter â€” BLE write failures silently dropped; stale closure in useStreetMode sends to wrong device after reconnect" â€” Rejected: "Global unhandled promise rejection handler" â€” too broad; per-call-site async handling required
  - **Source of Truth:** [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx#L393) Â· [useStreetMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts#L188)
  - **Details:** Prerequisite: Wave 5 fully merged into master before this worktree is created.



## ÃƒÂ¢Ã‚ÂÃ¢â‚¬Å¾ÃƒÂ¯Ã‚Â¸Ã‚Â Icebox / Backburner (Manual Trigger Only)

### ÃƒÂ°Ã…Â¸Ã…Â½Ã‚Âµ Epic: Music Mode

- [ ] `feat/music-intel-phase-1` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚Â¥Ã‚Â© Feast] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 50k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 6h] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¦ 2026-04-14] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] [Spotify Sync] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 15k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¦ 2026-04-14] [ÃƒÂ¢Ã¢â‚¬ÂºÃ¢â‚¬Â BLOCKED BY feat/music-intel-phase-1] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] [Media Access] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Android MediaSession detection (YouTube, Pandora, etc.). ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Âª LAB] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 15k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¦ 2026-04-14] [ÃƒÂ¢Ã¢â‚¬ÂºÃ¢â‚¬Â BLOCKED BY feat/music-intel-phase-1] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] [Live Rink Mode] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â ShazamKit/ACRCloud periodic background scanning (45s). ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 15k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¦ 2026-04-14] [ÃƒÂ¢Ã¢â‚¬ÂºÃ¢â‚¬Â BLOCKED BY feat/music-intel-phase-1] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] [Apple Music] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â MusicKit integration for native iOS BPM. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚Â¥Ã‚Â© Feast] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 45k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 6h] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¦ 2026-04-14] [ÃƒÂ¢Ã¢â‚¬ÂºÃ¢â‚¬Â BLOCKED BY feat/music-intel-phase-1] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] [Crew Party Sync] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Master BPM Choreography Engine with Realtime crew sync. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-music-integration-master.md)

- [ ] `feat/google-oauth-integration` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚Â¥Ã‚Â© Feast] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 30k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 6h] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã¢â‚¬Â¦ 2026-04-14] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] Integrate Google OAuth as an auth provider. (Requires Google Cloud Console setup + Supabase config). ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-google-oauth-integration.md)
- [ ] `feat/spatial-beat-mapping` : [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Âª LAB] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 18k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 15k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] Transform Dashboard into palette-synced dynamic backgrounds. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Âª LAB] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 10k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] Pattern selection (Strobe, Blink, Static) + music slider fix. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/kinetic-brake-lights` : [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Âª LAB] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 15k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] [Pillar 12] Kinetic Safety ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â phone accelerometer pulse RED for braking. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚Â¥Ã‚Â© Feast] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 30k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 6h] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] Geofence-based 'Hive Mind' synchronization. ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 8k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 25k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ PRO-HIGH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…Â¡Ã‚Â ÃƒÂ¯Ã‚Â¸Ã‚Â H-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 20k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â§Ã‚Â  THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [ÃƒÂ¢Ã‹Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â CLOUD] [ÃƒÂ¢Ã…â€œÃ¢â‚¬Â¦ L-RISK] [ÃƒÂ°Ã…Â¸Ã‚ÂÃ‚Â± Meal] [ÃƒÂ°Ã…Â¸Ã‚ÂªÃ¢â€žÂ¢ 12k] [ÃƒÂ¢Ã‚ÂÃ‚Â±ÃƒÂ¯Ã‚Â¸Ã‚Â 3h] [ÃƒÂ°Ã…Â¸Ã‚Â¤Ã¢â‚¬â€œ FLASH] [ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã‚ÂÃƒÂ¯Ã‚Â¸Ã‚Â NEEDS-PLAN] Card Swipe Navigation.



## Batch Strategy Tables Archive

### [BATCH:deepdive-sweep] â€” `deepdive-sweep-batch` â€” READY
> **Worktree Strategy**: Wave 1 (7 tasks fully parallel) -> Wave 2 (4 tasks fully parallel) -> Wave 3 (1 task) -> Wave 4 (2 tasks parallel) -> Wave 5 (1 task) -> Wave 6 (1 task)
> **Type**: Multi-wave parallel sweep â€” AST collision-verified
> **Prerequisite**: None â€” Wave 1 is parallel-safe with current master HEAD
> **Source Analysis**: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” 47-agent deepdive fleet; 456 verified findings across 16 domain clusters; 42 AST collision pairs detected

#### Batch Strategy Table
| Wave | Task Slug | Worktree | Key Files | Prerequisite |
|---|---|---|---|---|
| 1 | `chore/sweep-cloud-supabase` | `sweep-cloud-supabase` | `supabase/migrations/` (6 files) + `supabase/functions/notify-crew-session/` | None |
| 1 | `chore/sweep-devops-tooling` | `sweep-devops-tooling` | `tools/fortress-gatekeeper.ps1`, `tools/auto-archiver.js`, `tools/sentinel/`, `.husky/pre-commit` | None |
| 1 | `chore/sweep-protocol-core` | `sweep-protocol-core` | `src/protocols/ZenggeAdapter.ts`, `src/protocols/ZenggeProtocol.ts`, `src/hooks/useProtocolBuilder.ts` | None |
| 1 | `chore/sweep-ui-screens-dashboard` | `sweep-ui-screens-dashboard` | `src/screens/DashboardScreen.tsx`, `src/components/dashboard/` (5 files) | None |
| 1 | `chore/sweep-ui-visualizer-patterns` | `sweep-ui-visualizer-patterns` | `src/components/patterns/`, `src/components/CustomEffectVisualizer.tsx`, `src/components/NeonHueStrip.tsx` | None |
| 1 | `chore/sweep-os-permissions-manifests` | `sweep-os-permissions-manifests` | `android/app/src/main/AndroidManifest.xml`, `app.config.js` | None |
| 1 | `chore/sweep-native-watch` | `sweep-native-watch` | `targets/watch/WatchConnectivityManager.swift`, `android/sk8lytzWear/` (2 Kotlin files) | None |
| 2 | `chore/sweep-ble-core-dispatch` | `sweep-ble-core-dispatch` | `src/services/BleWriteDispatcher.ts`, `src/services/ble/` (5 files), `src/hooks/useBLE.ts`, `src/hooks/ble/` (2 files), `src/hooks/useControllerDispatch.ts` | Wave 1 merged |
| 2 | `chore/sweep-admin-telemetry` | `sweep-admin-telemetry` | `src/components/admin/` (6 files) | Wave 1 merged |
| 2 | `chore/sweep-storage-keys` | `sweep-storage-keys` | `src/hooks/useFavorites.ts`, `src/services/AppSettingsService.ts`, `src/services/DeviceRepository.ts`, `src/components/docked/QuickPresetModal.tsx` | Wave 1 merged |
| 2 | `chore/sweep-ui-modals-shared` | `sweep-ui-modals-shared` | `src/components/DeviceSettingsModal.tsx`, `src/components/GroupSettingsModal.tsx`, `src/components/SessionSummaryModal.tsx`, `src/components/account/types.ts` (+ 3 more) | Wave 1 merged |
| 3 | `chore/sweep-identity-auth` | `sweep-identity-auth` | `src/services/ProfileService.types.ts`, `src/services/AppLogger.ts`, `src/services/AuthProfileService.ts`, `src/hooks/useAccountOverview.ts`, `src/components/AccountModal.tsx` | Wave 2 merged |
| 4 | `chore/sweep-group-sync` | `sweep-group-sync` | `src/services/GroupRepository.ts`, `src/services/CrewService.ts`, `src/hooks/useCrewHub.ts`, `src/hooks/useCrewSession.ts`, `src/components/crew/` (2 files) + 4 more | Wave 3 merged |
| 4 | `chore/sweep-session-context` | `sweep-session-context` | `src/services/SpeedTrackingService.ts`, `src/services/ScenesService.ts`, `src/services/GradientsService.ts`, `src/context/SessionContext.tsx`, `src/hooks/useTelemetryLedger.ts`, `src/hooks/useDeviceStateLedger.ts` | Wave 3 merged |
| 5 | `chore/sweep-shared-utils` | `sweep-shared-utils` | `src/components/PositionalGradientBuilder.tsx`, `src/components/CustomSlider.tsx`, `src/services/LocationService.ts`, `src/utils/validation.ts` (new) + 3 more | Wave 4 merged |
| 6 | `chore/sweep-ui-docked-controller` | `sweep-ui-docked-controller` | `src/components/docked/UniversalSlidersFooter.tsx`, `src/components/docked/BuilderPanel.tsx`, `src/hooks/useStreetMode.ts`, `src/hooks/useDashboardController.tsx`, `src/components/DockedController.tsx` + 3 more | Wave 5 merged |

---


### ÃƒÂ¢Ã…Â¡Ã‚Â¡ [BATCH:doc-pipeline-sync] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `doc-pipeline-sync-batch` ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â READY
> **Worktree Strategy**: All 4 tasks fully parallel (tools/*.md files only ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â zero TypeScript overlap)
> **Type**: Parallel ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â all [WAVE:1]
> **Prerequisite**: None ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â AST confirmed zero import-tree overlap with session-xstate-engine Wave 3
> **Source Analysis**: ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â  [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Full doc ecosystem audit: 6 confirmed gaps after Wave 1+2 merges; Phase 4 of deepdive-docs was silently skipped; KB missing XState entry; 3 ADRs in SESSION_LOG never promoted; INDUSTRY_BENCHMARKS duplicated; TEST_PLAN pre-XState

#### Batch Strategy Table
| Wave | Task Slug | Files Touched | Prerequisite |
|---|---|---|---|
| 1 | `docs/cartographer-rebuild-and-harden` | `tools/SK8Lytz_App_Master_Reference.md` + Tier-3 satellite docs + 3 workflow files | None |
| 1 | `docs/xstate-v5-kb-capture` | `tools/knowledge-base/patterns/xstate-v5-patterns.md` + `tools/knowledge-base/INDEX.md` | None |
| 1 | `fix/industry-benchmarks-dedup` | `tools/INDUSTRY_BENCHMARKS.md` | None |
| 1 | `docs/test-plan-session-machine` | `tools/SK8Lytz_TEST_PLAN.md` | None |

### ÃƒÂ¢Ã…Â¡Ã‚Â¡ [BATCH:session-xstate-engine] ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â `session-xstate-engine-batch` ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â READY
> **Worktree Strategy**: Sequential waves (W0ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢W1ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢W2), then W3A+W3B+W3C fully parallel  
> **Type**: Mixed ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â Spike ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ Sequential ÃƒÂ¢Ã¢â‚¬Â Ã¢â‚¬â„¢ Parallel  
> **Prerequisite**: None ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â board is clear  
> **Source Analysis**: ÃƒÂ°Ã…Â¸Ã¢â‚¬Å“Ã…Â  [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) + [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â 10 confirmed session sync bugs traced to single root cause: no XState state authority for session lifecycle

#### Batch Strategy Table
| Wave | Task Slug | Worktree | Files Touched | Prerequisite |
|---|---|---|---|---|
| W0 | `spike/wear-os-bridge-field` | `spike-wear-os-bridge` | Read-only | None |
| W1 | `feat/session-services-layer` | `session-services-layer` | 9 new files in `src/services/session/` + `src/components/session/` | W0 confirmed |
| W2 | `refactor/session-context-xstate` | `session-context-xstate` | `SessionContext.tsx` only | W1 merged |
| W3A | `refactor/delete-legacy-hooks` | `delete-legacy-hooks` | `useGlobalTelemetry.ts` (DEL) + `useHealthTelemetry.ts` (DEL) + `App.tsx` | W2 merged |
| W3B | `feat/session-phase-badge-ui` | `session-phase-badge-ui` | `DashboardTelemetryHero.tsx` + `LiveTelemetryHUD.tsx` + `DockedController.tsx` (1 line) | W2 merged |
| W3C | `fix/session-bug-fixes` | `session-bug-fixes` | `StreetPanel.tsx` + `AccountTabStats.tsx` + `android/sk8lytzWear/` | W2 merged |

---



- [x] **`chore/session-service-test-coverage`**
  - **Tags:** `[âœ… READY]` `[ðŸ¤– INFERRED]` `[ðŸ§ª LAB]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  MEDIUM]`
  - **Goal:** Add substantive unit tests for SensorService, HealthService, and NotificationService â€” the 3 untested session actor services from the post-merge audit.
  - **Decision Log:** Post-merge audit 2026-06-11: 5 of 9 session service files have zero test coverage. SensorService is highest risk (GPS + accelerometer + crewService side effects). NotificationService ENDING-state button logic newly added in session-machine-actor-types task â€” needs test coverage for the 3-branch action logic.
  - **Analysis:** ðŸ“Š Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) Â· Plan: [PLAN-chore-session-service-test-coverage.md](./plans/PLAN-chore-session-service-test-coverage.md)
    Key finding: "44% test file coverage by file count; SensorService has GPS+accelerometer+crewService coupling and zero tests"
    Rejected alternative: "Skip until feature work" â€” test gaps in newly architected services compound quickly
  - **Source of Truth:** ðŸ“– [AutoPauseService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/AutoPauseService.test.ts) reference pattern Â· [SensorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SensorService.ts) Â· [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts) Â· [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
  - **Details:** Must run AFTER `fix/session-machine-actor-types` merges â€” NotificationService test for ENDING branch depends on the ENDING fix being in place. 3 new test files, no production source changes.

## ðŸš‘ TRIAGE QUEUE

> **Source Analysis**: ðŸ“Š [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md) â€” Unifies setup wizard logic fixes, brand color updates, and global header padding into a single surgical pass.

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 39 findings in sweep-root
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-root.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:22 M:8 L:9)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 10 findings in sweep-components-AccountModal.tsx
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-AccountModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:3 L:4)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 22 findings in sweep-components-admin
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-admin.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:7 M:7 L:8)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 7 findings in sweep-components-auth
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-auth.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:0 L:4)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-CommunityModal.tsx
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-CommunityModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:1 L:1)
  - [WAVE:1]

- [x] **[MAINTENANCE]** `[Status: [x]]` `[Verification: âœ… VERIFIED]` `[Layer: COMPONENT]` `[Risk: M-RISK]` `[Size: Meal]` `[Cognitive Load: HIGH]`
  - Task: Resolve 11 findings in sweep-components-crew
  - Source of Truth: `docs/plans/PLAN-sweep-components-crew.md`
  - Merge: 807f2235 - Extracted stats/forms, fixed double-taps, added error boundaries.
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:8 M:2 L:1)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 6 findings in sweep-components-dashboard
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-dashboard.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:2 L:2)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 18 findings in sweep-components-docked
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-docked.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:7 M:9 L:2)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-LocationPicker.tsx
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-LocationPicker.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:2 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-permissions
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-permissions.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:1 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-SkateSpotBottomSheet.tsx
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-SkateSpotBottomSheet.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:2 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-components-VisualizerUnit.tsx
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-VisualizerUnit.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:1 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-context-SessionContext.tsx
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-context-SessionContext.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:1 L:1)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 17 findings in sweep-hooks-ble
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [H-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-ble.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:10 L:6)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 11 findings in sweep-hooks-useControllerDispatch.ts
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useControllerDispatch.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:8 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-hooks-useCrewHub.ts
  - Tags: [âœ… READY] [âœ… AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useCrewHub.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:3 L:0)
  - [WAVE:1]
### [MERGED] deepdive-sweep-phase3 (Resolved via chore sweep)
  - **Tags:** `[G£à READY]` `[UI]` `[G£à L-RISK]` `[=ƒì¦ Meal]` `[=ƒñû PRO-HIGH]` `[BATCH:performance-telemetry]` `[WAVE:Standalone]`
  - **Decision Log:** The user requested tracking for individual page load times beyond just app startup. Rejected 3rd-party APM SDKs to maintain offline-first constraint.
  - **Source of Truth:** =ƒôû [PLAN-feat-performance-telemetry-ttid.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-feat-performance-telemetry-ttid.md)
  - **Details:** Surgical hook injection on core screens. Standalone wave.
- [x] `feat/app-wide-ux-tips` : [Gÿün+Å CLOUD] [G£à L-RISK] [=ƒì¦ Meal] [=ƒ¬Ö 12k] [GÅ¦n+Å 3h] [=ƒôà 2026-04-14] [=ƒñû FLASH] [=ƒô¥n+Å NEEDS-PLAN] Contextual tips system for key friction points. GåÆ [Plan](docs/plans/feat-app-wide-ux-tips.md)
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-CrewMemberDashboard.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-CrewMemberDashboard.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:1 M:0 L:0)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-components-DeviceItem.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-DeviceItem.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:3 M:2 L:0)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 13 findings in sweep-components-DockedController.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-DockedController.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:2 M:4 L:7)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-components-patterns
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-patterns.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:1 M:2 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-constants-storageKeys.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-constants-storageKeys.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:2 M:0 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-context-AppConfigContext.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-context-AppConfigContext.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:1 M:0 L:0)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 9 findings in sweep-hooks-useAccountOverview.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useAccountOverview.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:1 M:1 L:7)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-hooks-useBLE.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useBLE.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:1 M:2 L:5)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useCrewProximityRadar.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useCrewProximityRadar.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:1 M:0 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useCrewSession.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useCrewSession.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-hooks-useDashboardAutoConnect.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDashboardAutoConnect.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:0 L:7)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useDashboardCrew.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDashboardCrew.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:0 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 6 findings in sweep-hooks-useHardwareNotifications.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useHardwareNotifications.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:1 L:4)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useMusicMode.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useMusicMode.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:2 M:0 L:0)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useProductManager.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useProductManager.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:0 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-hooks-useTelemetryLedger.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useTelemetryLedger.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:0 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 6 findings in sweep-providers-ComplianceGate.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-providers-ComplianceGate.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:2 L:3)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-screens-AuthScreen.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-screens-AuthScreen.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:1 M:2 L:1)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 17 findings in sweep-screens-DashboardScreen.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-screens-DashboardScreen.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:7 M:7 L:3)
  - [WAVE:4]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-screens-Onboarding
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-screens-Onboarding.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:2 M:2 L:4)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 9 findings in sweep-services-AppLogger.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-AppLogger.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 5 execution. Prerequisite: Wave 4 fully merged. (H:2 M:7 L:0)
  - [WAVE:5]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 47 findings in sweep-services-ble
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [H-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-ble.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:6 M:7 L:34)
  - [WAVE:4]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-services-BleWriteDispatcher.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-BleWriteDispatcher.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:1 L:4)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-services-CrewProfileService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-CrewProfileService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:1 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 25 findings in sweep-services-CrewService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-CrewService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:10 M:6 L:9)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 33 findings in sweep-services-DeviceRepository.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-DeviceRepository.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:2 M:2 L:29)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-services-GradientsService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-GradientsService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:2 L:5)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 12 findings in sweep-services-GroupRepository.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-GroupRepository.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:1 L:9)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 6 findings in sweep-services-LocationService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-LocationService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:2 M:1 L:3)
  - [WAVE:4]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 7 findings in sweep-services-NotificationService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-NotificationService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:0 L:5)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 7 findings in sweep-services-PermissionService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-PermissionService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 6 execution. Prerequisite: Wave 5 fully merged. (H:4 M:1 L:2)
  - [WAVE:6]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-services-PushTokenService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-PushTokenService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:2 M:0 L:0)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 9 findings in sweep-services-ScenesService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-ScenesService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:2 M:3 L:4)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 10 findings in sweep-services-session
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-session.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:1 M:1 L:8)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 18 findings in sweep-services-SpeedTrackingService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-SpeedTrackingService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:3 L:13)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-components-account
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-account.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:2 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 6 findings in sweep-components-CameraTracker.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-CameraTracker.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:3 L:3)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-components-CrewModal.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-CrewModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:2 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-components-CustomSlider.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-CustomSlider.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-DeviceSettingsModal.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-DeviceSettingsModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-GroupSettingsModal.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-GroupSettingsModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-NeonHueStrip.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-NeonHueStrip.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:0)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-PositionalGradientBuilder.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-PositionalGradientBuilder.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:0 M:1 L:0)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-SessionSummaryModal.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-SessionSummaryModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:0)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-shared
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-shared.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-VerticalPatternDrum.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-VerticalPatternDrum.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-context-CrewContext.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-context-CrewContext.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:0 M:1 L:0)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-context-ThemeContext.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-context-ThemeContext.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 6 execution. Prerequisite: Wave 5 fully merged. (H:0 M:1 L:0)
  - [WAVE:6]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useCuratedPicks.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useCuratedPicks.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-hooks-useDashboardGroups.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDashboardGroups.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 6 execution. Prerequisite: Wave 5 fully merged. (H:0 M:1 L:7)
  - [WAVE:6]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-hooks-useDashboardProfile.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDashboardProfile.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:0 M:1 L:3)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useDeviceStateLedger.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDeviceStateLedger.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-hooks-useDiagnosticLog.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDiagnosticLog.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:3)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-hooks-useMapFilters.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useMapFilters.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-hooks-useOptimisticBLE.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useOptimisticBLE.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-hooks-useProductCatalog.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useProductCatalog.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:0 M:1 L:3)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-hooks-useRecentSpots.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useRecentSpots.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-hooks-useSkateStats.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useSkateStats.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:0)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-hooks-useStreetMode.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useStreetMode.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:2 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-protocols-ZenggeProtocol.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [H-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-protocols-ZenggeProtocol.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:1 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-providers-BluetoothGuard.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-providers-BluetoothGuard.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:2 L:2)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-services-AppSettingsService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-AppSettingsService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:0 M:1 L:2)
  - [WAVE:4]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 7 findings in sweep-services-AuthProfileService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-AuthProfileService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:3 L:4)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-services-BleCharacteristicCache.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-BleCharacteristicCache.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:2)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-services-BleWriteQueue.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-BleWriteQueue.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:2 L:3)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-services-supabaseClient.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-supabaseClient.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 6 execution. Prerequisite: Wave 5 fully merged. (H:0 M:3 L:0)
  - [WAVE:6]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-styles-DashboardStyles.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-styles-DashboardStyles.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-theme-theme.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-theme-theme.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 5 execution. Prerequisite: Wave 4 fully merged. (H:0 M:1 L:0)
  - [WAVE:5]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-utils-migrateAuthTokens.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [L-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-utils-migrateAuthTokens.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:1 L:0)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-components-ProductVisualizer.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-ProductVisualizer.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:0 M:0 L:1)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 9 findings in sweep-context-AuthContext.tsx
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-context-AuthContext.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:0 M:0 L:9)
  - [WAVE:4]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-cloud
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-cloud.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useAdminSettings.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useAdminSettings.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:2)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useAdminTelemetry.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useAdminTelemetry.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:2)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useAppMicrophone.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useAppMicrophone.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:2)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 4 findings in sweep-hooks-useCrewManage.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useCrewManage.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:4)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-hooks-useDashboardDeviceConfig.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useDashboardDeviceConfig.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 10 findings in sweep-hooks-useFavorites.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useFavorites.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:10)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-hooks-useProtocolBuilder.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useProtocolBuilder.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-hooks-useRegistration.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useRegistration.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 7 execution. Prerequisite: Wave 6 fully merged. (H:0 M:0 L:8)
  - [WAVE:7]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-protocols-BanlanxAdapter.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [H-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-protocols-BanlanxAdapter.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-protocols-SpatialEngine.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [H-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-protocols-SpatialEngine.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:2)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-protocols-SymphonyEngine.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [H-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-protocols-SymphonyEngine.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-protocols-VisualizerEngine.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [H-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-protocols-VisualizerEngine.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:1)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 8 findings in sweep-services-BlePingService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-BlePingService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:8)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-services-BleSessionFactory.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-BleSessionFactory.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-services-HealthSyncService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-HealthSyncService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-services-SessionShareService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-SessionShareService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:0 M:0 L:1)
  - [WAVE:1]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-services-SkateSpotsService.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-services-SkateSpotsService.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 3 execution. Prerequisite: Wave 2 fully merged. (H:0 M:0 L:3)
  - [WAVE:3]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-types-supabase.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-types-supabase.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:0 M:0 L:1)
  - [WAVE:4]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-utils-BlePayloadParser.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [L-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-utils-BlePayloadParser.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 2 execution. Prerequisite: Wave 1 fully merged. (H:0 M:0 L:2)
  - [WAVE:2]
- [x] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 1 findings in sweep-utils-ColorUtils.ts
  - Tags: [G£à READY] [G£à AST-VERIFIED] [CORE] [L-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-utils-ColorUtils.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 4 execution. Prerequisite: Wave 3 fully merged. (H:0 M:0 L:1)
  - [WAVE:4]


- [x] **`sweep-src-components-account`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸ± Meal]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 6 telemetry and architectural rule violations in the components-account domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-account.md](./plans/PLAN-sweep-src-components-account.md)
    Key finding: "6 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-account.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-account.md)
  - **Details:** Orthogonal cluster safe for parallel verification.

- [/] **`sweep-src-components-DockedController.tsx`**
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸ± Meal]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 9 telemetry and architectural rule violations in the components-DockedController.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-DockedController.tsx.md](./plans/PLAN-sweep-src-components-DockedController.tsx.md)
    Key finding: "9 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-DockedController.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-DockedController.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification.
### âš¡ [BATCH:WAVE-2] â€” `wave-2-sweep` â€” READY
> **Worktree**: `wave-2-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 1 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis


- [x] **`sweep-src-components-admin`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 27 telemetry and architectural rule violations in the components-admin domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-admin.md](./plans/PLAN-sweep-src-components-admin.md)
    Key finding: "27 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-admin.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-admin.md)
  - **Details:** Orthogonal cluster safe for parallel verification.


- [x] **`sweep-src-components-CrewMemberDashboard.tsx`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-CrewMemberDashboard.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-CrewMemberDashboard.tsx.md](./plans/PLAN-sweep-src-components-CrewMemberDashboard.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-CrewMemberDashboard.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-CrewMemberDashboard.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification.


- [x] **`sweep-src-components-CrewModal.tsx`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 2 telemetry and architectural rule violations in the components-CrewModal.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-CrewModal.tsx.md](./plans/PLAN-sweep-src-components-CrewModal.tsx.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-CrewModal.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-CrewModal.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification.


- [x] **`sweep-src-components-LocationPicker.tsx`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 3 telemetry and architectural rule violations in the components-LocationPicker.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-LocationPicker.tsx.md](./plans/PLAN-sweep-src-components-LocationPicker.tsx.md)
    Key finding: "3 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-LocationPicker.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-LocationPicker.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification.


- [x] **`sweep-src-components-TacticalSlider.tsx`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 3 telemetry and architectural rule violations in the components-TacticalSlider.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-TacticalSlider.tsx.md](./plans/PLAN-sweep-src-components-TacticalSlider.tsx.md)
    Key finding: "3 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-TacticalSlider.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-TacticalSlider.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification.


- [x] **`sweep-src-hooks-ble`** ðŸš€ Merged in HEAD
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âš ï¸ H-RISK]` `[ðŸ± Meal]` `[ðŸ§  THINK]` `[BATCH:WAVE-1]` `[WAVE:1]`
  - **Goal:** Resolve 6 telemetry and architectural rule violations in the hooks-ble domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-hooks-ble.md](./plans/PLAN-sweep-src-hooks-ble.md)
    Key finding: "6 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-hooks-ble.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-hooks-ble.md)
  - **Details:** Orthogonal cluster safe for parallel verification.


- [x] **`sweep-src-components-patterns`** ðŸš€ Merged in 04d347dd
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 3 telemetry and architectural rule violations in the components-patterns domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-patterns.md](./plans/PLAN-sweep-src-components-patterns.md)
    Key finding: "3 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-patterns.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-patterns.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-VisualizerUnit.tsx`** ðŸš€ Merged in 0802db8c
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-VisualizerUnit.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-VisualizerUnit.tsx.md](./plans/PLAN-sweep-src-components-VisualizerUnit.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-VisualizerUnit.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-VisualizerUnit.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-utils`** ðŸš€ Merged in de7cd00f
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âœ… L-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 4 telemetry and architectural rule violations in the utils domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-utils.md](./plans/PLAN-sweep-src-utils.md)
    Key finding: "4 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-utils.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-utils.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-CameraTracker.tsx`** ðŸš€ Merged in 75e2f53d
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 4 telemetry and architectural rule violations in the components-CameraTracker.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-CameraTracker.tsx.md](./plans/PLAN-sweep-src-components-CameraTracker.tsx.md)
    Key finding: "4 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-CameraTracker.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-CameraTracker.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-NeonHueStrip.tsx`** ðŸš€ Merged in d13261c4
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 2 telemetry and architectural rule violations in the components-NeonHueStrip.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-NeonHueStrip.tsx.md](./plans/PLAN-sweep-src-components-NeonHueStrip.tsx.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-NeonHueStrip.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-NeonHueStrip.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-VerticalPatternDrum.tsx`** ðŸš€ Merged in 08972ff5
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 2 telemetry and architectural rule violations in the components-VerticalPatternDrum.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-VerticalPatternDrum.tsx.md](./plans/PLAN-sweep-src-components-VerticalPatternDrum.tsx.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-VerticalPatternDrum.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-VerticalPatternDrum.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-shared`** ðŸš€ Merged in 0401d5b7
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 2 telemetry and architectural rule violations in the components-shared domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-shared.md](./plans/PLAN-sweep-src-components-shared.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-shared.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-shared.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`

### âš¡ [BATCH:WAVE-3] â€” `wave-3-sweep` â€” READY
> **Worktree**: `wave-3-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 2 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis


- [x] **`sweep-src-components-CustomEffectVisualizer.tsx`** ðŸš€ Merged in e6398099
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-2]` `[WAVE:2]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-CustomEffectVisualizer.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-CustomEffectVisualizer.tsx.md](./plans/PLAN-sweep-src-components-CustomEffectVisualizer.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-CustomEffectVisualizer.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-CustomEffectVisualizer.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-GlobalErrorBoundary.tsx`** ðŸš€ Merged in 2f89923f
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-3]` `[WAVE:3]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-GlobalErrorBoundary.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-GlobalErrorBoundary.tsx.md](./plans/PLAN-sweep-src-components-GlobalErrorBoundary.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-GlobalErrorBoundary.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-GlobalErrorBoundary.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 2 fully merged into master before this worktree is created.`

### âš¡ [BATCH:WAVE-4] â€” `wave-4-sweep` â€” READY
> **Worktree**: `wave-4-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 3 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis


- [x] **`sweep-src-components-permissions`** ðŸš€ Merged in cbf8342b
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-3]` `[WAVE:3]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-permissions domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-permissions.md](./plans/PLAN-sweep-src-components-permissions.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-permissions.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-permissions.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 2 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-crew`**
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-4]` `[WAVE:4]`
  - **Goal:** Resolve 27 telemetry and architectural rule violations in the components-crew domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-crew.md](./plans/PLAN-sweep-src-components-crew.md)
    Key finding: "27 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-crew.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-crew.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 3 fully merged into master before this worktree is created.`


- [x] **`sweep-src-context`** ðŸš€ Merged in 7e429d64
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âœ… L-RISK]` `[ðŸ± Meal]` `[ðŸ§  THINK]` `[BATCH:WAVE-4]` `[WAVE:4]`
  - **Goal:** Resolve 15 telemetry and architectural rule violations in the context domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-context.md](./plans/PLAN-sweep-src-context.md)
    Key finding: "15 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-context.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-context.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 3 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-ConnectionStrengthBadge.tsx`** ðŸš€ Merged in 6acedcf5
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-4]` `[WAVE:4]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-ConnectionStrengthBadge.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-ConnectionStrengthBadge.tsx.md](./plans/PLAN-sweep-src-components-ConnectionStrengthBadge.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-ConnectionStrengthBadge.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-ConnectionStrengthBadge.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 3 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-PositionalGradientBuilder.tsx`** ðŸš€ Merged in 622e28b2
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-4]` `[WAVE:4]`
  - **Goal:** Resolve 3 telemetry and architectural rule violations in the components-PositionalGradientBuilder.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-PositionalGradientBuilder.tsx.md](./plans/PLAN-sweep-src-components-PositionalGradientBuilder.tsx.md)
    Key finding: "3 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-PositionalGradientBuilder.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-PositionalGradientBuilder.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 3 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-ProductVisualizer.tsx`** ðŸš€ Merged in 5f046332
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-4]` `[WAVE:4]`
  - **Goal:** Resolve 2 telemetry and architectural rule violations in the components-ProductVisualizer.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-ProductVisualizer.tsx.md](./plans/PLAN-sweep-src-components-ProductVisualizer.tsx.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-ProductVisualizer.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-ProductVisualizer.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 3 fully merged into master before this worktree is created.`

### âš¡ [BATCH:WAVE-5] â€” `wave-5-sweep` â€” READY
> **Worktree**: `wave-5-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 4 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis


- [x] **`sweep-src-services`** ðŸš€ Merged in 2647c760
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-5]` `[WAVE:5]`
  - **Goal:** Resolve 247 telemetry and architectural rule violations in the services domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-services.md](./plans/PLAN-sweep-src-services.md)
    Key finding: "247 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-services.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-services.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 4 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-CustomSlider.tsx`** ðŸš€ Merged in 0f881896
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-5]` `[WAVE:5]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-CustomSlider.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-CustomSlider.tsx.md](./plans/PLAN-sweep-src-components-CustomSlider.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-CustomSlider.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-CustomSlider.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 4 fully merged into master before this worktree is created.`

### âš¡ [BATCH:WAVE-6] â€” `wave-6-sweep` â€” READY
> **Worktree**: `wave-6-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 5 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis


- [x] **`extract-and-sweep-AppLogger.ts`** â€” merged @ 233f2c7c: AppLogger modular extraction completed
  - **Tags:** `[ðŸƒ IN PROGRESS]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-6]` `[WAVE:6]`
  - **Goal:** Extract logic and resolve violations in `AppLogger.ts` (>30KB monolith).
  - **Decision Log:** S4 Monolith limit prevents direct edits. AST analysis confirms it is a foundational dependency for DeviceRepository and CrewService.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-extract-and-sweep-AppLogger.ts.md](./plans/PLAN-extract-and-sweep-AppLogger.ts.md)
    Key finding: "Monolith >30KB. Must be extracted into `src/services/appLogger/` before dependent services are touched."
  - **Source of Truth:** ðŸ“– [PLAN-extract-and-sweep-AppLogger.ts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-extract-and-sweep-AppLogger.ts.md)
  - **Details:** Foundational layer extraction. `Prerequisite: Wave 5 merged.`

### âš¡ [BATCH:WAVE-7] â€” `wave-7-sweep` â€” READY
> **Worktree**: `wave-7-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 6 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” AST-Verified Dependents Extraction


- [x] **`sweep-src-hooks`** â€” merged @ 3cc9eecb: Hook architecture remediation completed
  - **Tags:** `[ðŸƒ IN PROGRESS]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âœ… L-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-6]` `[WAVE:6]`
  - **Goal:** Resolve 133 telemetry and architectural rule violations in the hooks domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-hooks.md](./plans/PLAN-sweep-src-hooks.md)
    Key finding: "133 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-hooks.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-hooks.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 5 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-dashboard`** â€” merged @ 1c2d20ed: Dashboard component remediation completed
  - **Tags:** `[ðŸƒ IN PROGRESS]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸ± Meal]` `[ðŸ§  THINK]` `[BATCH:WAVE-6]` `[WAVE:6]`
  - **Goal:** Resolve 8 telemetry and architectural rule violations in the components-dashboard domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-dashboard.md](./plans/PLAN-sweep-src-components-dashboard.md)
    Key finding: "8 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-dashboard.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-dashboard.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 5 fully merged into master before this worktree is created.`


- [x] **`sweep-src-components-CommunityModal.tsx`** â€” merged @ 33463686: CommunityModal extraction completed
  - **Tags:** `[ðŸƒ IN PROGRESS]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[ðŸš§ M-RISK]` `[ðŸª Snack]` `[ðŸ§  THINK]` `[BATCH:WAVE-6]` `[WAVE:6]`
  - **Goal:** Resolve 1 telemetry and architectural rule violations in the components-CommunityModal.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-components-CommunityModal.tsx.md](./plans/PLAN-sweep-src-components-CommunityModal.tsx.md)
    Key finding: "1 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-components-CommunityModal.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-CommunityModal.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 5 fully merged into master before this worktree is created.`

### âš¡ [BATCH:WAVE-7] â€” `wave-7-sweep` â€” READY
> **Worktree**: `wave-7-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 6 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis


- [x] **`extract-and-sweep-DeviceRepository.ts`** merged @ 69eac04f81e35463900eb66ff69c338388fd16a5
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-7]` `[WAVE:7]`
  - **Goal:** Extract logic and resolve violations in `DeviceRepository.ts` (>30KB monolith).
  - **Decision Log:** S4 Monolith limit prevents direct edits. AST analysis confirms it depends on AppLogger, requiring Wave 7 placement.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-extract-and-sweep-DeviceRepository.ts.md](./plans/PLAN-extract-and-sweep-DeviceRepository.ts.md)
    Key finding: "Monolith >39KB. Depends on AppLogger. Extract into `src/services/deviceRepository/`."
  - **Source of Truth:** ðŸ“– [PLAN-extract-and-sweep-DeviceRepository.ts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-extract-and-sweep-DeviceRepository.ts.md)
  - **Details:** Dependent layer extraction. `Prerequisite: Wave 6 fully merged into master before this worktree is created.`

- [x] **`extract-and-sweep-CrewService.ts`** merged @ 471549bc
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-7]` `[WAVE:7]`
  - **Goal:** Extract logic and resolve violations in `CrewService.ts` (>30KB monolith).
  - **Decision Log:** S4 Monolith limit prevents direct edits. AST analysis confirms it depends on AppLogger, requiring Wave 7 placement.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-extract-and-sweep-CrewService.ts.md](./plans/PLAN-extract-and-sweep-CrewService.ts.md)
    Key finding: "Monolith >31KB. Depends on AppLogger. Extract into `src/services/crewService/`."
  - **Source of Truth:** ðŸ“– [PLAN-extract-and-sweep-CrewService.ts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-extract-and-sweep-CrewService.ts.md)
  - **Details:** Dependent layer extraction. `Prerequisite: Wave 6 fully merged into master before this worktree is created.`

- [x] **`sweep-src-other`** merged @ e87803d9
  - **Tags:** `[âœ… READY]` `[ðŸ” CONFIRMED]` `[ðŸ§¹ TECH DEBT]` `[âœ… L-RISK]` `[ðŸ¥© Feast]` `[ðŸ§  THINK]` `[BATCH:WAVE-7]` `[WAVE:7]`
  - **Goal:** Resolve 56 telemetry and architectural rule violations in the other domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Â· Plan: [PLAN-sweep-src-other.md](./plans/PLAN-sweep-src-other.md)
    Key finding: "56 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** ðŸ“– [PLAN-sweep-src-other.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-other.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 6 fully merged into master before this worktree is created.`

### âš¡ [BATCH:WAVE-8] â€” `wave-8-sweep` â€” READY
> **Worktree**: `wave-8-sweep` Â· **Type**: Parallel Â· **Prerequisite**: Wave 7 merged
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) â€” Deep-Dive Code Hunt orthogonal analysis




- [x] **`sweep-src-components-docked`** merged @ 835ad6b7
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[🚧 M-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:WAVE-8]` `[WAVE:8]`
  - **Goal:** Resolve 10 telemetry and architectural rule violations in the components-docked domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-components-docked.md](./plans/PLAN-sweep-src-components-docked.md)
    Key finding: "10 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-components-docked.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-docked.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 7 fully merged into master before this worktree is created.`

### ⚡ [BATCH:WAVE-9] — `wave-9-sweep` — READY
> **Worktree**: `wave-9-sweep` · **Type**: Parallel · **Prerequisite**: Wave 8 merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — Deep-Dive Code Hunt orthogonal analysis


- [x] **`AccountModal-Extraction-Spike`** - Merged @ 4551c750: Extracted components and scrubbed PII telemetry.
  - **Tags:** `[✅ READY]` `[🚧 M-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:WAVE-9]` `[WAVE:9]`
  - **Goal:** Extract sub-sections of `AccountModal.tsx` (31KB monolith) into `src/components/account/` to comply with Rule S4.
  - **Decision Log:** Subagent execution halted perfectly on Rule S4 (Monolith File limit). Needs dedicated architectural extraction plan before modifying.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-components-AccountModal.tsx.md](./plans/PLAN-sweep-src-components-AccountModal.tsx.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-components-AccountModal.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-AccountModal.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 8 fully merged into master before this worktree is created.`


- [x] **`sweep-src-screens`** 🚀 Merged in 305b45c9
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[✅ L-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:WAVE-8]` `[WAVE:8]`
  - **Goal:** Resolve 35 telemetry and architectural rule violations in the screens domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-screens.md](./plans/PLAN-sweep-src-screens.md)
    Key finding: "35 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-screens.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-screens.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 7 fully merged into master before this worktree is created.`


- [x] **`feat/command-center-perf-charts`** 🚀 Merged in b3cff9de
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 FLASH]` `[WAVE:1]`
  - **Goal:** Replace two generic widgets in the Command Center with a grid of screen-level TTID/TTFD trend graphs.
  - **Decision Log:** Command Center lacked screen-specific latency diagnostics, rendering only high-level app averages which masked screen-hydration bottlenecks.
  - **Analysis:** 📊 Source: [PLAN-feat-command-center-perf-charts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-feat-command-center-perf-charts.md) · Plan: [PLAN-feat-command-center-perf-charts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-feat-command-center-perf-charts.md)
    Key finding: "Divergence of TTID and TTFD plotted on a shared timeline exposes the network/GATT latency gap."
    Rejected alternative: "Global React Navigation router tracing (causes log floods and lacks data-load correlation)."
  - **Source of Truth:** 📖 [AppPerformanceWidget.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/command-center/src/components/widgets/AppPerformanceWidget.tsx#L13) §AppPerformanceWidget
  - **Details:** Isolated dashboard component. Wave 1 parallel-safe. No prerequisites.


- [x] **`feat/deep-dive-fixes-wave1`** — 17902bb2 (Merged deep-dive protocol, PII, and async fixes)
  - **Tags:** `[✅ READY]` `[🕵️‍♂️ AUDIT]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:feat/deep-dive-fixes-wave1]` `[WAVE:1]`
  - **Goal:** Execute the unified fixes for deep-dive issues #1, #4, and #5.
  - **Decision Log:** Uncovered during deep-dive cartography. High risk issues affecting protocol compliance and memory stability.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/fb5fb761-e7be-4241-a902-3cb07dca3307/implementation_plan.md)
  - **Source of Truth:** `artifacts/system_audit_report.md`
  - **Details:** Must be done in a unified batch to prevent gatekeeper collisions.


- [x] **`feat/watch-bidirectional-bridge`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[NATIVE/BRIDGE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[H-COG]` 
  - **Goal:** Upgrade the iOS and Android watch companion bridges from one-way telemetry to bidirectional JSON command dispatchers.
  - **Decision Log:** Sending BLE payloads from the watch is banned due to OS background throttling and dual-master conflicts; the phone must act as the BLE gateway.
  - **Analysis:** 📊 Source: [watch_app_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/c32537a3-610e-4934-884a-37f7878eec17/watch_app_analysis.md) · Plan: [PLAN-feat-watch-bidirectional-bridge.md](./plans/PLAN-feat-watch-bidirectional-bridge.md)
    Key finding: "Both platforms limit commands to strict `START_SESSION`/`STOP_SESSION` string literal paths."
    Rejected alternative: "Connecting watch directly to skates via BLE — Rejected because it causes dual-master disconnects with the phone."
  - **Source of Truth:** 📖 [index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/src/index.ts) §WatchCommand
  - **Details:** The `addWatchCommandListener` will be mounted globally in `BleMachine.ts` to ensure commands execute when the phone is locked in the skater's pocket.



- [x] **`feat/rich-os-notifications`** 🚀 Merged in 1ac8e688
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[NATIVE/UI]` `[⚠️ H-RISK]` `[🥩 Feast]` `[H-COG]` `[BATCH:feat/rich-os-notifications]` `[WAVE:1]`
  - **Goal:** Upgrade standard session notifications to Android Custom RemoteViews and iOS Live Activities with headless BLE quick-actions.
  - **Decision Log:** Current notifications are static text; top-tier apps use ActivityKit and MediaStyle to present interactive telemetry lock-screen widgets that don't require bringing the UI to the foreground.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a87e0851-7797-4ede-a5a4-e9e88d68809d/implementation_plan.md) · Plan: [PLAN-feat-rich-os-notifications.md](./plans/PLAN-feat-rich-os-notifications.md)
    Key finding: "iOS Live Activities require dropping to SwiftUI; Android fits exactly 3 action buttons comfortably."
    Rejected alternative: "Bypassing the global BleWriteDispatcher for headless payload execution — Rejected to prevent GATT collisions."
  - **Source of Truth:** 📖 [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts#L79)
  - **Details:** Requires writing native code and an Expo Config Plugin for iOS Live Activities, and intercepting background events to call existing hooks invisibly.


- [x] **`feat/crewz-resilience`**
  - **Tags:** `[✅ READY]` `[☁️ CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[M-COG]` `[BATCH:feat-crewz-resilience]` `[WAVE:1]`
  - **Goal:** Implement Crewz Mode Resilience (Phases 1, 3, 4) with Global Persistent Foreground Service and byte array payloads.
  - **Decision Log:** Global persistent service chosen to act as Phone-as-Gateway to keep BLE and Supabase alive during background operation, similar to Watch app. Payload compression required to reduce latency.
  - **Source of Truth:** 📖 docs/plans/PLAN-feat-crewz-resilience.md
  - **Details:** Refactoring `CrewRealtime.ts` for payloads, replacing `NotificationService` with `GlobalForegroundService.ts`.


- [x] **`fix/web-console-crash`** 🚀 Merged in 513ed2f0
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI/WEB]` `[⚠️ H-RISK]` `[🍱 Meal]` `[M-COG]` `[BATCH:fix/web-console-crash]` `[WAVE:10]`
  - **Goal:** Fix `getEnforcing` TypeError on Web to unblock the web demo.
  - **Decision Log:** Self-healing observatory found 2 occurrences of this crash blocking the headless quality gate.
  - **Analysis:** 📊 Source: [report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/observatory/reports/2026-06-17/report.md) · Plan: [PLAN-fix-web-console-crash.md](./plans/PLAN-fix-web-console-crash.md)
    Key finding: "TurboModule getEnforcing is undefined on web."
    Rejected alternative: "Wait for upstream library fix."
  - **Source of Truth:** 📖 [report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/observatory/reports/2026-06-17/report.md)
  - **Details:** Web platform mock required. Prerequisite: None.

### ⚡ [BATCH:fix/observatory-db-drift] — `fix/observatory-db-drift` — IN PROGRESS
> **Worktree**: `fix/observatory-db-drift` · **Type**: Isolated · **Prerequisite**: None
> **Source Analysis**: 📊 [report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/observatory/reports/2026-06-17/report.md) — 3 database anomalies detected from remote logs.


- [x] **`fix/observatory-db-drift`** 🚀 Merged in 2732fa00
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[☁️ CLOUD]` `[⚠️ M-RISK]` `[🍱 Meal]` `[M-COG]` `[BATCH:fix/observatory-db-drift]` `[WAVE:10]`
  - **Goal:** Apply migrations for label_designs drift, integer overflow, and telemetry constraints.
  - **Decision Log:** Remote logs flagged 10+ errors for schema mismatches and failing telemetry inserts.
  - **Analysis:** 📊 Source: [report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/observatory/reports/2026-06-17/report.md) · Plan: [PLAN-fix-observatory-db-drift.md](./plans/PLAN-fix-observatory-db-drift.md)
    Key finding: "label_designs missing product_name column, and severity constraints are violating."
  - **Source of Truth:** 📖 [report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/observatory/reports/2026-06-17/report.md)
  - **Details:** Requires creating and applying new Supabase migrations.


- [x] **`fix/performance-telemetry`** 🚀 Merged in 828549b8
  - **Tags:** `[✅ READY]` `[❌ UNVERIFIED]` `[☁️ CLOUD]` `[⚠️ M-RISK]` `[🍿 Snack]` `[L-COG]` `[BATCH:fix/performance-telemetry]` `[WAVE:11]`
  - **Goal:** Whitelist `SCREEN_LOAD_TTID` and `SCREEN_LOAD_TTFD` in AppLoggerService to bypass the lossy 100ms queue.
  - **Decision Log:** Discovered that app hydration fires TTID concurrently with APP_OPENED, causing TTID to be silently overwritten in the single-slot `pendingLogQueue`.
  - **Analysis:** 📊 Plan: [PLAN-fix-performance-telemetry.md](./plans/PLAN-fix-performance-telemetry.md)
  - **Source of Truth:** 📖 [AppLoggerService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerService.ts#L175-L180)
  - **Details:** Add performance events to the immediate-push list.


- [x] **`fix/split-brain-trifecta`** 🚀 Merged in 61009d43
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:2]`
  - **Goal:** Resolve 3 split-brain architectural defects: GroupRepository read bypass, protocol dispatch hardcode, and crew stats race condition.
  - **Decision Log:** R-21 structural sniper + DOMAIN_PROTOCOL_CORE confirmed 3 disjoint code paths that should converge. GroupRepository is a write-only sink, useControllerDispatch hardcodes Zengge instead of using adapter registry, and useCrewSession duplicates lifetime stats writes causing race conditions.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-split-brain-trifecta.md](./plans/PLAN-fix-split-brain-trifecta.md)
    Key finding: "3 independent code paths bypass their canonical repositories"
    Rejected alternative: "Rewriting GroupRepository with RxJS observable streams — overengineered for current needs"
  - **Source of Truth:** 📖 [GroupRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts) §Architecture
  - **Details:** 5 files across 3 domains. Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`fix/promise-io-guards`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[CLOUD]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:2]`
  - **Goal:** Add try/catch guards and .catch() handlers to all unguarded async IO operations to prevent unhandled promise rejections.
  - **Decision Log:** R-11 sniper + DOMAIN_NOTIFICATIONS_&_ROUTING confirmed 15 unguarded async ops. 3 HIGH severity (AppConfigContext, AppLoggerStorage, LocationService) can crash silently and cause data loss.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-promise-io-guards.md](./plans/PLAN-fix-promise-io-guards.md)
    Key finding: "AppLoggerStorage uses console.warn instead of AppLogger.warn to prevent re-entrant logging loops"
    Rejected alternative: "Global unhandledrejection handler — masks bugs instead of fixing them"
  - **Source of Truth:** 📖 [AppConfigContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AppConfigContext.tsx#L25) §IO Safety
  - **Details:** 5 files, 15 locations. Prerequisite: Wave 1 fully merged into master before this worktree is created.

#### 🌊 Wave 3 — Hardening & Code Quality (Prerequisite: Wave 2 merged)


- [x] **`fix/ble-core-type-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:2]`
  - **Goal:** Eliminate all `any` type casts from BLE core production services and fix the Device/DisplayDevice type hierarchy.
  - **Decision Log:** R-08 sniper + DOMAIN_BLE_CORE confirmed 11 `any` casts in production BLE services + 9 `as unknown as` casts in DashboardScreen indicating broken type hierarchy. Plan makes `DisplayDevice extends Device` to eliminate casts at the type system level.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-ble-core-type-safety.md](./plans/PLAN-fix-ble-core-type-safety.md)
    Key finding: "DisplayDevice and Device type hierarchies are disjoint, causing 9 forced casts"
    Rejected alternative: "Adapter functions at each cast site — eliminated by making DisplayDevice extend Device"
  - **Source of Truth:** 📖 [BleMachine.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L18) §BLE Types
  - **Details:** 7 files, ~20 locations. Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`fix/timer-audit-ble`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[✅ L-RISK]` `[🥩 Feast]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:3]`
  - **Goal:** Replace hardcoded setTimeout/setInterval delays in the BLE pipeline with named constants from `bleTimingConstants.ts`.
  - **Decision Log:** R-16 sniper found 94 hardcoded delays. Plan writer confirmed BleWriteDispatcher is already migrated. 10 files need migration, pure refactor — zero behavioral changes.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-timer-audit-ble.md](./plans/PLAN-fix-timer-audit-ble.md)
    Key finding: "BleWriteDispatcher already migrated, 10 other BLE files still have magic numbers"
    Rejected alternative: "Runtime-configurable params — unnecessary for hardware-specific timing constants"
  - **Source of Truth:** 📖 [bleTimingConstants.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/bleTimingConstants.ts) §BLE Constants
  - **Details:** 10 files, ~26 new constants. Prerequisite: Wave 2 fully merged into master before this worktree is created.


- [x] **`fix/reentrant-handler-guards`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:3]`
  - **Goal:** Add re-entrancy guards (`isProcessing` ref pattern) to all async UI handler functions that interact with BLE or Supabase.
  - **Decision Log:** R-26 sniper found 28 unguarded async handlers. Double-tap on `handleStartScan`, `handleLeave`, `handleJoinByCode` causes BLE races, orphaned sessions, and duplicate database entries.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-reentrant-handler-guards.md](./plans/PLAN-fix-reentrant-handler-guards.md)
    Key finding: "Shared refs for mutually exclusive handlers within the same component"
    Rejected alternative: "Disabling buttons during processing — already exists as UI state, but ref guard is the synchronous first-line defense"
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx) §UI Safety
  - **Details:** 12 files, 28 handlers. Prerequisite: Wave 2 fully merged into master before this worktree is created.


- [x] **`fix/fsm-state-matrix`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🥩 Feast]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:3]`
  - **Goal:** Replace disjoint loading/error/success boolean state variables with unified FSM `ViewState` string unions across 20 files.
  - **Decision Log:** R-14 sniper found 38 boolean trap violations. 3-tier approach: full FSM conversion (10 files), hybrid cleanup (4 files), error normalization (6 files). Creates shared `ViewState` type.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-fsm-state-matrix.md](./plans/PLAN-fix-fsm-state-matrix.md)
    Key finding: "38 components use disjoint booleans instead of FSM string unions"
    Rejected alternative: "XState for UI views — overkill, string unions sufficient for non-BLE state"
  - **Source of Truth:** 📖 [useAccountOverview.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAccountOverview.ts#L70) §State Management
  - **Details:** 20 files in 3 tiers. Prerequisite: Wave 2 fully merged into master before this worktree is created.


- [x] **`fix/group-concurrent-write`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Replace `Promise.all(targets.map(...))` with sequential `for...of` loops in group BLE dispatch to prevent GATT write collisions.
  - **Decision Log:** Deep-dive R-10 sniper + DOMAIN_GROUP_SYNC + DOMAIN_BLE_CORE confirmed 11 concurrent write violations across 4 files. GATT controller expects sequential device-by-device dispatch — parallel writes cause collision and packet loss on grouped skates.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-group-concurrent-write.md](./plans/PLAN-fix-group-concurrent-write.md)
    Key finding: "7 Promise.all(targets.map) in useControllerDispatch.ts bypass write queue serialization"
    Rejected alternative: "Adding mutex locks in BleWriteQueue — unnecessary, queue already serializes per-device"
  - **Source of Truth:** 📖 [useControllerDispatch.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts#L94) §BLE Dispatch
  - **Details:** 4 files, 11 locations. BLE pipeline — wrong fix risks bricking skates.


- [x] **`fix/protocol-seq-counter`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Fix sequence counter split-ownership in `0x40` chunked payload framing and replace hardcoded 54-pixel max with dynamic `numLeds`.
  - **Decision Log:** DOMAIN_PROTOCOL_CORE confirmed BleWriteDispatcher uses `Math.random()` for seqNum while ZenggeProtocol has a proper incrementing counter. Protocol Bible §0x51 mandates `seqNum increments per save operation`. Split ownership = out-of-sequence frames = silently dropped pixels.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-protocol-seq-counter.md](./plans/PLAN-fix-protocol-seq-counter.md)
    Key finding: "Dual sequence counter increment causes frame drops on 0x40 chunked payloads"
    Rejected alternative: "TransitionType 0x00→CASCADE mapping — confirmed FALSE POSITIVE by plan writer"
  - **Source of Truth:** 📖 [ZenggeAdapter.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts#L167) §Protocol Core
  - **Details:** 4 files, 5 locations. Byte-level payload pipeline — wrong values = physical LED corruption.


- [x] **`fix/pii-scrub-sweep`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍪 Snack]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Wrap all unscrubbed PII (deviceId, name, crewName) in AppLogger calls with `scrubPII()` utility.
  - **Decision Log:** R-09 sniper found 16 instances of unscrubbed PII reaching cloud telemetry. Plan writer reduced to 14 confirmed fixes after dropping 2 false positives (DEV-gated log, pattern label string).
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-pii-scrub-sweep.md](./plans/PLAN-fix-pii-scrub-sweep.md)
    Key finding: "14 unscrubbed PII values in production AppLogger calls across 7 files"
    Rejected alternative: "N/A — find-and-replace pattern, no design alternatives"
  - **Source of Truth:** 📖 [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L115) §PII
  - **Details:** 7 files, 14 locations. Privacy/compliance fix.


- [x] **`fix/os-variance-parity`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Fix 3 OS variance parity violations: missing Android elevation and web-specific styles not wrapped in `Platform.select`.
  - **Decision Log:** R-20 sniper confirmed 3 verified violations. `countdownBadge` missing Android elevation causes invisible shadow. CustomSlider/TacticalSlider merge web-only styles on all platforms.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-os-variance-parity.md](./plans/PLAN-fix-os-variance-parity.md)
    Key finding: "3 style definitions with iOS/web shadows but no Android elevation"
    Rejected alternative: "N/A — straightforward style fixes"
  - **Source of Truth:** 📖 [DashboardStyles.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/styles/DashboardStyles.ts#L105) §Styles
  - **Details:** 3 files, 3 locations. Style-only changes.


- [x] **`fix/stale-flush-group-kill`** 🚀 Merged in 5b36ab8b
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 THINK]`
  - **Goal:** Remove the stale flush in ConnectService that kills Device A when Device B connects in a separate auto-connect batch, breaking group assembly.
  - **Decision Log:** User reported (2026-06-17) only 1 of 2 grouped devices connects despite strong RSSI on both. Code trace confirmed ConnectService L77 marks Device A as "stale" when Device B arrives in a separate batch. Stale flush was designed for fleet switching but has no legitimate caller — fleet switching is handled by DISCONNECT_REQUEST.
  - **Analysis:** 📊 Source: Live user testing 2026-06-17 + subagent code trace across 8 files. Plan: [PLAN-fix-stale-flush-group-kill.md](./plans/PLAN-fix-stale-flush-group-kill.md)
    Key finding: "ConnectService stale flush disconnects Device A when Device B's batch arrives because A is not in B's targetMacs"
    Rejected alternative: "Merge all group MACs into a single batch — rejected because BLE discovery timing is non-deterministic"
  - **Source of Truth:** 📖 [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L77-L107) §Stale Flush
  - **Details:** 2 files, ~15 lines. BLE connection pipeline — wrong fix risks breaking all GATT connections.



### 🌊 Parallel Wave Strategy (AST-Verified)

| Wave | Task Clusters | Parallel-Safe? | Prerequisite |
|------|--------------|----------------|--------------|
| **W-1** | C1 (Group Concurrent Write), C3 (Protocol Seq Counter), C7 (PII Scrub), C10 (OS Variance) | ✅ Yes — zero import-tree overlap | None |
| **W-2** | C2 (Split-Brain), C4 (BLE Type Safety), C5 (Promise IO Guards) | ✅ Yes — no shared files within wave | W-1 fully merged |
| **W-3** | C6 (Re-entrancy Guards), C8 (FSM State Matrix), C9 (Timer Audit BLE) | ✅ Yes — disjoint after W-2 merges | W-2 fully merged |


- [x] **`fix/logger-telemetry-hardening`** *(merged: edc5cd79 — AppLogger type safety, telemetry gating, re-entrancy guards, catch hardening)*
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:1]`
  - **Goal:** Fix type laundering, error swallowing, floating promises, and AsyncStorage key drift in logger pipeline.
  - **Decision Log:** R-07/R-08/R-11 — logger chain swallows errors silently, masking production failures.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-logger-telemetry-hardening.md](./plans/PLAN-fix-logger-telemetry-hardening.md)
  - **Source of Truth:** 📖 [AppLoggerCloud.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerCloud.ts)
  - **Details:** HIGH severity. 4 files. KnownDevice[] type alias, AppSettingsService gate, structural JSON serialization, loadingRef guard.


- [x] **`fix/dashboard-screen-safety`** — merged @ 830ef034 — R-08 DevicePatternState typed; R-16 5 constants extracted; R-17 listener leak fixed (require→cleanup path); R-20 Platform.select; R-25 BackHandler guard. R-27/R-28 SKIPPED with documented justification.
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Fix type laundering, OS variance violations, event listener leaks, and FlatList bottlenecks.
  - **Decision Log:** R-08 — 10+ `as unknown as` type laundering instances in DashboardScreen.tsx.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-dashboard-screen-safety.md](./plans/PLAN-fix-dashboard-screen-safety.md)
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
  - **Details:** HIGH severity. 3 files. Prerequisite: Wave 1 fully merged.



- [x] **`fix/camera-visualizer-safety`** — merged @ 73e369fa — R-04/R-11/R-25 hardened in CameraTracker; any→unknown in VisualizerHooks; dead import removed. Two findings SKIPPED with documented justification.
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Fix missing error handling, type laundering, and delete duplicate CustomEffectVisualizer component.
  - **Decision Log:** R-21 — CustomEffectVisualizer is functionally identical to LEDStripPreview (split-brain duplication).
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-camera-visualizer-safety.md](./plans/PLAN-fix-camera-visualizer-safety.md)
  - **Source of Truth:** 📖 [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)
  - **Details:** HIGH severity. 5 files (1 deletion). Prerequisite: Wave 1 fully merged.


- [x] **`fix/docked-controller-safety`** — merged @ 85ca319e — Phase 1: extracted FixedPatternPreviewRow.tsx, DockedController.styles.ts, useLoadFavorite.ts (68KB→58KB). Phase 2: R-16 hardcoded 50ms→BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS; R-26 isMusicBusyRef+isPatternBusyRef re-entrancy guards in applyFixedPattern+handleMusicChange; test mock useRef added.
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Extract 67KB DockedController monolith into sub-components, then fix re-entrancy races and context overload.
  - **Decision Log:** R-23 — DockedController.tsx at 67KB is a collision zone. S4 mandates extraction before editing.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-docked-controller-safety.md](./plans/PLAN-fix-docked-controller-safety.md)
  - **Source of Truth:** 📖 [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
  - **Details:** HIGH severity. Phase 1: extract components. Phase 2: fix violations. Prerequisite: Wave 2 fully merged.


- [x] **`fix/test-suite-type-safety`** — merged @ 95eaac5c — Created test-env.d.ts global types; purged all `as any` from 22 test files; typed useRef/useCallback mocks in useControllerDispatch.test; jest.config.js exclusion. TSC ✅ Jest 234 passed 32 suites ✅ All guards ✅
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🥩 Feast]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Purge all `any` casts from 22 test files. Create global type declarations for test environment.
  - **Decision Log:** R-08 — 60+ `any`/`as any` casts across test suite violate type safety rules.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-test-suite-type-safety.md](./plans/PLAN-fix-test-suite-type-safety.md)
  - **Source of Truth:** 📖 [BleMachine.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/BleMachine.test.ts)
  - **Details:** LOW severity. 22 test files + 1 new type declarations file. Prerequisite: Wave 2 fully merged.

#### 🌊 Wave 4 — Services & Cleanup (4 parallel tasks, prerequisite: Wave 3 merged)


- [x] **`fix/crew-services-hardening`** — merged @ 5201c152 — R-07 empty catch in heartbeat→AppLogger.warn; R-04 payload_size/ssi added to 4 error calls; R-11 silent catch in handleHandoffLeadership→AppLogger.warn; R-05 AsyncStorage offline cache for member list + optimistic UI for executeEndSession; R-16 5 timing constants extracted (BROADCAST_DEBOUNCE_MS, HEARTBEAT_INTERVAL_MS, CHANNEL_TEARDOWN_DELAY_MS, LEADER_BROADCAST_DEBOUNCE_MS, MEMBER_POLL_INTERVAL_MS). TSC ✅ Jest ✅ 8/8 gates ✅
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[☁️ CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix circular dependencies, error swallowing, floating promises, memory leaks, and offline-first violations across crew services.
  - **Decision Log:** R-29 — 3 circular dependency cycles in CrewService domain prevent clean imports.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-crew-services-hardening.md](./plans/PLAN-fix-crew-services-hardening.md)
  - **Source of Truth:** 📖 [CrewSessionManager.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService/CrewSessionManager.ts)
  - **Details:** HIGH severity. 8 files. Prerequisite: Wave 3 fully merged.


- [x] **`fix/scanner-ble-hooks`** — merged @ ec50a5f3 — R-07: 2 empty catches in useBLEScanner → AppLogger.error; R-08: useRef<any> → useRef<EventFrom> in useBLE; R-16: 3 raw ms values → BLE_TIMING constants (OPTIMISTIC_CONFIRM_RESET_MS, OPTIMISTIC_RECONCILE_RESET_MS, LEDGER_WRITE_DEBOUNCE_MS) in useOptimisticBLE+useDeviceStateLedger; bleTimingConstants.ts updated. TSC ✅ Jest ✅ 8/8 gates ✅
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix error swallowing, type safety, and hardcoded delays in BLE scanner and device state hooks.
  - **Decision Log:** R-07 — empty catch blocks in useBLEScanner silently drop scan errors.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-scanner-ble-hooks.md](./plans/PLAN-fix-scanner-ble-hooks.md)
  - **Source of Truth:** 📖 [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
  - **Details:** HIGH severity. 5 files. Prerequisite: Wave 3 fully merged.


- [x] **`fix/memory-leak-sweep`** — merged @ 85291fb7 — R-22: useEffect cleanup returns added to 6 components (AccountModal, CommunityModal, MarqueeText, ProductVisualizer, PatternCard, useAppMicrophone); R-20: Platform.select in SessionSummaryModal; R-12: stale closure guard in useAppMicrophone. TSC ✅ Jest ✅ 8/8 gates ✅
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix useEffect cleanup omissions, stale closures, and OS variance issues.
  - **Decision Log:** R-22 — 6 components with useEffect lacking cleanup functions, causing memory leaks on unmount.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-memory-leak-sweep.md](./plans/PLAN-fix-memory-leak-sweep.md)
  - **Source of Truth:** 📖 [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
  - **Details:** MEDIUM severity. 7 files. Prerequisite: Wave 3 fully merged.


- [x] **`fix/ui-misc-safety`** — merged @ 08d279f4 — R-02 Oracle53 error handling; R-08 any[] fix; R-16 hardcoded delay constants extracted.
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:4]`
  - **Goal:** Fix type safety, hardcoded delays, and fire-and-forget BLE streaming in miscellaneous UI components.
  - **Decision Log:** R-02 — Oracle53LiveStream violates playback engine model with fire-and-forget 0x53 frames.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-ui-misc-safety.md](./plans/PLAN-fix-ui-misc-safety.md)
  - **Source of Truth:** 📖 [Oracle53LiveStream.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/oracle/Oracle53LiveStream.tsx)
  - **Details:** LOW severity. 8 files. Prerequisite: Wave 3 fully merged.

#### 🌊 Wave 5 — Engine Refactor (1 solo task, prerequisite: Wave 4 merged)


- [x] **`refactor/spatial-pattern-engines`** — merged @ 178c0b5f — Phase 1: extracted shared/engineTypes.ts, shared/spatialMath.ts, shared/coordinateSystem.ts, shared/engineUtils.ts. spatial/effectProcessors.ts (70+ builders). SpatialEngine 61KB→28KB. Circular deps fully resolved. TSC ✅ Jest ✅
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:5]`
  - **Goal:** Resolve circular dependencies between pattern engine files and extract SpatialEngine (61KB) monolith.
  - **Decision Log:** R-23/R-29 — 3 circular dependency cycles + SpatialEngine exceeds 30KB S4 safety threshold.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-refactor-spatial-pattern-engines.md](./plans/PLAN-refactor-spatial-pattern-engines.md)
  - **Source of Truth:** 📖 [SpatialEngine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/SpatialEngine.ts)
  - **Details:** MEDIUM severity. 4 existing files + 5 new extracted modules. Prerequisite: Wave 4 fully merged.


- [x] **`fix/notifications-routing-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MED]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:2]`
  - **Goal:** Fix floating promises, missing error handling, and telemetry context gaps in notification/routing services.
  - **Decision Log:** R-11 — unprotected Supabase query in LocationService can crash the crew landing screen.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-notifications-routing-safety.md](./plans/PLAN-fix-notifications-routing-safety.md)
  - **Source of Truth:** 📖 [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
  - **Details:** MEDIUM severity. 4 files (includes App.tsx). Prerequisite: Wave 1 fully merged.

#### 🌊 Wave 3 — Architecture & Test Safety (5 parallel tasks, prerequisite: Wave 2 merged)


- [x] **`fix/onboarding-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Fix error swallowing, hardcoded delays, and OS variance in onboarding screens.
  - **Decision Log:** R-07/R-20 — empty catch in AuthScreen + Platform.OS ternaries instead of Platform.select.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-onboarding-safety.md](./plans/PLAN-fix-onboarding-safety.md)
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** MEDIUM severity. 2 files. Prerequisite: Wave 2 fully merged.


- [x] **`fix/crew-ui-types`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:deepdive-audit-mega-sweep]` `[WAVE:3]`
  - **Goal:** Replace all `any` type annotations in crew UI components with proper types.
  - **Decision Log:** R-08 — 8 `any` casts across 4 crew UI components.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1acead38-84ce-4b41-965b-8da5f5cf62ab/system_audit_report.md) · Plan: [PLAN-fix-crew-ui-types.md](./plans/PLAN-fix-crew-ui-types.md)
  - **Source of Truth:** 📖 [CrewCard.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewCard.tsx)
  - **Details:** MEDIUM severity. 4 files. Prerequisite: Wave 2 fully merged.


- [x] **`fix/group-connect-stale-devices`** ✅ merged @ `98cfd8ea` — fixed scanner staleness, MAC case, wizard group ID 🚀 Merged in 98cfd8ea
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]`
  - **Goal:** Fix group card failing to connect both devices and device cards connecting to stale/wrong peripherals.
  - **Decision Log:** User-reported: group card connects 0-1 devices instead of 2; device card taps feel stale or connect wrong device. Root causes: `seenMacsRef` one-shot gate prevents `allDevices` refresh, `buildOfflineGroupMap` MAC case mismatch, wizard `group_ids` slug vs `CustomGroup.id` timestamp mismatch.
  - **Analysis:** 📊 Source: [group_connection_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/group_connection_audit.md) · Plan: [PLAN-fix-group-connect-stale-devices.md](./plans/PLAN-fix-group-connect-stale-devices.md)
  - **Source of Truth:** 📖 [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L270-L315) §scanCallback + [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L93) §buildOfflineGroupMap


- [x] **`fix/ble-state-machine-deadends`** — merged @ b5338db6 — BleMachine dead-end states eliminated
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:fix/ble-connection-pipeline]` `[WAVE:1]`
  - **Goal:** Eliminate dead-end states and silent event drops in BleMachine — DISCONNECTING service, RECOVERING timeout, dual HEARTBEAT_FAIL handling, concurrent CONNECT_REQUEST queuing.
  - **Decision Log:** Audit found DISCONNECTING has no invoked service (H1), second HEARTBEAT_FAIL during RECOVERING silently drops the device (H4), CONNECT_REQUEST during CONNECTING is lost (H5), RECOVERING has no timeout (M2), permanently-failed devices stay in connectedDevices (M6).
  - **Analysis:** 📊 Source: [connection_pipeline_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_pipeline_audit.md) · Plan: [PLAN-fix-ble-state-machine-deadends.md](./plans/PLAN-fix-ble-state-machine-deadends.md)
    Key finding: "DISCONNECTING waits for DISCONNECT_COMPLETE but nothing in the codebase sends it — machine hangs forever"
    Rejected alternative: "Using FORCE_IDLE for disconnect — rejected because it doesn't perform GATT teardown"
  - **Source of Truth:** 📖 [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L236-L243) §DISCONNECTING
  - **Details:** 9 surgical edits across BleMachine.ts, BleMachine.types.ts, useBLE.ts. Touches state transitions only — service internals are Plan 2.


- [x] **`fix/connect-recovery-services`** — merged @ b5338db6 — ConnectService subscriptions + RecoveryService Phase 3/cap fixed
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:fix/ble-connection-pipeline]` `[WAVE:1]`
  - **Goal:** Fix orphaned monitor subscriptions in ConnectService, dead Phase 3 recovery, and Phase 1 attempt cap bug in RecoveryService.
  - **Decision Log:** Audit found monitorCharacteristicForService subscription never stored/cleaned (H2 — duplicate callbacks after recovery), RecoveryService Phase 3 is dead code because getSweepedDevice is never injected (H3), while-loop guard caps at 5 despite PHASE_1_MAX=12 (H7).
  - **Analysis:** 📊 Source: [connection_pipeline_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_pipeline_audit.md) · Plan: [PLAN-fix-connect-recovery-services.md](./plans/PLAN-fix-connect-recovery-services.md)
    Key finding: "Phase 3 passive sweeper-watch loops for 10 minutes doing nothing then fails — getSweepedDevice is never passed from BleMachine"
    Rejected alternative: "Removing Phase 3 entirely — rejected because passive scanner reconnect is the correct last-resort strategy"
  - **Source of Truth:** 📖 [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L38-L169) §Phase3
  - **Details:** 5 surgical edits across ConnectService.ts, RecoveryService.ts, BleMachine.ts (invoke input only). State transitions untouched — Plan 1 handles those.


- [x] **`fix/autoconnect-dashboard-stale`** — merged @ 8af1ad8f — Cloud MAC + stale ref + config key fixed
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:fix/autoconnect-dashboard-stale]` `[WAVE:2]`
  - **Goal:** Fix cloud sync MAC case mismatch in auto-connect, sync stale allDevicesRef, and correct renderItem deviceConfigs lookup key.
  - **Decision Log:** Audit found cloud sync path uses raw device_mac without .toUpperCase() (H6 — same class of bug we just fixed in buildOfflineGroupMap), allDevicesRef never synced after init (M1), renderItem looks up deviceConfigs by Supabase composite key instead of MAC (L4).
  - **Analysis:** 📊 Source: [connection_pipeline_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_pipeline_audit.md) · Plan: [PLAN-fix-autoconnect-dashboard-stale.md](./plans/PLAN-fix-autoconnect-dashboard-stale.md)
    Key finding: "Cloud sync path at L396 uses raw device_mac — same .toUpperCase() bug we just fixed in buildOfflineGroupMap"
    Rejected alternative: "Canonicalizing all MACs to lowercase — rejected because BLE Device.id is uppercase by convention on both platforms"
  - **Source of Truth:** 📖 [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L396-L405) §cloudSync
  - **Details:** 4 surgical edits across useDashboardAutoConnect.ts and DashboardScreen.tsx. Prerequisite: Wave 1 fully merged into master before this worktree is created.




##  ❄️ Icebox / Backburner (Manual Trigger Only)

### 🧹 Tech Debt & Upgrades
### ⚡ [BATCH:refactor/upgrade-expo-56] — `refactor/upgrade-expo-56` — IN PROGRESS
> **Worktree**: `refactor/upgrade-expo-56` · **Type**: Sequential · **Prerequisite**: None
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/fb5fb761-e7be-4241-a902-3cb07dca3307/implementation_plan.md) — User explicitly requested forcing a major dependency update (Expo 55->56) mid-release to resolve deep-tree js-yaml vulnerabilities.


- [x] **`feat/ble-scan-filter-uuid`** 🚀 Merged in a5cbcb17
  - **Tags:** `[✅ READY]` `[BLE]` `[🟢 L-RISK]` `[🍟 Snack]` `[🔽 LOW]` `[BATCH:feat/ble-excellence-w1]` `[WAVE:1]`
  - **Goal:** Add Service UUID filter to `startDeviceScan` so we only scan for Zengge/BanlanX controllers — required prerequisite for iOS background scanning.
  - **Decision Log:** Industry gap analysis showed iOS background scanning REQUIRES Service UUID filters or it silently fails. We already import ZENGGE_SERVICE_UUID but only use it for post-scan filtering.
  - **Analysis:** 🔍 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) 📝 Plan: [PLAN-feat-ble-scan-filter-uuid.md](./plans/PLAN-feat-ble-scan-filter-uuid.md)
    Key finding: "iOS background scanning requires Service UUIDs — without filter, Gap 1 is dead on arrival"
    Rejected alternative: "Scan all + filter in callback (current approach) — wasteful and iOS-incompatible"
  - **Source of Truth:** 📖 [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L6) §ZENGGE_SERVICE_UUID import
  - **Details:** Also check BleMachine.ts SCANNING state for the actual `startDeviceScan` call site.


- [x] **`feat/connection-state-badges`** 🚀 Merged in a5cbcb17
  - **Tags:** `[✅ READY]` `[UI]` `[BLE]` `[🟢 L-RISK]` `[🍟 Snack]` `[🔽 LOW]` `[BATCH:feat/ble-excellence-w1]` `[WAVE:1]`
  - **Goal:** Add per-device connection state badges (Connected/Connecting/Reconnecting/Disconnected/Out of Range) to device and group cards alongside existing RSSI meters.
  - **Decision Log:** Industry gap analysis showed we collect RSSI data and show signal bars but have zero connection state visibility. Users see "connected" or nothing — no "reconnecting" or "out of range" states.
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-feat-connection-state-badges.md](./plans/PLAN-feat-connection-state-badges.md)
    Key finding: "RSSI bars exist but connection state is binary — no transitional states shown"
    Rejected alternative: "Toast-only approach — insufficient for persistent state visibility"
  - **Source of Truth:** 📖 [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx#L107) §RSSI meters
  - **Details:** Creates NEW ConnectionStateBadge.tsx component. Modifies SkateGroupCard, DeviceItem, MySkatesSlab, DashboardScreen, dashboard.types.ts.


- [x] **`fix/ble-gatt-resource-cleanup`** 🚀 Merged in a5cbcb17
  - **Tags:** `[✅ READY]` `[BLE]` `[🟢 L-RISK]` `[🍔 Meal]` `[🟠 MED]` `[BATCH:feat/ble-excellence-w1]` `[WAVE:1]`
  - **Goal:** Add explicit GATT handle cleanup after `cancelDeviceConnection` to prevent Android GATT handle exhaustion over many connect/disconnect cycles.
  - **Decision Log:** Industry gap analysis identified missing GATT close() call. Android has ~7-8 GATT connection slots — without cleanup, handles leak and eventually refuse all connections.
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-fix-ble-gatt-resource-cleanup.md](./plans/PLAN-fix-ble-gatt-resource-cleanup.md)
    Key finding: "cancelDeviceConnection disconnects but doesn't free native GATT handles"
    Rejected alternative: "Rely on GC — Android BLE GC is unreliable for native handles"
  - **Source of Truth:** 📖 [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L20) §disconnectService
  - **Details:** Touches BleMachine.ts disconnectService and ConnectService.ts error paths only.


- [x] **`refactor/ble-gatt-operation-queue`**
  - **Tags:** `[✅ READY]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:feat/ble-excellence-w2]` `[WAVE:2]`
  - **Goal:** Upgrade BleWriteQueue to a universal BLE operation queue that serializes ALL GATT operations (writes, reads, descriptors) — preventing silent failures from concurrent Android GATT ops.
  - **Decision Log:** Industry gap analysis identified that heartbeat pings, recovery handshakes, and color writes can race on Android's single-threaded GATT stack. Nordic gold standard: serialize everything through one FIFO.
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-refactor-ble-gatt-operation-queue.md](./plans/PLAN-refactor-ble-gatt-operation-queue.md)
    Key finding: "BleWriteQueue only serializes writes — reads and descriptors bypass the queue entirely"
    Rejected alternative: "Per-device mutex — doesn't handle priority ordering"
  - **Source of Truth:** 📖 [BleWriteQueue.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts#L1) §Priority FIFO Write Queue
  - **Details:** Touches BleWriteQueue.ts, ConnectService.ts, HeartbeatService.ts, RecoveryService.ts, and all queue consumers. Prerequisite: Wave 1 fully merged.


- [x] **`feat/ble-connection-params`**
  - **Tags:** `[✅ READY]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:feat/ble-excellence-w2]` `[WAVE:2]`
  - **Goal:** Tune BLE connection parameters — request High priority during active control, switch to Balanced during idle to save battery.
  - **Decision Log:** Industry gap analysis showed we use default OS-negotiated parameters. Active control needs ~15ms interval, idle can use ~100ms — 6x battery savings during idle.
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-feat-ble-connection-params.md](./plans/PLAN-feat-ble-connection-params.md)
    Key finding: "Default connection params waste battery — idle timeout should switch to Balanced"
    Rejected alternative: "Always High priority — wastes battery for no benefit during idle"
  - **Source of Truth:** 📖 [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L154) §connectToDevice
  - **Details:** Touches ConnectService.ts and bleTimingConstants.ts. Prerequisite: Wave 1 fully merged.


- [x] **`feat/ble-autoconnect-passive`**
  - **Tags:** `[✅ READY]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 MED]` `[BATCH:feat/ble-excellence-w3]` `[WAVE:3]`
  - **Goal:** Use `autoConnect: true` for reconnections (not first-time connects) so the OS reconnects passively when device comes back in range.
  - **Decision Log:** Industry gap analysis: direct connect for first pairing (faster), autoConnect for subsequent reconnections (fire-and-forget, OS-managed).
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-feat-ble-autoconnect-passive.md](./plans/PLAN-feat-ble-autoconnect-passive.md)
    Key finding: "autoConnect=true lets OS handle passive reconnect — zero app involvement needed"
    Rejected alternative: "Always autoConnect — slower for first-time pairing (~30s vs ~2s)"
  - **Source of Truth:** 📖 [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L154) §connectToDevice
  - **Details:** Touches ConnectService.ts, BleMachine.ts (invoke input), BleMachine.types.ts. Prerequisite: Wave 2 fully merged.


- [x] **`feat/smart-group-health`**
  - **Tags:** `[✅ READY]` `[UI]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:feat/ble-excellence-w3]` `[WAVE:3]`
  - **Goal:** Add per-device connection health within groups — degraded mode banner, per-device status dots, and connected-only command dispatch.
  - **Decision Log:** Industry gap analysis: no consumer LED app does smart group degradation. This is our competitive edge — show which skate is down and keep the other running.
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-feat-smart-group-health.md](./plans/PLAN-feat-smart-group-health.md)
    Key finding: "When 1 of N devices drops, whole group appears broken — no degraded mode"
    Rejected alternative: "Group-level binary connected/disconnected — current approach, no granularity"
  - **Source of Truth:** 📖 [SkateGroupCard.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/SkateGroupCard.tsx#L51) §group power state logic
  - **Details:** Touches SkateGroupCard, MySkatesSlab, DashboardScreen, dashboard.types.ts. Prerequisite: Wave 2 fully merged.


- [x] **`feat/ble-background-reconnect`**
  - **Tags:** `[✅ READY]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:feat/ble-excellence-w4]` `[WAVE:4]`
  - **Goal:** Maintain BLE connection when app backgrounds — iOS state restoration + Android foreground service so skaters' lights keep working with phone in pocket.
  - **Decision Log:** Industry gap analysis: #1 UX gap. Every competitor (Govee, Hue, LIFX) maintains connection in background. Our app drops connection on background — lights stop responding.
  - **Analysis:** 📊 Source: [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) · Plan: [PLAN-feat-ble-background-reconnect.md](./plans/PLAN-feat-ble-background-reconnect.md)
    Key finding: "Phone in pocket = lights die — the single highest-impact UX gap"
    Rejected alternative: "Polling reconnect on foreground only — fails when user expects lights to just work"
  - **Source of Truth:** 📖 [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts) §BleManager init
  - **Details:** Creates NEW BackgroundBLEService.ts. Modifies useBLE.ts, useDashboardAutoConnect.ts, Info.plist, AndroidManifest.xml, app.json. REQUIRES dependency proposal for Android foreground service library. Prerequisite: ALL prior waves merged.


- [x] **`sweep/error-unwrapping`**
  - **Tags:** `[x]` `[QUAL]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Add e instanceof Error unwrapping to all catch blocks missing it.
  - **Decision Log:** R-06 is #2 most-violated rule with ~28 MEDIUM findings. Zero architectural risk.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C05-error-unwrapping.md](./plans/PLAN-sweep-C05-error-unwrapping.md)
  - **Source of Truth:** 📖 [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
  - **Details:** ~8 key files. Prerequisite: Wave 1 fully merged.


- [x] **`sweep/telemetry-context`**
  - **Tags:** `[x]` `[QUAL]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w3]` `[WAVE:3]`
  - **Goal:** Add { payload_size, ssi } context to all AppLogger.error/warn calls missing it.
  - **Decision Log:** R-04 is #3 most-violated rule. ~24 MEDIUM findings. Zero architectural risk.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C06-telemetry-context.md](./plans/PLAN-sweep-C06-telemetry-context.md)
  - **Source of Truth:** 📖 [Oracle53LiveStream.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/tabs/oracle/Oracle53LiveStream.tsx)
  - **Details:** ~7 key files. Prerequisite: Wave 2 fully merged.


- [x] **`sweep/hardcoded-delays`**
  - **Tags:** `[x]` `[BLE]` `[⚠️ M-RISK]` `[🍽️ Meal]` `[🎯 FOCUSED]` `[BATCH:sweep/deep-dive-w3]` `[WAVE:3]`
  - **Goal:** Replace inline setTimeout/delay() calls with named constants.
  - **Decision Log:** R-16 found 10 MEDIUM findings. Magic number delays in BLE pipeline.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C07-hardcoded-delays.md](./plans/PLAN-sweep-C07-hardcoded-delays.md)
  - **Source of Truth:** 📖 [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
  - **Details:** 3 files. Prerequisite: Wave 2 fully merged.


- [x] **`sweep/dashboard-extraction`**
  - **Tags:** `[✅ READY]` `[UI]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 COMPLEX]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract DashboardScreen.tsx (57.16KB) into sub-components below 30KB threshold.
  - **Decision Log:** #1 hotspot file — 17 findings across 9 rules. Blocks 4 downstream clusters (C5, C8, C10, C15).
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C02-dashboard-extraction.md](./plans/PLAN-sweep-C02-dashboard-extraction.md)
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
  - **Details:** Extract to DashboardHeader, DashboardDeviceList, DashboardCrewHub, DashboardPowerControls. Fix R-23/26/25/27/24/18/17/14.


- [x] **`sweep/protocol-monolith`**
  - **Tags:** `[✅ READY]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 COMPLEX]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract ZenggeProtocol.ts (54.55KB), SpatialEngine (59.28KB), effectProcessors.ts (35.02KB) below 30KB.
  - **Decision Log:** 3 protocol files exceed S4 monolith threshold. R-21 split-brain between useControllerDispatch and useProtocolDispatch.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C03-protocol-monolith.md](./plans/PLAN-sweep-C03-protocol-monolith.md)
  - **Source of Truth:** 📖 [ZenggeProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
  - **Details:** Extract to handler modules (static, dynamic, music, spatial). Fix split-brain.


- [x] **`sweep/docked-controller`**
  - **Tags:** `[✅ READY]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 COMPLEX]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract DockedController.tsx (57.09KB) into sub-components, reduce context depth.
  - **Decision Log:** 12 findings. Consumes 4 React Contexts directly and hardcodes ZenggeProtocol bypassing adapter registry.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C04-docked-controller.md](./plans/PLAN-sweep-C04-docked-controller.md)
  - **Source of Truth:** 📖 [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/DockedController.tsx)
  - **Details:** Extract panels (Color, Effect, Music, Header). Route through ControllerRegistry.


- [x] **`sweep/split-brain`**
  - **Tags:** `[✅ READY]` `[ARCH]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 COMPLEX]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Consolidate split-brain patterns in GroupRepository and useCrewSession.
  - **Decision Log:** R-21 found 3 split-brain hazards. useDashboardGroups bypasses GroupRepository reads; useCrewSession duplicates SpeedTrackingService stat updates.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C14-split-brain.md](./plans/PLAN-sweep-C14-split-brain.md)
  - **Source of Truth:** 📖 [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
  - **Details:** 2 files. Consolidate read paths and remove duplicate stat updates.


- [x] **`sweep/circular-deps`**
  - **Tags:** `[✅ READY]` `[ARCH]` `[⚠️ M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Break import cycles in appLogger chain and CrewService internal modules.
  - **Decision Log:** R-29 found 4 circular dependency cycles. appLogger→AppSettings→appLogger and 3 CrewService internal cycles.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C16-circular-deps.md](./plans/PLAN-sweep-C16-circular-deps.md)
  - **Source of Truth:** 📖 [appLogger/index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/index.ts)
  - **Details:** 6 files. Break cycles with lazy imports or interface extraction.


- [x] **`sweep/memory-leaks`**
  - **Tags:** `[✅ READY]` `[UI]` `[⚠️ M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Add useEffect cleanup return functions for event listeners and animation loops.
  - **Decision Log:** R-22 found 6 HIGH severity memory leaks in UI components.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C08-memory-leaks.md](./plans/PLAN-sweep-C08-memory-leaks.md)
  - **Source of Truth:** 📖 [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
  - **Details:** 6 component files. Prerequisite: Wave 1 fully merged.


- [x] **`sweep/accessibility-i18n`**
  - **Tags:** `[✅ READY]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Wrap all hardcoded accessibilityLabel strings with t() from i18n.
  - **Decision Log:** 9 UI components have hardcoded English accessibility labels.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C11-accessibility-i18n.md](./plans/PLAN-sweep-C11-accessibility-i18n.md)
  - **Source of Truth:** 📖 [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
  - **Details:** 8 files. Prerequisite: Wave 1 fully merged.


- [x] **`sweep/flatlist-perf`**
  - **Tags:** `[✅ READY]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Extract inline FlatList components to stable useCallback/useMemo references.
  - **Decision Log:** R-28 found 3 FlatList bottlenecks with inline components causing re-renders.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C15-flatlist-perf.md](./plans/PLAN-sweep-C15-flatlist-perf.md)
  - **Source of Truth:** 📖 [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
  - **Details:** 2 files. Prerequisite: Wave 1 fully merged.


- [x] **`sweep/reentrancy-guards`**
  - **Tags:** `[✅ READY]` `[UI]` `[⚠️ M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w3]` `[WAVE:3]`
  - **Goal:** Add isProcessingRef.current guards to async UI handlers missing them.
  - **Decision Log:** R-26 found 6 re-entrancy races. Double-tap on async handlers causes duplicate operations.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C09-reentrancy-guards.md](./plans/PLAN-sweep-C09-reentrancy-guards.md)
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** 4 files. Prerequisite: Wave 2 fully merged.


- [x] **`sweep/boolean-fsm`**
  - **Tags:** `[✅ READY]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w3]` `[WAVE:3]`
  - **Goal:** Replace scattered boolean states with string union types or FSM patterns.
  - **Decision Log:** R-18 found 6 boolean trap patterns. Impossible state combinations possible.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C17-boolean-fsm.md](./plans/PLAN-sweep-C17-boolean-fsm.md)
  - **Source of Truth:** 📖 [AuthFormSignUp.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignUp.tsx)
  - **Details:** 3 files. Prerequisite: Wave 2 fully merged.


- [x] **`sweep/storage-key-registry`**
  - **Tags:** `[✅ READY]` `[QUAL]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w4]` `[WAVE:4]`
  - **Goal:** Centralize all AsyncStorage keys to storageKeys.ts.
  - **Decision Log:** R-24 found 7 undocumented/inline AsyncStorage keys. Risk: key collision and drift.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C10-storage-key-registry.md](./plans/PLAN-sweep-C10-storage-key-registry.md)
  - **Source of Truth:** 📖 [storageKeys.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/storageKeys.ts)
  - **Details:** 3 files. Prerequisite: Wave 3 fully merged.


- [x] **`sweep/type-safety`**
  - **Tags:** `[✅ READY]` `[QUAL]` `[⚠️ M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w5]` `[WAVE:5]`
  - **Goal:** Replace as unknown as double casts and JSON laundering with proper type guards.
  - **Decision Log:** R-08 is #4 most-violated rule. 18 findings (10 HIGH). Type laundering masks runtime errors.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C13-type-safety.md](./plans/PLAN-sweep-C13-type-safety.md)
  - **Source of Truth:** 📖 [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
  - **Details:** ~8 files. Prerequisite: Wave 4 fully merged.


- [x] **`fix/gatt-conn-133-exception`** — GHOST TASK: already fixed in master. ConnectService.ts:199-206 handles GATT 133 with 3-attempt jittered backoff (GATT_CONNECT_BACKOFF_MS: [500,1500,4000]). Source files cited in plan (useBLEAutoRecovery.ts, useBLESweeper.ts) no longer exist — refactored away. Archived 2026-06-23.


- [x] **`fix/ble-disconnect-service`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:branch-salvage]` `[WAVE:2]`
  - **Goal:** Port DisconnectService GATT teardown actor + FEF3 UUID scan detection from salvaged backup branch — fixes orphaned GATT zombie connections (VS-005) and silent device drops on fresh install.
  - **Decision Log:** Branch audit 2026-06-23 found `DisconnectService.ts` (new file) and FEF3 UUID detection NOT in master despite being implemented in `temp-troubleshoot-backup` commit `a3146b5f`. GATT orphaning causes reconnect failures that appear as BLE hangs. Blocked on Wave 1 (`refactor/burn-down-audit-failures`) due to shared `useBLEScanner.ts` edit — both tasks modify that file.
  - **Analysis:** 📊 Branch audit 2026-06-23 · Plan: [PLAN-fix-ble-disconnect-service.md](./plans/PLAN-fix-ble-disconnect-service.md)
    Key finding: `DisconnectService.ts` exists on `temp-troubleshoot-backup@a3146b5f` but absent from master; FEF3_UUID detection missing from scanner.
    Rejected alternative: Merge `temp-troubleshoot-backup` as-is — 472 commits of drift, debug artifacts (logcat dumps, `.old.ts` files), emergency override code. Port surgical diff instead.
  - **Source of Truth:** 📖 `temp-troubleshoot-backup` commit `a3146b5f` · [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) DISCONNECTING state · [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts) scan UUID filter
  - **Details:** 3 files: CREATE `DisconnectService.ts`, MODIFY `BleMachine.ts` (actor wiring), MODIFY `useBLEScanner.ts` (FEF3 UUID only). Do NOT port RSSI threshold changes or debug artifacts.


- [x] **`feat/applogger-mmkv-storage`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[PERF]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[WAVE:1]`
  - **Goal:** Swap AppLoggerStorage from AsyncStorage to MMKV (JSI, synchronous), increasing telemetry capacity 10x (500→5000 entries) and eliminating async bridge overhead during BLE event bursts.
  - **Decision Log:** Original MMKV branch (`feat/telemetry-mmkv-upgrade`) was abandoned after master refactored AppLogger to modular `appLogger/` directory — the branch's monolithic `AppLogger.ts` target no longer exists. Intake 2026-06-23 created a fresh plan targeting `AppLoggerStorage.ts` directly. Dependency Proposal included in plan — user approval required before `npm install`.
  - **Analysis:** 📊 Branch audit 2026-06-23 · Plan: [PLAN-feat-applogger-mmkv-storage.md](./plans/PLAN-feat-applogger-mmkv-storage.md)
    Key finding: MMKV intent was valid but architecture changed under the branch. Fresh impl against `AppLoggerStorage.ts` is 1 file + package.json — cleaner than rebasing 723 commits of drift.
    Rejected alternative: Rebase `feat/telemetry-mmkv-upgrade` — 723 commits of drift, monolithic target file no longer exists, rebase cost exceeds re-implementation cost.
  - **Source of Truth:** 📖 [AppLoggerStorage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/AppLoggerStorage.ts) · [PLAN-feat-applogger-mmkv-storage.md](./plans/PLAN-feat-applogger-mmkv-storage.md)
  - **Details:** 2 files: `AppLoggerStorage.ts` + `package.json`. No collisions with any active worktree or Wave 1 task — may execute in parallel with `refactor/burn-down-audit-failures` if desired.


- [x] **`spike/watch-bridge-clean-install`** 🚀 Merged in 0e5ac05d
  - **Tags:** `[🕵️ SPIKE]` `[✅ VERIFIED]` `[BUILD]` `[M-RISK]` `[Snack]` `[🧠 FOCUSED]`
  - **Goal:** Determine whether the `"sk8lytz-watch-bridge": "file:modules/sk8lytz-watch-bridge"` dependency breaks `npm install` on a clean checkout, given the target dir is empty and gitignored.
  - **Decision Log:** Flagged by /deepdive-docs DEPENDENCY_AUDIT cartographer (2026-06-22). NOTE: local `npm install` + full `npm run verify` currently PASS — so this is verify-before-touch, not a confirmed break. Do NOT delete the dep blind.
  - **Source of Truth:** `package.json` deps `sk8lytz-watch-bridge` · `.gitignore:167` `modules/sk8lytz-watch-bridge/` (whole module gitignored) · dir empty on this checkout. Spike: confirm if a config plugin (`./plugins/withWearOsModule`, `@bacons/apple-targets`) generates it at prebuild, or if it must be committed/scaffolded for CI.


- [x] **`fix/hw-settings-segments-haloz`** 🚀 Merged in fe1f64ea
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Fix `hardwareSettingsHandler.ts:124` — read `payload[5]` for segments in the classic 0x63 binary response branch instead of hardcoding `segments: 1`.
  - **Decision Log:** Audit VERIFIED (CRITICAL) — HALOZ devices (product_id=163, 2 segments) receive `hwSettings.segments=1` after classic 0x63 binary response, breaking the ring visualizer. JSON-inner branch correctly reads `payload[5]`; binary branch does not.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-001, CRITICAL) · Plan: [PLAN-fix-hw-settings-segments-haloz.md](./plans/PLAN-fix-hw-settings-segments-haloz.md)
    Key finding: `hardwareSettingsHandler.ts:124` — `segments: 1` hardcoded while `payload[5]` holds the real value; JSON-inner branch (line 104) reads it correctly.
    Rejected alternative: Reading only in JSON branch — rejected; binary response is the primary path for HALOZ controllers.
  - **Source of Truth:** 📖 [hardwareSettingsHandler.ts:124](src/protocols/handlers/hardwareSettingsHandler.ts#L124)
  - **Details:** Phase 1 (mandatory pre-edit): audit full hwSettings pipeline for additional hardcoding (8 files). Phase 2: surgical fix at line 124.


- [x] **`fix/dispatcher-padding-dead-code`** 🚀 Merged in 79a027c0
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Remove unreachable `padStaticColorfulPayload` call in `BleWriteDispatcher.ts:51`; update comment to locate the real minimum-pixel enforcement at `staticColorHandler.setMultiColor:44`.
  - **Decision Log:** Audit VERIFIED (MEDIUM) — V2-wrapped payloads always start with `0x00`; the `payload[0] !== 0x59` guard permanently fires and returns unchanged. Real defense is `Math.max(12, ...)` in `setMultiColor:44`.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-002, MEDIUM) · Plan: [PLAN-fix-dispatcher-padding-dead-code.md](./plans/PLAN-fix-dispatcher-padding-dead-code.md)
    Key finding: `padStaticColorfulPayload` reads `payload[0]` but V2 wrapping happens upstream — guard is permanently true, call is dead code.
    Rejected alternative: Moving guard pre-wrap — rejected; enforcement is stronger at the source (`setMultiColor`).
  - **Source of Truth:** 📖 [BleWriteDispatcher.ts:51](src/services/BleWriteDispatcher.ts#L51)
  - **Details:** One-line removal + comment update. Zero runtime behavioral change.


- [x] **`fix/protocol-dispatch-mtu-guard`** 🚀 Merged in 91058b4b
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Add 0x51 MTU interception inside `_dispatchToDevices` (useProtocolDispatch) to prevent silent GATT drops of 323-byte 0x51 extended payloads via `executeProtocolResults`.
  - **Decision Log:** Audit VERIFIED (HIGH) — `executeProtocolResults → _executeProtocolResultsInternal` has no MTU check; `prepareForTransmission._mtu` is unused. A 323-byte characteristic write silently drops on all real MTUs. Fix mirrors existing guard in `executeRawPayload:114`.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-003, HIGH) · Plan: [PLAN-fix-protocol-dispatch-mtu-guard.md](./plans/PLAN-fix-protocol-dispatch-mtu-guard.md)
    Key finding: `_dispatchToDevices` routes 0x51 payloads to `executeProtocolResults` with no size check; both safe paths (`writeToDevice`, `executeRawPayload`) already guard — only this path is missing.
    Rejected alternative: Fixing `_executeProtocolResultsInternal` directly — rejected; fix belongs at the API layer.
  - **Source of Truth:** 📖 [useProtocolDispatch.ts:20](src/hooks/useProtocolDispatch.ts#L20)
  - **Details:** Add has-oversized-0x51 check + writeChunked routing before the existing `executeProtocolResults` call. Add `writeChunked` to dep array.


- [x] **`fix/adapter-chunking-comment`** 🚀 Merged in ad1055ad
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Replace false "prepareForTransmission() will automatically apply 0x40 fragmentation" JSDoc in `ZenggeAdapter.ts:182-183` with accurate chunking responsibility map.
  - **Decision Log:** Audit VERIFIED (HIGH) — `prepareForTransmission` takes `_mtu` (underscore-prefixed, intentionally unused) and returns result unchanged. False comment is the primary mechanism allowing F-003 to survive code review.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-004, HIGH) · Plan: [PLAN-fix-adapter-chunking-comment.md](./plans/PLAN-fix-adapter-chunking-comment.md)
    Key finding: `prepareForTransmission` `_mtu` parameter is unused; function is a pass-through.
    Rejected alternative: Implementing real chunking in `prepareForTransmission` — rejected; chunking belongs at the dispatcher layer where MTU context is available.
  - **Source of Truth:** 📖 [ZenggeAdapter.ts:182](src/protocols/ZenggeAdapter.ts#L182)
  - **Details:** Comment-only change. Zero logic impact. Unblocks safe code review of F-003.


- [x] **`fix/settled-mode-direction`** 🚀 Merged in 9d573667
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Fix direction byte inversion in `dynamicEffectHandler.setSettledMode:52` — `(direction === 1 ? 0 : 1)` → `(direction === 1 ? 1 : 0)`.
  - **Decision Log:** Audit VERIFIED (MEDIUM) — Protocol Bible §0x41: dir=0=forward, dir=1=reverse. Current code sends the opposite byte. Function is `@DEPRECATED` / DiagnosticLab only; no production callers.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-005, MEDIUM) · Plan: [PLAN-fix-settled-mode-direction.md](./plans/PLAN-fix-settled-mode-direction.md)
    Key finding: `(direction === 1 ? 0 : 1)` ternary inverts the bit; Protocol Bible confirms 1=reverse.
    Rejected alternative: Removing the deprecated function — rejected; deprecation doesn't justify removal in this scope.
  - **Source of Truth:** 📖 [dynamicEffectHandler.ts:52](src/protocols/handlers/dynamicEffectHandler.ts#L52)
  - **Details:** One-line ternary fix. Deprecated function; no production impact.


- [x] **`fix/static-color-handler-cleanup`** 🚀 Merged in ec3174eb
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Fix stale speed-range comment (lines 51-53) + `any` type violation (line 3) in `staticColorHandler.ts`.
  - **Decision Log:** Audit VERIFIED — F-006 (MEDIUM): comment claims hardware range 1-31 but 0xA3 chipset accepts 1-100 (oracle-confirmed; `ANIM_SPEED_MAX=100` in code is correct). F-008 (LOW): `let _appLogger: any` violates No-`any` Law; structural `AppLoggerLike` type resolves circular import.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-006 + F-008, MEDIUM+LOW combined) · Plan: [PLAN-fix-static-color-handler-cleanup.md](./plans/PLAN-fix-static-color-handler-cleanup.md)
    Key finding: Stale comment misleads speed debugging; `any` cast active in production.
    Rejected alternative: `// @ts-ignore` for logger type — rejected per No-`any` Law.
  - **Source of Truth:** 📖 [staticColorHandler.ts:3](src/protocols/handlers/staticColorHandler.ts#L3) + [staticColorHandler.ts:51](src/protocols/handlers/staticColorHandler.ts#L51)
  - **Details:** Same file — combined worktree. Two changes: narrow `any` type + correct speed comment.


- [x] **`fix/music-mode-dep-array`** 🚀 Merged in 428ff383
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:2]` `[⛔ BLOCKED BY: fix/protocol-audit Wave 1]`
  - **Goal:** Add `handleMusicChange` to `useEffect` dep array in `useMusicMode.ts:116` to prevent stale 0x73 config when `micSensitivity` or `brightness` change programmatically.
  - **Decision Log:** Audit VERIFIED (MEDIUM) — `handleMusicChange` useCallback (deps include sensitivity, brightness) is recreated on those changes, but the re-send useEffect holds a stale closure. Impact: programmatic brightness/sensitivity changes (session restore, parent state) dispatch stale 0x73 to hardware.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-007, MEDIUM) · Plan: [PLAN-fix-music-mode-dep-array.md](./plans/PLAN-fix-music-mode-dep-array.md)
    Key finding: `useEffect` dep array at line 116 missing `handleMusicChange`; `micSensitivity`/`brightness` changes produce stale closure.
    Rejected alternative: Adding sensitivity+brightness directly — rejected; adding `handleMusicChange` is the complete transitive fix.
  - **Source of Truth:** 📖 [useMusicMode.ts:116](src/hooks/useMusicMode.ts#L116)
  - **Details:** One dep added to the array. Wave 2 because `useMusicMode` imports `useProtocolDispatch` (modified in Wave 1 F-003).


- [x] **`fix/crew-broadcast-scene`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:crew-e2e]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-crew-broadcast-scene.md](./plans/PLAN-fix-crew-broadcast-scene.md)
  - **Goal:** Repair leader→member light sync end-to-end. Delete the dead `broadcastScene`/`onCrewSceneChange` path; expose+wire `broadcastPayload` as the leader broadcast; fix the member receiver to route the `number[]` payload via a new `applyCrewPayload` handle into `writeToDevice` (not `applyCloudScene`).
  - **Decision Log:** Reyes VERIFIED (HIGH) — broadcastScene is a no-op AND broadcastPayload had zero callers AND member receiver type-mismatched. User chose Scope A (full repair). See docs/analysis/crew-broadcast-scene-redundancy.md.
  - **Source of Truth:** PLAN-fix-crew-broadcast-scene.md §Files to Create/Modify (7 files: CrewRealtime.ts, CrewService.ts, types.ts, useCrewLeaderBroadcast.ts [delete], DockedController.tsx, useDashboardController.tsx, DashboardScreen.tsx).


- [x] **`fix/crew-membership-presence`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[DATA]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:crew-e2e]` `[WAVE:2]`
  - **Plan:** 📎 [PLAN-fix-crew-membership-presence.md](./plans/PLAN-fix-crew-membership-presence.md)
  - **Goal:** Leader sees members live + every member renders with real name/avatar. Emit the missing `member_update` broadcast on join/leave; wire the two `() => {}` leader callbacks to refresh member UI; rewrite the `CrewMemberDashboard` query off the non-existent `role` column + `user_profiles` implicit join (use existing `crew_members.display_name` + derived role + explicit avatar query). NO migration needed.
  - **Decision Log:** Reyes VERIFIED — 3 sub-bugs (no member_update sender, no-op callbacks, schema-mismatch query). Quinn recommends query-rewrite over migration (session table `crew_members` correctly has no role column; persistent `crew_memberships` is a different table). See docs/analysis/crew-subsystem-e2e-audit.md Flow 4.
  - **Source of Truth:** PLAN-fix-crew-membership-presence.md §Files to Create/Modify (6 files: CrewSessionManager.ts, CrewService.ts, useCrewSession.ts, DashboardCrewPanel.tsx, useDashboardCrew.ts, CrewMemberDashboard.tsx).


- [x] **`feat/crew-scheduled-server-side`** — merged `320bfd50` ✅ (⚠️ deferred Supabase deploy — see SESSION_LOG)
  - **Tags:** `[x]` `[✅ SPIKE CLEARED]` `[CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 PRO-HIGH]` `[BATCH:crew-e2e]` `[WAVE:3]`
  - **Plan:** 📎 [PLAN-feat-crew-scheduled-server-side.md](./plans/PLAN-feat-crew-scheduled-server-side.md)
  - **⛔ BLOCKED (Rule 5 — Unverified Task Spike Gate):** Run the SPIKE before execution — confirm (1) `pg_cron`+`pg_net` available on the Supabase project, (2) `CRON_SECRET` + service-role env in the edge runtime, (3) `crew_sessions.is_active` insert default. If pg_cron is unavailable the activation mechanism must be redesigned.
  - **Goal:** Server-side scheduled-crew activation: a `pg_cron` job (1-min cadence) → new edge function `activate-scheduled-crews` that flips due `status='scheduled'` sessions to active (service-role, idempotent) and sends Expo push to members; client join error-path + 15-min reminder + notification deep-link.
  - **Decision Log:** User chose server-side activation 2026-06-22. Reyes found scheduling entirely cosmetic (no activation; members can't join scheduled rows). Quinn chose pg_cron+pg_net→edge function (reuses proven push-batch code; cron can't reuse the JWT-gated notify-crew-session fn). See docs/analysis/crew-subsystem-e2e-audit.md Flow 2.
  - **Source of Truth:** PLAN-feat-crew-scheduled-server-side.md §Files to Create/Modify (2 NEW: activate-scheduled-crews/index.ts, 20260622000000_activate_scheduled_crews_cron.sql; + CrewSessionManager.ts, CrewScheduleScreen.tsx, useDashboardProfile.ts, DashboardScreen.tsx).

### 🧹 Epic: Deep-Dive QA Synthesis Sweep

> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — 55-agent orthogonal QA fleet. ~195 verified findings across 30 guardrails.

#### Batch Strategy Table (AST-Verified — `ast-parser.js --collision-matrix`)

| Wave | Clusters | Parallel-Safe? | Prerequisite |
|------|----------|---------------|-------------|
| **1** | C1 (BLE Queue), C2 (Dashboard), C3 (Protocol), C4 (Docked), C12 (Build), C14 (Split-Brain), C16 (Circular Deps) | ✅ 7 parallel | None |
| **2** | C5 (Error Unwrap), C8 (Memory Leaks), C11 (A11y), C15 (FlatList) | ✅ 4 parallel | Wave 1 merged |
| **3** | C6 (Telemetry), C7 (Delays), C9 (Re-Entrancy), C17 (Boolean→FSM) | ✅ 4 parallel | Wave 2 merged |
| **4** | C10 (Storage Keys) | Solo | Wave 3 merged |
| **5** | C13 (Type Safety) | Solo | Wave 4 merged |


- [x] **`sweep/ble-write-queue`**
  - **Tags:** `[x]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Route all direct writeCharacteristicWithoutResponseForDevice calls through BleWriteDispatcher.enqueue().
  - **Decision Log:** Deep-dive fleet found 7 R-01 violations — direct BLE writes bypassing queue lock in HeartbeatService, InterrogatorService, BlePingService. Risk: packet interleaving during concurrent ops.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C01-ble-write-queue.md](./plans/PLAN-sweep-C01-ble-write-queue.md)
  - **Source of Truth:** 📖 [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts#L46)
  - **Details:** 7 instances across 3 files. All must route through BleWriteDispatcher.


- [x] **`sweep/build-config`**
  - **Tags:** `[x]` `[BUILD]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Add missing Android 14+ foreground service permissions and fix stale web shim targets.
  - **Decision Log:** Missing FOREGROUND_SERVICE_HEALTH/DATA_SYNC permissions cause SecurityException crash on Android 14+. Critical runtime impact.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C12-build-config.md](./plans/PLAN-sweep-C12-build-config.md)
  - **Source of Truth:** 📖 [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js#L43)
  - **Details:** 2 files: app.config.js + metro.config.js. Quick fix, high impact.


- [x] **`refactor/burn-down-audit-failures`** — SUPERSEDED ✅ work already in master via subsequent development
  - **Tags:** `[x]` `[AUTH]` `[✅ L-RISK]` `[🍱 Meal]` `[BATCH:branch-salvage]` `[WAVE:1]`
  - **Decision Log (2026-06-23 — CLOSED):** Attempted gatekeeper merge. Rebase revealed `CrewService.ts` was deleted in master (extracted to modular `src/services/CrewService/` directory). Grep confirmed ALL goals already achieved: zero rogue `supabase.auth.getUser()` in services/hooks, `bleGateRef` gone, `AuthContext` has all 5 auth methods with proper Supabase types (better than branch's `unknown` types). Merging would be a regression. Branch `185d41d0` deleted. Wave 2 unblocked — no `useBLEScanner.ts` collision concern remains.


- [x] **`sweep/dashboard-extraction`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract DashboardScreen.tsx (57 KB) into 4 sub-components + consolidated FSM state hook, reducing the orchestrator below 25 KB.
  - **Decision Log:** 55-agent QA fleet flagged 17 findings in this file — R-23 monolith, R-26 missing re-entrancy guards, R-17 listener leak, R-24 inline storage key, R-18 boolean traps.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C02-dashboard-extraction.md](./plans/PLAN-sweep-C02-dashboard-extraction.md)
    Key finding: "DashboardScreen.tsx at 57 KB with 4 context consumers, unguarded BackHandler, and DeviceEventEmitter leak."
    Rejected alternative: "Incremental cleanup in-place — rejected because the file is over the 30 KB hard stop; surgical edits risk collision."
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx) §Full file
  - **Details:** Creates Dashboard/ sub-directory with 5 new files. BackHandler Platform.OS guard required for iOS safety. AsyncStorage key must be centralized to storageKeys.ts.


- [x] **`sweep/protocol-monolith`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract ZenggeProtocol.ts (55 KB), SpatialEngine (59 KB), and effectProcessors.ts (35 KB) into domain-scoped handler and processor modules below 30 KB each.
  - **Decision Log:** 3 HIGH R-23 monolith findings across protocol core; oversized files collide on every BLE change and block parallel development.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C03-protocol-monolith.md](./plans/PLAN-sweep-C03-protocol-monolith.md)
    Key finding: "ZenggeProtocol.ts 55 KB, SpatialEngine 59 KB, effectProcessors.ts 35 KB — all over the 30 KB hard stop."
    Rejected alternative: "Inline handler comments only — rejected because file size alone prevents safe surgical edits."
  - **Source of Truth:** 📖 [ZenggeProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts) §Full file
  - **Details:** ⚠️ Sage must verify at execution time — `staticColorHandler.ts` and `dynamicEffectHandler.ts` may already exist from prior protocol-audit work. Read the files directory before creating [NEW] files to avoid overwrite.


- [x] **`sweep/docked-controller`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Complete DockedController.tsx extraction — add DockedHeader sub-component and useDockedState context facade to reduce context consumption from 4 to 1.
  - **Decision Log:** R-23 monolith (57 KB), R-27 (4 context consumers) — 3 panels already extracted as BuilderPanel/ProEffectsPanel/MusicPanel per prior sessions; remaining gap is header + state consolidation only.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C04-docked-controller.md](./plans/PLAN-sweep-C04-docked-controller.md)
    Key finding: "DockedController.tsx 57 KB with 4 context consumers; Color/Effect/Music panels already extracted — only DockedHeader and useDockedState remain."
    Rejected alternative: "Full re-extraction — 3 panel SKIPPED annotations in plan confirm prior work; don't overwrite existing components."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx) §Full file
  - **Details:** Scope is smaller than original plan due to prior extractions. Route remaining ZenggeProtocol direct calls through ControllerRegistry.


- [x] **`sweep/split-brain`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[DATA]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Route useDashboardGroups reads through GroupRepository and remove duplicate lifetime_distance/speed stat updates in useCrewSession that bypass SpeedTrackingService.
  - **Decision Log:** R-21 split-brain (3 HIGH) — lifetime stats updated in two independent paths; any write to the wrong path causes permanent stat drift.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C14-split-brain.md](./plans/PLAN-sweep-C14-split-brain.md)
    Key finding: "useDashboardGroups bypasses GroupRepository; useCrewSession duplicates stat updates that SpeedTrackingService already owns."
    Rejected alternative: "Delete the duplicate path only — rejected because GroupRepository reads must be added first to avoid breaking the read path."
  - **Source of Truth:** 📖 [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts) + [useCrewSession.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts)
  - **Details:** 2 files only. Snack-sized but data-integrity critical — stat drift is silent and permanent.


- [x] **`sweep/circular-deps`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[ARCH]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Break 4 import cycles in the appLogger chain and CrewService internal modules via lazy imports or interface extraction.
  - **Decision Log:** R-29 circular dependencies (4 cycles) from 55-agent QA fleet — circular imports cause undefined-at-runtime failures that are non-deterministic and hard to reproduce.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C16-circular-deps.md](./plans/PLAN-sweep-C16-circular-deps.md)
    Key finding: "4 import cycles across appLogger (AppLoggerService ↔ AppSettingsService) and CrewService (CrewService ↔ CrewAutoRejoin ↔ CrewRealtime)."
    Rejected alternative: "Barrel re-export reorganization alone — rejected because it masks cycles rather than breaking them."
  - **Source of Truth:** 📖 [appLogger/index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/index.ts) + [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService/CrewService.ts)
  - **Details:** 6 files across 2 module clusters. Use madge or manual trace to confirm cycle elimination before committing.


- [x] **`fix/docked-duplicate-favorite-modal`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-docked-duplicate-favorite-modal.md](./plans/PLAN-fix-docked-duplicate-favorite-modal.md)
  - **Goal:** Eliminate the two simultaneously-rendered `FavoritePromptModal` instances so a favorite saves with the correct mode/state.
  - **Decision Log:** CRITICAL wiring audit finding — both `DockedController.tsx:1214` and `BuilderPanel.tsx:219` bind `visible` to the same `promptState==='NAMING_FAVORITE'` from shared `FavoritesContext`, with DIFFERENT `onSave` handlers; whichever wins corrupts the saved favorite.
  - **Analysis:** 📊 Source: Reyes C4 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "Two `<Modal visible={true}>` stack; `handleConfirmFavorite` (BUILDER nodes) vs `handleConfirmSaveFavorite` (activeMode color/pattern) race."
    Rejected alternative: "Leave it — rejected; user-facing data corruption on a core save path."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1214) + [BuilderPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/BuilderPanel.tsx#L219)
  - **Details:** Decide single ownership of the naming modal (likely keep DockedController's, drop BuilderPanel's, route BUILDER save through one `onSave`). File collision: shares `DockedController.tsx` with `fix/docked-stale-imperative-handle` + `chore/teardown-dead-code-sweep` — cannot share a parallel wave.


- [x] **`fix/docked-stale-imperative-handle`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-docked-stale-imperative-handle.md](./plans/PLAN-fix-docked-stale-imperative-handle.md)
  - **Goal:** Fix the `useImperativeHandle` dep array so externally-exposed `applyCloudScene`/`loadFavorite` don't run stale closures.
  - **Decision Log:** MED wiring audit finding — `DockedController.tsx:448-469` deps `[speed, brightness, writeToDevice, optimisticWrite]` omit `applyCloudScene` + `loadFavorite`; crew scene-apply, crew loadout sync, and voice commands invoke stale closures.
  - **Analysis:** 📊 Source: Reyes C4 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "BLE reconcile path is safe (separate `onReconcileRef`); only the exposed ref handle is stale."
    Rejected alternative: "Widen deps blindly — must add exactly the two missing callbacks + confirm they're stable-wrapped."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L448)
  - **Details:** Shares `DockedController.tsx` with the duplicate-modal fix + cleanup sweep — sequence these.


- [x] **`fix/dashboard-autoconnect-double-listener`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-dashboard-autoconnect-double-listener.md](./plans/PLAN-fix-dashboard-autoconnect-double-listener.md)
  - **Goal:** Remove the duplicate AppState `change` listener so `retriggerAutoConnect` fires once per foreground resume, not twice.
  - **Decision Log:** HIGH wiring audit finding — `retriggerAutoConnect` is registered both inline (`DashboardScreen.tsx:405-421`) AND in `useDashboardAutoConnect.ts:480-486`; only the 5s throttle masks the double-fire. Throttle removal → double scan + double connect-queue.
  - **Analysis:** 📊 Source: Reyes C2 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "Extraction moved the listener into the hook but left the original in the screen — classic forgot-to-delete teardown artifact."
    Rejected alternative: "Rely on the throttle — rejected; correctness must not depend on a debounce side-effect."
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L405) + [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L480)
  - **Details:** Keep the hook's listener (correct owner); remove the screen's inline duplicate + its ref bridge if now redundant. Shares `DashboardScreen.tsx` with the cleanup sweep.


- [x] **`fix/dashboard-flatlist-rerender`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-dashboard-flatlist-rerender.md](./plans/PLAN-fix-dashboard-flatlist-rerender.md)
  - **Goal:** Stabilize the device-list `renderItem` so FlatList stops re-rendering every cell on every RSSI update.
  - **Decision Log:** MED perf wiring finding — `DashboardDeviceList.tsx:76-104` wraps `renderItem` in `useCallback` with `[props]` (whole object, new ref each render), defeating memoization.
  - **Analysis:** 📊 Source: Reyes C2 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "Every BLE RSSI tick re-renders all device cells; regression vs pre-teardown."
    Rejected alternative: "Memoize the whole props object upstream — rejected; granular deps on the values renderItem actually reads is the correct fix."
  - **Source of Truth:** 📖 [DashboardDeviceList.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Dashboard/DashboardDeviceList.tsx#L76)
  - **Details:** Isolated to `DashboardDeviceList.tsx` — parallel-safe with the docked fixes.


- [x] **`refactor/break-circular-deps`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[ARCH]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-refactor-break-circular-deps.md](./plans/PLAN-refactor-break-circular-deps.md)
  - **Goal:** Break the 9 circular dependencies remaining in master (6 in the `deviceRepository` barrel chain, 3 in `components/docked/Universal*` siblings).
  - **Decision Log:** `madge --circular` (2026-06-26) reports 9 cycles NOT covered by C16 (which only scoped appLogger/CrewService). `useRegistration → deviceRepository/index → DeviceRepositoryService → {GroupRepository, DeviceCloudSync, DeviceStateManagement, DeviceStorage, types}` (6) + `UniversalSlidersFooter ↔ {UniversalColorGrid, UniversalHueStripSlider, UniversalTacticalSliders}` (3).
  - **Analysis:** 📊 Source: Reyes C14/C16 audit + madge run (SESSION_LOG 2026-06-25/26).
    Key finding: "C16 left these out of scope (correctly); they remain real load-order fragility — undefined-at-import risk."
    Rejected alternative: "Lump into C16 — rejected; different module clusters, different fix (barrel back-edge vs sibling component cycle)."
  - **Source of Truth:** 📖 [deviceRepository/index.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/deviceRepository/index.ts) + [docked/UniversalSlidersFooter.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
  - **Details:** Two independent clusters — could split into a 2-task wave. Verify with `npx madge --circular src/` returning zero before merge. `docked/Universal*` cluster shares the docked directory neighborhood with the C4 fixes.


- [x] **`chore/teardown-dead-code-sweep`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[REFACTOR]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LIGHT]` `[Friction: 1]` `[BATCH:teardown-fixes]` `[WAVE:2]`
  - **Plan:** 📎 [PLAN-chore-teardown-dead-code-sweep.md](./plans/PLAN-chore-teardown-dead-code-sweep.md)
  - **Goal:** Clear the dead-code / type / naming debt the C2/C3/C4/C14 teardowns left behind, in one surgical pass.
  - **Decision Log:** LOW-severity wiring-audit residue across 4 clusters — no runtime impact, but No-`any` Law gaps + dead surface that misleads future readers (the C3 agent's "stripped 11 imports + typed logger" was incomplete).
  - **Analysis:** 📊 Source: Reyes C2/C3/C4/C14 wiring audits (SESSION_LOG 2026-06-25).
    Key finding: "8 distinct dead/stale items; 2 surviving `_appLogger: any` in stateHandler/legacyHandler."
    Rejected alternative: "One task per item — rejected as board noise; all are same-domain mechanical cleanup."
  - **Source of Truth:** 📖 SESSION_LOG `[ARTIFACT] Reyes — *Wiring Audit*` entries (2026-06-25), each with file:line.
  - **Details / checklist:**
    - C3: strip 5 dead imports each from `staticColorHandler.ts:1`, `stateHandler.ts:1`, `legacyHandler.ts:1`; replace `let _appLogger: any` at `stateHandler.ts:3` + `legacyHandler.ts:3` with the `AppLoggerLike` interface (No-`any` Law).
    - C4: remove dead imports (`StyleSheet`, `Text`, `LinearGradient`, `Layout/Spacing/Typography`, `LOCAL_PRODUCT_CATALOG`) + dead `gaugeSize` var + dead destructures `activeQuickPresetIndex`/`saveQuickPreset` in `DockedController.tsx`.
    - C2: rename/clarify `Dashboard/DashboardHeader.tsx` (exports `DashboardHeaderBanners`, not the header); drop dead `DashboardCrewPanel` re-export at `DashboardCrewHub.tsx:61`; delete stale TODO at `DashboardScreen.tsx:23`.
    - C2: ⚠️ TRACE FIRST — `useProtocolDispatch()` dead call at `DashboardScreen.tsx:743`: confirm the hook has NO instantiation side-effects, then remove. If side-effectful → escalate to its own `[H-RISK]` bug, do NOT bundle.
    - C2: `createDashboardStyles` (`DashboardStyles.ts:398`) is `@deprecated "Removed in Wave 2"` yet sole call site at `DashboardScreen.tsx:97` and ignores `_Colors` — trace the 4 sub-components (`DashboardCrewPanel`, `MySkatesSlab`, `RegisteredFleetSlab`, `SupportModal`) for theme staleness, then remove or fix.
    - C14: remove dead `getGroupCount()` (`GroupRepository.ts:133`, no caller); add a guard comment on `useTelemetryLedger.injectStreetSummary` (dormant 2nd-table top-speed writer) naming the ownership boundary.
    - File collisions: touches `DockedController.tsx` (shared with both docked bug fixes) + `DashboardScreen.tsx` (shared with the autoconnect-listener fix) → must run AFTER those bugs merge, or fold into the same worktree.

### ⚡ [BATCH:branch-salvage] — Branch Audit Recovery Tasks (2026-06-23)
> **Source Analysis**: Branch hygiene audit 2026-06-23 — 4 orphaned branches inspected; 2 tech-debt tasks identified as unmerged work, 1 new feature identified.
> **Decision Log:** Branch audit discovered completed-but-unmerged work and salvageable code. These tasks close the gap before those branches are deleted.

#### Batch Strategy Table (AST-Verified)

| Wave | Task | Parallel-Safe? | Prerequisite | Collision Basis |
|------|------|---------------|-------------|-----------------|
| **1** | ~~`refactor/burn-down-audit-failures`~~ | — | — | SUPERSEDED — master already clean |
| **2** | ~~`fix/ble-disconnect-service`~~ ✅ merged `b3bd6abc` | Solo | None (Wave 1 superseded) | standalone — no collision |


- [x] **`fix/docked-loadfavorite-stale-handle`**
  - **Tags:** `[📝 NEEDS PLAN]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]`
  - **Goal:** Add `loadFavorite` to the `DockedController` `useImperativeHandle` dep array so crew/voice callers don't invoke a stale closure — requires safely relocating the `useLoadFavorite()` call above `useImperativeHandle`.
  - **Decision Log:** Follow-on from `fix/docked-stale-imperative-handle` (merged `edefc352`, 2026-06-26). `applyCloudScene` + `applySpatialSegments` were fixed; `loadFavorite` was DEFERRED because it's declared at `DockedController.tsx:561` (after the hook) and adding it to deps needs a hook reorder, which the monolith guardrail (L205-500) forbids without a focused, careful pass.
  - **Analysis:** 📊 Source: Reyes C4 audit + docked-pair Sage report (SESSION_LOG 2026-06-26).
    Key finding: "loadFavorite is a stable useCallback; crew/voice callers invoke post-mount → low live risk, but the ref handle is genuinely stale."
    Rejected alternative: "Reorder hooks inline during the Wave 1 fix — rejected; guardrail violation, needs its own scoped task."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L561) (useLoadFavorite call) + L448 (useImperativeHandle)
  - **Details:** Verify the `useLoadFavorite` relocation introduces no use-before-declaration of its own inputs. M-RISK because it moves a hook in a monolith — surgical, isolated, test the imperative-handle consumers (crew scene apply, voice).


- [x] **`sweep/devops-secrets`** ⚠️ EXECUTE FIRST — CREDENTIALS LIVE IN SOURCE
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[DEVOPS]` `[⚠️ H-RISK]` `[🍱 Meal]` `[HIGH]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:1]`
  - **Goal:** Remove hardcoded live Supabase JWT + plaintext password from `tools/createTestUser.js`, fix double-execution in `apply_migration.js`, replace 3 absolute machine paths.
  - **Decision Log:** 2026-06-30 audit found live credentials committed — `tools/createTestUser.js:4` (Supabase anon JWT `eyJ…`) + `:12` (plaintext `Password!2026`). Assume live until confirmed otherwise. MUST execute before any `git push`.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-DEVOPS_SECRETS · Plan: [PLAN-devops-secrets.md](./plans/PLAN-devops-secrets.md)
    Key finding: "Hardcoded live JWT at `tools/createTestUser.js:4` + double-run() at `tools/apply_migration.js:31` + 3 absolute paths."
    Rejected alternative: "Defer until next push — REJECTED. Credential sits in git history every day it is live."
  - **Source of Truth:** 📖 [tools/createTestUser.js](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/createTestUser.js#L4)
  - **Details:** Requires credential rotation in Supabase dashboard BEFORE the code change. `.env.local` placeholder must be created. Git history grep must confirm no prior JWT commits (out-of-scope BFG if found).


- [x] **`sweep/pii-offline-first`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[HIGH]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:1]`
  - **Goal:** Harden the AppLogger PII scrubber (exportJSON bypasses its own scrubber) and patch the offline-first cache gaps where Supabase calls skip AsyncStorage.
  - **Decision Log:** 2026-06-30 audit flagged `AppLogger.exportJSON()` bypassing its own PII scrubber — raw emails/MACs exported to disk in diagnostic bundles. Critical GDPR/user-trust gap.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-PII · Plans: [PLAN-pii-scrubber-hardening.md](./plans/PLAN-pii-scrubber-hardening.md) · [PLAN-pii-logger-scrubber.md](./plans/PLAN-pii-logger-scrubber.md)
    Key finding: "`AppLogger.exportJSON()` at `src/services/AppLogger.ts` bypasses `scrubPII()` — raw telemetry fields reach the export bundle unmasked."
    Rejected alternative: "Remove exportJSON entirely — REJECTED, debug utility needed. Fix: pipe through scrubber."
  - **Source of Truth:** 📖 [src/services/AppLogger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
  - **Details:** Two plans unified — scrubber hardening (AppLogger internals) + logger scrubber (call-site audit). Parallel-safe with `sweep/type-safety` (no shared files).
