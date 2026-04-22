# PLAN: Chunked BLE Framing for 0x51 Extended Payload

**Slug**: `fix/chunked-ble-framing-0x51`  
**Created**: 2026-04-22  
**Status**: 🔲 Not Started  
**Risk**: H-RISK  
**Blocks**: EPIC-004 Phase 3 Scene Builder  
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Problem Summary

The `0x51` extended payload (32 scene slots × 10 bytes each + header = ~323 bytes total) **cannot be sent as a single BLE write**. BLE 4.x MTU is 20 bytes per write. Our current `writeToDevice()` sends a flat array — it will silently fail or corrupt for any payload over 20 bytes.

The ZENGGE APK wraps the full payload in sequential chunked writes using a specific framing header. Without this, the Scene Builder (EPIC-004 Phase 3) cannot function at all.

---

## Protocol Ground Truth (from APK analysis)

### Chunk Frame Format
Each chunk is 20 bytes maximum:
```
[0x40, seqByte, 0x00, 0x00, 0x01, 0x43, 0xBD, 0x0B, ...payload_bytes...]
```

Where:
- `0x40` — chunk start marker
- `seqByte` — sequence counter (0x00 for first chunk, increments per chunk)
- `0x00, 0x00` — padding/reserved
- `0x01, 0x43, 0xBD, 0x0B` — static framing signature (confirmed from HCI sniff)
- remaining bytes — slice of the actual payload

### Chunking Math
```
totalPayload = 323 bytes (32 slots × 10B + 3B header)
headerSize = 8 bytes per chunk
dataPerChunk = 20 - 8 = 12 bytes of payload per chunk
totalChunks = ceil(323 / 12) = 27 chunks
```

> ⚠️ **Verify the exact header signature** against `tools/ZENGGE_PROTOCOL_BIBLE.md` before implementing. The `[0x43, 0xBD, 0x0B]` bytes may be payload-length-dependent.

---

## Implementation Plan

### Step 1: Add `writeChunked()` to `useBLE.ts`

Location: `src/hooks/ble/useBLE.ts` — add after the existing `writeToDevice` callback.

```typescript
/**
 * Sends a large payload to BLE device using ZENGGE chunked framing.
 * Required for 0x51 extended mode (Scene Builder).
 * MTU on BLE 4.x = 20 bytes. Each chunk uses 8-byte ZENGGE header.
 * 
 * @param payload - The raw payload bytes to send (e.g. full 0x51 sequence)
 * @param chunkSize - Max bytes per BLE write (default 20 = BLE 4.x MTU)
 */
const writeChunked = useCallback(
  async (payload: number[], chunkSize: number = 20): Promise<void> => {
    if (!writeToDevice) return;

    const HEADER_SIZE = 8;
    const dataPerChunk = chunkSize - HEADER_SIZE;
    const chunks: number[][] = [];

    let seqByte = 0x00;
    for (let i = 0; i < payload.length; i += dataPerChunk) {
      const slice = payload.slice(i, i + dataPerChunk);
      const chunk = [
        0x40,                    // chunk marker
        seqByte,                 // sequence counter
        0x00, 0x00,             // reserved
        0x01, 0x43, 0xBD, 0x0B, // framing signature (verify against Protocol Bible)
        ...slice,
      ];
      chunks.push(chunk);
      seqByte = (seqByte + 1) & 0xFF;
    }

    AppLogger.log('BLE_CHUNKED_WRITE', { 
      payloadLen: payload.length, 
      numChunks: chunks.length 
    });

    for (const chunk of chunks) {
      await writeToDevice(chunk);
      // Small delay between chunks to prevent BLE buffer overflow
      await new Promise(resolve => setTimeout(resolve, 20));
    }
  },
  [writeToDevice]
);
```

**Return this from the hook** alongside `writeToDevice`.

### Step 2: Update `ZenggeProtocol.setCustomModeExtended()` to use `writeChunked`

Currently `setCustomModeExtended()` in `src/protocols/ZenggeProtocol.ts` builds the 323-byte array and returns it as a flat `number[]`. It needs to be changed to accept a `writeChunked` function and send it directly, OR the call site in `useControllerDispatch.ts` needs to pipe the return value through `writeChunked` instead of `writeToDevice`.

**Preferred approach** (keeps ZenggeProtocol pure/testable):
```typescript
// In useControllerDispatch.ts
const payload = ZenggeProtocol.setCustomModeExtended(slots);
if (payload) await writeChunked(payload); // NOT writeToDevice
```

### Step 3: Export `writeChunked` from `useControllerDispatch`

Add `writeChunked` to the return object of `useControllerDispatch` so Scene Builder UI can call it directly.

---

## Files To Touch

| File | Change |
|------|--------|
| `src/hooks/ble/useBLE.ts` | Add `writeChunked()` callback, export from hook return |
| `src/hooks/useControllerDispatch.ts` | Import `writeChunked`, use it for 0x51 extended calls |
| `src/protocols/ZenggeProtocol.ts` | Verify `setCustomModeExtended()` returns correct payload (no side effects) |

---

## Test Criteria

- [ ] `writeChunked` with a 24-byte payload sends 2 chunks of ≤20 bytes each
- [ ] `writeChunked` with a 323-byte payload sends exactly 27 chunks
- [ ] Each chunk starts with `0x40` marker byte
- [ ] Sequence byte increments correctly (00, 01, 02, ...)
- [ ] Hardware (HALOZ) receives and processes the full 0x51 extended payload
- [ ] Oracle Lab: verify with BLE sniffer that chunks arrive in order
- [ ] No regression: existing `writeToDevice` single-write patterns unaffected

---

## Risk Notes

- The 20ms inter-chunk delay is an estimate. Too short = buffer overflow. Too long = slow UI. Tune empirically with hardware.
- The framing signature `[0x01, 0x43, 0xBD, 0x0B]` must be verified from a live HCI sniff of the ZENGGE app sending a scene. If wrong, hardware will reject all chunks silently.
- Do this in PR-B alongside `fix/session-time-sync-0x10` (both touch `useBLE.ts`).
