# SK8Lytz Master Bucket List

> ⚠️ AI AGENT DIRECTIVES (THE CONSTITUTION)
> The constitution is located in `.agents/rules/kanban-constitution.md` for universal agent context injection.

---

## 📊 Global System Readiness

---

## Ã°Å¸â€Â´ CRITICAL: 🛡️ Performance, Stability & Security



- [ ] **`chore/session-service-test-coverage`**
  - **Tags:** `[✅ READY]` `[🤖 INFERRED]` `[🧪 LAB]` `[✅ L-RISK]` `[Ã°Å¸ÂÂ± Meal]` `[🧠 MEDIUM]`
  - **Goal:** Add substantive unit tests for SensorService, HealthService, and NotificationService — the 3 untested session actor services from the post-merge audit.
  - **Decision Log:** Post-merge audit 2026-06-11: 5 of 9 session service files have zero test coverage. SensorService is highest risk (GPS + accelerometer + crewService side effects). NotificationService ENDING-state button logic newly added in session-machine-actor-types task — needs test coverage for the 3-branch action logic.
  - **Analysis:** 📊 Source: [session_xstate_audit.md](file:///C:/Users/Magma/.gemini/antigravity/brain/215f67ea-4c87-4823-b1ce-c91d7ed5e78c/session_xstate_audit.md) · Plan: [PLAN-chore-session-service-test-coverage.md](./plans/PLAN-chore-session-service-test-coverage.md)
    Key finding: "44% test file coverage by file count; SensorService has GPS+accelerometer+crewService coupling and zero tests"
    Rejected alternative: "Skip until feature work" — test gaps in newly architected services compound quickly
  - **Source of Truth:** 📖 [AutoPauseService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/__tests__/AutoPauseService.test.ts) reference pattern · [SensorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/SensorService.ts) · [HealthService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/HealthService.ts) · [NotificationService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/session/NotificationService.ts)
  - **Details:** Must run AFTER `fix/session-machine-actor-types` merges — NotificationService test for ENDING branch depends on the ENDING fix being in place. 3 new test files, no production source changes.

### ⚡ [BATCH:doc-pipeline-sync] — `doc-pipeline-sync-batch` — READY
> **Worktree Strategy**: All 4 tasks fully parallel (tools/*.md files only — zero TypeScript overlap)
> **Type**: Parallel — all [WAVE:1]
> **Prerequisite**: None — AST confirmed zero import-tree overlap with session-xstate-engine Wave 3
> **Source Analysis**: 📊 [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) — Full doc ecosystem audit: 6 confirmed gaps after Wave 1+2 merges; Phase 4 of deepdive-docs was silently skipped; KB missing XState entry; 3 ADRs in SESSION_LOG never promoted; INDUSTRY_BENCHMARKS duplicated; TEST_PLAN pre-XState

#### Batch Strategy Table
| Wave | Task Slug | Files Touched | Prerequisite |
|---|---|---|---|
| 1 | `docs/cartographer-rebuild-and-harden` | `tools/SK8Lytz_App_Master_Reference.md` + Tier-3 satellite docs + 3 workflow files | None |
| 1 | `docs/xstate-v5-kb-capture` | `tools/knowledge-base/patterns/xstate-v5-patterns.md` + `tools/knowledge-base/INDEX.md` | None |
| 1 | `fix/industry-benchmarks-dedup` | `tools/INDUSTRY_BENCHMARKS.md` | None |
| 1 | `docs/test-plan-session-machine` | `tools/SK8Lytz_TEST_PLAN.md` | None |

## 🚑 TRIAGE QUEUE

- [ ] **fix/crash-telemetry-severity-check**
  - **Goal:** Fix check constraint violation on `crash_telemetry` table severity insert.
  - **Details:** `AppLogger.ts` inserts `severity: 'CRITICAL'` which is not in the allowed CHECK constraint list `('FATAL', 'ERROR', 'WARN', 'INFO')`. Fix `AppLogger.ts` to map `CRITICAL` events to `'FATAL'` or `'ERROR'`.
  - **Files:** `src/services/AppLogger.ts`
  - **Tags:** [🚑 TRIAGE QUEUE] [✅ READY] [⚙️ SERVICES] [L-RISK] [Snack] [L-COGNITIVE]
  - **Source of Truth:** C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\AppLogger.ts#L502
  - **Decision Log (2026-06-13):** Found during Supabase log audit. Check constraint restricts severity to FATAL/ERROR/WARN/INFO, but code sends CRITICAL.


## 🚧 ACTIVE SPRINT
> Currently executing: **`feat/harden-ble-regression-shields`**
> Completed: N/A




## Ã°Å¸â€Âµ LOW: ✨ New Features & UI Enhancements

_User-facing product value and UI refinements._

- [ ] `feat/app-wide-ux-tips` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 12k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [📅 2026-04-14] [🤖 FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Contextual tips system for key friction points. → [Plan](docs/plans/feat-app-wide-ux-tips.md)

---

## Ã°Å¸â€Â¥ ON DECK


### ⚡ [BATCH:session-xstate-engine] — `session-xstate-engine-batch` — READY
> **Worktree Strategy**: Sequential waves (W0→W1→W2), then W3A+W3B+W3C fully parallel  
> **Type**: Mixed — Spike → Sequential → Parallel  
> **Prerequisite**: None — board is clear  
> **Source Analysis**: 📊 [session_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/session_audit_report.md) + [implementation_plan.md](file:///C:/Users/Magma/.gemini/antigravity/brain/689630a3-694f-4156-a7bc-69878591a1d7/implementation_plan.md) — 10 confirmed session sync bugs traced to single root cause: no XState state authority for session lifecycle

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
- [ ] **`chore/sweep-cloud-supabase`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[CLOUD]` `[H-RISK]` `[Meal]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Harden 6 SECURITY DEFINER PostgreSQL functions with `SET search_path = ''` to eliminate SQL injection surface, fix email domain validation bypass, restrict scraper_blocklist RLS, and add error handling to the Deno edge function.
  - **Decision Log:** deepdive fleet confirmed 6 unguarded `SECURITY DEFINER` functions in Supabase migrations — without `SET search_path`, a caller can inject a malicious schema and execute arbitrary SQL under elevated privilege. The email LIKE `%@sk8lytz.com` pattern is bypassable with `x@sk8lytz.com.evil.com`.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-cloud-supabase.md](./plans/PLAN-sweep-cloud-supabase.md) — Key finding: "6 SECURITY DEFINER RPCs without SET search_path — SQL injection vectors (2 agents confirmed)" — Rejected: "App-layer validation only" — does not protect against DB-level schema injection
  - **Source of Truth:** [20260414_account_deletion_rpc.sql](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260414_account_deletion_rpc.sql) + 5 additional migration files listed in PLAN

- [ ] **`chore/sweep-devops-tooling`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[DEVOPS]` `[H-RISK]` `[Snack]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix gatekeeper rebase failure detection ($LASTEXITCODE), prevent regression healer from committing to master, fix auto-archiver slug regex collision, and replace raw `npx tsc`/`npx jest` in husky pre-commit hook.
  - **Decision Log:** Fleet confirmed `fortress-gatekeeper.ps1` does not check `$LASTEXITCODE` after `git rebase` — a failed rebase is silently ignored, leaving master in a corrupted merge state. `regression_healer.py` has no branch guard and can commit directly to master.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-devops-tooling.md](./plans/PLAN-sweep-devops-tooling.md) — Key finding: "Gatekeeper git rebase failure is unchecked; regression healer can commit to master" — Rejected: "Manual review step only" — silent failure mode requires a process guard
  - **Source of Truth:** [fortress-gatekeeper.ps1](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/fortress-gatekeeper.ps1#L93) · [regression_healer.py](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/sentinel/regression_healer.py#L188)

- [ ] **`chore/sweep-protocol-core`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[BLE]` `[H-RISK]` `[Snack]` `[H-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Resolve split-brain 0x40 chunking between ZenggeAdapter and BleWriteDispatcher, fix incorrect TransitionType mapping, and remove hardcoded 54-pixel max in streamPixelFrame.
  - **Decision Log:** 2 independent fleet agents confirmed ZenggeAdapter.prepareForTransmission and BleWriteDispatcher implement conflicting chunking logic — the controller receives double-chunked payloads, causing corrupted LED state. Per Protocol Bible: chunking belongs to the dispatcher only.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-protocol-core.md](./plans/PLAN-sweep-protocol-core.md) — Key finding: "Split-brain 0x40 chunking confirmed by 2 agents — double-chunked payloads corrupt LED state" — Rejected: "Move chunking to adapter" — Protocol Bible explicitly assigns chunking to the write dispatcher
  - **Source of Truth:** [ZenggeAdapter.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeAdapter.ts#L260) · [ZENGGE_PROTOCOL_BIBLE.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/docs/ZENGGE_PROTOCOL_BIBLE.md)

- [ ] **`chore/sweep-ui-screens-dashboard`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[H-RISK]` `[Meal]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Eliminate `as any`/`any[]` props from 5 dashboard sub-components, fix Animated.Value memory leak in render cycle, fix Platform.OS ternary missing Web case, and fix power-toggle loop missing queue serialization.
  - **Decision Log:** Fleet found 6 dashboard components with untyped any props in the primary render path. Animated.Value instantiated in CrewHubSlab.tsx:181 render body accumulates instances on every render causing memory growth over long skating sessions.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-screens-dashboard.md](./plans/PLAN-sweep-ui-screens-dashboard.md) — Key finding: "Animated.Value memory leak in render; 5 components with any-typed props in Dashboard render path" — Rejected: "@ts-ignore suppression" — banned by The No any Cast Law
  - **Source of Truth:** [CrewHubSlab.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/CrewHubSlab.tsx#L181) · [DashboardTelemetryHero.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/dashboard/DashboardTelemetryHero.tsx#L12)

- [ ] **`chore/sweep-ui-visualizer-patterns`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[UI]` `[M-RISK]` `[Meal]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix floating unawaited promise in UnifiedPatternPicker, remove duplicate hexToRgb, fix web-only CSS props crashing on native in NeonHueStrip, and extract inline FlatList callbacks to stable useCallback refs.
  - **Decision Log:** Fleet confirmed writeToDeviceRef.current(payload) in UnifiedPatternPicker returns a Promise never caught — BLE write failures silently dropped. NeonHueStrip passes touchAction/userSelect to a native View, which crashes silently on iOS/Android.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-ui-visualizer-patterns.md](./plans/PLAN-sweep-ui-visualizer-patterns.md) — Key finding: "Floating promise in UnifiedPatternPicker:62; NeonHueStrip web-only props crash on native" — Rejected: "Return void from writeToDevice" — masks errors; proper async error handling required
  - **Source of Truth:** [UnifiedPatternPicker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/patterns/UnifiedPatternPicker.tsx#L62) · [NeonHueStrip.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/NeonHueStrip.tsx#L99)

- [ ] **`chore/sweep-os-permissions-manifests`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[NATIVE]` `[H-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Move hardcoded Google Maps API key from AndroidManifest.xml to build-time env var, fix HealthKit activity type mismatch, and trim excessive foregroundServiceType declaration.
  - **Decision Log:** Fleet flagged Google Maps API key hardcoded in AndroidManifest.xml:29 as plaintext — committed to git history and exposed to all repo contributors. Android 14+ strictly enforces foregroundServiceType matching; the over-broad declaration risks Play Store rejection.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-os-permissions-manifests.md](./plans/PLAN-sweep-os-permissions-manifests.md) — Key finding: "Google Maps API key hardcoded in AndroidManifest.xml — PII/secret leak committed to git" — Rejected: "Add to .gitignore only" — key already in history; must rotate + move to env var
  - **Source of Truth:** [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml#L29) · [app.config.js](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/app.config.js#L15)

- [ ] **`chore/sweep-native-watch`**
  - **Tags:** `[READY]` `[CONFIRMED]` `[NATIVE]` `[M-RISK]` `[Snack]` `[M-COGNITIVE]` `[BATCH:deepdive-sweep]` `[WAVE:1]`
  - **Goal:** Fix @Published property mutation on WCSession background queue (iOS crash risk), fix non-atomic SharedPreferences write in Wear OS, and align exercise type to INLINE_SKATING.
  - **Decision Log:** Fleet confirmed WatchConnectivityManager.swift:105 mutates @Published properties from the WCSession background delegate queue — SwiftUI rendering on background threads causes crashes on iOS 17+. WearMessageSender.kt:85 non-atomic SharedPreferences write causes data corruption under concurrent health delivery.
  - **Analysis:** Source: [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) Plan: [PLAN-sweep-native-watch.md](./plans/PLAN-sweep-native-watch.md) — Key finding: "@Published modified on WCSession background queue — guaranteed crash on iOS 17+" — Rejected: "@MainActor attribute only" — WCSession delegates are not MainActor-isolated; must use explicit DispatchQueue.main.async
  - **Source of Truth:** [WatchConnectivityManager.swift](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift#L105) · [WearMessageSender.kt](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/WearMessageSender.kt#L85)

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

### 🎵 Epic: Music Mode

- [ ] `feat/music-intel-phase-1` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 50k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [📅 2026-04-14] [🧠 THINK] [Spotify Sync] — OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Media Access] — Android MediaSession detection (YouTube, Pandora, etc.). → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [🧪 LAB] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Live Rink Mode] — ShazamKit/ACRCloud periodic background scanning (45s). → [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🤖 PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] [Apple Music] — MusicKit integration for native iOS BPM. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 45k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [📅 2026-04-14] [⛔ BLOCKED BY feat/music-intel-phase-1] [🧠 THINK] [Crew Party Sync] — Master BPM Choreography Engine with Realtime crew sync. → [Plan](docs/plans/feat-music-integration-master.md)

- [ ] `feat/google-oauth-integration` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [📅 2026-04-14] [🧠 THINK] Integrate Google OAuth as an auth provider. (Requires Google Cloud Console setup + Supabase config). → [Plan](docs/plans/feat-google-oauth-integration.md)
- [ ] `feat/spatial-beat-mapping` : [🧪 LAB] [⚠️ H-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 18k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🧠 THINK] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). → [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🤖 PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Transform Dashboard into palette-synced dynamic backgrounds. → [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [🧪 LAB] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 10k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🤖 PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Pattern selection (Strobe, Blink, Static) + music slider fix. → [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/kinetic-brake-lights` : [🧪 LAB] [⚠️ H-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 15k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🧠 THINK] [Pillar 12] Kinetic Safety — phone accelerometer pulse RED for braking. → [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [⚠️ H-RISK] [🥩 Feast] [🪙 30k] [Ã¢ÂÂ±Ã¯Â¸Â 6h] [🧠 THINK] Geofence-based 'Hive Mind' synchronization. → [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `feat/neogleamz-brand-presence` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 8k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🤖 FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 25k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🤖 PRO-HIGH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [⚠️ H-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 20k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🧠 THINK] GPS-based auto-crew discovery.
- [ ] `feat/add-swipe-nav` : [Ã¢ËœÂÃ¯Â¸Â CLOUD] [✅ L-RISK] [Ã°Å¸ÂÂ± Meal] [🪙 12k] [Ã¢ÂÂ±Ã¯Â¸Â 3h] [🤖 FLASH] [Ã°Å¸â€œÂÃ¯Â¸Â NEEDS-PLAN] Card Swipe Navigation.

