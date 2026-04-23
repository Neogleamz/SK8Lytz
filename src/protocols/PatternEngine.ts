/**
 * SK8Lytz Pattern Engine (Math Synthesizer)
 *
 * SINGLE SOURCE OF TRUTH for the 28 SK8LYTZ_TEMPLATES.
 * Synthesizes dynamic pixel arrays based on math parameters instead of legacy firmware IDs.
 *
 * Hardware animation model:
 *   - The IC strip controller animates NATIVELY once a 0x59 or 0x51 packet is sent.
 *   - 0x59 transitionType=0x00: Hardware holds array static (Group 1)
 *   - 0x59 transitionType=0x03: Hardware continuously scrolls the array (Groups 2–4, 5b, 6)
 *   - 0x51 NATIVE: Used for full-strip temporal fades/flashes (Group 5a: IDs 20–22)
 *
 * @wiring-audit Verified 2026-04-23 [BATCH:P0] — all 28 IDs accounted for:
 *
 *   Group 1 — Static & Solid     (IDs  1– 5): generateArray → 0x59 type=0x00
 *   Group 2 — Chases & Meteors   (IDs  6– 9): generateArray → 0x59 type=0x03
 *   Group 3 — Marquees & Bands   (IDs 10–14): generateArray → 0x59 type=0x03
 *   Group 4 — Math Waves         (IDs 15–19): generateArray → 0x59 type=0x03 (18/19 center-split)
 *   Group 5 — Temporal           (IDs 20–22): getVisualizerFrame simulates → 0x51 (buildCustomModePayload)
 *   Group 5b— Wipe/Fill          (IDs 23–24): generateArray → 0x59 type=0x03 (center-split scroll)
 *   Group 6 — Generative Rainbow (IDs 25–28): generateArray → 0x59 type=0x03
 *
 *   Dispatch entry points:
 *     Hardware: buildPatternPayload() → buildMultiColorPayload() | buildCustomModePayload()
 *     Visualizer: getVisualizerFrame() (ProductVisualizer.tsx + CustomEffectVisualizer.tsx)
 *
 *   Known gaps (logged as tech debt for BATCH:P1):
 *     TODO[BATCH:P1]: ProductVisualizer PROGRAMS branch (line ~343) passes hardcoded red fg/bg
 *       to getVisualizerFrame — user-selected colors are ignored in that branch.
 *     TODO[BATCH:P1]: getPatternTransitionType returns only 0x00 or 0x03. Groups 3/4 may
 *       benefit from 0x01 (Gradual) for smoother hardware animation.
 */

import { ZenggeProtocol } from './ZenggeProtocol';

export interface RGB {
  r: number;
  g: number;
  b: number;
}

export type PatternId = number; // 1 through 28

// ─── MATH HELPERS ─────────────────────────────────────────────────────────────

function dim(c: RGB, factor: number): RGB {
  return {
    r: Math.round(c.r * factor),
    g: Math.round(c.g * factor),
    b: Math.round(c.b * factor),
  };
}

function lerpRGB(c1: RGB, c2: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(c1.r + (c2.r - c1.r) * t),
    g: Math.round(c1.g + (c2.g - c1.g) * t),
    b: Math.round(c1.b + (c2.b - c1.b) * t),
  };
}

function hueToRGB(hue: number): RGB {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return { r: Math.round(f(5) * 255), g: Math.round(f(3) * 255), b: Math.round(f(1) * 255) };
}

function blendRGB(a: RGB, b: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
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

// ─── GE.* PHASE 1A BUILDERS ───────────────────────────────────────────────────

function buildColorFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phaseOffset = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phaseOffset) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}

function buildColorBreathing(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = (Math.sin(tick * Math.PI * 2) * 0.5 + 0.5);
  const color = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  return Array(numLEDs).fill(color);
}

function buildColorJump(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const color = tick < 0.5 ? fg : bg;
  return Array(numLEDs).fill(color);
}

function buildRunningWater(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const BLOCK = 3;
  const offset = Math.floor((direction === 0 ? tick : 1 - tick) * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i + offset) % numLEDs;
    return pos < BLOCK ? fg : bg;
  });
}

function buildStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const on = (tick * 10) % 1 < 0.3;
  const color = on ? fg : { r: 0, g: 0, b: 0 };
  return Array(numLEDs).fill(color);
}

