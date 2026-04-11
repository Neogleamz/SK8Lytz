# SK8Lytz Master Bucket List

- [x] `review-device-reg` : Device Registration & Claim Process Review — Revisiting the user-to-device ownership flow. Need to brainstorm and build a new implementation plan as the previous extensive one was lost.
- [x] `ftue-initial-setup` : FTUE Phase 1 - Create HardwareSetupWizardScreen with probe scan logic and instructions.
- [x] `ftue-probe-discovery` : FTUE Phase 2 - Build device discovery list, product identification logic (LED count), and Blink test.
- [x] `ftue-claim-registration` : FTUE Phase 3 - Wire offline/online claiming via useRegistration.ts and UI portal integration.
- [x] `sandbox-testing` : Establish Dev Sandbox — Move Nuke button to Auth, inject Dead-Code eliminated BLE mocks directly into useBLE hook, and clear stale Dashboard mock logic.
- [x] `ftue-grouping-config` : FTUE Phase 4 - Add Mini Hardware Config for assigned positioning and auto-grouping generation.
- [x] `fix/hardware-setup-guide-url` : Fix the URL for the 'View Installation Guide' link on the Hardware Setup screen
- [x] `audit-admin-hardware` : #7 — Admin Hardware Tester audit — Verify `setMultiColor` path in admin tester applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [x] `fix-camera-touch` : #8 — Camera Mode: Touch Precision fix — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel.
- [x] `audit-device-grouping` : #16 — Device Grouping Audit & Redesign — A "ghost group" keeps persisting across installs. Groups are incorrectly maintaining persistence after deleting. Audit all grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups`.
- [x] `remove-protocol-setcolor` : #23 — `setColor()` in ZenggeProtocol — Does NOT apply color sorting. Should be removed or marked internal-only.
- [x] `audit-0x81-command` : #24 — `0x81` legacy command audit — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls.
- [x] `modern-rgb-slider` : #29 — Modern RGB Hue Slider — Design and implement a more sophisticated, high-precision RGB hue selection component to replace the standard sliders. Give me 3 choices and let me choose before moving on
- [x] `fix/music-buttons-scaling` : #37 — Music Mode: Mic & Play Buttons scaling fix — Fix the scaling of both the microphone footprint and the play buttons in Music Mode to dynamically fill available space without overlapping components.
- [x] `feat/speed-slider-turbo-color` : #38 — Dynamic Speed Slider Color — Update the speed slider to dynamically change its track fill color from white at 0% to bright red at 100% (turbo) for enhanced visual feedback.
- [x] `feat/brightness-slider-intensity` : #39 — Dynamic Brightness Slider Intensity — Update the brightness slider to visually scale its brightness representation (dim at 5%, super bright at 100%) and add a visual target line/marker at 80%.
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

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

### Target: `main`

- [x] `fix/dashboard-group-longpress` : Fix regression where long-pressing to edit or delete groups on the dashboard no longer functions as expected.
- [x] `fix/music-mode-duplicate-toggles` : Fix duplicate Light Bar/Screen mode toggles in Music Mode. Remove the old ones and move the new toggles to the top of the section.

### Target: `epic/device-registration`

- [ ] `audit/global-device-naming` : Comprehensive Device Naming Audit — The app is displaying raw 'LEDnet' names in the visualizer and dashboard instead of actual named devices (e.g. 'Soulz Right'). Devices also appear duplicated. Trace and audit ALL automatic naming and grouping persistence globally to establish a single source of truth for display names.
- [ ] `feat/global-naming-structure` : Implement a global device naming architecture and UI workflow to ensure consistent, readable names rather than relying on raw hardware strings or overlapping groupings.

### Target: `epic/telemetry-audit`

- [x] `feat/telemetry-error-logging` : Add comprehensive error handling and logging to Supabase. Implement an intelligent system to persist runtime crashes, unhandled exceptions, and BLE errors to a new DB table for AI review and bucket list generation.

### Target: `epic/protocol-integration`

- [ ] `fix/hardware-connection-drop` : Connection dropouts when using the app, dumping UI to setup on disconnect. Re-evaluate hardware polling and connection state handling.
- [ ] `audit/connection-polling-logic` : Audit how we are polling and pinging device states on the dashboard and controller. Fix issues with dropping connections and touchy multi-device control.
- [ ] `fix/controller-navigation-lockup` : Leaving the controller and re-entering is slow and causes lockups. Investigate component unmount/mount lifecycle and connection management.

### Target: `epic/device-registration`

- [x] `fix/supabase-auth-redirect` : Supabase Auth Confirmation Email Redirect — Fix the issue where the confirmation email link points to a dead `localhost` site. 
- [x] `fix/hardware-setup-loop` : Hardware Setup Wizard launches repeatedly on app reopen even after setup and registration. Investigate triggers and fix.
- [x] `fix/account-devices-display` : Account Manager / Devices tab is showing groups instead of individual devices. Refactor to display all registered devices with their details only.
- [x] `fix/device-setup-blink` : Blink button does not work on device setup; evaluate and implement handling strategy.
- [ ] `chore/rename-hardware-fleet` : Rename "Hardware Fleet" to "Registered Devices" in UI.

### Target: `epic/ui-refinement`

- [x] `fix/color-picker-overflow` : #40 — 11-Color Picker Mobile Overflow Fix — The preset color picker dots (black dot mapping to a second line) are overflowing due to static widths. Refactor to use dynamic flex scaling so all 11 dots stay on one single horizontal row.
- [ ] `fix/pro-effects-color-sync` : Pro effects mode color slider sync — The RGB slider does not stay in sync when toggling between Foreground and Background state. Update slider to reflect the currently selected layer's color.
- [ ] `feat/favorites-layout-modernization` : Fix Favorites layout and scrolling issues. Modernize the tab design (requires presenting multiple design concepts for approval).

