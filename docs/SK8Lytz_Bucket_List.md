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






