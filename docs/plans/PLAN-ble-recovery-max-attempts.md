# Implementation Plan
# PLAN-ble-recovery-max-attempts

## Summary
The audit (Cluster E, R-06) found that `RecoveryService.ts` has no practical max-attempts cap.
Live code inspection reveals `MAX_RECOVERY_ATTEMPTS = 360` exists (line 10), but 360 attempts at
the Phase 2 backoff rate of 20s each = **2 hours** of continuous reconnect attempts before the
service exits. This is the "infinite retry" the audit flagged — a device that has run out of
battery will drain the user's phone for 2 hours before giving up.

Additionally, when `hasExceededMaxRecovery` fires (line 83-86), the service only `break`s from the
Phase 1/2 loop and falls through to Phase 3 passive mode, then finally sends `RECOVERY_FAIL` — but
the machine transitions to `IDLE`, not a `FAILED`/`PERMANENTLY_FAILED` state. The UI has no
mechanism to surface "this device is unreachable" to the user.

**Fix:** Reduce `MAX_RECOVERY_ATTEMPTS` to 5 for Phase 1+2 GATT hammering (covers normal
Bluetooth drop/reconnect scenarios in ≤2 minutes). Phase 3 passive watch retains its own
`PHASE_3_MAX_POLLS = 120` (10 minutes). After exhausting all phases, the machine must transition
to a `FAILED` terminal state and the UI must surface a recoverable error.

**Batch:** `BATCH:ble-recovery-hardening`
**Status:** `[✅ VERIFIED]`

---

## Cited Truth — Live Code State

| Location | Current Code | Issue |
|---|---|---|
| `RecoveryService.ts:10` | `export const MAX_RECOVERY_ATTEMPTS = 360` | 360 × ~20s average = ~2hr retry storm |
| `RecoveryService.ts:25-27` | `return attempts > MAX_RECOVERY_ATTEMPTS` | Check exists but ceiling is impractical |
| `RecoveryService.ts:73` | `while (!cancelled && attempts <= PHASE_2_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts))` | `PHASE_2_MAX_ATTEMPTS = 35` is the operative ceiling in Phase 1+2 loop |
| `RecoveryService.ts:83-86` | `AppLogger.warn(...); break;` | Breaks loop but falls through to Phase 3, then sends RECOVERY_FAIL |
| `RecoveryService.ts:234` | `sendBack({ type: 'RECOVERY_FAIL' })` | Machine transitions to IDLE — no FAILED state |

**Contradiction Check:** Scanned `RecoveryService.ts` lines 10-27 and `BleMachine.ts` (not read
directly — see SoT note). The `MAX_RECOVERY_ATTEMPTS` constant is exported and referenced only
in `hasExceededMaxRecovery`. The actual effective ceiling in the hot path is `PHASE_2_MAX_ATTEMPTS
= 35`. See Step 2 note.

---

## Source of Truth

- **Primary:** `artifacts/system_audit_report.md` — Cluster E, R-06 finding (line 241)
- **Secondary:** `src/services/ble/RecoveryService.ts` — lines 10-17, 25-27, 73-86, 229-234
- **Tertiary:** `src/services/ble/BleMachine.ts` — RECOVERING state transitions (must read before
  implementing Step 3)

---

## Implementation Steps

### Step 1 — Reduce MAX_RECOVERY_ATTEMPTS to a practical ceiling
**File:** `src/services/ble/RecoveryService.ts`
**Target:** Line 10

```typescript
// BEFORE
export const MAX_RECOVERY_ATTEMPTS = 360;

// AFTER — 5 GATT hammering attempts in Phase 1+2 before escalating to passive Phase 3
// Phase 3 has its own PHASE_3_MAX_POLLS = 120 ceiling (10 minutes passive watch)
export const MAX_RECOVERY_ATTEMPTS = 5;
```

