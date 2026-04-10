# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

### Target: `epic/device-registration`

- [x] `review-device-reg` : Device Registration & Claim Process Review — Revisiting the user-to-device ownership flow. Need to brainstorm and build a new implementation plan as the previous extensive one was lost.
- [x] `ftue-initial-setup` : FTUE Phase 1 - Create HardwareSetupWizardScreen with probe scan logic and instructions.
- [x] `ftue-probe-discovery` : FTUE Phase 2 - Build device discovery list, product identification logic (LED count), and Blink test.
- [x] `ftue-claim-registration` : FTUE Phase 3 - Wire offline/online claiming via useRegistration.ts and UI portal integration.
- [x] `sandbox-testing` : Establish Dev Sandbox — Move Nuke button to Auth, inject Dead-Code eliminated BLE mocks directly into useBLE hook, and clear stale Dashboard mock logic.
- [x] `ftue-grouping-config` : FTUE Phase 4 - Add Mini Hardware Config for assigned positioning and auto-grouping generation.
- [x] `fix/hardware-setup-guide-url` : Fix the URL for the 'View Installation Guide' link on the Hardware Setup screen

### Target: `epic/crew-hub-overhaul`

- [ ] `refactor-crew-modal` : #3 — `CrewModal.tsx` refactor — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.

### Target: `epic/music-mode-parity`

- [ ] `lab-music-mode-parity` : #13 — Lab 0x73 Music Mode parity — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [ ] `fix-music-mode-color` : #14 — Music Mode: Sound column/drop color not applied — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied.

---

## 🟠 Medium Priority / Next Sprint

### Target: `epic/admin-tools`

- [ ] `audit-admin-hardware` : #7 — Admin Hardware Tester audit — Verify `setMultiColor` path in admin tester applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [ ] `build-picks-scheduler` : #28 — SK8Lytz Picks Admin Scheduler — Build admin UI to manage the `sk8lytz_picks` table scheduling. DB columns (`active_from`, `active_until`, `is_active`) already in place. Goal: seasonal picks (4th of July, Christmas, etc.) auto-show/hide. Needs: admin screen, date pickers, toggle per pick. needs to under hidden tool section

### Target: `epic/camera-mode`

- [ ] `fix-camera-touch` : #8 — Camera Mode: Touch Precision fix — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel.

### Target: `epic/visualizer-parity`

- [ ] `tune-visualizer-pro-effects` : #15 — Visualizer Parity: Pro Effects Patterns — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer.

### Target: `epic/device-management`

- [ ] `audit-device-grouping` : #16 — Device Grouping Audit & Redesign — A "ghost group" keeps persisting across installs. Audit all grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups`.

- [ ] `refine-crew-hub-layout` : #22 — Crew Hub layout refinement — Spacing, dashboard layout, member count display, and session timer as platform grows.

### Target: `epic/protocol-integration`

- [ ] `remove-protocol-setcolor` : #23 — `setColor()` in ZenggeProtocol — Does NOT apply color sorting. Should be removed or marked internal-only.
- [ ] `audit-0x81-command` : #24 — `0x81` legacy command audit — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls.

### Target: `epic/ui-refinement`

- [ ] `modern-rgb-slider` : #29 — Modern RGB Hue Slider — Design and implement a more sophisticated, high-precision RGB hue selection component to replace the standard sliders.

---

## 🟡 Backlog

### Target: `main`

- [x] `feat/offline-mode-warning-text` : Add descriptive text below the 'Continue Offline' button on the Auth screen detailing horizontal feature lockouts (no crews, sessions, picks, or cloud sync).

### Target: `epic/telemetry-audit`

- [ ] `verify-lab-telemetry` : #2 — Verify Supabase `led_diagnostics` table — Confirm Diagnostic Lab successfully pushes telemetry to Supabase. Query table after live test.
- [ ] `verify-telemetry-ingestion` : #21 — Telemetry ingestion verification — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly.
- [ ] `audit-applogger-coverage` : #30 — AppLogger Coverage Audit — Comprehensive audit of all features added this session (Street Mode, Picks, Favorites, Builder) to ensure 100% telemetry coverage in Analytics.

### Target: `epic/testing-suite`

- [ ] `verify-web-e2e` : #17 — Web E2E Verification — Map static thumbnails and address autocomplete in browser environment.
- [ ] `verify-native-platform` : #18 — Native Platform Verification — MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] `expand-e2e-tests` : #19 — Automated Testing Suite — Expand E2E tests for Location flows, crew session creation, and LED payload correctness.

