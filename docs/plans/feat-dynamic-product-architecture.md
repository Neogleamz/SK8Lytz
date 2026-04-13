# Dynamic Product Architecture (feat/dynamic-product-architecture)

## Design Decisions & Rationale

The app currently hardcodes all product knowledge (LED points, segments, IC type, auto-detect thresholds, visualizer geometry) directly inside `ZenggeProtocol.ts` (`SK8_DEFAULTS`) and `ProductVisualizer.tsx` (`isHaloz` binary branch). This works for two products but will shatter the moment a third product exists.

The strategy is a **two-layer catalog approach**: a **local-first `ProductCatalog`** (a TypeScript definition file that the app ships with, identical in structure to a Supabase table) that acts as the ground truth for offline/detection logic, plus a **Supabase-backed `product_catalog` table** that an admin can update to push new defaults without an app release. On boot, the app syncs the cloud catalog and replaces the local one in AsyncStorage — same proven pattern as `useRegistration`.

We are **NOT** changing the BLE protocol (0x62/0x63). We are only changing **where** LED point defaults, auto-detect thresholds, and visualizer geometry constants come from.

---

## Scope of Changes

### ⛔ What We Are NOT Touching

- `ZenggeProtocol.ts` BLE payload functions — zero changes to packet structure
- `useBLE.ts` — only the classification thresholds will move out
- `ProductVisualizer.tsx` rendering math — the geometric path calculations stay identical; we just replace the hardcoded `isHaloz ? 16 : 43` fallbacks with catalog lookups
- Any screens not directly rendering product hardware (Auth, Crew Hub, etc.)

---

## Proposed Changes

### Phase 1: Define the Product Catalog Type

#### [NEW] `src/types/ProductCatalog.ts`

```typescript
export interface ProductProfile {
  id: string; // e.g. 'HALOZ', 'SOULZ', 'FUTURE_PRODUCT'
  displayName: string; // e.g. 'HALOZ™ Wheel Box'
  defaultLedPoints: number; // e.g. 16
  defaultSegments: number; // e.g. 1
  defaultIcType: number; // e.g. 1 (WS2812B)
  defaultColorSorting: number; // e.g. 2 (GRB)
  // Auto-detect thresholds (from Master Reference §2 FTUE Logic)
  detectMinPoints: number; // Lower bound of LED count to classify as this product
  detectMaxPoints: number; // Upper bound
  // Visualizer geometry hints
  vizShape: "RING" | "OVAL"; // Controls which path math is used in ProductVisualizer
  vizDefaultPoints: number; // Fallback LED count for visualizer when device.points is unknown
  vizBlobDiameterMm: number; // Physical LED chip size in mm (used for dot sizing)
  vizBaseWidth: number; // Canvas width in scale units (S=0.38)
  vizBaseHeight: number; // Canvas height in scale units
}
```

#### [NEW] `src/constants/ProductCatalog.ts`

The local fallback catalog — ships with the app as a TypeScript constant. This is the Source of Truth when offline or when Supabase hasn't responded yet.

```typescript
import { ProductProfile } from "../types/ProductCatalog";

export const LOCAL_PRODUCT_CATALOG: ProductProfile[] = [
  {
    id: "HALOZ",
    displayName: "HALOZ™",
    defaultLedPoints: 16,
    defaultSegments: 1,
    defaultIcType: 1,
    defaultColorSorting: 2,
    detectMinPoints: 10,
    detectMaxPoints: 27,
    vizShape: "RING",
    vizDefaultPoints: 16,
    vizBlobDiameterMm: 7.6,
    vizBaseWidth: 60,
    vizBaseHeight: 90,
  },
  {
    id: "SOULZ",
    displayName: "SOULZ™",
    defaultLedPoints: 43,
    defaultSegments: 1,
    defaultIcType: 2,
    defaultColorSorting: 2,
    detectMinPoints: 28,
    detectMaxPoints: 300,
    vizShape: "OVAL",
    vizDefaultPoints: 43,
    vizBlobDiameterMm: 5.7,
    vizBaseWidth: 55,
    vizBaseHeight: 115,
  },
];
```

---

### Phase 2: Product Catalog Hook

#### [NEW] `src/hooks/useProductCatalog.ts`

A lightweight hook that:

