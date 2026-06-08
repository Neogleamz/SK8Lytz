import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import type { Database } from '../types/supabase';
import { BuilderNode, CustomBuilderPreset } from '../protocols/PositionalMathBuffer';

const LOCAL_GRADIENTS_KEY = '@Sk8lytz_Builder_Presets';



class GradientsServiceClass {
  async getSavedGradients(userId?: string): Promise<CustomBuilderPreset[]> {
    let localPresets: CustomBuilderPreset[] = [];
    
    // 1. Read Local (now caches globals as well for offline support)
    try {
      const localData = await AsyncStorage.getItem(LOCAL_GRADIENTS_KEY);
      if (localData) {
        const parsed = JSON.parse(localData);
        if (Array.isArray(parsed)) {
          localPresets = parsed.filter(p => p && p.id && p.name && Array.isArray(p.nodes));
        }
      }
    } catch (e: unknown) {
      AppLogger.error('GRADIENT_LOCAL_READ_FAIL', e instanceof Error ? e.message : String(e));
    }

    // 2. Background Sync
    const syncCloud = async () => {
      let globalPresets: CustomBuilderPreset[] = [];
      let userCloudPresets: CustomBuilderPreset[] = [];
      
      try {
        const { data, error } = await supabase
          .from('custom_builder_presets')
          .select('*')
          .neq('fill_mode', 'SCENE')
          .returns<CustomBuilderPreset[]>();
          
        if (!error && data) {
          globalPresets = data.filter(p => p && p.id && p.name && Array.isArray(p.nodes));
        }
      } catch (err: unknown) {
        AppLogger.warn('GRADIENT_SYNC_FAIL', err instanceof Error ? err.message : String(err));
      }

      if (userId) {
        try {
          const { data, error } = await supabase
            .from('user_saved_presets')
            .select('*')
            .eq('user_id', userId)
            .neq('fill_mode', 'SCENE')
            .returns<CustomBuilderPreset[]>();
            
          if (!error && data) {
            userCloudPresets = data.filter(p => p && p.id && p.name && Array.isArray(p.nodes));
          }
        } catch (err: unknown) {
          AppLogger.warn('GRADIENT_SYNC_FAIL', err instanceof Error ? err.message : String(err));
        }
      }

      // Merge and update Local cache
      const mergedMap = new Map<string, CustomBuilderPreset>();
      localPresets.forEach(p => mergedMap.set(p.id, p));
      globalPresets.forEach(p => mergedMap.set(p.id, p));
      userCloudPresets.forEach(p => mergedMap.set(p.id, p)); // User cloud wins over local/global
      
      const finalMerged = Array.from(mergedMap.values());
      try {
        await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(finalMerged));
      } catch (e: unknown) {}
    };

    syncCloud(); // Fire and forget

    return localPresets;
  }

  async saveGradient(preset: Partial<CustomBuilderPreset>, userId?: string): Promise<CustomBuilderPreset> {
    const isNew = !preset.id;
    const newPreset: CustomBuilderPreset = {
      id: preset.id || `cloud_${Date.now()}_${Math.random().toString(36).substring(2,9)}`,
      name: preset.name || 'Unnamed Gradient',
      nodes: preset.nodes || [],
      fill_mode: preset.fill_mode || 'GRADIENT',
      transition_type: preset.transition_type || 0x01, // 0x01 = Static fallback — never dispatch 0x00 (undefined opcode)
      user_id: userId
    };

    // 1. Save Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_GRADIENTS_KEY);
      let userPresets: CustomBuilderPreset[] = localData ? JSON.parse(localData) : [];
      
      const existingIdx = userPresets.findIndex(p => p.id === newPreset.id);
      if (existingIdx >= 0) {
        userPresets[existingIdx] = newPreset;
      } else {
        userPresets.push(newPreset);
      }
      await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(userPresets));
    } catch (e: unknown) {
      AppLogger.error('GRADIENT_LOCAL_SAVE_FAIL', e instanceof Error ? e.message : String(e));
    }

    // 2. Save Cloud (user_saved_presets)
    if (userId) {
      try {
        const payload: Database['public']['Tables']['user_saved_presets']['Insert'] = {
          id: newPreset.id,
          name: newPreset.name,
          nodes: newPreset.nodes as unknown as Database['public']['Tables']['user_saved_presets']['Insert']['nodes'],
          fill_mode: newPreset.fill_mode || 'GRADIENT',
          transition_type: newPreset.transition_type || 0x01,
          user_id: userId,
          created_at: new Date().toISOString()
        };
        const { error } = await supabase.from('user_saved_presets').upsert(payload);
        if (error) throw error;
      } catch (err: unknown) {
        AppLogger.warn('GRADIENT_CLOUD_SAVE_FAIL', err instanceof Error ? err.message : String(err));
      }
    }

    AppLogger.log('BUILDER_PRESET_SAVED', { id: newPreset.id, name: newPreset.name });
    return newPreset;
  }

  async deleteGradient(id: string, userId?: string): Promise<void> {
    // 1. Delete Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_GRADIENTS_KEY);
      let userPresets: CustomBuilderPreset[] = localData ? JSON.parse(localData) : [];
      userPresets = userPresets.filter(p => p.id !== id);
      await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(userPresets));
    } catch (e: unknown) {
      AppLogger.error('GRADIENT_LOCAL_DEL_FAIL', e instanceof Error ? e.message : String(e));
    }

    // 2. Delete Cloud (user_saved_presets)
    if (userId) {
      try {
        await supabase.from('user_saved_presets').delete().eq('id', id);
      } catch (err: unknown) {
        AppLogger.warn('GRADIENT_CLOUD_DEL_FAIL', err instanceof Error ? err.message : String(err));
      }
    }

    AppLogger.log('BUILDER_PRESET_DELETED', { id });
  }
}

export const GradientsService = new GradientsServiceClass();
