# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## 🔴 CRITICAL: 🛡️ Performance, Stability & Security

## 🟠 HIGH: 🛠️ Engineering Excellence & Tech Debt

- [ ] **chore/sweep-src-identity**
  - **Goal:** Resolve all static audit findings in the IDENTITY domain cluster.
  - **Details:** Wave 1 execution. Prerequisite: None.
  - **Files:** `src/components/account/AccountTabDevices.tsx`, `src/components/account/AccountTabProfile.tsx`, `src/components/account/AccountTabStats.tsx`, `src/components/account/SkaterStatsPanel.tsx`, `src/components/account/types.ts`, `src/components/auth/AuthFormSignIn.tsx`, `src/components/auth/DevSandboxDrawer.tsx`, `src/context/AuthContext.tsx`, `src/hooks/useAccountOverview.ts`, `src/services/AuthProfileService.ts`, `src/services/ProfileService.types.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:1]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-identity.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 1.

- [ ] **chore/sweep-src-ble-core**
  - **Goal:** Resolve all static audit findings in the BLE_CORE domain cluster.
  - **Details:** Wave 1 execution. Prerequisite: None.
  - **Files:** `src/hooks/ble/__tests__/ble-simulator.test.ts`, `src/hooks/ble/useBLEBatterySweep.ts`, `src/hooks/ble/useBLEScanner.ts`, `src/hooks/useBLE.ts`, `src/hooks/useOptimisticBLE.ts`, `src/services/ble/__tests__/BleMachine.test.ts`, `src/services/ble/ConnectService.ts`, `src/services/ble/HeartbeatService.ts`, `src/services/ble/InterrogatorService.ts`, `src/services/ble/RecoveryService.ts`, `src/services/ble/RSSIService.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [H-RISK] [Feast] [L-COGNITIVE] [WAVE:1]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-ble-core.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 1.

- [ ] **chore/sweep-src-ui-visualizer**
  - **Goal:** Resolve all static audit findings in the UI_VISUALIZER domain cluster.
  - **Details:** Wave 1 execution. Prerequisite: None.
  - **Files:** `src/components/CameraTracker.tsx`, `src/components/CustomEffectVisualizer.tsx`, `src/components/NeonHueStrip.tsx`, `src/components/patterns/GradientLibraryTab.tsx`, `src/components/patterns/PatternPickerTab.tsx`, `src/components/patterns/UnifiedPatternPicker.tsx`, `src/components/PositionalGradientBuilder.tsx`, `src/components/ProductVisualizer.tsx`, `src/components/VerticalPatternDrum.tsx`, `src/components/VisualizerUnit.tsx`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:1]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-ui-visualizer.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 1.

- [ ] **chore/sweep-src-pattern-engine**
  - **Goal:** Resolve all static audit findings in the PATTERN_ENGINE domain cluster.
  - **Details:** Wave 1 execution. Prerequisite: None.
  - **Files:** `src/hooks/useAppMicrophone.ts`, `src/hooks/useMusicMode.ts`, `src/hooks/useStreetMode.ts`, `src/protocols/SpatialEngine.ts`, `src/protocols/VisualizerEngine.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [H-RISK] [Meal] [L-COGNITIVE] [WAVE:1]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-pattern-engine.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 1.

- [ ] **chore/sweep-src-group-sync**
  - **Goal:** Resolve all static audit findings in the GROUP_SYNC domain cluster.
  - **Details:** Wave 2 execution. Prerequisite: Wave 1 fully merged.
  - **Files:** `src/components/crew/CrewDetailScreen.tsx`, `src/components/crew/CrewJoinScreen.tsx`, `src/components/crew/CrewLandingScreen.tsx`, `src/components/crew/CrewManageScreen.tsx`, `src/hooks/useCrewHub.ts`, `src/hooks/useCrewManage.ts`, `src/hooks/useCrewProximityRadar.ts`, `src/hooks/useCrewSession.ts`, `src/services/CrewProfileService.ts`, `src/services/CrewService.ts`, `src/services/GroupRepository.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:2]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-group-sync.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 2.

- [ ] **chore/sweep-src-protocol-core**
  - **Goal:** Resolve all static audit findings in the PROTOCOL_CORE domain cluster.
  - **Details:** Wave 2 execution. Prerequisite: Wave 1 fully merged.
  - **Files:** `src/hooks/useProtocolBuilder.ts`, `src/protocols/BanlanxAdapter.ts`, `src/protocols/ZenggeAdapter.ts`, `src/protocols/ZenggeProtocol.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [H-RISK] [Meal] [L-COGNITIVE] [WAVE:2]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-protocol-core.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 2.

- [ ] **chore/sweep-src-utils**
  - **Goal:** Resolve all static audit findings in the UTILS domain cluster.
  - **Details:** Wave 2 execution. Prerequisite: Wave 1 fully merged.
  - **Files:** `src/utils/kMeansPalette.ts`, `src/utils/migrateAuthTokens.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [L-RISK] [Snack] [L-COGNITIVE] [WAVE:2]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-utils.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 2.

