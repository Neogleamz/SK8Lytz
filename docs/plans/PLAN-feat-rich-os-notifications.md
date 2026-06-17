# Upgrade OS-Native Notifications: The "Rich Session" Architecture

This plan outlines the architectural upgrade from basic text-based foreground notifications to industry-standard interactive session trackers, drawing inspiration from top-tier apps like Strava, Uber, and Spotify.

## User Review Required

> [!WARNING]
> **Native Code Impact**
> Implementing iOS Live Activities and Android Custom Views requires dropping down to native code (SwiftUI and Kotlin/XML) via Expo Config Plugins or custom native modules. This increases the maintenance surface area.

## Open Questions

> [!IMPORTANT]
> 1. **iOS Live Activities:** Do we want to commit to building a SwiftUI widget for the Dynamic Island/Lock Screen? (This is the only way to get a truly custom, rich UI on iOS lock screens).
> 2. **Favorites Limitation:** Android notifications comfortably fit up to 3 action buttons. If we add `[End Session]` and `[Music Mode]`, we only have room for 1 `[Favorite]` pill. Should we prioritize the 3 actions?
> 3. **Headless BLE Reliability:** Firing a BLE payload from a killed/backgrounded state via a notification button is notoriously tricky on iOS. Are we okay with this requiring the app to be at least "suspended" in the background rather than force-killed?

## 1. What the Giants Are Doing (Industry Standards)

Top developers have abandoned standard text-based notifications for active, long-running sessions.

*   **Strava / Uber (iOS):** They use **ActivityKit (Live Activities)**. This places a rich, custom SwiftUI component on the Lock Screen and in the Dynamic Island. It bypasses the standard notification center entirely and updates locally with zero latency.
*   **Spotify (Android):** Uses `Notification.MediaStyle` to create a dedicated mini-player with up to 5 custom interactive buttons (Play, Pause, Skip, Heart) that execute background intents instantly.
*   **Nike Run Club (Android):** Uses **Custom `RemoteViews`**. Instead of standard text, they load an XML layout into the notification drawer, allowing for giant typography (e.g., Distance) and custom-styled pill buttons.
*   **The "Headless Dispatch":** When a user taps an action (like "Like" a song), the OS fires a Broadcast Receiver (Android) or App Extension (iOS) that executes a headless background task. The app's UI does *not* come to the foreground, but its core logic runs invisibly.

## 2. The SK8Lytz Upgrade Path

We will upgrade our Notifee implementation to match these standards.

### A. Android Custom RemoteView (The "Dashboard" Notification)
Instead of standard text, we build a custom XML layout for the Notifee foreground service.
*   **Visuals:** Large, stylized telemetry typography (Speed / Distance).
*   **Action Buttons (Max 3):** `[End Session]`, `[Toggle Music Mode]`, and `[Top Favorite Pill]`.

### B. iOS Live Activities & Dynamic Island
We replace the standard iOS location notification with a full ActivityKit integration.
*   **Dynamic Island:** Shows a pulsing colored dot (matching current skate color) and current speed in the island when the app is minimized.
*   **Lock Screen Widget:** A rich SwiftUI widget showing distance, speed, and interactive buttons.

### C. Headless UI Dispatch (The "Quick Fire" Clarification)
To clarify your question: We are **not** bypassing the `BleWriteDispatcher` or writing a separate raw BLE sender just for notifications! 
*   **How it works:** When you tap "Favorite" on the locked screen, the OS wakes up the React Native Javascript thread *in the background* (this is what "headless" means — it runs without rendering the UI screens).
*   The headless task simply calls our existing standard `useProtocolDispatch()` or `BleWriteDispatcher` queue, exactly as if you had tapped the button inside the app. 
*   **The Benefit:** You don't have to unlock your phone, open the app, and wait for the screen to render just to change the lights. The existing app logic handles it invisibly.

## Proposed Changes

---

### Phase 1: Android Custom Layout & Headless Dispatch

#### [MODIFY] `package.json`
Add background task execution libraries if needed, though Notifee handles basic headless events.

#### [MODIFY] `src/context/SessionContext.tsx`
Expand the `notifee.onBackgroundEvent` and `notifee.onForegroundEvent` listeners to catch `quick-favorite-1` and `toggle-music-mode` actions.
Route these directly to the `BleWriteDispatcher`.

#### [MODIFY] `src/services/session/NotificationService.ts`
Implement `android.customView` properties for Notifee to load a native XML layout. Update the `setInterval` loop to push data to the custom view elements rather than standard body text.

---

### Phase 2: iOS Live Activities (Requires Native Module)

#### [NEW] `modules/sk8lytz-live-activities/`
Create a local Expo module with Swift/Objective-C to expose ActivityKit to React Native, allowing us to start, update, and end a Live Activity with telemetry data.

#### [NEW] `ios/sk8lytz/Sk8lytzWidget/`
Write the native SwiftUI code that defines the visual layout of the Lock Screen and Dynamic Island widgets.

## Verification Plan

### Automated Tests
- Mock `notifee.onBackgroundEvent` to verify that tapping a "Favorite" pill successfully enqueues a write to `BleWriteDispatcher` without requiring React component mounts.
- Verify `SessionMachine` handles the new `TOGGLE_MUSIC` events originating from the notification service.

### Manual Verification
- **Android:** Lock the phone, trigger a favorite from the notification drawer, and visually confirm the skates change color while the screen remains locked.
- **iOS:** Minimize the app and verify the Dynamic Island expands to show current skate speed.
