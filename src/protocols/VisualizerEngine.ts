import type { RGB, PatternId, PatternOptions } from './PatternEngine';
import { generateArray, getPatternTransitionType, getHardwarePixelArray } from './SpatialEngine';


export function rotateArray(arr: RGB[], animTick: number, direction: 0 | 1): RGB[] {
  if (arr.length === 0) return arr;
  // Direction 1 = Forward (shift array right), 0 = Reverse (shift array left)
  // To make it flow forward visually, we offset by (1 - animTick). 
  // Reverse flows opposite.
  const phase = direction === 1 ? (1 - animTick) : animTick;
  const offset = Math.floor(phase * arr.length) % arr.length;
  
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
  animTick: number,
  direction: 0 | 1 = 1,
  options?: PatternOptions
): RGB[] {
  const n = Math.max(1, numLEDs);

  // For visualizer, we only generate ONE segment of data (segments=1),
  // since ProductVisualizer's 'isMirrored' will automatically handle the physical geometry mapping.
  const visualizerOptions = { ...options, segments: 1 };

  // ── Native 0x41 Settled Mode Effects ──
  // These are handled dynamically by generateArray at `animTick`.
  // No array rotation is needed, the math generators build the exact frame.
  if (patternId >= 1 && patternId <= 33) {
    return generateArray(patternId, fg, bg, n, animTick, direction, visualizerOptions);
  }

  // ── 0x59 Fallbacks (e.g. Street Modes 101-105) ──
  const transitionType = getPatternTransitionType(patternId);
  const isContinuousScroll = transitionType === 0x02;

  // Spatial effects (Continuous scroll) need a static `tick` to build a seamless snapshot.
  const passTick = isContinuousScroll ? 0.33 : animTick;

  const generated = generateArray(patternId, fg, bg, n, passTick, direction, visualizerOptions);

  if (isContinuousScroll) {
    return rotateArray(generated, animTick, direction);
  }

  return generated;
}

/**
 * Get the hardware pixel array for a pattern.
 * Used by applyFixedPattern() to build the 0x59 payload.
 */

export { getHardwarePixelArray };
