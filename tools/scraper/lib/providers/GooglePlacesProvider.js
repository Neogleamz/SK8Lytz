"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.GooglePlacesProvider = exports.RETAIL_BLOCKLIST = void 0;
exports.injectDynamicBlocklist = injectDynamicBlocklist;
const google_maps_services_js_1 = require("@googlemaps/google-maps-services-js");
const dotenv_1 = __importDefault(require("dotenv"));
const path_1 = __importDefault(require("path"));
dotenv_1.default.config({ path: path_1.default.resolve(__dirname, '../../../../.env') });
const googleClient = new google_maps_services_js_1.Client({});
const API_KEY = process.env.GOOGLE_PLACES_SERVER_KEY || process.env.VITE_GOOGLE_PLACES_API_KEY || '';
// ─────────────────────────────────────────────────────────────────────────────
// Search terms per facility type — keep these tightly scoped to avoid pollution.
// NOTE: Broader terms like "skate shop" or "skating center" alone are intentionally
//       omitted because they return skateboard shops and ice rinks respectively.
// ─────────────────────────────────────────────────────────────────────────────
const SEARCH_TERMS = {
    roller_rink: [
        "roller skating rink",
        "roller rink",
        "rollerdrome",
        "roller palace",
        "roller world",
        "skating palace",
        "family skating center",
        "skating pavilion",
        "skate center",
        "skate arena",
        "skate complex",
        "skate plaza",
        "skate park",
        "skate world",
        "skate palace",
        "skate land",
        "skate zone",
        "skate city",
        "skate town",
        "skate world",
        "skate palace",
        "skate land",
        "skate zone",
        "skate city",
        "skate town",
    ],
    skate_shop: [
        "roller skate shop",
        "roller skate store",
        "quad skate shop",
        "roller derby shop",
        "roller derby gear",
        "inline skate shop",
        "roller skating pro shop",
        "speed skate shop",
        "roller skating equipment store",
    ],
    skate_park: [
        "outdoor roller rink",
        "roller skating park",
        "community roller rink",
        "recreation center roller skating",
        "indoor skating facility",
    ],
};
// ─────────────────────────────────────────────────────────────────────────────
// Name-based allow-list for skate_park results only.
// Google doesn't cleanly separate roller parks from skateboard parks,
// so we require at least one of these keywords in the venue name.
// ─────────────────────────────────────────────────────────────────────────────
const SKATE_PARK_NAME_ALLOWLIST = [
    "rink", "roller", "skating", "pavilion", "plaza", "recreation",
    "rec center", "community center", "skating rink", "skate center",
];
// ─────────────────────────────────────────────────────────────────────────────
// Name keywords that disqualify ANY result regardless of facility type.
// ─────────────────────────────────────────────────────────────────────────────
const BLOCKED_NAME_KEYWORDS = [
    // Skateboard-only and other sports
    "skateboard", "skateboards", "skateboarding", "board shop", "boardshop", "snowboard",
    "zumiez", "vans skate", "surf shop", "ski shop", "bicycle", "bike shop",
    // Ice & hockey (block ice-specific only; "inline hockey" is valid — it uses roller rinks)
    "ice rink", "ice skating", "ice arena", "ice centre", "ice center",
    "ice complex", "ice palace", "ice house", "ice sport",
    "polar ice", " on ice",
    "figure skating", "figure skate",
    "ice hockey", "hockey rink", "hockey arena", "hockey centre", "hockey center", "hockey equipment", "pure hockey",
    "ice rink", "ice skating", "iceplex", "icehouse", "ice den", "ice arena", "ice garden", "ice palace",
    "minor league hockey", "thunder hockey", "hockey shop", "hockey supply",
    "curling",
    // Big-box retail chains (second-line defence; Google type filter catches most)
    "scheels", "zumiez",
];
// ─────────────────────────────────────────────────────────────────────────────
// Google Places `types` array values that categorically disqualify a result.
// ─────────────────────────────────────────────────────────────────────────────
const BLOCKED_PLACE_TYPES = new Set([
    "ice_skating_rink",
    "sporting_goods_store", // catches big-box generics (Scheels, Dick's, REI, etc.)
    "bicycle_store", // catches Trek, Eriks
    "shopping_mall", // catches Town Center Plaza, Tanger, etc.
    "shoe_store", // catches Shoe Carnival, Vans
    "clothing_store", // catches mall clothing shops
    "home_goods_store", // catches Michaels, Ollie's
    "department_store",
]);
// ─────────────────────────────────────────────────────────────────────────────
// Big-box retail name blocklist — catches stores that sell skates but aren't rinks/shops.
// Applied as a secondary check in GoogleSweep.ts as well.
// ─────────────────────────────────────────────────────────────────────────────
exports.RETAIL_BLOCKLIST = [
    // Sporting goods chains
    "dick's sporting goods", "dicks sporting", "academy sports", "hibbett sports",
    "hibbett", "big 5 sporting", "big 5", "dunham's", "modell's", "sport chalet",
    "sports authority", "play it again sports", "pure hockey", "perani's hockey",
    // Mass-market retail
    "target", "walmart", "costco", "kohl's", "kohls", "amazon", "hobby lobby", "five below", "barnes & noble", "dillons",
    // Outdoor / hunting / rv
    "rei", "bass pro", "bass pro shop", "cabela", "cabela's", "sportsman's warehouse",
    "sportsman", "scheels", "camping world", "gander mountain",
    // Bike shops (frequently sell inline skates but aren't skate shops)
    "trek bicycle", "trek store", "erik's bike", "eriks bike", "specialized", "performance bicycle",
    // Footwear & Clothing
    "foot locker", "footlocker", "vans", "shoe carnival", "zumiez",
    // Malls & Plazas
    "town center", "plaza", "tanger", "outlet", "bargain", "michaels", "resort", "casino",
    // Generic catch-all — any name with "sporting goods" that isn't a specialty store
    "sporting goods", "boardshop", "play it again",
    // Other Non-Skate
    "cargo largo", "speedway", "pickle", "fun city", "worlds of fun", "family fun",
];
// ─────────────────────────────────────────────────────────────────────────────
// Dynamic Blocklist Injection
// Call this from the orchestrator (GoogleSweep) before running the sweep.
// ─────────────────────────────────────────────────────────────────────────────
function injectDynamicBlocklist(keywords) {
    for (const kw of keywords) {
        const kwLower = kw.toLowerCase().trim();
        if (!exports.RETAIL_BLOCKLIST.includes(kwLower) && kwLower !== '') {
            exports.RETAIL_BLOCKLIST.push(kwLower);
        }
    }
}
// ─────────────────────────────────────────────────────────────────────────────
// Pollution filter — returns true if a result SHOULD be rejected.
// ─────────────────────────────────────────────────────────────────────────────
function isPolluted(name, types, facilityType) {
    const lowerName = name.toLowerCase();
    const lowerTypes = types.map(t => t.toLowerCase());
    // 1. Block by Google type array (fastest, most reliable)
    for (const t of lowerTypes) {
        if (BLOCKED_PLACE_TYPES.has(t))
            return true;
    }
    // 2. Block by name keyword
    for (const keyword of BLOCKED_NAME_KEYWORDS) {
        if (lowerName.includes(keyword))
            return true;
    }
    // 3. Ultra-Aggressive Retail Name Filter (to catch Malls, Bargain Outlets, Vans)
    for (const block of exports.RETAIL_BLOCKLIST) {
        if (lowerName.includes(block))
            return true;
    }
    // 4. Heuristic Generic Park Filter
    // Drops generic parks that happen to get wrapped up in 'roller skating rink in X' results.
    // Preserves actual skate parks, amusement parks, and valid rinks inside parks (e.g. Branch Brook Park Roller Skating Center)
    if (lowerName.endsWith('park') || lowerName.includes(' park ')) {
        if (!lowerName.includes('skate') && !lowerName.includes('skateland') && !lowerName.includes('rink') && !lowerName.includes('roller') && !lowerName.includes('arena') && !lowerName.includes('amusement')) {
            return true;
        }
    }
    // 5. Heuristic Generic Facility Filter (Community Centers, Halls, Plazas, Squares, Fairgrounds)
    const genericFacilityRegex = /(community center|recreation center|rec center|youth center|meeting hall|memorial hall|fairgrounds| square|plaza|watersports|shutters| skis| ski shop|lodge|resort|funplex|family fun|eat & play|water park)/;
    if (genericFacilityRegex.test(lowerName)) {
        if (!lowerName.includes('skate') && !lowerName.includes('skateland') && !lowerName.includes('rink') && !lowerName.includes('roller') && !lowerName.includes('arena') && !lowerName.includes('rollerblade')) {
            return true;
        }
    }
    // 4. Cross-contamination: Prevent Google from returning Skate Parks when searching for Rinks/Shops
    if (facilityType !== 'skate_park') {
        if (lowerName.includes('skate park') || lowerName.includes('skatepark')) {
            return true;
        }
    }
    // 5. For skate_park results, require at least one allow-list keyword in the name.
    //    This prevents pure skateboard parks from slipping through.
    if (facilityType === 'skate_park') {
        const passesAllowList = SKATE_PARK_NAME_ALLOWLIST.some(kw => lowerName.includes(kw));
        if (!passesAllowList)
            return true; // reject if no allow-list keyword found
    }
    return false;
}
class GooglePlacesProvider {
    /**
     * Sweeps a region for spots matching a specific facility type.
     * @param location     State name or city/region string passed to Google text search.
     * @param facilityType Determines which search terms and allow-list filters to apply.
     */
    static async searchRegion(location, facilityType = 'roller_rink') {
        const searchTerms = SEARCH_TERMS[facilityType];
        const verifiedPlaceIds = new Set();
        for (const term of searchTerms) {
            try {
                const query = `${term} in ${location}`;
                console.log(`[GooglePlaces] Querying: "${query}"`);
                let pageToken = undefined;
                let pagesFetched = 0;
                while (pagesFetched < 3) {
                    const params = {
                        params: {
                            query,
                            key: API_KEY,
                            pagetoken: pageToken,
                        }
                    };
                    const response = await googleClient.textSearch(params);
                    if (response.data.results) {
                        for (const result of response.data.results) {
                            const name = result.name || '';
                            const types = result.types || [];
                            if (!isPolluted(name, types, facilityType)) {
                                if (result.place_id)
                                    verifiedPlaceIds.add(result.place_id);
                            }
                            else {
                                console.log(`[GooglePlaces] 🚫 Rejected: "${name}" [types: ${types.join(', ')}]`);
                            }
                        }
                    }
                    if (response.data.next_page_token) {
                        pageToken = response.data.next_page_token;
                        pagesFetched++;
                        await new Promise(r => setTimeout(r, 2000)); // Google token delay
                    }
                    else {
                        break;
                    }
                }
            }
            catch (error) {
                console.error(`[GooglePlaces] Error for term "${term}":`, error.message);
                break;
            }
        }
        return Array.from(verifiedPlaceIds);
    }
    /**
     * Fetches full Place Details for a given place_id.
     * Returns null if place has no coordinates (unusable for mapping).
     */
    static async getPlaceDetails(placeId) {
        try {
            const params = {
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
                        "types",
                        // Extended fields — capture everything Google gives us
                        "editorial_summary",
                        "photos",
                        "business_status",
                        "url",
                    ],
                    key: API_KEY
                }
            };
            const response = await googleClient.placeDetails(params);
            const place = response.data.result;
            if (!place)
                return null;
            if (!place.geometry?.location)
                return null;
            return {
                place_id: place.place_id,
                name: place.name || 'Unknown',
                formatted_address: place.formatted_address,
                lat: place.geometry.location.lat,
                lng: place.geometry.location.lng,
                rating: place.rating,
                user_ratings_total: place.user_ratings_total,
                website: place.website,
                formatted_phone_number: place.international_phone_number || place.formatted_phone_number,
                // Full opening_hours object — has periods (structured) AND weekday_text (human readable)
                opening_hours: place.opening_hours ?? null,
                // Extended fields
                editorial_summary: place.editorial_summary?.overview ?? null,
                photos: place.photos
                    ? place.photos.slice(0, 5).map((p) => p.photo_reference)
                    : null,
                business_status: place.business_status ?? null,
                google_maps_url: place.url ?? null,
                types: place.types ?? null,
            };
        }
        catch (error) {
            console.error(`[GooglePlaces] Details error for ${placeId}:`, error.message);
            return null;
        }
    }
}
exports.GooglePlacesProvider = GooglePlacesProvider;
