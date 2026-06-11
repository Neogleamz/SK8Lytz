import { useCallback, useEffect, useRef, useState } from 'react';
import { AppSettingsMap, AppSettingsService, AppSettingsValue } from '../services/AppSettingsService';
import { AppLogger } from '../services/AppLogger';

type SettingsStatus = 'idle' | 'loading' | 'success' | 'error';

/**
 * useAdminSettings — Domain hook for managing global app configuration and feature flags.
 */
export function useAdminSettings(visible: boolean) {
  const [appSettings, setAppSettings] = useState<AppSettingsMap>({});
  const [status, setStatus] = useState<SettingsStatus>('idle');
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  const loadSettings = useCallback(async () => {
    setStatus('loading');
    try {
      const settings = await AppSettingsService.fetchAllSettings();
      if (!isMountedRef.current) return;
      setAppSettings(settings);
      setStatus('success');
    } catch (err: unknown) {
      if (!isMountedRef.current) return;
      AppLogger.warn('[AdminSettings] Failed to fetch app settings', { error: (err instanceof Error ? err.message : String(err)) });
      setStatus('error');
    }
  }, []);

  useEffect(() => {
    if (visible) loadSettings();
  }, [visible, loadSettings]);

  const updateSetting = useCallback(async (key: string, value: AppSettingsValue) => {
    // Optimistic update
    setAppSettings(prev => ({ ...prev, [key]: value }));
    try {
      const success = await AppSettingsService.updateSetting(key, value);
      if (!isMountedRef.current) return;
      if (!success) {
        // Simple rollback if failed
        loadSettings();
      } else {
        AppLogger.log('HARDWARE_CONFIG_CHANGED', { key, value });
      }
    } catch (err: unknown) {
      if (!isMountedRef.current) return;
      AppLogger.warn(`[AdminSettings] Failed to update setting ${key}`, { error: (err instanceof Error ? err.message : String(err)) });
      loadSettings();
    }
  }, [loadSettings]);

  return {
    appSettings,
    isLoading: status === 'loading',
    loadSettings,
    updateSetting,
  };
}
