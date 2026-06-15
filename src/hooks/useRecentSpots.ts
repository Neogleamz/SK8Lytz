import { STORAGE_RECENT_LOCATIONS } from '../constants/storageKeys';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useRef, useState } from 'react';
import { AppLogger } from '../services/AppLogger';

const STORAGE_KEY = STORAGE_RECENT_LOCATIONS;

export interface RecentSpot {
  id?: string;
  name: string;
  lat: number;
  lng: number;
  addedAt: number;
}

export function useRecentSpots() {
  const [recentSpots, setRecentSpots] = useState<RecentSpot[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
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
    setIsLoading(true);
    setError(null);
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEY);
      if (!isMountedRef.current) return;
      if (data) setRecentSpots(JSON.parse(data));
    } catch (e: unknown) {
      if (!isMountedRef.current) return;
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.warn('[useRecentSpots] Failed to load recent spots from storage', {
        error: msg,
        payload_size: 0,
        ssi: 0
      });
      setError(msg);
    } finally {
      if (isMountedRef.current) {
        setIsLoading(false);
      }
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
      setError(msg);
    }
  }, []);

  return { recentSpots, addRecentSpot, isLoading, error };
}