- [ ] **chore/sweep-src-ui-docked-controller**
  - **Goal:** Resolve all static audit findings in the UI_DOCKED_CONTROLLER domain cluster.
  - **Details:** Wave 3 execution. Prerequisite: Wave 2 fully merged.
  - **Files:** `src/components/docked/BuilderPanel.tsx`, `src/components/docked/CameraPanel.tsx`, `src/components/docked/DockedDock.tsx`, `src/components/docked/FavoritesPanel.tsx`, `src/components/docked/MusicPanel.tsx`, `src/components/docked/PresetCard.tsx`, `src/components/docked/QuickPresetModal.tsx`, `src/components/docked/UniversalSlidersFooter.tsx`, `src/components/DockedController.tsx`, `src/hooks/useControllerAnalytics.ts`, `src/hooks/useControllerDispatch.ts`, `src/hooks/useDashboardController.tsx`, `src/hooks/useDockedControllerState.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:3]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-ui-docked-controller.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 3.

- [ ] **chore/sweep-src-notifications-&-routing**
  - **Goal:** Resolve all static audit findings in the NOTIFICATIONS_&_ROUTING domain cluster.
  - **Details:** Wave 3 execution. Prerequisite: Wave 2 fully merged.
  - **Files:** `App.tsx`, `src/providers/BluetoothGuard.tsx`, `src/providers/ComplianceGate.tsx`, `src/services/LocationService.ts`, `src/services/NotificationService.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Meal] [L-COGNITIVE] [WAVE:3]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-notifications-&-routing.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 3.

