# [PLAN] feat/ble-hardware-watchdog — The Self-Healing Standard

### Design Decisions & Rationale

Bluetooth LE hardware is prone to "Silent Failures" where the GATT connection reports as alive (`isDeviceConnected() = true`) but the underlying Zengge microcontroller has soft-locked and stopped responding to writes. We are implementing an **Autonomous Hardware Watchdog** embedded directly inside `useBLE.ts` that monitors each device's liveness using `0x63` heartbeat pings, and performs serial silent re-latches to restore control — all without user interaction or UI disruption.

**Key architectural decisions hammered by the Devil's Advocate Gate:**

1. **Mutex gating:** The watchdog MUST acquire the same exclusive lock as `writeToDevice` to prevent collisions with the optimistic write pipeline.
2. **FIFO serial recovery queue:** ALL reconnects are sequential (one device at a time, 600ms cooldown) to comply with the Master Reference's GATT 133 prevention mandate.
3. **Heartbeat-based soft-lock detection:** `bleManager.isDeviceConnected()` is insufficient — we must probe each device with a real 0x63 write and wait for a FF02 notification response using the existing mutex-safe write path.
4. **Lockout flag:** A `isWatchdogRecovering` ref prevents the watcher interval from spawning overlapping recovery cycles.

---

## Proposed Changes

### Component: `useBLE.ts` — Watchdog Engine

#### [MODIFY] [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)

This is where all watchdog logic lives, per the **BLE Co-location Constraint** (Master Reference §4):

> "BLE state MUST remain co-located in `DashboardScreen.tsx`... All domain hooks receive BLE context via prop injection only."

Since BLE _logic_ lives in `useBLE.ts`, the watchdog also lives here. It does NOT get its own service file — that would violate the co-location constraint.

**New refs to add (near the top of the hook, alongside existing refs):**
```ts
// Watchdog: tracks whether any recovery cycle is in-flight to prevent overlapping attempts
const isWatchdogRecovering = useRef<boolean>(false);
// Watchdog: consecutive miss counter per device { [deviceId]: number }
const watchdogMissCountRef = useRef<Record<string, number>>({});
// Watchdog: interval handle for cleanup
const watchdogIntervalRef = useRef<ReturnType<typeof setInterval> | null>(null);
```

**New `pingDeviceForLiveness` function (private, ~30 lines):**

This is a lightweight wrapper around the existing `probeDevice` concept — it writes a `0x63` query and waits up to 2500ms for a FF02 response. Returns `true` if alive, `false` if the device is dark/soft-locked.

```ts
/**
 * pingDeviceForLiveness — attempt a 0x63 hardware query heartbeat.
 * Returns true if the device responds, false if it times out (soft-locked).
 * Does NOT disconnect — uses the existing open GATT connection.
 */
const pingDeviceForLiveness = async (deviceId: string): Promise<boolean> => {
  if (Platform.OS === 'web' || !bleManager) return true; // web always "alive"
  try {
    const result = await new Promise<boolean>((resolve) => {
      const timeout = setTimeout(() => { sub.remove(); resolve(false); }, 2500);
      const sub = bleManager.monitorCharacteristicForDevice(
        deviceId, ZENGGE_SERVICE_UUID, ZENGGE_NOTIFY_UUID,
        (err: any, char: any) => {
          if (err || !char?.value) return;
          try {
            const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
            // 0x63 response starts with 0x63 at byte 0 (or wrapped at inner payload offset)
            if (raw[0] === 0x63 || raw[8] === 0x63) {
              clearTimeout(timeout); sub.remove(); resolve(true);
            }
          } catch { /* ignore parse errors */ }
        }
      );
      // Fire the 0x63 query on the existing open connection
      const qp = ZenggeProtocol.queryHardwareSettings(false);
      bleManager.writeCharacteristicWithoutResponseForDevice(
        deviceId, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID,
        Buffer.from(qp).toString('base64')
      ).catch(() => {});
    });
    return result;
  } catch {
    return false;
  }
};
```

**New `silentRelatch` function (private, ~40 lines):**

Performs a strict sequential teardown → reconnect → re-subscribe sequence for a single device. Mirrors the existing `connectToDevice` path exactly to ensure characteristic monitoring and dropout listeners are properly re-established.

