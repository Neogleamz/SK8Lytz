# PLAN: Phase 1A — Reverse-Engineer All 33 ge.* Effects as PatternEngine TypeScript

**Slug**: `feat/pattern-engine-phase1a-ge-reversal`
**Created**: 2026-04-22
**Status**: 🔲 Blocked on `audit/cross-ref-existing-28-vs-source`
**Risk**: L-RISK (additive — new pattern implementations only)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Core Architecture Principle

> **We are NOT calling `0x41`. We are NOT invoking the ge.* code.**
>
> The `ge.*` Java class files are RESEARCH MATERIAL ONLY.
> We read their math → rewrite in TypeScript → fire via `0x59`.
> Visualizer runs the same TypeScript. Skates = Visualizer. Always.

---

## What Each ge.* Implementation Requires

For each of the 33 effects, we produce:

### 1. `CustomEffects.ts` entry
```typescript
{
  id: 29,                           // next available ID after existing 28
  name: 'Ocean Wave',               // user-facing name
  group: 'SETTLED',                 // Tier 1: ge.* reversal
  colorMode: 'FG_BG',              // which color pickers to show
  supportsDirection: true,          // show direction toggle?
  tier: 1,                          // source tier
  sourceRef: 'ge.OceanWaveEffect', // Java class reversed
  icon: '🌊',
}
```

### 2. `PatternEngine.ts` `getVisualizerFrame()` case
```typescript
case 29: // Ocean Wave — reversed from ge.OceanWaveEffect
  return buildOceanWave(fg, bg, numLEDs, tick, direction);
```

### 3. The frame builder function (pure TypeScript math)
```typescript
function buildOceanWave(
  fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1
): RGB[] {
  // [math here — see per-effect section below]
}
```

### 4. `colorMode` gate (drives UI picker visibility)
- `'FG_BG'` — both FG and BG pickers shown (e.g. comet + background)
- `'FG_ONLY'` — only FG shown (e.g. breathing on single color)
- `'GENERATIVE'` — neither shown (e.g. aurora — colors are hue-computed internally)

---

## The 33 ge.* Effects — Full TypeScript Math

> **Note**: Verify each effect ID against `ZENGGE_PROTOCOL_BIBLE.md` before implementing.
> Effect IDs listed here are provisional. The cross-ref audit produces the final ID assignments.

---

### Effect 1: Color Flow / Gradient Cycle
**Source**: `ge.GradientCycleEffect` (or similar)
**colorMode**: `GENERATIVE` (hue is computed, not FG/BG)
**supportsDirection**: true

```typescript
function buildColorFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phaseOffset = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phaseOffset) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}
```

---

### Effect 2: Color Breathing
**Source**: `ge.BreathingEffect`
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildColorBreathing(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = (Math.sin(tick * Math.PI * 2) * 0.5 + 0.5);
  const color = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  return Array(numLEDs).fill(color);
}
```

---

### Effect 3: Color Jump / Flash
**Source**: `ge.ColorJumpEffect`
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildColorJump(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Hard alternation between fg and bg at tick boundary
  const color = tick < 0.5 ? fg : bg;
  return Array(numLEDs).fill(color);
}
```

---

### Effect 4: Running Water / Marquee Chase
**Source**: `ge.RunningWaterEffect`
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildRunningWater(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const BLOCK = 3; // pixels lit per period
  const offset = Math.floor((direction === 0 ? tick : 1 - tick) * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i + offset) % numLEDs;
    return pos < BLOCK ? fg : bg;
  });
}
```

---

### Effect 5: Strobe Flash
**Source**: `ge.StrobeEffect`
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const on = (tick * 10) % 1 < 0.3; // 30% duty cycle
  const color = on ? fg : { r: 0, g: 0, b: 0 };
  return Array(numLEDs).fill(color);
}
```

---

