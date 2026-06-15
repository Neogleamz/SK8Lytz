/**
 * useProductCatalog.ts — Dynamic Product Catalog Hook
 *
 * Local-first architecture (mirrors useRegistration pattern):
 *   1. On mount → return LOCAL_PRODUCT_CATALOG immediately (zero loading flash, offline-safe)
 *   2. Background → fetch `product_catalog` from Supabase (is_active = true)
 *   3. Merge → Supabase rows win on id conflict; merged result cached to AsyncStorage
 *   4. Reconcile → any LOCAL profile not in Supabase is retained (new products not yet seeded)
 *
 * Usage:
 *   const { getProfileById, getProfileByPoints, allProfiles } = useProductCatalog();
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useRef, useState } from 'react';
import {
    LOCAL_PRODUCT_CATALOG,
    getLocalProfileById,
    getLocalProfileByPoints,
} from '../constants/ProductCatalog';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/appLogger';
import { STORAGE_PRODUCT_CATALOG } from '../constants/storageKeys';
import type { ProductProfile, VizShape } from '../types/ProductCatalog';

interface ProductCatalogRow {
  id: string;
  display_name: string;
  default_led_points: number;
  default_segments: number;
  default_ic_type: number;
  default_color_sorting: number;
  hardware_allows_custom_points?: boolean | null;
  detect_min_points: number;
  detect_max_points: number;
  viz_shape: string;
  viz_default_points: number;
  viz_blob_diameter_mm: number;
  viz_base_width: number;
  viz_base_height: number;
  viz_strip_count?: number | null;
  viz_strip_separation?: number | null;
  viz_strip_orientation?: string | null;
  viz_is_mirrored?: boolean | null;
  battery_capacity_milli_ampere_hour?: number | null;
  viz_theme_color?: string | null;
  brand_icon?: string | null;
}

function rowToProfile(row: ProductCatalogRow): ProductProfile {
  return {
    id:                   row.id,
    displayName:          row.display_name,
    defaultLedPoints:     row.default_led_points,
    defaultSegments:      row.default_segments,
    defaultIcType:        row.default_ic_type,
    defaultColorSorting:  row.default_color_sorting,
    hardwareAllowsCustomPoints: row.hardware_allows_custom_points ?? false,
    detectMinPoints:      row.detect_min_points,
    detectMaxPoints:      row.detect_max_points,
    vizShape:             row.viz_shape as VizShape,
    vizDefaultPoints:     row.viz_default_points,
    vizBlobDiameterMm:    row.viz_blob_diameter_mm,
    vizBaseWidth:         row.viz_base_width,
    vizBaseHeight:        row.viz_base_height,
    vizStripCount:        row.viz_strip_count ?? undefined,
    vizStripSeparation:   row.viz_strip_separation ?? undefined,
    vizStripOrientation:  row.viz_strip_orientation as "HORIZONTAL" | "VERTICAL" | undefined,
    vizIsMirrored:        row.viz_is_mirrored ?? undefined,
    batteryCapacityMilliAmpereHour: row.battery_capacity_milli_ampere_hour ?? 0,
    vizThemeColor:        row.viz_theme_color ?? undefined,
    brandIcon:            row.brand_icon ?? undefined,
  };
}

// ─── Hook ─────────────────────────────────────────────────────────────────────

export function useProductCatalog() {
  const [allProfiles, setAllProfiles] = useState<ProductProfile[]>(LOCAL_PRODUCT_CATALOG);
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  // ── Boot: load cached catalog from AsyncStorage, then sync from cloud ────────
  useEffect(() => {
    loadCachedCatalog()
      .then(() => syncFromCloud())
      .catch((e: unknown) => {
        AppLogger.warn('[ProductCatalog] Boot failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      });
  }, []);

  const loadCachedCatalog = async () => {
    try {
      // One-time data migration from banned ng_ namespace
      const old = await AsyncStorage.getItem('ng_product_catalog');
      if (old) {
        await AsyncStorage.setItem(STORAGE_PRODUCT_CATALOG, old);
        await AsyncStorage.removeItem('ng_product_catalog');
      }
      const raw = await AsyncStorage.getItem(STORAGE_PRODUCT_CATALOG);
      if (raw) {
        const cached: ProductProfile[] = JSON.parse(raw);
        if (!isMountedRef.current) return;
        if (cached.length > 0) setAllProfiles(cached);
      }
    } catch (e: unknown) {
      AppLogger.warn('[ProductCatalog] Cache load failed', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
    }
  };

  const syncFromCloud = async () => {
    try {
      const { data, error } = await supabase
        .from('product_catalog')
        .select('*')
        .eq('is_active', true)
        .order('id', { ascending: true });

      if (error) throw error;
      if (!data || data.length === 0) return;

      // Supabase rows win on id conflict; retain LOCAL-only entries not yet seeded in cloud
      const cloudProfiles = data.map(rowToProfile);
      const cloudIds = new Set(cloudProfiles.map((p: ProductProfile) => p.id));
      const localOnly = LOCAL_PRODUCT_CATALOG.filter((p: ProductProfile) => !cloudIds.has(p.id));
      const merged = [...cloudProfiles, ...localOnly];

      if (!isMountedRef.current) return;
      setAllProfiles(merged);
      await AsyncStorage.setItem(STORAGE_PRODUCT_CATALOG, JSON.stringify(merged));
    } catch (e: unknown) {
      AppLogger.warn('[ProductCatalog] Cloud sync failed (offline?)', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
    }
  };

  // ── Helpers (stable references via useCallback) ──────────────────────────────

  const getProfileById = useCallback((id: string): ProductProfile | undefined => {
    const upper = id.toUpperCase();
    return allProfiles.find(p => p.id.toUpperCase() === upper)
      ?? getLocalProfileById(id);
  }, [allProfiles]);

  const getProfileByPoints = useCallback((ledPoints: number): ProductProfile => {
    const match = allProfiles.find(
      p => ledPoints >= p.detectMinPoints && ledPoints <= p.detectMaxPoints
    );
    return match ?? getLocalProfileByPoints(ledPoints);
  }, [allProfiles]);

  /** Upsert a product profile to Supabase and refresh local state. */
  const saveProfile = useCallback(async (profile: ProductProfile): Promise<boolean> => {
    try {
      const dbRow = {
        id:                    profile.id,
        display_name:          profile.displayName,
        default_led_points:    profile.defaultLedPoints,
        default_segments:      profile.defaultSegments,
        default_ic_type:       profile.defaultIcType,
        default_color_sorting: profile.defaultColorSorting,
        detect_min_points:     profile.detectMinPoints,
        detect_max_points:     profile.detectMaxPoints,
        viz_shape:             profile.vizShape,
        viz_default_points:    profile.vizDefaultPoints,
        viz_blob_diameter_mm:  profile.vizBlobDiameterMm,
        viz_base_width:        profile.vizBaseWidth,
        viz_base_height:       profile.vizBaseHeight,
        viz_strip_count:       profile.vizStripCount ?? null,
        viz_strip_separation:  profile.vizStripSeparation ?? null,
        viz_strip_orientation: profile.vizStripOrientation ?? null,
        is_active:             true,
        updated_at:            new Date().toISOString(),
      };

      const { error } = await supabase
        .from('product_catalog')
        .upsert(dbRow, { onConflict: 'id' });

      if (error) throw error;

      // Invalidate cache and re-sync to pick up the saved row
      await AsyncStorage.removeItem(STORAGE_PRODUCT_CATALOG);
      await syncFromCloud();
      return true;
    } catch (e: unknown) {
      AppLogger.warn('[ProductCatalog] Save failed', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
      return false;
    }
  }, []);

  return {
    allProfiles,
    getProfileById,
    getProfileByPoints,
    saveProfile,
    syncFromCloud,
  };
}
