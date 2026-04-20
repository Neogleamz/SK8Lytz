import { Client, TextSearchRequest, PlaceDetailsRequest, Place } from "@googlemaps/google-maps-services-js";
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../../../../.env') });

const googleClient = new Client({});
const API_KEY = process.env.GOOGLE_PLACES_SERVER_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY || '';

export interface GooglePlaceResult {
  place_id: string;
  name: string;
  formatted_address?: string;
  lat: number;
  lng: number;
  rating?: number;
  user_ratings_total?: number;
  website?: string;
  formatted_phone_number?: string;
  opening_hours?: any;
}

// Terms that disqualify a Google Places result from being a roller-skating venue.
// Covers: skateboard shops, big-box sporting goods (Scheels), and ice/hockey rinks.
const BLOCKED_NAME_KEYWORDS = [
  // Skateboard-related
  "skateboard", "board shop", "snowboard", "zumiez", "vans",
  "skateboards", "skateboarding",
  // Big-box sporting goods that operate ice rinks
  "scheels",
  // Ice & hockey rinks (roller-only focus)
  "ice rink", "ice skating", "ice arena", "ice centre", "ice center",
  "hockey rink", "hockey arena", "hockey centre", "hockey center",
  "curling",
];

// Google Places `types` that belong to ice/hockey rink categories.
const BLOCKED_PLACE_TYPES = new Set([
  "ice_skating_rink",
  "sporting_goods_store", // catches Scheels generically
]);

function isPolluted(name: string, types: string[]): boolean {
  const lowerName = name.toLowerCase();
  const lowerTypes = types.map(t => t.toLowerCase());

  // Block by type array first (fastest path)
  for (const t of lowerTypes) {
    if (BLOCKED_PLACE_TYPES.has(t)) return true;
  }

  // Block by keyword match on name
  for (const keyword of BLOCKED_NAME_KEYWORDS) {
    if (lowerName.includes(keyword)) return true;
  }

  return false;
}

export class GooglePlacesProvider {
  /**
   * Sweeps a region for roller skating relevant spots.
   * Pollution Defense 1 specifies strict search terms.
   */
  static async searchRegion(zipcodeOrLocation: string): Promise<string[]> {
    const searchTerms = ["roller skating rink", "quad skate shop", "roller derby shop"];
    const verifiedPlaceIds = new Set<string>();

    for (const term of searchTerms) {
      try {
        const query = `${term} in ${zipcodeOrLocation}`;
        console.log(`[GooglePlaces] Initiating text search: "${query}"`);
        
        let pageToken = undefined;
        let pagesFetched = 0;

        while (pagesFetched < 3) {
          const params: TextSearchRequest = {
            params: {
              query,
              key: API_KEY,
              pagetoken: pageToken
            }
          };

          const response = await googleClient.textSearch(params);
        
        if (response.data.results) {
          for (const result of response.data.results) {
            const name = result.name || '';
            const types = result.types || [];
            
            // Pollution Defense 2
            if (!isPolluted(name, types)) {
                if (result.place_id) {
                    verifiedPlaceIds.add(result.place_id);
                }
            } else {
                console.log(`[GooglePlaces] 🚫 Pollution Defense triggered — rejected: "${name}" [types: ${types.join(', ')}]`);
            }
          }
          
          if (response.data.next_page_token) {
            pageToken = response.data.next_page_token;
            pagesFetched++;
            // Google requires a short delay before using the next page token
            await new Promise(r => setTimeout(r, 2000));
          } else {
            break; // No more pages
          }
          }
        }
      } catch (error: any) {
        console.error(`[GooglePlaces] Error searching for ${term}:`, error.message);
        break;
      }
    }

    return Array.from(verifiedPlaceIds);
  }

  /**
   * Performs Place Details lookup for high-value field masks.
   */
  static async getPlaceDetails(placeId: string): Promise<GooglePlaceResult | null> {
    try {
      const params: PlaceDetailsRequest = {
        params: {
          place_id: placeId,
          fields: [
            "place_id",
            "name",
            "formatted_address",
            "geometry",
            "international_phone_number",
            "formatted_phone_number",
            "opening_hours",
            "rating",
            "user_ratings_total",
            "website",
            "types"
          ],
          key: API_KEY
        }
      };

      const response = await googleClient.placeDetails(params);
      const place = response.data.result;

      if (!place) return null;
      if (!place.geometry?.location) return null; // We need coordinates

      return {
        place_id: place.place_id as string,
        name: place.name || 'Unknown Row',
        formatted_address: place.formatted_address,
        lat: place.geometry.location.lat,
        lng: place.geometry.location.lng,
        rating: place.rating,
        user_ratings_total: place.user_ratings_total,
        website: place.website,
        formatted_phone_number: place.international_phone_number || place.formatted_phone_number,
        opening_hours: place.opening_hours ? place.opening_hours.weekday_text : null
      };

    } catch (error: any) {
      console.error(`[GooglePlaces] Error fetching details for ${placeId}:`, error.message);
      return null;
    }
  }
}
