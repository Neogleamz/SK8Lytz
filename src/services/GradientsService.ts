import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { BuilderNode, CustomBuilderPreset } from '../protocols/PositionalMathBuffer';

const LOCAL_GRADIENTS_KEY = '@Sk8lytz_Builder_Presets';

export const BUILT_IN_GRADIENTS: CustomBuilderPreset[] = [
  {
    id: 'builtin_rainbow',
    name: 'Rainbow',
    nodes: [
      { id: '1', color: '#FF0000', pos: 0.0, opacity: 1 },
      { id: '2', color: '#00FF00', pos: 0.33, opacity: 1 },
      { id: '3', color: '#0000FF', pos: 0.66, opacity: 1 },
      { id: '4', color: '#FF00FF', pos: 1.0, opacity: 1 }
    ],
    fill_mode: 'GRADIENT',
    transition_type: 0,
    created_at: new Date().toISOString()
  },
  {
    id: 'builtin_usa',
    name: 'USA',
    nodes: [
      { id: '1', color: '#FF0000', pos: 0.0, opacity: 1 },
      { id: '2', color: '#FFFFFF', pos: 0.5, opacity: 1 },
      { id: '3', color: '#0000FF', pos: 1.0, opacity: 1 }
    ],
    fill_mode: 'GRADIENT',
    transition_type: 0,
    created_at: new Date().toISOString()
  },
  {
    id: 'builtin_cyberpunk',
    name: 'Cyberpunk',
    nodes: [
      { id: '1', color: '#FF0055', pos: 0.0, opacity: 1 },
      { id: '2', color: '#00F0FF', pos: 0.5, opacity: 1 },
      { id: '3', color: '#8A2BE2', pos: 1.0, opacity: 1 }
    ],
    fill_mode: 'GRADIENT',
    transition_type: 0,
    created_at: new Date().toISOString()
  },
  {
    id: 'builtin_fire',
    name: 'Fire',
    nodes: [
      { id: '1', color: '#FF0000', pos: 0.0, opacity: 1 },
      { id: '2', color: '#FF4500', pos: 0.3, opacity: 1 },
      { id: '3', color: '#FFD700', pos: 0.7, opacity: 1 },
      { id: '4', color: '#FFFFFF', pos: 1.0, opacity: 1 }
    ],
    fill_mode: 'GRADIENT',
    transition_type: 0,
    created_at: new Date().toISOString()
  }
];

class GradientsServiceClass {
  async getSavedGradients(userId?: string): Promise<CustomBuilderPreset[]> {
    let cloudPresets: CustomBuilderPreset[] = [];
    
    // 1. Try Cloud First
    if (userId) {
      try {
        const { data, error } = await supabase
          .from('custom_builder_presets')
          .select('*')
          .eq('user_id', userId);
          
        if (!error && data) {
          cloudPresets = (data as any as CustomBuilderPreset[]).filter(p => p && p.id && p.name && Array.isArray(p.nodes));
        }
      } catch (err) {
        AppLogger.warn('GRADIENT_SYNC_FAIL', err);
      }
    }
    
    // 2. Read Local
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
      AppLogger.error('GRADIENT_LOCAL_READ_FAIL', e);
    }
    
    // 3. Merge deduplicating by ID (Cloud wins)
    const mergedMap = new Map<string, CustomBuilderPreset>();
    localPresets.forEach(p => mergedMap.set(p.id, p));
    cloudPresets.forEach(p => mergedMap.set(p.id, p));
    
    // 4. Update Local cache with merged results
    const finalSaved = Array.from(mergedMap.values());
    try {
      await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(finalSaved));
    } catch (e) {}

    // 5. Append Built-ins at the top
    return [...BUILT_IN_GRADIENTS, ...finalSaved];
  }

  async saveGradient(preset: Partial<CustomBuilderPreset>, userId?: string): Promise<CustomBuilderPreset> {
    const isNew = !preset.id;
    const newPreset: CustomBuilderPreset = {
      id: preset.id || `cloud_${Date.now()}_${Math.random().toString(36).substring(2,9)}`,
      name: preset.name || 'Unnamed Gradient',
      nodes: preset.nodes || [],
      fill_mode: preset.fill_mode || 'GRADIENT',
      transition_type: preset.transition_type || 0,
      user_id: userId,
      created_at: preset.created_at || new Date().toISOString()
    };

    // 1. Save Local
    try {
      const allPresets = await this.getSavedGradients(userId);
      const userPresets = allPresets.filter(p => !p.id.startsWith('builtin_'));
      
      const existingIdx = userPresets.findIndex(p => p.id === newPreset.id);
      if (existingIdx >= 0) {
        userPresets[existingIdx] = newPreset;
      } else {
        userPresets.push(newPreset);
      }
      await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(userPresets));
    } catch (e) {
      AppLogger.error('GRADIENT_LOCAL_SAVE_FAIL', e);
    }

    // 2. Save Cloud
    if (userId) {
      try {
        const { error } = await supabase.from('custom_builder_presets').upsert(newPreset as any);
        if (error) throw error;
      } catch (err) {
        AppLogger.warn('GRADIENT_CLOUD_SAVE_FAIL', err);
      }
    }

    AppLogger.log('GRADIENT_SAVED', { id: newPreset.id, name: newPreset.name });
    return newPreset;
  }

  async deleteGradient(id: string, userId?: string): Promise<void> {
    if (id.startsWith('builtin_')) return; // Protection

    // 1. Delete Local
    try {
      const allPresets = await this.getSavedGradients(userId);
      const userPresets = allPresets.filter(p => !p.id.startsWith('builtin_') && p.id !== id);
      await AsyncStorage.setItem(LOCAL_GRADIENTS_KEY, JSON.stringify(userPresets));
    } catch (e) {
      AppLogger.error('GRADIENT_LOCAL_DEL_FAIL', e);
    }

    // 2. Delete Cloud
    if (userId) {
      try {
        await supabase.from('custom_builder_presets').delete().eq('id', id);
      } catch (err) {
        AppLogger.warn('GRADIENT_CLOUD_DEL_FAIL', err);
      }
    }

    AppLogger.log('GRADIENT_DELETED', { id });
  }
}

export const GradientsService = new GradientsServiceClass();
