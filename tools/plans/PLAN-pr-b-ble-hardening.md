# PLAN: PR-B — BLE Hardening (Session Time Sync + Chunked Framing)

**Slug**: `fix/pr-b-ble-hardening`
**Commit Group**: PR-B — touches `useBLE.ts` only
**Created**: 2026-04-22
**Status**: 🔲 Not Started
**Risk**: M-RISK (BLE infrastructure — touches connection handshake and write pipeline)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Covers These Tasks

1. `fix/session-time-sync-0x10` — time sync packet never sent on connection
2. `fix/chunked-ble-framing-0x51` — large payloads need chunked BLE writes (see full plan in PLAN-chunked-ble-framing-0x51.md)

Both touch `src/hooks/ble/useBLE.ts` — ship in same PR to avoid merge conflicts.

---

## Fix 1: `fix/session-time-sync-0x10`

**File**: `src/hooks/ble/useBLE.ts`
**Severity**: M-RISK — hardware timer starts from wrong baseline without this

### The Bug
`ZenggeProtocol.setSessionTime()` exists at `ZenggeProtocol.ts:742` but has **zero call sites** in the entire codebase.

The ZENGGE app sends `0x10` on EVERY successful BLE connection handshake, before any other writes. Without it, the hardware internal session clock starts from epoch 0 — timing-sensitive effects (especially 0x51 scene timing) may drift or misfire.

### The Fix

Find the `onConnected` callback in `useBLE.ts` — the point where the connection is confirmed and the service/characteristic is resolved. Immediately after that callback fires, send the time sync:

```typescript
// In useBLE.ts — inside the onConnected handler or the connection stabilization effect
const syncTime = async () => {
  if (!writeToDevice) return;
  const timePayload = ZenggeProtocol.setSessionTime();
  await writeToDevice(timePayload);
  AppLogger.log('BLE_TIME_SYNC', { timestamp: Date.now() });
};

// Call immediately after connection confirmed — before any other writes
useEffect(() => {
  if (connectionState === 'CONNECTED') {
    syncTime();
  }
}, [connectionState]);
```

> **Do NOT send before the device is fully ready** — wait for `connectionState === 'CONNECTED'`, not just `'CONNECTING'`. Check existing FSM state names in `useBLE.ts`.

### Verify `setSessionTime()` payload
From `ZenggeProtocol.ts:742` — confirm the method builds a proper 0x10 packet with current UTC timestamp. If the implementation looks incomplete or uses a stub, fix it:

```typescript
// Expected 0x10 format (from APK — TimeControllerFragment.java):
// [0x10, year-2000, month, day, hour, min, sec, weekday, checksum]
static setSessionTime(): number[] {
  const now = new Date();
  const raw = [
    0x10,
    now.getFullYear() - 2000,  // year offset from 2000
    now.getMonth() + 1,         // 1-12
    now.getDate(),              // 1-31
    now.getHours(),             // 0-23
    now.getMinutes(),           // 0-59
    now.getSeconds(),           // 0-59
    now.getDay(),               // 0=Sun, 1=Mon, ...
  ];
  raw.push(ZenggeProtocol.calculateChecksum(raw));
  return ZenggeProtocol.wrapCommand(raw);
}
```

---

## Fix 2: Chunked BLE Framing

This fix is fully specified in [PLAN-chunked-ble-framing-0x51.md](./PLAN-chunked-ble-framing-0x51.md).

**Summary**: Add `writeChunked(payload: number[], chunkSize = 20)` to `useBLE.ts`. Required for 0x51 extended Scene Builder payloads (323 bytes total, 27 chunks of 12 bytes each with 8-byte ZENGGE header).

---

## Execution Order Within PR-B

```
1. Verify setSessionTime() payload is correct in ZenggeProtocol.ts
2. Add syncTime() call in useBLE.ts onConnected handler
3. Add writeChunked() function to useBLE.ts
4. Export writeChunked from hook return value
5. Update useControllerDispatch.ts to use writeChunked for 0x51 payloads
```

---

## Files To Touch

| File | Change |
|------|--------|
| `src/hooks/ble/useBLE.ts` | Add syncTime on connect + add writeChunked() |
| `src/protocols/ZenggeProtocol.ts` | Verify/fix setSessionTime() payload format |
| `src/hooks/useControllerDispatch.ts` | Use writeChunked for 0x51 extended calls |

---

## Test Criteria

- [ ] Connect to HALOZ: BLE sniffer (or Oracle Lab log) shows `0x10` packet sent immediately after connection
- [ ] Oracle Lab: verify 0x10 packet contains correct current time bytes
- [ ] `writeChunked` with 40-byte payload sends 2 × 20-byte chunks
- [ ] `writeChunked` with 323-byte payload sends 27 chunks (each ≤20 bytes)
- [ ] No regression: existing `writeToDevice` single-write patterns still work
- [ ] `npx tsc --noEmit` — zero errors
