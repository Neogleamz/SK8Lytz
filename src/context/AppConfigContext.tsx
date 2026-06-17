import React, { createContext, useContext, useEffect, useState, useMemo } from 'react';
import { AppSettingsService, AppSettingsMap } from '../services/AppSettingsService';
import { useAuth } from './AuthContext';
import { AppState } from 'react-native';
import { AppLogger } from '../services/appLogger';

interface AppConfigContextValue {
  settings: AppSettingsMap;
  isVisibilityAllowed: (key: string) => boolean;
  isFeatureEnabled: (key: string, defaultValue?: boolean) => boolean;
  refresh: () => Promise<void>;
}

const AppConfigContext = createContext<AppConfigContextValue>({
  settings: {},
  isVisibilityAllowed: () => true,
  isFeatureEnabled: () => true,
  refresh: async () => {},
});

export const AppConfigProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [settings, setSettings] = useState<AppSettingsMap>({});
  const { isOfflineMode } = useAuth();

  const refresh = async () => {
    try {
      const fresh = await AppSettingsService.fetchAllSettings();
      setSettings(fresh);
    } catch (error) {
      AppLogger.warn('[AppConfigContext] fetchAllSettings failed', { error, payload_size: 0, ssi: 0 });
      setSettings({});
    }
  };

  useEffect(() => {
    refresh();
    
    // Refresh settings when app comes to foreground
    const sub = AppState.addEventListener('change', (state) => {
      if (state === 'active') {
        refresh();
      }
    });
    return () => sub.remove();
  }, []);

  const value = useMemo(() => {
    return {
      settings,
      refresh,
      /**
       * Evaluates a visibility enum against the user's current offline state.
       * e.g., 'visible_all', 'online_only', 'hidden_all'
       */
      isVisibilityAllowed: (key: string): boolean => {
        const val = settings[key];
        if (!val || val === 'visible_all') return true;
        if (val === 'hidden_all') return false;
        if (val === 'online_only') return !isOfflineMode;
        return true;
      },
      /**
       * Generic helper for booleans
       */
      isFeatureEnabled: (key: string, defaultValue = true): boolean => {
        if (settings[key] === undefined) return defaultValue;
        return settings[key] === true || settings[key] === 'true';
      }
    };
  }, [settings, isOfflineMode]);

  return (
    <AppConfigContext.Provider value={value}>
      {children}
    </AppConfigContext.Provider>
  );
};

export const useAppConfig = () => useContext(AppConfigContext);
