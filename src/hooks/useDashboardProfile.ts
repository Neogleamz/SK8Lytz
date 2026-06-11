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
import { useEffect, useState, useRef } from 'react';
import { AppState, AppStateStatus, Alert } from 'react-native';
import { AppLogger } from '../services/AppLogger';
import { AppSettingsMap, AppSettingsService } from '../services/AppSettingsService';
import { notificationService } from '../services/NotificationService';
import { profileService, UserProfile } from '../services/ProfileService';
import { supabase } from '../services/supabaseClient';
import { useAuth } from '../context/AuthContext';

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
  setIsAdminToolsVisible: (v: boolean) => void;  isSupportModalVisible: boolean;
  setIsSupportModalVisible: (v: boolean) => void;
  isMapVisible: boolean;
  setIsMapVisible: (v: boolean) => void;
}

export function useDashboardProfile({
  onCrewJoinNotification,
}: UseDashboardProfileOptions): UseDashboardProfileResult {
  const { session, signOut } = useAuth();
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const [appSettings, setAppSettings] = useState<AppSettingsMap>({});
  const [authUsername, setAuthUsername] = useState<string | null>(null);

  const onCrewJoinNotificationRef = useRef(onCrewJoinNotification);
  useEffect(() => {
    onCrewJoinNotificationRef.current = onCrewJoinNotification;
  }, [onCrewJoinNotification]);

  // ── Modal visibility state ───────────────────────────────────────────────
  type ActiveModalState = 'none' | 'account' | 'admin' | 'support' | 'map';
  const [activeModal, setActiveModal] = useState<ActiveModalState>('none');
  const isAccountModalVisible = activeModal === 'account';
  const isAdminToolsVisible = activeModal === 'admin';
  const isSupportModalVisible = activeModal === 'support';
  const isMapVisible = activeModal === 'map';

  const setIsAccountModalVisible = (v: boolean) => setActiveModal(v ? 'account' : 'none');
  const setIsAdminToolsVisible = (v: boolean) => setActiveModal(v ? 'admin' : 'none');
  const setIsSupportModalVisible = (v: boolean) => setActiveModal(v ? 'support' : 'none');
  const setIsMapVisible = (v: boolean) => setActiveModal(v ? 'map' : 'none');

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
      onCrewJoinNotificationRef.current(crewId);
    });

    notificationService.init(false, session?.user?.id).catch(e =>
      AppLogger.log('SYNC', { context: 'push_notification_init_skipped', error: (e instanceof Error ? e.message : String(e)) })
    );

    return () => {
      notificationService.cleanup(session?.user?.id).catch((e) => AppLogger.warn('NOTIFICATION_SERVICE', { event: 'cleanup_failed', error: (e instanceof Error ? e.message : String(e)) }));
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ── Stage 1: cached username for instant UI feedback ────────────────────
  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_auth_username').then(val => {
      if (val && !authUsername) setAuthUsername(val);
    }).catch((e) => AppLogger.warn('PERSISTENCE', { key: '@Sk8lytz_auth_username', event: 'load_failed', error: (e instanceof Error ? e.message : String(e)) }));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // ── Stage 2: derive reactively from userProfile + auth session ──────────
  useEffect(() => {
    if (!supabase) return;
    const dbDisplay = userProfile?.display_name?.trim();
    const dbUser = userProfile?.username?.trim();
    const sessionEmailPrefix = session?.user?.email?.split('@')[0];
    const fallback = dbDisplay || dbUser || sessionEmailPrefix || 'GUEST';
    setAuthUsername(fallback);
    AsyncStorage.setItem('@Sk8lytz_auth_username', fallback).catch((e) => AppLogger.warn('PERSISTENCE', { key: '@Sk8lytz_auth_username', event: 'save_failed', error: (e instanceof Error ? e.message : String(e)) }));
  }, [userProfile, session]);

  const handleLogout = async (): Promise<void> => {
    await signOut();
    // App.tsx onAuthStateChange detects session=null and redirects to AuthScreen
  };

  const refreshProfile = async (): Promise<void> => {
    try {
      const profile = await profileService.fetchOrCreateProfile(session?.user);
      if (profile?.is_banned) {
        Alert.alert(
          'Account Suspended',
          profile.ban_reason ? `Reason: ${profile.ban_reason}` : 'Your account has been suspended by an administrator.',
          [{ text: 'OK', onPress: () => handleLogout() }]
        );
      } else if (profile) {
        setUserProfile(profile);
      }
    } catch (e: unknown) {
      AppLogger.error('[useDashboardProfile] Profile refresh failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
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
    setIsAdminToolsVisible,    isSupportModalVisible,
    setIsSupportModalVisible,
    isMapVisible,
    setIsMapVisible,
  };
}

