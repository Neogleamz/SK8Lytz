/**
 * RbmDictionary.ts — SK8Lytz RBM Pattern Simulator
 *
 * Ground-truth pattern definitions derived from the Zengge APK
 * `symphony_SymphonyBuild_*` string table (300 modes).
 *
 * Each entry defines EXACTLY what the hardware does, mapped to a simulator
 * descriptor that the ProductVisualizer can render pixel-accurately:
 *
 *  motion:
 *    'SCROLL_FWD'   — one-way scroll start→end (RunningWater)
 *    'SCROLL_REV'   — one-way scroll end→start
 *    'PING_PONG'    — bounce start↔end continuously
 *    'MID_OUT'      — expand from center to both ends
 *    'ENDS_IN'      — contract from both ends to center
 *    'OVERLAY_FWD'  — fill/overlay start→end (LEDs stay lit as head moves)
 *    'OVERLAY_REV'  — fill/overlay end→start
 *    'OVERLAY_PING' — fill/overlay bounce
 *    'OVERLAY_MID'  — fill from middle outward
 *    'OVERLAY_ENDS' — fill from both ends to middle
 *    'FADE'         — whole-strip cross-fade / gradual change
 *    'JUMP'         — whole-strip hard color jump
 *    'STROBE'       — whole-strip strobe flash
 *    'BREATHE'      — whole-strip breathe (fade in/out)
 *    'COMET'        — one bright dot with fading trail
 *    'COMET_PING'   — comet bounce
 *    'STATIC'       — no animation
 *    'ALL'          — cycles all modes (not representable — fallback to FADE)
 *
 *  colors: which palette the hardware uses
 *    'RAINBOW7'     — 7-color: red, orange, yellow, green, cyan, blue, purple
 *    'RED','GREEN','BLUE','YELLOW','CYAN','PURPLE','WHITE','BLACK'
 *    'RG','RB','GB' — 2-color pairs
 *    'WHITE7'       — 7 colors with white leader
 *
 *  bg: background color ('BLACK','RED','GREEN','BLUE','YELLOW','PURPLE','CYAN','WHITE')
 *  points: dot size ('SINGLE','MULTI') — 'SINGLE' = 1 LED head, 'MULTI' = multi-LED head
 */

export type RbmMotion =
  | 'SCROLL_FWD' | 'SCROLL_REV' | 'PING_PONG'
  | 'MID_OUT' | 'ENDS_IN'
  | 'OVERLAY_FWD' | 'OVERLAY_REV' | 'OVERLAY_PING' | 'OVERLAY_MID' | 'OVERLAY_ENDS'
  | 'FADE' | 'JUMP' | 'STROBE' | 'BREATHE'
  | 'COMET' | 'COMET_PING' | 'STATIC' | 'ALL';

export type RbmColorPalette =
  | 'RAINBOW7' | 'WHITE7'
  | 'RED' | 'GREEN' | 'BLUE' | 'YELLOW' | 'CYAN' | 'PURPLE' | 'WHITE' | 'BLACK'
  | 'RG' | 'RB' | 'GB';

export type RbmBg =
  | 'BLACK' | 'RED' | 'GREEN' | 'BLUE' | 'YELLOW' | 'PURPLE' | 'CYAN' | 'WHITE';

export interface RbmPattern {
  id: number;
  name: string;
  motion: RbmMotion;
  colors: RbmColorPalette;
  bg: RbmBg;
  points: 'SINGLE' | 'MULTI'; // head size
}

// ── Palette color arrays ─────────────────────────────────────────────────────
// Used by the visualizer to generate pixel arrays
export const PALETTE: Record<RbmColorPalette, string[]> = {
  RAINBOW7: ['#FF0000', '#FF6600', '#FFFF00', '#00FF00', '#00FFFF', '#0066FF', '#AA00FF'],
  WHITE7:   ['#FFFFFF', '#FF0000', '#FF6600', '#FFFF00', '#00FF00', '#00FFFF', '#0066FF'],
  RED:      ['#FF0000'],
  GREEN:    ['#00FF00'],
  BLUE:     ['#0066FF'],
  YELLOW:   ['#FFFF00'],
  CYAN:     ['#00FFFF'],
  PURPLE:   ['#AA00FF'],
  WHITE:    ['#FFFFFF'],
  BLACK:    ['#000000'],
  RG:       ['#FF0000', '#00FF00'],
  RB:       ['#FF0000', '#0066FF'],
  GB:       ['#00FF00', '#0066FF'],
};

export const BG_HEX: Record<RbmBg, string> = {
  BLACK:  '#000000',
  RED:    '#FF0000',
  GREEN:  '#00FF00',
  BLUE:   '#0066FF',
  YELLOW: '#FFFF00',
  PURPLE: '#AA00FF',
  CYAN:   '#00FFFF',
  WHITE:  '#FFFFFF',
};

