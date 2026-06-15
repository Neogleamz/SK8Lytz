import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_FAVORITES } from '../constants/storageKeys';
import type { Database } from '../types/supabase';
import type { IFavoriteState } from '../types/dashboard.types';
import { AppLogger } from './appLogger';

const LOCAL_FAVORITES_KEY = STORAGE_FAVORITES;

class FavoritesServiceClass {
  private isSyncing = false;

  async getFavorites(userId?: string): Promise<IFavoriteState[]> {
    let localFavs: IFavoriteState[] = [];

    // 1. Read Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_FAVORITES_KEY);
      if (localData) {
        const parsed = JSON.parse(localData);
        if (Array.isArray(parsed)) {
          localFavs = parsed.map(f => {
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
        }
      }
    } catch (e: unknown) {
      AppLogger.warn('[FavoritesService] Local read fail', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
    }

    // 2. Background Sync
    const syncCloud = async () => {
      if (this.isSyncing || !userId) return;
      this.isSyncing = true;
      try {
        let cloudFavs: IFavoriteState[] = [];
        const { data, error } = await supabase
          .from('user_saved_presets')
          .select('*')
          .eq('user_id', userId)
          .eq('fill_mode', 'FAVORITE');

        if (!error && data) {
          cloudFavs = data.map(d => ({
            id: d.id,
            name: d.name,
            ...(typeof d.nodes === 'string' ? JSON.parse(d.nodes) : d.nodes as Record<string, unknown>)
          })) as IFavoriteState[];
        }

        // Merge local and cloud (cloud wins on ID collision)
        const mergedMap = new Map<string, IFavoriteState>();
        localFavs.forEach(f => mergedMap.set(f.id, f));
        cloudFavs.forEach(f => mergedMap.set(f.id, f));
        
        const finalFavs = Array.from(mergedMap.values());
        try {
          await AsyncStorage.setItem(LOCAL_FAVORITES_KEY, JSON.stringify(finalFavs));
        } catch (e: unknown) {
          AppLogger.warn('[FavoritesService] Failed to write favorites cache', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
        }
      } catch (err: unknown) {
        AppLogger.warn('[FavoritesService] Cloud sync fail', { error: (err instanceof Error ? err.message : String(err)), payload_size: 0, ssi: 0 });
      } finally {
        this.isSyncing = false;
      }
    };

    syncCloud(); // Fire and forget

    return localFavs;
  }

  async saveFavorite(favorite: IFavoriteState, userId?: string): Promise<boolean> {
    // 1. Save Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_FAVORITES_KEY);
      let userFavs: IFavoriteState[] = localData ? JSON.parse(localData) : [];
      
      const idx = userFavs.findIndex(f => f.id === favorite.id);
      if (idx >= 0) {
        userFavs[idx] = favorite;
      } else {
        userFavs.push(favorite);
      }
      await AsyncStorage.setItem(LOCAL_FAVORITES_KEY, JSON.stringify(userFavs));
    } catch (e: unknown) {
      AppLogger.warn('[FavoritesService] saveLocalFavorite error', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
    }

    // 2. Save Cloud directly (no sync queue for this iteration unless specified)
    if (userId) {
      const { id, name, ...payload } = favorite;
      try {
        const { error } = await supabase.from('user_saved_presets').upsert({
          id,
          user_id: userId,
          name,
          fill_mode: 'FAVORITE',
          transition_type: 0,
          nodes: payload as Database['public']['Tables']['user_saved_presets']['Insert']['nodes'],
          created_at: new Date().toISOString()
        });
        if (error) throw error;
      } catch (err: unknown) {
        AppLogger.warn('[FavoritesService] Cloud save failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
      }
    }

    return true;
  }

  async deleteFavorite(id: string, userId?: string): Promise<boolean> {
    // 1. Delete Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_FAVORITES_KEY);
      let userFavs: IFavoriteState[] = localData ? JSON.parse(localData) : [];
      userFavs = userFavs.filter(f => f.id !== id);
      await AsyncStorage.setItem(LOCAL_FAVORITES_KEY, JSON.stringify(userFavs));
    } catch (e: unknown) {
      AppLogger.warn('[FavoritesService] deleteLocalFavorite error', { error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
    }

    // 2. Delete Cloud
    if (userId) {
      try {
        const { error } = await supabase.from('user_saved_presets').delete().eq('id', id).eq('user_id', userId);
        if (error) throw error;
      } catch (err: unknown) {
        AppLogger.warn('[FavoritesService] Cloud delete failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
      }
    }

    return true;
  }
}

export const FavoritesService = new FavoritesServiceClass();