function buildColorWipe(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const fillCount = Math.floor(t * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}

function buildFireworks(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const seed = Math.floor(tick * 8);
  const frame = Array(numLEDs).fill(bg);
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

function buildOceanWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave1 = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 2);
    const wave2 = Math.sin(i / numLEDs * Math.PI * 6 + phase * Math.PI * 3 + 0.7);
    const combined = (wave1 + wave2) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}

function buildLightning(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const flashSeed = Math.floor(tick * 20);
  const frame: RGB[] = Array(numLEDs).fill({ r: 0, g: 0, b: 0 });
  for (let j = 0; j < 3; j++) {
    const pos = Math.abs(Math.sin((flashSeed + j) * 2.718) * numLEDs) | 0;
    const brightness = Math.abs(Math.cos((flashSeed + j) * 1.414));
    frame[pos] = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  }
  return frame;
}

function buildSnowfall(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let j = 0; j < 5; j++) {
    const speed = 0.3 + j * 0.15;
    const pos = Math.floor(((tick * speed + j * 0.2) % 1) * numLEDs);
    const brightness = 0.6 + Math.sin(tick * 5 + j) * 0.4;
    frame[pos] = blendRGB(fg, bg, brightness);
    if (pos + 1 < numLEDs) frame[pos + 1] = blendRGB(fg, bg, brightness * 0.3);
  }
  return frame;
}

function buildCandle(fg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(tick * 7 + i * 1.3) * Math.cos(tick * 11 + i * 0.7);
    const brightness = 0.5 + noise * 0.3 + Math.sin(tick * 3) * 0.2;
    const b = Math.max(0, Math.min(1, brightness));
    return { r: Math.round(fg.r * b), g: Math.round(fg.g * b * 0.7), b: Math.round(fg.b * b * 0.3) };
  });
}

function buildHeartbeat(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const t = tick % 1;
  let brightness: number;
  if (t < 0.1) brightness = Math.sin((t / 0.1) * Math.PI);
  else if (t < 0.3) brightness = Math.sin(((t - 0.15) / 0.15) * Math.PI) * 0.6;
  else brightness = 0;
  const b = Math.max(0, brightness);
  return Array(numLEDs).fill({ r: Math.round(fg.r * b), g: Math.round(fg.g * b), b: Math.round(fg.b * b) });
}

function buildMeteor(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.25);
  const head = Math.floor((direction === 0 ? tick : 1 - tick) * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist > TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 2));
  });
}

function buildAurora(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = (i / numLEDs * 0.4 + phase * 0.5 + 0.5) % 1;
    const brightness = (Math.sin(i / numLEDs * Math.PI * 3 + phase * Math.PI * 4) * 0.4 + 0.6);
    return hsvToRgb(hue, 0.8, brightness);
  });
}

function buildLava(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const blob1 = Math.sin(i / numLEDs * Math.PI * 2 + tick * Math.PI * 1.3);
    const blob2 = Math.sin(i / numLEDs * Math.PI * 3.7 + tick * Math.PI * 0.8 + 1.2);
    const combined = (blob1 + blob2) * 0.5 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}

function buildPlasma(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const v = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 6) * 
              Math.cos(i / numLEDs * Math.PI * 2 - phase * Math.PI * 2);
    return blendRGB(fg, bg, Math.abs(v));
  });
}

function buildStarCluster(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let j = 0; j < 4; j++) {
    const center = Math.floor(((tick * 0.5 + j * 0.25) % 1) * numLEDs);
    for (let i = -2; i <= 2; i++) {
      const pos = center + i;
      if (pos >= 0 && pos < numLEDs) {
        const brightness = 1 - Math.abs(i) / 3;
        frame[pos] = blendRGB(fg, frame[pos], brightness);
      }
    }
  }
  return frame;
}

function buildPoliceLights(numLEDs: number, tick: number): RGB[] {
  const red = { r: 255, g: 0, b: 0 };
  const blue = { r: 0, g: 0, b: 255 };
  const flashPhase = (tick * 8) % 1;
  const isOn = flashPhase < 0.5;
  const isRedTurn = (tick * 2) % 1 < 0.5;
  
  return Array.from({ length: numLEDs }, (_, i) => {
    if (!isOn) return { r: 0, g: 0, b: 0 };
    const isLeftHalf = i < numLEDs / 2;
    if (isRedTurn) return isLeftHalf ? red : { r: 0, g: 0, b: 0 };
    return !isLeftHalf ? blue : { r: 0, g: 0, b: 0 };
  });
}

