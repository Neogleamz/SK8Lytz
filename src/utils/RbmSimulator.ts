/**
 * RbmSimulator.ts — SK8Lytz Hardware-Accurate LED Frame Generator
 *
 * Converts an RbmPattern + animTick (0.0–1.0) into an exact RGB[] pixel array
 * that mirrors what the Zengge Symphony controller outputs to the physical strip.
 *
 * Motion model:
 *   animTick comes from the Animated.Value loop (0→1, looping).
 *   Each motion type indexes into this tick to derive head position / fill level.
 *
 * Also exports getRbmMusicFrame() for music mode — maps SymphonyEffect templates
 * to audio-reactive pixel arrays using the same motion primitives.
 */

import { BG_HEX, getRbmPattern, PALETTE, type RbmColorPalette, type RbmMotion } from './RbmDictionary';

// ── Internal RGB type (matches PatternEngine for compatibility) ──────────────
export interface RGB { r: number; g: number; b: number; }

function hexToRgb(hex: string): RGB {
  const h = hex.replace('#', '');
  return {
    r: parseInt(h.slice(0, 2), 16) || 0,
    g: parseInt(h.slice(2, 4), 16) || 0,
    b: parseInt(h.slice(4, 6), 16) || 0,
  };
}

export function rgbToHex(rgb: RGB): string {
  return `#${rgb.r.toString(16).padStart(2,'0')}${rgb.g.toString(16).padStart(2,'0')}${rgb.b.toString(16).padStart(2,'0')}`;
}

function lerp(a: number, b: number, t: number): number {
  return Math.round(a + (b - a) * Math.max(0, Math.min(1, t)));
}

function lerpRgb(a: RGB, b: RGB, t: number): RGB {
  return { r: lerp(a.r, b.r, t), g: lerp(a.g, b.g, t), b: lerp(a.b, b.b, t) };
}

// ── Build the palette as RGB objects ─────────────────────────────────────────
function getPaletteRgb(colors: RbmColorPalette): RGB[] {
  return PALETTE[colors].map(hexToRgb);
}

// Get the current cycling palette color at an animation tick
// For palette with N colors: changes color every 1/N of the cycle
function getPaletteColorAt(palette: RGB[], tick: number): RGB {
  if (palette.length === 0) return { r: 0, g: 0, b: 0 };
  if (palette.length === 1) return palette[0];
  const idx = Math.floor(tick * palette.length) % palette.length;
  return palette[idx];
}

// Get smoothly interpolated palette color at tick (for FADE)
function getPaletteColorSmooth(palette: RGB[], tick: number): RGB {
  if (palette.length === 0) return { r: 0, g: 0, b: 0 };
  if (palette.length === 1) return palette[0];
  const pos = (tick * palette.length) % palette.length;
  const i0 = Math.floor(pos) % palette.length;
  const i1 = (i0 + 1) % palette.length;
  const t = pos - Math.floor(pos);
  return lerpRgb(palette[i0], palette[i1], t);
}

// ── Motion frame builders ────────────────────────────────────────────────────

/**
 * Fill a strip with bg, then place the moving head dot(s) at the current position.
 * colorTick: which palette color to use for the head (cycles through palette independently)
 */
function frameScrollFwd(
  n: number, tick: number, palette: RGB[], bg: RGB, headSize: number
): RGB[] {
  const pixels = Array(n).fill(bg).map(_ => ({ ...bg }));
  const paletteColor = getPaletteColorAt(palette, tick);
  const headPos = Math.floor(tick * n) % n;
  for (let j = 0; j < headSize; j++) {
    const idx = (headPos + j) % n;
    pixels[idx] = paletteColor;
  }
  return pixels;
}

function frameScrollRev(
  n: number, tick: number, palette: RGB[], bg: RGB, headSize: number
): RGB[] {
  return frameScrollFwd(n, 1 - tick, palette, bg, headSize);
}

function framePingPong(
  n: number, tick: number, palette: RGB[], bg: RGB, headSize: number
): RGB[] {
  const t = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  return frameScrollFwd(n, t, palette, bg, headSize);
}

function frameMidOut(
  n: number, tick: number, palette: RGB[], bg: RGB, headSize: number
): RGB[] {
  const pixels = Array(n).fill(bg).map(_ => ({ ...bg }));
  const center = Math.floor(n / 2);
  const paletteColor = getPaletteColorAt(palette, tick);
  // Two heads emerge from center, moving toward both ends
  const reach = Math.floor(tick * center);
  for (let j = 0; j < headSize; j++) {
    const leftIdx = center - reach - j;
    const rightIdx = center + reach + j;
    if (leftIdx >= 0) pixels[leftIdx] = paletteColor;
    if (rightIdx < n) pixels[rightIdx] = paletteColor;
  }
  return pixels;
}

