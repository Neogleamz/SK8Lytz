# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🚧 ACTIVE SPRINT

> 🏆 **[BATCH:crew-e2e] GOAL COMPLETE** — all 3 waves merged 2026-06-22. Crew Hub functional end-to-end.
> ✅ Wave 3 Supabase deploy COMPLETE 2026-06-23 — edge function ACTIVE, pg_cron job firing every minute.
> ✅ **`fix/ble-disconnect-service` MERGED 2026-06-23** — `b3bd6abc`. DisconnectService extracted, VS-009 destroyClient-in-loop fixed, FEF3 pre-GATT filter + Tile guard (VS-006/VS-008) live. Master is green.
> ✅ **`feat/applogger-mmkv-storage` MERGED 2026-06-23** — `72e25ec7`. AppLoggerStorage migrated AsyncStorage → MMKV JSI. MAX_ENTRIES 500 → 5000. ProGuard rules patched. VS-010/011/012 resolved. Master is green.
> ✅ **`fix/controller-dispatch-safety` MERGED 2026-06-23** — `a93e73d2`. PII scrubbing (scrubPII) on all AppLogger device.id calls in useControllerDispatch. enqueueDelay (BleWriteQueue) replaces raw setTimeout for inter-device music write gaps. Master is green.
> ✅ **`fix/dashboard-styles-perf` MERGED 2026-06-23** — `4839c774`. Explicit ViewStyle/TextStyle annotations in theme.ts replacing as-casts. Zero runtime changes. Master is green.
> ✅ **`fix/protocol-core-integrity` MERGED 2026-06-23** — `f6867d92`. ZenggeAdapter now uses ZenggeProtocol.sharedInstance (PROTOCOL_CORE-004). Split-brain SeqNum between adapter and BleWriteDispatcher eliminated. Master is green.
> ✅ **`spike/watch-bridge-clean-install` MERGED 2026-06-24** — `57a2e9b4`. sk8lytz-watch-bridge module restored from 82b18f14. CI npm install break fixed. VS-012 (iOS startListening missing) resolved. Master is green.
> 🏆 **[BATCH:fix/protocol-audit] GOAL COMPLETE** — all 7 tasks merged 2026-06-24. Wave 1 (6 parallel) + Wave 2 (solo) complete.
> ✅ Wave 1 (6 parallel) — `ec3174eb` — adapter-chunking-comment · dispatcher-padding-dead-code · hw-settings-segments-haloz · protocol-dispatch-mtu-guard · settled-mode-direction · static-color-handler-cleanup. Master is green.
> ✅ Wave 2 (solo) — Completed: fix/music-mode-dep-array @ `428ff383` ✅. handleMusicChange dep array fix. Master is green.
> 🚧 **[BATCH:sweep/deep-dive-w1] EXECUTING (5 parallel)** — 2026-06-25 `/goal` autonomous. Worktrees live from `497a2f6e`.
> Currently executing: sweep/dashboard-extraction · sweep/protocol-monolith · sweep/docked-controller · sweep/split-brain · sweep/circular-deps

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`fix/supabase-db-security-advisors`**
  - **Tags:** `[📝 NEEDS PLAN]` `[DB]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 PRO-HIGH]`
  - **Goal:** Fix Supabase security advisors: SECURITY DEFINER views, mutable search_path, RLS disabled on public.spatial_ref_sys, and always-true RLS policies.
  - **Decision Log:** Logged by /health-sweep during /ship-it Phase 1. High security risk preventing release.
  - **Details:** 5 major flags including ERRORs on telemetry views and disabled RLS on spatial_ref_sys.

---

### 🔥 ON DECK

### ⚡ [BATCH:fix/protocol-audit] — BLE Protocol Defect Patch Series

> **Source Analysis**: 📊 Protocol Defect Audit 2026-06-24 — 8 confirmed defects (1 CRITICAL, 2 HIGH, 4 MEDIUM, 1 LOW) across BLE dispatch, hardware settings parsing, and React Native hooks. Read-only audit conducted session 2026-06-24.
> **Decision Log:** File-and-line evidence confirmed for all 8 findings. AST-verified wave assignments via `node tools/ast-parser.js --collision-matrix artifacts/protocol_audit_clusters.json`.

#### Batch Strategy Table (AST-Verified — `node tools/ast-parser.js --collision-matrix artifacts/protocol_audit_clusters.json`)

| Wave | Task | Parallel-Safe? | Prerequisite | Collision Basis |
| ---- | ---- | -------------- | ----------- | --------------- |
| **1** | `fix/hw-settings-segments-haloz` | ✅ 6 parallel | None | `hardwareSettingsHandler.ts` only |
| **1** | `fix/dispatcher-padding-dead-code` | ✅ 6 parallel | None | `BleWriteDispatcher.ts` only |
| **1** | `fix/protocol-dispatch-mtu-guard` | ✅ 6 parallel | None | `useProtocolDispatch.ts` only |
| **1** | `fix/adapter-chunking-comment` | ✅ 6 parallel | None | `ZenggeAdapter.ts` only |
| **1** | `fix/settled-mode-direction` | ✅ 6 parallel | None | `dynamicEffectHandler.ts` only |
| **1** | `fix/static-color-handler-cleanup` | ✅ 6 parallel | None | `staticColorHandler.ts` only |
| **2** | `fix/music-mode-dep-array` | Solo | Wave 1 merged | imports `useProtocolDispatch` (modified in F-003/Wave 1) |

