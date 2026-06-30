# Implementation Plan
# PLAN-animation-render-perf

## Summary
Six confirmed animation and render-performance findings across the visualizer, spectrum analyzer,
dashboard, and pattern picker layers. The highest-severity issues are: 16 independent `Animated.spring`
calls per audio tick on the JS thread (SpectrumAnalyzer), an orphaned animation loop inside that same
component causing a memory leak, addListener-driven `setState` at 60fps causing full hero re-renders
(DashboardTelemetryHero), approximately 20,000 new inline style objects per second inside VisualizerUnit,
N independent 50ms `setInterval` timers per PatternCard, and an O(n²) per-tick computation inside
`useVisualizerLeds`.

**Batch:** `BATCH:animation-render-perf`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files

| ID | File | Line | Severity | Finding |
|----|------|------|----------|---------|
| ANIM-001 | `src/components/docked/SpectrumAnalyzer.tsx` | 61 | HIGH | 16 independent `Animated.spring` calls per audio tick |
| ANIM-002 | `src/components/docked/SpectrumAnalyzer.tsx` | 81 | HIGH | Orphaned animation loop before `ambientAnimationRef` assignment |
| ANIM-003 | `src/components/dashboard/DashboardTelemetryHero.tsx` | 72 | HIGH | `addListener` → `setState` at 60fps, full hero re-render |
| ANIM-004 | `src/components/VisualizerUnit.tsx` | 152 | HIGH | New inline style object array per-LED per-render (~20k/sec) |
| ANIM-005 | `src/components/VisualizerUnit.tsx` | 157 | HIGH | 4 nested Animated.View layers per LED with new inline style objects |
| ANIM-006 | `src/components/LEDStripPreview.tsx` | 45 | MEDIUM | N independent 50ms setInterval timers per PatternCard in FlatList |
| ANIM-007 | `src/components/visualizer/VisualizerHooks.ts` | 142 | MEDIUM | O(n²) path sample scan (86 LEDs × 5000 samples) in useMemo at 60fps |
| ANIM-008 | `src/components/patterns/PatternPickerTab.tsx` | 50 | MEDIUM | `Animated.parallel` started in useEffect without cleanup handle |
| ANIM-009 | `src/components/patterns/GradientLibraryTab.tsx` | 35 | MEDIUM | `PositionalMathBuffer.generateArray` at render time without useMemo |
| ANIM-010 | `src/components/docked/StreetModeDistributionSlider.tsx` | 83 | MEDIUM | Inline width strings computed per-render during pan gestures |

- Raw audit: `artifacts/deepdive_raw/DOMAIN_ANIMATION_AND_PERFORMANCE_findings.json`
- KB: capture required (no existing Animated.parallel / useNativeDriver KB entry)

---

## Findings Detail

### HIGH — SpectrumAnalyzer: 16 Independent Animated.spring Calls (ANIM-001)
**File:** `src/components/docked/SpectrumAnalyzer.tsx:61`

On every `audioMagnitude` tick from the music pipeline, the component iterates `animatedValues`
and calls `Animated.spring(anim, {...}).start()` for each of 16 bars — 16 independent animation
starts per tick, all on the JS thread. `useNativeDriver: false` is unavoidable for `height`/`translateY`
properties on non-Reanimated components.

**Fix:** Batch the 16 starts into a single `Animated.parallel()` so the JS thread sees one animation
batch per tick instead of 16:
```typescript
Animated.parallel(
  animatedValues.map((anim) =>
    Animated.spring(anim, { toValue, useNativeDriver: false, speed: 28, bounciness: 6 })
  )
).start();
```

---

### HIGH — SpectrumAnalyzer: Orphaned Animation Loop Memory Leak (ANIM-002)
**File:** `src/components/docked/SpectrumAnalyzer.tsx:81`

In the DEVICE mic branch (lines 81–85), `runAnimation(val, i * 40)` is called per-value before
`ambientAnimationRef.current = Animated.parallel(loops)` is assigned. The loops started by
`runAnimation()` are never tracked in `ambientAnimationRef` and are therefore never stopped in the
cleanup function. These 16 orphaned `Animated.loop` instances run until the component unmounts.

