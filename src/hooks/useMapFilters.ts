import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

const FILTER_PERSIST_KEY = '@Sk8lytz_MapFilters';

export type MapFilterMatrix = {
  showRinks: boolean;
  showParks: boolean;
  showShops: boolean;
  requireIndoor: boolean;
};

const DEFAULT_FILTERS: MapFilterMatrix = {
  showRinks: true,
  showParks: true,
  showShops: true,
  requireIndoor: false,
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
          setFilters({ ...DEFAULT_FILTERS, ...JSON.parse(cached) });
        }
        if (isMounted) setStatus('ready');
      } catch (err) {
        console.error('Failed to load Map Filters from AsyncStorage', err);
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
    } catch (err) {
      console.warn('Failed to persist Map Filters', err);
    }
  };

  const applyFilters = (spots: any[]) => {
    return spots.filter(spot => {
      // 1. Indoor strict constraint
      if (filters.requireIndoor && !spot.is_indoor) return false;

      // 2. Facility constraints
      let allowed = false;
      const t = spot.facility_type;
      
      if (t === 'roller_rink' || t === 'hybrid') {
        if (filters.showRinks) allowed = true;
      }
      if (t === 'skatepark' || t === 'hybrid') {
        if (filters.showParks) allowed = true;
      }
      if (t === 'pro_shop' || spot.has_pro_shop) {
        if (filters.showShops) allowed = true;
      }
      
      // Keep legacy spots that might not have facility_type populated falling back to their old heuristic
      if (!t && filters.showRinks && spot.is_indoor) allowed = true;
      if (!t && filters.showParks && !spot.is_indoor) allowed = true;

      return allowed;
    });
  };

  return {
    filters,
    status,
    toggleFilter,
    applyFilters
  };
}
