import { useState, useEffect, useCallback } from 'react';
import { supabase } from '../services/supabaseClient';
import { GradientsService } from '../services/GradientsService';
import { CustomBuilderPreset } from '../protocols/PositionalMathBuffer';
import { AppLogger } from '../services/AppLogger';
import { useTelemetryLedger } from './useTelemetryLedger';
import { useAuth } from '../context/AuthContext';

export function useGradients() {
  const telemetry = useTelemetryLedger();
  const { user } = useAuth();
  const userId = user?.id;
  
  const [gradients, setGradients] = useState<CustomBuilderPreset[]>([]);
  type GradientStatus = 'idle' | 'loading' | 'error' | 'success';
  const [status, setStatus] = useState<GradientStatus>('loading');
  const [error, setError] = useState<string | null>(null);
  const isLoading = status === 'loading';

  const loadGradients = useCallback(async () => {
    try {
      setStatus('loading');
      setError(null);
      const data = await GradientsService.getSavedGradients(userId);
      setGradients(data);
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('USE_GRADIENTS_LOAD_ERROR', msg);
      setError(msg);
      setStatus('error');
    } finally {
      setStatus(prev => prev === 'loading' ? 'success' : prev);
    }
  }, [userId]);

  useEffect(() => {
    loadGradients();
  }, [loadGradients]);

  const saveGradient = async (preset: Partial<CustomBuilderPreset>) => {
    try {
      // Basic check to see if this is likely a new creation (id starts with temp_)
      const isNew = preset.id?.startsWith('temp_') || !preset.id;
      await GradientsService.saveGradient(preset, userId);
      await loadGradients();
      if (isNew) {
        telemetry.incrementCounter('favorites_created');
      }
    } catch (e: unknown) {
      AppLogger.error('USE_GRADIENTS_SAVE_ERROR', e instanceof Error ? e.message : String(e));
      throw e;
    }
  };

  const deleteGradient = async (id: string) => {
    try {
      await GradientsService.deleteGradient(id, userId);
      await loadGradients();
    } catch (e: unknown) {
      AppLogger.error('USE_GRADIENTS_DELETE_ERROR', e instanceof Error ? e.message : String(e));
      throw e;
    }
  };

  return {
    gradients,
    isLoading,
    status,
    error,
    saveGradient,
    deleteGradient,
    refreshGradients: loadGradients
  };
}
