import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useState } from 'react';

const STORAGE_KEY = '@Sk8lytz_RecentLocations';

export interface RecentSpot {
  id?: string;
  name: string;
  lat: number;
  lng: number;
  addedAt: number;
}

export function useRecentSpots() {
  const [recentSpots, setRecentSpots] = useState<RecentSpot[]>([]);

  useEffect(() => {
    loadRecents();
  }, []);

  const loadRecents = async () => {
    try {
      const data = await AsyncStorage.getItem(STORAGE_KEY);
      if (data) setRecentSpots(JSON.parse(data));
    } catch {}
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
      setRecentSpots(parsed);
    } catch {}
  }, []);

  return { recentSpots, addRecentSpot };
}
