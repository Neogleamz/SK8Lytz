import AsyncStorage from '@react-native-async-storage/async-storage';
import type { Database } from '../types/supabase';
import { AppLogger } from './AppLogger';
import { supabase } from './supabaseClient';
import { STORAGE_SKATE_SPOTS_CACHE } from '../constants/storageKeys';

export type SkateSpot = Database['public']['Tables']['skate_spots']['Row'];

interface BoundingBox {
  minLat: number;
  maxLat: number;
  minLng: number;
  maxLng: number;
}

export const SkateSpotsService = {
  /**
   * Fetch all cached spots from DB (with background sync).
   * SoT owner for STORAGE_SKATE_SPOTS_CACHE.
   */
  async getCachedSpots(): Promise<SkateSpot[]> {
    const TTL = 24 * 60 * 60 * 1000;
    let localData: SkateSpot[] = [];
    let cacheValid = false;

    try {
      const cachedStr = await AsyncStorage.getItem(STORAGE_SKATE_SPOTS_CACHE);
      if (cachedStr) {
        const parsed = JSON.parse(cachedStr);
        if (Array.isArray(parsed.data)) {
          localData = parsed.data;
          if (parsed.timestamp && Date.now() - parsed.timestamp < TTL) {
            cacheValid = true;
          }
        }
      }
    } catch (e: unknown) {}

    const syncCloud = async () => {
      try {
        const { data, error } = await supabase.from('skate_spots').select('*').eq('is_published', true).limit(500);
        if (!error && data) {
          AsyncStorage.setItem(STORAGE_SKATE_SPOTS_CACHE, JSON.stringify({ timestamp: Date.now(), data })).catch(e => console.error(e));
        }
      } catch (e: unknown) {
        console.error(e);
      }
    };

    if (!cacheValid) {
      if (localData.length === 0) {
        // Blocking fetch if no cache exists
        try {
          const { data, error } = await supabase
            .from('skate_spots')
            .select('*')
            .eq('is_published', true)
            .limit(500);
          if (!error && data) {
            localData = data as SkateSpot[];
            AsyncStorage.setItem(STORAGE_SKATE_SPOTS_CACHE, JSON.stringify({ timestamp: Date.now(), data })).catch(e => console.error(e));
          }
        } catch (e: unknown) {
          AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Error fetching native spots', info: e instanceof Error ? e.message : String(e) });
        }
      } else {
        syncCloud().catch(e => console.error(e));
      }
    }

    return localData;
  },

  /**
   * Fetch verified or community-created skate spots from our native DB
   * within a given map bounding box.
   */
  async getNativeSpots(bbox: BoundingBox): Promise<SkateSpot[]> {
    const localData = await this.getCachedSpots();

    const filterByBbox = (spots: SkateSpot[]) => spots.filter(s => 
      s.lat >= bbox.minLat && s.lat <= bbox.maxLat && 
      s.lng >= bbox.minLng && s.lng <= bbox.maxLng
    );

    return filterByBbox(localData);
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
        } as Database['public']['Tables']['skate_spots']['Insert'])
        .select()
        .single();

      if (error) {
        AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Failed to claim spot', info: error instanceof Error ? error.message : String(error) });
        return null;
      }
      return data;
    } catch (err: unknown) {
      AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'Error claiming spot', info: err instanceof Error ? err.message : String(err) });
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
    } catch (err: unknown) {
      AppLogger.log('ERROR', { context: 'SkateSpotsService', message: 'OSM fetch error', info: err instanceof Error ? err.message : String(err) });
      return [];
    }
  }
};