- [ ] **chore/sweep-src-data-layer**
  - **Goal:** Resolve all static audit findings in the DATA_LAYER domain cluster.
  - **Details:** Wave 4 execution. Prerequisite: Wave 3 fully merged.
  - **Files:** `src/hooks/useCuratedPicks.ts`, `src/hooks/useFavorites.ts`, `src/services/DeviceRepository.ts`, `src/services/GradientsService.ts`, `src/services/ScenesService.ts`, `src/services/SkateSpotsService.ts`, `src/services/SpeedTrackingService.ts`, `src/services/supabaseClient.ts`, `src/services/TelemetryService.ts`, `src/types/supabase.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:4]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-data-layer.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 4.

- [ ] **chore/sweep-src-admin-&-telemetry**
  - **Goal:** Resolve all static audit findings in the ADMIN_&_TELEMETRY domain cluster.
  - **Details:** Wave 5 execution. Prerequisite: Wave 4 fully merged.
  - **Files:** `src/components/admin/AdminToolsModal.tsx`, `src/components/admin/tools/AdminAuditLogViewer.tsx`, `src/components/admin/tools/AdminPicksScheduler.tsx`, `src/components/admin/tools/AdminRosterPanel.tsx`, `src/components/admin/tools/FeatureFlagsPanel.tsx`, `src/components/admin/tools/GlobalAnalyticsPanel.tsx`, `src/components/admin/tools/HardwareBlacklistPanel.tsx`, `src/components/admin/tools/Sk8LytzProgrammer.tsx`, `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx`, `src/components/admin/tools/tabs/DiagnosticLabOracleTab.tsx`, `src/components/admin/tools/UserManagementPanel.tsx`, `src/hooks/useDiagnosticLog.ts`, `src/services/AppLogger.ts`, `src/services/AppSettingsService.ts`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:5]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-admin-&-telemetry.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 5.

- [ ] **chore/sweep-src-ui-screens-and-modals**
  - **Goal:** Resolve all static audit findings in the UI_SCREENS_AND_MODALS domain cluster.
  - **Details:** Wave 6 execution. Prerequisite: Wave 5 fully merged.
  - **Files:** `android/app/src/main/AndroidManifest.xml`, `android/sk8lytzWear/src/main/AndroidManifest.xml`, `app.config.js`, `src/../.husky/pre-commit`, `src/../android/app/src/main/AndroidManifest.xml`, `src/../android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt`, `src/../android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt`, `src/../android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/tiles/Sk8lytzTileService.kt`, `src/../app.config.js`, `src/../modules/sk8lytz-watch-bridge/ios/Sk8lytzWatchBridgeModule.swift`, `src/../package-lock.json`, `src/../package.json`, `src/../supabase/functions/notify-crew-session/index.ts`, `src/../supabase/migrations/20260414_account_deletion_rpc.sql`, `src/../supabase/migrations/20260426200000_phase_control_panels.sql`, `src/../supabase/migrations/20260607100000_fix_telemetry_schema.sql`, `src/../supabase/migrations/20260609140000_live_debugger_views.sql`, `src/../supabase/migrations/20260609175500_restore_domain_admin_promotion.sql`, `src/../targets/watch/expo-target.config.js`, `src/../targets/watch/HealthManager.swift`, `src/../targets/watch/WatchConnectivityManager.swift`, `src/components/AccountModal.tsx`, `src/components/CommunityModal.tsx`, `src/components/CrewMemberDashboard.tsx`, `src/components/CustomSlider.tsx`, `src/components/dashboard/CrewHubSlab.tsx`, `src/components/dashboard/DashboardCrewPanel.tsx`, `src/components/dashboard/DashboardGroupList.tsx`, `src/components/dashboard/DashboardTelemetryHero.tsx`, `src/components/dashboard/HardwareStatusPills.tsx`, `src/components/dashboard/MySkatesSlab.tsx`, `src/components/dashboard/SkateGroupCard.tsx`, `src/components/DashboardGroupList.tsx`, `src/components/DeviceItem.tsx`, `src/components/DeviceSettingsModal.tsx`, `src/components/GroupSettingsModal.tsx`, `src/components/LocationPicker.tsx`, `src/components/LocationPickerMap.web.tsx`, `src/components/MarqueeText.tsx`, `src/components/SessionSummaryModal.tsx`, `src/components/shared/BLEErrorBoundary.tsx`, `src/constants/storageKeys.ts`, `src/context/AppConfigContext.tsx`, `src/context/SessionContext.tsx`, `src/context/ThemeContext.tsx`, `src/hooks/dev/useWebDemoConsoleBridge.ts`, `src/hooks/useDeviceStateLedger.ts`, `src/hooks/useTelemetryLedger.ts`, `src/screens/AuthScreen.tsx`, `src/screens/DashboardScreen.tsx`, `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`, `src/services/BlePingService.ts`, `src/services/BleWriteDispatcher.ts`, `src/services/BleWriteQueue.ts`, `src/services/PermissionService.ts`, `src/services/session/__tests__/SessionMachine.test.ts`, `src/services/session/NotificationService.ts`, `src/services/session/SessionMachine.ts`, `src/styles/DashboardStyles.ts`, `src/theme/theme.ts`, `targets/watch/Info.plist`
  - **Tags:** [🧹 TECH DEBT] [❌ UNVERIFIED] [⚙️ SERVICES] [M-RISK] [Feast] [L-COGNITIVE] [WAVE:6]
  - **Source of Truth:** docs/plans/PLAN-sweep-src-ui-screens-and-modals.md
  - **Decision Log (2026-06-13):** Generated from deep-dive fleet synthesis. AST coloring assigned Wave 6.



- [ ] **`chore/session-service-test-coverage`**
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[✅ L-RISK]` `[🍱 Meal]` `[🧠 MEDIUM]`
  - **Goal:** Add substantive unit tests for SensorService, HealthService, and NotificationService — the 3 untested session actor services from the post-merge audit.
  - **Decision Log:** Post-merge audit 2026-06-11: 5 of 9 session service files have zero test coverage. SensorService is highest risk (GPS + accelerometer + crewService side effects). NotificationService ENDING-state button logic newly added in session-machine-actor-types task — needs test coverage for the 3-branch action logic.
  - **Analysis:** 📊 Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) · Plan: [PLAN-chore-session-service-test-coverage.md](./plans/PLAN-chore-session-service-test-coverage.md)
    Key finding: "44% test file coverage by file count; SensorService has GPS+accelerometer+crewService coupling and zero tests"
    Rejected alternative: "Skip until feature work" — test gaps in newly architected services compound quickly
  - **Source of Truth:** 📖 [AutoPauseService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/AutoPauseService.test.ts) reference pattern · [SensorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SensorService.ts) · [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts) · [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
  - **Details:** Must run AFTER `fix/session-machine-actor-types` merges — NotificationService test for ENDING branch depends on the ENDING fix being in place. 3 new test files, no production source changes.

### ⚡ [BATCH:doc-pipeline-sync] — `doc-pipeline-sync-batch` — READY
> **Worktree Strategy**: All 4 tasks fully parallel (tools/*.md files only — zero TypeScript overlap)
> **Type**: Parallel — all [WAVE:1]
> **Prerequisite**: None — AST confirmed zero import-tree overlap with session-xstate-engine Wave 3
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) — Full doc ecosystem audit: 6 confirmed gaps after Wave 1+2 merges; Phase 4 of deepdive-docs was silently skipped; KB missing XState entry; 3 ADRs in SESSION_LOG never promoted; INDUSTRY_BENCHMARKS duplicated; TEST_PLAN pre-XState

#### Batch Strategy Table
| Wave | Task Slug | Files Touched | Prerequisite |
|---|---|---|---|
| 1 | `docs/cartographer-rebuild-and-harden` | `tools/SK8Lytz_App_Master_Reference.md` + Tier-3 satellite docs + 3 workflow files | None |
| 1 | `docs/xstate-v5-kb-capture` | `tools/knowledge-base/patterns/xstate-v5-patterns.md` + `tools/knowledge-base/INDEX.md` | None |
| 1 | `fix/industry-benchmarks-dedup` | `tools/INDUSTRY_BENCHMARKS.md` | None |
| 1 | `docs/test-plan-session-machine` | `tools/SK8Lytz_TEST_PLAN.md` | None |

## 🚑 TRIAGE QUEUE


> **Source Analysis**: 📊 [PLAN-hardware-setup-batch.md](./plans/PLAN-hardware-setup-batch.md) — Unifies setup wizard logic fixes, brand color updates, and global header padding into a single surgical pass.




---


### 🌊 Parallel Wave Strategy (AST-Verified)

| Wave | Task Clusters | Parallel-Safe? | Prerequisite |
|------|--------------|---------------|-------------------|
| 1 | [BATCH:sweep-src-identity], [BATCH:sweep-src-ble-core], [BATCH:sweep-src-ui-visualizer], [BATCH:sweep-src-pattern-engine] | ✅ Yes | None |
| 2 | [BATCH:sweep-src-group-sync], [BATCH:sweep-src-protocol-core], [BATCH:sweep-src-utils] | ✅ Yes | Wave 1 fully merged |
| 3 | [BATCH:sweep-src-ui-docked-controller], [BATCH:sweep-src-notifications-&-routing] | ✅ Yes | Wave 2 fully merged |
| 4 | [BATCH:sweep-src-data-layer] | ✅ Yes | Wave 3 fully merged |
| 5 | [BATCH:sweep-src-admin-&-telemetry] | ✅ Yes | Wave 4 fully merged |
| 6 | [BATCH:sweep-src-ui-screens-and-modals] | ✅ Yes | Wave 5 fully merged |

## 🚧 ACTIVE SPRINT
> Currently executing: **`feat/harden-ble-regression-shields`**
> Completed: N/A




## 🔵 LOW: ✨ New Features & UI Enhancements

_User-facing product value and UI refinements._

- [ ] `feat/app-wide-ux-tips` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 12k] [⏱️ 3h] [📅 2026-04-14] [🤖 FLASH] [📝️ NEEDS-PLAN] Contextual tips system for key friction points. → [Plan](docs/plans/feat-app-wide-ux-tips.md)

---

## 🔥 ON DECK


### ⚡ [BATCH:session-xstate-engine] — `session-xstate-engine-batch` — READY
> **Worktree Strategy**: Sequential waves (W0→W1→W2), then W3A+W3B+W3C fully parallel  
> **Type**: Mixed — Spike → Sequential → Parallel  
> **Prerequisite**: None — board is clear  
> **Source Analysis**: 📊 [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) + [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) — 10 confirmed session sync bugs traced to single root cause: no XState state authority for session lifecycle

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






_User-facing product value and UI refinements._

- [ ] `feat/app-wide-ux-tips` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 12k] [⏱️ 3h] [📅 2026-04-14] [🤖 FLASH] [📝️ NEEDS-PLAN] Contextual tips system for key friction points. → [Plan](docs/plans/feat-app-wide-ux-tips.md)

---

## ❄️ Icebox / Backburner (Manual Trigger Only)

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
 Fall Detection — triggers white 'Flare' strobe on impact. → [Plan](docs/plans/feat-impact-sentinel-safety.md)
- [ ] `feat/kinetic-brake-lights` : [🧪 LAB] [⚠️ H-RISK] [🍱 Meal] [🪙 15k] [⏱️ 3h] [🧠 THINK] [Pillar 12] Kinetic Safety — phone accelerometer pulse RED for braking. → [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [☁️ CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [⏱️ 6h] [🧠 THINK] Geofence-based 'Hive Mind' synchronization. → [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 8k] [⏱️ 3h] [🤖 FLASH] [📝️ NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 25k] [⏱️ 3h] [🤖 PRO-HIGH] [📝️ NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [☁️ CLOUD] [⚠️ H-RISK] [🍱 Meal] [🪙 20k] [⏱️ 3h] [🧠 THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [☁️ CLOUD] [✅ L-RISK] [🍱 Meal] [🪙 12k] [⏱️ 3h] [🤖 FLASH] [📝️ NEEDS-PLAN] Card Swipe Navigation.
