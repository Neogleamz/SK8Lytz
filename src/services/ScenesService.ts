import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { SceneStep } from '../hooks/useSceneBuilder';

export interface Scene {
  id: string;
  name: string;
  steps: SceneStep[];
  user_id?: string;
  created_at: string;
}

export interface ICloudScene {
  id: string;
  created_at: string;
  author_id: string;
  author_username: string;
  name: string;
  scene_payload: Scene;
  downloads: number;
  upvotes: number;
  is_public: boolean;
}

const LOCAL_SCENES_KEY = '@Sk8lytz_Scenes';

class ScenesServiceClass {
  
  /**
   * Fetches the global community scenes.
   */
  async getPublicScenes(limit: number = 50, offset: number = 0): Promise<ICloudScene[]> {
    try {
      const { data, error } = await supabase
        .from('shared_scenes')
        .select('*')
        .eq('is_public', true)
        .order('upvotes', { ascending: false })
        .order('created_at', { ascending: false })
        .range(offset, offset + limit - 1);

      if (error) throw error;
      return data as unknown as ICloudScene[];
    } catch (e) {
      console.error('[ScenesService] getPublicScenes error:', e);
      return [];
    }
  }

  /**
   * Fetches scenes authored by the current authenticated user.
   */
  async getMyScenes(): Promise<ICloudScene[]> {
    try {
      const { data: userData, error: userError } = await supabase.auth.getUser();
      if (userError || !userData?.user) throw new Error('Not authenticated');

      const { data, error } = await supabase
        .from('shared_scenes')
        .select('*')
        .eq('author_id', userData.user.id)
        .order('created_at', { ascending: false });

      if (error) throw error;
      return data as unknown as ICloudScene[];
    } catch (e) {
      console.error('[ScenesService] getMyScenes error:', e);
      return [];
    }
  }

  /**
   * Publishes or saves a scene to the cloud.
   */
  async publishScene(name: string, payload: any, isPublic: boolean = false): Promise<boolean> {
    try {
      const { data: userData, error: userError } = await supabase.auth.getUser();
      if (userError || !userData?.user) {
        console.warn('[ScenesService] Skipping cloud publish (not logged in)');
        return false;
      }

      const username = userData.user.user_metadata?.username || 'Anonymous Skater';

      const { error } = await supabase
        .from('shared_scenes')
        .insert([{
          author_id: userData.user.id,
          author_username: username,
          name: name,
          scene_payload: payload,
          is_public: isPublic
        }]);

      if (error) throw error;
      return true;
    } catch (e) {
      console.error('[ScenesService] publishScene error:', e);
      return false;
    }
  }

  /**
   * Deletes a scene you own
   */
  async deleteScene(sceneId: string): Promise<boolean> {
     try {
       const { error } = await supabase
         .from('shared_scenes')
         .delete()
         .eq('id', sceneId);
       
       if (error) throw error;
       return true;
     } catch (e) {
       console.error('[ScenesService] deleteScene error:', e);
       return false;
     }
  }

  /**
   * Upvote a scene via the secure RPC function.
   */
  async upvoteScene(sceneId: string): Promise<boolean> {
     try {
       const { error } = await supabase.rpc('increment_scene_upvote', { scene_id: sceneId });
       if (error) throw error;
       return true;
     } catch (e) {
       console.error('[ScenesService] upvoteScene error:', e);
       return false;
     }
  }

  /**
   * Register a download via the secure RPC function.
   */
  async downloadScene(sceneId: string): Promise<boolean> {
     try {
       const { error } = await supabase.rpc('increment_scene_download', { scene_id: sceneId });
       if (error) throw error;
       return true;
     } catch (e) {
       console.error('[ScenesService] downloadScene error:', e);
       return false;
     }
  }

