import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useState } from 'react';
import { AppLogger } from '../services/AppLogger';
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

  // Initialize from AsyncStorage
  useEffect(() => {
    // Note: getRbmPatternName(3) logic was here. To keep this hook pure,
    // we use a generic default if none exists, or the caller can inject logic.
    AsyncStorage.getItem(`${STORAGE_PREFIX}Favorites`).then((saved) => {
      if (saved) {
        try {
          const parsed: IFavoriteState[] = JSON.parse(saved);
          if (parsed && parsed.length > 0) {
            let needsMigration = false;
            const migrated = parsed.map(f => {
              if (f.mode === 'DIY' || f.mode === 'MULTI' || f.mode === 'MULTICOLOR') {
                needsMigration = true; return { ...f, mode: 'BUILDER' };
              }
              if (f.mode === 'RBM' || f.mode === 'PROGRAMS') {
                // Silently migrate retired PROGRAMS/RBM favorites to PATTERN (v2.8.0)
                needsMigration = true; return { ...f, mode: 'PATTERN', patternId: Math.min(f.patternId ?? 1, 28) };
              }
              return f;
            });
            if (needsMigration) {
              AsyncStorage.setItem(`${STORAGE_PREFIX}Favorites`, JSON.stringify(migrated));
            }
            setFavorites(migrated);
          } else {
            setFavorites([]);
          }
        } catch (e) { AppLogger.warn('[Favorites] Failed to parse saved favorites', { error: String(e) }); }
      }
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
    const newFav = {
      id: favPromptTargetId || Date.now().toString(),
      name,
      ...capturedState
    } as IFavoriteState;

    let newFavorites: IFavoriteState[];
    if (favPromptTargetId) {
      newFavorites = favorites.map(f => f.id === favPromptTargetId ? newFav : f);
    } else {
      newFavorites = [...favorites, newFav];
    }

    setFavorites(newFavorites);
    await AsyncStorage.setItem(`${STORAGE_PREFIX}Favorites`, JSON.stringify(newFavorites));
    closePrompt();
  }, [favorites, favPromptTargetId, closePrompt]);

  const deleteFavorite = useCallback(async (id: string) => {
    const newFavorites = favorites.filter(f => f.id !== id);
    setFavorites(newFavorites);
    if (activeFavoriteId === id) setActiveFavoriteId(null);
    await AsyncStorage.setItem(`${STORAGE_PREFIX}Favorites`, JSON.stringify(newFavorites));
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
