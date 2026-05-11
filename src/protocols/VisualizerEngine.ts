import type { RGB, PatternId, PatternOptions } from './PatternEngine';
import { generateArray, getPatternTransitionType } from './SpatialEngine';
import { getSymphonyVisualizerFrame } from './SymphonyEngine';

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

  // ── NATIVE 0x51 TEST PATTERN INTERCEPTION ──
  if (patternId >= 201 && patternId <= 244) {
    let modeId = patternId - 200;
    return getSymphonyVisualizerFrame(modeId, fg, bg, n, animTick);
  }

  // Check if hardware natively handles the animation via 0x02 Running
  const transitionType = getPatternTransitionType(patternId);
  const isContinuousScroll = transitionType === 0x02 && patternId < 100;

  // Spatial effects (Continuous scroll) need a static `tick` to build a seamless snapshot.
  // We use `seedTick` to ensure the initial frame is visually rich (not all-black).
  // Temporal effects (Breathe, Flash, Strobe) need dynamic `animTick` to interpolate phase.
  const ZERO_TICK_IDS = new Set([26, 28, 31, 46, 54]); // Strobe/Jump
  const QUARTER_TICK_IDS = new Set([24, 30, 36, 40]); // Breathe (sin wave peak at PI/2)
  const seedTick = ZERO_TICK_IDS.has(patternId) ? 0.0 : (QUARTER_TICK_IDS.has(patternId) ? 0.25 : 0.33);
  const passTick = isContinuousScroll ? seedTick : animTick;

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
