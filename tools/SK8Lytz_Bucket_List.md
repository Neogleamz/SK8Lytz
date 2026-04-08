# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Needs Testing NOW

- [ ] **#1 — Positional Array Builder UI** — Create a new submode in MULTIMODE called `BUILDER`. Implement a node-based positional mapping interface where users can define points along the strip mapped to Hex colors, and transition types (STATIC, RUNNING_WATER, STROBE). (Depends on the Math protocol bindings logic discovered during our 0x59 audit).
- [ ] **#2 — Verify Supabase `led_diagnostics` table** — Confirm Diagnostic Lab successfully pushes telemetry to Supabase. Query table after live test.

---

## 🟠 Medium Priority / Next Sprint

- [ ] **#3 — `CrewModal.tsx` refactor** — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] **#4 — Crew Hub UI: private crew invite code display** — Private crew cards should show invite code prominently under "My Crews". Verify rendering is correct after recent crew changes.
- [ ] **#5 — DIY Array Builder polish** — Review multicolor swatch picker, length slider, and transition mode selector for any UX issues after LED fixes.
- [ ] **#6 — Favorites persistence** — Confirm saved favorites correctly recall mode, colors, speed, pattern after LED payload fixes. Old saved favorites may have stale color data.
- [ ] **#7 — Admin Hardware Tester audit** — Verify `setMultiColor` path in admin tester applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [ ] **#8 — Camera Mode: Touch Precision fix** — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel.
- [ ] **#9 — Street Mode: Cruise LED bounce animation** — Mid-section cruise LEDs (between rear RED taillights and front WHITE headlights) should bounce/chase between the two zones. 
- [ ] **#10 — Street Mode: Tail light dimming** — Rear RED taillights should be **50% brightness** while CRUISING or ACCELERATING, ramp to **100%** when BRAKING or STOPPED. Fix `applyStreetPattern()`: `tailBright = isBraking ? factor : factor * 0.5`.
- [ ] **#11 — Visualizer: Street Mode parity** — `ProductVisualizer.tsx` must reflect tail dimming (50% cruise, 100% brake) and show the bouncing cruise LED animation matching hardware output.
- [ ] **#12 — Dashboard Header Layout** — Move Support and Theme icons to the far RIGHT side of the header, grouped next to each other matching Auth card icon style. Username/status pill left-justified. 
- [ ] **#13 — Lab 0x73 Music Mode parity** — Lab BUILDER's 0x73 section is missing Light Screen (0x27) vs Light Bar (0x26) matrix style toggle, primary/secondary color pickers, and mic source.
- [ ] **#14 — Music Mode: Sound column/drop color not applied** — Main app music mode is functioning but color changes for patterns that use "sound column" and "drop" effects are not being accepted/applied.

---

## 🟡 Backlog

- [ ] **#15 — Community Hub: Builder Preset Integration** — Allow users to submit and pull Custom Builder Presets using the public `shared_scenes` Community Library.
- [ ] **#16 — Device Grouping Audit & Redesign** — A "ghost group" keeps persisting across installs. Need to Audit all grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups`.
- [ ] **#17 — Web E2E Verification** — Map static thumbnails and address autocomplete in browser environment.
- [ ] **#18 — Native Platform Verification** — MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] **#19 — Automated Testing Suite** — Expand E2E tests for Location flows, crew session creation, and LED payload correctness.
- [ ] **#20 — Security & Performance Review** — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.
- [ ] **#21 — Telemetry ingestion verification** — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly.
- [ ] **#22 — Crew Hub layout refinement** — Spacing, dashboard layout, member count display, and session timer as platform grows.
- [ ] **#23 — `setColor()` in ZenggeProtocol** — Does NOT apply color sorting. Should be removed or marked internal-only.
- [ ] **#24 — `0x81` legacy command audit** — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls.

---

## ✅ Recently Completed

- [x] Protocol: Support full 0x51 logic (Apr 2026)
- [x] Visualizer: 1:1 mathematical parity for 33 Custom Step Effects (Apr 2026)
- [x] Add Black and White Color Extremes (Apr 2026)
- [x] Solid Pattern Re-indexing (Custom Mode #1) (Apr 2026)
- [x] Positional Multicolor Gradient/Fill-In System (Deep Dive) (Apr 2026)
- [x] Test the Expanded LED Diagnostic Lab (Apr 2026)
- [x] Verify LED color mapping on device (Apr 2026)
- [x] Street Mode UI Bug (Apr 2026)
- [x] Lab DEVICES tab wired into content render block (Apr 2026)
- [x] Lab transmit() now targets specific device via targetDeviceId (Apr 2026)
- [x] Diagnostic Lab safe area padding (notch/nav bar overlap fixed) (Apr 2026)
- [x] LED Diagnostic Lab fully tested on device — DEVICES scan, TARGET, BUILDER payloads firing (Apr 2026)
- [x] LED color mapping verified on hardware — pure RGB confirmed, GRB remapping working natively (Apr 2026)
- [x] PatternEngine: transition byte research ongoing — 0x03=TRIGGER, 0x00=CASCADE (under test Apr 2026)
- [x] LED color swap fix: pure RGB sent, hardware remaps GRB natively (Apr 2026)
- [x] Crew Hub: private crew shows invite code on card (Apr 2026)
- [x] Crew Hub: shows current leader and public/private status on card (Apr 2026)
- [x] AppLogger telemetry: crew session start/end, create/update/delete/join/leave (Apr 2026)
- [x] AppLogger telemetry: street mode activated/deactivated, jerk detected, sensitivity changed (Apr 2026)
- [x] AsyncStorage key migration: `ng_` → `@Sk8lytz_` namespace standardized (Apr 2026)
- [x] Master Reference cleanup: ~1,200 lines of noise removed, TOC + AI preamble added (Apr 2026)
- [x] Street mode transition type: HARD_BRAKING → Strobe (0x02), CRUISE → FREEZE (0x01) (Apr 2026)
- [x] Crew Hub Edit nav trap: Back always returns to hub landing (Apr 2026)
- [x] Session end flow: End Session button visible and functional in DockedController (Apr 2026)

---
*Last updated: 2026-04-08 | Automated sorting iteration*nd Session button visible and functional in DockedController (Apr 2026)

---
*Last updated: 2026-04-08 | #1 and #2 completed. Items #3–#25 active.*
