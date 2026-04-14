/**
 * ColorUtils.ts — Pure color conversion utilities.
 *
 * Extracted from DockedController.tsx to eliminate 6x copy-pasted
 * hue-to-hex conversion lambdas and centralize color math.
 */

/**
 * Convert a hex color string (#RRGGBB) to its hue value (0–360).
 */
export const hexToHue = (hex: string): number => {
  const r = (parseInt(hex.slice(1, 3), 16) || 0) / 255;
  const g = (parseInt(hex.slice(3, 5), 16) || 0) / 255;
  const b = (parseInt(hex.slice(5, 7), 16) || 0) / 255;
  const max = Math.max(r, g, b), min = Math.min(r, g, b);
  let h = 0;
  if (max === min) h = 0;
  else if (max === r) h = (g - b) / (max - min) + (g < b ? 6 : 0);
  else if (max === g) h = (b - r) / (max - min) + 2;
  else if (max === b) h = (r - g) / (max - min) + 4;
  return Math.round(h * 60);
};

/**
 * Convert a hue value (0–360) to a hex color string (#RRGGBB).
 * Replaces the 6x copy-pasted inline lambda `f(5), f(3), f(1)` pattern.
 */
export const hueToHex = (hue: number): string => {
  const f = (n: number, k = (n + hue / 60) % 6) =>
    1 - Math.max(Math.min(k, 4 - k, 1), 0);
  const toHex = (v: number) => Math.round(v * 255).toString(16).padStart(2, '0').toUpperCase();
  return `#${toHex(f(5))}${toHex(f(3))}${toHex(f(1))}`;
};

/**
 * Map a hex color to a human-friendly name.
 * Returns 'Custom' for unrecognized values.
 */
export const getColorName = (hex: string): string => {
  const map: Record<string, string> = {
    '#FF0000': 'Red', '#FFFF00': 'Yellow', '#00FF00': 'Green',
    '#00FFFF': 'Cyan', '#0000FF': 'Blue', '#FF00FF': 'Magenta',
    '#FFFFFF': 'White', '#000000': 'Black'
  };
  return map[hex.toUpperCase()] || 'Custom';
};

/**
 * Parse a hex color string into its RGB components.
 */
export const hexToRgb = (hex: string): { r: number; g: number; b: number } => {
  const h = hex || '#000000';
  return {
    r: parseInt(h.substring(1, 3), 16) || 0,
    g: parseInt(h.substring(3, 5), 16) || 0,
    b: parseInt(h.substring(5, 7), 16) || 0,
  };
};

/**
 * Standard 10-color preset palette used by the color grid.
 */
export const COLOR_PRESET_PALETTE = [
  '#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF',
  '#0000FF', '#800080', '#FF00FF', '#FFFFFF', '#000000'
] as const;

/**
 * Map of preset hex colors to their hue values for instant hue slider sync.
 */
export const PRESET_HUE_MAP: Record<string, number> = {
  '#FF0000': 0, '#FF8000': 30, '#FFFF00': 60, '#00FF00': 120,
  '#00FFFF': 180, '#0000FF': 240, '#800080': 280, '#FF00FF': 300,
  '#FFFFFF': 0, '#000000': 0
};