function buildRainbowBreathing(numLEDs: number, tick: number): RGB[] {
  const hue = tick % 1;
  const brightness = (Math.sin(tick * Math.PI * 4) * 0.5 + 0.5);
  const baseColor = hsvToRgb(hue, 1.0, 1.0);
  return Array(numLEDs).fill({
    r: Math.round(baseColor.r * brightness),
    g: Math.round(baseColor.g * brightness),
    b: Math.round(baseColor.b * brightness)
  });
}

function buildColorBurst(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const burstPhase = tick % 1;
  const center = Math.floor(numLEDs / 2);
  const radius = Math.floor(burstPhase * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = Math.abs(i - center);
    if (dist < radius && dist > radius - 3) return fg;
    return bg;
  });
}

function buildTwinkle(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let i = 0; i < numLEDs; i++) {
    const noise = Math.sin(i * 12.9898 + tick * 10) * 43758.5453;
    const val = noise - Math.floor(noise);
    if (val > 0.95) {
       const brightness = Math.sin(tick * 20 + i) * 0.5 + 0.5;
       frame[i] = blendRGB(fg, bg, brightness);
    }
  }
  return frame;
}

function buildCrystalShimmer(numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(i * 7.1 + tick * 15) * 0.5 + 0.5;
    const brightness = Math.pow(noise, 3);
    return {
      r: Math.round(150 * brightness),
      g: Math.round(200 * brightness),
      b: Math.round(255 * brightness)
    };
  });
}

function buildGradientChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i / numLEDs + phase) % 1;
    return blendRGB(fg, bg, Math.sin(pos * Math.PI));
  });
}

function buildCometDuo(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.2);
  const head1 = Math.floor(tick * (numLEDs + TAIL)) - TAIL;
  const head2 = Math.floor((1 - tick) * (numLEDs + TAIL)) - TAIL;
  
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist1 = head1 - i;
    const dist2 = i - head2;
    
    let c = bg;
    if (dist1 >= 0 && dist1 <= TAIL) {
      c = blendRGB(fg, c, 1 - (dist1 / TAIL));
    }
    if (dist2 >= 0 && dist2 <= TAIL) {
      c = blendRGB(fg, c, 1 - (dist2 / TAIL)); 
    }
    return c;
  });
}

function generatePhase1aArray(patternId: PatternId, fg: RGB, bg: RGB, n: number, tick: number): RGB[] | null {
  const direction = 1; // Default to 1 (forward) if not dynamically provided by visualizer caller yet
  switch (patternId) {
    case 29: return buildColorFlow(n, tick, direction);
    case 30: return buildColorBreathing(fg, n, tick);
    case 31: return buildColorJump(fg, bg, n, tick);
    case 32: return buildRunningWater(fg, bg, n, tick, direction);
    case 33: return buildStrobe(fg, n, tick);
    case 34: return buildColorWipe(fg, bg, n, tick, direction);
    case 35: return buildFireworks(fg, bg, n, tick);
    case 36: return buildOceanWave(fg, bg, n, tick, direction);
    case 37: return buildLightning(fg, n, tick);
    case 38: return buildSnowfall(fg, bg, n, tick);
    case 39: return buildCandle(fg, n, tick);
    case 40: return buildHeartbeat(fg, n, tick);
    case 41: return buildMeteor(fg, bg, n, tick, direction);
    case 42: return buildAurora(n, tick, direction);
    case 43: return buildLava(fg, bg, n, tick);
    case 44: return buildPlasma(fg, bg, n, tick, direction);
    case 45: return buildStarCluster(fg, bg, n, tick);
    case 46: return buildPoliceLights(n, tick);
    case 47: return buildRainbowBreathing(n, tick);
    case 48: return buildColorBurst(fg, bg, n, tick);
    case 49: return buildTwinkle(fg, bg, n, tick);
    case 50: return buildCrystalShimmer(n, tick);
    case 51: return buildGradientChase(fg, bg, n, tick, direction);
    case 52: return buildCometDuo(fg, bg, n, tick);
    default: return null;
  }
}


// ─── GENERATORS ───────────────────────────────────────────────────────────────

