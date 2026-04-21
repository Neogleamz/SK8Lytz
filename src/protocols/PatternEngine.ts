/**
 * SK8Lytz Pattern Engine (Math Synthesizer)
 *
 * SINGLE SOURCE OF TRUTH for the 28 SK8LYTZ_TEMPLATES.
 * Synthesizes dynamic pixel arrays based on math parameters instead of legacy firmware IDs.
 *
 * Hardware animation model:
 *   - The IC strip controller animates NATIVELY once a 0x59 or 0x51 packet is sent.
 *   - 0x59 CASCADE (0x00): Hardware continuous scroll/loop for math arrays
 *   - 0x59 FREEZE (0x01): Hardware locks the array in place
 *   - 0x51 NATIVE: Used for full-strip temporal fades/flashes (Breath/Strobe)
 */

import { ZenggeProtocol } from './ZenggeProtocol';
import { interpolateColor } from '../utils/ColorUtils'; // we'll implement a small lerp helper if needed

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

  return generateArray(patternId, fg, bg, Math.max(1, numLEDs));
}

/**
 * Get the 0x59 transition type for a pattern.
 */
export function getPatternTransitionType(patternId: PatternId): number {
  if (patternId >= 1 && patternId <= 5) return 0x01; // FREEZE
  return 0x00; // CASCADE (Scroll)
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

