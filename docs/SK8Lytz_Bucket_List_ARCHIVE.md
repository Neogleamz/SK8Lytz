### ⚡ [BATCH:deep-dive-remediation] — `deep-dive-remediation-batch` — 🟢 Completed

- [x] **`fix/auth-context-bypass`** - Merged 304b4d1f: Bypassed auth context in services.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[Services]` `[M-RISK]` `[Meal]` `[🤖 PRO-MED]`
  - **Plan:** 📎 [PLAN-auth-context-bypass.md](docs/plans/PLAN-auth-context-bypass.md)
  - **Source of Truth:** 📖 `src/services/CrewProfileService.ts` | `src/services/GroupRepository.ts` | `src/services/ScenesService.ts` | Audit: `R-15_findings.json`
  - **Goal:** Remove all 10 direct `supabase.auth.getUser()` / `getSession()` calls from service layer. Require callers to pass `userId` as a parameter sourced from AuthContext.
  - **Details:** 8 methods in CrewProfileService + GroupRepository.deleteGroup + ScenesService.flushSyncQueue all bypass AuthContext. Silent stale auth possible if token expires between calls.
- [x] **`qa/r06-r08-type-and-error-safety`**
- [x] **`qa/r11-r12-r16-async-and-closures`**
- [x] **`qa/r20-os-variance-parity-and-config`**
- [x] **`qa/r09-pii-scrubbing-leaks`**

### ⚡ [BATCH:error-handling-sweep] — 🟢 Completed

- [x] **`fix/type-safety-any-cast-phase1`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Banquet]` `[🤖 PRO-MED]`
  - **Details:** Phase 1: Fix structural `supabase as unknown as { rpc: any }` chain casts (2 files) + ProductManager interface `any[]` (6 violations) + `createStyles(Colors: any)` sweep. 294 total any-cast violations found. Phase 1 targeted the highest-structural-risk 30.

### ⚡ [BATCH:dep-diet-sweep] — 🟢 Completed

- [x] **`chore/dead-dependency-prune`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[Build]` `[L-RISK]` `[Snack]` `[🤖 PRO-LOW]`
  - **Details:** Removed 7 fully unutilized dependencies: `string-similarity`, `supercluster`, `jpeg-js`, `expo-speech`, `expo-image-manipulator`, `expo-blur`, and `react-native-nitro-image`. `react-native-vision-camera-worklets` and `react-native-nitro-modules` were retained as they inject necessary type definitions for Frame objects.

# SK8Lytz Master Bucket List Archive

This document contains the archive of all successfully completed and merged tasks, sprints, and epic batches within the SK8Lytz app ecosystem.

---

## 📦 ARCHIVED SPRINT LOGS

### Sprint: v3.9.1 — 2026-06-07 (ble-gatt-hardening)

### [BATCH:ble-gatt-hardening] (Complete)
- **Prerequisite**: None
- **Active Tasks**: 3 tasks

- [x] **`fix/ble-gatt-queue-hardening`** (Merged: 1f22f260)
  - **Source of Truth:** 📖 `src/services/BleWriteDispatcher.ts:141`, `src/services/BleConnectionManager.ts:252`, `src/hooks/ble/useBLEHeartbeat.ts:109` | Audit: `R-01_findings.json`, `R-13_findings.json`
  - **Goal:** Serialize all multi-device BLE GATT operations. Close the BleWriteQueue conditional bypass. Replace all `Promise.all(devices.map(...))` write paths with sequential `for...of` loops.
  - **Details:** R-01 found a conditional bypass in BleConnectionManager that allows direct `writeOp()` calls without queue protection. R-13 found 6 concurrent `Promise.all` write/disconnect patterns. Combined effect is GATT 133 error storms on multi-device operations. 7 files, 7 surgical edits.

- [x] **`fix/ble-pixel-buffer-clamp`** (Merged: 7156f1d4)
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[H-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Plan:** 📎 [PLAN-BLE-PIXEL-BUFFER-CLAMP.md](docs/plans/PLAN-BLE-PIXEL-BUFFER-CLAMP.md)
  - **Source of Truth:** 📖 `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx:178`, `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx:73` | Audit: `R-10_findings.json`
  - **Goal:** Enforce 12-pixel minimum for all `0x59` Static Colorful dispatches in diagnostic lab. Add `Math.max(12, pts)` guard to 5 diagnostic lab files.
  - **Details:** Hardware safety rule — payloads below 10 pixels cause physical EEPROM buffer lockouts on 0xA3 chipset. 5 files, 5 one-line guards.

- [x] **`fix/ble-jitter-backoff`** (Merged: 5f895783)
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[M-RISK]` `[Snack]` `[🤖 PRO-MED]`
  - **Plan:** 📎 [PLAN-BLE-JITTER-BACKOFF.md](docs/plans/PLAN-BLE-JITTER-BACKOFF.md)
  - **Source of Truth:** 📖 `src/hooks/useBLE.ts:131`, `src/hooks/useDashboardAutoConnect.ts:169`, `src/services/BleConnectionManager.ts:150` | Audit: `R-03_findings.json`
  - **Goal:** Add ±500ms jitter to all BLE reconnect retry timers to decoheres simultaneous multi-device reconnect stampedes.
  - **Details:** 3 fixed-interval retry paths with no jitter. Group reconnect after BLE drop causes synchronized retry burst every 1000ms, amplifying GATT 133 collisions. 1 utility function + 3 surgical edits.

---

### Sprint: v3.9.1 — 2026-06-07 (ble-p3-polish)

### [BATCH:ble-p3-polish] (Complete)
- **Prerequisite**: `[BATCH:ble-p2-architecture]` merged ✅
- **Active Tasks**: `ble/partial-group-connectivity-ui`, `ble/predictive-reconnection`

- [x] **`ble/partial-group-connectivity-ui`**
  - **Tags:** `[✅ DONE]` `[UI]` `[L-RISK]` `[Snack]` `[BATCH:ble-p3-polish]`
  - **Plan:** 📎 `PLAN-ble-partial-group-connectivity-ui.md`
  - **Outcome:** Replaced header connection string with interactive skate icons per group. Merged @ 9034fb44.
  - **Source of Truth:** 📖 [DashboardScreen.tsx:652-664] — auto-close guard; ghosted devices silently skipped by writeToDevice

- [x] **`ble/predictive-reconnection`** (CANCELLED)
  - **Tags:** `[❌ ICED]` `[CORE]` `[L-RISK]` `[Feast]` `[BATCH:ble-p3-polish]`
  - **Outcome:** Cancelled by user. Decided not to implement predictive reconnection to save complexity.


### Sprint: v3.7.3 Ã¢â‚¬â€ 2026-06-01 (camera-mode-white-only)

- [x] **`fix/camera-mode-white-only`** Ã¢â‚¬â€ Resolved white-only camera capture bug by utilizing useResizer from react-native-vision-camera-resizer to properly downscale frames on the GPU to a 50x50 RGB buffer. Merged `283774f7`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-camera-mode-white-only.md](./plans/PLAN-camera-mode-white-only.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx#L96) Ã‚Â§onFrame
  - **Goal:** Fix the camera frame processor crash that results in only capturing white colors.
  - **Details:** COMPLETE Ã¢â‚¬â€ Disposed GPUFrame inside finally block to prevent VRAM memory leaks. Set minimum channel offset of 3 to eliminate potential infinite loop hazards. Prioritized port 8081 for CDP console sniffer.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-29 (grouping-architecture-overhaul)

### [BATCH:grouping-architecture-overhaul] (Active) Ã¢â‚¬â€ Ã¢Å¡Â¡ Parallel-Safe
- **Prerequisite**: None
- **Active Tasks**: `feat/group-many-to-many`, `feat/group-mixed-state`, `fix/skatepark-hijack`, `fix/infinite-blob`, `fix/local-cloud-split-brain`

- [x] **`feat/group-many-to-many`** Ã¢â‚¬â€ Migrated device group membership to junction table. Merged `7e34ba7`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Feast]` `[Ã°Å¸Â¤â€“ THINK]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-group-many-to-many.md](./plans/PLAN-group-many-to-many.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
  - **Goal:** Resolve The "Mutually Exclusive" Flaw (1-to-1 Mapping).
  - **Details:** COMPLETE Ã¢â‚¬â€ Refactored `DeviceSettings` and Supabase schema to support a many-to-many relationship using array-based group mapping.

- [x] **`feat/group-mixed-state`** Ã¢â‚¬â€ Added mixed state rendering and tap-to-unify UI. Merged `21636ec`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-group-mixed-state.md](./plans/PLAN-group-mixed-state.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
  - **Goal:** Resolve Lack of "Mixed State" Aggregation.
  - **Details:** COMPLETE Ã¢â‚¬â€ Updated group UI cards to intelligently aggregate states using green/red status dots and introduced "Mixed - Tap to Sync" visual cue.

- [x] **`fix/skatepark-hijack`** Ã¢â‚¬â€ Added RSSI gating to background discovery. Merged `378366a7`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ THINK]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-skatepark-hijack.md](./plans/PLAN-skatepark-hijack.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L323)
  - **Goal:** Resolve The "Skatepark Hijack" (Proximity Blindness).
  - **Details:** COMPLETE Ã¢â‚¬â€ Introduced RSSI proximity gating and explicit user confirmation steps in `runAutoProvisioning` to prevent hijacking unregistered devices in public. Built an admin slider to adjust the threshold.

- [x] **`fix/infinite-blob`** Ã¢â‚¬â€ Implemented while loop to auto-increment group names and prevent appending. Merged `a48aaebe`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-infinite-blob.md](./plans/PLAN-infinite-blob.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L363)
  - **Goal:** Resolve The "Infinite Blob" (Accidental Mega-Groups).
  - **Details:** COMPLETE Ã¢â‚¬â€ Replaced `findIndex` with an auto-incrementing `while` loop so that multiple pairs of the same product type are placed into "My SK8Lytz HALOZ", "My SK8Lytz HALOZ 2", etc.

- [x] **`fix/local-cloud-split-brain`** Ã¢â‚¬â€ Fixed the UUID lookup failure in `saveGroupTransactional` by queuing cloud sync if devices are pending. Merged `d9bf414`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ THINK]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-local-cloud-split-brain.md](./plans/PLAN-local-cloud-split-brain.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [DeviceRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts#L488)
  - **Goal:** Resolve The Local/Cloud Split-Brain (MAC vs DB ID).
  - **Details:** COMPLETE Ã¢â‚¬â€ Implemented `hasPendingDevices` guard to bypass the RPC and delegate group mapping to `_queuePendingGroupSync` to prevent silent FK violations.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-27 (ble-hci)

### [BATCH:ble-hci] Ã¢â‚¬â€ Ã¢Å¡Â¡ (Complete)


- [x] **`spike/0x40-chunked-framing-hci-verify`** Ã¢â‚¬â€ Verified `writeChunked` 0x40 frame format on hardware. Merged `74ec886d`.
  - **Tags:** `[Ã°Å¸â€¢ÂµÃ¯Â¸Â SPIKE]` `[Ã¢Å“â€¦ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:ble-hci]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-chunked-ble-framing-0x51.md](./plans/PLAN-chunked-ble-framing-0x51.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L706) Ã‚Â§writeChunked and [ZENGGE_PROTOCOL_BIBLE.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L189) Ã‚Â§0x51 Custom Scene
  - **Goal:** HCI-sniff verify the `writeChunked` 0x40 fragmentation frame format on real HALOZ/SOULZ hardware before wiring the Scene Builder UI to production chunked writes.
  - **Details:** COMPLETE Ã¢â‚¬â€ Validated `writeChunked` behavior on physical hardware via ADB HCI sniff, matched byte frames, and updated ZENGGE_PROTOCOL_BIBLE.md.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-27 (dependency-diet)

### [BATCH:dependency-diet] Ã¢â‚¬â€ Ã¢Å¡Â¡ (Complete)

- [x] **`spike/major-dep-upgrades`** Ã¢â‚¬â€ Upgraded React to 19.2.6 and @types/react to 19.2.15. Merged `89825d9c`.
  - **Tags:** `[Ã°Å¸â€¢ÂµÃ¯Â¸Â SPIKE]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Feast]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:dependency-diet]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-spike-major-dep-upgrades.md](./plans/PLAN-spike-major-dep-upgrades.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json#L1)
  - **Goal:** Evaluate breaking changes for React and core dependencies.
  - **Details:** COMPLETE Ã¢â‚¬â€ Upgraded `react` and `react-dom` to 19.2.6, and `@types/react` to 19.2.15 while locking `react-native`, `typescript`, and `@react-native-async-storage/async-storage` to their stable versions to prevent BLE thread instability. Verified via full QA suite and merged.

---


### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-27 (monolith-cleanup)

### [BATCH:monolith-cleanup] Ã¢â‚¬â€ Ã¢Å¡Â¡ (Complete)

- [x] **`refactor/split-monolith-files`** Ã¢â‚¬â€ Monolith partitioned cleanly into 4 helper services + thin composition hook. Merged `48d35783`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:monolith-cleanup]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-refactor-split-monolith-files.md](./plans/PLAN-refactor-split-monolith-files.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [agent-behavior.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/agent-behavior.md#L15) Ã‚Â§Look Before You Leap
  - **Goal:** Break down monolithic files > 30KB discovered during audit.
  - **Details:** COMPLETE Ã¢â‚¬â€ Surgically extracted `BleConnectionManager`, `BleWriteDispatcher`, `BlePingService`, and `BleLifecycleManager` into separate stateless modules, and refactored `useBLE.ts` as a clean, thin orchestrator hook. Passed all unit, compiler, browser, and static quality checks.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-27 (ble-and-camera-hardening)

### [BATCH:ble-hardening] Ã¢â‚¬â€ Ã°Å¸â€œâ€¹ (Complete)

- [x] **`fix/sweeper-gatt-discovery-skip`** Ã¢â‚¬â€ Merged `971af30c`. Hoisted GATT discovery above cache lookup in interrogateDevice.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-hardening]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-fix-sweeper-gatt-discovery-skip.md](./plans/PLAN-fix-sweeper-gatt-discovery-skip.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useBLESweeper.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts#L250) Ã‚Â§interrogateDevice L250-268
  - **Goal:** Fix the third and final copy of the GATT discovery-on-cache-hit bug.
  - **Details:** COMPLETE Ã¢â‚¬â€ Hoisted `discoverAllServicesAndCharacteristics()` above the cache lookup block in interrogateDevice to ensure native handle maps populate before the `0x63` hardware settings query.

- [x] **`refactor/ble-session-factory`** Ã¢â‚¬â€ Merged `ffa980c8`. Extracted connectÃ¢â€ â€™discoverÃ¢â€ â€™resolve adapter sequence.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:ble-hardening]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-refactor-ble-session-factory.md](./plans/PLAN-refactor-ble-session-factory.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L670) Ã‚Â§handshakeDevice
  - **Goal:** Extract connectÃ¢â€ â€™discoverÃ¢â€ â€™resolve sequence from 4 callsites to eliminate Shotgun Surgery.
  - **Details:** COMPLETE Ã¢â‚¬â€ Created `BleSessionFactory` to own the invariant sequence `connect Ã¢â€ â€™ discoverAll Ã¢â€ â€™ resolveAdapter Ã¢â€ â€™ return { conn, adapter }` with AbortSignal support, purging ~120 duplicate lines from useBLE, useBLESweeper, and useBLEAutoRecovery.

- [x] **`refactor/ble-typed-fsm-gate`** Ã¢â‚¬â€ Merged `f8b1c07a`. Replaced raw string gates with compile-enforced FSM.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:ble-hardening]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-refactor-ble-typed-fsm-gate.md](./plans/PLAN-refactor-ble-typed-fsm-gate.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L61) Ã‚Â§bleGateRef
  - **Goal:** Replace raw string union refs with typed state machine.
  - **Details:** COMPLETE Ã¢â‚¬â€ Created `BleStateMachine` FSM with valid transition checks and state event listeners. Replaced `bleGateRef` usages in useBLE, useBLESweeper, useBLEAutoRecovery, and useDashboardAutoConnect to use `.tag` checks and strict transitions, completely avoiding split-brain states and GATT collisions.

### [BATCH:camera-hardening] Ã¢â‚¬â€ Ã¢Å¡Â¡ (Complete)

- [x] **`fix/camera-tracker-5hz-sideeffect`** Ã¢â‚¬â€ Merged `459319ad`. Decoupled 5Hz updates using shared React Ref.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:camera-hardening]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-fix-camera-tracker-5hz-sideeffect.md](./plans/PLAN-fix-camera-tracker-5hz-sideeffect.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx#L103) Ã‚Â§dispatchSniperColor
  - **Goal:** Remove 5Hz BLE side-effects.
  - **Details:** COMPLETE Ã¢â‚¬â€ Destructured `liveColorRef` in `CameraTrackerProps` and updated it at 5Hz inside the frame processor, bypassing parent re-renders. Replaced the 5Hz callback inside `CameraPanel` with a no-op, reading from `liveColorRef.current` strictly on Shutter Capture. Passed the verification QA suite cleanly.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-27 (camera-v2)

### [BATCH:camera-v2] Ã¢â‚¬â€ Ã¢Å¡Â¡ (Complete)

- [x] **`feat/camera-vibe-catcher-v2`** Ã¢â‚¬â€ Complete Camera Vibe Catcher v2 redesign using VisionCamera v5 and K-Means. Merged `24cb371`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[H-RISK]` `[Feast]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:camera-v2]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-camera-vibe-catcher-v2.md](./plans/PLAN-camera-vibe-catcher-v2.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [Image.nitro.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/node_modules/react-native-nitro-image/src/specs/Image.nitro.ts#L138) Ã‚Â§crop() API, [PreviewView.nitro.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/node_modules/react-native-vision-camera/src/specs/views/PreviewView.nitro.ts#L112) Ã‚Â§takeSnapshot @platform Android, [useDockedControllerState.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDockedControllerState.ts#L8) Ã‚Â§BuilderNode interface
  - **Goal:** Complete camera mode restart: replace broken `takeSnapshot()` + Nitro Image pipeline with cross-platform `useFrameProcessor` + `vision-camera-resize-plugin`. Add SNIPER (center-pixel solid color) and VIBE (K-Means 3-color palette Ã¢â€ â€™ auto-Builder payload) dual sub-modes.
  - **Details:** COMPLETE Ã¢â‚¬â€ Migrated to the VisionCamera v5 API utilizing the GPU-backed `useFrameOutput` and `vision-camera-resize-plugin` (50x50 RGB scaling). Added SNIPER center-pixel sample targeting the 25x25 center element and VIBE K-Means (k=3, 5 iterations) clustering to automatically map dominant swatches to FG/BG/ACCENT. Wired static and flow custom spatial patterns to `0x59` BLE commands with padded 12-pixel buffer overflow protection. Files changed: CameraTracker.tsx, CameraPanel.tsx, DockedController.tsx, package.json, kMeansPalette.ts, and kMeansPalette.test.ts. Passed all local and E2E QA checks cleanly.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-27 (permission-gating)

### [BATCH:permission-gating] Ã¢â‚¬â€ Ã¢Å¡Â¡ (Complete)

- [x] **`feat/strict-permission-gating`** Ã¢â‚¬â€ Fixed 3 permission bugs + reactive dock icon gating. Merged `51104a3`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:permission-gating]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-strict-permission-gating.md](./plans/PLAN-strict-permission-gating.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `PermissionService.ts`, `DockedDock.tsx`, `DockedController.tsx`, `app.json`
  - **Goal:** Fix 3 permission bugs + enforce strict reactive UI gating for CAMERA/STREET dock icons.
  - **Details:** COMPLETE Ã¢â‚¬â€ BUG-1: Added ACTIVITY_RECOGNITION to app.json android.permissions. BUG-2: Gated CAMERA mode behind checkPermission + openGlobalPermissionsModal. BUG-3: Gated STREET mode behind LOCATION check. Added reactive dock visibility via PERMISSION_STATUS_CHANGED_EVENT + AppState listener. Favorite restore for CAMERA falls back to MULTIMODE if denied. Files changed: app.json, PermissionService.ts, DockedController.tsx, DockedDock.tsx.

- [x] **`feat/offline-guest-gating`** Ã¢â‚¬â€ Gated cloud publishing and community scenes behind offline mode. Merged `ccf4f2f`.
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:permission-gating]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-offline-guest-gating.md](./plans/PLAN-offline-guest-gating.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ `Supabase` Session State and UI Layouts
  - **Goal:** Implement an "Offline/Guest" mode that selectively hides cloud-dependent UI features.
  - **Details:** COMPLETE Ã¢â‚¬â€ Propagated `isOfflineMode` through `DashboardScreen`, `DockedController`, `QuickPresetModal`, and `CommunityModal` to hide all cloud-based features and default to local "My Saves" appropriately. Passed QA Suite verification.

---

### Sprint: v3.6.3 Ã¢â‚¬â€ 2026-05-26 (camera-color-swatch-redesign)

- [x] **`feat/camera-color-swatch-redesign`** Ã¢â‚¬â€ Platform-split color analysis (Android TextureView + iOS worklet), center crop, neutral white snap, and 5-swatch UI palette. Merged `7c254f60`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:camera-color-swatch-redesign]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-camera-color-swatch-redesign.md](./plans/PLAN-camera-color-swatch-redesign.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L306) Ã‚Â§0x59 Static Colorful Pixel Array
  - **Goal:** Split Android & iOS camera tracking, implement center crop & neutral snap gate, and build a 5-swatch tactile UI.
  - **Details:** COMPLETE Ã¢â‚¬â€ Successfully split the CameraTracker hook into Android (TextureView takeSnapshot) and iOS (worklet useFrameOutput) platforms, resolving the Android react-native-worklets crash. Implemented a 32x32 center crop with 1x1 resize to average color over reticle rather than whole frame. Added a delta < 0.15 neutral white snapping gate to stop noisy green/blue drift. Completely redesigned CameraPanel to support a scrollable row of up to 5 captured tactile color swatches. Passed all verifiable checks cleanly.

---

### Sprint: v3.6.2 Ã¢â‚¬â€ 2026-05-26 (music-mode-fix)

- [x] **`fix/music-mode-payload-cap`** Ã¢â‚¬â€ Capped magnitude stream to 150 and disabled built-in mic when App mic active. Merged `99550a0`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:music-mode-fix]`
  - **Plan:** Ã°Å¸â€œÅ½ [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity-ide/brain/3f09133f-0b00-4051-bb43-79e27aa0f099/implementation_plan.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L773) Ã‚Â§Music Mode and [MusicModeFragment.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/com/zengge/wifi/activity/NewSymphony/fragment/MusicModeFragment.java#L595)
  - **Goal:** Fix the `isOn` microphone toggle routing and scale magnitude limits to 150 to stop ambient visualizer pattern cycling.
  - **Details:** COMPLETE Ã¢â‚¬â€ Capped real-time App mic magnitude stream to 150 (down from 255) to prevent physical controller saturation/visualizer lockout, and corrected the 0x73 'isOn' byte routing to dynamically toggle off the hardware microphone in App Mic mode.

---

### Sprint: v3.9.0 Ã¢â‚¬â€ 2026-05-26 (rf-remote)

- [x] **`spike/rf-remote-2.4g-settings`** Ã¢â‚¬â€ Corrected RF remote state opcodes and integrated DeviceSettingsModal into DashboardScreen. Merged `f94a0b5d`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:rf-remote]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-spike-rf-remote-2.4g-settings.md](./plans/PLAN-spike-rf-remote-2.4g-settings.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [ZENGGE_PROTOCOL_BIBLE.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L1) Ã‚Â§RF Remote Settings
  - **Goal:** Reverse engineer and implement the 2.4G RF Remote Settings (Allow All, Don't Allow, Paired Only, Clear Pairing).
  - **Details:** COMPLETE Ã¢â‚¬â€ Fixed inverted `ALLOW_ALL` (0x01), `ALLOW_NONE` (0x02), and `ALLOW_PAIRED` (0x03) byte mappings in `ZenggeProtocol.ts`, updated parsing mappings, added comprehensive unit tests, and fully imported/rendered the settings modal in `DashboardScreen.tsx` so long-pressing a device card displays the settings modal end-to-end.

---

### Sprint: v3.8.0 Ã¢â‚¬â€ 2026-05-26 (supabase-security)

- [x] **`chore/audit-supabase-security`** Ã¢â‚¬â€ Hardened PL/pgSQL function search paths and RLS policies on skate_spots, sk8lytz_app_settings, and telemetry. Merged `539dc791`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:supabase-security]`
  - **Plan:** Ã°Å¸â€œÅ½ [PLAN-chore-audit-supabase-security.md](./plans/PLAN-chore-audit-supabase-security.md)
  - **Source of Truth:** Ã°Å¸â€œâ€“ [SK8Lytz_App_Master_Reference.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md#L309) Ã‚Â§Admin Tools Hub
  - **Goal:** Fix Supabase RLS policies and mutable search paths.
  - **Details:** COMPLETE Ã¢â‚¬â€ Hardened PL/pgSQL function search paths, dropped permissive RLS overrides on sk8lytz_app_settings, removed public write exposures on skate_spots, and fixed the broken admin select filter on discovered_devices_telemetry. Excluded inventory product_catalog from scope.

---

### Sprint: v3.7.0 Ã¢â‚¬â€ 2026-05-26 (tooling + chores + camera + music)

- [x] **`fix(tooling)/fortress-gatekeeper-divergence`** Ã¢â‚¬â€ Patched gatekeeper to not destroy branches on failed --ff-only merge. Merged `053ed333`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Added rebase-before-merge step + exit code capture + `continue` on failure. Branch and worktree are now PRESERVED when merge fails. Victory Snapshot VS-001 written to `safety-protocol.md`.

- [x] **`fix/missing-telemetry-script`** Ã¢â‚¬â€ Implemented `tools/sync_remote_errors.mjs`. Merged `57d04a80`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:telemetry-sync]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Native Node 18+ fetch, zero deps, queries Supabase `telemetry_errors`, prints triage table + top 5 errors. CLI flags: `--hours`, `--limit`, `--json`. Offline-safe. Windows UV_HANDLE crash fixed via `process.exitCode`.

