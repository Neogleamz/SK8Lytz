import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useState } from 'react';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
import type { IFavoriteState, IQuickPreset } from '../types/dashboard.types';

// Shared Storage Prefix constant
const STORAGE_PREFIX = '@Sk8lytz_';

export type FavoritesPromptState = 'HIDDEN' | 'NAMING_FAVORITE' | 'NAMING_PRESET';

export function useFavorites() {
  const [favorites, setFavorites] = useState<IFavoriteState[]>([]);
  const [activeFavoriteId, setActiveFavoriteId] = useState<string | null>(null);

  const [quickPresets, setQuickPresets] = useState<IQuickPreset[]>([
    { name: 'Rainbow', colors: ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#9400D3'], type: 3 },
    { name: 'America', colors: ['#FF0000', '#FFFFFF', '#0000FF'], type: 3 },
    { name: 'Cyberpunk', colors: ['#00FFFF', '#FF00FF', '#FFFF00'], type: 3 },
    { name: 'Forest', colors: ['#00FF00', '#008000', '#228B22', '#32CD32'], type: 1 },
    { name: 'Sunset', colors: ['#FF0000', '#FF4500', '#FF8C00', '#FFA500'], type: 1 },
    { name: 'Ice', colors: ['#FFFFFF', '#E0FFFF', '#00FFFF', '#0000FF'], type: 1 },
    { name: 'Custom 1', colors: ['#000000', '#FFFFFF', '#000000'], type: 3 },
    { name: 'Custom 2', colors: ['#000000', '#FFFFFF', '#000000'], type: 3 }
  ]);

  const [promptState, setPromptState] = useState<FavoritesPromptState>('HIDDEN');
  const [promptName, setPromptName] = useState('');
  const [favPromptTargetId, setFavPromptTargetId] = useState<string | null>(null);
  const [quickPromptTargetIndex, setQuickPromptTargetIndex] = useState(-1);
  const [activeQuickPresetIndex, setActiveQuickPresetIndex] = useState<number | null>(null);

  // Initialize from AsyncStorage and Cloud
  useEffect(() => {
    let localFavorites: IFavoriteState[] = [];

    // 1. Fetch Local
    AsyncStorage.getItem(`${STORAGE_PREFIX}Favorites`).then((saved) => {
      if (saved) {
        try {
          const parsed: IFavoriteState[] = JSON.parse(saved);
          if (parsed && parsed.length > 0) {
            localFavorites = parsed.map(f => {
              let nf = { ...f };
              if (nf.mode === 'DIY' || nf.mode === 'MULTI' || nf.mode === 'MULTICOLOR') {
                nf.mode = 'BUILDER';
              }
              if (nf.mode === 'RBM' || nf.mode === 'PROGRAMS') {
                nf.mode = 'PATTERN';
                nf.patternId = Math.min(nf.patternId ?? 1, 28);
              }
              return nf;
            });
            setFavorites(localFavorites);
          }
        } catch (e) { AppLogger.warn('[Favorites] Failed to parse saved favorites', { error: String(e) }); }
      }
    }).finally(() => {
      // 2. Fetch Cloud and merge
      supabase.auth.getUser().then(async ({ data: userData }) => {
        if (!userData?.user) return;
        try {
          const { data, error } = await supabase
            .from('user_saved_presets')
            .select('*')
            .eq('user_id', userData.user.id)
            .eq('fill_mode', 'FAVORITE');

          if (!error && data) {
            const cloudFavs = (data as any[]).map(d => ({
              id: d.id,
              name: d.name,
              ...(typeof d.nodes === 'string' ? JSON.parse(d.nodes) : d.nodes)
            })) as IFavoriteState[];

            // Merge local and cloud (cloud wins on ID collision)
            const mergedMap = new Map<string, IFavoriteState>();
            localFavorites.forEach(f => mergedMap.set(f.id, f));
            cloudFavs.forEach(f => mergedMap.set(f.id, f));
            
            const finalFavs = Array.from(mergedMap.values());
            setFavorites(finalFavs);
            await AsyncStorage.setItem(`${STORAGE_PREFIX}Favorites`, JSON.stringify(finalFavs));
          }
        } catch (err) {
          AppLogger.warn('[Favorites] Failed to fetch cloud favorites', { error: String(err) });
        }
      });
    });

    AsyncStorage.getItem(`${STORAGE_PREFIX}QuickPresets`).then((saved) => {
      if (saved) {
        try {
          const parsed = JSON.parse(saved);
          if (parsed && parsed.length > 0) setQuickPresets(parsed);
        } catch (e) { AppLogger.warn('[Favorites] Failed to parse quick presets', { error: String(e) }); }
      }
    });
  }, []);

  const openFavoritePrompt = useCallback((targetId?: string, defaultName: string = '') => {
    setFavPromptTargetId(targetId || null);
    setPromptName(defaultName);
    setPromptState('NAMING_FAVORITE');
  }, []);

  const openPresetPrompt = useCallback((targetIndex: number, defaultName: string = 'Preset') => {
    setQuickPromptTargetIndex(targetIndex);
    setPromptName(defaultName);
    setPromptState('NAMING_PRESET');
  }, []);

  const closePrompt = useCallback(() => {
    setPromptState('HIDDEN');
    setPromptName('');
    setFavPromptTargetId(null);
    setQuickPromptTargetIndex(-1);
  }, []);

  const saveFavorite = useCallback(async (capturedState: Partial<IFavoriteState>, name: string) => {
    const id = favPromptTargetId || `fav_${Date.now()}_${Math.random().toString(36).substring(2,9)}`;
    const newFav = {
      id,
      name,
      ...capturedState
    } as IFavoriteState;

    let newFavorites: IFavoriteState[];
    if (favPromptTargetId) {
      newFavorites = favorites.map(f => f.id === favPromptTargetId ? newFav : f);
    } else {
      newFavorites = [...favorites, newFav];
    }

    // 1. Save Local
    setFavorites(newFavorites);
    await AsyncStorage.setItem(`${STORAGE_PREFIX}Favorites`, JSON.stringify(newFavorites));
    
    // 2. Save Cloud
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (user) {
        // Strip out id/name from capturedState so we just store the pure payload in nodes
        const { id: _id, name: _name, ...payload } = newFav;
        await supabase.from('user_saved_presets').upsert({
          id,
          user_id: user.id,
          name,
          fill_mode: 'FAVORITE',
          transition_type: 0,
          nodes: payload as any,
          created_at: new Date().toISOString()
        } as any);
      }
    } catch (err) {
      AppLogger.warn('[Favorites] Cloud save failed', { error: String(err) });
    }

    closePrompt();
  }, [favorites, favPromptTargetId, closePrompt]);

  const deleteFavorite = useCallback(async (id: string) => {
    const newFavorites = favorites.filter(f => f.id !== id);
    setFavorites(newFavorites);
    if (activeFavoriteId === id) setActiveFavoriteId(null);
    
    // 1. Delete Local
    await AsyncStorage.setItem(`${STORAGE_PREFIX}Favorites`, JSON.stringify(newFavorites));
    
    // 2. Delete Cloud
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (user) {
        await supabase.from('user_saved_presets').delete().eq('id', id).eq('user_id', user.id);
      }
    } catch (err) {
      AppLogger.warn('[Favorites] Cloud delete failed', { error: String(err) });
    }
  }, [favorites, activeFavoriteId]);

  const saveQuickPreset = useCallback(async (index: number, preset: IQuickPreset) => {
    const newArr = [...quickPresets];
    newArr[index] = preset;
    setQuickPresets(newArr);
    await AsyncStorage.setItem(`${STORAGE_PREFIX}QuickPresets`, JSON.stringify(newArr));
    closePrompt();
  }, [quickPresets, closePrompt]);

  return {
    favorites,
    setFavorites,
    activeFavoriteId,
    setActiveFavoriteId,
    quickPresets,
    setQuickPresets,
    activeQuickPresetIndex,
    setActiveQuickPresetIndex,
    promptState,
    promptName,
    setPromptName,
    favPromptTargetId,
    quickPromptTargetIndex,
    openFavoritePrompt,
    openPresetPrompt,
    closePrompt,
    saveFavorite,
    deleteFavorite,
    saveQuickPreset
  };
}
