# PLAN: Retire RbmSimulator & Audit PatternEngine Wiring

**Slug**: `refactor/retire-rbm-simulator`  
**Created**: 2026-04-22  
**Status**: 🔲 Not Started  
**Risk**: L-RISK (deletion only — no logic changes)  
**Must Complete Before**: EPIC-004 Phase 1 (Pattern Expansion)  
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem Summary

Two zombie files exist in `src/utils/` that are holdovers from the pre-PatternEngine era:
- `RbmSimulator.ts` (17KB) — legacy math simulator, fully superseded by `PatternEngine.ts`
- `RbmDictionary.ts` (17KB) — legacy pattern lookup table, superseded by `CustomEffects.ts` + `RbmPatterns.ts`

These are dead weight that confuse new development and violate the Single Source of Truth principle. Additionally, `PatternEngine.ts` needs a full wiring audit before Phase 1 expansion to ensure all 28 patterns are reaching hardware correctly.

---

## Step 1: Confirm Zero Live Usages

Before deleting, verify nothing imports these files:

```powershell
# Run from SK8Lytz root
Get-ChildItem -Path src -Recurse -Include *.ts,*.tsx | 
  Select-String "RbmSimulator|RbmDictionary" | 
  ForEach-Object { "$($_.Filename):$($_.LineNumber): $($_.Line.Trim())" }
```

**Expected result**: Only internal self-references (the files importing their own types). Any external import = STOP, investigate before deleting.

Note: `RbmSimulator.ts` imports PatternEngine internally (`import type { RGB } from '../protocols/PatternEngine'`) — this is expected and not a blocker.

---

## Step 2: Delete the Files

```
DELETE: src/utils/RbmSimulator.ts
DELETE: src/utils/RbmDictionary.ts
```

Use the file system, not git — `git rm` would be ideal:
```powershell
git rm src/utils/RbmSimulator.ts src/utils/RbmDictionary.ts
```

---

## Step 3: PatternEngine Wiring Audit

After deletion, run a TypeScript check to confirm nothing broke:
```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```

Then manually verify the end-to-end pattern pipeline for each group:

| Group | Patterns | Verify |
|-------|----------|--------|
| Group 1 (Static) | Solid, Split, Trisection, Quartered, Center Accent | Connect HALOZ, tap pattern, confirm hardware response |
| Group 2 (Chase) | Single Dot, Reflected Dot, Comet, Meteor | Confirm hardware scrolls |
| Group 3 (Marquee) | Micro Ants, Theater, Dashed, Barber, Stripes | Confirm hardware scrolls |
| Group 4 (Waves) | Sine Pulse, Wave Pinch, Breathing Wave, Center-Out × 2 | Confirm pattern visible |
| Group 5 (Temporal) | Smooth Breath, Jump Flash, Strobe, Wipe × 2 | Confirm 0x51 path fires |
| Group 6 (Rainbow) | Rainbow Flow, Rainbow Marquee, Rainbow Comet, Cyberpunk | Confirm color cycling |

For each group: tap a pattern in the UI, observe hardware, log result.

### Key Wiring Points to Check

1. `applyFixedPattern` in `useControllerDispatch.ts:58` — calls `buildPatternPayload`
2. `buildPatternPayload` in `PatternEngine.ts:302` — routes to `buildMultiColorPayload` or `buildCustomModePayload`
3. `writeToDevice(payload)` fires at `useControllerDispatch.ts:89`
4. FG/BG color changes in `UniversalSlidersFooter.tsx:246` re-fire `applyFixedPattern`

### Known Issue to Flag

Check if `numLEDs` passed to `buildPatternPayload` at line 84 is using the correct `hwSettings.ledPoints`:
```typescript
const payload = buildPatternPayload(
  patternId,
  fgRaw,
  bgRaw,
  numLEDs,   // ← confirm this is hwSettings.ledPoints, NOT hardcoded
  clampSpeed(currentSpeed ?? 50),
  1
);
```

Log the value in `__DEV__` mode during audit.

---

## Files To Touch

| File | Change |
|------|--------|
| `src/utils/RbmSimulator.ts` | **DELETE** |
| `src/utils/RbmDictionary.ts` | **DELETE** |
| `src/hooks/useControllerDispatch.ts` | Verify `numLEDs` source (read-only audit, no edit unless bug found) |

---

## Test Criteria

- [ ] `Get-ChildItem src | Select-String "RbmSimulator"` returns zero results
- [ ] `npx tsc --noEmit` shows zero new errors after deletion
- [ ] At least one pattern from each of the 6 groups verified working on hardware
- [ ] `numLEDs` confirmed to be `hwSettings.ledPoints` (not hardcoded)
- [ ] No regression in the `ProgramsPanel` (uses `RbmPatterns.ts` for labels — different file, not deleted)
