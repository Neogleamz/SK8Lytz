# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🚧 ACTIVE SPRINT

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`fix/supabase-db-security-advisors`**
  - **Tags:** `[📝 NEEDS PLAN]` `[DB]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 PRO-HIGH]`
  - **Goal:** Fix Supabase security advisors: SECURITY DEFINER views, mutable search_path, RLS disabled on public.spatial_ref_sys, and always-true RLS policies.
  - **Decision Log:** Logged by /health-sweep during /ship-it Phase 1. High security risk preventing release.
  - **Details:** 5 major flags including ERRORs on telemetry views and disabled RLS on spatial_ref_sys.

- [ ] **`fix/gatt-conn-133-exception`**
  - **Tags:** `[📝 NEEDS PLAN]` `[LAB]` `[M-RISK]` `[Snack]` `[🤖 PRO-HIGH]`
  - **Plan:** 📎 [PLAN-telemetry-gatt-conn-133-exception.md](./plans/PLAN-telemetry-gatt-conn-133-exception.md)
  - **Goal:** Resolve automated telemetry crash: GattException: status 133 (0x85) during BLE scan discover for HALOZ
  - **Details:** Found crash telemetry with ID err_091a in file `src/hooks/ble/useBLEAutoRecovery.ts`. Trace: at useBLE.ts:321
at useBLESweeper.ts:145

---

### 🔥 ON DECK

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

---

### ⚡ [BATCH:sweep/deep-dive-w1] — `sweep/deep-dive-w1` — READY
> **Worktree**: Individual per-cluster · **Type**: Parallel (7 clusters) · **Prerequisite**: None
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 1 of deep-dive synthesis sweep

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

---

### ⏳ [BATCH:sweep/deep-dive-w2] — `sweep/deep-dive-w2` — BLOCKED
> **Worktree**: Individual per-cluster · **Type**: Parallel (4 clusters) · **Prerequisite**: Wave 1 fully merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 2 of deep-dive synthesis sweep

- [ ] **`sweep/memory-leaks`**
  - **Tags:** `[✅ READY]` `[UI]` `[⚠️ M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Add useEffect cleanup return functions for event listeners and animation loops.
  - **Decision Log:** R-22 found 6 HIGH severity memory leaks in UI components.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C08-memory-leaks.md](./plans/PLAN-sweep-C08-memory-leaks.md)
  - **Source of Truth:** 📖 [AccountModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/AccountModal.tsx)
  - **Details:** 6 component files. Prerequisite: Wave 1 fully merged.

- [ ] **`sweep/accessibility-i18n`**
  - **Tags:** `[✅ READY]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Wrap all hardcoded accessibilityLabel strings with t() from i18n.
  - **Decision Log:** 9 UI components have hardcoded English accessibility labels.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C11-accessibility-i18n.md](./plans/PLAN-sweep-C11-accessibility-i18n.md)
  - **Source of Truth:** 📖 [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
  - **Details:** 8 files. Prerequisite: Wave 1 fully merged.

- [ ] **`sweep/flatlist-perf`**
  - **Tags:** `[✅ READY]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w2]` `[WAVE:2]`
  - **Goal:** Extract inline FlatList components to stable useCallback/useMemo references.
  - **Decision Log:** R-28 found 3 FlatList bottlenecks with inline components causing re-renders.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C15-flatlist-perf.md](./plans/PLAN-sweep-C15-flatlist-perf.md)
  - **Source of Truth:** 📖 [AdminToolsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/AdminToolsModal.tsx)
  - **Details:** 2 files. Prerequisite: Wave 1 fully merged.

---

### ⏳ [BATCH:sweep/deep-dive-w3] — `sweep/deep-dive-w3` — BLOCKED
> **Worktree**: Individual per-cluster · **Type**: Parallel (4 clusters) · **Prerequisite**: Wave 2 fully merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 3 of deep-dive synthesis sweep

- [ ] **`sweep/reentrancy-guards`**
  - **Tags:** `[✅ READY]` `[UI]` `[⚠️ M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w3]` `[WAVE:3]`
  - **Goal:** Add isProcessingRef.current guards to async UI handlers missing them.
  - **Decision Log:** R-26 found 6 re-entrancy races. Double-tap on async handlers causes duplicate operations.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C09-reentrancy-guards.md](./plans/PLAN-sweep-C09-reentrancy-guards.md)
  - **Source of Truth:** 📖 [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** 4 files. Prerequisite: Wave 2 fully merged.

- [ ] **`sweep/boolean-fsm`**
  - **Tags:** `[✅ READY]` `[UI]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w3]` `[WAVE:3]`
  - **Goal:** Replace scattered boolean states with string union types or FSM patterns.
  - **Decision Log:** R-18 found 6 boolean trap patterns. Impossible state combinations possible.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C17-boolean-fsm.md](./plans/PLAN-sweep-C17-boolean-fsm.md)
  - **Source of Truth:** 📖 [AuthFormSignUp.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/auth/AuthFormSignUp.tsx)
  - **Details:** 3 files. Prerequisite: Wave 2 fully merged.

