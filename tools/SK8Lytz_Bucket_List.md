# SK8Lytz Master Bucket List

All active tasks, bugs, and feature work. Prioritized. Updated every session.

---

## 🔴 High Priority / Needs Testing NOW

- [ ] **Verify LED color swap fix on device** — GRB sort now gated on `detected` flag. Connect HALOZ/SOULZ, confirm red=red, green=green across MULTIMODE, PRESETS, STREET. If still wrong, the 0x63 response `colorSorting` byte is returning wrong value and needs a deeper protocol trace.
- [ ] **Verify Street Mode on device** — Confirm CRUISING/STOPPED show static car lights (no scrolling). Confirm HARD_BRAKING strobes. Confirm color order is now correct.
- [ ] **Verify MULTIMODE patterns on device** — Patterns 2–5, 9–10 (RunningWater) should show moving tiled patterns, not slow blinks. Pattern 1 (Solid) should be fully static. Patterns 6–8 (0x51) should breathe/flash/strobe correctly.
- [ ] **Verify LED count from probed hardware** — Confirm `hwSettings.ledPoints` is being populated from the 0x63 response (check via Admin Hardware Tester or AppLogger logs). All LEDs should be addressed — no dark/unlit LEDs on SOULZ (43) or HALOZ (16).

---

## 🟠 Medium Priority / Next Sprint

- [ ] **`CrewModal.tsx` refactor** — 14 useEffect hooks, 2,600+ lines. Extract `useCrewHub()` and `useCrewSession()` custom hooks. Highest maintainability debt in the codebase.
- [ ] **Crew Hub UI: private crew invite code display** — Private crew cards should show invite code prominently under "My Crews". Verify this is rendering correctly after recent crew changes.
- [ ] **DIY Array Builder polish** — Review multicolor swatch picker, length slider, and transition mode selector for any UX issues after LED fixes.
- [ ] **Favorites persistence** — Confirm saved favorites correctly recall mode, colors, speed, pattern after LED payload fixes. Old saved favorites may have stale color data.
- [ ] **Admin Hardware Tester** — Verify `setMultiColor` path in admin tester also applies color sorting correctly (currently does NOT call `applyColorSorting` — review `AdminHardwareTester.tsx` L169).

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
*Last updated: 2026-04-07 | LED color + transition type fixes deployed. APK built + installed.*
