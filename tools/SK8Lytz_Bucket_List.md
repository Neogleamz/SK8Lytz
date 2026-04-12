# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized by **App Performance, Stability, and System Health**. New Features are appended at the bottom.

---

## 📊 Global System Readiness

```mermaid
%%{init: {'theme': 'dark'}}%%
pie title Core Development Progress
  "Completed" : 9
  "Remaining" : 41
```

---

## 🔴 CRITICAL: Performance, Stability & Security

*These items address crashes, data corruption, and security blocks that impact the core experience.*

```mermaid
%%{init: {'theme': 'dark'}}%%
pie title Critical Stability
  "Completed" : 4
  "Remaining" : 4
```

- [x] `fix/rls-telemetry-block` : [CLOUD] [BATCH] [H-RISK] [Snack] [CRITICAL] Resolve 403 Forbidden errors on `parsed_session_stats`, `device_diagnostics`, and `parsed_session_devices`. Likely RLS policy mismatch for authenticated telemetry sinks. → [Plan](docs/plans/fix-rls-telemetry-block.md)
- [x] `fix/device-group-fk-integrity` : [CLOUD] [BATCH] [H-RISK] [Snack] [CRITICAL] Resolve foreign key constraint violations in `registered_devices` group_id. Upsert logic is referencing non-existent groups. → [Plan](docs/plans/fix-device-group-fk-integrity.md)
- [x] `audit/device-group-persistence` : [CLOUD] [H-RISK] [Meal] [TOP PRIORITY] Audit device group state handling to prevent duplicate/ghost groups and ensure seamless offline-to-online persistence. → [Plan](docs/plans/audit-device-group-persistence.md)
- [x] `fix/voice-mic-access` : [LAB] [BATCH] [L-RISK] [Snack] [URGENT] Resolve issue where Voice Engine fails to request microphone permissions, causing immediate errors on start. → [Plan](docs/plans/fix-voice-mic-access.md)
- [ ] `fix/crew-hub-stability-patch` : [CLOUD] [H-RISK] [Meal] [URGENT] Address critical April 7th audit findings: fix malformed JSX stacking in CrewModal, patch privacy leak in nearby session discovery, and fix the crew-matching logic gap. → [Plan](docs/plans/fix-crew-hub-stability-patch.md)
- [x] `fix/dynamic-arch-regressions` : [LAB] [H-RISK] [Meal] Resolve 'isHaloz' ReferenceError in ProductVisualizer and perform a sanitization audit (DockedController, ZenggeProtocol, Setup Wizard) to remove remaining hardcoded binary logic. → [Plan](docs/plans/fix-dynamic-arch-regressions.md)
- [x] `fix/tsc-errors-audit` : [CLOUD] [BATCH] [L-RISK] [Meal] Fix TypeScript errors remaining from dynamic-arch-regressions (Audio namespace, missing EventType for 'BUILDER_PRESET_SAVED' in DockedController, IVoiceAction/Typography Subheader in DashboardScreen). → [Plan](docs/plans/fix-tsc-errors-audit.md)
- [ ] `gate-offline-mode` : [CLOUD] [H-RISK] [Feast] [Stability] Gate off online capabilities when in offline mode (Crew Hub, Community Favorites, SK8Lytz Picks). Ensure Crew Hub card stays on dashboard but displays an "Offline" warning. → [Plan](docs/plans/gate-offline-mode.md)

---

## 🟠 HIGH: Engineering Excellence & Tech Debt

*System-wide health improvements, refactors, and performance optimizations.*

```mermaid
%%{init: {'theme': 'dark'}}%%
pie title Engineering Health
  "Completed" : 1
  "Remaining" : 11
```