### Effect 6: Color Wipe
**Source**: `ge.ColorWipeEffect`
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildColorWipe(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const fillCount = Math.floor(t * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}
```

---

### Effect 7: Fireworks / Sparkle Burst
**Source**: `ge.FireworksEffect`
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildFireworks(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Use a seeded pseudo-random to get consistent per-tick sparks
  const seed = Math.floor(tick * 8); // 8 burst frames per cycle
  const frame = Array(numLEDs).fill(bg);
  // Burst origin: pseudo-random center based on seed
  const center = Math.abs(Math.sin(seed * 1.618) * numLEDs) | 0;
  const spreadRadius = Math.floor((tick * 8 % 1) * (numLEDs / 4));
  for (let i = 0; i < spreadRadius; i++) {
    const brightness = 1 - (i / spreadRadius);
    const c = blendRGB(fg, bg, brightness);
    const posL = Math.max(0, center - i);
    const posR = Math.min(numLEDs - 1, center + i);
    if (posL >= 0) frame[posL] = c;
    if (posR < numLEDs) frame[posR] = c;
  }
  return frame;
}
```

---

### Effect 8: Ocean Wave
**Source**: `ge.OceanWaveEffect`
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildOceanWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    // Two overlapping sine waves with phase offset
    const wave1 = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 2);
    const wave2 = Math.sin(i / numLEDs * Math.PI * 6 + phase * Math.PI * 3 + 0.7);
    const combined = (wave1 + wave2) / 2 * 0.5 + 0.5; // 0..1
    return blendRGB(fg, bg, combined);
  });
}
```

---

### Effect 9: Lightning Strike
**Source**: `ge.LightningEffect`
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildLightning(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // Fast random bright flashes on random pixels
  const flashSeed = Math.floor(tick * 20);
  const frame: RGB[] = Array(numLEDs).fill({ r: 0, g: 0, b: 0 });
  // 3 simultaneous random flash points
  for (let j = 0; j < 3; j++) {
    const pos = Math.abs(Math.sin((flashSeed + j) * 2.718) * numLEDs) | 0;
    const brightness = Math.abs(Math.cos((flashSeed + j) * 1.414));
    frame[pos] = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  }
  return frame;
}
```

---

### Effect 10: Snowfall
**Source**: `ge.SnowfallEffect`
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildSnowfall(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  // 5 independent snowflakes at different speeds/positions
  for (let j = 0; j < 5; j++) {
    const speed = 0.3 + j * 0.15;
    const pos = Math.floor(((tick * speed + j * 0.2) % 1) * numLEDs);
    const brightness = 0.6 + Math.sin(tick * 5 + j) * 0.4; // twinkle
    frame[pos] = blendRGB(fg, bg, brightness);
    if (pos + 1 < numLEDs) frame[pos + 1] = blendRGB(fg, bg, brightness * 0.3); // soft tail
  }
  return frame;
}
```

---

### Effect 11: Candle Flicker
**Source**: `ge.CandleEffect`
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildCandle(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // Warm flicker: brightness varies per-zone with noise approximation
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(tick * 7 + i * 1.3) * Math.cos(tick * 11 + i * 0.7);
    const brightness = 0.5 + noise * 0.3 + Math.sin(tick * 3) * 0.2;
    const b = Math.max(0, Math.min(1, brightness));
    return { r: Math.round(fg.r * b), g: Math.round(fg.g * b * 0.7), b: Math.round(fg.b * b * 0.3) };
  });
}
```

---

### Effect 12: Heartbeat Pulse
**Source**: `ge.HeartbeatEffect`
**colorMode**: `FG_ONLY`
**supportsDirection**: false

```typescript
function buildHeartbeat(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // Double-pulse EKG envelope: two quick peaks then long rest
  const t = tick % 1;
  let brightness: number;
  if (t < 0.1) brightness = Math.sin((t / 0.1) * Math.PI);        // first beat
  else if (t < 0.3) brightness = Math.sin(((t - 0.15) / 0.15) * Math.PI) * 0.6; // second beat
  else brightness = 0;                                              // rest
  const b = Math.max(0, brightness);
  return Array(numLEDs).fill({ r: Math.round(fg.r * b), g: Math.round(fg.g * b), b: Math.round(fg.b * b) });
}
```

---

### Effect 13: Meteor / Shooting Star
**Source**: `ge.MeteorEffect`
**colorMode**: `FG_BG`
**supportsDirection**: true

