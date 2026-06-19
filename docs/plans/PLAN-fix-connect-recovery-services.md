# Implementation Plan — Fix ConnectService & RecoveryService Internals

> **Slug:** `fix-connect-recovery-services`
> **Batch:** connection-pipeline-fix
> **Wave:** 1
> **Risk:** H-RISK
> **Size:** Meal
> **Cognitive Load:** High

---

## Problem Statement

`ConnectService.ts` and `RecoveryService.ts` contain resource leaks, dead recovery paths, and defensive gaps that cause duplicate callbacks, wasted recovery time, and silent failures on Android 12+.

### Audit Findings Covered

| ID | Severity | Summary | File | Lines |
|----|----------|---------|------|-------|
| **H2** | HIGH | `monitorCharacteristicForService` subscription is never stored or cleaned — duplicates on reconnect | [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L230-L234) | 230–234 |
| **H3** | HIGH | RecoveryService Phase 3 is dead code — `getSweepedDevice` never injected by BleMachine | [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L38) + [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L244-L256) | RS:38,169 / BM:244–256 |
| **H7** | HIGH | While-loop guard uses `PHASE_2_MAX (5)` — Phase 1's 12-attempt cap is dead | [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L69) | 69 |
| **M5** | MEDIUM | `isDeviceConnected` + `connectedDevices([])` race on Android 12+ | [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L134-L147) | 134–147 |
| **M7** | MEDIUM | Empty catch in MTU retry — silent failure | [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L204) | 204 |

---

## Files to Create/Modify

| File | Action | Reason |
|------|--------|--------|
| `src/services/ble/ConnectService.ts` | MODIFY | Fix H2 (orphaned subscription), M5 (Android race), M7 (empty catch) |
| `src/services/ble/RecoveryService.ts` | MODIFY | Fix H7 (while-loop guard), H3 (wire `getSweepedDevice`) |
| `src/services/ble/BleMachine.ts` | MODIFY | Pass `getSweepedDevice` in recoveryService invoke input (H3) |

---

## Step-by-Step Fix

### Step 1: Store `monitorCharacteristicForService` subscription for cleanup (H2)

**Source:** [ConnectService.ts L230–234](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L230-L234) — `conn.monitorCharacteristicForService(...)` is called but its return value (a `Subscription`) is discarded. The disconnect listener at [L223](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L223) properly cleans up via `disconnectListeners.current[conn.id].remove()`, but the notification monitor has no equivalent cleanup.

**Change:**
1. Add a new `notificationListeners` ref map to `ConnectServiceInput`:
   ```typescript
   notificationListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
   ```
2. Before registering the new monitor (L230), clean up any existing subscription:
   ```typescript
   // Clean up stale notification subscription before re-registering
   if (notificationListeners.current[conn.id]) {
     notificationListeners.current[conn.id].remove();
     delete notificationListeners.current[conn.id];
   }
   notificationListeners.current[conn.id] = conn.monitorCharacteristicForService(
     adapter.serviceUUID,
     adapter.notifyCharacteristicUUID,
     (error, characteristic) => handleNotification(error, characteristic, conn.id)
   );
   ```
3. Apply the same pattern in RecoveryService at [L130–135](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L130-L135) and [L208–213](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L208-L213).

**Verify:** After N recovery cycles, exactly 1 notification subscription exists per device (not N+1).

---

### Step 2: Add `AppLogger.warn` to empty MTU catch block (M7)

**Source:** [ConnectService.ts L204](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L204) — The `catch` block after `conn.requestMTU(512)` is completely empty.

**Change:**
```diff
-          } catch {
+          } catch (mtuErr: unknown) {
+            AppLogger.warn('[ConnectService] MTU negotiation attempt failed', {
+              error: mtuErr instanceof Error ? mtuErr.message : String(mtuErr),
+              deviceId: scrubPII(conn.id),
+              attempt: mtuAttempt,
+              payload_size: 0,
+              ssi: 0,
+            });
```

**Verify:** MTU retry failures now appear in telemetry logs.

---

### Step 3: Add defensive retry around `connectedDevices([])` for Android 12+ race (M5)

**Source:** [ConnectService.ts L134–147](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L134-L147) — When `isDeviceConnected(mac)` returns `true`, `bleManager.connectedDevices([])` is called to retrieve the `Device` object. On Android 12+, there's a known race where the BLE stack reports `isDeviceConnected = true` but `connectedDevices([])` doesn't yet include the device in its list. The code falls through to `connectToDevice()` which throws "already connected."

**Change:** Add a retry with a 200ms settle delay when the race condition is detected:
```typescript
if (isConnected) {
  let devicesList = await bleManager.connectedDevices([]).catch(() => []);
  conn = devicesList.find(d => d.id === mac) || null;

  // M5 FIX: Android 12+ race — isDeviceConnected=true but connectedDevices list hasn't caught up.
  // Retry once after a short settle if the device isn't found.
  if (!conn) {
    await new Promise(r => setTimeout(r, 200));
    devicesList = await bleManager.connectedDevices([]).catch(() => []);
    conn = devicesList.find(d => d.id === mac) || null;
  }
} else {
  conn = null;
}
```

**Verify:** On Android 12+, pre-connected devices are correctly retrieved without triggering an "already connected" error.

---

### Step 4: Fix RecoveryService while-loop guard to respect Phase 1 attempt cap (H7)

