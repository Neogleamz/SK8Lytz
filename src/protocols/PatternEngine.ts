/**
 * SK8Lytz Pattern Engine (Math Synthesizer)
 *
 * SINGLE SOURCE OF TRUTH for all 61 SK8LYTZ_TEMPLATES — registry + math builders + dispatch.
 * DO NOT import SK8LYTZ_TEMPLATES from anywhere else. CustomEffects.ts is DELETED.
 * Synthesizes dynamic pixel arrays based on math parameters instead of legacy firmware IDs.
 *
 * Hardware animation model:
 *   - The IC strip controller animates NATIVELY once a 0x59 or 0x51 packet is sent.
 *   - 0x59 commandType=0x01 (Static):  Hardware freezes the array in place (Group 1)
 *   - 0x59 commandType=0x02 (Running): Hardware continuously scrolls the array (Groups 2-4, 5b, 6, 7)
 *   - 0x51 Custom Mode              : Hardware crossfades temporal states (Group 5a)
 *   APK source: StaticColorfulMode.java — commandType 1=Static,2=Running,3=Strobe,4=Jump,5=Breathe,6=Twinkly
 * 
 * ==============================================================================
 * GROUP TAXONOMY & HARDWARE DISPATCH MAP  (IDs 1–61, all routes 0x59/0x51)
 * ==============================================================================
 *
 *   ┌─────────────────────────────────────────────────────────────────────┐
 *   │  PHASE 1B — Programs Reversal & SK8Lytz Originals (IDs 1–28)       │
 *   ├──────────┬──────────────────────────────┬───────────────────────────┤
 *   │ Group 1  │ Solid & Static   (IDs  1– 5) │ 0x59 cmd=0x01  STATIC    │
 *   │          │  1-Solid  2-Split  3-Tri      │                           │
 *   │          │  4-Quartered  5-CenterAccent  │                           │
 *   ├──────────┼──────────────────────────────┼───────────────────────────┤
 *   │ Group 2  │ Chases & Meteors (IDs  6– 9) │ 0x59 cmd=0x02  RUNNING   │
 *   │          │  6-DotChase  7-ReflectedDot   │                           │
 *   │          │  8-CometChase  9-MeteorShower │                           │
 *   ├──────────┼──────────────────────────────┼───────────────────────────┤
 *   │ Group 3  │ Marquees & Bands (IDs 10–14) │ 0x59 cmd=0x02  RUNNING   │
 *   │          │  10-MicroAnts  11-Theater     │                           │
 *   │          │  12-Dashed  13-Barber  14-Bold│                           │
 *   ├──────────┼──────────────────────────────┼───────────────────────────┤
 *   │ Group 4  │ Math Waves       (IDs 15–19) │ 0x59 cmd=0x02  RUNNING   │
 *   │          │  15-SinePulse  16-WavePinch   │ (18/19 center-split)     │
 *   │          │  17-Breathing  18-CoOut        │                           │
 *   │          │  19-CenterOutMarquee           │                           │
 *   ├──────────┼──────────────────────────────┼───────────────────────────┤
 *   │ Group 5a │ Temporal Fades   (IDs 20–22) │ 0x51  STEP_GRADUAL/JUMP  │
 *   │          │  20-SmoothBreath 21-HardJump   │ Hardware crossfade       │
 *   │          │  22-Strobe                     │                           │
 *   ├──────────┼──────────────────────────────┼───────────────────────────┤
 *   │ Group 5b │ Wipe / Fill      (IDs 23–24) │ 0x59 cmd=0x02  RUNNING   │
 *   │          │  23-WipeFill  24-WipeCenterOut│                           │
 *   ├──────────┼──────────────────────────────┼───────────────────────────┤
 *   │ Group 6  │ Generative Rainbow(IDs 25–28)│ 0x59 cmd=0x02  RUNNING   │
 *   │          │  25-RainbowFlow  26-RainbowMq │ HSV math, no FG/BG       │
 *   │          │  27-RainbowComet 28-Cyberpunk │                           │
 *   └──────────┴──────────────────────────────┴───────────────────────────┘
 *
 *   ┌─────────────────────────────────────────────────────────────────────┐
 *   │  PHASE 1A — ge.* Java Class Reversals (IDs 29–61)                  │
 *   ├──────────┬──────────────────────────────┬───────────────────────────┤
 *   │ Group 7  │ ge.* Reversals   (IDs 29–61) │ 0x59 cmd=0x02  RUNNING   │
 *   │          │  29-ColorFlow    (ge.ColorFlowEffect)                    │
 *   │          │  30-ColorBreath  (ge.BreathEffect)                       │
 *   │          │  31-ColorJump    (ge.JumpEffect)                         │
 *   │          │  32-RunningWater (ge.RunningWaterEffect)                 │
 *   │          │  33-StrobeFlash  (ge.StrobeEffect)                       │
 *   │          │  34-ColorWipe    (ge.ColorWipeEffect)                    │
 *   │          │  35-Fireworks    (ge.FireworksEffect)                    │
 *   │          │  36-OceanWave    (ge.OceanWaveEffect)                    │
 *   │          │  37-Lightning    (ge.LightningEffect)                    │
 *   │          │  38-Snowfall     (ge.SnowfallEffect)                     │
 *   │          │  39-Candle       (ge.CandleEffect)                       │
 *   │          │  40-Heartbeat    (ge.HeartbeatEffect)                    │
 *   │          │  41-Meteor       (ge.MeteorEffect)                       │
 *   │          │  42-Aurora       (ge.AuroraEffect)                       │
 *   │          │  43-LavaLamp     (ge.LavaEffect)                         │
 *   │          │  44-PlasmaWave   (ge.PlasmaEffect)                       │
 *   │          │  45-StarCluster  (ge.StarClusterEffect)                  │
 *   │          │  46-PoliceLights  47-RainbowBreathing                    │
 *   │          │  48-ColorBurst    49-Twinkle                             │
 *   │          │  50-CrystalShimmer 51-GradientChase                      │
 *   │          │  52-CometDuo      53-FireFlame                           │
 *   │          │  54-CyberGlitch   55-NeonPulse                           │
 *   │          │  56-RainbowChaser 57-MatrixRain                          │
 *   │          │  58-SparkleFade   59-DualScan                            │
 *   │          │  60-Starlight     61-Hyperspace                          │
 *   └──────────┴──────────────────────────────────────────────────────────┘
 *
 *   Dispatch entry points:
 *     Hardware:   buildPatternPayload() → buildMultiColorPayload() → 0x59 (ALL patterns)
 *     Visualizer: getVisualizerFrame() (ProductVisualizer.tsx + CustomEffectVisualizer.tsx)
 *
 *   commandType per group (APK-PROVEN StaticColorfulMode.java):
 *     IDs 1–5  → 0x01 Static   (freeze)
 *     IDs 20   → 0x05 Breathe  (hardware pulses)
 *     IDs 21   → 0x04 Jump     (hardware hard-jump)
 *     IDs 22   → 0x03 Strobe   (hardware flash)
 *     All else → 0x02 Running  (continuous scroll)
 */

