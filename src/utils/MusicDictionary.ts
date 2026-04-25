/**
 * MusicDictionary.ts
 * Authoritative registry for all 46 hardware-native music profiles.
 *
 * Hardware Source: ZENGGE Protocol Bible §0x73 (APK-verified, 2026-04-22)
 *
 * Two distinct hardware matrices, each with its own pattern table:
 *  - 0x26 (Light Bar):    16 profiles (IDs 1–16)
 *  - 0x27 (Light Screen): 30 profiles (IDs 1–30)
 *
 * colorMode gates which color pickers the UI renders:
 *  - 'NONE'    → generative / algorithmic — hardware ignores FG/BG bytes
 *  - 'FG_ONLY' → foreground (sound column) color only
 *  - 'FG_BG'   → both foreground (sound column) + background (drop) pickers
 *
 * Protocol Bible reference (lines 699–710, confirmed from APK MusicModeFragment):
 *  Light Bar  IDs 1–10, 12, 13, 16 → NONE (generative spectrum)
 *  Light Bar  IDs 11, 14, 15       → FG_ONLY (monochromatic rhythm)
 *  Light Screen IDs 1–18, 25–27    → NONE (advanced spectrum)
 *  Light Screen IDs 19–24, 28–30   → FG_BG (custom matrix rhythm)
 */

/** Color picker availability for a music profile. */
export type MusicColorMode =
  | 'NONE'     // Generative — hardware computes color algorithmically
  | 'FG_ONLY'  // Foreground (sound column) color only
  | 'FG_BG';   // Foreground + Background (drop) color both shown

/** Canonical shape of a hardware music profile. */
export interface MusicProfile {
  /** effectId byte sent in 0x73 payload (1-indexed). */
  id: number;
  /** User-facing display name. */
  name: string;
  /** Controls which color pickers the UI renders. */
  colorMode: MusicColorMode;
  /** Speed is supported by all profiles — field reserved for future per-profile overrides. */
  supportsSpeed: true;
  /** Brightness is supported by all profiles. */
  supportsBrightness: true;
}

// ---------------------------------------------------------------------------
// 0x26 — Light Bar Matrix (16 profiles)
// ---------------------------------------------------------------------------
// IDs 1–10, 12, 13, 16 → NONE (generative spectrum)
// IDs 11, 14, 15       → FG_ONLY (monochromatic rhythm)
// ---------------------------------------------------------------------------

export const LIGHT_BAR_PROFILES: MusicProfile[] = [
  { id: 1,  name: 'Pulse Classic',   colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 2,  name: 'Rhythm Wave',     colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 3,  name: 'Beat Flow',       colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 4,  name: 'Energy Burst',    colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 5,  name: 'Bass Spike',      colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 6,  name: 'Frequency Scan',  colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 7,  name: 'Spectrum Rush',   colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 8,  name: 'Audio Tide',      colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 9,  name: 'Sound Bloom',     colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 10, name: 'Vibe Cascade',    colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 11, name: 'Mono Pulse',      colorMode: 'FG_ONLY', supportsSpeed: true, supportsBrightness: true },
  { id: 12, name: 'Sweep Glow',      colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 13, name: 'Crystal Fade',    colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
  { id: 14, name: 'Rhythm Chroma',   colorMode: 'FG_ONLY', supportsSpeed: true, supportsBrightness: true },
  { id: 15, name: 'Beat Shimmer',    colorMode: 'FG_ONLY', supportsSpeed: true, supportsBrightness: true },
  { id: 16, name: 'Classic Strobe',  colorMode: 'NONE',    supportsSpeed: true, supportsBrightness: true },
];

// ---------------------------------------------------------------------------
// 0x27 — Light Screen Matrix (30 profiles)
// ---------------------------------------------------------------------------
// IDs 1–18, 25–27 → NONE (advanced spectrum)
// IDs 19–24, 28–30 → FG_BG (custom matrix rhythm — FG=sound, BG=drop)
// ---------------------------------------------------------------------------

export const LIGHT_SCREEN_PROFILES: MusicProfile[] = [
  { id: 1,  name: 'Spectrum Pulse',  colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 2,  name: 'Audio Rain',      colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 3,  name: 'Wave Rush',       colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 4,  name: 'Bounce Grid',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 5,  name: 'Frequency Arc',   colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 6,  name: 'Bass Column',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 7,  name: 'Neon Climb',      colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 8,  name: 'Pixel Bloom',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 9,  name: 'Sound Scatter',   colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 10, name: 'Vibe Matrix',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 11, name: 'Bass Burst',      colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 12, name: 'Spectrum Fade',   colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 13, name: 'Audio Flare',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 14, name: 'Rhythm Grid',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 15, name: 'Light Cascade',   colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 16, name: 'Neon Drift',      colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 17, name: 'Energy Sweep',    colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 18, name: 'Crystal Storm',   colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 19, name: 'Custom Drop',     colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 20, name: 'Dual Wave',       colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 21, name: 'Color Rain',      colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 22, name: 'Beat Columns',    colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 23, name: 'Hue Pulse',       colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 24, name: 'Rhythm Fade',     colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 25, name: 'Aurora Flow',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 26, name: 'Spectrum Dance',  colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 27, name: 'Pixel Storm',     colorMode: 'NONE',  supportsSpeed: true, supportsBrightness: true },
  { id: 28, name: 'Glow Drop',       colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 29, name: 'Chroma Wave',     colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
  { id: 30, name: 'Matrix Pulse',    colorMode: 'FG_BG', supportsSpeed: true, supportsBrightness: true },
];

// ---------------------------------------------------------------------------
// Lookup helpers
// ---------------------------------------------------------------------------

/** Returns the correct profile array for the active modeType byte. */
export function getMusicProfiles(modeType: number): MusicProfile[] {
  return modeType === 0x27 ? LIGHT_SCREEN_PROFILES : LIGHT_BAR_PROFILES;
}

/** Returns the max valid patternId for the active modeType. */
export function getMusicPatternMax(modeType: number): number {
  return getMusicProfiles(modeType).length;
}

/**
 * Returns the profile object for the current selection.
 * Falls back to the first profile if patternId is out of range.
 */
export function getActiveMusicProfile(modeType: number, patternId: number): MusicProfile {
  return (
    getMusicProfiles(modeType).find(p => p.id === patternId) ??
    getMusicProfiles(modeType)[0]
  );
}

/** Returns the display name for a given modeType + patternId combination. */
export function getMusicPatternLabel(modeType: number, patternId: number): string {
  return getActiveMusicProfile(modeType, patternId).name;
}
