# Restore Visualizer Animation Smoothness (Hardware Parity) & FlatList Refactor

The recent PatternEngine refactor broke the visualizer loop by passing `animTick` directly into all 61 pattern builders. This caused discrete patterns to animate at a "chunky" 2-4 FPS and caused continuous math patterns to visually "snap" back to zero midway through the loop. Furthermore, rendering the massive number of new Symphony templates via a `ScrollView` `.map` is blocking the React JS thread by hundreds of milliseconds, throwing `[Violation] 'message' handler took <N>ms`.

This plan restores the buttery smooth architectural intent for both the math loop and the React DOM node limit.

## Proposed Changes

### src/protocols/PatternEngine.ts
- **[MODIFY] getVisualizerFrame**: 
  - Change the `generateArray` call to selectively pass `tick = 0` for all **spatial 0x59 patterns**.
  - Continue passing the live `animTick` for **temporal 0x51 patterns** (IDs 20-43, 70-72, 201-244).
  - Re-introduce `rotateArray()` at the end of the function for spatial patterns.
- **[MODIFY] rotateArray**:
  - Update `rotateArray` to accept `direction: 0 | 1`.
  - Negate `animTick` appropriately so that `direction=0` scrolls backwards.

### src/components/patterns/PatternPickerTab.tsx
- **[MODIFY] Component**:
  - Convert the `ScrollView` that wraps the `filteredTemplates.map` into a `<FlatList>`.
  - Set `data={filteredTemplates}`.
  - Set `numColumns={2}` to maintain the grid layout.
  - Remove the manual `onLayout` debouncing gate (`visibleIds`), as `FlatList` has built-in virtualization which prevents off-screen items from rendering heavy DOM nodes or running intervals simultaneously.
  - Update grid wrapper styling (`columnWrapperStyle`) to support the `justifyContent: 'space-between'` gap.
  - **ADDITION (Click Latency Fix)**: Extract the `renderItem` function from being defined inline to a stable `useCallback` with an empty dependency array. Use a `useRef` to pass volatile state (`selectedEffectId`, `fgColor`, etc.) into the stable `renderItem` without triggering a new closure creation.
  - **ADDITION**: Pass the volatile state variables to `FlatList` via the `extraData` prop so that the virtualization engine knows to re-diff the cells when state changes, without tearing down the DOM wrappers.

### src/components/ProductVisualizer.tsx
- **[MODIFY] Component**:
  - **ADDITION (Memory Leak/Render Storm Fix)**: Wrap the fallback `renderDevices` array creation in a `useMemo`. When no `devices` array is passed (e.g. in the Web Demo), creating an inline `[{ name: product... }]` array breaks the `React.memo` inside the child `VisualizerUnit`. This ensures `VisualizerUnit` only re-renders when actual props (like `patternId`) change.

## Verification Plan
1. **Performance Test:** Open the `Test` category. It should instantly load without throwing `[Violation] 'message' handler took <N>ms` in the terminal.
2. **Visual Test:** Marquees and Barber Poles should slide across the 12-LED preview one physical pixel at a time.
3. **Looping:** Ocean Wave and Lava Lamp loop infinitely without any jarring "snaps".
4. **Symphony Parity:** Native Breathe and Native Strobe still pulse correctly.