import { ZenggeProtocol } from './ZenggeProtocol';

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
  { id: 1,  name: 'Solid',              icon: '⬛', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 2, group: 'Static' },
  { id: 2,  name: 'Split Colors',       icon: '🔲', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Static' },
  { id: 3,  name: 'Trisection',         icon: '🟦', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Static' },
  { id: 4,  name: 'Quartered',          icon: '🔳', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Static' },
  { id: 5,  name: 'Center Accent',      icon: '🎯', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Static' },

  // ── GROUP 2: CHASES & METEORS (0x59 CASCADE) ────────────────────────────
  { id: 6,  name: 'Single Dot Chase',   icon: '💫', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase', sourceRef: 'Programs:DotChase' },
  { id: 7,  name: 'Double Dot Chase',icon: '↔️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase' },
  { id: 8,  name: 'Comet Chase',        icon: '☄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase', sourceRef: 'Programs:CometChase' },
  { id: 9,  name: 'Meteor Shower',      icon: '🌠', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Chase' },

  // ── GROUP 3: MARQUEES & BANDS (0x59 CASCADE) ────────────────────────────
  { id: 10, name: 'Micro Ants',         icon: '🐜', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },
  { id: 11, name: 'Theater Chase',      icon: '🎭', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },
  { id: 12, name: 'Dashed Marquee',     icon: '➖', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 2, group: 'Marquee' },  { id: 14, name: 'Bold Stripes',       icon: '🟥', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 2, group: 'Marquee' },

  // ── GROUP 4: MATH WAVES (0x59 CASCADE) ──────────────────────────────────
  { id: 15, name: 'Sine Pulse Wave',    icon: '〰️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Wave' },
  { id: 16, name: 'Wave Pinch',         icon: '🌊', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Wave' },
  { id: 17, name: 'Breathing Wave',     icon: '💨', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Breathe' },
  // ── GROUP 5: TEMPORAL FULL-STRIP (0x51 STEP_GRADUAL/JUMP) ───────────────
  { id: 20, name: 'Smooth Breath',      icon: '🫁', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe', sourceRef: 'ge.BreathEffect' },
  // ── GROUP 5b: WIPE / FILL (0x59 CASCADE) ────────────────────────────────
  { id: 23, name: 'Wipe / Fill',        icon: '▶️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Marquee' },
  // ── GROUP 6: GENERATIVE RAINBOW (0x59 CASCADE HSV MATH) ─────────────────
  { id: 25, name: 'True Rainbow Flow',  icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 26, name: 'Rainbow Marquee',    icon: '🎆', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 27, name: 'Rainbow Comet',      icon: '🌠', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 28, name: 'Cyberpunk Shift',    icon: '🤖', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },

  // ── GROUP 7: ge.* PHASE 1A REVERSALS (0x59 CASCADE) ─────────────────────
  { id: 29, name: 'Color Flow',         icon: '🎨', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Rainbow', sourceRef: 'ge.ColorFlowEffect' },
  { id: 30, name: 'Color Breathing',    icon: '🫧', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe', sourceRef: 'ge.BreathEffect' },  { id: 32, name: 'Running Water',      icon: '💧', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Chase', sourceRef: 'ge.RunningWaterEffect' },
  { id: 33, name: 'Strobe Flash',       icon: '⚡', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle & Flash', sourceRef: 'ge.StrobeEffect' },  { id: 36, name: 'Ocean Wave',         icon: '🌊', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.OceanWaveEffect' },
  { id: 37, name: 'Lightning Strike',   icon: '🌩️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Sparkle & Flash', sourceRef: 'ge.LightningEffect' },
  { id: 38, name: 'Snowfall',           icon: '❄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: false, tier: 1, group: 'Chase', sourceRef: 'ge.SnowfallEffect' },
  { id: 39, name: 'Candle Flicker',     icon: '🕯️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Static', sourceRef: 'ge.CandleEffect' },
  { id: 40, name: 'Heartbeat Pulse',    icon: '❤️', colorMode: 'FG_ONLY',  requiresForeground: true,  requiresBackground: false,  supportsDirection: false, supportsSegment: false, tier: 1, group: 'Breathe', sourceRef: 'ge.HeartbeatEffect' },
  { id: 41, name: 'Meteor',             icon: '☄️', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Chase', sourceRef: 'ge.MeteorEffect' },
  { id: 42, name: 'Aurora Borealis',    icon: '🌌', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Rainbow', sourceRef: 'ge.AuroraEffect' },
  { id: 43, name: 'Lava Lamp',          icon: '🫠', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.LavaEffect' },
  { id: 44, name: 'Plasma Wave',        icon: '🔮', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 1, group: 'Wave', sourceRef: 'ge.PlasmaEffect' },
  { id: 45, name: 'Star Cluster',       icon: '✨', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 1, group: 'Sparkle & Flash', sourceRef: 'ge.StarClusterEffect' },  { id: 47, name: 'Rainbow Breathing',  icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: false, supportsSegment: false, tier: 3, group: 'Breathe' },  { id: 50, name: 'Crystal Shimmer',    icon: '💎', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Sparkle & Flash' },
  { id: 51, name: 'Gradient Chase',     icon: '🌅', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Chase' },  { id: 53, name: 'Fire Flame',         icon: '🔥', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Wave' },  { id: 55, name: 'Neon Pulse',         icon: '💜', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Breathe' },
  { id: 56, name: 'Rainbow Chaser',     icon: '🌈', colorMode: 'GENERATIVE',requiresForeground: false, requiresBackground: false,supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Rainbow' },
  { id: 57, name: 'Matrix Rain',        icon: '🟩', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: true,  supportsSegment: true,  tier: 3, group: 'Chase' },  { id: 60, name: 'Starlight',          icon: '🌟', colorMode: 'FG_BG',    requiresForeground: true,  requiresBackground: true,    supportsDirection: false, supportsSegment: true,  tier: 3, group: 'Sparkle & Flash' },
  // ── GROUP 8: STREET MODES (HIDDEN FROM NORMAL PICKER) ───────────────────
  { id: 101, name: 'Street Stopped',      icon: '🛑', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 102, name: 'Street Cruising',     icon: '🚘', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 103, name: 'Street Braking',      icon: '🚨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 104, name: 'Street Slowing',      icon: '⚠️', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
  { id: 105, name: 'Street Accelerating', icon: '💨', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: true, tier: 3, group: 'Street' },
];

// ─── MATH HELPERS ─────────────────────────────────────────────────────────────

function dim(c: RGB, factor: number): RGB {
  return {
    r: Math.round(c.r * factor),
    g: Math.round(c.g * factor),
    b: Math.round(c.b * factor),
  };
}

function lerpRGB(c1: RGB, c2: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(c1.r + (c2.r - c1.r) * t),
    g: Math.round(c1.g + (c2.g - c1.g) * t),
    b: Math.round(c1.b + (c2.b - c1.b) * t),
  };
}

function hueToRGB(hue: number): RGB {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return { r: Math.round(f(5) * 255), g: Math.round(f(3) * 255), b: Math.round(f(1) * 255) };
}

function blendRGB(a: RGB, b: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return {
    r: Math.round(a.r * t + b.r * (1 - t)),
    g: Math.round(a.g * t + b.g * (1 - t)),
    b: Math.round(a.b * t + b.b * (1 - t)),
  };
}

function hsvToRgb(h: number, s: number, v: number): RGB {
  const i = Math.floor(h * 6);
  const f = h * 6 - i;
  const p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
  const [r, g, b] = [
    [v, q, p, p, t, v], [t, v, v, q, p, p], [p, p, t, v, v, q]
  ].map(ch => ch[i % 6]);
  return { r: Math.round(r * 255), g: Math.round(g * 255), b: Math.round(b * 255) };
}

// ─── GROUP 7 BUILDERS (ge.* HARDWARE-NATIVE EFFECTS) ────────────────────────────

function buildColorFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phaseOffset = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phaseOffset) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}

function buildColorBreathing(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = (Math.sin(tick * Math.PI * 2) * 0.5 + 0.5);
  const color = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  return Array(numLEDs).fill(color);
}

function buildColorJump(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const color = tick < 0.5 ? fg : bg;
  return Array(numLEDs).fill(color);
}

function buildRunningWater(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const BLOCK = 3;
  const offset = Math.floor((direction === 0 ? tick : 1 - tick) * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i + offset) % numLEDs;
    return pos < BLOCK ? fg : bg;
  });
}

function buildStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const on = (tick * 10) % 1 < 0.3;
  const color = on ? fg : { r: 0, g: 0, b: 0 };
  return Array(numLEDs).fill(color);
}

function buildColorWipe(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const fillCount = Math.floor(t * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}

function buildFireworks(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const seed = Math.floor(tick * 8);
  const frame = Array(numLEDs).fill(bg);
  const center = Math.abs(Math.sin(seed * 1.618) * numLEDs) | 0;
  const spreadRadius = Math.floor((tick * 8 % 1) * (numLEDs / 4));
  for (let i = 0; i < spreadRadius; i++) {
    const brightness = 1 - (i / spreadRadius);
    const c = blendRGB(fg, bg, brightness);
    const posL = Math.max(0, center - i);
    const posR = Math.min(numLEDs - 1, center + i);
    if (posL >= 0) frame[posL] = c;
    if (posR < numLEDs) frame[posR] = c;
  }
  return frame;
}

function buildOceanWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave1 = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 2);
    const wave2 = Math.sin(i / numLEDs * Math.PI * 6 + phase * Math.PI * 3 + 0.7);
    const combined = (wave1 + wave2) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}

function buildLightning(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const flashSeed = Math.floor(tick * 20);
  const frame: RGB[] = Array(numLEDs).fill({ r: 0, g: 0, b: 0 });
  for (let j = 0; j < 3; j++) {
    const pos = Math.abs(Math.sin((flashSeed + j) * 2.718) * numLEDs) | 0;
    const brightness = Math.abs(Math.cos((flashSeed + j) * 1.414));
    frame[pos] = { r: Math.round(fg.r * brightness), g: Math.round(fg.g * brightness), b: Math.round(fg.b * brightness) };
  }
  return frame;
}

function buildSnowfall(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let j = 0; j < 5; j++) {
    const speed = 0.3 + j * 0.15;
    const pos = Math.floor(((tick * speed + j * 0.2) % 1) * numLEDs);
    const brightness = 0.6 + Math.sin(tick * 5 + j) * 0.4;
    frame[pos] = blendRGB(fg, bg, brightness);
    if (pos + 1 < numLEDs) frame[pos + 1] = blendRGB(fg, bg, brightness * 0.3);
  }
  return frame;
}

function buildCandle(fg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(tick * 7 + i * 1.3) * Math.cos(tick * 11 + i * 0.7);
    const brightness = 0.5 + noise * 0.3 + Math.sin(tick * 3) * 0.2;
    const b = Math.max(0, Math.min(1, brightness));
    return { r: Math.round(fg.r * b), g: Math.round(fg.g * b * 0.7), b: Math.round(fg.b * b * 0.3) };
  });
}

function buildHeartbeat(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const t = tick % 1;
  let brightness: number;
  if (t < 0.1) brightness = Math.sin((t / 0.1) * Math.PI);
  else if (t < 0.3) brightness = Math.sin(((t - 0.15) / 0.15) * Math.PI) * 0.6;
  else brightness = 0;
  const b = Math.max(0, brightness);
  return Array(numLEDs).fill({ r: Math.round(fg.r * b), g: Math.round(fg.g * b), b: Math.round(fg.b * b) });
}

function buildMeteor(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.25);
  const head = Math.floor((direction === 0 ? tick : 1 - tick) * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist > TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 2));
  });
}

function buildAurora(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = (i / numLEDs * 0.4 + phase * 0.5 + 0.5) % 1;
    const brightness = (Math.sin(i / numLEDs * Math.PI * 3 + phase * Math.PI * 4) * 0.4 + 0.6);
    return hsvToRgb(hue, 0.8, brightness);
  });
}

function buildLava(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const blob1 = Math.sin(i / numLEDs * Math.PI * 2 + tick * Math.PI * 1.3);
    const blob2 = Math.sin(i / numLEDs * Math.PI * 3.7 + tick * Math.PI * 0.8 + 1.2);
    const combined = (blob1 + blob2) * 0.5 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}

function buildPlasma(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const v = Math.sin(i / numLEDs * Math.PI * 4 + phase * Math.PI * 6) * 
              Math.cos(i / numLEDs * Math.PI * 2 - phase * Math.PI * 2);
    return blendRGB(fg, bg, Math.abs(v));
  });
}