```ts
/**
 * silentRelatch — gracefully cycle a soft-locked device's GATT connection.
 * 1. Cancel the current connection (sequential teardown per GATT 133 mandate).
 * 2. Wait 250ms OS buffer.
 * 3. Reconnect with 5000ms timeout.
 * 4. Re-discover services + re-start FF02 monitor.
 * 5. Re-send 0x63 hw query to restore hardware state awareness.
 */
const silentRelatch = async (device: Device): Promise<boolean> => {
  try {
    AppLogger.log('WATCHDOG_RELATCH', { deviceId: device.id, name: device.name, action: 'begin' });
    
    // Step 1: Clean teardown
    if (disconnectListeners.current[device.id]) {
      disconnectListeners.current[device.id].remove();
      delete disconnectListeners.current[device.id];
    }
    await bleManager.cancelDeviceConnection(device.id).catch(() => {});
    await new Promise(r => setTimeout(r, 250)); // OS GATT stack buffer

    // Step 2: Fresh connect
    const conn = await bleManager.connectToDevice(device.id, { timeout: 5000 });
    await conn.discoverAllServicesAndCharacteristics();

    // Step 3: Re-register dropout listener
    disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
      AppLogger.log('DEVICE_DISCONNECTED', { id: conn.id, reason: 'dropout_post_relatch', error: error?.message });
      setDroppedOutDeviceIds(prev => [...prev, conn.id]);
      setConnectedDevices(prev => prev.filter(c => c.id !== conn.id));
      if (disconnectListeners.current[conn.id]) {
        disconnectListeners.current[conn.id].remove();
        delete disconnectListeners.current[conn.id];
      }
    });

    // Step 4: Re-subscribe to FF02 notifications
    conn.monitorCharacteristicForService(
      ZENGGE_SERVICE_UUID, ZENGGE_NOTIFY_UUID,
      (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
    );

    // Step 5: Restore hardware awareness
    await new Promise(r => setTimeout(r, 600));
    const qp = ZenggeProtocol.queryHardwareSettings(false);
    await conn.writeCharacteristicWithoutResponseForService(
      ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, Buffer.from(qp).toString('base64')
    ).catch(() => {});

    // Update connectedDevices so the UI + writeToDevice see the fresh connection object
    setConnectedDevices(prev => prev.map(d => d.id === device.id ? conn : d));
    
    AppLogger.log('WATCHDOG_RELATCH', { deviceId: device.id, action: 'success' });
    return true;
  } catch (e: any) {
    AppLogger.warn(`[Watchdog] Relatch failed for ${device.id}`, e?.message);
    AppLogger.log('WATCHDOG_RELATCH', { deviceId: device.id, action: 'failed', error: e?.message });
    return false;
  }
};
```

**New `startWatchdog` / `stopWatchdog` functions:**

```ts
/**
 * startWatchdog — begins the 30s heartbeat polling cycle.
 * Call this AFTER connection is established.
 * Guards with isWatchdogRecovering to prevent overlapping cycles.
 */
const startWatchdog = () => {
  if (watchdogIntervalRef.current) clearInterval(watchdogIntervalRef.current);
  watchdogMissCountRef.current = {};
  watchdogIntervalRef.current = setInterval(async () => {
    if (isWatchdogRecovering.current) return; // guard: skip if recovery in flight
    const devices = connectedDevicesRef.current;
    if (devices.length === 0 || Platform.OS === 'web') return;

    isWatchdogRecovering.current = true;
    try {
      // SERIAL queue — one device at a time
      for (const device of devices) {
        const isAlive = await pingDeviceForLiveness(device.id);
        if (isAlive) {
          watchdogMissCountRef.current[device.id] = 0; // reset miss counter
        } else {
          const misses = (watchdogMissCountRef.current[device.id] ?? 0) + 1;
          watchdogMissCountRef.current[device.id] = misses;
          AppLogger.warn(`[Watchdog] Device ${device.id} missed heartbeat (${misses}/2)`);
          AppLogger.log('WATCHDOG_MISS', { deviceId: device.id, misses });

          if (misses >= 2) {
            // Confirmed soft-lock — initiate relatch
            watchdogMissCountRef.current[device.id] = 0; // reset before attempt
            await silentRelatch(device);
            await new Promise(r => setTimeout(r, 600)); // cooldown between devices
          }
        }
      }
    } finally {
      isWatchdogRecovering.current = false;
    }
  }, 30_000); // 30-second heartbeat interval
};

const stopWatchdog = () => {
  if (watchdogIntervalRef.current) {
    clearInterval(watchdogIntervalRef.current);
    watchdogIntervalRef.current = null;
  }
  watchdogMissCountRef.current = {};
  isWatchdogRecovering.current = false;
};
```