function frameEndsIn(
  n: number, tick: number, palette: RGB[], bg: RGB, headSize: number
): RGB[] {
  const pixels = Array(n).fill(bg).map(_ => ({ ...bg }));
  const center = Math.floor(n / 2);
  const paletteColor = getPaletteColorAt(palette, tick);
  // Two heads start at ends, converge toward center
  const reach = Math.floor(tick * center);
  for (let j = 0; j < headSize; j++) {
    const leftIdx = reach + j;
    const rightIdx = n - 1 - reach - j;
    if (leftIdx < center) pixels[leftIdx] = paletteColor;
    if (rightIdx >= center) pixels[rightIdx] = paletteColor;
  }
  return pixels;
}

/**
 * Overlay: fill from start→end progressively (color fills in as head moves right).
 * Color changes per completed fill cycle.
 */
function frameOverlayFwd(
  n: number, tick: number, palette: RGB[], bg: RGB
): RGB[] {
  const paletteColor = getPaletteColorAt(palette, tick);
  const fillCount = Math.floor(tick * n);
  return Array.from({ length: n }, (_, i) => i < fillCount ? paletteColor : { ...bg });
}

function frameOverlayRev(
  n: number, tick: number, palette: RGB[], bg: RGB
): RGB[] {
  const paletteColor = getPaletteColorAt(palette, tick);
  const fillCount = Math.floor(tick * n);
  return Array.from({ length: n }, (_, i) => i >= (n - fillCount) ? paletteColor : { ...bg });
}

function frameOverlayPing(
  n: number, tick: number, palette: RGB[], bg: RGB
): RGB[] {
  const t = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  return frameOverlayFwd(n, t, palette, bg);
}

function frameOverlayMid(
  n: number, tick: number, palette: RGB[], bg: RGB
): RGB[] {
  const paletteColor = getPaletteColorAt(palette, tick);
  const center = Math.floor(n / 2);
  const reach = Math.floor(tick * center);
  return Array.from({ length: n }, (_, i) => {
    const dist = Math.abs(i - center);
    return dist <= reach ? paletteColor : { ...bg };
  });
}

function frameOverlayEnds(
  n: number, tick: number, palette: RGB[], bg: RGB
): RGB[] {
  const paletteColor = getPaletteColorAt(palette, tick);
  const reach = Math.floor(tick * (n / 2));
  return Array.from({ length: n }, (_, i) => {
    return (i < reach || i >= n - reach) ? paletteColor : { ...bg };
  });
}

function frameFade(n: number, tick: number, palette: RGB[]): RGB[] {
  const color = getPaletteColorSmooth(palette, tick);
  return Array(n).fill(color).map(_ => ({ ...color }));
}

function frameJump(n: number, tick: number, palette: RGB[]): RGB[] {
  const color = getPaletteColorAt(palette, tick);
  return Array(n).fill(color).map(_ => ({ ...color }));
}

function frameStrobe(n: number, tick: number, palette: RGB[]): RGB[] {
  // Fast flash: ~6 flashes per cycle
  const color = getPaletteColorAt(palette, tick);
  const on = Math.floor(tick * 12) % 2 === 0;
  return Array(n).fill(on ? color : { r: 0, g: 0, b: 0 }).map(_ => on ? { ...color } : { r: 0, g: 0, b: 0 });
}

function frameBreathe(n: number, tick: number, palette: RGB[]): RGB[] {
  // Full-strip breathe: fade in/out using sine
  const t = (Math.sin(tick * Math.PI * 2) + 1) / 2; // 0→1→0 smoothly
  const color = getPaletteColorAt(palette, tick);
  const dimmed: RGB = {
    r: Math.round(color.r * t),
    g: Math.round(color.g * t),
    b: Math.round(color.b * t),
  };
  return Array(n).fill(dimmed).map(_ => ({ ...dimmed }));
}