function generateArray(patternId: PatternId, fg: RGB, bg: RGB, n: number): RGB[] {
  const arr: RGB[] = Array(n).fill(bg);

  switch (patternId) {
    // ── GROUP 1: SOLID & STATIC ──
    case 1: // Solid
      return Array(n).fill(fg);
    case 2: // Split Colors
      return arr.map((_, i) => (i < n / 2 ? fg : bg));
    case 3: // Trisection
      return arr.map((_, i) => {
        const seg = Math.floor(i / (n / 3));
        return seg === 1 ? bg : fg;
      });
    case 4: // Quartered
      return arr.map((_, i) => {
        const seg = Math.floor(i / (n / 4));
        return seg % 2 === 0 ? fg : bg;
      });
    case 5: // Center Accent
      return arr.map((_, i) => {
        const mid = n / 2;
        return Math.abs(i - mid) < n * 0.15 ? fg : bg;
      });

    // ── GROUP 2: CHASES & METEORS ──
    case 6: // Single Dot Chase
      arr[0] = fg; return arr;
    case 7: // Reflected Dot Chase (Two dots spaced out)
      arr[0] = fg;
      arr[Math.floor(n / 2)] = fg;
      return arr;
    case 8: // Comet Chase
      for (let i = 0; i < Math.min(6, n); i++) arr[i] = dim(fg, 1 - i * 0.15);
      return arr;
    case 9: // Meteor Shower
      for (let i = 0; i < n; i++) {
        if (i % 8 === 0) arr[i] = fg;
        else if (i % 8 < 4) arr[i] = dim(fg, 1 - (i % 8) * 0.25);
      }
      return arr;

    // ── GROUP 3: MARQUEES & BANDS ──
    case 10: // Micro Ants
      return arr.map((_, i) => (i % 2 === 0 ? fg : bg));
    case 11: // Theater Chase
      return arr.map((_, i) => (i % 3 === 0 ? fg : bg));
    case 12: // Dashed Marquee
      return arr.map((_, i) => ((i % 8) < 4 ? fg : bg));
    case 13: // Barber Pole
      return arr.map((_, i) => {
        const mod = i % 12;
        if (mod < 4) return fg;
        if (mod < 8) return lerpRGB(fg, bg, 0.5); // transition
        return bg;
      });
    case 14: // Bold Stripes
      return arr.map((_, i) => ((i % 16) < 8 ? fg : bg));

    // ── GROUP 4: MATH WAVES & GRADIENTS ──
    case 15: // Sine Pulse Wave
      return arr.map((_, i) => lerpRGB(bg, fg, (Math.sin((i / n) * Math.PI * 4) + 1) / 2));
    case 16: // Wave Pinch
      return arr.map((_, i) => {
        const distToCenter = Math.abs(i - n / 2) / (n / 2); // 0 at center, 1 at ends
        return lerpRGB(bg, fg, distToCenter);
      });
    case 17: // Breathing Wave (Gradient across strip)
      return arr.map((_, i) => lerpRGB(fg, bg, i / n));
    case 18: // Center-Out Comet
      return arr.map((_, i) => {
        const distToCenter = Math.abs(i - n / 2) / (n / 2);
        return lerpRGB(fg, bg, distToCenter * distToCenter); // non-linear fade
      });
    case 19: // Center-Out Marquee
      return arr.map((_, i) => {
        const mid = n / 2;
        const dist = Math.abs(i - mid);
        return dist % 6 < 3 ? fg : bg;
      });

    // ── GROUP 5: TEMPORAL FULL-STRIP (0x51 handled differently, arrays just return solid for visualizer) ──
    case 20: // Smooth Breath
    case 21: // Hard Jump Flash
    case 22: // Strobe
      return Array(n).fill(fg); // Used only by visualizer's temporal logic
    case 23: // Wipe/Fill Start to End (Simulated via gradient for 0x59)
      return arr.map((_, i) => (i < n / 2 ? fg : bg));
    case 24: // Wipe/Fill Center Out
      return arr.map((_, i) => (Math.abs(i - n / 2) < n / 4 ? fg : bg));

    // ── GROUP 6: GENERATIVE RAINBOWS & TRI-COLOR ──
    case 25: // True Rainbow Flow
      return arr.map((_, i) => hueToRGB((i / n) * 360));
    case 26: // Rainbow Marquee
      return arr.map((_, i) => {
        if ((i % 8) < 4) return bg;
        return hueToRGB((i / n) * 360);
      });
    case 27: // Rainbow Comet
      return arr.map((_, i) => {
        if (i < n / 3) return dim(hueToRGB((i / (n / 3)) * 360), 1 - (i / (n / 3)));
        return bg;
      });
    case 28: // Cyberpunk Shift (Cyan/Magenta/Yellow alternate)
      const c1 = { r: 0, g: 255, b: 255 }; // Cyan
      const c2 = { r: 255, g: 0, b: 255 }; // Magenta
      const c3 = { r: 255, g: 255, b: 0 }; // Yellow
      return arr.map((_, i) => {
        const mod = i % 15;
        if (mod < 5) return c1;
        if (mod < 10) return c2;
        return c3;
      });

    default:
      return Array(n).fill(fg);
  }
}