**Fix:** Remove the `animatedValues.forEach((val, i) => runAnimation(val, i * 40))` block entirely
(lines 81–84). The subsequent `loops` array built by the NEXT block covers the same animation with
identical parameters; only the `Animated.parallel(loops)` stored in `ambientAnimationRef.current`
is tracked and stopped on cleanup.

---

### HIGH — DashboardTelemetryHero: addListener → setState at 60fps (ANIM-003)
**File:** `src/components/dashboard/DashboardTelemetryHero.tsx:72,79`

```typescript
const id = animRatio.addListener(({ value }) => setNeedleAngle(angleForRatio(value)));
```
This drives a React `setState` on every animation frame, causing `DashboardTelemetryHero`
(including all `TelemetryPill` children) to re-render at 60fps during active sessions.

**Fix:** Replace `useState` with `useRef` for `needleAngle`. The ref value is consumed only by the
SVG needle coordinate computation. If the SVG requires a React state to re-render, wrap the needle
tip computation in a dedicated sub-component that reads the ref via `requestAnimationFrame` throttling,
or interpolate needle angle directly as an `Animated.Value` derivation without a setState bridge.

---

### HIGH — VisualizerUnit: Inline Style Object Explosion (ANIM-004, ANIM-005)
**Files:** `src/components/VisualizerUnit.tsx:152,157`

At 86 LEDs × 4 Animated.View layers × 60fps, approximately 20,000 new inline style objects are
allocated per second. The static portion of each LED wrapper style (width, height, alignItems,
justifyContent, overflow, zIndex) is identical across renders for a given `diam` value.

**Fix (ANIM-004):** Extract the static LED wrapper style into a `useMemo` keyed on `diam`:
```typescript
const ledWrapStyle = useMemo(
  () => StyleSheet.flatten({
    width: diam, height: diam,
    alignItems: 'center', justifyContent: 'center',
    overflow: 'visible', zIndex: 10,
  }),
  [diam]
);
// In render: style={[led.position, ledWrapStyle, { opacity: led.chipSoften }]}
```

**Fix (ANIM-005):** For non-FAVORITES modes where `led.activeColor` is a static hex string, create
pre-registered `StyleSheet` styles for the 4 Animated.View layers. Add a `mode !== 'FAVORITES'`
branch that uses these cached styles. FAVORITES mode (where `activeColor` is an interpolation)
must keep inline objects — this eliminates 75%+ of object allocation in the common case.

---

### MEDIUM — LEDStripPreview: N Independent setInterval Timers (ANIM-006)
**File:** `src/components/LEDStripPreview.tsx:45`

Each `PatternCard` in the `FlatList` inside `PatternPickerTab` mounts a `LEDStripPreview` with its
own 50ms `setInterval`. With 8 visible cards, 160 JS-thread callbacks fire per second, each calling
`getVisualizerFrame()`.

**Fix:** Replace N independent intervals with a single RAF-coordinated tick counter at the
`PatternPickerTab` level, passed as a `tick` prop to each `LEDStripPreview`. Each preview computes
its frame from the shared tick rather than maintaining its own timer. This collapses N JS callbacks
per interval into 1 RAF callback total.

---

### MEDIUM — useVisualizerLeds: O(n²) Path Sample Scan at 60fps (ANIM-007)
**File:** `src/components/visualizer/VisualizerHooks.ts:142`

The `useMemo` keyed on `animTick` runs 86 LEDs × up to 5000 path sample scans synchronously on
every animation tick (60fps). While the sequential scan does advance `lastSampleIdx`, the entire
computation is synchronous on the JS thread.

**Fix:** Pre-compute a lookup table (LUT) mapping LED index → path sample index during the
`pathGeometry` phase (which already runs in a separate expensive `useMemo` keyed on product shape
change). The per-tick `useMemo` becomes an O(n) array index lookup. Hex string construction for
static-color modes should also be pre-baked into the LUT where colors are stable.

