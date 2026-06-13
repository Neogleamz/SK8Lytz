# Implementation Plan: audit-fixes-scanner

**Slug:** `fix/audit-fixes-scanner`
**Batch:** `[BATCH:audit-fixes-scanner]`
**Source Audit:** [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md)
**Findings Addressed:** M3 (BleLifecycleManager any-typed), M4 (Battery PAUSED silent), L4 (scanForPeripherals no guard), L5 (as unknown as any in Dashboard), L7 (RAILZ placeholder thresholds — SPIKE)

---

## Background

Four scanner/lifecycle bugs and one type safety gap found across the BLE scanner stack and peripheral lifecycle manager. Combined because they share the same domain cluster without file conflicts. L7 (RAILZ thresholds) becomes a research spike — actual LED counts require hardware confirmation.

---

## Cited Truth

```
Source: src/services/BleLifecycleManager.ts:11-18
  // bleManager: any, autoRecovery: any, adapterMapRef: MutableRefObject<Map<string, any>>
  // cancelAllRecoveries() call at L23 on autoRecovery: any — unverified by TS

Source: src/hooks/ble/useBLEBatterySweep.ts:65,69-72
  // Battery.getBatteryLevelAsync() called on startSweeper()
  // If < 15% (PAUSED tier), function silently returns: no scan, no user feedback

Source: src/hooks/ble/useBLEScanner.ts:299
  // bleManager.startDeviceScan() called without checking if scan already active
  // No _isScanning guard before startDeviceScan

Source: src/screens/DashboardScreen.tsx:1134
  // as unknown as any cast present — bypasses type system

Source: src/constants/ProductCatalog.ts:100-102
  // RAILZ entry: detectMinPoints: 1, detectMaxPoints: 9
  // Comment: "Placeholder thresholds — update when RAILZ hardware LED count is confirmed"
```

---

## Step 1 — Fix M3: Type BleLifecycleManager signature

**Source:** `src/services/BleLifecycleManager.ts:11-18`

View full file first (Look-Before-Leap).

Replace all `any` parameters with explicit types:

```typescript
import type { BleManager } from 'react-native-ble-plx';
import type { MutableRefObject } from 'react';
import type { IControllerProtocol } from '../protocols/IControllerProtocol';
import type { Device } from 'react-native-ble-plx';

export async function executeRealDisconnect(params: {
  bleManager: BleManager;
  autoRecovery: { cancelAllRecoveries: () => Promise<void> };
  adapterMapRef: MutableRefObject<Map<string, IControllerProtocol>>;
  disconnectListeners: MutableRefObject<Record<string, { remove: () => void }>>;
  setConnectedDevices: (devices: Device[]) => void;
  keepaliveTimerRef: MutableRefObject<ReturnType<typeof setTimeout> | null>;
}): Promise<void>
```

**Verify:** `tsc --noEmit` 0 errors. `cancelAllRecoveries()` call TS-verified. No `any` in file.

---

## Step 2 — Fix M4: Surface `isBatteryConstrained` flag and show warning

**Source:** `src/hooks/ble/useBLEBatterySweep.ts:65-72`

### Action A — Return flag from hook
Add `isBatteryConstrained: boolean` to the return value of `useBLEBatterySweep`. Set it to `true` when in PAUSED tier (< 15% battery).

```typescript
const [isBatteryConstrained, setIsBatteryConstrained] = useState(false);

// In startSweeper(), before the early return:
if (tier === BatterySweepTier.PAUSED) {
  setIsBatteryConstrained(true);
  AppLogger.warn('[BLE Sweeper] Battery critically low — scanning paused', { batteryLevel });
  return;
}
setIsBatteryConstrained(false);
```

### Action B — Expose via useBLEScanner
`useBLEBatterySweep` result is consumed inside `useBLEScanner`. Surface `isBatteryConstrained` from `useBLEScanner`'s return value so DashboardScreen can consume it.

### Action C — Show banner in DashboardScreen
**IMPORTANT:** `DashboardScreen.tsx` is a God Object (>30KB). Only add the minimum required lines — a conditional banner render. Do NOT refactor anything else.

Add a `isBatteryConstrained` prop read from `useBLEScanner` (via useBLE/BLEContext), and render a warning:
```tsx
{isBatteryConstrained && (
  <View style={styles.batteryWarningBanner}>
    <Text style={styles.batteryWarningText}>
      ⚠️ Battery critically low — device scanning paused
    </Text>
  </View>
)}
```

Add minimal styles for `batteryWarningBanner` and `batteryWarningText` to the existing StyleSheet at the bottom of DashboardScreen.

**Verify:** At < 15% battery OR in simulator with mock battery level, DashboardScreen shows banner. Sweeper logs warn. No crash.

---

## Step 3 — Fix L4: Add double-start guard to scanForPeripherals

**Source:** `src/hooks/ble/useBLEScanner.ts:299` (or wherever `bleManager.startDeviceScan` is called)

View the exact lines. Add a ref-based guard:

```typescript
const _isScanActiveRef = useRef(false);

// Before startDeviceScan call:
if (_isScanActiveRef.current) {
  AppLogger.warn('[BLE Scanner] startDeviceScan called while scan already active — skipping');
  return;
}
_isScanActiveRef.current = true;

// In the scan stop path (stopSweeper):
_isScanActiveRef.current = false;
```

**Verify:** Calling `startSweeper()` twice in rapid succession logs warn on second call and does not double-start native scan.

---

## Step 4 — Fix L5: Remove `as unknown as any` in DashboardScreen

**Source:** `src/screens/DashboardScreen.tsx:1134`

View line 1134 ±5 lines. Replace the double cast with a properly typed intermediate. If the cast is to satisfy a prop type mismatch, fix the prop type instead of casting.

**Boy Scout boundary:** Touch ONLY line 1134 and any directly adjacent lines required for the type fix. Do NOT touch any other code in this 50KB file.

**Verify:** `tsc --noEmit` 0 errors. `as unknown as any` removed from line 1134.

---

## Step 5 — L7: RAILZ threshold spike (research — no code change this task)

**Source:** `src/constants/ProductCatalog.ts:100-102`

RAILZ `detectMinPoints: 1, detectMaxPoints: 9` is an acknowledged placeholder. The actual LED count range for RAILZ hardware must be confirmed from:
1. Physical hardware measurement
2. Or ZENGGE_PROTOCOL_BIBLE.md if documented there
3. Or user confirmation

**This step is documentation only.** Read `tools/ZENGGE_PROTOCOL_BIBLE.md` for any RAILZ LED count references. If found → update the catalog. If not found → leave the placeholder and file as a spike in TRIAGE.

**Verify:** Either `detectMinPoints/MaxPoints` updated with evidence citation, or a spike task filed to TRIAGE with the research question.

---

## Step 6 — Verify & Commit

```bash
npm run verify
```
- TSC: 0 errors ✅
- Jest: 126/126 ✅
- Commit: `fix(ble): type BleLifecycleManager, surface battery constraint banner, add scan guard, remove any cast`

---

## Out of Scope
- DashboardScreen refactoring beyond the two-line banner add and one-line cast fix
- Battery UI redesign
- RSSI thresholds (handled in audit-fixes-ble-signal)
- Sweeper throttle tuning
