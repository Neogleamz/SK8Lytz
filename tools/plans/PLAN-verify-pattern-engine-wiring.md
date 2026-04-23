# PLAN: Verify PatternEngine Wiring Audit

**Slug**: `refactor/verify-pattern-engine-wiring`
**Created**: 2026-04-23 (AI-First Mandate)
**Status**: 🟡 In Progress
**Risk**: L-RISK — read-only audit + JSDoc only
**Batch**: P0
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem Summary

Before EPIC-004 Phase 1A (new pattern expansion) begins, we must confirm that all 28 existing
PatternEngine patterns are correctly wired end-to-end: visualizer → hardware dispatch → correct
0x59/0x51 opcode. Any gaps or misrouted patterns discovered here become P1 blockers.

---

## Audit Findings (2026-04-23)

### PatternEngine Structure (`src/protocols/PatternEngine.ts` — 316 lines)

| Group | IDs | Motion Type | Visualizer | Hardware Path |
|-------|-----|-------------|------------|---------------|
| 1 — Static & Solid | 1–5 | Static (no scroll) | `generateArray` | `buildMultiColorPayload` → 0x59 transitionType=0x00 |
| 2 — Chases & Meteors | 6–9 | Scroll (RunningWater) | `generateArray` + `rotateArray` | 0x59 transitionType=0x03 |
| 3 — Marquees & Bands | 10–14 | Scroll | Same | Same |
| 4 — Math Waves | 15–19 | Scroll (center-out split for 18/19) | Same | Same |
| 5 — Temporal Full-Strip | 20–22 | Temporal (full strip FX) | `getVisualizerFrame` simulates visually | `buildCustomModePayload` → 0x51 |
| 5 — Wipe/Fill | 23–24 | Center-out scroll | `generateArray` + center split | 0x59 transitionType=0x03 |
| 6 — Generative Rainbows | 25–28 | Scroll | `generateArray` + `rotateArray` | 0x59 transitionType=0x03 |

### Dispatch Chain Verified

```
User selects pattern
  → applyFixedPattern() [useControllerDispatch.ts:58]
  → buildPatternPayload(patternId, fg, bg, numLEDs, speed) [PatternEngine.ts:302]
  → buildMultiColorPayload() → 0x59  (patterns 1–19, 23–28)
  → buildCustomModePayload() → 0x51  (patterns 20–22)
  → writeToDevice(payload)
```

### Visualizer Chain Verified

```
ProductVisualizer.tsx:436 → getVisualizerFrame(pid, fgRgb, bgRgb, numLEDs, animTick)
CustomEffectVisualizer.tsx:57 → getVisualizerFrame(effectId, fgRgb, bgRgb, points, tick)
```

### Issues Found

| ID | Severity | Finding |
|----|----------|---------|
| W1 | ⚠️ WARN | Patterns 23–24 (Wipe/Fill) return `null` from `getHardwarePixelArray` because `patternId >= 20 && patternId <= 22` check excludes them — but they are NOT in that range. Actually: 23/24 ARE correctly handled by `generateArray` since the guard is `>= 20 && <= 22`. ✅ False alarm — wired correctly. |
| W2 | ⚠️ WARN | `ProductVisualizer.tsx:343` — PROGRAMS mode calls `getVisualizerFrame` with a hardcoded `fgRgbProg = {r:255, g:0, b:0}` and `bgRgbProg = {r:0, g:0, b:0}`. This means PROGRAMS visualizer always shows red — user-selected colors are ignored for this branch. **NOT a blocker for P0 — log as P1 tech debt.** |
| W3 | 📝 NOTE | `default:` fallback in `generateArray` returns `Array(n).fill(fg)` — IDs > 28 degrade gracefully to solid. Safe. |
| W4 | 📝 NOTE | `getPatternTransitionType` returns only 0x00 or 0x03 — no per-pattern overrides for Groups 3/4 which might benefit from 0x01 (Gradual) for smoother visual. Low priority — leave for BATCH:P1. |

---

## Target Files

| File | Absolute Path | Change |
|------|--------------|--------|
| `PatternEngine.ts` | `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\...\src\protocols\PatternEngine.ts` | Add audit-verified JSDoc header block + W2/W4 TODO comments |
| `tools/plans/PLAN-verify-pattern-engine-wiring.md` | This file | Source of truth for audit results |

---

## Collateral Damage Locks

| File | Why Locked |
|------|-----------|
| `useControllerDispatch.ts` | dispatch logic is verified correct — no changes |
| `ProductVisualizer.tsx` | W2 is P1 tech debt — do NOT fix here |
| `CustomEffectVisualizer.tsx` | wired correctly — no changes |
| All other files | audit only |

---

## Execution Checklist

### Step 1 — Create Worktree
```powershell
git worktree add ../SK8Lytz-worktrees/refactor/verify-pattern-engine-wiring -b refactor/verify-pattern-engine-wiring
```

### Step 2 — Add audit-verified JSDoc to PatternEngine.ts
Add a `@wiring-audit` block to the top JSDoc comment documenting the verified 6-group structure,
transition types, and W2/W4 known gaps as `TODO[BATCH:P1]` comments.

### Step 3 — TSC (read only from master)
```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```

### Step 4 — Commit
```powershell
git add .
git commit -m "docs(protocols): add wiring audit results to PatternEngine JSDoc [BATCH:P0]"
```

---

## Rollback Strategy
All changes are additive comments. Zero runtime impact. `git reset --hard HEAD` is instant recovery.

---

## Test Criteria
- [x] All 28 pattern IDs accounted for in the 6-group table above
- [x] `buildPatternPayload` verified as master dispatcher for all 28 IDs
- [x] 0x51 path verified for IDs 20–22
- [x] 0x59 path verified for all remaining IDs
- [x] `getVisualizerFrame` verified as single entry point for both ProductVisualizer + CustomEffectVisualizer
- [ ] `npx tsc --noEmit` — zero new errors
- [x] W2 (PROGRAMS hardcoded color) logged as P1 tech debt
