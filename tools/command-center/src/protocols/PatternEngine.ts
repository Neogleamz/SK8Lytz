/**
 * SK8Lytz Pattern Engine (Math Synthesizer)
 *
 * Source of truth for all SK8LYTZ_TEMPLATES — registry + math builders + dispatch.
 * Synthesizes dynamic pixel arrays based on math parameters instead of legacy firmware IDs.
 */
import { getVisualizerFrame } from './VisualizerEngine';
export interface RGB {
  r: number;
  g: number;
  b: number;
}


export type PatternId = number; // 1 through 61

// ─── PATTERN REGISTRY ─────────────────────────────────────────────────────────
// Per Master Reference §1 SK8LytzTemplate schema. This is the SSOT for all
// pattern metadata. UI components import from here — NOT from CustomEffects.ts.


export type ColorMode = 'FG_BG' | 'FG_ONLY' | 'BG_ONLY' | 'GENERATIVE';


export interface SK8LytzTemplate {
  id: number;                   // Unique. Never reuse. 1-28=P1B, 29-61=P1A ge.*
  name: string;                 // User-facing label in picker
  icon: string;                 // Emoji for picker card
  colorMode: ColorMode;         // Canonical: 'FG_BG' | 'FG_ONLY' | 'BG_ONLY' | 'GENERATIVE'
  requiresForeground: boolean;  // Derived: colorMode !== 'GENERATIVE' — show FG picker
  requiresBackground: boolean;  // Derived: colorMode === 'FG_BG'     — show BG picker
  supportsDirection: boolean;   // Show direction toggle in UI?
  supportsSegment: boolean;     // Segment-mirroring compatible?
  tier: 1 | 2 | 3;             // 1=ge.* reversal, 2=Programs reversal, 3=SK8Lytz original
  group: string;                // UI grouping label in picker
  isHidden?: boolean;           // If true, hide from UI picker
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
  { id: 17, name: 'Smooth Breath',      icon: '🫁', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: false, tier: 1, group: 'N/A', isHidden: true, sourceRef: 'ge.BreathEffect' },

