# [PLAN] refactor/ble-notification-mailroom

## Design Decisions & Rationale
The BLE notification handler is currently an un-gated firehose that processes raw bytes inline causing severe performance degradation. Every BLE notification currently triggers redundant parsing, `O(N)` array searches, identical state overrides, and unfiltered Supabase telemetry ingestion. 

To resolve this, we will decompose it into a "Mailroom" pattern with a strict gatekeeper. 

1. **The Diag-Gate**: Sniffer UI state and Supabase diagnostics upload will be gated behind a new toggle (e.g., `isDiagnosticsMode` from Context or Zustand). Under normal operation, these heavy telemetry drops will be bypassed.
2. **Delta Check (De-Duplication)**: The State/AsyncStorage writer will only execute if the hardware configuration (points, sorting) actually changed, eliminating excessive disk writes.
3. **Pure Utility Parser**: The duplicate parsing logic will be extracted out of the hook into `src/utils/BlePayloadParser.ts`, returning typed payloads that the hook simply routes.
4. **Debouncer**: Rapid-fire identical packets will be dropped at the top of the function to prevent UI thread freezing.

---

## Scope

**Target file(s):** 
- `src/hooks/useHardwareNotifications.ts`
- `src/utils/BlePayloadParser.ts` (NEW)

### Proposed Architecture

```typescript
onBLENotification(rawBytes)
  │
  ├─ [Gatekeeper] Throttle identical back-to-back packets
  │
  ├─ if (isDiagnosticsMode)
  │   ├─ 1. stampRawNotification(rawBytes)
  │   └─ 2. uploadDiagnosticsTelemetry(rawBytes)
  │
  ├─ 3. ledConfig = parseLedPayload(rawBytes) // pure function call
  │
  └─ if (ledConfig && hasConfigChanged(ledConfig, prevConfig))
      └─ 4. writeLedConfigToState(ledConfig) 
```

---

## Proposed Changes

### [MODIFY] `src/hooks/useHardwareNotifications.ts`
- Inject a diagnostics-mode flag check to `useHardwareNotifications`.
- Replace duplicate parsing logic with a single call to `BlePayloadParser`.
- Pass a cached name/ID map down to avoid `O(N)` `allDevices.find()` lookups on every ping.
- Add a strict Delta Check comparing incoming parsed `points` and `colorSorting` against `deviceConfigs[deviceId]` before initiating React State `setDeviceConfigs` and `AsyncStorage` rewrites.

### [NEW] `src/utils/BlePayloadParser.ts`
- Extract hardware parsing logic into a stateless `parseLedPayload(rawBytes: Uint8Array): LedConfig | null` function.
- Add robust error handling so it never throws if the packet is malformed.

---

## Verification Plan

### Manual Verification
- **During normal usage:** Connect skates. Ensure sniffer array does not increase, and Supabase does not record a flood of diagnostics.
- **Enable Diagnostics:** Toggle diagnostic mode on to ensure Sniffer and Supabase receive telemetry.
- **Delta Check:** Ensure `AsyncStorage.setItem` is only called ONCE upon connection rather than 5+ times.