---

### MEDIUM — AnimatedCategoryPill: Parallel Animation Without Cleanup Handle (ANIM-008)
**File:** `src/components/patterns/PatternPickerTab.tsx:50`

```typescript
useEffect(() => {
  Animated.parallel([
    Animated.spring(scale, { ... }),
    Animated.timing(opacity, { ... })
  ]).start();
}, [isActive, scale, opacity]);
```
No cleanup handle is stored; previous animations are not stopped before new ones start on rapid
`isActive` changes.

**Fix:**
```typescript
useEffect(() => {
  const anim = Animated.parallel([
    Animated.spring(scale, { ... }),
    Animated.timing(opacity, { ... })
  ]);
  anim.start();
  return () => anim.stop();
}, [isActive, scale, opacity]);
```

---

### MEDIUM — GradientLibraryTab: generateArray at Render Time (ANIM-009)
**File:** `src/components/patterns/GradientLibraryTab.tsx:35`

`PositionalMathBuffer.generateArray(preset.nodes, 12, preset.fill_mode === 'GRADIENT')` is called
synchronously at render time inside a `React.memo` GradientCard. If parent state changes cause
re-renders with new object references for `preset`, 20+ cards recompute simultaneously.

**Fix:** Wrap the call in `useMemo`:
```typescript
const previewColors = useMemo(
  () => PositionalMathBuffer.generateArray(preset.nodes, 12, preset.fill_mode === 'GRADIENT'),
  [preset.nodes, preset.fill_mode]
);
```

---

### MEDIUM — StreetModeDistributionSlider: Inline Width Strings During Pan (ANIM-010)
**File:** `src/components/docked/StreetModeDistributionSlider.tsx:83`

Width percentage strings are computed as template literals on every pan gesture move event.
Static style portions (backgroundColor, justifyContent, alignItems) are merged inline alongside
the dynamic width.

**Fix:** Extract static style portions into a `StyleSheet.create` definition. Only the `width`
value remains dynamic. Alternatively, use `Animated.Value` for `t1Pos`/`t2Pos` and drive width
via interpolation.

---

## Implementation Steps (Priority Order)

### Step 1 — SpectrumAnalyzer: Remove Orphaned Loop (ANIM-002) [Highest Risk — Memory Leak]
**File:** `src/components/docked/SpectrumAnalyzer.tsx`
1. Read `src/components/docked/SpectrumAnalyzer.tsx` lines 75–100.
2. Identify the `animatedValues.forEach((val, i) => runAnimation(val, i * 40))` block (lines 81–84).
3. Delete those lines entirely. Confirm the subsequent `loops` array block below it covers the same values.
4. `Verify:` `git diff HEAD src/components/docked/SpectrumAnalyzer.tsx` — confirm only lines 81–84 removed.
5. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 2 — SpectrumAnalyzer: Batch to Animated.parallel (ANIM-001)
**File:** `src/components/docked/SpectrumAnalyzer.tsx`
1. Read the current state of the `animatedValues.forEach` loop that fires per audio tick (post Step 1 state).
2. Replace the 16-way `.start()` call loop with a single `Animated.parallel(animatedValues.map(...)).start()`.
3. `Verify:` `git diff HEAD src/components/docked/SpectrumAnalyzer.tsx` — confirm parallel wrapping is correct.
4. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 3 — DashboardTelemetryHero: Replace setState with useRef (ANIM-003)
**File:** `src/components/dashboard/DashboardTelemetryHero.tsx`
1. Read `src/components/dashboard/DashboardTelemetryHero.tsx` lines 65–95.
2. Replace `const [needleAngle, setNeedleAngle] = useState(0)` with `const needleAngleRef = useRef(0)`.
3. In the `addListener` callback, update: `needleAngleRef.current = angleForRatio(value)`.
4. Evaluate whether the SVG needle reads from state or computed value — if it must re-render, extract
   the needle sub-component with its own RAF-throttled update. Flag to user if architectural decision
   needed.
