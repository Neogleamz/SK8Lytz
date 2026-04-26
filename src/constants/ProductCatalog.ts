/**
 * ProductCatalog.ts — Local Fallback Product Catalog
 *
 * This is the offline-safe source of truth for all SK8Lytz product profiles.
 * It ships with the app binary and is used immediately on mount before any
 * Supabase sync has had a chance to respond.
 *
 * Architecture:
 *   1. App boot → use LOCAL_PRODUCT_CATALOG instantly (no flash, fully offline)
 *   2. Background → fetch `product_catalog` from Supabase (is_active = true)
 *   3. Merge → Supabase row wins on id conflict; result cached to AsyncStorage
 *
 * To add a new product:
 *   1. Add a `ProductProfile` entry here with confirmed hardware specs
 *   2. Run the Supabase migration to seed the matching row
 *   3. If the product requires a new vizShape, add the path math in ProductVisualizer.tsx
 *
 * Threshold values sourced from: Master Reference §2 — FTUE Threshold Classification
 */

import type { ProductProfile } from '../types/ProductCatalog';

export const LOCAL_PRODUCT_CATALOG: ProductProfile[] = [
  {
    id: 'HALOZ',
    displayName: 'HALOZ™',

    // ── HARDWARE TRUTH (confirmed 2026-04-22) ──────────────────────────────
    // Layer 1 (canvas):  ledPoints=8  → 8 addressable LEDs per segment
    // Layer 2 (mirror):  segments=2   → hardware auto-mirrors pattern to 2nd segment
    // Layer 3 (physics): 16 total physical LEDs (8 × 2)
    // ⚠️  NEVER build a 16-element pixel array and send it to HALOZ.
    //     Build an 8-element array. The hardware duplicates it automatically.
    // ⚠️  Previous code had defaultLedPoints:16, segments:1 which is WRONG.
    //     It resulted in bypassing the hardware segment mirror engine.
    defaultLedPoints: 8,
    defaultSegments: 2,
    defaultIcType: 1,      // WS2812B
    defaultColorSorting: 2, // GRB
    hardwareAllowsCustomPoints: false, // HALOZ ring is fixed geometry

    detectMinPoints: 10,
    detectMaxPoints: 27,

    vizShape: 'RING',
    vizDefaultPoints: 16, // visualizer renders 16 dots (8 × 2 segments) for accurate preview
    vizBlobDiameterMm: 7.6,
    vizBaseWidth: 60,
    vizBaseHeight: 90,
    vizIsMirrored: false, // HALOZ: hardware auto-mirrors to seg2. Software must NOT mirror additionally (causes 4-dot artifact via applySymmetry/mirroredFract ping-pong).
    batteryCapacityMilliAmpereHour: 1200,
    vizThemeColor: '#00C8FF',
    brandIcon: 'circle-double',
  },
  {
    id: 'SOULZ',
    displayName: 'SOULZ™',

    // ── HARDWARE TRUTH (confirmed 2026-04-22) ──────────────────────────────
    // Layer 1 (canvas):  ledPoints=43 (user-adjustable after cutting strips to length)
    // Layer 2 (mirror):  segments=1   → no hardware mirroring
    // Layer 3 (physics): 86 total physical LEDs across BOTH skate boots
    //   → 43 LEDs on LEFT skate (outside of boot)
    //   → 43 LEDs on RIGHT skate (inside of boot)
    //   → Both strips are Y-wired to the SAME controller output.
    //     The controller is oblivious to the doubling — it drives one 43-point canvas.
    //     The wiring handles physical duplication transparently.
    // ⚠️  IF the user cuts strips shorter they MUST adjust ledPoints in HW Setup
    //     (e.g. cut to 36 per skate → set ledPoints=36). The controller must match reality.
    //     hardwareAllowsCustomPoints=true enables the LED Points adjuster in setup wizard.
    defaultLedPoints: 43,
    defaultSegments: 1,
    defaultIcType: 2,      // SM16703
    defaultColorSorting: 2, // GRB
    hardwareAllowsCustomPoints: true, // User can trim strips to custom length

    detectMinPoints: 28,
    detectMaxPoints: 300,

    vizShape: 'OVAL',
    vizDefaultPoints: 43,
    vizBlobDiameterMm: 5.7,
    vizBaseWidth: 55,
    vizBaseHeight: 115,
    vizIsMirrored: false,
    batteryCapacityMilliAmpereHour: 2000,
    vizThemeColor: '#9D4EFF',
    brandIcon: 'lightning-bolt',
  },
  {
    id: 'RAILZ',
    displayName: 'RAILZ™',

    defaultLedPoints: 30,
    defaultSegments: 2,
    defaultIcType: 1,      // WS2812B
    defaultColorSorting: 2, // GRB
    hardwareAllowsCustomPoints: true,

    // WARNING: Placeholder thresholds — update when RAILZ hardware LED count is confirmed
    detectMinPoints: 1,
    detectMaxPoints: 9,

    vizShape: 'DUAL_STRIP',
    vizDefaultPoints: 30,
    vizBlobDiameterMm: 5.0,
    vizBaseWidth: 80,
    vizBaseHeight: 120,

    vizStripCount: 2,
    vizStripSeparation: 32,
    vizStripOrientation: 'VERTICAL',
    vizIsMirrored: true,
    batteryCapacityMilliAmpereHour: 2000,
    vizThemeColor: '#FF5A00',
    brandIcon: 'reorder-vertical',
  },
];

/** Returns a product profile by its string ID. Case-insensitive. */
export function getLocalProfileById(id: string): ProductProfile | undefined {
  return LOCAL_PRODUCT_CATALOG.find(p => p.id.toUpperCase() === id.toUpperCase());
}

/**
 * Classifies a device by its LED point count using detect thresholds.
 * Returns the matching profile, or SOULZ as a safe fallback.
 */
export function getLocalProfileByPoints(ledPoints: number): ProductProfile {
  const match = LOCAL_PRODUCT_CATALOG.find(
    p => ledPoints >= p.detectMinPoints && ledPoints <= p.detectMaxPoints
  );
  return match ?? (LOCAL_PRODUCT_CATALOG.find(p => p.id === 'SOULZ') as ProductProfile);
}
