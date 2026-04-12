import { supabase } from './supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';

/**
 * Valid known keys for App Settings
 */
export type AppSettingKey = 
  | 'global_crew_hub_locked'
  | 'global_community_hub_locked'
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
    try {
      const { data, error } = await supabase
        .from('app_settings')
        .select('setting_key, setting_value');

      if (error) throw error;

      const settingsMap: AppSettingsMap = {};
      if (data) {
        for (const row of data) {
          settingsMap[row.setting_key] = row.setting_value;
        }
      }

      // Cache locally
      await AsyncStorage.setItem(CACHE_KEY, JSON.stringify(settingsMap));
      return settingsMap;
    } catch (err: any) {
      AppLogger.log('ERROR_CAUGHT', { message: 'Failed to fetch app settings', error: err.message });
      
      // Fallback to cache if offline
      try {
        const cached = await AsyncStorage.getItem(CACHE_KEY);
        if (cached) {
          return JSON.parse(cached) as AppSettingsMap;
        }
      } catch (e) {}

      return {}; // return empty if completely failed and no cache
    }
  },

  /**
   * Updates a single setting in Supabase.
   * Uses upsert to create it if it doesn't exist.
   */
  async updateSetting(key: AppSettingKey, value: any): Promise<boolean> {
    try {
      const { error } = await supabase
        .from('app_settings')
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
      } catch (e) {}

      return true;
    } catch (err: any) {
      AppLogger.log('ERROR_CAUGHT', { message: `Failed to update setting ${key}`, error: err.message });
      return false;
    }
  }
};
