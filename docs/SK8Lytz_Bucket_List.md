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
> 🚧 **[BATCH:teardown-fixes] EXECUTING** — 2026-06-26 `/goal` autonomous, from base `0edbdcaa`. 6 tasks, 2 waves (AST-verified).

#### Batch Strategy Table — [BATCH:teardown-fixes] (AST-Verified — `node tools/ast-parser.js --collision-matrix artifacts/teardown_audit_clusters.json`)

| Wave | Task | Parallel-Safe? | Prerequisite | Collision Basis |
| ---- | ---- | -------------- | ------------ | --------------- |
| **1** | `fix/docked-duplicate-favorite-modal` + `fix/docked-stale-imperative-handle` | ⚠️ Unified worktree | None | Both edit `DockedController.tsx` — co-located in ONE worktree (Rule 1 Override) to avoid same-file parallel hazard the import-based AST missed |
| **1** | `fix/dashboard-autoconnect-double-listener` | ✅ parallel | None | `DashboardScreen.tsx` + `useDashboardAutoConnect.ts` |
| **1** | `fix/dashboard-flatlist-rerender` | ✅ parallel | None | `Dashboard/DashboardDeviceList.tsx` only |
| **1** | `refactor/break-circular-deps` | ✅ parallel | None | `deviceRepository/*` + `docked/Universal*.tsx` |
| **2** | `chore/teardown-dead-code-sweep` | Solo | Wave 1 merged | Collides w/ all (touches `DockedController.tsx` + `DashboardScreen.tsx`) — runs last on final state |

> AST output: `total_collisions: 4` (all vs `chore/teardown-dead-code-sweep`), `total_waves: 2`. Wave 1: 4 worktrees (DockedController pair unified). Wave 2: 1 solo.
> Currently executing: Wave 1 — docked-pair · autoconnect-listener · flatlist-rerender · break-circular-deps

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`fix/supabase-db-security-advisors`**
  - **Tags:** `[📝 NEEDS PLAN]` `[DB]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 PRO-HIGH]`
  - **Goal:** Fix Supabase security advisors: SECURITY DEFINER views, mutable search_path, RLS disabled on public.spatial_ref_sys, and always-true RLS policies.
  - **Decision Log:** Logged by /health-sweep during /ship-it Phase 1. High security risk preventing release.
  - **Details:** 5 major flags including ERRORs on telemetry views and disabled RLS on spatial_ref_sys.

> **Source Analysis (tasks below):** 🕵️ Reyes monolith-teardown wiring audit 2026-06-25 — 4-agent read-only audit of C2/C3/C4/C14/C16 + `madge` cycle scan. Evidence in `docs/SESSION_LOG.md` `[ARTIFACT] Reyes — *Wiring Audit*` entries. All findings carry file:line proof. These are PRE-EXISTING teardown debts (not introduced by the deep-dive-w1 merges).

