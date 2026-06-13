# PLAN: Pattern Favorites v2 — Full State Persistence

**Slug**: `feat/pattern-favorites-v2`
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: L-RISK (additive — doesn't break existing favorites)
**Prerequisite**: `feat/pattern-engine-expansion-group7/8/9` (so new pattern IDs are available)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem with v1

The current `FavoritesPanel` / favorites system saves a raw snapshot of device state. The exact fields it captures may not include:
- `fixedPatternId` (which of the 28/45 patterns)
- `fixedFgColor` + `fixedBgColor` (hex strings)
- `transitionType` (for builder-mode patterns)
- `direction`
- `activeMode` ('FIXED', 'MUSIC', 'PROGRAMS', etc.)
- `fixedSubMode` ('PATTERN' vs 'BUILDER')

Without all of these, loading a favorite may fire the hardware correctly but the UI controls won't restore visually — the user sees their strip but the pickers and sliders don't match.

---

## v2 Full State Schema

```typescript
interface FavoriteV2 {
  id: string;
  name: string;                          // User-given name, e.g. "Rink Night Red"
  version: 2;                            // Schema version flag for migration

  // Mode routing
  activeMode: 'FIXED' | 'MUSIC' | 'PROGRAMS' | 'STREET';
  fixedSubMode?: 'PATTERN' | 'BUILDER';  // Only relevant for FIXED mode

  // Pattern state (FIXED → PATTERN sub-mode)
  fixedPatternId?: number;               // 1–45
  fixedFgColor?: string;                 // '#RRGGBB'
  fixedBgColor?: string;                 // '#RRGGBB'
  direction?: 0 | 1;

  // Builder state (FIXED → BUILDER sub-mode)
  builderNodes?: BuilderNode[];          // From PositionalGradientBuilder
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;

  // Symphony state (future)
  symphonyEffectId?: number;             // 1–33

  // Shared controls
  speed: number;               // 1–100
  brightness: number;          // 0–100

  // Metadata
  user_id?: string;            // For cloud sync
  created_at: string;
  thumbnail?: string;          // hex array of first 10 LED colors for preview swatch
}
```

---

## Generating the Thumbnail

When saving a favorite, generate a 10-LED color snapshot for the shelf preview:

```typescript
function generateFavoriteThumbnail(state: FavoriteV2): string[] {
  if (state.fixedSubMode === 'BUILDER' && state.builderNodes) {
    return PositionalMathBuffer
      .generateArray(state.builderNodes, 10, state.builderFillMode === 'GRADIENT')
      .map(c => `rgb(${c.r},${c.g},${c.b})`);
  }
  if (state.fixedPatternId && state.fixedFgColor && state.fixedBgColor) {
    const fg = hexToRgb(state.fixedFgColor);
    const bg = hexToRgb(state.fixedBgColor);
    return getHardwarePixelArray(state.fixedPatternId, fg, bg, 10)
      ?.map(c => `rgb(${c.r},${c.g},${c.b})`) ?? [];
  }
  return Array(10).fill('#111111');
}
```

This thumbnail is stored in `FavoriteV2.thumbnail` and used by the favorites shelf to render a color preview without re-running the math.

---

## Persistence

### Schema
```typescript
// Supabase: favorites table (already exists — check current columns)
// Need to add: version, fixedSubMode, builderNodes, builderFillMode, builderTransitionType,
//              symphonyEffectId, thumbnail

// Migration: ALTER TABLE favorites ADD COLUMN IF NOT EXISTS version INT DEFAULT 1;
// etc. — run via Supabase MCP apply_migration
```

### Local fallback
```typescript
// AsyncStorage key: '@Sk8lytz_Favorites_v2'
// Old key '@Sk8lytz_Favorites' should be migrated on first load
```

---

## Migration from v1

On app startup, if `@Sk8lytz_Favorites` exists and `@Sk8lytz_Favorites_v2` doesn't:
```typescript
const old = await AsyncStorage.getItem('@Sk8lytz_Favorites');
if (old) {
  const parsed = JSON.parse(old);
  const migrated = parsed.map(fav => ({
    ...fav,
    version: 2,
    // Map old field names to new schema
    fixedPatternId: fav.patternId,
    fixedFgColor: fav.fixedFgColor ?? '#FF0000',
    fixedBgColor: fav.fixedBgColor ?? '#000000',
    // thumbnail won't exist — generate on next open
  }));
  await AsyncStorage.setItem('@Sk8lytz_Favorites_v2', JSON.stringify(migrated));
}
```

---

## Loading a Favorite

When user taps a favorite, restore ALL state:
```typescript
const loadFavoriteV2 = (fav: FavoriteV2) => {
  setActiveMode(fav.activeMode);
  setSpeed(fav.speed);
  setBrightness(fav.brightness);

  if (fav.fixedSubMode === 'PATTERN' && fav.fixedPatternId) {
    setFixedSubMode('PATTERN');
    setFixedPatternId(fav.fixedPatternId);
    setFixedFgColor(fav.fixedFgColor ?? '#FF0000');
    setFixedBgColor(fav.fixedBgColor ?? '#000000');
    applyFixedPattern(fav.fixedPatternId, fav.fixedFgColor, fav.fixedBgColor, fav.speed, fav.brightness);
  }

  if (fav.fixedSubMode === 'BUILDER' && fav.builderNodes) {
    setFixedSubMode('BUILDER');
    setBuilderNodes(fav.builderNodes);
    setFillMode(fav.builderFillMode ?? 'GRADIENT');
    setTransitionType(fav.builderTransitionType ?? 0x03);
    // Builder's own useEffect will re-dispatch to hardware automatically
  }
};
```

---

## UI Changes

### Favorites Shelf (existing)
- Keep existing shelf layout
- Add color thumbnail strip (10 dots) to each card using `FavoriteV2.thumbnail`
- Add mode badge: `PATTERN`, `BUILDER`, `SYMPHONY`

### Save Flow
- Prompt for name (existing save modal pattern from PositionalGradientBuilder)
- Auto-generate thumbnail before saving
- Show "Saved to cloud" vs "Saved locally" indicator

---

## Files To Touch

| File | Change |
|------|--------|
| `src/types/favorites.ts` | Define `FavoriteV2` interface [NEW or MODIFY] |
| `src/services/FavoritesService.ts` | Add v2 save/load/migrate logic [MODIFY] |
| `src/hooks/useFavorites.ts` | Update `loadFavorite()` to restore full state [MODIFY] |
| `src/components/FavoritesPanel.tsx` or equivalent | Add thumbnail strip, mode badge [MODIFY] |
| Supabase migration | Add version + new columns to favorites table |

---

## Test Criteria

- [ ] Save a PATTERN favorite (Police Lights, FG=red, BG=blue, speed=70) → load it → hardware fires Police Lights red/blue at speed 70, UI shows correct color pickers and speed slider
- [ ] Save a BUILDER favorite (gradient red→purple, GRADIENT fill) → load it → builder nodes restored, hardware plays gradient
- [ ] v1 favorites migrate without crash
- [ ] Thumbnail renders correct colors for both pattern and builder favorites
- [ ] Cloud save works when logged in; local fallback when not
- [ ] Deleting a cloud favorite removes from Supabase, not just local
