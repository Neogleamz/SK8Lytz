# Implementation Plan
# PLAN-split-brain-dedup

## Summary
Four confirmed instances of `hexToRgb` defined independently across the protocols and utils layers,
a latent split-brain cache-key divergence in hardware config storage, a dual-writer race on quick-preset
storage, and a redundant lerp function (`lerpRGBMusic` vs `lerpRGB`). The MAC-case divergence is the
highest-risk item: a lowercase MAC input causes InterrogatorService to write a key that HardwareSetupWizardScreen
cannot read (silent cache miss, device re-interrogated on every connect).

**Batch:** `BATCH:split-brain-dedup`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files

| ID | File | Line | Finding |
|----|------|------|---------|
| R-21-001 | `src/protocols/SymphonyEngine.ts` | 15 | `hexToRgb` local definition #1 |
| R-21-001 | `src/protocols/shared/engineUtils.ts` | 15 | `hexToRgb` canonical protocol copy |
| R-21-001 | `src/protocols/PositionalMathBuffer.ts` | 17 | `hexToRgb` static method copy #3 |
| R-21-002 | `src/utils/ColorUtils.ts` | 51 | `hexToRgb` app-layer copy #4 |
| R-21-003 | `src/services/ble/InterrogatorService.ts` | 25 | `HW_CACHE_KEY` private — `toUpperCase()` vs canonical no-case |
| R-21-003 | `src/constants/storageKeys.ts` | 55 | `getHardwareConfigKey` — no `.toUpperCase()` normalization |
| R-21-004 | `src/hooks/useFavorites.ts` | 138 | `STORAGE_QUICK_PRESETS` writer #1 |
| R-21-004 | `src/components/docked/QuickPresetModal.tsx` | 76,90 | `STORAGE_QUICK_PRESETS` writer #2 |
| R-21-008 | `src/protocols/SymphonyEngine.ts` | 21 | `lerpRGBMusic` — identical to `lerpRGB` |
| R-21-008 | `src/protocols/shared/spatialMath.ts` | 21 | `lerpRGB` — canonical |

- Raw audit: `artifacts/deepdive_raw/R-21_findings.json`
- KB: capture required (no existing ColorUtils / storageKeys KB entry)

---

## Findings Detail

### CRITICAL — MAC-Case Split-Brain Cache Key (R-21-003)
**Files:** `src/services/ble/InterrogatorService.ts:25`, `src/constants/storageKeys.ts:55`

InterrogatorService defines a private key generator:
```typescript
const HW_CACHE_KEY = (mac: string) => `@sk8_hw_${mac.toUpperCase()}`;
```
The canonical key in `storageKeys.ts` is:
```typescript
export const getHardwareConfigKey = (mac: string) => `@sk8_hw_${mac}`;
```
`HardwareSetupWizardScreen.tsx` (line 258) writes via the canonical key with a mixed-case BLE MAC.
InterrogatorService reads via its private key with `.toUpperCase()`. If a BLE device delivers a
lowercase MAC, the wizard writes `@sk8_hw_aa:bb:cc:dd:ee:ff` while InterrogatorService reads
`@sk8_hw_AA:BB:CC:DD:EE:FF` — a permanently missed cache entry causing silent re-interrogation on
every connect.

**Fix:** Delete `HW_CACHE_KEY` from InterrogatorService. Import `getHardwareConfigKey` from
`src/constants/storageKeys.ts`. Add `.toUpperCase()` normalization inside `getHardwareConfigKey`
itself so ALL callers normalize consistently.

> [!CAUTION]
> This is a silent data loss bug. Existing users whose MACs were written lowercase will never hit
> the hardware config cache until their storage entry is re-written by a new wizard run.

---

### HIGH — hexToRgb Four-Way Duplication (R-21-001, R-21-002)
**Files:**
- `src/protocols/SymphonyEngine.ts:15` — local export
- `src/protocols/shared/engineUtils.ts:15` — canonical protocol copy (JSDoc: "Extracted to prevent circular imports")
- `src/protocols/PositionalMathBuffer.ts:17` — static class method
- `src/utils/ColorUtils.ts:51` — app-layer export (used in 8+ files)

