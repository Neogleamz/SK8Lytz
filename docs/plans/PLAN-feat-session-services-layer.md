# Implementation Plan
# feat/session-services-layer — XState Session Services Layer

**Wave:** 1 (requires Wave 0 merged — distance field name confirmed)
**Worktree:** `session-services-layer-batch`
**Type:** New files only — zero existing file modifications

## Source of Truth
- `src/services/ble/BleMachine.ts` — structural pattern to mirror exactly
- `src/services/ble/HeartbeatService.ts` — `fromCallback` actor pattern
- `src/services/ble/ConnectService.ts` — `fromPromise` actor pattern
- `src/hooks/useBLE.ts:177` — `useMachine` wiring pattern
- `src/context/SessionContext.tsx:285–359` — notification logic to extract
- `src/context/SessionContext.tsx:1–49` — storage keys and phase constants to reuse
- Implementation Plan: `C:\Users\Magma\.gemini\antigravity\brain\689630a3-694f-4156-a7bc-69878591a1d7\implementation_plan.md`

## Files to Create
All in `src/services/session/` (new directory):
1. `SessionMachine.types.ts`
2. `SessionMachine.ts`
3. `SensorService.ts`
4. `AutoPauseService.ts`
5. `HealthService.ts`
6. `SessionCommitService.ts`
7. `NotificationService.ts`
8. `SessionBridge.ts`

Also: `src/components/session/SessionPhaseBadge.tsx` (new directory)

## Steps

### Step 1 — Create `src/services/session/` directory structure
- Verify: directory exists, no existing files

### Step 2 — Create `SessionMachine.types.ts`
```ts
export interface SessionMachineContext {
  startTimeMs: number | null;
  pausedMsAccum: number;
  autoPauseEnabled: boolean;
  gpsSpeedRef: { current: number };
  onTelemetryUpdate: (t: TelemetrySnapshot) => void;
  onHealthUpdate: (h: HealthSnapshot) => void;
  onSessionSaved: () => void;
  externalStartTimeMs?: number;
}
export type SessionMachineEvent =
  | { type: 'START'; externalStartTimeMs?: number }
  | { type: 'PAUSE' }
  | { type: 'RESUME' }
  | { type: 'END' }
  | { type: 'AUTO_PAUSE' }
  | { type: 'AUTO_RESUME' };
export type SessionPhase = 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING';
```
- Source: `SessionContext.tsx:17` — existing `SessionPhase` union matches exactly
- Verify: `tsc --noEmit` on this file passes

### Step 3 — Create `SessionMachine.ts`
Mirror `BleMachine.ts:8–end` structure exactly.
- `setup({ types, actors, actions })` pattern
- States: IDLE → ACTIVE → PAUSED → ENDING → IDLE
- Entry actions: `recordStartTime`, `syncWatchActive`, `persistPhaseActive`, `logTransition`
- Exit actions: `accumulatePausedMs`
- ACTIVE invokes: `sensorService`, `autoPauseService`, `healthService`, `notificationService`
- PAUSED invokes: `autoPauseService`, `notificationService`
- ENDING invokes: `sessionCommitService` (fromPromise) — onDone: `syncWatchStopped` → IDLE
- Source: `BleMachine.ts:8–65` — structural mirror
- Verify: TSC compiles, `createActor(sessionMachine).start()` does not throw

### Step 4 — Create `SensorService.ts`
- `fromCallback<any, SensorServiceInput>` — mirrors `HeartbeatService.ts:17`
- Starts `expo-location` GPS watch + accelerometer subscription
- On GPS update: compute speed (mph), distance (mi), gForce, peak speed
- Calls `input.onTelemetryUpdate(snapshot)` — writes to `telemetryRef.current`
- Sets `input.gpsSpeedRef.current = spdMph`
- Cleanup return: removes both subscriptions
- Source: `useGlobalTelemetry.ts:200–280` — sensor logic to extract
- Verify: Cleanup function executes without error in test

