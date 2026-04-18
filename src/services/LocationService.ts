/**
 * LocationService.ts — SK8Lytz Location Tagging
 *
 * Wraps expo-location for crew session tagging:
 *   - Request foreground permission (optional - session starts without if denied)
 *   - Get current coordinates
 *   - Reverse geocode to human-readable venue label
 *     ("SkateCity OP", "Riverside Skatepark", "8th & Main St")
 */

import * as Location from 'expo-location';
import { AppLogger } from './AppLogger';
import { supabase } from './supabaseClient';
import { checkPermission, openGlobalPermissionsModal } from './PermissionService';

export interface SessionLocation {
  label: string;          // "SkateCity OP, Olathe KS"
  coords: { lat: number; lng: number };
}

class LocationService {
  /**
   * Request permission + get a venue label for the current position.
   * Returns null if permission denied or location unavailable — caller
   * should handle gracefully (session proceeds without location).
   */
  async getSessionLocation(): Promise<SessionLocation | null> {
    try {
      let isGranted = await checkPermission('LOCATION');
      if (!isGranted) {
        await openGlobalPermissionsModal();
        isGranted = await checkPermission('LOCATION');
      }

      if (!isGranted) {
        AppLogger.log('ERROR_CAUGHT', { service: 'LocationService', reason: 'foreground_location_denied' });
        return null;
      }

      // Get position (balanced accuracy = fast + reasonable precision)
      const pos = await Location.getCurrentPositionAsync({
        accuracy: Location.Accuracy.Balanced,
      });

      const { latitude, longitude } = pos.coords;

      // Reverse geocode via device OS geocoder (Google on Android, Apple on iOS — free, no key)
      const results = await Location.reverseGeocodeAsync({ latitude, longitude });

      const label = this._buildLabel(results[0] ?? {});
      const location: SessionLocation = {
        label,
        coords: { lat: latitude, lng: longitude },
      };

      AppLogger.log('PERFORMANCE_METRIC', {
        metricName: 'LOCATION_ACQUIRED',
        value: 1,
        label,
        accuracy: pos.coords.accuracy,
      });

      return location;
    } catch (err) {
      AppLogger.warn('[LocationService] Error acquiring location', err);
      return null;
    }
  }

  /**
   * Silently attempts to get the last known location.
   * Will NOT trigger a permission modal. Will NOT spin up the GPS.
   * Returns null if permission is denied or location is unknown.
   */
  async getSilentLocation(): Promise<{ lat: number; lng: number } | null> {
    try {
      const { status } = await Location.getForegroundPermissionsAsync();
      if (status !== 'granted') return null;

      // Use last known position for zero battery impact ambient scanning
      const pos = await Location.getLastKnownPositionAsync();
      if (pos) {
        return { lat: pos.coords.latitude, lng: pos.coords.longitude };
      }
      return null;
    } catch (err) {
      // Don't clutter logs during passive scanning
      return null;
    }
  }

