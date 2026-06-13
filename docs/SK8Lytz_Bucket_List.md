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

- [ ] **`chore/sweep-ble-core-dispatch`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[BLE]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Replace Promise.all concurrent BLE writes with serialized sequential dispatch, add re-entrancy guards to processQueue and battery sweep, and PII-scrub 9 raw MAC address leaks from telemetry logs.
  - **Decision Log:** 2 independent agents confirmed BleWriteDispatcher.ts:164 and :228 use Promise.all for concurrent characteristic writes — the Zengge controller has a single GATT characteristic; parallel writes cause GATT collisions and undefined controller state. Protocol Bible mandates strictly sequential delivery.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ble-core-dispatch.md](./plans/PLAN-sweep-ble-core-dispatch.md) — Key finding: "Promise.all at BleWriteDispatcher:164,:228 — concurrent GATT writes violate sequential write contract (2 agents confirmed)" — Rejected: "Retry on GATT collision" — serialization is the correct fix; retry masks root cause
  - **Source of Truth:** [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts#L164) · [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-admin-telemetry`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Meal]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Extract all inline keyExtractor/renderItem callbacks in admin FlatLists to stable useCallback refs, add 4-state UI matrices to 3 admin panels, and fix AppLogger telemetry context structure in 2 files.
  - **Decision Log:** Fleet found inline arrow functions for keyExtractor in every admin panel FlatList — these defeat FlatList virtualization causing full re-renders on every state update. AdminRosterPanel, HardwareBlacklistPanel, and FeatureFlagsPanel show blank screens on fetch failure with no error feedback.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-admin-telemetry.md](./plans/PLAN-sweep-admin-telemetry.md) — Key finding: "6 admin FlatLists with inline keyExtractor defeat virtualization; 3 panels missing error/empty states" — Rejected: "Memoize entire list component" — stable callback refs are the targeted correct fix
  - **Source of Truth:** [AdminRosterPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/AdminRosterPanel.tsx#L178) · [HardwareBlacklistPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/admin/tools/HardwareBlacklistPanel.tsx#L255)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-storage-keys`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[STORAGE]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Fix 3 AsyncStorage key collision bugs, consolidate all hardcoded @Sk8lytz_* key literals into the central STORAGE_KEYS registry, and flip AppSettingsService to write local cache first (offline-first mandate).
  - **Decision Log:** Fleet confirmed useFavorites and QuickPresetModal resolve favorites keys independently with conflicting logic — reads/writes from different code paths silently overwrite each other. DashboardScreen hardcodes '@Sk8lytz_Favorites' bypassing the registry; if the key is ever renamed the Dashboard silently reads nothing.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-storage-keys.md](./plans/PLAN-sweep-storage-keys.md) — Key finding: "@Sk8lytz_Favorites hardcoded in DashboardScreen:648; AppSettingsService blocks local cache behind network — violates offline-first mandate" — Rejected: "Document keys" — doesn't prevent future renames from silently breaking Dashboard
  - **Source of Truth:** [useFavorites.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useFavorites.ts#L33) · [AppSettingsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppSettingsService.ts#L91)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-ui-modals-shared`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Snack]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:2]`
  - **Goal:** Replace static color imports with useTheme() hook in 3 modal components, fix any-typed props in CommunityModal and MarqueeText, and break the circular dependency in account/types.ts.
  - **Decision Log:** Fleet confirmed DeviceSettingsModal and GroupSettingsModal import colors statically, bypassing useTheme() — these components are invisible to dark mode/theme switching. The account/types.ts circular import chain causes unpredictable module resolution order on hot reload.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-modals-shared.md](./plans/PLAN-sweep-ui-modals-shared.md) — Key finding: "DeviceSettingsModal and GroupSettingsModal ignore dark mode — static color import bypasses useTheme()" — Rejected: "Pass colors as props" — props threading for theme is an antipattern; hook consumption is correct
  - **Source of Truth:** [DeviceSettingsModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DeviceSettingsModal.tsx#L7) · [account/types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/types.ts)
  - **Details:** Prerequisite: Wave 1 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-identity-auth`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[AUTH]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:3]`
  - **Goal:** Fix notif_preferences: any in core profile type, remove direct supabase.auth.getUser() from AppLogger, and fix 5 AppLogger telemetry context structure errors in auth layer.
  - **Decision Log:** Fleet confirmed ProfileService.types.ts:21 declares notif_preferences: any — this core type field poisons every component that consumes the profile type. AppLogger.ts:674 fires a live supabase.auth.getUser() network call on every log flush cycle, adding latency and failing silently when offline.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-identity-auth.md](./plans/PLAN-sweep-identity-auth.md) — Key finding: "notif_preferences: any in core type; AppLogger fires live auth network call on every log flush" — Rejected: "Cast to unknown instead of any" — still loses type information; proper interface required
  - **Source of Truth:** [ProfileService.types.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ProfileService.types.ts#L21) · [AppLogger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts#L674)
  - **Details:** Prerequisite: Wave 2 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-group-sync`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[CLOUD]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Eliminate 4 as-any type casts in GroupRepository and CrewService DB access paths, fix stale closure in useCrewProximityRadar, add 4-state UI to Crew screens, and PII-scrub 3 raw user data leaks.
  - **Decision Log:** Fleet confirmed 4x as-any on GroupRepository + CrewService Supabase row access — bypasses shape validation on DB rows, causing runtime crashes when schema evolves. useCrewProximityRadar:131 captures crewService.isNearby as non-reactive, causing proximity radar to never update after initial mount.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-group-sync.md](./plans/PLAN-sweep-group-sync.md) — Key finding: "4x as-any on DB row access in GroupRepository/CrewService; stale closure in useCrewProximityRadar stops proximity radar after mount" — Rejected: "Runtime schema validation library" — overweight; Supabase-generated types already present in project
  - **Source of Truth:** [GroupRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts#L27) · [useCrewProximityRadar.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewProximityRadar.ts#L131)
  - **Details:** Prerequisite: Wave 3 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-session-context`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[SERVICES]` `[H-RISK]` `[Meal]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:4]`
  - **Goal:** Add isFlushInProgress re-entrancy guards to 3 flushSyncQueue functions, add try/catch to 3 unawaited AsyncStorage calls in SessionContext, and register 6 undocumented storage keys into the registry.
  - **Decision Log:** Fleet confirmed SpeedTrackingService, ScenesService, and GradientsService all have the same re-entrancy bug — concurrent callers double-upload the queue then both clear it, silently deleting pending session data that one caller never successfully POSTed. This is an active data loss bug.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-session-context.md](./plans/PLAN-sweep-session-context.md) — Key finding: "3x flushSyncQueue with no re-entrancy guard — concurrent callers corrupt queue and silently delete pending session data (2 agents confirmed)" — Rejected: "Move flush to singleton scheduler" — boolean ref guard solves problem with zero new dependencies
  - **Source of Truth:** [SpeedTrackingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SpeedTrackingService.ts#L243) · [ScenesService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ScenesService.ts#L258) · [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts#L161)
  - **Details:** Prerequisite: Wave 3 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-shared-utils`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[SERVICES]` `[M-RISK]` `[Snack]` `[L-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:5]`
  - **Goal:** Deduplicate isValidEmail to a canonical src/utils/validation.ts, add accessibility props to CustomSlider and DeviceItem, fix PositionalGradientBuilder error handling, and add platform guard to LocationService.
  - **Decision Log:** Fleet found isValidEmail duplicated across 3+ auth forms — any future change requires updating all copies in sync. CustomSlider uses PanResponder with zero accessibility props — completely invisible to screen readers, violating App Store accessibility guidelines.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-shared-utils.md](./plans/PLAN-sweep-shared-utils.md) — Key finding: "isValidEmail duplicated 3+ times; CustomSlider has zero accessibility props — invisible to screen readers" — Rejected: "Shared comment" — comments don't prevent drift; canonical module import is correct
  - **Source of Truth:** [CustomSlider.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CustomSlider.tsx#L102) · [LocationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts#L81)
  - **Details:** Prerequisite: Wave 4 fully merged into master before this worktree is created.

- [ ] **`chore/sweep-ui-docked-controller`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[H-RISK]` `[Feast]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:6]`
  - **Goal:** Wrap 5+ unawaited writeToDevice calls in UniversalSlidersFooter with async error handlers, fix stale closure in useStreetMode accelerometer listener, and stabilize MemoizedSk8lytzController dependencies.
  - **Decision Log:** Fleet confirmed writeToDevice called fire-and-forget in 5+ locations in UniversalSlidersFooter.tsx — BLE write failures silently swallowed with no user feedback. useStreetMode:188 captures deviceContext at listener registration — after device reconnect, the listener holds a stale reference and sends to the wrong device.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-docked-controller.md](./plans/PLAN-sweep-ui-docked-controller.md) — Key finding: "5+ unawaited writeToDevice in UniversalSlidersFooter — BLE write failures silently dropped; stale closure in useStreetMode sends to wrong device after reconnect" — Rejected: "Global unhandled promise rejection handler" — too broad; per-call-site async handling required
  - **Source of Truth:** [UniversalSlidersFooter.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/UniversalSlidersFooter.tsx#L393) · [useStreetMode.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useStreetMode.ts#L188)
  - **Details:** Prerequisite: Wave 5 fully merged into master before this worktree is created.



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

