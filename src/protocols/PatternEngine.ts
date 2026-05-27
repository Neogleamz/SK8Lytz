/**
 * SK8Lytz Pattern Engine (Math Synthesizer & Dispatcher)
 *
 * SINGLE SOURCE OF TRUTH for all SK8LYTZ_TEMPLATES — registry + math builders + dispatch.
 * DO NOT import SK8LYTZ_TEMPLATES from anywhere else. CustomEffects.ts is DELETED.
 *
 * Hardware animation model:
 *   - The IC strip controller animates NATIVELY once a 0x41 or 0x59 packet is sent.
 *   - 0x41 (Settled Mode): Hardware natively animates 33 highly-complex dual-color effects. (Groups 1-7)
 *   - 0x59 (Static Colorful): Hardware freezes or scrolls an array we send. (Group 8: Street Modes)
 * 
 * ==============================================================================
 * GROUP TAXONOMY & HARDWARE DISPATCH MAP  (IDs 1–105)
 * ==============================================================================
 *
 *   ┌─────────────────────────────────────────────────────────────────────┐
 *   │  PRO EFFECTS — NATIVE PARITY MODES (IDs 1–33)                      │
 *   ├──────────┬──────────────────────────────┬───────────────────────────┤
 *   │ Group 1-7│ 33 Native Hardware Effects   │ 0x41 Settled Mode         │
 *   │          │ (e.g. Ping-Pong, Ants,       │ Hardware processes        │
 *   │          │  Rainbow Wipe, etc.)         │ animation natively.       │
 *   └──────────┴──────────────────────────────┴───────────────────────────┘
 *
 *   ┌─────────────────────────────────────────────────────────────────────┐
 *   │  CUSTOM FALLBACK MODES (IDs 101–105)                                │
 *   ├──────────┬──────────────────────────────┬───────────────────────────┤
 *   │ Group 8  │ Street Modes                 │ 0x59 cmd=0x01/0x02        │
 *   │          │ (Cruising, Braking, Stopped) │ Math Synthesizer array    │
 *   └──────────┴──────────────────────────────┴───────────────────────────┘
 *
 *   Dispatch entry points:
 *     Hardware:   buildPatternPayload() → 0x41 (1-33) OR 0x59 (101-105)
 *     Visualizer: getVisualizerFrame() (ProductVisualizer.tsx + CustomEffectVisualizer.tsx)
 *
 *   0x59 commandType per group (APK-PROVEN StaticColorfulMode.java):
 *     0x01 Static   (freeze - e.g. Stopped/Cruising)
 *     0x02 Running  (continuous scroll - e.g. Braking/Accelerating)
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
  // ── 0x41 SETTLED MODE PRO EFFECTS (1-33) ──
  { id: 1 , name: 'Large Scroll',      icon: '🌊', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 2 , name: 'Gradient Chunk',    icon: '✨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 3 , name: 'Single Dot Chase',  icon: '💫', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 4 , name: 'Ping-Pong Fill',    icon: '↔️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Wave' },
  { id: 5 , name: 'Ping-Pong Dot',     icon: '☄️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 6 , name: 'Marching Ants',     icon: '🐜', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Marquee' },
  { id: 7 , name: 'Smooth Breath',     icon: '🫁', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 8 , name: '3-Color Breath',    icon: '❤️', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 9 , name: 'Rainbow Breath',    icon: '🌈', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 10, name: '3-Color Jump',      icon: '⚡', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 11, name: 'Rainbow Pulse',     icon: '🌟', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 12, name: 'Rainbow Crossfade', icon: '🌈', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe' },
  { id: 13, name: 'Rainbow Jump',      icon: '⚡', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 14, name: 'Irregular Strobe',  icon: '🌩️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 15, name: '3-Color Strobe',    icon: '✨', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 16, name: 'Rainbow Strobe',    icon: '🎆', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle' },
  { id: 17, name: 'Comet Chase',       icon: '🌠', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 18, name: 'Meteor Shower',     icon: '☄️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 19, name: 'Fast Dot Chase',    icon: '💨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 20, name: 'Static Gradient',   icon: '🌅', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 21, name: 'Multi-Comet Flow',  icon: '🌌', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 22, name: 'Rainbow Wipe',      icon: '▶️', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Wave' },
  { id: 23, name: 'Rainbow Sweep',     icon: '⏩', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Wave' },
  { id: 24, name: 'Tetris Stacker',    icon: '🧱', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Marquee' },
  { id: 25, name: 'Alternating Comet', icon: '💞', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 26, name: 'Center-In Wipe',    icon: '🎯', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Wave' },
  { id: 27, name: 'Rainbow Wave',      icon: '🌊', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 28, name: 'Fire Flame',        icon: '🔥', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Chase' },
  { id: 29, name: 'Rainbow Block',     icon: '🟩', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },
  { id: 30, name: 'Ping-Pong Center',  icon: '🔀', colorMode: 'GENERATIVE', requiresForeground: false, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 1, group: 'Wave' },
  { id: 31, name: 'Stripe Scroll',     icon: '🦓', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Marquee' },
  { id: 32, name: 'Glitch Marquee',    icon: '👾', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Marquee' },
  { id: 33, name: 'Rainbow Stream',    icon: '🛤️', colorMode: 'FG_BG', requiresForeground: false, requiresBackground: true, supportsDirection: true, supportsSegment: false, tier: 1, group: 'Rainbow' },

  // ── GROUP 8: STREET MODES (HIDDEN FROM NORMAL PICKER) ───────────────────
  { id: 101, name: 'Street Stopped',      icon: '🛑', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 102, name: 'Street Cruising',     icon: '🚘', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 103, name: 'Street Braking',      icon: '🚨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 104, name: 'Street Slowing',      icon: '⚠️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 105, name: 'Street Accelerating', icon: '💨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' }
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

  const brt = Math.max(0, Math.min(100, brightness)) / 100;
  const scaledPixels = brt >= 1.0 ? pixels : pixels.map(p => ({
    r: Math.round(p.r * brt),
    g: Math.round(p.g * brt),
    b: Math.round(p.b * brt),
  }));

  const transitionType = getPatternTransitionType(patternId);
  return ZenggeProtocol.setMultiColor(scaledPixels, hardwareLedPoints ?? numLEDs, speed, direction, transitionType);
}

/**
 * Master dispatcher for SK8LYTZ_TEMPLATES.
 * - IDs 1-33: Natively map to 0x41 Settled Mode, using hardware animation.
 * - IDs 101-105: Route to 0x59 Static Colorful for Street Mode custom logic.
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
  // ── Native 0x41 Hardware Routing ──
  if (patternId >= 1 && patternId <= 33) {
    const hwDirection = direction === 1 ? 0 : 1; // 1 (UI fwd) -> 0 (HW fwd). 0 (UI rev) -> 1 (HW rev)
    return ZenggeProtocol.setSettledMode(patternId, fg, bg, speed, hwDirection);
  }

  // ── 0x59 Fallback for Street Modes (101-105) ──
  return buildMultiColorPayload(patternId, fg, bg, numLEDs, speed, direction, brightness, options, hardwareLedPoints);
}

// ─── MUSIC MODE VISUALIZER ────────────────────────────────────────────────────

/**
 * Private helpers for music mode — migrated from RbmSimulator.ts [BATCH:P1].
 */

export { getVisualizerFrame, getMusicVisualizerFrame, getSymphonyVisualizerFrame };