- [x] **`chore/audit-npm-vulnerabilities`** Ã¢â‚¬â€ npm audit returned 0 vulnerabilities. Merged `6b20619f`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:vulnerability-fix]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Zero vulnerabilities found. Bucket list entry was stale. No package changes required.

- [x] **`spike/rearchitect-camera-mode`** Ã¢â‚¬â€ Platform-split fix: Android TextureView + iOS worklet. Merged `939a5262`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[H-RISK]` `[Feast]` `[Ã°Å¸Â¤â€“ THINK]`
  - **Details:** COMPLETE Ã¢â‚¬â€ 6 previous fixes all silently failed. Root causes: `takeSnapshot()` is `@platform Android` only; `implementationMode='performance'` (SurfaceView) doesn't support it; bare `catch{}` ate all errors; iOS had no valid fallback. Fix: Android uses TextureView + `takeSnapshot()`. iOS uses `useFrameOutput` worklet + `runOnJS`.

- [x] **`fix/camera-mode-warmup-reset`** Ã¢â‚¬â€ Stabilized callback identity cascade and resolved Android warmup timer reset loop (stuck-on-pink bug). Merged `dc643b27`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Wrote a stable callback wrapper for `onColorDetected` inside a `useRef` inside `CameraTracker.tsx` to stop warmup loops and leaks.

- [x] **`fix/music-mode-ui-flex`** Ã¢â‚¬â€ Wired ColorSwatch FG/BG pickers in MusicPanel. Merged `2e6363fa`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]`
  - **Details:** COMPLETE Ã¢â‚¬â€ `ColorSwatch` wired + `overflow:'hidden'` clip removed.

- [x] **`fix/music-mode-color-inversion`** Ã¢â‚¬â€ Fixed APP mic deactivation in music mode. Merged `a303d409`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]`
  - **Details:** COMPLETE Ã¢â‚¬â€ `src === 'DEVICE'` as isOn flag silently killed APP mic path.

- [x] **`fix/music-mode-pattern-mapping`** Ã¢â‚¬â€ Fixed colorMode, modeType passthrough, effectId clamp. Merged `4e41f7b8`.
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[L-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]`
  - **Details:** COMPLETE Ã¢â‚¬â€ 3 bugs across MusicDictionary, ZenggeAdapter, ZenggeProtocol.

---

### Sprint: v3.6.0 Ã¢â‚¬â€ 2026-05-26

- [x] **`fix/health-telemetry-autostart`** Ã¢â‚¬â€ Auto-started HealthKit/HealthConnect bridges before polling. Merged `03096cf3`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Injected `AppleHealthKit.initHealthKit()` (iOS) and `initialize()` (Android Health Connect) at the top of each `pollHealthData()` cycle.

- [x] **`spike/appstate-ble-reconnect`** Ã¢â‚¬â€ Autonomous recovery on appstate wake. Merged `f518d38f`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[CORE]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ THINK]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Added a 1500ms OS stack wakeup delay to `retriggerAutoConnect` and implemented a native `isDeviceConnected` audit in `useBLE.ts` to clear stale split-brain connections when the app wakes up.

- [x] **`feat/dashboard-pull-to-refresh`** Ã¢â‚¬â€ Add pull-to-refresh to dashboard for manual BLE sweep. Merged `f8c0b2bd`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Wrapped `DashboardScreen` main `ScrollView` with `RefreshControl`. Triggered `retriggerAutoConnectRef.current()` on pull.

- [x] **`feat/crew-hub-radius-refresh`** Ã¢â‚¬â€ Added 10mi radius option. Merged `7aa002c5`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Added 10 miles to the "Live Near You" map radius dropdown.

- [x] **`fix/crew-hub-collapsed-padding`** Ã¢â‚¬â€ Fixed CrewHubSlab vertical padding state. Merged `7aa002c5`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Collapse padding when collapsed.

- [x] **`feat/crew-hub-mini-map`** Ã¢â‚¬â€ Added MapViewCluster inside slab. Merged `7aa002c5`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Added native mini-map static view inside collapsed slab.

- [x] **`chore/remove-test-category`** Ã¢â‚¬â€ Removed Test category from engine and UI. Merged `7aa002c5`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:pro-effects-cleanup]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Stripped all `group: 'Test'` entries.

- [x] **`feat/universal-slider-labels`** Ã¢â‚¬â€ Refactored TacticalSlider labels inline. Merged `7aa002c5`.
  - **Tags:** `[Ã¢Å“â€¦ DONE]` `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:universal-sliders]`
  - **Details:** COMPLETE Ã¢â‚¬â€ Added `label` prop to `TacticalSlider.tsx`.

---

### Sprint: v3.5.2 and prior

- [x] **`fix/detective-social-only-sites`** Ã¢â‚¬â€ Yelp/Places search on social-only sites. Merged auto-cleaned.
- [x] **`fix/detective-google-photo-refs`** Ã¢â‚¬â€ Harvest photo references from Places. Merged auto-cleaned.
- [x] **`fix/detective-escalation-all-passes`** Ã¢â‚¬â€ Re-run LLM passes on enriched text. Merged auto-cleaned.
- [x] **`spike/detective-model-upgrade-7b`** Ã¢â‚¬â€ 7B/8B model evaluation (GDDR6 VRAM optimization). Merged auto-cleaned.
- [x] **`feat/user-correction-feedback-loop`** Ã¢â‚¬â€ Log PUT spots feedback loop. Merged auto-cleaned.
- [x] **`feat/review-text-mining`** Ã¢â‚¬â€ Regex mine reviews for structured data. Merged auto-cleaned.
- [x] **`hotfix/data-integrity-guards`** Ã¢â‚¬â€ Preserved data upsert guards. Merged `1289dae4`.
- [x] **`chore/test-usecontrollerdispatch`** Ã¢â‚¬â€ Mocked dispatch testing suite. Merged `7ff122d7`.
- [x] **`fix/gatt-conn-133-exception`** Ã¢â‚¬â€ Android GATT 133 retries/teardown. Merged `df7a3c40`.
- [x] **`feat/detox-e2e-automation`** Ã¢â‚¬â€ Detox E2E tests for React Native. Merged `94e52cd8`.
- [x] **`chore/consolidate-rules-workflows`** Ã¢â‚¬â€ Rules consolidation (behavior and safety). Merged `17971a6`.
- [x] **`chore/align-git-workflows`** Ã¢â‚¬â€ Unified attestation gating on git workflows. Merged `8bdea378`.
- [x] **`chore/verifiable-attestation`** Ã¢â‚¬â€ Verifiable QA check-runner with cryptographic attestations. Merged `46f95b12`.
- [x] **`chore/worktree-junction-tsc`** Ã¢â‚¬â€ Enable Directory Junctions for worktrees. Merged `214d4249`.
- [x] **`test/component-smoke`** Ã¢â‚¬â€ Static parser checks for container views relative imports. Merged `214d4249`.
- [x] **`chore/eslint-import-gate`** Ã¢â‚¬â€ ESLint Flat Config + unused-import gates. Merged `214d4249`.
- [x] **`fix/split-brain-hygiene`** Ã¢â‚¬â€ Unified shared favorites and deleted dead stubs. Merged `15db163e`.
- [x] **`fix/ble-split-brain`** Ã¢â‚¬â€ Unified single BLEProvider context. Merged `6b8e6917`.
- [x] **`fix/two-way-health-sync`** Ã¢â‚¬â€ Push workout data natively to HealthKit/HealthConnect. Merged `b75f3f52`.
- [x] **`fix/telemetry-hardening-v2`** Ã¢â‚¬â€ Consolidated AppLogger telemetry channels. Merged `6ef76e05`.
- [x] **`feat/babel-syntax-gate`** Ã¢â‚¬â€ Pre-commit static AST syntax validator. Merged `6ef76e05`.
- [x] **`fix/black-hole-errors`** Ã¢â‚¬â€ CENTRALIZED production error visibility in BLE. Merged `f63a44c6`.
- [x] **`feat/geofence-rink-sync`** Ã¢â‚¬â€ social radar check-in geofence. Merged `c18bae6`.
- [x] **`feat/health-sync-integration`** Ã¢â‚¬â€ skating health telemetry sync. Merged `9168b2e`.
- [x] **`feat/scene-offline-sync-queue`** Ã¢â‚¬â€ offline AsyncStorage sync queues. Merged `fe99fb3f`.
- [x] **`spike/ios-android-parity-audit`** Ã¢â‚¬â€ Android takeSnapshot TextureView + iOS worklet parity. Merged `f0516ac9`.


---

### Sprint: v3.9.1 Ã¢â‚¬â€ 2026-06-06 (session-integrity)

### [BATCH:session-integrity] Ã¢â‚¬â€ Ã°Å¸â€œâ€¹ Sequential (Complete)

- [x] **`fix/session-watch-stale-closure`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Added `endSessionRef` stable-ref forwarder to `SessionContext.tsx`. `notifee.onBackgroundEvent`, `AppState` listener, and 10s STOPPED watchdog all call through the stable wrapper, eliminating the stale-closure data-loss bug.

- [x] **`fix/session-appstate-deps-loop`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Removed `sessionPhase` from the `AppState` listener `useEffect` dependency array in `SessionContext.tsx`. Listener now registered once on mount Ã¢â‚¬â€ eliminates double-registration and racing double-pause on background.

- [x] **`fix/session-autopause-starttime`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Removed the redundant `WatchBridge.syncSessionState` call in `SessionContext.tsx` auto-resume path. `useGlobalTelemetry` already pushes the correctly shifted anchor Ã¢â‚¬â€ the SessionContext push was overwriting it with `new Date()` (wrong).

- [x] **`fix/session-paused-persistence`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:session-integrity]`
  - **Outcome:** `@sk8lytz_session_active` AsyncStorage key upgraded from `'true'`/`'false'` to `JSON.stringify({ state, pausedAt })`. On crash-recovery, PAUSED state restored correctly. Backward compat: legacy `'true'`/`'false'` values handled as `'active'`/`'idle'`.

- [x] **`fix/session-background-end-data-loss`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** `notifee.onBackgroundEvent` in `index.ts` now calls `WatchBridge.syncSessionState({ status: 'STOPPED' })` + sets `@sk8lytz_pending_bg_end` flag. On next foreground, `SessionContext` detects pending flag and runs full `commitSession()` with cached telemetry. Eliminated the silent total-data-loss path.

- [x] **`fix/session-idle-race-summary`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:session-integrity]`
  - **Outcome:** Added `'ENDING'` to `SessionPhase` type. `endSession()` sets `ENDING` first (keeps FGS alive), awaits SUMMARY push to watch, then sets `IDLE`. Updated `isSkateSessionActive` derivation and `useGlobalTelemetry` type guard.

- [x] **`fix/session-watch-contract-audit`** Ã¢â‚¬â€ `[75f5cbf7] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:session-integrity]`
  - **Outcome:** Documentation-only. Audited `WatchConnectivityManager.swift` L81-117 and `WearableCommunicationService.kt` L125-130 Ã¢â‚¬â€ both handle all 4 states. Added JSDoc contract comment to `WatchSessionState` type confirming native compliance.

---

### Sprint: v3.9.1 Ã¢â‚¬â€ 2026-06-06 (ble-connection-resilience)

### [BATCH:ble-connection-resilience] Ã¢â‚¬â€ Ã°Å¸â€œâ€¹ Sequential (Complete)

- [x] **`fix/ble-gate-silent-invalid-transition`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `AppLogger.error()` + `__DEV__` throw on invalid transitions in `BleStateMachine.ts`. Added `forceTransitionTo()` escape hatch. Added `SCANNING Ã¢â€ â€™ DISCONNECTING` as valid transition. `setGate()` now checks return value. Error-recovery catch blocks use `forceTransitionTo()`. 3 new Jest tests.

- [x] **`fix/ble-state-ref-lag`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Created `setConnectedDevicesSync()` wrapper that updates both `connectedDevicesRef.current` AND calls `setConnectedDevices()` atomically. Replaced all callsites. Removed the sync `useEffect`. 1-frame lag eliminated.

- [x] **`fix/ble-disconnect-stale-closure`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `handleOrganicDisconnectRef` stable-ref forwarder. Passed stable wrapper to `useBLEAutoRecovery` and `BleConnectionManager`. Ref always updated on every render Ã¢â‚¬â€ stale closure eliminated.

- [x] **`fix/ble-autoconnect-drain-permanent`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `failedAutoConnectRef: Map<string, { attempts, lastAttempt }>`. On failure: re-queues with backoff (1s Ã¢â€ â€™ 4s Ã¢â€ â€™ 12s). After 3 failures: ejects permanently. `retriggerAutoConnect()` clears failed map too.

- [x] **`fix/ble-ghost-state-flicker`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Removed pre-dispatch ghost-clear from Group Dropout Coordinator. Ghost state now cleared exclusively in `.then()` success callback after `connectToDevices` resolves. Devices remain visually dimmed until confirmed reconnected.

- [x] **`fix/ble-gatt-mutex-hotreload`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Added `_generation` counter + `_hotReloadCleanup()` to `useBLEGattMutex.ts`. On Hot Reload: aborts current holder, resets lock, increments generation. `acquireGattLock` races against 200ms timeout for orphaned promises. Stall reduced from 15s to ~200ms.

- [x] **`fix/ble-autoconnect-single-group`** Ã¢â‚¬â€ `[69f65537] Unified batch completed successfully`
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[BLE]` `[L-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:ble-connection-resilience]`
  - **Outcome:** Replaced `groups[0]` single-group selection with `Set<string>` MAC aggregation across ALL groups. Both cloud and offline paths collect all unique device MACs. `retriggerAutoConnect()` also clears `autoConnectRetriesRef`.

---

### Sprint: v3.9.1 Ã¢â‚¬â€ 2026-06-06 (account-critical)

### [BATCH:account-critical] Ã¢â‚¬â€ Ã°Å¸â€œâ€¹ Sequential (Complete)

- [x] **`fix/offline-session-persistence-queue`** Ã¢â‚¬â€ merged `76067e15` | C-01 CLOSED
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[Ã°Å¸Â¤â€“ PRO-HIGH]` `[BATCH:account-critical]`
  - **Outcome:** `SpeedTrackingService.saveSession()` queues offline sessions to `@SK8Lytz_PendingSession_Queue`. `flushPendingSessionQueue()` with re-entrancy guard wired into `useOfflineSyncWorker` 60s loop. User sees Alert instead of silent data loss. 4 Jest tests. 129/129 passing.

- [x] **`fix/offline-eula-bypass`** Ã¢â‚¬â€ merged `66fc95cf` | M-07 CLOSED
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** `ComplianceGate.tsx` offline bypass removed. First offline launch shows full `EulaModal`. Acceptance writes versioned JSON to `@Sk8lytz_offline_eula_accepted`. Subsequent launches pass immediately. Authenticated path unchanged.

- [x] **`fix/session-expiry-ux-message`** Ã¢â‚¬â€ merged `72ea48a9` | M-02 CLOSED
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** `App.tsx` `init()` detects expired token via `@Sk8lytz_auth_last_email` after null `getSession()`. Amber banner on `AuthScreen`: "Your session expired. Please sign in again." Clears on `SIGNED_IN`. No banner on fresh install.

- [x] **`fix/crew-delete-rpc`** Ã¢â‚¬â€ merged `d0cf72ee` | M-05 CLOSED
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CLOUD]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** `AccountModal.tsx` `handleDeleteCrew` was calling `leavePermanentCrew` (membership-only removal). Fixed to call `profileService.deleteCrew()` Ã¢â‚¬â€ hard-deletes crew + cascades memberships + broadcasts `session_ended`. Service layer was already correct; only the UI handler was wrong.

- [x] **`fix/offline-device-userid-stamp`** Ã¢â‚¬â€ NO-OP | M-06 CLOSED (defect does not exist)
  - **Tags:** `[Ã¢Å“â€¦ VERIFIED]` `[CLOUD]` `[M-RISK]` `[Snack]` `[Ã°Å¸Â¤â€“ FLASH]` `[BATCH:account-critical]`
  - **Outcome:** Audit finding was incorrect. `DeviceRepository._flushPendingSync(userId)` already receives `userId` from auth-gated `syncFromCloud()`. `dbRow.user_id = userId` at L704 stamps correctly at flush time. No null path exists.



### [BATCH:account-hardening] â€” âœ… Completed 2026-06-06

- [x] **`refactor/auth-context-provider`** â€” merged 64daf01d | C-02 CLOSED
  - **Tags:** [âœ… VERIFIED] [UI] [M-RISK] [Meal] [ðŸ¤– PRO-HIGH] [BATCH:account-hardening]
  - **Outcome:** Extracted auth state from App.tsx into AuthContext. Replaced independent getUser() calls across services and hooks with useAuth().user to prevent race conditions and redundant lookups.

- [x] **`fix/auth-tokens-secure-store`** â€” merged 738ba170 | M-01 CLOSED
  - **Tags:** [âœ… VERIFIED] [CLOUD] [M-RISK] [Snack] [ðŸ¤– FLASH] [BATCH:account-hardening]
  - **Outcome:** Replaced AsyncStorage with expo-secure-store for Supabase auth token storage adapter. Added migration logic to move existing tokens on first launch.

- [x] **`fix/password-change-reauth`** â€” merged 363b9808 | M-03 CLOSED
  - **Tags:** [âœ… VERIFIED] [CLOUD] [M-RISK] [Snack] [ðŸ¤– FLASH] [BATCH:account-hardening]
  - **Outcome:** Added "Current Password" verification to AccountModal.tsx before allowing a password update. signInWithPassword gate added before updateUser.

- [x] **`feat/notif-prefs-cloud-sync`** â€” merged 60067804 | M-04 CLOSED
  - **Tags:** [âœ… VERIFIED] [CLOUD] [M-RISK] [Snack] [ðŸ¤– FLASH] [BATCH:account-hardening]
  - **Outcome:** Added notif_preferences JSONB column. Updated AuthProfileService.updateProfile and useAccountOverview to sync local preferences to the cloud and merge them on load.

*End of Archive.*
### [BATCH:ble-p2-xstate-fsm] â€” ðŸ“‹ Sequential (touches all BLE state files)

- [x] **le/xstate-fsm-migration** â€” migrated BLE engine to XState v5 FSM. Merged \5cdeb702\.
  - **Tags:** [ðŸ•µï¸ SPIKE] [âœ… VERIFIED] [CORE] [H-RISK] [Feast] [ðŸ¤– THINK] [BATCH:ble-p2-xstate-fsm]
  - **Plan:** ðŸ“Ž PLAN-ble-xstate-fsm-migration.md
  - **Source of Truth:** ðŸ“– [BleStateMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleStateMachine.ts)
  - **Goal:** Evaluate migrating BLE state management from scattered refs/effects/tagged unions to XState statecharts. Per-device FSMs composed into group-level summaries. Invalid states become structurally impossible.
  - **Details:** COMPLETE â€” Migrated BLE system to XState v5 (useMachine). Created BleMachine.ts and BleMachine.types.ts. Replaced BleStateMachine class. Added shim to leGateRef to prevent regression and satisfy type checking. Verified via 
pm run verify which includes QA tests.

### [BATCH:ble-p3-polish] â€” âš¡ Parallel (Completed Tasks)

