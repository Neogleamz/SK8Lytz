# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Needs Testing NOW

- [ ] **#1 — Test the Expanded LED Diagnostic Lab** — Launch from Admin Tools on phone. Test the DEVICES scanner to TARGET a device. Use BUILDER tab to synthesize and fire `0x59`, `0x61`, `0x73`, and `0x62` packets with byte annotations.
- [ ] **#2 — Verify LED color mapping on device** — App now sends pure RGB (no software color sorting on 0x59 paths). Verify hardware's native GRB remapping correctly displays Red=Red and Green=Green in STREET, PRESET, and MULTI modes.
- [ ] **#3 — Verify Transition Types on device** — Solid mode (Pattern 1) and Street car lights use `0x01` (FREEZE). Animated patterns use `0x03` (RunningWater). Confirm solid/street is static and animations scroll smoothly.
- [ ] **#4 — Verify Supabase `led_diagnostics` table** — Confirm Diagnostic Lab successfully pushes telemetry to Supabase. Query table after live test.

---

## 🟠 Medium Priority / Next Sprint

- [ ] **#5 — `CrewModal.tsx` refactor** — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] **#6 — Crew Hub UI: private crew invite code display** — Private crew cards should show invite code prominently under "My Crews". Verify rendering is correct after recent crew changes.
- [ ] **#7 — DIY Array Builder polish** — Review multicolor swatch picker, length slider, and transition mode selector for any UX issues after LED fixes.
- [ ] **#8 — Favorites persistence** — Confirm saved favorites correctly recall mode, colors, speed, pattern after LED payload fixes. Old saved favorites may have stale color data.
- [ ] **#9 — Admin Hardware Tester audit** — Verify `setMultiColor` path in admin tester applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [ ] **#10 — Camera Mode: Touch Precision fix** — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel.
- [ ] **#11 — Street Mode: Cruise LED bounce animation** — Mid-section cruise LEDs (between rear RED taillights and front WHITE headlights) should bounce/chase between the two zones. Use JS interval to shift a moving LED position within the cruise zone, sending rapid 0x59 FREEZE updates. Head/tail zones stay locked.
- [ ] **#12 — Street Mode: Tail light dimming** — Rear RED taillights should be **50% brightness** while CRUISING or ACCELERATING, ramp to **100%** when BRAKING or STOPPED. Fix `applyStreetPattern()`: `tailBright = isBraking ? factor : factor * 0.5`.
- [ ] **#13 — Visualizer: Street Mode parity** — `ProductVisualizer.tsx` must reflect tail dimming (50% cruise, 100% brake) and show the bouncing cruise LED animation matching hardware output.
- [ ] **#14 — Dashboard Header Layout** — Move Support and Theme icons to the far RIGHT side of the header, grouped next to each other matching Auth card icon style. Username/status pill left-justified. Use the same `MaterialCommunityIcons` set and sizing already in the app.

---

## 🟡 Backlog

- [ ] **#15 — Device Grouping Audit & Redesign** — A "ghost group" keeps persisting across installs. Need to: (1) Audit all grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups` Supabase table; (2) Document where auto-grouping fires; (3) Design consistent naming convention, decide opt-in vs opt-out; (4) Ensure group state fully clears on sign-out and fresh install.
- [ ] **#16 — Web E2E Verification** — Map static thumbnails and address autocomplete in browser environment.
- [ ] **#17 — Native Platform Verification** — MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] **#18 — Automated Testing Suite** — Expand E2E tests for Location flows, crew session creation, and LED payload correctness.
- [ ] **#19 — Security & Performance Review** — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.
- [ ] **#20 — Telemetry ingestion verification** — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly. Query after a real session.
- [ ] **#21 — Crew Hub layout refinement** — Spacing, dashboard layout, member count display, and session timer as platform grows.
- [ ] **#22 — `setColor()` in ZenggeProtocol** — Does NOT apply color sorting. Should be removed or marked internal-only. Production paths that call it directly must use `sendColor()` instead.
- [ ] **#23 — `0x81` legacy command audit** — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls.

---

## ✅ Recently Completed

- [x] Lab DEVICES tab wired into content render block (Apr 2026)
- [x] Lab transmit() now targets specific device via targetDeviceId (Apr 2026)
- [x] Diagnostic Lab safe area padding (notch/nav bar overlap fixed) (Apr 2026)
- [x] PatternEngine: restored 0x03 RunningWater for animated patterns (Apr 2026)
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
*Last updated: 2026-04-08 | All items numbered #1–#23.*
