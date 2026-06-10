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

export type SceneSyncJob = 
  | { id: string; type: 'upsert_user_scene'; payload: Database['public']['Tables']['user_saved_presets']['Insert']; timestamp: number; }
  | { id: string; type: 'delete_user_scene'; payload: { id: string }; timestamp: number; }
  | { id: string; type: 'publish_community_scene'; payload: Database['public']['Tables']['shared_scenes']['Insert']; timestamp: number; };

function isSceneStepArray(nodes: unknown): nodes is SceneStep[] {
  if (!Array.isArray(nodes)) return false;
  return nodes.every(n => typeof n === 'object' && n !== null && 'id' in n && 'effectId' in n && typeof (n as Record<string, unknown>).id === 'string' && typeof (n as Record<string, unknown>).effectId === 'number');
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
          .range(offset, offset + limit - 1)
          .returns<ICloudScene[]>();
        if (!error && data) {
          AsyncStorage.setItem(STORAGE_SCENES_CACHE, JSON.stringify(data)).catch(() => {});
          return data;
        }
      } catch (e: unknown) {
        AppLogger.error('[ScenesService] Background sync error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
  async getMyScenes(userId: string): Promise<ICloudScene[]> {
    try {
      if (!userId) throw new Error('Not authenticated');

      const { data, error } = await supabase
        .from('shared_scenes')
        .select('*')
        .eq('author_id', userId)
        .order('created_at', { ascending: false })
        .returns<ICloudScene[]>();

      if (error) throw error;
      return data;
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] getMyScenes error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
      return [];
    }
  }

  /**
   * Publishes or saves a scene to the cloud.
   */
  async publishScene(name: string, payload: Scene | Record<string, unknown>, isPublic: boolean = false, userId?: string, username?: string): Promise<boolean> {
    try {
      if (!userId) {
        AppLogger.warn('[ScenesService] Skipping cloud publish (not logged in)');
        return false;
      }

      const safeUsername = username || 'Anonymous Skater';

      const jobPayload: Database['public']['Tables']['shared_scenes']['Insert'] = {
        author_id: userId,
        author_username: safeUsername,
        name: name,
        scene_payload: payload as Database['public']['Tables']['shared_scenes']['Insert']['scene_payload'],
        is_public: isPublic
      };

      await this.enqueueSyncJob({
        id: `pub_${Date.now()}_${Math.random().toString(36).substring(7)}`,
        type: 'publish_community_scene',
        payload: jobPayload,
        timestamp: Date.now()
      });

      return true;
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] publishScene error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
     } catch (e: unknown) {
       AppLogger.error('[ScenesService] deleteScene error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
     } catch (e: unknown) {
       AppLogger.error('[ScenesService] upvoteScene error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
     } catch (e: unknown) {
       AppLogger.error('[ScenesService] downloadScene error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] Local read fail', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
            steps: isSceneStepArray(p.nodes) ? p.nodes : [],
            created_at: p.created_at ?? '',
            user_id: p.user_id ?? undefined
          }));
        }
      } catch (err: unknown) {
        AppLogger.warn('[ScenesService] Global sync fail', { error: (err instanceof Error ? err.message : String(err)) });
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
              steps: isSceneStepArray(p.nodes) ? p.nodes : [],
              created_at: p.created_at ?? '',
              user_id: p.user_id ?? undefined
            }));
          }
        } catch (err: unknown) {
          AppLogger.warn('[ScenesService] User cloud sync fail', { error: (err instanceof Error ? err.message : String(err)) });
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
      } catch (e: unknown) {}
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
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] saveLocalScene error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
    }

    // 2. Save Cloud (user_saved_presets) via Queue
    if (userId) {
      await this.enqueueSyncJob({
        id: `save_${scene.id}_${Date.now()}`,
        type: 'upsert_user_scene',
        payload: {
          id: scene.id,
          name: scene.name,
          nodes: scene.steps as unknown as Database['public']['Tables']['user_saved_presets']['Insert']['nodes'],
          fill_mode: 'SCENE',
          transition_type: 0,
          user_id: userId,
          created_at: scene.created_at || new Date().toISOString(),
          updated_at: new Date().toISOString()
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
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] deleteLocalScene error', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
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
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] Enqueue fail', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
    }
  }

  async flushSyncQueue(userId: string) {
    if (!supabase) return;
    try {
      const rawQueue = await AsyncStorage.getItem(LOCAL_SCENE_SYNC_QUEUE_KEY);
      let queue: SceneSyncJob[] = rawQueue ? JSON.parse(rawQueue) : [];
      
      if (queue.length === 0) return;
      
      if (!userId) {
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
            const { error } = await supabase.from('user_saved_presets').upsert(job.payload);
            if (!error) success = true;
          } else if (job.type === 'delete_user_scene') {
            const { error } = await supabase.from('user_saved_presets').delete().eq('id', job.payload.id);
            if (!error) success = true;
          }
        } catch (err: unknown) {
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
    } catch (e: unknown) {
      AppLogger.error('[ScenesService] Flush fail', { error: (e instanceof Error ? e.message : String(e)) , payload_size: 0, ssi: 0 });
    }
  }
}

export const ScenesService = new ScenesServiceClass();