```typescript
function buildMeteor(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.25); // 25% of strip for tail
  const head = Math.floor((direction === 0 ? tick : 1 - tick) * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist > TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 2)); // quadratic decay
  });
}
```

---

### Effect 14: Aurora Borealis
**Source**: `ge.AuroraEffect`
**colorMode**: `GENERATIVE`
**supportsDirection**: true

```typescript
function buildAurora(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = (i / numLEDs * 0.4 + phase * 0.5 + 0.5) % 1; // cool hue range
    const brightness = (Math.sin(i / numLEDs * Math.PI * 3 + phase * Math.PI * 4) * 0.4 + 0.6);
    return hsvToRgb(hue, 0.8, brightness);
  });
}
```

---

### Effect 15: Lava Lamp
**Source**: `ge.LavaEffect`
**colorMode**: `FG_BG`
**supportsDirection**: false

```typescript
function buildLava(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    // Slow overlapping blobs using low-frequency sine interference
    const blob1 = Math.sin(i / numLEDs * Math.PI * 2 + tick * Math.PI * 1.3);
    const blob2 = Math.sin(i / numLEDs * Math.PI * 3.7 + tick * Math.PI * 0.8 + 1.2);
    const combined = (blob1 + blob2) * 0.5 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}
```

---

### Effects 16–33: Additional ge.* Effects
> **These need to be extracted from `tools/ZENGGE_PROTOCOL_BIBLE.md`** after the cross-ref audit documents all 33 class names and their visual behavior descriptions.

Likely remaining effects (verify against PROTOCOL_BIBLE):
- Plasma / Interference Wave
- Galaxies / Star Cluster  
- Police Lights (R/B alternating blocks)
- Rainbow Breathing
- Color Splash / Burst
- Twinkle / Random Sparkle
- Crystal Shimmer
- Gradient Chase (FG→BG gradient scrolling)
- Comet Duo (two comets from opposite ends)
- Color Storm
- Neon Pulse
- Cyber Glitch
- Fire Flame (dense center decay)
- Plus any remaining confirmed from PROTOCOL_BIBLE

**Implement each using the same framework above**: `buildXxx(fg, bg, numLEDs, tick, direction)` → add to `CustomEffects.ts` → add `case N:` to `PatternEngine.ts`.

---

## Helper Functions Required

```typescript
// In PatternEngine.ts or a new src/utils/colorMath.ts

function blendRGB(a: RGB, b: RGB, t: number): RGB {
  return {
    r: Math.round(a.r * t + b.r * (1 - t)),
    g: Math.round(a.g * t + b.g * (1 - t)),
    b: Math.round(a.b * t + b.b * (1 - t)),
  };
}

function hsvToRgb(h: number, s: number, v: number): RGB {
  const i = Math.floor(h * 6);
  const f = h * 6 - i;
  const p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
  const [r, g, b] = [
    [v, q, p, p, t, v], [t, v, v, q, p, p], [p, p, t, v, v, q]
  ].map(ch => ch[i % 6]);
  return { r: Math.round(r * 255), g: Math.round(g * 255), b: Math.round(b * 255) };
}
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/constants/CustomEffects.ts` | Add 33 new entries to `SK8LYTZ_TEMPLATES` array |
| `src/protocols/PatternEngine.ts` | Add case for each new pattern ID → frame builder function |
| `src/utils/colorMath.ts` | [NEW] Shared math helpers: `blendRGB`, `hsvToRgb`, `hexToRgb` |

---

## Test Criteria

- [ ] All 33 ge.* effects visible in pattern picker
- [ ] Each fires a `0x59` array to hardware — confirmed via Oracle Lab byte log
- [ ] ProductVisualizer shows the effect animating — matches hardware
- [ ] `colorMode` correct — FG_BG picker shown for effects that need it, hidden for GENERATIVE
- [ ] `supportsDirection` correct — direction toggle visible/hidden per effect
- [ ] Hardware on HALOZ: visually compare each effect against the description from PROTOCOL_BIBLE
- [ ] `npx tsc --noEmit` — zero errors
