# PLAN: perf/sensor-suspension-inactive-modes

## Goal
Suspend the microphone audio subscription and the accelerometer subscription when their respective modes are not active, reducing unnecessary CPU usage and battery drain.

## Problem
`useAppMicrophone` runs a live audio magnitude subscription at all times regardless of `activeMode`. `useStreetMode` runs an accelerometer subscription at all times regardless of `activeMode`. Both hooks are always mounted in `DockedController`. A user in MULTIMODE tapping patterns is burning CPU/battery on audio + motion sensor sampling that has zero effect on the hardware.

## Impact
- Mic subscription: audio processing pipeline runs continuously (AudioContext, PCM analysis)
- Accel subscription: gyroscope/accelerometer polling (significant Android battery drain, typically 2-5% per hour)
- Both subscriptions prevent the CPU from reaching deep idle states

## Target Files
- `src/hooks/useAppMicrophone.ts` — suspend audio subscription when `activeMode !== 'MUSIC'`
- `src/hooks/useStreetMode.ts` — suspend accel subscription when `activeMode !== 'STREET'`

## Execution Checklist

### useAppMicrophone
1. Read the hook — find where the audio subscription is registered (likely a `useEffect` with a start/stop pattern)
2. Add `activeMode` as a dep to that effect
3. Gate the subscription start: `if (activeMode !== 'MUSIC') { subscription.stop(); return; }`
4. Ensure cleanup runs on mode exit (the subscription should already have cleanup, just gate it)

### useStreetMode
1. Read the hook — find where `Accelerometer.addListener` or equivalent is called
2. Gate: `if (activeMode !== 'STREET') { Accelerometer.removeAllListeners(); return; }`
3. On mode entry (`activeMode === 'STREET'`), restart the subscription with existing sensitivity

### Validation
- In MULTIMODE, open AppLogger and confirm no `[MIC]` or `[ACCEL]` log events firing
- In MUSIC mode, confirm mic subscription resumes
- In STREET mode, confirm accel subscription resumes and brake detection still works

## Rollback
Remove the `activeMode` gate. Subscriptions resume running always. Zero functional regression.

## Collateral Damage Locks
- DO NOT change sensitivity values, thresholds, or the motion FSM logic in useStreetMode
- DO NOT change the PCM analysis or magnitude normalization in useAppMicrophone
- ONLY add the mode guard to the subscription lifecycle effect