5. `Verify:` `git diff HEAD src/components/dashboard/DashboardTelemetryHero.tsx`.
6. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 4 — VisualizerUnit: Memoize Static LED Wrapper Style (ANIM-004)
**File:** `src/components/VisualizerUnit.tsx`
1. Read `src/components/VisualizerUnit.tsx` lines 140–170. Check file size before touching: if >30KB
   halt and report [S4 MONOLITH CHECK].
2. Add `ledWrapStyle` useMemo keyed on `diam` above the render return.
3. Replace the inline style array at line 152 with `[led.position, ledWrapStyle, { opacity: led.chipSoften }]`.
4. `Verify:` `git diff HEAD src/components/VisualizerUnit.tsx`.
5. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 5 — VisualizerUnit: Pre-cache Animated.View Layer Styles (ANIM-005)
**File:** `src/components/VisualizerUnit.tsx`
1. Read lines 150–175 (current post-Step 4 state).
2. Add a `layerStylesCache` useMemo keyed on `diam` returning 4 pre-registered layer style objects.
3. In the render, branch on `mode !== 'FAVORITES'`: use cached styles. In FAVORITES mode, keep
   inline objects (unavoidable due to interpolation).
4. `Verify:` `git diff HEAD src/components/VisualizerUnit.tsx` — confirm FAVORITES branch unchanged.
5. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 6 — PatternPickerTab: Add cleanup handle to AnimatedCategoryPill (ANIM-008)
**File:** `src/components/patterns/PatternPickerTab.tsx`
1. Read `src/components/patterns/PatternPickerTab.tsx` lines 44–60.
2. Capture the `Animated.parallel(...)` result into `const anim`. Call `anim.start()`. Return `() => anim.stop()`.
3. `Verify:` `git diff HEAD src/components/patterns/PatternPickerTab.tsx`.
4. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 7 — GradientLibraryTab: Wrap generateArray in useMemo (ANIM-009)
**File:** `src/components/patterns/GradientLibraryTab.tsx`
1. Read `src/components/patterns/GradientLibraryTab.tsx` lines 28–45.
2. Wrap the `PositionalMathBuffer.generateArray(...)` call in `useMemo([preset.nodes, preset.fill_mode])`.
3. `Verify:` `git diff HEAD src/components/patterns/GradientLibraryTab.tsx`.
4. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 8 — StreetModeDistributionSlider: Isolate Static Style (ANIM-010)
**File:** `src/components/docked/StreetModeDistributionSlider.tsx`
1. Read `src/components/docked/StreetModeDistributionSlider.tsx` lines 75–95.
2. Extract static style properties (backgroundColor, justifyContent, alignItems, height, flex) into a
   `StyleSheet.create` block.
3. Keep only `{ width: ... }` as an inline dynamic value.
4. `Verify:` `git diff HEAD src/components/docked/StreetModeDistributionSlider.tsx`.
5. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 9 — LEDStripPreview: Consolidate setInterval to Shared RAF Tick [LARGEST SCOPE]
**File:** `src/components/LEDStripPreview.tsx`, `src/components/patterns/PatternPickerTab.tsx`

> [!WARNING]
> This step requires a prop-interface change across two files. Evaluate the impact on all
> LEDStripPreview callers before executing. If callers outside PatternPickerTab exist, extend
> the shared tick approach to those as well or keep the existing interval for non-FlatList usage.

1. Read `src/components/patterns/PatternPickerTab.tsx` — identify the FlatList render and where
   `LEDStripPreview` is instantiated.
2. Add a top-level `useAnimationFrame` hook or `setInterval` (single instance) in `PatternPickerTab`
   that maintains a `tick` counter in a ref.
3. Pass `tick` as a new optional prop to `LEDStripPreview`. When `tick` prop is provided, the
   component reads from it rather than maintaining its own `setInterval`.
4. Add backward-compatible fallback: if `tick` prop is absent, the component retains its own interval.
5. `Verify:` `git diff HEAD src/components/LEDStripPreview.tsx src/components/patterns/PatternPickerTab.tsx`.
6. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 10 — useVisualizerLeds: Pre-compute Path Sample LUT [DEFERRED — HIGH COMPLEXITY]
**File:** `src/components/visualizer/VisualizerHooks.ts`

