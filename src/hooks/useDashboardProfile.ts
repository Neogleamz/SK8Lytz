/**
 * useDashboardProfile.ts — Dashboard Profile & App Settings Domain Hook
 *
 * Owns the authenticated user profile, global app settings, and all
 * modal visibility flags that are not tied to BLE or crew state.
 *
 * Extracted from DashboardScreen.tsx (Phase 1 — Domain-Driven Refactor).
 *
 * Depends on: ProfileService, AppSettingsService, NotificationService
 */
import { useEffect, useState } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import { AppSettingsMap, AppSettingsService } from '../services/AppSettingsService';
import { notificationService } from '../services/NotificationService';
import { profileService, UserProfile } from '../services/ProfileService';
import { AppLogger } from '../services/AppLogger';

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
  // ─── Modal visibility ──────────────────────────────────────────
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

  // ─── Modal visibility state ────────────────────────────────────────────────
  const [isAccountModalVisible, setIsAccountModalVisible] = useState(false);
  const [isAdminToolsVisible, setIsAdminToolsVisible] = useState(false);
  const [isSupportModalVisible, setIsSupportModalVisible] = useState(false);
  const [isMapVisible, setIsMapVisible] = useState(false);

  // ─── App settings — fetched on mount, refreshed on app foreground ──────────
  useEffect(() => {
    AppSettingsService.fetchAllSettings().then(setAppSettings);
    const sub = AppState.addEventListener('change', (s: AppStateStatus) => {
      if (s === 'active') AppSettingsService.fetchAllSettings().then(setAppSettings);
    });
    return () => sub.remove();
  }, []);

  // ─── Push notification init — wires crew join handler ─────────────────────
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

  const refreshProfile = async (): Promise<void> => {
    try {
      const profile = await profileService.fetchOrCreateProfile();
      setUserProfile(profile);
    } catch (e) {
      AppLogger.error('[useDashboardProfile] Profile refresh failed', e);
    }
  };

  return {
    userProfile,
    appSettings,
    refreshProfile,
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
