# Implementation Plan
# PLAN-BLE-JITTER-BACKOFF

## Summary
Three BLE reconnect paths use fixed-delay retry timers with no jitter. When multiple devices attempt simultaneous reconnection (group sync scenario), all retries fire at identical intervals, creating a stampede on the GATT semaphore and amplifying GATT 133 collision risk. Adding randomized jitter decoheres the retry schedule.

**Batch:** `BATCH:ble-gatt-hardening`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/hooks/useBLE.ts` — Line 131 (R-03-001)
- `src/hooks/useDashboardAutoConnect.ts` — Line 169 (R-03-002)
- `src/services/BleConnectionManager.ts` — Line 150 (R-03-003)
- Raw audit: `R-03_findings.json`

---

## Findings

| ID | File | Line | Current Delay | Severity |
|----|------|------|--------------|----------|
| R-03-001 | `src/hooks/useBLE.ts` | 131 | Fixed 1000ms | HIGH |
| R-03-002 | `src/hooks/useDashboardAutoConnect.ts` | 169 | Fixed 1000ms | MEDIUM |
| R-03-003 | `src/services/BleConnectionManager.ts` | 150 | Static array [500, 1500, 4000] | MEDIUM |

---

## Implementation Steps

### Step 1 — Add jitter utility (1 new helper)
Create or add to existing utils:
```typescript
// src/utils/backoff.ts (or inline)
export const jitteredDelay = (baseMs: number, jitterMs = 500): number =>
  baseMs + Math.floor(Math.random() * jitterMs);
```

### Step 2 — Fix useBLE.ts iOS state restoration reconnect
**File:** `src/hooks/useBLE.ts:131`
```typescript
// BEFORE
setTimeout(() => { connectToDevicesRef.current(peripherals); }, 1000);

// AFTER
const delay = jitteredDelay(1000, 500); // 1000–1500ms
setTimeout(() => { connectToDevicesRef.current(peripherals); }, delay);
```

### Step 3 — Fix useDashboardAutoConnect retry
**File:** `src/hooks/useDashboardAutoConnect.ts:169`
```typescript
// BEFORE
debounceTimerRef.current = setTimeout(attemptConnection, 1000);

// AFTER
const backoffMs = Math.min(1000 * Math.pow(1.5, attemptCount), 10000);
const delay = jitteredDelay(backoffMs, backoffMs * 0.25);
debounceTimerRef.current = setTimeout(attemptConnection, delay);
```
*(Requires tracking `attemptCount` ref — increment on each retry, reset on success.)*

### Step 4 — Fix BleConnectionManager static backoff array
**File:** `src/services/BleConnectionManager.ts:150`
```typescript
// BEFORE
const delay = GATT_BACKOFF_MS[attempt - 1];
await new Promise(resolve => setTimeout(resolve, delay));

// AFTER
const baseDelay = GATT_BACKOFF_MS[attempt - 1];
const delay = jitteredDelay(baseDelay, 500);
await new Promise(resolve => setTimeout(resolve, delay));
```

---

## Verification
- `npm run verify`
- Soak test: Force disconnect 2 connected devices simultaneously → observe reconnect timing in logcat → confirm no simultaneous retry bursts
- `adb logcat -s "ReactNativeJS:V" | grep "reconnect"` should show staggered timestamps

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: M-RISK]`
- `[Size: Snack]`
- `[Cognitive Load: Low]`
- `[BATCH: ble-gatt-hardening]`
