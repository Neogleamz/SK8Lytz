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
|------|------|---------------|-------------|-----------------|
| **1** | `fix/hw-settings-segments-haloz` | ✅ 6 parallel | None | `hardwareSettingsHandler.ts` only |
| **1** | `fix/dispatcher-padding-dead-code` | ✅ 6 parallel | None | `BleWriteDispatcher.ts` only |
| **1** | `fix/protocol-dispatch-mtu-guard` | ✅ 6 parallel | None | `useProtocolDispatch.ts` only |
| **1** | `fix/adapter-chunking-comment` | ✅ 6 parallel | None | `ZenggeAdapter.ts` only |
| **1** | `fix/settled-mode-direction` | ✅ 6 parallel | None | `dynamicEffectHandler.ts` only |
| **1** | `fix/static-color-handler-cleanup` | ✅ 6 parallel | None | `staticColorHandler.ts` only |
| **2** | `fix/music-mode-dep-array` | Solo | Wave 1 merged | imports `useProtocolDispatch` (modified in F-003/Wave 1) |

> AST output: `total_collisions: 1` (fix/protocol-dispatch-mtu-guard ↔ fix/music-mode-dep-array via `useProtocolDispatch.ts`). Wave 1: 6-parallel. Wave 2: 1 solo.

- [ ] **`fix/hw-settings-segments-haloz`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Fix `hardwareSettingsHandler.ts:124` — read `payload[5]` for segments in the classic 0x63 binary response branch instead of hardcoding `segments: 1`.
  - **Decision Log:** Audit VERIFIED (CRITICAL) — HALOZ devices (product_id=163, 2 segments) receive `hwSettings.segments=1` after classic 0x63 binary response, breaking the ring visualizer. JSON-inner branch correctly reads `payload[5]`; binary branch does not.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-001, CRITICAL) · Plan: [PLAN-fix-hw-settings-segments-haloz.md](./plans/PLAN-fix-hw-settings-segments-haloz.md)
    Key finding: `hardwareSettingsHandler.ts:124` — `segments: 1` hardcoded while `payload[5]` holds the real value; JSON-inner branch (line 104) reads it correctly.
    Rejected alternative: Reading only in JSON branch — rejected; binary response is the primary path for HALOZ controllers.
  - **Source of Truth:** 📖 [hardwareSettingsHandler.ts:124](src/protocols/handlers/hardwareSettingsHandler.ts#L124)
  - **Details:** Phase 1 (mandatory pre-edit): audit full hwSettings pipeline for additional hardcoding (8 files). Phase 2: surgical fix at line 124.

- [ ] **`fix/dispatcher-padding-dead-code`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Remove unreachable `padStaticColorfulPayload` call in `BleWriteDispatcher.ts:51`; update comment to locate the real minimum-pixel enforcement at `staticColorHandler.setMultiColor:44`.
  - **Decision Log:** Audit VERIFIED (MEDIUM) — V2-wrapped payloads always start with `0x00`; the `payload[0] !== 0x59` guard permanently fires and returns unchanged. Real defense is `Math.max(12, ...)` in `setMultiColor:44`.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-002, MEDIUM) · Plan: [PLAN-fix-dispatcher-padding-dead-code.md](./plans/PLAN-fix-dispatcher-padding-dead-code.md)
    Key finding: `padStaticColorfulPayload` reads `payload[0]` but V2 wrapping happens upstream — guard is permanently true, call is dead code.
    Rejected alternative: Moving guard pre-wrap — rejected; enforcement is stronger at the source (`setMultiColor`).
  - **Source of Truth:** 📖 [BleWriteDispatcher.ts:51](src/services/BleWriteDispatcher.ts#L51)
  - **Details:** One-line removal + comment update. Zero runtime behavioral change.

- [ ] **`fix/protocol-dispatch-mtu-guard`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Add 0x51 MTU interception inside `_dispatchToDevices` (useProtocolDispatch) to prevent silent GATT drops of 323-byte 0x51 extended payloads via `executeProtocolResults`.
  - **Decision Log:** Audit VERIFIED (HIGH) — `executeProtocolResults → _executeProtocolResultsInternal` has no MTU check; `prepareForTransmission._mtu` is unused. A 323-byte characteristic write silently drops on all real MTUs. Fix mirrors existing guard in `executeRawPayload:114`.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-003, HIGH) · Plan: [PLAN-fix-protocol-dispatch-mtu-guard.md](./plans/PLAN-fix-protocol-dispatch-mtu-guard.md)
    Key finding: `_dispatchToDevices` routes 0x51 payloads to `executeProtocolResults` with no size check; both safe paths (`writeToDevice`, `executeRawPayload`) already guard — only this path is missing.
    Rejected alternative: Fixing `_executeProtocolResultsInternal` directly — rejected; fix belongs at the API layer.
  - **Source of Truth:** 📖 [useProtocolDispatch.ts:20](src/hooks/useProtocolDispatch.ts#L20)
  - **Details:** Add has-oversized-0x51 check + writeChunked routing before the existing `executeProtocolResults` call. Add `writeChunked` to dep array.

- [ ] **`fix/adapter-chunking-comment`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Replace false "prepareForTransmission() will automatically apply 0x40 fragmentation" JSDoc in `ZenggeAdapter.ts:182-183` with accurate chunking responsibility map.
  - **Decision Log:** Audit VERIFIED (HIGH) — `prepareForTransmission` takes `_mtu` (underscore-prefixed, intentionally unused) and returns result unchanged. False comment is the primary mechanism allowing F-003 to survive code review.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-004, HIGH) · Plan: [PLAN-fix-adapter-chunking-comment.md](./plans/PLAN-fix-adapter-chunking-comment.md)
    Key finding: `prepareForTransmission` `_mtu` parameter is unused; function is a pass-through.
    Rejected alternative: Implementing real chunking in `prepareForTransmission` — rejected; chunking belongs at the dispatcher layer where MTU context is available.
  - **Source of Truth:** 📖 [ZenggeAdapter.ts:182](src/protocols/ZenggeAdapter.ts#L182)
  - **Details:** Comment-only change. Zero logic impact. Unblocks safe code review of F-003.

- [ ] **`fix/settled-mode-direction`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Fix direction byte inversion in `dynamicEffectHandler.setSettledMode:52` — `(direction === 1 ? 0 : 1)` → `(direction === 1 ? 1 : 0)`.
  - **Decision Log:** Audit VERIFIED (MEDIUM) — Protocol Bible §0x41: dir=0=forward, dir=1=reverse. Current code sends the opposite byte. Function is `@DEPRECATED` / DiagnosticLab only; no production callers.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-005, MEDIUM) · Plan: [PLAN-fix-settled-mode-direction.md](./plans/PLAN-fix-settled-mode-direction.md)
    Key finding: `(direction === 1 ? 0 : 1)` ternary inverts the bit; Protocol Bible confirms 1=reverse.
    Rejected alternative: Removing the deprecated function — rejected; deprecation doesn't justify removal in this scope.
  - **Source of Truth:** 📖 [dynamicEffectHandler.ts:52](src/protocols/handlers/dynamicEffectHandler.ts#L52)
  - **Details:** One-line ternary fix. Deprecated function; no production impact.

- [ ] **`fix/static-color-handler-cleanup`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[✅ L-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:1]`
  - **Goal:** Fix stale speed-range comment (lines 51-53) + `any` type violation (line 3) in `staticColorHandler.ts`.
  - **Decision Log:** Audit VERIFIED — F-006 (MEDIUM): comment claims hardware range 1-31 but 0xA3 chipset accepts 1-100 (oracle-confirmed; `ANIM_SPEED_MAX=100` in code is correct). F-008 (LOW): `let _appLogger: any` violates No-`any` Law; structural `AppLoggerLike` type resolves circular import.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-006 + F-008, MEDIUM+LOW combined) · Plan: [PLAN-fix-static-color-handler-cleanup.md](./plans/PLAN-fix-static-color-handler-cleanup.md)
    Key finding: Stale comment misleads speed debugging; `any` cast active in production.
    Rejected alternative: `// @ts-ignore` for logger type — rejected per No-`any` Law.
  - **Source of Truth:** 📖 [staticColorHandler.ts:3](src/protocols/handlers/staticColorHandler.ts#L3) + [staticColorHandler.ts:51](src/protocols/handlers/staticColorHandler.ts#L51)
  - **Details:** Same file — combined worktree. Two changes: narrow `any` type + correct speed comment.

- [ ] **`fix/music-mode-dep-array`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[M-RISK]` `[🍪 Snack]` `[🧠 FOCUSED]` `[BATCH:fix/protocol-audit]` `[WAVE:2]` `[⛔ BLOCKED BY: fix/protocol-audit Wave 1]`
  - **Goal:** Add `handleMusicChange` to `useEffect` dep array in `useMusicMode.ts:116` to prevent stale 0x73 config when `micSensitivity` or `brightness` change programmatically.
  - **Decision Log:** Audit VERIFIED (MEDIUM) — `handleMusicChange` useCallback (deps include sensitivity, brightness) is recreated on those changes, but the re-send useEffect holds a stale closure. Impact: programmatic brightness/sensitivity changes (session restore, parent state) dispatch stale 0x73 to hardware.
  - **Analysis:** 📊 Protocol Defect Audit 2026-06-24 (F-007, MEDIUM) · Plan: [PLAN-fix-music-mode-dep-array.md](./plans/PLAN-fix-music-mode-dep-array.md)
    Key finding: `useEffect` dep array at line 116 missing `handleMusicChange`; `micSensitivity`/`brightness` changes produce stale closure.
    Rejected alternative: Adding sensitivity+brightness directly — rejected; adding `handleMusicChange` is the complete transitive fix.
  - **Source of Truth:** 📖 [useMusicMode.ts:116](src/hooks/useMusicMode.ts#L116)
  - **Details:** One dep added to the array. Wave 2 because `useMusicMode` imports `useProtocolDispatch` (modified in Wave 1 F-003).

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

- [x] **`fix/crew-broadcast-scene`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:crew-e2e]` `[WAVE:1]`
  - **Plan:** 📎 [PLAN-fix-crew-broadcast-scene.md](./plans/PLAN-fix-crew-broadcast-scene.md)
  - **Goal:** Repair leader→member light sync end-to-end. Delete the dead `broadcastScene`/`onCrewSceneChange` path; expose+wire `broadcastPayload` as the leader broadcast; fix the member receiver to route the `number[]` payload via a new `applyCrewPayload` handle into `writeToDevice` (not `applyCloudScene`).
  - **Decision Log:** Reyes VERIFIED (HIGH) — broadcastScene is a no-op AND broadcastPayload had zero callers AND member receiver type-mismatched. User chose Scope A (full repair). See docs/analysis/crew-broadcast-scene-redundancy.md.
  - **Source of Truth:** PLAN-fix-crew-broadcast-scene.md §Files to Create/Modify (7 files: CrewRealtime.ts, CrewService.ts, types.ts, useCrewLeaderBroadcast.ts [delete], DockedController.tsx, useDashboardController.tsx, DashboardScreen.tsx).

- [x] **`fix/crew-membership-presence`**
  - **Tags:** `[✅ READY]` `[✅ VERIFIED]` `[DATA]` `[M-RISK]` `[🍱 Meal]` `[🧠 FOCUSED]` `[BATCH:crew-e2e]` `[WAVE:2]`
  - **Plan:** 📎 [PLAN-fix-crew-membership-presence.md](./plans/PLAN-fix-crew-membership-presence.md)
  - **Goal:** Leader sees members live + every member renders with real name/avatar. Emit the missing `member_update` broadcast on join/leave; wire the two `() => {}` leader callbacks to refresh member UI; rewrite the `CrewMemberDashboard` query off the non-existent `role` column + `user_profiles` implicit join (use existing `crew_members.display_name` + derived role + explicit avatar query). NO migration needed.
  - **Decision Log:** Reyes VERIFIED — 3 sub-bugs (no member_update sender, no-op callbacks, schema-mismatch query). Quinn recommends query-rewrite over migration (session table `crew_members` correctly has no role column; persistent `crew_memberships` is a different table). See docs/analysis/crew-subsystem-e2e-audit.md Flow 4.
  - **Source of Truth:** PLAN-fix-crew-membership-presence.md §Files to Create/Modify (6 files: CrewSessionManager.ts, CrewService.ts, useCrewSession.ts, DashboardCrewPanel.tsx, useDashboardCrew.ts, CrewMemberDashboard.tsx).

- [x] **`feat/crew-scheduled-server-side`** — merged `320bfd50` ✅ (⚠️ deferred Supabase deploy — see SESSION_LOG)
  - **Tags:** `[x]` `[✅ SPIKE CLEARED]` `[CLOUD]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🤖 PRO-HIGH]` `[BATCH:crew-e2e]` `[WAVE:3]`
  - **Plan:** 📎 [PLAN-feat-crew-scheduled-server-side.md](./plans/PLAN-feat-crew-scheduled-server-side.md)
  - **⛔ BLOCKED (Rule 5 — Unverified Task Spike Gate):** Run the SPIKE before execution — confirm (1) `pg_cron`+`pg_net` available on the Supabase project, (2) `CRON_SECRET` + service-role env in the edge runtime, (3) `crew_sessions.is_active` insert default. If pg_cron is unavailable the activation mechanism must be redesigned.
  - **Goal:** Server-side scheduled-crew activation: a `pg_cron` job (1-min cadence) → new edge function `activate-scheduled-crews` that flips due `status='scheduled'` sessions to active (service-role, idempotent) and sends Expo push to members; client join error-path + 15-min reminder + notification deep-link.
  - **Decision Log:** User chose server-side activation 2026-06-22. Reyes found scheduling entirely cosmetic (no activation; members can't join scheduled rows). Quinn chose pg_cron+pg_net→edge function (reuses proven push-batch code; cron can't reuse the JWT-gated notify-crew-session fn). See docs/analysis/crew-subsystem-e2e-audit.md Flow 2.
  - **Source of Truth:** PLAN-feat-crew-scheduled-server-side.md §Files to Create/Modify (2 NEW: activate-scheduled-crews/index.ts, 20260622000000_activate_scheduled_crews_cron.sql; + CrewSessionManager.ts, CrewScheduleScreen.tsx, useDashboardProfile.ts, DashboardScreen.tsx).

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

- [x] **`refactor/burn-down-audit-failures`** — SUPERSEDED ✅ work already in master via subsequent development
  - **Tags:** `[x]` `[AUTH]` `[✅ L-RISK]` `[🍱 Meal]` `[BATCH:branch-salvage]` `[WAVE:1]`
  - **Decision Log (2026-06-23 — CLOSED):** Attempted gatekeeper merge. Rebase revealed `CrewService.ts` was deleted in master (extracted to modular `src/services/CrewService/` directory). Grep confirmed ALL goals already achieved: zero rogue `supabase.auth.getUser()` in services/hooks, `bleGateRef` gone, `AuthContext` has all 5 auth methods with proper Supabase types (better than branch's `unknown` types). Merging would be a regression. Branch `185d41d0` deleted. Wave 2 unblocked — no `useBLEScanner.ts` collision concern remains.

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






