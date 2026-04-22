# PLAN: EPIC-004 Phase 3 — Unified UX, LEDStripPreview & Scene Builder Finish Line

**Slug**: Multiple — `feat/unified-pattern-picker-ui`, `feat/led-strip-preview-component`, `feat/scene-builder-32-slot`
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: H-RISK (major UI restructure + new hardware feature)
**Prerequisites**:
- `fix/chunked-ble-framing-0x51` MUST be done (writeChunked infrastructure)
- `feat/symphony-effect-visualizer-port` MUST be done (SymphonyEffectSimulator.ts)
- `feat/pattern-engine-expansion-group7/8/9` MUST be done (45 total patterns)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Overview

Phase 3 brings everything together into one cohesive, world-class LED control UI. Three deliverables ship together:

1. **`<LEDStripPreview>`** — reusable animated LED strip React component
2. **Unified Pattern Picker** — replaces the fragmented current pickers with one tabbed UI
3. **32-Slot Scene Builder** — the finish line: compose HD light shows from all 45 patterns + 33 symphony effects

---

## Part 1: `<LEDStripPreview>` Component

### Purpose
A reusable animated component that renders a strip of N colored dots, driven by PatternEngine OR SymphonyEffectSimulator. Used everywhere: pattern picker cards, scene builder step preview, favorites shelf.

### Props Interface
```typescript
interface LEDStripPreviewProps {
  // Source: either PatternEngine or Symphony
  mode: 'pattern' | 'symphony';
  effectId: number;              // patternId (1-45) or symphonyId (1-33)

  // Colors
  fg: string;                   // hex color
  bg: string;                   // hex color

  // Hardware
  numLEDs: number;              // from hwSettings.ledPoints

  // Animation
  speed?: number;               // 1-100
  direction?: 0 | 1;
  autoPlay?: boolean;           // default true

  // Display
  dotSize?: number;             // default 6px
  height?: number;              // default 16px
  style?: ViewStyle;
}
```

### Implementation
```typescript
// src/components/LEDStripPreview.tsx
export const LEDStripPreview = React.memo(({ mode, effectId, fg, bg, numLEDs, speed, autoPlay = true, dotSize = 6, height = 16 }: LEDStripPreviewProps) => {
  const animRef = useRef(new Animated.Value(0)).current;
  const [frame, setFrame] = useState<RGB[]>([]);

  useEffect(() => {
    if (!autoPlay) return;
    const interval = setInterval(() => {
      const tick = (Date.now() % (1000 / (speed || 50) * 100)) / (1000 / (speed || 50) * 100);
      if (mode === 'pattern') {
        setFrame(getVisualizerFrame(effectId, hexToRgb(fg), hexToRgb(bg), numLEDs, tick));
      } else {
        const ticker = SYMPHONY_EFFECTS[effectId];
        if (ticker) setFrame(ticker(tick, hexToRgb(fg), hexToRgb(bg), numLEDs, speed || 50));
      }
    }, 50); // 20fps — enough for smooth previews
    return () => clearInterval(interval);
  }, [mode, effectId, fg, bg, numLEDs, speed, autoPlay]);

  return (
    <View style={{ height, flexDirection: 'row', borderRadius: 4, overflow: 'hidden' }}>
      {frame.map((c, i) => (
        <View key={i} style={{ flex: 1, backgroundColor: `rgb(${c.r},${c.g},${c.b})` }} />
      ))}
    </View>
  );
});
```

**File**: `src/components/LEDStripPreview.tsx` [NEW]

---

## Part 2: Unified Pattern Picker UI

### Architecture Decision
Replace the current fragmented pickers (EffectsPanel, MultiModePanel each has own list) with ONE `<UnifiedPatternPicker>` component used everywhere.

### Three Tabs
```
┌──────────────────────────────────────┐
│  [ PATTERNS (45) ] [ SYMPHONY (33) ] [ SCENES ] │
└──────────────────────────────────────┘
```

### Tab 1: Math Patterns (45 effects)
- Grid of cards, 2 columns
- Each card: pattern name + `<LEDStripPreview mode="pattern" effectId={id} />` animated live
- FG/BG color pickers at top (persistent across card selection)
- Tap card → fires `applyFixedPattern(id, fg, bg, speed)` immediately
- Speed slider at bottom

### Tab 2: Symphony Effects (33 effects)
- Same grid layout as patterns
- `<LEDStripPreview mode="symphony" effectId={id} />` for each card
- Color pickers respect `getSymphonyColorGate(id)` — show FG+BG, FG only, or hidden
- Tap → fires `ZenggeProtocol.setSettledMode(id, fg, bg, speed, direction)`
- Direction toggle visible only for `supportsDirection: true` effects

### Tab 3: Scenes
- Empty state with "Create New Scene" CTA → opens Scene Builder modal
- Saved scenes listed with `<LEDStripPreview>` preview of first step
- Tap → loads scene, fires it to hardware

### Component Structure
```
src/components/patterns/
  UnifiedPatternPicker.tsx      [NEW] — top-level with tabs
  PatternPickerTab.tsx          [NEW] — grid for patterns
  SymphonyPickerTab.tsx         [NEW] — grid for symphony
  ScenePickerTab.tsx            [NEW] — saved scenes list
  PatternCard.tsx               [NEW] — individual card with LEDStripPreview
```

