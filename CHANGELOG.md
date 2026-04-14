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
