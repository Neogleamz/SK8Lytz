/**
 * useDashboardProfile.ts — Dashboard Profile & App Settings Domain Hook
 *
 * Owns the authenticated user profile, global app settings, authUsername
 * derivation, logout, and all modal visibility flags not tied to BLE/crew.
 *
 * Extracted from DashboardScreen.tsx (Phase 1 — Domain-Driven Refactor).
 * Extended in chore/refactor-dashboard-monolith to absorb authUsername
 * derivation and handleLogout.
 *
 * Depends on: ProfileService, AppSettingsService, NotificationService, Supabase
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useEffect, useState } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import { AppLogger } from '../services/AppLogger';
import { AppSettingsMap, AppSettingsService } from '../services/AppSettingsService';
import { notificationService } from '../services/NotificationService';
import { profileService, UserProfile } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';

interface UseDashboardProfileOptions {
  /**
   * Called when a push notification crew-join tap arrives.
   * DashboardScreen handles this because it owns crew + BLE state.
   */
  onCrewJoinNotification: (crewId: string) => void;
}

export interface UseDashboardProfileResult {
  userProfile: UserProfile | null;
  appSettings: AppSettingsMap;
  /** Triggers a fresh profile fetch (e.g. after editing display name in AccountModal). */
  refreshProfile: () => Promise<void>;
  /** Derived from userProfile + cached AsyncStorage value for instant UI feedback. */
  authUsername: string | null;
  /** Signs the current user out via Supabase. App.tsx handles the redirect. */
  handleLogout: () => Promise<void>;
  // ── Modal visibility ──────────────────────────────────────────────────────
  isAccountModalVisible: boolean;
  setIsAccountModalVisible: (v: boolean) => void;
  isAdminToolsVisible: boolean;
  setIsAdminToolsVisible: (v: boolean) => void;
  isSupportModalVisible: boolean;
  setIsSupportModalVisible: (v: boolean) => void;
  isMapVisible: boolean;
  setIsMapVisible: (v: boolean) => void;
}

export function useDashboardProfile({
  onCrewJoinNotification,
}: UseDashboardProfileOptions): UseDashboardProfileResult {
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const [appSettings, setAppSettings] = useState<AppSettingsMap>({});
  const [authUsername, setAuthUsername] = useState<string | null>(null);

  // ── Modal visibility state ───────────────────────────────────────────────
  const [isAccountModalVisible, setIsAccountModalVisible] = useState(false);
  const [isAdminToolsVisible, setIsAdminToolsVisible] = useState(false);
  const [isSupportModalVisible, setIsSupportModalVisible] = useState(false);
  const [isMapVisible, setIsMapVisible] = useState(false);

  // ── App settings — fetched on mount, refreshed on foreground ────────────
  useEffect(() => {
    AppSettingsService.fetchAllSettings().then(setAppSettings);
    const sub = AppState.addEventListener('change', (s: AppStateStatus) => {
      if (s === 'active') AppSettingsService.fetchAllSettings().then(setAppSettings);
    });
    return () => sub.remove();
  }, []);

  // ── Push notification init — wires crew join handler ────────────────────
  useEffect(() => {
    notificationService.setJoinHandler((crewId: string, _sessionId: string) => {
      onCrewJoinNotification(crewId);
    });

    notificationService.init().catch(e =>
      console.log('[useDashboardProfile] Push notification init skipped:', e)
    );

    return () => {
      notificationService.cleanup().catch(() => {});
    };
    // onCrewJoinNotification is a stable callback ref — intentionally excluded from deps
    // to avoid re-registering the notification service on every render.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ── Stage 1: cached username for instant UI feedback ────────────────────
  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_auth_username').then(val => {
      if (val && !authUsername) setAuthUsername(val);
    }).catch(() => {});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ── Stage 2: derive reactively from userProfile + auth session ──────────
  useEffect(() => {
    if (!supabase) return;
    supabase.auth.getSession().then(({ data: { session } }: { data: { session: any } }) => {
      const dbDisplay = userProfile?.display_name?.trim();
      const dbUser = userProfile?.username?.trim();
      const sessionEmailPrefix = session?.user?.email?.split('@')[0];
      const fallback = dbDisplay || dbUser || sessionEmailPrefix || 'GUEST';
      setAuthUsername(fallback);
      AsyncStorage.setItem('@Sk8lytz_auth_username', fallback).catch(() => {});
    }).catch(() => {});
  }, [userProfile]);

  const refreshProfile = async (): Promise<void> => {
    try {
      const profile = await profileService.fetchOrCreateProfile();
      setUserProfile(profile);
    } catch (e) {
      AppLogger.error('[useDashboardProfile] Profile refresh failed', e);
    }
  };

  const handleLogout = async (): Promise<void> => {
    try {
      await supabase.auth.signOut();
      // App.tsx onAuthStateChange detects session=null and redirects to AuthScreen
    } catch (e) {
      AppLogger.error('Logout error:', e);
    }
  };

  return {
    userProfile,
    appSettings,
    refreshProfile,
    authUsername,
    handleLogout,
    isAccountModalVisible,
    setIsAccountModalVisible,
    isAdminToolsVisible,
    setIsAdminToolsVisible,
    isSupportModalVisible,
    setIsSupportModalVisible,
    isMapVisible,
    setIsMapVisible,
  };
}
