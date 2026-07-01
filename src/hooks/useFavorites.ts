import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useState } from 'react';
import { AppLogger } from '../services/appLogger';
import { FavoritesService } from '../services/FavoritesService';
import type { IFavoriteState, IQuickPreset } from '../types/dashboard.types';

import { STORAGE_QUICK_PRESETS } from '../constants/storageKeys';

export type FavoritesPromptState = 'HIDDEN' | 'NAMING_FAVORITE' | 'NAMING_PRESET';

import { useAuth } from '../context/AuthContext';

export function useFavorites() {
  const { user } = useAuth();
  const [favorites, setFavorites] = useState<IFavoriteState[]>([]);
  const [activeFavoriteId, setActiveFavoriteId] = useState<string | null>(null);
  const [status, setStatus] = useState<'idle' | 'loading' | 'success' | 'error' | 'empty'>('loading');
  const [errorMsg, setErrorMsg] = useState('');

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
    setErrorMsg('');

    Promise.all([
      // 1. Fetch Favorites via Service
      FavoritesService.getFavorites(user?.id).then((favs) => {
        if (active && favs.length > 0) {
          setFavorites(favs);
        }
      }).catch((err: unknown) => {
        if (!active) return;
        const msg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[useFavorites] Favorites read failed', { error: msg, payload_size: 0, ssi: 0 });
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
            AppLogger.warn('[Favorites] Failed to parse quick presets', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
          }
        }
      }).catch((err: unknown) => {
        if (!active) return;
        const msg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[useFavorites] QuickPresets read failed', { error: msg, payload_size: 0, ssi: 0 });
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

    // Save via Service
    setFavorites(newFavorites);
    FavoritesService.saveFavorite(newFav, user?.id);

    closePrompt();
  }, [favorites, favPromptTargetId, closePrompt, user]);

  const deleteFavorite = useCallback(async (id: string) => {
    const newFavorites = favorites.filter(f => f.id !== id);
    setFavorites(newFavorites);
    if (activeFavoriteId === id) setActiveFavoriteId(null);
    
    FavoritesService.deleteFavorite(id, user?.id);
  }, [favorites, activeFavoriteId, user]);

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
    status,
    errorMsg,
    // Legacy support
    isLoading: status === 'loading',
    error: status === 'error' ? errorMsg : null
  };
}
