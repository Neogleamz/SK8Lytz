import { useCallback, useEffect, useState } from 'react';
import { crewService, CrewSession } from '../services/CrewService';
import { locationService, NearbySession } from '../services/LocationService';
import { PermanentCrew, profileService } from '../services/ProfileService';

export function useCrewHub(visible: boolean, step: string) {
  const [discoverRadiusMi, setDiscoverRadiusMi] = useState<number | null>(50);
  
  const [myCrews, setMyCrews] = useState<PermanentCrew[]>([]);
  const [permanentCrews, setPermanentCrews] = useState<{ id: string; name: string }[]>([]);
  const [crewMemberCounts, setCrewMemberCounts] = useState<Record<string, { count: number; avatarColors: string[] }>>({});
  
  const [nearbySessions, setNearbySessions] = useState<NearbySession[]>([]);
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
    }).catch((e) => { console.warn('[useCrewHub] Failed to load my crews:', e); });
  }, [visible, step]);

  // Load member counts for My Crews
  useEffect(() => {
    if (!visible || step !== 'landing' || myCrews.length === 0) return;
    myCrews.forEach(crew => {
      if (crewMemberCounts[crew.id]) return;
      profileService.getCrewMembersForDisplay(crew.id).then(info => {
        setCrewMemberCounts(prev => ({ ...prev, [crew.id]: info }));
      }).catch((e) => { console.warn('[useCrewHub] Failed to load member counts:', e); });
    });
  }, [visible, step, myCrews]);

  // Refresh Nearby Sessions
  const refreshNearby = useCallback(() => {
    setIsLoadingNearby(true);
    locationService.getNearbyPublicSessions(discoverRadiusMi)
      .then(s => setNearbySessions(s))
      .catch(() => { })
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
    isLoadingNearby, refreshNearby,
    activeSessions, setActiveSessions,
    isLoadingSessions, loadActiveSessions,
    isGettingLocation, setIsGettingLocation,
    locationLabel, setLocationLabel,
    locationCoords, setLocationCoords,
    handleDetectLocation
  };
}
