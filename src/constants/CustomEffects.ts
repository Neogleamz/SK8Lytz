export interface CustomEffectMetadata {
  id: number;
  name: string;
  requiresForeground: boolean;
  requiresBackground: boolean;
  supportsSegment: boolean;
  supportsDirection: boolean;
}

// APK-verified effect IDs 1–33 matching hardware SymphonyEffect IDs exactly.
// ID = hardware effectId byte sent in the 0x51 payload. No offset needed.
export const ZENGGE_EFFECTS: CustomEffectMetadata[] = [
  { id: 1,  name: "Change Gradually",                              requiresForeground: true,  requiresBackground: true,  supportsSegment: false, supportsDirection: false },
  { id: 2,  name: "Bright Up & Fade Gradually",                   requiresForeground: true,  requiresBackground: true,  supportsSegment: false, supportsDirection: false },
  { id: 3,  name: "Change Quickly",                               requiresForeground: true,  requiresBackground: true,  supportsSegment: false, supportsDirection: false },
  { id: 4,  name: "Strobe Flash",                                 requiresForeground: true,  requiresBackground: true,  supportsSegment: false, supportsDirection: false },
  { id: 5,  name: "Running, 1pt — Start to End",                  requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 6,  name: "Running, 1pt — End to Start",                  requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 7,  name: "Running, 1pt — Middle to Ends",                requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 8,  name: "Running, 1pt — Ends to Middle",                requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 9,  name: "Overlay — Start to End",                       requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 10, name: "Overlay — End to Start",                       requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 11, name: "Overlay — Middle to Ends",                     requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 12, name: "Overlay — Ends to Middle",                     requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 13, name: "Fading Run, 1pt — Start to End",               requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 14, name: "Fading Run, 1pt — End to Start",               requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 15, name: "Olivary Flow — Start to End",                  requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 16, name: "Olivary Flow — End to Start",                  requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 17, name: "Running w/BG, 1pt — Start to End",             requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 18, name: "Running w/BG, 1pt — End to Start",             requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 19, name: "2-Color Run, Multi — Start to End",            requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 20, name: "2-Color Run, Multi — End to Start",            requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 21, name: "2-Color Fade Alt — Start to End",              requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 22, name: "2-Color Fade Alt — End to Start",              requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 23, name: "2-Color Multi Alt — Start to End",             requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 24, name: "2-Color Multi Alt — End to Start",             requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 25, name: "Fading Out Flow — Start to End",               requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 26, name: "Fading Out Flow — End to Start",               requiresForeground: true,  requiresBackground: true,  supportsSegment: true,  supportsDirection: true  },
  { id: 27, name: "7-Color Run, 1pt Multi BG — Start to End",     requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
  { id: 28, name: "7-Color Run, 1pt Multi BG — End to Start",     requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
  { id: 29, name: "7-Color Run, 1pt — Start to End",              requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
  { id: 30, name: "7-Color Run, 1pt — End to Start",              requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
  { id: 31, name: "7-Color Run, Multi — Start to End",            requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
  { id: 32, name: "7-Color Run, Multi — End to Start",            requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
  { id: 33, name: "7-Color Overlay, Multi — Start to End",        requiresForeground: false, requiresBackground: false, supportsSegment: true,  supportsDirection: true  },
];
