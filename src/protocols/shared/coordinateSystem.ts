/**
 * SK8Lytz Coordinate System
 *
 * LED position mapping and coordinate transform utilities extracted from SpatialEngine.ts.
 * Handles coordinate math used across multiple effect builders.
 */

/**
 * Generate an array of `n` elements using a factory function.
 * Equivalent to Array.from({ length: n }, factory).
 */
export function generateArray<T>(n: number, factory: (index: number) => T): T[] {
  return Array.from({ length: n }, (_, i) => factory(i));
}

/**
 * Normalize position `i` within a strip of `n` LEDs to [0.0, 1.0].
 */
export function normalizePosition(i: number, n: number): number {
  return n > 1 ? i / (n - 1) : 0;
}

/**
 * Given a phase offset (0–1) and direction, compute the scrolled position
 * within an n-LED strip. Returns the starting LED index.
 */
export function phaseToOffset(phase: number, n: number): number {
  return Math.floor(phase * n);
}

/**
 * Mirror a pixel array to create a palindrome (left-right symmetrical strip).
 * Used for HALOZ ring segments.
 */
export function mirrorArray<T>(arr: T[]): T[] {
  return [...arr, ...[...arr].reverse()];
}

/**
 * Build a multi-segment physical strip array by repeating rightSide (mirrored on odd segments).
 * Used by buildStreetMode for HALOZ rings vs SOULZ Y-splits.
 */
export function buildSegmentedArray<T>(rightSide: T[], segments: number): T[] {
  const leftSide = [...rightSide].reverse();
  const fullPayload: T[] = [];
  for (let i = 0; i < segments; i++) {
    if (i % 2 === 0) fullPayload.push(...rightSide);
    else fullPayload.push(...leftSide);
  }
  return fullPayload.slice(0, rightSide.length * segments);
}
