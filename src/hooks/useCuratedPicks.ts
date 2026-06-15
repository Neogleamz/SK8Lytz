/**
 * useCuratedPicks.ts — SK8Lytz Picks data fetching hook.
 *
 * Implements a cache-first pattern: instantly loads picks from AsyncStorage,
 * then performs a background Supabase revalidation with date-range filtering.
 *
 * Extracted from DockedController.tsx to isolate cloud data fetching.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useEffect, useState } from 'react';
import { STORAGE_PREFIX } from '../constants/AppConstants';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';
import type { IFavoriteState } from '../types/dashboard.types';
import type { Database } from '../types/supabase';

/**
 * Fetches and caches SK8Lytz Picks (curated presets) from the Supabase
 * `sk8lytz_picks` table. Uses stale-while-revalidate caching strategy.
 */
export function useCuratedPicks() {
  const [curatedPresets, setCuratedPresets] = useState<IFavoriteState[]>([]);
  const [picksLoading, setPicksLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    const CACHE_KEY = `${STORAGE_PREFIX}PicksCache`;

    const loadFromCache = async () => {
      try {
        const cached = await AsyncStorage.getItem(CACHE_KEY);
        if (!active) return;
        if (cached) {
          const parsed = JSON.parse(cached);
          if (parsed && Array.isArray(parsed) && parsed.length > 0) {
            setCuratedPresets(parsed);
            setPicksLoading(false);
          }
        }
      } catch (e: unknown) {
        if (active) {
          AppLogger.error('[SK8Lytz Picks] Cache read error', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
        }
      }
    };

    const fetchPicks = async () => {
      try {
        if (!supabase) return;
        const today = new Date().toISOString().split('T')[0];
        const { data, error } = await supabase
          .from('sk8lytz_picks')
          .select('*')
          .eq('is_active', true)
          .or(`active_from.is.null,active_from.lte.${today}`)
          .or(`active_until.is.null,active_until.gte.${today}`)
          .order('sort_order', { ascending: true })
          .returns<Database['public']['Tables']['sk8lytz_picks']['Row'][]>();

        if (!active) return;

        if (error) {
          AppLogger.error('[SK8Lytz Picks] Failed to fetch from DB', error instanceof Error ? error.message : String(error), { payload_size: 0, ssi: 0 });
          setError(error.message);
          return;
        }

        if (data && Array.isArray(data)) {
          AppLogger.log('PICK_LOADED', { source: 'supabase', count: data.length, payload_size: 0, ssi: 0 });
          // Map snake_case DB columns → IFavoriteState camelCase
          const mapped: IFavoriteState[] = data.map((row) => ({
            id: row.id,
            name: row.name,
            customName: row.custom_name ?? undefined,
            mode: row.mode as IFavoriteState['mode'],
            color: row.color ?? undefined,
            patternId: row.pattern_id ?? undefined,
            speed: row.speed ?? 50,
            brightness: row.brightness ?? 90,
            fixedColorMode: (row.fixed_color_mode as IFavoriteState['fixedColorMode']) ?? undefined,
            fixedFgColor: row.fixed_fg_color ?? undefined,
            fixedBgColor: row.fixed_bg_color ?? undefined,
            fixedHue: row.fixed_hue ?? undefined,
            multiColors: Array.isArray(row.multi_colors) ? row.multi_colors.map(String) : undefined,
            multiTransition: row.multi_transition ?? undefined,
            multiLength: row.multi_length ?? undefined,
            musicPrimaryColor: row.music_primary_color ?? undefined,
            musicSecondaryColor: row.music_secondary_color ?? undefined,
            micSensitivity: row.mic_sensitivity ?? undefined,
            micSource: (row.mic_source as IFavoriteState['micSource']) ?? undefined,
            musicMatrixStyle: (row.music_matrix_style as IFavoriteState['musicMatrixStyle']) ?? undefined,
          }));

          // Only update and re-render if the fetched data differs to prevent flicker
          setCuratedPresets((prev) => {
            if (JSON.stringify(prev) !== JSON.stringify(mapped)) {
              return mapped;
            }
            return prev;
          });

          // Update cache asynchronously
          AsyncStorage.setItem(CACHE_KEY, JSON.stringify(mapped)).catch(e => {
            if (active) AppLogger.warn('Failed to cache curated picks', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
          });
        }
      } catch (e: unknown) {
        if (!active) return;
        AppLogger.error('[SK8Lytz Picks] Exception fetching from DB', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
        setError(e instanceof Error ? e.message : String(e));
      } finally {
        if (active) {
          setPicksLoading(false);
        }
      }
    };

    // 1. Instantly load from cache to populate UI
    loadFromCache().then(() => {
      // 2. Perform background revalidation
      if (active) fetchPicks();
    });

    return () => {
      active = false;
    };
  }, []);

  return { curatedPresets, picksLoading, error };
}