function buildStarCluster(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let j = 0; j < 4; j++) {
    const center = Math.floor(((tick * 0.5 + j * 0.25) % 1) * numLEDs);
    for (let i = -2; i <= 2; i++) {
      const pos = center + i;
      if (pos >= 0 && pos < numLEDs) {
        const brightness = 1 - Math.abs(i) / 3;
        frame[pos] = blendRGB(fg, frame[pos], brightness);
      }
    }
  }
  return frame;
}

function buildPoliceLights(numLEDs: number, tick: number): RGB[] {
  const red = { r: 255, g: 0, b: 0 };
  const blue = { r: 0, g: 0, b: 255 };
  const flashPhase = (tick * 8) % 1;
  const isOn = flashPhase < 0.5;
  const isRedTurn = (tick * 2) % 1 < 0.5;
  
  return Array.from({ length: numLEDs }, (_, i) => {
    if (!isOn) return { r: 0, g: 0, b: 0 };
    const isLeftHalf = i < numLEDs / 2;
    if (isRedTurn) return isLeftHalf ? red : { r: 0, g: 0, b: 0 };
    return !isLeftHalf ? blue : { r: 0, g: 0, b: 0 };
  });
}

function buildRainbowBreathing(numLEDs: number, tick: number): RGB[] {
  const hue = tick % 1;
  const brightness = (Math.sin(tick * Math.PI * 4) * 0.5 + 0.5);
  const baseColor = hsvToRgb(hue, 1.0, 1.0);
  return Array(numLEDs).fill({
    r: Math.round(baseColor.r * brightness),
    g: Math.round(baseColor.g * brightness),
    b: Math.round(baseColor.b * brightness)
  });
}

function buildColorBurst(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const burstPhase = tick % 1;
  const center = Math.floor(numLEDs / 2);
  const radius = Math.floor(burstPhase * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = Math.abs(i - center);
    if (dist < radius && dist > radius - 3) return fg;
    return bg;
  });
}

function buildTwinkle(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let i = 0; i < numLEDs; i++) {
    const noise = Math.sin(i * 12.9898 + tick * 10) * 43758.5453;
    const val = noise - Math.floor(noise);
    if (val > 0.95) {
       const brightness = Math.sin(tick * 20 + i) * 0.5 + 0.5;
       frame[i] = blendRGB(fg, bg, brightness);
    }
  }
  return frame;
}

function buildCrystalShimmer(numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise = Math.sin(i * 7.1 + tick * 15) * 0.5 + 0.5;
    const brightness = Math.pow(noise, 3);
    return {
      r: Math.round(150 * brightness),
      g: Math.round(200 * brightness),
      b: Math.round(255 * brightness)
    };
  });
}

function buildGradientChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const pos = (i / numLEDs + phase) % 1;
    return blendRGB(fg, bg, Math.sin(pos * Math.PI));
  });
}

