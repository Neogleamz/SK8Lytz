# PLAN: PatternEngine Expansion — Groups 7, 8 & 9 (17 New Patterns)

**Slug**: `feat/pattern-engine-expansion-group7/8/9`
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: L-RISK (additive only — no changes to existing 28 patterns)
**Prerequisite**: `refactor/retire-rbm-simulator` complete (Phase 0 cleanup)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Architecture

All 17 new patterns slot into the existing `PatternEngine.ts` framework:
- `generateArray(patternId, fg, bg, n)` → add cases 29–45
- All use `NATIVE SCROLL` transition type (`0x03` = RunningWater) unless noted
- All registered in `CustomEffects.ts` with correct `requiresForeground/requiresBackground/supportsDirection` flags
- `getVisualizerFrame()` handles temporal simulation via existing `rotateArray()` — no new animation pipeline needed

**Pattern ID assignment**: Continue from 28 → IDs 29–45.

---

## Group 7 — Nature & Organic (IDs 29–34)

### 29: Ocean Wave
**Concept**: Two overlapping sine waves with different phase offsets and amplitudes — creates organic turbulence between FG and BG.
```typescript
case 29: // Ocean Wave
  return arr.map((_, i) => {
    const wave1 = (Math.sin((i / n) * Math.PI * 3) + 1) / 2;
    const wave2 = (Math.sin((i / n) * Math.PI * 5 + 1.2) + 1) / 2;
    return lerpRGB(bg, fg, (wave1 + wave2) / 2);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 30: Dragon Breath
**Concept**: Intense hot core at position 0, decaying exponentially outward. Tail fades to BG. On hardware scroll it sweeps like a flame breath.
```typescript
case 30: // Dragon Breath
  return arr.map((_, i) => {
    const decay = Math.exp(-i * (6 / n));
    return lerpRGB(bg, fg, decay);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 31: Lava Flow
**Concept**: Irregular clumps of FG color on BG, modeled via noise approximation (sine harmonics).
```typescript
case 31: // Lava Flow
  return arr.map((_, i) => {
    const noise = (Math.sin(i * 0.7) + Math.sin(i * 1.3) + Math.sin(i * 0.4)) / 3;
    const t = (noise + 1) / 2;
    return lerpRGB(bg, fg, t > 0.55 ? 1 : 0); // Hard threshold for "clump" look
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 32: Heartbeat
**Concept**: Two quick bright pulses (systole/diastole) separated by a long dim gap. EKG signature.
```typescript
case 32: // Heartbeat — generates one beat cycle across strip
  return arr.map((_, i) => {
    const pos = i / n;
    const pulse1 = Math.exp(-Math.pow((pos - 0.2) * 15, 2));
    const pulse2 = Math.exp(-Math.pow((pos - 0.35) * 20, 2)) * 0.6;
    const t = Math.min(1, pulse1 + pulse2);
    return lerpRGB(bg, fg, t);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: false` (heartbeat doesn't reverse)

### 33: Aurora Borealis
**Concept**: Slow gradient cycling through cool hues (cyan→purple→green→blue). No FG/BG — generative hue sweep.
```typescript
case 33: // Aurora Borealis
  return arr.map((_, i) => {
    // Hue sweeps from 160° (cyan) to 300° (purple) across strip
    const hue = 160 + (i / n) * 140;
    const rgb = hueToRGB(hue);
    return dim(rgb, 0.7 + (Math.sin(i * 0.8) + 1) * 0.15); // subtle shimmer
  });
```
**Flags**: `requiresForeground: false, requiresBackground: false, supportsDirection: true`

### 34: Flame
**Concept**: Dense FG at center, decaying outward to BG at both ends. Classic candle shape.
```typescript
case 34: // Flame
  return arr.map((_, i) => {
    const distFromCenter = Math.abs(i - n / 2) / (n / 2);
    const flicker = Math.pow(1 - distFromCenter, 2.5);
    return lerpRGB(bg, fg, flicker);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: false`

---

## Group 8 — Tech & Energy (IDs 35–40)

### 35: Police Lights
**Concept**: Bold alternating RED/BLUE blocks of 4 LEDs. Use FG=red, BG=blue for authentic police light. Hard cascade scroll makes it strobe-chase.
```typescript
case 35: // Police Lights
  return arr.map((_, i) => ((Math.floor(i / 4) % 2 === 0) ? fg : bg));
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 36: Laser Beam
**Concept**: Single sharp bright dot on pure black. Tighter than Single Dot Chase — 1px wide, hard edge, no dim trail.
```typescript
case 36: // Laser Beam
  arr[0] = fg; // Single pixel, hardware scroll moves it
  return arr;   // Same as id:6 but BG forced black in UI convention
```
**Flags**: `requiresForeground: true, requiresBackground: false, supportsDirection: true`
> Note: UI should lock BG to black for this pattern.

### 37: Pixel Glitch
**Concept**: Semi-random digital corruption. Every ~5 LEDs fires a random intensity flash, rest is BG. Appears glitchy/corrupted on scroll.
```typescript
case 37: // Pixel Glitch
  return arr.map((_, i) => {
    const seed = (Math.sin(i * 127.1 + 311.7) * 43758.5) % 1; // deterministic pseudo-random
    return seed > 0.8 ? dim(fg, seed) : bg;
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 38: Crystal Prism
**Concept**: Micro-rainbow segments of 3 LEDs each cycling hue. More compact than Rainbow Marquee.
```typescript
case 38: // Crystal Prism
  return arr.map((_, i) => hueToRGB(((Math.floor(i / 3)) / Math.ceil(n / 3)) * 360));
```
**Flags**: `requiresForeground: false, requiresBackground: false, supportsDirection: true`

### 39: Neon Sign
**Concept**: Solid FG with evenly spaced single-LED BG gaps every 6 pixels. Looks like a neon tube sign grid.
```typescript
case 39: // Neon Sign
  return arr.map((_, i) => (i % 6 === 0 ? bg : fg));
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 40: Binary Code
**Concept**: FG and BG alternating in a binary-sequence pattern (1100100110...) simulating data bits.
```typescript
case 40: // Binary Code
  const bits = [1,1,0,0,1,0,0,1,1,0]; // Pattern sequence
  return arr.map((_, i) => (bits[i % bits.length] ? fg : bg));
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

---

## Group 9 — Motion & Sport (IDs 41–45)

### 41: Speed Lines
**Concept**: Long FG tail (8 LEDs) with aggressive exponential fade to BG. Like motion blur on a fast skater.
```typescript
case 41: // Speed Lines
  return arr.map((_, i) => {
    const tailLen = Math.min(8, Math.floor(n * 0.5));
    if (i >= tailLen) return bg;
    return dim(fg, 1 - (i / tailLen) * 0.9);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 42: Pulse Ring
**Concept**: Two bright heads start at opposite ends and converge to the center, then reset. Generates via inward-facing gradient peaks.
```typescript
case 42: // Pulse Ring
  return arr.map((_, i) => {
    const mid = n / 2;
    const dist = Math.abs(i - mid) / mid; // 0 at center, 1 at ends
    const pulse = Math.exp(-Math.pow(dist * 4 - 3, 2)); // peaks near ends
    return lerpRGB(bg, fg, pulse);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: false`

### 43: Double Comet
**Concept**: Two comet trails traveling in opposite directions simultaneously. FG for one, dim(FG) for the other.
```typescript
case 43: // Double Comet — two comets spaced n/2 apart
  const comet1Start = 0;
  const comet2Start = Math.floor(n / 2);
  const cometLen = Math.min(5, Math.floor(n * 0.3));
  return arr.map((_, i) => {
    const d1 = i - comet1Start;
    const d2 = i - comet2Start;
    if (d1 >= 0 && d1 < cometLen) return dim(fg, 1 - d1 / cometLen);
    if (d2 >= 0 && d2 < cometLen) return dim(fg, 0.7 - (d2 / cometLen) * 0.7);
    return bg;
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

### 44: Strobe Chase
**Concept**: A moving dot that alternates ON/OFF at high frequency as it scrolls. Hardware cascade + strobe transition type combined.
```typescript
case 44: // Strobe Chase — single dot on dark BG, hardware strobes it via transition
  arr[0] = fg;
  return arr;
```
> **Special**: This pattern uses `transitionType = 0x02` (STROBE) instead of the normal `0x03` RunningWater. Update `getPatternTransitionType()`:
```typescript
case 44: return 0x02; // STROBE
```
**Flags**: `requiresForeground: true, requiresBackground: false, supportsDirection: true`

### 45: DNA Helix
**Concept**: Two interleaved scrolling sequences offset by n/2, each a short FG burst. Looks like a double helix when scrolling.
```typescript
case 45: // DNA Helix
  return arr.map((_, i) => {
    const wave1 = Math.sin((i / n) * Math.PI * 4);
    const wave2 = Math.sin((i / n) * Math.PI * 4 + Math.PI); // Offset by π
    const t = Math.max(wave1, wave2);
    return lerpRGB(bg, fg, t > 0.7 ? 1 : 0);
  });
```
**Flags**: `requiresForeground: true, requiresBackground: true, supportsDirection: true`

---

## CustomEffects.ts Additions

Append to `SK8LYTZ_TEMPLATES` array:

```typescript
// ── GROUP 7: NATURE & ORGANIC (0x59 Cascade) ──
{ id: 29, name: "Ocean Wave", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 30, name: "Dragon Breath", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 31, name: "Lava Flow", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 32, name: "Heartbeat", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
{ id: 33, name: "Aurora Borealis", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
{ id: 34, name: "Flame", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },

// ── GROUP 8: TECH & ENERGY (0x59 Cascade) ──
{ id: 35, name: "Police Lights", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 36, name: "Laser Beam", requiresForeground: true, requiresBackground: false, supportsSegment: true, supportsDirection: true },
{ id: 37, name: "Pixel Glitch", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 38, name: "Crystal Prism", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
{ id: 39, name: "Neon Sign", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 40, name: "Binary Code", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },

// ── GROUP 9: MOTION & SPORT (0x59 Cascade / STROBE) ──
{ id: 41, name: "Speed Lines", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 42, name: "Pulse Ring", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
{ id: 43, name: "Double Comet", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
{ id: 44, name: "Strobe Chase", requiresForeground: true, requiresBackground: false, supportsSegment: true, supportsDirection: true },
{ id: 45, name: "DNA Helix", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
```

---

## `getPatternTransitionType()` Update

```typescript
// Add to PatternEngine.ts
case 44: return 0x02; // STROBE — Strobe Chase uses hardware strobe transition
// All others (29-43, 45) use existing default 0x03 SCROLL
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/protocols/PatternEngine.ts` | Add cases 29–45 in `generateArray()`, update `getPatternTransitionType()` |
| `src/constants/CustomEffects.ts` | Append 17 metadata entries |

---

## Test Criteria

- [ ] `PatternId = 1` through `45` all produce non-null arrays from `generateArray()`
- [ ] All new visualizer frames animate correctly in `ProductVisualizer.tsx`
- [ ] Pattern 44 (Strobe Chase) fires with `transitionType = 0x02` confirmed in BLE sniffer
- [ ] Pattern 32 (Heartbeat) and 34 (Flame) `supportsDirection: false` → no direction chip shown in UI
- [ ] Pattern 33 and 38 `requiresForeground: false` → FG picker hidden in UI correctly
- [ ] Hardware: verify at least 2 patterns from each group (6 physical tests minimum) on HALOZ
- [ ] TypeScript: zero `any` types, no `tsc --noEmit` errors