function frameComet(
  n: number, tick: number, palette: RGB[], bg: RGB, pingPong: boolean
): RGB[] {
  const pixels = Array(n).fill(null).map(() => ({ ...bg }));
  const t = pingPong
    ? (tick < 0.5 ? tick * 2 : (1 - tick) * 2)
    : tick;
  const headPos = Math.floor(t * n) % n;
  const paletteColor = getPaletteColorAt(palette, tick);
  // Head full bright, 3-step fading trail
  pixels[headPos] = paletteColor;
  const trail = [0.5, 0.25, 0.1];
  for (let j = 0; j < trail.length; j++) {
    const trailIdx = (headPos - j - 1 + n) % n;
    pixels[trailIdx] = lerpRgb(bg, paletteColor, trail[j]);
  }
  return pixels;
}

// ── Main dispatcher ──────────────────────────────────────────────────────────

/**
 * Get a hardware-accurate pixel array for an RBM pattern at the given animTick.
 * Returns RGB[] of length numLEDs ready to map to the visualizer render points.
 */
export function getRbmVisualizerFrame(
  patternId: number,
  numLEDs: number,
  animTick: number
): RGB[] {
  const n = Math.max(1, numLEDs);
  const pattern = getRbmPattern(patternId);
  const palette = getPaletteRgb(pattern.colors);
  const bg = hexToRgb(BG_HEX[pattern.bg]);
  const headSize = pattern.points === 'SINGLE' ? 1 : 3;

  switch (pattern.motion as RbmMotion) {
    case 'SCROLL_FWD':   return frameScrollFwd(n, animTick, palette, bg, headSize);
    case 'SCROLL_REV':   return frameScrollRev(n, animTick, palette, bg, headSize);
    case 'PING_PONG':    return framePingPong(n, animTick, palette, bg, headSize);
    case 'MID_OUT':      return frameMidOut(n, animTick, palette, bg, headSize);
    case 'ENDS_IN':      return frameEndsIn(n, animTick, palette, bg, headSize);
    case 'OVERLAY_FWD':  return frameOverlayFwd(n, animTick, palette, bg);
    case 'OVERLAY_REV':  return frameOverlayRev(n, animTick, palette, bg);
    case 'OVERLAY_PING': return frameOverlayPing(n, animTick, palette, bg);
    case 'OVERLAY_MID':  return frameOverlayMid(n, animTick, palette, bg);
    case 'OVERLAY_ENDS': return frameOverlayEnds(n, animTick, palette, bg);
    case 'FADE':         return frameFade(n, animTick, palette);
    case 'JUMP':         return frameJump(n, animTick, palette);
    case 'STROBE':       return frameStrobe(n, animTick, palette);
    case 'BREATHE':      return frameBreathe(n, animTick, palette);
    case 'COMET':        return frameComet(n, animTick, palette, bg, false);
    case 'COMET_PING':   return frameComet(n, animTick, palette, bg, true);
    case 'STATIC':
    case 'ALL':
    default: {
      // STATIC: first palette color solid
      const c = palette[0] || { r: 255, g: 0, b: 0 };
      return Array(n).fill(null).map(() => ({ ...c }));
    }
  }
}

// ── Music Mode Frame Generator ───────────────────────────────────────────────
/**
 * Music mode patterns (1-13) use SymphonyEffect templates as their motion base,
 * then modulate opacity/intensity with live audio magnitude.
 *
 * Returns an { pixels: RGB[], opacity: number[] } where opacity per-LED (0.0-1.0)
 * encodes the audio reactivity. The visualizer applies these as LED brightness.
 *
 * SymphonyEffect → Music Mode mapping (derived from APK):
 *   1  Soft        → Effect2: Breathe gradually (whole strip, magnitude controls depth)
 *   2  Cheerful    → Effect3: Jump quickly (sparkle on peaks)
 *   3  Energy      → Effect4: Strobe-flash (magnitude gated)
 *   4  Relax       → Effect1: Fade gradually (slow smooth color change)
 *   5  Passion     → Effect11: Overlay from middle to both ends (magnitude = fill level)
 *   6  Brisk       → Effect3: Jump by segments (alternate segments on beat)
 *   7  Rhythm      → Effect9: Overlay from start (magnitude = VU fill height)
 *   8  Rolling     → Effect37: Flow gradually start→end (running color flow)
 *   9  Flicker     → Effect13: Fading run 1pt (randomized on magnitude)
 *   10 Accumulate  → Effect12: Overlay from both ends to middle (magnitude = fill)
 *   11 Shuttle     → Effect7: Running 1pt from middle to both ends (bouncing anim)
 *   12 Fireworks   → Effect11+burst: Expand from center, burst on peak
 *   13 Snow        → Effect14: Fading run 1pt end to start (drifting dots)
 */
