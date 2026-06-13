# Implementation Plan

## Task: fix/animated-loop-leak-sweep
## Cluster: TC-06
## Risk: [L-RISK] | Size: [Meal] | Layer: [UI] | [BATCH]
## Status: [⚪ READY]

---

## Problem Statement

6 components call `Animated.loop(...).start()` inside `useEffect` without returning a cleanup function. When the component unmounts (e.g., modal closes, card leaves list), the animation loop continues running in the background — consuming CPU and holding a reference to the now-unmounted component's state.

Uniform fix: `const loop = Animated.loop(...); loop.start(); return () => loop.stop();`

---

## Source of Truth

- **`artifacts/deepdive_raw/R-22_findings.json`** → MEM-001 through MEM-006

| ID | File | Line | Severity |
|---|---|---|---|
| MEM-001 | `src/components/AccountModal.tsx` | 84 | HIGH |
| MEM-002 | `src/components/CrewMemberDashboard.tsx` | 103 | HIGH |
| MEM-003 | `src/components/ProductVisualizer.tsx` | 77 | HIGH |
| MEM-004 | `src/components/patterns/PatternCard.tsx` | 37 | HIGH |
| MEM-005 | `src/components/MarqueeText.tsx` | 17 | MEDIUM |
| MEM-006 | `src/components/CommunityModal.tsx` | 33 | LOW |

---

## Proposed Changes

### Pattern (same for all 6 files)
```diff
- Animated.loop(
-   Animated.sequence([...])
- ).start();
+ const loop = Animated.loop(
+   Animated.sequence([...])
+ );
+ loop.start();
+ return () => loop.stop();
```

### Files to modify
1. `src/components/AccountModal.tsx:84`
2. `src/components/CrewMemberDashboard.tsx:103`
3. `src/components/ProductVisualizer.tsx:77`
4. `src/components/patterns/PatternCard.tsx:37`
5. `src/components/MarqueeText.tsx:17`
6. `src/components/CommunityModal.tsx:33` — This one already calls `anim.stopAnimation()` in cleanup. Replace with `loop.stop()` for correctness.

**Batching authorization**: All [Snack]-level, same pattern, same file types, same fix → Unified Batch Override applies → single worktree.

---

## Verification Plan

### Automated
- `npm run verify` — TSC clean (no API changes, just refactor)

### Manual
1. Open AccountModal → close it → confirm no lingering pulse animation visible in inspector
2. Open PatternCard with a selected card → navigate away → confirm no orphaned interval in Perf Monitor
3. Repeat for each component

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\animated-loop-leak-sweep\`
Branch: `fix/animated-loop-leak-sweep`
