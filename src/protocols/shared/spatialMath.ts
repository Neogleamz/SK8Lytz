/**
 * SK8Lytz Spatial Math Utilities
 *
 * Pure color math functions extracted from SpatialEngine.ts (Phase 1 S4 extraction).
 * These have NO imports from engine files, breaking the dependency cycle.
 * All functions are pure: given the same inputs, they always return the same outputs.
 */

import type { RGB } from './engineTypes';

/** Dim an RGB color by a scalar factor (0.0–1.0). */
export function dim(c: RGB, factor: number): RGB {
  return {
    r: Math.round(c.r * factor),
    g: Math.round(c.g * factor),
    b: Math.round(c.b * factor),
  };
}

/** Linear interpolation between two RGB colors. t is clamped to [0,1]. */
export function lerpRGB(c1: RGB, c2: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(c1.r + (c2.r - c1.r) * t),
    g: Math.round(c1.g + (c2.g - c1.g) * t),
    b: Math.round(c1.b + (c2.b - c1.b) * t),
  };
}

/** Convert a hue angle (degrees 0–360) to an RGB color at full saturation/value. */
export function hueToRGB(hue: number): RGB {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return { r: Math.round(f(5) * 255), g: Math.round(f(3) * 255), b: Math.round(f(1) * 255) };
}

/** Blend two RGB colors. t=1 returns `a`, t=0 returns `b`. Clamped to [0,1]. */
export function blendRGB(a: RGB, b: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(a.r * t + b.r * (1 - t)),
    g: Math.round(a.g * t + b.g * (1 - t)),
    b: Math.round(a.b * t + b.b * (1 - t)),
  };
}

/**
 * Convert HSV to RGB.
 * @param h  Hue, 0.0–1.0 (fractional, not degrees)
 * @param s  Saturation, 0.0–1.0
 * @param v  Value (brightness), 0.0–1.0
 */
export function hsvToRgb(h: number, s: number, v: number): RGB {
  const i = Math.floor(h * 6);
  const f = h * 6 - i;
  const p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
  const [r, g, b] = [
    [v, q, p, p, t, v], [t, v, v, q, p, p], [p, p, t, v, v, q]
  ].map(ch => ch[i % 6]);
  return { r: Math.round(r * 255), g: Math.round(g * 255), b: Math.round(b * 255) };
}