export function getRbmMusicFrame(
  musicPatternId: number,
  numLEDs: number,
  animTick: number,
  magnitude: number,   // 0.0 – 1.0 live audio amplitude
  baseColorHex: string
): { pixels: RGB[]; opacities: number[] } {
  const n = Math.max(1, numLEDs);
  const base = hexToRgb(baseColorHex);
  const white: RGB = { r: 255, g: 255, b: 255 };
  const black: RGB = { r: 0, g: 0, b: 0 };
  const mag = Math.max(0, Math.min(1, magnitude));

  // Rainbow palette for modes that cycle colors
  const rainbow: RGB[] = PALETTE.RAINBOW7.map(hexToRgb);

  const pixels: RGB[] = Array(n).fill(null).map(() => ({ ...base }));
  const opacities: number[] = Array(n).fill(1.0);

  switch (musicPatternId) {
    case 1: { // Soft — whole strip breathe, depth = magnitude
      const breathe = 0.2 + mag * 0.8;
      const color = lerpRgb(black, base, breathe);
      for (let i = 0; i < n; i++) { pixels[i] = { ...color }; opacities[i] = breathe; }
      break;
    }
    case 2: { // Cheerful — sparkle white on peaks
      for (let i = 0; i < n; i++) {
        if (mag > 0.7 && (i + Math.floor(animTick * 7)) % 3 === 0) {
          pixels[i] = { ...white }; opacities[i] = 1.0;
        } else {
          pixels[i] = { ...base }; opacities[i] = 0.3 + mag * 0.5;
        }
      }
      break;
    }
    case 3: { // Energy — strobe gated by magnitude
      const on = mag > 0.5 && Math.floor(animTick * 10) % 2 === 0;
      const c = getPaletteColorAt(rainbow, animTick);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = on ? 1.0 : 0.05; }
      break;
    }
    case 4: { // Relax — slow smooth rainbow fade
      const c = getPaletteColorSmooth(rainbow, animTick * 0.3);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = 0.5 + mag * 0.5; }
      break;
    }
    case 5: { // Passion — overlay expand from center, fill level = magnitude
      const center = Math.floor(n / 2);
      const reach = Math.floor(mag * center);
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - center);
        pixels[i] = { ...base };
        opacities[i] = dist <= reach ? 1.0 : 0.05;
      }
      break;
    }
    case 6: { // Brisk — alternating segments jump on beat
      const step = Math.floor(animTick * 4) % 2;
      for (let i = 0; i < n; i++) {
        const active = ((Math.floor(i / 3) % 2) === step) && mag > 0.3;
        pixels[i] = { ...base }; opacities[i] = active ? 1.0 : 0.1;
      }
      break;
    }
    case 7: { // Rhythm — VU meter bottom→top, fill = magnitude
      for (let i = 0; i < n; i++) {
        const fract = i / n;
        opacities[i] = fract < mag ? 1.0 : 0.05;
        // Color gradient: base → yellow → red as it fills up
        pixels[i] = fract > 0.85 ? { r: 255, g: 0, b: 0 } : fract > 0.65 ? { r: 255, g: 200, b: 0 } : { ...base };
      }
      break;
    }
    case 8: { // Rolling — rainbow flow, magnitude controls opacity
      for (let i = 0; i < n; i++) {
        const pos = ((i / n) + animTick) % 1;
        pixels[i] = getPaletteColorSmooth(rainbow, pos);
        opacities[i] = 0.2 + mag * 0.8;
      }
      break;
    }
    case 9: { // Flicker — random twinkling dots on magnitude
      for (let i = 0; i < n; i++) {
        // Deterministic "random" via i+tick hash — avoids true random re-renders
        const seed = Math.sin(i * 127.1 + animTick * 311.7) * 43758.5;
        const r = seed - Math.floor(seed);
        opacities[i] = r < mag ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 10: { // Accumulation — overlay from both ends to center, fill = magnitude
      const reach = Math.floor(mag * (n / 2));
      for (let i = 0; i < n; i++) {
        const fromEdge = Math.min(i, n - 1 - i);
        opacities[i] = fromEdge < reach ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 11: { // Shuttle — bright dot bouncing in sync with audio peak
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
        if (burst) {
          pixels[i] = { ...white }; opacities[i] = 1.0;
        } else {
          pixels[i] = { ...base };
          opacities[i] = dist <= reach ? (1 - dist / (n / 2)) * mag : 0.02;
        }
      }
      break;
    }
    case 13: { // Snow — drifting dots falling end→start
      for (let i = 0; i < n; i++) {
        // "Snowflake" at every 1/4 of strip, drifting
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