### Integration Points
- `DockedController.tsx` → replace current `EffectsPanel` + `MultiModePanel` references with `<UnifiedPatternPicker>`
- State passed in: `fg`, `bg`, `speed`, `direction`, `currentPatternId`, `currentMode`

---

## Part 3: 32-Slot Scene Builder

### What It Does
Users compose a light show of up to 32 steps. Each step plays for a set duration then advances. The full sequence is sent as a `0x51` extended payload via `writeChunked()`.

### Data Model
```typescript
interface SceneStep {
  id: string;
  mode: 'pattern' | 'symphony';
  effectId: number;              // pattern 1-45 or symphony 1-33
  fg: string;                   // hex
  bg: string;                   // hex
  speed: number;                // 1-100
  duration: number;             // seconds this step plays (1-60)
  direction: 0 | 1;
}

interface Scene {
  id: string;
  name: string;
  steps: SceneStep[];           // max 32
  user_id?: string;             // for cloud save
  created_at: string;
}
```

### UI Layout
```
┌─────────────────────────────────────┐
│  SCENE BUILDER                [SAVE]│
├─────────────────────────────────────┤
│  Step 1 ─────────────────── [+]     │
│  [LEDStripPreview]                  │
│  Comet Chase  FG:■  BG:■  ⏱ 3s    │
│  ─────────────────────────────────  │
│  Step 2                             │
│  [LEDStripPreview]                  │
│  Police Lights  FG:■  BG:■  ⏱ 2s  │
│  ─── [+ ADD STEP] ─────────────     │
└─────────────────────────────────────┘
│  [PREVIEW ALL]        [FIRE TO SKATES] │
```

### Hardware Protocol Mapping
Each `SceneStep` maps to one slot in the `0x51` extended payload:
```
Slot format (10 bytes):
[effectTypeId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, duration, flags]
```

- `effectTypeId`: pattern IDs 1–45 map to ZenggeProtocol step mode constants
- Duration is sent as seconds (1–60) — hardware plays each step for that duration then advances
- After last step, hardware loops back to step 0

> ⚠️ Verify exact 10-byte slot format against `tools/ZENGGE_PROTOCOL_BIBLE.md` — the full 0x51 extended spec must be confirmed from APK before building this.

### Scene Persistence
```typescript
// Supabase table: scenes
// Columns: id, user_id, name, steps (JSONB), created_at, updated_at

// Local fallback: AsyncStorage '@Sk8lytz_Scenes'
```

Same local/cloud merge pattern as `PositionalGradientBuilder` presets.

### Component Structure
```
src/components/scenes/
  SceneBuilderModal.tsx         [NEW] — full screen modal
  SceneStepCard.tsx             [NEW] — individual step editor card
  SceneStepPicker.tsx           [NEW] — pick pattern or symphony for a step
src/services/ScenesService.ts   [MODIFY] — add save/load/delete for Scene type
src/hooks/useSceneBuilder.ts    [NEW] — builder state: steps CRUD, dispatch, save
```

---

## Files To Create/Modify

| File | Status | Purpose |
|------|--------|---------|
| `src/components/LEDStripPreview.tsx` | NEW | Reusable animated strip |
| `src/components/patterns/UnifiedPatternPicker.tsx` | NEW | Tabbed picker shell |
| `src/components/patterns/PatternPickerTab.tsx` | NEW | 45-pattern grid |
| `src/components/patterns/SymphonyPickerTab.tsx` | NEW | 33-symphony grid |
| `src/components/patterns/ScenePickerTab.tsx` | NEW | Saved scenes list |
| `src/components/patterns/PatternCard.tsx` | NEW | Individual card |
| `src/components/scenes/SceneBuilderModal.tsx` | NEW | Full scene builder UI |
| `src/components/scenes/SceneStepCard.tsx` | NEW | Single step editor |
| `src/hooks/useSceneBuilder.ts` | NEW | Scene builder state & dispatch |
| `src/services/ScenesService.ts` | MODIFY | Add Scene CRUD |
| `src/components/DockedController.tsx` | MODIFY | Wire in UnifiedPatternPicker |

---

## Test Criteria

### LEDStripPreview
- [ ] Renders for all 45 pattern IDs without crash
- [ ] Renders for all 33 symphony IDs without crash
- [ ] Animation updates at ~20fps (smooth visually)
- [ ] `autoPlay=false` renders a static single frame

### Unified Picker
- [ ] Tab switching preserves selection state
- [ ] Tapping pattern card triggers hardware write within 100ms
- [ ] Symphony tab hides FG/BG pickers for `NONE` gate effects
- [ ] Direction toggle hidden for effects that don't support it

### Scene Builder
- [ ] Can add up to 32 steps
- [ ] Cannot add step 33 (add button disabled at 32)
- [ ] Reordering steps updates preview correctly
- [ ] "Fire to Skates" sends `writeChunked()` with correct 0x51 payload
- [ ] Scene saved locally if not logged in, to cloud if logged in
- [ ] Saved scene appears in Scene Picker tab with correct preview
- [ ] Hardware loops through all steps and repeats
