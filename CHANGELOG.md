## [3.3.0] - 2026-05-02

### ✨ Features
- **ble**: Overwatch BLE Engine — reactive always-on discovery architecture
- **ble**: On-demand hardware probe via BLINK tap — kill 7s blocking scan phase

### 🐛 Bug Fixes
- **ble**: Overwatch Polish — P1 preemption, dedup classification, dual-scan hardening
- **ble**: Atomic group connect — single setConnectedDevices after full loop
- **wizard**: Atomic pingDevice() primitive — fixes Phantom Blink and GATT probe collision
- **ble**: Flush allDevices pre-scan + RSSI gate to kill ghost devices
- **ble**: Hoist wasSweeperActive above try block (TS2304 scope fix)
- **ble**: Fix TS1117 duplicate scanForPeripherals property in useBLE return object
- **ble**: Match writeToDevice signature in AccountModal (TS2322)

### 🔧 Maintenance & Refactoring
- **ble**: Overwatch cleanup — remove legacy code, fix burst timer race, dedup Interrogator, thread writeToDevice prop

---

## [3.2.1] - 2026-04-24

### 🐛 Bug Fixes
- **core**: prevent session abandonment during crew deletion (`5871dc4`)

### 📖 Documentation
- **plans**: commit AI-First plans for sk8lytz picks and session abandonment fix (`6a0b3e9`)

---

## [3.2.0] - 2026-04-24
### ✨ Features
- **admin**: Rebuild App Manager as a registry-driven tabbed controls hub (`fc756d9`)
- **ui**: Apply dashboard and pattern UI polish, fix group creation duplication bug (`ab61434`)
- **ble**: Overhaul useBLEScanner for passive telemetry and add AdvancedHardwareModal (`354a869`)
- **hardware**: Achieve 0xA3 protocol parity for spatial arrays and extended sequences (`c343568`)
- **pattern-engine**: Implement 0x51 MTU chunking and Native Temporal modes (`bae8df9`)
- **ui**: Controller power button refactor and group sync fix (`eb7ec94`)
- **ui**: Batch UI snacks - map radius, crewz rename, minimalist sliders, hide scenes (`8ab74ab`)
- **ui**: Implement spectrum analyzer and fix music mode 0x73 byte order (`b4f7718`, `d391dce`, `f68771d`, `51ed2f5`)
- **map**: Add spot labels, session clustering, and smart location picker (`0f11e6f`)

### 🐛 Bug Fixes
- **pattern**: Recalibrate math visualizers and resolve BLE blackout (`26ca458`)
- **ble**: Resolve tsc errors in useBLEScanner and AdvancedHardwareModal (`584fedc`)
- **ui**: Deduplicate power buttons by removing header button (`73d9c3b`)
- **ui**: Use actual profile display name instead of auth metadata fallback in CrewModal (`0748a0a`)
- **tsc**: Resolve undefined nearbySpots from context destructuring (`c978609`)
- **ui**: Lock ProductVisualizer to dark background in all themes (`c126c25`)

### 🔧 Maintenance & Refactoring
- **refactor**: Decompose AccountModal god object into domain tabs (`ae2ebf1`)
- **refactor**: Finalize GodObject extraction into standalone tab components for Diagnostic Lab (`d551a26`)
- **refactor**: Decompose AuthScreen into domain-specific form components (`a6ab7a1`)
- **refactor**: Extract VisualizerUnit from ProductVisualizer (`c07bce2`)
- **chore**: Fix missing imports and TS type regressions from god object decomposition (`8ade3b9`, `7bcfac8`)

### 📖 Documentation
- **protocol**: Update bible and master reference with deep dive findings (`a5184c9`)
- **protocol**: Update 0x59 payload specs and transition types (`c28804e`)

---

## [3.1.0] - 2026-04-23

### 🚀 Features
- feat(street-mode): implement pattern engine parity and dynamic distribution slider
- feat(ui): direction toggle pill in UniversalSlidersFooter for animated patterns

### 🐛 Bug Fixes
- fix(street-mode): correct HALOZ physical geometry rendering and drop software palindrome
- fix(street-mode): migrate hardware array math to PatternEngine for perfect parity
- fix(street): align Street Mode payload with hardware sequential repeat
- fix(visualizer): align HALOZ mirroring parity with hardware sequential repeat
- fix(ui): thread fixedDirection into ProductVisualizer — product shape now flips on REV/FWD toggle
- fix(ui): remove direction race — rely on UnifiedPatternPicker useEffect for dispatch
- fix(ui): thread direction through PatternPickerTab→PatternCard→LEDStripPreview so visualizers flip on REV/FWD toggle

### 🛠 Maintenance
- chore(release): v3.1.0
- Revert massive visualizer overrides, keeping visualizer unmodified

## [3.0.0] - 2026-04-23

### ✨ Features
- feat(scenes): implement 32-slot Scene Builder UI per EPIC-004 Phase 3 plan
- feat(ui): implement UnifiedPatternPicker and integrate into DockedController
- feat(favorites): full BUILDER state persistence in favorites v2
- feat(led-strip): add LEDStripPreview and fix PatternEngine buildStrobe duplicate
- feat(pattern-engine): migrate phase1b temporal and generative patterns
- feat(pattern-engine): migrate phase1b math waves to autonomous builders
- feat(pattern-engine): migrate phase1b marquees to math builders
- feat(pattern-engine): complete feat/pattern-engine-phase1b-static
- feat(patterns): implement Batch 3 native effects (25-33) via synthesized math
- feat(patterns): implement Batch 2 native effects (13-24) via synthesized math
- feat(patterns): implement Phase 1A Batch 1 ge.* effects (IDs 29-40)
- feat(pattern-engine): implement Group B Chases and Meteors math
- feat(hw-screen): display product_id chip in DeviceSettingsModal
- feat(tools): add automated database backup daemon and integrate into agent workflows

