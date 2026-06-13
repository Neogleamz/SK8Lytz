# PLAN: Phase 1B — Reverse-Engineer All Programs Mode Effects as PatternEngine TypeScript

**Slug**: `feat/pattern-engine-phase1b-programs-reversal`
**Created**: 2026-04-22
**Status**: 🔲 Blocked on `audit/cross-ref-existing-28-vs-source`
**Risk**: L-RISK (additive — new pattern implementations only)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Core Architecture Principle

> **We are NOT calling `0x42`. We are NOT using the baked-in Programs hardware effects.**
>
> Every effect below is a TypeScript `getVisualizerFrame()` function that generates
> a pixel array, fired via `0x59`. ProductVisualizer runs the same math.
> Visualizer = Skates. Always.

---

## Universal Controls Per Pattern

Every pattern supports:
- **FG color** (`fg: RGB`) — always passed in
- **BG color** (`bg: RGB`) — always passed in, ignored if `colorMode !== 'FG_BG'`
- **Speed** (`speed: number, 1–100`) — controls `tick` advancement rate + `0x59` scroll param
- **Brightness** (`0x55` packet) — global, independent of pattern
- **Direction** (`direction: 0 | 1`) — passed to `getVisualizerFrame()`, only shown in UI if `supportsDirection: true`

---

## Group A: Static Placement

These produce a **frozen pixel array** (no tick-based animation). They use `0x01` FREEZE transition in `0x59`.

---

### Programs-1: Solid & Static
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildSolid(fg: RGB, numLEDs: number): RGB[] {
  return Array(numLEDs).fill(fg);
}
```

---

### Programs-2: Split Colors
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildSplitColors(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const half = Math.floor(numLEDs / 2);
  return [
    ...Array(half).fill(fg),
    ...Array(numLEDs - half).fill(bg),
  ];
}
```

---

### Programs-3: Trisection
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildTrisection(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const third = Math.floor(numLEDs / 3);
  return [
    ...Array(third).fill(fg),
    ...Array(third).fill(bg),
    ...Array(numLEDs - third * 2).fill(fg), // remainder goes to last third
  ];
}
```

---

### Programs-4: Quartered
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildQuartered(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const quarter = Math.floor(numLEDs / 4);
  return [
    ...Array(quarter).fill(fg),
    ...Array(quarter).fill(bg),
    ...Array(quarter).fill(fg),
    ...Array(numLEDs - quarter * 3).fill(bg),
  ];
}
```

---

### Programs-5: Center Accent
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildCenterAccent(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const accentSize = Math.max(1, Math.floor(numLEDs * 0.25));
  const sideSize = Math.floor((numLEDs - accentSize) / 2);
  return [
    ...Array(sideSize).fill(bg),
    ...Array(accentSize).fill(fg),
    ...Array(numLEDs - sideSize - accentSize).fill(bg),
  ];
}
```

---

## Group B: Chases & Meteors

Scrolling patterns — use `0x03` RunningWater scroll in `0x59`.

---

### Programs-6: Single Dot Chase
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildSingleDotChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const pos = Math.floor(t * numLEDs) % numLEDs;
  return Array.from({ length: numLEDs }, (_, i) => i === pos ? fg : bg);
}
```

---

### Programs-7: Reflected Dot Chase
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildReflectedDotChase(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Two dots start at opposite ends and meet in the middle
  const posA = Math.floor(tick * (numLEDs / 2)) % Math.ceil(numLEDs / 2);
  const posB = numLEDs - 1 - posA;
  return Array.from({ length: numLEDs }, (_, i) =>
    (i === posA || i === posB) ? fg : bg
  );
}
```

---

### Programs-8: Comet Chase
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildCometChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.max(3, Math.floor(numLEDs * 0.2));
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 1.5));
  });
}
```

---

### Programs-9: Meteor Shower
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildMeteorShower(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const METEOR_COUNT = 3;
  const TAIL = Math.floor(numLEDs * 0.15);
  const frame = Array(numLEDs).fill(bg);
  for (let m = 0; m < METEOR_COUNT; m++) {
    const offset = (m / METEOR_COUNT); // stagger start
    const t = direction === 0 ? (tick + offset) % 1 : 1 - ((tick + offset) % 1);
    const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
    for (let i = 0; i < numLEDs; i++) {
      const dist = head - i;
      if (dist >= 0 && dist < TAIL) {
        const brightness = 1 - (dist / TAIL);
        frame[i] = blendRGB(fg, frame[i], Math.pow(brightness, 2));
      }
    }
  }
  return frame;
}
```

---

## Group C: Marquees & Bands

---

### Programs-10: Micro Ants
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildMicroAnts(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * 2) % 2; // alternates every half-tick
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % 2 === 0 ? fg : bg);
}
```

---

### Programs-11: Theater Chase
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildTheaterChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // every 3rd LED lights
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD === 0 ? fg : bg);
}
```

---

### Programs-12: Dashed Marquee
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildDashedMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const DASH = 4; const GAP = 3; // 4 on, 3 off
  const PERIOD = DASH + GAP;
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD < DASH ? fg : bg);
}
```

---

### Programs-13: Barber Pole
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildBarberPole(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(2, Math.floor(numLEDs / 6));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2);
  return Array.from({ length: numLEDs }, (_, i) => {
    // Diagonal stripe: position + offset mod period
    return (i + offset) % (STRIPE * 2) < STRIPE ? fg : bg;
  });
}
```

---

### Programs-14: Bold Stripes
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildBoldStripes(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const STRIPE = Math.max(3, Math.floor(numLEDs / 5));
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.floor(i / STRIPE) % 2 === 0 ? fg : bg
  );
}
```

