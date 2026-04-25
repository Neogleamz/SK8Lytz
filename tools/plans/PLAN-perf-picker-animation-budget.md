# PLAN: perf/picker-animation-budget

## Summary

Reduce the concurrent animation timer overhead inside the `UnifiedPatternPicker` by
gating `LEDStripPreview` and `CustomEffectVisualizer` animation intervals based on
viewport visibility, and replacing the expensive string-serialization frame-diff
guard with a lightweight numeric hash.

**Blocked by:** `perf/visualizer-engine-overhaul` (Task A) — must be merged first.

---

## Problem Statement

When `UnifiedPatternPicker` is open in MultiMode, it renders a grid of pattern cards.
Each card contains either a `<LEDStripPreview>` or `<CustomEffectVisualizer>` with its
own `setInterval` animation loop:

- `LEDStripPreview`: 50ms interval (20fps) — calls `getVisualizerFrame()` per tick
- `CustomEffectVisualizer`: 16–200ms interval — calls `getVisualizerFrame()` per tick

With 20+ cards visible in the picker (and more cards mounted off-screen in the scroll
container), this creates **20+ concurrent `setInterval` loops** all calling PatternEngine
math simultaneously on the JS thread. Even with `React.memo`, each interval's `setFrame`
call triggers a mini-reconcile for that card's LEDs.

Additionally, `LEDStripPreview` performs an expensive string serialization on every tick
to detect frame changes:
```typescript
const serialized = nextFrame.map(c => `${c.r},${c.g},${c.b}`).join('|');
// For 43-LED arrays: 43 string templates + join on every 50ms tick
```

---

## Fix 1 — `UnifiedPatternPicker.tsx`: Visibility tracking via `onViewableItemsChanged`

Use the `FlatList` / `ScrollView` `onViewableItemsChanged` callback to track which
pattern card indices are currently visible in the viewport. Pass an `isVisible` prop
to each `<LEDStripPreview>` / `<CustomEffectVisualizer>` card.

```typescript
// UnifiedPatternPicker.tsx
const [visibleIndices, setVisibleIndices] = useState<Set<number>>(new Set());

const onViewableItemsChanged = useCallback(({ viewableItems }) => {
  setVisibleIndices(new Set(viewableItems.map(item => item.index)));
}, []);

const viewabilityConfig = useRef({ viewAreaCoveragePercentThreshold: 10 });

// In FlatList/render:
<LEDStripPreview
  ...
  autoPlay={visibleIndices.has(index)}  // only animate if on screen
/>
```

The `autoPlay` prop already exists on `LEDStripPreview` (line 21) — it just needs
to be driven dynamically rather than defaulting to `true` always.

`CustomEffectVisualizer` needs `autoPlay` prop added (currently always-on).

---

## Fix 2 — `LEDStripPreview.tsx`: Lightweight frame-diff hash

Replace the string serialization frame-diff guard with an integer sum hash:

```diff
- const serialized = nextFrame.map(c => `${c.r},${c.g},${c.b}`).join('|');
- if (serialized !== prevFrameRef.current) {
-   prevFrameRef.current = serialized;
+ // Sum of all channel values — O(n) with zero string allocation
+ const hash = nextFrame.reduce((acc, c) => acc + c.r * 65536 + c.g * 256 + c.b, 0);
+ if (hash !== prevFrameRef.current) {
+   prevFrameRef.current = hash;
    setFrame(nextFrame);
  }
```

Change `prevFrameRef` type from `useRef<string>('')` to `useRef<number>(0)`.

**Correctness:** The hash can theoretically collide (e.g., two different frames that
sum to the same value), but for the LED visualizer context this is harmless — a missed
frame update simply holds the previous frame for one tick (50ms), imperceptible to the
user.

---

## Fix 3 — `CustomEffectVisualizer.tsx`: Add `autoPlay` prop + early-exit for static patterns

```typescript
interface CustomEffectVisualizerProps {
  ...
  autoPlay?: boolean;  // NEW — default true for backward compatibility
}

// In useEffect:
useEffect(() => {
  if (autoPlay === false) return; // ← early exit, no interval started
  const frameRate = Math.max(16, 200 - (speed * 1.8));
  ...
}, [speed, autoPlay]);
```

---

## Execution Order

1. Fix 2 (`LEDStripPreview.tsx`) — zero-dep, pure optimization
2. Fix 3 (`CustomEffectVisualizer.tsx`) — add prop, backward-compatible
3. Fix 1 (`UnifiedPatternPicker.tsx`) — wire visibility → autoPlay

---

## Files Modified

| File | Change |
|---|---|
| `src/components/LEDStripPreview.tsx` | Fix 2: integer hash frame diff |
| `src/components/CustomEffectVisualizer.tsx` | Fix 3: autoPlay prop |
| `src/components/patterns/UnifiedPatternPicker.tsx` | Fix 1: visibility tracking |

## Collateral Damage Lock

- `PatternEngine.ts` — DO NOT TOUCH
- `VisualizerUnit.tsx` — DO NOT TOUCH (Task A scope)
- `DockedController.tsx` — DO NOT TOUCH