  /**
   * Fetch active public sessions sorted by distance from current position.
   * Falls back to creation-date order if location permission denied.
   */
  async getNearbyPublicSessions(radiusMi?: number | null): Promise<NearbySession[]> {

    const SESSION_SELECT = 'id, name, invite_code, location_label, location_coords, scheduled_at, created_at, is_public, crew_members(count), crews(name)';

    // ── Query 1: All active PUBLIC sessions (visible to everyone in radius) ──
    const { data: publicData } = await supabase
      .from('crew_sessions')
      .select(SESSION_SELECT)
      .eq('is_active', true)
      .eq('is_public', true)
      .order('created_at', { ascending: false })
      .limit(80);

    // ── Query 2: Active sessions the user is a session-member of (private crew sessions) ──
    // This covers private sessions from crews the user belongs to.
    // We look at `crew_members` (session membership) not `crew_memberships` (permanent crew membership).
    let privateData: any[] = [];
    try {
      const { data: { user } } = await supabase.auth.getUser();
      if (user) {
        // Fetch sessions where user is a permanent crew member OR an active session participant
        const [mRes, sRes] = await Promise.all([
          supabase.from('crew_memberships').select('crew_id').eq('user_id', user.id),
          supabase.from('crew_members').select('session_id').eq('user_id', user.id)
        ]);

        const myCrewIds = (mRes.data ?? []).map((m: any) => m.crew_id).filter(Boolean);
        const mySessionIds = (sRes.data ?? []).map((s: any) => s.session_id).filter(Boolean);

        if (myCrewIds.length > 0 || mySessionIds.length > 0) {
          let query = supabase
            .from('crew_sessions')
            .select(SESSION_SELECT)
            .eq('is_active', true)
            .eq('is_public', false);

          // Build OR filter for (belongs to my crew) OR (already in this session)
          const orParts: string[] = [];
          if (myCrewIds.length > 0) orParts.push(`crew_id.in.(${myCrewIds.join(',')})`);
          if (mySessionIds.length > 0) orParts.push(`id.in.(${mySessionIds.join(',')})`);
          
          const { data: memberSessions } = await query
            .or(orParts.join(','))
            .order('created_at', { ascending: false });

          privateData = memberSessions ?? [];
        }
      }
    } catch (err) {
      AppLogger.warn('[LocationService] Private session fetch failed', err);
    }

    // ── Merge + deduplicate by session id ────────────────────────────────────
    const combined = [...(publicData ?? []), ...privateData];
    const seen = new Set<string>();
    const unique = combined.filter((s: any) => {
      if (seen.has(s.id)) return false;
      seen.add(s.id);
      return true;
    });

    if (unique.length === 0) return [];

    // ── Get user location for distance sort ──────────────────────────────────
    let userLat: number | null = null;
    let userLng: number | null = null;
    try {
      const { status } = await Location.getForegroundPermissionsAsync();
      if (status === 'granted') {
        const pos = await Location.getCurrentPositionAsync({ accuracy: Location.Accuracy.Low });
        userLat = pos.coords.latitude;
        userLng = pos.coords.longitude;
      }
    } catch { /* location unavailable - sort by date */ }

    const sessions: NearbySession[] = unique.map((s: any) => {
      const coords = s.location_coords as { lat?: number; lng?: number } | null;
      let distanceMi: number | null = null;
      let distanceLabel = '';

      if (userLat !== null && userLng !== null && coords?.lat && coords?.lng) {
        distanceMi = this._haversineMi(userLat, userLng, coords.lat, coords.lng);
        distanceLabel = distanceMi < 0.1
          ? 'Here now'
          : distanceMi < 1
            ? `${(distanceMi * 5280).toFixed(0)} ft away`
            : `${distanceMi.toFixed(1)} mi away`;
      }

      return {
        id:            s.id,
        name:          s.name,
        inviteCode:    s.invite_code,
        locationLabel: s.location_label ?? 'Unknown Location',
        leaderName:    'Unknown',
        crewName:      s.crews?.name ?? null,
        memberCount:   s.crew_members?.[0]?.count ?? 0,
        scheduledAt:   s.scheduled_at,
        isPublic:      s.is_public ?? true,
        distanceMi,
        distanceLabel,
        lat: coords?.lat ?? null,
        lng: coords?.lng ?? null,
      };
    });

    // Sort: nearest first, then by date
    const sorted = sessions.sort((a, b) => {
      if (a.distanceMi !== null && b.distanceMi !== null) return a.distanceMi - b.distanceMi;
      if (a.distanceMi !== null) return -1;
      if (b.distanceMi !== null) return 1;
      return 0;
    });

    // Apply radius filter (sessions without coords pass through — user might be at same location)
    // Apply radius filter (Only show sessions with valid coordinates within the radius)
    if (radiusMi != null) {
      return sorted.filter(s => s.distanceMi !== null && s.distanceMi <= radiusMi);
    }
    return sorted;
  }

