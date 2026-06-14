/**
 * useBLEInterrogator.ts — Hardware probe hook (thin wrapper)
 *
 * React hook wrapper around InterrogatorService. Owns the hwCache React state
 * so downstream components can react to new probe results via normal re-renders.
 * Core probe logic lives in services/ble/InterrogatorService.ts (pure, testable).
 */
import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import type { BleManager } from 'react-native-ble-plx';
import { loadHWCacheFromStorage, createProbeQueue } from '../../services/ble/InterrogatorService';
import { type PingResult } from '../../types/dashboard.types';

export interface UseBLEInterrogatorProps {
  bleManager: BleManager | null;
  registeredMacs: string[];
  onDeviceInterrogated: () => void;
}

export function useBLEInterrogator({ bleManager, registeredMacs, onDeviceInterrogated }: UseBLEInterrogatorProps) {
  const [hwCache, setHwCache] = useState<Record<string, PingResult>>({});
  const hwCacheRef = useRef<Record<string, PingResult>>({});
  const probingMacsRef = useRef<Set<string>>(new Set());

  // Load persisted hardware profiles from AsyncStorage on mount
  useEffect(() => {
    if (Platform.OS === 'web') return;
    loadHWCacheFromStorage().then(loaded => {
      if (Object.keys(loaded).length > 0) {
        hwCacheRef.current = { ...hwCacheRef.current, ...loaded };
        setHwCache(prev => ({ ...prev, ...loaded }));
      }
    });
  }, []);

  // Create the probe queue — wired to update hwCache state on completion
  const { queueDeviceForInterrogation, cleanup } = bleManager
    ? createProbeQueue({
        bleManager,
        probingMacsRef,
        hwCacheRef,
        getRegisteredMacsCount: () => registeredMacs.length,
        onDeviceInterrogated: () => {
          // Sync hwCacheRef into React state so consumers re-render
          setHwCache({ ...hwCacheRef.current });
          onDeviceInterrogated();
        },
      })
    : { queueDeviceForInterrogation: (_mac: string) => {}, cleanup: () => {} };

  useEffect(() => {
    return () => {
      cleanup();
    };
  }, [cleanup]);

  const stableQueueDeviceForInterrogation = useCallback(
    (mac: string) => queueDeviceForInterrogation(mac),
    [bleManager, registeredMacs.length, queueDeviceForInterrogation],
  );

  return { hwCache, hwCacheRef, queueDeviceForInterrogation: stableQueueDeviceForInterrogation };
}
