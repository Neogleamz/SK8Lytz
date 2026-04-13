# Domain-Driven Architecture Refactor
### Branch: `refactor/domain-driven-architecture`
### Task Tag: `[H-RISK] [Feast] [CLOUD]`

---

## Design Decisions & Rationale

The SK8Lytz codebase has 3 God-Object monoliths collectively owning **~7,000 lines** of co-mingled BLE
transport, UI state, persistence logic, and business flows. The approach is a strict **"Domain Extraction"**
pattern: rather than creating new abstractions from scratch, we surgically decompose each monolith by migrating
responsibility clusters into purpose-built custom hooks. UI components become pure renderers consuming hook
outputs. This is the lowest-risk path because it respects the existing BLE state co-location constraint while
achieving full modularity.

---

## Current State Audit (The Problem)

### God-Object Inventory

| File | Lines | Size | Primary Violations |
|:---|:---|:---|:---|
| `DockedController.tsx` | **3,255** | 157 KB | 8+ domains: Street Mode (GPS+Accel), Session Tracking, Music, Favorites, Presets, Builder, Crew Scenes, Community — all co-mingled |
| `DashboardScreen.tsx` | **2,500** | 103 KB | 6+ domains: BLE orchestration, Auth/Profile, Group fleet, Voice, Notifications, Persistence |
| `AccountModal.tsx` | ~1,400 | 65 KB | [x] COMPLETE |
| `Sk8LytzDiagnosticLab.tsx` | **1,117** | 63 KB | Protocol builders (0x51-0x73), BLE Sniffer/Log, Transition probes |
| `AdminToolsModal.tsx` | **1,073** | 56 KB | Telemetry timeline, Analytics, Product Catalog CRUD, App Settings |

### Anti-Pattern Violations (per `clean-code.md` + `modern-ui-ux.md`)

1. ❌ **No FSMs**: `DockedController` uses 15+ scattered booleans (`isStreetBraking`, `sessionActive`,
   `isPublishingCloud`, `isCommunityModalVisible`, etc.) instead of deterministic Enum states.
2. ❌ **No Single Responsibility**: `DockedController` simultaneously owns Street Mode (GPS + Accelerometer),
   Music Mode (live Audio recording), Session Tracking (distance/speed/G-force), Crew Scenes, Community
   Publishing, Quick Presets, Favorites, and Builder Mode.
3. ❌ **Business Logic in Components**: `DashboardScreen` contains `handleRegistrationComplete()`, FTUE
   detection, group derivation from `registeredDevices`, `lastGroupPatterns` persistence, and notification
   scheduling — none of which is UI-related.
4. ❌ **No Domain Hooks**: No `useDashboardGroups`, `useStreetMode`, `useSessionTracking`, or
   `useDockedControllerState`. Every cross-cutting concern is inlined inline component state.
5. ❌ **`any` type pollution**: `hwSettings?: any`, `userProfile: any` in `SkateGroupCard`, `Record<string, any>`
   for device configs — all should be typed interfaces.

---

## Safety Constraints (NON-NEGOTIABLE)

> **BLE state MUST remain co-located with DashboardScreen.** Per the Master Reference:
> *"This is intentionally monolithic — all BLE state must be co-located to prevent race conditions."*
> We will NOT move `useBLE` out of DashboardScreen. We extract only non-BLE responsibilities.

> **DockedController is 3,255 lines.** The Surgical Strike Protocol strictly applies. We extract ONE domain
> hook at a time with an immediate `git diff` check after each extraction. We never rewrite from memory.

---

## Phase 0: Branch & Safety Anchor

**Risk**: Zero | **Duration**: ~5 min

- `git checkout master` → `git pull`
- `git checkout -b refactor/domain-driven-architecture`
- `git commit -m "chore(checkpoint): pre-refactor anchor for domain-driven-architecture"` (if uncommitted changes exist)

---

## Phase 1: Extract Dashboard Domain Hooks

**Risk**: Low-Medium | **Duration**: ~2-3 hours

Extract all non-BLE business logic from `DashboardScreen.tsx` into 3 focused hooks.
After this phase, `DashboardScreen` becomes a **BLE orchestrator + layout provider only**.

---

### [NEW] `src/hooks/useDashboardProfile.ts`

Owns everything related to the authenticated user in the Dashboard context.

