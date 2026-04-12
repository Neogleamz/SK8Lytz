import { supabase } from './supabaseClient';
import type { Database } from '../types/supabase';
import { AppLogger } from './AppLogger';

export type SkateSpot = Database['public']['Tables']['skate_spots']['Row'];

interface BoundingBox {
  minLat: number;
  maxLat: number;
  minLng: number;
  maxLng: number;
}

export const SkateSpotsService = {
  /**
   * Fetch verified or community-created skate spots from our native DB
   * within a given map bounding box.
   */
  async getNativeSpots(bbox: BoundingBox): Promise<SkateSpot[]> {
    try {
      const { data, error } = await supabase
        .from('skate_spots')
        .select('*')
        .gte('lat', bbox.minLat)
        .lte('lat', bbox.maxLat)
        .gte('lng', bbox.minLng)
        .lte('lng', bbox.maxLng);

      if (error) {
        AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Failed to fetch native spots', info: error });
        return [];
      }
      return data || [];
    } catch (err: any) {
      AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Error fetching native spots', info: err });
      return [];
    }
  },

  /**
   * Claim an unverified fallback spot and persist it into the native SK8Lytz db.
   * Or update an existing spot if it exists.
   */
  async claimAndUpdateSpot(spot: Partial<SkateSpot>): Promise<SkateSpot | null> {
    try {
      // Upsert logic relies on ID for existing, or insert for new
      const { data, error } = await supabase
        .from('skate_spots')
        .upsert({
          ...spot,
          source: 'native',
          is_verified: true, // Marked verified if crowdsourced, or we can leave it false until admin approval
          updated_at: new Date().toISOString()
        } as any)
        .select()
        .single();

      if (error) {
        AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Failed to claim spot', info: error });
        return null;
      }
      return data;
    } catch (err: any) {
      AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Error claiming spot', info: err });
      return null;
    }
  },

  /**
   * Fallback OSM search to scrape nearby "roller skating rink" spots
   * if our native density is too low. Returns them roughly shaped like our SkateSpot type.
   */
  async getFallbackOSMSpots(bbox: BoundingBox, query = 'roller skating rink'): Promise<Partial<SkateSpot>[]> {
    try {
      const viewbox = `&viewbox=${bbox.minLng},${bbox.maxLat},${bbox.maxLng},${bbox.minLat}&bounded=1`;
      const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&limit=50&addressdetails=1${viewbox}`;
      
      const res = await fetch(url, { headers: { 'User-Agent': 'SK8Lytz App' } });
      const data = await res.json();

      if (!data || !Array.isArray(data)) return [];

      // Map to Partial<SkateSpot>
      return data.map(item => {
        const parts = [
          item.address?.amenity, item.address?.park, item.address?.road, item.address?.city || item.address?.town
        ].filter(Boolean);
        const shortName = parts.length > 0 ? parts.join(', ') : item.name || item.display_name.split(',')[0];

        return {
          id: `osm-${item.place_id}`,
          name: shortName,
          lat: parseFloat(item.lat),
          lng: parseFloat(item.lon),
          surface_type: 'unknown',
          is_indoor: true,
          source: 'osm_fallback',
          is_verified: false
        } as Partial<SkateSpot>;
      });
    } catch (err) {
      AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'OSM fetch error', info: err });
      return [];
    }
  }
};