- [x] **`ble/connection-health-heartbeat`** â€” `pingConnectedDevice` hook + 7 Jest tests. Merged `84e21bb3`.
  - **Tags:** `[âœ… READY]` `[âœ… VERIFIED]` `[LAB]` `[L-RISK]` `[Snack]` `[ðŸ¤– PRO-HIGH]` `[BATCH:ble-p3-polish]`
  - **Plan:** ðŸ“Ž [PLAN-ble-connection-health-heartbeat.md](file:///C:/Users/Magma/.gemini/antigravity/brain/acebf202-b9db-4779-8e51-e3ed33ab835d/PLAN-ble-connection-health-heartbeat.md)
  - **Source of Truth:** ðŸ“– `useBLE.ts` â€” no periodic connection liveness check; stale GATT handles survive for minutes on Samsung Galaxy A-series
  - **Goal:** Add a lightweight BLE ping every 45-60s to connected devices to verify the connection is actually alive. If ping fails, preemptively trigger recovery instead of waiting for the next write to fail.
  - **Details:** COMPLETE â€” Created `src/hooks/ble/useBLEHeartbeat.ts` with `pingConnectedDevice()` (pure exported fn, testable) and `useBLEHeartbeat` (thin setInterval orchestrator). Wired into `useBLE.ts`. 7 tests in `useBLEHeartbeat.test.ts` cover Zengge happy path, BanlanX fallback, empty-packets fallback, GATT 133 error, code 8 error, cancel-throws safety, success no-op. Also fixed: `verifiable-check-runner.js` junction relink idempotency + `jest.config.js` `transformIgnorePatterns` expanded for `expo-*` packages. Needs physical device smoke test to confirm stale link recovery fires correctly in the field.

- [x] **`ble/post-connect-rssi-monitoring`** â€” `useBLERSSIMonitor` + live rssiMap on device cards. Merged `fd635db8`.
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[LAB]` `[L-RISK]` `[Snack]` `[ðŸ¤– FLASH]` `[BATCH:ble-p3-polish]`
  - **Plan:** ðŸ“Ž [PLAN-ble-post-connect-rssi-monitoring.md](file:///C:/Users/Magma/.gemini/antigravity/brain/acebf202-b9db-4779-8e51-e3ed33ab835d/PLAN-ble-post-connect-rssi-monitoring.md)
  - **Source of Truth:** ðŸ“– `useBLE.ts` â€” RSSI checked only during scan, never after connection established
  - **Goal:** Poll RSSI every 30s on connected devices. If RSSI drops below -75 dBm, show a "weak connection" warning badge on the device card. If it drops below -82 dBm, preemptively disconnect and reconnect.
  - **Details:** COMPLETE â€” Created `src/hooks/ble/useBLERSSIMonitor.ts` with `readDeviceRSSI()` (pure testable fn) and `useBLERSSIMonitor()` (30s polling hook, returns `Record<string,number>`). Created `src/components/ConnectionStrengthBadge.tsx` (3-bar pure-View signal icon, no SVG dep). Wired `rssiMap` into `BluetoothLowEnergyApi` + `useBLE.ts` return. In `DashboardScreen.tsx` `renderItem`, live `rssiMap[mac]` overrides stale scan-time `device.rssi` â€” existing wifi icon auto-updates. Preemptive reconnect guard uses verified `ghostedDeviceIds.includes(mac)`. 9 tests. Needs physical device smoke test to confirm badge updates within 30s of signal degradation.


#### Batch Strategy Table â€” Deep-Dive Code Audit (2026-06-07)

---

*(account-hardening batch complete â€” archived @ Sprint v3.9.1)*
*(account-critical batch complete â€” archived @ Sprint v3.9.1)*

## ðŸš§ ACTIVE SPRINT

---

> âœ… All triage items from this audit have been completed and archived in `tools/SK8Lytz_Bucket_List_ARCHIVE.md` under Sprint v3.9.1 (2026-06-06).
> 3 research agents Ã— 30+ files Ã— every line read. 14 issues identified across session lifecycle and BLE connection resilience.
> ðŸ“Š **Source Analysis**: [Connection & Session Architecture Audit (2026-06-06)](file:///C:/Users/Magma/.gemini/antigravity/brain/25ac1742-4218-4218-91d4-cea42835db9b/analysis_results.md)

## ðŸš‘ TRIAGE QUEUE (Bugs & Hotfixes)

---

> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.
> âš ï¸ AI AGENT DIRECTIVES (THE CONSTITUTION)

# SK8Lytz Master Bucket List



#### Batch Strategy Table â€” Deep-Dive Code Audit (2026-06-07)

> Identified violations from the Deep-Dive Code Audit (Rule 16 + Offline Telemetry).

| Batch | Type | Tasks | File Overlap | Prerequisite |
|-------|------|-------|-------------|-------------|
| `[BATCH:deep-dive-regressions]` | ðŸ“‹ Sequential | 1 | Touches 25+ files | None |

---

### âš¡ [BATCH:deep-dive-regressions] â€” `refactor/deep-dive-regressions` â€” ðŸ”´ Critical (Active)
> **Worktree**: `refactor/deep-dive-regressions` Â· **Type**: Sequential Â· **Prerequisite**: None
> **Source Analysis**: ðŸ“Š [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) â€” 16-agent deep dive exposed numerous Rule 16 violations (missing try/catch, `any` casts, inline functions) and critical offline telemetry dropping.

- [x] **`refactor/deep-dive-telemetry`** â€” merged @ 256d3257 (Added robust setTimeout debounce to AppLogger and wrapped I/O in try/catch)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[DATA]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix offline telemetry drops in `DATA_LAYER`, `SESSION_TRACKING`, and `AppLogger.ts`.
  - **Decision Log:** Critical I/O operations lack try/catch wrappers, causing silent failures when the app goes offline. Replaced faulty manual debounce with true setTimeout buffer and forced offline persists.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Â· Plan: [PLAN-refactor-deep-dive-telemetry.md](docs/plans/PLAN-refactor-deep-dive-telemetry.md)
  - **Source of Truth:** ðŸ“– `src/services/AppLogger.ts`

- [x] **`refactor/deep-dive-type-safety`** â€” merged @ 9ca523d3 (Eliminated `any` casts in AccountModal and CrewTab, enforced strict types via `React.Dispatch<React.SetStateAction<...>>`)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[CORE]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Eradicate all `any` casts from `NOTIFICATIONS_&_ROUTING`, `GROUP_SYNC`, `HARDWARE_PROTOCOLS`, and `IDENTITY`.
  - **Decision Log:** Pervasive use of `any` casts bypasses TypeScript safety during crashes, violating Rule 16.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Â· Plan: [PLAN-refactor-deep-dive-type-safety.md](docs/plans/PLAN-refactor-deep-dive-type-safety.md)
  - **Source of Truth:** ðŸ“– `src/components/crew/CrewLandingScreen.tsx` + `CONSTITUTION.md`

- [x] **`refactor/deep-dive-perf`** â€” merged @ e72ff390 (Extracted inline styles to StyleSheet.create and moved inline mappings and renderItem to useCallback/useMemo across UI components)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[UI]` `[âš ï¸ M-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Resolve performance leaks caused by inline functions and styles in FlatLists across `UI_CONTROLS` and `GROUP_SYNC`.
  - **Decision Log:** Widespread inline styles/functions cause severe re-render thrashing.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Â· Plan: [PLAN-refactor-deep-dive-perf.md](docs/plans/PLAN-refactor-deep-dive-perf.md)
  - **Source of Truth:** ðŸ“– `src/components/DockedController.tsx`

- [x] **`refactor/deep-dive-ble-core`** â€” merged @ 0718bb3b (Fixed stale closures in AutoRecovery, added offline AsyncStorage telemetry queues, removed any casts in useBLESweeper)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[CORE]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix stale closures in `useBLEAutoRecovery.ts`, missing offline queues in `useBLEScanner.ts`, and `any` casts in `useBLESweeper.ts`.
  - **Decision Log:** BLE components have critical stale closures and lack offline persistence for discovered devices.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Â· Plan: [PLAN-refactor-deep-dive-ble-core.md](docs/plans/PLAN-refactor-deep-dive-ble-core.md)
  - **Source of Truth:** ðŸ“– `src/hooks/ble/useBLEAutoRecovery.ts`

- [x] **`refactor/deep-dive-os-permissions`** â€” merged @ 14dff9da (Fixed Android 14+ FOREGROUND_SERVICE location flags in AndroidManifest.xml and wrapped AsyncStorage permissions telemetry)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[NATIVE]` `[âš ï¸ M-RISK]` `[ðŸ± Meal]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix missing Android 14+ FOREGROUND_SERVICE flags and conflicting location permissions in `AndroidManifest.xml`.
  - **Decision Log:** Missing OS-level foreground service definitions for Android 14+ will cause background execution crashes.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Â· Plan: [PLAN-refactor-deep-dive-os-permissions.md](docs/plans/PLAN-refactor-deep-dive-os-permissions.md)
  - **Source of Truth:** ðŸ“– `android/app/src/main/AndroidManifest.xml`

- [x] **`refactor/deep-dive-native-cloud`** â€” merged @ c03b83e5 (Fixed telemetry overwrites in WatchConnectivity, buffered WearMessageSender, added try/catch to edge functions, and safely casted SQL JSON metrics)
  - **Tags:** `[âœ… READY]` `[ðŸ¤” INFERRED]` `[CLOUD]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– PRO-HIGH]` `[BATCH:deep-dive-regressions]`
  - **Goal:** Fix telemetry overwrites in WatchConnectivityManager, dropouts in WearMessageSender, and missing try/catch in Supabase Edge Functions.
  - **Decision Log:** Native watch targets and cloud edge functions drop data due to unhandled exceptions and payload overwriting.
  - **Analysis:** ðŸ“Š Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/1f13c375-3bed-42bc-9c4f-245d16fb8e06/system_audit_report.md) Â· Plan: [PLAN-refactor-deep-dive-native-cloud.md](docs/plans/PLAN-refactor-deep-dive-native-cloud.md)
  - **Source of Truth:** ðŸ“– `targets/watch/WatchConnectivityManager.swift`

---



### ðŸ”´ Tier 1: Critical System Integrity

#### Batch Strategy Table â€” Constitutional Audit Burn-Down

> Identified violations from the Deep-Dive System Audit (2026-06-06).

| Batch | Type | Tasks | File Overlap | Prerequisite |
|-------|------|-------|-------------|-------------|
| `[BATCH:burn-down-audit-failures]` | ðŸ“‹ Sequential | 1 | Touches 25+ files (BLE & Auth) | None |

---

### âš¡ [BATCH:burn-down-audit-failures] â€” `refactor/burn-down-audit-failures` â€” ðŸ”´ Critical
> **Worktree**: `refactor/burn-down-audit-failures` Â· **Type**: Sequential Â· **Prerequisite**: None
> **Source Analysis**: ðŸ“Š [audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/25ac1742-4218-4218-91d4-cea42835db9b/audit_report.md) â€” 3-agent deep dive exposed 14 new `any` casts, faked XState shims, and 16 AuthContext bypasses.

- [/] **`refactor/burn-down-audit-failures`**
  - **Tags:** `[ðŸš§ IN PROGRESS]` `[ðŸ¤” INFERRED]` `[CORE]` `[âš ï¸ H-RISK]` `[ðŸ¥© Feast]` `[ðŸ¤– PRO-HIGH]` `[BATCH:burn-down-audit-failures]`
  - **Goal:** Eradicate 14 injected `any` casts, finalize the split-brain XState migration, and enforce AuthContext globally across 16 bypassed hooks/services.
  - **Decision Log:** Deep dive audit by 3 subagents found 14 injected `any` casts, split-brain XState migration, and 16 bypassed AuthContext hooks/services. Pushed on 2026-06-06 as 'completed' but failed constitutional laws.
  - **Analysis:** ðŸ“Š Source: [audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/25ac1742-4218-4218-91d4-cea42835db9b/audit_report.md) Â· Plan: [PLAN-refactor-burn-down-audit-failures.md](docs/plans/PLAN-refactor-burn-down-audit-failures.md)
    Key finding: "XState migration faked via `any` shims. 16 files bypass AuthContext. 14 new `any` casts violated Rule 1."
    Rejected alternative: "Leaving them as Tech Debt. This violates core rules and causes cascading type failures."
  - **Source of Truth:** ðŸ“– `src/context/AuthContext.tsx:9-10` + `CONSTITUTION.md` Rule 1.
  - **Details:** 3-phase burn down: 1. Purge `any` casts. 2. Rip out `BleStateMachine.ts` and fake `bleGateRef` shims. 3. Delete `supabase.auth.getUser` from 8 hooks and 8 services, forcing context/prop passing.

---



### [BATCH:ble-p1-ios-platform] — ? Parallel (zero file overlap)

> ?? **P2 — Planned Improvements.** iOS-specific fixes.

- **Prerequisite**: None
- **Active Tasks**: le/ios-state-restoration

- [x] **le/ios-state-restoration** (Merged @ f6af517d — Implemented react-native-ble-plx restoreStateIdentifier for silent iOS CoreBluetooth connection recovery)
  - **Tags:** [?? NEEDS PLAN] [?? INFERRED] [LAB] [M-RISK] [Meal] [?? THINK] [BATCH:ble-p1-ios-platform]
  - **Plan:** ?? PLAN-ble-ios-state-restoration.md *(pending)*
  - **Source of Truth:** ?? Apple [State Preservation and Restoration docs](https://developer.apple.com/library/archive/documentation/NetworkingInternetWeb/Conceptual/CoreBluetooth_concepts/CoreBluetoothBackgroundProcessingForIOSApps/PerformingTasksWhileYourAppIsInTheBackground.html)
  - **Goal:** Implement Core Bluetooth State Preservation and Restoration via a small native module (~30 lines Swift) so iOS can restore pending BLE connections when the app is terminated and relaunched.
  - **Details:** Issue iOS-02. If iOS kills the app while in Music/Street mode, BLE connections are silently dropped with no recovery. Core Bluetooth provides CBCentralManagerOptionRestoreIdentifierKey — the system saves pending connections and restores them on relaunch. Nanoleaf and LIFX both implement this. Requires: register restore identifier in native BLE init, implement centralManager:willRestoreState: delegate, bridge restored peripheral IDs back to JS via eact-native-ble-plx configuration or a small custom native module.

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


- [x] **`qa/fix-error-handling-and-io-safety`** 🚀 Merged in 86edaf43
  - **Tags:** `[🔴 TRIAGE]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[🤖 FAST]`
  - **Plan:** 📎 [PLAN-fix-error-handling-and-io-safety.md](docs/plans/PLAN-fix-error-handling-and-io-safety.md)
  - **Source of Truth:** 📖 `system_audit_report.md`
  - **Goal:** Standardize `e instanceof Error` unwrapping across the app (R-06). Wrap unhandled async networks in `try/catch` (R-11). Eliminate `any` casts (R-08).

- [x] **`qa/fix-os-parity-and-build-config`** 🚀 Merged in 86edaf43
  - **Tags:** `[🔴 TRIAGE]` `[✅ VERIFIED]` `[NATIVE]` `[H-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Plan:** 📎 [PLAN-fix-os-parity-and-build-config.md](docs/plans/PLAN-fix-os-parity-and-build-config.md)
  - **Source of Truth:** 📖 `system_audit_report.md`
  - **Goal:** Fix R-20 missing Foreground Service Types and iOS Location usage descriptions in `app.config.js`. Scrub API keys. Fix shadowColor/elevation parity. 
 # # #   [ B A T C H : s p l i t - b r a i n - e r a d i c a t i o n ]   -   C R I T I C A L   |   C O M P L E T E D  
 -   [ x ]   f i x / s p l i t - b r a i n - e r a d i c a t i o n   ( 6 5 2 8 2 c 2 7 )   -   E l i m i n a t e d   3   c o n f i r m e d   s p l i t - b r a i n   s t a t e   d u p l i c a t i o n   p a t t e r n s   v i a   C r e w S e r v i c e   E v e n t E m i t t e r   p u b / s u b ,   s i n g l e   s p e e d   q u e u e ,   a n d   p r o t o c o l   i n s t a n c e s .  
     -   T a g s :   [ V E R I F I E D ]   [ B L E ]   [ H - R I S K ]   [ B a n q u e t ]   [ T H I N K ]  
 

- [x] **`fix/pii-scrub-telemetry`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[Services]` `[H-RISK]` `[Meal]` `[🧠 PRO-MED]`
  - **Plan:** 🔗 [PLAN-PII-SCRUB-TELEMETRY.md](docs/plans/PLAN-PII-SCRUB-TELEMETRY.md)
  - **Source of Truth:** 📍 `src/hooks/useCrewSession.ts:107`, `src/hooks/useBLE.ts:444`, `src/hooks/useDashboardAutoConnect.ts:222`, `src/hooks/useDeviceStateLedger.ts:162`, `src/screens/DashboardScreen.tsx:434` | Audit: `R-09_findings.json`
  - **Goal:** Remove raw MAC addresses and user display_names from all AppLogger telemetry calls. Implement `scrubPII()` hash helper. 49 total call sites (5 primary + 44 sweep).
  - **Details:** GDPR compliance risk. MAC addresses and display names are leaking into cloud telemetry logs. Primary fix: implement `scrubPII()` deterministic hash, apply to 5 verified high-severity sites, then grep-sweep remaining 44.

- [x] **`fix/stale-closure-intervals`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[Hooks]` `[H-RISK]` `[Meal]` `[🤖 PRO-MED]`
  - **Plan:** 📎 [PLAN-STALE-CLOSURE-INTERVALS.md](docs/plans/PLAN-STALE-CLOSURE-INTERVALS.md)
  - **Source of Truth:** 📖 `src/hooks/cloud/useOfflineSyncWorker.ts:36`, `src/hooks/useDashboardAutoConnect.ts:169`, `src/hooks/useTelemetryLedger.ts:169`, `src/hooks/ble/useBLEHeartbeat.ts:105` | Audit: `R-12_findings.json`, `R-26_findings.json`
  - **Goal:** Fix 2 stale closure intervals that capture null user/session (preventing offline sync from ever running). Add `_isRunning` boolean ref guards to 6 async interval callbacks to prevent concurrent execution and duplicate DB writes.
  - **Details:** `useOfflineSyncWorker` captures stale `user=null` in empty-dep `useEffect` — offline queue never flushes post-login. R-26 found 7 intervals firing async callbacks concurrently, causing double DB inserts, duplicate GATT reads, and duplicate notifee calls on slow networks.
### [BATCH:offline-first-sweep] ?? LOW | (Archived)
- [x] **`feat/offline-first-cache-layer`** (Merged in aa5ad615)
  - **Goal:** Added AsyncStorage cache-first layer to 6 Supabase fetch paths. Cache serves immediately; network sync runs in background. Fixed offline experience for hardware_blacklist, app settings, gradients, scenes, and skate spots.


- [x] **`fix/async-error-hardening`** 🚀 Merged in 027bc694
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[Services]` `[M-RISK]` `[Banquet]` `[🤖 PRO-LOW]`
  - **Plan:** 📎 [PLAN-ASYNC-ERROR-HARDENING.md](docs/plans/PLAN-ASYNC-ERROR-HARDENING.md)
  - **Source of Truth:** 📖 `src/services/CrewProfileService.ts:31-461`, `src/services/CrewService.ts:76-597`, `src/services/DeviceRepository.ts:140-724` | Audit: `R-11_findings.json` (120+ findings), `R-06_findings.json` (72+ findings)
  - **Goal:** Wrap 120+ naked `await` operations in try/catch and fix 72+ catch blocks missing `e instanceof Error` unwrapping. ~192 total surgical edits across ~30 files.
  - **Details:** `CrewProfileService.ts` alone has 55 naked awaits. `DeviceRepository.ts` has 8 untyped catch blocks. These are all LOW-cognitive-load, high-volume pattern fixes ideal for a lesser model to execute with this plan as the SoT.

- [x] **`fix/gatt-race-conditions`** (Merged @ accf781c) 🚀 Merged in accf781c
  - **Tags:** `[⚪ READY]` `[✅ VERIFIED]` `[BLE]` `[H-RISK]` `[Meal]` `[🤖 THINK]`
  - **Plan:** 📎 [PLAN-gatt-race-conditions.md](docs/plans/PLAN-gatt-race-conditions.md)
  - **Source of Truth:** 📖 `src/hooks/ble/useBLEBatterySweep.ts:135` | `src/hooks/useBLE.ts:410` | `src/services/BleConnectionManager.ts:47` | `src/hooks/useDashboardGroups.ts:235` | `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx:178` | `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx:73` | Audit: `R-26_findings.json` + `R-10_findings.json`
  - **Goal:** Fix 4 re-entrancy race conditions (burstTimer overwrite, pingDevice sweeper race, GATT lock pre-check, double-tap power toggle) and 2 DiagLab `Promise.all` concurrent write collisions → GATT 133.
  - **Details:** GATT 133 errors on Android are directly caused by these races. Rapid battery sweep, rapid ping, and DiagLab group commands all trigger the issue today.

- [x] **`fix/flatlist-perf-sweep`** - Merged 5fcdb090: Injected removeClippedSubviews and initialNumToRender performance props to all flatlists. 🚀 Merged in 5fcdb090
  - **Tags:** `[⚪ READY]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Meal]` `[🤖 PRO-LOW]` `[BATCH]`
  - **Plan:** 📎 `(generate on `/start-task`)`
  - **Source of Truth:** 📖 `src/components/docked/FavoritesPanel.tsx:80,94` | `src/components/patterns/GradientLibraryTab.tsx:118,121,125` | `src/components/patterns/PatternPickerTab.tsx:151,153,154` | `src/components/CommunityModal.tsx:258,262` | `src/components/VerticalPatternDrum.tsx:101,115,116` | `src/components/admin/tools/tabs/DiagnosticLabSnifferTab.tsx:27,28,29` | Audit: `R-07_findings.json → PERF-R07-001 through PERF-R07-016`
  - **Goal:** Extract 16 inline FlatList props (`keyExtractor`, `renderItem`, `contentContainerStyle`, `columnWrapperStyle`, `getItemLayout`, `ListEmptyComponent`) to stable `useCallback`/`StyleSheet.create` references across 6 components.
  - **Details:** Every inline prop re-creates a new object/function reference on each render, causing FlatList to re-render its entire item list. Pattern picker and gradient library performance is directly affected.

- [x] **`fix/error-handling-sweep`** - Merged 165f7be7: Applied instanceof Error type safety checks to catch blocks. 🚀 Merged in 165f7be7
  - **Tags:** `[⚪ READY]` `[✅ VERIFIED]` `[Services]` `[L-RISK]` `[Meal]` `[🤖 PRO-LOW]` `[BATCH]`
  - **Plan:** 📎 `(generate on /start-task)`
  - **Source of Truth:** 📖 `src/services/supabaseClient.ts:21,29` | `src/utils/migrateAuthTokens.ts:26` | `src/components/CameraTracker.tsx:149` | `src/components/DockedController.tsx:435,467` | `src/hooks/ble/useBLEAutoRecovery.ts:474` | `src/hooks/useCrewManage.ts:62` | `src/services/SpeedTrackingService.ts:149,278` | `src/services/BleWriteQueue.ts:181` | `src/context/SessionContext.tsx:353,386` | Audit: `R-06_findings.json` + `DOMAIN_NATIVE_&_WATCH_findings.json`
  - **Goal:** Replace 17 raw `console.error(e)` and bare `String(e)` error log patterns with the standard `e instanceof Error ? e.message : String(e)` pattern. Replace 1 silent swallow `catch (e) {}` with `AppLogger.warn`.
  - **Details:** 4 HIGH severity `console.error(rawError)` calls can log [object Object] instead of message. Standard error pattern is enforced codebase-wide but these 17 instances were missed.

- [x] **`fix/os-permissions-config`** - Merged: Added missing iOS descriptors and removed redundant Android intents.
  - **Tags:** `[⚪ READY]` `[✅ VERIFIED]` `[BUILD]` `[M-RISK]` `[Snack]` `[🤖 PRO-MED]`
  - **Plan:** 📎 [PLAN-os-permissions-config.md](docs/plans/PLAN-os-permissions-config.md)
  - **Source of Truth:** 📖 `app.config.js:17,21` | `android/app/src/main/AndroidManifest.xml:49` | Audit: `DOMAIN_OS_PERMISSIONS_findings.json → OS_PERM-001, OS_PERM-002, OS_PERM-003`
  - **Goal:** Add missing `NSHealthUpdateUsageDescription` (iOS crash fix), correct inaccurate `NSLocationWhenInUseUsageDescription` (App Store rejection fix), remove 4 duplicate AndroidManifest intent-filter entries.
  - **Details:** Missing `NSHealthUpdateUsageDescription` will crash iOS when Health permissions are requested. Inaccurate location description will trigger App Store review rejection. Both are pre-release blockers.

- [x] **`fix/animated-loop-leak-sweep`** - Merged: Fixed 6 component animation leak bugs with loop.stop() cleanup.
  - **Tags:** `[⚪ READY]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Meal]` `[🤖 PRO-LOW]` `[BATCH]`
  - **Plan:** 📎 [PLAN-animated-loop-leak-sweep.md](docs/plans/PLAN-animated-loop-leak-sweep.md)
  - **Source of Truth:** 📖 `src/components/AccountModal.tsx:84` | `src/components/CrewMemberDashboard.tsx:103` | `src/components/ProductVisualizer.tsx:77` | `src/components/patterns/PatternCard.tsx:37` | `src/components/MarqueeText.tsx:17` | `src/components/CommunityModal.tsx:33` | Audit: `R-22_findings.json → MEM-001 through MEM-006`
  - **Goal:** Add `return () => loop.stop()` cleanup to 6 `Animated.loop().start()` useEffect calls that currently have no cleanup. Prevents background animation loops after component unmount.
  - **Details:** Identical single-line fix pattern across 6 files. Unified Batch Override authorized. CPU leak on every modal close/card unmount.

- [x] **`fix/pii-scrub-sweep`** 🚀 Merged in 1ecea5d6
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[AUTH]` `[H-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Remove user account identifiers (email, display name, user IDs) from 4 AppLogger call sites. MAC addresses explicitly excluded — local-only telemetry, BLE controller MACs are not user-linkable.
  - **Decision Log:** R-09 found 23 raw-ID instances but the indefensible subset is 4: `UserManagementPanel.tsx:222` (full profile object with email+name), `CrewService.ts:375` (userId), `useCrewSession.ts:98` (userId), `DeviceRepository.ts:358` (user.id). MAC address scrubbing excluded by user decision 2026-06-08 — AppLogger is local-only and BLE controller MACs are not personally linkable. Evidence: `R-09_findings.json` (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-pii-scrub-sweep.md](docs/plans/PLAN-pii-scrub-sweep.md)
    Key finding: "4 indefensible PII leaks: 1 full profile object (email+name), 3 raw user IDs. The 19 MAC address instances are excluded per local-only telemetry decision."
    Rejected alternative: "Scrubbing all 23 instances including MACs — rejected 2026-06-08, AppLogger is local-only and BLE controller MACs are not user-linkable in practice."
  - **Source of Truth:** 📖 [R-09_findings.json](artifacts/deepdive_raw/R-09_findings.json) · `src/components/admin/tools/UserManagementPanel.tsx:222` · `src/services/CrewService.ts:375` · `src/hooks/useCrewSession.ts:98` · `src/services/DeviceRepository.ts:358`
  - **Details:** 4 files, ~4 line changes total. `UserManagementPanel.tsx:222` — replace full `data` object log with `{ count: data.length }`. The 3 userId refs — remove from log payload (crew/session IDs provide sufficient debug context). Post-fix grep: `grep -rn "AppLogger" src/ | grep -E "(email|display_name|user\.id|userId)" | grep -v "MAC"` must return zero.

- [x] **`fix/re-entrancy-guards`** 🚀 Merged in bf1d1629
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[AUTH]` `[BLE]` `[H-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Add `isActive` / `isRunningRef` re-entrancy guards to 5 confirmed async functions called from `useEffect` or event listeners without protection, preventing state corruption on rapid re-renders or concurrent deep links.
  - **Decision Log:** R-26 + DOMAIN_IDENTITY confirmed `useRegistration.ts:81`, `SkaterStatsPanel.tsx:24`, `AuthContext.tsx:102` (handleDeepLink) call async functions without guards — on rapid navigation or back-to-back deep links, multiple concurrent calls race each other. Evidence: `R-26_findings.json`, `DOMAIN_IDENTITY_findings.json` (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-re-entrancy-guards.md](docs/plans/PLAN-re-entrancy-guards.md)
    Key finding: "3 CONFIRMED instances in auth/identity layer. handleDeepLink in AuthContext is the highest risk — rapid deep links can corrupt auth state."
    Rejected alternative: "AbortController — rejected because these are not fetch calls; a simple boolean ref is cleaner and zero-dependency."
  - **Source of Truth:** 📖 [R-26_findings.json](artifacts/deepdive_raw/R-26_findings.json) · [DOMAIN_IDENTITY_findings.json](artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json)
  - **Details:** Pattern: `let isActive = true; return () => { isActive = false; }` in useEffect. For event listeners: `useRef(false)` guard checked at function entry, cleared in finally. 3 files, ~15 lines of change total.

- [x] **`fix/auth-context-bypass`** 🚀 Merged in ac739bc6
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[AUTH]` `[M-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Route all 4 direct `supabase.auth.*` calls in UI components through `AuthContext` methods, enabling centralized auth testing and removing the scattered auth bypass smell.
  - **Decision Log:** R-15 + DOMAIN_IDENTITY confirmed `AuthFormSignIn.tsx:73`, `AuthFormSignUp.tsx:106`, `AuthFormForgotPassword.tsx:38`, `useDashboardProfile.ts:113` all bypass the `AuthContext` abstraction and call Supabase directly. This makes auth behavior untestable and creates split-brain risk if auth session handling changes. Evidence: `DOMAIN_IDENTITY_findings.json` lines 122–148 (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-auth-context-bypass-fix.md](docs/plans/PLAN-auth-context-bypass-fix.md)
    Key finding: "4 UI components bypass AuthContext for signIn, signUp, resetPassword, signOut. AuthContext already exists — these calls just aren't routed through it."
    Rejected alternative: "Creating a separate AuthService class — rejected because AuthContext already serves this purpose; adding another abstraction layer is unnecessary complexity."
  - **Source of Truth:** 📖 [DOMAIN_IDENTITY_findings.json](artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json) · `src/context/AuthContext.tsx`
  - **Details:** May require adding `resetPassword` and `signUp` as exported methods on AuthContext if not already present. 4 files, small change each.

- [x] **`refactor/ble-delay-constants`** 🚀 Merged in d647af14
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[H-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Extract all hardcoded millisecond delay values in the BLE pipeline into named constants in `src/constants/bleTimingConstants.ts`, making timing tunable without hunting magic numbers.
  - **Decision Log:** R-16 sniper found ~85 setTimeout usages codebase-wide; after scrubbing UI debounce and tests, ~30 HIGH-risk instances remain in the BLE write pipeline with hardcoded ms values (`50`, `100`, `200`, `250`, `400`, `600`). These are undocumented and impossible to tune safely. Evidence: `R-16_findings.json` BLE service entries (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-ble-delay-constants.md](docs/plans/PLAN-ble-delay-constants.md)
    Key finding: "`BleConnectionManager.ts`, `BleWriteDispatcher.ts`, `BleSessionFactory.ts`, `BlePingService.ts`, `useBLEAutoRecovery.ts`, `BleLifecycleManager.ts` all contain magic ms numbers in BLE pipeline delays."
    Rejected alternative: "Configurable runtime delays — rejected because these are protocol-level timing constraints that should be compile-time constants, not runtime settings."
  - **Source of Truth:** 📖 [R-16_findings.json](artifacts/deepdive_raw/R-16_findings.json) · `tools/ZENGGE_PROTOCOL_BIBLE.md`
  - **Details:** Create `BLE_TIMING` const object. 6 files touched. No logic changes — purely naming. Low execution risk despite H-RISK domain tag.
- [x] **\ix/error-handling-standardization\** � Merged a171835: Standardized ~150 catch blocks with instanceof Error unwrapping.
  - **Tags:** \[? READY]\ \[?? INFERRED]\ \[Services]\ \[L-RISK]\ \[Feast]\ \[?? PRO-MED]\ \[BATCH:deepdive-synthesis-2026-06-08]\
  - **Goal:** Add \instanceof Error\ unwrapping to all ~190 catch blocks that pass raw \e: unknown\ directly to AppLogger, replacing \[object Object]\ telemetry with readable error messages.
  - **Decision Log:** R-06 sniper found 2130 raw entries (~190 unique) of \catch (e: unknown)\ blocks that log \e\ directly without unwrapping. This produces \[object Object]\ in production telemetry, making debugging impossible. The pattern \e instanceof Error ? e.message : String(e)\ is already used correctly in ~60% of the codebase � the other 40% needs to catch up. Evidence: \R-06_findings.json\ (2026-06-08).


- [x] **`refactor/boolean-fsm-admin-tools`** 🚀 Merged in 07f94b36
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Collapse 11 independent boolean visibility states in `AdminToolsModal.tsx` into a single `activePanel: AdminPanel | null` union type, making panel navigation deterministic and eliminating impossible states.
  - **Decision Log:** R-18 sniper found `AdminToolsModal.tsx` declares 11 separate `isXVisible` booleans (lines 65–75). With 11 booleans there are 2^11 = 2048 possible states — only 12 are valid. This is a correctness hazard and a cognitive load tax. Evidence: `R-18_findings.json` lines 59–121 (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-boolean-fsm-admin-tools.md](docs/plans/PLAN-boolean-fsm-admin-tools.md)
    Key finding: "11 booleans → 1 union type. DashboardScreen has 5 booleans but is a monolith (H-RISK) — AdminToolsModal is the safe Snack entry point for this pattern."
    Rejected alternative: "Applying FSM to all 16 affected files at once — rejected as too broad; start with AdminToolsModal as proof of pattern."
  - **Source of Truth:** 📖 [R-18_findings.json](artifacts/deepdive_raw/R-18_findings.json) · `src/components/admin/AdminToolsModal.tsx:65-75`
  - **Details:** 1 file. Define `AdminPanel` union type, replace 11 useState with 1, update all call sites and conditional renders. ~30 lines changed.

- [x] **`fix/state-matrix-error-ui`** 🚀 Merged in 6e5e2601
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[M-RISK]` `[Meal]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Add explicit Error UI state to ~18 data-driven components that silently render stale/empty data on fetch failure, completing the 4-state matrix (Loading, Error, Empty, Success).
  - **Decision Log:** R-14 + DOMAIN_IDENTITY confirmed `SkaterStatsPanel.tsx` swallows `fetchStats` errors and renders zero-value stats silently. `useScenes.ts` and `useGradients.ts` have no error state returned to consumers. Per Offline-First mandate (agent-behavior.md §4), all data views MUST implement the full 4-state matrix. Evidence: `R-14_findings.json`, `DOMAIN_IDENTITY_findings.json:116` (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-state-matrix-error-ui.md](docs/plans/PLAN-state-matrix-error-ui.md)
    Key finding: "~18 components missing error state. P0: SkaterStatsPanel (silently shows zeros), useScenes (empty list on failure), useGradients (no error feedback)."
    Rejected alternative: "Global error boundary only — rejected because per-component error states provide granular UX (retry button, specific message) that a generic boundary cannot."
  - **Source of Truth:** 📖 [R-14_findings.json](artifacts/deepdive_raw/R-14_findings.json) · `agent-behavior.md §4`
  - **Details:** Add `error: string | null` state + retry callback to each affected hook. Consumers render `<ErrorState />`. Use existing error component if one exists.
### [BATCH:deepdive-synthesis-2026-06-08] — Priority-Ordered Sweep | From `/deepdive-code-synthesis`

> **Worktree**: Sequential execution — one task at a time. **Type**: Sequential. **Prerequisite**: None.
> **Source Analysis**: 📊 [system_audit_report.md](artifacts/system_audit_report.md) — 48-agent deep-dive, ~875 unique verified findings across 26 anti-pattern rules.

#### ~~🔴 CRITICAL~~ ❌ VOIDED — FALSE POSITIVE

- [x] ~~**`feat/pattern-engine-protocol-fix`**~~ — **CANCELLED: AUDIT AGENT MISINFORMATION**
  - **Tags:** `[❌ VOIDED]` `[FALSE-POSITIVE]` `[BLE]` `[H-RISK]` `[Snack]`
  - **Plan:** 📎 [PLAN-pattern-engine-protocol-fix.md](docs/plans/PLAN-pattern-engine-protocol-fix.md) *(INVALID — do not execute)*
  - **Void Reason (2026-06-08):** The audit agent (DOMAIN_PATTERN_ENGINE sniper) confused `setCustomModeExtendedCompact` with `setCustomModeExtended`. These are entirely different methods. `setCustomModeExtendedCompact` emits a **21-byte compact 0x51 packet** (direct GATT write, no chunking) — the 10th byte is the **direction flag**, intentionally included. Switching to `setCustomModeCompact` would **break direction support** for all 0x51 patterns. Hardware is functioning correctly. The `dir` field is required, not dead code. Evidence: `ZenggeProtocol.ts:736-771` (method JSDoc + implementation), `PatternEngine.ts:244` comment: *"Use the 10-byte unpadded compact payload so the direction flag is respected!"*

---

#### 🔴 P1 — CRITICAL Security

- [x] **`fix/pii-scrubber-hardening`** — Merged 2924dce6: Expanded PII key coverage to location/auth patterns via substring matching, added array recursion to obfuscate, renamed LocationService label→address, removed hardcoded Maps API key from AndroidManifest.


---

#### 🟠 P2 — High Correctness




---

#### 🟡 P3 — Medium Priority / Architecture Hygiene


- [x] **`refactor/storage-key-registry-v2`** @ a2fdee53 - Standardized AsyncStorage keys across codebase and updated Master Reference.
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[Services]` `[M-RISK]` `[Meal]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Fix 4 raw AsyncStorage string usages bypassing constants, and document 6+ undocumented keys in Master Reference §A.2, completing the storage key registry started by `fix/async-storage-key-registry`.
  - **Decision Log:** R-24 sniper found `AdminToolsModal.tsx:104` uses `'@Sk8lytz_device_configs'` as a raw string while `DeviceRepository.ts` uses `CONFIGS_KEY` constant for the same key. Same split in `useBLEScanner.ts`, `useAccountOverview.ts`, `AuthContext.tsx`. 6 keys used but absent from Master Reference §A.2. Prior merge (b707386d) fixed `ng_` namespace keys but missed these. Evidence: `R-24_findings.json` (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-storage-key-registry-v2.md](docs/plans/PLAN-storage-key-registry-v2.md)
    Key finding: "4 raw string key violations, 6 undocumented keys. Prior merge missed these secondary violations."
    Rejected alternative: "Automated key validation at runtime — rejected as over-engineering; static constants enforced at import time is sufficient."
  - **Source of Truth:** 📖 [R-24_findings.json](artifacts/deepdive_raw/R-24_findings.json) · `src/constants/storageKeys.ts` · `tools/SK8Lytz_App_Master_Reference.md`
  - **Details:** 4 raw string fixes + Master Reference §A.2 update. Must update docs per VS-003 rule before running gatekeeper.



---

#### 🔵 P4 — Type Safety Sweeps (Large Scope)

- [x] **`refactor/type-safety-data-layer`** @ 76ac0911 — Replaced all double-casts with `.returns<T>()` and strict guards
  - **Tags:** `[✅ COMPLETED]` `[🤔 INFERRED]` `[Services]` `[M-RISK]` `[Meal]` `[🤖 THINK]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Fix `as unknown as` type laundering and `payload: any` in 6 core data layer service files by replacing unsafe double-casts with proper type guards or correctly typed Supabase queries.
  - **Decision Log:** DOMAIN_DATA_LAYER + R-08 confirmed 20+ type laundering instances. Fixed via strict Supabase interface alignment.
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-type-safety-data-layer.md](docs/plans/PLAN-type-safety-data-layer.md)
  - **Source of Truth:** 📖 [DOMAIN_DATA_LAYER_findings.json](artifacts/deepdive_raw/DOMAIN_DATA_LAYER_findings.json) · `src/types/supabase.ts`
  - **Details:** Prerequisite for `refactor/type-safety-ui-layer` is now cleared.

