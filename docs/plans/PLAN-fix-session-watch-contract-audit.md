# Implementation Plan: fix-session-watch-contract-audit

## Problem

**BUG-S7 LOW** — The TypeScript WatchBridge at [modules/sk8lytz-watch-bridge/src/index.ts L9](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/modules/sk8lytz-watch-bridge/src/index.ts#L9) defines a 4-state contract:

```typescript
status: 'ACTIVE' | 'STOPPED' | 'PAUSED' | 'SUMMARY';
```

Both native companions need to handle **all four states** correctly. This plan audits the current state of each companion and identifies any gaps.

## Audit Results

### watchOS — `WatchConnectivityManager.swift`

**File**: [targets/watch/WatchConnectivityManager.swift L81–117](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift#L81-L117)

| Status | Handled? | Behavior |
|--------|----------|----------|
| `ACTIVE` | ✅ L84–90 | Sets `isSessionActive = true`, `isPaused = false`, dismisses summary |
| `PAUSED` | ✅ L91–93 | Sets `isSessionActive = true`, `isPaused = true` |
| `SUMMARY` | ✅ L94–109 | Parses metrics, shows summary card, 10s auto-dismiss timer |
| `STOPPED` | ✅ L110–117 | Falls into `default` case — clears all state |

**watchOS ContentView.swift**: [targets/watch/ContentView.swift L15–21](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/ContentView.swift#L15-L21) renders 3 views:
- `showingSummary` → `summaryView` ✅
- `isSessionActive` + `isPaused` → `activeSessionView` with "⏸ PAUSED" label (L44–47) ✅
- Default → `idleView` ✅

**Verdict: watchOS is FULLY COMPLIANT.** All 4 states are handled.

### Wear OS — `WearableCommunicationService.kt`

**File**: [android/sk8lytzWear/.../WearableCommunicationService.kt L125–130](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/services/WearableCommunicationService.kt#L125-L130)

| Status | Handled? | Behavior |
|--------|----------|----------|
| `ACTIVE` | ✅ L126 | Maps to `SessionState.ACTIVE` |
| `PAUSED` | ✅ L127 | Maps to `SessionState.PAUSED` |
| `SUMMARY` | ✅ L128 | Maps to `SessionState.SUMMARY`, caches metrics at L136–142 |
| `STOPPED` | ✅ L129 | Falls into `else` → maps to `SessionState.IDLE` |

**SessionState.kt**: [android/sk8lytzWear/.../SessionState.kt L12–24](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/kotlin/com/neogleamz/sk8lytzwear/presentation/SessionState.kt#L12-L24) defines all 4 states: `IDLE`, `ACTIVE`, `PAUSED`, `SUMMARY` ✅

**Verdict: Wear OS is FULLY COMPLIANT.** All 4 states are handled.

## Root Cause

This bug was logged as a **risk** during the audit, but upon inspection both native companions are already fully compliant with the 4-state contract. The WearOS `SessionState.kt` enum was updated to include `PAUSED` and `SUMMARY` (lines 19–23), and the parsing logic in `WearableCommunicationService.kt` handles all four states.

## Proposed Fix

**No code changes required.** Both native companions correctly handle all 4 states.

However, to prevent future contract drift, add a documentation comment to the TypeScript WatchBridge type:

### Step 1: Add contract documentation to WatchBridge types (L7–9)

Replace:
```typescript
/** The session state payload pushed from phone → watch. */
export interface WatchSessionState {
  /** 'ACTIVE', 'PAUSED', 'SUMMARY' (post-session card, 10s), or 'STOPPED' */
  status: 'ACTIVE' | 'STOPPED' | 'PAUSED' | 'SUMMARY';
```

With:
```typescript
/**
 * The session state payload pushed from phone → watch.
 *
 * CONTRACT: Every status value MUST be handled by BOTH native companions:
 *   - watchOS: WatchConnectivityManager.swift → handlePayload()
 *   - Wear OS: WearableCommunicationService.kt → onDataChanged()
 * Adding a new status here requires updating both companions.
 * Last audit: 2026-06-06 — all 4 states verified in both platforms.
 */
export interface WatchSessionState {
  /** 'ACTIVE', 'PAUSED', 'SUMMARY' (post-session card, 10s), or 'STOPPED' */
  status: 'ACTIVE' | 'STOPPED' | 'PAUSED' | 'SUMMARY';
```

### Step 2 (Optional): Add ENDING to the contract if BUG-S6 is applied

If PLAN-fix-session-idle-race-summary adds an `ENDING` phase to the React state, note that `ENDING` is **phone-internal only** — it is never pushed to the watch. The watch receives `SUMMARY` then `STOPPED`. No companion changes needed for `ENDING`.

## Files Modified

| File | Change |
|------|--------|
| `modules/sk8lytz-watch-bridge/src/index.ts` | Add contract documentation comment to `WatchSessionState` interface |

## Verification

1. Grep all native companion files for the status strings:
   ```powershell
   Select-String -Pattern '"ACTIVE"|"PAUSED"|"SUMMARY"|"STOPPED"' -Path "targets\watch\*.swift","android\sk8lytzWear\**\*.kt" -Recurse
   ```
2. Verify all 4 appear in both `WatchConnectivityManager.swift` and `WearableCommunicationService.kt`
3. Run the watchOS and WearOS companion builds to confirm no compile errors

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| Future status added without updating companions | Low | JSDoc contract comment now warns developers to update both companions |
| `ENDING` state accidentally sent to watch | None | `ENDING` is never passed to `WatchBridge.syncSessionState()` — only `SUMMARY` and `STOPPED` are sent during teardown |

**Rollback**: `git checkout -- modules/sk8lytz-watch-bridge/src/index.ts`
