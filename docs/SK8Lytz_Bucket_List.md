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

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE

- [ ] **`fix/stale-flush-group-kill`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[BLE]` `[⚠️ H-RISK]` `[🍪 Snack]` `[🧠 THINK]`
  - **Goal:** Remove the stale flush in ConnectService that kills Device A when Device B connects in a separate auto-connect batch, breaking group assembly.
  - **Decision Log:** User reported (2026-06-17) only 1 of 2 grouped devices connects despite strong RSSI on both. Code trace confirmed ConnectService L77 marks Device A as "stale" when Device B arrives in a separate batch. Stale flush was designed for fleet switching but has no legitimate caller — fleet switching is handled by DISCONNECT_REQUEST.
  - **Analysis:** 📊 Source: Live user testing 2026-06-17 + subagent code trace across 8 files. Plan: [PLAN-fix-stale-flush-group-kill.md](./plans/PLAN-fix-stale-flush-group-kill.md)
    Key finding: "ConnectService stale flush disconnects Device A when Device B's batch arrives because A is not in B's targetMacs"
    Rejected alternative: "Merge all group MACs into a single batch — rejected because BLE discovery timing is non-deterministic"
  - **Source of Truth:** 📖 [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L77-L107) §Stale Flush
  - **Details:** 2 files, ~15 lines. BLE connection pipeline — wrong fix risks breaking all GATT connections.



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
