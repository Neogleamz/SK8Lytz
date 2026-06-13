import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useState } from 'react';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
import type { Database } from '../types/supabase';
import type { IFavoriteState, IQuickPreset } from '../types/dashboard.types';

import { STORAGE_FAVORITES, STORAGE_QUICK_PRESETS } from '../constants/storageKeys';

export type FavoritesPromptState = 'HIDDEN' | 'NAMING_FAVORITE' | 'NAMING_PRESET';

import { useAuth } from '../context/AuthContext';

export function useFavorites() {
  const { user } = useAuth();
  const [favorites, setFavorites] = useState<IFavoriteState[]>([]);
  const [activeFavoriteId, setActiveFavoriteId] = useState<string | null>(null);
  const [status, setStatus] = useState<'idle' | 'loading' | 'success' | 'error'>('loading');
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

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
    let active = true;
    let localFavorites: IFavoriteState[] = [];
    setStatus('loading');
    setErrorMsg(null);

    Promise.all([
      // 1. Fetch Local
      AsyncStorage.getItem(STORAGE_FAVORITES).then(async (saved) => {
        if (!active) return;
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
              if (active) setFavorites(localFavorites);
            }
          } catch (e: unknown) {
            AppLogger.warn('[Favorites] Failed to parse saved favorites', { error: (e instanceof Error ? e.message : String(e)) });
          }
        }

        // 2. Fetch Cloud and merge
        if (user) {
          try {
            const { data, error: cloudErr } = await supabase
              .from('user_saved_presets')
              .select('*')
              .eq('user_id', user.id)
              .eq('fill_mode', 'FAVORITE');
            
            if (!active) return;
            
            if (!cloudErr && data) {
              const cloudFavs = data.map(d => ({
                id: d.id,
                name: d.name,
                ...(typeof d.nodes === 'string' ? JSON.parse(d.nodes) : d.nodes as Record<string, unknown>)
              })) as IFavoriteState[];

              // Merge local and cloud (cloud wins on ID collision)
              const mergedMap = new Map<string, IFavoriteState>();
              localFavorites.forEach(f => mergedMap.set(f.id, f));
              cloudFavs.forEach(f => mergedMap.set(f.id, f));
              
              const finalFavs = Array.from(mergedMap.values());
              if (active) {
                setFavorites(finalFavs);
                AsyncStorage.setItem(STORAGE_FAVORITES, JSON.stringify(finalFavs)).catch((err: unknown) => AppLogger.warn('[useFavorites] Failed to persist favorites', err instanceof Error ? err.message : String(err)));
              }
            }
          } catch (err: unknown) {
            AppLogger.warn('[Favorites] Failed to fetch cloud favorites', { error: (err instanceof Error ? err.message : String(err)) });
          }
        }
      }).catch((err: unknown) => {
        if (!active) return;
        const msg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[useFavorites] Favorites read failed', { error: msg });
        setErrorMsg(msg);
        setStatus('error');
      }),

      AsyncStorage.getItem(STORAGE_QUICK_PRESETS).then((saved) => {
        if (!active) return;
        if (saved) {
          try {
            const parsed = JSON.parse(saved);
            if (parsed && parsed.length > 0 && active) setQuickPresets(parsed);
          } catch (e: unknown) {
            AppLogger.warn('[Favorites] Failed to parse quick presets', { error: (e instanceof Error ? e.message : String(e)) });
          }
        }
      }).catch((err: unknown) => {
        if (!active) return;
        const msg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[useFavorites] QuickPresets read failed', { error: msg });
        setErrorMsg(msg);
        setStatus('error');
      })
    ]).finally(() => {
      if (active) setStatus('success');
    });

    return () => {
      active = false;
    };
  }, [user]);

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
    try {
      await AsyncStorage.setItem(STORAGE_FAVORITES, JSON.stringify(newFavorites));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[Favorites] Local save failed', { error: msg });
    }
    
    // 2. Save Cloud
    if (user) {
      // Strip out id/name from capturedState so we just store the pure payload in nodes
      const { id: _id, name: _name, ...payload } = newFav;
      supabase.from('user_saved_presets').upsert({
        id,
        user_id: user.id,
        name,
        fill_mode: 'FAVORITE',
        transition_type: 0,
        nodes: payload as Database['public']['Tables']['user_saved_presets']['Insert']['nodes'],
        created_at: new Date().toISOString()
      }).then(({ error }) => {
        if (error) throw error;
      }).catch((err: unknown) => {
        const msg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[Favorites] Cloud save failed', { error: msg });
      });
    }

    closePrompt();
  }, [favorites, favPromptTargetId, closePrompt]);

  const deleteFavorite = useCallback(async (id: string) => {
    const newFavorites = favorites.filter(f => f.id !== id);
    setFavorites(newFavorites);
    if (activeFavoriteId === id) setActiveFavoriteId(null);
    
    // 1. Delete Local
    try {
      await AsyncStorage.setItem(STORAGE_FAVORITES, JSON.stringify(newFavorites));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[Favorites] Local delete failed', { error: msg });
    }
    
    // 2. Delete Cloud
    if (user) {
      supabase.from('user_saved_presets').delete().eq('id', id).eq('user_id', user.id).then(({ error }) => {
        if (error) throw error;
      }).catch((err: unknown) => {
        const msg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[Favorites] Cloud delete failed', { error: msg });
      });
    }
  }, [favorites, activeFavoriteId]);

  const saveQuickPreset = useCallback(async (index: number, preset: IQuickPreset) => {
    const newArr = [...quickPresets];
    newArr[index] = preset;
    setQuickPresets(newArr);
    try {
      await AsyncStorage.setItem(STORAGE_QUICK_PRESETS, JSON.stringify(newArr));
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[Favorites] Local quick preset save failed', { error: msg });
    }
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
    saveQuickPreset,
    isLoading: status === 'loading',
    error: status === 'error' ? errorMsg : null
  };
}
