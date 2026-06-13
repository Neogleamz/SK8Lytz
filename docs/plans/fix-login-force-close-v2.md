# Implementation Plan - Fix Login Force-Close (Root & BLE Hardening)

The previous attempt hardened the Dashboard and Registration hooks, but the "Force Close" persists. This strongly suggests the crash is occurring in the **parent** (App.tsx) or the **native BLE bridge** during the transition between the Auth and Dashboard screens.

## Design Decisions & Rationale

### Root Hardening (App.tsx)

I found brittle `{ data: { session } }` and `{ data: { subscription } }` destructuring at the root level in `App.tsx`. If these return `null` during an auth transition (even temporarily), the entire app crashes before the Dashboard mounts. I will apply the same "Safe Access" pattern here.

### Native BLE Bridge Safety

`useBLE.ts` instantiates `new BleManager()` in a `useMemo`. If the native side of `react-native-ble-plx` throws during this constructor (e.g. if Bluetooth is disabled but the library expects it enabled for the constructor), the app crashes. I will wrap this in a safe guard.

### Telemetry Enrichment

I will add `AppLogger` breadcrumbs specifically to the `AppContent` state transitions in `App.tsx` to help us see exactly which side of the `sessionLoaded` / `isAuthenticated` gate the app is on when it dies.

## Proposed Changes

### [App.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/App.tsx)

- [MODIFY] Harden `getSession` and `onAuthStateChange` destructuring.
- [MODIFY] Add `AppLogger` breadcrumbs for session changes.

### [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)

- [MODIFY] Wrap `new BleManager()` in a `try-catch` to prevent native bridge initialization crashes.

### [AuthScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/AuthScreen.tsx)

- [MODIFY] Add telemetry to `onAuthSuccess` to track the exact moment of transition.

## UI & Platform Strategy

Universal logic hardening. No UI changes.

## Verification Plan

### Automated Tests

- `npx expo export:embed --platform android` to verify syntax.

### Manual Verification

- Ask the user to re-install and attempt login.
- If successful, the app should stay open.
