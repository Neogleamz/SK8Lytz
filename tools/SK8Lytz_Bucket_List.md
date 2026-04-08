# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Needs Testing NOW

- [ ] **Test the Expanded LED Diagnostic Lab** — Launch from Admin Tools. Test the new DEVICES scanner to explicitly TARGET a device. Use the BUILDER tab to manually synthesize and fire `0x59`, `0x61`, `0x73`, and `0x62` packets tracking byte annotations.
- [ ] **Verify LED color mapping on device** — Software color sorting (`applyColorSorting`) has been removed from 0x59 paths. The app now sends pure RGB. Verify that the hardware's native (`0x81`) GRB fix correctly displays Red for Red and Green for Green during STREET, PRESET, and MULTI modes.
- [ ] **Verify Transition Types on device** — Solid mode (Preset 1) and Street car lights now use `0x01` (FREEZE). Animated modes use `0x00` (CASCADE). Verify Solid/Street is completely static, and animations scroll smoothly without blinking.
- [ ] **Verify Supabase `led_diagnostics` table** — Confirm that using the Diagnostic Lab successfully pushes telemetry results into the Supabase database.

---

## 🟠 Medium Priority / Next Sprint

- [ ] **`CrewModal.tsx` refactor** — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] **Crew Hub UI: private crew invite code display** — Private crew cards should show invite code prominently under "My Crews". Verify this is rendering correctly after recent crew changes.
- [ ] **DIY Array Builder polish** — Review multicolor swatch picker, length slider, and transition mode selector for any UX issues after LED fixes.
- [ ] **Favorites persistence** — Confirm saved favorites correctly recall mode, colors, speed, pattern after LED payload fixes. Old saved favorites may have stale color data.
- [ ] **Admin Hardware Tester** — Verify `setMultiColor` path in admin tester also applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).
- [ ] **Camera Mode: Touch Precision fix** — Color picker swatch is sampling too large an area. Touch/tap should sample ONLY the pixel directly under the finger and run the existing color enhancement routine on that single pixel. Current behavior picks up too wide a region causing color averaging artifacts.
- [ ] **Street Mode: Cruise LED bounce animation** — The mid-section cruise LEDs (between rear RED taillights and front WHITE headlights) should bounce/chase between the two fixed zones rather than sitting static. Use software-driven JS interval to shift a moving LED position within the cruise zone, sending rapid 0x59 FREEZE updates. Keep head/tail zones locked.
- [ ] **Street Mode: Tail light dimming** — Rear RED taillights should be at **50% brightness** while CRUISING or ACCELERATING, and ramp to **100% brightness** when BRAKING or STOPPED. Currently both states use a hardcoded modifier that does not dim correctly. Update `applyStreetPattern()` and the visualizer to reflect: `tailBright = isBraking ? factor : factor * 0.5`.
- [ ] **Visualizer: Street Mode parity** — `ProductVisualizer.tsx` Street Mode rendering must reflect the tail dimming changes (50% cruise, 100% brake) and show the bouncing cruise LED animation to match hardware output.
- [ ] **Dashboard Header Layout** — Move Support and Theme icons to the far RIGHT side of the header, grouped together and matching the icon style used on the Auth card. Username/status pill should be left-justified. All icons must use the same `MaterialCommunityIcons` set and sizing already established in the app. Reference the Auth card header as the design target.

---

## 🟡 Backlog

- [ ] **Web E2E Verification** — Map static thumbnails and address autocomplete in browser environment.
- [ ] **Native Platform Verification** — MapView bounds on physical Android/iOS devices for coordinate accuracy.
- [ ] **Automated Testing Suite** — Expand E2E tests for Location flows, crew session creation, and LED payload correctness.
- [ ] **Security & Performance Review** — Routine RLS audit on Supabase queries; optimize React Native render cycles for dashboard gauges.
- [ ] **Telemetry ingestion verification** — Confirm AppLogger events (crew, street mode, hardware config) are landing in Supabase `device_logs` table correctly. Query the table after a real session.
- [ ] **Crew Hub layout refinement** — Spacing, dashboard layout, member count display, and session timer as platform grows.
- [ ] **`setColor()` in ZenggeProtocol** — Does NOT apply color sorting (by design, for raw testing). Should either be removed or clearly marked internal-only. Any production code paths that still call it directly need to be routed through `sendColor()` instead.
- [ ] **`0x81` legacy command audit** — Confirm it's no longer being sent on connect. `0x62` (EEPROM write) is the correct command. Remove any remaining `0x81` calls if found.
- [ ] **Device Grouping Audit & Redesign** — A "ghost group" keeps persisting across installs and users can't control naming or pairing. Need to: (1) Audit all legacy grouping logic in `DashboardScreen.tsx`, `AsyncStorage`, and `registered_groups` Supabase table; (2) Document exactly where auto-grouping fires and what triggers it; (3) Design a consistent group naming convention and decide whether auto-group should be opt-in or opt-out; (4) Ensure group state is fully cleared on sign-out and fresh install. Treat this as a hardening project before the next public release.

---

## ✅ Recently Completed

- [x] Crew Hub: private crew shows invite code on card (Apr 2026)
- [x] Crew Hub: shows current leader and public/private status on card (Apr 2026)
- [x] AppLogger telemetry: crew session start/end, create/update/delete/join/leave (Apr 2026)
- [x] AppLogger telemetry: street mode activated/deactivated, jerk detected, sensitivity changed (Apr 2026)
- [x] AsyncStorage key migration: `ng_` → `@Sk8lytz_` namespace standardized (Apr 2026)
- [x] Master Reference cleanup: ~1,200 lines of noise removed, TOC + AI preamble added (Apr 2026)
- [x] Master Reference: AppLogger EventType catalog, Street Mode spec, Crew Hub schema, Build guide, Known Issues documented (Apr 2026)
- [x] LED color swap fix: `activeHwSettings.colorSorting` now gated on `detected` flag (Apr 2026)
- [x] `applyColorSorting` added to `sendColor()` and `applyStreetPattern()` (Apr 2026)
- [x] Street mode transition type: CRUISING → Static (0x00), HARD_BRAKING → Strobe (0x02) (Apr 2026)
- [x] Crew Hub Edit nav trap: Back always returns to hub landing, not stacked cards (Apr 2026)
- [x] Session end flow: End Session button visible and functional in DockedController (Apr 2026)

---
*Last updated: 2026-04-08 | Lab targeting fixed, dashboard header layout + device grouping audit added to backlog.*
