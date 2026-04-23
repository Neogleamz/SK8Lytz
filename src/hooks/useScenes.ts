import { useState, useCallback, useEffect } from 'react';
import { Scene, ScenesService } from '../services/ScenesService';
import { useAuth } from './useAuth';

export function useScenes() {
  const [localScenes, setLocalScenes] = useState<Scene[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { session } = useAuth();
  const userId = session?.user?.id;

  const loadScenes = useCallback(async () => {
    setIsLoading(true);
    try {
      const scenes = await ScenesService.getSavedScenes(userId);
      setLocalScenes(scenes);
    } catch (e) {
      console.error('[useScenes] Failed to load saved scenes', e);
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