- [x] **`refactor/type-safety-ui-layer`** @ 38d792dd - Resolved prop `any` types across UI layer and stabilized display device mapping.
  - **Tags:** `[✅ COMPLETED]` `[🤔 INFERRED]` `[UI]` `[M-RISK]` `[Feast]` `[🤖 PRO-MED]` `[BATCH:deepdive-synthesis-2026-06-08]`
  - **Goal:** Fix `: any` prop type annotations across Dashboard UI, Admin Diagnostic Lab tabs, and Docked components — replacing prop `any` with explicit types from the domain model.
  - **Decision Log:** R-08 sniper found ~380 total `any` instances across 253 files. UI layer accounts for ~180 of these, primarily in Admin Diagnostic Lab tab components (25) and Dashboard slabs (15). These are prop interfaces lacking domain type imports. Blocked by data layer task — service types must be clean first to avoid cascading errors. Evidence: `R-08_findings.json` (2026-06-08).
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-type-safety-ui-layer.md](docs/plans/PLAN-type-safety-ui-layer.md)
    Key finding: "UI layer `any` casts are primarily prop interface gaps — import the right type from `src/types/` and the fix is trivial."
    Rejected alternative: "One giant sweep of all 380 instances — rejected as too high collision risk. Sequential: data layer → UI layer."
  - **Source of Truth:** 📖 [R-08_findings.json](artifacts/deepdive_raw/R-08_findings.json) · `src/types/`
  - **Details:** Execute in 4 sub-batches (Diagnostic Lab tabs → Dashboard slabs → Docked → Remaining). Commit + TSC check after each sub-batch. **Blocked by `refactor/type-safety-data-layer`.**

---


- [x] **`fix/audit-fixes-ble-signal`** `[MERGE: 54cc1111]`
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[M-RISK]` `[Meal]` `[🤖 PRO-MED]` `[BATCH:audit-fixes-ble-signal]`
  - **Goal:** Fix 5 BLE signal/state bugs: fire FSM `CONNECT_SUCCESS`/`DISCONNECT_COMPLETE`, fix RSSI stale prune dep, wire `onWeakSignal`, correct power opcode `0xCC→0x71`, remove duplicate `bleGateState` dep.
  - **Decision Log (2026-06-09):** BleMachine defines `CONNECT_SUCCESS` and `DISCONNECT_COMPLETE` events that are never fired by `BleConnectionManager` or `BleLifecycleManager`. FSM transitions `IDLE→CONNECTING→IDLE` only — `READY` and `DISCONNECTING` states are unreachable. `derivedBleState` in `useBLE.ts:602-606` compensates by reading `connectedDevices.length` directly, masking the bug today. `BleWriteQueue.ts:52` maps `0xCC` as power opcode — Zengge's actual power opcode is `0x71` (ZenggeProtocol.ts:829). Power writes dispatched as `normal` priority instead of `critical`. `useBLERSSIMonitor.ts:134` prune depends on `[bleManager]` which never changes — stale RSSI persists post-disconnect.
  - **Analysis:** 📊 Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) · Plan: [PLAN-audit-fixes-ble-signal.md](docs/plans/PLAN-audit-fixes-ble-signal.md)
    Key finding: "FSM READY state unreachable — any future FSM subscriber will never see CONNECTED. Power opcode 0xCC never emitted — power writes run at normal not critical priority."
    Rejected alternative: "Removing the READY/DISCONNECTING states from BleMachine entirely — rejected because they are part of the documented architecture and future UI consumers (connection animations) will need them."
  - **Source of Truth:** 📖 [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) · [BleConnectionManager.ts:295-310](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L295-L310) · [BleWriteQueue.ts:52](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts#L52) · [ZenggeProtocol.ts:829](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts#L829) · [useBLERSSIMonitor.ts:120-134](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLERSSIMonitor.ts#L120-L134) · [useBLE.ts:466-476,667,673](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L466-L476)
  - **Details:** 5 atomic surgical fixes. `bleSend` must be added to `BleConnectionRequest` type — check first if already there. `connectedDeviceIds: string[]` added to `UseBLERSSIMonitorParams` — passed from useBLE call site. Changing `0xCC→0x71` promotes power writes from `normal→critical` — this is **intentionally correct** (power should preempt pattern writes). `onWeakSignal` wired to AppLogger only, not auto-recovery — weak signal is informational not critical.

- [x] **`fix/audit-fixes-scanner`** 6d5f9130 - Fixed scanner typescript issues, added double-start guard, and surfaced low battery warning to UI.
  - **Tags:** `[✅ MERGED]` `[✅ VERIFIED]` `[BLE]` `[M-RISK]` `[Meal]` `[🧠 PRO-MED]` `[BATCH:audit-fixes-scanner]`
  - **Goal:** Type `BleLifecycleManager` signature, surface battery-constrained warning to UI, add scan double-start guard, remove `as unknown as any` cast in DashboardScreen.
  - **Decision Log (2026-06-09):** `BleLifecycleManager.ts:11-18` is fully `any`-typed — `cancelAllRecoveries()` call on `autoRecovery: any` is not TS-verified. `useBLEBatterySweep.ts:69-72`: battery < 15% causes silent sweeper shutdown with no UI feedback — 1500ms auto-connect races battery check and waits for devices that never appear. `useBLEScanner.ts:299` calls `startDeviceScan` without checking if scan is already active (no double-start guard). `DashboardScreen.tsx:1134` has `as unknown as any` cast — bypasses type system in the most complex file in the codebase.
  - **Analysis:** 📊 Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) · Plan: [PLAN-audit-fixes-scanner.md](docs/plans/PLAN-audit-fixes-scanner.md)
    Key finding: "Battery PAUSED path silently returns without notifying UI — user sees 0 devices and no explanation. BleLifecycleManager.ts is fully any-typed — `cancelAllRecoveries()` TS-unverified."
    Rejected alternative: "Showing a modal instead of a banner for battery warning — rejected because a modal blocks the UI and users need to still navigate while constrained."
  - **Source of Truth:** 📖 [BleLifecycleManager.ts:11-18](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleLifecycleManager.ts#L11-L18) · [useBLEBatterySweep.ts:65-72](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts#L65-L72) · [useBLEScanner.ts:299](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L299) · [DashboardScreen.tsx:1134](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L1134) · [IControllerProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/IControllerProtocol.ts)
  - **Details:** God Object lock applies to DashboardScreen — ONLY touch line 1134 (cast fix) and the 2-line banner conditional. No other changes in DashboardScreen. `BleLifecycleManager` type fixes require `BleManager` import from `react-native-ble-plx` — check if already imported before adding. Battery banner must use existing StyleSheet pattern from DashboardScreen.

- [x] **`fix/re-entrancy-guards-phase-2`** `[MERGE: 39490c68]` — Added useRef boolean gates to checkAutoPause, syncSessionState, pollHealthData, and scan.
  - **Tags:** `[⚪ TRIAGE]` `[🤔 INFERRED]` `[Services]` `[H-RISK]` `[Meal]` `[🤖 PRO-MED]`
  - **Goal:** Add re-entrancy guards to 4 remaining R-26 instances NOT covered in phase 1: `SessionContext.tsx` (checkAutoPause GPS flood + syncSessionState AppState race), `DashboardScreen.tsx` (checkNewDevice — monolith, extract first), `useHealthTelemetry.ts` (setInterval pattern), `useCrewProximityRadar.ts` (partially guarded).
  - **Decision Log (2026-06-08):** These were found in R-26 during fix/re-entrancy-guards execution. DashboardScreen is blocked by monolith extraction (>30KB rule). SessionContext is high-risk — requires spike to understand GPS speed dependency chain before touching. Filed to TRIAGE per zero-collateral-damage rule.
  - **Source of Truth:** 📖 [R-26_findings.json](artifacts/deepdive_raw/R-26_findings.json) entries 2–5

- [x] **`fix/triage-ble-buffer-lockout`** `[MERGE: 3b9eca9f]`
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[BLE]` `[H-RISK]` `[Snack]` `[🤖 PRO-HIGH]` `[BATCH:triage-sweep]`
  - **Goal:** Enforce 12-pixel minimum length constraint for 0x59 payloads to prevent A3 chipset EEPROM buffer lockouts.
  - **Decision Log (2026-06-08):** Flagged during deep-dive synthesis: payloads below 10 pixels lock the A3 buffer.
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-triage-ble-buffer-lockout.md](docs/plans/PLAN-triage-ble-buffer-lockout.md)
    Key finding: "BleWriteDispatcher.ts fails to enforce a minimum length constraint for 0x59 payloads."
    Rejected alternative: "Modifying the firmware — rejected as it's outside our control; client must pad."
  - **Source of Truth:** 📖 `.agents/rules/agent-behavior.md` Rule 10
  - **Details:** Must pad 0x59 payloads < 12 pixels.

- [x] **`cloud/triage-cloud-security`** `[MERGE: e38ca42f]`
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:triage-sweep]`
  - **Goal:** Fix IDOR/Privilege Escalation edge function vulnerability and Search Path Hijacking in migrations.
  - **Decision Log (2026-06-08):** Flagged during deep-dive synthesis: SECURITY DEFINER functions missing path resets.
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-triage-cloud-security.md](docs/plans/PLAN-triage-cloud-security.md)
    Key finding: "Multiple SECURITY DEFINER functions omit SET search_path = public."
    Rejected alternative: "Dropping the functions — rejected because they are required for admin operations."
  - **Source of Truth:** 📖 `tools/SK8Lytz_App_Master_Reference.md` § Supabase Security Rules
  - **Details:** Fix migrations and edge function authorization checks.

- [x] **`refactor/triage-type-safety`**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Batch]` `[🤖 PRO-MED]` `[BATCH:triage-sweep]`
  - **Goal:** Replace dangerous `any` casts with `unknown` or specific interfaces across UI and BLE hooks.
  - **Decision Log (2026-06-08):** Flagged during deep-dive synthesis: multiple direct `any` bypasses in core components.
  - **Analysis:** 📊 Source: [system_audit_report.md](artifacts/system_audit_report.md) · Plan: [PLAN-triage-type-safety.md](docs/plans/PLAN-triage-type-safety.md)
    Key finding: "Strict type violations (any) used extensively to bypass TS compilation errors."
    Rejected alternative: "Using @ts-ignore — rejected as it violates the same rule."
  - **Source of Truth:** 📖 `.agents/rules/prime-directive.md` S3
  - **Details:** Surgical replacements only. Do not refactor actual logic.