---

## Group D: Math Waves

---

### Programs-15: Sine Pulse Wave
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildSinePulseWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 4 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}
```

---

### Programs-16: Wave Pinch
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildWavePinch(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    // Two waves moving from ends toward center
    const posNorm = i / (numLEDs - 1);
    const waveL = Math.sin(posNorm * Math.PI * 3 + tick * Math.PI * 2);
    const waveR = Math.sin((1 - posNorm) * Math.PI * 3 + tick * Math.PI * 2);
    const combined = (waveL + waveR) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}
```

---

### Programs-17: Breathing Wave
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildBreathingWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    // Sine wave traveling across strip
    const brightness = Math.sin((i / numLEDs) * Math.PI * 2 - phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, brightness);
  });
}
```

---

### Programs-18: Center-Out Comet
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildCenterOutComet(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const TAIL = Math.floor(numLEDs * 0.15);
  const head = Math.floor(tick * (center + TAIL));
  return Array.from({ length: numLEDs }, (_, i) => {
    // Mirror: two comets going from center to edges
    const distL = head - (center - i);
    const distR = head - (i - center);
    const dist = Math.min(Math.abs(distL), Math.abs(distR));
    if (i < center) {
      return (head - (center - i) >= 0 && head - (center - i) < TAIL)
        ? blendRGB(fg, bg, 1 - (head - (center - i)) / TAIL) : bg;
    } else {
      return (head - (i - center) >= 0 && head - (i - center) < TAIL)
        ? blendRGB(fg, bg, 1 - (head - (i - center)) / TAIL) : bg;
    }
  });
}
```

---

### Programs-19: Center-Out Marquee
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildCenterOutMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const PERIOD = 6;
  const offset = Math.floor(tick * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    const distFromCenter = Math.abs(i - center);
    return (distFromCenter + offset) % PERIOD < 3 ? fg : bg;
  });
}
```

---

## Group E: Temporal

---

### Programs-20: Smooth Breath
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildSmoothBreath(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = Math.sin(tick * Math.PI) ** 2; // smooth power curve
  return Array(numLEDs).fill({
    r: Math.round(fg.r * brightness),
    g: Math.round(fg.g * brightness),
    b: Math.round(fg.b * brightness),
  });
}
```

---

### Programs-21: Hard Jump Flash
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildHardJumpFlash(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Hard binary alternation — no crossfade
  return Array(numLEDs).fill(tick < 0.5 ? fg : bg);
}
```

---

### Programs-22: Strobe
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // High-frequency flash (10x per cycle)
  const on = (tick * 10) % 1 < 0.2; // 20% duty cycle = sharp strobe
  return Array(numLEDs).fill(on ? fg : { r: 0, g: 0, b: 0 });
}
```

---

### Programs-23: Wipe Fill
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildWipeFill(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  // Fill from one end, then wipe back — forward half fills, back half wipes
  const progress = t < 0.5 ? t * 2 : (1 - t) * 2;
  const fillCount = Math.floor(progress * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}
```

---

### Programs-24: Wipe Center-Out
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildWipeCenterOut(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const radius = Math.floor(tick * center);
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.abs(i - center) <= radius ? fg : bg
  );
}
```

---

## Group F: Generative / Rainbow

---

### Programs-25: True Rainbow Flow
**colorMode**: `GENERATIVE`
**supportsDirection**: true

```typescript
function buildTrueRainbowFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phase) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}
```

---

### Programs-26: Rainbow Marquee
**colorMode**: `GENERATIVE`
**supportsDirection**: true

```typescript
function buildRainbowMarquee(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // grouped blocks with rainbow hue
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    if ((i + offset) % PERIOD !== 0) return { r: 0, g: 0, b: 0 }; // gap
    const hue = (i / numLEDs + t) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}
```

---

### Programs-27: Rainbow Comet
**colorMode**: `GENERATIVE`
**supportsDirection**: true

```typescript
function buildRainbowComet(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.3);
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return { r: 0, g: 0, b: 0 };
    const brightness = 1 - (dist / TAIL);
    const hue = (i / numLEDs + t) % 1.0; // rainbow position along strip
    return hsvToRgb(hue, 1.0, brightness);
  });
}
```

---

### Programs-28: Cyberpunk Shift
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildCyberpunkShift(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / numLEDs;
    // Sharp digital glitch band scrolling
    const band = (posNorm + phase * 2) % 1;
    if (band < 0.08) return fg; // bright FG slice
    if (band < 0.12) return { r: 255, g: 255, b: 255 }; // white edge flash
    // Sub-bands of bg with slight brightness modulation
    const bgBrightness = 0.3 + Math.sin(posNorm * Math.PI * 8 + phase * Math.PI * 4) * 0.2;
    return blendRGB(bg, { r: 0, g: 0, b: 0 }, bgBrightness);
  });
}
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/constants/CustomEffects.ts` | Add all 28 new entries after ge.* IDs |
| `src/protocols/PatternEngine.ts` | Add case for each new pattern ID |
| `src/utils/colorMath.ts` | Shared: `blendRGB`, `hsvToRgb` (create one file used by both 1A and 1B) |

---

## Test Criteria

- [ ] All 28 Programs effects visible in pattern picker
- [ ] Static patterns (Group A): send `0x01` FREEZE, hardware holds color
- [ ] Scroll patterns (Groups B–F): hardware scrolls at correct speed
- [ ] `colorMode` correct — FG/BG pickers show/hide correctly per pattern
- [ ] `supportsDirection`: direction toggle visible for applicable patterns
- [ ] HALOZ hardware visual confirmation: each pattern looks correct
- [ ] `npx tsc --noEmit` — zero errors
