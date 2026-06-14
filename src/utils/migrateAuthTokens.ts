import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';
import { AppLogger } from '../services/AppLogger';
import { STORAGE_AUTH_MIGRATION_FLAG, STORAGE_SUPABASE_AUTH_KEY } from '../constants/storageKeys';

export async function migrateAuthTokensToSecureStore() {
  try {
    const hasMigrated = await AsyncStorage.getItem(STORAGE_AUTH_MIGRATION_FLAG);
    if (hasMigrated === 'true') {
      return;
    }

    const legacyToken = await AsyncStorage.getItem(STORAGE_SUPABASE_AUTH_KEY);
    if (legacyToken) {
      const existingSecureToken = await SecureStore.getItemAsync(STORAGE_SUPABASE_AUTH_KEY);
      if (!existingSecureToken) {
        await SecureStore.setItemAsync(STORAGE_SUPABASE_AUTH_KEY, legacyToken);
      }
      await AsyncStorage.removeItem(STORAGE_SUPABASE_AUTH_KEY);
      AppLogger.info('Migrated Supabase auth token to SecureStore', { payload_size: 0, ssi: 0 });
    }

    await AsyncStorage.setItem(STORAGE_AUTH_MIGRATION_FLAG, 'true');
  } catch (e: unknown) {
    AppLogger.error('Failed to migrate auth tokens', e instanceof Error ? e : new Error(String(e)), { payload_size: 0, ssi: 0 });
  }
}
