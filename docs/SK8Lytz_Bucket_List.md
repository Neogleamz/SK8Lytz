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
> 🏆 **[BATCH:sweep/deep-dive-w1] GOAL COMPLETE** — all 5 remaining Wave 1 clusters resolved 2026-06-25 via `/goal`. Master is green at `df995610`.
> ✅ Completed: sweep/dashboard-extraction @ `49ddd601` · sweep/docked-controller @ `213b44a9` · sweep/protocol-monolith @ `1f4517af` · sweep/split-brain @ `82b60425` · sweep/circular-deps (verification-only, madge-confirmed) ✅
> 🏆 **[BATCH:teardown-fixes] GOAL COMPLETE** — 2026-06-26 `/goal` autonomous. 6 tasks, 2 waves (AST-verified). Master green at `ade1a45e`.

#### Batch Strategy Table — [BATCH:teardown-fixes] (AST-Verified — `node tools/ast-parser.js --collision-matrix artifacts/teardown_audit_clusters.json`)

| Wave | Task | Parallel-Safe? | Prerequisite | Collision Basis |
| ---- | ---- | -------------- | ------------ | --------------- |
| **1** | `fix/docked-duplicate-favorite-modal` + `fix/docked-stale-imperative-handle` | ⚠️ Unified worktree | None | Both edit `DockedController.tsx` — co-located in ONE worktree (Rule 1 Override) to avoid same-file parallel hazard the import-based AST missed |
| **1** | `fix/dashboard-autoconnect-double-listener` | ✅ parallel | None | `DashboardScreen.tsx` + `useDashboardAutoConnect.ts` |
| **1** | `fix/dashboard-flatlist-rerender` | ✅ parallel | None | `Dashboard/DashboardDeviceList.tsx` only |
| **1** | `refactor/break-circular-deps` | ✅ parallel | None | `deviceRepository/*` + `docked/Universal*.tsx` |
| **2** | `chore/teardown-dead-code-sweep` | Solo | Wave 1 merged | Collides w/ all (touches `DockedController.tsx` + `DashboardScreen.tsx`) — runs last on final state |

> AST output: `total_collisions: 4` (all vs `chore/teardown-dead-code-sweep`), `total_waves: 2`. Wave 1: 4 worktrees (DockedController pair unified). Wave 2: 1 solo.
> ✅ **Wave 1 MERGED** 2026-06-26 — master `1ad6db84`, full verify ✅ + madge 0 cycles. autoconnect `f576c431` · flatlist `9a6cabb2` · docked-pair `edefc352` (modal ✅ / handle ⚠️ partial — loadFavorite deferred → new TRIAGE) · break-circular-deps `1ad6db84`.
> ✅ **Wave 2 MERGED** 2026-06-26 — chore/teardown-dead-code-sweep @ `ade1a45e`. 10/11 items done (1 N/A — TODO already gone post-Wave-1). 2 `_appLogger:any` eliminated, dead createDashboardStyles shim removed, dead getGroupCount removed. Full verify ✅.
> ✅ **Wave 1 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 3/3 tasks merged @ `662e099b`
> ✅ **Wave 2 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 1/1 task merged @ `7932f168`
> ✅ **Wave 3 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 3/3 resolved (pii-telemetry pre-existing ✅ · ble-stability @ `cbac245c` ✅ · animation-render-perf @ `831e35b6` ✅)
> Completed: sweep/devops-secrets @ `60f2f33c` ✅ · sweep/pii-offline-first @ `5be04584` ✅ · sweep/type-safety @ `662e099b` ✅ · sweep/split-brain-dedup @ `7932f168` ✅
> ✅ **Wave 4 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 1/1 verified @ `cd6a9c85` (pre-existing)
> ✅ **Wave 5 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 1/1 verified @ `4c0aa302` (pre-existing)
> ✅ **Wave 6 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 1/1 merged @ `ae192040` (3 catch blocks fixed in useAccountOverview.ts)
> ✅ **Wave 7 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 2/2 merged (platform-guards @ `76cb2aa6` · async-storage-keys @ `ef458f73`)
> ✅ **Wave 8 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 1/1 verified @ `68f2626b` (all R-14/R-16/R-24 targets pre-existing)
> ✅ **Wave 9 COMPLETE** — [BATCH:deepdive-audit-2026-06-30] — 1/1 merged @ `a414a1c7` (re-entrancy guard on checkNewDevice + for/break→while in VisualizerHooks)
> 🏆 **[BATCH:deepdive-audit-2026-06-30] GOAL COMPLETE** — all 9 waves, 14 task clusters resolved 2026-06-30. Master is green.
> Currently executing: none
> Completed: chore/quick-preset-dead-writer-cleanup @ 19911657 ✅
> Completed: fix/device-cloud-sync-null-mac-guard @ 6dcdda8a ✅

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`chore/harden-backup-script`**
  - **Tags:** `[📝 NEEDS PLAN]` `[OPS]` `[⚠️ M-RISK]` `[🍪 Snack]` `[🤖 FLASH]`
  - **Goal:** Harden `tools/backup_database.ps1` — add empty-check on all 3 output files, exit non-zero on failure, and add a restore-verify step.
  - **Decision Log:** ⚠️ PREMISE CORRECTION (Reyes, 2026-07-01): The wind-down report from 2026-06-26 was wrong — the agent observed the directory during the cleanup pass (which deletes old files FIRST before dumping). All 3 files from that run exist on disk: `schema` = 453.5 KB, `data` = 6.2 MB, `roles` = 0.3 KB. The pipeline is NOT broken. Three real gaps exist: (1) only `$DataFile` is size-checked — schema/roles are never verified (L58-62); (2) script exits code 0 even on empty output, giving automation callers a false success signal; (3) no restore-verify script exists in `tools/`. Deprioritized 2026-07-01 — pipeline works, harden when time permits.
  - **Details:** The script uses `npx supabase db dump` (not raw pg_dump). Connects via `postgresql://postgres.{PROJECT_ID}:{SUPABASE_DB_PASSWORD}@aws-0-us-west-2.pooler.supabase.com:6543/postgres`. Writes to `backups/`. Cleanup at L22 deletes files older than 7 days BEFORE running the dump — explains the misleading observation window.