**Migrated from DashboardScreen:**
- `userProfile` state + `profileService.getProfile()` fetch
- `isAccountModalVisible` toggle
- `notificationService` initialization
- `appSettings` state + `AppSettingsService.fetchAllSettings()` poll on mount & AppState foreground
- `isAdminToolsVisible` toggle
- `isSupportModalVisible` toggle

**Exported Interface:**
```typescript
interface UseDashboardProfileResult {
  userProfile: UserProfile | null;
  appSettings: AppSettingsMap;
  isAccountModalVisible: boolean;
  setIsAccountModalVisible: (v: boolean) => void;
  isAdminToolsVisible: boolean;
  setIsAdminToolsVisible: (v: boolean) => void;
  isSupportModalVisible: boolean;
  setIsSupportModalVisible: (v: boolean) => void;
}
```

---

### [NEW] `src/hooks/useDashboardGroups.ts`

Owns all group/device fleet management logic.

**Migrated from DashboardScreen:**
- `customGroups` state + derivation from `registeredDevices`
- `deviceConfigs` state + `ng_device_configs` AsyncStorage persistence
- `powerStates` map + `setPowerState()`
- `lastGroupPatterns` state + `@Sk8lytz_last_group_patterns` AsyncStorage persistence
- `isGroupModalVisible`, `groupModalMode`, `editingGroupId`
- `isDeviceListCollapsed`, `isRegisteredCollapsed`
- `selectedIds`, `isSelectionMode`
- `handleRegistrationComplete()` full logic

**FSM to replace scattered booleans:**
```typescript
type GroupModalState = 'HIDDEN' | 'CREATE' | 'RENAME';
```

**Exported Interface:**
```typescript
interface UseDashboardGroupsResult {
  customGroups: CustomGroup[];
  deviceConfigs: Record<string, DeviceSettings>;
  updateDeviceConfig: (deviceId: string, cfg: Partial<DeviceSettings>) => void;
  powerStates: Record<string, boolean>;
  setPowerState: (deviceId: string, on: boolean) => void;
  lastGroupPatterns: Record<string, string>;
  setLastGroupPattern: (groupId: string, pattern: string) => void;
  groupModalState: GroupModalState;
  openGroupCreate: () => void;
  openGroupRename: (groupId: string) => void;
  closeGroupModal: () => void;
  editingGroupId: string | null;
  selectedIds: string[];
  isSelectionMode: boolean;
  toggleDeviceSelection: (id: string) => void;
  clearSelection: () => void;
  isDeviceListCollapsed: boolean;
  setIsDeviceListCollapsed: (v: boolean) => void;
  isRegisteredCollapsed: boolean;
  setIsRegisteredCollapsed: (v: boolean) => void;
}
```

---

### [NEW] `src/hooks/useDashboardVoice.ts`

Owns voice command feature lifecycle in the dashboard.

**Migrated from DashboardScreen:**
- `isVoiceModalVisible`, `isVoiceTutorialVisible`, `isVoiceTutorialDismissed`
- `favorites` state + `@Sk8lytz_Favorites` AsyncStorage load (refreshed when voice modal opens)
- `useVoiceControl()` hook delegation
- Tutorial dismissal AsyncStorage write

**Exported Interface:**
```typescript
interface UseDashboardVoiceResult {
  isVoiceModalVisible: boolean;
  setIsVoiceModalVisible: (v: boolean) => void;
  isVoiceTutorialVisible: boolean;
  setIsVoiceTutorialVisible: (v: boolean) => void;
  isVoiceTutorialDismissed: boolean;
  dismissTutorial: () => Promise<void>;
  favorites: IFavoriteState[];
  voiceControl: ReturnType<typeof useVoiceControl>;
}
```

---

### [MODIFY] `src/screens/DashboardScreen.tsx`

**After Phase 1, DashboardScreen owns ONLY:**
- `useBLE()` (non-negotiable per Master Reference)
- `useRegistration()` (device cloud sync)
- `useDashboardProfile()` (consumption)
- `useDashboardGroups()` (consumption)
- `useDashboardVoice()` (consumption)
- Crew session state (`crewSession`, `crewRole`, `crewModeSummary`, `lastLeaderScene`) — stays here
  because it feeds directly into BLE write dispatch
