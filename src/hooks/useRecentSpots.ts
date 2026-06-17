import { STORAGE_RECENT_LOCATIONS } from '../constants/storageKeys';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useRef, useState } from 'react';
import { AppLogger } from '../services/appLogger';

const STORAGE_KEY = STORAGE_RECENT_LOCATIONS;
import type { ViewState } from '../types/ViewState';

export interface RecentSpot {
  id?: string;
  name: string;
  lat: number;
  lng: number;
  addedAt: number;
}

export function useRecentSpots() {
  const [recentSpots, setRecentSpots] = useState<RecentSpot[]>([]);
  /** 4-state FSM: idle → loading → success/empty/error */
  const [viewState, setViewState] = useState<ViewState>('loading');
  const [errorMsg, setErrorMsg] = useState('');
  const isMountedRef = useRef(true);

  useEffect(() => {
    isMountedRef.current = true;
    return () => {
      isMountedRef.current = false;
    };
  }, []);

  useEffect(() => {
    loadRecents();
  }, []);

  const loadRecents = async () => {
    setViewState('loading');
    setErrorMsg('');
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEY);
      if (!isMountedRef.current) return;
      if (data) {
        const parsed = JSON.parse(data);
        setRecentSpots(parsed);
        setViewState(parsed.length > 0 ? 'success' : 'empty');
      } else {
        setViewState('empty');
      }
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[useRecentSpots] Failed to load recent spots from storage', {
        error: msg,
        payload_size: 0,
        ssi: 0
      });
      setErrorMsg(msg);
      setViewState('error');
    }
  };

  const addRecentSpot = useCallback(async (spot: Omit<RecentSpot, 'addedAt'>) => {
    try {
      const current = await AsyncStorage.getItem(STORAGE_KEY);
      let parsed: RecentSpot[] = current ? JSON.parse(current) : [];
      
      // Remove dupes based on coordinates or exact name
      parsed = parsed.filter(s => !(s.lat === spot.lat && s.lng === spot.lng));
      
      const newSpot: RecentSpot = { ...spot, addedAt: Date.now() };
      parsed.unshift(newSpot);
      
      // Keep only last 10
      if (parsed.length > 10) parsed = parsed.slice(0, 10);
      
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(parsed));
      if (!isMountedRef.current) return;
      setRecentSpots(parsed);
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[useRecentSpots] Failed to add recent spot', {
        error: msg,
        payload_size: 0,
        ssi: 0
      });
      setErrorMsg(msg);
      setViewState('error');
    }
  }, []);

  return { 
    recentSpots, 
    addRecentSpot, 
    viewState, 
    errorMsg,
    // Legacy support for unmodified consumers
    isLoading: viewState === 'loading', 
    error: viewState === 'error' ? errorMsg : null
  };
}
