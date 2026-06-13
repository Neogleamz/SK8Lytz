# Plan: `refactor-crew-modal`

### Design Decisions & Rationale

`CrewModal.tsx` is the highest-risk technical debt file in the codebase at 140KB and 14 `useEffect` hooks. The refactor extracts state and side-effects into two isolated custom hooks while keeping the JSX render tree largely untouched to minimize regression risk. We do NOT change business logic — only modularize it.

---

## ⚠️ User Review Required

> [!CAUTION]
> This is the highest-risk refactor in the codebase. `CrewModal.tsx` is 140KB. We must execute this with extreme Surgical Strike discipline — no Boy Scout cleanups, no logic changes, strict extraction only. Recommend doing this on its own branch with a full manual regression test before merging.

---

## Proposed Changes

### [NEW] `src/hooks/useCrewHub.ts`

- Extracts all session discovery, proximity filtering, and public/private session logic from `CrewModal.tsx`.
- Manages: `nearbySessions`, `isDiscovering`, `radiusMi`, `refresh()`, `cleanupExpiredSessions()`.
- Supabase subscriptions for real-time session updates.

### [NEW] `src/hooks/useCrewSession.ts`

- Extracts all "active session" state: `activeSession`, `joinSession()`, `leaveSession()`, `broadcastPosition()`, `memberList`.
- Houses the location broadcasting interval.
- Houses the `onDeviceDisconnected` cascade that auto-ends session.

### [MODIFY] `src/components/CrewModal.tsx`

- Replace all extracted `useState`/`useEffect` blocks with calls to `useCrewHub()` and `useCrewSession()`.
- Target: reduce from 14 `useEffect` hooks down to ≤4 (lifecycle only).
- Target: reduce file size from 140KB to ≤60KB.

---

## Open Questions

- **Q:** Are there any `useCallback` + `useEffect` cross-dependencies between the session and hub logic that require careful sequencing during extraction?
- **Q:** Do we want to add basic unit tests for `useCrewHub` and `useCrewSession` before merging?

## Verification Plan

1. Complete a full manual walkthrough of Crew Hub: create session, join session, leave session, proximity filter.
2. Verify real-time member updates fire correctly.
3. Verify session cleanup (24hr stale logic) still runs on discovery refresh.