function buildCometDuo(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.2);
  const head1 = Math.floor(tick * (numLEDs + TAIL)) - TAIL;
  const head2 = Math.floor((1 - tick) * (numLEDs + TAIL)) - TAIL;
  
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist1 = head1 - i;
    const dist2 = i - head2;
    
    let c = bg;
    if (dist1 >= 0 && dist1 <= TAIL) {
      c = blendRGB(fg, c, 1 - (dist1 / TAIL));
    }
    if (dist2 >= 0 && dist2 <= TAIL) {
      c = blendRGB(fg, c, 1 - (dist2 / TAIL)); 
    }
    return c;
  });
}

function buildFireFlame(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const noise1 = Math.sin(i * 0.5 + tick * 15);
    const noise2 = Math.sin(i * 1.3 - tick * 22 + 4.5);
    const noise3 = Math.sin(i * 3.7 + tick * 33);
    const flicker = (noise1 + noise2 + noise3) / 3 * 0.5 + 0.5;
    const heightGradient = 1 - (i / numLEDs); 
    const brightness = flicker * heightGradient * 1.5;
    return blendRGB(fg, bg, Math.min(1, brightness));
  });
}

function buildCyberGlitch(numLEDs: number, tick: number): RGB[] {
  const c1 = { r: 0, g: 255, b: 255 };
  const c2 = { r: 255, g: 0, b: 255 };
  const frame = Array(numLEDs).fill({ r: 0, g: 0, b: 0 });
  const glitchBlockSize = Math.floor(Math.sin(tick * 50) * 4) + 1;
  const glitchPos = Math.floor(Math.sin(tick * 80) * numLEDs + numLEDs) % numLEDs;
  for (let i = 0; i < glitchBlockSize; i++) {
    if (glitchPos + i < numLEDs) {
      frame[glitchPos + i] = Math.random() > 0.5 ? c1 : c2;
    }
  }
  return frame;
}

function buildNeonPulse(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  let pulse = Math.pow(Math.sin(tick * Math.PI * 2), 4);
  if (tick % 1 < 0.1) pulse = Math.random() * 0.5 + 0.5;
  return Array(numLEDs).fill(blendRGB(fg, bg, pulse));
}

function buildRainbowChaser(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) * 3 + phase * 2) % 1.0;
    const brightness = Math.sin(i * 0.5 + phase * 10) * 0.5 + 0.5;
    const c = hsvToRgb(hue, 1.0, 1.0);
    return dim(c, brightness);
  });
}

function buildMatrixRain(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  const frame = Array(numLEDs).fill(bg);
  const tailLength = Math.max(3, Math.floor(numLEDs / 4));
  for (let d = 0; d < 3; d++) {
    const dropPhase = (phase + d * 0.33) % 1;
    const head = Math.floor(dropPhase * (numLEDs + tailLength)) - tailLength;
    for (let i = 0; i <= tailLength; i++) {
      const pos = head + i;
      if (pos >= 0 && pos < numLEDs) {
        const brightness = i / tailLength;
        frame[pos] = blendRGB(fg, frame[pos], Math.pow(brightness, 2));
      }
    }
  }
  return frame;
}

function buildSparkleFade(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  for (let i = 0; i < numLEDs; i++) {
    const noise = Math.sin(i * 45.2 + tick * 2.1) * 43758.5453;
    const rand = noise - Math.floor(noise);
    if (rand > 0.98) frame[i] = fg;
    else if (rand > 0.90) frame[i] = blendRGB(fg, bg, 0.5);
  }
  return frame;
}

function buildDualScan(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const phase = (Math.sin(tick * Math.PI * 2) * 0.5 + 0.5);
  const pos1 = Math.floor(phase * (numLEDs - 1));
  const pos2 = numLEDs - 1 - pos1;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist1 = Math.abs(i - pos1);
    const dist2 = Math.abs(i - pos2);
    if (dist1 === 0 || dist2 === 0) return fg;
    if (dist1 === 1 || dist2 === 1) return blendRGB(fg, bg, 0.4);
    return bg;
  });
}

function buildStarlight(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const twinkle = Math.sin(tick * 5 + i * 2.3) * Math.sin(tick * 2.1 + i * 1.1);
    const brightness = Math.max(0, twinkle);
    return blendRGB(fg, bg, Math.pow(brightness, 3));
  });
}

function buildHyperspace(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const frame = Array(numLEDs).fill(bg);
  const center = Math.floor(numLEDs / 2);
  const speed = 4;
  const phase = (tick * speed) % 1;
  const dist = Math.floor(Math.pow(phase, 2) * center); 
  const p1 = center - dist;
  const p2 = center + dist;
  if (p1 >= 0 && p1 < numLEDs) frame[p1] = fg;
  if (p2 >= 0 && p2 < numLEDs) frame[p2] = fg;
  if (p1 + 1 < center) frame[p1 + 1] = blendRGB(fg, bg, 0.3);
  if (p2 - 1 > center) frame[p2 - 1] = blendRGB(fg, bg, 0.3);
  return frame;
}




// ─── GROUP 1–6 BUILDERS ──────────────────────────────────────────────────────────

// GROUP A: Static Placement
function buildSolid(fg: RGB, numLEDs: number): RGB[] {
  return Array(numLEDs).fill(fg);
}

function buildSplitColors(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const half = Math.floor(numLEDs / 2);
  return [
    ...Array(half).fill(fg),
    ...Array(numLEDs - half).fill(bg),
  ];
}

function buildTrisection(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const third = Math.floor(numLEDs / 3);
  return [
    ...Array(third).fill(fg),
    ...Array(third).fill(bg),
    ...Array(numLEDs - third * 2).fill(fg),
  ];
}

function buildQuartered(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const quarter = Math.floor(numLEDs / 4);
  return [
    ...Array(quarter).fill(fg),
    ...Array(quarter).fill(bg),
    ...Array(quarter).fill(fg),
    ...Array(numLEDs - quarter * 3).fill(bg),
  ];
}

function buildCenterAccent(fg: RGB, bg: RGB, numLEDs: number): RGB[] {
  const accentSize = Math.max(1, Math.floor(numLEDs * 0.25));
  const sideSize = Math.floor((numLEDs - accentSize) / 2);
  return [
    ...Array(sideSize).fill(bg),
    ...Array(accentSize).fill(fg),
    ...Array(numLEDs - sideSize - accentSize).fill(bg),
  ];
}

// GROUP B: Chases & Meteors
function buildSingleDotChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const pos = Math.floor(t * numLEDs) % numLEDs;
  return Array.from({ length: numLEDs }, (_, i) => i === pos ? fg : bg);
}

function buildReflectedDotChase(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Two dots start at opposite ends and meet in the middle
  const posA = Math.floor(tick * (numLEDs / 2)) % Math.ceil(numLEDs / 2);
  const posB = numLEDs - 1 - posA;
  return Array.from({ length: numLEDs }, (_, i) =>
    (i === posA || i === posB) ? fg : bg
  );
}

function buildCometChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.max(3, Math.floor(numLEDs * 0.2));
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return bg;
    const brightness = 1 - (dist / TAIL);
    return blendRGB(fg, bg, Math.pow(brightness, 1.5));
  });
}

function buildMeteorShower(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const METEOR_COUNT = 3;
  const TAIL = Math.floor(numLEDs * 0.15);
  const frame = Array(numLEDs).fill(bg);
  for (let m = 0; m < METEOR_COUNT; m++) {
    const offset = (m / METEOR_COUNT); // stagger start
    const t = direction === 0 ? (tick + offset) % 1 : 1 - ((tick + offset) % 1);
    const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
    for (let i = 0; i < numLEDs; i++) {
      const dist = head - i;
      if (dist >= 0 && dist < TAIL) {
        const brightness = 1 - (dist / TAIL);
        frame[i] = blendRGB(fg, frame[i], Math.pow(brightness, 2));
      }
    }
  }
  return frame;
}

