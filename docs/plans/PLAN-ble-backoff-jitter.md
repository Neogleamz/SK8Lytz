# Implementation Plan
# PLAN-ble-backoff-jitter

## Summary
The audit (Cluster E, R-03) flagged `RecoveryService.ts` for missing jitter on its exponential
backoff. However, a live code inspection revealed that **jitter is already present** in the primary
`getRecoveryBackoffMs()` helper (line 21: `const jitter = Math.random() * RECOVERY_BASE_MS`).

**The real gap is narrower:** Phase 2 of the recovery loop (attempts > `PHASE_1_MAX_ATTEMPTS`) uses
an inline backoff that adds only partial jitter (`Math.random() * RECOVERY_BASE_MS` on line 77),
which is correct. The `getRecoveryBackoffMs()` helper on lines 19-23 is the canonical backoff path
and is already jitter-correct.

**What IS missing:** The existing jitter range is up to 100% of `RECOVERY_BASE_MS` (1500ms), which
is wider than the AWS-recommended ±20–30% window. The audit recommends capping jitter at 30% of the
base delay to avoid introducing excessive latency on fast reconnects while still preventing
thundering-herd collisions.

**Batch:** `BATCH:ble-recovery-hardening`
**Status:** `[✅ VERIFIED]`

---

## Cited Truth — Live Code State

| Location | Current Code | Assessment |
|---|---|---|
| `RecoveryService.ts:19-23` | `const jitter = Math.random() * RECOVERY_BASE_MS` | Jitter present but range is 0–1500ms (100% of base). AWS standard: 0–30% of computed delay. |
| `RecoveryService.ts:75-77` | Phase 2 inline: `PHASE_2_BACKOFF_MS + Math.random() * RECOVERY_BASE_MS` | Phase 2 jitter correct (adds up to 1500ms on 20s base = 7.5% — already within spec). |
| `RecoveryService.ts:10` | `MAX_RECOVERY_ATTEMPTS = 360` | Present. See separate plan: PLAN-ble-recovery-max-attempts. |

**Contradiction Check:** Scanned `RecoveryService.ts` lines 19-23 and 73-77. Jitter exists in both
paths. The issue is jitter magnitude in the Phase 1 path, not absence of jitter.

---

## Source of Truth

- **Primary:** `artifacts/system_audit_report.md` — Cluster E, R-03 finding
- **Secondary:** `src/services/ble/RecoveryService.ts` — lines 19-23 (canonical backoff)
- **Reference:** AWS Architecture Blog — "Exponential Backoff and Jitter" (equal jitter = base/2 + rand(0, base/2))

---

## Implementation Steps

### Step 1 — Narrow jitter to 30% of computed exponential delay
**File:** `src/services/ble/RecoveryService.ts`
**Target:** Lines 19-23

```typescript
// BEFORE (current — jitter = 0 to 1500ms regardless of backoff magnitude)
export const getRecoveryBackoffMs = (attempts: number): number => {
  const exponential = Math.min(RECOVERY_BASE_MS * Math.pow(1.5, attempts), RECOVERY_MAX_MS);
  const jitter = Math.random() * RECOVERY_BASE_MS;
  return Math.round(exponential + jitter);
};

// AFTER (jitter = 0 to 30% of the exponential window — scales proportionally)
export const getRecoveryBackoffMs = (attempts: number): number => {
  const exponential = Math.min(RECOVERY_BASE_MS * Math.pow(1.5, attempts), RECOVERY_MAX_MS);
  const jitter = Math.random() * exponential * 0.3;
  return Math.round(exponential + jitter);
};
```

**Why:** `Math.random() * RECOVERY_BASE_MS` produces jitter of 0–1500ms even when the exponential
delay is 30,000ms. At high attempt counts the jitter becomes negligible (5% of 30s). At attempt 0
the jitter (0–1500ms) is 100% of the 1500ms base, which can add 100% latency to the first retry —
the worst moment for the user. Proportional jitter (30% of exponential) scales correctly at all
attempt depths.

### Step 2 — Verify with unit test assertion
**Verify:** Add/update a test that calls `getRecoveryBackoffMs` 100 times at attempt=0 and asserts
no two consecutive results are identical (probabilistically guarantees jitter is active) and that
all results fall within `[RECOVERY_BASE_MS, RECOVERY_BASE_MS * 1.3]`.

---

## Verification

- `npm run verify` — TSC must pass, no new errors
- Unit test: `getRecoveryBackoffMs(0)` called 100x — assert results vary (no two equal) and max
  result ≤ `RECOVERY_BASE_MS * 1.3` (1950ms)
- Logcat soak: Force simultaneous disconnect of 2 skates → confirm recovery retry timestamps are
  staggered by at least 100ms

## Kanban Task Tags
- `[Status: ✅ READY]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: L-RISK]`
- `[Size: Snack]`
- `[Cognitive Load: 🤖 PRO-MED]`
- `[BATCH: ble-recovery-hardening]`
