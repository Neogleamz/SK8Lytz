/**
 * ProductCatalog.ts — SK8Lytz Product Profile Type System
 *
 * Defines the shape of a single product entry in the dynamic catalog.
 * Replaces hardcoded SK8_DEFAULTS and the binary `isHaloz` switch throughout the app.
 *
 * A `ProductProfile` is the single source of truth for:
 *   - Hardware defaults (LED points, IC type, color sorting) written via 0x62
 *   - FTUE auto-detection thresholds (LED count range → product type)
 *   - ProductVisualizer geometry (shape, canvas size, LED dot diameter)
 */

/** The shape rendered by ProductVisualizer for this product. */
export type VizShape = 'RING' | 'OVAL' | 'DUAL_STRIP';

export interface ProductProfile {
  /** Unique product identifier. Must match `product_type` in `registered_devices`. */
  id: string;

  /** Human-readable label shown in UI cards and admin tool. */
  displayName: string;

  // ─── Hardware Defaults (written via 0x62 flash command) ───────────────────

  /** Default LED pixel count per controller. */
  defaultLedPoints: number;

  /** Default virtual segments per controller. */
  defaultSegments: number;

  /** Default IC type index (see IC_TYPES in ZenggeProtocol). 1 = WS2812B. */
  defaultIcType: number;

  /** Default color sorting index (see COLOR_SORTING_RGB in ZenggeProtocol). 2 = GRB. */
  defaultColorSorting: number;

  // ─── FTUE Auto-Detection Thresholds ──────────────────────────────────────

  /**
   * Minimum LED point count reported by 0x63 query to classify as this product.
   * Source: Master Reference §2 FTUE Logic — Threshold Classification.
   */
  detectMinPoints: number;

  /**
   * Maximum LED point count reported by 0x63 query to classify as this product.
   */
  detectMaxPoints: number;

  // ─── Visualizer Geometry ──────────────────────────────────────────────────

  /** Controls which path renderer is used in ProductVisualizer. */
  vizShape: VizShape;

  /** Fallback LED count for the visualizer when device.points is unknown. */
  vizDefaultPoints: number;

  /** Physical LED chip width in mm — maps to dot diameter in the visualizer canvas. */
  vizBlobDiameterMm: number;

  /** Bounding box width in canvas scale units (S = 0.38 in ProductVisualizer). */
  vizBaseWidth: number;

  /** Bounding box height in canvas scale units. */
  vizBaseHeight: number;

  // ─── DUAL_STRIP only (RAILZ-style products) ───────────────────────────────

  /** Number of parallel strips. DUAL_STRIP default = 2. */
  vizStripCount?: number;

  /** Gap between strip centre-lines in canvas scale units. */
  vizStripSeparation?: number;

  /** Whether strips run vertically (top-to-bottom) or horizontally (left-to-right). */
  vizStripOrientation?: 'HORIZONTAL' | 'VERTICAL';
}
