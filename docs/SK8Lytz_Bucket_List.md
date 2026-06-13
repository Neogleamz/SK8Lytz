# SK8Lytz Master Bucket List

> Ã¢Å¡Â Ã¯Â¸Â AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## Ã°Å¸â€œÅ  Global System Readiness

---

## Ã°Å¸â€Â´ CRITICAL: Ã°Å¸â€ºÂ¡Ã¯Â¸Â Performance, Stability & Security



- [ ] **`chore/session-service-test-coverage`**
  - **Tags:** `[Ã¢Å“â€¦ READY]` `[Ã°Å¸Â¤â€“ INFERRED]` `[Ã°Å¸Â§Âª LAB]` `[Ã¢Å“â€¦ L-RISK]` `[Ã°Å¸ÂÂ± Meal]` `[Ã°Å¸Â§Â  MEDIUM]`
  - **Goal:** Add substantive unit tests for SensorService, HealthService, and NotificationService Ã¢â‚¬â€ the 3 untested session actor services from the post-merge audit.
  - **Decision Log:** Post-merge audit 2026-06-11: 5 of 9 session service files have zero test coverage. SensorService is highest risk (GPS + accelerometer + crewService side effects). NotificationService ENDING-state button logic newly added in session-machine-actor-types task Ã¢â‚¬â€ needs test coverage for the 3-branch action logic.
  - **Analysis:** Ã°Å¸â€œÅ  Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) Ã‚Â· Plan: [PLAN-chore-session-service-test-coverage.md](./plans/PLAN-chore-session-service-test-coverage.md)
    Key finding: "44% test file coverage by file count; SensorService has GPS+accelerometer+crewService coupling and zero tests"
    Rejected alternative: "Skip until feature work" Ã¢â‚¬â€ test gaps in newly architected services compound quickly
  - **Source of Truth:** Ã°Å¸â€œâ€“ [AutoPauseService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/AutoPauseService.test.ts) reference pattern Ã‚Â· [SensorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SensorService.ts) Ã‚Â· [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts) Ã‚Â· [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
  - **Details:** Must run AFTER `fix/session-machine-actor-types` merges Ã¢â‚¬â€ NotificationService test for ENDING branch depends on the ENDING fix being in place. 3 new test files, no production source changes.

### Ã¢Å¡Â¡ [BATCH:doc-pipeline-sync] Ã¢â‚¬â€ `doc-pipeline-sync-batch` Ã¢â‚¬â€ READY
> **Worktree Strategy**: All 4 tasks fully parallel (tools/*.md files only Ã¢â‚¬â€ zero TypeScript overlap)
> **Type**: Parallel Ã¢â‚¬â€ all [WAVE:1]
> **Prerequisite**: None Ã¢â‚¬â€ AST confirmed zero import-tree overlap with session-xstate-engine Wave 3
> **Source Analysis**: Ã°Å¸â€œÅ  [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Ã¢â‚¬â€ Full doc ecosystem audit: 6 confirmed gaps after Wave 1+2 merges; Phase 4 of deepdive-docs was silently skipped; KB missing XState entry; 3 ADRs in SESSION_LOG never promoted; INDUSTRY_BENCHMARKS duplicated; TEST_PLAN pre-XState

#### Batch Strategy Table
| Wave | Task Slug | Files Touched | Prerequisite |
|---|---|---|---|
| 1 | `docs/cartographer-rebuild-and-harden` | `tools/SK8Lytz_App_Master_Reference.md` + Tier-3 satellite docs + 3 workflow files | None |
| 1 | `docs/xstate-v5-kb-capture` | `tools/knowledge-base/patterns/xstate-v5-patterns.md` + `tools/knowledge-base/INDEX.md` | None |
| 1 | `fix/industry-benchmarks-dedup` | `tools/INDUSTRY_BENCHMARKS.md` | None |
| 1 | `docs/test-plan-session-machine` | `tools/SK8Lytz_TEST_PLAN.md` | None |

## Ã°Å¸Å¡â€˜ TRIAGE QUEUE

- [ ] **fix/crash-telemetry-severity-check**
  - **Goal:** Fix check constraint violation on `crash_telemetry` table severity insert.
  - **Details:** `AppLogger.ts` inserts `severity: 'CRITICAL'` which is not in the allowed CHECK constraint list `('FATAL', 'ERROR', 'WARN', 'INFO')`. Fix `AppLogger.ts` to map `CRITICAL` events to `'FATAL'` or `'ERROR'`.
  - **Files:** `src/services/AppLogger.ts`
  - **Tags:** [Ã°Å¸Å¡â€˜ TRIAGE QUEUE] [Ã¢Å“â€¦ READY] [Ã¢Å¡â„¢Ã¯Â¸Â SERVICES] [L-RISK] [Snack] [L-COGNITIVE]
  - **Source of Truth:** C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\AppLogger.ts#L502
  - **Decision Log (2026-06-13):** Found during Supabase log audit. Check constraint restricts severity to FATAL/ERROR/WARN/INFO, but code sends CRITICAL.


## Ã°Å¸Å¡Â§ ACTIVE SPRINT
> Currently executing: **`feat/harden-ble-regression-shields`**
> Completed: N/A




## Ã°Å¸â€Âµ LOW: Ã¢Å“Â¨ New Features & UI Enhancements

_User-facing product value and UI refinements._

- [ ] `feat/app-wide-ux-tips` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 12k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã°Å¸Â¤â€“ FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Contextual tips system for key friction points. Ã¢â€ â€™ [Plan](docs/plans/feat-app-wide-ux-tips.md)

---

## Ã°Å¸â€Â¥ ON DECK


### Ã¢Å¡Â¡ [BATCH:session-xstate-engine] Ã¢â‚¬â€ `session-xstate-engine-batch` Ã¢â‚¬â€ READY
> **Worktree Strategy**: Sequential waves (W0Ã¢â€ â€™W1Ã¢â€ â€™W2), then W3A+W3B+W3C fully parallel  
> **Type**: Mixed Ã¢â‚¬â€ Spike Ã¢â€ â€™ Sequential Ã¢â€ â€™ Parallel  
> **Prerequisite**: None Ã¢â‚¬â€ board is clear  
> **Source Analysis**: Ã°Å¸â€œÅ  [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) + [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) Ã¢â‚¬â€ 10 confirmed session sync bugs traced to single root cause: no XState state authority for session lifecycle

#### Batch Strategy Table
| Wave | Task Slug | Worktree | Files Touched | Prerequisite |
|---|---|---|---|---|
| W0 | `spike/wear-os-bridge-field` | `spike-wear-os-bridge` | Read-only | None |
| W1 | `feat/session-services-layer` | `session-services-layer` | 9 new files in `src/services/session/` + `src/components/session/` | W0 confirmed |
| W2 | `refactor/session-context-xstate` | `session-context-xstate` | `SessionContext.tsx` only | W1 merged |
| W3A | `refactor/delete-legacy-hooks` | `delete-legacy-hooks` | `useGlobalTelemetry.ts` (DEL) + `useHealthTelemetry.ts` (DEL) + `App.tsx` | W2 merged |
| W3B | `feat/session-phase-badge-ui` | `session-phase-badge-ui` | `DashboardTelemetryHero.tsx` + `LiveTelemetryHUD.tsx` + `DockedController.tsx` (1 line) | W2 merged |
| W3C | `fix/session-bug-fixes` | `session-bug-fixes` | `StreetPanel.tsx` + `AccountTabStats.tsx` + `android/sk8lytzWear/` | W2 merged |

---


## Ã¢Ââ€žÃ¯Â¸Â Icebox / Backburner (Manual Trigger Only)

### Ã°Å¸Å½Âµ Epic: Music Mode

- [ ] `feat/music-intel-phase-1` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 50k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã°Å¸Â§Â  THINK] [Spotify Sync] Ã¢â‚¬â€ OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Media Access] Ã¢â‚¬â€ Android MediaSession detection (YouTube, Pandora, etc.). Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [Ã°Å¸Â§Âª LAB] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Live Rink Mode] Ã¢â‚¬â€ ShazamKit/ACRCloud periodic background scanning (45s). Ã¢â€ â€™ [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Apple Music] Ã¢â‚¬â€ MusicKit integration for native iOS BPM. Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 45k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã¢â€ºâ€ BLOCKED BY feat/music-intel-phase-1] [Ã°Å¸Â§Â  THINK] [Crew Party Sync] Ã¢â‚¬â€ Master BPM Choreography Engine with Realtime crew sync. Ã¢â€ â€™ [Plan](docs/plans/feat-music-integration-master.md)

- [ ] `feat/google-oauth-integration` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 30k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸â€œâ€¦ 2026-04-14] [Ã°Å¸Â§Â  THINK] Integrate Google OAuth as an auth provider. (Requires Google Cloud Console setup + Supabase config). Ã¢â€ â€™ [Plan](docs/plans/feat-google-oauth-integration.md)
- [ ] `feat/spatial-beat-mapping` : [Ã°Å¸Â§Âª LAB] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 18k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â§Â  THINK] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). Ã¢â€ â€™ [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Transform Dashboard into palette-synced dynamic backgrounds. Ã¢â€ â€™ [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [Ã°Å¸Â§Âª LAB] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 10k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Pattern selection (Strobe, Blink, Static) + music slider fix. Ã¢â€ â€™ [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/kinetic-brake-lights` : [Ã°Å¸Â§Âª LAB] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â§Â  THINK] [Pillar 12] Kinetic Safety Ã¢â‚¬â€ phone accelerometer pulse RED for braking. Ã¢â€ â€™ [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸Â¥Â© Feast] [Ã°Å¸Âªâ„¢ 30k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [Ã°Å¸Â§Â  THINK] Geofence-based 'Hive Mind' synchronization. Ã¢â€ â€™ [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 8k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 25k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å¡Â Ã¯Â¸Â H-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 20k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â§Â  THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [Ã¢Å“â€¦ L-RISK] [Ã°Å¸ÂÂ± Meal] [Ã°Å¸Âªâ„¢ 12k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [Ã°Å¸Â¤â€“ FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Card Swipe Navigation.

