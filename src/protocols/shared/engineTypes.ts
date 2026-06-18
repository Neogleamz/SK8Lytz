/**
 * SK8Lytz Shared Engine Types
 *
 * Single source of truth for shared type definitions used by multiple engine files.
 * Extracted from PatternEngine.ts to break the following circular import cycles:
 *   - PatternEngine ↔ SpatialEngine
 *   - PatternEngine ↔ VisualizerEngine
 *   - PatternEngine ↔ SymphonyEngine
 *
 * DO NOT add business logic here. Types only.
 */

/** 24-bit RGB color triplet. All values 0–255. */
export interface RGB {
  r: number;
  g: number;
  b: number;
}

/** Pattern identifier — integer from 1 through 233 (includes Street Mode IDs 101–105). */
export type PatternId = number;

/** Color rendering mode for UI picker logic. */
export type ColorMode = 'FG_BG' | 'FG_ONLY' | 'BG_ONLY' | 'GENERATIVE';

/** Options passed to spatial pattern builders. */
export interface PatternOptions {
  distribution?: [number, number, number]; // [tail, cruise, head]
  segments?: number;
}

/** SK8Lytz pattern template metadata schema (Master Reference A§1). */
export interface SK8LytzTemplate {
  id: number;                   // Unique. Never reuse. 1-28=P1B, 29-61=P1A ge.*
  name: string;                 // User-facing label in picker
  icon: string;                 // Emoji for picker card
  colorMode: ColorMode;         // Canonical: 'FG_BG' | 'FG_ONLY' | 'BG_ONLY' | 'GENERATIVE'
  requiresForeground: boolean;  // Derived: colorMode !== 'GENERATIVE' → show FG picker
  requiresBackground: boolean;  // Derived: colorMode === 'FG_BG'     → show BG picker
  supportsDirection: boolean;   // Show direction toggle in UI?
  supportsSegment: boolean;     // Segment-mirroring compatible?
  tier: 1 | 2 | 3;             // 1=ge.* reversal, 2=Programs reversal, 3=SK8Lytz original
  group: string;                // UI grouping label in picker
  isHidden?: boolean;           // If true, hide from UI picker
  sourceRef?: string;           // e.g. 'ge.OceanWaveEffect' or 'Programs:CometChase'
}
