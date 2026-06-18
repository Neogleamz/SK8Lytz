/**
 * SK8Lytz Engine Shared Utilities
 *
 * Common utility functions shared across engine files.
 * Extracted to prevent circular imports between PatternEngine, VisualizerEngine, and SymphonyEngine.
 * No engine file imports — safe to import from any engine.
 */

import type { RGB } from './engineTypes';

/**
 * Convert a hex color string (#RRGGBB) to an RGB object.
 * Returns { r: 0, g: 0, b: 0 } for invalid/empty input.
 */
export function hexToRgb(hex: string): RGB {
  const h = hex.replace('#', '');
  return {
    r: parseInt(h.slice(0, 2), 16) || 0,
    g: parseInt(h.slice(2, 4), 16) || 0,
    b: parseInt(h.slice(4, 6), 16) || 0,
  };
}

/**
 * Clamp a number between min and max (inclusive).
 */
export function clamp(value: number, min: number, max: number): number {
  return Math.max(min, Math.min(max, value));
}

/**
 * Smooth step interpolation (ease in/out) from 0 to 1.
 * @param t  Input value in [0,1]
 */
export function smoothStep(t: number): number {
  t = clamp(t, 0, 1);
  return t * t * (3 - 2 * t);
}

/**
 * Map a value from one numeric range to another.
 */
export function mapRange(
  value: number,
  inMin: number,
  inMax: number,
  outMin: number,
  outMax: number
): number {
  return outMin + ((value - inMin) / (inMax - inMin)) * (outMax - outMin);
}
