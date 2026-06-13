# PLAN: Hardware Stress Test Validation

**Slug**: `fix/hw-stress-test-validation`
**Created**: 2026-04-22
**Status**: 🔲 Not Started — requires 2 physical devices
**Risk**: H-RISK (BLE resilience architecture)
**Bucket List**: [SK8Lytz_Bucket_List.md](../SK8Lytz_Bucket_List.md)

---

## Overview

This is a **physical hardware testing milestone**, not a code change. The BLE AutoRecovery engine is built. The Group Sync architecture is built. This task validates both under real-world stress conditions before the app can be called production-stable.

Test protocol is written in `tools/SK8Lytz_TEST_PLAN.md` — Section 17 (6 sub-sections, 26 test steps).

---

## Prerequisites

- 2 physical HALOZ controllers (or 1 HALOZ + 1 SOULZ)
- Android device with latest debug APK installed
- ADB connected for logcat monitoring
- `Sk8LytzDiagnosticLab` accessible in app

---

## Test Suite: Section 17 — BLE Stress & AutoRecovery

### 17.1 — Group Connect Under Load

**Goal**: Two devices in the same group both receive and execute a pattern write simultaneously.

**Steps**:
1. Pair both devices to the app
2. Add both to the same Group
3. Select pattern from UI → observe both devices light simultaneously
4. Time from tap to both devices lit: must be < 2 seconds

**Pass criteria**: Both devices light within 2s. No `'partial'` return from either write.

---

### 17.2 — Forced Disconnect → AutoRecovery → Reconnect

**Goal**: When BLE connection drops mid-session, AutoRecovery restores without user action.

**Steps**:
1. Connect one device, set a pattern
2. Power cycle (turn off then on) the hardware controller
3. Wait — observe app behavior
4. App should: detect disconnect → enter RECOVERING state → attempt reconnect → restore pattern

**Pass criteria**:
- App shows disconnected state within 5 seconds of power cycle
- App begins reconnect attempt within 2 seconds
- Connection restored within 30 seconds
- Same pattern resumes on hardware (not blank)

---

### 17.3 — `'partial'` Write Verification

**Goal**: When a BLE write returns `'partial'` (characteristic write acknowledged but data incomplete), the system handles it gracefully.

**Steps**:
1. Enable verbose BLE logging: `AppLogger.setLevel('DEBUG')`
2. Trigger multiple rapid pattern changes (slider drag)
3. Look for `'partial'` returns in logcat
4. Observe UI behavior — should not crash, should not show stale state

**Pass criteria**: `'partial'` write logged, UI recovers without user action, next write succeeds.

---

### 17.4 — Gate Semaphore Safety Under Load

**Goal**: Concurrent BLE writes don't interleave or corrupt payload.

**Steps**:
1. While device is connected, spam 10 rapid pattern changes < 500ms apart
2. Monitor logcat for write queue behavior
3. Hardware should not show garbled/flickering patterns

**Pass criteria**: Hardware shows smooth transitions, no mid-payload corruption, semaphore hold/release logged correctly in DEBUG mode.

---

### 17.5 — MAX_RECOVERY Ejection

**Goal**: After N failed reconnect attempts, app stops trying and surfaces an error to the user.

**Steps**:
1. Connect device
2. Move device out of BLE range (or disable Bluetooth on controller side)
3. Wait for max recovery attempts to exhaust
4. Observe UI — should show "Device Offline" or equivalent error state, NOT infinite spinner

**Pass criteria**: Error state shown to user after MAX_RECOVERY attempts. Retry available via manual action.

---

### 17.6 — Chaos Cycling (Soak Test)

**Goal**: 10 minutes of random pattern changes, speed changes, and mode switches without crash.

**Steps**:
1. Connect both devices
2. Rapidly cycle through: Fixed patterns, Music mode, Programs mode, Builder
3. Adjust speed/brightness frequently
4. Let run for 10 minutes

**Pass criteria**: No app crash, no ANR dialog, no device disconnection without external cause.

---

## Logcat Monitoring Commands

```powershell
$ADB = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"

# Stream BLE-relevant logs
& $ADB logcat -s "BluetoothGatt:D BluetoothLeScanner:D ReactNativeJS:V"

# Clear buffer before test
& $ADB logcat -c

# Capture to file for later analysis
& $ADB logcat -d | Out-File "tools/stress-test-$(Get-Date -Format 'yyyyMMdd-HHmmss').log"
```

---

## Pass/Fail Log

Record results here after testing:

| Test | Status | Notes |
|------|--------|-------|
| 17.1 Group Connect | 🔲 | |
| 17.2 AutoRecovery | 🔲 | |
| 17.3 Partial Write | 🔲 | |
| 17.4 Semaphore Safety | 🔲 | |
| 17.5 MAX_RECOVERY | 🔲 | |
| 17.6 Chaos Soak | 🔲 | |

---

## If Tests Fail

Any failure means a code fix is required in `useBLE.ts` or the Group Sync architecture before the app can be considered production-stable. Open a new bug task with the specific failure details.
