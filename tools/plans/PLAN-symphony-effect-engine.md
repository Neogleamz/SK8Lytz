# PLAN: EPIC-004 Phase 2 — Symphony Effect Parity Engine

**Slug**: `feat/symphony-effect-engine`  
**Created**: 2026-04-22  
**Status**: 🔲 Not Started  
**Risk**: H-RISK  
**Hard Prerequisite**: `fix/0x41-settled-mode-correct-format` MUST ship first  
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Vision

The ZENGGE app's "Symphony" tab shows 33 animated lighting effects (fireworks, ocean, aurora, etc.) each with their own color pickers and direction controls. These run on the hardware using `0x41` Settled Mode — a completely separate opcode from `0x59` (PatternEngine) and `0x42` (RBM). We need to port all 33 of these to TypeScript and wire them to the existing FG/BG color picker and speed UI.

---

## Hard Prerequisite: `fix/0x41-settled-mode-correct-format`

The `0x41` implementation must be verified correct BEFORE building anything on top of it.

**Correct 13-byte format** (from `C7775l.java`):
```
[0x41, effectId, FG.R, FG.G, FG.B, BG.R, BG.G, BG.B, speed, dir, 0x00, 0xF0, checksum]
```

Implement as:
```typescript
// In ZenggeProtocol.ts
static setSettledMode(
  effectId: number,
  fg: { r: number; g: number; b: number },
  bg: { r: number; g: number; b: number },
  speed: number,
  direction: 0 | 1 = 0
): number[] {
  const raw = [
    0x41,
    effectId & 0xFF,
    Math.min(255, fg.r), Math.min(255, fg.g), Math.min(255, fg.b),
    Math.min(255, bg.r), Math.min(255, bg.g), Math.min(255, bg.b),
    Math.max(1, Math.min(255, speed)),
    direction & 0x01,
    0x00, 0xF0,
  ];
  raw.push(ZenggeProtocol.calculateChecksum(raw));
  return ZenggeProtocol.wrapCommand(raw);
}
```

Verify with Oracle Lab (Sk8LytzDiagnosticLab) before proceeding.

---

## Part 1: Symphony Effect Color Gate

### What
The UI must show the correct color pickers per effect ID. From `C9273c.java` in the APK:
- **FG + BG pickers**: effect IDs `[1,2,3,4,5,6,17,18,19,25,31,32]`
- **FG only**: effect ID `[7]`
- **No pickers**: all other IDs

### Implementation

Create `src/constants/SymphonyEffects.ts`:
```typescript
export const SYMPHONY_FG_BG_IDS = new Set([1,2,3,4,5,6,17,18,19,25,31,32]);
export const SYMPHONY_FG_ONLY_IDS = new Set([7]);

export function getSymphonyColorGate(effectId: number): 'FG_BG' | 'FG_ONLY' | 'NONE' {
  if (SYMPHONY_FG_BG_IDS.has(effectId)) return 'FG_BG';
  if (SYMPHONY_FG_ONLY_IDS.has(effectId)) return 'FG_ONLY';
  return 'NONE';
}
```

Use `getSymphonyColorGate(effectId)` in the Symphony tab UI to show/hide pickers.

---

## Part 2: Symphony Effect Visualizer (33 `ge.*` classes)

### What
Port all 33 Java `ge.*` visualizer classes from the APK to TypeScript as a single `SymphonyEffectSimulator.ts` file. Each class becomes a `tick(t: number): string[]` function returning an array of hex color strings (one per LED).

### Architecture

Create `src/utils/SymphonyEffectSimulator.ts`:
```typescript
export type SymphonyTicker = (
  t: number,           // animation tick 0.0–1.0
  fg: RGB,             // foreground color
  bg: RGB,             // background color  
  n: number,           // numLEDs (ledPoints)
  speed: number        // 1–100
) => RGB[];

export const SYMPHONY_EFFECTS: Record<number, SymphonyTicker> = {
  1: (t, fg, bg, n) => { /* fireworks */ },
  2: (t, fg, bg, n) => { /* ocean */ },
  3: (t, fg, bg, n) => { /* aurora */ },
  // ... all 33
};
```

### The 33 Effects (from APK `ge.*` class list)
> ⚠️ Full class-to-effect mapping must be extracted from `jadx_src/` or the decompiled APK during implementation. Do NOT guess the mapping without source truth.

Priority order for porting:
1. Simple palette effects (solid color transitions) — days 1-2
2. Math wave effects (sine, cosine patterns) — days 2-3  
3. Complex particle effects (fireworks, rain, sparkle) — days 3-5

### Integration with ProductVisualizer

`ProductVisualizer.tsx` already calls `getVisualizerFrame()` from PatternEngine. Add a parallel path:
```typescript
// If in SYMPHONY mode, use SymphonyEffectSimulator instead of PatternEngine
if (mode === 'SYMPHONY' && SYMPHONY_EFFECTS[effectId]) {
  return SYMPHONY_EFFECTS[effectId](animTick, fgRGB, bgRGB, numLEDs, speed);
}
```

---

## Part 3: Direction & Section Toggle UI

From `m16236m()` and `m16234o()` in the APK — per-effect truth tables for:
- **Direction support**: can this effect reverse?
- **Section support**: can this effect apply to a sub-section of the strip?

Add these as boolean fields to `SymphonyEffects.ts`:
```typescript
export interface SymphonyEffectMeta {
  id: number;
  name: string;
  colorGate: 'FG_BG' | 'FG_ONLY' | 'NONE';
  supportsDirection: boolean;
  supportsSection: boolean;
}
```

Show/hide direction chip and section toggle in the UI based on these flags.

---

## Files To Create

| File | Purpose |
|------|---------|
| `src/constants/SymphonyEffects.ts` | Effect metadata, color gates, direction flags |
| `src/utils/SymphonyEffectSimulator.ts` | All 33 `ge.*` tick functions |

## Files To Modify

| File | Change |
|------|--------|
| `src/protocols/ZenggeProtocol.ts` | Add/fix `setSettledMode()` method |
| `src/components/ProductVisualizer.tsx` | Add symphony visualizer path |
| Symphony tab UI component (TBD) | Wire FG/BG pickers to color gate, add direction/section |

---

## Test Criteria

### Protocol
- [ ] Oracle Lab: `setSettledMode(1, red, blue, 50, 0)` sends correct 13-byte payload
- [ ] Hardware confirms effect ID 1 animates with red FG, blue BG
- [ ] Hardware confirms effect 7 responds to FG only, ignores BG bytes
- [ ] Hardware confirms effects with `NONE` gate animate without color bytes mattering

### Visualizer
- [ ] Each of the 33 `tick()` functions returns exactly `n` RGB values
- [ ] Visualizer animation updates smoothly at 60fps on device
- [ ] No `NaN` or out-of-bounds colors in output

### UI
- [ ] FG+BG pickers visible only for correct effect IDs
- [ ] Direction chip hidden for effects that don't support it
- [ ] Speed slider fires `setSettledMode` with updated speed in real time