> AST output: `total_collisions: 1` (fix/protocol-dispatch-mtu-guard ↔ fix/music-mode-dep-array via `useProtocolDispatch.ts`). Wave 1: 6-parallel. Wave 2: 1 solo.

---

### 🚀 Epic: Crew Hub End-to-End Repair — `[BATCH:crew-e2e]`

> **Source Analysis**: 🕵️ Reyes E2E audit — 📊 [crew-subsystem-e2e-audit.md](./analysis/crew-subsystem-e2e-audit.md) + [crew-broadcast-scene-redundancy.md](./analysis/crew-broadcast-scene-redundancy.md). Crew feature ~50% functional; light-sync, membership presence, and scheduled-crew activation all broken.
> **Decision Log:** User directive 2026-06-22 — crew must work end-to-end; scheduled crews = server-side activation; plan all three together before building.

#### Batch Strategy Table (AST-Verified — `node tools/ast-parser.js --collision-matrix artifacts/crew_epic_clusters.json`)

| Wave | Task | Parallel-Safe? | Prerequisite | Collision basis |
|------|------|---------------|-------------|-----------------|
| **1** | `fix/crew-broadcast-scene` | Solo | None | shares `CrewService.ts`, `DashboardScreen.tsx` |
| **2** | `fix/crew-membership-presence` | Solo | Wave 1 merged | shares `CrewService.ts`, `CrewSessionManager.ts` |
| **3** | `feat/crew-scheduled-server-side` | Solo | Wave 2 merged + SPIKE cleared | shares `CrewSessionManager.ts`, `DashboardScreen.tsx` |

> ⚠️ AST tool output: `total_collisions: 3`, `total_waves: 3` — all three pairwise-collide on shared files; **zero parallelism**, strictly sequential per VS-001.

---

### ⚡ [BATCH:sweep/deep-dive-w1] — `sweep/deep-dive-w1` — READY
> **Worktree**: Individual per-cluster · **Type**: Parallel (7 clusters) · **Prerequisite**: None
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 1 of deep-dive synthesis sweep

- [ ] **`sweep/dashboard-extraction`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract DashboardScreen.tsx (57 KB) into 4 sub-components + consolidated FSM state hook, reducing the orchestrator below 25 KB.
  - **Decision Log:** 55-agent QA fleet flagged 17 findings in this file — R-23 monolith, R-26 missing re-entrancy guards, R-17 listener leak, R-24 inline storage key, R-18 boolean traps.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C02-dashboard-extraction.md](./plans/PLAN-sweep-C02-dashboard-extraction.md)
    Key finding: "DashboardScreen.tsx at 57 KB with 4 context consumers, unguarded BackHandler, and DeviceEventEmitter leak."
    Rejected alternative: "Incremental cleanup in-place — rejected because the file is over the 30 KB hard stop; surgical edits risk collision."
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx) §Full file
  - **Details:** Creates Dashboard/ sub-directory with 5 new files. BackHandler Platform.OS guard required for iOS safety. AsyncStorage key must be centralized to storageKeys.ts.

- [ ] **`sweep/protocol-monolith`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Extract ZenggeProtocol.ts (55 KB), SpatialEngine (59 KB), and effectProcessors.ts (35 KB) into domain-scoped handler and processor modules below 30 KB each.
  - **Decision Log:** 3 HIGH R-23 monolith findings across protocol core; oversized files collide on every BLE change and block parallel development.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C03-protocol-monolith.md](./plans/PLAN-sweep-C03-protocol-monolith.md)
    Key finding: "ZenggeProtocol.ts 55 KB, SpatialEngine 59 KB, effectProcessors.ts 35 KB — all over the 30 KB hard stop."
    Rejected alternative: "Inline handler comments only — rejected because file size alone prevents safe surgical edits."
  - **Source of Truth:** 📖 [ZenggeProtocol.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts) §Full file
  - **Details:** ⚠️ Sage must verify at execution time — `staticColorHandler.ts` and `dynamicEffectHandler.ts` may already exist from prior protocol-audit work. Read the files directory before creating [NEW] files to avoid overwrite.

