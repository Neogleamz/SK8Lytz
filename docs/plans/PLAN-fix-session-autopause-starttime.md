# Implementation Plan: fix-session-autopause-starttime

## Problem

**BUG-S3 MEDIUM** — When auto-pause resumes the session in [SessionContext.tsx L136–141](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/SessionContext.tsx#L136-L141), the watch is told the session started at `new Date().toISOString()` — the **current** time, not the original (anchor-shifted) start time.

```typescript
// L138-141 — the bug
await WatchBridge.syncSessionState({
  status: 'ACTIVE',
  startTime: new Date().toISOString(),  // ← NOW, not original start
});
```

**Meanwhile**, `useGlobalTelemetry` at [L66–90](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useGlobalTelemetry.ts#L66-L90) correctly shifts the internal `sessionStartTimeRef` forward by the pause duration (L74) and pushes the **correct** shifted anchor to the watch (L77–83). This means the watch actually receives **two** conflicting `ACTIVE` payloads on resume:

1. First from `useGlobalTelemetry` L78–83 → **correct** shifted anchor ✅
2. Then from `SessionContext.tsx` L138–141 → `new Date()` **overwrites** the correct one ❌

The second push wins, and the watch timer resets to zero.

## Root Cause

The auto-pause resume path in `SessionContext.tsx` was written before the anchor-shifting logic was added to `useGlobalTelemetry`. It redundantly pushes `ACTIVE` to the watch with a wrong timestamp. The `useGlobalTelemetry` effect at L66–90 already handles the correct watch sync on PAUSED→ACTIVE transitions.

## Proposed Fix

**Remove the redundant watch sync from the auto-pause resume path.** `useGlobalTelemetry` already handles it correctly via the `prevSessionPhaseRef` transition detector at L67–89.

### Step 1: Remove the watch sync from auto-pause disable path (L136–143)

Replace:
```typescript
        if (sessionPhase === 'PAUSED') {
            setSessionPhase('ACTIVE');
            await WatchBridge.syncSessionState({
              status: 'ACTIVE',
              startTime: new Date().toISOString(),
            });
          }
```

With:
```typescript
        if (sessionPhase === 'PAUSED') {
            setSessionPhase('ACTIVE');
            // Watch sync handled by useGlobalTelemetry PAUSED→ACTIVE transition (L66-90)
          }
```

### Step 2: Verify the speed-based resume path is also clean (L159–162)

The speed-based auto-resume at L159–162 already does **not** push to the watch — it only calls `setSessionPhase('ACTIVE')`, which triggers `useGlobalTelemetry`'s transition detector. This path is already correct. No change needed.

```typescript
          if (sessionPhase === 'PAUSED') {
            setSessionPhase('ACTIVE');
            AppLogger.log('APP_LOG', { event: 'auto_resume_triggered' });
          }
```

## Files Modified

| File | Change |
|------|--------|
| `src/context/SessionContext.tsx` | Remove `WatchBridge.syncSessionState()` call from auto-pause disable resume path at L138–141 |

## Verification

1. Enable auto-pause in settings
2. Start session → skate (build up telemetry) → stop moving for 10s → session auto-pauses
3. Resume skating → watch elapsed timer should **continue** from where it left off, NOT reset to zero
4. Compare phone elapsed time vs watch elapsed time — they must match within ±1 second
5. Disable auto-pause while in PAUSED state → session resumes → watch timer still correct
6. Check AppLogger for a single `anchor_shifted` event from `useGlobalTelemetry` (not two)

## Risk Assessment

| Risk | Severity | Mitigation |
|------|----------|------------|
| Auto-pause disable no longer pushes ACTIVE to watch | None | `useGlobalTelemetry` L66–90 fires on `sessionPhase` change and pushes ACTIVE with correct anchor |
| `useGlobalTelemetry` effect doesn't fire on auto-pause disable | None | `setSessionPhase('ACTIVE')` triggers re-render → `useGlobalTelemetry` receives new `sessionPhase` prop → L67–89 detects PAUSED→ACTIVE |
| Removing the import of WatchBridge from SessionContext | None | WatchBridge is still used in `startSession` (L301), `endSession` (L334, L347), and auto-pause trigger (L152) |

**Rollback**: `git checkout -- src/context/SessionContext.tsx`
