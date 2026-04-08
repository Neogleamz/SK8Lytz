# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Next Up

- [ ] **Device Registration & Claim Process Review** — Revisiting the user-to-device ownership flow. Need to brainstorm and build a new implementation plan as the previous extensive one was lost.
- [ ] **Review Lab Discoveries & Payload Integration** — Review the Lab again to integrate all new protocol discoveries and payload changes made recently (especially 0x51 effects and custom segments).
- [ ] **#3 — `CrewModal.tsx` refactor** — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] **#13 — Lab 0x73 Music Mode parity** — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [ ] **#14 — Music Mode: Sound column/drop color not applied** — Main app music mode is functioning but color changes for patterns using "sound column" and "drop" effects are not being accepted/applied.

---

## 🟠 Medium Priority / Next Sprint

- [ ] **#7 — Admin Hardware Tester audit** — Verify `setMultiColor` path in admin tester applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [ ] **#8 — Camera Mode: Touch Precision fix** — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel.
- [ ] **#15 — Visualizer Parity: Pro Effects Patterns** — Exhaustively review and tune the interpolation mathematics for all 33 of the 'pro effects' patterns to make them physically accurate within the product visualizer.
- [ ] **#16 — Device Grouping Audit & Redesign** — A "ghost group" keeps persisting across installs. Audit all grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups`.
- [ ] **#22 — Crew Hub layout refinement** — Spacing, dashboard layout, member count display, and session timer as platform grows.
- [ ] **#23 — `setColor()` in ZenggeProtocol** — Does NOT apply color sorting. Should be removed or marked internal-only.
- [ ] **#24 — `0x81` legacy command audit** — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls.
- [ ] **#28 — SK8Lytz Picks Admin Scheduler** — Build admin UI to manage the `sk8lytz_picks` table scheduling. DB columns (`active_from`, `active_until`, `is_active`) already in place. Goal: seasonal picks (4th of July, Christmas, etc.) auto-show/hide. Needs: admin screen, date pickers, toggle per pick.
- [ ] **#29 — Modern RGB Hue Slider** — Design and implement a more sophisticated, high-precision RGB hue selection component to replace the standard sliders.

---

## 🟡 Backlog

- [ ] **#2 — Verify Supabase `led_diagnostics` table** — Confirm Diagnostic Lab successfully pushes telemetry to Supabase. Query table after live test.
- [ ] **#17 — Web E2E Verification** — Map static thumbnails and address autocomplete in browser environment.
- [ ] **#18 — Native Platform Verification** — MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] **#19 — Automated Testing Suite** — Expand E2E tests for Location flows, crew session creation, and LED payload correctness.
- [ ] **#20 — Security & Performance Review** — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.
- [ ] **#21 — Telemetry ingestion verification** — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly.
- [ ] **#27 — Community Hub: Builder Preset Integration** — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library.
- [ ] **#30 — AppLogger Coverage Audit** — Comprehensive audit of all features added this session (Street Mode, Picks, Favorites, Builder) to ensure 100% telemetry coverage in Analytics.
- [ ] **#34 — Card Swipe Navigation** — Add the ability to swipe left and right to navigate back and forth between cards (Favorites, Picks, Presets) for a more fluid mobile UX.

---

- [x] **#1 — Positional Array Builder UI** — Builder submode in MULTIMODE with node-based gradient interface
- [x] **#4 — Crew Hub: private crew invite code display** — Private crew cards show invite code under "My Crews"
- [x] **#5 — DIY Array Builder retired** — Fully replaced by new Builder workflow; legacy DIY mode scrubbed
- [x] **#6 — Favorites persistence** — Legacy DIY/RBM mode entries migrated to BUILDER/PROGRAMS on load; write-back to AsyncStorage
- [x] **#9 — Street Mode: Cruise LED bounce** — Hardware chase ticker bounces bright spot through mid-zone on each dispatch
- [x] **#10 — Street Mode: Tail light dimming** — Absolute brightness: 100% braking (R=255), 50% cruising (R=127), not slider-scaled
- [x] **#11 — Visualizer Street Mode parity** — `motionState` prop drives tail opacity and cruise bounce in visualizer
- [x] **#12 — Dashboard Header Layout** — User pill left-justified, Support & Theme icons right-grouped, matching Auth screen style
- [x] **#25 — Builder UI Enhancement** — ADD PIN moved into visual map as blank circular pin
- [x] **#26 — SK8Lytz Picks DB Migration** — Moved to `sk8lytz_picks` Supabase table with seasonal scheduler columns
- [x] **#31 — Legacy Tool Retirement** — Retired Simple Scanner and Admin Hardware Tester; consolidated discovery and testing into LED Diagnostic Lab.
- [x] **#32 — Diagnostic Lab UI Modernization** — Restyled Lab header, exit logic, and card styling to achieve 1:1 visual parity with the Programmer modal.
- [x] **#33 — Lab Navigation Flow** — Standardized "Exit" behavior to return users to the Analytics view, maintaining context during debug sessions.
- [x] **#35 — Pro Effects Stale Closure Regression Fixed** — ROOT CAUSE: `applyFixedPattern` was a plain `const` that was never included in the reactive `useEffect` dependency array. When `parentWriteToDevice` first became available (device connected), the stale closure silently swallowed all `0x51` dispatches. Fix: wrapped in `useCallback` with `parentWriteToDevice` in deps; moved after dependent `useState` declarations; removed `setTimeout` debounce; added `applyFixedPattern` to reactive `useEffect` deps. Also unified Solid (ID 1) `onPress` LED count to use `hwSettings?.ledPoints` with 12px minimum (was using `devices?.[0]?.points`). Added `0x51` decoder to `ProductVisualizer` so the dot visualizer syncs to active Pro Effect from raw payload.

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
