# PLAN: Gradient Builder — Hardware Corrections & Enhancements

**Slug**: `feat/gradient-builder-corrections`  
**Created**: 2026-04-22  
**Status**: 🔲 Not Started  
**Risk**: M-RISK  
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem Summary

`PositionalGradientBuilder.tsx` is a fully functional builder UI — color pins, gradient/solid fill, animation types, preset save/load — but it has **3 hardware-correctness bugs** and **2 UX gaps** that need fixing before it can be trusted for production use.

---

## Bug 1: Hardcoded LED Count (CRITICAL)

### Root Cause
`PositionalGradientBuilder.tsx:236` generates the preview array with hardcoded `100`:
```ts
const previewLeds = PositionalMathBuffer.generateArray(nodes, 100, fillMode === 'GRADIENT');
```
And `line 194` maps speed against `deviceLedCount` but the preview bar always shows 100 segments regardless of actual hardware.

The live dispatch (line 192) does use `deviceLedCount` correctly:
```ts
const generatedRgbArray = PositionalMathBuffer.generateArray(nodes, deviceLedCount, fillMode === 'GRADIENT');
```
So hardware gets the right length — but the **visual preview bar** lies to the user.

### Fix
```ts
// Line 236 — fix preview to match actual device
const previewLeds = PositionalMathBuffer.generateArray(nodes, deviceLedCount, fillMode === 'GRADIENT');
```

**Files**: `src/components/PositionalGradientBuilder.tsx:236`  
**Risk**: Zero — pure cosmetic fix, no protocol change.

---

## Bug 2: Max Pin Count Not Hardware-Aware (HIGH)

### Root Cause
Line 305 hardcodes `MAX 16` label:
```tsx
<Text>LAYOUT (MAX 16)</Text>
```
Line 340 gates the add button at `nodes.length < 16`.

For HALOZ (`ledPoints=8`), you can place 16 pins on an 8-LED canvas — more pins than LEDs. For SOULZ (`ledPoints=43`), you're capped at 16 on a 43-LED strip — artificially limiting expressiveness.

### Fix
```tsx
// Derive max pins from device — cap at ledPoints, max 32
const maxPins = Math.min(deviceLedCount, 32);

// Line 305
<Text>LAYOUT (MAX {maxPins})</Text>

// Line 340
{nodes.length < maxPins && ( <add button> )}
```

**Files**: `src/components/PositionalGradientBuilder.tsx:305,340`

---

## Bug 3: STROBE Animation Label is Wrong (MEDIUM)

### Root Cause
The animation chip row (line 414–420):
```ts
{ id: 3, label: 'STROBE' },
{ id: 4, label: 'WATER' },
```
These `id` values map directly to `transitionType` in the `0x59` payload.

From `ZENGGE_PROTOCOL_BIBLE.md` ground truth:
- `0x02` = STROBE (alternating flash)  
- `0x03` = RunningWater / SCROLL (continuous scroll — NOT strobe)

So the current mapping sends `0x03` when user taps "STROBE" but hardware scrolls instead of strobing. User sees scrolling and thinks strobe is broken.

### Fix
```ts
[
  { id: 0x00, label: 'STATIC' },
  { id: 0x01, label: 'FREEZE' },
  { id: 0x02, label: 'STROBE' },   // ← correct: actual strobe
  { id: 0x03, label: 'SCROLL' },   // ← was 'WATER', renamed to SCROLL
  { id: 0x04, label: 'JUMP' },     // ← verify 0x04 behavior on hardware
]
```

> ⚠️ **Hardware verify required**: confirm `0x04` JUMP behavior on physical HALOZ before shipping. Add to Oracle Lab test if uncertain.

**Files**: `src/components/PositionalGradientBuilder.tsx:414–420`

---

## Enhancement 1: Pin Width/Spread Control (TIER 2)

### What
Each `BuilderNode` currently has `position` (0–100%) as a point. Add optional `width` (0–50%) so the pin "occupies" a swath of the strip.

### How
Extend `BuilderNode` type in `src/protocols/PositionalMathBuffer.ts`:
```ts
export interface BuilderNode {
  id: string;
  position: number;   // 0–100, center of zone
  colorHex: string;
  width?: number;     // 0–50%, defaults to 0 (point)
}
```

Update `PositionalMathBuffer.generateArray()` to treat pins with `width > 0` as flat zones before gradient interpolation kicks in between zone edges.

Add a small width slider in the Active Pin Editor section (component line 356–371), shown only when fill mode is not GRADIENT.

**Files**:
- `src/protocols/PositionalMathBuffer.ts` — extend type + generator logic
- `src/components/PositionalGradientBuilder.tsx:356–371` — add width slider UI

---

## Enhancement 2: Friendlier Animation Labels (TIER 1)

Replace the current cryptic chip labels with icons + labels:

| Old Label | New Label | Icon | Hardware byte |
|-----------|-----------|------|--------------|
| STATIC | ◼ STATIC | `led-strip` | `0x00` |
| GRADUAL | 〜 FADE | `gradient` | `0x01` |
| STROBE | ⚡ STROBE | `flash` | `0x02` |
| WATER | ▶ SCROLL | `arrow-right` | `0x03` |
| JUMP | ↕ JUMP | `swap-vertical` | `0x04` |

---

## Implementation Order

```
1. Bug 1 (preview bar) — 1 line, ship immediately
2. Bug 3 (STROBE label) — update ids, verify 0x04 on hardware
3. Bug 2 (max pins) — derive from deviceLedCount
4. Enhancement 2 (better labels + icons) — cosmetic, any time
5. Enhancement 1 (pin width) — requires PositionalMathBuffer change, test thoroughly
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/components/PositionalGradientBuilder.tsx` | Bugs 1,2,3 + Enh 2 |
| `src/protocols/PositionalMathBuffer.ts` | Enh 1 (BuilderNode width) |

---

## Test Criteria

- [ ] Preview bar pixel count matches `deviceLedCount` for HALOZ (8) and SOULZ (43)
- [ ] Max pins cap respects `deviceLedCount`
- [ ] Tapping STROBE chip sends `0x02` (not `0x03`) — verify with Oracle Lab or BLE sniffer
- [ ] Tapping SCROLL sends `0x03` — hardware scrolls the gradient
- [ ] Width-enabled pins render a flat zone in the preview LED bar
- [ ] All existing presets load correctly after `BuilderNode` type extension (width optional)