// ── Full pattern table (IDs 1–100, mapped 1:1 to SymphonyBuild) ─────────────
export const RBM_PATTERNS: RbmPattern[] = [
  // 1:  Circulate all modes — fallback to rainbow fade
  { id: 1,   name: 'Static',             motion: 'STATIC',      colors: 'RED',     bg: 'BLACK', points: 'MULTI' },
  // SymphonyBuild 2-13 (global motion modes — no bg, all 7 colors)
  { id: 2,   name: 'Rainbow Jump',        motion: 'JUMP',        colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 3,   name: '7 Color Jump',        motion: 'JUMP',        colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 4,   name: 'Red Green Jump',      motion: 'JUMP',        colors: 'RG',       bg: 'BLACK', points: 'MULTI' },
  { id: 5,   name: 'Red Blue Jump',       motion: 'JUMP',        colors: 'RB',       bg: 'BLACK', points: 'MULTI' },
  { id: 6,   name: 'Green Blue Jump',     motion: 'JUMP',        colors: 'GB',       bg: 'BLACK', points: 'MULTI' },
  { id: 7,   name: 'Rainbow Gradual',     motion: 'FADE',        colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 8,   name: '7 Color Gradual',     motion: 'FADE',        colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 9,   name: 'Red Green Gradual',   motion: 'FADE',        colors: 'RG',       bg: 'BLACK', points: 'MULTI' },
  { id: 10,  name: 'Red Blue Gradual',    motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 11,  name: 'Green Blue Gradual',  motion: 'BREATHE',     colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 12,  name: '7 Color Strobe',      motion: 'STROBE',      colors: 'RAINBOW7', bg: 'BLACK', points: 'MULTI' },
  { id: 13,  name: 'Red Strobe',          motion: 'BREATHE',     colors: 'RED',      bg: 'BLACK', points: 'MULTI' },
  // 14–21: Run circularly, 7 colors with BG, 1pt start→end  (SymphonyBuild 14-21)
  { id: 14,  name: 'Green Strobe',        motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'BLACK',  points: 'SINGLE' },
  { id: 15,  name: 'Blue Strobe',         motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'RED',    points: 'SINGLE' },
  { id: 16,  name: 'Yellow Strobe',       motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'GREEN',  points: 'SINGLE' },
  { id: 17,  name: 'Cyan Strobe',         motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'BLUE',   points: 'SINGLE' },
  { id: 18,  name: 'Purple Strobe',       motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'YELLOW', points: 'SINGLE' },
  { id: 19,  name: 'White Strobe',        motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'PURPLE', points: 'SINGLE' },
  { id: 20,  name: '7 Color Flow',        motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'CYAN',   points: 'SINGLE' },
  { id: 21,  name: '7 Color Chase',       motion: 'SCROLL_FWD',  colors: 'RAINBOW7', bg: 'WHITE',  points: 'SINGLE' },
  // 22–29: Run circularly, 7 colors with BG, 1pt end→start (SymphonyBuild 22-29)
  { id: 22,  name: '7 Color Marquee',     motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'BLACK',  points: 'SINGLE' },
  { id: 23,  name: '7 Color Rain',        motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'RED',    points: 'SINGLE' },
  { id: 24,  name: '7 Color Breath',      motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'GREEN',  points: 'SINGLE' },
  { id: 25,  name: '7 Color Comet',       motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'BLUE',   points: 'SINGLE' },
  { id: 26,  name: '7 Color Wave',        motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'YELLOW', points: 'SINGLE' },
  { id: 27,  name: '7 Color Stack',       motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'PURPLE', points: 'SINGLE' },
  { id: 28,  name: '7 Color Trail',       motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'CYAN',   points: 'SINGLE' },
  { id: 29,  name: '7 Color Burst',       motion: 'SCROLL_REV',  colors: 'RAINBOW7', bg: 'WHITE',  points: 'SINGLE' },
  // 30–37: Run circularly, 7 colors with BG, 1pt start→end→return (PING_PONG)
  { id: 30,  name: '7 Color Scatter',     motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'BLACK',  points: 'SINGLE' },
  { id: 31,  name: '7 Color Beam',        motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'RED',    points: 'SINGLE' },
  { id: 32,  name: '7 Color Sparkle',     motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'GREEN',  points: 'SINGLE' },
  { id: 33,  name: '7 Color Starry',      motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'BLUE',   points: 'SINGLE' },
  { id: 34,  name: '7 Color Flash',       motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'YELLOW', points: 'SINGLE' },
  { id: 35,  name: '7 Color Pulse',       motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'PURPLE', points: 'SINGLE' },
  { id: 36,  name: '7 Color Spin',        motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'CYAN',   points: 'SINGLE' },
  { id: 37,  name: '7 Color Swing',       motion: 'PING_PONG',   colors: 'RAINBOW7', bg: 'WHITE',  points: 'SINGLE' },
  // 38–45: Run circularly, 7 colors with BG, 1pt MID_OUT (middle→both ends)
  { id: 38,  name: '7 Color Twirl',       motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'BLACK',  points: 'SINGLE' },
  { id: 39,  name: '7 Color Glide',       motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'RED',    points: 'SINGLE' },
  { id: 40,  name: 'Red Flow',            motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'GREEN',  points: 'SINGLE' },
  { id: 41,  name: 'Green Flow',          motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'BLUE',   points: 'SINGLE' },
  { id: 42,  name: 'Blue Flow',           motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'YELLOW', points: 'SINGLE' },
  { id: 43,  name: 'Yellow Flow',         motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'PURPLE', points: 'SINGLE' },
  { id: 44,  name: 'Cyan Flow',           motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'CYAN',   points: 'SINGLE' },
  { id: 45,  name: 'Purple Flow',         motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'WHITE',  points: 'SINGLE' },
  // 46–53: Run circularly, 7 colors with BG, 1pt ENDS_IN (both ends→middle)
  { id: 46,  name: 'White Flow',          motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'BLACK',  points: 'SINGLE' },
  { id: 47,  name: 'Red Chase',           motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'RED',    points: 'SINGLE' },
  { id: 48,  name: 'Green Chase',         motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'GREEN',  points: 'SINGLE' },
  { id: 49,  name: 'Blue Chase',          motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'BLUE',   points: 'SINGLE' },
  { id: 50,  name: 'Yellow Chase',        motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'YELLOW', points: 'SINGLE' },
  { id: 51,  name: 'Cyan Chase',          motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'PURPLE', points: 'SINGLE' },
  { id: 52,  name: 'Purple Chase',        motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'CYAN',   points: 'SINGLE' },
  { id: 53,  name: 'White Chase',         motion: 'ENDS_IN',     colors: 'RAINBOW7', bg: 'WHITE',  points: 'SINGLE' },
  // 54–61: Run circularly MID→ENDS→return (ping-pong from center)
  { id: 54,  name: 'Red Marquee',         motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'BLACK',  points: 'SINGLE' },
  { id: 55,  name: 'Green Marquee',       motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'RED',    points: 'SINGLE' },
  { id: 56,  name: 'Blue Marquee',        motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'GREEN',  points: 'SINGLE' },
  { id: 57,  name: 'Yellow Marquee',      motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'BLUE',   points: 'SINGLE' },
  { id: 58,  name: 'Cyan Marquee',        motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'YELLOW', points: 'SINGLE' },
  { id: 59,  name: 'Purple Marquee',      motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'PURPLE', points: 'SINGLE' },
  { id: 60,  name: 'White Marquee',       motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'CYAN',   points: 'SINGLE' },
  { id: 61,  name: 'Red Rain',            motion: 'MID_OUT',     colors: 'RAINBOW7', bg: 'WHITE',  points: 'SINGLE' },
  // 62–69: Overlay circularly, 7 colors with BG, start→end
  { id: 62,  name: 'Green Rain',          motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'BLACK',  points: 'MULTI' },
  { id: 63,  name: 'Blue Rain',           motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'RED',    points: 'MULTI' },
  { id: 64,  name: 'Yellow Rain',         motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'GREEN',  points: 'MULTI' },
  { id: 65,  name: 'Cyan Rain',           motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'BLUE',   points: 'MULTI' },
  { id: 66,  name: 'Purple Rain',         motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'YELLOW', points: 'MULTI' },
  { id: 67,  name: 'White Rain',          motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'PURPLE', points: 'MULTI' },
  { id: 68,  name: 'Red Breath',          motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'CYAN',   points: 'MULTI' },
  { id: 69,  name: 'Green Breath',        motion: 'OVERLAY_FWD', colors: 'RAINBOW7', bg: 'WHITE',  points: 'MULTI' },
  // 70–77: Overlay circularly, 7 colors with BG, end→start
  { id: 70,  name: 'Blue Breath',         motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'BLACK',  points: 'MULTI' },
  { id: 71,  name: 'Yellow Breath',       motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'RED',    points: 'MULTI' },
  { id: 72,  name: 'Cyan Breath',         motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'GREEN',  points: 'MULTI' },
  { id: 73,  name: 'Purple Breath',       motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'BLUE',   points: 'MULTI' },
  { id: 74,  name: 'White Breath',        motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'YELLOW', points: 'MULTI' },
  { id: 75,  name: 'Red Comet',           motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'PURPLE', points: 'MULTI' },
  { id: 76,  name: 'Green Comet',         motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'CYAN',   points: 'MULTI' },
  { id: 77,  name: 'Blue Comet',          motion: 'OVERLAY_REV', colors: 'RAINBOW7', bg: 'WHITE',  points: 'MULTI' },
  // 78–85: Overlay circularly, start→end→return (PING)
  { id: 78,  name: 'Yellow Comet',        motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'BLACK',  points: 'MULTI' },
  { id: 79,  name: 'Cyan Comet',          motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'RED',    points: 'MULTI' },
  { id: 80,  name: 'Purple Comet',        motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'GREEN',  points: 'MULTI' },
  { id: 81,  name: 'White Comet',         motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'BLUE',   points: 'MULTI' },
  { id: 82,  name: 'Red Wave',            motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'YELLOW', points: 'MULTI' },
  { id: 83,  name: 'Green Wave',          motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'PURPLE', points: 'MULTI' },
  { id: 84,  name: 'Blue Wave',           motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'CYAN',   points: 'MULTI' },
  { id: 85,  name: 'Yellow Wave',         motion: 'OVERLAY_PING', colors: 'RAINBOW7', bg: 'WHITE',  points: 'MULTI' },
  // 86–93: Overlay circularly, middle→both ends (OVERLAY_MID)
  { id: 86,  name: 'Cyan Wave',           motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'BLACK',  points: 'MULTI' },
  { id: 87,  name: 'Purple Wave',         motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'RED',    points: 'MULTI' },
  { id: 88,  name: 'White Wave',          motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'GREEN',  points: 'MULTI' },
  { id: 89,  name: 'Firefly',             motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'BLUE',   points: 'MULTI' },
  { id: 90,  name: 'Meteor Wash',         motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'YELLOW', points: 'MULTI' },
  { id: 91,  name: 'Aurora',              motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'PURPLE', points: 'MULTI' },
  { id: 92,  name: 'Lightning',           motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'CYAN',   points: 'MULTI' },
  { id: 93,  name: 'Disco',               motion: 'OVERLAY_MID', colors: 'RAINBOW7', bg: 'WHITE',  points: 'MULTI' },
  // 94–100: Overlay circularly, both ends→middle (OVERLAY_ENDS)
  { id: 94,  name: 'Neon City',           motion: 'OVERLAY_ENDS', colors: 'RAINBOW7', bg: 'BLACK',  points: 'MULTI' },
  { id: 95,  name: 'Forest Path',         motion: 'OVERLAY_ENDS', colors: 'RAINBOW7', bg: 'RED',   points: 'MULTI' },
  { id: 96,  name: 'Lava Lamp',           motion: 'OVERLAY_ENDS', colors: 'RAINBOW7', bg: 'GREEN', points: 'MULTI' },
  { id: 97,  name: 'Ocean Drift',         motion: 'OVERLAY_ENDS', colors: 'RAINBOW7', bg: 'BLUE',  points: 'MULTI' },
  { id: 98,  name: 'Comet',               motion: 'COMET',        colors: 'RAINBOW7', bg: 'BLACK', points: 'SINGLE' },
  { id: 99,  name: 'Comet Ping',          motion: 'COMET_PING',   colors: 'RAINBOW7', bg: 'BLACK', points: 'SINGLE' },
  { id: 100, name: 'Emergency',           motion: 'STATIC',       colors: 'RED',      bg: 'BLACK', points: 'MULTI' },
];

// ── Lookup helpers ──────────────────────────────────────────────────────────

export function getRbmPattern(id: number): RbmPattern {
  return RBM_PATTERNS.find(p => p.id === id) ?? RBM_PATTERNS[6]; // default: FADE rainbow
}

/** Legacy archetype helper — kept for callers not yet updated */
export const getArchetypeFromId = (id: number): 'MARQUEE' | 'METEOR' | 'BREATHING' | 'STROBE' | 'OUTLIERS' => {
  const p = getRbmPattern(id);
  if (p.motion === 'STROBE') return 'STROBE';
  if (p.motion === 'BREATHE') return 'BREATHING';
  if (['SCROLL_FWD','SCROLL_REV','PING_PONG','MID_OUT','ENDS_IN','COMET','COMET_PING'].includes(p.motion)) return 'MARQUEE';
  if (['OVERLAY_FWD','OVERLAY_REV','OVERLAY_PING','OVERLAY_MID','OVERLAY_ENDS'].includes(p.motion)) return 'MARQUEE';
  return 'OUTLIERS';
};
