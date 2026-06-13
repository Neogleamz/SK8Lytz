# PLAN: perf/reconnect-replay-mutex-unblock

## Goal
Prevent the ledger reconnect replay in `DockedController.tsx` from locking the `writeMutex` and blocking user-initiated pattern taps for 300-400ms after device connection.

## Problem
On connect, `DockedController`'s reconnect replay effect (lines 419-433) fires `parentWriteToDevice` directly. `parentWriteToDevice` is `useBLE.writeToDevice` which routes through the `writeMutex` chain in `useBLE.ts`. For a 2-device group, the replay staggers at `300ms + 100ms = 400ms` — the mutex is locked for that entire window. Any pattern tap the user fires in that window queues behind the replay. This makes the controller feel unresponsive immediately after connecting.

## Root Cause
- Replay uses `parentWriteToDevice` (the raw useBLE write) which hits the shared `writeMutex`
- Mutex is a module-level chain — FIFO, no priority lanes
- User tap arrives → queues behind replay write → apparent 300-400ms tap lag on fresh connect

## Target Files
- `src/components/DockedController.tsx` (replay effect, lines 419-433)
- `src/hooks/useBLE.ts` (mutex, `_executeWriteToDevice`, `writeGeneration`)

## Execution Checklist

### Step 1: Add a `lowPriority` flag to `writeToDevice` signature
```ts
// useBLE.ts — writeToDevice signature
const writeToDevice = async (
  payload: number[],
  targetDeviceId?: string,
  opts?: { lowPriority?: boolean }
): Promise<boolean | 'partial'>
```
A `lowPriority: true` write is treated as a pattern write (gets generation tracking) but does NOT increment `writeGeneration`. This means if a user-initiated write arrives during the replay debounce window, the replay gets silently dropped by the stale-generation check — exactly what we want.

### Step 2: Replay effect passes `lowPriority: true`
```ts
parentWriteToDevice(ledgerState.rawPayload, d.id, { lowPriority: true }).catch(() => {});
```

### Step 3: Inside `writeToDevice` — low priority writes use current generation, don't increment
```ts
const thisGeneration = opts?.lowPriority ? writeGeneration : ++writeGeneration;
```

### Step 4: Run TSC verification from master

## Rollback
Remove `opts` parameter from signature, revert replay call site. Zero functional change.

## Collateral Damage Locks
- DO NOT touch the reconnect replay timeout values (300ms + 100ms stagger) — they are correct for GATT contention avoidance
- DO NOT touch `writeMutex` itself — the chain pattern is correct
- ONLY modify the generation-increment logic and the replay call site
