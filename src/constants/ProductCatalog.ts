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

    defaultLedPoints: 16,
    defaultSegments: 1,
    defaultIcType: 1,      // WS2812B
    defaultColorSorting: 2, // GRB
    hardwareAllowsCustomPoints: false,

    detectMinPoints: 10,
    detectMaxPoints: 27,

    vizShape: 'RING',
    vizDefaultPoints: 16,
    vizBlobDiameterMm: 7.6,
    vizBaseWidth: 60,
    vizBaseHeight: 90,
    vizIsMirrored: true,
    batteryCapacityMilliAmpereHour: 1200,
    vizThemeColor: '#00C8FF',
    brandIcon: 'circle-double',
  },
  {
    id: 'SOULZ',
    displayName: 'SOULZ™',

    defaultLedPoints: 43,
    defaultSegments: 1,
    defaultIcType: 2,      // SM16703
    defaultColorSorting: 2, // GRB
    hardwareAllowsCustomPoints: true,

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
