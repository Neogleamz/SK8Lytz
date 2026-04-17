import { useCallback, useEffect, useState } from 'react';
import { AppSettingsMap, AppSettingsService } from '../services/AppSettingsService';
import { AppLogger } from '../services/AppLogger';

/**
 * useAdminSettings — Domain hook for managing global app configuration and feature flags.
 */
export function useAdminSettings(visible: boolean) {
  const [appSettings, setAppSettings] = useState<AppSettingsMap>({});
  const [isLoading, setIsLoading] = useState(false);

  const loadSettings = useCallback(async () => {
    setIsLoading(true);
    try {
      const settings = await AppSettingsService.fetchAllSettings();
      setAppSettings(settings);
    } catch (err) {
      AppLogger.warn('[AdminSettings] Failed to fetch app settings', { error: String(err) });
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    if (visible) loadSettings();
  }, [visible, loadSettings]);

  const updateSetting = useCallback(async (key: string, value: any) => {
    // Optimistic update
    setAppSettings(prev => ({ ...prev, [key]: value }));
    try {
      const success = await AppSettingsService.updateSetting(key, value);
      if (!success) {
        // Simple rollback if failed
        loadSettings();
      }
    } catch (err) {
      AppLogger.warn(`[AdminSettings] Failed to update setting ${key}`, { error: String(err) });
      loadSettings();
    }
  }, [loadSettings]);

  return {
    appSettings,
    isLoading,
    loadSettings,
    updateSetting,
  };
}
