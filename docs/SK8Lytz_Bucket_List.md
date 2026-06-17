# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🚧 ACTIVE SPRINT

- `[x]` **`fix/js-yaml-audit`** — Add js-yaml override to resolve npm audit block on push.
  - **Tags:** `[✅ READY]` `[❌ UNVERIFIED]` `[APP]` `[L-RISK]` `[Snack]` `[BATCH:audit-sweep]` `[WAVE:12]`
  - **Source of Truth:** [PLAN-fix-js-yaml-audit.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-fix-js-yaml-audit.md)
  - **Decision Log:** Vulnerability is dev-only, but strict pre-push requires zero-moderate audit. Override is the safest minimal fix.
  - **Completed:** Merged at 50f48fb2

- `[/]` **`fix/group-concurrent-write`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Replace `Promise.all(targets.map(...))` with sequential `for...of` loops in group BLE dispatch to prevent GATT write collisions.
  - **Decision Log:** Deep-dive R-10 sniper + DOMAIN_GROUP_SYNC + DOMAIN_BLE_CORE confirmed 11 concurrent write violations across 4 files. GATT controller expects sequential device-by-device dispatch — parallel writes cause collision and packet loss on grouped skates.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-group-concurrent-write.md](./plans/PLAN-fix-group-concurrent-write.md)
    Key finding: "7 Promise.all(targets.map) in useControllerDispatch.ts bypass write queue serialization"
    Rejected alternative: "Adding mutex locks in BleWriteQueue — unnecessary, queue already serializes per-device"
  - **Source of Truth:** 📖 [useControllerDispatch.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts#L94) §BLE Dispatch
  - **Details:** 4 files, 11 locations. BLE pipeline — wrong fix risks bricking skates.

- `[/]` **`fix/protocol-seq-counter`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Fix sequence counter split-ownership in `0x40` chunked payload framing and replace hardcoded 54-pixel max with dynamic `numLeds`.
  - **Decision Log:** DOMAIN_PROTOCOL_CORE confirmed BleWriteDispatcher uses `Math.random()` for seqNum while ZenggeProtocol has a proper incrementing counter. Protocol Bible §0x51 mandates `seqNum increments per save operation`. Split ownership = out-of-sequence frames = silently dropped pixels.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-protocol-seq-counter.md](./plans/PLAN-fix-protocol-seq-counter.md)
    Key finding: "Dual sequence counter increment causes frame drops on 0x40 chunked payloads"
    Rejected alternative: "TransitionType 0x00→CASCADE mapping — confirmed FALSE POSITIVE by plan writer"
  - **Source of Truth:** 📖 [ZenggeAdapter.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts#L167) §Protocol Core
  - **Details:** 4 files, 5 locations. Byte-level payload pipeline — wrong values = physical LED corruption.

- `[/]` **`fix/pii-scrub-sweep`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[APP]` `[✅ L-RISK]` `[🍪 Snack]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Wrap all unscrubbed PII (deviceId, name, crewName) in AppLogger calls with `scrubPII()` utility.
  - **Decision Log:** R-09 sniper found 16 instances of unscrubbed PII reaching cloud telemetry. Plan writer reduced to 14 confirmed fixes after dropping 2 false positives (DEV-gated log, pattern label string).
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-pii-scrub-sweep.md](./plans/PLAN-fix-pii-scrub-sweep.md)
    Key finding: "14 unscrubbed PII values in production AppLogger calls across 7 files"
    Rejected alternative: "N/A — find-and-replace pattern, no design alternatives"
  - **Source of Truth:** 📖 [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L115) §PII
  - **Details:** 7 files, 14 locations. Privacy/compliance fix.