### 🐛 Bug Fixes
- fix(web): silence useNativeDriver warning spam in PatternCard
- fix(web): resolve Maximum update depth loop in LEDStripPreview + UnifiedPatternPicker
- fix(programs): minimal surgical fixes for Metro 500 - zero collateral damage
- fix(programs): wire FloatingDock into DockedController render tree
- fix(programs): remove residual PROGRAMS syntax artifacts causing Metro 500
- fix(pattern-picker): resolve color snap-back regression
- fix(visualizer): restore animation parity between app and hardware
- fix(scene-builder): resolve all TSC errors across SceneBuilder components
- fix(gradient-builder): correct animation chips to APK-proven transitionType 0x00-0x03
- fix(ui): correct BuilderNode init payload in UnifiedPatternPicker
- fix(pattern-engine): audit parity - remove visualizer overrides and pass direction
- fix(led-strip): remove invalid direction arg
- fix(led-strip): correct import paths
- fix(map): auto-zoom map camera to selected radius bounds by populating silent location
- fix(map): filter out unpublished skate spots from live map
- fix(hw-screen): wrap productId || ?? chain in parens (TS5076)
- fix(pr-b)!: BLE hardening — session time sync + chunked write framing
- fix(pr-c)!: protocol guards — 0x43 condemned, 0x42 clamp, 0x41 correct format, EventType fix
- fix(telemetry): add MUSIC_MODE_EXIT to EventType union (missed in PR-A)
- fix(pr-a)!: protocol correctness — product_id, mic source, music exit packet
- fix(gradient-builder): correct LED count, pin cap, transition IDs + label icons
- fix(emergency-pattern)!: correct HALOZ segment mirror + SOULZ ledPoints scaling
- fix(hardware): correct HALOZ ledPoints 16/1-seg to 8/2-seg and document three-layer LED model

### 🔧 Maintenance & Refactoring
- chore(programs)!: retire PROGRAMS mode and 0x42 RBM architecture
- refactor(account-modal): remove dead legacy shims
- refactor(visualizer): gut simMode legacy engine and dead mode branches
- chore(pattern-engine): purge condemned 0x03 opcodes from entire codebase
- chore(cleanup): sync uncommitted master changes after epic merge
- refactor(protocols): migrate MUSIC mode off RbmSimulator → PatternEngine, delete RbmSimulator.ts
- refactor(protocols): deprecate Symphony/0x41 branding, inline RbmDictionary into RbmSimulator
- chore(agents): enforce clean slate rule in Release Manager persona and Phase 6 pipeline
- refactor(utils): retire RbmDictionary, migrate ProductVisualizer off getRbmVisualizerFrame/rgbToHex
- chore(agents): inject battle-hardened constraints into lifecycle personas
- chore(agents): add rule 14 to ban automated scripts from mangling the bucket list
- chore(agents): execute The Great Consolidation to unify and harden the architecture
- chore(wind-down): sync Master Reference + archive BATCH:PR-A/B/C in bucket list
- refactor(epic004): reframe Phase 1 as complete hardware parity reversal
- refactor(epic004): delete Symphony Phase 2 — PatternEngine owns all payloads
- chore(repo): clean gitignore and commit protocol bible + scraper stack update

### 📖 Documentation
- docs(audit): fix ModeType table in Master Reference + remove stale PROGRAMS TODO
- docs(master-ref): update header — BATCH:P2 complete, v2.7.0
- docs(workflows): revert shared worktree batch architecture in favor of isolated per-task worktrees
- docs(workflows): upgrade /start-task engine to fully support batch execution, sequential worktrees, and completion stamps
- docs(plans): commit AI-First audit plan for verify-pattern-engine-wiring [BATCH:P0]
- docs(protocols): add wiring audit to PatternEngine JSDoc — all 28 IDs verified [BATCH:P0]
- docs(workflows): upgrade /intake with batch eligibility gates and group assignment logic
- docs(plans): commit upgraded AI-First plan for delete-symphony-arch [BATCH:P0]
- docs(plans): commit upgraded AI-First plan for retire-rbm-simulator [BATCH:P0]
- docs(plans): complete 100% plan coverage for all tonight's tasks
- docs(plans): backfill plan files for all tonight's EPIC-004 tasks
- docs(plans): add task plan files and wire links into Bucket List

---

## [2.3.0] - 2026-04-21

### ✨ Features
- **ZENGGE Protocol Oracle Lab — Phase 2 (Diagnostic Lab)**: Added 4 new interactive accordion panels to the `🔬 Oracle` tab for hardware verification of APK-hypothesized opcodes: `0x41` Settled Mode (effectId 1–33, FG/BG colors, speed/bright, direction), `0x43` Multi-Sequence (50-button tap grid, up to 50 effect IDs), `0x53` Live Pixel Stream (gradient start/end pickers, 1–60fps loop + single-frame shot), and `0x56/57/58` Scene Management (slot 0–31 picker + QUERY/ACTIVATE/DELETE). All panels labeled `[HYPOTHESIS]` — Oracle-gated before production promotion.
- **ZENGGE Protocol Oracle Lab — Phase 1**: Added auto-streaming 323B pixel frame tester with continuous `0x53` multi-MTU write loop for real-time hardware verification. Integrated hardware response sniffer into the Oracle tab.
- **ZENGGE Protocol Oracle Lab — Phase 0**: Deployed the core Protocol Oracle tab with dedicated `0x59`, `0x51`, `0x62`, and `0x73` builder panels — all with live hex byte previews, TX buttons wired to BLE, and response logging.
- **Math Synthesizer Pattern Engine**: Implemented 28 math-synthesized pattern templates replacing legacy hardcoded arrays. All pattern computation routes through the Math Synthesizer.

### 🐛 Bug Fixes
- **0x73 Builder — APK-Verified 13-Byte Format**: Rewrote the Symphony Mode `0x73` panel to use the confirmed 13-byte payload structure: `musicMode`, `micSource` (`0x26` App / `0x27` Device), `isOn` byte, C1 RGB, C2 RGB, sensitivity, brightness, checksum. Replaced the matrix-style picker with an explicit mic source button pair.
- **FTUE — Auto BLE Permission Dialog**: Auto-triggers the native Bluetooth permission dialog on `PermissionsOnboardingScreen` mount, eliminating the manual setup step that caused user drop-off.
- **BLE Solid/Camera Mode**: Unified `SOLID` and `CAMERA` modes to `0x59 FREEZE` command, bypassing deprecated firmware commands that caused hardware lockups.
- **Pattern Engine Routing**: Routed `applyFixedPattern` through the Math Synthesizer pipeline, eliminating the last legacy pattern bypasses.
- **Session Summary Modal**: Added missing `Platform` import resolving a runtime `ReferenceError` on Android.
- **TSC Strict Mode**: Resolved TypeScript compiler errors blocking Metro bundler in strict mode.
- **Visualizer Dead Import**: Removed lingering `ZenggeVisualizerMath` import after Math Synthesizer migration.
- **`setMusicConfig` Caller Sync**: Updated all 4 call sites (`useMusicMode`, `useControllerDispatch`, `useProtocolBuilder`, `ZenggeAdapter`) to the new 13B APK-verified signature `(musicMode, 0x26|0x27, isOn, c1, c2, sens, bright)`. Dropped the legacy `matrixStyle` parameter.

### ⚡ Performance
- **Account Overview Mount**: Parallelized authentication and data fetching on `useAccountOverview` mount, eliminating sequential waterfalls. Added skeleton loading states for instant perceived responsiveness.

