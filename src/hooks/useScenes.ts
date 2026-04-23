import { useState, useCallback, useEffect } from 'react';
import { Scene, ScenesService } from '../services/ScenesService';

export function useScenes() {
  const [localScenes, setLocalScenes] = useState<Scene[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const loadScenes = useCallback(async () => {
    setIsLoading(true);
    try {
      const scenes = await ScenesService.getLocalScenes();
      setLocalScenes(scenes);
    } catch (e) {
      console.error('[useScenes] Failed to load local scenes', e);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const deleteScene = useCallback(async (sceneId: string) => {
    const success = await ScenesService.deleteLocalScene(sceneId);
    if (success) {
      setLocalScenes(prev => prev.filter(s => s.id !== sceneId));
    }
    return success;
  }, []);

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
