# Implementation Plan
# feat/session-phase-badge-ui — Session Phase Badge UI Integration

**Wave:** 3B (parallel-safe with 3A and 3C — no shared files)
**Worktree:** `session-phase-badge-ui`
**Type:** 2 file modifications + 1 new component (already created in Wave 1)

## Source of Truth
- `src/components/session/SessionPhaseBadge.tsx` (Wave 1 output — must be merged)
- `src/components/dashboard/DashboardTelemetryHero.tsx` — add badge below TIME pill
- `src/components/dashboard/LiveTelemetryHUD.tsx` — add badge in pill bar rightmost slot
- AST: `DashboardTelemetryHero.tsx` imported_by `DashboardScreen.tsx` only
- AST: `LiveTelemetryHUD.tsx` imported_by `[]` (orphan — DockedController imports it transitively)

## Steps

### Step 1 — Read DashboardTelemetryHero.tsx in full before touching
- `view_file` the full file
- Locate the TIME pill render block
- Identify exact insertion point for `<SessionPhaseBadge />`
- Verify: File size < 30KB (S4 check)

### Step 2 — Add `sessionPhase` prop to DashboardTelemetryHero
- Add `sessionPhase: SessionPhase` to the component's props interface
- Import `SessionPhaseBadge` from `../session/SessionPhaseBadge`
- Import `SessionPhase` type from `../../services/session/SessionMachine.types`
- Insert `<SessionPhaseBadge sessionPhase={sessionPhase} />` below the TIME pill
- Source: `DashboardScreen.tsx:486` — `sessionPhase` already destructured from `useSession()`
- Source: `DashboardScreen.tsx:1060` — prop drill target confirmed
- Verify: `git diff HEAD` shows only badge import + prop addition

### Step 3 — Pass `sessionPhase` from DashboardScreen to DashboardTelemetryHero
- Read `DashboardScreen.tsx` around line 1060 to see current prop list
- Add `sessionPhase={sessionPhase}` to the `<DashboardTelemetryHero>` JSX
- Source: `DashboardScreen.tsx:486` — `const { sessionPhase } = useSession()` exists
- Verify: No other prop changes in DashboardScreen

### Step 4 — Read LiveTelemetryHUD.tsx in full before touching
- `view_file` the full file
- Locate rightmost slot or final View in the pill bar
- Identify exact insertion point for `<SessionPhaseBadge />`
- Verify: File size < 30KB (S4 check)

### Step 5 — Add `sessionPhase` prop to LiveTelemetryHUD
- Add `sessionPhase: SessionPhase` to props interface
- Import `SessionPhaseBadge`
- Insert `<SessionPhaseBadge sessionPhase={sessionPhase} />` in rightmost pill slot
- Source: `DockedController.tsx:1080–1084` — LiveTelemetryHUD current props confirmed
- Verify: `git diff HEAD` shows only badge import + prop addition

### Step 6 — Pass `sessionPhase` from DockedController to LiveTelemetryHUD
- DockedController is a S4 monolith (68KB) — surgical single-line prop addition ONLY
- Add `sessionPhase={sessionPhase}` to `<LiveTelemetryHUD>` at line 1080–1085
- `sessionPhase` must be received as prop by DockedController (check its props interface first)
- If not in DockedController props: add to props interface + wire from DashboardScreen
- Verify: `git diff HEAD DockedController.tsx` shows ONLY the `sessionPhase` prop line added

### Step 7 — Run verify
```powershell
npm run verify
```
- Verify: TSC 0 errors, Jest passes

## Out of Scope
- StreetPanel badge integration (Wave 3C — separate worktree)
- Notification button changes (handled in NotificationService.ts — Wave 1)
- Any changes to SessionMachine.ts, SessionContext.tsx (earlier waves)
