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