### Target: `epic/crew-hub-overhaul`

- [ ] `refactor-crew-modal` : #3 — `CrewModal.tsx` refactor — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [x] `fix/crew-session-stale-data` : Audit Live Sessions logic - Fix stale data/duplicates in 'Live Near You' after deletion and implement unique session naming (e.g. CrewName_Date)
- [x] `feat/crew-discovery-refinement` : Refine 'Live Near You' discovery - Show only sessions (not crews), show all public sessions, but only private sessions for crews you belong to.
- [x] `fix/crew-hub-button-styling` : Fix 'Start' and 'Schedule' buttons — Currently overflow their container and need to be resized/smaller to fit the landing view box.

### Target: `epic/camera-mode`

- [ ] `fix/camera-color-interpreter` : The camera color interpreter is behaving oddly. Discuss options and brainstorm solutions for refining or replacing the color sampling logic.

### Target: `epic/music-mode-parity`

- [x] `lab-music-mode-parity` : #13 — Lab 0x73 Music Mode parity — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [x] `fix-music-mode-color` : #14 — Music Mode: Sound column/drop color not applied — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied.

---

## 🟠 Medium Priority / Next Sprint

### Target: `epic/admin-tools`

- [ ] `build-picks-scheduler` : #28 — SK8Lytz Picks Admin Scheduler — Build admin UI to manage the `sk8lytz_picks` table scheduling. DB columns (`active_from`, `active_until`, `is_active`) already in place. Goal: seasonal picks (4th of July, Christmas, etc.) auto-show/hide. Needs: admin screen, date pickers, toggle per pick. needs to be under hidden tool section. lets talk about this one
- [ ] `feat/admin-proximity-telemetry` : Proximity Logging Hub — Aggregate all nearby BLE devices seen by the scanner in the Admin Tools "Device" tab. Prioritize registered hardware at the top, followed by a historical list of all unique MACs seen to correlate user behavior and rink density.

### Target: `epic/visualizer-parity`

- [ ] `tune-visualizer-pro-effects` : #15 — Visualizer Parity: Pro Effects Patterns — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer.

---

## 🟡 Backlog

### Target: `main`

- [x] `feat/dashboard-redesign` : **[PRIORITY]** Redesign scanner dashboard into a modern interface (Style 3 - Vertical Slabs, No-Scrolling). 4-Slab Hierarchy: [Header] ➔ [Crew Hub Sessions] ➔ [Skates (Hardware Groups)] ➔ [Registered Device List + Add Button]. See `docs/plans/feat-dashboard-redesign.md` for details.
- [x] `fix/dynamic-username` : Display the actual username from Supabase session/profile instead of hardcoded 'Skater'
- [ ] `fix/username-case-sensitivity` : Why is the username case-sensitive? Enforce global lowercase normalization on registration, authentication, and display to prevent duplicate/split identities.
- [ ] `feat/signup-profile-requirements` : Require users to set a Handle and Display Name during initial sign-up. Discuss options for preventing skipped profiling and ensuring identity is established upfront.
- [ ] `fix/misspelling-back-button` : Fix global spelling error on navigation header buttons. Currently says "Bac" everywhere instead of "Back" or uses a faulty icon label.
- [x] `fix/setup-finish-destination` : Ensure Hardware Setup Wizard returns to the Dashboard after completion instead of auto-launching the controller
- [ ] `hw-test/remote-pairing-logic` : Research and verify RF Remote pairing ID discovery and Power ON/OFF parity (APP vs RF Remote)
- [x] `fix/camera-mode-layout` : Camera mode regression fix — Restore proper permissions prompt system, optimize layout so color bar is minimized at the bottom, maximize camera preview area, and add user instruction tooltip ("Click a color on screen...").
- [ ] `fix/dashboard-long-press-tip` : Fix the long press tip button on dashboard.
- [ ] `feat/speed-tracking-telemetry` : Add average mph and speed tracking to sessions, crews, and street mode. Need to brainstorm and discuss implementation options.
- [ ] `chore/wireless-adb-setup` : Investigate and document wireless APK installation via ADB over Wi-Fi so the phone doesn't need to be constantly plugged in via USB.

### Target: `epic/telemetry-audit`

- [x] `verify-telemetry-ingestion` : #21 — Telemetry ingestion verification — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly.
- [x] `audit-applogger-coverage` : #30 — AppLogger Coverage Audit — Comprehensive audit of all features added this session (Street Mode, Picks, Favorites, Builder) to ensure 100% telemetry coverage in Analytics.

### Target: `epic/testing-suite`

- [ ] `verify-web-e2e` : #17 — Web E2E Verification — Map static thumbnails and address autocomplete in browser environment.
- [ ] `verify-native-platform` : #18 — Native Platform Verification — MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] `expand-e2e-tests` : #19 — Automated Testing Suite — Expand E2E tests for Location flows, crew session creation, and LED payload correctness.

### Target: `epic/security-audit`

- [ ] `chore/supabase-auth-reconciliation` : #22 — Supabase Auth Reconciliation — Audit the mixing of standard Supabase Auth with internal application auth logic; identify impacts on session management and RLS security.
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
- [x] `feat/admin-tools-reorg` : Admin Tools Overhaul — Renamed LogViewerModal to AdminTools, reorganized hierarchy (Timeline, Stats, Device, Tools), and integrated as primary dashboard diagnostic hub.
- [x] `feat/crew-discovery-refinement` : Refine 'Live Near You' discovery — Integrated membership-based session discovery and removed static crew browsing for a session-first Hub experience.

## ✅ Completed Previously

---
*Last updated: 2026-04-10 | This session: Music Mode Parity achieved.*