- All JSX layout (4-slab structure, unchanged)
- All modal orchestration (mounting modals, wiring props)

**Target size after Phase 1:** ~1,400 lines (down from 2,500).

---

## Phase 2: Extract DockedController Domain Hooks

**Risk**: HIGH | **Duration**: ~4-5 hours | `[H-RISK]`

DockedController is 3,255 lines. We apply the **Component Extraction Escape Hatch**: extract one domain at
a time, run `npx tsc --noEmit` + `git diff` check, then proceed to the next.

**Extraction order (least risky → most risky):**

---

### [NEW] `src/hooks/useSessionTracking.ts`

GPS + Accelerometer session recording, fully decoupled from LED control.

**Migrated from DockedController:**
- All `session*` state refs (`sessionActive`, `sessionStartTimeRef`, `sessionSpeedSamplesRef`,
  `sessionDistanceMilesRef`, `sessionPeakGForceRef`, `sessionPeakSpeedRef`, `sessionLastLocationRef`)
- `showSessionModal`, `sessionSummary`
- `SpeedTrackingService.saveSession()` call
- Distance accumulation math from GPS `watchPositionAsync`
- `peakGForce` tracking from `Accelerometer.addListener`

**FSM replaces `sessionActive: boolean`:**
```typescript
type SessionState = 'IDLE' | 'RECORDING' | 'COMPLETE';
```

**Exported Interface:**
```typescript
interface UseSessionTrackingResult {
  sessionState: SessionState;
  startSession: () => void;
  stopSession: () => Promise<void>;
  sessionSummary: ISessionSnapshot | null;
  showSessionModal: boolean;
  setShowSessionModal: (v: boolean) => void;
  gpsSpeed: number;           // live read for HUD display
  peakGForce: number;         // live read for HUD display
}
```

---

### [NEW] `src/hooks/useStreetMode.ts`

Accelerometer-reactive car-light LED logic — pure hardware dispatch.

**Migrated from DockedController:**
- `streetSensitivity`, `streetCruiseColor`, `streetBrakeColor`
- `motionState` + `motionStateRef`
- `isStreetBraking` + `streetBrakingRef`
- `lastAccelRef`, `cruiseChaseRef`
- `applyStreetPattern()` — the full car-light pixel calculation function (~200 lines)
- `updateMotion()` helper
- `Accelerometer.addListener` subscription (when `activeMode === 'STREET'`)
- `Location.watchPositionAsync` subscription (when `activeMode === 'STREET'`)

**FSM (already partially present, now formalized):**
```typescript
type MotionState = 'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING';
```

**Exported Interface:**
```typescript
interface UseStreetModeResult {
  streetSensitivity: number;
  setStreetSensitivity: (v: number) => void;
  streetCruiseColor: string;
  setStreetCruiseColor: (v: string) => void;
  streetBrakeColor: string;
  setStreetBrakeColor: (v: string) => void;
  isStreetBraking: boolean;
  motionState: MotionState;
  gpsSpeed: number;
  peakGForce: number;
  // Called by DockedController when STREET mode is active to dispatch BLE
  commitStreetPattern: (brightness: number, speed: number) => void;
}
```

> **Note on GPS sharing**: Both `useStreetMode` and `useSessionTracking` need `gpsSpeed`. In Phase 2,
> `useStreetMode` will own the GPS subscription and export `gpsSpeed`. `useSessionTracking` will consume
> it as a prop parameter rather than running its own GPS subscription — avoiding duplicate listeners.

---

### [NEW] `src/hooks/useFavorites.ts`

Persistent favorites + cloud quick presets management.

**Migrated from DockedController:**
- `favorites` state + `@Sk8lytz_Favorites` AsyncStorage read/write
- `isFavPromptVisible`, `favPromptName`, `favPromptTargetId`, `activeFavoriteId`
- `quickPresets` state + Supabase cloud sync fetch
- `isQuickPromptVisible`, `quickPromptName`, `quickPromptTargetIndex`, `activeQuickPresetIndex`
- `loadFavorite()` function
- `saveFavorite()` + `deleteFavorite()` functions

**FSM replaces 6 scattered `isFavPromptVisible` + `isQuickPromptVisible` booleans:**
```typescript
type FavoritesPromptState = 'HIDDEN' | 'NAMING_FAVORITE' | 'NAMING_PRESET';
```