### 🔧 Maintenance / Protocol
- **`ZenggeProtocol` — New Methods**: `setSettledMode()` (0x41), `setEffectSequence()` (0x43), `streamPixelFrame()` (0x53), `setMusicConfig()` (13B APK format), `setMusicConfigLegacy()` (12B preserved for diffing).
- **APK Protocol Audit**: Full reverse-engineering documentation of `0xA3` hardware opcodes committed to `ZENGGE_PROTOCOL_BIBLE.md` and `SK8Lytz_App_Master_Reference.md`.
- **Web Compatibility**: Silenced React Native Web CSS `boxShadow`/`shadow*` deprecation warnings in `Sk8LytzProgrammer`.
- **Legacy Fixed Mode Removed**: Ripped out the legacy fixed mode from DockedController navigation — replaced by the Math Synthesizer pattern system.
- **APK Path Fix**: Corrected build tooling `APK` path to use Gradle default `app-release.apk` output name.

### 📖 Documentation
- Synced Master Reference with AsyncStorage registry and Math Synthesizer architecture updates.

---

## [2.2.0] - 2026-04-21

### ✨ Features
- **Scraper Guillotine REST API**: Fully functionalized the `/api/scraper/blocklist` and `/api/scraper/spots/:id/purge` endpoints across the CCTower. Adding keywords to the Phase 1 Blocklist GUI now instantly triggers a multi-table database purge via programmatic SQL drops.
- **Dynamic Orchestrator Injection**: The master Google Sweep orchestrator now intercepts internal blocklist keywords natively, syncing to the database at launch to prevent historical re-ingestion.

### 🛡️ Pipeline Defenses
- **Positive Heuristic Sieve (Phase 1)**: Deployed a dual-verification regex inside `GooglePlacesProvider.ts`. Generic municipal venues (Community Centers, Meeting Halls, Plazas, Fairgrounds) and open "Parks" natively returned by Google Places are now instantly vaporized *before* queue insertion unless the title explicitly guarantees roller-rink vernacular (`Skate`, `Rink`, `Roller`, `Arena`). 
- **Corporate Exclusion Net**: Dramatically expanded the native Google `RETAIL_BLOCKLIST` array to actively identify and drop Ice Rinks, professional Hockey Arenas, Mega-Casinos, and big-box mega-chains (Hobby Lobby, Barnes & Noble, Dillons). Total Phase 1 queue fidelity is now operating at unprecedented validation thresholds.

### 🐛 Bug Fixes
- **Dashboard Guillotine Soft-Lock**: Re-wired CSS index layers and React button states that previously blocked the "Block & Purge" execution on Phase 1 data rows.
- **API Crash Loop Rescue**: Hunted down and eradicated a legacy `pushLog()` logging dependency crash that was terminating SQL `DELETE` sequences mid-flight, successfully restoring end-to-end Guillotine operations.

---

## [2.1.2] - 2026-04-21### ✨ Features
- **Dual-Mode Coverage Map (Phase 6)**: The Databank QA map now has a mode toggle — **📊 Quality Mode** colors states by dominant pipeline status (existing behavior); **🚀 Published Mode** shows a green gradient by % of records `is_published = true`, giving a live per-state app coverage view.
- **State-Scoped Publish / Retract**: Clicking any state on the map (or typing a 2-letter code in the search box) now reveals contextual **"🚀 Publish XX"** and **"↩ Retract XX"** buttons in the Databank toolbar. These call new CCTower endpoints (`POST /api/promote-state/:state` / `POST /api/unpublish-state/:state`) to promote or retract all eligible records for a single state atomically.
- **Coverage Map Auto-Refresh**: The coverage map now refreshes after every `promoteSpot`, `bulkPromote`, `promoteState`, and `unpublishState` action — state colors update in real-time after publish operations.

### 🐛 Bug Fixes
- **GoogleSweep Status Preservation**: Re-running the Google Places sweep would overwrite `MEDIA_READY` (and any higher-phase) records with `verification_status: 'ENRICHED'`, silently downgrading them. Fixed by splitting the record into `metaRecord` (Google factual data, no status) and `freshRecord` (metaRecord + `ENRICHED` for brand-new inserts only). All update/upsert-on-conflict paths now use `metaRecord`, preserving pipeline status across re-seeds.

### 🗑️ Data Operations
- **OSM Record Purge**: Deleted 230 PENDING OSM-legacy records (`google_place_id IS NULL`). All 913 remaining records are 100% Google Places seeded (775 ENRICHED + 138 MEDIA_READY), zero null states. The Phase 1 coverage map now accurately reflects real state coverage.

### 🌐 Scraper Pipeline (Daemon — separate from app)
- All changes isolated to `tools/scraper/CCTower.ts`, `tools/scraper/GoogleSweep.ts`, and `tools/scraper-dashboard/src/App.tsx`. Zero mobile `src/` code was modified.

---

## [2.1.1] - 2026-04-21


