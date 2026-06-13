# PLAN: feat/map-filters-crew-overhaul

> **Status:** ✅ READY  
> **Worktree:** `SK8Lytz-worktrees/map-filters-crew-overhaul`  
> **Risk:** M-RISK — touches shared filter hook, map render, and LocationService query  
> **Prerequisites:** None

---

## Goal

Fix broken map filter logic, replace Indoor toggle with Crew Sessions toggle, implement color-coded custom map markers that exactly match their toggle pill color, render crew session pins using the crew's avatar (not the user's), and replace the tap-to-cycle radius pill with an Option C inline dropdown.

---

## Confirmed Color Palette (SSOT)

| Layer | Hex | Toggle Label |
|---|---|---|
| 🏟️ Rinks (`roller_rink`) | `#3B82F6` | Rinks |
| 🏬 Shops (`skate_shop`) | `#8B5CF6` | Shops |
| 🛹 Parks (`skatepark`) | `#92400E` | Parks |
| 🔥 Crew Sessions | `#F97316` | Crews |

---

## Target Files

1. `src/services/LocationService.ts`
2. `src/hooks/useMapFilters.ts`
3. `src/components/crew/MapFiltersTray.tsx`
4. `src/components/crew/CrewLandingScreen.tsx`
5. `src/components/crew/CrewLandingMap.tsx`

---

## Step-by-Step Execution

### STEP 1 — `LocationService.ts`

**Target:** Lines 98 and 185–199, plus interface at 322–336.

**Change 1a — Expand `SESSION_SELECT` to include crew avatar fields:**
```typescript
// OLD
const SESSION_SELECT = 'id, name, invite_code, location_label, location_coords, scheduled_at, created_at, is_public, crew_members(count), crews(name)';

// NEW
const SESSION_SELECT = 'id, name, invite_code, location_label, location_coords, scheduled_at, created_at, is_public, crew_id, crew_members(count), crews(name, avatar_url, avatar_icon, avatar_color)';
```

**Change 1b — Add 3 avatar fields to the `NearbySession` mapping (lines 185–199):**
```typescript
return {
  // ...existing fields unchanged...
  crewAvatarUrl:   s.crews?.avatar_url   ?? null,
  crewAvatarIcon:  s.crews?.avatar_icon  ?? null,
  crewAvatarColor: s.crews?.avatar_color ?? null,
};
```

**Change 1c — Add 3 fields to `NearbySession` interface (line 322+):**
```typescript
crewAvatarUrl:   string | null;
crewAvatarIcon:  string | null;
crewAvatarColor: string | null;
```

**Change 1d — Add `facility_type` to `NearbySkateSpot` interface and mapping:**
```typescript
// In getNearbySkateSpots() map():
facility_type: spot.facility_type ?? null,

// In NearbySkateSpot interface:
facility_type: string | null;
```

---

### STEP 2 — `useMapFilters.ts`

**Change 2a — Replace `requireIndoor` with `showCrewSessions` in the type:**
```typescript
export type MapFilterMatrix = {
  showRinks: boolean;
  showParks: boolean;
  showShops: boolean;
  showCrewSessions: boolean; // replaces requireIndoor
};
```

**Change 2b — Update `DEFAULT_FILTERS`:**
```typescript
const DEFAULT_FILTERS: MapFilterMatrix = {
  showRinks: true,
  showParks: true,
  showShops: true,
  showCrewSessions: true, // default on
};
```

**Change 2c — Migration guard in `loadCache`:**
```typescript
// After JSON.parse, strip legacy key and inject new default
const parsed = JSON.parse(cached);
delete parsed.requireIndoor;
if (parsed.showCrewSessions === undefined) parsed.showCrewSessions = true;
setFilters({ ...DEFAULT_FILTERS, ...parsed });
```

**Change 2d — Rewrite `applyFilters` with clean logic:**
```typescript
const applyFilters = (spots: NearbySkateSpot[]) => {
  return spots.filter(spot => {
    const t = spot.facility_type;
    if (t === 'roller_rink') return filters.showRinks;
    if (t === 'skatepark')   return filters.showParks;
    if (t === 'skate_shop')  return filters.showShops;
    // No facility_type = legacy fallback: always show
    return true;
  });
};
```

