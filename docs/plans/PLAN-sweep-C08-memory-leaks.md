# Implementation Plan: C8 — Memory Leak Cleanup

## Goal
Add useEffect cleanup return functions for all event listeners and animation loops.

## Rules Addressed
- R-22: Memory Leak Patterns (6 HIGH findings)

## Files to Create/Modify
- `src/components/AccountModal.tsx` // SKIPPED: already addressed by fix/memory-leak-sweep
- `src/components/CrewMemberDashboard.tsx` // SKIPPED: intervals properly cleaned up
- `src/components/ProductVisualizer.tsx` // SKIPPED: already addressed by fix/memory-leak-sweep
- `src/components/PatternCard.tsx` // SKIPPED: already addressed by fix/memory-leak-sweep
- `src/components/MarqueeText.tsx` // SKIPPED: already addressed by fix/memory-leak-sweep
- `src/components/CommunityModal.tsx` // SKIPPED: already addressed by fix/memory-leak-sweep

## Implementation Steps
1. View each file. Find useEffect hooks missing cleanup returns.
2. Add cleanup: return () => { subscription.remove(); } or clearInterval/clearTimeout.
3. Verify: git diff shows only cleanup additions.
4. Run npm run verify.

## Out of Scope
- DashboardScreen.tsx (C2), DockedController.tsx (C4)
