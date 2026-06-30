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
> Completed: sweep/devops-secrets @ `60f2f33c` ✅ · sweep/pii-offline-first @ `5be04584` ✅ · sweep/type-safety @ `662e099b` ✅ · sweep/split-brain-dedup @ `7932f168` ✅
> Currently executing: Wave 3 (3-parallel) — sweep/animation-render-perf · sweep/ble-stability · sweep/pii-telemetry

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`fix/db-backup-pipeline-failing`**
  - **Tags:** `[📝 NEEDS PLAN]` `[✅ VERIFIED]` `[OPS]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🤖 PRO-HIGH]`
  - **Goal:** Repair `tools/backup_database.ps1` — it produces EMPTY dumps (no usable backup).
  - **Decision Log:** /wind-down 2026-06-26 ran the backup: `schema_2026-06-26_19-41.sql` = **0 bytes**, `roles_*.sql` = 0.3 KB, and **NO `data_*.sql` produced**. The prior "backup" (`data_2026-06-13_02-21.sql`) was also 0 bytes — so backups have been silently failing since ≥ 2026-06-13. There is effectively NO working DB backup.
  - **Details:** Likely cause: pg_dump version mismatch vs Supabase Postgres, or stale/invalid connection string / credentials in the script (it exits without error but writes empty files). Fix must (1) make the script FAIL LOUDLY on empty output, (2) restore a real data dump, (3) verify file sizes > 0. Highest-priority ops gap — a DB incident right now is unrecoverable.

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

> **Source Analysis (tasks below):** 🕵️ Reyes monolith-teardown wiring audit 2026-06-25 — 4-agent read-only audit of C2/C3/C4/C14/C16 + `madge` cycle scan. Evidence in `docs/SESSION_LOG.md` `[ARTIFACT] Reyes — *Wiring Audit*` entries. All findings carry file:line proof. These are PRE-EXISTING teardown debts (not introduced by the deep-dive-w1 merges).

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

- [ ] **`sweep/animation-render-perf`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍱 Meal]` `[MEDIUM]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:3]`
  - **Goal:** Fix animation performance violations — 55 findings (9H/25M/21L) covering inline renderItem functions, missing useCallback/useMemo, and Reanimated shared-value misuse.
  - **Decision Log:** 2026-06-30 audit flagged FlatList renderItem inline closures and missing memoization causing unnecessary re-renders and jank on the dashboard device list.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-ANIMATION · Plan: [PLAN-animation-render-perf.md](./plans/PLAN-animation-render-perf.md)
    Key finding: "55 animation/render findings — inline renderItem functions in FlatList components, unstable refs to animated shared values, missing useCallback on expensive handlers."
    Rejected alternative: "Virtualization overhaul — REJECTED (P4), surgical renderItem extraction is sufficient."
  - **Source of Truth:** 📖 [artifacts/system_audit_report.md](../artifacts/system_audit_report.md) §CLUSTER-ANIMATION
  - **Details:** Wave 3, parallel-safe with `sweep/pii-telemetry` and `sweep/ble-stability`.

- [ ] **`sweep/ble-stability`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[M-RISK]` `[🍱 Meal]` `[MEDIUM]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:3]`
  - **Goal:** Harden BLE retry/reconnect paths — replace raw `setTimeout` delays with `enqueueDelay`, add missing constants for magic timeout literals.
  - **Decision Log:** 2026-06-30 audit flagged raw retry waits outside the BleWriteQueue — GATT-133 recovery and MTU-glitch waits use raw setTimeout, violating R-16. Quinn P1-corrected audit: `BackgroundBLEService.ts` citations were wrong (file is 49 lines); real targets are `ConnectService.ts` and `RecoveryService.ts`.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-BLE_STABILITY · Plan: [PLAN-ble-stability-hardening.md](./plans/PLAN-ble-stability-hardening.md)
    Key finding: "`ConnectService.ts:210,246,257` — GATT-133 retry and MTU-glitch waits use raw setTimeout; `RecoveryService.ts:78` — Phase 1/2 reconnect backoff lacks enqueueDelay."
    Rejected alternative: "Increase retry timeout caps globally — REJECTED, `RECOVERY_MAX_MS = 30_000` already exists and is the correct cap."
  - **Source of Truth:** 📖 [src/services/ble/ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L210)
  - **Details:** Wave 3, parallel-safe with `sweep/animation-render-perf` and `sweep/pii-telemetry`. Highest regression risk: `RecoveryService.test.ts:328` cancel-during-backoff test.