- [x] **`fix/wizard-ftue-scan`** `[MERGE: 54cc1111]`
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[H-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Goal:** Fix the async sweeper race that leaves new users stuck on "Scanning for devices..." forever in the Hardware Setup Wizard — app is non-functional for all new installs.
  - **Decision Log (2026-06-09):** Confirmed Strike 2 on this bug. Previous Fix 1 patched 5s→10s timeout. Previous Fix 2 adjusted RSSI. Neither fixed the root cause: `startSweeper()` is async (calls `Battery.getBatteryLevelAsync()`), so `isSweeperActive` is still `false` when the wizard synchronously calls `scanForPeripherals()`. Wizard hits the raw 5s scan → hard stop → no retry on step 1 → infinite "Scanning..." state. Industry standard (Govee, LIFX, Hue, Sonos): discovery is a service-level concern, wizard is a passive subscriber. Fix routes FTUE through `startSweeper()` directly, bypassing the race.
  - **Analysis:** 📊 Source: Direct read 2026-06-09 · Plan: [PLAN-wizard-ftue-scan.md](docs/plans/PLAN-wizard-ftue-scan.md)
    Key finding: "`scanForPeripherals()` checks `isSweeperActive` synchronously but `startSweeper()` resolves async (battery check). Race = 5s raw scan + hard stop + no step-1 keepAlive retry = wizard stuck forever."
    Rejected alternatives: "Increase timeout (Fix 1 — failed). Adjust RSSI (Fix 2 — failed). Both missed the async race root cause."
  - **Source of Truth:** 📖 [useBLEScanner.ts:291-307](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L291-L307) · [useBLEBatterySweep.ts:65](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts#L65) · [HardwareSetupWizardScreen.tsx:113](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L113)
  - **Details:** 2-file surgical fix. `useBLEScanner.ts`: add `registeredMacs.length === 0` FTUE branch that calls `startSweeper()` and returns immediately — eliminates the race entirely. `HardwareSetupWizardScreen.tsx`: change `step === 2` to `(step === 1 || step === 2)` in keepAlive gate — belt-and-suspenders fallback ensures retry fires even when stuck on step 1. **⚠️ Three-Strike rule active — verify `startSweeper()` is idempotent in Step 3 of the plan BEFORE writing any code.**

- [x] **`fix/audit-fixes-auth`** - merged @ e732f8f (Offline rpc crash fixed)
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[Auth]` `[H-RISK]` `[Meal]` `[🤖 PRO-MED]` `[BATCH:audit-fixes-auth]`
  - **Goal:** Fix 4 confirmed auth path bugs: offline `rpc` crash (HIGH), silent profile error swallowing (MEDIUM), dead `safeErr` variables (LOW), duplicated ternary dead code (LOW).
  - **Decision Log (2026-06-09):** Offline stub at `supabaseClient.ts:48-64` has no `rpc` method. `AuthFormSignIn.tsx:57` calls `supabase.rpc('get_email_by_username', ...)` for username login — throws `TypeError: supabase.rpc is not a function` in offline mode. Confirmed by reading both files in the 2026-06-09 audit. Highest priority auth finding.
  - **Analysis:** 📊 Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) · Plan: [PLAN-audit-fixes-auth.md](docs/plans/PLAN-audit-fixes-auth.md)
    Key finding: "Offline stub missing `rpc` method — username login crashes in offline mode (H1). AuthProfileService.ts:79 comment acknowledges missing AppLogger call (M5)."
    Rejected alternative: "Null-checking the rpc call site — rejected because every future offline rpc call would also need a guard; fixing the stub is the single-source fix."
  - **Source of Truth:** 📖 [supabaseClient.ts:48-64](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/supabaseClient.ts#L48-L64) · [AuthFormSignIn.tsx:57](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignIn.tsx#L57) · [AuthProfileService.ts:77-82](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts#L77-L82) · [SessionContext.tsx:44,323,401](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L44)
  - **Details:** 4 atomic fixes in one worktree. Step 1 (H1 — add `rpc` stub) is the only user-facing crash risk. Steps 2-4 are Boy Scout cleanups. No changes outside auth domain. `DashboardScreen.tsx` is explicitly out of scope (God Object lock).

- [x] **`fix/audit-fixes-ble-protocol`** @ 3af6b482 - UI dispatches now route through the proper device adapter
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[H-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:audit-fixes-ble-protocol]`
  - **Goal:** Route pattern dispatch through the per-device `IControllerProtocol` adapter so BanlanX devices in mixed groups receive correct protocol bytes instead of Zengge 0x59 packets.
  - **Decision Log (2026-06-09):** `useControllerDispatch.ts:17-21` imports and calls `ZenggeProtocol.setMultiColor()` directly. Comment at lines 10-11 explicitly bans calling `useProtocolDispatch()` (orphan BLE instance risk). `PatternEngine.ts:192` also calls Zengge static. BLE Payload Dispatch auditor confirmed: solo BanlanX ✅, solo Zengge ✅, mixed group ❌. Fix routes through `adapterMapRef` (already populated at connection time by `createGattSession` — same map used by BleWriteDispatcher.ts:143).
  - **Analysis:** 📊 Source: [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md) · Plan: [PLAN-audit-fixes-ble-protocol.md](docs/plans/PLAN-audit-fixes-ble-protocol.md)
    Key finding: "Two parallel dispatch chains exist. Chain B (DockedController → useControllerDispatch) bypasses IControllerProtocol adapter entirely — BanlanX in mixed groups receives Zengge 0x59 packets."
    Rejected alternative: "Calling useProtocolDispatch() from useControllerDispatch — banned by comment at useControllerDispatch.ts:10-11 (creates orphan useBLE instance). Rejected."
  - **Source of Truth:** 📖 [useControllerDispatch.ts:17-21](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts#L17-L21) · [PatternEngine.ts:192](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/PatternEngine.ts#L192) · [IControllerProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/IControllerProtocol.ts) · [BleWriteDispatcher.ts:143](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts#L143)
  - **Details:** Adds `getAdapterForDevice` + `primaryDeviceId` to `UseControllerDispatchParams`. Adds optional `protocol` param to `buildPatternPayload` with Zengge as default (zero regression for existing solo-Zengge users). `DockedController.tsx` changes are surgical — only the `useControllerDispatch` call site (lines 551-560). God Object lock still applies — no other changes in that file.

- [x] **`feat/restore-virtual-skates`** — merged @ 0aca27c1 (restored virtual skates DI)
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🤖 PRO-HIGH]` `[BATCH:virtual-skates]`
  - **Goal:** Restore the Dev Sandbox Virtual Skates by injecting Mock devices into the BLE Scanner engine.
  - **Decision Log:** The UI toggle correctly sets the DEMO flag, but the scanner engine currently drops the connection loop because it never intercepts the physical scan. This restores local testing velocity without actual hardware.
  - **Analysis:** 📊 Plan: [PLAN-restore-virtual-skates.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/plans/PLAN-restore-virtual-skates.md)
    Key finding: "The previous implementation set STORAGE_DEMO_HALO/SOUL but the scanner never read them. We will unify under STORAGE_DEMO_MODE."
    Rejected alternative: "Mocking the BleManager library completely via jest.mock — rejected because it breaks the iOS build and doesn't run in the actual Expo Go app."
  - **Source of Truth:** 📖 `src/hooks/ble/useBLEScanner.ts`
  - **Details:** Must strictly enforce `__DEV__` gating. Use Dependency Injection in the scanner to yield fake Device objects instead of triggering the Bluetooth radio.

- [x] **`bug/split-brain-telemetry-drop`**
  - **Tags:** `[⚪ TRIAGE]` `[❌ UNVERIFIED]` `[BLE]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Goal:** Fix `DeviceRepository.saveDevice` to properly capture and persist `ble_version`, `factory_name`, and `manufacturer_data` to `registered_devices` during the registration flow instead of hardcoding `null`.
  - **Decision Log (2026-06-09):** The global admin dashboard revealed that `registered_devices` drops BLE chipset version data. A spike confirmed that the BLE scanner captures it, but `DeviceRepository.ts` strips it.
  - **Plan:** 📎 [PLAN-split-brain-telemetry-drop.md](docs/plans/PLAN-split-brain-telemetry-drop.md)
  - **Source of Truth:** 📖 `src/services/DeviceRepository.ts`
  - **Details:** Revert dashboard join workarounds and fix the root cause in the mobile app persistence layer.

- [x] **`fix/db-hygiene-batch`**
  - **Tags:** `[🚧 ACTIVE]` `[✅ VERIFIED]` `[CLOUD]` `[M-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:db-hygiene]`
  - **Goal:** Fix 5 orphaned database fields (presets updated_at, scanner is_claimed, remove active_calories, crew role mapping, crew avatar URL).
  - **Decision Log:** See PLAN-db-hygiene-batch.md for BLE deduplication logic and health metrics calories mapping rules.
  - **Source of Truth:** 📖 `src/types/supabase.ts`
  - **Details:** Updating payloads across multiple services to achieve 1:1 parity with database schema.

- [x] **`fix/db-telemetry-drift`** 2026-06-09 merged dual-write and drift fix
  - **Tags:** `[📦 ARCHIVE]` `[✅ VERIFIED]` `[CLOUD/BLE]` `[H-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:telemetry-drift]`
  - **Goal:** Dual-write crash logs, fix location data black hole, and fix solo lifetime stats drift.
  - **Decision Log:** Dual-write pattern approved to safely hydrate crash_telemetry without breaking legacy dashboards.
  - **Source of Truth:** 📖 `src/services/AppLogger.ts`, `src/services/SpeedTrackingService.ts`
  - **Details:** Worktree active. See PLAN-db-telemetry-fix.md for payload mapping.

- [x] **`fix/eeprom-product-confirm`**
  - **Tags:** `[🔥 ON DECK]` `[❌ UNVERIFIED]` `[BLE/DB]` `[M-RISK]` `[Snack]` `[🤖 PRO-LOW]` `[BATCH:db-hygiene]`
  - **Goal:** Ensure `product_id_confirmed_at` is populated correctly.
  - **Decision Log:** EEPROM 0x63 responses successfully identify product IDs but fail to persist the confirmation back to registered_devices. This task wires that persistence.
  - **Source of Truth:** 📖 `src/hooks/useControllerDispatch.ts`, `src/services/DeviceRepository.ts`

- [x] **`feat/map-relational-drilldown`** 🚀 Merged in 2188ff2a
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI/CLOUD]` `[M-RISK]` `[Meal]` `[🤖 PRO-MED]` `[BATCH:fleet-map-ops]`
  - **Goal:** Transform the Command Center map into a lazy-loaded relational inspector with spider-web vectors, Crew Zones, and Supabase Realtime telemetry firehose.
  - **Decision Log:** Brainstorming session confirmed the user wants interactive relational drill-downs connecting users to hardware/crews visually, plus live moving telemetry via WebSockets. Historical polylines were rejected as too invasive.
  - **Analysis:** 📊 Plan: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/26f16420-6723-4436-a1a4-3544dd1b501f/implementation_plan.md)
    Key finding: "We must use lazy-loaded Supabase queries on click to prevent overwhelming the browser with the full relational graph."
    Rejected alternative: "Showing historical skate paths (polylines) — rejected due to privacy/invasiveness."
  - **Source of Truth:** 📖 `src/types/supabase.ts` & `MapWidget.tsx`
  - **Details:** Must implement Supabase `.channel()` for real-time `telemetry_snapshots` inserts to move pins without refresh.

- [x] **`feat/live-debugger-suite`**
  - **Tags:** `[? READY]` `[? VERIFIED]` `[UI/DB]` `[M-RISK]` `[Meal]` `[?? PRO-MED]` `[BATCH:live-debugger-suite]`
  - **Goal:** Evolve the Live Debugger into a 3-tab Diagnostic Suite (Live Stream, Crash Autopsy, Non-Fatal Diagnostics) with alerting and grouping.
  - **Decision Log:** The initial Live Debugger is a basic feed; user requires a robust interface to triage, group, and resolve errors (90-day retention, simple DB status updates) using crash and error telemetry.
  - **Analysis:** ?? Plan: [PLAN-live-debugger-suite.md](../../docs/plans/PLAN-live-debugger-suite.md)
  - **Source of Truth:** ?? `src/components/widgets/LiveDebuggerWidget.tsx`, `crash_telemetry`, `telemetry_errors`
  - **Details:** Requires creating a Supabase View `view_crash_aggregates`, updating `LiveDebuggerWidget.tsx` with AG Grid, and adding specific highlighting for spike thresholds (>10 crashes/hour).

- [x] **`feat/auto-factory-tagging`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:ble-discovery]`
  - **Goal:** Add BLE signature fingerprinting to `useBLEScanner.ts` to automatically populate the `factory_name` property during discovery.
  - **Decision Log:** User noted `manufacturer_data` is a raw Base64 string and asked for "Zengge" or "BanlanX" mapping. We need to infer `factory_name` natively on the scanner layer.
  - **Analysis:** 📊 Plan: [PLAN-auto-factory-tagging.md](../../docs/plans/PLAN-auto-factory-tagging.md)
    Key finding: "Zengge and BanlanX emit specific service UUIDs (FFE5 vs FFE0+0x5053) which we can map to factory_name."
    Rejected alternative: "Storing the raw base64 string in factory_name — rejected because it is not human-readable and violates DB schema intent."
  - **Source of Truth:** 📖 `src/hooks/ble/useBLEScanner.ts`
  - **Details:** Must parse `device.serviceUUIDs` and `device.manufacturerData` to determine the brand before yielding to the UI state.
### ⚡ [BATCH:fleet-map-ops] — `feat/map-relational-drilldown` — [✅ READY]
> **Worktree**: `feat/map-relational-drilldown` · **Type**: Standalone · **Prerequisite**: None
> **Source Analysis**: 📊 [SESSION_LOG.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SESSION_LOG.md) — Map transitioning to a relational inspector with realtime telemetry.

- [x] **`feat/docker-web-demo`**
  - **Tags:** `[🚧 ACTIVE]` `[✅ VERIFIED]` `[TOOLING]` `[L-RISK]` `[Snack]` `[🤖 PRO-MED]` `[BATCH:docker-web-demo]`
  - **Goal:** Containerize the Expo Web Demo using Docker to resolve manual boot friction.
  - **Decision Log:** User experienced persistent friction restarting the web demo manually on port 8081. Transitioning to a Docker service within the existing `docker-compose.yml` stack to match Command Center/Scraper behavior.
  - **Source of Truth:** 📖 `docker-compose.yml`, `Dockerfile.web` (to be created)

- [x] **`refactor/ble-p3-connect-service`** — merged `e92c63c6`
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[M-RISK]` `[Meal]` `[🧠 HIGH]` `[BATCH:ble-xstate-pipeline]`
  - **Goal:** Move GATT connection pipeline into a `fromPromise` XState service invoked by CONNECTING state. `BleConnectionManager.ts` deleted. Machine owns full connect lifecycle.
  - **Decision Log (2026-06-10):** Audit found 7 call sites of `connectToDevices` in `DashboardScreen.tsx` (lines 543, 574, 596, 611, 645, 845, 1195) — all routing through `BleConnectionManager.ts:42`. Manual `CONNECT_SUCCESS`/`CONNECT_FAIL` bleSend calls require the caller to correctly bracket the state machine, which it doesn't always do under async pressure. XState `invoke.onDone`/`onError` is atomic.
  - **Analysis:** 📊 Source: [BLE_AUDIT_REPORT.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_REPORT.md) §3 Async Path Map · Plan: [PLAN-ble-p3-connect-service.md](./plans/PLAN-ble-p3-connect-service.md)
    Key finding: "Device Connection entry trigger `BleConnectionManager.ts:42` — exit cleanup `BleConnectionManager.ts:88` — no abort signal propagated to intermediate GATT awaits"
    Rejected alternative: "Fix cancellation in BleConnectionManager — rejected because the root problem is that the machine does not own the lifecycle; fixing the manager leaves the structural gap"
  - **Source of Truth:** 📖 [BleConnectionManager.ts:42-88](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleConnectionManager.ts#L42) · [BleSessionFactory.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleSessionFactory.ts)
  - **Details:** Pure GATT helpers (GATT 133 retry, MTU negotiation, handshake) move to `ConnectService.ts` or `BleSessionFactory.ts`. Double-tap gating is now free — machine in CONNECTING ignores CONNECT_REQUEST.

- [x] **fix/ble-t2-static-guards** — Add BLE architecture invariant guards to verifiable-check-runner 🚀 Merged in bdcfd9f8
  - **Tags:** [✅ READY] [✅ VERIFIED] [TOOLING] [L-RISK] [Snack] [🤖 PRO-LOW] [BATCH:ble-test-hardening]
  - **Goal:** Add 2 new static checks to `tools/verifiable-check-runner.js`: (1) detect `bleManager.startDeviceScan()` calls outside BleMachine.ts, (2) detect `onOrganicDisconnect` missing from useBLE.ts machine input.
  - **Decision Log (2026-06-10):** The dual-scan bug and the organic disconnect bug were both invisible to the test suite. Static AST/grep guards at the gate level catch these at commit time, not at runtime during a skate session.
  - **Analysis:** 📊 Source: [ble_test_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a20aff2e-be9b-43b9-b217-269aa20e5f8a/ble_test_gap_analysis.md) — "Missing Static git gate" section
  - **Plan:** 📎 [PLAN-ble-t2-static-guards.md](docs/plans/PLAN-ble-t2-static-guards.md)
  - **Source of Truth:** 📖 `tools/verifiable-check-runner.js` · `src/services/ble/BleMachine.ts` · `src/hooks/useBLE.ts`
  - **Details:** Both guards must pass on current codebase AND fail when the pattern is violated. Snack-sized — pure JS additions to existing check runner, no new files needed.

- [x] **test/ble-t1-machine-tests** — BleMachine state machine unit tests 🚀 Merged in 2b1226a8
  - **Tags:** [✅ READY] [✅ VERIFIED] [BLE] [M-RISK] [Meal] [🧠 MED] [BATCH:ble-test-hardening]
  - **Goal:** Write comprehensive unit tests for all 6 BleMachine state transitions + the organic disconnect → RECOVERY_START wiring. This test is the regression guard for the HIGH-severity silent bug fixed in 2276ac8a.
  - **Decision Log (2026-06-10):** Audit found zero tests for BleMachine.ts. The organic disconnect fix has no guard — any future refactor of useBLE.ts input construction silently breaks recovery again with a green test suite. Filed as P0.
  - **Analysis:** 📊 Source: [BLE_AUDIT_2/01_bleMachine.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/01_bleMachine.md) · [SESSION_LOG.md DECISION 2026-06-10T08:38](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SESSION_LOG.md)
  - **Plan:** 📎 [PLAN-ble-t1-machine-tests.md](docs/plans/PLAN-ble-t1-machine-tests.md)
  - **Source of Truth:** 📖 `src/services/ble/BleMachine.ts` · `src/hooks/useBLE.ts:L182-187`
  - **Details:** 18 test cases across 3 groups: state transitions, organic disconnect regression guard, context assertions. Use XState `createActor` with mocked service stubs.

- [x] **test/ble-t3-connect-service-tests** — ConnectService unit tests 🚀 Merged in 43377f8c
  - **Tags:** [✅ READY] [✅ VERIFIED] [BLE] [M-RISK] [Meal] [🧠 MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for ConnectService.ts covering group connect, GATT 133 retry, stale device flush, MTU negotiation, adapter mapping, notification registration, and the onOrganicDisconnect disconnect subscription.
  - **Decision Log (2026-06-10):** Post-migration audit confirmed zero tests for ConnectService. This is the most complex actor (288 lines, 3 retry loops, MTU negotiation, multi-device sequential flow). Any regression is invisible until hardware fails in the field.
  - **Analysis:** 📊 Source: [BLE_AUDIT_2/02_connectService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/02_connectService.md)
  - **Plan:** 📎 [PLAN-ble-t3-connect-service-tests.md](docs/plans/PLAN-ble-t3-connect-service-tests.md)
  - **Source of Truth:** 📖 `src/services/ble/ConnectService.ts` · `tools/BLE_AUDIT_2/02_connectService.md`
  - **Details:** 18 test cases across 7 groups. Mock `bleManager`, `createGattSession`, and capture `onDeviceDisconnected` callback to verify `onOrganicDisconnect` fires.

- [x] **test/ble-t4-recovery-service-tests** — RecoveryService unit tests
  - **Tags:** [✅ READY] [✅ VERIFIED] [BLE] [M-RISK] [Meal] [🧠 MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for RecoveryService.ts covering Phase 1/2/3 backoff, clearWriteQueue regression guard, RECOVERY_COMPLETE/FAIL events, and re-registration of notifications + adapter after reconnect.
  - **Decision Log (2026-06-10):** The clearWriteQueue() fix (2276ac8a) also has no regression guard. The 3-phase recovery loop is the most complex async flow in the codebase — zero tests means any breakage is silent.
  - **Analysis:** 📊 Source: [BLE_AUDIT_2/03_recoveryService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/03_recoveryService.md)
  - **Plan:** 📎 [PLAN-ble-t4-recovery-service-tests.md](docs/plans/PLAN-ble-t4-recovery-service-tests.md)
  - **Source of Truth:** 📖 `src/services/ble/RecoveryService.ts` · `tools/BLE_AUDIT_2/03_recoveryService.md`
  - **Details:** 13 test cases across 5 groups. Use `jest.useFakeTimers()` to fast-forward backoff delays. Assert `clearWriteQueue` called as first action.

- [x] **test/ble-t5-heartbeat-service-tests** — HeartbeatService unit tests
  - **Tags:** [✅ READY] [✅ VERIFIED] [BLE] [L-RISK] [Meal] [🧠 MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for HeartbeatService.ts covering 45s tick, exact 0x63 opcode bytes, HEARTBEAT_FAIL event, RSSI fallback path, multi-device sequential loop, and clearInterval cleanup.
  - **Decision Log (2026-06-10):** HeartbeatService is the liveness detector for connected devices. A broken heartbeat means dead skates that appear connected. Zero tests.
  - **Analysis:** 📊 Source: [BLE_AUDIT_2/04_heartbeatService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/04_heartbeatService.md)
  - **Plan:** 📎 [PLAN-ble-t5-heartbeat-service-tests.md](docs/plans/PLAN-ble-t5-heartbeat-service-tests.md)
  - **Source of Truth:** 📖 `src/services/ble/HeartbeatService.ts` · `tools/BLE_AUDIT_2/04_heartbeatService.md`
  - **Details:** 12 test cases across 6 groups. Verify exact opcode bytes [0x63, 0x12, 0x21, 0x0F, 0xA5] from audit. Use fake timers.

- [x] **test/ble-t6-interrogator-service-tests** — InterrogatorService unit tests
  - **Tags:** [✅ READY] [✅ VERIFIED] [BLE] [L-RISK] [Meal] [🧠 MED] [BATCH:ble-test-hardening]
  - **Goal:** Write unit tests for InterrogatorService.ts covering probe lifecycle, cancelDeviceConnection in finally (both success and failure paths), FTUE vs standard queue delay, AsyncStorage parse error resilience.
  - **Decision Log (2026-06-10):** InterrogatorService reads hardware EEPROM on first contact — wrong behavior here means wrong LED counts and broken color patterns for the user's lifetime. Zero tests.
  - **Analysis:** 📊 Source: [BLE_AUDIT_2/06_interrogatorService.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/BLE_AUDIT_2/06_interrogatorService.md)
  - **Plan:** 📎 [PLAN-ble-t6-interrogator-service-tests.md](docs/plans/PLAN-ble-t6-interrogator-service-tests.md)
  - **Source of Truth:** 📖 `src/services/ble/InterrogatorService.ts` · `tools/BLE_AUDIT_2/06_interrogatorService.md`
  - **Details:** 12 test cases across 5 groups. Mock AsyncStorage, createGattSession, enqueueWrite. Verify `cancelDeviceConnection` called in finally on both success AND failure paths.

- [x] **fix/street-mode-accelerometer** 🚀 Merged in 92a3b893
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [CORE] [M-RISK] [Snack] [🤖 PRO-MED] [BATCH:group-concurrency-sweep]
  - **Goal:** Fix accelerometer race condition missing `useRef` flag for street mode triggers.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster F).
  - **Plan:** 📎 [PLAN-street-mode-accelerometer.md](docs/plans/PLAN-street-mode-accelerometer.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-10)

- [x] **fix/session-notifee-listener** 🚀 Merged in 92a3b893
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [CORE] [M-RISK] [Meal] [🤖 PRO-MED] [BATCH:group-concurrency-sweep]
  - **Goal:** Implement cleanup pattern for Notifee event listeners to prevent duplicate background alerts.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster F).
  - **Plan:** 📎 [PLAN-session-notifee-listener.md](docs/plans/PLAN-session-notifee-listener.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-10)

- [x] **fix/memory-leak-hardware-notifications**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [CORE] [L-RISK] [Snack] [🤖 PRO-MED] [BATCH:memory-leak-sweep]
  - **Goal:** Add cleanup function for BLEContext hardware notifications to prevent listener duplication.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster D).
  - **Plan:** 📎 [PLAN-memory-leak-hardware-notifications.md](docs/plans/PLAN-memory-leak-hardware-notifications.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-17)

- [x] **fix/memory-leak-scanner-timers**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [CORE] [L-RISK] [Snack] [🤖 PRO-MED] [BATCH:memory-leak-sweep]
  - **Goal:** Clear timeout refs on unmount to prevent setting state on unmounted scanner hooks.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster D).
  - **Plan:** 📎 [PLAN-memory-leak-scanner-timers.md](docs/plans/PLAN-memory-leak-scanner-timers.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-12, R-17)

- [x] **fix/telemetry-ledger-global-timer**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [TELEMETRY] [M-RISK] [Meal] [🤖 PRO-HIGH] [BATCH:memory-leak-sweep]
  - **Goal:** Refactor shared globalFlushTimer to use an instance counter pattern.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster D).
  - **Plan:** 📎 [PLAN-telemetry-ledger-global-timer.md](docs/plans/PLAN-telemetry-ledger-global-timer.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-17)

- [x] **fix/pii-logger-scrubber**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [SECURITY] [H-RISK] [Snack] [🤖 PRO-MED] [BATCH:pii-scrub-sweep]
  - **Goal:** Implement automatic PII/location redaction in AppLogger before telemetry dispatch.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster A). High severity.
  - **Plan:** 📎 [PLAN-pii-logger-scrubber.md](docs/plans/PLAN-pii-logger-scrubber.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-09)

- [x] **fix/ble-backoff-jitter**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [BLE] [M-RISK] [Snack] [🤖 PRO-MED] [BATCH:error-handling-sweep]
  - **Goal:** Add randomized jitter to BLE reconnection backoffs to prevent broadcast storm collisions.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster E).
  - **Plan:** 📎 [PLAN-ble-backoff-jitter.md](docs/plans/PLAN-ble-backoff-jitter.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-06)

- [x] **fix/ble-recovery-max-attempts**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [BLE] [M-RISK] [Snack] [🤖 PRO-MED] [BATCH:error-handling-sweep]
  - **Goal:** Implement max reconnection attempts logic to prevent infinite looping.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster E).
  - **Plan:** 📎 [PLAN-ble-recovery-max-attempts.md](docs/plans/PLAN-ble-recovery-max-attempts.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-06)

- [x] **fix/heartbeat-gatt-guard**
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [BLE] [M-RISK] [Meal] [🤖 PRO-HIGH] [BATCH:error-handling-sweep]
  - **Goal:** Prevent heartbeat dispatches from crashing the BLE Queue if GATT is unavailable.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster E).
  - **Plan:** 📎 [PLAN-heartbeat-gatt-guard.md](docs/plans/PLAN-heartbeat-gatt-guard.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-06)

- [x] **fix/promise-io-safety-critical** 🚀 Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [IO] [L-RISK] [Snack] [🤖 PRO-MED] [BATCH:promise-io-safety-sweep]
  - **Goal:** Fix 5 critical fire-and-forget Promises missing `.catch` blocks.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster C).
  - **Plan:** 📎 [PLAN-promise-io-safety-critical.md](docs/plans/PLAN-promise-io-safety-critical.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-11)

- [x] **fix/promise-io-safety-medium** 🚀 Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [IO] [L-RISK] [Meal] [🤖 PRO-MED] [BATCH:promise-io-safety-sweep]
  - **Goal:** Fix 7 medium severity fire-and-forget Promises in AsyncStorage writes.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster C).
  - **Plan:** 📎 [PLAN-promise-io-safety-medium.md](docs/plans/PLAN-promise-io-safety-medium.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-11)

- [x] **fix/type-laundering-sweep** 🚀 Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [TYPES] [M-RISK] [Feast] [🤖 PRO-MED] [BATCH:type-safety-sweep]
  - **Goal:** Eliminate 28 instances of `as unknown as` casting across the codebase.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster B).
  - **Plan:** 📎 [PLAN-type-laundering-sweep.md](docs/plans/PLAN-type-laundering-sweep.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-08)

- [x] **fix/ble-programmer-gatt-delays** 🚀 Merged in 882704065b83f7807014ba20d8d185b995627f87
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [BLE] [M-RISK] [Meal] [🤖 PRO-HIGH] [BATCH:hardcoded-delay-sweep]
  - **Goal:** Replace hardcoded static GATT settle delays with reactive patterns or configurable constants.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster G).
  - **Plan:** 📎 [PLAN-ble-programmer-gatt-delays.md](docs/plans/PLAN-ble-programmer-gatt-delays.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-16)

- [x] **fix/auth-context-bypass** 🚀 Merged in 2213d4cc8db8c6b1ae8b21ccef4f23a1b738f83e
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [SECURITY] [H-RISK] [Meal] [🤖 PRO-HIGH] [BATCH:auth-context-sweep]
  - **Goal:** Harden global navigation guards against AsyncStorage tampering for isOfflineMode bypass.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster K). Critical security gap.
  - **Plan:** 📎 [PLAN-auth-context-bypass.md](docs/plans/PLAN-auth-context-bypass.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-15)

- [x] **fix/device-settings-probe-fsm** 🚀 Merged in 2213d4cc8db8c6b1ae8b21ccef4f23a1b738f83e
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [UI] [M-RISK] [Meal] [🤖 PRO-MED] [BATCH:boolean-fsm-sweep]
  - **Goal:** Replace binary isProbing flag with granular ProbeStatus union type in DeviceSettingsModal.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster I).
  - **Plan:** 📎 [PLAN-device-settings-probe-fsm.md](docs/plans/PLAN-device-settings-probe-fsm.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-18)

- [x] **fix/hal-enclosure-oracle-tab** 🚀 Merged in 2213d4cc8db8c6b1ae8b21ccef4f23a1b738f83e
  - **Tags:** [⚪ READY] [✅ VERIFIED] [BLE] [L-RISK] [Snack] [CHORE] [BATCH:hal-enclosure-sweep]
  - **Plan:** 📎 [PLAN-hal-enclosure-oracle-tab.md](docs/plans/PLAN-hal-enclosure-oracle-tab.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (Cluster H, R-19)
  - **Goal:** Extract raw BLE byte arrays from DiagnosticLabOracleTab.tsx into ZenggeProtocol.ts methods. Exclude the 0x73 hypothesis test payloads.
  - **Rationale for Demotion (2026-06-08):** Oracle Lab functions independently. However, 2026-06-10 deepdive-code-synthesis flagged this as a HAL Enclosure violation (R-19) for the 0x58/0x57/0x56 scene blocks. Plan updated and task formalized.

- [x] **fix/ui-state-matrix** 🚀 Merged in 83feb803e4511fad99933de527feee45d384a3b9
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [UI] [M-RISK] [Feast] [🤖 PRO-HIGH] [BATCH:state-matrix-sweep]
  - **Goal:** Implement missing Error and Empty states across 9 specific UI screens.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster J).
  - **Plan:** 📎 [PLAN-ui-state-matrix.md](docs/plans/PLAN-ui-state-matrix.md)
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-14)

- [x] **feat/observatory-schema** 🚀 Merged in 83feb803e4511fad99933de527feee45d384a3b9
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Establish the TypeScript UnifiedErrorRecord schema and BaseCollector structure.
  - **Decision Log:** Foundation required to normalize 12 disparate error sources into a single queryable pipeline.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-a1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "A unified schema is required before any deduplication or routing can occur."
    Rejected alternative: "Storing raw payloads — rejected because it makes exact-match dedup impossible."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §4 Unified Error Schema
  - **Details:** Must support fingerprints, breadcrumbs, cross-reference matches, and urgency scores.

- [x] **feat/observatory-local-collectors** 🚀 Merged in 83feb803e4511fad99933de527feee45d384a3b9
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build local file-reading and grep-scanning collectors (Sources 7-12) to parse known issues, logs, and AST output.
  - **Decision Log:** We need to capture institutional memory (KNOWN_ISSUES, SESSION_LOG) and static analysis signals to detect regressions.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-a2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Local grep and file parsing provide zero-dependency signals for regression detection."
    Rejected alternative: "N/A"
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Sources 7-12)
  - **Details:** Zero external dependencies. Wraps `grep -rn "console.error" src/` avoiding `__DEV__` lines.

- [x] **feat/observatory-build-collectors** 🚀 Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build the build-time collectors for Jest, TSC, and Web Console errors (Sources 3-5).
  - **Decision Log:** Unit test failures and TSC compilation errors must be auto-triaged just like production errors.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-a3](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Build pipeline errors provide high-confidence signals with exact file:line references."
    Rejected alternative: "Manual test verification — rejected to enforce auto-triage of regressions."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Sources 3-5)
  - **Details:** Must parse JSON outputs from Jest and map TSC stdout to the Unified schema.

- [x] **feat/observatory-report-generator** 🚀 Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Generate the daily markdown triage report for user review.
  - **Decision Log:** Tasks require human approval. The report is the presentation layer for the user's "Ship it" gate.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-d2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Report must sort by max urgency score to focus attention on CRITICAL and HIGH items."
    Rejected alternative: "N/A"
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §8
  - **Details:** Writes to `tools/observatory/reports/YYYY-MM-DD/report.md`.

- [x] **feat/observatory-workflow** 🚀 Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Write the `/self-heal` markdown workflow and deprecate `/audit-codebase`.
  - **Decision Log:** This brings the system to life, tying River, Blake, Reyes, Jordan, and Alex into a unified 5-phase execution map.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-e1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "A dedicated workflow file ensures consistent execution and persona handoffs."
    Rejected alternative: "Keeping audit-codebase — rejected per user decision C to consolidate."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §8, §10.4
  - **Details:** Must include the Step 0 institutional memory check. Deprecates `audit-codebase.md`.

- [x] **feat/observatory-auto-heal-library** 🚀 Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Implement the AH-001 through AH-009 auto-heal pattern library.
  - **Decision Log:** We must automate fixes for known, low-risk, repetitive errors to free up human cycles.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-e2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Auto-heals must NEVER apply to BLE or CORE domains."
    Rejected alternative: "Auto-committing patches — rejected; user must approve all proposals."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §7
  - **Details:** Flags tasks with `[🔧 AUTO-HEAL PROPOSED]`.

- [x] **feat/observatory-tests** 🚀 Merged in 22e1907d01b97e4c507a92cad74c208228ccf665
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Write Jest unit tests for the collection, dedup, scoring, and task generation engines.
  - **Decision Log:** The observatory is critical infrastructure and must be protected by the `npm run verify` pipeline.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-f1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Test coverage ensures dedup and scoring logic remains stable."
    Rejected alternative: "N/A"
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §11
  - **Details:** Add at least 3 test suites (`dedup.test.js`, `scoring.test.js`, `task_generator.test.js`).

- [x] **fix/auth-context-fsm** 🚀 Merged in 461e16d6f591884c9e0f3ed222f340600c4f704c
  - **Tags:** [⚪ TRIAGE] [✅ VERIFIED] [CORE] [H-RISK] [Feast] [🤖 PRO-HIGH] [BATCH:boolean-fsm-sweep]
  - **Goal:** Refactor AuthContext from 3 overlapping boolean flags into a strict Finite State Machine.
  - **Decision Log (2026-06-10):** Generated by deepdive-code-synthesis (Cluster I). Tagged Feast and H-RISK. Sent to Roadmap to prevent disrupting current sprint.
  - **Plan:** TBD
  - **Source of Truth:** 📖 artifacts/system_audit_report.md (R-18)

- [x] **feat/observatory-remote-collectors**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CLOUD]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build collectors to fetch Supabase `telemetry_errors` and MMKV-backed `crash_telemetry` (Sources 1-2).
  - **Decision Log:** We opted for a beefier homegrown CrashReporter (Option D) and need to collect those live production crashes.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-b1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "MMKV persistence survives native crashes; we must pull these breadcrumbs into the observatory."
    Rejected alternative: "Sentry/Crashlytics SDKs — rejected to adhere to the Anti-Bloat Protocol."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Sources 1-2)
  - **Details:** Extends existing `tools/sync_remote_errors.mjs`.

- [x] **feat/observatory-device-collector**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Build an ADB logcat parser to detect native device crashes (Source 6).
  - **Decision Log:** React Native JS boundaries cannot catch ANR/OOMs; ADB logcat is the only visibility into native deaths.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-b2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "ADB parsing provides a zero-dependency safety net for native crashes."
    Rejected alternative: "N/A"
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §3 (Source 6)
  - **Details:** Gracefully outputs 0 findings if no ADB device is connected.

- [x] **feat/observatory-dedup-engine**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Implement the 4-pass deduplication pipeline and the composite urgency scoring algorithm (0-100).
  - **Decision Log:** 12 sources will create massive noise and duplicate reports. A robust 4-pass dedup is required to cluster root causes.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-c1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Fuzzy matching and root cause clustering are critical for reducing noise."
    Rejected alternative: "Simple exact match — rejected because the same error can trigger slightly different line numbers."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §5.1, §5.2
  - **Details:** Must support Exact Match, Fuzzy Match, Root Cause, and False Positive Scrubbing.

- [x] **feat/observatory-crossref-engine**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Cross-reference deduplicated errors against KNOWN_ISSUES and FRICTION_LEDGER for regression detection.
  - **Decision Log:** We must never re-derive known fixes or fail to identify regressions of previously resolved patterns.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-c2](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Checking errors against institutional memory creates the self-reinforcing knowledge loop."
    Rejected alternative: "N/A"
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §5.3, §5.4
  - **Details:** Increases urgency by 30 if an error fingerprint matches a resolved VS-XXX pattern.

- [x] **feat/observatory-task-generator**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[TELEMETRY]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🤖 MODEL]` `[BATCH:observatory-pipeline]`
  - **Goal:** Generate strict kanban task entries with automated tags, goals, and Decision Logs from error clusters.
  - **Decision Log:** The action layer must seamlessly integrate into Casey's kanban flow without breaking formatting rules.
  - **Analysis:** 📊 Source: [self_healing_audit_system.md](../../C:/Users/Magma/.gemini/antigravity/brain/28213fdb-ce7c-4291-859e-45b22a1df8e4/self_healing_audit_system.md) · Plan: [PLAN-observatory-pipeline.md#task-d1](docs/plans/PLAN-observatory-pipeline.md)
    Key finding: "Auto-generated tasks must follow strict multi-line schema to avoid breaking parsers."
    Rejected alternative: "Direct Bucket List insertion — rejected; tasks must be presented for user review first."
  - **Source of Truth:** 📖 `self_healing_audit_system.md` §6.1, §6.2
  - **Details:** Output string must strictly match SK8Lytz Kanban Schema.

- [x] **chore/exception-masking-sweep** 🚀 Merged in 559dcaaf
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[CORE]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 PRO-MED]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix 17 empty catch blocks and `String(e)` misuses across 12 files. Protocol files (`ZenggeProtocol.ts`, `BanlanxAdapter.ts`) are silently swallowing errors that should be logged. Apply canonical `e instanceof Error ? e.message : String(e)` pattern throughout.
  - **Decision Log (2026-06-10):** Fleet found 3 silent catches in `ZenggeProtocol.ts:18,192,393` — protocol-level errors are swallowed with zero visibility. `BanlanxAdapter.ts:95` same pattern. `SessionContext.tsx:366,399` uses raw `String(err)` instead of proper unwrapping.
  - **Analysis:** 📊 Source: `artifacts/system_audit_report.md` · CLUSTER-04 (17 findings, R-06)
    Key finding: "Protocol-level silent catches are the highest priority — they hide BLE command failures with zero observability."
    Rejected alternative: "Bare re-throw — rejected, crashes the caller instead of gracefully logging."
  - **Plan:** 📎 [PLAN-exception-masking-sweep.md](docs/plans/PLAN-exception-masking-sweep.md)
  - **Source of Truth:** 📖 `src/protocols/ZenggeProtocol.ts:18,192,393` · `src/protocols/BanlanxAdapter.ts:95` · `src/context/SessionContext.tsx:366,399`

- [x] **chore/promise-safety-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[CORE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🤖 PRO-HIGH]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Add try/catch and `.catch()` handlers to 23 async operations across 13 files — including the hardware wizard flow, session context notification setup, AdminTelemetry export, and all AsyncStorage fire-and-forget calls.
  - **Decision Log (2026-06-10):** Fleet confirmed `HardwareSetupWizardScreen.tsx:64,601` has async flows with no catch — any BLE rejection silently crashes the wizard. `setupNotification()` is called fire-and-forget with internal awaits. `PushTokenService` makes two Supabase writes with zero error handling.
  - **Analysis:** 📊 Source: `artifacts/system_audit_report.md` · CLUSTER-02 (23 findings, R-11)
    Key finding: "8 HIGH severity unhandled rejections in wizard, session, and telemetry flows — crash risk on BLE/network failure."
    Rejected alternative: "Global unhandledRejection handler — rejected, masks root causes and violates surgical principle."
  - **Plan:** 📎 [PLAN-promise-safety-sweep.md](docs/plans/PLAN-promise-safety-sweep.md)
  - **Source of Truth:** 📖 `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:64,601` · `src/context/SessionContext.tsx:240` · `src/hooks/useAdminTelemetry.ts:49,55` · `src/services/PushTokenService.ts:22,36`

- [x] **chore/fsm-boolean-trap-sweep** — ✅ Merged @ bd3a0435. Refactored 18 target files to use strict status string unions ('idle' | 'loading' | 'success' | 'error'), eliminating impossible race states. Deferred Hook Zones in DashboardScreen/DockedController to avoid collateral damage per surgical rules.
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[UI]` `[⚠️ M-RISK]` `[🍱 Meal]` `[🤖 PRO-HIGH]` `[BATCH:deepdive-sweep]` `[WAVE:3]`
  - **Goal:** Replace scattered boolean flag triplets (`isLoading + isError + isSuccess`) in 18 files with single FSM string union state (`'idle' | 'loading' | 'error' | 'success'`).
  - **Decision Log (2026-06-10):** Fleet found 18 files with boolean traps including root screens (`DashboardScreen.tsx:178`) and critical flows (`HardwareSetupWizardScreen.tsx:53-55`, `AuthFormSignUp.tsx:37`). Scattered booleans allow `isLoading && isSuccess` to be simultaneously true — a logically impossible race state.
  - **Analysis:** 📊 Source: `artifacts/system_audit_report.md` · CLUSTER-03 (18 findings, R-18)
    Key finding: "DashboardScreen, DockedController, and the Hardware Wizard all have boolean traps — the three highest-traffic screens."
    Rejected alternative: "useReducer for every component — rejected as over-engineering for simple 3-state flows."
  - **Plan:** 📎 [PLAN-fsm-boolean-trap-sweep.md](docs/plans/PLAN-fsm-boolean-trap-sweep.md)
  - **Source of Truth:** 📖 `src/screens/DashboardScreen.tsx:178` · `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:53-55` · `src/components/auth/AuthFormSignUp.tsx:37` · `artifacts/system_audit_report.md CLUSTER-03`

- [x] **chore/state-matrix-sweep** 🚀 Merged in 0374dc4c
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[UI]` `[⚠️ M-RISK]` `[🍱 Meal]` `[🤖 PRO-MED]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Expose missing `isLoading`/`error` states from hooks (R-14). Audit and normalize all AsyncStorage keys to consistent namespace (R-24). Replace hardcoded `setTimeout` in BLE + splash flows with proper lifecycle events (R-16).
  - **Decision Log (2026-06-10):** Fleet confirmed `useCuratedPicks.ts:112` catches Supabase errors and logs them but never exposes `error` state to consumers — the UI renders stale data silently. `FavoritesPanel` declares `picksLoading` prop but ignores it. `DashboardScreen.tsx:197` uses `setTimeout` where `InteractionManager` should be used.
  - **Analysis:** 📊 Source: `artifacts/system_audit_report.md` · CLUSTER-08 (21 findings, R-14+R-24+R-16)
    Key finding: "`useCuratedPicks` hiding errors means admins never see failed spot fetches — offline-first promise broken."
    Rejected alternative: "Global error boundary only — rejected, doesn't expose the error to the specific UI widget that needs it."
  - **Plan:** 📎 [PLAN-state-matrix-sweep.md](docs/plans/PLAN-state-matrix-sweep.md)
  - **Source of Truth:** 📖 `src/hooks/useCuratedPicks.ts:112` · `src/components/docked/FavoritesPanel.tsx:100` · `src/screens/DashboardScreen.tsx:197` · `src/constants/storageKeys.ts`

- [x] **chore/misc-guardrail-sweep** 🚀 Merged in 2a682c5b
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[CORE]` `[⚠️ M-RISK]` `[🍱 Meal]` `[🤖 PRO-MED]` `[BATCH:deepdive-sweep]` `[WAVE:5]`
  - **Goal:** Fix 27 findings across 8 minor guardrails: PII MAC leaks (R-09), Android BT permission flag (R-20), memory leaks (R-22), re-entrancy races (R-26), context depth (R-27), event listener leak (R-17), stale closure (R-12), split-brain duplication (R-21).
  - **Decision Log (2026-06-10):** Highest urgency within this cluster is `AndroidManifest.xml:8` — `BLUETOOTH_SCAN` missing `neverForLocation` forces Android 12+ users to grant Location permission just to find their skates. Second priority: MAC PII leak in `DeviceRepository.ts:793`. These two are 1-line fixes with outsized impact.
  - **Analysis:** 📊 Source: `artifacts/system_audit_report.md` · CLUSTER-09 (27 findings)
    Key finding: "Missing `neverForLocation` on BLUETOOTH_SCAN is a UX regression — users will see an unexpected Location permission prompt on fresh installs."
    Rejected alternative: "Accept the Location permission — rejected, violates user trust and App Store privacy guidelines."
  - **Plan:** 📎 [PLAN-misc-guardrail-sweep.md](docs/plans/PLAN-misc-guardrail-sweep.md)
  - **Source of Truth:** 📖 `android/app/src/main/AndroidManifest.xml:8` · `src/services/DeviceRepository.ts:793` · `src/hooks/useDashboardCrew.ts:70` · `artifacts/system_audit_report.md CLUSTER-09`

- [x] **chore/type-safety-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[CORE]` `[⚠️ M-RISK]` `[🥩 Feast]` `[🤖 PRO-HIGH]` `[BATCH:deepdive-sweep]` `[WAVE:6]`
  - **Goal:** Eliminate 48 `any` casts and `as unknown as` type laundering patterns across 36 files. Highest concentration in hooks, dashboard components, admin tools, and crew screens.
  - **Decision Log (2026-06-10):** Fleet flagged 30 HIGH severity `any` usages. `useHardwareNotifications.ts` alone has 4. `supabaseClient.ts:78` uses `as unknown as` to bypass the mock type — must implement the interface properly. `CrewCreateScreen.tsx:122` uses `(s: any)` on a spot filter with a known `SkateSpot` type available.
  - **Analysis:** 📊 Source: `artifacts/system_audit_report.md` · CLUSTER-01 (48 findings, R-08)
    Key finding: "36 files affected — this is the widest surface area finding in the entire audit."
    Rejected alternative: "Suppress with @ts-ignore — strictly forbidden per No `any` Cast Law in agent-behavior.md."
  - **Plan:** 📎 [PLAN-type-safety-sweep.md](docs/plans/PLAN-type-safety-sweep.md)
  - **Source of Truth:** 📖 `src/types/supabase.ts` · `src/hooks/useHardwareNotifications.ts` · `src/components/crew/CrewCreateScreen.tsx:122` · `artifacts/system_audit_report.md CLUSTER-01`

- [x] **chore/anti-bloatprotocolr-21-sweep** (merged: c750de0f)
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all Anti-Bloat Protocol / R-21 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating Anti-Bloat Protocol / R-21. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-anti-bloatprotocolr-21-sweep.md](docs/plans/PLAN-anti-bloatprotocolr-21-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule Anti-Bloat Protocol / R-21
  - **Prerequisite:** None

- [x] **chore/r-17-sweep** (merged: 06ed1ab7)
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-17 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-17. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-17-sweep.md](docs/plans/PLAN-r-17-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-17
  - **Prerequisite:** None

- [x] **chore/r-04-sweep** (merged: c39f38f5)
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-04 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-04. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-04-sweep.md](docs/plans/PLAN-r-04-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-04
  - **Prerequisite:** None

- [x] **chore/r-10-sweep** (merged: 61315662)
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-10 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-10. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-10-sweep.md](docs/plans/PLAN-r-10-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-10
  - **Prerequisite:** None

- [x] **chore/anti-bloatprotocol-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all Anti-Bloat Protocol violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating Anti-Bloat Protocol. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-anti-bloatprotocol-sweep.md](docs/plans/PLAN-anti-bloatprotocol-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule Anti-Bloat Protocol
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-03-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-03 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-03. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-03-sweep.md](docs/plans/PLAN-r-03-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-03
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-05-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-05 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-05. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-05-sweep.md](docs/plans/PLAN-r-05-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-05
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-08-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-08 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-08. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-08-sweep.md](docs/plans/PLAN-r-08-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-08
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-22-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-22 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-22. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-22-sweep.md](docs/plans/PLAN-r-22-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-22
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/unknown-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all Unknown violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating Unknown. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-unknown-sweep.md](docs/plans/PLAN-unknown-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule Unknown
  - **Prerequisite:** Wave 1 fully merged

- [x] **chore/r-18-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:3]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-18 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-18. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-18-sweep.md](docs/plans/PLAN-r-18-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-18
  - **Prerequisite:** Wave 2 fully merged

- [x] **chore/r-13-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:3]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-13 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-13. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-13-sweep.md](docs/plans/PLAN-r-13-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-13
  - **Prerequisite:** Wave 2 fully merged

- [x] **chore/r-23-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:4]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-23 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-23. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-23-sweep.md](docs/plans/PLAN-r-23-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-23
  - **Prerequisite:** Wave 3 fully merged

- [x] **chore/r-21-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:4]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-21 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-21. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-21-sweep.md](docs/plans/PLAN-r-21-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-21
  - **Prerequisite:** Wave 3 fully merged

- [x] **chore/r-26-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:5]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-26 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-26. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-26-sweep.md](docs/plans/PLAN-r-26-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-26
  - **Prerequisite:** Wave 4 fully merged

- [x] **chore/r-09-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:5]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-09 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-09. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-09-sweep.md](docs/plans/PLAN-r-09-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-09
  - **Prerequisite:** Wave 4 fully merged

- [x] **chore/r-24-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-24 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-24. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-24-sweep.md](docs/plans/PLAN-r-24-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-24
  - **Prerequisite:** Wave 5 fully merged

- [x] **chore/r-06-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[H-RISK]` `[Meal]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-06 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-06. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-06-sweep.md](docs/plans/PLAN-r-06-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-06
  - **Prerequisite:** Wave 5 fully merged

- [x] **chore/r-14-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:6]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-14 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-14. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-14-sweep.md](docs/plans/PLAN-r-14-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-14
  - **Prerequisite:** Wave 5 fully merged

- [x] **chore/r-15-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:7]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-15 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-15. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-15-sweep.md](docs/plans/PLAN-r-15-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-15
  - **Prerequisite:** Wave 6 fully merged

- [x] **chore/r-07-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Meal]` `[WAVE:7]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-07 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-07. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-07-sweep.md](docs/plans/PLAN-r-07-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-07
  - **Prerequisite:** Wave 6 fully merged

- [x] **chore/r-11-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:8]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-11 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-11. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-11-sweep.md](docs/plans/PLAN-r-11-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-11
  - **Prerequisite:** Wave 7 fully merged

- [x] **chore/r-27-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:8]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-27 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-27. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-27-sweep.md](docs/plans/PLAN-r-27-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-27
  - **Prerequisite:** Wave 7 fully merged

- [x] **chore/r-20-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Snack]` `[WAVE:9]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-20 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-20. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-20-sweep.md](docs/plans/PLAN-r-20-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-20
  - **Prerequisite:** Wave 8 fully merged

- [x] **chore/r-16-sweep**
  - **Tags:** `[⚪ TRIAGE]` `[✅ VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Feast]` `[WAVE:10]` `[BATCH:deepdive-sweep-phase2]`
  - **Goal:** Resolve all R-16 violations across the identified files.
  - **Decision Log (2026-06-10):** Fleet synthesis identified these files as violating R-16. Grouped by collision graph into parallel-safe batches.
  - **Plan:** 📎 [PLAN-r-16-sweep.md](docs/plans/PLAN-r-16-sweep.md)
  - **Source of Truth:** 📖 `artifacts/system_audit_report.md` · Rule R-16
  - **Prerequisite:** Wave 9 fully merged

- [x] **`feat/deepdive-docs-holistic-sync`** 🚀 Merged in 64e6826d
  - **Tags:** `[✅ READY]` `[TOOLING]` `[L-RISK]` `[Snack]` `[🤖 PRO-MED]`
  - **Goal:** Enhance /deepdive-docs to automatically maintain high-level non-developer documentation.
  - **Decision Log:** High-level diagrams and user journeys will slowly drift out of date if they aren't synced dynamically alongside the Master Reference when code changes.
  - **Analysis:** 📊 Plan: [PLAN-deepdive-docs-holistic-sync.md](../../docs/plans/PLAN-deepdive-docs-holistic-sync.md)
    Key finding: "The Cartographers must flag changes impacting User Journey flows, C4 Context boundaries, or Hardware state machines."
    Rejected alternative: "Manually patching the documentation was rejected in favor of automatic synthesis."
  - **Source of Truth:** 📖 [.agents/workflows/deepdive-docs.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-docs.md)
  - **Details:** Must not break non-destructive archival protocols.

- [x] **chore/sweep-services-core**
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:1]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 94 findings in domain services-core. Every file in the plan must appear in the diff.
  - **Source of Truth:** docs/plans/PLAN-sweep-services-core.md

- [x] **chore/sweep-components-admin** 🚀 Merged in d9585164
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 35 findings in domain components-admin. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-components-admin.md

- [x] **chore/sweep-components-auth** 🚀 Merged in 54b1cea5
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Meal]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 13 findings in domain components-auth. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-components-auth.md

- [x] **chore/sweep-services-ble** 🚀 Merged in 414097f8
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[H-RISK]` `[Meal]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 11 findings in domain services-ble. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-services-ble.md

- [x] **chore/sweep-supabase** 🚀 Merged in a561ee25
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Snack]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 3 findings in domain supabase. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-supabase.md

- [x] **chore/sweep-utils** 🚀 Merged in 7a827677
  - **Tags:** `[TRIAGE]` `[VERIFIED]` `[MAINTENANCE]` `[L-RISK]` `[Meal]` `[WAVE:2]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 8 findings in domain utils. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 1 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-utils.md

- [x] **chore/sweep-hooks-core** 🚀 Merged in b3d43808
  - **Tags:** `[READY]` `[VERIFIED]` `[MAINTENANCE]` `[M-RISK]` `[Feast]` `[WAVE:3]` `[BATCH:deepdive-sweep-phase3]`
  - **Goal:** Fix all 118 findings in domain hooks-core. Every file in the plan must appear in the diff.
  - **Prerequisite:** Wave 2 fully merged
  - **Source of Truth:** docs/plans/PLAN-sweep-hooks-core.md
  - **Decision Log:** Eradicate all core hook findings identified in the DeepDive audit (system_audit_report.md) to enforce type safety, error boundaries, and re-entrancy safety.

- [x] **chore/sweep-root** 🚀 Merged in 9bdeb129
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
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:session-xstate-engine]` `[WAVE:0]`
  - **Goal:** Confirm exact field name for session distance in WatchBridge payload so Wave 1 + Wear OS fix use the correct key.
  - **Decision Log:** `SessionCommitService.ts` and the Wear OS distance bug fix both read from a WatchBridge message field — field name must be verified before writing a single line of code that uses it.
  - **Analysis:** 📊 Source: [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) · Plan: [PLAN-spike-wear-os-bridge-field.md](./plans/PLAN-spike-wear-os-bridge-field.md)
    Key finding: "Wear OS DashboardScreen.kt shows hardcoded 0.0 distance — field name must be from WatchBridge payload type"
    Rejected alternative: "Assume field name is `distance` — rejected, assumption violates P1"
  - **Source of Truth:** 📖 [android/sk8lytzWear/](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/) + [targets/watch/](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/) + `sk8lytz-watch-bridge` package types
  - **Details:** Read-only spike. Output: one `[DECISION]` entry in SESSION_LOG with the confirmed field name. No code changes. `Prerequisite: none.`

- [x] **`feat/session-services-layer`** 🚀 Merged in b9c7baa9
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:session-xstate-engine]` `[WAVE:1]`
  - **Goal:** Create all 9 new files in `src/services/session/` and `src/components/session/` — the full XState session engine layer — without touching any existing file.
  - **Decision Log:** 10 confirmed session sync bugs (watch desync, auto-pause race, notification drift, stats wrong counter) all trace to one root cause: no single XState state authority for session lifecycle. Confirmed 2026-06-11 via full `SessionContext.tsx` + `useGlobalTelemetry.ts` + `useHealthTelemetry.ts` audit.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-feat-session-services-layer.md](./plans/PLAN-feat-session-services-layer.md)
    Key finding: "`BleMachine.ts` pattern (`setup({ actors, actions })` + `fromCallback` services + `fromPromise` commit) is already proven in this codebase — zero new patterns required"
    Rejected alternative: "Custom pub/sub event bus — rejected because XState v5 already installed and BleMachine proves pattern works"
  - **Source of Truth:** 📖 [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) + [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts) + [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts) + [SessionContext.tsx:285–359](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L285-L359)
  - **Details:** Creates: `SessionMachine.types.ts`, `SessionMachine.ts`, `SensorService.ts`, `AutoPauseService.ts`, `HealthService.ts`, `SessionCommitService.ts`, `NotificationService.ts`, `SessionBridge.ts`, `../components/session/SessionPhaseBadge.tsx`. Zero existing file modifications. Jest test stubs required. `Prerequisite: Wave 0 fully merged — distance field name confirmed in SESSION_LOG.`

