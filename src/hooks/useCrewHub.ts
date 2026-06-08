import { useCallback, useEffect, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Location from 'expo-location';
import { crewService, CrewSession } from '../services/CrewService';
import { locationService, NearbySession, NearbySkateSpot } from '../services/LocationService';
import { PermanentCrew, profileService } from '../services/ProfileService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';

const RADIUS_STORAGE_KEY = '@Sk8lytz_RadiusPreference';

export function useCrewHub(visible: boolean, step: string) {
  const { user } = useAuth();
  const [discoverRadiusMi, _setDiscoverRadiusMi] = useState<number | null>(20);

  useEffect(() => {
    AsyncStorage.getItem(RADIUS_STORAGE_KEY).then(val => {
      if (val !== null) _setDiscoverRadiusMi(JSON.parse(val));
    }).catch((e) => AppLogger.warn('PERSISTENCE', { key: RADIUS_STORAGE_KEY, event: 'load_failed', error: String(e) }));
  }, []);

  const setDiscoverRadiusMi = useCallback((val: number | null) => {
    _setDiscoverRadiusMi(val);
    AsyncStorage.setItem(RADIUS_STORAGE_KEY, JSON.stringify(val)).catch((e) => AppLogger.warn('PERSISTENCE', { key: RADIUS_STORAGE_KEY, event: 'save_failed', error: String(e) }));
  }, []);
  
  const [myCrews, setMyCrews] = useState<PermanentCrew[]>([]);
  const [permanentCrews, setPermanentCrews] = useState<{ id: string; name: string }[]>([]);
  const [crewMemberCounts, setCrewMemberCounts] = useState<Record<string, { count: number; avatarColors: string[] }>>({});
  
  const [nearbySessions, setNearbySessions] = useState<NearbySession[]>([]);
  const [nearbySpots, setNearbySpots] = useState<NearbySkateSpot[]>([]);
  const [isLoadingNearby, setIsLoadingNearby] = useState(false);
  
  const [activeSessions, setActiveSessions] = useState<CrewSession[]>([]);
  const [isLoadingSessions, setIsLoadingSessions] = useState(false);

  const [isGettingLocation, setIsGettingLocation] = useState(false);
  const [locationLabel, setLocationLabel] = useState('');
  const [locationCoords, setLocationCoords] = useState<{ lat: number; lng: number } | undefined>();

  // Load permanent crews
  useEffect(() => {
    if (!visible) return;
    if (step !== 'landing' && step !== 'create' && step !== 'schedule' && step !== 'manage') return;
    if (!user?.id) return;
    profileService.getMyCrew(undefined, user.id).then((crews: PermanentCrew[]) => {
      setMyCrews(crews);
      setPermanentCrews(crews.map(c => ({ id: c.id, name: c.name })));
    }).catch((e) => { AppLogger.error('[useCrewHub] Failed to load my crews', e instanceof Error ? e.message : String(e)); });
  }, [visible, step]);

  // Load member counts for My Crews
  useEffect(() => {
    if (!visible || step !== 'landing' || myCrews.length === 0) return;
    myCrews.forEach(crew => {
      if (crewMemberCounts[crew.id]) return;
      profileService.getCrewMembersForDisplay(crew.id).then(info => {
        setCrewMemberCounts(prev => ({ ...prev, [crew.id]: info }));
      }).catch((e) => { AppLogger.error('[useCrewHub] Failed to load member counts', e instanceof Error ? e.message : String(e)); });
    });
  }, [visible, step, myCrews]);

  useEffect(() => {
    // Attempt to silently grab location if permission exists so map knows where to center initially
    locationService.getSilentLocation().then(coords => {
      if (coords) setLocationCoords(coords);
    });
  }, []);

  const refreshNearby = useCallback(() => {
    setIsLoadingNearby(true);

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

        const pos = await Promise.race<Location.LocationObject | null>([
          Location.getCurrentPositionAsync({ accuracy: Location.Accuracy.Low }),
          new Promise<null>(resolve => setTimeout(() => resolve(null), GPS_TIMEOUT_MS)),
        ]);

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
          .catch(err => { AppLogger.warn('[useCrewHub] sessions query failed', err instanceof Error ? err.message : String(err)); return [] as NearbySession[]; });
        const spotsP = locationService.getNearbySkateSpots(discoverRadiusMi, userCoords)
          .catch(err => { AppLogger.warn('[useCrewHub] spots query failed', err instanceof Error ? err.message : String(err)); return [] as NearbySkateSpot[]; });

        return Promise.all([sessionsP, spotsP]);
      })
      .then(([sessions, spots]) => {
        setNearbySessions(sessions);
        setNearbySpots(spots);
      })
      .catch((err) => {
        AppLogger.warn('[useCrewHub] refreshNearby failed', err instanceof Error ? err.message : String(err));
      })
      .finally(() => setIsLoadingNearby(false));
  }, [discoverRadiusMi, locationCoords]);

  useEffect(() => {
    if (!visible || (step !== 'landing' && step !== 'map')) return;
    refreshNearby();
  }, [visible, step, discoverRadiusMi, refreshNearby]);

  // Load Active Sessions
  const loadActiveSessions = useCallback(async () => {
    setIsLoadingSessions(true);
    const sessions = await crewService.fetchActiveSessions().catch(() => []);
    setActiveSessions(sessions);
    setIsLoadingSessions(false);
  }, []);

  useEffect(() => {
    if (!visible || step !== 'landing') return;
    loadActiveSessions();
  }, [visible, step, loadActiveSessions]);

  useEffect(() => {
    if (step === 'join') loadActiveSessions();
  }, [step, loadActiveSessions]);

  const handleDetectLocation = async () => {
    setIsGettingLocation(true);
    const loc = await locationService.getSessionLocation();
    setIsGettingLocation(false);
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
    isLoadingNearby, refreshNearby,
    activeSessions, setActiveSessions,
    isLoadingSessions, loadActiveSessions,
    isGettingLocation, setIsGettingLocation,
    locationLabel, setLocationLabel,
    locationCoords, setLocationCoords,
    handleDetectLocation
  };
}
