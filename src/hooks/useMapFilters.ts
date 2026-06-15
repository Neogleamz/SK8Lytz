import { STORAGE_MAP_FILTERS } from '../constants/storageKeys';
import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from '../services/appLogger';

const FILTER_PERSIST_KEY = STORAGE_MAP_FILTERS;

export type MapFilterMatrix = {
  showRinks: boolean;
  showParks: boolean;
  showShops: boolean;
  showCrewSessions: boolean;
};

const DEFAULT_FILTERS: MapFilterMatrix = {
  showRinks: true,
  showParks: true,
  showShops: true,
  showCrewSessions: true,
};

export function useMapFilters() {
  const [filters, setFilters] = useState<MapFilterMatrix>(DEFAULT_FILTERS);
  const [status, setStatus] = useState<'idle' | 'loading' | 'ready' | 'error'>('idle');

  useEffect(() => {
    let isMounted = true;
    const loadCache = async () => {
      try {
        setStatus('loading');
        const cached = await AsyncStorage.getItem(FILTER_PERSIST_KEY);
        if (cached && isMounted) {
          const parsed = JSON.parse(cached);
          // Migration guard: strip legacy requireIndoor, inject showCrewSessions default
          delete parsed.requireIndoor;
          if (parsed.showCrewSessions === undefined) parsed.showCrewSessions = true;
          setFilters({ ...DEFAULT_FILTERS, ...parsed });
        }
        if (isMounted) setStatus('ready');
      } catch (err: unknown) {
        AppLogger.warn('[MapFilters] Failed to load from AsyncStorage', { error: (err instanceof Error ? err.message : String(err)), payload_size: 0, ssi: 0 });
        if (isMounted) setStatus('error');
      }
    };
    loadCache();
    return () => { isMounted = false; };
  }, []);

  const toggleFilter = async (key: keyof MapFilterMatrix) => {
    const nextState = { ...filters, [key]: !filters[key] };
    setFilters(nextState);
    try {
      await AsyncStorage.setItem(FILTER_PERSIST_KEY, JSON.stringify(nextState));
    } catch (err: unknown) {
      AppLogger.warn('[MapFilters] Failed to persist filters', { error: (err instanceof Error ? err.message : String(err)), payload_size: 0, ssi: 0 });
    }
  };

  const applyFilters = <T extends { facility_type?: string | null }>(spots: T[]): T[] => {
    return spots.filter(spot => {
      const t = spot.facility_type;
      if (t === 'roller_rink') return filters.showRinks;
      if (t === 'skatepark')   return filters.showParks;
      if (t === 'skate_shop')  return filters.showShops;
      // No facility_type = legacy fallback: always show
      return true;
    });
  };

  return {
    filters,
    status,
    toggleFilter,
    applyFilters
  };
}
