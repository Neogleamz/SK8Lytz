
import { createClient, SupportedStorage } from '@supabase/supabase-js';
import * as SecureStore from 'expo-secure-store';
import 'react-native-url-polyfill/auto';

import { Database } from '../types/supabase';

class SecureStoreAdapter implements SupportedStorage {
  async getItem(key: string): Promise<string | null> {
    try {
      return await SecureStore.getItemAsync(key);
    } catch {
      return null;
    }
  }

  async setItem(key: string, value: string): Promise<void> {
    try {
      await SecureStore.setItemAsync(key, value);
    } catch (e: unknown) {
      console.error('SecureStore setItem failed', e);
    }
  }

  async removeItem(key: string): Promise<void> {
    try {
      await SecureStore.deleteItemAsync(key);
    } catch (e: unknown) {
      console.error('SecureStore removeItem failed', e);
    }
  }
}

const supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL || '';
const supabaseAnonKey = process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '';

export const supabase = supabaseUrl && supabaseAnonKey 
  ? createClient<Database>(supabaseUrl, supabaseAnonKey, {
      auth: {
        storage: new SecureStoreAdapter(),
        autoRefreshToken: true,
        persistSession: true,
        detectSessionInUrl: false,
      },
    })
  : ({
      auth: {
        getUser: async () => ({ data: { user: null }, error: null }),
        onAuthStateChange: () => ({ data: { subscription: { unsubscribe: () => {} } } }),
        signInWithPassword: async () => ({ data: null, error: new Error('Offline mode') }),
        signOut: async () => ({ error: null }),
      },
      channel: () => ({
        on: () => ({ subscribe: () => ({ unsubscribe: () => {} }) }),
      }),
      from: () => ({
        select: () => ({ eq: () => ({ eq: () => ({ maybeSingle: async () => ({ data: null }), order: async () => ({ data: [] }) }), maybeSingle: async () => ({ data: null }), order: async () => ({ data: [] }) }), maybeSingle: async () => ({ data: null }), order: async () => ({ data: [] }) }),
        upsert: async () => ({ error: null }),
        delete: () => ({ eq: () => ({ eq: async () => ({ error: null }), delete: async () => ({ error: null }) }) })
      })
    } as unknown as ReturnType<typeof createClient<Database>>);
