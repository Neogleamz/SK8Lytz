/**
 * SK8Lytz Pattern Engine
 *
 * SINGLE SOURCE OF TRUTH for all 10 Fixed Mode patterns.
 * Both the hardware payload and the app visualizer read from this engine.
 *
 * Hardware animation model (APK proven 2026-04-06, confirmed on device):
 *   - The IC strip controller animates NATIVELY once a 0x59 or 0x51 packet is sent.
 *   - We send ONE command; the hardware loops it autonomously.
 *   - The app visualizer SIMULATES that animation locally using an animTick (0.0–1.0).
 *
 * HARDWARE-CONFIRMED transition byte meanings (live device testing Apr 2026):
 *   0x00 = CASCADE  — continuous scroll/loop (correct for animated patterns)
 *   0x01 = FREEZE   — locks array in place (use for solid/static)
 *   0x02 = STROBE   — intended flash, but may not differ visibly from 0x01 on all HW
 *   0x03 = TRIGGER  — fires once: renders array at NEXT offset then STOPS.
 *                     Causes "blink + new position" behavior on each command.
 *                     NOT a continuous animation — DO NOT use for animated patterns.
 *
 * Pattern split:
 *   Pattern  1         → 0x59 with FREEZE (0x01) — pixel array locked, no scroll
 *   Patterns 2–5, 9–10 → 0x59 with CASCADE (0x00) — hardware continuous scroll
 *   Patterns 6–8       → 0x51 two-step DIY (hardware fades/jumps between FG and BG)
 */

import { ZenggeProtocol } from './ZenggeProtocol';

export interface RGB {
  r: number;
  g: number;
  b: number;
}

export type PatternId = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10;

// ─── PATTERN TEMPLATES ───────────────────────────────────────────────────────
// Define the repeating tile for each pattern.
// Each template is a function of fg and bg color.
// Patterns 6–8 return empty arrays (they use 0x51, not pixel arrays).

function dim(c: RGB, factor: number): RGB {
  return {
    r: Math.round(c.r * factor),
    g: Math.round(c.g * factor),
    b: Math.round(c.b * factor),
  };
}

function getPatternTile(patternId: PatternId, fg: RGB, bg: RGB): RGB[] {
  switch (patternId) {
    case 1:  return [fg];                                                    // Solid: single tile, tiled to full strip
    case 2:  return [fg, bg, bg, bg, bg, bg, bg, bg];                       // Single Dot: 1 FG in 7 BG
    case 3:  return [fg, dim(fg, 0.5), dim(fg, 0.2), bg, bg, bg];           // Comet: FG + 2-step trail
    case 4:  return [fg, fg, fg, fg, bg, bg, bg, bg];                       // Dashed: 4-on 4-off
    case 5:  return [fg, fg, bg, bg];                                        // Alternating: 2-on 2-off
    case 6:  return [];                                                       // Breath: handled by 0x51
    case 7:  return [];                                                       // Flash: handled by 0x51
    case 8:  return [];                                                       // Strobe: handled by 0x51
    case 9:  return [bg, bg, fg, fg, bg, bg];                                // Wave: FG pair in BG field
    case 10: return [fg, bg, bg, bg, bg, fg];                                // Pinch: FG ends closing in
    default: return [fg];
  }
}

/**
 * Tile a pattern template to exactly `numLEDs` pixels.
 * If the template is shorter, repeat it until we have enough pixels.
 */
function tilePattern(tile: RGB[], numLEDs: number): RGB[] {
  if (tile.length === 0) return [];
  if (tile.length === numLEDs) return [...tile];

  const result: RGB[] = [];
  while (result.length < numLEDs) {
    result.push(...tile);
  }
  return result.slice(0, numLEDs);
}

/**
 * Rotate a pixel array by `offset` positions (for visualizer scroll simulation).
 * Offset 0.0 = no scroll, 1.0 = full cycle = back to original.
 */
function rotateArray(arr: RGB[], animTick: number): RGB[] {
  if (arr.length === 0) return arr;
  const offset = Math.floor(animTick * arr.length) % arr.length;
  if (offset === 0) return arr;
  return [...arr.slice(offset), ...arr.slice(0, offset)];
}

// ─── PUBLIC API ───────────────────────────────────────────────────────────────

/**
 * Get the full pixel array for a pattern at a specific animation frame.
 * Used by the VISUALIZER to render the LED strip and dot pattern views.
 *
 * For patterns 6–8 (0x51 based), returns a pulsing approximation for visual purposes only.
 *
 * @param patternId  1–10
 * @param fg         Foreground color (sorted for display — do NOT apply color sorting here)
 * @param bg         Background color
 * @param numLEDs    Actual device LED count from hwSettings.ledPoints
 * @param animTick   Animation progress 0.0–1.0 (from Animated.Value or manual tick)
 */
