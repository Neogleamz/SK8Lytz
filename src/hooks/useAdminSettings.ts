import { useState, useCallback, useEffect } from 'react';
import { AppSettingsService, AppSettingsMap } from '../services/AppSettingsService';

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
      console.error('Failed to fetch app settings:', err);
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
      console.error(`Failed to update setting ${key}:`, err);
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
