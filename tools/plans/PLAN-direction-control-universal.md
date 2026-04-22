# PLAN: Universal Direction Control

**Slug**: `feat/direction-control-universal`
**Created**: 2026-04-22
**Status**: 🔲 Not Started — implement alongside Phase 1A/1B (required by new patterns)
**Risk**: L-RISK (additive — new prop, backward compatible)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Overview

Direction (forward/reverse) is a universal parameter for all PatternEngine patterns.
Patterns where `supportsDirection: true` show a direction toggle in the UI.
All patterns receive `direction: 0 | 1` in their frame builder — they just ignore it if irrelevant.

---

## Step 1: Add `direction` to PatternTemplate

**File**: `src/constants/CustomEffects.ts`

```typescript
// Add to SK8LytzTemplate interface:
interface SK8LytzTemplate {
  id: number;
  name: string;
  icon: string;
  colorMode: 'FG_BG' | 'FG_ONLY' | 'GENERATIVE';
  supportsDirection: boolean;  // ← ADD THIS
  tier: 1 | 2 | 3;            // ← ADD THIS (1=ge.*, 2=Programs, 3=Originals)
  sourceRef?: string;          // ← ADD THIS (e.g. 'ge.OceanWaveEffect')
  group?: string;              // ← ADD THIS for UI grouping
}
```

**Backfill all existing 28 patterns** with correct `supportsDirection` value.

Patterns that support direction (scroll-based):
- Comet, Rainbow Comet, Meteor, Running Water, Marquee variants, all Chases → `true`

Patterns that do NOT support direction:
- Breathing, Strobe, Flash, Center-Out (symmetric), Solid, Split, Trisection → `false`

---

## Step 2: Add `direction` to `getVisualizerFrame()`

**File**: `src/protocols/PatternEngine.ts`

```typescript
// Current signature:
function getVisualizerFrame(patternId: number, fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[]

// New signature:
function getVisualizerFrame(
  patternId: number,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  tick: number,
  direction: 0 | 1 = 0  // default forward — backward compatible
): RGB[]
```

Pass `direction` through to all frame builder functions that use it.

---

## Step 3: Add `direction` to `0x59` Write

**File**: `src/protocols/ZenggeProtocol.ts` — `setMultiColor()` or equivalent

The `0x59` Cascade packet has a direction byte. Confirm the exact byte position in `ZENGGE_PROTOCOL_BIBLE.md`.

```typescript
// 0x59 direction byte: 0x00 = forward, 0x01 = reverse
static setMultiColor(
  pixels: RGB[],
  speed: number,
  direction: 0 | 1 = 0,
  transition: number = 0x03  // 0x01=FREEZE, 0x02=STROBE, 0x03=SCROLL
): number[] {
  // ... existing implementation, use direction byte at correct position
}
```

---

## Step 4: Add Direction State to Controller

**File**: `src/hooks/useControllerDispatch.ts`

```typescript
// Add to controller state:
const [direction, setDirection] = useState<0 | 1>(0);

// Add to applyFixedPattern call:
const applyFixedPattern = useCallback((patternId, fg, bg, speed) => {
  const pixels = getVisualizerFrame(patternId, fg, bg, numLEDs, currentTick, direction);
  const payload = ZenggeProtocol.setMultiColor(pixels, speed, direction, transition);
  writeToDevice(payload);
}, [direction, numLEDs, writeToDevice]);

// Expose:
return { ..., direction, setDirection };
```

---

## Step 5: Add Direction Toggle to UI

**File**: `src/components/UniversalSlidersFooter.tsx` or the Unified Pattern Picker

```tsx
{/* Only show if current pattern supportsDirection */}
{currentPattern?.supportsDirection && (
  <View style={styles.directionRow}>
    <TouchableOpacity
      onPress={() => setDirection(direction === 0 ? 1 : 0)}
      style={styles.directionToggle}
    >
      <MaterialCommunityIcons
        name={direction === 0 ? 'arrow-right' : 'arrow-left'}
        size={20}
        color={Colors.accent}
      />
      <Text style={styles.directionLabel}>
        {direction === 0 ? 'Forward' : 'Reverse'}
      </Text>
    </TouchableOpacity>
  </View>
)}
```

---

## Step 6: Persist Direction in Favorites

**File**: `src/services/FavoritesService.ts`

The V2 favorites schema (see `PLAN-pattern-favorites-v2.md`) already includes `direction`.
Ensure it is saved and restored correctly.

---

## Files To Touch

| File | Change |
|------|--------|
| `src/constants/CustomEffects.ts` | Add `supportsDirection`, `tier`, `sourceRef`, `group` to template interface + backfill all 28 |
| `src/protocols/PatternEngine.ts` | Add `direction` param to `getVisualizerFrame()` |
| `src/protocols/ZenggeProtocol.ts` | Ensure direction byte is used in `setMultiColor()` |
| `src/hooks/useControllerDispatch.ts` | Add `direction` state, expose `setDirection` |
| `src/components/UniversalSlidersFooter.tsx` | Add conditional direction toggle |
| `src/services/FavoritesService.ts` | Persist direction in favorites |

---

## Test Criteria

- [ ] Pattern with `supportsDirection: true` shows direction toggle in UI
- [ ] Pattern with `supportsDirection: false` has NO direction toggle
- [ ] Comet chase: tap Reverse → hardware plays comet going opposite direction
- [ ] Same pattern, same colors, different direction → different pixel array sent → Oracle Lab confirms direction byte change in `0x59` packet
- [ ] `getVisualizerFrame()` with `direction: 1` renders reversed animation in ProductVisualizer
- [ ] Visualizer and hardware match in both directions
- [ ] Direction saved to favorites, restored correctly
- [ ] `npx tsc --noEmit` — zero errors