### 🐛 Bug Fixes
- **Critical: Bulk Publish Skipped MEDIA_READY**: The `/api/promote-all` route only promoted `VERIFIED` and `ENRICHED` records — `MEDIA_READY` (the Photographer's final output) was excluded. 129 records were silently ineligible for promotion. Fixed by adding `MEDIA_READY` to the OR clause.
- **Critical: Phase 4 Queue Wrong Status**: The Photographer's processing queue (Phase 4) was querying `verification_status = INDEXED` — a dead status the current pipeline never writes. Queue now correctly filters `candidate_photos IS NOT NULL AND photos IS NULL`, matching the Photographer's actual work backlog.
- **Critical: Phase 5 Queue Wrong Status**: The Publisher queue (Phase 5) was showing all `ENRICHED` records (784 items) instead of `MEDIA_READY` records (the correct input), misrepresenting the publication queue entirely.
- **Phase 4 Description Stale Copy**: Phase 4 explainer block referenced "Instagram / Yelp API" — a Phase 1-era description. Now accurately describes the Photographer daemon (OG image, DOM media, Street View fallback → MEDIA_READY).
- **Phase 5 Description Stale Copy**: Phase 5 explainer block described "WebP CDN media engine" — this is Phase 4's job. Now correctly describes the Publisher Gate.
- **Phase 4 Flow Input Metric**: Left metric showed `enrichedCount` (800+ records, meaningless as a Photographer input). Now shows `candidatesReadyCount` — the exact count of records with `candidate_photos IS NOT NULL AND photos IS NULL`.
- **Log Panel Filter Missing Photographer**: Phase 4 tab was falling through to `return true` (showing all logs). Now correctly filters to `source === 'Photographer'` only.

### ⚡ Performance
- **Smart Queue Polling**: `fetchQueue` was making 6 parallel API calls to CCTower every 5 seconds regardless of which tab was active. The polling interval now only re-fetches the currently active tab's phase queue (+ recent), reducing background requests by ~83% during normal dashboard use.

### 🔧 Maintenance
- **New CCTower Metric**: `/status` endpoint now returns `candidatesReadyCount` — the real-time count of Photographer candidates — used by both Phase 3 output and Phase 4 input flow visualizers.

### 🌐 Scraper Pipeline (Daemon — separate from app)
- All 8 correctness fixes are isolated to `tools/scraper/CCTower.ts` and `tools/scraper-dashboard/src/App.tsx`. Zero mobile `src/` code was modified.

---

## [2.1.0] - 2026-04-20


### 🐛 Bug Fixes
- **SSOT Bypass Eliminated**: Removed `AsyncStorage` import from `useHardwareNotifications.ts`. All 3 direct `setItem('@Sk8lytz_device_configs')` calls (RF config, LED probe, hardware probe) are now routed through `DeviceRepository.updateConfig()` — eliminating split-brain corruption on every BLE connect cycle.
- **Provisioning Schema Corruption Fixed**: Replaced the 35-line direct Supabase write loop in `runAutoProvisioning` (which used raw MAC addresses as `id`, bypassing tombstone guards) with `repo.saveGroupTransactional()`. Removed the rogue `supabase` import from `useDashboardGroups.ts`.
- **Stale Closure in Optimistic BLE**: Added `disableOptimisticUI` and `disableHaptics` to the `useOptimisticBLE` `useCallback` dependency array — App Manager runtime toggles now take effect immediately without waiting for an unrelated re-render.
- **DockedController Platform Import**: Added missing `Platform` import in `DockedController.tsx` (pre-existing TSC error caught by release gate).

### 🔧 Maintenance
- **Settings Table Isolation**: Migrated all SK8Lytz admin settings from the shared `app_settings` table to a new, dedicated `sk8lytz_app_settings` table with `updated_at` auto-trigger and seeded keys. The Neogleamz Inventory app is unaffected.
- **Orphaned Constant Removed**: Removed unused `PATTERNS_KEY` constant from `DeviceRepository.ts`. The key `@Sk8lytz_last_group_patterns` is documented as an intentional UI-local concern owned by `useDashboardGroups`.
- **TypeScript Config**: Added `scratch/` to `tsconfig.json` exclude list to prevent non-production exploratory scripts from polluting the release gate.

### 🌐 Scraper Pipeline (Daemon — separate from app)
- **Phase 3 Queue Mirroring**: Queue now mirrors the Indexer RPC, surfacing `ENRICHED + crawled=false` records for targeted re-processing.
- **Headless Browser**: Switched Puppeteer to `headless: 'new'` mode to suppress deprecation warnings.
- **Indexer v2**: Structured extraction pipeline for hours, adult-night schedules, pricing, and events.

---

## [1.12.4] - 2026-04-19


### 🐛 Bug Fixes
- **Data Sync Regression**: Resolved offline synchronization integrity regressions across the device and group pipelines, ensuring cloud connectivity does not unintentionally wipe locally cached devices or phantom groups.
- **Pro Effects Controller Lock**: Enforced single MTU strict write constraint logic and abolished multi-packet BLE chunking payloads to rectify severe hardware-level protocol freezes.

---

## [1.12.3] - 2026-04-18

## [1.12.2] - 2026-04-18

### ðŸš€ Features
- **Voice Engine Deprecation**: Surgically stripped the `@react-native-voice/voice` native dependency and the Voice Command Engine UI from the Dashboard to reduce the Android APK bundle size and improve app latency.

### ðŸ› Bug Fixes
- **React Native Web Hydration Regression**: Pinned `react-dom` explicitly to version `19.2.0` to resolve an invariant crash during web startup caused by NPM module resolution drift.

---

## [1.10.0] - 2026-04-18

### ðŸš€ Features
- feat(telemetry): implemented passive, zero-performance-impact ambient BLE telemetry harvester
- feat(db): integrated `discovered_devices_telemetry` table with PostGIS location batching for global hardware heatmapping
- feat(ux): added silent background location fetcher via `LocationService` to avoid UI interruption/permission modals during background sync

### ðŸ› Bug Fixes
- fix(dashboard): resolved "Ghost Device" and orphaned group split-brain regressions by enforcing native `Alert` dialogs on RLS deletion failures and patching PostgreSQL policies

## [1.9.0] - 2026-04-18

### âœ¨ Features
- feat(telemetry): implement robust dual-probe harvesting pipeline (0x63 Hardware Config + 0x2B RF Remote State)
- feat(ui): deploy high-fidelity `HardwareStatusPills` component to Dashboard and Setup Wizard
- feat(ui): optimize `AuthScreen` layout and significantly reduce the visual footprint of the offline mode controls

### ðŸ› Bug Fixes
- fix(scanner): corrected product classification logic for HALOZ and SOULZ devices via flexible prefix matching
- fix(onboarding): resolved telemetry persistence regression where hardware settings were overwritten by defaults during setup

### ðŸ”§ Maintenance & Infrastructure
- chore(agents): restored global workflow registry with missing YAML frontmatter descriptions
- chore(release): bumped Android `versionCode` to 19

---

## [1.8.20] - 2026-04-18

### âœ¨ Features
- feat(ux): implement Vivid Flush Slab architecture for Camera Tracker
- feat(admin): build zero-edge user management architecture
- feat(etl): implement comprehensive Cultural Daemon Pulse UI monitor inside Admin Tools
- feat(etl): decoupled cultural enrichment daemon with PM2 queue
- feat(ble): inject GATT 133 retry bumper and elevate connection priority to High

### ðŸ› Bug Fixes
- fix(dashboard): stabilize profile service and resolve type regressions
- fix(permissions): decouple push notification init and reorder priority
- fix(ble): convert payload engine to synchronous interleaved chunking to prevent native lockups
- fix(ble): parallelize group teardown and payload pipeline
- fix(db): add missing delete RLS policies to prevent ghost device retention on deregistration

### ðŸ”§ Maintenance & Architecture
- refactor(ble): rip out firmware and hardware state query bloat from connectToDevices to eliminate lag
- docs: formalize health sweep pre-flight requirement and capture architecture anomalies
- docs: update BLE stability constraints to memorialize GATT 133 retry and high priority connection architectural invariants

---

## [1.8.19] - 2026-04-17

### âœ¨ Features
- feat(hardware): option to delete hardware on dashboard and enforce MAC uniqueness
- feat(permissions): architect universal routing to Global Permissions Onboarding UI

### ðŸ› Bug Fixes
- fix(ble): isolate probing to setup wizard and remove diagnostic probe alerts
- fix(hardware): remove legacy group migration to prevent case-sensitive recursive ghost injection
- fix(hardware): normalize all device MACs to uppercase â€” single device identity invariant
- fix(hardware): eliminate split-brain duplicate groups on setup completion
- fix(groups): sync account-manager renaming with dashboard ui cache
- fix(groups): sync account-manager device grouping cache with dashboard deletes

### ðŸ”§ Maintenance
- chore(db): add nuclear option script to completely flush all devices and groups database-wide
- chore: major repository cleanup

---

## [1.8.18] - 2026-04-17
- AppLogger telemetry hardening
- DockedController diet phase 2

---

## [1.8.17] - 2026-04-17

### ðŸ”§ BLE Pipeline Overhaul (`fix/ble-pipeline-overhaul`)
- **Gate Semaphore**: Implemented `bleGateRef` FSM (`IDLE|SCANNING|CONNECTING|DISCONNECTING|RECOVERING`) to serialize all BLE lifecycle operations and eliminate GATT collisions
- **connectToDevice Deleted**: Removed singular device connector; unified all connections through `connectToDevices` group path
- **AutoRecovery v2**: Replaced `useBLEWatchdog.ts` (deleted) with AbortController-based cancellation and 8-retry ceiling
- **HAL Seam**: Created `IControllerProtocol.ts` interface + `ZenggeAdapter` + `ControllerRegistry` for future multi-hardware support
- **Write Type Propagation**: `writeToDevice` now returns `Promise<boolean | 'partial'>` across all 15 consumers
- **Auto-Connect Observer**: Debounced with gate checks to prevent concurrent GATT boots

### ðŸ›¡ï¸ Observability & Error Handling
- **Silent Catch Purge**: Replaced 15 empty `catch(e){}` blocks with `AppLogger.warn()` across 6 BLE hooks
- **BLEErrorBoundary**: New crash recovery component wrapping DockedController in DashboardScreen
- **Consoleâ†’AppLogger Migration**: Converted 20 raw `console.log/warn/error` calls in 9 hooks to AppLogger
- **Telemetry Hardening**: Added `PROMISE_REJECTION` event type, unhandled rejection handler (web), `APP_FOREGROUNDED` event, `SCREEN_OPENED` telemetry for 4 screens, `__DEV__`-guarded console.error in AppLogger internals

### ðŸ—ï¸ Architecture Diet (`chore/docked-controller-diet`)
- **Color Math Dedup**: Replaced 8 inline hueâ†’hex lambdas with `ColorUtils.hueToHex()`; centralized `COLOR_PRESET_PALETTE` and `PRESET_HUE_MAP`
- **Persistence Hook**: Wired orphaned `useControllerPersistence` hook, deleted 45 lines of duplicate inline AsyncStorage effects
- **BLE Dispatch Hook**: Extracted 6 functions into `useControllerDispatch.ts` (185 lines): `sendColor`, `applyFixedPattern`, `applyStaticModePattern`, `applyEmergencyPattern`, `handleMusicChange`, `clampSpeed`
- **FavoritePromptModal**: Extracted inline modal JSX into `FavoritePromptModal.tsx` (69 lines)
- **Result**: DockedController slimmed from 97KB/2,080 lines â†’ 87KB/1,874 lines (-10KB, -206 lines)

### ðŸ§¹ Housekeeping
- **Legacy Purge**: Eliminated all `ng_*` AsyncStorage keys from codebase; fixed AdminToolsModal reading dead `ng_device_configs`
- **Scraper Caches**: Added `nominatim_cache.json` and `state_caches/` to `.gitignore` to fix persistent CRLF dirtying
- **Master Reference**: Updated storage keys, watchdogâ†’AutoRecovery docs, gate semaphore docs, writeToDevice type docs

---


## [1.8.16] - 2026-04-17

### âœ¨ Features
- feat(schema): add is_featured column to skate_spots schema and harvester pipeline

---

## [1.8.15] - 2026-04-17

### âœ¨ Features
- feat(crewz): rebrand UI exclusively to CREWZ and migrate national harvest pipeline to vertical synchrony
- feat(map): unify crew hub map strategy with interactive clustering and OS-specific fallbacks
- feat(crew): unified radius UI, flex map filters, dynamic local memory, and smart skate spots

### ðŸŽ¨ Style
- style(crewz): fix grammar by restoring singular CREW capitalization in UI display text

### ðŸ“– Documentation
- docs(schema): sync Master Reference with new skate_spots DB schema

---

## [1.8.14] - 2026-04-17

### ðŸ“– Documentation
- docs(discord-bridge): expand developer portal setup instructions

---

## [1.8.13] - 2026-04-17

### âœ¨ Features
- feat(discord-bridge): implement bidirectional telemetry agent link via powershell keystrokes
- docs(discord-bridge): add setup and usage instructions
- chore(agents): add standalone discord bridge startup workflow and inject into hello routine

---

## [1.8.12] - 2026-04-17
- fix(voice): resolve null reference on launch and harden cleanup handler

---

## [1.8.11] - 2026-04-17

### âœ¨ Features
- feat(security): implement RF remote ID discovery and UI audit

---

## [1.8.10] - 2026-04-17

### ðŸ”§ Maintenance
- style(theme): enforce tokenized 8pt spacing grid app-wide via codemod
- chore(database): audit RLS performance and seal telemetry access vectors
- docs(bucket-list): sweep backlog, clean tasks, and sync core 8pt grid metrics

---

## [1.8.9] - 2026-04-17

### âœ¨ Features
- feat(dashboard): extract fixed mode into dedicated docked panel with strobe/blink support

### ðŸ› Bug Fixes
- fix(ble): inject neverForLocation plugin flag and sync scanner race blocks
- fix(ble): resolve device settings probing and group connection regressions
- fix(ble): resolve scanner race condition and manifest permissions gap
- fix(ble): revert manual BLE permissions blocking Android 12 scanning
- fix(permissions): stop Android auto-backup FTUE bypass, decouple dashboard Bluetooth spam, and sync AccountManager ledger
- fix(ble): decouple keepAlive from GATT lock to restore wizard polling regression
- fix(ble): hotfix zero-day android gatt exception by restoring scan lock and queue resumer
- fix(ble): resolve group firmware sync race condition and popcorn effect

### ðŸ”§ Maintenance
- chore(dashboard): decompose DashboardScreen monolith (-450 lines, -32.9%)
- chore(docked): decompose DockedController JSX into 6 memoized sub-panels
- build(version): bump version to 1.8.7 for permission ledger fix

---

## [1.8.8] - 2026-04-14

### ðŸš‘ Emergency Hotfixes

**Android 12+ BLE Scanning Deadlock**
- **Restored neverForLocation Flag** (`app.json`): Reverted the raw injection of `android.permission.BLUETOOTH_SCAN`. Manually defining this in the manifest overrode `react-native-ble-plx`'s auto-configurator, intentionally dropping the `usesPermissionFlags="neverForLocation"` attribute. Without this flag, Android 12+ enforced strict Location tracking requirements on all BLE scanning events and silently blockaded the Bluetooth radio at the OS level since we do not request background location tracking natively.

---

## [1.8.7] - 2026-04-14

**Permission Ledger Desync & FTUE Bypass**
- **Android Auto-Backup Override** (`app.json`): Added `allowBackup: false` to the Android manifest schema. Previously, when a user uninstalled and reinstalled the application, Android secretly restored the `@Sk8lytz_has_seen_permissions` flag from Google Drive, permanently lock-bypassing the `PermissionsOnboardingScreen` FTUE gate.
- **Unified Permission Ledger** (`PermissionService.ts`, `DashboardScreen.tsx`): Completely stripped the aggressive `useEffect` inside DashboardScreen that was blindly spamming legacy Bluetooth permission prompts on every single mount. Deleted the legacy, isolated `blePermissions.ts` orchestrator and explicitly wired the BLE engine directly into the unified `AccountManager` ledger.
- **Android Manifest Alignment** (`app.json`): Manually injected `android.permission.BLUETOOTH_SCAN` and `ACCESS_FINE_LOCATION` into the explicit permissions array. Previously, the `react-native-ble-plx` plugin silently handled this on compile, but dynamically calling `PermissionsAndroid.check()` for OS-level verifications immediately failed on AccountManager loads, causing the privacy toggles to always evaluate as `false`.

---

## [1.8.6] - 2026-04-14

**Hardware Wizard Regression**
- **Scanner Polling Restored** (`useBLEScanner.ts`, `useBLE.ts`, `HardwareSetupWizardScreen.tsx`): The `3bb9b691` deploy (v1.8.3) improperly hijacked `options.keepAlive: true` and forced it to act as a hard kill-switch for the entire scanner to satisfy a state machine standard. This broke the `HardwareSetupWizard` which relies on `keepAlive: true` to continuously poll for new devices over consecutive 2-second heartbeats without repeatedly wiping the list of discovered devices. The logic has been completely decoupled: `keepAlive: true` once again simply instructs the scanner to "scan without wiping the cache", and a new, explicit `stopScanner()` method handles Android GATT suspension.

---

## [1.8.5] - 2026-04-14

**Zero-Day Connection Blocker**
- **Android GATT/LE Scan Exception** (`useBLE.ts`, `useDashboardAutoConnect.ts`): Re-injected the hard `bleManager.stopDeviceScan()` immediately prior to GATT initialization. The v1.8.4 deployment removed this under the mistaken assumption it was sabotaging the auto-scanner. Without it, Android violently rejects all incoming GATT connection intents (`operation canceled`) because native LE background scans consume the BLE adapter's duty-cycle. To resolve the 'Auto-Scanner Sabotage' properly, a new recursive `scanForPeripherals()` resume trigger was injected into the `.finally()` block of `useDashboardAutoConnect`, ensuring multi-skate discovery continues *after* the GATT initialization completes.

---

## [1.8.4] - 2026-04-14

**BLE Group Concurrency & Sync**
- **GATT Initialization Race** (`useBLE.ts`): Blocked early UI payload dispatch by delaying `setConnectedDevices` until after the hardware completes its critical 600ms boot-up MTU queries. This prevents the "GATT ambush" where the React UI blasted heavy animation payloads at a skate before the chip was ready, causing one skate in a group to ignore all commands.
- **Auto-Scanner Sabotage** (`useBLE.ts`): Removed aggressive `bleManager.stopDeviceScan()` from the inner loop of `connectToDevices()`, allowing the continuous background observer in `useDashboardAutoConnect` to properly queue and connect the second skate rather than randomly stranding it in the ether.
- **Popcorn Latency Effect** (`useBLE.ts`): Converted the sequential `for/await` loop inside `writeToDevice` into an aggressive, highly concurrent `Promise.allSettled(targets.map(...))` array. This guarantees both write intents enter the BLE adapter simultaneously, nearly eradicating visual desync across the group.

---

## [1.8.3] - 2026-04-14

**Zero-Day Permission Soft-Lock**
- **Boot Desync Eradicated** (`PermissionService.ts`, `App.tsx`): Prevented `syncSystemPermissions` from silently opting out users from all hardware capabilities. On fresh installs, Native OS state is 'undetermined', which evaluated as `false`, triggering a flawed OS Desync Sweep that aggressively forced `@sk8lytz_permissions_optout` to TRUE without prompting. Users were permanently soft-locked. Neutered the sequence.
- **Android 12+ BLE Prompts Fixed** (`PermissionService.ts`): Removed `ACCESS_FINE_LOCATION` from the `PermissionsAndroid.requestMultiple` array for devices API 31+. Requesting fine location without coarse location on Android 12 immediately throws a rejection, collapsing the UI prompt natively before the user ever sees it.

---

## [1.8.2] - 2026-04-14

### ðŸ› Bug Fixes

**BLE Group Stability â€” 5 Surgical Fixes**
- **Critical Write Reconciliation Loop** (`useBLE.ts`): `writeToDevice` returned `void`, cast to `Promise<boolean>` but resolving as `undefined` at runtime â€” every single BLE write was falsy, triggering optimistic UI rollback on every command. Fixed to return honest `boolean` per-device success.
- **Scanner FSM Poisoning** (`useBLEScanner.ts`): `keepAlive` calls mutated `scannerState` to `'SCANNING'` before checking the flag, causing`derivedBleState` to report `SCANNING` immediately after a successful group connection. Fixed by early-exiting before any state mutation.
- **Ghost Device Infinite Loop** (`useBLEAutoRecovery.ts`): Recovery loop had no retry ceiling â€” a Zengge chip in a GATT soft-lock would loop forever, permanently blocking all writes to the dropped device. Added 8-attempt ceiling with device ejection on exhaustion.
- **Premature Write Guard** (`DockedController.tsx`): Guard missed `CONNECTING` and `PROBING` states, allowing `useEffect` writes to race `discoverAllServices` during group connect. Simplified to canonical `bleState !== 'READY'`.
- **Duplicate Boot Scan System** (`DashboardScreen.tsx`): Legacy `handleScan` + `hasAutoScanned` useEffect (System B) racing against `useDashboardAutoConnect` (System A) on every launch. System B's `runAutoProvisioning()` at t+8s mutated device state mid-GATT handshake, permanently corrupting one device in every pair. System B removed entirely.

**Naming Consistency â€” 3 Missed Gaps**
- **Scanner Storage Key** (`useBLEScanner.ts`): Scanner read `ng_registered_devices` (legacy) instead of `@Sk8lytz_registered_devices` â€” MAC deduplication was completely blind post-migration.
- **swapDevicePositions Naming** (`useRegistration.ts`): Rebuilt `device_name` as `"HALOZ Left"` bypassing `NamingUtils`. Now uses `getDefaultDeviceName(mac) + position`.
- **Auto-Provisioning Group Name** (`useDashboardGroups.ts`): Built group names as `"HALOZ SK8Lytz"` inline instead of `"My SK8Lytz HALOZ"` via `getDefaultGroupName()`. Same hardware produced different group names per registration path.

---

## [1.8.1] - 2026-04-14

### ðŸ› Bug Fixes
- **Critical Dashboard Stability**: Resolved a hook split-brain bug in `DashboardScreen.tsx` where `isActuallyConnected` was computed in two separate, desynchronized locations â€” an inline expression passed to `useDashboardAutoConnect` and a `useMemo` defined ~150 lines later. Hoisted `displayConnectedDevices`, `isActuallyConnected`, and `isGrouped` above all consumers to establish a single canonical source of truth. This eliminates the root cause of the potential `Rendered more hooks than expected` crash during BLE reconnect cycles.
- **TS Compliance**: Fixed `PermissionService` default ledger initialization from `{}` to a fully-typed `DEFAULT_LEDGER` constant, and resolved `delete_account` RPC type narrowing error in `AccountModal`.

---

## [1.8.0] - 2026-04-14


### âœ¨ Features
- **Legal Compliance**: Executed granular legal compliance architecture and Account Deletion workflow.
- **Telemetry Convergence**: Standardized device naming syntax and unified global namespace to `@Sk8lytz_`.
- **Ghost Protocol**: Built pessimistic silent auto-recovery engine for Soft Disconnect drops.
- **Skate Discovery ETL**: Evolved Headless Scraper pipeline into a zero-api DOM crawler with automated background job runner logic for stealth.
- **Magic Workflows**: Added `/run-scraper` AI trigger.

### ðŸ› Bug Fixes
- **BLE Engine**: Neutered over-aggressive hardware watchdog to resolve connection dropouts. Tore down legacy Soft Disconnect UI alerts.
- **Dashboard Hook Ordering**: Solved infinite 'Rendered more hooks' React error during async group recovery via closure stabilization.
- **Account Management**: Resolved `AccountModal` crash loops and network hangs.
- **Expo Auth Deep Linking**: Repaired dynamic `expo-auth-session` scheme callbacks, fixing Web 400 errors.

### ðŸ”§ Maintenance & Performance
- **Ghost Reconciliation**: Enforced true pessimistic reconciliation loop within `DockedController`.
- **Fast-Lane Telemetry**: Refactored error-boundary catch blocks to bypass standard spool queues for VIP AppLogger payloads.
- **Agent Governance**: Upgraded internal Bucket List parsing and enforced explicit PUSH blocks in AI workflow pipelines.
- **UI Simplification**: Compressed offline buttons and deprecated HIBP string literals.

## [1.7.1] - 2026-04-14
### ? Features
- feat(governance): implement backlog grooming algorithm and dependency tagging
- feat(workflows): append publish branch merging and cloud deployment pipeline to ship-it release protocol

### ?? Maintenance
- chore(performance): ast-driven purge of dead imports
- chore(audit): document DDA findings and batch-sync supabase types

### ?? Documentation
- fix(docs): synchronize pie charts with actual task distribution and historical offset
- chore(docs): move music intel epic to icebox and add mermaid dependency graph
- chore(docs): mark dead-code purge as completed in bucket list
- chore(docs): standardize estimated time to completion across all remaining bucket list tasks
# Changelog


## [1.12.1] - 2026-04-18

### Bug Fixes
* **environment:** migrated \pp.json\ to dynamic \pp.config.js\ for secure Google Maps API key injection via \.env\ to resolve native MapView crashes on release builds.
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.12.0] - 2026-04-18

