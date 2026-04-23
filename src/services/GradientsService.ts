import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { BuilderNode, CustomBuilderPreset } from '../protocols/PositionalMathBuffer';

const LOCAL_GRADIENTS_KEY = '@Sk8lytz_Builder_Presets';



class GradientsServiceClass {
  async getSavedGradients(userId?: string): Promise<CustomBuilderPreset[]> {
    let globalPresets: CustomBuilderPreset[] = [];
    let userCloudPresets: CustomBuilderPreset[] = [];
    
    // 1. Fetch Globals (custom_builder_presets)
    try {
      const { data, error } = await supabase
        .from('custom_builder_presets')
        .select('*')
        .neq('fill_mode', 'SCENE');
        
      if (!error && data) {
        globalPresets = (data as any as CustomBuilderPreset[]).filter(p => p && p.id && p.name && Array.isArray(p.nodes));
      }
    } catch (err) {
      AppLogger.warn('GRADIENT_SYNC_FAIL' as any, err);
    }

    // 2. Fetch User Cloud (user_saved_presets)
    if (userId) {
      try {
        const { data, error } = await supabase
          .from('user_saved_presets')
          .select('*')
          .eq('user_id', userId)
          .neq('fill_mode', 'SCENE');
          
        if (!error && data) {
          userCloudPresets = (data as any as CustomBuilderPreset[]).filter(p => p && p.id && p.name && Array.isArray(p.nodes));
        }
      } catch (err) {
        AppLogger.warn('GRADIENT_SYNC_FAIL' as any, err);
      }
    }
    
    // 3. Read Local (only the user's private saves, globals aren't cached locally to allow OTA updates)
    let localPresets: CustomBuilderPreset[] = [];
    try {
      const localData = await AsyncStorage.getItem(LOCAL_GRADIENTS_KEY);
      if (localData) {
        const parsed = JSON.parse(localData);
        if (Array.isArray(parsed)) {
          localPresets = parsed.filter(p => p && p.id && p.name && Array.isArray(p.nodes));
        }
      }
    } catch (e) {
      AppLogger.error('GRADIENT_LOCAL_READ_FAIL' as any, e);
    }
    
    // 4. Merge deduplicating by ID 
    const mergedMap = new Map<string, CustomBuilderPreset>();
    localPresets.forEach(p => mergedMap.set(p.id, p));
    userCloudPresets.forEach(p => mergedMap.set(p.id, p)); // User cloud wins over local
    
    // Update Local cache with merged user results
    const finalUserSaved = Array.from(mergedMap.values());
    try {
      await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(finalUserSaved));
    } catch (e) {}

    // Globals are appended to the top but NOT cached to local storage
    return [...globalPresets, ...finalUserSaved];
  }

  async saveGradient(preset: Partial<CustomBuilderPreset>, userId?: string): Promise<CustomBuilderPreset> {
    const isNew = !preset.id;
    const newPreset: CustomBuilderPreset = {
      id: preset.id || `cloud_${Date.now()}_${Math.random().toString(36).substring(2,9)}`,
      name: preset.name || 'Unnamed Gradient',
      nodes: preset.nodes || [],
      fill_mode: preset.fill_mode || 'GRADIENT',
      transition_type: preset.transition_type || 0,
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
    } catch (e) {
      AppLogger.error('GRADIENT_LOCAL_SAVE_FAIL' as any, e);
    }

    // 2. Save Cloud (user_saved_presets)
    if (userId) {
      try {
        const { error } = await supabase.from('user_saved_presets').upsert({ ...newPreset, created_at: new Date().toISOString() } as any);
        if (error) throw error;
      } catch (err) {
        AppLogger.warn('GRADIENT_CLOUD_SAVE_FAIL' as any, err);
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
    } catch (e) {
      AppLogger.error('GRADIENT_LOCAL_DEL_FAIL' as any, e);
    }

    // 2. Delete Cloud (user_saved_presets)
    if (userId) {
      try {
        await supabase.from('user_saved_presets').delete().eq('id', id);
      } catch (err) {
        AppLogger.warn('GRADIENT_CLOUD_DEL_FAIL' as any, err);
      }
    }

    AppLogger.log('BUILDER_PRESET_DELETED', { id });
  }
}

export const GradientsService = new GradientsServiceClass();
