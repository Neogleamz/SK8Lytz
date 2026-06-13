# PLAN: Retire RbmSimulator & Audit PatternEngine Wiring

**Slug**: `refactor/retire-rbm-simulator`  
**Created**: 2026-04-22 | **Upgraded**: 2026-04-23 (AI-First Mandate)  
**Status**: 🟡 In Progress  
**Risk**: M-RISK  
**Batch**: P0 — Must complete before EPIC-004 Phase 1 expansion  
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem Summary

Two zombie files exist in `src/utils/` that are holdovers from the pre-PatternEngine era:
- `RbmSimulator.ts` (17KB) — legacy math simulator, fully superseded by `PatternEngine.ts`
- `RbmDictionary.ts` (17KB) — legacy pattern lookup table, superseded by `CustomEffects.ts` + `RbmPatterns.ts`

These are dead weight that confuse new development and violate the Single Source of Truth principle. Additionally, `PatternEngine.ts` needs a full wiring audit before Phase 1 expansion to ensure all 28 patterns are reaching hardware correctly.

---

## Target Files

| File | Absolute Path | Change |
|------|--------------|--------|
| `RbmSimulator.ts` | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\refactor\retire-rbm-simulator\src\utils\RbmSimulator.ts` | **DELETE via `git rm`** |
| `RbmDictionary.ts` | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\refactor\retire-rbm-simulator\src\utils\RbmDictionary.ts` | **DELETE via `git rm`** |
| `useControllerDispatch.ts` | `...\src\hooks\useControllerDispatch.ts` | **AUDIT ONLY** — read `numLEDs` source on line ~84. Do NOT edit unless a hardcoded bug is confirmed. |

---

## Collateral Damage Locks

> ⛔ The Developer MUST NOT touch any of these files. They are adjacent but out-of-scope.

| File | Why It's Locked |
|------|----------------|
| `PatternEngine.ts` | Core engine — read-only for this task |
| `CustomEffects.ts` | Pattern registry — read-only for this task |
| `RbmPatterns.ts` | Still used by `ProgramsPanel` for labels — do NOT delete |
| `usePrograms.ts` | Retirement planned for BATCH:RETIRE, not now |
| `ProgramsPanel.tsx` | Retirement planned for BATCH:RETIRE, not now |

---

## Execution Checklist

### Step 1 — Confirm Zero Live Usages
Run from the **worktree root**:
```powershell
Get-ChildItem -Path src -Recurse -Include *.ts,*.tsx |
  Select-String "RbmSimulator|RbmDictionary" |
  ForEach-Object { "$($_.Filename):$($_.LineNumber): $($_.Line.Trim())" }
```
**Expected**: Only self-references within the two target files themselves.  
**If external import found**: HALT. Do not delete. Notify user immediately.

### Step 2 — Delete the Files
```powershell
git rm src/utils/RbmSimulator.ts src/utils/RbmDictionary.ts
```

### Step 3 — TypeScript Check
Run from the **master fortress** (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\`):
```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```
**Expected**: Zero new errors introduced.  
**If errors found**: Read each error, identify the broken import, patch only that specific import. Do not touch anything else.

### Step 4 — PatternEngine Wiring Audit
Verify `numLEDs` source in `useControllerDispatch.ts` at `buildPatternPayload` call site:
```typescript
// CONFIRM this is hwSettings.ledPoints — NOT hardcoded
const payload = buildPatternPayload(
  patternId,
  fgRaw,
  bgRaw,
  numLEDs,   // ← must trace back to hwSettings.ledPoints
  clampSpeed(currentSpeed ?? 50),
  1
);
```
Add a `__DEV__` log if needed to verify at runtime:
```typescript
if (__DEV__) console.log('[PatternEngine Audit] numLEDs:', numLEDs);
```

### Step 5 — Pattern Group Verification Table
Document results for each group (hardware test if device available, code trace if not):

| Group | Patterns | Status |
|-------|----------|--------|
| Group 1 (Static) | Solid, Split, Trisection, Quartered, Center Accent | ⬜ |
| Group 2 (Chase) | Single Dot, Reflected Dot, Comet, Meteor | ⬜ |
| Group 3 (Marquee) | Micro Ants, Theater, Dashed, Barber, Stripes | ⬜ |
| Group 4 (Waves) | Sine Pulse, Wave Pinch, Breathing Wave, Center-Out ×2 | ⬜ |
| Group 5 (Temporal) | Smooth Breath, Jump Flash, Strobe, Wipe ×2 | ⬜ |
| Group 6 (Rainbow) | Rainbow Flow, Rainbow Marquee, Rainbow Comet, Cyberpunk | ⬜ |

### Step 6 — Commit
```powershell
git add .
git commit -m "refactor(utils): retire RbmSimulator and RbmDictionary zombie files [BATCH:P0]"
```

---

## Rollback Strategy

If anything goes wrong after deletion:

```powershell
# Option A — Restore both files from git history
git checkout HEAD~1 -- src/utils/RbmSimulator.ts src/utils/RbmDictionary.ts

# Option B — Nuclear reset the entire worktree
git reset --hard HEAD
git clean -fd
```

The master branch is never touched until after the worktree merge, so master is always the safe fallback.

---

## Test Criteria (Definition of Done)

- [ ] `Select-String "RbmSimulator|RbmDictionary" src/` returns zero external import hits
- [ ] Both files deleted via `git rm` — confirmed absent from `git status`
- [ ] `npx tsc --noEmit` shows zero new errors after deletion
- [ ] `numLEDs` confirmed to trace back to `hwSettings.ledPoints`, not hardcoded
- [ ] All 6 pattern groups documented in the verification table above
- [ ] `RbmPatterns.ts` still present and untouched (used by ProgramsPanel)
- [ ] Worktree committed cleanly before merge
