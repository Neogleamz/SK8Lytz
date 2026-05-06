/**
 * presetColorUtils.ts — Shared Color Resolution Utilities
 *
 * Single source of truth for rendering preset/group card colors.
 * Used by both PresetCard (Favorites/Picks) and SkateGroupCard (Dashboard).
 *
 * Key design decision: GENERATIVE patterns (Rainbow, Aurora, Color Flow) have
 * no meaningful fg/bg because the hardware generates HSV math autonomously.
 * We detect these via patternId → SK8LYTZ_TEMPLATES.colorMode and render a
 * representative rainbow strip instead of a bogus solid color.
 */
import { SK8LYTZ_TEMPLATES } from '../protocols/PatternEngine';
import type { IFavoriteState } from '../types/dashboard.types';
import type { GroupPatternSnapshot } from '../types/dashboard.types';

// ─── Internal helpers ─────────────────────────────────────────────────────────

/** 7-stop rainbow representing GENERATIVE patterns — rendered in order on the card */
export const GENERATIVE_RAINBOW: string[] = [
  '#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#00BFFF', '#0000FF', '#8B00FF',
];

/** Resolve the colorMode for a given patternId from the canonical template registry. */
const getColorMode = (patternId?: number): 'FG_ONLY' | 'FG_BG' | 'GENERATIVE' | null => {
  if (!patternId) return null;
  const template = SK8LYTZ_TEMPLATES.find(t => t.id === patternId);
  return (template?.colorMode as any) ?? null;
};

// ─── Exported resolvers ────────────────────────────────────────────────────────

/**
 * Resolves the dominant glow/shadow color for a preset card.
 * For GENERATIVE patterns returns a vibrant rainbow hue instead of a stored
 * fixedFgColor that was never used by the hardware.
 */
export const resolveGlowColor = (fav: IFavoriteState, fallback: string): string => {
  if (fav.mode === 'MUSIC') return fav.musicPrimaryColor || fav.musicSecondaryColor || fallback;

  if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') {
    const colorMode = getColorMode(fav.patternId);
    if (colorMode === 'GENERATIVE') return '#7F00FF'; // violet — mid-rainbow anchor
    return fav.fixedFgColor || fav.fixedBgColor || fallback;
  }

  if (fav.mode === 'MULTI' || fav.mode === 'BUILDER') return fav.multiColors?.[0] || fallback;
  return fallback;
};

/**
 * Resolves the LinearGradient color array for a preset card.
 *
 * GENERATIVE patterns → 7-stop rainbow (accurate to what the hardware shows)
 * FG_ONLY patterns    → solid single color
 * FG_BG patterns      → [fgColor, bgColor] gradient
 * MUSIC               → [primary, secondary]
 * BUILDER             → multiColors array (node-based gradient)
 */
export const resolveGradientColors = (fav: IFavoriteState, glow: string): string[] => {
  if (fav.mode === 'MUSIC' && fav.musicPrimaryColor) {
    return [fav.musicPrimaryColor, fav.musicSecondaryColor || fav.musicPrimaryColor];
  }

  if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') {
    const colorMode = getColorMode(fav.patternId);
    if (colorMode === 'GENERATIVE') return GENERATIVE_RAINBOW;
    if ((colorMode === 'FG_ONLY' || colorMode === 'FG_BG') && fav.fixedFgColor) {
      return [fav.fixedFgColor, fav.fixedBgColor || fav.fixedFgColor];
    }
  }

  if ((fav.mode === 'MULTI' || fav.mode === 'BUILDER') && fav.multiColors && fav.multiColors.length > 0) {
    return fav.multiColors.length === 1
      ? [fav.multiColors[0], fav.multiColors[0]]
      : fav.multiColors;
  }

  return [glow, glow];
};

/**
 * Resolves gradient colors from a GroupPatternSnapshot (used by SkateGroupCard).
 * Mirrors resolveGradientColors but operates on the leaner snapshot type stored
 * in lastGroupPatterns — no need to carry the full IFavoriteState.
 */
export const resolveGroupCardColors = (
  snapshot: GroupPatternSnapshot | undefined,
  fallback: string[],
): string[] => {
  if (!snapshot) return fallback;

  // GENERATIVE patterns — check patternId first
  if (snapshot.patternId) {
    const colorMode = getColorMode(snapshot.patternId);
    if (colorMode === 'GENERATIVE') return GENERATIVE_RAINBOW;
  }

  // BUILDER mode — use the stored multiColors
  if (snapshot.mode === 'BUILDER' && snapshot.multiColors && snapshot.multiColors.length > 0) {
    return snapshot.multiColors.length === 1
      ? [snapshot.multiColors[0], snapshot.multiColors[0]]
      : snapshot.multiColors;
  }

  // MUSIC mode
  if (snapshot.mode === 'MUSIC' && snapshot.fgColor) {
    return [snapshot.fgColor, snapshot.bgColor || snapshot.fgColor];
  }

  // Standard FG/BG
  if (snapshot.fgColor) {
    return [snapshot.fgColor, snapshot.bgColor || snapshot.fgColor];
  }

  return fallback;
};

/** Resolve mode icon name for a preset card (unchanged from PresetCard.tsx). */
export const resolveModeIcon = (mode: string): string => {
  if (mode === 'MUSIC') return 'microphone-outline';
  if (mode === 'RBM') return 'animation-play';
  if (mode === 'MULTI' || mode === 'BUILDER') return 'shape-square-plus';
  return 'speedometer';
};
