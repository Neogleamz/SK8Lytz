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

const ANTI_SKATEBOARD_KEYWORDS = [
  "skateboard", "board shop", "snowboard", "zumiez", "vans",
  "skateboards", "skateboarding"
];

function isPolluted(name: string, types: string[]): boolean {
  const combinedText = `${name} ${types.join(' ')}`.toLowerCase();
  for (const keyword of ANTI_SKATEBOARD_KEYWORDS) {
    if (combinedText.includes(keyword)) {
      return true; // Found a skateboard-specific term
    }
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
        
        const params: TextSearchRequest = {
          params: {
            query,
            key: API_KEY
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
                console.log(`[GooglePlaces] 🚫 Anti-Skateboard Defense triggered for: "${name}"`);
            }
          }
        }
      } catch (error: any) {
        console.error(`[GooglePlaces] Error searching for ${term}:`, error.message);
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