- [x] **`refactor/session-context-xstate`** 🚀 Merged in 4df46b81
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 HIGH]` `[BATCH:session-xstate-engine]` `[WAVE:2]`
  - **Goal:** Rewrite `SessionContext.tsx` to be a thin `useMachine(sessionMachine)` wrapper with identical public `useSession()` API — zero changes to any consumer.
  - **Decision Log:** Current `SessionContext.tsx` is 474 lines of fragmented React state FSM + 6 chained useEffects. Every session phase transition requires synchronizing 4 independent systems manually. Wave 1 moves all logic into the machine — this wave wires it.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-refactor-session-context-xstate.md](./plans/PLAN-refactor-session-context-xstate.md)
    Key finding: "`useSession()` return shape at `SessionContext.tsx:463` — `{ isSkateSessionActive, sessionPhase, startSession, endSession, telemetry, health }` — must remain identical"
    Rejected alternative: "Change useSession() return shape — rejected because 14+ consumers would break"
  - **Source of Truth:** 📖 [SessionContext.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx) + [useBLE.ts:177](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L177)
  - **Details:** Single file rewrite. Keeps: `SessionContext` createContext, storage key constants, `persistSessionPhase`, `useSession()` export. Removes: all useState FSM, all manual useEffect chains. Adds: `useMachine`, `SessionBridge.register`, crash recovery via `STORAGE_PENDING_BG_END`, watch listeners via `SessionBridge`. `Prerequisite: Wave 1 fully merged into master before this worktree is created.`

- [x] **`refactor/delete-legacy-hooks`** 🚀 Merged in c8e30287
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:session-xstate-engine]` `[WAVE:3]`
  - **Goal:** Delete `useGlobalTelemetry.ts` + `useHealthTelemetry.ts` (now orphaned after Wave 2) and register the Notifee background event handler in app root.
  - **Decision Log:** AST confirmed both files have `imported_by: [SessionContext.tsx]` only. After Wave 2 removes those imports, both files are dead code. Background Notifee handler must be registered at app root (outside React tree) for notification action buttons to work when app is backgrounded.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-refactor-delete-legacy-hooks.md](./plans/PLAN-refactor-delete-legacy-hooks.md)
    Key finding: "AST output: `useGlobalTelemetry.ts imported_by: [SessionContext.tsx]` and `useHealthTelemetry.ts imported_by: [SessionContext.tsx]` — confirmed safe to delete after Wave 2"
    Rejected alternative: "Keep as deprecated stubs — rejected because dead code is risk and confusion"
  - **Source of Truth:** 📖 [useGlobalTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts) + [useHealthTelemetry.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHealthTelemetry.ts) + [SessionContext.tsx:441–448](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L441-L448)
  - **Details:** Parallel-safe with Wave 3B and 3C — no shared files. Files touched: 2 deletions + `App.tsx` (1 new block). Must grep for remaining imports before deleting. `Prerequisite: Wave 2 fully merged into master before this worktree is created.`