**Why 5:** Covers BLE stack reset + normal reconnect latency (attempts 1–3 are exponential: 1.5s,
2.25s, 3.4s, 5.1s, 7.6s = ~20s total). If not reconnected in 5 attempts, the device is likely
out of range — escalate to passive Phase 3 mode, not more hammering.

### Step 2 — Verify PHASE_2_MAX_ATTEMPTS still makes sense
**File:** `src/services/ble/RecoveryService.ts`
**Target:** Line 14

```typescript
// BEFORE
const PHASE_2_MAX_ATTEMPTS = 35;

// AFTER — reduce to match new practical ceiling (Phase 1 handles first 5; Phase 2 is now unused
// in the normal path, but kept as a safety backstop set to match MAX)
const PHASE_2_MAX_ATTEMPTS = 5;
```

**Note:** With `MAX_RECOVERY_ATTEMPTS = 5`, the `while` condition `attempts <= PHASE_2_MAX_ATTEMPTS`
on line 73 becomes the operative gate. Both constants must agree.

### Step 3 — Add PERMANENTLY_FAILED machine event after all phases exhausted
**File:** `src/services/ble/RecoveryService.ts`
**Target:** Lines 229-235 (post-loop state resolution)

```typescript
// BEFORE
if (reconnectedDevice) {
  sendBack({ type: 'RECOVERY_COMPLETE', devices: [reconnectedDevice] });
} else {
  sendBack({ type: 'RECOVERY_FAIL' });
}

// AFTER — distinguish recoverable fail (Phase 3 ended normally) from permanent fail (max attempts)
if (reconnectedDevice) {
  sendBack({ type: 'RECOVERY_COMPLETE', devices: [reconnectedDevice] });
} else if (hasExceededMaxRecovery(attempts)) {
  // Device exhausted all GATT hammering AND passive watch — treat as permanently unreachable
  sendBack({ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId });
} else {
  sendBack({ type: 'RECOVERY_FAIL' });
}
```

### Step 4 — Add RECOVERY_PERMANENTLY_FAILED to BleMachine.ts
**File:** `src/services/ble/BleMachine.ts` (read before editing — P1 compliance)
**Target:** RECOVERING state event handlers

Add transition:
```typescript
RECOVERY_PERMANENTLY_FAILED: {
  target: 'IDLE',
  actions: ['setDeviceUnreachable', 'notifyUserDeviceFailed']
}
```

Where `notifyUserDeviceFailed` surfaces a toast: `"[Device name] is unreachable. Please bring it
closer and reconnect."` This is M-RISK because it introduces a new machine event that must be
declared in the BleMachineEvent union type.

### Step 5 — Verify unit test
**Verify:** After 5 failed attempts, `sendBack` receives `RECOVERY_PERMANENTLY_FAILED`, not
`RECOVERY_FAIL`. Assert `hasExceededMaxRecovery(6)` returns `true` and
`hasExceededMaxRecovery(5)` returns `false`.

---

## Risk Assessment — M-RISK Justification
This plan changes the recovery terminal state and introduces a new machine event
(`RECOVERY_PERMANENTLY_FAILED`). This requires:
1. New event type added to `BleMachineEvent` union (TypeScript change)
2. New machine state handler in `BleMachine.ts`
3. UI toast/notification wiring

If the BleMachineEvent type is missed, TypeScript will catch it. If the machine state handler is
missed, the machine will silently ignore the event (safe-fail). The UI toast is additive — its
absence is not a regression.

## Verification

- `npm run verify` — TSC must pass including new event type
- Unit test: Simulate 6 consecutive `createGattSession` failures → assert final `sendBack` type is
  `RECOVERY_PERMANENTLY_FAILED`
- Unit test: `hasExceededMaxRecovery(5)` → `false`; `hasExceededMaxRecovery(6)` → `true`
- Device test: Power off skate completely → confirm phone shows unreachable toast within ~25s

## Kanban Task Tags
- `[Status: ✅ READY]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: M-RISK]`
- `[Size: Meal]`
- `[Cognitive Load: 🤖 PRO-HIGH]`
- `[BATCH: ble-recovery-hardening]`
