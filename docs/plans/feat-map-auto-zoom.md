# ⚡ Flash-Executable Implementation Plan: Map Auto Zoom

> **WARNING TO AUTHOR (THINK MODEL)**: This plan is designed to be executed blindly by a `[🤖 FLASH]` or pure execution model in a future session.
> Do NOT use line numbers (`Replace lines 45-50`). The codebase may have drifted between plan creation and execution.
> You MUST use **Semantic Anchors** (e.g. `Find the entire useBLE hook`, `Search for the exact phrase: return <View>`).
> All code snippets must be 100% complete, fully typed, and ready to be copy-pasted via the `replace_file_content` tool.

---

## 1. Pre-Flight Context Check (Drift Verification)

- [ ] **Check 1:** Open `src/screens/SkateMapScreen.tsx`. Verify `const mapRef = useRef<any>(null);` exists.
- [ ] **Check 2:** Open `src/screens/SkateMapScreen.tsx`. Verify `// Mock recenter to area` exists in the crosshair FAB.

---

## 2. Step-by-Step Execution Strict Instructions

### Step 2.1: Add LocationService Import
- **Target File:** `src/screens/SkateMapScreen.tsx`
- **Semantic Anchor:** Top imports section
- **Action:** Multi-Replace
- **Anchor 1:** Find `import { AppLogger } from '../services/AppLogger';`
- **Exact Replacement:**
```typescript
import { AppLogger } from '../services/AppLogger';
import { locationService } from '../services/LocationService';
```

### Step 2.2: Add Silent Location Auto-Zoom
- **Target File:** `src/screens/SkateMapScreen.tsx`
- **Semantic Anchor:** The `SCREEN_OPENED` tracking effect inside the component.
- **Action:** Multi-Replace
- **Anchor 1:** 
```typescript
  // ── Screen Navigation Telemetry ────────────────────────────────────────────
  useEffect(() => {
    if (visible) AppLogger.log('SCREEN_OPENED', { screen: 'SkateMapScreen' });
  }, [visible]);
```
- **Exact Replacement:**
```typescript
  // ── Screen Navigation Telemetry & Auto-Zoom ────────────────────────────────
  useEffect(() => {
    if (visible) {
      AppLogger.log('SCREEN_OPENED', { screen: 'SkateMapScreen' });
      
      // Auto-zoom to local radius instantly using silent cached location (no permission prompt)
      locationService.getSilentLocation().then(pos => {
        if (pos && mapRef.current) {
          mapRef.current.animateToRegion({
            latitude: pos.lat,
            longitude: pos.lng,
            latitudeDelta: 0.15,
            longitudeDelta: 0.15,
          }, 1000);
        }
      });
    }
  }, [visible]);
```

### Step 2.3: Wire GPS Crosshairs Button
- **Target File:** `src/screens/SkateMapScreen.tsx`
- **Semantic Anchor:** The `TouchableOpacity` rendering `crosshairs-gps`.
- **Action:** Multi-Replace
- **Anchor 1:**
```typescript
      {/* Density Scanner FAB */}
      <TouchableOpacity 
        style={styles.fab} 
        onPress={() => {
          if (mapRef.current) {
            // Mock recenter to area or trigger reload
            // In future, can jump to current user location
          }
        }}
      >
        <MaterialCommunityIcons name="crosshairs-gps" size={24} color="#000" />
      </TouchableOpacity>
```
- **Exact Replacement:**
```typescript
      {/* Density Scanner FAB */}
      <TouchableOpacity 
        style={styles.fab} 
        onPress={async () => {
          if (mapRef.current) {
            const loc = await locationService.getSessionLocation();
            if (loc) {
              mapRef.current.animateToRegion({
                latitude: loc.coords.lat,
                longitude: loc.coords.lng,
                latitudeDelta: 0.05,
                longitudeDelta: 0.05,
              }, 600);
            }
          }
        }}
      >
        <MaterialCommunityIcons name="crosshairs-gps" size={24} color="#000" />
      </TouchableOpacity>
```

---

## 3. Post-Execution Verification

- [ ] **Command:** `npx tsc --noEmit`
  - _Expected Output:_ Clean exit proving no dangling Dashboard imports.

---

**Completion:** `feat(ui): implement silent map auto-zoom and wire gps crosshair fab in skate map`