- [x] **`feat/session-phase-badge-ui`** 🚀 Merged in 481839b5
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]` `[BATCH:session-xstate-engine]` `[WAVE:3]`
  - **Goal:** Integrate `SessionPhaseBadge` into `DashboardTelemetryHero` and `LiveTelemetryHUD` so users see `● RECORDING` / `⏸ PAUSED` / `⏺ SAVING...` near the session timer.
  - **Decision Log:** User explicitly requested phase indicator below/near timers in session — `DashboardTelemetryHero` (HUD) and `LiveTelemetryHUD` (controller pill) are the two display surfaces confirmed by prop chain audit at `DockedController.tsx:1080–1084`. `SessionPhaseBadge` component created in Wave 1.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-feat-session-phase-badge-ui.md](./plans/PLAN-feat-session-phase-badge-ui.md)
    Key finding: "`sessionPhase` already destructured from `useSession()` in `DashboardScreen.tsx:486` — zero new data fetching required"
    Rejected alternative: "Add `useSession()` call inside DashboardTelemetryHero directly — rejected because it bypasses the existing prop contract and creates a second context read"
  - **Source of Truth:** 📖 [DashboardTelemetryHero.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx) + [DockedController.tsx:1080](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1080) + [DashboardScreen.tsx:486](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L486)
  - **Details:** Parallel-safe with Wave 3A and 3C. Files: `DashboardTelemetryHero.tsx` (badge below TIME pill), `LiveTelemetryHUD.tsx` (rightmost pill slot), `DockedController.tsx` (1 prop addition line only — S4 monolith flag active, surgical only). StreetPanel badge done in Wave 3C. `Prerequisite: Wave 2 fully merged into master before this worktree is created.`

- [x] **`fix/session-bug-fixes`** 🚀 Merged in 481839b5
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]` `[BATCH:session-xstate-engine]` `[WAVE:3]`
  - **Goal:** Fix 3 isolated bugs: StreetPanel dual-source-of-truth data + badge, AccountTabStats wrong sessions counter, Wear OS hardcoded 0.0 mi distance.
  - **Decision Log:** (1) `StreetPanel.tsx:80–81` reads `crewService.sessionTelemetry` instead of the drilled-in `sessionPeakSpeed`/`sessionDistanceMiles` props — confirmed by code read. (2) `AccountTabStats.tsx:49` uses `history?.length` (crew history count, not skate sessions) — confirmed source-of-truth mismatch. (3) Wear OS stop confirmation screen shows hardcoded `0.0 mi` — confirmed from prior audit.
  - **Analysis:** 📊 Source: [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) · Plan: [PLAN-fix-session-bug-fixes.md](./plans/PLAN-fix-session-bug-fixes.md)
    Key finding: "`DockedController.tsx:1249–1258` confirms `sessionPeakSpeed` and `sessionDistanceMiles` are already drilled to StreetPanel — the fix is replacing the wrong data source, not adding new props"
    Rejected alternative: "Fix via crewService changes — rejected because crewService is the wrong data source entirely"
  - **Source of Truth:** 📖 [StreetPanel.tsx:80–81](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/StreetPanel.tsx#L80-L81) + [AccountTabStats.tsx:49](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/AccountTabStats.tsx#L49) + [DockedController.tsx:1249](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1249)
  - **Details:** Parallel-safe with Wave 3A and 3B. Files: `StreetPanel.tsx` (2-line data fix + badge integration + 1 prop addition), `AccountTabStats.tsx` (1-line counter fix), `android/sk8lytzWear/` KT file (hardcoded distance fix — field name from Wave 0 SESSION_LOG entry). `Prerequisite: Wave 2 fully merged into master. Wave 0 SESSION_LOG entry must contain confirmed distance field name.`

- [x] **`docs/cartographer-rebuild-and-harden`**
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🥩 Feast]` `[🧠 HIGH]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Run the full 21-node cartographer fleet, update all Tier-3 satellite docs unconditionally, inject 3 missing ADRs, and harden 3 workflow files so Phase 4 can never be silently skipped again.
  - **Decision Log:** Post-Wave 1+2 audit confirmed: Master Reference missing 9 new session service files + rewritten SessionContext.tsx; Phase 4 of `/deepdive-docs` was skipped on `5aa3aa68` run because it is conditional on flags sub-agents optionally emit; 3 [DECISION] entries from 2026-06-11 never promoted to ADR; State_Charts_UX.md has no sessionMachine chart; User_Journey_Maps.md Journey 3 shows outdated linear watch flow.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-docs-cartographer-rebuild-and-harden.md](./plans/PLAN-docs-cartographer-rebuild-and-harden.md)
    Key finding: "`git log --oneline -3 -- tools/State_Charts_UX.md` shows last update `5aa3aa68` (yesterday) — Phase 4 ran but produced no changes because [IMPACTS_STATE_CHART] flag was conditional"
    Rejected alternative: "Delta sync of 4 domains only — rejected because Phase 4 was skipped entirely so all satellite docs need first real sync"
  - **Source of Truth:** 📖 [deepdive-docs.md:L121-L129](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/workflows/deepdive-docs.md#L121-L129) §Phase 4 + [SESSION_LOG.md:L40-L52](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SESSION_LOG.md#L40-L52) §3 missing ADRs
  - **Details:** Phase A = 21-node cartographer fleet → Phase 3 injection → Phase 4 unconditional satellite update → Phase 5 ADR sync. Phase B = 3 surgical workflow edits (deepdive-docs, start-task, wind-down). Zero TypeScript source file changes. Full rebuild justified: Domain 12 (SESSION_TRACKING) entirely different after Wave 2 rewrite.

- [x] **`docs/test-plan-session-machine`**
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Add a "Session Machine Test Coverage (XState v5)" section to `tools/SK8Lytz_TEST_PLAN.md` documenting the 28-suite / 218-test coverage added in Waves 1+2, including known gaps.
  - **Decision Log:** Wave 1 (`b9c7baa9`) + Wave 2 (`4df46b81`) merged a complete sessionMachine implementation with 10+ new test cases. `SK8Lytz_TEST_PLAN.md` (39KB) has no session machine section — last update was `a3973b94` (pre-XState). Without documenting what is covered and what is NOT covered (the gaps), future QA runs have no baseline to work from.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-docs-test-plan-session-machine.md](./plans/PLAN-docs-test-plan-session-machine.md)
    Key finding: "`git log --oneline -1 -- tools/SK8Lytz_TEST_PLAN.md` → `a3973b94` (pre-Wave 1) — test plan 2 merges behind"
    Rejected alternative: "Write new tests instead — rejected; user explicitly said `/intake` for test plan documentation, not new test authoring. Test authoring is a separate `/tdd` task."
  - **Source of Truth:** 📖 [SK8Lytz_TEST_PLAN.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_TEST_PLAN.md) + merge commits `b9c7baa9` (W1) + `4df46b81` (W2)
  - **Details:** Must read actual test files before documenting coverage — do not document from memory. Document both covered states/edges AND known gaps (network failure during COMMITTED, concurrent END triggers). Parallel-safe with all other batch tasks.

- [x] **`docs/xstate-v5-kb-capture`**
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Read `BleMachine.ts` + `sessionMachine.ts` live implementations and write a KB entry for the XState v5 patterns SK8Lytz actually uses, so future agents never re-derive them.
  - **Decision Log:** KB INDEX has no XState entry (confirmed by search — 2026-06-11). Two XState v5 state machines now run in the app (`BleMachine.ts` + `sessionMachine.ts` from Wave 1). Without a KB entry, every cold-start session agent re-derives patterns from documentation or guesswork. Rule 13 requires KB entry before asserting facts about external libraries.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-docs-xstate-v5-kb-capture.md](./plans/PLAN-docs-xstate-v5-kb-capture.md)
    Key finding: "`grep 'xstate' tools/knowledge-base/INDEX.md` → no results. Zero KB coverage for XState v5 despite two active state machines in production."
    Rejected alternative: "Full XState v5 API docs — rejected; scope is patterns-we-use only to keep KB focused (user confirmed Q1)"
  - **Source of Truth:** 📖 [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts) + [sessionMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/sessionMachine.ts) + [knowledge-base/INDEX.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/knowledge-base/INDEX.md)
  - **Details:** Scope = `createMachine`, `useMachine`, `assign`, guard syntax, `SessionBridge` actor pattern. Staleness window: 180 days. Feeds into Master Reference §3 State Machine Library. Parallel-safe with all other batch tasks.

- [x] **`fix/industry-benchmarks-dedup`**
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[☁️ CLOUD]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:doc-pipeline-sync]` `[WAVE:1]`
  - **Goal:** Remove the duplicate second copy of content in `tools/INDUSTRY_BENCHMARKS.md` — the full file content appears twice due to a prior double-write bug.
  - **Decision Log:** `view_file tools/INDUSTRY_BENCHMARKS.md` confirmed (2026-06-11): lines 1-49 identical to lines 50-99 verbatim. A double-write during a prior context-compiler run wrote the header + 3 benchmark entries twice. Lines 100-125 are unique (High-Density Grouping, Hardware Mocks, DB PII Encryption) and must be preserved.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) · Plan: [PLAN-fix-industry-benchmarks-dedup.md](./plans/PLAN-fix-industry-benchmarks-dedup.md)
    Key finding: "File is 125 lines but unique content is only ~75 lines — 50-line duplicate section from a prior double-write"
    Rejected alternative: "Leave as-is — rejected; duplication confuses agents and the second header block (`# Industry Benchmarks`) causes parsing errors in synthesis workflows"
  - **Source of Truth:** 📖 [INDUSTRY_BENCHMARKS.md:L1-L125](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/INDUSTRY_BENCHMARKS.md)
  - **Details:** 2-minute surgical fix. Read full file, identify exact duplicate range, remove second copy, commit. No content loss — only duplicate removal.

