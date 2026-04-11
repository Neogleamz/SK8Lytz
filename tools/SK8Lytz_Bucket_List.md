# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

<!-- AUTO_SYNC_ERRORS_START -->
<!-- AUTO_SYNC_ERRORS_END -->
### Target: `epic/offline-mode`

- [ ] `gate-offline-mode` : Gate off online capabilities when in offline mode (Crew Hub, Community Favorites, SK8Lytz Picks). Ensure Crew Hub card stays on dashboard but displays an "Offline" warning.

### Target: `epic/device-registration`

### Target: `epic/crew-hub-overhaul`

- [ ] `refactor-crew-modal` : #3 — `CrewModal.tsx` refactor — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] `feat/advanced-map-integration` : Verify and rework map integration in Crew Hub and "Live Near Me" (post-modal rebuild). Add a live map showing the selected radius and a dot for the session location. Also beef up map integration within the scheduler.

### Target: `epic/music-mode-parity`

- [ ] `lab-music-mode-parity` : #13 — Lab 0x73 Music Mode parity — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [ ] `fix-music-mode-color` : #14 — Music Mode: Sound column/drop color not applied — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied.

---

## 🟠 Medium Priority / Next Sprint

### Target: `epic/admin-tools`

- [ ] `feat/picks-scheduler-builder` : Revise the SK8Lytz picks scheduler algorithms to finalize the assignment mode logic, and integrate an administrative version of the array builder to create custom community picks on the fly. also allow access to patterns in program mode for assignment

### Target: `epic/camera-mode`

### Target: `epic/visualizer-parity`

- [ ] `tune-visualizer-pro-effects` : #15 — Visualizer Parity: Pro Effects Patterns — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer.

### Target: `epic/device-management`

### Target: `epic/protocol-integration`

### Target: `epic/ui-refinement`

---

## 🟡 Backlog

### Target: `main`

### Target: `epic/telemetry-audit`



### Target: `epic/security-audit`

- [ ] `audit-rls-performance` : #20 — Security & Performance Review — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.

### Target: `epic/community-hub`

- [ ] `integrate-builder-presets` : #27 — Community Hub: Builder Preset Integration — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library.

### Target: `epic/ui-refinement`

- [ ] `feat/lab-ui-modernization` : Modernize and style the LED Diagnostic Lab layout to match the aesthetics, typography, and input styling of the rest of the app.
- [ ] `add-swipe-nav` : #34 — Card Swipe Navigation — Add the ability to swipe left and right to navigate back and forth between cards (Favorites, Picks, Presets) for a more fluid mobile UX.

---

## ❄️ Icebox / Backburner (Manual Trigger Only)

*Items placed here are explicitly ignored by the automatic queue. They will act as a catalog for future features that require manual authorization to begin work on.*

---

## ✅ Completed This Session (Apr 2026)

- [x] `feat/logo-branding-auth` : Integrate SK8Lytz logo (`assets/logo.png`) onto Welcome/Auth screens with the "Glow your way." slogan. Implement white logo variant for dark mode compatibility.
- [x] `feat/auth-offline-card-reorder` : Move the 'Continue Offline' card on the Auth page to the bottom, anchoring it similar to the Registered Devices slab on the dashboard.
- [x] `feat/global-error-telemetry` : Integrate a global error boundary and remote crash reporting solution (e.g., Sentry or Supabase Edge logging) to automatically capture, diagnose, and push unhandled exceptions without user intervention. this should add items to bucket list for us to review and fix.
- [x] `audit-applogger-coverage` : #30 — AppLogger Coverage Audit — Comprehensive audit of all features added this session (Street Mode, Picks, Favorites, Builder) to ensure 100% telemetry coverage in Analytics.
- [x] `verify-telemetry-ingestion` : #21 — Telemetry ingestion verification — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly.
- [x] `verify-lab-telemetry` : #2 — Verify Supabase `led_diagnostics` table — Confirm Diagnostic Lab successfully pushes telemetry to Supabase. Query table after live test.
- [x] `fix/dashboard-anchor` : Anchor Registered Devices slab permanently to the bottom of the dashboard screen (outside ScrollView).
- [x] `feat/dashboard-layout-update` : Move 'Registered Devices' to the bottom of the dashboard and make it collapsible. Make the Crew Hub card twice as tall and the My Skates Card bigger.
- [x] `review-device-reg` : Device Registration & Claim Process Review — Revisiting the user-to-device ownership flow. Need to brainstorm and build a new implementation plan as the previous extensive one was lost.
- [x] `ftue-initial-setup` : FTUE Phase 1 - Create HardwareSetupWizardScreen with probe scan logic and instructions.
- [x] `ftue-probe-discovery` : FTUE Phase 2 - Build device discovery list, product identification logic (LED count), and Blink test.
- [x] `ftue-claim-registration` : FTUE Phase 3 - Wire offline/online claiming via useRegistration.ts and UI portal integration.
- [x] `sandbox-testing` : Establish Dev Sandbox — Move Nuke button to Auth, inject Dead-Code eliminated BLE mocks directly into useBLE hook, and clear stale Dashboard mock logic.
- [x] `ftue-grouping-config` : FTUE Phase 4 - Add Mini Hardware Config for assigned positioning and auto-grouping generation.
- [x] `fix/hardware-setup-guide-url` : Fix the URL for the 'View Installation Guide' link on the Hardware Setup screen
- [x] `audit-admin-hardware` : #7 — Admin Hardware Tester audit — Verify `setMultiColor` path in admin tester applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [x] `build-picks-scheduler` : #28 — SK8Lytz Picks Admin Scheduler — Build admin UI to manage the `sk8lytz_picks` table scheduling. DB columns (`active_from`, `active_until`, `is_active`) already in place. Goal: seasonal picks (4th of July, Christmas, etc.) auto-show/hide. Needs: admin screen, date pickers, toggle per pick. needs to under hidden tool section
- [x] `feat/admin-button-visibility` : Hide admin LogViewer buttons when Dev Sandbox is off, and remove the redundant button from the header so only the bottom button remains.
- [x] `fix-camera-touch` : #8 — Camera Mode: Touch Precision fix — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel.
- [x] `audit-device-grouping` : #16 — Device Grouping Audit & Redesign — A "ghost group" keeps persisting across installs. Groups are incorrectly maintaining persistence after deleting. Audit all grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups`.
- [x] `remove-protocol-setcolor` : #23 — `setColor()` in ZenggeProtocol — Does NOT apply color sorting. Should be removed or marked internal-only.
- [x] `audit-0x81-command` : #24 — `0x81` legacy command audit — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls.
- [x] `feat/offline-mode-warning-text` : Add descriptive text below the 'Continue Offline' button on the Auth screen detailing horizontal feature lockouts (no crews, sessions, picks, or cloud sync).
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

---
*Last updated: 2026-04-09 | This session: IDE Rules configured for auto-branching.*