> ⚠️ **COLLATERAL DAMAGE LOCK**: The `status` FSM state, AsyncStorage key, and `toggleFilter` function signature are UNTOUCHED. Only the `MapFilterMatrix` type, defaults, migration guard, and filter predicate change.

---

### STEP 3 — `MapFiltersTray.tsx`

**Change 3a — Replace Indoor option with Crew Sessions:**
```typescript
const FILTER_OPTS = [
  { key: 'showRinks',        label: 'Rinks', icon: 'roller-skate',        activeColor: '#3B82F6' },
  { key: 'showParks',        label: 'Parks', icon: 'flag-triangle',        activeColor: '#92400E' },
  { key: 'showShops',        label: 'Shops', icon: 'storefront-outline',   activeColor: '#8B5CF6' },
  { key: 'showCrewSessions', label: 'Crews', icon: 'account-group-outline', activeColor: '#F97316' },
] as const;
```

No other changes to this file.

---

### STEP 4 — `CrewLandingScreen.tsx`

**Change 4a — Add `showRadiusPicker` state:**
```typescript
const [showRadiusPicker, setShowRadiusPicker] = useState(false);
```

**Change 4b — Replace tap-to-cycle radius pill with dropdown trigger:**
```tsx
<TouchableOpacity
  style={[styles.radiusPill, { flexDirection: 'row', alignItems: 'center', ... }]}
  onPress={() => setShowRadiusPicker(p => !p)}
>
  <MaterialCommunityIcons name="map-marker-radius" size={13} color="#FFAA00" />
  <Text style={[styles.radiusPillTextActive, { fontSize: 11, fontWeight: '700' }]}>
    {discoverRadiusMi != null ? `${discoverRadiusMi} mi` : 'All'}
  </Text>
  <MaterialCommunityIcons name={showRadiusPicker ? 'chevron-up' : 'chevron-down'} size={11} color="#FFAA00" />
</TouchableOpacity>

{showRadiusPicker && (
  <View style={styles.radiusDropdown}>
    {([20, 50, 100, 250, null] as (number | null)[]).map(r => (
      <TouchableOpacity
        key={String(r)}
        style={[styles.radiusOption, discoverRadiusMi === r && styles.radiusOptionActive]}
        onPress={() => { setDiscoverRadiusMi(r); setShowRadiusPicker(false); }}
      >
        <Text style={[styles.radiusOptionText, discoverRadiusMi === r && { color: '#FFAA00' }]}>
          {r != null ? `${r} mi` : 'All'}
        </Text>
        {discoverRadiusMi === r && <MaterialCommunityIcons name="check" size={12} color="#FFAA00" />}
      </TouchableOpacity>
    ))}
  </View>
)}
```

**Change 4c — Wire `showCrewSessions` filter to nearbySessions:**
```tsx
// In CrewLandingMap props:
nearbySessions={filters.showCrewSessions ? nearbySessions : []}
```

**Change 4d — Add dropdown styles:**
```typescript
radiusDropdown: {
  position: 'absolute',
  top: 44, // below the filter bar
  left: 0,
  backgroundColor: '#1A1A1A',
  borderRadius: 10,
  borderWidth: 1,
  borderColor: 'rgba(255,170,0,0.3)',
  zIndex: 100,
  minWidth: 100,
  overflow: 'hidden',
},
radiusOption: {
  flexDirection: 'row',
  justifyContent: 'space-between',
  alignItems: 'center',
  paddingHorizontal: 14,
  paddingVertical: 9,
  borderBottomWidth: 1,
  borderBottomColor: 'rgba(255,255,255,0.05)',
},
radiusOptionActive: { backgroundColor: 'rgba(255,170,0,0.08)' },
radiusOptionText: { color: '#CCC', fontSize: 13, fontWeight: '600' },
```

> ⚠️ **COLLATERAL DAMAGE LOCK**: Do NOT touch session joining logic, crew list rendering, manage/create/join navigation, or any BLE dispatch. Surgical edits only to the filter bar section (lines ~541–580) and styles.

---

### STEP 5 — `CrewLandingMap.tsx`

**Change 5a — Replace `pinColor` string logic with custom marker views:**