The app-layer and protocol-layer return types are structurally identical (`{ r, g, b }`). The split
exists to prevent circular imports. The safe consolidation is:
- Keep `engineUtils.ts` as the protocol-layer canonical source.
- Keep `ColorUtils.ts` as the app-layer canonical source (8+ callers depend on this path).
- Delete the two non-canonical copies from `SymphonyEngine.ts` and `PositionalMathBuffer.ts`, replacing
  them with imports from `engineUtils.ts`.
- This preserves the two-layer split deliberately — R-21-002 carries MEDIUM false-positive risk per
  the audit because the split may be intentional for circular-import prevention.

---

### MEDIUM — Quick-Preset Dual Writer (R-21-004)
**Files:** `src/hooks/useFavorites.ts:138`, `src/components/docked/QuickPresetModal.tsx:76,90`

Both files call `AsyncStorage.setItem(STORAGE_QUICK_PRESETS, ...)` independently. The modal receives
`quickPresets` as a prop from the hook — if the prop is stale when the modal saves, the modal's write
clobbers the hook's in-memory state.

**Fix:** Remove `AsyncStorage.setItem` calls from `QuickPresetModal.tsx` at lines 76 and 90. The modal
must call a persist callback provided by `useFavorites` (e.g., `onPresetsChanged`) so all storage writes
flow through the hook.

---

### LOW — lerpRGBMusic / lerpRGB Duplication (R-21-008)
**Files:** `src/protocols/SymphonyEngine.ts:21`, `src/protocols/shared/spatialMath.ts:21`

`lerpRGBMusic` is functionally identical to `lerpRGB`. Only the name differs.

**Fix:** Import `lerpRGB` from `./shared/spatialMath` in `SymphonyEngine.ts`. If the music-mode naming
is semantically valuable, add `export { lerpRGB as lerpRGBMusic }` in `spatialMath.ts` instead of a
separate implementation.

---

## Implementation Steps (Priority Order)

### Step 1 — Fix MAC-Case Key in storageKeys.ts [HIGHEST RISK — silent data loss]
**File:** `src/constants/storageKeys.ts:55`
1. Read `src/constants/storageKeys.ts` lines 50–60.
2. Edit `getHardwareConfigKey` to apply `.toUpperCase()` normalization inside the function body:
   ```typescript
   export const getHardwareConfigKey = (mac: string) =>
     `@sk8_hw_${mac.toUpperCase()}`;
   ```
3. `Verify:` run `git diff HEAD src/constants/storageKeys.ts` — confirm only line 55 changes.

### Step 2 — Delete HW_CACHE_KEY from InterrogatorService and import canonical key
**File:** `src/services/ble/InterrogatorService.ts:25`
1. Read `src/services/ble/InterrogatorService.ts` lines 1–50.
2. Delete the private `const HW_CACHE_KEY = ...` declaration at line 25.
3. Add import: `import { getHardwareConfigKey } from '../../constants/storageKeys';`
4. Replace all internal uses of `HW_CACHE_KEY(mac)` with `getHardwareConfigKey(mac)`.
5. `Verify:` run `git diff HEAD src/services/ble/InterrogatorService.ts` — confirm no
   `HW_CACHE_KEY` references remain.
6. `Verify:` run `npm run verify` — expect 0 TSC errors.

### Step 3 — Delete lerpRGBMusic from SymphonyEngine, import lerpRGB
**File:** `src/protocols/SymphonyEngine.ts:21`
1. Read `src/protocols/SymphonyEngine.ts` lines 1–30.
2. Delete the `lerpRGBMusic` function body at line 21.
3. Add import at top: `import { lerpRGB } from './shared/spatialMath';`
4. Replace all internal `lerpRGBMusic(` calls with `lerpRGB(`.
5. `Verify:` `grep -rn "lerpRGBMusic" src/` — should return 0 results outside of spatialMath.ts
   if the re-export approach is used, or 0 results if direct rename is used.
6. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 4 — Delete hexToRgb from SymphonyEngine, import from engineUtils
**File:** `src/protocols/SymphonyEngine.ts:15`
1. Read `src/protocols/SymphonyEngine.ts` lines 1–20 (verify Step 3 already done).
2. Delete the `hexToRgb` export at line 15.
3. Add import: `import { hexToRgb } from './shared/engineUtils';`
4. Confirm no internal callers reference `hexToRgb` from the local definition.
5. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 5 — Delete hexToRgb static method from PositionalMathBuffer, import from engineUtils
**File:** `src/protocols/PositionalMathBuffer.ts:17`
1. Read `src/protocols/PositionalMathBuffer.ts` lines 1–30.
2. Delete the static `hexToRgb` method at line 17.
3. Add import at top: `import { hexToRgb } from './shared/engineUtils';`
4. Replace all `PositionalMathBuffer.hexToRgb(` call sites (grep required: `grep -rn "PositionalMathBuffer.hexToRgb" src/`).
5. `Verify:` `npm run verify` — expect 0 TSC errors.

### Step 6 — Remove AsyncStorage writes from QuickPresetModal
**File:** `src/components/docked/QuickPresetModal.tsx:76,90`
1. Read `src/components/docked/QuickPresetModal.tsx` lines 65–100.
2. Identify the `onPresetsChanged` or equivalent callback prop from `useFavorites`. If no callback prop exists yet:
   a. Add `onPresetsChanged: (presets: QuickPreset[]) => void` to the modal's props type.
   b. Update the modal's callers to pass the hook's persist function.
3. Replace `AsyncStorage.setItem(STORAGE_QUICK_PRESETS, ...)` at lines 76 and 90 with `props.onPresetsChanged(newArr)`.
4. `Verify:` `grep -n "AsyncStorage.setItem.*QUICK_PRESETS" src/components/docked/QuickPresetModal.tsx` — should return 0 results.
5. `Verify:` `npm run verify` — expect 0 TSC errors.

---

## Verification

- `npm run verify` — TSC must pass after every step.
- Post-completion grep sweep:
  ```powershell
  # Confirm no stray hexToRgb definitions remain outside canonical files
  grep -rn "function hexToRgb\|const hexToRgb\|hexToRgb = " src/ | grep -v "engineUtils\|ColorUtils"
  # Confirm no private HW_CACHE_KEY remains
  grep -rn "HW_CACHE_KEY" src/
  # Confirm no lerpRGBMusic definition remains
  grep -rn "function lerpRGBMusic\|lerpRGBMusic =" src/
  # Confirm no AsyncStorage.setItem for QUICK_PRESETS in modal
  grep -n "AsyncStorage.setItem.*QUICK_PRESETS" src/components/docked/QuickPresetModal.tsx
  ```
- Manual smoke test: connect a device → confirm hardware config loads from cache on second connect
  (AppLogger should NOT show a `[InterrogatorService] Probing device` entry for a previously-seen MAC).

---

## Out of Scope
- `src/utils/ColorUtils.ts` — its `hexToRgb` is the app-layer canonical source and MUST NOT be deleted.
- `src/protocols/shared/engineUtils.ts` — canonical protocol source; no changes.
- `src/protocols/shared/spatialMath.ts` — canonical lerp source; no changes beyond optional re-export.
- `src/hooks/useFavorites.ts` — the persist logic inside the hook is correct; only the modal's redundant
  writes are being removed.
- The `STORAGE_AUTO_PAUSE_ENABLED` split-brain (R-21-005) — out of scope for this task; route to
  TRIAGE for a separate plan.
- The `crew_sessions` dual-query split-brain (R-21-010) — out of scope; requires LocationService
  architecture discussion.

---

## Decision Log
- **Why keep the two-layer hexToRgb split (ColorUtils + engineUtils)?**
  R-21-002 carries MEDIUM false-positive risk. The audit confirms the split may be intentional to
  prevent circular imports between the protocols/ subtree and app-layer utils/. A single source
  would require both layers to import from a location accessible to both — this risks introducing
  a circular import chain. Consolidation deferred unless a shared `/src/primitives/` module is
  created in a future architecture task.
- **Why normalize in storageKeys.ts rather than at call sites?**
  Normalizing inside the canonical key generator ensures ALL future callers inherit correct behavior
  without requiring each to remember to call `.toUpperCase()`. This is the safest defense against
  future split-brain regressions.

---

## Kanban Task Tags
- `[Status: ✅ READY]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Utils]`
- `[Risk: M-RISK]`
- `[Size: Meal]`
- `[Cognitive Load: MEDIUM]`
- `[WAVE:2]`
- `[BATCH: split-brain-dedup]`