// GROUP C: Marquees & Bands
function buildMicroAnts(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * 2) % 2; // alternates every half-tick
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % 2 === 0 ? fg : bg);
}

function buildTheaterChase(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // every 3rd LED lights
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD === 0 ? fg : bg);
}

function buildDashedMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const DASH = 4; const GAP = 3; // 4 on, 3 off
  const PERIOD = DASH + GAP;
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => (i + offset) % PERIOD < DASH ? fg : bg);
}

function buildBarberPole(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(2, Math.floor(numLEDs / 6));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2);
  return Array.from({ length: numLEDs }, (_, i) => {
    // Diagonal stripe: position + offset mod period
    return (i + offset) % (STRIPE * 2) < STRIPE ? fg : bg;
  });
}

function buildBoldStripes(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const STRIPE = Math.max(3, Math.floor(numLEDs / 5));
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * STRIPE * 2) % (STRIPE * 2); // scroll one full period
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.floor((i + offset) / STRIPE) % 2 === 0 ? fg : bg
  );
}

// ---------------------------------------------
// Group D: Math Waves (IDs 15-19)
// ---------------------------------------------

function buildSinePulseWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 4 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}

function buildWavePinch(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / (numLEDs - 1);
    const waveL = Math.sin(posNorm * Math.PI * 3 + tick * Math.PI * 2);
    const waveR = Math.sin((1 - posNorm) * Math.PI * 3 + tick * Math.PI * 2);
    const combined = (waveL + waveR) / 2 * 0.5 + 0.5;
    return blendRGB(fg, bg, combined);
  });
}

function buildBreathingWave(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const wave = Math.sin((i / numLEDs) * Math.PI * 2 + phase * Math.PI * 2) * 0.5 + 0.5;
    return blendRGB(fg, bg, wave);
  });
}

function buildCenterOutComet(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const TAIL = Math.floor(numLEDs * 0.15);
  const head = Math.floor(tick * (center + TAIL));
  return Array.from({ length: numLEDs }, (_, i) => {
    // Mirror: two comets going from center to edges
    const distL = head - (center - i);
    const distR = head - (i - center);
    const dist = Math.min(Math.abs(distL), Math.abs(distR));
    if (i < center) {
      return (head - (center - i) >= 0 && head - (center - i) < TAIL)
        ? blendRGB(fg, bg, 1 - dist / TAIL)
        : bg;
    } else {
      return (head - (i - center) >= 0 && head - (i - center) < TAIL)
        ? blendRGB(fg, bg, 1 - dist / TAIL)
        : bg;
    }
  });
}

function buildCenterOutMarquee(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  const PERIOD = 6;
  const offset = Math.floor(tick * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    const distFromCenter = Math.abs(i - center);
    return (distFromCenter + offset) % PERIOD < 3 ? fg : bg;
  });
}

// ---------------------------------------------
// Group E: Temporal (IDs 20-24)
// ---------------------------------------------

function buildSmoothBreath(fg: RGB, numLEDs: number, tick: number): RGB[] {
  const brightness = Math.sin(tick * Math.PI) ** 2; // smooth power curve
  return Array(numLEDs).fill({
    r: Math.round(fg.r * brightness),
    g: Math.round(fg.g * brightness),
    b: Math.round(fg.b * brightness),
  });
}

function buildHardJumpFlash(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  // Hard binary alternation - no crossfade
  return Array(numLEDs).fill(tick < 0.5 ? fg : bg);
}

function buildTemporalStrobe(fg: RGB, numLEDs: number, tick: number): RGB[] {
  // High-frequency flash (10x per cycle)
  const on = (tick * 10) % 1 < 0.2; // 20% duty cycle = sharp strobe
  return Array(numLEDs).fill(on ? fg : { r: 0, g: 0, b: 0 });
}

function buildWipeFill(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const t = direction === 0 ? tick : 1 - tick;
  // Fill from one end, then wipe back - forward half fills, back half wipes
  const progress = t < 0.5 ? t * 2 : (1 - t) * 2;
  const fillCount = Math.floor(progress * numLEDs);
  return Array.from({ length: numLEDs }, (_, i) => i < fillCount ? fg : bg);
}

function buildWipeCenterOut(fg: RGB, bg: RGB, numLEDs: number, tick: number): RGB[] {
  const center = Math.floor(numLEDs / 2);
  // progress from 0 to 1 back to 0
  const progress = tick < 0.5 ? tick * 2 : (1 - tick) * 2;
  const radius = Math.floor(progress * center);
  return Array.from({ length: numLEDs }, (_, i) =>
    Math.abs(i - center) <= radius ? fg : bg
  );
}

// ---------------------------------------------
// Group F: Generative / Rainbow (IDs 25-28)
// ---------------------------------------------

function buildTrueRainbowFlow(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const hue = ((i / numLEDs) + phase) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}

function buildRainbowMarquee(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const PERIOD = 3; // grouped blocks with rainbow hue
  const t = direction === 0 ? tick : 1 - tick;
  const offset = Math.floor(t * PERIOD) % PERIOD;
  return Array.from({ length: numLEDs }, (_, i) => {
    if ((i + offset) % PERIOD !== 0) return { r: 0, g: 0, b: 0 }; // gap
    const hue = (i / numLEDs + t) % 1.0;
    return hsvToRgb(hue, 1.0, 1.0);
  });
}

function buildRainbowComet(numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const TAIL = Math.floor(numLEDs * 0.3);
  const t = direction === 0 ? tick : 1 - tick;
  const head = Math.floor(t * (numLEDs + TAIL)) - TAIL;
  return Array.from({ length: numLEDs }, (_, i) => {
    const dist = head - i;
    if (dist < 0 || dist >= TAIL) return { r: 0, g: 0, b: 0 };
    const brightness = 1 - (dist / TAIL);
    const hue = (i / numLEDs + t) % 1.0; // rainbow position along strip
    return hsvToRgb(hue, 1.0, brightness);
  });
}

function buildCyberpunkShift(fg: RGB, bg: RGB, numLEDs: number, tick: number, direction: 0 | 1): RGB[] {
  const phase = direction === 0 ? tick : 1 - tick;
  return Array.from({ length: numLEDs }, (_, i) => {
    const posNorm = i / numLEDs;
    // Sharp digital glitch band scrolling
    const band = (posNorm + phase * 2) % 1;
    if (band < 0.08) return fg; // bright FG slice
    if (band < 0.12) return { r: 255, g: 255, b: 255 }; // white edge flash
    // Sub-bands of bg with slight brightness modulation
    const bgBrightness = 0.3 + Math.sin(posNorm * Math.PI * 8 + phase * Math.PI * 4) * 0.2;
    return blendRGB(bg, { r: 0, g: 0, b: 0 }, bgBrightness);
  });
}

// ─── GENERATORS ───────────────────────────────────────────────────────────────

export interface PatternOptions {
  distribution?: [number, number, number]; // [tail, cruise, head]
  segments?: number;
}