- `[/]` **`fix/os-variance-parity`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:1]`
  - **Goal:** Fix 3 OS variance parity violations: missing Android elevation and web-specific styles not wrapped in `Platform.select`.
  - **Decision Log:** R-20 sniper confirmed 3 verified violations. `countdownBadge` missing Android elevation causes invisible shadow. CustomSlider/TacticalSlider merge web-only styles on all platforms.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-os-variance-parity.md](./plans/PLAN-fix-os-variance-parity.md)
    Key finding: "3 style definitions with iOS/web shadows but no Android elevation"
    Rejected alternative: "N/A — straightforward style fixes"
  - **Source of Truth:** 📖 [DashboardStyles.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/styles/DashboardStyles.ts#L105) §Styles
  - **Details:** 3 files, 3 locations. Style-only changes.

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

---

### 🌊 Parallel Wave Strategy (AST-Verified)

| Wave | Task Clusters | Parallel-Safe? | Prerequisite |
|------|--------------|----------------|--------------|
| **W-1** | C1 (Group Concurrent Write), C3 (Protocol Seq Counter), C7 (PII Scrub), C10 (OS Variance) | ✅ Yes — zero import-tree overlap | None |
| **W-2** | C2 (Split-Brain), C4 (BLE Type Safety), C5 (Promise IO Guards) | ✅ Yes — no shared files within wave | W-1 fully merged |
| **W-3** | C6 (Re-entrancy Guards), C8 (FSM State Matrix), C9 (Timer Audit BLE) | ✅ Yes — disjoint after W-2 merges | W-2 fully merged |

---

## 🔥 ON DECK
### ⚡ [BATCH:fix/performance-telemetry] — `fix/performance-telemetry` — DONE
> **Worktree**: `fix/performance-telemetry` · **Type**: Isolated · **Prerequisite**: None
> **Source Analysis**: 📊 Command Center widget `AppPerformanceWidget.tsx` is starving for data because React Native's telemetry queue drops TTID events.
> Completed: `fix/performance-telemetry` @ 828549b8 ✅

---

### ⚡ [BATCH:deepdive-audit-sweep] — `deepdive-audit-sweep` — READY
> **Worktree**: Per-task isolated worktrees · **Type**: Parallel (3 Waves) · **Prerequisite**: `fix/js-yaml-audit` merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) — Deep-dive code synthesis from 36 Map-Reduce agents. 268 verified findings across 10 clusters targeting group connection, payload sync, and critical architectural issues.

#### 🌊 Wave 1 — BLE Pipeline & Quick Wins (Parallel-Safe)

#### 🌊 Wave 2 — Architecture & Safety (Prerequisite: Wave 1 merged)

- [ ] **`fix/ble-core-type-safety`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:deepdive-audit-sweep]` `[WAVE:2]`
  - **Goal:** Eliminate all `any` type casts from BLE core production services and fix the Device/DisplayDevice type hierarchy.
  - **Decision Log:** R-08 sniper + DOMAIN_BLE_CORE confirmed 11 `any` casts in production BLE services + 9 `as unknown as` casts in DashboardScreen indicating broken type hierarchy. Plan makes `DisplayDevice extends Device` to eliminate casts at the type system level.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-ble-core-type-safety.md](./plans/PLAN-fix-ble-core-type-safety.md)
    Key finding: "DisplayDevice and Device type hierarchies are disjoint, causing 9 forced casts"
    Rejected alternative: "Adapter functions at each cast site — eliminated by making DisplayDevice extend Device"
  - **Source of Truth:** 📖 [BleMachine.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L18) §BLE Types
  - **Details:** 7 files, ~20 locations. Prerequisite: Wave 1 fully merged into master before this worktree is created.

- [ ] **`fix/promise-io-guards`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[CLOUD]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:2]`
  - **Goal:** Add try/catch guards and .catch() handlers to all unguarded async IO operations to prevent unhandled promise rejections.
  - **Decision Log:** R-11 sniper + DOMAIN_NOTIFICATIONS_&_ROUTING confirmed 15 unguarded async ops. 3 HIGH severity (AppConfigContext, AppLoggerStorage, LocationService) can crash silently and cause data loss.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-promise-io-guards.md](./plans/PLAN-fix-promise-io-guards.md)
    Key finding: "AppLoggerStorage uses console.warn instead of AppLogger.warn to prevent re-entrant logging loops"
    Rejected alternative: "Global unhandledrejection handler — masks bugs instead of fixing them"
  - **Source of Truth:** 📖 [AppConfigContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AppConfigContext.tsx#L25) §IO Safety
  - **Details:** 5 files, 15 locations. Prerequisite: Wave 1 fully merged into master before this worktree is created.

#### 🌊 Wave 3 — Hardening & Code Quality (Prerequisite: Wave 2 merged)

- [ ] **`fix/reentrant-handler-guards`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🍱 Meal]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:3]`
  - **Goal:** Add re-entrancy guards (`isProcessing` ref pattern) to all async UI handler functions that interact with BLE or Supabase.
  - **Decision Log:** R-26 sniper found 28 unguarded async handlers. Double-tap on `handleStartScan`, `handleLeave`, `handleJoinByCode` causes BLE races, orphaned sessions, and duplicate database entries.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-reentrant-handler-guards.md](./plans/PLAN-fix-reentrant-handler-guards.md)
    Key finding: "Shared refs for mutually exclusive handlers within the same component"
    Rejected alternative: "Disabling buttons during processing — already exists as UI state, but ref guard is the synchronous first-line defense"
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx) §UI Safety
  - **Details:** 12 files, 28 handlers. Prerequisite: Wave 2 fully merged into master before this worktree is created.

