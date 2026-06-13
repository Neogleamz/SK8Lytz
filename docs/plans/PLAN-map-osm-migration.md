# PLAN: feat/map-osm-migration

> **Status:** ✅ READY
> **Risk:** L-RISK — No library swap, no schema change, surgical edits to 2 components + app.json
> **Prerequisites:** None

---

## Goal

Replace Google Maps tile rendering with OpenStreetMap (OSM) tiles. No library swap required — `react-native-maps` supports custom tile overlays via `UrlTile`. This eliminates the Google Maps API key billing dependency.

Web support is NOT required. Web stubs (`.web.tsx`) for map components remain in place as-is.

---

## Approach

Keep `react-native-maps`. Add `<MapView.UrlTile>` with the OSM tile CDN URL. Remove `PROVIDER_GOOGLE`.

```
BEFORE: <MapView provider={PROVIDER_GOOGLE} ...>
AFTER:  <MapView provider={PROVIDER_DEFAULT} ...>
          <UrlTile urlTemplate="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
                   maximumZ={19} flipY={false} />
```

The `UrlTile` component renders OSM raster tiles directly on top of the base map canvas. `PROVIDER_DEFAULT` on Android renders a blank grey canvas — the OSM tiles paint over it completely.

---

## Target Files

### [MODIFY] `src/components/crew/CrewLandingMap.tsx`

1. Remove `PROVIDER_GOOGLE` from import: `import MapView, { Marker, PROVIDER_DEFAULT } from 'react-native-maps';`
2. Replace `provider={PROVIDER_GOOGLE}` with `provider={PROVIDER_DEFAULT}` on `<MapView>`
3. Add `<UrlTile>` as first child of `<MapView>`:
   ```tsx
   import MapView, { Marker, UrlTile, PROVIDER_DEFAULT } from 'react-native-maps';
   // Inside <MapView ...>:
   <UrlTile
     urlTemplate="https://tile.openstreetmap.org/{z}/{x}/{y}.png"
     maximumZ={19}
     flipY={false}
   />
   ```

**Collateral Damage Lock:** Do NOT touch marker render, session logic, filter props, animation refs, or `useEffect` dependencies. Only the `provider` prop and the new `UrlTile` child.

---

### [MODIFY] `src/components/LocationPickerMap.tsx`

Same two-line change:
1. Remove `PROVIDER_GOOGLE` from import, add `UrlTile`
2. Swap `provider={PROVIDER_GOOGLE}` → `provider={PROVIDER_DEFAULT}`
3. Add `<UrlTile>` as first child

---

### [MODIFY] `app.json`

Remove the `googleMapsApiKey` entry from the Android config block (or leave as empty string `""` if other Google services use it):

```json
// BEFORE
"android": {
  "config": {
    "googleMaps": { "apiKey": "AIza..." }
  }
}

// AFTER — remove the googleMaps block entirely
// (only if no other Google service in the app uses this key)
```

> ⚠️ **Decision gate**: Check if `EXPO_PUBLIC_GOOGLE_MAPS_API_KEY` is used anywhere else (Places API, Geocoding, etc.) before removing. If yes, leave the key in place — OSM tiles don't need it, but Places API calls still might.

---

## Verification

1. Build release APK: `gradlew assembleRelease`
2. Open Crew tab → map renders with OSM street tiles (not Google's visual style)
3. Location picker map also shows OSM tiles
4. No `OVER_QUERY_LIMIT` or `REQUEST_DENIED` console errors
5. Markers (crew pins, skate spots) still render correctly on top of OSM layer
6. TSC: `npx tsc --noEmit` — 0 errors

## Rollback

Revert the two `UrlTile` additions and swap `PROVIDER_DEFAULT` back to `PROVIDER_GOOGLE`. One-line rollback per file.