**Source:** [RecoveryService.ts L69](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L69) — The while-loop condition is:
```typescript
while (!cancelled && attempts <= BLE_TIMING.RECOVERY_PHASE_2_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts))
```
`RECOVERY_PHASE_2_MAX_ATTEMPTS` is 5 ([bleTimingConstants.ts L167](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/bleTimingConstants.ts#L167)). `RECOVERY_PHASE_1_MAX_ATTEMPTS` is 12 ([L161](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/bleTimingConstants.ts#L161)). The while-loop exits at attempt 6 regardless of Phase 1's 12-attempt budget — Phase 1 never gets its full retry allowance.

**Change:** Fix the while-loop guard to use the correct total max:
```diff
-    while (!cancelled && attempts <= BLE_TIMING.RECOVERY_PHASE_2_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts)) {
+    const TOTAL_MAX_ATTEMPTS = BLE_TIMING.RECOVERY_PHASE_1_MAX_ATTEMPTS + BLE_TIMING.RECOVERY_PHASE_2_MAX_ATTEMPTS;
+    while (!cancelled && attempts < TOTAL_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts)) {
```

Also update `MAX_RECOVERY_ATTEMPTS` at [L13](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L13) to match:
```diff
-export const MAX_RECOVERY_ATTEMPTS = 5;
+export const MAX_RECOVERY_ATTEMPTS = 17; // Phase 1 (12) + Phase 2 (5)
```

Or better — derive it dynamically from the constants to avoid future drift:
```typescript
// Import BLE_TIMING if not already imported
export const MAX_RECOVERY_ATTEMPTS = BLE_TIMING.RECOVERY_PHASE_1_MAX_ATTEMPTS + BLE_TIMING.RECOVERY_PHASE_2_MAX_ATTEMPTS;
```

**Verify:** Recovery now actually uses up to 12 Phase 1 attempts before switching to Phase 2 backoff. Log output confirms attempt counts > 5.

---

### Step 5: Wire `getSweepedDevice` from BleMachine invoke input (H3)

**Source:** [RecoveryService.ts L38](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L38) — `getSweepedDevice` is declared as optional in the `RecoveryInput` interface. [BleMachine.ts L244–256](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L244-L256) — The recoveryService invoke input does NOT include `getSweepedDevice`.

Phase 3 at [L160–222](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L160-L222) calls `getSweepedDevice?.(deviceId)` which always returns `undefined`, making Phase 3 passive scan mode completely non-functional.

**Change in BleMachine.ts:**
1. Add `getSweepedDevice` to `BleMachineContext` in `BleMachine.types.ts`:
   ```typescript
   getSweepedDevice?: (deviceId: string) => Device | undefined;
   ```
2. Pass it in the recoveryService invoke input at [BleMachine.ts L247–256](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L247-L256):
   ```diff
    input: ({ context }) => ({
      bleManager: context.bleManager,
      ghostedDeviceIds: context.ghostedDeviceIds ?? [],
      adapterMapRef: context.adapterMapRef,
      mtuMapRef: context.mtuMapRef,
      disconnectListeners: context.disconnectListeners,
      handleOrganicDisconnect: context.handleOrganicDisconnect,
      onOrganicDisconnect: context.onOrganicDisconnect,
      handleNotification: context.handleNotification,
   +  getSweepedDevice: context.getSweepedDevice,
    }),
   ```

**Change in useBLE.ts:**
Wire `getSweepedDevice` in the machine input at [useBLE.ts L172–198](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L172-L198):
```typescript
getSweepedDevice: (deviceId: string) => {
  return allDevicesRef.current.find(d => d.id.toUpperCase() === deviceId.toUpperCase());
},
```

**Verify:** Phase 3 recovery can now detect a re-advertised device and reconnect it. Verify with logging: `AUTO_RECOVERY phase: 3, event: mac_reappeared_attempting_reconnect` should appear in logs when a ghosted device re-enters scan range.

---

## Out of Scope

- **BleMachine state transitions** (dead-end fixes, timeouts, event handlers) — covered by Plan 1
- **DashboardScreen.tsx** — covered by Plan 3
- **useDashboardAutoConnect.ts** — covered by Plan 3
- **HeartbeatService internals** — not audited in this batch
- **L8 (Phase 3 no recovery ping)** — low severity, deferred
- **L9 (Phase 3 single-retry give-up)** — low severity, deferred
- **L10 (BleError type cast)** — low severity, deferred

---

## Verification Matrix

| Step | What to verify | Method |
|------|----------------|--------|
| 1 | After 3 reconnects: `Object.keys(notificationListeners.current)` has ≤ N entries (no growth) | Manual BLE test with telemetry review |
| 2 | MTU failures appear in log output | `npm run verify` + log audit |
| 3 | Pre-connected device on Android 12+ doesn't throw "already connected" | Manual Android test |
| 4 | Recovery attempt counter exceeds 5 when needed (up to 12 Phase 1 + 5 Phase 2) | Log audit: `attempts` field in `AUTO_RECOVERY_SUCCESS` or `RecoveryService` warn logs |
| 5 | Phase 3 `mac_reappeared_attempting_reconnect` log entry fires when device re-advertises | Manual BLE test: disconnect device → wait for Phase 1/2 exhaustion → power device back on → verify Phase 3 reconnects |