- [ ] **`fix/fsm-state-matrix`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI]` `[✅ L-RISK]` `[🥩 Feast]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:3]`
  - **Goal:** Replace disjoint loading/error/success boolean state variables with unified FSM `ViewState` string unions across 20 files.
  - **Decision Log:** R-14 sniper found 38 boolean trap violations. 3-tier approach: full FSM conversion (10 files), hybrid cleanup (4 files), error normalization (6 files). Creates shared `ViewState` type.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-fsm-state-matrix.md](./plans/PLAN-fix-fsm-state-matrix.md)
    Key finding: "38 components use disjoint booleans instead of FSM string unions"
    Rejected alternative: "XState for UI views — overkill, string unions sufficient for non-BLE state"
  - **Source of Truth:** 📖 [useAccountOverview.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAccountOverview.ts#L70) §State Management
  - **Details:** 20 files in 3 tiers. Prerequisite: Wave 2 fully merged into master before this worktree is created.

- [ ] **`fix/timer-audit-ble`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[✅ L-RISK]` `[🥩 Feast]` `[🤖 FLASH]` `[BATCH:deepdive-audit-sweep]` `[WAVE:3]`
  - **Goal:** Replace hardcoded setTimeout/setInterval delays in the BLE pipeline with named constants from `bleTimingConstants.ts`.
  - **Decision Log:** R-16 sniper found 94 hardcoded delays. Plan writer confirmed BleWriteDispatcher is already migrated. 10 files need migration, pure refactor — zero behavioral changes.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md) · Plan: [PLAN-fix-timer-audit-ble.md](./plans/PLAN-fix-timer-audit-ble.md)
    Key finding: "BleWriteDispatcher already migrated, 10 other BLE files still have magic numbers"
    Rejected alternative: "Runtime-configurable params — unnecessary for hardware-specific timing constants"
  - **Source of Truth:** 📖 [bleTimingConstants.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/bleTimingConstants.ts) §BLE Constants
  - **Details:** 10 files, ~26 new constants. Prerequisite: Wave 2 fully merged into master before this worktree is created.

---

##  ❄️ Icebox / Backburner (Manual Trigger Only)

### 🧹 Tech Debt & Upgrades
### ⚡ [BATCH:refactor/upgrade-expo-56] — `refactor/upgrade-expo-56` — IN PROGRESS
> **Worktree**: `refactor/upgrade-expo-56` · **Type**: Sequential · **Prerequisite**: None
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/fb5fb761-e7be-4241-a902-3cb07dca3307/implementation_plan.md) — User explicitly requested forcing a major dependency update (Expo 55->56) mid-release to resolve deep-tree js-yaml vulnerabilities.

- [ ] **`refactor/upgrade-expo-56`**
  - **Tags:** `[✅ READY]` `[☁️ CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 M132]` `[BATCH:refactor/upgrade-expo-56]`
  - **Goal:** Upgrade the project from Expo SDK 55 to 56, including React Native 0.85, to wipe out all outstanding NPM vulnerabilities.
  - **Decision Log:** User explicitly requested during `/ship-it` to override release freeze and force a full dependency update.
  - **Analysis:** 📊 Plan: [PLAN-refactor-upgrade-expo-56.md](./plans/PLAN-refactor-upgrade-expo-56.md)
  - **Source of Truth:** 📖 [package.json](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/package.json)
  - **Details:** High risk of breaking custom native modules and legacy UI components due to React Native 0.85 bridging changes.

### 🎵 Epic: Music Mode

- [ ] `feat/music-intel-phase-1` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 50k] [⏱️ 6h] [📅 2026-04-14] [🧠 THINK] [Spotify Sync] — OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] [Media Access] — Android MediaSession detection (YouTube, Pandora, etc.). → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [🧪 LAB] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] [Live Rink Mode] — ShazamKit/ACRCloud periodic background scanning (45s). → [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] [Apple Music] — MusicKit integration for native iOS BPM. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 45k] [⏱️ 6h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🧠 THINK] [Crew Party Sync] — Master BPM Choreography Engine with Realtime crew sync. → [Plan](docs/plans/feat-music-integration-master.md)

- [ ] `feat/google-oauth-integration` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [⏱️ 6h] [📅 2026-04-14] [🧠 THINK] Integrate Google OAuth as an auth provider. (Requires Google Cloud Console setup + Supabase config). → [Plan](docs/plans/feat-google-oauth-integration.md)
- [ ] `feat/spatial-beat-mapping` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 18k] [⏱️ 3h] [🧠 THINK] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). → [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Transform Dashboard into palette-synced dynamic backgrounds. → [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [🧪 LAB] [✅ L-RISK] [🍱 Meal] [🪙 10k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Pattern selection (Strobe, Blink, Static) + music slider fix. → [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/impact-sentinel-safety` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🧠 THINK] [Pillar 13] Fall Detection — triggers white 'Flare' strobe on impact. → [Plan](docs/plans/feat-impact-sentinel-safety.md)
- [ ] `feat/kinetic-brake-lights` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🧠 THINK] [Pillar 12] Kinetic Safety — phone accelerometer pulse RED for braking. → [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [⏱️ 6h] [🧠 THINK] Geofence-based 'Hive Mind' synchronization. → [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 8k] [⏱️ 3h] [🤖 FLASH] [📝️ NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 25k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [☁️ CLOUD] [⚠️ H-RISK] [🍱 Meal] [🪙 20k] [⏱️ 3h] [🧠 THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 12k] [⏱️ 3h] [🤖 FLASH] [📝️ NEEDS-PLAN] Card Swipe Navigation.
