import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';

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
      console.info('Migrated Supabase auth token to SecureStore');
    }

    await AsyncStorage.setItem(MIGRATION_FLAG, 'true');
  } catch (e: unknown) {
    console.error('Failed to migrate auth tokens', e);
  }
}
