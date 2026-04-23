export interface CustomEffectMetadata {
  id: number;
  name: string;
  requiresForeground: boolean;
  requiresBackground: boolean;
  supportsSegment: boolean;
  supportsDirection: boolean;
}

export const SK8LYTZ_TEMPLATES: CustomEffectMetadata[] = [
  // ── GROUP 1: SOLID & STATIC (0x59 Freeze) ──
  { id: 1, name: "Solid", requiresForeground: true, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 2, name: "Split Colors", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 3, name: "Trisection", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 4, name: "Quartered", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 5, name: "Center Accent", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },

  // ── GROUP 2: CHASES & METEORS (0x59 Cascade) ──
  { id: 6, name: "Single Dot Chase", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 7, name: "Reflected Dot Chase", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 8, name: "Comet Chase", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 9, name: "Meteor Shower", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  
  // ── GROUP 3: MARQUEES & BANDS (0x59 Cascade) ──
  { id: 10, name: "Micro Ants", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 11, name: "Theater Chase", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 12, name: "Dashed Marquee", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 13, name: "Barber Pole", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 14, name: "Bold Stripes", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },

  // ── GROUP 4: MATH WAVES & GRADIENTS (0x59 Cascade) ──
  { id: 15, name: "Sine Pulse Wave", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 16, name: "Wave Pinch", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 17, name: "Breathing Wave", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 18, name: "Center-Out Comet", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 19, name: "Center-Out Marquee", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },

  // ── GROUP 5: TEMPORAL FULL-STRIP (0x51 Native / 0x59 Freeze hacks) ──
  { id: 20, name: "Smooth Breath", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 21, name: "Hard Jump Flash", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 22, name: "Strobe", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 23, name: "Wipe / Fill (Start to End)", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 24, name: "Wipe / Fill (Center Out)", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },

  // ── GROUP 6: GENERATIVE RAINBOWS & TRI-COLOR (0x59 Cascade Math) ──
  { id: 25, name: "True Rainbow Flow", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 26, name: "Rainbow Marquee", requiresForeground: false, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 27, name: "Rainbow Comet", requiresForeground: false, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 28, name: "Cyberpunk Shift", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },

  // ── GROUP 7: NATIVE HARDWARE REVERSALS (Phase 1A) ──
  { id: 29, name: "Color Flow", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 30, name: "Color Breathing", requiresForeground: true, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 31, name: "Color Jump", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 32, name: "Running Water", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 33, name: "Strobe Flash", requiresForeground: true, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 34, name: "Color Wipe", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 35, name: "Fireworks", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 36, name: "Ocean Wave", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 37, name: "Lightning Strike", requiresForeground: true, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 38, name: "Snowfall", requiresForeground: true, requiresBackground: true, supportsSegment: false, supportsDirection: false },
  { id: 39, name: "Candle Flicker", requiresForeground: true, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 40, name: "Heartbeat Pulse", requiresForeground: true, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 41, name: "Meteor", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 42, name: "Aurora Borealis", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 43, name: "Lava Lamp", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 44, name: "Plasma Wave", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 45, name: "Star Cluster", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 46, name: "Police Lights", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: false },
  { id: 47, name: "Rainbow Breathing", requiresForeground: false, requiresBackground: false, supportsSegment: false, supportsDirection: false },
  { id: 48, name: "Color Burst", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 49, name: "Twinkle", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 50, name: "Crystal Shimmer", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: false },
  { id: 51, name: "Gradient Chase", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 52, name: "Comet Duo", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 53, name: "Fire Flame", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 54, name: "Cyber Glitch", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: false },
  { id: 55, name: "Neon Pulse", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 56, name: "Rainbow Chaser", requiresForeground: false, requiresBackground: false, supportsSegment: true, supportsDirection: true },
  { id: 57, name: "Matrix Rain", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: true },
  { id: 58, name: "Sparkle Fade", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 59, name: "Dual Scan", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 60, name: "Starlight", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
  { id: 61, name: "Hyperspace", requiresForeground: true, requiresBackground: true, supportsSegment: true, supportsDirection: false },
];