### Changed

- **Device Pipeline Stabilization**: Extracted the ultimate `DeviceRepository` singleton service, completely decoupling device, group, and configuration persistence from the React presentation layer to guarantee atomicity.
- **State Consolidation**: Executed the death of `useDeviceFleet`, unifying all local storage and RPC queries into a single shared authority to eliminate race conditions.
- **Dashboard Context Sharing**: Re-architected `HardwareSetupWizardScreen` to consume the global Dashboard BLE Context Provider natively, eradicating redundant concurrent `useBLE` instances that were causing BLE transport pipeline crashes.

### Fixed

- **TS Type Safety**: Resolved strict typing regressions regarding Supabase dynamic model assignment payload objects in `AdminPicksScheduler` and `CrewService`. 
- **Identity Invariant**: Hardened MAC address lookup strings universally, enforcing `toUpperCase()` transformation and database-layer `ilike` operators to destroy "ghost device" synchronization bugs.

## [3.3.1] - 2026-05-02
 
### Fixed
 
- **FTUE Navigation Decoupling**: Decoupled the Dashboard UI from the raw background BLE auto-connect observer state. Resolved a critical UX race condition where background auto-connections would falsely hijack the screen during the First Time User Experience (FTUE) setup wizard.
- **Controller Background Persistence**: Replaced the Controller 'Force Disconnect' header action with a non-destructive 'Minimize' action (`chevron-down`), allowing users to return to the Dashboard while maintaining a hot, zero-latency BLE background connection to their hardware.
- **Boot Sync Race Condition**: Delayed UI loading indicators to explicitly wait for `repo.syncFromCloud()`, preventing the app from falsely trapping established users in the Setup Wizard due to local cache delays.

