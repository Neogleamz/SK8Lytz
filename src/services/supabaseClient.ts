import AsyncStorage from '@react-native-async-storage/async-storage';
import { createClient } from '@supabase/supabase-js';
import 'react-native-url-polyfill/auto';

import { Database } from '../types/supabase';

const supabaseUrl = process.env.EXPO_PUBLIC_SUPABASE_URL || '';
const supabaseAnonKey = process.env.EXPO_PUBLIC_SUPABASE_ANON_KEY || '';

export const supabase = supabaseUrl && supabaseAnonKey 
  ? createClient<Database>(supabaseUrl, supabaseAnonKey, {
      auth: {
        storage: AsyncStorage,
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
