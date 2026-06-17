# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

### 🚑 TRIAGE QUEUE
- [ ] **`fix/ble-group-sync-debounce`**
  - **Tags:** `[✅ READY]` `[🧪 LAB]` `[⚠️ H-RISK]` `[🍪 Snack]` `[BLE]`
  - **Goal:** Fix the global debouncer bug dropping sequential BLE writes to grouped skates and restore missing color dots.
  - **Decision Log:** Users report group writes only affecting a single skate due to a global debounce timer overwriting concurrent requests in BleWriteDispatcher.
  - **Analysis:** 📊 Plan: [PLAN-fix-ble-group-sync-debounce.md](./plans/PLAN-fix-ble-group-sync-debounce.md)
  - **Source of Truth:** 📖 [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
---

### 🌊 Parallel Wave Strategy (AST-Verified)

| Wave | Task Clusters | Parallel-Safe? | Prerequisite |
|------|--------------|---------------|-------------------|
| 1 | sweep-src-components-TacticalSlider.tsx, sweep-src-components-LocationPicker.tsx, sweep-src-components-admin, sweep-src-hooks-ble, sweep-src-components-CrewModal.tsx, sweep-src-components-CrewMemberDashboard.tsx, sweep-src-components-account, sweep-src-components-DockedController.tsx | ✅ Yes | None |
| 2 | sweep-src-components-NeonHueStrip.tsx, sweep-src-components-CameraTracker.tsx, sweep-src-components-patterns, sweep-src-components-VerticalPatternDrum.tsx, sweep-src-components-VisualizerUnit.tsx, sweep-src-components-CustomEffectVisualizer.tsx, sweep-src-utils, sweep-src-components-shared | ✅ Yes | Wave 1 merged |
| 3 | sweep-src-components-permissions, sweep-src-components-GlobalErrorBoundary.tsx | ✅ Yes | Wave 2 merged |
| 4 | sweep-src-components-crew, sweep-src-components-PositionalGradientBuilder.tsx, sweep-src-context, sweep-src-components-ConnectionStrengthBadge.tsx, sweep-src-components-ProductVisualizer.tsx | ✅ Yes | Wave 3 merged |
| 5 | sweep-src-services, sweep-src-components-CustomSlider.tsx | ✅ Yes | Wave 4 merged |
| 6 | sweep-src-hooks, sweep-src-components-dashboard, sweep-src-components-CommunityModal.tsx | ✅ Yes | Wave 5 merged |
| 7 | sweep-src-other | ✅ Yes | Wave 6 merged |
| 8 | sweep-src-screens, sweep-src-components-docked | ✅ Yes | Wave 7 merged |
| 9 | sweep-src-components-AccountModal.tsx | ✅ Yes | Wave 8 merged |

## 🚧 ACTIVE SPRINT
### ⚡ [BATCH:feat/command-center-perf-charts] — `feat/command-center-perf-charts` — IN PROGRESS
> **Worktree**: `feat/command-center-perf-charts` · **Type**: Isolated · **Prerequisite**: None
> **Source Analysis**: 📊 PLAN-feat-command-center-perf-charts.md

### ⚡ [BATCH:feat/deep-dive-fixes-wave1] — `feat/deep-dive-fixes-wave1` — IN PROGRESS
> **Worktree**: `feat/deep-dive-fixes-wave1` · **Type**: Unified Batch · **Prerequisite**: None
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/fb5fb761-e7be-4241-a902-3cb07dca3307/implementation_plan.md) — Fixes for BLE Protocol Corruption, Memory Leaks, and Unhandled Async Rejections.

### ⚡ [BATCH:fix/hal-parity-split-brain] — `fix/hal-parity-split-brain` — IN PROGRESS
> **Worktree**: `fix/hal-parity-split-brain` · **Type**: Isolated · **Prerequisite**: None
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/c32537a3-610e-4934-884a-37f7878eec17/implementation_plan.md) — Refactor of components bypassing HAL.

- [/] **`fix/hal-parity-split-brain`**
  - **Tags:** `[✅ READY]` `[🤔 INFERRED]` `[UI/BLE]` `[⚠️ H-RISK]` `[🍱 Meal]` `[M-COG]` `[BATCH:fix/hal-parity-split-brain]` `[WAVE:1]`
  - **Goal:** Migrate UI components bypassing `useProtocolDispatch` to use the Hardware Abstraction Layer instead of hardcoding protocol payloads.
  - **Decision Log:** Deep-dive audit found that UniversalTacticalSliders and BuilderPanel were constructing raw Zengge bytes and passing them directly to writeToDevice, causing mixed protocol (Zengge+BanlanX) groups to fall out of parity.
  - **Analysis:** 📊 Source: [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/c32537a3-610e-4934-884a-37f7878eec17/implementation_plan.md) · Plan: [PLAN-fix-hal-parity.md](./plans/PLAN-fix-hal-parity.md)
    Key finding: "Direct use of ZenggeProtocol in UI breaks multi-device heterogeneous grouping."
    Rejected alternative: "Leaving the components as-is (Rejected: breaks the core 'parity is law' directive)."
  - **Source of Truth:** 📖 [UniversalTacticalSliders.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalTacticalSliders.tsx#L69)
  - **Details:** Refactoring requires updating the inner dispatch closures and props in DockedController to use HAL methods.

---

## 🔥 ON DECK



### ⚡ [BATCH:feat/rich-os-notifications] — `feat/rich-os-notifications` — [✅ READY]
> **Worktree**: `feat/rich-os-notifications` · **Type**: Isolated · **Prerequisite**: None
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/a87e0851-7797-4ede-a5a4-e9e88d68809d/implementation_plan.md) — Industry benchmarking of top-tier apps (Strava, Spotify) and custom view capabilities.

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


