# PLAN: perf/music-mode-dep-narrowing

## Goal
Narrow the dependency array of the Music mode `useEffect` in `DockedController.tsx` to prevent spurious BLE writes from firing when non-music state values coincidentally change while the user is in Music mode.

## Problem
The Music mode effect (DockedController.tsx ~line 681) triggers `handleMusicChange` → a full BLE write on 6 dependency values:
```ts
}, [activeMode, musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);
```
Any of those 6 changing while in MUSIC mode fires a hardware write. This causes:
1. Unwanted mid-session writes when the user brushes a color slider for any reason
2. Duplicate writes when entering Music mode (both the `activeMode` change AND a settings dep change can fire in the same batch)
3. The write is not debounce-protected like slider writes — it fires immediately

## Root Cause
The effect correctly uses `activeMode` to gate entry into MUSIC mode, but it treats ALL 6 values as equal-priority triggers. The intent is: write when entering the mode OR when a music-specific setting changes. But `musicPrimaryColor` and `musicSecondaryColor` are also modified by the color picker, which can fire during mode entry transitions.

## Target File
`src/components/DockedController.tsx`

## Fix

### Step 1: Add a `isInMusicMode` ref
```ts
const isInMusicModeRef = useRef(activeMode === 'MUSIC');
isInMusicModeRef.current = activeMode === 'MUSIC';
```

### Step 2: Separate entry trigger from settings trigger
```ts
// Trigger 1: Mode entry
React.useEffect(() => {
  if (activeMode !== 'MUSIC' || !writeToDevice) return;
  handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
// eslint-disable-next-line react-hooks/exhaustive-deps
}, [activeMode]); // ONLY fires on mode entry — not on color changes

// Trigger 2: Settings change (only fires when already in Music mode)
React.useEffect(() => {
  if (!isInMusicModeRef.current || !writeToDevice) return;
  handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
// eslint-disable-next-line react-hooks/exhaustive-deps
}, [musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);
```

This ensures:
- Mode entry: always fires once, cleanly
- Settings change: only fires when user is actively in Music mode and deliberately changes a setting
- Color picker bleed: eliminated (musicPrimaryColor/Secondary won't trigger writes unless in Music mode)

### Step 3: TSC from master

## Rollback
Revert to the original 6-dep single effect. Zero functional regression beyond the spurious writes resuming.

## Collateral Damage Locks
- DO NOT change `handleMusicChange` signature or implementation
- DO NOT modify `useMusicMode.ts`
- ONLY split the single effect into two targeted effects
