# Implementation Plan
# fix/session-bug-fixes — Session Bug Fixes (StreetPanel + AccountTabStats + Wear OS)

**Wave:** 3C (parallel-safe with 3A and 3B — no shared files)
**Worktree:** `session-bug-fixes`
**Type:** 3 file modifications across 3 isolated domains

## Source of Truth
- `src/components/docked/StreetPanel.tsx:80–81` — `crewService.sessionTelemetry` calls to replace
- `src/components/docked/StreetPanel.tsx` — REC dot location for badge integration
- `src/components/account/AccountTabStats.tsx:49` — `history?.length` to replace
- `android/sk8lytzWear/` — Kotlin file with hardcoded `0.0` distance
- `DockedController.tsx:1249–1258` — confirms `sessionPeakSpeed` and `sessionDistanceMiles` already drilled to StreetPanel
- `useGlobalTelemetry.ts:353` — confirms `sessionPeakSpeed` exists in telemetry return value

## Steps

### Step 1 — Read StreetPanel.tsx in full before touching
- `view_file` the entire file
- Locate lines 80–81 (`crewService.sessionTelemetry.topSpeedMph`, `crewService.sessionTelemetry.distanceMiles`)
- Locate REC dot render block — insertion point for `<SessionPhaseBadge />`
- Locate props interface — confirm `sessionPeakSpeed` and `sessionDistanceMiles` are in props
- Verify: Props confirmed in `DockedController.tsx:1249–1258` before editing

### Step 2 — Fix StreetPanel data source (Bug: dual source-of-truth)
- Line ~80: Replace `crewService.sessionTelemetry.topSpeedMph` with `sessionPeakSpeed` (prop)
- Line ~81: Replace `crewService.sessionTelemetry.distanceMiles` with `sessionDistanceMiles` (prop)
- Source: `DockedController.tsx:1249–1258` — these props are already drilled in
- Source: `useGlobalTelemetry.ts:353` — `sessionPeakSpeed` is in telemetry
- Verify: `git diff HEAD` shows ONLY these 2 line replacements in StreetPanel

### Step 3 — Add SessionPhaseBadge to StreetPanel REC dot area
- Import `SessionPhaseBadge` from `../session/SessionPhaseBadge`
- Import `SessionPhase` from `../../services/session/SessionMachine.types`
- Add `sessionPhase: SessionPhase` to StreetPanel props interface
- Replace or augment existing REC dot with `<SessionPhaseBadge sessionPhase={sessionPhase} />`
- Source: `DockedController.tsx:1254` — `sessionActive` prop already passed; add `sessionPhase` alongside
- Verify: `git diff HEAD` shows ONLY badge import + prop + single JSX insertion

### Step 4 — Pass `sessionPhase` from DockedController to StreetPanel
- Read `DockedController.tsx:1244–1260` surgical view
- Add `sessionPhase={sessionPhase}` to `<StreetPanel>` at line ~1254
- DockedController receives `sessionPhase` as prop from DashboardScreen (Wave 3B wired this)
- Verify: `git diff HEAD DockedController.tsx` shows ONLY this single prop addition

### Step 5 — Read AccountTabStats.tsx before touching
- `view_file` lines 40–60 — locate the `history?.length` expression
- Verify: `lifetimeStats?.totalSessions` is available in the component's data

### Step 6 — Fix AccountTabStats sessions counter (Bug 8)
- Line 49: Replace `history?.length` with `lifetimeStats?.totalSessions ?? '—'`
- Source: `AccountTabStats.tsx:49`
- Verify: `git diff HEAD` shows ONLY this 1-line change

### Step 7 — Find and fix Wear OS distance bug
- Read `android/sk8lytzWear/` to find the Kotlin file displaying `0.0 mi`
- Locate the hardcoded `0.0` distance value in the stop confirmation screen
- Add `var lastKnownDistance: Float = 0f` updated on WatchBridge message receipt
- Replace hardcoded `0.0` with `lastKnownDistance` formatted string
- Source: Field name confirmed by Wave 0 spike result (check SESSION_LOG for [DECISION] entry)
- Verify: `git diff HEAD` shows ONLY the Wear OS file changes

### Step 8 — Run verify
```powershell
npm run verify
```
- Verify: TSC 0 errors, Jest passes

## Out of Scope
- DashboardTelemetryHero badge (Wave 3B — separate worktree)
- LiveTelemetryHUD badge (Wave 3B — separate worktree)
- SessionContext changes (Wave 2 — already merged)