1. Returns the local catalog immediately (no loading flash)
2. Syncs from `product_catalog` Supabase table in the background
3. Caches result to `AsyncStorage` key `ng_product_catalog`
4. Exposes a `getProfileById(id)` and `getProfileByPoints(ledPoints)` helper

This replaces ALL current uses of:

- `SK8_DEFAULTS.HALOZ` / `SK8_DEFAULTS.SOULZ` in `Sk8LytzProgrammerModal`
- The hardcoded `isHaloz ? 16 : 43` fallback in `ProductVisualizer.tsx` (line 83)
- The hardcoded `10–27` / `28–300` threshold ranges in `useBLE.ts`

---

### Phase 3: Supabase Schema

#### [NEW] Supabase table: `product_catalog`

```sql
CREATE TABLE product_catalog (
  id TEXT PRIMARY KEY,
  display_name TEXT NOT NULL,
  default_led_points INT NOT NULL,
  default_segments INT NOT NULL,
  default_ic_type INT NOT NULL,
  default_color_sorting INT NOT NULL,
  detect_min_points INT NOT NULL,
  detect_max_points INT NOT NULL,
  viz_shape TEXT NOT NULL DEFAULT 'OVAL',
  viz_default_points INT NOT NULL,
  viz_blob_diameter_mm FLOAT NOT NULL,
  viz_base_width INT NOT NULL,
  viz_base_height INT NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT true,
  updated_at TIMESTAMPTZ DEFAULT now()
);
-- Seed with current products
INSERT INTO product_catalog VALUES
  ('HALOZ','HALOZ™',16,1,1,2,10,27,'RING',16,7.6,60,90,true,now()),
  ('SOULZ','SOULZ™',43,1,2,2,28,300,'OVAL',43,5.7,55,115,true,now());
```

RLS: Read-only for anon/authenticated. Write only for `service_role`.

---

### Phase 4: Admin Tool Tile (Product Defaults Editor)

#### [MODIFY] `src/components/AdminToolsModal.tsx`

Add a new **"PRODUCTS"** tab to the existing admin hub (Tab 5). Displays the live `product_catalog` rows with editable fields for default LED points, min/max thresholds. Changes write directly to Supabase via `service_role` (or at a minimum flag for admin review).

> This will be a **read-only display in Phase 1** — showing the current catalog. Editing comes in Phase 2 of this epic.

---

### Phase 5: Wire Up Consumers

#### [MODIFY] `src/components/Sk8LytzProgrammerModal.tsx`

Replace `SK8_DEFAULTS.HALOZ` / `SK8_DEFAULTS.SOULZ` references with `useProductCatalog().getProfileById(activeProfile)`.

#### [MODIFY] `src/components/ProductVisualizer.tsx`

Replace the hardcoded `isHaloz ? 16 : 43` fallback (line 83) with `catalog.getProfileByPoints(device.points)?.vizDefaultPoints`.
Replace hardcoded `haloBase` / `soulBase` dimensions with `vizBaseWidth` / `vizBaseHeight` from catalog entry.

#### [MODIFY] `src/protocols/ZenggeProtocol.ts`

Update `SK8_DEFAULTS` to import from `LOCAL_PRODUCT_CATALOG` to keep backward compatibility while removing the duplication.

---

## Open Questions

> [!IMPORTANT]
> **Q1:** For the Admin Products tab — should an admin be able to edit the catalog live in production, or should edits require a staging review first? This changes whether we need an approval workflow.
> **Q2:** When a new product is added to the catalog, the `ProductVisualizer` currently uses a binary `isHaloz` path for the geometric shape math (ring vs oval). Should a future product always use one of these shapes, or do we anticipate a completely new shape (e.g., a front-lit chassis strip) that would need new rendering math?

---

## Verification Plan

### Automated

- `npx tsc --noEmit` confirms zero new errors
- Confirm `useProductCatalog` returns `HALOZ` and `SOULZ` profiles from local catalog with no network

### Manual Verification

1. Switch app to airplane mode — confirm hardware wizard FTUE still classifies 16-LED device as HALOZ
2. Confirm `Sk8LytzProgrammerModal` shows correct default points for both profiles
3. Confirm `ProductVisualizer` renders HALOZ ring and SOULZ oval shapes identically to before
4. In Admin Tools, confirm new PRODUCTS tab shows current catalog rows