  /**
   * Get all user and global saved scenes.
   */
  async getSavedScenes(userId?: string): Promise<Scene[]> {
    let globalScenes: Scene[] = [];
    let userCloudScenes: Scene[] = [];
    
    // 1. Fetch Globals (custom_builder_presets)
    try {
      const { data, error } = await supabase
        .from('custom_builder_presets')
        .select('*')
        .eq('fill_mode', 'SCENE');
        
      if (!error && data) {
        globalScenes = (data as any[]).map(p => ({
          id: p.id,
          name: p.name,
          steps: Array.isArray(p.nodes) ? p.nodes : [],
          created_at: p.created_at,
          user_id: p.user_id
        }));
      }
    } catch (err) {
      console.warn('[ScenesService] Global sync fail:', err);
    }

    // 2. Fetch User Cloud (user_saved_presets)
    if (userId) {
      try {
        const { data, error } = await supabase
          .from('user_saved_presets')
          .select('*')
          .eq('user_id', userId)
          .eq('fill_mode', 'SCENE');
          
        if (!error && data) {
          userCloudScenes = (data as any[]).map(p => ({
            id: p.id,
            name: p.name,
            steps: Array.isArray(p.nodes) ? p.nodes : [],
            created_at: p.created_at,
            user_id: p.user_id
          }));
        }
      } catch (err) {
        console.warn('[ScenesService] User cloud sync fail:', err);
      }
    }

    // 3. Read Local (only the user's private saves)
    let localScenes: Scene[] = [];
    try {
      const localData = await AsyncStorage.getItem(LOCAL_SCENES_KEY);
      if (localData) {
        const parsed = JSON.parse(localData);
        if (Array.isArray(parsed)) {
          localScenes = parsed;
        }
      }
    } catch (e) {
      console.error('[ScenesService] Local read fail:', e);
    }

    // 4. Merge deduplicating by ID 
    const mergedMap = new Map<string, Scene>();
    localScenes.forEach(s => mergedMap.set(s.id, s));
    userCloudScenes.forEach(s => mergedMap.set(s.id, s)); // User cloud wins over local
    
    // Update Local cache with merged user results
    const finalUserSaved = Array.from(mergedMap.values());
    try {
      await AsyncStorage.setItem(LOCAL_SCENES_KEY, JSON.stringify(finalUserSaved));
    } catch (e) {}

    // Globals are appended to the top but NOT cached to local storage
    return [...globalScenes, ...finalUserSaved];
  }

  /**
   * Save a scene locally and to user_saved_presets cloud.
   */
  async saveScene(scene: Scene, userId?: string): Promise<boolean> {
    // 1. Save Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_SCENES_KEY);
      let userScenes: Scene[] = localData ? JSON.parse(localData) : [];
      
      const idx = userScenes.findIndex(s => s.id === scene.id);
      if (idx >= 0) {
        userScenes[idx] = scene;
      } else {
        userScenes.push(scene);
      }
      await AsyncStorage.setItem(LOCAL_SCENES_KEY, JSON.stringify(userScenes));
    } catch (e) {
      console.error('[ScenesService] saveLocalScene error:', e);
    }

    // 2. Save Cloud (user_saved_presets)
    if (userId) {
      try {
        const { error } = await supabase.from('user_saved_presets').upsert({
          id: scene.id,
          name: scene.name,
          nodes: scene.steps,
          fill_mode: 'SCENE',
          transition_type: 0,
          user_id: userId,
          created_at: scene.created_at || new Date().toISOString()
        } as any);
        if (error) throw error;
      } catch (err) {
        console.warn('[ScenesService] Cloud save fail:', err);
      }
    }

    return true;
  }

  /**
   * Delete a saved scene.
   */
  async deleteSavedScene(sceneId: string, userId?: string): Promise<boolean> {
    // 1. Delete Local
    try {
      const localData = await AsyncStorage.getItem(LOCAL_SCENES_KEY);
      let userScenes: Scene[] = localData ? JSON.parse(localData) : [];
      userScenes = userScenes.filter(s => s.id !== sceneId);
      await AsyncStorage.setItem(LOCAL_SCENES_KEY, JSON.stringify(userScenes));
    } catch (e) {
      console.error('[ScenesService] deleteLocalScene error:', e);
    }

    // 2. Delete Cloud (user_saved_presets)
    if (userId) {
      try {
        await supabase.from('user_saved_presets').delete().eq('id', sceneId);
      } catch (err) {
        console.warn('[ScenesService] Cloud delete fail:', err);
      }
    }

    return true;
  }
}

export const ScenesService = new ScenesServiceClass();
