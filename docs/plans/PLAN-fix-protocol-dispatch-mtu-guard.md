# Implementation Plan
## fix/protocol-dispatch-mtu-guard — Add MTU Guard to `useProtocolDispatch.setCustomModeExtended`

**Severity:** HIGH  
**Source:** Defect Audit 2026-06-24 — F-003

---

## Context

`useProtocolDispatch.setCustomModeExtended` (line 66) calls `_dispatchToDevices`, which
calls `executeProtocolResults → _executeProtocolResultsInternal`. That internal function
calls `adapter.prepareForTransmission(result, mtu)` — which is a pass-through returning
`result` unchanged — then writes packets directly via `writeCharacteristicWithoutResponseForService`.

There is **no MTU check** on this path for 0x51 extended payloads (323 bytes).
A 323-byte packet exceeds any typical BLE MTU (default 186 bytes negotiated, 183 bytes safe).
The BLE stack will silently drop or fragment it incorrectly, causing the hardware to receive
a corrupt or empty command.

The safe path (`writeToDevice → executeWriteToDevice → _executeWriteToDeviceInternal:155`) DOES
have the correct guard:
```typescript
if (cmdId === 0x51 && payload.length > 200) {
  await executeWriteChunked(payload, ...);
  return true;
}
```

The fix: intercept oversized 0x51 `ProtocolResult` packets inside `_dispatchToDevices` and
route them through `writeChunked` instead of `executeProtocolResults`.

Note: `executeRawPayload` in the same hook already has this guard at line 115. The fix mirrors
that pattern. No new logic is invented — only the missing gate is added.

---

## Files to Create / Modify

| File | Lines | Change |
|------|-------|--------|
| `src/hooks/useProtocolDispatch.ts` | 20–43 (`_dispatchToDevices`) | Add 0x51 MTU interception before routing to `executeProtocolResults` |

---

## Step-by-Step Execution

**1 — Look before leap: read `_dispatchToDevices` in full**
```
Read: src/hooks/useProtocolDispatch.ts, lines 20–45
```
Confirm: The function structure, the `executeProtocolResults` call, and the dependency array.

**2 — Read `executeRawPayload` as the reference implementation**
```
Read: src/hooks/useProtocolDispatch.ts, lines 110–145
```
The guard pattern at lines 114–122 is exactly what we need to mirror inside `_dispatchToDevices`.

**3 — Read `_executeProtocolResultsInternal` to confirm no hidden MTU check**
```
Read: src/services/BleWriteDispatcher.ts, lines 363–425
```
Verify: `prepareForTransmission` is called at line 396, and no packet-size check exists before
line 401 (`writeCharacteristicWithoutResponseForService`).

**4 — Surgical edit: add MTU guard to `_dispatchToDevices`**

Inside `_dispatchToDevices`, after building `payloads` (line ~35-40) but BEFORE calling
`executeProtocolResults`, insert:

```typescript
// MTU GUARD: 0x51 extended payloads (323B) must route through writeChunked.
// executeProtocolResults → _executeProtocolResultsInternal has no MTU check and would
// attempt to write 323B in a single characteristic write — silent GATT drop on all MTUs.
// This mirrors the guard in executeRawPayload:114 and executeWriteToDevice:155.
const hasOversized0x51 = payloads.some(
  ({ result }) => result.packets.some(p => p[0] === 0x51 && p.length > 200)
);
if (hasOversized0x51) {
  // Route each oversized packet through writeChunked per device
  const writes = payloads.map(({ targetDeviceId, result }) =>
    Promise.all(
      result.packets
        .filter(p => p[0] === 0x51 && p.length > 200)
        .map(p => writeChunked(p, targetDeviceId))
    )
  );
  await Promise.all(writes);
  return true;
}
```

Then verify `writeChunked` is in the `_dispatchToDevices` useCallback's scope — it is
destructured from `context` at line 18.

**5 — Add `writeChunked` to the `_dispatchToDevices` useCallback dep array**
```
Read: src/hooks/useProtocolDispatch.ts, line 43 (dep array)
```
Add `writeChunked` to the dep array if not already present:
```typescript
[connectedDevices, getAdapterForDevice, executeProtocolResults, writeChunked]
```

**6 — Post-edit diff**
```
git diff HEAD src/hooks/useProtocolDispatch.ts
```
Verify: Only `_dispatchToDevices` modified. No other functions touched.

**7 — Verify**
```
npm run verify
```
Expected: TSC ✅ Jest ✅

**8 — Manual validation**
In DiagnosticLab, trigger `setCustomModeExtended` with a 0x51 payload.
Observe: No GATT error logged. Hardware responds correctly to the chunked command.
(Requires device; document result in SESSION_LOG if hardware available.)

---

## Out of Scope

- Modifying `_executeProtocolResultsInternal` directly (fix belongs at the dispatch API layer)
- Modifying `BleWriteDispatcher.ts` (the safe `writeToDevice` path is already correct)
- Fixing the stale comment in ZenggeAdapter.ts (see F-004 / PLAN-fix-adapter-chunking-comment.md)
- Modifying any screen that calls `useProtocolDispatch` (API surface unchanged)
