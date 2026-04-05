import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';

export interface ICloudScene {
  id: string;
  created_at: string;
  author_id: string;
  author_username: string;
  name: string;
  scene_payload: any;
  downloads: number;
  upvotes: number;
  is_public: boolean;
}

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
      return data as ICloudScene[];
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
      return data as ICloudScene[];
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
        console.error('[ScenesService] Must be logged in to publish a scene.');
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
}

export const ScenesService = new ScenesServiceClass();
