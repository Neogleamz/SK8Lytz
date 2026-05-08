# SK8Lytz Persistence Contract

This document clarifies the architectural boundary between the two primary local persistence domains used by the SK8Lytz application.

## 1. `useControllerPersistence.ts` (The UI State)

*   **Storage Key:** `@Sk8lytz_ControllerState`
*   **Purpose:** Persists pure UI widget state. This ensures that when a user opens the app, the sliders, color pickers, and mode selections look exactly as they left them.
*   **What it Stores:**
    *   `activeMode` (Street, Color, Music, etc.)
    *   `selectedColor`, `brightness`, `speed`
    *   `fixedFgColor`, `fixedBgColor`
*   **What it DOES NOT Store:** Actual BLE hardware dispatch data. Just because a slider is at 50% doesn't mean that 50% was successfully transmitted to the physical skate.

## 2. `useDeviceStateLedger.ts` (The Hardware Ledger)

*   **Storage Key:** `@SK8Lytz_DeviceState_v2_{MAC}`
*   **Purpose:** The single source of truth for what the physical hardware is currently running.
*   **What it Stores:**
    *   `mode` (The raw opcode/mode sent)
    *   `fgColor`, `bgColor`
    *   `rawPayload` (The actual hex array dispatched over BLE)
*   **When it Updates:** Only upon successful (or intentionally fired) BLE characteristic writes.

## The "Restore Session" Rule

If building a feature that asks, *"What was the device doing last time?"* (e.g., resuming a session or displaying a preview card):
**👉 You MUST read from `useDeviceStateLedger.ts`.**

If building a feature that asks, *"What should the UI sliders default to when opening the controller?"*
**👉 You MUST read from `useControllerPersistence.ts`.**

Never mix the two. Reading `fgColor` from the Controller Persistence and sending it to hardware as a "resume" action will cause drift if the UI state was updated while disconnected.
