# Implementation Plan: Fix Auto-Pause Jitter

## Objective
Reduce GPS multipath jitter from causing rapid auto-pause thrashing by raising the resume threshold to 1.5 mph, keeping the pause threshold at 0.5 mph, and incorporating a secondary heuristic if possible, or simply utilizing hysteresis.

## Source of Truth
- `src/services/session/AutoPauseService.ts#L17-L20`

## Steps
1. **Modify Speed Thresholds & Hysteresis**
   - In `AutoPauseService.ts`, change the pause condition from `< 0.2` to `< 0.5` mph for 10 seconds.
   - Change the resume condition from `>= 0.2` to `>= 1.5` mph.
2. **Verify**
   - Run `npm run verify` to ensure tests pass.
   - Assert that standing still (e.g. speed fluctuating between 0 and 1.0) does not trigger a resume if already paused.

## Files to Create/Modify
- `src/services/session/AutoPauseService.ts`
- `src/services/session/__tests__/AutoPauseService.test.ts` (if required to fix test assertions)

## Out of Scope
- Modifying `SessionMachine.ts` or other unrelated services.