/**
 * Rotate a pixel array by `offset` positions (for visualizer scroll simulation).
 */
function rotateArray(arr: RGB[], animTick: number): RGB[] {
  if (arr.length === 0) return arr;
  // Negate animTick so it flows visually correct
  const offset = Math.floor((1 - animTick) * arr.length) % arr.length;
  if (offset === 0) return arr;
  return [...arr.slice(offset), ...arr.slice(0, offset)];
}

// ─── PUBLIC API ───────────────────────────────────────────────────────────────

/**
 * Get the full pixel array for a pattern at a specific animation frame.
 * Used by the VISUALIZER.
 */
export function getVisualizerFrame(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  animTick: number
): RGB[] {
  const n = Math.max(1, numLEDs);

  // Group 5 (Temporal 0x51) -> simulate visually with full-strip changes
  if (patternId === 20) { // Breath
    const t = (Math.sin(animTick * Math.PI * 2) + 1) / 2; // 0→1→0
    return Array(n).fill(lerpRGB(bg, fg, t));
  }
  if (patternId === 21) { // Jump Flash
    return Array(n).fill(animTick < 0.5 ? fg : bg);
  }
  if (patternId === 22) { // Strobe
    return Array(n).fill(Math.floor(animTick * 12) % 2 === 0 ? fg : bg);
  }

  // Phase 1A ge.* native pattern builder overrides (IDs 29-40)
  const phase1a = generatePhase1aArray(patternId, fg, bg, n, animTick);
  if (phase1a) return phase1a;

  const generated = generateArray(patternId, fg, bg, n);

  // Group 1 (Static) does not scroll
  if (patternId >= 1 && patternId <= 5) return generated;

  // Center-Out effects (18, 19, 24) should split scroll if true center out
  if (patternId === 18 || patternId === 19 || patternId === 24) {
    const mid = Math.floor(n / 2);
    const left = rotateArray(generated.slice(0, mid).reverse(), animTick).reverse();
    const right = rotateArray(generated.slice(mid), animTick);
    return [...left, ...right];
  }

  // All other groups scroll natively
  return rotateArray(generated, animTick);
}

/**
 * Get the hardware pixel array for a pattern.
 * Used by applyFixedPattern() to build the 0x59 payload.
 */
export function getHardwarePixelArray(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number
): RGB[] | null {
  // 0x51 temporal patterns return null so they fallback to buildCustomModePayload
  if (patternId >= 20 && patternId <= 22) return null;

  // Phase 1A ge.* native patterns evaluate at tick=0 to create the base hardware frame
  const phase1a = generatePhase1aArray(patternId, fg, bg, Math.max(1, numLEDs), 0);
  if (phase1a) return phase1a;

  return generateArray(patternId, fg, bg, Math.max(1, numLEDs));
}

/**
 * Get the 0x59 transition type for a pattern.
 */
export function getPatternTransitionType(patternId: PatternId): number {
  if (patternId >= 1 && patternId <= 5) return 0x00; // STATIC (Hardware holds array in place)
  return 0x03; // NATIVE SCROLL (RunningWater - Hardware dynamically scrolls the array)
}

/**
 * Build the full 0x59 hardware command.
 */
export function buildMultiColorPayload(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1
): number[] | null {
  const pixels = getHardwarePixelArray(patternId, fg, bg, numLEDs);
  if (!pixels) return null;

  const transitionType = getPatternTransitionType(patternId);
  return ZenggeProtocol.setMultiColor(pixels, speed, direction, transitionType);
}

/**
 * Build the 0x51 hardware command for temporal patterns (20-22).
 */
