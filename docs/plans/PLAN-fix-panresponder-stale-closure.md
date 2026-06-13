# PLAN: fix/panresponder-stale-closure

## Goal
Fix the stale closure bug in `DockedController`'s `swipePanResponder` where `activeMode` is captured at creation time and never updated, causing mode swipe navigation to misfire.

## Problem
`swipePanResponder` is created once inside a `useRef` call (line 784). The `onPanResponderRelease` callback closes over `activeMode` at that moment. Because `useRef` doesn't cause re-creation, the `activeMode` value read by the handler is always the value from the initial render — typically `'HOME'` or whatever the persisted mode was at mount time.

## Impact
- Swipe Right from any mode always computes index from the stale initial mode, not the current one
- Mode navigation can jump to wrong panels or silently no-op
- This is intermittent and hard to repro because it only manifests after the user has changed modes at least once

## Root Cause
```ts
// line 784 — PanResponder is created ONCE
const swipePanResponder = React.useRef(
  PanResponder.create({
    onPanResponderRelease: (evt, gestureState) => {
      // activeMode here is STALE — always the value from first render
      const currentModeStr = activeMode === 'MULTI' as any ? 'MULTIMODE' : activeMode;
```

## Target File
`src/components/DockedController.tsx`

## Fix
Use a ref to always read the latest `activeMode` inside the PanResponder closure:
```ts
const activeModeRef = useRef(activeMode);
// NOTE: if perf/write-to-device-usecallback is merged first, this ref already exists — reuse it.
activeModeRef.current = activeMode; // update every render (free, no re-creation)

const swipePanResponder = React.useRef(
  PanResponder.create({
    onPanResponderRelease: (evt, gestureState) => {
      // Read from ref — always current
      const currentModeStr = activeModeRef.current === 'MULTI' as any ? 'MULTIMODE' : activeModeRef.current;
      ...
    }
  })
).current; // Note: use .current so the PanResponder instance is stable
```

## Execution Checklist
1. Check if `activeModeRef` already exists (it will if `perf/write-to-device-usecallback` was merged first)
2. If not: add `const activeModeRef = useRef(activeMode); activeModeRef.current = activeMode;` directly above the swipePanResponder ref
3. Replace all `activeMode` reads inside `onPanResponderRelease` with `activeModeRef.current`
4. Verify the `MODE_ORDER` array is also read correctly (it's a const — no issue)
5. Run TSC from master

## Prerequisite
`[BLOCKED BY: perf/write-to-device-usecallback]` — if that task creates `activeModeRef` first, this task reuses it. If executing standalone, create the ref independently.

## Rollback
Revert the 3 changed lines. Stale closure bug resumes — no regression beyond original behavior.

## Collateral Damage Locks
- DO NOT change the swipe distance threshold (30px horizontal, 50px release)
- DO NOT change MODE_ORDER
- ONLY fix the stale activeMode reference inside onPanResponderRelease
