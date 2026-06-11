import { useState, useCallback, useEffect, useRef } from 'react';
import { supabase } from '../services/supabaseClient';
import { Scene, ScenesService } from '../services/ScenesService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';

export function useScenes() {
  const [localScenes, setLocalScenes] = useState<Scene[]>([]);
  const [status, setStatus] = useState<'idle' | 'loading' | 'success' | 'error'>('loading');
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const { user } = useAuth();
  const userId = user?.id;
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  const loadScenes = useCallback(async () => {
    setStatus('loading');
    setErrorMsg(null);
    try {
      const scenes = await ScenesService.getSavedScenes(userId);
      if (!isMountedRef.current) return;
      setLocalScenes(scenes);
      setStatus('success');
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('load_scenes_failed', msg, { payload_size: 0, ssi: 0 });
      setErrorMsg(msg);
      setStatus('error');
    }
  }, [userId]);

  const deleteScene = useCallback(async (sceneId: string) => {
    const success = await ScenesService.deleteSavedScene(sceneId, userId);
    if (!isMountedRef.current) return success;
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
    isLoading: status === 'loading',
    error: status === 'error' ? errorMsg : null,
    loadScenes,
    deleteScene
  };
}
