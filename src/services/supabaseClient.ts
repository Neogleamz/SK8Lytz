
/* eslint-disable no-undef */
import { createClient, SupabaseClient, SupportedStorage } from '@supabase/supabase-js';
import * as SecureStore from 'expo-secure-store';
import 'react-native-url-polyfill/auto';

import { Database } from '../types/supabase';

import { Platform } from 'react-native';

class SecureStoreAdapter implements SupportedStorage {
  async getItem(key: string): Promise<string | null> {
    try {
      if (Platform.OS === 'web') return localStorage.getItem(key);
      return await SecureStore.getItemAsync(key);
    } catch {
      return null;
    }
  }

  async setItem(key: string, value: string): Promise<void> {
    try {
      if (Platform.OS === 'web') {
        localStorage.setItem(key, value);
      } else {
        await SecureStore.setItemAsync(key, value);
      }
    } catch (e: unknown) {
      // R-04 note: AppLogger cannot be imported here (circular dep risk).
      // Using console.warn so auth storage errors are visible in dev logs.
      if (__DEV__) console.warn('[supabaseClient] SecureStore setItem failed', e instanceof Error ? e.message : String(e));
    }
  }

  async removeItem(key: string): Promise<void> {
    try {
      if (Platform.OS === 'web') {
        localStorage.removeItem(key);
      } else {
        await SecureStore.deleteItemAsync(key);
      }
    } catch (e: unknown) {
      // R-04 note: AppLogger cannot be imported here (circular dep risk).
      if (__DEV__) console.warn('[supabaseClient] SecureStore removeItem failed', e instanceof Error ? e.message : String(e));
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
  // @ts-expect-error TS2352: The offline stub is intentionally an incomplete mock interface
  : ({
      auth: {
        getUser: async () => ({ data: { user: null }, error: null }),
        onAuthStateChange: () => ({ data: { subscription: { unsubscribe: () => {} } } }),
        signInWithPassword: async () => ({ data: null, error: new Error('Offline mode') }),
        signOut: async () => ({ error: null }),
      },
      rpc: async (_fn: string, _args?: object) => ({
        data: null,
        error: new Error('Offline mode — network unavailable. Please use email to sign in.'),
      }),
      channel: () => ({
        on: () => ({ subscribe: () => ({ unsubscribe: () => {} }) }),
      }),
      from: () => ({
        select: () => ({ eq: () => ({ eq: () => ({ maybeSingle: async () => ({ data: null }), order: async () => ({ data: [] }) }), maybeSingle: async () => ({ data: null }), order: async () => ({ data: [] }) }), maybeSingle: async () => ({ data: null }), order: async () => ({ data: [] }) }),
        upsert: async () => ({ error: null }),
        insert: async () => ({ error: null }),
        delete: () => ({ eq: () => ({ eq: async () => ({ error: null }), delete: async () => ({ error: null }) }) })
      })
    // R-08: The offline stub must satisfy SupabaseClient<Database>.
    // The structural object is intentionally incomplete (only paths exercised in offline
    // mode are mocked)
    } as SupabaseClient<Database>);