**Exported Interface:**
```typescript
interface UseFavoritesResult {
  favorites: IFavoriteState[];
  saveFavorite: (capturedState: Partial<IFavoriteState>, name: string) => Promise<void>;
  deleteFavorite: (id: string) => Promise<void>;
  loadFavorite: (fav: IFavoriteState) => void;
  activeFavoriteId: string | null;
  quickPresets: IQuickPreset[];
  saveQuickPreset: (index: number, preset: IQuickPreset) => Promise<void>;
  promptState: FavoritesPromptState;
  openFavoritePrompt: (targetId?: string) => void;
  openPresetPrompt: (targetIndex: number) => void;
  closePrompt: () => void;
  promptName: string;
  setPromptName: (v: string) => void;
}
```

---

### [NEW] `src/hooks/useDockedControllerState.ts`

The core LED control state — active mode, color, pattern, brightness, speed.
This is the central FSM for the entire DockedController.

**Migrated from DockedController:**
- `activeMode` (ModeType)
- `lastOperatingMode`
- `selectedColor`, `selectedHue`
- `selectedPatternId`
- `brightness`, `speed`
- `fixedSubMode` ('PATTERN' | 'BUILDER')
- `multiColors`, `multiTransition`, `multiLength`
- `builderNodes`, `builderFillMode`, `builderTransitionType`, `builderDirection`
- `musicPatternId`, `musicPrimaryColor`, `musicSecondaryColor`, `micSensitivity`, `micSource`, `musicMatrixStyle`
- `captureEntireState()` snapshot function
- `applyCloudScene()` scene restoration function
- `applySpatialSegments()` voice command integration

**Mode FSM (formalized):**
```typescript
type ModeType = 'FAVORITES' | 'MULTIMODE' | 'PROGRAMS' | 'MUSIC' | 'STREET' | 'CAMERA';
```

**Exported Interface:** Full LED control state + all setters + `captureEntireState()` + `applyCloudScene()` +
`applySpatialSegments()`.

---

### [MODIFY] `src/components/DockedController.tsx`

**After Phase 2, DockedController owns ONLY:**
- Receiving `writeToDevice` prop (no change)
- Consuming `useDockedControllerState()`, `useStreetMode()`, `useSessionTracking()`, `useFavorites()`
- Wiring hook outputs to `ZenggeProtocol.*` dispatch calls (the actual BLE commands stay here)
- All JSX render tree (unchanged visually)
- `useImperativeHandle` ref handle (now delegates to hook setters)

**Target size after Phase 2:** ~1,500 lines (down from 3,255). The JSX and protocol dispatch calls remain.
Only state management exits.

**`writeToDevice` wiring decision:** Pass `writeToDevice` as a parameter into each hook that needs to
dispatch BLE commands (`useStreetMode`, `useDockedControllerState`). This avoids Context overhead and keeps
the data-flow explicit and traceable.

---

## Phase 3: AccountModal Domain Decomposition

**Risk**: Medium | **Duration**: ~1.5 hours

AccountModal's 4 tabs are logically independent but share co-mingled data fetching.

### [NEW] `src/hooks/useAccountOverview.ts`
- Profile display: `displayName`, avatar, `userProfile`
- Theme toggle bridge to `ThemeContext`
- EULA version check + `acceptedEulaVersion` gating logic

### [NEW] `src/hooks/useSkateStats.ts`
- `SpeedTrackingService.fetchLifetimeStats()` + `fetchRecentSessions(10)`
- State FSM: `'IDLE' | 'LOADING' | 'SUCCESS' | 'ERROR'` (replaces raw `isLoading: boolean`)

### [NEW] `src/hooks/useDeviceFleet.ts`
- `registeredDevices` display list (read from `useRegistration`)
- `deregisterDevice()` action with confirmation
- Device sync status indicator

### [MODIFY] `src/components/AccountModal.tsx`
After Phase 3, AccountModal becomes a pure tab-router that passes hook results down to stateless tab
sub-components. **Target size: ~700 lines** (down from ~1,400).

---

## Phase 4: Diagnostic Lab Extraction

**Risk**: Medium | **Duration**: ~2 hours

Decompose the hardware testing monolith `Sk8LytzDiagnosticLab.tsx`.

