# Implementation Plan
## fix/music-mode-dep-array — Fix Stale Closure in `useMusicMode` useEffect Dep Array

**Severity:** MEDIUM  
**Source:** Defect Audit 2026-06-24 — F-007  
**Wave:** 2 (blocked by Wave 1 — imports useProtocolDispatch which is modified in F-003)

---

## Context

`useMusicMode.ts` lines 110–116: the `useEffect` that re-sends the 0x73 music config on
parameter change has this dep array:
```typescript
}, [musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);
```

It calls `handleMusicChange`, which is a `useCallback` with these deps (line 107):
```typescript
[dispatch, musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle]
```

The dep array omits `handleMusicChange`. This means:
- If `micSensitivity` or `brightness` change (without any of the listed deps also changing),
  `handleMusicChange` is recreated with the new values
- But the `useEffect` holds a stale reference to the old `handleMusicChange`
- The 0x73 config sent to hardware carries the old `micSensitivity` / `brightness` values

**Practical impact:** MusicPanel directly calls `handleMusicChange` on slider interaction
(confirmed in MusicPanel.tsx:150–172), so the stale closure only affects programmatic
brightness/sensitivity changes (e.g., session restore, parent state update). Still a correctness bug.

**Prerequisite:** Wave 1 must be merged first (specifically F-003 modifies useProtocolDispatch,
which useMusicMode imports). No API change in useProtocolDispatch affects this fix.

---

## Files to Create / Modify

| File | Lines | Change |
|------|-------|--------|
| `src/hooks/useMusicMode.ts` | 116 | Add `handleMusicChange` to dep array |

---

## Step-by-Step Execution

**1 — Confirm Wave 1 merged (S9 gate)**
```
git log --oneline master | head -10
```
Verify: A commit for `fix/protocol-dispatch-mtu-guard` (or the Wave 1 batch) appears in master.
If not → HALT. Do not proceed until Wave 1 is merged.

**2 — Look before leap: read the target useEffect**
```
Read: src/hooks/useMusicMode.ts, lines 107–120
```
Confirm: `handleMusicChange` useCallback dep array at line 107.
Confirm: The re-send useEffect dep array at line 116 missing `handleMusicChange`.
Confirm: The `activeMode` guard at line 111 — `if (activeMode !== 'MUSIC') return;`

**3 — Surgical edit: add `handleMusicChange` to dep array**

Change line 116:
```typescript
  }, [musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);
```
To:
```typescript
  }, [handleMusicChange, musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);
```

Note: `micSensitivity` and `brightness` do NOT need to be added directly — they are
already in `handleMusicChange`'s dep array (line 107), so adding `handleMusicChange` is
sufficient to capture the complete transitive dependency chain.

Note: `activeMode` is used as a guard condition inside the effect but is NOT a dep
that should trigger a hardware write — this is intentional (the mode-exit effect at line 147
handles mode transitions separately). Do NOT add `activeMode` to this dep array.

**4 — Post-edit diff**
```
git diff HEAD src/hooks/useMusicMode.ts
```
Verify: Only line 116 changed (one dependency added).

**5 — Verify**
```
npm run verify
```
Expected: TSC ✅ Jest ✅ (no behavioral test change for this dep — effect now correctly
re-fires when programmatic sensitivity/brightness changes arrive)

---

## Out of Scope

- Adding `activeMode` to the dep array (intentionally omitted — see note in step 3)
- Modifying `handleMusicChange` itself
- Modifying the mode-exit effect (lines 147–166)
- Any MusicPanel.tsx changes
- Any changes to useProtocolDispatch.ts (in Wave 1)