  /**
   * Fetch static skate spots sorted by distance from current position.
   */
  async getNearbySkateSpots(radiusMi?: number | null): Promise<NearbySkateSpot[]> {
    const { data } = await supabase
      .from('skate_spots')
      .select('*')
      .limit(500); // For MVP, grab a reasonable chunk. (Requires PostGIS bounding box for scale)

    if (!data || data.length === 0) return [];

    let userLat: number | null = null;
    let userLng: number | null = null;
    try {
      const { status } = await Location.getForegroundPermissionsAsync();
      if (status === 'granted') {
        const pos = await Location.getCurrentPositionAsync({ accuracy: Location.Accuracy.Low });
        userLat = pos.coords.latitude;
        userLng = pos.coords.longitude;
      }
    } catch { /* ignore */ }

    const spots: NearbySkateSpot[] = data.map((spot: any) => {
      let distanceMi: number | null = null;
      let distanceLabel = '';

      if (userLat !== null && userLng !== null && spot.lat && spot.lng) {
        distanceMi = this._haversineMi(userLat, userLng, spot.lat, spot.lng);
        distanceLabel = distanceMi < 0.1
          ? 'Here now'
          : distanceMi < 1
            ? `${(distanceMi * 5280).toFixed(0)} ft away`
            : `${distanceMi.toFixed(1)} mi away`;
      }

      return {
        id: spot.id,
        name: spot.name,
        lat: spot.lat,
        lng: spot.lng,
        city: spot.city,
        state: spot.state,
        zip: spot.zip,
        phone: spot.phone,
        surface_type: spot.surface_type,
        is_indoor: spot.is_indoor,
        vibe_rating: spot.vibe_rating,
        distanceMi,
        distanceLabel,
      } as NearbySkateSpot;
    });

    const sorted = spots.sort((a, b) => {
      if (a.distanceMi !== null && b.distanceMi !== null) return a.distanceMi - b.distanceMi;
      if (a.distanceMi !== null) return -1;
      if (b.distanceMi !== null) return 1;
      return 0;
    });

    if (radiusMi != null) {
      return sorted.filter(s => s.distanceMi !== null && s.distanceMi <= radiusMi);
    }
    return sorted;
  }

  /** Haversine distance in miles between two lat/lng points */
  private _haversineMi(lat1: number, lng1: number, lat2: number, lng2: number): number {
    const R = 3958.8; // Earth radius in miles
    const dLat = this._deg2rad(lat2 - lat1);
    const dLng = this._deg2rad(lng2 - lng1);
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(this._deg2rad(lat1)) * Math.cos(this._deg2rad(lat2)) *
      Math.sin(dLng / 2) * Math.sin(dLng / 2);
    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }

  private _deg2rad(deg: number): number { return deg * (Math.PI / 180); }

  private _buildLabel(geo: Location.LocationGeocodedAddress): string {
    // Priority: venue name → street → district → city + region
    const parts: string[] = [];

    // Named venue (e.g. "SkateCity OP", "Riverside Park")
    if (geo.name && geo.name !== geo.street) {
      parts.push(geo.name);
    } else if (geo.street) {
      parts.push(geo.street);
    }

    // City or district
    const place = geo.city || geo.district || geo.subregion;
    if (place) parts.push(place);

    // Fallback: state abbreviation
    if (geo.region && parts.length < 2) {
      parts.push(geo.region);
    }

    return parts.join(', ') || 'Unknown Location';
  }
}

export interface NearbySession {
  id:            string;
  name:          string;
  inviteCode:    string;
  locationLabel: string;
  leaderName:    string;
  crewName:      string | null;
  memberCount:   number;
  scheduledAt:   string | null;
  isPublic:      boolean;  // whether this session is publicly discoverable
  distanceMi:    number | null;
  distanceLabel: string;
  lat:           number | null;
  lng:           number | null;
}

export interface NearbySkateSpot {
  id: string;
  name: string;
  lat: number;
  lng: number;
  city: string | null;
  state: string | null;
  zip: string | null;
  phone: string | null;
  surface_type: 'wood' | 'concrete' | 'asphalt' | 'sport_court' | 'unknown' | null;
  is_indoor: boolean | null;
  vibe_rating: number | null;
  distanceMi: number | null;
  distanceLabel: string;
}

export const locationService = new LocationService();

