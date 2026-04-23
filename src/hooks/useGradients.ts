import { useState, useEffect, useCallback } from 'react';
import { supabase } from '../services/supabaseClient';
import { GradientsService } from '../services/GradientsService';
import { CustomBuilderPreset } from '../protocols/PositionalMathBuffer';
import { AppLogger } from '../services/AppLogger';

export function useGradients() {
  const [userId, setUserId] = useState<string | undefined>();
  
  useEffect(() => {
    supabase.auth.getSession().then(({ data: { session } }) => {
      setUserId(session?.user?.id);
    });
  }, []);
  
  const [gradients, setGradients] = useState<CustomBuilderPreset[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const loadGradients = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await GradientsService.getSavedGradients(userId);
      setGradients(data);
    } catch (e) {
      AppLogger.error('USE_GRADIENTS_LOAD_ERROR', e);
    } finally {
      setIsLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    loadGradients();
  }, [loadGradients]);

  const saveGradient = async (preset: Partial<CustomBuilderPreset>) => {
    try {
      await GradientsService.saveGradient(preset, userId);
      await loadGradients();
    } catch (e) {
      AppLogger.error('USE_GRADIENTS_SAVE_ERROR', e);
      throw e;
    }
  };

  const deleteGradient = async (id: string) => {
    try {
      await GradientsService.deleteGradient(id, userId);
      await loadGradients();
    } catch (e) {
      AppLogger.error('USE_GRADIENTS_DELETE_ERROR', e);
      throw e;
    }
  };

  return {
    gradients,
    isLoading,
    saveGradient,
    deleteGradient,
    refreshGradients: loadGradients
  };
}
