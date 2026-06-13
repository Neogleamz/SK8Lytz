# Implementation Plan

## Goal
Replace the bespoke `BleStateMachine` class and scattered boolean refs across the BLE engine with a deterministic XState v5 Finite State Machine (FSM) to eliminate race conditions and invalid connection states.

## User Review Required
> [!WARNING]
> **Dependency Budget & React Re-Renders**
> Adding `xstate` and `@xstate/react` costs ~45KB. Because `useMachine()` ties the FSM directly to the React component tree, the entire `useBLE.ts` hook will re-render whenever the machine state or context updates. Since BLE events (like RSSI updates) fire rapidly, we must design the XState context carefully to avoid thrashing the UI thread.
> 
> Are you comfortable proceeding with the global replacement immediately, or should we isolate the heavy data points (like RSSI and HW caches) via standard refs *outside* the XState context?

## Source of Truth
- `src/services/BleStateMachine.ts`:[1-115] — Current bespoke FSM implementation.
- `src/hooks/useBLE.ts`:[115-128] — The `bleGateRef` connection semaphore.
- `src/hooks/useBLE.ts`:[564-569] — Derivation of `bleState` from disparate component states (`bleGateState`, `scannerState`, `connectedDevices.length`).

## Steps

### Step 1 — Dependency Diet
- Action: Install `xstate` and `@xstate/react` via `npm install xstate @xstate/react`.
- Source: Dependency addition rule.
- Verify: `package.json` reflects dependencies and `npm run verify` passes.

### Step 2 — Define XState Schema & Events
- Action: Create `src/services/ble/BleMachine.types.ts`. Define the FSM context (`connectedDevices`, `ghostedDeviceIds`) and events (`SCAN_START`, `SCAN_STOP`, `CONNECT_REQUEST`, `CONNECT_SUCCESS`, `DISCONNECT`, `RECOVERY_START`).
- Source: `useBLE.ts`:[110-120] (current state refs).
- Verify: TSC compiles the strict XState type definitions.

### Step 3 — Build the Global Statechart
- Action: Create `src/services/ble/BleMachine.ts`. Implement the `createMachine` statechart with hierarchical states (`IDLE`, `SCANNING`, `CONNECTING`, `READY`, `DISCONNECTING`, `RECOVERING`).
- Source: `BleStateMachine.ts`:[80-95] (existing valid transitions).
- Verify: All transitions from the legacy class are accounted for in the statechart with proper `entry` and `exit` actions.

### Step 4 — Refactor Orchestrator (`useBLE.ts`)
- Action: Delete `bleGateRef` and bespoke `BleStateMachine` import. Inject `const [state, send] = useMachine(bleMachine)` into `useBLE.ts`. Map the legacy `derivedBleState` export to the new XState hierarchical path.
- Source: `useBLE.ts`:[564-569].
- Verify: Hook exports match the exact `BluetoothLowEnergyApi` contract, preventing consumer components from breaking.

### Step 5 — Refactor Sub-hook Delegation
- Action: Modify `useBLEScanner.ts`, `useBLEAutoRecovery.ts`, and `useBLESweeper.ts` to accept the XState `send` dispatcher rather than managing their own internal string states. They will fire `send({ type: 'SCAN_COMPLETE' })` instead of `setScannerState('IDLE')`.
- Source: `useBLE.ts`:[349-372] (sub-hook instantiations).
- Verify: Sub-hooks compile without internal state duplications.

### Step 6 — Purge Legacy Code
- Action: Delete `src/services/BleStateMachine.ts`. Clean up all `console.warn` invalid transition traps, as XState makes invalid transitions structurally impossible.
- Source: `BleStateMachine.ts`:[1-115].
- Verify: Full test suite (`npm run verify`) and all Jest tests (`src/hooks/__tests__/useBLE.test.ts` equivalents) pass.

## Verification Plan
### Automated Tests
- Run `npm run verify` (TSC, Jest).
- `BleStateMachine.test.ts` will be deleted; XState unit tests will be written for `BleMachine.ts` to assert that `CONNECTING` cannot transition directly to `SCANNING`.

### Manual Verification
- Deploy to Android device via USB (`/deploy-device`).
- Run the setup wizard, connect to a skate, kill the power (triggering auto-recovery), and power it back on. The FSM must predictably step through `READY` -> `RECOVERING` -> `CONNECTING` -> `READY`.

## Out of Scope
- Rewriting the protocol parsers (`ZenggeProtocol.ts`, `BanlanxAdapter.ts`).
- Moving the `BleWriteDispatcher` queue into XState actors (the queue will remain external for performance reasons to avoid React context thrashing on fast writes).