function buildStreetMode(patternId: PatternId, fg: RGB, bg: RGB, n: number, tick: number, options?: PatternOptions): RGB[] {
  const segments = Math.max(1, options?.segments || 1);
  const physicalLength = n * segments;

  const dist = options?.distribution || [0.3, 0.4, 0.3];
  const tailCount = Math.max(1, Math.round(n * dist[0]));
  const headCount = Math.max(1, Math.round(n * dist[2]));
  const midCount = Math.max(1, n - tailCount - headCount);
  
  const tailColor = fg; // Brake/Tail
  const cruiseColor = bg; // Cruise dot
  const headColor = { r: 255, g: 255, b: 255 }; // Headlights
  
  const rightSide: RGB[] = [];

  if (patternId === 101 || patternId === 104) {
    // STOPPED (101) or SLOWING DOWN (104)
    const dimBrake = { r: Math.round(tailColor.r * 0.5), g: Math.round(tailColor.g * 0.5), b: Math.round(tailColor.b * 0.5) };
    const tailPixels = Array(tailCount).fill(patternId === 104 ? dimBrake : tailColor);
    const midColor = patternId === 104 ? { r: 255, g: 100, b: 0 } : { r: 0, g: 0, b: 0 };
    const midPixels = Array(midCount).fill(midColor);
    const headPixels = Array(headCount).fill(headColor);
    rightSide.push(...tailPixels, ...midPixels, ...headPixels);
    
  } else if (patternId === 103) {
    // HARD BRAKING (103)
    const tailPixels = Array(tailCount).fill(tailColor);
    const midPixels = Array(midCount).fill(tailColor);
    const headPixels = Array(headCount).fill(headColor);
    rightSide.push(...tailPixels, ...midPixels, ...headPixels);
    
  } else {
    // CRUISING (102) or ACCELERATING (105)
    const dimBrake = { r: Math.round(tailColor.r * 0.5), g: Math.round(tailColor.g * 0.5), b: Math.round(tailColor.b * 0.5) };
    const tailPixels = Array(tailCount).fill(dimBrake);
    const headPixels = Array(headCount).fill(headColor);
    
    // Bouncing dot in the gap using tick (0 to 2)
    const bouncePhase = tick <= 1 ? tick : 2 - tick; // 0 to 1 to 0
    const dotPos = Math.round(bouncePhase * (midCount - 1));
    
    const dimCruise = { r: Math.round(cruiseColor.r * 0.2), g: Math.round(cruiseColor.g * 0.2), b: Math.round(cruiseColor.b * 0.2) };
    const midPixels = Array.from({ length: midCount }, (_, i) => {
      const d = Math.abs(i - dotPos);
      if (d === 0) return cruiseColor;
      if (d === 1) return { r: Math.round(cruiseColor.r * 0.6), g: Math.round(cruiseColor.g * 0.6), b: Math.round(cruiseColor.b * 0.6) };
      return dimCruise;
    });
    
    rightSide.push(...tailPixels, ...midPixels, ...headPixels);
  }

  // Palindrome bypass logic: If segments > 1 (e.g. HALOZ ring), we build the full physical array
  // by appending the reversed left side. If segments === 1 (e.g. SOULZ Y-split), we just return rightSide.
  if (segments === 1) {
    return rightSide;
  }

  const leftSide = [...rightSide].reverse();
  const fullPayload: RGB[] = [];
  for (let i = 0; i < segments; i++) {
    if (i % 2 === 0) fullPayload.push(...rightSide);
    else fullPayload.push(...leftSide);
  }
  return fullPayload.slice(0, physicalLength);
}

function generateArray(patternId: PatternId, fg: RGB, bg: RGB, n: number, tick: number = 0, direction: 0 | 1 = 1, options?: PatternOptions): RGB[] {
  const arr: RGB[] = Array(n).fill(bg);

  switch (patternId) {
    // ── GROUP 1: SOLID & STATIC ──
    case 1: return buildSolid(fg, n);
    case 2: return buildSplitColors(fg, bg, n);
    case 3: return buildTrisection(fg, bg, n);
    case 4: return buildQuartered(fg, bg, n);
    case 5: return buildCenterAccent(fg, bg, n);

    // ── GROUP 2: CHASES & METEORS ──
    case 6: return buildSingleDotChase(fg, bg, n, tick, direction);
    case 7: return buildReflectedDotChase(fg, bg, n, tick);
    case 8: return buildCometChase(fg, bg, n, tick, direction);
    case 9: return buildMeteorShower(fg, bg, n, tick, direction);

    // ── GROUP 3: MARQUEES & BANDS ──
    case 10: return buildMicroAnts(fg, bg, n, tick, direction);
    case 11: return buildTheaterChase(fg, bg, n, tick, direction);
    case 12: return buildDashedMarquee(fg, bg, n, tick, direction);
    case 13: return buildBarberPole(fg, bg, n, tick, direction);
    case 14: return buildBoldStripes(fg, bg, n, tick, direction);

    // ── GROUP 4: MATH WAVES & GRADIENTS ──
    case 15: return buildSinePulseWave(fg, bg, n, tick, direction);
    case 16: return buildWavePinch(fg, bg, n, tick);
    case 17: return buildBreathingWave(fg, bg, n, tick, direction);
    case 18: return buildCenterOutComet(fg, bg, n, tick);
    case 19: return buildCenterOutMarquee(fg, bg, n, tick);

    // ── GROUP 5: TEMPORAL FULL-STRIP ──
    case 20: return buildSmoothBreath(fg, n, tick);
    case 21: return buildHardJumpFlash(fg, bg, n, tick);
    case 22: return buildTemporalStrobe(fg, n, tick);
    case 23: return buildWipeFill(fg, bg, n, tick, direction);
    case 24: return buildWipeCenterOut(fg, bg, n, tick);

    // ── GROUP 6: GENERATIVE RAINBOWS & TRI-COLOR ──
    case 25: return buildTrueRainbowFlow(n, tick, direction);
    case 26: return buildRainbowMarquee(n, tick, direction);
    case 27: return buildRainbowComet(n, tick, direction);
    case 28: return buildCyberpunkShift(fg, bg, n, tick, direction);

    // ── GROUP 7: ge.* HARDWARE-NATIVE EFFECTS (IDs 29-61) ──
    // All patterns have dedicated tick-based builders — no fallback.
    case 29: return buildColorFlow(n, tick, direction);
    case 30: return buildColorBreathing(fg, n, tick);
    case 31: return buildColorJump(fg, bg, n, tick);
    case 32: return buildRunningWater(fg, bg, n, tick, direction);
    case 33: return buildStrobe(fg, n, tick);
    case 34: return buildColorWipe(fg, bg, n, tick, direction);
    case 35: return buildFireworks(fg, bg, n, tick);
    case 36: return buildOceanWave(fg, bg, n, tick, direction);
    case 37: return buildLightning(fg, n, tick);
    case 38: return buildSnowfall(fg, bg, n, tick);
    case 39: return buildCandle(fg, n, tick);
    case 40: return buildHeartbeat(fg, n, tick);
    case 41: return buildMeteor(fg, bg, n, tick, direction);
    case 42: return buildAurora(n, tick, direction);
    case 43: return buildLava(fg, bg, n, tick);
    case 44: return buildPlasma(fg, bg, n, tick, direction);
    case 45: return buildStarCluster(fg, bg, n, tick);
    case 46: return buildPoliceLights(n, tick);
    case 47: return buildRainbowBreathing(n, tick);
    case 48: return buildColorBurst(fg, bg, n, tick);
    case 49: return buildTwinkle(fg, bg, n, tick);
    case 50: return buildCrystalShimmer(n, tick);
    case 51: return buildGradientChase(fg, bg, n, tick, direction);
    case 52: return buildCometDuo(fg, bg, n, tick);
    case 53: return buildFireFlame(fg, bg, n, tick);
    case 54: return buildCyberGlitch(n, tick);
    case 55: return buildNeonPulse(fg, bg, n, tick);
    case 56: return buildRainbowChaser(n, tick, direction);
    case 57: return buildMatrixRain(fg, bg, n, tick, direction);
    case 58: return buildSparkleFade(fg, bg, n, tick);
    case 59: return buildDualScan(fg, bg, n, tick);
    case 60: return buildStarlight(fg, bg, n, tick);
    case 61: return buildHyperspace(fg, bg, n, tick);

    // ── GROUP 8: STREET MODES ──
    case 101:
    case 102:
    case 103:
    case 104:
    case 105:
      return buildStreetMode(patternId, fg, bg, n, tick, options);

    default:
      return Array(n).fill(fg);
  }
}

