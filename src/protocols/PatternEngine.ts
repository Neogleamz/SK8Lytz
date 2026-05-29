/**
 * SK8Lytz Pattern Engine (Math Synthesizer)
 *
 * Source of truth for all SK8LYTZ_TEMPLATES — registry + math builders + dispatch.
 * Synthesizes dynamic pixel arrays based on math parameters instead of legacy firmware IDs.
 */




import { ZenggeProtocol } from './ZenggeProtocol';
import { getPatternTransitionType, getHardwarePixelArray } from './SpatialEngine';
import { getVisualizerFrame } from './VisualizerEngine';
import { getMusicVisualizerFrame, getSymphonyVisualizerFrame } from './SymphonyEngine';

export interface RGB {
  r: number;
  g: number;
  b: number;
}


export type PatternId = number; // 1 through 61

// ─── PATTERN REGISTRY ─────────────────────────────────────────────────────────
// Per Master Reference §1 SK8LytzTemplate schema. This is the SSOT for all
// pattern metadata. UI components import from here — NOT from CustomEffects.ts.


export type ColorMode = 'FG_BG' | 'FG_ONLY' | 'GENERATIVE';


export interface SK8LytzTemplate {
  id: number;                   // Unique. Never reuse. 1-28=P1B, 29-61=P1A ge.*
  name: string;                 // User-facing label in picker
  icon: string;                 // Emoji for picker card
  colorMode: ColorMode;         // Canonical: 'FG_BG' | 'FG_ONLY' | 'GENERATIVE'
  requiresForeground: boolean;  // Derived: colorMode !== 'GENERATIVE' — show FG picker
  requiresBackground: boolean;  // Derived: colorMode === 'FG_BG'     — show BG picker
  supportsDirection: boolean;   // Show direction toggle in UI?
  supportsSegment: boolean;     // Segment-mirroring compatible?
  tier: 1 | 2 | 3;             // 1=ge.* reversal, 2=Programs reversal, 3=SK8Lytz original
  group: string;                // UI grouping label in picker
  sourceRef?: string;           // e.g. 'ge.OceanWaveEffect' or 'Programs:CometChase'
}