export function buildCustomModePayload(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  speed: number
): number[] | null {
  const s = Math.max(1, Math.min(100, Math.round(speed)));

  if (patternId === 20) { // Smooth Breath
    return ZenggeProtocol.setCustomMode([
      { mode: ZenggeProtocol.STEP_GRADUAL, speed: s, color1: fg, color2: bg },
      { mode: ZenggeProtocol.STEP_GRADUAL, speed: s, color1: bg, color2: fg },
    ]);
  }
  if (patternId === 21) { // Hard Jump
    return ZenggeProtocol.setCustomMode([
      { mode: ZenggeProtocol.STEP_JUMP, speed: s, color1: fg, color2: bg },
      { mode: ZenggeProtocol.STEP_JUMP, speed: s, color1: bg, color2: fg },
    ]);
  }
  if (patternId === 22) { // Strobe
    return ZenggeProtocol.setCustomMode([
      { mode: ZenggeProtocol.STEP_JUMP, speed: 100, color1: fg, color2: bg },
      { mode: ZenggeProtocol.STEP_JUMP, speed: 100, color1: bg, color2: fg },
    ]);
  }
  return null;
}

/**
 * Master dispatcher — builds payload for hardware write.
 */
export function buildPatternPayload(
  patternId: number,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1
): number[] | null {
  const multiColor = buildMultiColorPayload(patternId, fg, bg, numLEDs, speed, direction);
  if (multiColor) return multiColor;

  return buildCustomModePayload(patternId, fg, bg, speed);
}

// ─── MUSIC MODE VISUALIZER ────────────────────────────────────────────────────

/**
 * Private helpers for music mode — migrated from RbmSimulator.ts [BATCH:P1].
 */
function hexToRgb(hex: string): RGB {
  const h = hex.replace('#', '');
  return { r: parseInt(h.slice(0, 2), 16) || 0, g: parseInt(h.slice(2, 4), 16) || 0, b: parseInt(h.slice(4, 6), 16) || 0 };
}

function lerpRGBMusic(a: RGB, b: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return { r: Math.round(a.r + (b.r - a.r) * t), g: Math.round(a.g + (b.g - a.g) * t), b: Math.round(a.b + (b.b - a.b) * t) };
}

function getMusicPaletteAt(palette: RGB[], tick: number): RGB {
  if (palette.length === 0) return { r: 0, g: 0, b: 0 };
  return palette[Math.floor(tick * palette.length) % palette.length];
}

function getMusicPaletteSmooth(palette: RGB[], tick: number): RGB {
  if (palette.length <= 1) return palette[0] ?? { r: 0, g: 0, b: 0 };
  const pos = (tick * palette.length) % palette.length;
  return lerpRGBMusic(palette[Math.floor(pos) % palette.length], palette[(Math.floor(pos) + 1) % palette.length], pos - Math.floor(pos));
}

const MUSIC_RAINBOW7: RGB[] = ['#FF0000','#FF6600','#FFFF00','#00FF00','#00FFFF','#0066FF','#AA00FF'].map(hexToRgb);

/**
 * Get a hardware-accurate pixel + opacity array for MUSIC mode patterns 1–13.
 *
 * Audio-reactive: `magnitude` (0.0–1.0) gates intensity, fill depth, and flash thresholds.
 * The visualizer applies `opacities[i]` as per-LED brightness multiplier.
 *
 * Migrated from RbmSimulator.getRbmMusicFrame [BATCH:P1]. PatternEngine is now SSOT.
 *
 * @param musicPatternId - Music pattern ID 1–13
 * @param numLEDs        - LED count for the segment
 * @param animTick       - Animation tick 0.0–1.0 (looping)
 * @param magnitude      - Live audio amplitude 0.0–1.0
 * @param baseColorHex   - User-selected base color e.g. '#FF0000'
 */