- [x] **`fix/session-machine-actor-types`** — *Merged to master @ 8f482d06*
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]` `[BATCH:session-xstate-hardening]` `[WAVE:1A]`
  - **Goal:** Type all 4 `fromCallback<any>` actor calls to `SessionMachineEvent`, remove 10s `syncWatchStopped` delay, fix ENDING notification buttons.
  - **Decision Log:** Post-merge audit 2026-06-11 found `fromCallback<any, ...>` on AutoPauseService L9, SensorService L18, HealthService L12, NotificationService L18 — suppresses type checking on sendBack events. syncWatchStopped 10s delay creates race if new session starts within window. ENDING phase shows contextually wrong action buttons.
  - **Analysis:** 📊 Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) · Plan: [PLAN-fix-session-machine-actor-types.md](./plans/PLAN-fix-session-machine-actor-types.md)
    Key finding: "4 actors emit untyped sendBack events; 1 machine action has a 10s race window; ENDING notification shows wrong buttons"
    Rejected alternative: "Add // @ts-ignore to suppress" — violates No any Cast Law (S3)
  - **Source of Truth:** 📖 [SessionMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SessionMachine.ts#L127-L131) `syncWatchStopped` · [AutoPauseService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/AutoPauseService.ts#L9) · [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts#L42-L71)
  - **Details:** All 4 actor files share `SessionMachine.ts` as their parent consumer — AST confirmed these must run in one worktree. `SessionMachineEvent` type exists in `SessionMachine.types.ts` and is the correct sendBack type. The ENDING fix adds an early-return branch before the isPaused branch in NotificationService.

- [x] **`fix/ble-connection-hang`** 🚀 Merged in de974879
  - **Tags:** `[✅ READY]` `[🧪 LAB]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]`
  - **Goal:** Fix the connection hang when opening the controller screen on already connected devices.
  - **Decision Log:** physical device gets stuck on blue screen when opening controller because connectedDevices([mac]) passes MAC instead of service UUIDs, causing connectToDevice to be called redundantly and hang.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/002ab10f-9595-41ac-89ac-a7516dd02366/implementation_plan.md) · Plan: [PLAN-fix-ble-connection-hang.md](./plans/PLAN-fix-ble-connection-hang.md)
    Key finding: "passing MAC to connectedDevices returns [] forcing redundant connectToDevice call which hangs BLE stack"
    Rejected alternative: "bypassing isDeviceConnected check — rejected because we need connection-state check to prevent redundant connection attempts"
  - **Source of Truth:** 📖 [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L149) §1
  - **Details:** Must run verify after changes.

- [x] **`fix/hardware-setup-identification`**
  - **Tags:** `[✅ READY]` `[✅ UNVERIFIED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:hardware-setup]`
  - **Goal:** Fix setup wizard color swap logic, blink button matching colors, and correctly persist left/right custom device names.
  - **Decision Log:** Setup wizard had flawed color swap assignment causing both skates to get same color on step 2, blink buttons defaulted to cyan instead of red/green, and custom names did not persist to DB.
  - **Analysis:** 📊 Plan: [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md)
    Key finding: "position assignment overrides itself if both names default to 'left' substring; `custom_name` missing from payload."
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** Wave assignment: [WAVE:1] — verified by AST analysis. No import overlap with active batches. Merged in c9c64b88 — Fixed left/right assignment bug and name logic.

- [x] **`feat/hardware-setup-brand-colors`**
  - **Tags:** `[✅ READY]` `[✅ UNVERIFIED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:hardware-setup]`
  - **Goal:** Update Hardware Setup Wizard to use Neogleamz brand colors (Blue and Orange) instead of generic Red and Green.
  - **Decision Log:** The user requested swapping the generic red/green identification colors to the official brand colors 1B4279 and F79320.
  - **Analysis:** 📊 Plan: [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md)
    Key finding: "Hardware identity flashing should match the brand aesthetic for a premium feel."
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** Wave assignment: [WAVE:1] — shares import tree with `fix/hardware-setup-identification`. Will execute in unified batch worktree. Merged in c9c64b88 — Applied brand colors to setup wizard.

- [x] **`fix/global-header-spacing`**
  - **Tags:** `[✅ READY]` `[✅ UNVERIFIED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 LOW]` `[BATCH:hardware-setup]`
  - **Goal:** Eliminate double padding blank space at the top of the app header.
  - **Decision Log:** SafeAreaProvider was added to App.tsx, but DashboardScreen retained SafeAreaView while DashboardHeader also used insetTop, causing double top padding.
  - **Analysis:** 📊 Plan: [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md)
    Key finding: "DashboardScreen nested SafeAreaView causes double padding."
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
  - **Details:** Wave assignment: [WAVE:1] — verified by AST analysis. Shares import tree with hardware setup batch. Merged in c9c64b88 — Removed SafeAreaView to fix double padding.

- [x] **`fix/app-safe-area-boundaries`** - Merged @ 1122bb39 (Migrated to react-native-safe-area-context)
  - **Tags:** `[✅ READY]` `[✅ UNVERIFIED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:safe-area-migration]` `[WAVE:1]`
  - **Goal:** Migrate core React Native SafeAreaView imports to react-native-safe-area-context.
  - **Decision Log:** SafeAreaView from react-native is deprecated and fails to apply notch/header padding on Android devices properly.
  - **Analysis:** 📊 Plan: [PLAN-fix-app-safe-area-boundaries.md](./plans/PLAN-fix-app-safe-area-boundaries.md)
    Key finding: "Core SafeAreaView does not respect Android notification bar insets correctly."
  - **Source of Truth:** 📖 [GlobalErrorBoundary.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/GlobalErrorBoundary.tsx#L2) · [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L11)
  - **Details:** Replaces the deprecated import in the two remaining files. Drops into existing UI with no layout shifts other than correct notch spacing.

- [x] **`fix/admin-modal-safe-areas`** [merge: efe231b2] 🚀 Merged in efe231b2
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 LOW]` `[BATCH:admin-modal-safe-areas]` `[WAVE:1]`
  - **Goal:** Migrate remaining legacy SafeAreaView imports in Admin/Account Modals to react-native-safe-area-context.
  - **Decision Log:** Codebase audit revealed 8 admin panels and EulaModal still use core SafeAreaView, and AccountModal uses an unpadded absolute `top: 16`.
  - **Analysis:** 📊 Source: N/A · Plan: [PLAN-fix-admin-modal-safe-areas.md](./plans/PLAN-fix-admin-modal-safe-areas.md)
    Key finding: "Legacy SafeAreaView allows notch bleeding on Android."
    Rejected alternative: "Ignore since they are admin tools" — breaks UX for power users and EulaModal affects all users.
  - **Source of Truth:** 📖 [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
  - **Details:** Simple import swap across 8 files and one dynamic padding change.

- [x] **`feat/harden-ble-regression-shields`** 🚀 Merged in fd3df999
  - **Tags:** `[✅ READY]` `[🧪 LAB]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]`
  - **Goal:** Build automated Jest regression tests and codify permanent rules to lock down setup wizard scanning and group connection state.
  - **Decision Log:** FTUE onboarding race conditions and setup wizard scanning deadlocks cost 12 hours of debugging across multiple sessions; codifying regression tests and strict rules prevents future AI/developer regressions.
  - **Analysis:** 📊 Source: [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L345) and [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L561) · Plan: [PLAN-harden-ble-regression-shields.md](./plans/PLAN-harden-ble-regression-shields.md)
    Key finding: Idempotent sweeper triggers and Next button state checks are critical to FTUE onboarding stability.
    Rejected alternative: Relying solely on textual instructions in session summaries, which inevitably drift or get ignored by future models.
  - **Source of Truth:** 📖 [useBLEScanner.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L345) L345-355 · [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx#L561) L561-571 · [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L282) L282
  - **Details:** Non-production changes only. Adds tests in `ConnectService.test.ts` and `HardwareSetupWizardScreen.test.tsx`, plus a new `useBLEScanner.test.ts`. Codifies rules in `21_GUARDRAILS.md` and `prime-directive.md`.

- [x] **`chore/sweep-cloud-supabase`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Harden 6 SECURITY DEFINER PostgreSQL functions with `SET search_path = ''` to eliminate SQL injection surface, fix email domain validation bypass, restrict scraper_blocklist RLS, and add error handling to the Deno edge function.
  - **Decision Log:** deepdive fleet confirmed 6 unguarded `SECURITY DEFINER` functions in Supabase migrations — without `SET search_path`, a caller can inject a malicious schema and execute arbitrary SQL under elevated privilege. The email LIKE `%@sk8lytz.com` pattern is bypassable with `x@sk8lytz.com.evil.com`.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-cloud-supabase.md](./plans/PLAN-sweep-cloud-supabase.md) — Key finding: "6 SECURITY DEFINER RPCs without SET search_path — SQL injection vectors (2 agents confirmed)" — Rejected: "App-layer validation only" — does not protect against DB-level schema injection
  - **Source of Truth:** [20260414_account_deletion_rpc.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260414_account_deletion_rpc.sql) + 5 additional migration files listed in PLAN


- [x] **`chore/sweep-devops-tooling`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[DEVOPS]` `[H-RISK]` `[Snack]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix gatekeeper rebase failure detection ($LASTEXITCODE), prevent regression healer from committing to master, fix auto-archiver slug regex collision, and replace raw `npx tsc`/`npx jest` in husky pre-commit hook.
  - **Decision Log:** Fleet confirmed `fortress-gatekeeper.ps1` does not check `$LASTEXITCODE` after `git rebase` — a failed rebase is silently ignored, leaving master in a corrupted merge state. `regression_healer.py` has no branch guard and can commit directly to master.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-devops-tooling.md](./plans/PLAN-sweep-devops-tooling.md) — Key finding: "Gatekeeper git rebase failure is unchecked; regression healer can commit to master" — Rejected: "Manual review step only" — silent failure mode requires a process guard
  - **Source of Truth:** [fortress-gatekeeper.ps1](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/fortress-gatekeeper.ps1#L93) · [regression_healer.py](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/sentinel/regression_healer.py#L188)


- [x] **`chore/sweep-protocol-core`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[BLE]` `[H-RISK]` `[Snack]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Resolve split-brain 0x40 chunking between ZenggeAdapter and BleWriteDispatcher, fix incorrect TransitionType mapping, and remove hardcoded 54-pixel max in streamPixelFrame.
  - **Decision Log:** 2 independent fleet agents confirmed ZenggeAdapter.prepareForTransmission and BleWriteDispatcher implement conflicting chunking logic — the controller receives double-chunked payloads, causing corrupted LED state. Per Protocol Bible: chunking belongs to the dispatcher only.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-protocol-core.md](./plans/PLAN-sweep-protocol-core.md) — Key finding: "Split-brain 0x40 chunking confirmed by 2 agents — double-chunked payloads corrupt LED state" — Rejected: "Move chunking to adapter" — Protocol Bible explicitly assigns chunking to the write dispatcher
  - **Source of Truth:** [ZenggeAdapter.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts#L260) · [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)


- [x] **`chore/sweep-ui-screens-dashboard`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[H-RISK]` `[Meal]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Eliminate `as any`/`any[]` props from 5 dashboard sub-components, fix Animated.Value memory leak in render cycle, fix Platform.OS ternary missing Web case, and fix power-toggle loop missing queue serialization.
  - **Decision Log:** Fleet found 6 dashboard components with untyped any props in the primary render path. Animated.Value instantiated in CrewHubSlab.tsx:181 render body accumulates instances on every render causing memory growth over long skating sessions.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-screens-dashboard.md](./plans/PLAN-sweep-ui-screens-dashboard.md) — Key finding: "Animated.Value memory leak in render; 5 components with any-typed props in Dashboard render path" — Rejected: "@ts-ignore suppression" — banned by The No any Cast Law
  - **Source of Truth:** [CrewHubSlab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/CrewHubSlab.tsx#L181) · [DashboardTelemetryHero.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx#L12)


- [x] **`chore/sweep-ui-visualizer-patterns`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Meal]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix floating unawaited promise in UnifiedPatternPicker, remove duplicate hexToRgb, fix web-only CSS props crashing on native in NeonHueStrip, and extract inline FlatList callbacks to stable useCallback refs.
  - **Decision Log:** Fleet confirmed writeToDeviceRef.current(payload) in UnifiedPatternPicker returns a Promise never caught — BLE write failures silently dropped. NeonHueStrip passes touchAction/userSelect to a native View, which crashes silently on iOS/Android.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-visualizer-patterns.md](./plans/PLAN-sweep-ui-visualizer-patterns.md) — Key finding: "Floating promise in UnifiedPatternPicker:62; NeonHueStrip web-only props crash on native" — Rejected: "Return void from writeToDevice" — masks errors; proper async error handling required
  - **Source of Truth:** [UnifiedPatternPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx#L62) · [NeonHueStrip.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/NeonHueStrip.tsx#L99)


- [x] **`chore/sweep-os-permissions-manifests`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[NATIVE]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Move hardcoded Google Maps API key from AndroidManifest.xml to build-time env var, fix HealthKit activity type mismatch, and trim excessive foregroundServiceType declaration.
  - **Decision Log:** Fleet flagged Google Maps API key hardcoded in AndroidManifest.xml:29 as plaintext — committed to git history and exposed to all repo contributors. Android 14+ strictly enforces foregroundServiceType matching; the over-broad declaration risks Play Store rejection.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-os-permissions-manifests.md](./plans/PLAN-sweep-os-permissions-manifests.md) — Key finding: "Google Maps API key hardcoded in AndroidManifest.xml — PII/secret leak committed to git" — Rejected: "Add to .gitignore only" — key already in history; must rotate + move to env var
  - **Source of Truth:** [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml#L29) · [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js#L15)


- [x] **`chore/sweep-native-watch`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[NATIVE]` `[M-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix @Published property mutation on WCSession background queue (iOS crash risk), fix non-atomic SharedPreferences write in Wear OS, and align exercise type to INLINE_SKATING.
  - **Decision Log:** Fleet confirmed WatchConnectivityManager.swift:105 mutates @Published properties from the WCSession background delegate queue — SwiftUI rendering on background threads causes crashes on iOS 17+. WearMessageSender.kt:85 non-atomic SharedPreferences write causes data corruption under concurrent health delivery.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-native-watch.md](./plans/PLAN-sweep-native-watch.md) — Key finding: "@Published modified on WCSession background queue — guaranteed crash on iOS 17+" — Rejected: "@MainActor attribute only" — WCSession delegates are not MainActor-isolated; must use explicit DispatchQueue.main.async
  - **Source of Truth:** [WatchConnectivityManager.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift#L105) · [WearMessageSender.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt#L85)


- [x] **`chore/sweep-admin-telemetry`** 🚀 Merged in aa782643
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Meal]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Extract all inline keyExtractor/renderItem callbacks in admin FlatLists to stable useCallback refs, add 4-state UI matrices to 3 admin panels, and fix AppLogger telemetry context structure in 2 files.
  - **Decision Log:** Fleet found inline arrow functions for keyExtractor in every admin panel FlatList — these defeat FlatList virtualization causing full re-renders on every state update. AdminRosterPanel, HardwareBlacklistPanel, and FeatureFlagsPanel show blank screens on fetch failure with no error feedback.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-admin-telemetry.md](./plans/PLAN-sweep-admin-telemetry.md) — Key finding: "6 admin FlatLists with inline keyExtractor defeat virtualization; 3 panels missing error/empty states" — Rejected: "Memoize entire list component" — stable callback refs are the targeted correct fix
  - **Source of Truth:** [AdminRosterPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminRosterPanel.tsx#L178) · [HardwareBlacklistPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/HardwareBlacklistPanel.tsx#L255)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-ble-core-dispatch`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[BLE]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Replace Promise.all concurrent BLE writes with serialized sequential dispatch, add re-entrancy guards to processQueue and battery sweep, and PII-scrub 9 raw MAC address leaks from telemetry logs.
  - **Decision Log:** 2 independent agents confirmed BleWriteDispatcher.ts:164 and :228 use Promise.all for concurrent characteristic writes — the Zengge controller has a single GATT characteristic; parallel writes cause GATT collisions and undefined controller state. Protocol Bible mandates strictly sequential delivery.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ble-core-dispatch.md](./plans/PLAN-sweep-ble-core-dispatch.md) — Key finding: "Promise.all at BleWriteDispatcher:164,:228 — concurrent GATT writes violate sequential write contract (2 agents confirmed)" — Rejected: "Retry on GATT collision" — serialization is the correct fix; retry masks root cause
  - **Source of Truth:** [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts#L164) · [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-storage-keys`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[STORAGE]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Fix 3 AsyncStorage key collision bugs, consolidate all hardcoded @Sk8lytz_* key literals into the central STORAGE_KEYS registry, and flip AppSettingsService to write local cache first (offline-first mandate).
  - **Decision Log:** Fleet confirmed useFavorites and QuickPresetModal resolve favorites keys independently with conflicting logic — reads/writes from different code paths silently overwrite each other. DashboardScreen hardcodes '@Sk8lytz_Favorites' bypassing the registry; if the key is ever renamed the Dashboard silently reads nothing.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-storage-keys.md](./plans/PLAN-sweep-storage-keys.md) — Key finding: "@Sk8lytz_Favorites hardcoded in DashboardScreen:648; AppSettingsService blocks local cache behind network — violates offline-first mandate" — Rejected: "Document keys" — doesn't prevent future renames from silently breaking Dashboard
  - **Source of Truth:** [useFavorites.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useFavorites.ts#L33) · [AppSettingsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppSettingsService.ts#L91)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-ui-modals-shared`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Snack]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Replace static color imports with useTheme() hook in 3 modal components, fix any-typed props in CommunityModal and MarqueeText, and break the circular dependency in account/types.ts.
  - **Decision Log:** Fleet confirmed DeviceSettingsModal and GroupSettingsModal import colors statically, bypassing useTheme() — these components are invisible to dark mode/theme switching. The account/types.ts circular import chain causes unpredictable module resolution order on hot reload.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-modals-shared.md](./plans/PLAN-sweep-ui-modals-shared.md) — Key finding: "DeviceSettingsModal and GroupSettingsModal ignore dark mode — static color import bypasses useTheme()" — Rejected: "Pass colors as props" — props threading for theme is an antipattern; hook consumption is correct
  - **Source of Truth:** [DeviceSettingsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceSettingsModal.tsx#L7) · [account/types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/types.ts)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.


- [x] **`chore/sweep-identity-auth`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[AUTH]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:3]`
  - **Goal:** Fix notif_preferences: any in core profile type, remove direct supabase.auth.getUser() from AppLogger, and fix 5 AppLogger telemetry context structure errors in auth layer.
  - **Decision Log:** Fleet confirmed ProfileService.types.ts:21 declares notif_preferences: any — this core type field poisons every component that consumes the profile type. AppLogger.ts:674 fires a live supabase.auth.getUser() network call on every log flush cycle, adding latency and failing silently when offline.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-identity-auth.md](./plans/PLAN-sweep-identity-auth.md) — Key finding: "notif_preferences: any in core type; AppLogger fires live auth network call on every log flush" — Rejected: "Cast to unknown instead of any" — still loses type information; proper interface required
  - **Source of Truth:** [ProfileService.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ProfileService.types.ts#L21) · [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts#L674)
  - **Details:** Prerequisite: Wave 2 fully merged into master before this worktree is created.


- [x] **`chore/sweep-group-sync`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[CLOUD]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Eliminate 4 as-any type casts in GroupRepository and CrewService DB access paths, fix stale closure in useCrewProximityRadar, add 4-state UI to Crew screens, and PII-scrub 3 raw user data leaks.
  - **Decision Log:** Fleet confirmed 4x as-any on GroupRepository + CrewService Supabase row access — bypasses shape validation on DB rows, causing runtime crashes when schema evolves. useCrewProximityRadar:131 captures crewService.isNearby as non-reactive, causing proximity radar to never update after initial mount.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-group-sync.md](./plans/PLAN-sweep-group-sync.md) — Key finding: "4x as-any on DB row access in GroupRepository/CrewService; stale closure in useCrewProximityRadar stops proximity radar after mount" — Rejected: "Runtime schema validation library" — overweight; Supabase-generated types already present in project
  - **Source of Truth:** [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts#L27) · [useCrewProximityRadar.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewProximityRadar.ts#L131)
  - **Details:** Prerequisite: Wave 3 fully merged into master before this worktree is created.


- [x] **`chore/sweep-session-context`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[SERVICES]` `[H-RISK]` `[Meal]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Add isFlushInProgress re-entrancy guards to 3 flushSyncQueue functions, add try/catch to 3 unawaited AsyncStorage calls in SessionContext, and register 6 undocumented storage keys into the registry.
  - **Decision Log:** Fleet confirmed SpeedTrackingService, ScenesService, and GradientsService all have the same re-entrancy bug — concurrent callers double-upload the queue then both clear it, silently deleting pending session data that one caller never successfully POSTed. This is an active data loss bug.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-session-context.md](./plans/PLAN-sweep-session-context.md) — Key finding: "3x flushSyncQueue with no re-entrancy guard — concurrent callers corrupt queue and silently delete pending session data (2 agents confirmed)" — Rejected: "Move flush to singleton scheduler" — boolean ref guard solves problem with zero new dependencies
  - **Source of Truth:** [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts#L243) · [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts#L258) · [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts#L161)
  - **Details:** Prerequisite: Wave 3 fully merged into master before this worktree is created.


- [x] **`chore/sweep-shared-utils`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[SERVICES]` `[M-RISK]` `[Snack]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:5]`
  - **Goal:** Deduplicate isValidEmail to a canonical src/utils/validation.ts, add accessibility props to CustomSlider and DeviceItem, fix PositionalGradientBuilder error handling, and add platform guard to LocationService.
  - **Decision Log:** Fleet found isValidEmail duplicated across 3+ auth forms — any future change requires updating all copies in sync. CustomSlider uses PanResponder with zero accessibility props — completely invisible to screen readers, violating App Store accessibility guidelines.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-shared-utils.md](./plans/PLAN-sweep-shared-utils.md) — Key finding: "isValidEmail duplicated 3+ times; CustomSlider has zero accessibility props — invisible to screen readers" — Rejected: "Shared comment" — comments don't prevent drift; canonical module import is correct
  - **Source of Truth:** [CustomSlider.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomSlider.tsx#L102) · [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts#L81)
  - **Details:** Prerequisite: Wave 4 fully merged into master before this worktree is created.


- [x] **`chore/sweep-ui-docked-controller`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:6]`
  - **Goal:** Wrap 5+ unawaited writeToDevice calls in UniversalSlidersFooter with async error handlers, fix stale closure in useStreetMode accelerometer listener, and stabilize MemoizedSk8lytzController dependencies.
  - **Decision Log:** Fleet confirmed writeToDevice called fire-and-forget in 5+ locations in UniversalSlidersFooter.tsx — BLE write failures silently swallowed with no user feedback. useStreetMode:188 captures deviceContext at listener registration — after device reconnect, the listener holds a stale reference and sends to the wrong device.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-docked-controller.md](./plans/PLAN-sweep-ui-docked-controller.md) — Key finding: "5+ unawaited writeToDevice in UniversalSlidersFooter — BLE write failures silently dropped; stale closure in useStreetMode sends to wrong device after reconnect" — Rejected: "Global unhandled promise rejection handler" — too broad; per-call-site async handling required
  - **Source of Truth:** [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx#L393) · [useStreetMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts#L188)
  - **Details:** Prerequisite: Wave 5 fully merged into master before this worktree is created.



## Ã¢Ââ€žÃ¯Â¸Â Icebox / Backburner (Manual Trigger Only)

### Ã°Å¸Å½Âµ Epic: Music Mode

- [ ] `feat/music-intel-phase-1` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 50k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã°Å¸Â§Â  THINK] [Spotify Sync] Ã¢â‚¬â€ OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Media Access] Ã¢â‚¬â€ Android MediaSession detection (YouTube, Pandora, etc.). Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [Ã°Å¸Â§Âª LAB] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Live Rink Mode] Ã¢â‚¬â€ ShazamKit/ACRCloud periodic background scanning (45s). Ã¢â€ â€™ [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Apple Music] Ã¢â‚¬â€ MusicKit integration for native iOS BPM. Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 45k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â§Â  THINK] [Crew Party Sync] Ã¢â‚¬â€ Master BPM Choreography Engine with Realtime crew sync. Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)

- [ ] `feat/google-oauth-integration` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 30k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã°Å¸Â§Â  THINK] Integrate Google OAuth as an auth provider. (Requires Google Cloud Console setup + Supabase config). Ã¢â€ â€™ [Plan](docs/plans/feat-google-oauth-integration.md)
- [ ] `feat/spatial-beat-mapping` : [Ã°Å¸Â§Âª LAB] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 18k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â§Â  THINK] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). Ã¢â€ â€™ [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Transform Dashboard into palette-synced dynamic backgrounds. Ã¢â€ â€™ [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [Ã°Å¸Â§Âª LAB] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 10k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Pattern selection (Strobe, Blink, Static) + music slider fix. Ã¢â€ â€™ [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/kinetic-brake-lights` : [Ã°Å¸Â§Âª LAB] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â§Â  THINK] [Pillar 12] Kinetic Safety Ã¢â‚¬â€ phone accelerometer pulse RED for braking. Ã¢â€ â€™ [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 30k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸Â§Â  THINK] Geofence-based 'Hive Mind' synchronization. Ã¢â€ â€™ [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 8k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 25k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 20k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â§Â  THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 12k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Card Swipe Navigation.



## Batch Strategy Tables Archive

### [BATCH:deepdive-sweep] — `deepdive-sweep-batch` — READY
> **Worktree Strategy**: Wave 1 (7 tasks fully parallel) -> Wave 2 (4 tasks fully parallel) -> Wave 3 (1 task) -> Wave 4 (2 tasks parallel) -> Wave 5 (1 task) -> Wave 6 (1 task)
> **Type**: Multi-wave parallel sweep — AST collision-verified
> **Prerequisite**: None — Wave 1 is parallel-safe with current master HEAD
> **Source Analysis**: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — 47-agent deepdive fleet; 456 verified findings across 16 domain clusters; 42 AST collision pairs detected

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


### Ã¢Å¡Â¡ [BATCH:doc-pipeline-sync] Ã¢â‚¬â€ `doc-pipeline-sync-batch` Ã¢â‚¬â€ READY
> **Worktree Strategy**: All 4 tasks fully parallel (tools/*.md files only Ã¢â‚¬â€ zero TypeScript overlap)
> **Type**: Parallel Ã¢â‚¬â€ all [WAVE:1]
> **Prerequisite**: None Ã¢â‚¬â€ AST confirmed zero import-tree overlap with session-xstate-engine Wave 3
> **Source Analysis**: Ã°Å¸â€œÅ  [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Ã¢â‚¬â€ Full doc ecosystem audit: 6 confirmed gaps after Wave 1+2 merges; Phase 4 of deepdive-docs was silently skipped; KB missing XState entry; 3 ADRs in SESSION_LOG never promoted; INDUSTRY_BENCHMARKS duplicated; TEST_PLAN pre-XState

#### Batch Strategy Table
| Wave | Task Slug | Files Touched | Prerequisite |
|---|---|---|---|
| 1 | `docs/cartographer-rebuild-and-harden` | `tools/SK8Lytz_App_Master_Reference.md` + Tier-3 satellite docs + 3 workflow files | None |
| 1 | `docs/xstate-v5-kb-capture` | `tools/knowledge-base/patterns/xstate-v5-patterns.md` + `tools/knowledge-base/INDEX.md` | None |
| 1 | `fix/industry-benchmarks-dedup` | `tools/INDUSTRY_BENCHMARKS.md` | None |
| 1 | `docs/test-plan-session-machine` | `tools/SK8Lytz_TEST_PLAN.md` | None |

### Ã¢Å¡Â¡ [BATCH:session-xstate-engine] Ã¢â‚¬â€ `session-xstate-engine-batch` Ã¢â‚¬â€ READY
> **Worktree Strategy**: Sequential waves (W0Ã¢â€ â€™W1Ã¢â€ â€™W2), then W3A+W3B+W3C fully parallel  
> **Type**: Mixed Ã¢â‚¬â€ Spike Ã¢â€ â€™ Sequential Ã¢â€ â€™ Parallel  
> **Prerequisite**: None Ã¢â‚¬â€ board is clear  
> **Source Analysis**: Ã°Å¸â€œÅ  [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) + [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Ã¢â‚¬â€ 10 confirmed session sync bugs traced to single root cause: no XState state authority for session lifecycle

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
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]`
  - **Goal:** Add substantive unit tests for SensorService, HealthService, and NotificationService — the 3 untested session actor services from the post-merge audit.
  - **Decision Log:** Post-merge audit 2026-06-11: 5 of 9 session service files have zero test coverage. SensorService is highest risk (GPS + accelerometer + crewService side effects). NotificationService ENDING-state button logic newly added in session-machine-actor-types task — needs test coverage for the 3-branch action logic.
  - **Analysis:** 📊 Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) · Plan: [PLAN-chore-session-service-test-coverage.md](./plans/PLAN-chore-session-service-test-coverage.md)
    Key finding: "44% test file coverage by file count; SensorService has GPS+accelerometer+crewService coupling and zero tests"
    Rejected alternative: "Skip until feature work" — test gaps in newly architected services compound quickly
  - **Source of Truth:** 📖 [AutoPauseService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/AutoPauseService.test.ts) reference pattern · [SensorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SensorService.ts) · [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts) · [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
  - **Details:** Must run AFTER `fix/session-machine-actor-types` merges — NotificationService test for ENDING branch depends on the ENDING fix being in place. 3 new test files, no production source changes.

## 🚑 TRIAGE QUEUE

> **Source Analysis**: 📊 [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md) — Unifies setup wizard logic fixes, brand color updates, and global header padding into a single surgical pass.

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 39 findings in sweep-root
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-root.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:22 M:8 L:9)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 10 findings in sweep-components-AccountModal.tsx
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-AccountModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:3 L:4)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 22 findings in sweep-components-admin
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-admin.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:7 M:7 L:8)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 7 findings in sweep-components-auth
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-auth.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:0 L:4)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-CommunityModal.tsx
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-CommunityModal.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:1 L:1)
  - [WAVE:1]

- [x] **[MAINTENANCE]** `[Status: [x]]` `[Verification: ✅ VERIFIED]` `[Layer: COMPONENT]` `[Risk: M-RISK]` `[Size: Meal]` `[Cognitive Load: HIGH]`
  - Task: Resolve 11 findings in sweep-components-crew
  - Source of Truth: `docs/plans/PLAN-sweep-components-crew.md`
  - Merge: 807f2235 - Extracted stats/forms, fixed double-taps, added error boundaries.
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:8 M:2 L:1)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 6 findings in sweep-components-dashboard
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-dashboard.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:2 L:2)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 18 findings in sweep-components-docked
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-docked.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:7 M:9 L:2)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-LocationPicker.tsx
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-LocationPicker.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:2 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-permissions
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-permissions.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:1 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 3 findings in sweep-components-SkateSpotBottomSheet.tsx
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-SkateSpotBottomSheet.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:2 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 2 findings in sweep-components-VisualizerUnit.tsx
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-components-VisualizerUnit.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:1 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-context-SessionContext.tsx
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-context-SessionContext.tsx.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:1 L:1)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 17 findings in sweep-hooks-ble
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [H-RISK] [Feast] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-ble.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:1 M:10 L:6)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 11 findings in sweep-hooks-useControllerDispatch.ts
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Meal] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useControllerDispatch.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:3 M:8 L:0)
  - [WAVE:1]

- [ ] [BATCH:deepdive-sweep-phase3]
  - Task: Resolve 5 findings in sweep-hooks-useCrewHub.ts
  - Tags: [✅ READY] [✅ AST-VERIFIED] [CORE] [M-RISK] [Snack] [HIGH]
  - Source of Truth: `docs/plans/PLAN-sweep-hooks-useCrewHub.ts.md`
  - Decision Log: `TBD`
  - Details: Wave 1 execution. None. (H:2 M:3 L:0)
  - [WAVE:1]