### [NEW] `src/hooks/useDiagnosticLog.ts`
- RX/TX Buffer: Stores the last 200 packets.
- RX Listener: Listens to `liveRxPayload` and applies `0x63` parsing for hardware settings.
- Transmit Bridge: Helper that wraps `writeToDevice` with logging and hex formatting.

### [NEW] `src/hooks/useProtocolBuilder.ts`
- Protocol FSM: `0x51 | 0x59 | 0x61 | 0x73 | 0x62`.
- Param States: All inputs for builders (colors, points, speed, IC types, etc).
- Generator logic: Rebuilds the payload whenever params change.

### [MODIFY] `src/components/Sk8LytzDiagnosticLab.tsx`
- Consume `useDiagnosticLog` and `useProtocolBuilder`.
- Target size: **~500 lines** (down from 1,117).

---

## Phase 5: Admin Tools Decomposition

**Risk**: Medium | **Duration**: ~2 hours

Decompose the administrative hub `AdminToolsModal.tsx`.

### [NEW] `src/hooks/useAdminTelemetry.ts`
- Log/Stats Fetching: Pulls from `AppLogger` on mount.
- Export/Upload logic: Handles sharing and Supabase bucket uploads.
- Clear/Flush: Logic for wiping logs.

### [NEW] `src/hooks/useProductManager.ts`
- Catalog CRUD: Owns `editingProfile` state and `saveProfile` calls.
- Derivation: Logic for `blankProfile()` and `patchEdit()`.
- Bridge to `useProductCatalog`.

### [NEW] `src/hooks/useAdminSettings.ts`
- App Settings: Owns `appSettings` map and `AppSettingsService` persistence.

### [MODIFY] `src/components/AdminToolsModal.tsx`
- Consume admin hooks.
- Target size: **~400 lines** (down from 1,073).

---

## Phase 6: Type Safety Hardening

**Risk**: Low | **Duration**: ~1 hour

Fix all `any`-typed interfaces exposed by the refactor — mandatory quality gate before merge.

### [NEW] `src/types/dashboard.types.ts`

Central types contract for all domain interfaces:

```typescript
export interface DeviceSettings {
  name: string;
  type: string;
  points: number;
  segments: number;
  stripType: string;
  sorting: string;
  grouped: boolean;
  groupId?: string;
  groupName?: string;
}

export interface CustomGroup {
  id: string;
  name: string;
  isGroup: boolean;
  deviceIds: string[];
  type?: string;
  lastPatternName?: string;
}

export type GroupModalState = 'HIDDEN' | 'CREATE' | 'RENAME';
export type FavoritesPromptState = 'HIDDEN' | 'NAMING_FAVORITE' | 'NAMING_PRESET';
export type SessionState = 'IDLE' | 'RECORDING' | 'COMPLETE';
export type MotionState = 'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING';
export type ModeType = 'FAVORITES' | 'MULTIMODE' | 'PROGRAMS' | 'MUSIC' | 'STREET' | 'CAMERA';
```

### [MODIFY] `src/components/DockedController.tsx`
- Replace `hwSettings?: any` → typed `IHardwareSettings` interface
- Replace `userProfile: any` on `SkateGroupCard` → `UserProfile` from `ProfileService`

### [MODIFY] `src/hooks/useDashboardGroups.ts`
- Replace `Record<string, any>` → `Record<string, DeviceSettings>` (imported from `dashboard.types.ts`)

---

## Phase 7: Architecture Map Update

**Risk**: Zero | **Duration**: ~30 min

### [NEW / OVERWRITE] `docs/ARCHITECTURE_MAP.md`

Generate updated architecture map documenting the new domain structure, data-flow diagram, and hook
dependency graph.

---

## Final Architecture (Post-Refactor)

