import { useState, useCallback, useEffect } from 'react';
import { supabase } from '../services/supabaseClient';
import { Scene, ScenesService } from '../services/ScenesService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';

export function useScenes() {
  const [localScenes, setLocalScenes] = useState<Scene[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { user } = useAuth();
  const userId = user?.id;

  const loadScenes = useCallback(async () => {
    setIsLoading(true);
    try {
      const scenes = await ScenesService.getSavedScenes(userId);
      setLocalScenes(scenes);
    } catch (e: unknown) {
      AppLogger.error('SCENE_SERVICE', { event: 'load_scenes_failed', error: (e instanceof Error ? e.message : String(e)) });
    } finally {
      setIsLoading(false);
    }
  }, [userId]);

  const deleteScene = useCallback(async (sceneId: string) => {
    const success = await ScenesService.deleteSavedScene(sceneId, userId);
    if (success) {
      setLocalScenes(prev => prev.filter(s => s.id !== sceneId));
    }
    return success;
  }, [userId]);

  useEffect(() => {
    loadScenes();
  }, [loadScenes]);

  return {
    localScenes,
    isLoading,
    loadScenes,
    deleteScene
  };
}
