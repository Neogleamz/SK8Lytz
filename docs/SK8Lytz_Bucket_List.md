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


## 🚧 ACTIVE SPRINT
*(Empty — ready for intake)*
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

### [BATCH:deepdive-sweep] — `deepdive-sweep-batch` — READY
> **Worktree Strategy**: Wave 1 (7 tasks fully parallel) -> Wave 2 (4 tasks fully parallel) -> Wave 3 (1 task) -> Wave 4 (2 tasks parallel) -> Wave 5 (1 task) -> Wave 6 (1 task)
> **Type**: Multi-wave parallel sweep — AST collision-verified
> **Prerequisite**: None — Wave 1 is parallel-safe with current master HEAD
> **Source Analysis**: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — 47-agent deepdive fleet; 456 verified findings across 16 domain clusters; 42 AST collision pairs detected

#### Batch Strategy Table
| Wave | Task Slug | Worktree | Key Files | Prerequisite |
|---|---|---|---|---|
| 1 | `chore/sweep-cloud-supabase` | `sweep-cloud-supabase` | `supabase/migrations/` (6 files) + `supabase/functions/notify-crew-session/` | None |
| 1 | `chore/sweep-devops-tooling` | `sweep-devops-tooling` | `tools/fortress-gatekeeper.ps1`, `tools/auto-archiver.js`, `tools/sentinel/`, `.husky/pre-commit` | None |
| 1 | `chore/sweep-protocol-core` | `sweep-protocol-core` | `src/protocols/ZenggeAdapter.ts`, `src/protocols/ZenggeProtocol.ts`, `src/hooks/useProtocolBuilder.ts` | None |
| 1 | `chore/sweep-ui-screens-dashboard` | `sweep-ui-screens-dashboard` | `src/screens/DashboardScreen.tsx`, `src/components/dashboard/` (5 files) | None |
| 1 | `chore/sweep-ui-visualizer-patterns` | `sweep-ui-visualizer-patterns` | `src/components/patterns/`, `src/components/CustomEffectVisualizer.tsx`, `src/components/NeonHueStrip.tsx` | None |
| 1 | `chore/sweep-os-permissions-manifests` | `sweep-os-permissions-manifests` | `android/app/src/main/AndroidManifest.xml`, `app.config.js` | None |
| 1 | `chore/sweep-native-watch` | `sweep-native-watch` | `targets/watch/WatchConnectivityManager.swift`, `android/sk8lytzWear/` (2 Kotlin files) | None |
| 2 | `chore/sweep-ble-core-dispatch` | `sweep-ble-core-dispatch` | `src/services/BleWriteDispatcher.ts`, `src/services/ble/` (5 files), `src/hooks/useBLE.ts`, `src/hooks/ble/` (2 files), `src/hooks/useControllerDispatch.ts` | Wave 1 merged |
| 2 | `chore/sweep-admin-telemetry` | `sweep-admin-telemetry` | `src/components/admin/` (6 files) | Wave 1 merged |
| 2 | `chore/sweep-storage-keys` | `sweep-storage-keys` | `src/hooks/useFavorites.ts`, `src/services/AppSettingsService.ts`, `src/services/DeviceRepository.ts`, `src/components/docked/QuickPresetModal.tsx` | Wave 1 merged |
| 2 | `chore/sweep-ui-modals-shared` | `sweep-ui-modals-shared` | `src/components/DeviceSettingsModal.tsx`, `src/components/GroupSettingsModal.tsx`, `src/components/SessionSummaryModal.tsx`, `src/components/account/types.ts` (+ 3 more) | Wave 1 merged |
| 3 | `chore/sweep-identity-auth` | `sweep-identity-auth` | `src/services/ProfileService.types.ts`, `src/services/AppLogger.ts`, `src/services/AuthProfileService.ts`, `src/hooks/useAccountOverview.ts`, `src/components/AccountModal.tsx` | Wave 2 merged |
| 4 | `chore/sweep-group-sync` | `sweep-group-sync` | `src/services/GroupRepository.ts`, `src/services/CrewService.ts`, `src/hooks/useCrewHub.ts`, `src/hooks/useCrewSession.ts`, `src/components/crew/` (2 files) + 4 more | Wave 3 merged |
| 4 | `chore/sweep-session-context` | `sweep-session-context` | `src/services/SpeedTrackingService.ts`, `src/services/ScenesService.ts`, `src/services/GradientsService.ts`, `src/context/SessionContext.tsx`, `src/hooks/useTelemetryLedger.ts`, `src/hooks/useDeviceStateLedger.ts` | Wave 3 merged |
| 5 | `chore/sweep-shared-utils` | `sweep-shared-utils` | `src/components/PositionalGradientBuilder.tsx`, `src/components/CustomSlider.tsx`, `src/services/LocationService.ts`, `src/utils/validation.ts` (new) + 3 more | Wave 4 merged |
| 6 | `chore/sweep-ui-docked-controller` | `sweep-ui-docked-controller` | `src/components/docked/UniversalSlidersFooter.tsx`, `src/components/docked/BuilderPanel.tsx`, `src/hooks/useStreetMode.ts`, `src/hooks/useDashboardController.tsx`, `src/components/DockedController.tsx` + 3 more | Wave 5 merged |

---

