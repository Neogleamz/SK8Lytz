# Fix Device Setup Blink

The hardware setup wizard's `BLINK` button is currently failing to illuminate the LED controllers when pressed.

## Design Decisions & Rationale

1. The current code uses `ZenggeProtocol.setSymphonyColor()`, which generates a legacy `0x41` payload. Recent findings indicate that `0x59` is the reliable and mandatory command for pushing static arrays and basic solid colors to the hardware.
2. I will replace the `0x41` transmission with the universally supported `0x59` payload via `ZenggeProtocol.setMultiColor()`, pushing a solid 43-LED green array.
3. The process will send the green payload, wait 500ms, and then send `ZenggeProtocol.turnOff()` to complete the physical blink, matching the previous timeline intent.

## Proposed Changes

### `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`

- Replace `ZenggeProtocol.setSymphonyColor(0, 255, 0)` with `ZenggeProtocol.setMultiColor(Array(43).fill({r: 0, g: 255, b: 0}), 1, 1, 0x00)`.

## Verification Plan

1. Launch the app and clear setup state if necessary to trigger Hardware Setup.
2. Complete Step 1 (Hardware Setup).
3. On Step 2, tap `BLINK` on a discovered controller.
4. Verify the LEDs glow solid green for 500ms before returning to the off state.
