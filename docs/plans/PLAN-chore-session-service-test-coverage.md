# Implementation Plan

## chore/session-service-test-coverage

### Goal
Add substantive unit tests for 3 untested session services identified in the post-merge audit: `SensorService.ts`, `HealthService.ts`, and `NotificationService.ts`. `SessionPhaseBadge.tsx` and `StreetPanel.tsx` consumer tests are deferred to a later sprint.

### Source of Truth
- `src/services/session/__tests__/AutoPauseService.test.ts` — reference pattern for `fromCallback` service tests with fake timers
- `src/services/session/__tests__/SessionBridge.test.ts` — reference pattern for simple unit assertions
- `src/services/session/SensorService.ts` — the service under test
- `src/services/session/HealthService.ts` — the service under test
- `src/services/session/NotificationService.ts` — the service under test

---

## Files to Create/Modify

### [NEW] `src/services/session/__tests__/SensorService.test.ts`
**Minimum 4 test cases:**
1. GPS speed tick updates `telemetryRef.current.gpsSpeed` via `sendBack({ type: 'UPDATE_TELEMETRY', ... })`
2. Accelerometer high-G tick sends `sendBack({ type: 'UPDATE_TELEMETRY', peakGForce: ... })`
3. Cleanup removes both subscriptions (location + accelerometer) — mock `watchPositionAsync` and `Accelerometer.addListener` returns
4. `sendBack` NOT called after cleanup (verify no lingering side effects)

Pattern: Use `jest.fn()` mocks for `expo-location` and `expo-sensors`. Inject minimal `SensorServiceInput`.

### [NEW] `src/services/session/__tests__/HealthService.test.ts`
**Minimum 3 test cases:**
1. `addWatchHealthListener` callback fires → `sendBack({ type: 'UPDATE_HEALTH', ... })`
2. 30s poll interval fires → `sendBack` called with health data
3. Cleanup: `unsubscribeHealth()` called + `clearInterval` fired

Pattern: `jest.useFakeTimers()`. Mock `WatchBridge.addWatchHealthListener` to capture the callback.

### [NEW] `src/services/session/__tests__/NotificationService.test.ts`
**Minimum 4 test cases:**
1. ACTIVE phase: notification displays PAUSE + END action buttons (no RESUME)
2. PAUSED phase: notification displays RESUME + END action buttons (no PAUSE)
3. ENDING phase: notification displays NO action buttons (only "Saving Session..." title)
4. Cleanup: `clearInterval` + `notifee.stopForegroundService()` called

Pattern: Mock `@notifee/react-native`. Capture `displayNotification` calls and assert `actions` array contents.

---

## Verification Plan

### Automated
```
npm run verify
```
Jest must pass 31/31 suites (28 existing + 3 new). TSC clean.

### Manual Verification
- All 3 new test files have >0 `expect()` assertions that assert real behavior
- `notifee` mock captures notification calls correctly

### Out of Scope
- `SessionPhaseBadge.tsx` tests — deferred
- `StreetPanel.tsx` consumer tests — deferred
- No changes to any production source files
