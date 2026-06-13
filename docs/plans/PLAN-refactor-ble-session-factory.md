# Implementation Plan: refactor/ble-session-factory

## Goal
Extract the duplicated "connect → discoverAllServicesAndCharacteristics → resolve adapter (cache-aware) → cache adapter" sequence into a single `BleSessionFactory.createGattSession()` utility. This eliminates the Shotgun Surgery code smell that caused the same GATT discovery bug to appear in 3 of 4 callsites.

## Background (Cited Truth)
The connect + discover + resolve sequence is currently duplicated in:

| Function | File | Lines |
|:---------|:-----|:------|
| `handshakeDevice()` | `src/hooks/useBLE.ts` | L670-705 |
| `pingDevice()` | `src/hooks/useBLE.ts` | L300-328 |
| `interrogateDevice()` | `src/hooks/ble/useBLESweeper.ts` | L218-268 |
| `initiateRecovery()` | `src/hooks/ble/useBLEAutoRecovery.ts` | L165-208 |

Each copy has ~40 lines of nearly identical logic with subtle divergences that cause bugs.

## Proposed Changes

### [NEW] `src/services/BleSessionFactory.ts`

A pure module-level utility (NOT a React hook) that encapsulates the connection invariant:

```typescript
interface GattSessionResult {
  conn: Device;          // The connected device handle
  adapter: IControllerProtocol;  // Resolved protocol adapter
  usedCache: boolean;    // Whether the adapter was from cache
}

interface CreateSessionOptions {
  timeout?: number;       // Connection timeout (default: 6000ms)
  retries?: number;       // Connection retry count (default: 2)
  signal?: AbortSignal;   // P1 preemption signal from GattMutex
}

export async function createGattSession(
  bleManager: BleManager,
  mac: string,
  options?: CreateSessionOptions
): Promise<GattSessionResult>;
```

**Key invariant enforced:** `discoverAllServicesAndCharacteristics()` ALWAYS runs before adapter resolution, regardless of cache state. This is not optional, not configurable, not bypassable.

### [MODIFY] `src/hooks/useBLE.ts`
- `handshakeDevice()`: Replace ~40 lines of connect+discover+resolve with `const { conn, adapter } = await createGattSession(bleManager, device.id);`
- `pingDevice()`: Same replacement.

### [MODIFY] `src/hooks/ble/useBLESweeper.ts`
- `interrogateDevice()`: Replace ~50 lines with `createGattSession()` call, passing the AbortSignal from GattMutex.

### [MODIFY] `src/hooks/ble/useBLEAutoRecovery.ts`
- `initiateRecovery()`: Replace ~45 lines with `createGattSession()` call, passing AbortSignal.

## Design Decisions

1. **Module-level, not a hook**: Same pattern as `useBLEGattMutex.ts` — a pure async function that can be called from any context without React hook ordering constraints.
2. **AbortSignal passthrough**: Each async step checks `signal.aborted` to support the existing P1 preemption mechanism.
3. **No MTU negotiation inside**: MTU is caller-specific (pingDevice doesn't negotiate, handshakeDevice does). Keep it outside the factory.
4. **No notification setup inside**: Notification monitoring is caller-specific. Factory only handles the universal "connect + discover + resolve" contract.

## Estimated Impact
- **Net code reduction**: ~120 lines removed across 4 files, replaced by ~60 lines in one file.
- **Bug prevention**: Any future GATT invariant change (e.g., adding connection priority) only needs to change ONE function.

## Verification Plan

### Automated Tests
- `npm run verify` — TypeScript + Jest suite.
- Add unit test for `createGattSession` with mocked BleManager (verify discovery always called).

### Manual Verification
- Deploy to physical hardware.
- Test all 4 paths: group connect, wizard ping, sweeper interrogation, and auto-recovery reconnect.
- Verify `gatt_cache_hit` logs still appear (cache is working) AND device identification succeeds (discovery ran).
