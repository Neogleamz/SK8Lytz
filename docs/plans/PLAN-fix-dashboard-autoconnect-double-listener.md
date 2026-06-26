# Implementation Plan: fix/dashboard-autoconnect-double-listener

## Goal
Eliminate the duplicate `AppState` `change` listener so `retriggerAutoConnect` fires exactly once per foreground resume.

## Source Analysis
Reyes C2 wiring audit (docs/SESSION_LOG.md, 2026-06-25). Two listeners both invoke `retriggerAutoConnect` on `nextState === 'active'`:
- Source: `src/screens/DashboardScreen.tsx:405-421` (inline, added during teardown to bridge the ref)
- Source: `src/hooks/useDashboardAutoConnect.ts:480-486` (the hook's own listener, deps `[]`)

The hook is the correct owner. The screen's inline listener is the leftover. Only the 5s throttle inside `retriggerAutoConnect` masks the double-fire today.

## Files to Create/Modify
- `src/screens/DashboardScreen.tsx` — remove the inline `AppState.addEventListener('change', ...)` block at L405-421 and its subscription cleanup; remove the now-orphaned `retriggerAutoConnectRef` bridge plumbing IF it has no other consumer (verify by grep before removing).

## Implementation Steps
1. Read `src/hooks/useDashboardAutoConnect.ts:470-500` to confirm the hook registers + cleans up its own AppState listener and that `retriggerAutoConnect` is stable. Verify: hook listener calls `retriggerAutoConnect()` on `active` and removes on unmount.
2. Read `src/screens/DashboardScreen.tsx:395-425` (the inline listener + ref bridge) in full.
3. Grep `retriggerAutoConnectRef` across `src/screens/DashboardScreen.tsx`. If used ONLY by the inline listener, remove the ref + its assignment effect too. If used elsewhere, leave the ref, remove only the listener.
4. Delete the inline AppState listener `useEffect` (L405-421) including its `subscription.remove()` cleanup.
5. `git diff HEAD src/screens/DashboardScreen.tsx` — confirm only the listener block (and dead ref plumbing, if applicable) was removed; no other logic touched.
6. Verify: `npm run verify` passes clean (TSC + Jest + guards).

## Out of Scope
- `useDashboardAutoConnect.ts` internals (the throttle, the connect logic) — HARD boundary. Do not modify the hook.
- Any other AppState usage in DashboardScreen unrelated to autoconnect.
- The FlatList renderItem fix (separate task fix/dashboard-flatlist-rerender).
