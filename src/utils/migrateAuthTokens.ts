import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';
import { AppLogger } from '../services/AppLogger';

const MIGRATION_FLAG = '@Sk8lytz_auth_migration_v1';
const SUPABASE_AUTH_KEY = 'supabase.auth.token';

export async function migrateAuthTokensToSecureStore() {
  try {
    const hasMigrated = await AsyncStorage.getItem(MIGRATION_FLAG);
    if (hasMigrated === 'true') {
      return;
    }

    const legacyToken = await AsyncStorage.getItem(SUPABASE_AUTH_KEY);
    if (legacyToken) {
      const existingSecureToken = await SecureStore.getItemAsync(SUPABASE_AUTH_KEY);
      if (!existingSecureToken) {
        await SecureStore.setItemAsync(SUPABASE_AUTH_KEY, legacyToken);
      }
      await AsyncStorage.removeItem(SUPABASE_AUTH_KEY);
      AppLogger.info('Migrated Supabase auth token to SecureStore');
    }

    await AsyncStorage.setItem(MIGRATION_FLAG, 'true');
  } catch (e: unknown) {
      const safeErr = e instanceof Error ? e : new Error(String(e));
    AppLogger.error('Failed to migrate auth tokens', e instanceof Error ? e.message : String(e));
  }
}