- [ ] **`fix/discord-bridge-unhealthy`**
  - **Tags:** `[📝 NEEDS PLAN]` `[✅ VERIFIED]` `[OPS]` `[M-RISK]` `[🍪 Snack]` `[🤖 FLASH]`
  - **Goal:** Restore the Discord bridge — `notify_discord.ps1` throws WebException despite the `cctower` container being Up.
  - **Decision Log:** /ship-it + /wind-down 2026-06-26: both Discord notifications failed (WebException) though `docker compose ps` shows `sk8lytz-scraper-stack` (cctower) Up 3 days. Container alive but the bridge endpoint/webhook is unreachable (expired webhook URL, dead listener inside cctower, or network).
  - **Details:** Check the bridge listener inside the cctower container + the Discord webhook URL validity. Non-blocking (notifications only) but every merge/release/session-end notification is currently silently lost.

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

### ✅ [BATCH:sweep/deep-dive-w1] — `sweep/deep-dive-w1` — COMPLETE (all 7 clusters resolved 2026-06-25)
> C1 BLE Queue ✅ · C12 Build Config ✅ (merged prior sessions) · C2 Dashboard `49ddd601` · C3 Protocol `1f4517af` · C4 Docked `213b44a9` · C14 Split-Brain `82b60425` · C16 Circular-Deps (verification-only, madge-confirmed) ✅
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/d866dd8f-29e4-4fcb-9112-6ebb619bbbc1/system_audit_report.md) — Wave 1 of deep-dive synthesis sweep. Structural work was largely pre-absorbed organically; agents delivered last-mile cleanup + verification (same pattern as W2–W5).

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

### ⚡ [BATCH:deepdive-audit-2026-06-30] — Full-Spectrum Code Audit Remediation (477 Findings, 9 Waves)

> **Source Analysis:** 🔬 Deep-dive sweep 2026-06-30 — 55-agent fleet, 477 unique verified findings, 9 AST-verified waves. Report: `artifacts/system_audit_report.md`
> **Decision Log:** User directive 2026-06-30 — "intake all tasks with fully detailed kanban plans ready to run in waves then execute all fixes until completed." All 14 PLAN files P1-verified by Quinn agents against live source.
> **⚠️ Wave gate (S9):** Wave N MUST NOT start until all Wave N-1 tasks confirmed merged via `git log`. `sweep/devops-secrets` MUST run first — live Supabase JWT and plaintext password committed in source.

#### Batch Strategy Table (AST-Verified — `node tools/ast-parser.js --collision-matrix artifacts/domain_clusters.json`)

| Wave | Cluster | Slug | Parallel-Safe? | Prerequisite | Risk |
|------|---------|------|---------------|-------------|------|
| **1** | DEVOPS_SECRETS | `sweep/devops-secrets` | Solo FIRST | None — must precede any `git push` | 🚨 H-RISK |
| **1** | TYPE_SAFETY | `sweep/type-safety` | ✅ parallel w/ pii | None | H-RISK |
| **1** | PII + OFFLINE_FIRST | `sweep/pii-offline-first` | ✅ parallel w/ type | None | 🚨 H-RISK |
| **2** | DUPLICATION | `sweep/split-brain-dedup` | Solo | Wave 1 merged | H-RISK |
| **3** | ANIMATION | `sweep/animation-render-perf` | ✅ 3-parallel | Wave 2 merged | M-RISK |
| **3** | PII_TELEMETRY | `sweep/pii-telemetry` | ✅ 3-parallel | Wave 2 merged | 🚨 H-RISK |
| **3** | BLE_STABILITY | `sweep/ble-stability` | ✅ 3-parallel | Wave 2 merged | M-RISK |
| **4** | MEMORY_LEAKS | `sweep/memory-lifecycle` | Solo | Wave 3 merged | H-RISK |
| **5** | MONOLITH | `sweep/monolith-extraction` | Solo | Wave 4 merged | H-RISK |
| **6** | ERROR_HANDLING | `sweep/error-handling` | Solo | Wave 5 merged | M-RISK |
| **7** | ASYNC_STORAGE | `sweep/async-storage-keys` | ✅ 2-parallel | Wave 6 merged | M-RISK |
| **7** | PLATFORM | `sweep/platform-guards` | ✅ 2-parallel | Wave 6 merged | L-RISK |
| **8** | STATE_MATRIX | `sweep/state-matrix` | Solo | Wave 7 merged | M-RISK |
| **9** | REENTRANCY | `sweep/reentrancy-guards` | Solo | Wave 8 merged | L-RISK |

---

---

## 🧹 TECH DEBT

> **Source Analysis (teardown-debt tasks below):** 🕵️ Reyes monolith-teardown wiring audit 2026-06-25 (SESSION_LOG `[ARTIFACT]` entries) + `npx madge --circular src/`. Pre-existing debt the deep-dive-w1 sweep did not address.

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






