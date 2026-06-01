# SK8Lytz Master Bucket List Archive

This document contains the archive of all successfully completed and merged tasks, sprints, and epic batches within the SK8Lytz app ecosystem.

---

## 📦 ARCHIVED SPRINT LOGS

### Sprint: v3.7.3 — 2026-06-01 (camera-mode-white-only)

- [x] **`fix/camera-mode-white-only`** — Resolved white-only camera capture bug by utilizing useResizer from react-native-vision-camera-resizer to properly downscale frames on the GPU to a 50x50 RGB buffer. Merged `283774f7`.
  - **Tags:** `[✅ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[🤖 FLASH]`
  - **Plan:** 📎 [PLAN-camera-mode-white-only.md](./plans/PLAN-camera-mode-white-only.md)
  - **Source of Truth:** 📖 [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx#L96) §onFrame
  - **Goal:** Fix the camera frame processor crash that results in only capturing white colors.
  - **Details:** COMPLETE — Disposed GPUFrame inside finally block to prevent VRAM memory leaks. Set minimum channel offset of 3 to eliminate potential infinite loop hazards. Prioritized port 8081 for CDP console sniffer.

---

### Sprint: v3.6.3 — 2026-05-29 (grouping-architecture-overhaul)

### [BATCH:grouping-architecture-overhaul] (Active) — ⚡ Parallel-Safe
- **Prerequisite**: None
- **Active Tasks**: `feat/group-many-to-many`, `feat/group-mixed-state`, `fix/skatepark-hijack`, `fix/infinite-blob`, `fix/local-cloud-split-brain`

- [x] **`feat/group-many-to-many`** — Migrated device group membership to junction table. Merged `7e34ba7`.
  - **Tags:** `[✅ VERIFIED]` `[CORE]` `[H-RISK]` `[Feast]` `[🤖 THINK]`
  - **Plan:** 📎 [PLAN-group-many-to-many.md](./plans/PLAN-group-many-to-many.md)
  - **Source of Truth:** 📖 [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
  - **Goal:** Resolve The "Mutually Exclusive" Flaw (1-to-1 Mapping).
  - **Details:** COMPLETE — Refactored `DeviceSettings` and Supabase schema to support a many-to-many relationship using array-based group mapping.

- [x] **`feat/group-mixed-state`** — Added mixed state rendering and tap-to-unify UI. Merged `21636ec`.
  - **Tags:** `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[🤖 PRO-HIGH]`
  - **Plan:** 📎 [PLAN-group-mixed-state.md](./plans/PLAN-group-mixed-state.md)
  - **Source of Truth:** 📖 [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts)
  - **Goal:** Resolve Lack of "Mixed State" Aggregation.
  - **Details:** COMPLETE — Updated group UI cards to intelligently aggregate states using green/red status dots and introduced "Mixed - Tap to Sync" visual cue.

- [x] **`fix/skatepark-hijack`** — Added RSSI gating to background discovery. Merged `378366a7`.
  - **Tags:** `[✅ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[🤖 THINK]`
  - **Plan:** 📎 [PLAN-skatepark-hijack.md](./plans/PLAN-skatepark-hijack.md)
  - **Source of Truth:** 📖 [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L323)
  - **Goal:** Resolve The "Skatepark Hijack" (Proximity Blindness).
  - **Details:** COMPLETE — Introduced RSSI proximity gating and explicit user confirmation steps in `runAutoProvisioning` to prevent hijacking unregistered devices in public. Built an admin slider to adjust the threshold.

- [x] **`fix/infinite-blob`** — Implemented while loop to auto-increment group names and prevent appending. Merged `a48aaebe`.
  - **Tags:** `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Plan:** 📎 [PLAN-infinite-blob.md](./plans/PLAN-infinite-blob.md)
  - **Source of Truth:** 📖 [useDashboardGroups.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardGroups.ts#L363)
  - **Goal:** Resolve The "Infinite Blob" (Accidental Mega-Groups).
  - **Details:** COMPLETE — Replaced `findIndex` with an auto-incrementing `while` loop so that multiple pairs of the same product type are placed into "My SK8Lytz HALOZ", "My SK8Lytz HALOZ 2", etc.

- [x] **`fix/local-cloud-split-brain`** — Fixed the UUID lookup failure in `saveGroupTransactional` by queuing cloud sync if devices are pending. Merged `d9bf414`.
  - **Tags:** `[✅ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[🤖 THINK]`
  - **Plan:** 📎 [PLAN-local-cloud-split-brain.md](./plans/PLAN-local-cloud-split-brain.md)
  - **Source of Truth:** 📖 [DeviceRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts#L488)
  - **Goal:** Resolve The Local/Cloud Split-Brain (MAC vs DB ID).
  - **Details:** COMPLETE — Implemented `hasPendingDevices` guard to bypass the RPC and delegate group mapping to `_queuePendingGroupSync` to prevent silent FK violations.

---

### Sprint: v3.6.3 — 2026-05-27 (ble-hci)

### [BATCH:ble-hci] — ⚡ (Complete)


- [x] **`spike/0x40-chunked-framing-hci-verify`** — Verified `writeChunked` 0x40 frame format on hardware. Merged `74ec886d`.
  - **Tags:** `[🕵️ SPIKE]` `[✅ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[🤖 THINK]` `[BATCH:ble-hci]`
  - **Plan:** 📎 [PLAN-chunked-ble-framing-0x51.md](./plans/PLAN-chunked-ble-framing-0x51.md)
  - **Source of Truth:** 📖 [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L706) §writeChunked and [ZENGGE_PROTOCOL_BIBLE.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L189) §0x51 Custom Scene
  - **Goal:** HCI-sniff verify the `writeChunked` 0x40 fragmentation frame format on real HALOZ/SOULZ hardware before wiring the Scene Builder UI to production chunked writes.
  - **Details:** COMPLETE — Validated `writeChunked` behavior on physical hardware via ADB HCI sniff, matched byte frames, and updated ZENGGE_PROTOCOL_BIBLE.md.

---

### Sprint: v3.6.3 — 2026-05-27 (dependency-diet)

### [BATCH:dependency-diet] — ⚡ (Complete)

- [x] **`spike/major-dep-upgrades`** — Upgraded React to 19.2.6 and @types/react to 19.2.15. Merged `89825d9c`.
  - **Tags:** `[🕵️ SPIKE]` `[✅ VERIFIED]` `[CORE]` `[H-RISK]` `[Feast]` `[🤖 THINK]` `[BATCH:dependency-diet]`
  - **Plan:** 📎 [PLAN-spike-major-dep-upgrades.md](./plans/PLAN-spike-major-dep-upgrades.md)
  - **Source of Truth:** 📖 [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json#L1)
  - **Goal:** Evaluate breaking changes for React and core dependencies.
  - **Details:** COMPLETE — Upgraded `react` and `react-dom` to 19.2.6, and `@types/react` to 19.2.15 while locking `react-native`, `typescript`, and `@react-native-async-storage/async-storage` to their stable versions to prevent BLE thread instability. Verified via full QA suite and merged.

---


### Sprint: v3.6.3 — 2026-05-27 (monolith-cleanup)

### [BATCH:monolith-cleanup] — ⚡ (Complete)

- [x] **`refactor/split-monolith-files`** — Monolith partitioned cleanly into 4 helper services + thin composition hook. Merged `48d35783`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[Meal]` `[🤖 THINK]` `[BATCH:monolith-cleanup]`
  - **Plan:** 📎 [PLAN-refactor-split-monolith-files.md](./plans/PLAN-refactor-split-monolith-files.md)
  - **Source of Truth:** 📖 [agent-behavior.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/.agents/rules/agent-behavior.md#L15) §Look Before You Leap
  - **Goal:** Break down monolithic files > 30KB discovered during audit.
  - **Details:** COMPLETE — Surgically extracted `BleConnectionManager`, `BleWriteDispatcher`, `BlePingService`, and `BleLifecycleManager` into separate stateless modules, and refactored `useBLE.ts` as a clean, thin orchestrator hook. Passed all unit, compiler, browser, and static quality checks.

---

### Sprint: v3.6.3 — 2026-05-27 (ble-and-camera-hardening)

### [BATCH:ble-hardening] — 📋 (Complete)

- [x] **`fix/sweeper-gatt-discovery-skip`** — Merged `971af30c`. Hoisted GATT discovery above cache lookup in interrogateDevice.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:ble-hardening]`
  - **Plan:** 📎 [PLAN-fix-sweeper-gatt-discovery-skip.md](./plans/PLAN-fix-sweeper-gatt-discovery-skip.md)
  - **Source of Truth:** 📖 [useBLESweeper.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts#L250) §interrogateDevice L250-268
  - **Goal:** Fix the third and final copy of the GATT discovery-on-cache-hit bug.
  - **Details:** COMPLETE — Hoisted `discoverAllServicesAndCharacteristics()` above the cache lookup block in interrogateDevice to ensure native handle maps populate before the `0x63` hardware settings query.

- [x] **`refactor/ble-session-factory`** — Merged `ffa980c8`. Extracted connect→discover→resolve adapter sequence.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[🤖 THINK]` `[BATCH:ble-hardening]`
  - **Plan:** 📎 [PLAN-refactor-ble-session-factory.md](./plans/PLAN-refactor-ble-session-factory.md)
  - **Source of Truth:** 📖 [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L670) §handshakeDevice
  - **Goal:** Extract connect→discover→resolve sequence from 4 callsites to eliminate Shotgun Surgery.
  - **Details:** COMPLETE — Created `BleSessionFactory` to own the invariant sequence `connect → discoverAll → resolveAdapter → return { conn, adapter }` with AbortSignal support, purging ~120 duplicate lines from useBLE, useBLESweeper, and useBLEAutoRecovery.

- [x] **`refactor/ble-typed-fsm-gate`** — Merged `f8b1c07a`. Replaced raw string gates with compile-enforced FSM.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[H-RISK]` `[Meal]` `[🤖 THINK]` `[BATCH:ble-hardening]`
  - **Plan:** 📎 [PLAN-refactor-ble-typed-fsm-gate.md](./plans/PLAN-refactor-ble-typed-fsm-gate.md)
  - **Source of Truth:** 📖 [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L61) §bleGateRef
  - **Goal:** Replace raw string union refs with typed state machine.
  - **Details:** COMPLETE — Created `BleStateMachine` FSM with valid transition checks and state event listeners. Replaced `bleGateRef` usages in useBLE, useBLESweeper, useBLEAutoRecovery, and useDashboardAutoConnect to use `.tag` checks and strict transitions, completely avoiding split-brain states and GATT collisions.

### [BATCH:camera-hardening] — ⚡ (Complete)

- [x] **`fix/camera-tracker-5hz-sideeffect`** — Merged `459319ad`. Decoupled 5Hz updates using shared React Ref.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]` `[BATCH:camera-hardening]`
  - **Plan:** 📎 [PLAN-fix-camera-tracker-5hz-sideeffect.md](./plans/PLAN-fix-camera-tracker-5hz-sideeffect.md)
  - **Source of Truth:** 📖 [CameraTracker.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx#L103) §dispatchSniperColor
  - **Goal:** Remove 5Hz BLE side-effects.
  - **Details:** COMPLETE — Destructured `liveColorRef` in `CameraTrackerProps` and updated it at 5Hz inside the frame processor, bypassing parent re-renders. Replaced the 5Hz callback inside `CameraPanel` with a no-op, reading from `liveColorRef.current` strictly on Shutter Capture. Passed the verification QA suite cleanly.

---

### Sprint: v3.6.3 — 2026-05-27 (camera-v2)

### [BATCH:camera-v2] — ⚡ (Complete)

- [x] **`feat/camera-vibe-catcher-v2`** — Complete Camera Vibe Catcher v2 redesign using VisionCamera v5 and K-Means. Merged `24cb371`.
  - **Tags:** `[✅ VERIFIED]` `[✅ VERIFIED]` `[UI]` `[H-RISK]` `[Feast]` `[🤖 THINK]` `[BATCH:camera-v2]`
  - **Plan:** 📎 [PLAN-camera-vibe-catcher-v2.md](./plans/PLAN-camera-vibe-catcher-v2.md)
  - **Source of Truth:** 📖 [Image.nitro.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/node_modules/react-native-nitro-image/src/specs/Image.nitro.ts#L138) §crop() API, [PreviewView.nitro.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/node_modules/react-native-vision-camera/src/specs/views/PreviewView.nitro.ts#L112) §takeSnapshot @platform Android, [useDockedControllerState.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDockedControllerState.ts#L8) §BuilderNode interface
  - **Goal:** Complete camera mode restart: replace broken `takeSnapshot()` + Nitro Image pipeline with cross-platform `useFrameProcessor` + `vision-camera-resize-plugin`. Add SNIPER (center-pixel solid color) and VIBE (K-Means 3-color palette → auto-Builder payload) dual sub-modes.
  - **Details:** COMPLETE — Migrated to the VisionCamera v5 API utilizing the GPU-backed `useFrameOutput` and `vision-camera-resize-plugin` (50x50 RGB scaling). Added SNIPER center-pixel sample targeting the 25x25 center element and VIBE K-Means (k=3, 5 iterations) clustering to automatically map dominant swatches to FG/BG/ACCENT. Wired static and flow custom spatial patterns to `0x59` BLE commands with padded 12-pixel buffer overflow protection. Files changed: CameraTracker.tsx, CameraPanel.tsx, DockedController.tsx, package.json, kMeansPalette.ts, and kMeansPalette.test.ts. Passed all local and E2E QA checks cleanly.

---

### Sprint: v3.6.3 — 2026-05-27 (permission-gating)

### [BATCH:permission-gating] — ⚡ (Complete)

- [x] **`feat/strict-permission-gating`** — Fixed 3 permission bugs + reactive dock icon gating. Merged `51104a3`.
  - **Tags:** `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:permission-gating]`
  - **Plan:** 📎 [PLAN-strict-permission-gating.md](./plans/PLAN-strict-permission-gating.md)
  - **Source of Truth:** 📖 `PermissionService.ts`, `DockedDock.tsx`, `DockedController.tsx`, `app.json`
  - **Goal:** Fix 3 permission bugs + enforce strict reactive UI gating for CAMERA/STREET dock icons.
  - **Details:** COMPLETE — BUG-1: Added ACTIVITY_RECOGNITION to app.json android.permissions. BUG-2: Gated CAMERA mode behind checkPermission + openGlobalPermissionsModal. BUG-3: Gated STREET mode behind LOCATION check. Added reactive dock visibility via PERMISSION_STATUS_CHANGED_EVENT + AppState listener. Favorite restore for CAMERA falls back to MULTIMODE if denied. Files changed: app.json, PermissionService.ts, DockedController.tsx, DockedDock.tsx.

- [x] **`feat/offline-guest-gating`** — Gated cloud publishing and community scenes behind offline mode. Merged `ccf4f2f`.
  - **Tags:** `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:permission-gating]`
  - **Plan:** 📎 [PLAN-offline-guest-gating.md](./plans/PLAN-offline-guest-gating.md)
  - **Source of Truth:** 📖 `Supabase` Session State and UI Layouts
  - **Goal:** Implement an "Offline/Guest" mode that selectively hides cloud-dependent UI features.
  - **Details:** COMPLETE — Propagated `isOfflineMode` through `DashboardScreen`, `DockedController`, `QuickPresetModal`, and `CommunityModal` to hide all cloud-based features and default to local "My Saves" appropriately. Passed QA Suite verification.

---

### Sprint: v3.6.3 — 2026-05-26 (camera-color-swatch-redesign)

- [x] **`feat/camera-color-swatch-redesign`** — Platform-split color analysis (Android TextureView + iOS worklet), center crop, neutral white snap, and 5-swatch UI palette. Merged `7c254f60`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[H-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:camera-color-swatch-redesign]`
  - **Plan:** 📎 [PLAN-camera-color-swatch-redesign.md](./plans/PLAN-camera-color-swatch-redesign.md)
  - **Source of Truth:** 📖 [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L306) §0x59 Static Colorful Pixel Array
  - **Goal:** Split Android & iOS camera tracking, implement center crop & neutral snap gate, and build a 5-swatch tactile UI.
  - **Details:** COMPLETE — Successfully split the CameraTracker hook into Android (TextureView takeSnapshot) and iOS (worklet useFrameOutput) platforms, resolving the Android react-native-worklets crash. Implemented a 32x32 center crop with 1x1 resize to average color over reticle rather than whole frame. Added a delta < 0.15 neutral white snapping gate to stop noisy green/blue drift. Completely redesigned CameraPanel to support a scrollable row of up to 5 captured tactile color swatches. Passed all verifiable checks cleanly.

---

### Sprint: v3.6.2 — 2026-05-26 (music-mode-fix)

- [x] **`fix/music-mode-payload-cap`** — Capped magnitude stream to 150 and disabled built-in mic when App mic active. Merged `99550a0`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]` `[BATCH:music-mode-fix]`
  - **Plan:** 📎 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity-ide/brain/3f09133f-0b00-4051-bb43-79e27aa0f099/implementation_plan.md)
  - **Source of Truth:** 📖 [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L773) §Music Mode and [MusicModeFragment.java](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/ZENGGE_APK/ZENGGE_DECOMPILED/sources/com/zengge/wifi/activity/NewSymphony/fragment/MusicModeFragment.java#L595)
  - **Goal:** Fix the `isOn` microphone toggle routing and scale magnitude limits to 150 to stop ambient visualizer pattern cycling.
  - **Details:** COMPLETE — Capped real-time App mic magnitude stream to 150 (down from 255) to prevent physical controller saturation/visualizer lockout, and corrected the 0x73 'isOn' byte routing to dynamically toggle off the hardware microphone in App Mic mode.

---

### Sprint: v3.9.0 — 2026-05-26 (rf-remote)

- [x] **`spike/rf-remote-2.4g-settings`** — Corrected RF remote state opcodes and integrated DeviceSettingsModal into DashboardScreen. Merged `f94a0b5d`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[LAB]` `[H-RISK]` `[Snack]` `[🤖 THINK]` `[BATCH:rf-remote]`
  - **Plan:** 📎 [PLAN-spike-rf-remote-2.4g-settings.md](./plans/PLAN-spike-rf-remote-2.4g-settings.md)
  - **Source of Truth:** 📖 [ZENGGE_PROTOCOL_BIBLE.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/ZENGGE_PROTOCOL_BIBLE.md#L1) §RF Remote Settings
  - **Goal:** Reverse engineer and implement the 2.4G RF Remote Settings (Allow All, Don't Allow, Paired Only, Clear Pairing).
  - **Details:** COMPLETE — Fixed inverted `ALLOW_ALL` (0x01), `ALLOW_NONE` (0x02), and `ALLOW_PAIRED` (0x03) byte mappings in `ZenggeProtocol.ts`, updated parsing mappings, added comprehensive unit tests, and fully imported/rendered the settings modal in `DashboardScreen.tsx` so long-pressing a device card displays the settings modal end-to-end.

---

### Sprint: v3.8.0 — 2026-05-26 (supabase-security)

- [x] **`chore/audit-supabase-security`** — Hardened PL/pgSQL function search paths and RLS policies on skate_spots, sk8lytz_app_settings, and telemetry. Merged `539dc791`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:supabase-security]`
  - **Plan:** 📎 [PLAN-chore-audit-supabase-security.md](./plans/PLAN-chore-audit-supabase-security.md)
  - **Source of Truth:** 📖 [SK8Lytz_App_Master_Reference.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md#L309) §Admin Tools Hub
  - **Goal:** Fix Supabase RLS policies and mutable search paths.
  - **Details:** COMPLETE — Hardened PL/pgSQL function search paths, dropped permissive RLS overrides on sk8lytz_app_settings, removed public write exposures on skate_spots, and fixed the broken admin select filter on discovered_devices_telemetry. Excluded inventory product_catalog from scope.

---

### Sprint: v3.7.0 — 2026-05-26 (tooling + chores + camera + music)

- [x] **`fix(tooling)/fortress-gatekeeper-divergence`** — Patched gatekeeper to not destroy branches on failed --ff-only merge. Merged `053ed333`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[🤖 FLASH]`
  - **Details:** COMPLETE — Added rebase-before-merge step + exit code capture + `continue` on failure. Branch and worktree are now PRESERVED when merge fails. Victory Snapshot VS-001 written to `safety-protocol.md`.

- [x] **`fix/missing-telemetry-script`** — Implemented `tools/sync_remote_errors.mjs`. Merged `57d04a80`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:telemetry-sync]`
  - **Details:** COMPLETE — Native Node 18+ fetch, zero deps, queries Supabase `telemetry_errors`, prints triage table + top 5 errors. CLI flags: `--hours`, `--limit`, `--json`. Offline-safe. Windows UV_HANDLE crash fixed via `process.exitCode`.

- [x] **`chore/audit-npm-vulnerabilities`** — npm audit returned 0 vulnerabilities. Merged `6b20619f`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:vulnerability-fix]`
  - **Details:** COMPLETE — Zero vulnerabilities found. Bucket list entry was stale. No package changes required.

- [x] **`spike/rearchitect-camera-mode`** — Platform-split fix: Android TextureView + iOS worklet. Merged `939a5262`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[H-RISK]` `[Feast]` `[🤖 THINK]`
  - **Details:** COMPLETE — 6 previous fixes all silently failed. Root causes: `takeSnapshot()` is `@platform Android` only; `implementationMode='performance'` (SurfaceView) doesn't support it; bare `catch{}` ate all errors; iOS had no valid fallback. Fix: Android uses TextureView + `takeSnapshot()`. iOS uses `useFrameOutput` worklet + `runOnJS`.

- [x] **`fix/camera-mode-warmup-reset`** — Stabilized callback identity cascade and resolved Android warmup timer reset loop (stuck-on-pink bug). Merged `dc643b27`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Details:** COMPLETE — Wrote a stable callback wrapper for `onColorDetected` inside a `useRef` inside `CameraTracker.tsx` to stop warmup loops and leaks.

- [x] **`fix/music-mode-ui-flex`** — Wired ColorSwatch FG/BG pickers in MusicPanel. Merged `2e6363fa`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Details:** COMPLETE — `ColorSwatch` wired + `overflow:'hidden'` clip removed.

- [x] **`fix/music-mode-color-inversion`** — Fixed APP mic deactivation in music mode. Merged `a303d409`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[L-RISK]` `[Snack]` `[🤖 FLASH]`
  - **Details:** COMPLETE — `src === 'DEVICE'` as isOn flag silently killed APP mic path.

- [x] **`fix/music-mode-pattern-mapping`** — Fixed colorMode, modeType passthrough, effectId clamp. Merged `4e41f7b8`.
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[L-RISK]` `[Meal]` `[🤖 PRO-HIGH]`
  - **Details:** COMPLETE — 3 bugs across MusicDictionary, ZenggeAdapter, ZenggeProtocol.

---

### Sprint: v3.6.0 — 2026-05-26

- [x] **`fix/health-telemetry-autostart`** — Auto-started HealthKit/HealthConnect bridges before polling. Merged `03096cf3`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE — Injected `AppleHealthKit.initHealthKit()` (iOS) and `initialize()` (Android Health Connect) at the top of each `pollHealthData()` cycle.

- [x] **`spike/appstate-ble-reconnect`** — Autonomous recovery on appstate wake. Merged `f518d38f`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[Meal]` `[🤖 THINK]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE — Added a 1500ms OS stack wakeup delay to `retriggerAutoConnect` and implemented a native `isDeviceConnected` audit in `useBLE.ts` to clear stale split-brain connections when the app wakes up.

- [x] **`feat/dashboard-pull-to-refresh`** — Add pull-to-refresh to dashboard for manual BLE sweep. Merged `f8c0b2bd`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:core-app-lifecycle]`
  - **Details:** COMPLETE — Wrapped `DashboardScreen` main `ScrollView` with `RefreshControl`. Triggered `retriggerAutoConnectRef.current()` on pull.

- [x] **`feat/crew-hub-radius-refresh`** — Added 10mi radius option. Merged `7aa002c5`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE — Added 10 miles to the "Live Near You" map radius dropdown.

- [x] **`fix/crew-hub-collapsed-padding`** — Fixed CrewHubSlab vertical padding state. Merged `7aa002c5`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE — Collapse padding when collapsed.

- [x] **`feat/crew-hub-mini-map`** — Added MapViewCluster inside slab. Merged `7aa002c5`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[Meal]` `[🤖 PRO-HIGH]` `[BATCH:crew-hub-enhancements]`
  - **Details:** COMPLETE — Added native mini-map static view inside collapsed slab.

- [x] **`chore/remove-test-category`** — Removed Test category from engine and UI. Merged `7aa002c5`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:pro-effects-cleanup]`
  - **Details:** COMPLETE — Stripped all `group: 'Test'` entries.

- [x] **`feat/universal-slider-labels`** — Refactored TacticalSlider labels inline. Merged `7aa002c5`.
  - **Tags:** `[✅ DONE]` `[✅ VERIFIED]` `[UI]` `[L-RISK]` `[Snack]` `[🤖 FLASH]` `[BATCH:universal-sliders]`
  - **Details:** COMPLETE — Added `label` prop to `TacticalSlider.tsx`.

---

### Sprint: v3.5.2 and prior

- [x] **`fix/detective-social-only-sites`** — Yelp/Places search on social-only sites. Merged auto-cleaned.
- [x] **`fix/detective-google-photo-refs`** — Harvest photo references from Places. Merged auto-cleaned.
- [x] **`fix/detective-escalation-all-passes`** — Re-run LLM passes on enriched text. Merged auto-cleaned.
- [x] **`spike/detective-model-upgrade-7b`** — 7B/8B model evaluation (GDDR6 VRAM optimization). Merged auto-cleaned.
- [x] **`feat/user-correction-feedback-loop`** — Log PUT spots feedback loop. Merged auto-cleaned.
- [x] **`feat/review-text-mining`** — Regex mine reviews for structured data. Merged auto-cleaned.
- [x] **`hotfix/data-integrity-guards`** — Preserved data upsert guards. Merged `1289dae4`.
- [x] **`chore/test-usecontrollerdispatch`** — Mocked dispatch testing suite. Merged `7ff122d7`.
- [x] **`fix/gatt-conn-133-exception`** — Android GATT 133 retries/teardown. Merged `df7a3c40`.
- [x] **`feat/detox-e2e-automation`** — Detox E2E tests for React Native. Merged `94e52cd8`.
- [x] **`chore/consolidate-rules-workflows`** — Rules consolidation (behavior and safety). Merged `17971a6`.
- [x] **`chore/align-git-workflows`** — Unified attestation gating on git workflows. Merged `8bdea378`.
- [x] **`chore/verifiable-attestation`** — Verifiable QA check-runner with cryptographic attestations. Merged `46f95b12`.
- [x] **`chore/worktree-junction-tsc`** — Enable Directory Junctions for worktrees. Merged `214d4249`.
- [x] **`test/component-smoke`** — Static parser checks for container views relative imports. Merged `214d4249`.
- [x] **`chore/eslint-import-gate`** — ESLint Flat Config + unused-import gates. Merged `214d4249`.
- [x] **`fix/split-brain-hygiene`** — Unified shared favorites and deleted dead stubs. Merged `15db163e`.
- [x] **`fix/ble-split-brain`** — Unified single BLEProvider context. Merged `6b8e6917`.
- [x] **`fix/two-way-health-sync`** — Push workout data natively to HealthKit/HealthConnect. Merged `b75f3f52`.
- [x] **`fix/telemetry-hardening-v2`** — Consolidated AppLogger telemetry channels. Merged `6ef76e05`.
- [x] **`feat/babel-syntax-gate`** — Pre-commit static AST syntax validator. Merged `6ef76e05`.
- [x] **`fix/black-hole-errors`** — CENTRALIZED production error visibility in BLE. Merged `f63a44c6`.
- [x] **`feat/geofence-rink-sync`** — social radar check-in geofence. Merged `c18bae6`.
- [x] **`feat/health-sync-integration`** — skating health telemetry sync. Merged `9168b2e`.
- [x] **`feat/scene-offline-sync-queue`** — offline AsyncStorage sync queues. Merged `fe99fb3f`.
- [x] **`spike/ios-android-parity-audit`** — Android takeSnapshot TextureView + iOS worklet parity. Merged `f0516ac9`.

*End of Archive.*
