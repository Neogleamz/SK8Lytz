# PLAN: Fix Telemetry Bloat in Connection Phase

## Goal
Prevent organic "Device disconnected" events during the connection negotiation phase from triggering `PROTOCOL_ERROR` or `BLE_CONNECTION_ERROR` database logs.

## Context
In a previous sprint (`bug/fix-telemetry-bloat`), we suppressed normal organic dropouts from flooding VIP telemetry by catching strings like `"was disconnected"` inside `handleNotification`. 
However, if a device connects but drops out during the MTU negotiation or setup phase (inside `connectToDevices`), the `catch` blocks around line 337 and 410 in `useBLE.ts` still log it as a critical `BLE_CONNECTION_ERROR` with the message "FAILED TO CONNECT TO INDIVIDUAL DEVICE".

## Proposed Changes

### `src/hooks/useBLE.ts`
- Inside `connectToDevices`, locate the catch blocks that handle `deviceError` and `lastErr` (around lines 337 and 410).
- Extract the error message string.
- If the error message includes `"was disconnected"`, `"is not connected"`, `"Device disconnected"`, or `"not connected"`, suppress the telemetry log and downgrade the local `AppLogger.error` to an `AppLogger.warn` indicating an organic setup dropout.

## Verification
- Review code visually (this is a simple conditional check).
- Run `tsc --noEmit` to verify type safety.