- [ ] **`sweep/memory-lifecycle`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[⚠️ H-RISK]` `[🥩 Feast]` `[HIGH]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:4]`
  - **Goal:** Plug memory leaks — 59 findings (14H/24M/21L): setInterval without clearInterval, missing useEffect cleanup for event listeners, hardware notification subscriptions not torn down.
  - **Decision Log:** 2026-06-30 audit found 14 HIGH-severity memory leak patterns — hardware notification subscriptions and scanner timers left running after component unmount or BLE disconnect.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-MEMORY_LEAKS · Plans: [PLAN-memory-leak-hardware-notifications.md](./plans/PLAN-memory-leak-hardware-notifications.md) · [PLAN-memory-leak-scanner-timers.md](./plans/PLAN-memory-leak-scanner-timers.md)
    Key finding: "14 HIGH findings — hardware notification subscriptions (`useHardwareNotifications.ts`) and scanner interval refs leak on unmount."
    Rejected alternative: "AbortController pattern — REJECTED for BLE context, ref-based cleanup is idiomatic per project pattern."
  - **Source of Truth:** 📖 [artifacts/system_audit_report.md](../artifacts/system_audit_report.md) §CLUSTER-MEMORY_LEAKS
  - **Details:** Wave 4, solo. Waits for Wave 3 (animation + pii-telemetry + ble-stability) to merge first.

- [ ] **`sweep/monolith-extraction`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[HIGH]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:5]`
  - **Goal:** Extract DashboardScreen (51KB) and HardwareSetupWizardScreen (41KB) into sub-components — both exceed the 30KB Monolith Scan hard stop.
  - **Decision Log:** 2026-06-30 audit flagged 2 files exceeding the 30KB monolith threshold (R-23). Both are collision zones — every concurrent edit risks destroying unrelated features. Extraction reduces blast radius for all future Wave 4+ tasks.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-MONOLITH · Plan: [PLAN-monolith-extraction-audit.md](./plans/PLAN-monolith-extraction-audit.md)
    Key finding: "`DashboardScreen.tsx` = 51KB (hard stop — S4), `HardwareSetupWizardScreen.tsx` = 41KB (hard stop). Both require extraction before future edits."
    Rejected alternative: "Inline refactor without extraction — REJECTED, violates S4 (30KB hard stop)."
  - **Source of Truth:** 📖 [src/screens/DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx) + [src/screens/Onboarding/HardwareSetupWizardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
  - **Details:** Wave 5, solo. Runs AFTER memory-lifecycle to avoid mid-extraction conflicts with leak fixes that also touch DashboardScreen.

- [ ] **`sweep/error-handling`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[🍱 Meal]` `[MEDIUM]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:6]`
  - **Goal:** Standardize error handling — 56 findings (3H/28M/25L): missing `e instanceof Error` unwrapping, catch blocks without AppLogger, silent async failures.
  - **Decision Log:** 2026-06-30 audit found 56 error handling violations — raw `catch(e)` without type narrowing causes "e.message is undefined" crashes; catch blocks without AppLogger create silent failures that are invisible in production.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-ERROR_HANDLING · Plan: [PLAN-error-handling-standardization.md](./plans/PLAN-error-handling-standardization.md)
    Key finding: "3H — async operations in services throwing unguarded. 28M — catch blocks log nothing (silent fail). Standard pattern: `if (e instanceof Error) AppLogger.error('ctx', e.message)`."
    Rejected alternative: "Global error boundary only — REJECTED, doesn't cover service-layer silent fails."
  - **Source of Truth:** 📖 [artifacts/system_audit_report.md](../artifacts/system_audit_report.md) §CLUSTER-ERROR_HANDLING
  - **Details:** Wave 6, solo. Runs after monolith extraction to avoid touching extracted components mid-refactor.

- [ ] **`sweep/async-storage-keys`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[M-RISK]` `[🍪 Snack]` `[MEDIUM]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:7]`
  - **Goal:** Fix AsyncStorage key consistency — 3 real findings (3 of 6 audit findings were false positives per Quinn P1 check): R-24 group-count violation in DashboardHeader, InterrogatorService MAC case normalization, BleCharacteristicCache key registry.
  - **Decision Log:** 2026-06-30 audit flagged 6 AsyncStorage key issues; Quinn P1-verified against live source — 3 were already fixed (useBLE.ts, DeviceStorage.ts, SpeedTrackingService). 3 real: DashboardHeader R-24 violation + InterrogatorService lowercase MAC + BleCharacteristicCache prefix gap.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-ASYNC_STORAGE · Plan: [PLAN-async-storage-key-registry-audit.md](./plans/PLAN-async-storage-key-registry-audit.md)
    Key finding: "`DashboardHeader.tsx:106` uses `firstDevice?.grouped` (R-24 violation) instead of `displayConnectedDevices.length > 1` ground truth. `InterrogatorService.ts:25` builds HW_CACHE_KEY with lowercase MAC (canonical is UPPERCASE)."
    Rejected alternative: "Rebuild entire key registry — REJECTED (P4), 3 targeted fixes are sufficient."
  - **Source of Truth:** 📖 [src/components/dashboard/DashboardHeader.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardHeader.tsx#L106)
  - **Details:** Wave 7, parallel-safe with `sweep/platform-guards`.

- [ ] **`sweep/platform-guards`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[NATIVE]` `[✅ L-RISK]` `[🍪 Snack]` `[LOW]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:7]`
  - **Goal:** Add missing Android manifest permission flags and fix inline Platform.OS imports in LocationService.
  - **Decision Log:** 2026-06-30 audit found BLUETOOTH_SCAN missing `neverForLocation` flag (allows OS to infer location from BLE — privacy regression) + 2 inline `require('react-native')` instead of top-level import in LocationService.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-PLATFORM · Plan: [PLAN-platform-guards.md](./plans/PLAN-platform-guards.md)
    Key finding: "`AndroidManifest.xml:8` — BLUETOOTH_SCAN missing `android:usesPermissionFlags=\"neverForLocation\"`. `LocationService.ts:30,88` — inline `require('react-native').Platform.OS`."
    Rejected alternative: "Target SDK downgrade to avoid the flag — REJECTED, security regression."
  - **Source of Truth:** 📖 [android/app/src/main/AndroidManifest.xml](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml#L8)
  - **Details:** Wave 7, parallel-safe with `sweep/async-storage-keys`. Config-only changes — no Master Reference docs gate required.

- [ ] **`sweep/state-matrix`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍱 Meal]` `[MEDIUM]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:8]`
  - **Goal:** Implement missing Loading/Error/Empty UI states in data-driven components — 20 findings (2H/9M/9L) across dashboard and crew screens.
  - **Decision Log:** 2026-06-30 audit found 20 state-matrix violations — components rendering Success state only, with no Loading/Error/Empty branches. Users see blank screens or stale data on network failures.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-STATE_MATRIX · Plans: [PLAN-state-matrix-error-ui.md](./plans/PLAN-state-matrix-error-ui.md) · [PLAN-state-matrix-sweep.md](./plans/PLAN-state-matrix-sweep.md)
    Key finding: "2 HIGH — crew and device list show blank on error with no feedback. 9 MEDIUM — loading skeletons missing across data views."
    Rejected alternative: "Global skeleton provider — REJECTED (P4), per-component state machines are idiomatic."
  - **Source of Truth:** 📖 [artifacts/system_audit_report.md](../artifacts/system_audit_report.md) §CLUSTER-STATE_MATRIX
  - **Details:** Wave 8, solo. Runs after async-storage + platform-guards to avoid conflict with DashboardHeader changes from Wave 7.

- [ ] **`sweep/reentrancy-guards`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[CORE]` `[✅ L-RISK]` `[🍪 Snack]` `[LOW]` `[BATCH:deepdive-audit-2026-06-30]` `[WAVE:9]`
  - **Goal:** Add re-entrancy guards to 2 verified async effects — `DashboardScreen.tsx` checkNewDevice effect and `VisualizerHooks.ts` sample loop.
  - **Decision Log:** 2026-06-30 audit listed 12 re-entrancy findings; Quinn P1-verified — 10 were phantom/stale citations (HeartbeatService already guarded, MusicModeService doesn't exist, SymphonyEngine has no setInterval). 2 real: DashboardScreen checkNewDevice async effect lacks re-entrancy flag; VisualizerHooks sample loop is O(n+m) but needs clarity refactor.
  - **Analysis:** 📊 Source: [system_audit_report.md](../artifacts/system_audit_report.md) CLUSTER-REENTRANCY · Plan: [PLAN-reentrancy-guards.md](./plans/PLAN-reentrancy-guards.md)
    Key finding: "`DashboardScreen.tsx:452-478` — `checkNewDevice` async effect in useEffect has `isMounted` guard but LACKS re-entrancy flag — can trigger parallel executions on rapid device connect events."
    Rejected alternative: "useMemo Map in VisualizerHooks — REJECTED (P4), `lastSampleIdx` monotonic resume already makes it O(n+m); fix is a clarity comment."
  - **Source of Truth:** 📖 [src/screens/DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L452) + [src/components/visualizer/VisualizerHooks.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/visualizer/VisualizerHooks.ts#L198)
  - **Details:** Wave 9, solo. Final wave — lowest risk, all prior fixes merged. 10/12 audit citations were stale per P1 check.

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