### Target: `epic/security-audit`

- [ ] `audit-rls-performance` : #20 — Security & Performance Review — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.

### Target: `epic/community-hub`

- [ ] `integrate-builder-presets` : #27 — Community Hub: Builder Preset Integration — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library.

### Target: `epic/ui-refinement`

- [ ] `add-swipe-nav` : #34 — Card Swipe Navigation — Add the ability to swipe left and right to navigate back and forth between cards (Favorites, Picks, Presets) for a more fluid mobile UX.

---

## ✅ Completed This Session (Apr 2026)

- [x] `#36` — Pro Effects + Effects Mode fully restored — `EffectsPanel.tsx` standalone mode working. BREAKTHROUGH: hardware accepts variable-length 0x51 packets. `setCustomModeCompact()` added to `ZenggeProtocol.ts`.
- [x] `#35` — Pro Effects 0x51 regression diagnosis — Root cause found: full 32-slot 291-byte payload exceeded BLE MTU (186 bytes default). Variable-length format bypasses MTU completely.
- [x] `#1` — Positional Array Builder UI — Builder submode in MULTIMODE with node-based gradient interface
- [x] `#4` — Crew Hub: private crew invite code display — Private crew cards show invite code under "My Crews"
- [x] `#5` — DIY Array Builder retired — Fully replaced by new Builder workflow; legacy DIY mode scrubbed
- [x] `#6` — Favorites persistence — Legacy DIY/RBM mode entries migrated to BUILDER/PROGRAMS on load; write-back to AsyncStorage
- [x] `#9` — Street Mode: Cruise LED bounce — Hardware chase ticker bounces bright spot through mid-zone on each dispatch
- [x] `#10` — Street Mode: Tail light dimming — Absolute brightness: 100% braking (R=255), 50% cruising (R=127), not slider-scaled
- [x] `#11` — Visualizer Street Mode parity — `motionState` prop drives tail opacity and cruise bounce in visualizer
- [x] `#12` — Dashboard Header Layout — User pill left-justified, Support & Theme icons right-grouped, matching Auth screen style
- [x] `#25` — Builder UI Enhancement — ADD PIN moved into visual map as blank circular pin
- [x] `#26` — SK8Lytz Picks DB Migration — Moved to `sk8lytz_picks` Supabase table
- [x] `#31` — Legacy Tool Retirement — Retired Simple Scanner and Admin Hardware Tester; consolidated
- [x] `#32` — Diagnostic Lab UI Modernization — Restyled Lab header, exit logic, and card styling.
- [x] `#33` — Lab Navigation Flow — Standardized "Exit" behavior to return users to the Analytics view.

## ✅ Completed Previously

- [x] Protocol: Support full 0x51 logic
- [x] Visualizer: 1:1 mathematical parity for 33 Custom Step Effects
- [x] Add Black and White Color Extremes
- [x] Solid Pattern Re-indexing (Custom Mode #1)
- [x] Test the Expanded LED Diagnostic Lab
- [x] Verify LED color mapping on device
- [x] Street Mode UI Bug fix
- [x] Lab DEVICES tab wired into content render block
- [x] Lab transmit() now targets specific device via targetDeviceId
- [x] Diagnostic Lab safe area padding (notch/nav bar overlap fixed)
- [x] LED Diagnostic Lab fully tested on device — DEVICES scan, TARGET, BUILDER payloads firing
- [x] LED color mapping verified: pure RGB sent, GRB remapped natively by hardware
- [x] PatternEngine: transition byte research — 0x03=TRIGGER, 0x00=CASCADE
- [x] Crew Hub: private crew invite code on card; leader and public/private status
- [x] AppLogger telemetry: crew sessions, street mode, jerk detection, sensitivity
- [x] AsyncStorage key migration: `ng_` → `@Sk8lytz_` namespace standardized
- [x] Master Reference cleanup: ~1,200 lines removed, TOC + AI preamble added
- [x] Street mode transition types: HARD_BRAKING → Strobe (0x02), CRUISE → FREEZE (0x01)
- [x] Crew Hub Edit nav trap fixed: Back always returns to hub landing
- [x] Session end flow: End Session button visible and functional in DockedController
- [x] Builder UI Layout Compression: elements collapse cleanly under docked controller
- [x] Builder UI Stabilization: 8-slot Tactical Grid with Marquee names

---
*Last updated: 2026-04-09 | This session: IDE Rules configured for auto-branching.*
