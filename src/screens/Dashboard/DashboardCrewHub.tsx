import React, { useCallback, useEffect, useState } from 'react';
import { Linking } from 'react-native';
import * as ExpoLinking from 'expo-linking';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from '../../services/appLogger';
import { STORAGE_CREW_HUB_COLLAPSED } from '../../constants/storageKeys';

export function useDashboardCrewHub() {
  const [crewInitialStep, setCrewInitialStep] = useState<'landing' | 'join' | 'create' | 'map'>('landing');
  const [isCrewHubCollapsed, setIsCrewHubCollapsed] = useState(false);
  const [initialDeepLinkCode, setInitialDeepLinkCode] = useState<string | null>(null);

  useEffect(() => {
    AsyncStorage.getItem(STORAGE_CREW_HUB_COLLAPSED)
      .then(res => { if (res !== null) setIsCrewHubCollapsed(res === 'true'); })
      .catch((e) => AppLogger.warn('PERSISTENCE', { key: STORAGE_CREW_HUB_COLLAPSED, event: 'load_failed', error: String(e), payload_size: 0, ssi: 0 }));
  }, []);

  const toggleCrewHubCollapse = useCallback(() => {
    setIsCrewHubCollapsed(prev => {
      const next = !prev;
      AsyncStorage.setItem(STORAGE_CREW_HUB_COLLAPSED, String(next)).catch((e) => AppLogger.warn('PERSISTENCE', { key: STORAGE_CREW_HUB_COLLAPSED, event: 'save_failed', error: String(e), payload_size: 0, ssi: 0 }));
      return next;
    });
  }, []);

  return {
    crewInitialStep,
    setCrewInitialStep,
    isCrewHubCollapsed,
    toggleCrewHubCollapse,
    initialDeepLinkCode,
    setInitialDeepLinkCode,
  };
}

export function useCrewDeepLink(setInitialDeepLinkCode: (code: string) => void, setCrewInitialStep: (step: 'landing' | 'join' | 'create' | 'map') => void, setIsCrewModalVisible: (visible: boolean) => void) {
  useEffect(() => {
    const handleDeepLink = ({ url }: { url: string }) => {
      if (!url) return;
      try {
        const parsed = ExpoLinking.parse(url) as { path?: string; queryParams?: Record<string, string> };
        if (parsed.path === 'crew/join' && parsed.queryParams?.code) {
          const inviteCode = String(parsed.queryParams.code).toUpperCase();
          AppLogger.log('DEEP_LINK', { action: 'crew_join', inviteCode });
          setInitialDeepLinkCode(inviteCode);
          setCrewInitialStep('join');
          setIsCrewModalVisible(true);
        }
      } catch (err: unknown) {
        AppLogger.error('DEEP_LINK', { event: 'parse_failed', url, error: (err instanceof Error ? err.message : String(err)) , payload_size: 0, ssi: 0 });
      }
    };
    const linkSubscription = Linking.addEventListener('url', handleDeepLink);
    Linking.getInitialURL().then(url => { if (url) handleDeepLink({ url }); });
    return () => linkSubscription.remove();
  }, [setIsCrewModalVisible, setInitialDeepLinkCode, setCrewInitialStep]);
}

