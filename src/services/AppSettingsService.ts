import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { supabase } from './supabaseClient';

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

export interface AppSettingsMap {
  [key: string]: any;
}

const CACHE_KEY = '@sk8lytz_app_settings';

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

        const newSettings: Record<string, any> = {};
        for (const row of (data || [])) {
          // Only apply the override if the setting is enabled globally
          if (row.is_enabled !== false) {
            newSettings[row.setting_key] = row.setting_value;
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
  async updateSetting(key: AppSettingKey, value: any): Promise<boolean> {
    try {
      const { error } = await supabase
        .from('sk8lytz_app_settings')
        .upsert({ 
          setting_key: key, 
          setting_value: value 
        }, { onConflict: 'setting_key' });

      if (error) throw error;

      // Update local cache
      try {
        const cachedStr = await AsyncStorage.getItem(CACHE_KEY);
        const cached = cachedStr ? JSON.parse(cachedStr) : {};
        cached[key] = value;
        await AsyncStorage.setItem(CACHE_KEY, JSON.stringify(cached));
      } catch (e: unknown) {
        AppLogger.warn('AppSettingsService cache update failed', e instanceof Error ? e.message : String(e));
      }

      return true;
    } catch (err: unknown) {
      AppLogger.log('ERROR_CAUGHT', { message: `Failed to update setting ${key}`, error: err instanceof Error ? err.message : String(err)  });
      return false;
    }
  }
};