For each `nearbySpots` marker, replace:
```tsx
<Marker ... pinColor={pin} title={...} description={...} />
```

With a custom view marker:
```tsx
<Marker key={`spot-${spot.id}`} coordinate={...} title={spot.name} description={desc}>
  <View style={{
    width: 28, height: 28, borderRadius: 14,
    backgroundColor: pinHex,
    borderWidth: 2, borderColor: '#FFF',
    justifyContent: 'center', alignItems: 'center',
    shadowColor: pinHex, shadowRadius: 6, shadowOpacity: 0.6,
  }}>
    <MaterialCommunityIcons name={pinIcon} size={14} color="#FFF" />
  </View>
</Marker>
```

Pin color/icon lookup:
```typescript
function getSpotMarker(spot: NearbySkateSpot): { hex: string; icon: string } {
  if (spot.facility_type === 'roller_rink') return { hex: '#3B82F6', icon: 'roller-skate' };
  if (spot.facility_type === 'skate_shop')  return { hex: '#8B5CF6', icon: 'storefront-outline' };
  if (spot.facility_type === 'skatepark')   return { hex: '#92400E', icon: 'flag-triangle' };
  return { hex: '#555', icon: 'map-marker' }; // unknown legacy
}
```

**Change 5b — Crew session pin — 3-tier avatar logic:**
```tsx
{nearbySessions.map((s: NearbySession) => {
  if (!s.lat || !s.lng) return null;
  return (
    <Marker key={`session-${s.id}`} coordinate={{ latitude: s.lat, longitude: s.lng }} onPress={() => handleJoinById(s.id)}>
      <Animated.View style={{ opacity: pulseAnim }}>
        <View style={{
          width: 36, height: 36, borderRadius: 18,
          borderWidth: 3, borderColor: '#F97316',
          overflow: 'hidden',
          shadowColor: '#F97316', shadowRadius: 8, shadowOpacity: 0.8,
        }}>
          {s.crewAvatarUrl ? (
            <Image source={{ uri: s.crewAvatarUrl }} style={{ width: '100%', height: '100%' }} />
          ) : (
            <View style={{ flex: 1, backgroundColor: s.crewAvatarColor || '#F97316', justifyContent: 'center', alignItems: 'center' }}>
              <MaterialCommunityIcons name={(s.crewAvatarIcon as any) || 'account-group'} size={18} color="#FFF" />
            </View>
          )}
        </View>
      </Animated.View>
      {/* Label callout below the pin */}
      <View style={{ backgroundColor: 'rgba(0,0,0,0.85)', padding: 4, borderRadius: 4, alignSelf: 'center', minWidth: 80, alignItems: 'center', marginTop: 4 }}>
        <Text style={{ color: '#FFF', fontSize: 10, fontWeight: '700' }}>{s.crewName || s.name}</Text>
        <Text style={{ color: '#F97316', fontSize: 9 }}>{s.memberCount} Skaters</Text>
      </View>
    </Marker>
  );
})}
```

> ⚠️ **COLLATERAL DAMAGE LOCK**: Do NOT touch `useEffect` for map zoom, `discoverRadiusMi` effect, or `MapViewCluster` config. Only the marker render JSX inside the two `.map()` blocks changes.

---

## Rollback Strategy

If custom markers cause a blank map or crash on Android:
1. `git stash` in worktree
2. Revert `CrewLandingMap.tsx` to `pinColor` approach (one-line rollback)
3. Re-test on device

Custom marker crash is the most likely failure mode — it's isolated to one component and reverts cleanly.

---

## Verification

1. **TSC:** `npx tsc --noEmit` — 0 errors
2. **Visual:** Rink pins are blue, shop pins are purple, park pins are brown, crew beacons are orange-ringed with crew avatar
3. **Toggle parity:** Toggling Rinks pill hides all `#3B82F6` pins. Toggling Crews hides all orange session beacons.
4. **Radius dropdown:** Tap pill → dropdown appears. Select `50 mi` → dropdown closes, map re-centers. Label updates to `50 mi`.
5. **AsyncStorage migration:** Force-reload with old cached filters — `requireIndoor` strips cleanly, `showCrewSessions` defaults to `true`.
