# Implementation Plan — `fix/error-handling-standardization`

## Goal
Add `instanceof Error` unwrapping to all ~190 catch blocks that pass raw `e: unknown` directly to AppLogger, replacing `[object Object]` telemetry with readable error messages.

## Source of Truth
- `artifacts/deepdive_raw/R-06_findings.json` — 2130 raw entries (~190 unique after dedup)
- `artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json` — identity-layer instances
- `artifacts/system_audit_report.md` §R-06

## Out of Scope
- Changing AppLogger internals
- Removing catch blocks
- Changing error recovery logic

## Fix Pattern
```typescript
// BEFORE (bad)
} catch (e: unknown) {
  AppLogger.warn('Failed', e);
}

// AFTER (correct)
} catch (e: unknown) {
  AppLogger.warn('Failed', e instanceof Error ? e.message : String(e));
}
```

---

## Step 1 — Baseline scan
- **Action:** `grep -rn "catch (e: unknown)" src/ | wc -l`
- **Verify:** Count matches ~190 expected instances.

## Step 2 — Fix BLE layer (highest risk)
Files: `useBLEAutoRecovery.ts` (3), `useBLEGattMutex.ts` (1), `useBLEHeartbeat.ts` (1), `useBLEInterrogator.ts` (3), `useBLEScanner.ts` (2)
- **Action:** View each file, apply surgical fix to each catch block
- **Verify:** `grep -n "AppLogger" src/hooks/ble/*.ts | grep -v "instanceof Error\|String(e)"` returns zero unguarded log calls.

## Step 3 — Fix context layer
Files: `AuthContext.tsx` (4), `SessionContext.tsx` (6)
- **Action:** Fix each catch block per the pattern
- **Verify:** All catch blocks in these files use `instanceof Error` guard.

## Step 4 — Fix hooks layer
Files: `useAccountOverview.ts` (8), `useDashboardAutoConnect.ts` (4), `useCrewSession.ts` (2), `useCuratedPicks.ts` (2), `useCrewProximityRadar.ts` (1), `useBLE.ts` (5)
- **Action:** Batch fix via view → replace
- **Verify:** Per-file grep for unguarded log calls returns zero.

## Step 5 — Fix admin components
Files: `UserManagementPanel.tsx` (7), `AdminPicksScheduler.tsx` (6), `HardwareBlacklistPanel.tsx` (3), `FeatureFlagsPanel.tsx` (4)
- **Action:** Batch fix
- **Verify:** Zero unguarded admin catch blocks.

## Step 6 — Fix crew UI layer
Files: `CrewLandingScreen.tsx` (5), `CrewDetailScreen.tsx` (4), `CrewJoinScreen.tsx` (2)
- **Action:** Batch fix
- **Verify:** Zero unguarded crew catch blocks.

## Step 7 — Final verification grep
- **Action:** `grep -rn "} catch" src/ | grep -v "instanceof Error\|String(e)\|// swallow\|__DEV__"`
- **Verify:** All remaining hits are intentional swallowed catches with a comment.

## Step 8 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors.
