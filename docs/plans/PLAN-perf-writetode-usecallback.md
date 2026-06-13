# PLAN: perf/write-to-device-usecallback

## Goal
Wrap `writeToDevice` and its closure dependencies in `useCallback` inside `DockedController.tsx` so that the `dockedBus` memo object gets a stable reference across renders, restoring the `React.memo` isolation of all sub-panels.

## Problem
`writeToDevice` is a plain inline `async function` defined at line 228 of `DockedController.tsx`. It is regenerated on every render. Because it sits in the `dockedBus` dependency array (line 780), `dockedBus` receives a new object reference on every render — making the `React.memo` wrappers on all sub-panels (`ProEffectsPanel`, `BuilderPanel`, `MusicPanel`, `CameraPanel`, `StreetPanel`, `FavoritesPanel`) completely ineffective. Any state change anywhere in the 1,694-line parent causes all panels to re-render.

## Root Cause
- `writeToDevice` (line 228) → inline function, new ref every render
- `captureEntireState` (line 389) → inline function that calls `baseCaptureEntireState`, new ref every render
- `applyCloudScene` (line 390) → inline function, new ref every render
- All three flow into `dockedBus` → busts memo

## Target File
`src/components/DockedController.tsx`

## Execution Checklist

### Step 1: Audit `writeToDevice` dependencies
Read lines 228–249. Identify the closed-over values:
- `parentWriteToDevice` (prop ref — stable if parent memoizes)
- `captureEntireState` (inline fn — must be useCallback'd first)
- `setVizLock` (stable setter)
- `setLastSentPayload` (stable setter)
- `optimisticWrite` (from useOptimisticBLE — check its stability)
- `activeMode`, `fixedSubMode`, `musicPatternId`, `fixedPatternId`, `selectedPatternId`, `visualizerColor` (volatile state)

### Step 2: Wrap `captureEntireState` first
```ts
const captureEntireState = React.useCallback(
  (override?: Record<string, any>) =>
    baseCaptureEntireState(streetSensitivity, streetCruiseColor, streetBrakeColor, override),
  [baseCaptureEntireState, streetSensitivity, streetCruiseColor, streetBrakeColor]
);
```

### Step 3: Wrap `applyCloudScene`
```ts
const applyCloudScene = React.useCallback(
  (scenePayload: any) =>
    baseApplyCloudScene(scenePayload, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor),
  [baseApplyCloudScene, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor]
);
```

### Step 4: Wrap `writeToDevice`
The volatile state values (`activeMode`, `fixedSubMode`, etc.) needed for `setVizLock` can be read via refs rather than being deps — this is the standard React pattern for "read latest value without making it a dep":
```ts
const activeModeRef = useRef(activeMode);
activeModeRef.current = activeMode;
// repeat for fixedSubMode, musicPatternId, fixedPatternId, selectedPatternId, visualizerColor

const writeToDevice = React.useCallback(
  async (payload: number[], override?: Record<string, any>) => {
    if (!parentWriteToDevice) return;
    lastConfirmedStateRef.current = captureEntireState(override);
    const currentResolvedMode = (activeModeRef.current === 'MULTIMODE' && fixedSubModeRef.current === 'BUILDER') ? 'BUILDER' : activeModeRef.current;
    const currentResolvedPattern = activeModeRef.current === 'MUSIC' ? musicPatternIdRef.current : ...;
    setVizLock({ mode: currentResolvedMode, patternId: currentResolvedPattern, color: visualizerColorRef.current });
    setLastSentPayload([...payload]);
    await optimisticWrite(payload);
  },
  [parentWriteToDevice, captureEntireState, optimisticWrite]
);
```

### Step 5: Verify `dockedBus` stability
After wrapping, confirm the `dockedBus` useMemo dep array only contains stable refs. Remove `writeToDevice` from the dep array check — it should now be stable.

### Step 6: Run TSC from master
```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 20
```

### Step 7: Verify render count reduction
Add a temporary `console.log('ProEffectsPanel render')` inside `ProEffectsPanel` and confirm it no longer fires on brightness/speed slider drags when not on the ProEffects panel.

## Rollback
`git reset --hard HEAD` in the worktree. The stale-but-working inline function path is restored.

## Collateral Damage Locks
- DO NOT refactor `useDockedControllerState`. It is out of scope.
- DO NOT modify the `dockedBus` type definition in `dashboard.types.ts`.
- DO NOT extract any additional hooks. This is a useCallback wrapping task only.
- LEAVE `onReconcileRef.current = ...` pattern intact (line 396) — it is intentionally not a useCallback.