```
src/
├── hooks/
│   ├── useBLE.ts                      ← UNCHANGED. BLE transport layer. (1,000 lines)
│   ├── useRegistration.ts             ← UNCHANGED. Device cloud sync.
│   ├── useProductCatalog.ts           ← UNCHANGED.
│   ├── useVoiceControl.ts             ← UNCHANGED.
│   ├── useCrewHub.ts                  ← UNCHANGED.
│   ├── useCrewManage.ts               ← UNCHANGED.
│   ├── useCrewSession.ts              ← UNCHANGED.
│   │
│   ├── useDashboardProfile.ts         ← NEW: Auth, settings, modal visibility flags
│   ├── useDashboardGroups.ts          ← NEW: Fleet, groups, persistence, power states
│   ├── useDashboardVoice.ts           ← NEW: Voice command lifecycle + favorites bridge
│   │
│   ├── useDockedControllerState.ts    ← NEW: Core LED mode/color FSM + scene capture
│   ├── useStreetMode.ts               ← NEW: Accelerometer + GPS + car-light dispatch
│   ├── useSessionTracking.ts          ← NEW: GPS session recording + stats accumulation
│   └── useFavorites.ts                ← NEW: Favorites + quick presets persistence
│   ├── useAccountOverview.ts          ← COMPLETE
│   ├── useSkateStats.ts               ← COMPLETE
│   ├── useDeviceFleet.ts              ← COMPLETE
│   │
│   ├── useDiagnosticLog.ts            ← NEW: RX/TX buffer + parse logic
│   ├── useProtocolBuilder.ts          ← NEW: 0x51-0x73 payload generators
│   ├── useAdminTelemetry.ts           ← NEW: Analytics + Log export/upload
│   ├── useProductManager.ts           ← NEW: Profile CRUD logic
│   └── useAdminSettings.ts            ← NEW: App feature flag management
│
├── types/
│   ├── supabase.ts                    ← Existing
│   └── dashboard.types.ts             ← NEW: All shared domain type contracts
│
├── screens/
│   └── DashboardScreen.tsx            ← ~1,400 lines (BLE + layout only)
│
└── components/
    ├── DockedController.tsx           ← ~1,500 lines (BLE dispatch + JSX only)
    ├── AccountModal.tsx               ← ~700 lines (pure tab-router)
    ├── Sk8LytzDiagnosticLab.tsx       ← ~500 lines (diagnostic router)
    └── AdminToolsModal.tsx            ← ~400 lines (admin router)
```

---

## Open Questions (Require Your Input Before Proceed)

1. **GPS sharing between `useStreetMode` and `useSessionTracking`**: Plan is for `useStreetMode` to own the
   GPS listener and export `gpsSpeed`, then `useSessionTracking` receives it as a parameter. Does this feel
   clean to you, or do you want a shared primitive `useGPS.ts` hook?

2. **`writeToDevice` wiring into sub-hooks**: Plan is to pass `writeToDevice` as a direct parameter into
   `useStreetMode(writeToDevice, ...)` and `useDockedControllerState(writeToDevice, ...)`. This avoids
   global Context overhead and keeps data-flow traceable. Confirmed approach?

3. **Phase execution strategy**: Do you want all 5 phases in one session (~8-10h focused), or split with a
   master merge after Phase 1 and Phase 2 independently?

---

## Verification Plan

### After Each Hook Extraction
```bash
npx tsc --noEmit          # Zero TypeScript errors required
git diff HEAD             # Verify no unintended deletions outside target
```

### Test Stubs (TDD — written BEFORE each hook)
```
src/__tests__/hooks/useDashboardGroups.test.ts
src/__tests__/hooks/useFavorites.test.ts
src/__tests__/hooks/useStreetMode.test.ts
src/__tests__/hooks/useSessionTracking.test.ts
src/__tests__/hooks/useDockedControllerState.test.ts
```

### Manual QA Checklist (After Phase 2)
1. ✅ Connect to real hardware → BLE commands fire correctly
2. ✅ Cycle all 6 DockedController modes → no regression
3. ✅ Street Mode → motion state changes → LED patterns respond
4. ✅ Create a Favorite → restart app → verify persisted
5. ✅ Crew Hub → join session → Crew Mode scene sync works
6. ✅ Admin Tools → logo tap → modal opens
7. ✅ Account Modal → Stats tab loads → Device Fleet shows devices

---

## Risk Matrix

| Phase | Risk | Primary Mitigation |
|:---|:---|:---|
| 0: Branch | None | N/A |
| 1: Dashboard Hooks | Low | `tsc --noEmit` after each hook |
| 2: DockedController Hooks | **High** | Surgical 10-line edits, immediate diff checks, one hook at a time |
| 3: AccountModal | Medium | Tab-by-tab extraction |
| 4: Type Safety | Low | Pure annotation, no logic changes |
| 5: Docs | None | N/A |
