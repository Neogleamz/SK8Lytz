# Telemetry Coverage Audit & Ingestion (Task #30)

During my code trace, I discovered that **100% of the newly added UI workflows completely bypass the `AppLogger` system.** 
While we track the raw BLE payloads sent to the device, we do not know what the user *specifically tapped* in the application.

## User Review Required

> [!CAUTION]
> The `DockedController.tsx` handles almost every major user interaction (Street Mode toggles, Custom Presets, Favorites, and Picks) but currently contains exactly ZERO invocations of `AppLogger.log`. This leaves us blind to feature engagement metrics.

I propose injecting targeted `AppLogger` telemetry hooks into the UI components to establish full observability. 

## Proposed Changes

---

### UI Controllers

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
We will inject `AppLogger.log()` explicitly into the callback definitions:
- **Picks:** Log `PICK_SELECTED` with the pick's `id` and `name` when a user taps a card under the "SK8Lytz Picks" column.
- **Favorites:** Log `FAVORITE_RENDERED` with the exact pattern properties (to differentiate from generic BLE pattern pushes).
- **Builder:** Log `BUILDER_PRESET_SAVED` and `BUILDER_UI_TOGGLED` to track engagement with the procedural light engine grid.

#### [MODIFY] [StreetModeProvider.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/contexts/StreetModeProvider.tsx)
- The provider currently handles the accelerometer data and calculates speed, but we must explicitly log `STREET_SENSITIVITY_CHANGED` and `STREET_JERK_DETECTED` from the core provider if they aren't fully bubbled up, or ensure `DockedController.tsx` logs the explicit manual UI toggles.

## Open Questions

> [!IMPORTANT]
> Is there a specific threshold for telemetry ingestion you want to enforce? For example, should we log *every single drag* on the Speed Slider, or only log it `onSlidingComplete` to prevent flooding the database with hundreds of packets per second?

## Verification Plan
1. Launch the web demo.
2. Tap on a Pick, drag a slider, and activate Builder.
3. Query the `parsed_logs` table via Supabase to confirm the `PICK_SELECTED` and `BUILDER_PRESET_SAVED` events trigger precisely once per interaction.
