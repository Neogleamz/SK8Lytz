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

/**
 * Fetches and caches SK8Lytz Picks (curated presets) from the Supabase
 * `sk8lytz_picks` table. Uses stale-while-revalidate caching strategy.
 */
export function useCuratedPicks() {
  const [curatedPresets, setCuratedPresets] = useState<IFavoriteState[]>([]);
  const [picksLoading, setPicksLoading] = useState(true);

  useEffect(() => {
    const CACHE_KEY = `${STORAGE_PREFIX}PicksCache`;

    const loadFromCache = async () => {
      try {
        const cached = await AsyncStorage.getItem(CACHE_KEY);
        if (cached) {
          const parsed = JSON.parse(cached);
          if (parsed && Array.isArray(parsed) && parsed.length > 0) {
            setCuratedPresets(parsed);
            setPicksLoading(false);
          }
        }
      } catch (e) {
        AppLogger.error('[SK8Lytz Picks] Cache read error', e);
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
          .order('sort_order', { ascending: true });

        if (error) {
          AppLogger.error('[SK8Lytz Picks] Failed to fetch from DB', error);
          return;
        }

        if (data && Array.isArray(data)) {
          // Map snake_case DB columns → IFavoriteState camelCase
          const mapped: IFavoriteState[] = data.map((row: any) => ({
            id: row.id,
            name: row.name,
            customName: row.custom_name,
            mode: row.mode,
            color: row.color,
            patternId: row.pattern_id,
            speed: row.speed ?? 50,
            brightness: row.brightness ?? 90,
            fixedColorMode: row.fixed_color_mode,
            fixedFgColor: row.fixed_fg_color,
            fixedBgColor: row.fixed_bg_color,
            fixedHue: row.fixed_hue,
            multiColors: row.multi_colors ?? undefined,
            multiTransition: row.multi_transition,
            multiLength: row.multi_length,
            musicPrimaryColor: row.music_primary_color,
            musicSecondaryColor: row.music_secondary_color,
            micSensitivity: row.mic_sensitivity,
            micSource: row.mic_source,
            musicMatrixStyle: row.music_matrix_style,
          }));

          // Only update and re-render if the fetched data differs to prevent flicker
          setCuratedPresets((prev) => {
            if (JSON.stringify(prev) !== JSON.stringify(mapped)) {
              return mapped;
            }
            return prev;
          });

          // Update cache asynchronously
          AsyncStorage.setItem(CACHE_KEY, JSON.stringify(mapped)).catch(() => {});
        }
      } catch (e) {
        AppLogger.error('[SK8Lytz Picks] Exception fetching from DB', e);
      } finally {
        setPicksLoading(false);
      }
    };

    // 1. Instantly load from cache to populate UI
    loadFromCache().then(() => {
      // 2. Perform background revalidation
      fetchPicks();
    });
  }, []);

  return { curatedPresets, picksLoading };
}