export function getVisualizerFrame(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  animTick: number
): RGB[] {
  const n = Math.max(1, numLEDs);

  // Patterns 6–8 use 0x51 (full-strip transitions), simulate visually
  if (patternId === 6) {
    // Breath: fade FG→BG→FG based on tick
    const t = Math.sin(animTick * Math.PI); // 0→1→0
    return Array(n).fill({
      r: Math.round(fg.r * t + bg.r * (1 - t)),
      g: Math.round(fg.g * t + bg.g * (1 - t)),
      b: Math.round(fg.b * t + bg.b * (1 - t)),
    });
  }
  if (patternId === 7) {
    // Flash: hard cut FG/BG at midpoint
    return Array(n).fill(animTick < 0.5 ? fg : bg);
  }
  if (patternId === 8) {
    // Strobe: much faster flash
    return Array(n).fill(Math.floor(animTick * 6) % 2 === 0 ? fg : bg);
  }

  // Patterns 1, 2, 3, 4, 5, 9, 10 — tile and scroll
  const tile = getPatternTile(patternId, fg, bg);
  const tiled = tilePattern(tile, n);

  // Solid doesn't scroll
  if (patternId === 1) return tiled;

  // All others scroll (simulating hardware RunningWater)
  return rotateArray(tiled, animTick);
}

/**
 * Get the hardware pixel array for a pattern.
 * Used by applyFixedPattern() to build the 0x59 payload.
 *
 * IMPORTANT: Pass raw RGB values — do NOT pre-sort with applyColorSorting().
 * The hardware controller auto-remaps GRB internally via 0x81 config.
 *
 * For patterns 6–8: returns null (caller should use buildCustomModePayload instead).
 *
 * @param patternId  1–10
 * @param fg         Raw foreground RGB (no color sorting)
 * @param bg         Raw background RGB (no color sorting)
 * @param numLEDs    Actual device LED count from hwSettings.ledPoints
 */
export function getHardwarePixelArray(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number
): RGB[] | null {
  if (patternId === 6 || patternId === 7 || patternId === 8) return null;

  const n = Math.max(1, numLEDs);
  const tile = getPatternTile(patternId, fg, bg);
  return tilePattern(tile, n);
}

/**
 * Get the 0x59 transition type for a pattern.
 *
 * CONFIRMED hardware meanings:
 *   0x00 = CASCADE  — hardware scrolls the array (use for animated patterns)
 *   0x01 = FREEZE   — hardware locks the array in place (use for solid/static)
 */
export function getPatternTransitionType(patternId: PatternId): number {
  if (patternId === 1) return 0x01; // FREEZE — solid, locked, no scrolling
  return 0x00;                       // CASCADE — hardware continuously scrolls pixel array
                                     // NOTE: 0x03 was tested and causes blink+jump-to-new-position
                                     // (one-shot trigger, not continuous) — DO NOT use for animations
}

/**
 * Build the full 0x59 hardware command for patterns 1–5, 9–10.
 * Returns null for patterns 6–8 (use buildCustomModePayload instead).
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
 * Build the 0x51 hardware command for patterns 6 (Breath), 7 (Flash), 8 (Strobe).
 * Returns null for patterns 1–5, 9–10 (use buildMultiColorPayload instead).
 */
export function buildCustomModePayload(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  speed: number
): number[] | null {
  const s = Math.max(1, Math.min(100, Math.round(speed)));

  if (patternId === 6) {
    // Breath: 2-step Gradual fade FG→BG→FG (hardware loops)
    return ZenggeProtocol.setCustomMode([
      { mode: ZenggeProtocol.STEP_GRADUAL, speed: s, color1: fg, color2: bg },
      { mode: ZenggeProtocol.STEP_GRADUAL, speed: s, color1: bg, color2: fg },
    ]);
  }
  if (patternId === 7) {
    // Flash: 2-step Jump cut FG↔BG (hardware loops)
    return ZenggeProtocol.setCustomMode([
      { mode: ZenggeProtocol.STEP_JUMP, speed: s, color1: fg, color2: bg },
      { mode: ZenggeProtocol.STEP_JUMP, speed: s, color1: bg, color2: fg },
    ]);
  }
  if (patternId === 8) {
    // Strobe: same as Flash but forced max speed
    return ZenggeProtocol.setCustomMode([
      { mode: ZenggeProtocol.STEP_JUMP, speed: 100, color1: fg, color2: bg },
      { mode: ZenggeProtocol.STEP_JUMP, speed: 100, color1: bg, color2: fg },
    ]);
  }
  return null;
}

/**
 * Master dispatcher — sends the correct hardware payload for any pattern ID.
 * Returns the byte array to write to device, or null if inputs are invalid.
 */
export function buildPatternPayload(
  patternId: number,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1
): number[] | null {
  const pid = patternId as PatternId;

  const multiColor = buildMultiColorPayload(pid, fg, bg, numLEDs, speed, direction);
  if (multiColor) return multiColor;

  return buildCustomModePayload(pid, fg, bg, speed);
}