/**
 * Rotate a pixel array by `offset` positions (for visualizer scroll simulation).
 */
function rotateArray(arr: RGB[], animTick: number): RGB[] {
  if (arr.length === 0) return arr;
  // Negate animTick so it flows visually correct
  const offset = Math.floor((1 - animTick) * arr.length) % arr.length;
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
  const generated = generateArray(patternId, fg, bg, n, animTick, direction, visualizerOptions);

  // All 61 builders manage their own tick-based animation internally.
  return generated;
}

/**
 * Get the hardware pixel array for a pattern.
 * Used by applyFixedPattern() to build the 0x59 payload.
 */
export function getHardwarePixelArray(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  options?: PatternOptions
): RGB[] | null {
  // All 61 patterns now use 0x59. commandType selects the hardware behavior:
  //   IDs 20-22, 30-31, 33, 37, 39-40, 46-47, 54-55 use Breathe/Jump/Strobe commandTypes.
  //   The pixel array is the color seed; hardware animates it autonomously.
  //
  // Use tick=0.33 for most patterns so the initial hardware seed frame is visually rich.
  // EXCEPTION: Jump (0x04) and Strobe (0x03) patterns — IDs 31, 33, 37, 46, 54.
  // These builders include an isOn phase-gate that can return all-black at tick=0.33.
  // Since hardware handles flash/jump autonomously once the array is received,
  // we need a fully-lit seed. tick=0.0 guarantees all these builders return visible colors.
  const JUMP_STROBE_IDS = new Set([31, 33, 37, 46, 54]);
  const seedTick = JUMP_STROBE_IDS.has(patternId) ? 0.0 : 0.33;
  return generateArray(patternId, fg, bg, Math.max(1, numLEDs), seedTick, 1, options);

}

/**
 * Get the 0x59 transition (commandType) byte for a pattern.
 *
 * APK GROUND TRUTH — StaticColorfulMode.java (ZENGGE_DECOMPILED):
 *   Static(1)  → 0x01 — Freeze array in place
 *   Running(2) → 0x02 — Continuous hardware scroll (ANIMATION)
 *   Strobe(3)  → 0x03 — Strobe flash
 *   Jump(4)    → 0x04 — Hard jump
 *   Breathe(5) → 0x05 — Breathe
 *   Twinkly(6) → 0x06 — Twinkle
 *
 * Previous bug: was returning 0x00 for animated patterns. 0x00 is NOT a valid
 * commandType — it maps to nothing. Hardware received undefined byte → no animation.
 */
export function getPatternTransitionType(patternId: PatternId): number {
  // ── GROUP 1: STATIC (IDs 1–5) ──────────────────────────────────────────────
  if (patternId >= 1 && patternId <= 5) return 0x01; // Static — hardware freezes array

  // ── GROUP 5a: HARDWARE TEMPORAL (IDs 20–22) ────────────────────────────────
  // APK source: StaticColorfulMode.java — verified commandType enum
  if (patternId === 20) return 0x05; // Breathe  — hardware pulses brightness
  if (patternId === 21) return 0x04; // Jump     — hardware hard-cuts between states
  if (patternId === 22) return 0x03; // Strobe   — hardware flashes on/off

  // ── GROUP 7: ge.* BREATHE EFFECTS (IDs 30, 39, 40, 47, 55) ───────────────
  // Hardware receives the pixel array and pulses its brightness autonomously.
  // Visualizer uses buildColorBreathing / buildSmoothBreath / buildRainbowBreathing / buildNeonPulse at animTick.
  if (patternId === 30) return 0x05; // Color Breathing   — FG_ONLY
  if (patternId === 39) return 0x05; // Candle Flicker    — FG_ONLY (breathe ≈ flicker)
  if (patternId === 40) return 0x05; // Heartbeat Pulse   — FG_ONLY (breathe pulse)
  if (patternId === 47) return 0x05; // Rainbow Breathing — GENERATIVE
  if (patternId === 55) return 0x05; // Neon Pulse        — FG_BG (NeonPulse math uses buildNeonPulse)

  // ── GROUP 7: ge.* JUMP EFFECTS (IDs 31, 46, 54) ──────────────────────────
  // Hardware receives the pixel array and hard-cuts states autonomously.
  // Visualizer uses buildColorJump / buildPoliceLights / buildCyberGlitch at animTick.
  if (patternId === 31) return 0x04; // Color Jump        — FG_BG hard cut
  if (patternId === 46) return 0x04; // Police Lights     — GENERATIVE, hardcoded red/blue
  if (patternId === 54) return 0x04; // Cyber Glitch      — GENERATIVE, hardcoded cyan/magenta

  // ── GROUP 7: ge.* STROBE EFFECTS (IDs 33, 37) ────────────────────────────
  // Hardware receives the pixel array and flashes it on/off autonomously.
  // Visualizer uses buildStrobe / buildLightning at animTick.
  if (patternId === 33) return 0x03; // Strobe Flash      — FG_ONLY
  if (patternId === 37) return 0x03; // Lightning Strike  — FG_ONLY (random flash)

  // ── ALL OTHER ge.* SCROLL EFFECTS (IDs 29, 32, 34–36, 38, 41–45, 48–53, 56–61) ──
  // Hardware scrolls the pre-computed pixel array continuously via 0x02 Running.
  // NOTE: Twinkle-style patterns (35, 45, 49, 50, 58, 60) intentionally use 0x02 (Running)
  // because 0x06 (Twinkle) is NOT yet confirmed from APK decompile.
  
  // ── GROUP 8: STREET MODES (101-105) ──────────────────────────────────────
  if (patternId === 101 || patternId === 104) return 0x01; // STOPPED/SLOWING — STATIC
  if (patternId === 103) return 0x02; // HARD BRAKING — FLASH/STROBE
  // CRUISING (102) & ACCELERATING (105) use 0x01 because the hardware cannot 
  // autonomously bounce the dot. It is just a static array we update periodically.
  if (patternId >= 100) return 0x01; 

  return 0x02; // Running — continuous hardware scroll
}

/**
 * Build the full 0x59 hardware command.
 */
export function buildMultiColorPayload(
  patternId: PatternId,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1,
  brightness: number = 100,
  options?: PatternOptions
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

  const transitionType = getPatternTransitionType(patternId);
  return ZenggeProtocol.setMultiColor(scaledPixels, speed, direction, transitionType);
}

/**
 * Master dispatcher — ALL patterns use 0x59. No 0x51.
 * commandType is determined by getPatternTransitionType() for each ID.
 */
export function buildPatternPayload(
  patternId: number,
  fg: RGB,
  bg: RGB,
  numLEDs: number,
  speed: number,
  direction: number = 1,
  brightness: number = 100,
  options?: PatternOptions
): number[] | null {
  return buildMultiColorPayload(patternId, fg, bg, numLEDs, speed, direction, brightness, options);
}

// ─── MUSIC MODE VISUALIZER ────────────────────────────────────────────────────

/**
 * Private helpers for music mode — migrated from RbmSimulator.ts [BATCH:P1].
 */