- [ ] `chore/sentient-tech-debt-sweep` : [CLOUD] [BATCH] [L-RISK] [Feast] [TOP PRIORITY] Standardize AsyncStorage keys (@Sk8lytz_ prefix), normalize UI speed → Hardware (1-31), and eliminate redundant buffer requires. → [Plan](docs/plans/chore-sentient-tech-debt-sweep.md)
- [ ] `refactor/micro-app-crew-modal` : [CLOUD] [H-RISK] [Feast] [Pillar 13] Extract the 2,200-line CrewModal monolith into decoupled domain hooks (useCrewHub, useCrewSession) and purified sub-components. → [Plan](docs/plans/refactor-micro-app-crew-modal.md)
- [ ] `refactor/state-machine-standard` : [CLOUD] [H-RISK] [Feast] [Pillar 8] Deterministic UI — transition from boolean flags to explicit Enum-based Finite State Machines. → [Plan](docs/plans/refactor-state-machine-standard.md)
- [ ] `feat/ble-hardware-watchdog` : [LAB] [H-RISK] [Feast] [Pillar 7] Autonomous BLE 'Self-Healing' loop — detects hardware soft-locks and silent-relatches connections. → [Plan](docs/plans/feat-ble-hardware-watchdog.md)
- [ ] `perf/optimistic-ble-updates` : [LAB] [H-RISK] [Meal] [Pillar 2] Mask hardware latency using 'Ghost' optimistic UI updates and state reconciliation. → [Plan](docs/plans/perf-optimistic-ble-updates.md)
- [ ] `perf/delta-sync-protocol` : [CLOUD] [L-RISK] [Meal] [Pillar 4] Implement differential data fetching to reduce bandwidth and battery consumption. → [Plan](docs/plans/perf-delta-sync-protocol.md)
- [ ] `audit/domain-driven-architecture` : [CLOUD] [H-RISK] [Feast] [Future Deep Dive] Decouple Hardware, Community, and Session logic into isolated, testable containers. → [Plan](docs/plans/audit-domain-driven-architecture.md)
- [x] `chore/telemetry-standards` : [CLOUD] [L-RISK] [Meal] Implement the 5-point 'Black Box' logging standard (JSON, HW Context, PII Masking, Levels, FIFO Buffer). → [Plan](docs/plans/chore-telemetry-standards.md)
- [ ] `fix/remote-id-audit` : [LAB] [H-RISK] [Meal] [Security] Implementation of the 0x2B protocol parser to extract and display unique paired RF Remote IDs in the Device Settings modal for security verification. → [Plan](docs/plans/hw-test-remote-pairing-logic.md)
- [ ] `audit-rls-performance` : [CLOUD] [H-RISK] [Meal] #20 — Security & Performance Review — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges. → [Plan](docs/plans/audit-rls-performance.md)
- [ ] `style/tokenized-spacing-standard` : [CLOUD] [L-RISK] [Meal] [Pillar 9] The 8pt Grid — enforce 8pt spacing tokens app-wide to eliminate magic numbers. → [Plan](docs/plans/style-tokenized-spacing-standard.md)
- [x] `fix/typescript-debt-audit` : [CLOUD] [BATCH] [L-RISK] [Feast] Resolve pre-existing TS errors across the codebase (dead state vars, type drift, missing imports). → [Plan](docs/plans/fix-typescript-debt-audit.md)

---

## 🟡 MEDIUM: Compliance & Governance

*Legal requirements and administrative control systems.*

```mermaid
%%{init: {'theme': 'dark'}}%%
pie title Governance Shield
  "Completed" : 0
  "Remaining" : 3
```

- [ ] `feat/eula-onboarding` : [CLOUD] [H-RISK] [Meal] Implement the **Legal Shield** — a mandatory, scroll-to-accept EULA flow (Kinetic Safety, Photosensitivity, Data Privacy) in the Auth registration and global version enforcement for active sessions. → [Plan](docs/plans/feat-eula-onboarding.md)
- [ ] `feat/telemetry-onboarding-ux` : [CLOUD] [L-RISK] [Meal] Implement a casual 'Permissions Hub' onboarding screen after EULA to enable Camera, Mic, GPS, and Bluetooth. → [Plan](docs/plans/feat-telemetry-onboarding-ux.md)
- [ ] `feat/admin-app-manager` : [CLOUD] [L-RISK] [Feast] Finalized Governance Hub with Safety Locks (Consolidated Scope) → [Plan](docs/plans/feat-admin-app-manager.md)

---

## 🔵 LOW: New Features & UI Enhancements

*User-facing product value and UI refinements.*

```mermaid
%%{init: {'theme': 'dark'}}%%
pie title Feature Velocity
  "Completed" : 0
  "Remaining" : 18
```

