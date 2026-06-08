import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_SCENES_CACHE } from '../constants/storageKeys';
import type { Database } from '../types/supabase';
import { AppLogger } from './AppLogger';

export interface SceneStep {
  id: string;
  mode: 'pattern' | 'symphony';
  effectId: number;
  fg: string;
  bg: string;
  speed: number;
  duration: number;
  direction: 0 | 1;
}

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
const LOCAL_SCENE_SYNC_QUEUE_KEY = '@Sk8lytz_Scene_Sync_Queue';

export interface SceneSyncJob {
  id: string;
  type: 'upsert_user_scene' | 'delete_user_scene' | 'publish_community_scene';
  payload: any;
  timestamp: number;
}

class ScenesServiceClass {
  
  async getPublicScenes(limit: number = 50, offset: number = 0): Promise<ICloudScene[]> {
    // Fire-and-forget background sync
    const syncCloud = async () => {
      try {
        const { data, error } = await supabase
          .from('shared_scenes')
          .select('*')
          .eq('is_public', true)
          .order('upvotes', { ascending: false })
          .order('created_at', { ascending: false })
          .range(offset, offset + limit - 1);
        if (!error && data) {
          AsyncStorage.setItem(STORAGE_SCENES_CACHE, JSON.stringify(data)).catch(() => {});
          return data as unknown as ICloudScene[];
        }
      } catch (e) {
        AppLogger.error('[ScenesService] Background sync error', { error: String(e) });
      }
      return null;
    };

    // 1. Try Local Cache First
    try {
      const cached = await AsyncStorage.getItem(STORAGE_SCENES_CACHE);
      if (cached) {
        syncCloud(); // Launch cloud sync in background
        return JSON.parse(cached) as ICloudScene[];
      }
    } catch {
      // Ignore cache errors
    }

    // 2. Fallback to Cloud if cache empty
    return (await syncCloud()) || [];
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
      AppLogger.error('[ScenesService] getMyScenes error', { error: String(e) });
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
        AppLogger.warn('[ScenesService] Skipping cloud publish (not logged in)');
        return false;
      }

      const username = userData.user.user_metadata?.username || 'Anonymous Skater';

      const jobPayload = {
        author_id: userData.user.id,
        author_username: username,
        name: name,
        scene_payload: payload,
        is_public: isPublic
      };

      await this.enqueueSyncJob({
        id: `pub_${Date.now()}_${Math.random().toString(36).substring(7)}`,
        type: 'publish_community_scene',
        payload: jobPayload,
        timestamp: Date.now()
      });

