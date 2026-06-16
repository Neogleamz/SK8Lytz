# Implementation Plan: Fix BLE Group Sync Debounce & Missing Colors

## Goal Description
Fix the "Global Debouncer Bug" in `BleWriteDispatcher.ts` that drops sequential writes to grouped skates during rapid color slider drags, causing only one skate to update. Also, restore the missing white, black, cyan, and magenta dots in `UniversalColorGrid.tsx` that were omitted during a recent refactor.

## Proposed Changes

### Files to Create/Modify
#### [MODIFY] `src/hooks/useBLE.ts`
- Convert the global `writeDebounceTimerRef` and `writeDebounceResolveRef` from storing single timeouts to storing a `Map<string, ReturnType<typeof setTimeout>>` and `Map<string, (result: boolean | 'partial') => void>` keyed by `targetDeviceId`.

#### [MODIFY] `src/services/BleWriteDispatcher.ts`
- Update `BleWriteStateRefs` interface to type the debounce refs as Maps.
- Refactor `executeWriteToDevice` and `executeProtocolResults` to use `.get(key)`, `.set(key, ...)`, and `.delete(key)` and `.has(key)` for timers.
- Use `targetDeviceId ?? 'global'` as the key in `executeWriteToDevice`.
- Use `'protocol_results'` as the key in `executeProtocolResults`.

#### [MODIFY] `src/components/docked/UniversalColorGrid.tsx`
- Add `#FFFFFF` (White), `#000000` (Black), `#00FFFF` (Cyan), and `#FF00FF` (Magenta) back into the `PRESET_COLORS` array.
- Update `PRESET_HUE_MAP` to include hues for Cyan (`180`) and Magenta (`300`). White and Black do not use hues and are left out of the map.

## Verification Plan

### Automated Tests
- Run `npm run verify` to check Type Safety and Jest tests.

### Manual Verification
- Deploy to device and verify grouped skates both respond concurrently to color slider drags.
- Verify UniversalColorGrid displays the correct number of color dots including black and white.

## Out of Scope
- Modifying UI components outside of `UniversalColorGrid.tsx`.
- Modifying other BLE logic not related to write dispatch debouncing.
