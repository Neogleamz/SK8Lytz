# Implementation Plan: Refactor Session Unified Heartbeat

## Objective
Consolidate multiple `setInterval` loops across services into a single unified 1Hz clock ticking from `SessionMachine.ts` to reduce React Native JS thread wakeups and battery drain.

## Source of Truth
- `src/services/session/AutoPauseService.ts#L31`
- `src/services/session/NotificationService.ts#L136`

## Steps
1. **Centralize Timer in SessionMachine**
   - Add a 1Hz `setInterval` invoke or actor inside the `ACTIVE` and `PAUSED` states of `SessionMachine.ts`.
   - This actor emits a `TICK` event every 1000ms.
2. **Refactor Child Services**
   - Convert `AutoPauseService` and `NotificationService` to listen to the `TICK` event (or transition their internal logic to respond to `SessionMachine` state context updates triggered by TICK).
   - Alternatively, convert them from independent timers to simple pure functions that the `SessionMachine` invokes on every `TICK`.
3. **Verify**
   - Run `npm run verify` to ensure the session machine and its tests pass.

## Files to Create/Modify
- `src/services/session/SessionMachine.ts`
- `src/services/session/SessionMachine.types.ts`
- `src/services/session/AutoPauseService.ts`
- `src/services/session/NotificationService.ts`

## Out of Scope
- Unrelated telemetry tracking logic.