## [1.7.0] - 2026-04-14

### Changed

- **BLE Engine Decoupling**: Refactored the monolithic `useBLE` God Object (1,200 LOC, 42KB) into a lightweight Domain-Driven Architecture. Isolated strictly-scoped hooks (`useBLEScanner`, `useBLEWatchdog`, and `blePermissions`) while explicitly preserving a centralized Thin Orchestrator to prevent native Android GATT 133 bugs.
- **Workflow Architecture**: Injected exact LLM Token consumption estimates directly into the Bucket List system, drastically minimizing redundant workflow decision overhead.


## [1.6.0] - 2026-04-14

### Changed

- **Telemetry Integrity**: Performed complete Supabase RLS security audit. Deprecated universal anon INSERT protocols for `telemetry_snapshots` and `led_diagnostics`, enforcing strict authentication barriers for all telemetry logging to prevent data spoofing.
- **Rendering Performance**: Implemented rigorous React.memo and useMemo optimizations across SVG Cartesian coordinate mapping components (`AnalogGauge`), saving critical JS thread FPS during Dashboard data cascades.
- **Query Hardening**: Applied an automated `idx_parsed_session_stats_session_id` B-Tree Database Migration mapping to `parsed_session_stats`, eliminating Postgres Sequential Scan fallbacks.
- **Agentic Workflows**: Integrated `turbo-all` autonomy scripts to speed up safe administrative and Git cleanup macros.

