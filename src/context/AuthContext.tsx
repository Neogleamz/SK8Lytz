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

// S4 Monolith Acknowledgment: This file (AuthContext.tsx) is flagged as a monolith (>30KB).
// We are only modifying specific line items listed in PLAN-sweep-context.md.

import React, { createContext, useContext, useEffect, useRef, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Linking from 'expo-linking';
import { Session, User, AuthChangeEvent } from '@supabase/supabase-js';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';
import { migrateAuthTokensToSecureStore } from '../utils/migrateAuthTokens';
import { STORAGE_LAST_EMAIL, STORAGE_OFFLINE_SKIP } from '../constants/storageKeys';

// ─────────────────────────────────────────────────────────────────────────────
// Types
// ─────────────────────────────────────────────────────────────────────────────

export type AuthStatus = 'checking' | 'authenticated' | 'expired' | 'offline' | 'unauthenticated';

export interface AuthContextValue {
  /** The current authentication finite state. */
  status: AuthStatus;
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
  /** Sign in with email + password via Supabase. Centralised so all auth goes through context. */
  signIn: (email: string, password: string) => Promise<{ error: Error | null }>;
  /** Sign up a new user. Options forwarded directly to supabase.auth.signUp. */
  signUp: (email: string, password: string, options?: Parameters<NonNullable<typeof supabase>['auth']['signUp']>[0]['options']) => Promise<{ error: Error | null }>;
  /** Send a password-reset email via Supabase. */
  resetPassword: (email: string, redirectTo?: string) => Promise<{ error: Error | null }>;
  /** Sign the current user out via Supabase. */
  signOut: () => Promise<void>;
}

// ─────────────────────────────────────────────────────────────────────────────
// Context
// ─────────────────────────────────────────────────────────────────────────────

// STORAGE_OFFLINE_SKIP imported from constants

const AuthContext = createContext<AuthContextValue | null>(null);

// ─────────────────────────────────────────────────────────────────────────────
// Provider
// ─────────────────────────────────────────────────────────────────────────────

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [session, setSession] = useState<Session | null>(null);
  const [status, setStatus] = useState<AuthStatus>('checking');

  // Stable ref to prevent stale closure in deep-link handler
  const setSessionRef = useRef(setSession);
  setSessionRef.current = setSession;

  // Re-entrancy guard — prevents concurrent deep link processing on rapid-fire events
  const isHandlingDeepLinkRef = useRef(false);

  useEffect(() => {
    if (!supabase) {
      AppLogger.warn('[AuthContext] Supabase not configured — offline-only mode.');
      setStatus('offline');
      return;
    }

    // ── Deep link handler (magic link / OAuth callback) ──────────────────────
    const handleDeepLink = async ({ url }: { url: string }) => {
      if (!url) return;
      // Re-entrancy guard: drop duplicate concurrent deep link events
      if (isHandlingDeepLinkRef.current) return;
      isHandlingDeepLinkRef.current = true;
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
              AppLogger.log('ERROR_CAUGHT', { context: 'deep_link', message: error.message, payload_size: 0, ssi: 0 });
            }
          }
        }
      } catch (err: unknown) {
        AppLogger.warn('[AuthContext] Deep link parse error', { error: err instanceof Error ? err.message : String(err)  });
      } finally {
        isHandlingDeepLinkRef.current = false;
      }
    };

    const linkSubscription = Linking.addEventListener('url', handleDeepLink);
    Linking.getInitialURL().then(url => {
      if (url) handleDeepLink({ url });
    }).catch((err: unknown) => AppLogger.warn('[AuthContext] getInitialURL failed', { error: err instanceof Error ? err.message : String(err) }));

    // ── Cold-start: restore session or detect expiry ──────────────────────────
    const init = async () => {
      try {
        // 0. Migrate auth tokens to SecureStore
        await migrateAuthTokensToSecureStore();

        // 1. Check if user previously chose Continue Offline
        let offlineSkip = null;
        try {
          offlineSkip = await AsyncStorage.getItem(STORAGE_OFFLINE_SKIP);
        } catch (e: unknown) {
          AppLogger.warn('[AuthContext] Failed to read offline skip', e instanceof Error ? e.message : String(e));
        }
        if (offlineSkip === 'true') {
          setStatus('offline');
          return;
        }

        // 2. Check active Supabase session
        const { data } = await supabase.auth.getSession();
        const existing = data?.session;
        if (existing) {
          setSession(existing);
          setStatus('authenticated');
          return;
        }

        // 3. No active session — check if user had a prior one (token expired)
        let lastEmail = null;
        try {
          lastEmail = await AsyncStorage.getItem(STORAGE_LAST_EMAIL);
        } catch (e: unknown) {
          AppLogger.warn('[AuthContext] Failed to read last email', e instanceof Error ? e.message : String(e));
        }
        if (lastEmail) {
          setStatus('expired');
        } else {
          setStatus('unauthenticated');
        }
      } catch (err: unknown) {
        AppLogger.log('ERROR_CAUGHT', { message: 'AuthContext init failed', info: err instanceof Error ? err.message : String(err), context: 'AuthContext.init', payload_size: 0, ssi: 0 });
        setStatus('unauthenticated');
      }
    };

    init();

    // ── Reactive: auth state change listener ──────────────────────────────────
    const { data } = supabase.auth.onAuthStateChange((_event: AuthChangeEvent, newSession: Session | null) => {
      AppLogger.log('SYNC', { context: 'auth_change', event: _event, hasSession: !!newSession });
      setSessionRef.current(newSession);
      if (_event === 'SIGNED_IN') {
        setStatus('authenticated');
      }
      if (_event === 'SIGNED_OUT') {
        setStatus('unauthenticated');
        AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP).catch(e => {
          AppLogger.warn('[AuthContext] Failed to remove offline skip', e instanceof Error ? e.message : String(e));
        });
      }
    });

    return () => {
      if (data?.subscription) data.subscription.unsubscribe();
      linkSubscription.remove();
    };
  }, []);

  const setIsOfflineMode = (value: boolean) => {
    if (value) {
      setStatus('offline');
    } else {
      setStatus(session ? 'authenticated' : 'unauthenticated');
    }
  };

  const clearOfflineMode = () => {
    setStatus(session ? 'authenticated' : 'unauthenticated');
    AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP).catch(() => {});
  };

  // ── Centralised auth action methods ──────────────────────────────────────────
  const signIn = async (email: string, password: string): Promise<{ error: Error | null }> => {
    if (!supabase) return { error: new Error('Supabase not configured') };
    try {
      const { error } = await supabase.auth.signInWithPassword({ email, password });
      return { error: error ?? null };
    } catch (e: unknown) {
      AppLogger.error('[AuthContext] signIn failed', e, { payload_size: 0, ssi: 0 });
      return { error: e instanceof Error ? e : new Error(String(e)) };
    }
  };

  const signUp = async (
    email: string,
    password: string,
    options?: Parameters<NonNullable<typeof supabase>['auth']['signUp']>[0]['options']
  ): Promise<{ error: Error | null }> => {
    if (!supabase) return { error: new Error('Supabase not configured') };
    try {
      const { error } = await supabase.auth.signUp({ email, password, options  });
      return { error: error ?? null };
    } catch (e: unknown) {
      AppLogger.error('[AuthContext] signUp failed', e, { payload_size: 0, ssi: 0 });
      return { error: e instanceof Error ? e : new Error(String(e)) };
    }
  };

  const resetPassword = async (email: string, redirectTo?: string): Promise<{ error: Error | null }> => {
    if (!supabase) return { error: new Error('Supabase not configured') };
    try {
      const { error } = await supabase.auth.resetPasswordForEmail(email, redirectTo ? { redirectTo } : undefined);
      return { error: error ?? null };
    } catch (e: unknown) {
      AppLogger.error('[AuthContext] resetPassword failed', e, { payload_size: 0, ssi: 0 });
      return { error: e instanceof Error ? e : new Error(String(e)) };
    }
  };

  const signOut = async (): Promise<void> => {
    if (!supabase) return;
    try {
      await supabase.auth.signOut();
    } catch (e: unknown) {
      AppLogger.error('[AuthContext] signOut failed', e, { payload_size: 0, ssi: 0 });
    }
  };

  const user = session?.user ?? null;

  const isOfflineMode = status === 'offline';
  const sessionLoaded = status !== 'checking';
  const sessionExpired = status === 'expired';
  const isAuthenticated = status === 'authenticated' || status === 'offline' || !supabase;

  const value: AuthContextValue = {
    status,
    session,
    user,
    isOfflineMode,
    isAuthenticated,
    sessionLoaded,
    sessionExpired,
    setIsOfflineMode,
    clearOfflineMode,
    signIn,
    signUp,
    resetPassword,
    signOut,
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

