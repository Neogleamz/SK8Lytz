# Implementation Plan: fix/wizard-ftue-scan

**Slug:** `fix/wizard-ftue-scan`  
**Batch:** Standalone (no file overlap with existing batches)  
**Priority:** 🔴 P0 BLOCKER — New users cannot complete onboarding  
**Source Audit:** 2026-06-09 direct source read — Strike 2 on this bug. Theory 1 confirmed.  
**Previous attempts:** 2 prior fixes failed because they patched scan window/RSSI instead of the async race root cause.

---

## Root Cause (Confirmed from Source)

**The race:**
1. BT permission granted → `isBluetoothEnabled = true` → `DashboardScreen` fires `startSweeper()`
2. `startSweeper()` is **async** — calls `Battery.getBatteryLevelAsync()` before starting scan
   - Source: `src/hooks/ble/useBLEBatterySweep.ts:65`
3. **Simultaneously**, wizard `useEffect` fires `handleStartScan()` → `scanForPeripherals()`
4. `scanForPeripherals()` checks `isSweeperActive` — **still false** (battery check hasn't resolved)
   - Source: `src/hooks/ble/useBLEScanner.ts:291`
5. Hits `else` branch: raw 5-second `startDeviceScan`, then **hard stop** with no retry
   - Source: `src/hooks/ble/useBLEScanner.ts:300-306`
6. Wizard is stuck on step 1. keepAlive only fires on `step === 2` — never reached.
   - Source: `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:113`

**Why previous fixes failed:** They patched the 5s timeout and RSSI threshold. Neither fixed the `isSweeperActive` race condition.

---

## Cited Truth

```
Source: src/hooks/ble/useBLEScanner.ts:291-307
  if (isSweeperActive) {
    burstScan(options?.keepAlive ? 10000 : 5000);
  } else {
    bleSend({ type: 'SCAN_START' });
    bleManager?.startDeviceScan(null, null, scanCallback);
    setTimeout(() => {
      bleManager?.stopDeviceScan();           // ← HARD STOP
      bleSend({ type: 'SCAN_STOP' });
      scannerStateRef.current = 'IDLE';
    }, 5000);                                 // ← NO RETRY
  }

Source: src/hooks/ble/useBLEBatterySweep.ts:65
  const level = await Battery.getBatteryLevelAsync();  // ← ASYNC — isSweeperActive lags

Source: src/screens/Onboarding/HardwareSetupWizardScreen.tsx:113
  if (hasStartedScan && step === 2 && pendingRegistrations.length === 0 && ...)
  // ← step === 2 gate: keepAlive NEVER fires when stuck on step 1 with 0 devices
```

---

## Industry Benchmark

Govee, LIFX, Philips Hue, Sonos all use the same pattern: **discovery is a service-level concern, not a wizard-level concern.** The scan starts when BT is enabled and runs continuously. The wizard is a passive subscriber to discovery results — it never calls `startScan()` itself.

Our architectural error: the wizard is BOTH the display layer and the scan trigger. This is why we keep fighting this.

---

## Step 1 — Fix `scanForPeripherals` FTUE path

**File:** `src/hooks/ble/useBLEScanner.ts`  
**Lines to view first:** 285-315 (the full `scanForPeripherals` function)

**Change:** When `registeredMacs.length === 0` (FTUE path, no known hardware), call `startSweeper()` directly instead of checking `isSweeperActive`. This routes FTUE through the persistent, battery-aware sweeper — not a one-shot raw scan.

```typescript
const scanForPeripherals = useCallback((options?: { keepAlive?: boolean }) => {
  if (!options?.keepAlive) {
    setPendingRegistrations([]);
    rejectedMacsRef.current.clear();
    setAllDevices([]);
    allDevicesRef.current = [];
  }

  // FTUE path: 0 registered devices = persistent sweep until hardware found
  // Bypasses isSweeperActive race — startSweeper() is idempotent when already running
  if (registeredMacs.length === 0 && !options?.keepAlive) {
    startSweeper();
    return;
  }

  if (isSweeperActive) {
    burstScan(options?.keepAlive ? 10000 : 5000);
  } else {
    bleSend({ type: 'SCAN_START' });
    scannerStateRef.current = 'SCANNING';
    bleManager?.startDeviceScan(null, null, scanCallback);
    setTimeout(() => {
      bleManager?.stopDeviceScan();
      bleSend({ type: 'SCAN_STOP' });
      scannerStateRef.current = 'IDLE';
    }, 5000);
  }
}, [registeredMacs.length, isSweeperActive, burstScan, startSweeper, bleManager, scanCallback, bleSend, setAllDevices]);
```

**Verify:** 
- `startSweeper()` is idempotent — calling it when already running is a documented no-op
- Source-check: `useBLEBatterySweep.ts` — confirm `startSweeper` has no double-start side effects (if not, the L4 scan guard fix in `audit-fixes-scanner` covers this)
- `tsc --noEmit` — 0 errors

---

## Step 2 — Fix keepAlive to fire on step 1

**File:** `src/screens/Onboarding/HardwareSetupWizardScreen.tsx`  
**Lines to view first:** 108-125 (the keepAlive `useEffect`)

**Change:** Remove the `step === 2` gate from the keepAlive condition so it also retriggers scanning on step 1 when stuck with 0 devices:

```typescript
// BEFORE:
if (hasStartedScan && step === 2 && pendingRegistrations.length === 0 && bleState !== 'SCANNING' && bleState !== 'PROBING') {

// AFTER:
if (hasStartedScan && (step === 1 || step === 2) && pendingRegistrations.length === 0 && bleState !== 'SCANNING' && bleState !== 'PROBING') {
```

The 2000ms timer already exists — this change just lets it fire on step 1 as well. The call is `scanForPeripherals({ keepAlive: true })` which does NOT wipe `pendingRegistrations` (the `keepAlive` guard prevents that).

**Verify:**
- Wizard on step 1 with 0 devices: `bleState` transitions to `SCANNING` every 2s until device appears
- Wizard on step 2 with 0 devices: same as before

---

## Step 3 — Confirm `startSweeper` is idempotent (Look-Before-Leap)

**File:** `src/hooks/ble/useBLEBatterySweep.ts`  
**Lines to view:** 60-110 (`startSweeper` implementation)

Verify `startSweeper()` can be called when already active without side effects. If it has an early return for the active case → ✅ safe to call from `scanForPeripherals`. If it doesn't → add a guard at the top of `startSweeper`:

```typescript
if (isSweeperActiveRef.current) return;
```

**Verify:** No double-start crash in dev. `_isScanActiveRef` guard (from `audit-fixes-scanner` L4 fix) will also protect this path once that batch merges.

---

## Step 4 — Add AppLogger for FTUE scan path

In the new FTUE branch, add:
```typescript
AppLogger.log('BLE_STATE_CHANGE', { event: 'ftue_persistent_scan_start', registeredMacs: 0 });
```

This makes the FTUE scan path visible in telemetry so we can confirm it's firing correctly.

**Verify:** Dev logcat shows `ftue_persistent_scan_start` event when wizard opens with 0 registered devices.

---

## Step 5 — Verify & Commit

```bash
npm run verify
```
- TSC: 0 errors ✅
- Jest: 126/126 ✅
- Manual test: Fresh install state (clear AsyncStorage) → open app → grant BT permissions → wizard opens → devices appear within 10-15s without touching "Retry"
- Commit: `fix(ble): persistent FTUE scan on wizard — fix async sweeper race causing infinite scanning`

---

## Out of Scope
- Redesigning the wizard UI
- Changing RSSI thresholds (Theory 2 — secondary concern once continuous scan is running)
- Changing the scan filter (Theory 3 — secondary concern)
- DashboardScreen refactoring
- Any changes to BleConnectionManager, BleSessionFactory, or auth flows

---

## Why This Won't Fail a Third Time

- Previous Fix 1: patched 5s → 10s timeout. Didn't fix the race. Failed.
- Previous Fix 2: adjusted RSSI threshold. Didn't fix the race. Failed.
- **This fix:** Routes FTUE through `startSweeper()` which is the correct persistent scan path. `isSweeperActive` race is eliminated because FTUE bypasses the check entirely. keepAlive fires on step 1 as a belt-and-suspenders fallback.