- [ ] **`sweep/docked-controller`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Complete DockedController.tsx extraction — add DockedHeader sub-component and useDockedState context facade to reduce context consumption from 4 to 1.
  - **Decision Log:** R-23 monolith (57 KB), R-27 (4 context consumers) — 3 panels already extracted as BuilderPanel/ProEffectsPanel/MusicPanel per prior sessions; remaining gap is header + state consolidation only.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C04-docked-controller.md](./plans/PLAN-sweep-C04-docked-controller.md)
    Key finding: "DockedController.tsx 57 KB with 4 context consumers; Color/Effect/Music panels already extracted — only DockedHeader and useDockedState remain."
    Rejected alternative: "Full re-extraction — 3 panel SKIPPED annotations in plan confirm prior work; don't overwrite existing components."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx) §Full file
  - **Details:** Scope is smaller than original plan due to prior extractions. Route remaining ZenggeProtocol direct calls through ControllerRegistry.

- [ ] **`sweep/split-brain`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[DATA]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Route useDashboardGroups reads through GroupRepository and remove duplicate lifetime_distance/speed stat updates in useCrewSession that bypass SpeedTrackingService.
  - **Decision Log:** R-21 split-brain (3 HIGH) — lifetime stats updated in two independent paths; any write to the wrong path causes permanent stat drift.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C14-split-brain.md](./plans/PLAN-sweep-C14-split-brain.md)
    Key finding: "useDashboardGroups bypasses GroupRepository; useCrewSession duplicates stat updates that SpeedTrackingService already owns."
    Rejected alternative: "Delete the duplicate path only — rejected because GroupRepository reads must be added first to avoid breaking the read path."
  - **Source of Truth:** 📖 [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts) + [useCrewSession.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts)
  - **Details:** 2 files only. Snack-sized but data-integrity critical — stat drift is silent and permanent.

- [ ] **`sweep/circular-deps`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[ARCH]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:sweep/deep-dive-w1]` `[WAVE:1]`
  - **Goal:** Break 4 import cycles in the appLogger chain and CrewService internal modules via lazy imports or interface extraction.
  - **Decision Log:** R-29 circular dependencies (4 cycles) from 55-agent QA fleet — circular imports cause undefined-at-runtime failures that are non-deterministic and hard to reproduce.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) · Plan: [PLAN-sweep-C16-circular-deps.md](./plans/PLAN-sweep-C16-circular-deps.md)
    Key finding: "4 import cycles across appLogger (AppLoggerService ↔ AppSettingsService) and CrewService (CrewService ↔ CrewAutoRejoin ↔ CrewRealtime)."
    Rejected alternative: "Barrel re-export reorganization alone — rejected because it masks cycles rather than breaking them."
  - **Source of Truth:** 📖 [appLogger/index.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/appLogger/index.ts) + [CrewService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService/CrewService.ts)
  - **Details:** 6 files across 2 module clusters. Use madge or manual trace to confirm cycle elimination before committing.

---

### ✅ [BATCH:sweep/deep-dive-w2] — `sweep/deep-dive-w2` — COMPLETE (verified in master 2026-06-23)
> C5 Error Unwrap ✅ · C8 Memory Leaks ✅ · C11 Accessibility ✅ · C15 FlatList Perf ✅ — all confirmed done via grep verification; no remaining findings.
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 2 of deep-dive synthesis sweep

---

### ✅ [BATCH:sweep/deep-dive-w3] — `sweep/deep-dive-w3` — COMPLETE (verified in master 2026-06-23)

> C6 Telemetry Context ✅ · C7 Hardcoded Delays ✅ · C9 Re-Entrancy ✅ · C17 Boolean→FSM ✅ — all confirmed done via grep verification.
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 3 of deep-dive synthesis sweep

---

### ✅ [BATCH:sweep/deep-dive-w4] — `sweep/deep-dive-w4` — COMPLETE (verified in master 2026-06-23)

> C10 Storage Key Registry ✅ — confirmed done via grep verification.
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 4 of deep-dive synthesis sweep

---

### ✅ [BATCH:sweep/deep-dive-w5] — `sweep/deep-dive-w5` — COMPLETE (verified in master 2026-06-23)

> C13 Type Safety ✅ — confirmed done via grep verification (no `as unknown as` double casts in target files).
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 5 of deep-dive synthesis sweep

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

---

## 🧹 TECH DEBT

### ⚡ [BATCH:branch-salvage] — Branch Audit Recovery Tasks (2026-06-23)
> **Source Analysis**: Branch hygiene audit 2026-06-23 — 4 orphaned branches inspected; 2 tech-debt tasks identified as unmerged work, 1 new feature identified.
> **Decision Log:** Branch audit discovered completed-but-unmerged work and salvageable code. These tasks close the gap before those branches are deleted.

#### Batch Strategy Table (AST-Verified)

| Wave | Task | Parallel-Safe? | Prerequisite | Collision Basis |
|------|------|---------------|-------------|-----------------|
| **1** | ~~`refactor/burn-down-audit-failures`~~ | — | — | SUPERSEDED — master already clean |
| **2** | ~~`fix/ble-disconnect-service`~~ ✅ merged `b3bd6abc` | Solo | None (Wave 1 superseded) | standalone — no collision |

---

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






