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

  // ── GROUP 9: NATIVE TEMPORAL (0x51 EXTENDED) ────────────────────────────
  // (Note: Smooth Breath (17), Wipe/Fill (18), and Strobe Flash (26) natively intercept via 0x51 in buildPatternPayload)
  { id: 72, name: 'Center-Out Marquee',   icon: '🎆', colorMode: 'FG_ONLY', requiresForeground: true, requiresBackground: false, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Marquee' },

  // ── GROUP 10: TEST NATIVE (0x51) ─────────────────────────────────────────
  { id: 201, name: 'Circulate all modes', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 202, name: '7 colors change gradually', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 203, name: '7 colors run in olivary', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 204, name: '7 colors change quickly', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 205, name: '7 colors strobe-flash', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 206, name: '7 colors running, 1 point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 207, name: '7 colors running, multi points from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 208, name: '7 colors overlay, multi points from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 209, name: '7 colors overlay, multi points from the middle to the both ends and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 210, name: '7 colors flow gradually, from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 211, name: 'Fading out run, 7 colors from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 212, name: 'Runs in olivary, 7 colors from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 213, name: 'Fading out run, 7 colors start with white color from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 214, name: 'Run circularly, 7 colors with black background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 215, name: 'Run circularly, 7 colors with red background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 216, name: 'Run circularly, 7 colors with green background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 217, name: 'Run circularly, 7 colors with blue background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 218, name: 'Run circularly, 7 colors with yellow background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 219, name: 'Run circularly, 7 colors with purple background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 220, name: 'Run circularly, 7 colors with cyan background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 221, name: 'Run circularly, 7 colors with white background, 1point from start to end', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 222, name: 'Run circularly, 7 colors with black background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 223, name: 'Run circularly, 7 colors with red background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 224, name: 'Run circularly, 7 colors with green background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 225, name: 'Run circularly, 7 colors with blue background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 226, name: 'Run circularly, 7 colors with yellow background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 227, name: 'Run circularly, 7 colors with purple background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 228, name: 'Run circularly, 7 colors with cyan background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 229, name: 'Run circularly, 7 colors with white background, 1point from end to start', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 230, name: 'Run circularly, 7 colors with black background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 231, name: 'Run circularly, 7 colors with red background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 232, name: 'Run circularly, 7 colors with green background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 233, name: 'Run circularly, 7 colors with blue background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 234, name: 'Run circularly, 7 colors with yellow background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 235, name: 'Run circularly, 7 colors with purple background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 236, name: 'Run circularly, 7 colors with cyan background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 237, name: 'Run circularly, 7 colors with white background, 1point from start to end and return back', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 238, name: 'Run circularly, 7 colors with black background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 239, name: 'Run circularly, 7 colors with red background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 240, name: 'Run circularly, 7 colors with green background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 241, name: 'Run circularly, 7 colors with blue background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 242, name: 'Run circularly, 7 colors with yellow background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 243, name: 'Run circularly, 7 colors with purple background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
  { id: 244, name: 'Run circularly, 7 colors with cyan background, 1point from middle to both ends', icon: '🧪', colorMode: 'FG_BG', requiresForeground: true, requiresBackground: true, supportsDirection: false, supportsSegment: false, tier: 3, group: 'Test' },
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
  // ── GROUP 9: NATIVE TEMPORAL (0x51 INTERCEPTION) ──
  const is0x51Target = [17, 18, 24, 26, 72].includes(patternId) || (patternId >= 201 && patternId <= 244);
  if (is0x51Target) {
    let modeId = 1; // Default to Breathe
    if (patternId === 17 || patternId === 24) modeId = 1; // Change gradually (Breathe)
    if (patternId === 18) modeId = direction === 1 ? 5 : 6; // Running, 1point from start to end (Sweep/Wipe)
    if (patternId === 26) modeId = 4; // Strobe-flash
    if (patternId === 72) modeId = direction === 1 ? 7 : 8; // Running, from middle to both ends (Center-Out)

    if (patternId >= 201 && patternId <= 244) {
      modeId = patternId - 200;
    }

    // Direction for named patterns (17,18,26,72) is encoded via the modeId itself.
    // Direction for TEST group (201-244) is encoded via the modeId pairing (e.g. 5=fwd, 6=rev).
    // We MUST use setCustomModeExtended (10-byte format) to unlock effects 34-44 on 0xA3 hardware.
    // The payload is 323B and is automatically routed through the 0x40 chunker in useBLE.ts.
    return ZenggeProtocol.setCustomModeExtended([{
      mode: modeId,
      speed,
      color1: fg,
      color2: bg,
      dir: 0x80 // Forward + Section Toggle ON (default for testing)
    }]);
  }

  return buildMultiColorPayload(patternId, fg, bg, numLEDs, speed, direction, brightness, options, hardwareLedPoints);
}

// ─── MUSIC MODE VISUALIZER ────────────────────────────────────────────────────

/**
 * Private helpers for music mode — migrated from RbmSimulator.ts [BATCH:P1].
 */

export { getVisualizerFrame, getMusicVisualizerFrame, getSymphonyVisualizerFrame };

