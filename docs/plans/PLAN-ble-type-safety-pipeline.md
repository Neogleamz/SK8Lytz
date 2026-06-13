# Implementation Plan: `ble/type-safety-pipeline`

**Branch:** `ble-type-safety-pipeline`
**Worktree:** `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\ble-type-safety-pipeline`
**Batch:** `[BATCH:ble-p0-critical]` — Task 1 of 4

---

## Goal

Eliminate all `any` types from the BLE connection pipeline without refactoring any logic.
The primary win: TypeScript will catch a wrong parameter type or swap at compile time instead of silently failing at runtime on hardware.

> [!IMPORTANT]
> **Surgical Strike Protocol**: This plan targets **types only**. Zero logic changes. The diff should be purely type annotations — no moved code, no new control flow, no restructured functions.

---

## Scope of Changes

### Files touched (5 files + 1 new):

| File | Change |
|------|--------|
| `src/types/ble.types.ts` | **[NEW]** — Shared BLE type definitions |
| `src/services/BleConnectionManager.ts` | Signature: replace 13 positional `any` params with `BleConnectionRequest` |
| `src/services/BleSessionFactory.ts` | Type `conn: any` → `conn: Device \| null`, local `any[]` → typed |
| `src/hooks/ble/useBLEAutoRecovery.ts` | `bleManager: any` → `bleManager: BleManager` |
| `src/hooks/ble/useBLESweeper.ts` | `bleManager: any` → `bleManager: BleManager` |
| `src/services/useDashboardAutoConnect.ts` | `groupsToProcess: any[]` → typed local interface |

---

## Step 1 — Create `src/types/ble.types.ts` [NEW]

This file centralizes BLE type definitions so all pipeline files import from one place.

```typescript
/**
 * ble.types.ts — Shared BLE pipeline type definitions.
 *
 * Centralizes all BleManager, Device, and connection-related types so the
 * BLE pipeline can be fully typed without any `any` casts.
 *
 * Re-exports the official react-native-ble-plx types alongside our own
 * domain-specific interfaces.
 */
import type React from 'react';
import type { BleManager, Device, Subscription, Characteristic } from 'react-native-ble-plx';
import type { BleStateMachine, BLEPhaseTag } from '../services/BleStateMachine';
import type { IControllerProtocol } from '../protocols/IControllerProtocol';

// Re-export library types so consumers import from one place
export type { BleManager, Device, Subscription, Characteristic };

/** Minimal shape for a group from registered_groups Supabase table */
export interface RegisteredGroup {
  id: string;
  group_name: string;
  created_at: string;
  user_id?: string;
}

/** Sweeper/scanner interface duck-type (only methods used by BleConnectionManager) */
export interface BleSweeperHandle {
  isSweeperActive: boolean;
  stopSweeper(): void;
  startSweeper(): void;
}

export interface BleScannerHandle {
  stopScanner(): void;
}

export interface BleAutoRecoveryHandle {
  cancelAllRecoveries(): Promise<void>;
}

/**
 * BleConnectionRequest — replaces the 13 positional `any` parameters
 * of executeConnectToDevices with a single typed options object.
 *
 * Groups logically related refs/callbacks so call sites are self-documenting.
 */
export interface BleConnectionRequest {
  /** Devices to connect (from allDevices scan list) */
  devices: Device[];
  /** The react-native-ble-plx BleManager singleton */
  bleManager: BleManager;
  /** Ref holding currently connected Device objects */
  connectedDevicesRef: React.MutableRefObject<Device[]>;
  /** MACs (uppercase) that are hardware-blacklisted */
  blacklistedMacsRef: React.MutableRefObject<string[]>;
  /** Keepalive timer ref — cancelled on reconnect */
  keepaliveTimerRef: React.MutableRefObject<ReturnType<typeof setTimeout> | null>;
  /** Per-device disconnect subscription map */
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>;
  /** Sweeper handle for stop/start lifecycle */
  sweeper: BleSweeperHandle;
  /** Scanner handle for stop lifecycle */
  scanner: BleScannerHandle;
  /** Auto-recovery handle (unused in connect path, kept for symmetry with cancel) */
  autoRecovery: BleAutoRecoveryHandle;
  /** Global BLE connection gate semaphore */
  bleGateRef: React.MutableRefObject<BleStateMachine>;
  /** Per-device negotiated MTU map */
  mtuMapRef: React.MutableRefObject<Map<string, number>>;
  /** Per-device resolved protocol adapter map */
  adapterMapRef: React.MutableRefObject<Map<string, IControllerProtocol>>;
  /** Raw notification callback (passed to monitor) */
  dataReceivedCallbackRef: React.MutableRefObject<((deviceId: string, data: number[]) => void) | undefined>;
  /** Notification handler for GATT characteristic changes */
  handleNotificationRef: React.MutableRefObject<(error: Error | null, characteristic: Characteristic | null, deviceId: string) => void>;
  /** Called on organic (unexpected) disconnect */
  handleOrganicDisconnect: (error: Error | null, deviceId: string) => void;
  /** React state setter for connected devices */
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  /** Gate phase setter */
  setGate: (phase: BLEPhaseTag) => void;
}
```

---

## Step 2 — Update `BleConnectionManager.ts`

**Target lines:** L15–33 (function signature only)

Replace the 13 positional parameters with a single destructured `BleConnectionRequest`:

