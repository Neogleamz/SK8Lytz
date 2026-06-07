### ⚡ [BATCH:deep-dive-remediation] — `deep-dive-remediation-batch` — 🟢 Completed

- [x] **`qa/r06-r08-type-and-error-safety`**
- [x] **`qa/r11-r12-r16-async-and-closures`**
- [x] **`qa/r20-os-variance-parity-and-config`**
- [x] **`qa/r09-pii-scrubbing-leaks`**

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