function hexToRgb(hex: string): RGB {
  const h = hex.replace('#', '');
  return { r: parseInt(h.slice(0, 2), 16) || 0, g: parseInt(h.slice(2, 4), 16) || 0, b: parseInt(h.slice(4, 6), 16) || 0 };
}

function lerpRGBMusic(a: RGB, b: RGB, t: number): RGB {
  t = Math.max(0, Math.min(1, t));
  return { r: Math.round(a.r + (b.r - a.r) * t), g: Math.round(a.g + (b.g - a.g) * t), b: Math.round(a.b + (b.b - a.b) * t) };
}

function getMusicPaletteAt(palette: RGB[], tick: number): RGB {
  if (palette.length === 0) return { r: 0, g: 0, b: 0 };
  return palette[Math.floor(tick * palette.length) % palette.length];
}

function getMusicPaletteSmooth(palette: RGB[], tick: number): RGB {
  if (palette.length <= 1) return palette[0] ?? { r: 0, g: 0, b: 0 };
  const pos = (tick * palette.length) % palette.length;
  return lerpRGBMusic(palette[Math.floor(pos) % palette.length], palette[(Math.floor(pos) + 1) % palette.length], pos - Math.floor(pos));
}

const MUSIC_RAINBOW7: RGB[] = ['#FF0000','#FF6600','#FFFF00','#00FF00','#00FFFF','#0066FF','#AA00FF'].map(hexToRgb);

/**
 * Get a hardware-accurate pixel + opacity array for MUSIC mode patterns 1–13.
 *
 * Audio-reactive: `magnitude` (0.0–1.0) gates intensity, fill depth, and flash thresholds.
 * The visualizer applies `opacities[i]` as per-LED brightness multiplier.
 *
 * Migrated from RbmSimulator.getRbmMusicFrame [BATCH:P1]. PatternEngine is now SSOT.
 *
 * @param musicPatternId - Music pattern ID 1–13
 * @param numLEDs        - LED count for the segment
 * @param animTick       - Animation tick 0.0–1.0 (looping)
 * @param magnitude      - Live audio amplitude 0.0–1.0
 * @param baseColorHex   - User-selected base color e.g. '#FF0000'
 */
export function getMusicVisualizerFrame(
  musicPatternId: number,
  numLEDs: number,
  animTick: number,
  magnitude: number,
  baseColorHex: string
): { pixels: RGB[]; opacities: number[] } {
  const n = Math.max(1, numLEDs);
  const base = hexToRgb(baseColorHex);
  const white: RGB = { r: 255, g: 255, b: 255 };
  const black: RGB = { r: 0, g: 0, b: 0 };
  const mag = Math.max(0, Math.min(1, magnitude));

  const pixels: RGB[] = Array(n).fill(null).map(() => ({ ...base }));
  const opacities: number[] = Array(n).fill(1.0);

  switch (musicPatternId) {
    case 1: { // Soft — whole strip breathe, depth = magnitude
      const breathe = 0.2 + mag * 0.8;
      const color = lerpRGBMusic(black, base, breathe);
      for (let i = 0; i < n; i++) { pixels[i] = { ...color }; opacities[i] = breathe; }
      break;
    }
    case 2: { // Cheerful — sparkle white on peaks
      for (let i = 0; i < n; i++) {
        if (mag > 0.7 && (i + Math.floor(animTick * 7)) % 3 === 0) {
          pixels[i] = { ...white }; opacities[i] = 1.0;
        } else { pixels[i] = { ...base }; opacities[i] = 0.3 + mag * 0.5; }
      }
      break;
    }
    case 3: { // Energy — magnitude-gated strobe flash
      const on = mag > 0.5 && Math.floor(animTick * 10) % 2 === 0;
      const c = getMusicPaletteAt(MUSIC_RAINBOW7, animTick);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = on ? 1.0 : 0.05; }
      break;
    }
    case 4: { // Relax — slow smooth rainbow fade
      const c = getMusicPaletteSmooth(MUSIC_RAINBOW7, animTick * 0.3);
      for (let i = 0; i < n; i++) { pixels[i] = { ...c }; opacities[i] = 0.5 + mag * 0.5; }
      break;
    }
    case 5: { // Passion — overlay expand center→ends; fill level = magnitude
      const center = Math.floor(n / 2);
      const reach = Math.floor(mag * center);
      for (let i = 0; i < n; i++) {
        pixels[i] = { ...base };
        opacities[i] = Math.abs(i - center) <= reach ? 1.0 : 0.05;
      }
      break;
    }
    case 6: { // Brisk — alternating segments jump on beat
      const step = Math.floor(animTick * 4) % 2;
      for (let i = 0; i < n; i++) {
        pixels[i] = { ...base };
        opacities[i] = ((Math.floor(i / 3) % 2) === step) && mag > 0.3 ? 1.0 : 0.1;
      }
      break;
    }
    case 7: { // Rhythm — VU meter bottom→top, fill = magnitude
      for (let i = 0; i < n; i++) {
        const fract = i / n;
        opacities[i] = fract < mag ? 1.0 : 0.05;
        pixels[i] = fract > 0.85 ? { r: 255, g: 0, b: 0 } : fract > 0.65 ? { r: 255, g: 200, b: 0 } : { ...base };
      }
      break;
    }
    case 8: { // Rolling — rainbow flow, magnitude controls opacity
      for (let i = 0; i < n; i++) {
        pixels[i] = getMusicPaletteSmooth(MUSIC_RAINBOW7, ((i / n) + animTick) % 1);
        opacities[i] = 0.2 + mag * 0.8;
      }
      break;
    }
    case 9: { // Flicker — deterministic twinkling dots on magnitude
      for (let i = 0; i < n; i++) {
        const seed = Math.sin(i * 127.1 + animTick * 311.7) * 43758.5;
        opacities[i] = (seed - Math.floor(seed)) < mag ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 10: { // Accumulate — overlay from both ends to center, fill = magnitude
      const reach = Math.floor(mag * (n / 2));
      for (let i = 0; i < n; i++) {
        opacities[i] = Math.min(i, n - 1 - i) < reach ? 1.0 : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 11: { // Shuttle — bright dot bouncing in sync with animTick
      const bouncedPos = animTick < 0.5 ? animTick * 2 : (1 - animTick) * 2;
      const head = Math.floor(bouncedPos * n);
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - head);
        opacities[i] = dist === 0 ? 1.0 : dist === 1 ? 0.4 * mag : 0.05;
        pixels[i] = { ...base };
      }
      break;
    }
    case 12: { // Fireworks — expand from center, burst flash on peak
      const center = Math.floor(n / 2);
      const burst = mag > 0.8;
      const reach = burst ? n : Math.floor(animTick * (n / 2));
      for (let i = 0; i < n; i++) {
        const dist = Math.abs(i - center);
        if (burst) { pixels[i] = { ...white }; opacities[i] = 1.0; }
        else { pixels[i] = { ...base }; opacities[i] = dist <= reach ? (1 - dist / (n / 2)) * mag : 0.02; }
      }
      break;
    }
    case 13: { // Snow — drifting dots falling end→start
      for (let i = 0; i < n; i++) {
        const snowflake = Math.abs(Math.sin(i * 47.3 + Math.floor(animTick * 3) * 1234.5)) > (1 - mag * 0.7);
        pixels[i] = snowflake ? { ...white } : { ...base };
        opacities[i] = snowflake ? 1.0 : 0.1;
      }
      break;
    }
    default: {
      for (let i = 0; i < n; i++) { pixels[i] = { ...base }; opacities[i] = mag; }
    }
  }

  return { pixels, opacities };
}


