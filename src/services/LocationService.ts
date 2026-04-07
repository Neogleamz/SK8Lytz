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
      // Check existing permission
      const { status: existing } = await Location.getForegroundPermissionsAsync();
      let granted = existing === 'granted';

      if (!granted) {
        const { status } = await Location.requestForegroundPermissionsAsync();
        granted = status === 'granted';
      }

      if (!granted) {
        console.log('[LocationService] Permission denied — proceeding without location');
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
      console.warn('[LocationService] Error acquiring location:', err);
      return null;
    }
  }

  /**
   * Fetch active public sessions sorted by distance from current position.
   * Falls back to creation-date order if location permission denied.
   */
  async getNearbyPublicSessions(radiusMi?: number | null): Promise<NearbySession[]> {
    const { supabase } = await import('./supabaseClient');

    // Pull all active sessions the user is authorized to see (via RLS: public OR is member)
    const { data, error } = await supabase
      .from('crew_sessions')
      .select('id, name, invite_code, location_label, location_coords, scheduled_at, created_at, crew_members(count)')
      .eq('is_active', true)
      .order('created_at', { ascending: false })
      .limit(100);

    if (error || !data) return [];

    // Try to get user location for distance sort
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

    const sessions: NearbySession[] = data.map((s: any) => {
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
        crewName:      null,
        memberCount:   s.crew_members?.[0]?.count ?? 0,
        scheduledAt:   s.scheduled_at,
        distanceMi,
        distanceLabel,
      };
    });

    // Sort: nearest first, then by date
    const sorted = sessions.sort((a, b) => {
      if (a.distanceMi !== null && b.distanceMi !== null) return a.distanceMi - b.distanceMi;
      if (a.distanceMi !== null) return -1;
      if (b.distanceMi !== null) return 1;
      return 0;
    });

    // Apply radius filter if provided
    if (radiusMi != null) {
      return sorted.filter(s => s.distanceMi === null || s.distanceMi <= radiusMi);
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
  distanceMi:    number | null;
  distanceLabel: string;
}

export const locationService = new LocationService();