## [1.5.0] - 2026-04-14

### Changed

- **God Object Elimination**: Refactored the massive 2,857-line `DockedController.tsx` down to a thin 2,441-line orchestrator by decoupling logic into 4 specialized domain hooks (`useMusicMode`, `useCuratedPicks`, `useAppMicrophone`, `useControllerAnalytics`) while strictly adhering to BLE singleton concurrency safety.

## [1.4.2] - 2026-04-13

### Changed

- **Telemetry Architecture**: Redesigned device telemetry ingestion from an un-gated firehose into a Constraint-Based Auditing model. Implemented a unified JSONB ingestion model (`telemetry_snapshots`), local spooling/batching, and a VIP Fast-Lane for critical errors to minimize cloud overhead and ensure data integrity.

## [1.4.1] - 2026-04-13

### Added

- **Hardware Watchdog**: Autonomous BLE 'Self-Healing' loop â€” detects hardware soft-locks, clears GATT buffers, and silently relatches connections to maintain stability.
- **Diagnostic Controls**: Extracted Sniffer/Telemetry Diagnostics to its own toggle state in the Diagnostics Lab UI, rather than linking it to the modal open state.

### Changed

- **BLE Notification Mailroom**: Decomposed the monolithic BLE notification callback into 4 performant, single-responsibility handlers (gatekeeper, sniffer, pure config parser, and delta state writer), vastly minimizing UI-thread parser lag on heavy loads.
- **Dashboard Deconstruction**: Executed massive refactor of `DashboardScreen.tsx` (previously a 95KB God Object). Stripped out 48 hooks and decomposed state management into structured modular domains.

