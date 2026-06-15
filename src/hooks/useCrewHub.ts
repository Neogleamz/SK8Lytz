import { STORAGE_RADIUS_PREFERENCE } from '../constants/storageKeys';
import { useCallback, useEffect, useState, useRef } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Location from 'expo-location';
import { crewService, CrewSession } from '../services/CrewService';
import { locationService, NearbySession, NearbySkateSpot } from '../services/LocationService';
import { PermanentCrew, profileService } from '../services/ProfileService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';

const RADIUS_STORAGE_KEY = STORAGE_RADIUS_PREFERENCE;

export function useCrewHub(visible: boolean, step: string) {
  const { user } = useAuth();
  const [discoverRadiusMi, _setDiscoverRadiusMi] = useState<number | null>(20);

  useEffect(() => {
    AsyncStorage.getItem(RADIUS_STORAGE_KEY).then(val => {
      if (val !== null) _setDiscoverRadiusMi(JSON.parse(val));
    }).catch((e) => AppLogger.warn('PERSISTENCE', { key: RADIUS_STORAGE_KEY, event: 'load_failed', error: String(e), payload_size: 0, ssi: 0 }));
  }, []);

  const setDiscoverRadiusMi = useCallback((val: number | null) => {
    _setDiscoverRadiusMi(val);
    AsyncStorage.setItem(RADIUS_STORAGE_KEY, JSON.stringify(val)).catch((e) => AppLogger.warn('PERSISTENCE', { key: RADIUS_STORAGE_KEY, event: 'save_failed', error: String(e), payload_size: 0, ssi: 0 }));
  }, []);
  
  const [myCrews, setMyCrews] = useState<PermanentCrew[]>([]);
  const [permanentCrews, setPermanentCrews] = useState<{ id: string; name: string }[]>([]);
  const [crewMemberCounts, setCrewMemberCounts] = useState<Record<string, { count: number; avatarColors: string[] }>>({});
  
  type HubRequestStatus = 'idle' | 'loading' | 'error' | 'success';

  const [nearbySessions, setNearbySessions] = useState<NearbySession[]>([]);
  const [nearbySpots, setNearbySpots] = useState<NearbySkateSpot[]>([]);
  const [nearbyStatus, setNearbyStatus] = useState<HubRequestStatus>('idle');
  const isLoadingNearby = nearbyStatus === 'loading';
  
  const [activeSessions, setActiveSessions] = useState<CrewSession[]>([]);
  const [sessionsStatus, setSessionsStatus] = useState<HubRequestStatus>('idle');
  const isLoadingSessions = sessionsStatus === 'loading';

  const [locationStatus, setLocationStatus] = useState<HubRequestStatus>('idle');
  const isGettingLocation = locationStatus === 'loading';
  const [locationLabel, setLocationLabel] = useState('');
  const [locationCoords, setLocationCoords] = useState<{ lat: number; lng: number } | undefined>();

  // Load permanent crews
  useEffect(() => {
    let active = true;
    if (!visible) return;
    if (step !== 'landing' && step !== 'create' && step !== 'schedule' && step !== 'manage') return;
    if (!user?.id) return;
    profileService.getMyCrew(undefined, user.id).then((crews: PermanentCrew[]) => {
      if (!active) return;
      setMyCrews(crews);
      setPermanentCrews(crews.map(c => ({ id: c.id, name: c.name })));
    }).catch((e) => { 
      if (!active) return;
      AppLogger.error('[useCrewHub] Failed to load my crews', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 }); 
    });
    return () => { active = false; };
  }, [visible, step, user?.id]);

  // Load member counts for My Crews
  const fetchingCrewIdsRef = useRef<Set<string>>(new Set());
  useEffect(() => {
    let active = true;
    if (!visible || step !== 'landing' || myCrews.length === 0) return;
    myCrews.forEach(crew => {
      if (crewMemberCounts[crew.id] || fetchingCrewIdsRef.current.has(crew.id)) return;
      fetchingCrewIdsRef.current.add(crew.id);
      profileService.getCrewMembersForDisplay(crew.id).then(info => {
        if (!active) return;
        setCrewMemberCounts(prev => ({ ...prev, [crew.id]: info }));
      }).catch((e) => { 
        if (!active) return;
        AppLogger.error('[useCrewHub] Failed to load member counts', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 }); 
      }).finally(() => {
        fetchingCrewIdsRef.current.delete(crew.id);
      });
    });
    return () => { active = false; };
  }, [visible, step, myCrews, crewMemberCounts]);

  useEffect(() => {
    // Attempt to silently grab location if permission exists so map knows where to center initially
    locationService.getSilentLocation().then(coords => {
      if (coords) setLocationCoords(coords);
    }).catch(err => {
      AppLogger.warn('[useCrewHub] Silent location acquisition failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
    });
  }, []);

  const refreshNearby = useCallback(() => {
    setNearbyStatus('loading');

    const GPS_TIMEOUT_MS = 3000;

    // Acquire GPS once — silent cached position first (instant), then fresh with timeout.
    // This eliminates the dual concurrent getCurrentPositionAsync race that could hang Promise.all.
    const acquireCoords = async (): Promise<{ lat: number; lng: number } | null> => {
      // Step 1: Try cached OS position — always resolves instantly, zero battery cost
      const silent = await locationService.getSilentLocation();
      if (silent) return silent;

      // Step 2: Request fresh fix with a hard 3s timeout — we never hang
      try {
        const { status } = await Location.getForegroundPermissionsAsync();
        if (status !== 'granted') return null;

        let timerId: ReturnType<typeof setTimeout> | undefined;
        const timeoutPromise = new Promise<null>(resolve => {
          timerId = setTimeout(() => resolve(null), GPS_TIMEOUT_MS);
        });

        const pos = await Promise.race<Location.LocationObject | null>([
          Location.getCurrentPositionAsync({ accuracy: Location.Accuracy.Low }),
          timeoutPromise,
        ]);

        if (timerId) clearTimeout(timerId);

        if (!pos) return null;
        return { lat: pos.coords.latitude, lng: pos.coords.longitude };
      } catch {
        return null;
      }
    };

    acquireCoords()
      .then(userCoords => {
        // Side-effect: seed the map centre if it hasn't been set yet
        if (userCoords && !locationCoords) {
          setLocationCoords(userCoords);
        }
        // Isolated queries — one failing must NOT nuke the other.
        // Previously used Promise.all which is atomic: if getNearbyPublicSessions
        // threw (stale auth, network), setNearbySpots was never called → no pins.
        const sessionsP = locationService.getNearbyPublicSessions(discoverRadiusMi, userCoords, user?.id || null)
          .catch(err => { AppLogger.warn('[useCrewHub] sessions query failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }); return [] as NearbySession[]; });
        const spotsP = locationService.getNearbySkateSpots(discoverRadiusMi, userCoords)
          .catch(err => { AppLogger.warn('[useCrewHub] spots query failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 }); return [] as NearbySkateSpot[]; });

        return Promise.all([sessionsP, spotsP]);
      })
      .then(([sessions, spots]) => {
        setNearbySessions(sessions);
        setNearbySpots(spots);
      })
      .catch((err) => {
        AppLogger.warn('[useCrewHub] refreshNearby failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
        setNearbyStatus('error');
      })
      .finally(() => {
        setNearbyStatus(prev => prev === 'loading' ? 'success' : prev);
      });
  }, [discoverRadiusMi, locationCoords, user?.id]);

  useEffect(() => {
    if (!visible || (step !== 'landing' && step !== 'map')) return;
    refreshNearby();
  }, [visible, step, discoverRadiusMi, refreshNearby]);

  // Load Active Sessions
  const loadActiveSessions = useCallback(async () => {
    setSessionsStatus('loading');
    const sessions = await crewService.fetchActiveSessions().catch(() => {
      setSessionsStatus('error');
      return [];
    });
    setActiveSessions(sessions);
    setSessionsStatus(prev => prev === 'loading' ? 'success' : prev);
  }, []);

  useEffect(() => {
    if (!visible || step !== 'landing') return;
    loadActiveSessions();
  }, [visible, step, loadActiveSessions]);

  useEffect(() => {
    if (step === 'join') loadActiveSessions();
  }, [step, loadActiveSessions]);

  const handleDetectLocation = async () => {
    setLocationStatus('loading');
    const loc = await locationService.getSessionLocation().catch(() => {
      setLocationStatus('error');
      return null;
    });
    setLocationStatus(loc ? 'success' : 'error');
    if (loc) {
      setLocationLabel(loc.label);
      setLocationCoords(loc.coords);
    }
    return loc;
  };

  return {
    discoverRadiusMi, setDiscoverRadiusMi,
    myCrews, setMyCrews,
    permanentCrews, setPermanentCrews,
    crewMemberCounts, setCrewMemberCounts,
    nearbySessions, setNearbySessions,
    nearbySpots, setNearbySpots,
    nearbyStatus, isLoadingNearby, refreshNearby,
    activeSessions, setActiveSessions,
    sessionsStatus, isLoadingSessions, loadActiveSessions,
    isGettingLocation, setIsGettingLocation: (v: boolean) => setLocationStatus(v ? 'loading' : 'idle'),
    locationLabel, setLocationLabel,
    locationCoords, setLocationCoords,
    handleDetectLocation
  };
}