### Step 5 — Create `AutoPauseService.ts`
- `fromCallback<any, AutoPauseServiceInput>` — mirrors `HeartbeatService.ts:17`
- `setInterval(500ms)` — reads `input.gpsSpeedRef.current`
- Low-speed for 10s + `input.autoPauseEnabled` → `sendBack({ type: 'AUTO_PAUSE' })`
- Speed ≥ 0.2 after pause → `sendBack({ type: 'AUTO_RESUME' })`
- Cleanup: `clearInterval`
- Source: `SessionContext.tsx:184–242` — logic to extract
- Verify: `sendBack` fires in test after mocked 10s low-speed signal

### Step 6 — Create `HealthService.ts`
- `fromCallback<any, HealthServiceInput>` — mirrors `HeartbeatService.ts:17`
- Absorbs `useHealthTelemetry.ts` polling logic
- Polls Apple HealthKit / Google Health every 30s
- Registers `WatchBridge.addWatchHealthListener` for watch HR relay
- Calls `input.onHealthUpdate(health)` on each update
- Cleanup: removes listener, clears interval
- Source: `useHealthTelemetry.ts` — full extraction
- Verify: Cleanup removes all listeners

### Step 7 — Create `SessionCommitService.ts`
- `fromPromise<void, SessionCommitInput>` — mirrors `ConnectService.ts:42`
- Reads `input.telemetryRef.current` + `input.startTimeMs` + `input.pausedMsAccum`
- Computes `finalDurationSec` = `(Date.now() - startTimeMs - pausedMsAccum) / 1000`
- Calls `SpeedTrackingService.saveSession(payload)` — SpeedTrackingService untouched
- `onSuccess`: calls `input.onSessionSaved()`
- `onError`: logs via AppLogger, resolves (does not rethrow — machine goes IDLE either way)
- Source: `SessionContext.tsx:388–438` — endSession logic to extract
- Verify: `onSessionSaved` fires on mock success; machine goes IDLE on mock error

### Step 8 — Create `NotificationService.ts`
- `fromCallback<any, NotificationServiceInput>` — mirrors `HeartbeatService.ts:17`
- Extracts `displayNotification` + `setInterval(5000)` from `SessionContext.tsx:285–350`
- Creates notification channel (idempotent)
- Android 14+ location permission guard (from `SessionContext.tsx:272–280`)
- Action buttons: `END SESSION` (existing), + new `PAUSE` / `RESUME` toggle
- `setInterval(5000)` updates notification body with live telemetry from `input.telemetryRef`
- Cleanup: `clearInterval` + `notifee.stopForegroundService()` / `cancelNotification()`
- Source: `SessionContext.tsx:285–358`
- Verify: `clearInterval` called on cleanup, no dangling timers

### Step 9 — Create `SessionBridge.ts`
```ts
let _send: ((event: SessionMachineEvent) => void) | null = null;
export const SessionBridge = {
  register:   (fn: typeof _send) => { _send = fn; },
  unregister: () => { _send = null; },
  send:       (event: SessionMachineEvent) => _send?.(event),
};
```
- Verify: `send()` is a no-op when unregistered (no throw)

### Step 10 — Create `src/components/session/SessionPhaseBadge.tsx`
- Props: `sessionPhase: SessionPhase`
- ACTIVE → `● RECORDING` — `#F79320` orange, animated pulsing dot
- PAUSED → `⏸ PAUSED` — `#FFD700` yellow, static
- ENDING → `⏺ SAVING...` — rgba white, dim, ActivityIndicator
- IDLE → `null`
- Verify: renders correctly for all 4 phases in isolation

### Step 11 — Write Jest test stubs
- `src/services/session/__tests__/SessionMachine.test.ts` — state matrix skeleton
- `src/services/session/__tests__/AutoPauseService.test.ts` — interval logic skeleton
- `src/services/session/__tests__/SessionCommitService.test.ts` — onSessionSaved test
- `src/services/session/__tests__/SessionBridge.test.ts` — no-op + routing tests
- Verify: `npm run verify` passes (stubs pass, not full coverage)

## Out of Scope
- Any changes to `SessionContext.tsx` (Wave 2)
- Any UI file modifications (Wave 3B, 3C)
- Deletion of `useGlobalTelemetry.ts` or `useHealthTelemetry.ts` (Wave 3A)
