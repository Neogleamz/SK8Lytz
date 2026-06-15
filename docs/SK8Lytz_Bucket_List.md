# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

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
### ⚡ [BATCH:WAVE-1] — `wave-1-sweep` — READY
> **Worktree**: `wave-1-sweep` · **Type**: Parallel · **Prerequisite**: None
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — Deep-Dive Code Hunt orthogonal analysis

### ⚡ [BATCH:WAVE-6] — `wave-6-sweep` — IN PROGRESS
> **Worktree**: `wave-6-sweep` · **Type**: Parallel · **Prerequisite**: Wave 5 merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — AST-Verified Extraction Foundation

- [ ] **`extract-and-sweep-DeviceRepository.ts`**
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:WAVE-7]` `[WAVE:7]`
  - **Goal:** Extract logic and resolve violations in `DeviceRepository.ts` (>30KB monolith).
  - **Decision Log:** S4 Monolith limit prevents direct edits. AST analysis confirms it depends on AppLogger, requiring Wave 7 placement.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-extract-and-sweep-DeviceRepository.ts.md](./plans/PLAN-extract-and-sweep-DeviceRepository.ts.md)
    Key finding: "Monolith >39KB. Depends on AppLogger. Extract into `src/services/deviceRepository/`."
  - **Source of Truth:** 📖 [PLAN-extract-and-sweep-DeviceRepository.ts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-extract-and-sweep-DeviceRepository.ts.md)
  - **Details:** Dependent layer extraction. `Prerequisite: Wave 6 fully merged into master before this worktree is created.`

- [ ] **`extract-and-sweep-CrewService.ts`**
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[⚠️ H-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:WAVE-7]` `[WAVE:7]`
  - **Goal:** Extract logic and resolve violations in `CrewService.ts` (>30KB monolith).
  - **Decision Log:** S4 Monolith limit prevents direct edits. AST analysis confirms it depends on AppLogger, requiring Wave 7 placement.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-extract-and-sweep-CrewService.ts.md](./plans/PLAN-extract-and-sweep-CrewService.ts.md)
    Key finding: "Monolith >31KB. Depends on AppLogger. Extract into `src/services/crewService/`."
  - **Source of Truth:** 📖 [PLAN-extract-and-sweep-CrewService.ts.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-extract-and-sweep-CrewService.ts.md)
  - **Details:** Dependent layer extraction. `Prerequisite: Wave 6 fully merged into master before this worktree is created.`

- [ ] **`sweep-src-other`**
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[✅ L-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:WAVE-7]` `[WAVE:7]`
  - **Goal:** Resolve 56 telemetry and architectural rule violations in the other domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-other.md](./plans/PLAN-sweep-src-other.md)
    Key finding: "56 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-other.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-other.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 6 fully merged into master before this worktree is created.`

### ⚡ [BATCH:WAVE-8] — `wave-8-sweep` — READY
> **Worktree**: `wave-8-sweep` · **Type**: Parallel · **Prerequisite**: Wave 7 merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — Deep-Dive Code Hunt orthogonal analysis

- [ ] **`sweep-src-screens`**
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[✅ L-RISK]` `[🥩 Feast]` `[🧠 THINK]` `[BATCH:WAVE-8]` `[WAVE:8]`
  - **Goal:** Resolve 35 telemetry and architectural rule violations in the screens domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-screens.md](./plans/PLAN-sweep-src-screens.md)
    Key finding: "35 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-screens.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-screens.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 7 fully merged into master before this worktree is created.`

- [ ] **`sweep-src-components-docked`**
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[🚧 M-RISK]` `[🍱 Meal]` `[🧠 THINK]` `[BATCH:WAVE-8]` `[WAVE:8]`
  - **Goal:** Resolve 10 telemetry and architectural rule violations in the components-docked domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-components-docked.md](./plans/PLAN-sweep-src-components-docked.md)
    Key finding: "10 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-components-docked.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-docked.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 7 fully merged into master before this worktree is created.`

### ⚡ [BATCH:WAVE-9] — `wave-9-sweep` — READY
> **Worktree**: `wave-9-sweep` · **Type**: Parallel · **Prerequisite**: Wave 8 merged
> **Source Analysis**: 📊 [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — Deep-Dive Code Hunt orthogonal analysis

- [ ] **`sweep-src-components-AccountModal.tsx`**
  - **Tags:** `[✅ READY]` `[🔍 CONFIRMED]` `[🧹 TECH DEBT]` `[🚧 M-RISK]` `[🍪 Snack]` `[🧠 THINK]` `[BATCH:WAVE-9]` `[WAVE:9]`
  - **Goal:** Resolve 2 telemetry and architectural rule violations in the components-AccountModal.tsx domain.
  - **Decision Log:** Findings surfaced during the deep-dive orthogonal sweep required immediate resolution before proceeding with features.
  - **Analysis:** 📊 Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) · Plan: [PLAN-sweep-src-components-AccountModal.tsx.md](./plans/PLAN-sweep-src-components-AccountModal.tsx.md)
    Key finding: "2 specific rule violations located and deduplicated in this cluster."
    Rejected alternative: "Grouping by rule instead of domain (creates worktree file collisions)."
  - **Source of Truth:** 📖 [PLAN-sweep-src-components-AccountModal.tsx.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/plans/PLAN-sweep-src-components-AccountModal.tsx.md)
  - **Details:** Orthogonal cluster safe for parallel verification. `Prerequisite: Wave 8 fully merged into master before this worktree is created.`

---

##  ❄️ Icebox / Backburner (Manual Trigger Only)

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


