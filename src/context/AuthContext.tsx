/**
 * AuthContext.tsx — Centralized Authentication State Provider
 *
 * Owns: session, user, isOfflineMode, isAuthenticated, sessionLoaded, sessionExpired
 *
 * Eliminates N parallel supabase.auth.getUser() calls across screens and services.
 * All hooks and components must use useAuth() instead of calling supabase.auth directly.
 *
 * Services (non-hook modules) cannot call useAuth() — callers must pass user/userId
 * as parameters to service methods that accept a cachedUser argument.
 *
 * Created: C-02 BATCH:account-hardening
 */

import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Linking from 'expo-linking';
import { Session, User, AuthChangeEvent } from '@supabase/supabase-js';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';
import { migrateAuthTokensToSecureStore } from '../utils/migrateAuthTokens';

// ─────────────────────────────────────────────────────────────────────────────
// Types
// ─────────────────────────────────────────────────────────────────────────────

export interface AuthContextValue {
  /** The active Supabase Session, or null if unauthenticated. */
  session: Session | null;
  /** Derived from session — the authenticated user, or null. */
  user: User | null;
  /** True if the user chose "Continue Offline" — no Supabase session. */
  isOfflineMode: boolean;
  /** True if either (1) a valid session exists, (2) supabase is not configured, or (3) offline mode. */
  isAuthenticated: boolean;
  /** True once the initial auth check has completed — prevents flash of auth screen. */
  sessionLoaded: boolean;
  /** True if a prior session was detected but the token has since expired. */
  sessionExpired: boolean;
  /** Allow child components to activate offline mode (e.g. AuthScreen "Continue Offline" button). */
  setIsOfflineMode: (value: boolean) => void;
  /** Clears offline mode flag and removes the AsyncStorage skip key. */
  clearOfflineMode: () => void;
}

// ─────────────────────────────────────────────────────────────────────────────
// Context
// ─────────────────────────────────────────────────────────────────────────────

const STORAGE_OFFLINE_SKIP = '@Sk8lytz_offline_skip';

const AuthContext = createContext<AuthContextValue | null>(null);

// ─────────────────────────────────────────────────────────────────────────────
// Provider
// ─────────────────────────────────────────────────────────────────────────────

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [session, setSession] = useState<Session | null>(null);
  const [sessionLoaded, setSessionLoaded] = useState(false);
  const [isOfflineMode, setIsOfflineMode] = useState(false);
  const [sessionExpired, setSessionExpired] = useState(false);

  // Stable ref to prevent stale closure in deep-link handler
  const setSessionRef = useRef(setSession);
  setSessionRef.current = setSession;

  useEffect(() => {
    if (!supabase) {
      AppLogger.warn('[AuthContext] Supabase not configured — offline-only mode.');
      setSessionLoaded(true);
      return;
    }

    // ── Deep link handler (magic link / OAuth callback) ──────────────────────
    const handleDeepLink = async ({ url }: { url: string }) => {
      if (!url) return;
      try {
        if (url.includes('#access_token=')) {
          const hashString = url.split('#')[1];
          const params = hashString.split('&').reduce((acc, current) => {
            const [key, value] = current.split('=');
            acc[key] = decodeURIComponent(value);
            return acc;
          }, {} as Record<string, string>);

          if (params.access_token && params.refresh_token) {
            const { error } = await supabase.auth.setSession({
              access_token: params.access_token,
              refresh_token: params.refresh_token,
            });
            if (error) {
              AppLogger.log('ERROR_CAUGHT', { context: 'deep_link', message: error.message });
            }
          }
        }
      } catch (err) {
        AppLogger.warn('[AuthContext] Deep link parse error', { error: String(err) });
      }
    };

    const linkSubscription = Linking.addEventListener('url', handleDeepLink);
    Linking.getInitialURL().then(url => {
      if (url) handleDeepLink({ url });
    });

    // ── Cold-start: restore session or detect expiry ──────────────────────────
    const init = async () => {
      try {
        // 0. Migrate auth tokens to SecureStore
        await migrateAuthTokensToSecureStore();

        // 1. Check if user previously chose Continue Offline
        let offlineSkip = null;
        try {
          offlineSkip = await AsyncStorage.getItem(STORAGE_OFFLINE_SKIP);
        } catch (e) {
          AppLogger.warn('[AuthContext] Failed to read offline skip', e);
        }
        if (offlineSkip === 'true') {
          setIsOfflineMode(true);
          return;
        }

        // 2. Check active Supabase session
        const { data } = await supabase.auth.getSession();
        const existing = data?.session;
        if (existing) {
          setSession(existing);
          return;
        }

        // 3. No active session — check if user had a prior one (token expired)
        let lastEmail = null;
        try {
          lastEmail = await AsyncStorage.getItem('@Sk8lytz_auth_last_email');
        } catch (e) {
          AppLogger.warn('[AuthContext] Failed to read last email', e);
        }
        if (lastEmail) {
          setSessionExpired(true);
        }
      } catch (err) {
        AppLogger.log('ERROR_CAUGHT', { message: 'AuthContext init failed', info: err, context: 'AuthContext.init' });
      } finally {
        setSessionLoaded(true);
      }
    };

    init();

    // ── Reactive: auth state change listener ──────────────────────────────────
    const { data } = supabase.auth.onAuthStateChange((_event: AuthChangeEvent, newSession: Session | null) => {
      AppLogger.log('SYNC', { context: 'auth_change', event: _event, hasSession: !!newSession });
      setSessionRef.current(newSession);
      if (_event === 'SIGNED_IN') {
        setSessionExpired(false);
      }
      if (!newSession) {
        setIsOfflineMode(false);
        AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP).catch(e => {
          AppLogger.warn('[AuthContext] Failed to remove offline skip', e);
        });
      }
    });

    return () => {
      if (data?.subscription) data.subscription.unsubscribe();
      linkSubscription.remove();
    };
  }, []);

  const clearOfflineMode = () => {
    setIsOfflineMode(false);
    AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP).catch(() => {});
  };

  const user = session?.user ?? null;
  const isAuthenticated = !!(session && session.user) || !supabase || isOfflineMode;

  const value: AuthContextValue = {
    session,
    user,
    isOfflineMode,
    isAuthenticated,
    sessionLoaded,
    sessionExpired,
    setIsOfflineMode,
    clearOfflineMode,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// ─────────────────────────────────────────────────────────────────────────────
// Hook
// ─────────────────────────────────────────────────────────────────────────────

/**
 * useAuth — access centralized authentication state.
 *
 * Must be called within an <AuthProvider> subtree.
 * Throws an invariant error in development if called outside the provider.
 *
 * @example
 * const { user, session, isOfflineMode, isAuthenticated } = useAuth();
 */
export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error(
      '[useAuth] must be called within an <AuthProvider>. ' +
      'Ensure <AuthProvider> wraps the component that calls useAuth().'
    );
  }
  return ctx;
}