- [ ] `feat/music-intel-phase-1` : [CLOUD] [H-RISK] [Feast] [Spotify Sync] — OAuth2 PKCE login, BPM/Energy mapping, and Album Art color extraction. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-2` : [CLOUD] [L-RISK] [Meal] [Media Access] — Android MediaSession detection (YouTube, Pandora, etc.). → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-3` : [LAB] [L-RISK] [Meal] [Live Rink Mode] — ShazamKit/ACRCloud periodic background scanning (45s). → [Plan](docs/plans/feat-live-rink-mode.md)
- [ ] `feat/music-intel-phase-4` : [CLOUD] [L-RISK] [Meal] [Apple Music] — MusicKit integration for native iOS BPM. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/music-intel-phase-5` : [CLOUD] [H-RISK] [Feast] [Crew Party Sync] — Master BPM Choreography Engine with Realtime crew sync. → [Plan](docs/plans/feat-music-integration-master.md)
- [ ] `feat/interactive-skate-spot-map` : [CLOUD] [L-RISK] [Feast] Implement a high-density, interactive skate spot map using react-native-maps. → [Plan](docs/plans/feat-interactive-skate-spot-map.md)
- [ ] `feat/street-mode-telemetry-overhaul` : [CLOUD] [L-RISK] [Meal] Overhaul Street Mode with metrics grid and auto-scaling gauges. → [Plan](docs/plans/feat-street-mode-telemetry-overhaul.md)
- [ ] `feat/usa-skate-spots-dataset` : [CLOUD] [BATCH] [L-RISK] [Snack] US-only dataset of rinks and parks for map overlays. → [Plan](docs/plans/feat-usa-skate-spots-dataset.md)
- [ ] `feat/app-wide-ux-tips` : [CLOUD] [L-RISK] [Meal] Contextual tips system for key friction points. → [Plan](docs/plans/feat-app-wide-ux-tips.md)

---

## ❄️ Icebox / Backburner (Manual Trigger Only)

- [ ] `feat/spatial-beat-mapping` : [LAB] [H-RISK] [Meal] [Pillar 11] Sound-to-Light Spatialization (Bass/Mid/Treble mapping). → [Plan](docs/plans/feat-spatial-beat-mapping.md)
- [ ] `feat/cockpit-dash-dynamic-bg` : [CLOUD] [L-RISK] [Meal] Transform Dashboard into palette-synced dynamic backgrounds. → [Plan](docs/plans/feat-cockpit-dash-dynamic-bg.md)
- [ ] `feat/fixed-mode-refactor` : [LAB] [L-RISK] [Meal] Pattern selection (Strobe, Blink, Static) + music slider fix. → [Plan](docs/plans/feat-fixed-mode-refactor.md)
- [ ] `feat/battery-health-predictor` : [LAB] [H-RISK] [Meal] Power modeling to predict battery life and auto-dimming. → [Plan](docs/plans/feat-battery-health-predict.md)
- [ ] `feat/impact-sentinel-safety` : [LAB] [H-RISK] [Meal] [Pillar 6] Fall Detection — triggers white 'Flare' strobe on impact. → [Plan](docs/plans/feat-impact-sentinel-safety.md)
- [ ] `feat/kinetic-brake-lights` : [LAB] [H-RISK] [Meal] [Pillar 12] Kinetic Safety — phone accelerometer pulse RED for braking. → [Plan](docs/plans/feat-kinetic-brake-lights.md)
- [ ] `feat/zero-touch-crew-sync` : [CLOUD] [H-RISK] [Feast] Geofence-based 'Hive Mind' synchronization. → [Plan](docs/plans/feat-zero-touch-crew-sync.md)
- [ ] `hw-test/proximity-magic-tap` : [LAB] [H-RISK] [Meal] [The Magic Tap] RSSI-gated hardware identification.
- [ ] `feat/neogleamz-brand-presence` : [CLOUD] [L-RISK] [Meal] Neogleamz identity integration.
- [ ] `feat/siri-google-assistant-integration` : [CLOUD] [L-RISK] [Meal] Siri/Google Assistant phone-level voice control.
- [ ] `feat/geofence-rink-sync` : [CLOUD] [H-RISK] [Meal] GPS-based auto-crew discovery.
- [ ] `add-swipe-nav` : [CLOUD] [L-RISK] [Meal] Card Swipe Navigation.

---

## ✅ Completed Previously

- [x] `feat/admin-hub-design-system` : Standardized admin headers and ToolCard grid.
- [x] `chore/delete-orphan-backup` : Removed Sk8LytzDiagnosticLab_old.tsx.
- [x] `lab-music-mode-parity` : Lab 0x73 Music Mode parity.
- [x] `epic/connection-reliability` : Multiphase BLE connection overhaul.
- [x] `fix-ble-audit` : BLE discovery speed fixes.
- [x] `feat/speed-tracking-telemetry` : Statistics, Session Metadata, and GPS accumulation.
- [x] `feat/voice-command-engine` : Offline voice command resolution.
- [x] `feat/empty-skates-setup-cta` : Setup Wizard dashboard CTA.

---
*Last updated: 2026-04-12 | Reorganized by performance criticality.*