      return true;
    } catch (e) {
      AppLogger.error('[ScenesService] publishScene error', { error: String(e) });
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
       AppLogger.error('[ScenesService] deleteScene error', { error: String(e) });
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
       AppLogger.error('[ScenesService] upvoteScene error', { error: String(e) });
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
       AppLogger.error('[ScenesService] downloadScene error', { error: String(e) });
       return false;
     }
  }

  /**
   * Get all user and global saved scenes.
   */
  async getSavedScenes(userId?: string): Promise<Scene[]> {
    let localScenes: Scene[] = [];
    
    // 1. Read Local (now caches globals as well for offline support)
    try {
      const localData = await AsyncStorage.getItem(LOCAL_SCENES_KEY);
      if (localData) {
        const parsed = JSON.parse(localData);
        if (Array.isArray(parsed)) {
          localScenes = parsed;
        }
      }
    } catch (e) {
      AppLogger.error('[ScenesService] Local read fail', { error: String(e) });
    }

    // 2. Background Sync
    const syncCloud = async () => {
      let globalScenes: Scene[] = [];
      let userCloudScenes: Scene[] = [];
      
      try {
        const { data, error } = await supabase
          .from('custom_builder_presets')
          .select('*')
          .eq('fill_mode', 'SCENE');
          
        if (!error && data) {
          globalScenes = data.map(p => ({
            id: p.id,
            name: p.name,
            steps: Array.isArray(p.nodes) ? p.nodes as unknown as SceneStep[] : [],
            created_at: p.created_at ?? '',
            user_id: p.user_id ?? undefined
          }));
        }
      } catch (err) {
        AppLogger.warn('[ScenesService] Global sync fail', { error: String(err) });
      }

      if (userId) {
        try {
          const { data, error } = await supabase
            .from('user_saved_presets')
            .select('*')
            .eq('user_id', userId)
            .eq('fill_mode', 'SCENE');
            
          if (!error && data) {
            userCloudScenes = data.map(p => ({
              id: p.id,
              name: p.name,
              steps: Array.isArray(p.nodes) ? p.nodes as unknown as SceneStep[] : [],
              created_at: p.created_at ?? '',
              user_id: p.user_id ?? undefined
            }));
          }
        } catch (err) {
          AppLogger.warn('[ScenesService] User cloud sync fail', { error: String(err) });
        }
      }

      // Merge deduplicating by ID 
      const mergedMap = new Map<string, Scene>();
      localScenes.forEach(s => mergedMap.set(s.id, s));
      globalScenes.forEach(s => mergedMap.set(s.id, s));
      userCloudScenes.forEach(s => mergedMap.set(s.id, s)); // User cloud wins over local/global
      
      // Update Local cache with merged results
      const finalUserSaved = Array.from(mergedMap.values());
      try {
        await AsyncStorage.setItem(LOCAL_SCENES_KEY, JSON.stringify(finalUserSaved));
      } catch (e) {}
    };

    syncCloud(); // Fire and forget

    return localScenes;
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
      AppLogger.error('[ScenesService] saveLocalScene error', { error: String(e) });
    }

    // 2. Save Cloud (user_saved_presets) via Queue
    if (userId) {
      await this.enqueueSyncJob({
        id: `save_${scene.id}_${Date.now()}`,
        type: 'upsert_user_scene',
        payload: {
          id: scene.id,
          name: scene.name,
          nodes: scene.steps,
          fill_mode: 'SCENE',
          transition_type: 0,
          user_id: userId,
          created_at: scene.created_at || new Date().toISOString()
        },
        timestamp: Date.now()
      });
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
      AppLogger.error('[ScenesService] deleteLocalScene error', { error: String(e) });
    }

    // 2. Delete Cloud (user_saved_presets) via Queue
    if (userId) {
      await this.enqueueSyncJob({
        id: `del_${sceneId}_${Date.now()}`,
        type: 'delete_user_scene',
        payload: { id: sceneId },
        timestamp: Date.now()
      });
    }

    return true;
  }

  // ── Sync Queue Operations ──

  private async enqueueSyncJob(job: SceneSyncJob) {
    try {
      const rawQueue = await AsyncStorage.getItem(LOCAL_SCENE_SYNC_QUEUE_KEY);
      const queue: SceneSyncJob[] = rawQueue ? JSON.parse(rawQueue) : [];
      queue.push(job);
      await AsyncStorage.setItem(LOCAL_SCENE_SYNC_QUEUE_KEY, JSON.stringify(queue));
      AppLogger.debug('[ScenesService] Enqueued sync job', { jobType: job.type, jobId: job.id });
    } catch (e) {
      AppLogger.error('[ScenesService] Enqueue fail', { error: String(e) });
    }
  }

  async flushSyncQueue() {
    if (!supabase) return;
    try {
      const rawQueue = await AsyncStorage.getItem(LOCAL_SCENE_SYNC_QUEUE_KEY);
      let queue: SceneSyncJob[] = rawQueue ? JSON.parse(rawQueue) : [];
      
      if (queue.length === 0) return;
      
      const { data: session } = await supabase.auth.getSession();
      if (!session?.session?.user) {
        // Can't sync without auth, keep in queue
        return;
      }

      let remainingQueue: SceneSyncJob[] = [];
      let successCount = 0;

      for (const job of queue) {
        let success = false;
        try {
          if (job.type === 'publish_community_scene') {
            const { error } = await supabase.from('shared_scenes').insert([job.payload]);
            if (!error) success = true;
          } else if (job.type === 'upsert_user_scene') {
            const { error } = await supabase.from('user_saved_presets').upsert(job.payload as unknown as Database['public']['Tables']['user_saved_presets']['Insert']);
            if (!error) success = true;
          } else if (job.type === 'delete_user_scene') {
            const { error } = await supabase.from('user_saved_presets').delete().eq('id', job.payload.id);
            if (!error) success = true;
          }
        } catch (err) {
          // Swallow explicit error to allow retry
        }

        if (success) {
          successCount++;
        } else {
          remainingQueue.push(job);
        }
      }

      await AsyncStorage.setItem(LOCAL_SCENE_SYNC_QUEUE_KEY, JSON.stringify(remainingQueue));
      
      if (successCount > 0) {
        AppLogger.info('[ScenesService] Sync queue flushed', { successCount, remaining: remainingQueue.length });
      }
    } catch (e) {
      AppLogger.error('[ScenesService] Flush fail', { error: String(e) });
    }
  }
}

export const ScenesService = new ScenesServiceClass();