> [!NOTE]
> Step 10 is included for completeness but carries the highest implementation complexity. It requires
> modifying the `pathGeometry` useMemo to emit a pre-built LED-to-sample-index LUT. If execution
> time is constrained, Steps 1–9 deliver the majority of the performance gain. Step 10 should be
> executed separately or escalated to a dedicated sub-task.

1. Read `src/components/visualizer/VisualizerHooks.ts` lines 130–160. Check file size before
   touching: if >30KB halt and report [S4 MONOLITH CHECK].
2. In the `pathGeometry` useMemo, compute `ledSampleIndexLUT: number[]` mapping `[0..renderLeds-1]`
   to the correct path sample index.
3. In the per-tick `useMemo`, replace the inner `for` loop scan with `lutIndex = ledSampleIndexLUT[i]`.
4. `Verify:` `npm run verify` — expect 0 TSC errors.
5. `Verify:` Manual: Attach React Native Perf Monitor — JS thread FPS should improve measurably
   during active visualizer rendering.

---

## Verification

- `npm run verify` after each step.
- Manual performance test (Steps 1–2): Open SpectrumAnalyzer in music mode → attach Perf Monitor →
  confirm JS FPS does not drop below 55fps during audio reactive animation.
- Manual re-render test (Step 3): Open DashboardTelemetryHero during active GPS session → attach
  React DevTools profiler → confirm no `setNeedleAngle` re-render storms.
- Manual GC test (Steps 4–5): Open Visualizer with SOULZ product (86 LEDs) for 60 seconds →
  confirm JS memory does not climb continuously in the Perf Monitor.
- Manual interval test (Step 9): Open PatternPickerTab → scroll FlatList → attach Perf Monitor →
  confirm JS thread callback count per second does not scale with visible card count.

---

## Out of Scope
- `src/components/auth/AuthFormSignUp.tsx:56` — password strength animation is low-severity and
  intentionally not included; it is only active during password entry.
- `src/components/dashboard/CrewHubSlab.tsx:51` — dead `pulseAnim` removal; route to TRIAGE as
  a Boy Scout cleanup item on next DashboardScreen touch.
- `src/components/CommunityModal.tsx:48` — `useNativeDriver: false` on translateX; route separately
  as a quick L-RISK snack (`useNativeDriver: true` one-line change).
- `src/components/docked/StreetModeDistributionSlider.tsx:32` — PanResponder stale closure; this is
  an R-17/R-12 issue routed to the MEMORY_LEAKS cluster (Wave 4), not animation perf.
- `src/screens/DashboardScreen.tsx` — S4 MONOLITH (51KB); no edits permitted without monolith
  extraction plan (PLAN-MONOLITH-EXTRACTION.md).

---

## Decision Log
- **Why keep useNativeDriver: false for SpectrumAnalyzer bars?**
  `height` and `translateY`-derived height animations cannot use the native driver in the core
  React Native `Animated` API. Full migration to Reanimated 2+ shared values is out of scope
  for this task (requires DashboardScreen monolith extraction first due to file size).
- **Why batch with Animated.parallel instead of migrating to Reanimated?**
  Surgical-before-heroic (P4). `Animated.parallel` is a 5-line change that eliminates 15 of the
  16 redundant `.start()` calls without introducing a new library dependency. Reanimated migration
  is deferred to a dedicated [Feast] task after the monolith extraction.
- **Why defer Step 10 (LUT pre-computation)?**
  The O(n²) scan in `useVisualizerLeds` is performance-degrading but not a correctness bug.
  Steps 1–9 address the highest-severity render storms first. The LUT optimization can be a
  standalone snack after this plan merges.

---

## Kanban Task Tags
- `[Status: ✅ READY]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: UI]`
- `[Risk: M-RISK]`
- `[Size: Feast]`
- `[Cognitive Load: HIGH]`
- `[WAVE:3]`
- `[BATCH: animation-render-perf]`
