import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { supabase } from './supabaseClient';
import { STORAGE_APP_SETTINGS } from '../constants/storageKeys';

/**
 * Valid known keys for App Settings
 */
export type AppSettingKey = 
  | 'visibility_street_mode'
  | 'visibility_maps_tab'
  | 'visibility_crew_hub'
  | 'visibility_community_hub'
  | 'global_telemetry_enabled'
  | 'required_eula_version'
  | string;

// R-08 fix: explicit value union instead of `any`.
// All known app settings are either boolean feature flags or string version identifiers.
// Narrowing to string | boolean eliminates the any cast AND maintains consumer compatibility.
export type AppSettingsValue = string | boolean;
export interface AppSettingsMap {
  [key: string]: AppSettingsValue;
}

const CACHE_KEY = STORAGE_APP_SETTINGS;

export const AppSettingsService = {
  /**
   * Fetches all app settings from Supabase.
   * Caches the results locally for offline mode.
   */
  async fetchAllSettings(): Promise<AppSettingsMap> {
    let settingsMap: AppSettingsMap = {};
    
    // 1. Return cache immediately
    try {
      const cached = await AsyncStorage.getItem(CACHE_KEY);
      if (cached) {
        settingsMap = JSON.parse(cached) as AppSettingsMap;
      }
    } catch (e: unknown) {
      AppLogger.log('ERROR_CAUGHT', { message: 'Failed to access cached app settings', info: e instanceof Error ? e.message : String(e) });
    }

    // 2. Background network sync (non-blocking)
    const syncCloud = async () => {
      try {
        const { data, error } = await supabase
          .from('sk8lytz_app_settings')
          .select('setting_key, setting_value, is_enabled');

        if (error) {
          AppLogger.log('ERROR', { context: 'AppSettingsService', message: 'Fetch settings failed', info: error instanceof Error ? error.message : String(error) });
          return;
        }

        const newSettings: AppSettingsMap = {};
        for (const row of (data || [])) {
          // Only apply the override if the setting is enabled globally
          if (row.is_enabled !== false) {
            // setting_value is Supabase Json — extract only the scalar subset we support.
            const raw = row.setting_value;
            if (typeof raw === 'string' || typeof raw === 'boolean') {
              newSettings[row.setting_key] = raw;
            }
          }
        }

        try {
          await AsyncStorage.setItem(CACHE_KEY, JSON.stringify(newSettings));
        } catch (e: unknown) {
          AppLogger.warn('AppSettingsService cache write failed', e instanceof Error ? e.message : String(e));
        }
      } catch (err: unknown) {
        AppLogger.log('ERROR', { context: 'AppSettingsService', message: 'Settings sync failed', info: err instanceof Error ? err.message : String(err) });
      }
    };

    syncCloud();

    return settingsMap;
  },

  /**
   * Updates a single setting in Supabase.
   * Uses upsert to create it if it doesn't exist.
   */
  async updateSetting(key: AppSettingKey, value: unknown): Promise<boolean> {
    // 1. Optimistic Local Update
    try {
      const cachedStr = await AsyncStorage.getItem(CACHE_KEY);
      const cached = cachedStr ? JSON.parse(cachedStr) : {};
      cached[key] = value;
      await AsyncStorage.setItem(CACHE_KEY, JSON.stringify(cached));
    } catch (e: unknown) {
      AppLogger.warn('AppSettingsService cache update failed', e instanceof Error ? e.message : String(e));
    }

    // 2. Background Cloud Sync
    supabase
      .from('sk8lytz_app_settings')
      .upsert({ 
        setting_key: key, 
        setting_value: value as unknown as import('../types/supabase').Json
      }, { onConflict: 'setting_key' })
      .then(({ error }) => {
        if (error) throw error;
      })
      .catch((err: unknown) => {
        AppLogger.log('ERROR_CAUGHT', { message: `Failed to update setting ${key}`, error: err instanceof Error ? err.message : String(err) });
      });

    return true;
  }
};
