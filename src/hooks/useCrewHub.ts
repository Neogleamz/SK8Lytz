import { useCallback, useEffect, useState } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { crewService, CrewSession } from '../services/CrewService';
import { locationService, NearbySession, NearbySkateSpot } from '../services/LocationService';
import { PermanentCrew, profileService } from '../services/ProfileService';
import { AppLogger } from '../services/AppLogger';

const RADIUS_STORAGE_KEY = '@Sk8lytz_RadiusPreference';

export function useCrewHub(visible: boolean, step: string) {
  const [discoverRadiusMi, _setDiscoverRadiusMi] = useState<number | null>(50);

  useEffect(() => {
    AsyncStorage.getItem(RADIUS_STORAGE_KEY).then(val => {
      if (val !== null) _setDiscoverRadiusMi(JSON.parse(val));
    }).catch(() => {});
  }, []);

  const setDiscoverRadiusMi = useCallback((val: number | null) => {
    _setDiscoverRadiusMi(val);
    AsyncStorage.setItem(RADIUS_STORAGE_KEY, JSON.stringify(val)).catch(() => {});
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
    profileService.getMyCrew().then((crews: PermanentCrew[]) => {
      setMyCrews(crews);
      setPermanentCrews(crews.map(c => ({ id: c.id, name: c.name })));
    }).catch((e) => { AppLogger.error('[useCrewHub] Failed to load my crews', e); });
  }, [visible, step]);

  // Load member counts for My Crews
  useEffect(() => {
    if (!visible || step !== 'landing' || myCrews.length === 0) return;
    myCrews.forEach(crew => {
      if (crewMemberCounts[crew.id]) return;
      profileService.getCrewMembersForDisplay(crew.id).then(info => {
        setCrewMemberCounts(prev => ({ ...prev, [crew.id]: info }));
      }).catch((e) => { AppLogger.error('[useCrewHub] Failed to load member counts', e); });
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
    
    Promise.all([
      locationService.getNearbyPublicSessions(discoverRadiusMi),
      locationService.getNearbySkateSpots(discoverRadiusMi)
    ]).then(([sessions, spots]) => {
      setNearbySessions(sessions);
      setNearbySpots(spots);
    }).catch(() => { })
    .finally(() => setIsLoadingNearby(false));
  }, [discoverRadiusMi]);

  useEffect(() => {
    if (!visible || step !== 'landing') return;
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
