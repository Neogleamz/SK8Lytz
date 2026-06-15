import { useEffect, useState, useRef } from 'react';
import { supabase } from '../services/supabaseClient';
import { locationService } from '../services/LocationService';
import { AppLogger } from '../services/appLogger';
import { crewService } from '../services/CrewService';

export type RadarMatchType = 'PRIVATE_CREW' | 'PUBLIC_SESSION' | 'EMPTY_RINK' | 'NONE';

export interface RadarAlert {
  matchType: RadarMatchType;
  venueName: string;
  distanceMi: number;
  crewName?: string;
  memberCount?: number;
  sessionId?: string;
  inviteCode?: string;
}

const RADAR_RADIUS_MI = 0.5;

export function useCrewProximityRadar() {
  const [radarAlert, setRadarAlert] = useState<RadarAlert | null>(null);

  const isScanningRef = useRef(false);

  const [isInCrew, setIsInCrew] = useState(crewService.isInCrew);

  useEffect(() => {
    return crewService.subscribe(() => {
      setIsInCrew(crewService.isInCrew);
    });
  }, []);

  useEffect(() => {
    let mounted = true;

    async function scan() {
      if (isScanningRef.current) return;
      isScanningRef.current = true;
      try {
        // 1. Get user foreground location (silently handles permissions internally)
        const userLoc = await locationService.getSessionLocation();
        if (!userLoc || !mounted) {
          setRadarAlert({ matchType: 'NONE', venueName: '', distanceMi: 0 });
          return;
        }

        // 2. See if we are near a roller rink
        const spots = await locationService.getNearbySkateSpots(RADAR_RADIUS_MI, userLoc.coords);
        if (spots.length === 0 || !mounted) {
          setRadarAlert({ matchType: 'NONE', venueName: '', distanceMi: 0 });
          return;
        }

        const closestSpot = spots[0]; // Already sorted by distance in LocationService
        const distance = closestSpot.distanceMi ?? 0;

        // 3. Fetch active sessions.
        // RLS natively filters private sessions to ONLY the ones the user is a member of.
        // We also want public sessions. We just query all active sessions we have access to!
        const { data: activeSessions, error } = await supabase
          .from('crew_sessions')
          .select('id, name, is_public, invite_code, location_coords, crew_members(count), crews(name)')
          .eq('is_active', true)
          .gt('expires_at', new Date().toISOString());

        if (error || !activeSessions || !mounted) {
          // If query fails or no sessions, it's an empty rink!
          setRadarAlert({
            matchType: 'EMPTY_RINK',
            venueName: closestSpot.name,
            distanceMi: distance,
          });
          return;
        }

        // 4. Find sessions that are within the radius of the user
        let privateMatch: RadarAlert | null = null;
        let publicMatch: RadarAlert | null = null;

        for (const session of activeSessions) {
          const coords = session.location_coords as { lat: number; lng: number } | null;
          if (!coords || typeof coords.lat !== 'number' || typeof coords.lng !== 'number') continue;

          const sessionDist = locationService.haversineMi(userLoc.coords.lat, userLoc.coords.lng, coords.lat, coords.lng);
          
          if (sessionDist <= RADAR_RADIUS_MI) {
            const membersJoin = session.crew_members as { count: number }[] | null;
            const memberCount = membersJoin?.[0]?.count ?? 0;
            const crewsJoin = session.crews as { name: string } | null;
            const crewName = crewsJoin?.name ?? session.name ?? 'Unknown Crew';

            const match: RadarAlert = {
              matchType: session.is_public ? 'PUBLIC_SESSION' : 'PRIVATE_CREW',
              venueName: closestSpot.name,
              distanceMi: sessionDist,
              crewName,
              memberCount,
              sessionId: session.id,
              inviteCode: session.invite_code ?? undefined,
            };

            if (!session.is_public && !privateMatch) {
              privateMatch = match;
            } else if (session.is_public && !publicMatch) {
              publicMatch = match;
            }
          }
        }

        if (privateMatch) {
          setRadarAlert(privateMatch);
        } else if (publicMatch) {
          setRadarAlert(publicMatch);
        } else {
          setRadarAlert({
            matchType: 'EMPTY_RINK',
            venueName: closestSpot.name,
            distanceMi: distance,
          });
        }
      } catch (err: unknown) {
        const errMsg = err instanceof Error ? err.message : String(err);
        AppLogger.warn('[useCrewProximityRadar] scan failed', { error: errMsg, payload_size: 0, ssi: 0 });
        if (mounted) setRadarAlert({ matchType: 'NONE', venueName: '', distanceMi: 0 });
      } finally {
        isScanningRef.current = false;
      }
    }

    // Only run if not already in a session
    if (!isInCrew) {
      scan();
    } else {
      setRadarAlert({ matchType: 'NONE', venueName: '', distanceMi: 0 });
    }

    return () => { mounted = false; };
  }, [isInCrew]);

  return radarAlert;
}
