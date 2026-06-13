# Implementation Plan
# PLAN-STALE-CLOSURE-INTERVALS

## Summary
Two async interval callbacks capture stale closure values and contain no re-entrancy guards, causing silent sync failures and potential duplicate database operations:

1. `useOfflineSyncWorker.ts` — `setInterval` captures stale `user` (null), preventing offline queue from ever flushing after login
2. `useDashboardAutoConnect.ts` — `syncCloudAndAutoConnect` captures stale `session` after BLE state changes
3. Multiple intervals (`useTelemetryLedger`, `useBLEHeartbeat`, `useBLERSSIMonitor`, etc.) fire concurrent async callbacks without re-entrancy guards → duplicate DB inserts and GATT pileup

**Batch:** `BATCH:interval-hardening`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/hooks/cloud/useOfflineSyncWorker.ts` — Line 36 (R-12-001), Line 41 (R-26-001)
- `src/hooks/useDashboardAutoConnect.ts` — Line 169 (R-12-002, R-26-004 related)
- `src/hooks/useTelemetryLedger.ts` — Line 169 (R-26-002)
- `src/hooks/ble/useBLEHeartbeat.ts` — Line 105 (R-26-004)
- `src/hooks/ble/useBLERSSIMonitor.ts` — Line 83 (R-26-005)
- `src/components/CrewMemberDashboard.tsx` — Line 169 (R-26-006)
- `src/context/SessionContext.tsx` — Line 313 (R-26-007)
- Raw audit: `R-12_findings.json`, `R-26_findings.json`

---

## Findings

### CRITICAL — Stale Closures in Interval Callbacks (R-12)

**R-12-001: useOfflineSyncWorker stale user**
```typescript
// PROBLEM
const { user } = useAuth();
useEffect(() => {
  const runSync = async () => {
    if (!user) return; // STALE — user is null at mount, never updates
  };
  syncIntervalRef.current = setInterval(runSync, SYNC_INTERVAL_MS);
}, []); // empty dep array = stale forever
```
**Fix:** Use a ref to forward the latest user:
```typescript
const userRef = useRef(user);
useEffect(() => { userRef.current = user; }, [user]);
// Inside runSync: if (!userRef.current) return;
```

**R-12-002: useDashboardAutoConnect stale session**
```typescript
// PROBLEM
useEffect(() => {
  async function syncCloudAndAutoConnect() {
    if (session?.user) { ... } // captures stale session
  }
  syncCloudAndAutoConnectRef.current = syncCloudAndAutoConnect;
}, [isBluetoothSupported, isBluetoothEnabled]); // session not in deps
```
**Fix:** Either add `session` to the dependency array, or use a `sessionRef` that updates on every render.

### HIGH — Missing Re-entrancy Guards (R-26)

| ID | File | Line | Risk |
|----|------|------|------|
| R-26-001 | `useOfflineSyncWorker.ts` | 41 | Double DB insert |
| R-26-002 | `useTelemetryLedger.ts` | 169 | Duplicate RPC calls |
| R-26-004 | `useBLEHeartbeat.ts` | 105 | GATT command pileup |
| R-26-005 | `useBLERSSIMonitor.ts` | 83 | Duplicate GATT reads |
| R-26-006 | `CrewMemberDashboard.tsx` | 169 | Duplicate Supabase queries |
| R-26-007 | `SessionContext.tsx` | 313 | Duplicate notifee calls |

**Fix pattern (all):**
```typescript
const _isFlushingRef = useRef(false);
const guardedCallback = async () => {
  if (_isFlushingRef.current) return;
  _isFlushingRef.current = true;
  try {
    await originalCallback();
  } finally {
    _isFlushingRef.current = false;
  }
};
setInterval(guardedCallback, INTERVAL_MS);
```

---

## Implementation Steps

### Step 1 — Fix stale user in useOfflineSyncWorker (highest risk — silent data loss)
**File:** `src/hooks/cloud/useOfflineSyncWorker.ts`
1. Add `userRef.current = user` synced via `useEffect`
2. Replace `if (!user)` with `if (!userRef.current)` in `runSync`
3. Add `_isFlushingSync` ref guard

### Step 2 — Fix stale session in useDashboardAutoConnect
**File:** `src/hooks/useDashboardAutoConnect.ts:169`
1. Add `sessionRef.current = session` synced via `useEffect`
2. Update `syncCloudAndAutoConnect` to read from `sessionRef.current`
3. Alternatively: add `session` to the useEffect dependency array (may cause reconnect storm — evaluate first)

### Step 3 — Add re-entrancy guards to telemetry flush
**File:** `src/hooks/useTelemetryLedger.ts:169`
Wrap `flushToDatabase()` in `_isFlushingRef` guard.

### Step 4 — Add re-entrancy guards to BLE intervals
**Files:** `src/hooks/ble/useBLEHeartbeat.ts:105`, `src/hooks/ble/useBLERSSIMonitor.ts:83`
Apply `_isRunningRef` guard to each interval callback.

### Step 5 — Add re-entrancy guards to dashboard and session intervals
**Files:** `src/components/CrewMemberDashboard.tsx:169`, `src/context/SessionContext.tsx:313`
Apply standard guard pattern.

---

## Verification
- `npm run verify`
- Stale closure test: Boot app → stay offline for 30s → sign in → verify sync queue processes within next interval cycle (check AppLogger output)
- Re-entrancy test: Throttle network to 3G in emulator → confirm `_isFlushingSync` guard prevents double-invoke in `useTelemetryLedger`

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Hooks]`
- `[Risk: H-RISK]`
- `[Size: Meal]`
- `[Cognitive Load: Medium]`
- `[BATCH: interval-hardening]`