**Wire-up changes:**
- Call `startWatchdog()` at the END of `connectToDevice()` (single) and `connectToDevices()` (group), after the connection is fully established.
- Call `stopWatchdog()` at the START of `disconnectFromDevice()`.
- Add a `useEffect` cleanup to call `stopWatchdog()` on hook unmount to prevent memory leaks.
- Export `isWatchdogActive: boolean` (derived from `watchdogIntervalRef.current !== null`) via the return object so the Admin Hub TIMELINE can surface it.

**Boy Scout Cleanup (while in this file):**
- Remove the stale `console.log('BLE State Change:', state)` debug log at line 146 — replace with `AppLogger.log('BLE_STATE_CHANGE', { state })` for structured telemetry.

---

### Component: `BluetoothLowEnergyApi` Interface

**Update the return type interface at lines 39–61 to include:**
```ts
isWatchdogActive: boolean;
```

---

### Component: Admin Hub Telemetry Visibility

#### [MODIFY] [AdminToolsModal.tsx or useAdminTelemetry.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAdminTelemetry.ts)

The `WATCHDOG_RELATCH` and `WATCHDOG_MISS` log events will automatically flow into the TIMELINE tab via `AppLogger` — **no code changes needed**. However, we should verify that `WATCHDOG_RELATCH` and `WATCHDOG_MISS` are recognized event types in `AppLogger.ts`.

#### [MODIFY] [AppLogger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)

- Add `WATCHDOG_MISS` and `WATCHDOG_RELATCH` as recognized telemetry event type constants if they are typed/enum'd in that file.

---

### Component: Master Reference Sync

#### [MODIFY] [SK8Lytz_App_Master_Reference.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md)

- Add a new sub-section under Section 3 (BLE Protocol Library) documenting the Watchdog architecture:
  - Heartbeat interval: `30 seconds`
  - Miss threshold before relatch: `2 consecutive misses`
  - Relatch sequence: teardown → 250ms buffer → reconnect → re-discover → re-monitor → 0x63 query
  - Cooldown between device recoveries: `600ms`
  - Event types: `WATCHDOG_MISS`, `WATCHDOG_RELATCH`

---

## UI Craftsmanship (Modern UI/UX Standard)

The watchdog is intentionally **invisible to the user** during normal operation. When a silent relatch succeeds, the user experiences zero disruption — the skate simply keeps working.

**The only visible state change** is a subtle indicator in the `DockedController` status dot (if connected devices update during a relatch). The existing optimistic write FSM's `RECONCILED` state already handles any transient write failures that might occur during the ~1-2 second reconnect window.

**Admin Hub TIMELINE tab** will naturally surface `WATCHDOG_MISS` and `WATCHDOG_RELATCH` events for diagnostics, no additional UI work needed.

---

## Verification Plan

### Automated / Semi-Automated
1. Build a debug mode toggle in Admin Tools that **reduces the watchdog interval from 30s → 5s** so we can observe the cycle without waiting.
2. `npx tsc --noEmit` — must exit 0 after changes.

### Manual Hardware Test
1. **Soft-Lock Simulation**: Power-cycle the Zengge controller while keeping the GATT connection alive (remove and insert battery while app maintains connection). The app should log 2x `WATCHDOG_MISS` events, then `WATCHDOG_RELATCH: begin` → `WATCHDOG_RELATCH: success` within ~60s, and restore write control without user input.
2. **Multi-Device Stress Test**: Connect 2 devices. Induce soft-lock on Device 1. Verify Device 2 continues to receive writes during Device 1's relatch process (confirming serial isolation).
3. **Admin TIMELINE Verification**: Open Admin Hub → TIMELINE tab. Confirm `WATCHDOG_MISS` and `WATCHDOG_RELATCH` events appear with correct device IDs.
4. **Memory Leak Check**: Connect, let watchdog run for 2 minutes, disconnect. Reconnect. Confirm only ONE watchdog interval is running (no double-starts).