```typescript
// BEFORE (lines 15-33):
export async function executeConnectToDevices(
  devices: any[],
  bleManager: any,
  connectedDevicesRef: React.MutableRefObject<any[]>,
  blacklistedMacsRef: React.MutableRefObject<string[]>,
  keepaliveTimerRef: React.MutableRefObject<ReturnType<typeof setTimeout> | null>,
  disconnectListeners: React.MutableRefObject<Record<string, any>>,
  sweeper: any,
  scanner: any,
  autoRecovery: any,
  bleGateRef: React.MutableRefObject<BleStateMachine>,
  mtuMapRef: React.MutableRefObject<Map<string, number>>,
  adapterMapRef: React.MutableRefObject<Map<string, any>>,
  dataReceivedCallbackRef: React.MutableRefObject<((deviceId: string, data: number[]) => void) | undefined>,
  handleNotificationRef: React.MutableRefObject<(error: any, characteristic: any, deviceId: string) => void>,
  handleOrganicDisconnect: (error: any, deviceId: string) => void,
  setConnectedDevices: React.Dispatch<React.SetStateAction<any[]>>,
  setGate: (phase: BLEPhaseTag) => void
): Promise<void>

// AFTER:
import type { BleConnectionRequest } from '../types/ble.types';

export async function executeConnectToDevices({
  devices,
  bleManager,
  connectedDevicesRef,
  blacklistedMacsRef,
  keepaliveTimerRef,
  disconnectListeners,
  sweeper,
  scanner,
  autoRecovery,
  bleGateRef,
  mtuMapRef,
  adapterMapRef,
  dataReceivedCallbackRef,
  handleNotificationRef,
  handleOrganicDisconnect,
  setConnectedDevices,
  setGate,
}: BleConnectionRequest): Promise<void>
```

**Internal locals** in the function body also need minor type fixes:
- `const rawConns: any[]` → `const rawConns: Device[]`
- `let conn: any = null` → `let conn: Device | null = null`
- `let lastErr: any = null` → `let lastErr: Error | null = null`
- `disconnectListeners: React.MutableRefObject<Record<string, any>>` in body references are already covered by the param type

> [!IMPORTANT]
> After changing to destructured object, ALL call sites of `executeConnectToDevices` in `useBLE.ts` must be updated to pass a single object. Read the call sites before touching the function.

---

## Step 3 — Update call sites in `useBLE.ts`

Need to find and read the call site(s) before updating. Expected pattern:

```typescript
// BEFORE:
await executeConnectToDevices(
  devices,
  bleManager,
  connectedDevicesRef,
  ... // 13 args
);

// AFTER:
await executeConnectToDevices({
  devices,
  bleManager,
  connectedDevicesRef,
  blacklistedMacsRef,
  keepaliveTimerRef,
  disconnectListeners,
  sweeper,
  scanner,
  autoRecovery,
  bleGateRef,
  mtuMapRef,
  adapterMapRef,
  dataReceivedCallbackRef,
  handleNotificationRef,
  handleOrganicDisconnect,
  setConnectedDevices,
  setGate,
});
```

---

## Step 4 — Update `BleSessionFactory.ts`

**Target:** `bleManager: any` param (L70), `conn: any` locals (L83, L101, L104, etc.)

```typescript
// BEFORE (L69-70):
export async function createGattSession(
  bleManager: any,

// AFTER:
import type { BleManager, Device } from 'react-native-ble-plx';

export async function createGattSession(
  bleManager: BleManager,
```

Local `conn` variables: `let conn: any = null` → `let conn: Device | null = null`
Also type `const svcs` and `svcUUIDs` inline where `any` appears.

---

## Step 5 — Update `useBLEAutoRecovery.ts`

**Target:** L23 in `UseBLEAutoRecoveryProps`

```typescript
// BEFORE:
bleManager: any;

// AFTER:
import type { BleManager } from 'react-native-ble-plx';
bleManager: BleManager;
```

Also type the `handleNotification` callback params:
```typescript
// BEFORE:
handleNotification: (error: any, characteristic: any, deviceId: string) => void;

// AFTER:
import type { Characteristic } from 'react-native-ble-plx';
handleNotification: (error: Error | null, characteristic: Characteristic | null, deviceId: string) => void;
```

---

## Step 6 — Update `useBLESweeper.ts`

**Target:** L61 in `UseBLESweeperProps`

```typescript
// BEFORE:
bleManager: any;

// AFTER:
import type { BleManager } from 'react-native-ble-plx';
bleManager: BleManager;
```

---

## Step 7 — Update `useDashboardAutoConnect.ts`

Replace untyped `groupsToProcess: any[]` and `groups: any[]` with the `RegisteredGroup` type from `ble.types.ts`:

```typescript
// BEFORE (L191):
let groupsToProcess: any[] = [];
// L206:
let groups: any[] | null = null;

// AFTER:
import type { RegisteredGroup } from '../types/ble.types';
let groupsToProcess: RegisteredGroup[] = [];
let groups: RegisteredGroup[] | null = null;
```

---

## Verification Plan

### Automated
```powershell
# Run from worktree root
npm run verify
```
TSC must pass with zero errors on the changed files.

### Manual
- Confirm `executeConnectToDevices` call sites in `useBLE.ts` pass the object form
- Confirm no `any` remains in the 5 target files (grep check)

### Layer Gate
`[CORE]` layer — `npm run verify` (TypeScript + Jest) is sufficient. No physical device test needed for type-only changes.