- [ ] **`fix/docked-duplicate-favorite-modal`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-docked-duplicate-favorite-modal.md](./plans/PLAN-fix-docked-duplicate-favorite-modal.md)
  - **Goal:** Eliminate the two simultaneously-rendered `FavoritePromptModal` instances so a favorite saves with the correct mode/state.
  - **Decision Log:** CRITICAL wiring audit finding — both `DockedController.tsx:1214` and `BuilderPanel.tsx:219` bind `visible` to the same `promptState==='NAMING_FAVORITE'` from shared `FavoritesContext`, with DIFFERENT `onSave` handlers; whichever wins corrupts the saved favorite.
  - **Analysis:** 📊 Source: Reyes C4 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "Two `<Modal visible={true}>` stack; `handleConfirmFavorite` (BUILDER nodes) vs `handleConfirmSaveFavorite` (activeMode color/pattern) race."
    Rejected alternative: "Leave it — rejected; user-facing data corruption on a core save path."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L1214) + [BuilderPanel.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/BuilderPanel.tsx#L219)
  - **Details:** Decide single ownership of the naming modal (likely keep DockedController's, drop BuilderPanel's, route BUILDER save through one `onSave`). File collision: shares `DockedController.tsx` with `fix/docked-stale-imperative-handle` + `chore/teardown-dead-code-sweep` — cannot share a parallel wave.

- [ ] **`fix/dashboard-autoconnect-double-listener`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-dashboard-autoconnect-double-listener.md](./plans/PLAN-fix-dashboard-autoconnect-double-listener.md)
  - **Goal:** Remove the duplicate AppState `change` listener so `retriggerAutoConnect` fires once per foreground resume, not twice.
  - **Decision Log:** HIGH wiring audit finding — `retriggerAutoConnect` is registered both inline (`DashboardScreen.tsx:405-421`) AND in `useDashboardAutoConnect.ts:480-486`; only the 5s throttle masks the double-fire. Throttle removal → double scan + double connect-queue.
  - **Analysis:** 📊 Source: Reyes C2 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "Extraction moved the listener into the hook but left the original in the screen — classic forgot-to-delete teardown artifact."
    Rejected alternative: "Rely on the throttle — rejected; correctness must not depend on a debounce side-effect."
  - **Source of Truth:** 📖 [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx#L405) + [useDashboardAutoConnect.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts#L480)
  - **Details:** Keep the hook's listener (correct owner); remove the screen's inline duplicate + its ref bridge if now redundant. Shares `DashboardScreen.tsx` with the cleanup sweep.

- [ ] **`fix/docked-stale-imperative-handle`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-docked-stale-imperative-handle.md](./plans/PLAN-fix-docked-stale-imperative-handle.md)
  - **Goal:** Fix the `useImperativeHandle` dep array so externally-exposed `applyCloudScene`/`loadFavorite` don't run stale closures.
  - **Decision Log:** MED wiring audit finding — `DockedController.tsx:448-469` deps `[speed, brightness, writeToDevice, optimisticWrite]` omit `applyCloudScene` + `loadFavorite`; crew scene-apply, crew loadout sync, and voice commands invoke stale closures.
  - **Analysis:** 📊 Source: Reyes C4 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "BLE reconcile path is safe (separate `onReconcileRef`); only the exposed ref handle is stale."
    Rejected alternative: "Widen deps blindly — must add exactly the two missing callbacks + confirm they're stable-wrapped."
  - **Source of Truth:** 📖 [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx#L448)
  - **Details:** Shares `DockedController.tsx` with the duplicate-modal fix + cleanup sweep — sequence these.

- [ ] **`fix/dashboard-flatlist-rerender`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[UI]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-dashboard-flatlist-rerender.md](./plans/PLAN-fix-dashboard-flatlist-rerender.md)
  - **Goal:** Stabilize the device-list `renderItem` so FlatList stops re-rendering every cell on every RSSI update.
  - **Decision Log:** MED perf wiring finding — `DashboardDeviceList.tsx:76-104` wraps `renderItem` in `useCallback` with `[props]` (whole object, new ref each render), defeating memoization.
  - **Analysis:** 📊 Source: Reyes C2 wiring audit (SESSION_LOG 2026-06-25).
    Key finding: "Every BLE RSSI tick re-renders all device cells; regression vs pre-teardown."
    Rejected alternative: "Memoize the whole props object upstream — rejected; granular deps on the values renderItem actually reads is the correct fix."
  - **Source of Truth:** 📖 [DashboardDeviceList.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Dashboard/DashboardDeviceList.tsx#L76)
  - **Details:** Isolated to `DashboardDeviceList.tsx` — parallel-safe with the docked fixes.

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

## 🧹 TECH DEBT

> **Source Analysis (teardown-debt tasks below):** 🕵️ Reyes monolith-teardown wiring audit 2026-06-25 (SESSION_LOG `[ARTIFACT]` entries) + `npx madge --circular src/`. Pre-existing debt the deep-dive-w1 sweep did not address.

- [ ] **`refactor/break-circular-deps`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[ARCH]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:teardown-fixes]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-refactor-break-circular-deps.md](./plans/PLAN-refactor-break-circular-deps.md)
  - **Goal:** Break the 9 circular dependencies remaining in master (6 in the `deviceRepository` barrel chain, 3 in `components/docked/Universal*` siblings).
  - **Decision Log:** `madge --circular` (2026-06-26) reports 9 cycles NOT covered by C16 (which only scoped appLogger/CrewService). `useRegistration → deviceRepository/index → DeviceRepositoryService → {GroupRepository, DeviceCloudSync, DeviceStateManagement, DeviceStorage, types}` (6) + `UniversalSlidersFooter ↔ {UniversalColorGrid, UniversalHueStripSlider, UniversalTacticalSliders}` (3).
  - **Analysis:** 📊 Source: Reyes C14/C16 audit + madge run (SESSION_LOG 2026-06-25/26).
    Key finding: "C16 left these out of scope (correctly); they remain real load-order fragility — undefined-at-import risk."
    Rejected alternative: "Lump into C16 — rejected; different module clusters, different fix (barrel back-edge vs sibling component cycle)."
  - **Source of Truth:** 📖 [deviceRepository/index.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/deviceRepository/index.ts) + [docked/UniversalSlidersFooter.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx)
  - **Details:** Two independent clusters — could split into a 2-task wave. Verify with `npx madge --circular src/` returning zero before merge. `docked/Universal*` cluster shares the docked directory neighborhood with the C4 fixes.

- [ ] **`chore/teardown-dead-code-sweep`**
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