export const SK8LYTZ_TEMPLATES: SK8LytzTemplate[] = [
  // ── GROUP 1: SOLID & STATIC (0x59 FREEZE) ───────────────────────────────
  { id: 1 ,  name: 'Solid',              icon: '⬛', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 2, group: 'Solid' },
  { id: 2 ,  name: 'Split Colors',       icon: '🔲', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Solid' },
  { id: 3 ,  name: 'Trisection',         icon: '🟦', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Solid' },
  { id: 4 ,  name: 'Quartered',          icon: '🔳', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Solid' },
  { id: 5 ,  name: 'Center Accent',      icon: '🎯', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Solid' },

  // ── GROUP 2: CHASES & METEORS (0x59 CASCADE) ────────────────────────────
  { id: 6 ,  name: 'Single Dot Chase',   icon: '💫', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase', sourceRef: 'Programs:DotChase' },
  { id: 7 ,  name: 'Double Dot Chase',icon: '↔️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase' },
  { id: 8 ,  name: 'Comet Chase',        icon: '☄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase', sourceRef: 'Programs:CometChase' },
  { id: 9 ,  name: 'Meteor Shower',      icon: '🌠', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase' },

  // ── GROUP 3: MARQUEES & BANDS (0x59 CASCADE) ────────────────────────────
  { id: 10, name: 'Micro Ants',         icon: '🐜', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },
  { id: 11, name: 'Theater Chase',      icon: '🎭', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },
  { id: 12, name: 'Dashed Marquee',     icon: '➖', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },
  { id: 13, name: 'Bold Stripes',       icon: '🟥', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },

  // ── GROUP 4: MATH WAVES (0x59 CASCADE) ──────────────────────────────────
  { id: 14, name: 'Sine Pulse Wave',    icon: '〰️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Wave' },
  { id: 15, name: 'Wave Pinch',         icon: '🌊', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Wave' },
  { id: 16, name: 'Breathing Wave',     icon: '💨', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Breathe' },

  // ── GROUP 5: TEMPORAL FULL-STRIP (0x51 STEP_GRADUAL/JUMP) ───────────────
  { id: 17, name: 'Smooth Breath',      icon: '🫁', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe', sourceRef: 'ge.BreathEffect' },

  // ── GROUP 5b: WIPE / FILL (0x59 CASCADE) ────────────────────────────────
  { id: 18, name: 'Wipe / Fill',        icon: '▶️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Marquee' },

  // ── GROUP 6: GENERATIVE RAINBOW (0x59 CASCADE HSV MATH) ─────────────────
  { id: 19, name: 'True Rainbow Flow',  icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 20, name: 'Rainbow Marquee',    icon: '🎆', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 21, name: 'Rainbow Comet',      icon: '🌠', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 22, name: 'Cyberpunk Shift',    icon: '🤖', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },

  // ── GROUP 7: ge.* PHASE 1A REVERSALS (0x59 CASCADE) ─────────────────────
  { id: 23, name: 'Color Flow',         icon: '🎨', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Rainbow', sourceRef: 'ge.ColorFlowEffect' },
  { id: 24, name: 'Color Breathing',    icon: '🫧', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe', sourceRef: 'ge.BreathEffect' },
  { id: 25, name: 'Running Water',      icon: '💧', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Chase', sourceRef: 'ge.RunningWaterEffect' },
  { id: 26, name: 'Strobe Flash',       icon: '⚡', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle', sourceRef: 'ge.StrobeEffect' },
  { id: 27, name: 'Ocean Wave',         icon: '🌊', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.OceanWaveEffect' },
  { id: 28, name: 'Lightning Strike',   icon: '🌩️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle', sourceRef: 'ge.LightningEffect' },
  { id: 29, name: 'Snowfall',           icon: '❄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: false, tier: 1, group: 'Chase', sourceRef: 'ge.SnowfallEffect' },
  { id: 30, name: 'Heartbeat Pulse',    icon: '❤️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe', sourceRef: 'ge.HeartbeatEffect' },
  { id: 31, name: 'Meteor',             icon: '☄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Chase', sourceRef: 'ge.MeteorEffect' },
  { id: 32, name: 'Aurora Borealis',    icon: '🌌', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Rainbow', sourceRef: 'ge.AuroraEffect' },
  { id: 33, name: 'Lava Lamp',          icon: '🫠', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.LavaEffect' },
  { id: 34, name: 'Plasma Wave',        icon: '🔮', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.PlasmaEffect' },
  { id: 35, name: 'Star Cluster',       icon: '✨', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Sparkle', sourceRef: 'ge.StarClusterEffect' },
  { id: 36, name: 'Rainbow Breathing',  icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: false, supportsSegment: false, tier: 3, group: 'Breathe' },
  { id: 37, name: 'Crystal Shimmer',    icon: '💎', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Sparkle' },
  { id: 38, name: 'Gradient Chase',     icon: '🌅', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Chase' },
  { id: 39, name: 'Fire Flame',         icon: '🔥', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Wave' },
  { id: 40, name: 'Neon Pulse',         icon: '💜', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Breathe' },
  { id: 41, name: 'Rainbow Chaser',     icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 42, name: 'Matrix Rain',        icon: '🟩', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Chase' },
  { id: 43, name: 'Starlight',          icon: '🌟', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Sparkle' },

  // ── GROUP 8: STREET MODES (HIDDEN FROM NORMAL PICKER) ───────────────────
  { id: 101, name: 'Street Stopped',      icon: '🛑', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 102, name: 'Street Cruising',     icon: '🚘', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 103, name: 'Street Braking',      icon: '🚨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 104, name: 'Street Slowing',      icon: '⚠️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 105, name: 'Street Accelerating', icon: '💨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },

  // ── GROUP 9: NATIVE TEMPORAL (0x51 COMPACT) ────────────────────────────
  // (Note: Smooth Breath (17), Wipe/Fill (18), Strobe Flash (26), SK8Lytz Signature (44) natively intercept via 0x51 in buildPatternPayload)
  { id: 44, name: 'SK8Lytz Signature',  icon: '🛹', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Signature' },
  { id: 72, name: 'Center-Out Marquee',   icon: '🎆', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Marquee' },

  // ── GROUP 10: NATIVE 0x41 PARITY (Test) ──
  { id: 201, name: 'Large Scroll',      icon: '🌊', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 202, name: 'Gradient Chunk',    icon: '✨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 203, name: 'Single Dot Chase',  icon: '💫', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 204, name: 'Ping-Pong Fill',    icon: '↔️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 205, name: 'Ping-Pong Dot',     icon: '🎾', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 206, name: 'Marching Ants',     icon: '🐜', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 207, name: 'Smooth Breath',     icon: '💨', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 208, name: '3-Color Breath',    icon: '🫁', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 209, name: 'Rainbow Breath',    icon: '🌈', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 210, name: '3-Color Jump',      icon: '🐸', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 211, name: '7-Color Breathing', icon: '💓', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 212, name: 'Rainbow Crossfade', icon: '🌌', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 213, name: 'Rainbow Jump',      icon: '🦘', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 214, name: 'Irregular Strobe',  icon: '⚡', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 215, name: '3-Color Strobe',    icon: '📸', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 216, name: 'Rainbow Strobe',    icon: '💥', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 217, name: 'Comet Chase',       icon: '☄️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 218, name: 'Comet Chase II',    icon: '🌠', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 219, name: 'Fast Dot Chase',    icon: '🏎️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 220, name: 'Static Gradient',   icon: '🌅', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 221, name: 'Multi-Comet Flow',  icon: '💫', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 222, name: 'Rainbow Wipe',      icon: '🧹', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 223, name: 'Rainbow Sweep',     icon: '🖌️', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 224, name: 'Tetris Stacker',    icon: '🧱', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 225, name: 'Fading Chunks',     icon: '🧬', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 226, name: 'Center-In Wipe',    icon: '🗜️', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 227, name: 'Large Multi-Comet', icon: '🏄', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 228, name: 'Fire Flame',        icon: '🔥', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 229, name: 'Rainbow Block',     icon: '🧱', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 230, name: 'Center Fill Cycle', icon: '🪀', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 231, name: 'Custom Marquee',    icon: '🦓', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 232, name: 'Glitch Marquee',    icon: '👾', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' },
  { id: 233, name: 'Rainbow Stream',    icon: '🛤️', colorMode: 'FG_BG', requiresForeground: false, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Test' }
];

// ─── MATH HELPERS ─────────────────────────────────────────────────────────────


export interface PatternOptions {
  distribution?: [number, number, number]; // [tail, cruise, head]
  segments?: number;
}


export function buildMultiColorPayload(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1,
  brightness: number = 100,
  options?: PatternOptions,
  hardwareLedPoints?: number
): number[] | null {
  const pixels = getHardwarePixelArray(patternId, fg, bg, numLEDs, options);
  if (!pixels) return null;

  // Bake brightness into the pixel array before sending.
  // 0x37 global brightness is unreliable while 0x59 engine is active.
  const brt = Math.max(0, Math.min(100, brightness)) / 100;
  const scaledPixels = brt >= 1.0 ? pixels : pixels.map(p => ({
    r: Math.round(p.r * brt),
    g: Math.round(p.g * brt),
    b: Math.round(p.b * brt),
  }));

  // Pass hardwareLedPoints separately so the 0x59 footer correctly encodes the
  // physical strip length — decoupled from the BLE-safe pixel array cap (MAX_PIXELS=54).
  const transitionType = getPatternTransitionType(patternId);
  return ZenggeProtocol.setMultiColor(scaledPixels, hardwareLedPoints ?? numLEDs, speed, direction, transitionType);
}

/**
 * Master dispatcher — ALL patterns use 0x59. No 0x51.
 * commandType is determined by getPatternTransitionType() for each ID.
 *
 * @param numLEDs           - Pixel array math size (capped at MAX_PIXELS internally).
 * @param hardwareLedPoints - Actual physical LED count. Encoded in the 0x59 footer
 *                            so the hardware scales the pattern to the full strip.
 *                            If omitted, falls back to numLEDs (backwards-compatible).
 */

export function buildPatternPayload(
  patternId: number,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1,
  brightness: number = 100,
  options?: PatternOptions,
  hardwareLedPoints?: number
): number[] | null {
  // ── GROUP 10: NATIVE 0x41 TEMPORAL INTERCEPTION ──
  // The 33 native hardware effects (IDs 201-233) must use 0x41.
  if (patternId >= 201 && patternId <= 233) {
    const hwDirection = direction === 1 ? 0 : 1; // 1 (UI fwd) -> 0 (HW fwd). 0 (UI rev) -> 1 (HW rev)
    const hardwareModeId = patternId - 200; // 201 maps to mode 1
    return ZenggeProtocol.setSettledMode(hardwareModeId, fg, bg, speed, hwDirection);
  }

  // ── GROUP 9: NATIVE 0x51 INTERCEPTION ──
  // The named legacy interception effects (17, 18, 24, 26, 44, 72)
  const is0x51Target = [17, 18, 24, 26, 44, 72].includes(patternId);
  if (is0x51Target) {
    let modeId = 1; // Default to Breathe
    if (patternId === 17 || patternId === 24) modeId = 1; // Change gradually (Breathe)
    if (patternId === 18) modeId = direction === 1 ? 5 : 6; // Running, 1point from start to end (Sweep/Wipe)
    if (patternId === 26) modeId = 4; // Strobe-flash
    if (patternId === 44) modeId = 26; // ZENGGE HW Mode 26: 7-Color Center-In Fill (But accepts custom colors via 0x51!)
    if (patternId === 72) modeId = direction === 1 ? 7 : 8; // Running, from middle to both ends (Center-Out)

    // PER PROTOCOL BIBLE: 0xA3 hardware strictly requires 9B setCustomModeCompact.
    // setCustomModeExtended (10B format) is condemned on 0xA3 as it fails to animate.
    return ZenggeProtocol.setCustomModeCompact([{
      mode: modeId,
      speed,
      color1: fg,
      color2: bg
    }]);
  }

  return buildMultiColorPayload(patternId, fg, bg, numLEDs, speed, direction, brightness, options, hardwareLedPoints);
}

// ─── MUSIC MODE VISUALIZER ────────────────────────────────────────────────────

/**
 * Private helpers for music mode — migrated from RbmSimulator.ts [BATCH:P1].
 */

export { getVisualizerFrame, getMusicVisualizerFrame, getSymphonyVisualizerFrame };