  // ── GROUP 5b: WIPE / FILL (0x59 CASCADE) ────────────────────────────────
  { id: 18, name: 'Wipe / Fill',        icon: '▶️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'N/A', isHidden: true },

  // ── GROUP 6: GENERATIVE RAINBOW (0x59 CASCADE HSV MATH) ─────────────────
  { id: 19, name: 'True Rainbow Flow',  icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 20, name: 'Rainbow Marquee',    icon: '🎆', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 21, name: 'Rainbow Comet',      icon: '🌠', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 22, name: 'Cyberpunk Shift',    icon: '🤖', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },

  // ── GROUP 7: ge.* PHASE 1A REVERSALS (0x59 CASCADE) ─────────────────────
  { id: 23, name: 'Color Flow',         icon: '🎨', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'SK8Lytz', sourceRef: 'ge.ColorFlowEffect' },
  { id: 24, name: 'Color Breathing',    icon: '🫧', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'N/A', isHidden: true, sourceRef: 'ge.BreathEffect' },
  { id: 25, name: 'Running Water',      icon: '💧', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Chase', sourceRef: 'ge.RunningWaterEffect' },
  { id: 26, name: 'Strobe Flash',       icon: '⚡', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'N/A', isHidden: true, sourceRef: 'ge.StrobeEffect' },
  { id: 27, name: 'Ocean Wave',         icon: '🌊', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.OceanWaveEffect' },
  { id: 28, name: 'Lightning Strike',   icon: '🌩️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle', sourceRef: 'ge.LightningEffect' },
  { id: 29, name: 'Snowfall',           icon: '❄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: false, tier: 1, group: 'Chase', sourceRef: 'ge.SnowfallEffect' },
  { id: 30, name: 'Heartbeat Pulse',    icon: '❤️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'N/A', isHidden: true, sourceRef: 'ge.HeartbeatEffect' },
  { id: 31, name: 'Meteor',             icon: '☄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Chase', sourceRef: 'ge.MeteorEffect' },
  { id: 32, name: 'Aurora Borealis',    icon: '🌌', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'SK8Lytz', sourceRef: 'ge.AuroraEffect' },
  { id: 33, name: 'Lava Lamp',          icon: '🫠', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.LavaEffect' },
  { id: 34, name: 'Plasma Wave',        icon: '🔮', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.PlasmaEffect' },
  { id: 35, name: 'Star Cluster',       icon: '✨', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Sparkle', sourceRef: 'ge.StarClusterEffect' },
  { id: 36, name: 'Rainbow Breathing',  icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: false, supportsSegment: false, tier: 3, group: 'N/A', isHidden: true },
  { id: 37, name: 'Crystal Shimmer',    icon: '💎', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Sparkle' },
  { id: 38, name: 'Gradient Chase',     icon: '🌅', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Chase' },
  { id: 39, name: 'Fire Flame',         icon: '🔥', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Wave' },
  { id: 40, name: 'Neon Pulse',         icon: '💜', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'N/A', isHidden: true },
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
  { id: 72, name: 'Center-Out Marquee',   icon: '🎆', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 3, group: 'N/A', isHidden: true },

  // ── GROUP 10: NATIVE 0x41 PARITY (Test) ──
  { id: 201, name: 'Large Scroll',      icon: '🌊', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 202, name: 'Gradient Chunk',    icon: '✨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 203, name: 'Single Dot Chase',  icon: '💫', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 204, name: 'Ping-Pong Fill',    icon: '↔️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 205, name: 'Ping-Pong Dot',     icon: '🎾', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 206, name: 'Marching Ants',     icon: '🐜', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 207, name: 'Smooth Breath',     icon: '💨', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 208, name: '3-Color Breath',    icon: '🫁', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 209, name: 'Rainbow Breath',    icon: '🌈', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 210, name: '3-Color Jump',      icon: '🐸', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 211, name: '7-Color Breathing', icon: '💓', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 212, name: 'Rainbow Crossfade', icon: '🌌', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 213, name: 'Rainbow Jump',      icon: '🦘', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 214, name: 'Irregular Strobe',  icon: '⚡', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 215, name: '3-Color Strobe',    icon: '📸', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 216, name: 'Rainbow Strobe',    icon: '💥', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 217, name: 'Comet Chase',       icon: '☄️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 218, name: 'Comet Chase II',    icon: '🌠', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 219, name: 'Fast Dot Chase',    icon: '🏎️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 220, name: 'Static Gradient',   icon: '🌅', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 221, name: 'Multi-Comet Flow',  icon: '💫', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 222, name: 'Rainbow Wipe',      icon: '🧹', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 223, name: 'Rainbow Sweep',     icon: '🖌️', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 224, name: 'Tetris Stacker',    icon: '🧱', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'SK8Lytz' },
  { id: 225, name: 'Fading Chunks',     icon: '🧬', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 226, name: 'Center-In Wipe',    icon: '🗜️', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'SK8Lytz' },
  { id: 227, name: 'Large Multi-Comet', icon: '🏄', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 228, name: 'Fire Flame',        icon: '🔥', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'SK8Lytz' },
  { id: 229, name: 'Rainbow Block',     icon: '🧱', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 230, name: 'Center Fill Cycle', icon: '🪀', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 231, name: 'Custom Marquee',    icon: '🦓', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Marquee' },
  { id: 232, name: 'Glitch Marquee',    icon: '👾', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Marquee' },
  { id: 233, name: 'Rainbow Stream',    icon: '🛤️', colorMode: 'BG_ONLY', requiresForeground: false, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' }
];

// Hardware building removed for Web UI

export interface PatternOptions {
  distribution?: [number, number, number]; // [tail, cruise, head]
  segments?: number;
}

// ─── MUSIC MODE VISUALIZER ────────────────────────────────────────────────────

/**
 * Private helpers for music mode — migrated from RbmSimulator.ts [BATCH:P1].
 */

export { getVisualizerFrame };

