## [1.8.10] - 2026-04-17

### 🔧 Maintenance
- style(theme): enforce tokenized 8pt spacing grid app-wide via codemod
- chore(database): audit RLS performance and seal telemetry access vectors
- docs(bucket-list): sweep backlog, clean tasks, and sync core 8pt grid metrics

---

## [1.8.9] - 2026-04-17

### ✨ Features
- feat(dashboard): extract fixed mode into dedicated docked panel with strobe/blink support

### 🐛 Bug Fixes
- fix(ble): inject neverForLocation plugin flag and sync scanner race blocks
- fix(ble): resolve device settings probing and group connection regressions
- fix(ble): resolve scanner race condition and manifest permissions gap
- fix(ble): revert manual BLE permissions blocking Android 12 scanning
- fix(permissions): stop Android auto-backup FTUE bypass, decouple dashboard Bluetooth spam, and sync AccountManager ledger
- fix(ble): decouple keepAlive from GATT lock to restore wizard polling regression
- fix(ble): hotfix zero-day android gatt exception by restoring scan lock and queue resumer
- fix(ble): resolve group firmware sync race condition and popcorn effect

### 🔧 Maintenance
- chore(dashboard): decompose DashboardScreen monolith (-450 lines, -32.9%)
- chore(docked): decompose DockedController JSX into 6 memoized sub-panels
- build(version): bump version to 1.8.7 for permission ledger fix

---

## [1.8.8] - 2026-04-14

### 🚑 Emergency Hotfixes

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

### 🐛 Bug Fixes

**BLE Group Stability — 5 Surgical Fixes**
- **Critical Write Reconciliation Loop** (`useBLE.ts`): `writeToDevice` returned `void`, cast to `Promise<boolean>` but resolving as `undefined` at runtime — every single BLE write was falsy, triggering optimistic UI rollback on every command. Fixed to return honest `boolean` per-device success.
- **Scanner FSM Poisoning** (`useBLEScanner.ts`): `keepAlive` calls mutated `scannerState` to `'SCANNING'` before checking the flag, causing`derivedBleState` to report `SCANNING` immediately after a successful group connection. Fixed by early-exiting before any state mutation.
- **Ghost Device Infinite Loop** (`useBLEAutoRecovery.ts`): Recovery loop had no retry ceiling — a Zengge chip in a GATT soft-lock would loop forever, permanently blocking all writes to the dropped device. Added 8-attempt ceiling with device ejection on exhaustion.
- **Premature Write Guard** (`DockedController.tsx`): Guard missed `CONNECTING` and `PROBING` states, allowing `useEffect` writes to race `discoverAllServices` during group connect. Simplified to canonical `bleState !== 'READY'`.
- **Duplicate Boot Scan System** (`DashboardScreen.tsx`): Legacy `handleScan` + `hasAutoScanned` useEffect (System B) racing against `useDashboardAutoConnect` (System A) on every launch. System B's `runAutoProvisioning()` at t+8s mutated device state mid-GATT handshake, permanently corrupting one device in every pair. System B removed entirely.

**Naming Consistency — 3 Missed Gaps**
- **Scanner Storage Key** (`useBLEScanner.ts`): Scanner read `ng_registered_devices` (legacy) instead of `@Sk8lytz_registered_devices` — MAC deduplication was completely blind post-migration.
- **swapDevicePositions Naming** (`useRegistration.ts`): Rebuilt `device_name` as `"HALOZ Left"` bypassing `NamingUtils`. Now uses `getDefaultDeviceName(mac) + position`.
- **Auto-Provisioning Group Name** (`useDashboardGroups.ts`): Built group names as `"HALOZ SK8Lytz"` inline instead of `"My SK8Lytz HALOZ"` via `getDefaultGroupName()`. Same hardware produced different group names per registration path.

---

## [1.8.1] - 2026-04-14

### 🐛 Bug Fixes
- **Critical Dashboard Stability**: Resolved a hook split-brain bug in `DashboardScreen.tsx` where `isActuallyConnected` was computed in two separate, desynchronized locations — an inline expression passed to `useDashboardAutoConnect` and a `useMemo` defined ~150 lines later. Hoisted `displayConnectedDevices`, `isActuallyConnected`, and `isGrouped` above all consumers to establish a single canonical source of truth. This eliminates the root cause of the potential `Rendered more hooks than expected` crash during BLE reconnect cycles.
- **TS Compliance**: Fixed `PermissionService` default ledger initialization from `{}` to a fully-typed `DEFAULT_LEDGER` constant, and resolved `delete_account` RPC type narrowing error in `AccountModal`.

---

## [1.8.0] - 2026-04-14


### ✨ Features
- **Legal Compliance**: Executed granular legal compliance architecture and Account Deletion workflow.
- **Telemetry Convergence**: Standardized device naming syntax and unified global namespace to `@Sk8lytz_`.
- **Ghost Protocol**: Built pessimistic silent auto-recovery engine for Soft Disconnect drops.
- **Skate Discovery ETL**: Evolved Headless Scraper pipeline into a zero-api DOM crawler with automated background job runner logic for stealth.
- **Magic Workflows**: Added `/run-scraper` AI trigger.

### 🐛 Bug Fixes
- **BLE Engine**: Neutered over-aggressive hardware watchdog to resolve connection dropouts. Tore down legacy Soft Disconnect UI alerts.
- **Dashboard Hook Ordering**: Solved infinite 'Rendered more hooks' React error during async group recovery via closure stabilization.
- **Account Management**: Resolved `AccountModal` crash loops and network hangs.
- **Expo Auth Deep Linking**: Repaired dynamic `expo-auth-session` scheme callbacks, fixing Web 400 errors.

### 🔧 Maintenance & Performance
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

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

- **Hardware Watchdog**: Autonomous BLE 'Self-Healing' loop — detects hardware soft-locks, clears GATT buffers, and silently relatches connections to maintain stability.
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

