# PLAN: perf/visualizer-engine-overhaul

## Summary

Fix two intertwined correctness and performance problems in the `DashboardScreen` →
`ProductVisualizer` → `VisualizerUnit` rendering chain.

**Correctness Bug (confirmed in production):** The probed `hwSettings.segments` value
(HALOZ=2) is never forwarded to `VisualizerUnit`. The visualizer silently defaults
`deviceSegments=1`, causing it to run the pattern engine over 16 LEDs instead of 8 and
render the wrong pattern geometry on physical hardware.

**Performance Bug:** `VisualizerUnit.leds` is a `useMemo` keyed to `animTick` — a React
state value that changes every animation frame (~60fps). This forces the React reconciler
to re-run the entire PatternEngine frame computation + hex-to-RGB parsing for every LED on
every animation frame, saturating the JS thread identically to the hex payload logging
that was previously removed.

---

## Architecture Constraints (Must Not Violate)

- `DashboardScreen.tsx` is the master fortress. Do NOT restructure BLE state ownership.
- `DockedController.tsx` is a Core Fortress file. Touch ONLY the single prop pass-through
  line that routes `hwSettings` downstream.
- `VisualizerUnit.tsx` uses `Animated.Value` from `react-native` (NOT Reanimated2).
  Do NOT introduce Reanimated as a new dependency.
- The Visualizer = Skates contract must be maintained. The pixel count used for the
  visualizer frame MUST equal `hwSettings.ledPoints` (the Layer 1 canvas value).

---

## Root Cause Chain

```
DashboardScreen
  activeHwSettings = { ledPoints: 8, segments: 2, ... }   ← correctly computed ✅

  <DockedController hwSettings={activeHwSettings} points={displayConnectedDevices[0].points} />
                                                  ^^^^^^^^^ BUG: raw field, not hwSettings.ledPoints

DockedController
  <ProductVisualizer points={points} devices={devices} />
  ← hwSettings is NEVER forwarded to ProductVisualizer     ← BUG: hwSettings dropped here

ProductVisualizer
  <VisualizerUnit device={dev} fallbackPoints={points} />
  ← device.segments may be missing on un-probed paths       ← BUG: segments silently = 1

VisualizerUnit
  devicePoints   = device?.points || fallbackPoints || vizDefaultPoints  → 16
  deviceSegments = device?.segments || 1                                 → 1 (BUG)
  numLeds        = floor(16 / 1) = 16                                    → WRONG (should be 8)

  leds = useMemo(() => {
    const fgRgb = hexToRgb(fgColor);   // hex parse on EVERY frame tick
    const bgRgb = hexToRgb(bgColor);   // hex parse on EVERY frame tick
    return getVisualizerFrame(..., tick);  // PatternEngine math on EVERY frame tick
  }, [animTick, fgColor, bgColor, patternId, ...]);
  ← animTick changes every frame → memo busts every frame → 60fps JS thread work  ← BUG
```

---

## Step-by-Step Execution Checklist

### Step 1 — `DashboardScreen.tsx` (Surgical — 1 line change)

**Target:** Line 578 — the `points` prop on `<DockedController>`.

**Problem:** `points={(displayConnectedDevices[0] as any).points}` passes the raw BLE
device field. If BLE hasn't hydrated `.points` yet, this is `undefined`.

**Fix:** Change to use `activeHwSettings.ledPoints` which already has a reliable fallback
chain (line 533):

```diff
- points={(displayConnectedDevices[0] as any).points}
+ points={activeHwSettings.ledPoints}
```

**Also add** `hwSettings` forwarding to `ProductVisualizer` via DockedController.
Since DockedController already receives `hwSettings={activeHwSettings}` (line 571), we
just need to route it through.

**Collateral Damage Lock:** Do NOT touch any other prop on `<DockedController>`. Do NOT
refactor `activeHwSettings` computation.

---

### Step 2 — `DockedController.tsx` (Surgical — 1 prop addition)

**Target:** The `<ProductVisualizer>` JSX block (approximately line 708).

**Problem:** `ProductVisualizer` has no `hwSettings` prop — the probed segment/ledPoints
data is silently dropped here.

**Fix:** Pass `hwSettings` to `ProductVisualizer`:

```diff
  <ProductVisualizer
    product={activeProduct}
    color={visualizerColor}
+   hwSettings={hwSettings}
    ...
  />
```

**Collateral Damage Lock:** Touch ONLY the `<ProductVisualizer>` JSX block. Do NOT
restructure DockedController state. Do NOT touch BLE write paths.

---

### Step 3 — `ProductVisualizer.tsx` (Interface + prop forward)

**Target:** `ProductVisualizerProps` interface and `VisualizerUnit` call sites.

**Changes:**

1. Add `hwSettings?: { ledPoints?: number; segments?: number }` to
   `ProductVisualizerProps`.

2. Forward `hwSettings` to each `<VisualizerUnit>` in the render loop:

```diff
  <VisualizerUnit
    key={dev.id || index.toString()}
    device={dev}
+   hwSettings={hwSettings}
    ...
  />
```

3. Fix the `renderDevices` fallback (line 78) to use `hwSettings.ledPoints` instead of
   the raw `points` prop for consistency:

```diff
  const renderDevices = (devices && devices.length > 0) ? devices : (
    isPaired
-     ? [{ name: `${product} Left`, type: product, points }, { name: `${product} Right`, type: product, points }]
-     : [{ name: product, type: product, points }]
+     ? [{ name: `${product} Left`, type: product, points: hwSettings?.ledPoints || points }, { name: `${product} Right`, type: product, points: hwSettings?.ledPoints || points }]
+     : [{ name: product, type: product, points: hwSettings?.ledPoints || points }]
  );
```