export function getMusicVisualizerFrame(
  musicPatternId: number,
  numLEDs: number,
  animTick: number,
  magnitude: number,
  baseColorHex: string
): { pixels: RGB[]; opacities: number[] } {
  const n = Math.max(1, numLEDs);
  const base = hexToRgb(baseColorHex);
  const white: RGB = { r: 255, g: 255, b: 255 };
  const black: RGB = { r: 0, g: 0, b: 0 };
  const mag = Math.max(0, Math.min(1, magnitude));

  const pixels: RGB[] = Array(n).fill(null).map(() => ({ ...base }));
  const opacities: number[] = Array(n).fill(1.0);

  switch (musicPatternId) {
    case 1: { // Soft — whole strip breathe, depth = magnitude
      const breathe = 0.2 + mag * 0.8;
      const color = lerpRGBMusic(black, base, breathe);
      for (let i = 0; i < n; i++) { pixels[i] = { ...color }; opacities[i] = breathe; }
      break;
    }
    case 2: { // Cheerful — sparkle white on peaks
      for (let i = 0; i < n; i++) {
        if (mag > 0.7 && (i + Math.floor(animTick * 7)) % 3 === 0) {
          pixels[i] = { ...white }; opacities[i] = 1.0;
        } else { pixels[i] = { ...base }; opacities[i] = 0.3 + mag * 0.5; }
      }
      break;
    }
    case 3: { // Energy — magnitude-gated strobe flash
      const on = mag > 0.5 && Math.floor(animTick * 10) % 2 === 0;
      const c = getMusicPaletteAt(MUSIC_RAINBOW7, animTick);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = on ? 1.0 : 0.05; }
      break;
    }
    case 4: { // Relax — slow smooth rainbow fade
      const c = getMusicPaletteSmooth(MUSIC_RAINBOW7, animTick * 0.3);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = 0.5 + mag * 0.5; }
      break;
    }
    case 5: { // Passion — overlay expand center→ends; fill level = magnitude
      const center = Math.floor(n / 2);
      const reach = Math.floor(mag * center);
      for (let i = 0; i < n; i++) {
        pixels[i] = { ...base };
        opacities[i] = Math.abs(i - center) <= reach ? 1.0 : 0.05;
      }
      break;
    }
    case 6: { // Brisk — alternating segments jump on beat
      const step = Math.floor(animTick * 4) % 2;
      for (let i = 0; i < n; i++) {
        pixels[i] = { ...base };
        opacities[i] = ((Math.floor(i / 3) % 2) === step) && mag > 0.3 ? 1.0 : 0.1;
      }
      break;
    }
    case 7: { // Rhythm — VU meter bottom→top, fill = magnitude
      for (let i = 0; i < n; i++) {
        const fract = i / n;
        opacities[i] = fract < mag ? 1.0 : 0.05;
        pixels[i] = fract > 0.85 ? { r: 255, g: 0, b: 0 } : fract > 0.65 ? { r: 255, g: 200, b: 0 } : { ...base };
      }
      break;
    }
    case 8: { // Rolling — rainbow flow, magnitude controls opacity
      for (let i = 0; i < n; i++) {
        pixels[i] = getMusicPaletteSmooth(MUSIC_RAINBOW7, ((i / n) + animTick) % 1);
        opacities[i] = 0.2 + mag * 0.8;
      }
      break;
    }
    case 9: { // Flicker — deterministic twinkling dots on magnitude
      for (let i = 0; i < n; i++) {
        const seed = Math.sin(i * 127.1 + animTick * 311.7) * 43758.5;
        opacities[i] = (seed - Math.floor(seed)) < mag ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 10: { // Accumulate — overlay from both ends to center, fill = magnitude
      const reach = Math.floor(mag * (n / 2));
      for (let i = 0; i < n; i++) {
        opacities[i] = Math.min(i, n - 1 - i) < reach ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 11: { // Shuttle — bright dot bouncing in sync with animTick
      const bouncedPos = animTick < 0.5 ? animTick * 2 : (1 - animTick) * 2;
      const head = Math.floor(bouncedPos * n);
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - head);
        opacities[i] = dist === 0 ? 1.0 : dist === 1 ? 0.4 * mag : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 12: { // Fireworks — expand from center, burst flash on peak
      const center = Math.floor(n / 2);
      const burst = mag > 0.8;
      const reach = burst ? n : Math.floor(animTick * (n / 2));
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - center);
        if (burst) { pixels[i] = { ...white }; opacities[i] = 1.0; }
        else { pixels[i] = { ...base }; opacities[i] = dist <= reach ? (1 - dist / (n / 2)) * mag : 0.02; }
      }
      break;
    }
    case 13: { // Snow — drifting dots falling end→start
      for (let i = 0; i < n; i++) {
        const snowflake = Math.abs(Math.sin(i * 47.3 + Math.floor(animTick * 3) * 1234.5)) > (1 - mag * 0.7);
        pixels[i] = snowflake ? { ...white } : { ...base };
        opacities[i] = snowflake ? 1.0 : 0.1;
      }
      break;
    }
    default: {
      for (let i = 0; i < n; i++) { pixels[i] = { ...base }; opacities[i] = mag; }
    }
  }

  return { pixels, opacities };
}
