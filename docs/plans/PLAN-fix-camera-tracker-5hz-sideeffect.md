# Implementation Plan: fix/camera-tracker-5hz-sideeffect

## Goal
Remove the 5Hz BLE side-effect leak from `CameraTracker.tsx`. Currently, `dispatchSniperColor` (line 103-108) calls `onColorDetectedRef.current(hex)` on every frame processor tick (5 times per second). Even though `CameraPanel` now gates the BLE dispatch behind a button tap, the CameraTracker component itself still fires the raw callback at 5Hz. If any future consumer wires `onColorDetected` directly to a BLE-dispatching function, the JS thread flood instantly returns.

## Root Cause (Cited Truth)
```typescript
// CameraTracker.tsx L103-108 (CURRENT)
const dispatchSniperColor = useCallback((r: number, g: number, b: number) => {
  if (isNaN(r) || isNaN(g) || isNaN(b)) return;
  const hex = rgbToVividHex(r, g, b);
  setLiveHex(hex);
  onColorDetectedRef.current(hex);  // ← FIRES AT 5Hz — leaks into any consumer
}, []);
```

## Proposed Change

### [MODIFY] [CameraTracker.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CameraTracker.tsx)

Remove `onColorDetectedRef.current(hex)` from `dispatchSniperColor`. The CameraTracker should ONLY update its own local visual state (the reticle color). The `onColorDetected` prop should only be invoked from explicit user actions in the parent component (CameraPanel's capture button or swatch selection).

```diff
  const dispatchSniperColor = useCallback((r: number, g: number, b: number) => {
    if (isNaN(r) || isNaN(g) || isNaN(b)) return;
    const hex = rgbToVividHex(r, g, b);
    setLiveHex(hex);
-   onColorDetectedRef.current(hex);
  }, []);
```

**Secondary change**: Since `CameraPanel` needs access to the live hex for its capture button, expose the current `liveHex` value via a ref callback or state-lifting pattern so CameraPanel can read it on-demand when the user taps capture.

This may require:
- Adding a new `onLiveHexUpdate?: (hex: string) => void` prop that CameraPanel uses purely for local UI state (reticle color update)
- OR having CameraTracker expose a `liveHexRef` via `useImperativeHandle`

## Design Decision
The cleanest approach is to keep `onColorDetected` as the "live hex update" callback (used by CameraPanel to update its local `liveHex` state for the reticle), and ensure CameraPanel NEVER forwards this to BLE — it only dispatches on explicit user taps. This is the current state after our CameraPanel fix, but the naming is confusing. A rename of the prop to `onLiveHexUpdate` would make the contract clearer.

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest.

### Manual Verification
- Deploy to physical device.
- Open Camera Mode SNIPER — verify reticle still tracks live color.
- Verify that BLE writes ONLY fire when tapping the capture button (check logcat for `0x59` writes).
- Open Camera Mode VIBE — verify gradient preview updates live, BLE only fires on "APPLY VIBE."