**Collateral Damage Lock:** Do NOT touch the animation loop in ProductVisualizer.
Do NOT change the `animValue` Animated.loop setup.

---

### Step 4 — `VisualizerUnit.tsx` (The Main Surgery)

This is the most complex step. Two sub-tasks:

#### 4a — hwSettings prop wiring (Correctness fix)

Add `hwSettings?: { ledPoints?: number; segments?: number }` to the component props.

Replace the fragile device-field derivation with authoritative hwSettings:

```diff
- const devicePoints   = device?.points || fallbackPoints || productProfile.vizDefaultPoints;
- const deviceSegments = device?.segments || 1;
+ const devicePoints   = hwSettings?.ledPoints || device?.points || fallbackPoints || productProfile.vizDefaultPoints;
+ const deviceSegments = hwSettings?.segments  || device?.segments || 1;
  const numLeds        = Math.floor(devicePoints / deviceSegments);
```

This ensures the probed `segments=2` for HALOZ flows all the way to the frame math.

#### 4b — Decouple animTick from the leds useMemo (Performance fix)

**Current broken pattern:**
```typescript
const [animTick, setAnimTick] = useState(0);
// animValue listener calls setAnimTick(v) on every frame
// → React re-render → leds useMemo busts → PatternEngine runs at 60fps
```

**Target pattern:**
```typescript
// 1. Cache expensive color parsing ONCE per color change (NOT per tick)
const fgRgbMemo = useMemo(() => hexToRgb(fixedFgColor || color), [fixedFgColor, color]);
const bgRgbMemo = useMemo(() => hexToRgb(fixedBgColor || '#000000'), [fixedBgColor]);

// 2. Store the current tick in a ref — NO React state, NO re-render
const tickRef = useRef(0);

// 3. Store the computed LED frame in a ref
const frameRef = useRef<RGB[]>([]);

// 4. Use a React state ONLY for the final render-triggering frame
//    but batch-update it via requestAnimationFrame instead of per-tick
const [displayFrame, setDisplayFrame] = useState<RGB[]>([]);
const rafRef = useRef<number | null>(null);

// 5. animValue listener: update ref, compute frame off React, schedule RAF
useEffect(() => {
  const listenerId = animValue.addListener(({ value }) => {
    tickRef.current = value;
    // Compute new frame entirely in the ref (no setState here)
    frameRef.current = getVisualizerFrame(effectivePatternId, fgRgbMemo, bgRgbMemo, numLeds, value);
    // Only schedule a new RAF if one isn't already pending
    if (!rafRef.current) {
      rafRef.current = requestAnimationFrame(() => {
        setDisplayFrame([...frameRef.current]);
        rafRef.current = null;
      });
    }
  });
  return () => {
    animValue.removeListener(listenerId);
    if (rafRef.current) cancelAnimationFrame(rafRef.current);
  };
}, [animValue, effectivePatternId, fgRgbMemo, bgRgbMemo, numLeds]);
```

**Why this is safe:**
- `fgRgbMemo` and `bgRgbMemo` only recompute when colors change (not on every tick)
- `tickRef` stores the current animation position without triggering renders
- `requestAnimationFrame` naturally throttles state updates to the display refresh rate
- `frameRef` ensures the latest frame is always available even if RAF coalesces updates
- The existing `Animated.loop` + `animValue` setup in `ProductVisualizer` is UNTOUCHED

**Remove** the old `animTick` useState and the `animValue.addListener` that calls
`setAnimTick`. Replace with the above pattern.

**Remove** the old `leds` useMemo entirely — it's replaced by `displayFrame` state.

**Update JSX** to use `displayFrame` instead of `leds` when rendering LED dots.

---

### Step 5 — TypeScript Verification

Run from master (TSC Discipline - Rule 10):
```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```

All errors must be resolved in the WORKTREE files, never in master directly.

---

## Verification Plan

### Manual Verification (requires physical HALOZ device)
1. Connect HALOZ hardware (8 ledPoints, 2 segments)
2. Open MultiMode → select a chase pattern (e.g., Pattern 3)
3. **Before fix:** Pattern renders over 16 LED positions (doubled up, half the ring covered)
4. **After fix:** Pattern renders over 8 LED positions, hardware mirrors to the second segment
5. Verify animation is still smooth (no jank from animTick removal)
6. Switch between patterns rapidly — verify no visual artifacts

### Web/Simulator Verification
1. Launch Expo web
2. Open DockedController → MultiMode
3. Verify `VisualizerUnit` renders the correct number of dots per product
4. Verify smooth animation at 60fps with no console re-render warnings

### TSC Gate
- Zero `error TS` output from `npx tsc --noEmit`

---

## Files Modified

| File | Change Type | Scope |
|---|---|---|
| `src/screens/DashboardScreen.tsx` | Surgical (1 line) | `points` prop on DockedController |
| `src/components/DockedController.tsx` | Surgical (1 prop) | `hwSettings` forward to ProductVisualizer |
| `src/components/ProductVisualizer.tsx` | Interface + prop | Accept and forward `hwSettings` |
| `src/components/VisualizerUnit.tsx` | Core surgery | hwSettings wiring + animTick decoupling |

## Collateral Damage Log

- `useBLE.ts` — DO NOT TOUCH
- `ZenggeProtocol.ts` — DO NOT TOUCH
- `PatternEngine.ts` — DO NOT TOUCH
- `DockedController.tsx` BLE write paths — DO NOT TOUCH
- `ProductVisualizer.tsx` animation loop (`Animated.loop`) — DO NOT TOUCH