## [1.4.0] - 2026-04-13

### Changed

- **Telemetry Efficiency**: Resolved extreme database bloat by rewriting the multi-device array unrolling into a structured group_id mapping constraint, preventing massive redundancy in Postgres parsed logs during group sessions.

## [1.3.0] - 2026-04-13

### Changed

- **Admin Tools Refactor**: Broken down the monolithic 800-line `AdminToolsModal.tsx` God Object into feature-specific admin modules (`DeviceTab`, `StatsTab`, `AdminTab`, `AppManagerModal`, `ProductManagerModal`, `ConfirmDeleteModal`) leveraging a clean Controller architectural pattern.

## [1.2.2] - 2026-04-13

### Changed

- **Tokenized Spacing Standard**: Enforced an 8pt spacing grid (`Spacing` tokens) app-wide via aggressive codemod to eliminate thousands of hardcoded layout magic numbers, establishing perfect visual rhythm.

### Fixed

- **Profile Persistence Data Loss**: Hardened self-healing profile logic and injected `display_name` into the Supabase auth signup metadata payload to guarantee profile name persistence.

## [1.2.1] - 2026-04-13

### Changed

- **UI Degradation**: Temporarily hid the Voice Command FAB from the Dashboard until the native engine module is repaired.

### Fixed

- **Security**: Mitigated 10 severe dependencies (including critical `xmldom` injection) by forcibly overriding the resolving config-plugins within the Voice library dependency tree, keeping voice dependencies from downgrading violently.

## [1.2.0] - 2026-04-13

### Added

- **EULA Shield & Permissions Hub**: Mandatory scroll-to-accept EULA flow and casual onboarding screen for hardware capabilities (Bluetooth, GPS, Camera, Mic) with updated Neogleamz legal text.
- **Admin App Manager**: Finalized Governance Hub featuring system safety locks.
- **Optimistic BLE Updates**: Masked hardware latency using 'Ghost' optimistic UI updates and state reconciliation for immediate visual feedback.
- **USA Skate Spots Dataset**: Implemented comprehensive US-only dataset of rinks and parks for interactive map overlays.

### Changed

- **State Machine Standard**: Deterministic UI refactor transitioning from scattered boolean flags to explicit Enum-based Finite State Machines (FSMs).
- **Bucket List Beautification**: Prettified internal backlog with tags, icons, and updated intake rules for better task predictability.

### Fixed

- **Database Schema Parity**: Resolved critical 'type' column anomalies and structural mismatches across mutations to perfectly align TypeScript models with hardened Supabase Postgres schemas.
- **Voice Button Null Ref**: Handled missing native module bridge gracefully without crashing.
- **Tech Debt Cleanup**: Resolved legacy import TODOs in CrewCreate, CrewDetail, and CrewManage screens.

## [1.1.0] - 2026-04-11

### Added

- **FTUE (First Time User Experience)**: Multiphase hardware setup wizard (`HardwareSetupWizardScreen`) with probe scans, product identification, and automated device claiming/grouping.
- **Dynamic Product Architecture**: Migrated hardware configurations to a Supabase-backed catalog with local caching for RAILZ and future product support.
- **Crew Hub Overhaul**: Optimized discovery engine with universal radius filtering and automated global session cleanup.
- **Visualizer Parity**: Achieved 1:1 mathematical parity for 33 "Pro" (Symphony) lighting effects within the product visualizer.
- **Street Mode**: Real-time braking (Strobe) and cruise (Freeze/Bounce) light responses with integrated telemetry.
- **Global Error Telemetry**: Integrated remote crash reporting and error boundaries using Supabase/Analytics.
- **Admin Tools Hub**: Consolidated diagnostic tools, LED lab, and system stats into a unified command center.
- **Builder UI**: New Tactical Grid layout for Positional Array Builder with node-based gradient control.
- **Auth Branding**: Added Neogleamz branding and "Glow your way" slogan to the authentication flow.

### Fixed

- **Camera Mode Regression**: Restored CAMERA mode UI in DockedController and improved touch sampling precision.
- **BLE Transport Reliability**: Resolved 0x51 payload MTU overflows using a variable-length chunking strategy.
- **UI Navigation**: Fixed "nav traps" in Crew Hub Edit and stabilized dashboard slab anchoring.
- **Grouping Logic**: Audited and fixed "ghost group" persistence issues in registered devices.

### Changed

- **AsyncStorage Standard**: Migrated all local storage keys to the `@Sk8lytz_` namespace.
- **Dashboard Layout**: Optimized 4-Slab architecture with collapsible device registries and taller Crew Hub cards.
- **Service Refactor**: Consolidated `LocationService` and `CrewService` for better radius-aware operation.

### Removed

- **Legacy Tools**: Retired the Simple Scanner, legacy DIY Builder, and Admin Hardware Tester in favor of consolidated modern modules.
- **Stale Data**: Purged legacy 0x81 protocol commands and hardcoded product heuristics.