---

### ⏳ [BATCH:sweep/deep-dive-w4] — `sweep/deep-dive-w4` — BLOCKED
> **Worktree**: Solo · **Type**: Solo · **Prerequisite**: Wave 3 fully merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 4 of deep-dive synthesis sweep

- [ ] **`sweep/storage-key-registry`**
  - **Tags:** `[✅ READY]` `[QUAL]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 ROUTINE]` `[BATCH:sweep/deep-dive-w4]` `[WAVE:4]`
  - **Goal:** Centralize all AsyncStorage keys to storageKeys.ts.
  - **Decision Log:** R-24 found 7 undocumented/inline AsyncStorage keys. Risk: key collision and drift.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C10-storage-key-registry.md](./plans/PLAN-sweep-C10-storage-key-registry.md)
  - **Source of Truth:** 📖 [storageKeys.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/storageKeys.ts)
  - **Details:** 3 files. Prerequisite: Wave 3 fully merged.

---

### ⏳ [BATCH:sweep/deep-dive-w5] — `sweep/deep-dive-w5` — BLOCKED
> **Worktree**: Solo · **Type**: Solo · **Prerequisite**: Wave 4 fully merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 5 of deep-dive synthesis sweep

- [ ] **`sweep/type-safety`**
  - **Tags:** `[✅ READY]` `[QUAL]` `[⚠️ M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w5]` `[WAVE:5]`
  - **Goal:** Replace as unknown as double casts and JSON laundering with proper type guards.
  - **Decision Log:** R-08 is #4 most-violated rule. 18 findings (10 HIGH). Type laundering masks runtime errors.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C13-type-safety.md](./plans/PLAN-sweep-C13-type-safety.md)
  - **Source of Truth:** 📖 [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
  - **Details:** ~8 files. Prerequisite: Wave 4 fully merged.

---

### 🛡️ Epic: BLE Connection Excellence

> **Source Analysis**: 📊 [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) — Industry gap analysis vs Govee/Hue/LIFX/Nordic gold standards (10 gaps, 8 selected for intake)

#### Batch Strategy Table

| Wave | Tasks | Worktree | Prerequisite |
|------|-------|----------|-------------|
| Wave 1 | scan-filter-uuid, connection-state-badges, gatt-resource-cleanup | `feat/ble-excellence-w1` | None |
| Wave 2 | gatt-operation-queue, connection-params | `feat/ble-excellence-w2` | Wave 1 merged |
| Wave 3 | autoconnect-passive, smart-group-health | `feat/ble-excellence-w3` | Wave 2 merged |
| Wave 4 | background-reconnect | `feat/ble-excellence-w4` | Wave 3 merged |

---

### ⚡ [BATCH:feat/ble-excellence-w1] — `feat/ble-excellence-w1` — COMPLETE
> **Worktree**: `feat/ble-excellence-w1` · **Type**: Sequential (unified) · **Prerequisite**: None
> **Source Analysis**: 📊 [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) — Gaps 9, 3, 10

---

### ⚡ [BATCH:feat/ble-excellence-w2] — `feat/ble-excellence-w2` — COMPLETE
> **Worktree**: `feat/ble-excellence-w2` · **Type**: Sequential (unified) · **Prerequisite**: Wave 1 merged
> **Source Analysis**: 📊 [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) — Gaps 2, 6

---

### ⚡ [BATCH:feat/ble-excellence-w3] — `feat/ble-excellence-w3` — COMPLETE
> **Worktree**: `feat/ble-excellence-w3` · **Type**: Sequential · **Prerequisite**: Wave 2 merged
> **Source Analysis**: 📊 [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) — Gaps 4, 7

---

### ⚡ [BATCH:feat/ble-excellence-w4] — `feat/ble-excellence-w4` — COMPLETE
> **Worktree**: `feat/ble-excellence-w4` · **Type**: Solo · **Prerequisite**: Wave 3 merged
> **Source Analysis**: 📊 [connection_gap_analysis.md](file:///C:/Users/Magma/.gemini/antigravity/brain/4d36a4af-a431-4005-8193-df3fb92727c5/connection_gap_analysis.md) — Gap 1 (the big one)

---

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






