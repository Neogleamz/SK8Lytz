# Implementation Plan: audit-fixes-ble-protocol

**Slug:** `fix/audit-fixes-ble-protocol`
**Batch:** `[BATCH:audit-fixes-ble-protocol]`
**Source Audit:** [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md)
**Findings Addressed:** H2 (HIGH — Mixed Zengge+BanlanX protocol dispatch gap)

---

## Background

The BLE Payload Dispatch audit found that `useControllerDispatch` bypasses the `IControllerProtocol` adapter layer entirely. It imports `ZenggeProtocol` directly and calls `ZenggeProtocol.setMultiColor()` (static namespace) regardless of the connected device type. Similarly, `PatternEngine.ts:192` calls the Zengge static directly.

**Impact:** Any BanlanX device (e.g., SP621E) connected in the same group as a Zengge device receives Zengge `0x59` format packets. The BanlanX hardware ignores these, producing no color output. Solo BanlanX ✅ | Solo Zengge ✅ | Mixed group ❌ for all pattern writes from DockedController.

**Root cause comment found in source:** `useControllerDispatch.ts:10-11` explicitly says "Do NOT call useProtocolDispatch() here — it creates an orphan useBLE instance." This rationale is valid. The fix is NOT to call `useProtocolDispatch`, but to use the adapter already registered in `adapterMapRef` at connection time.

---

## Cited Truth

```
Source: src/hooks/useControllerDispatch.ts:17-21
  import ZenggeProtocol from '../protocols/ZenggeProtocol';
  import { buildPatternPayload } from '../protocols/PatternEngine';
  // ZenggeProtocol.setMultiColor() called directly at line 73

Source: src/protocols/PatternEngine.ts:192
  ZenggeProtocol.setMultiColor(scaledPixels, hardwareLedPoints ?? numLEDs, speed, direction, transitionType)
  // Static call — no per-device adapter routing

Source: src/hooks/useBLE.ts:64-66
  // BluetoothLowEnergyApi exposes getAdapterForDevice(deviceId: string): IControllerProtocol | undefined
  // This is already available via BLEContext — safe to consume in DockedController

Source: src/protocols/IControllerProtocol.ts
  // Interface already defines: setMultiColor(colors, ledPoints, speed, dir, transition): number[][]
  // BanlanxAdapter and ZenggeAdapter both implement this interface
```

---

## Architecture Decision

The fix routes pattern calls through the per-device adapter from `adapterMapRef` (populated during `createGattSession`). This is the same map used by `BleWriteDispatcher.ts:143` for actual writes — fully consistent.

**Rejected alternative:** Calling `useProtocolDispatch()` inside `useControllerDispatch` — explicitly banned by the comment at line 10-11, creates orphan BLE instances.

**Rejected alternative:** Making `buildPatternPayload` adapter-aware — would require passing adapter into `PatternEngine`, increasing coupling for a utility that is currently stateless.

---

## Step 1 — Read target files (Look-Before-Leap)

View these exact lines before writing:
- `src/hooks/useControllerDispatch.ts` — full file
- `src/protocols/PatternEngine.ts:180–220` (the `applyPattern` function)
- `src/hooks/useBLE.ts:64–66` (getAdapterForDevice signature)
- `src/protocols/IControllerProtocol.ts` — setMultiColor signature

---

## Step 2 — Extend `useControllerDispatch` to accept adapter map

**Source:** `src/hooks/useControllerDispatch.ts`

Add `getAdapterForDevice: (mac: string) => IControllerProtocol | undefined` to the `UseControllerDispatchParams` interface. This function is already exposed by `BluetoothLowEnergyApi` (useBLE) and BLEContext.

```typescript
export interface UseControllerDispatchParams {
  writeToDevice: WriteFn;
  hwSettings: HWSettings | null;
  points: number;
  // NEW:
  getAdapterForDevice: (mac: string) => IControllerProtocol | undefined;
  primaryDeviceId?: string;  // MAC of the primary device being controlled
}
```

**Verify:** TS error appears at `DockedController.tsx` call site — expected, fixed in Step 3.

---

## Step 3 — Route `setMultiColor` through adapter in `useControllerDispatch`

**Source:** `src/hooks/useControllerDispatch.ts:73` (the direct `ZenggeProtocol.setMultiColor()` call)

Replace the static call:
```typescript
// BEFORE (Zengge-only):
const packets = ZenggeProtocol.setMultiColor(colors, ledPoints, speed, dir, transition);

// AFTER (adapter-routed):
const adapter = getAdapterForDevice(primaryDeviceId ?? '');
const protocol = adapter ?? ZenggeProtocol; // Zengge as safe fallback for unresolved devices
const packets = protocol.setMultiColor(colors, ledPoints, speed, dir, transition);
```

The Zengge fallback is intentional: if `primaryDeviceId` is not in `adapterMapRef` (e.g., device is reconnecting), behaviour is unchanged from before. No regression for existing Zengge-only users.

**Verify:** `getAdapterForDevice` called with correct MAC returns `BanlanxAdapter` for BanlanX devices.

---

## Step 4 — Pass `getAdapterForDevice` from DockedController

**Source:** `src/components/DockedController.tsx:551-560` (useControllerDispatch call site)

`DockedController` already receives `writeToDevice` and other BLE props. Add `getAdapterForDevice` from the same source:

```typescript
// DockedController already has access to useBLE via BLEContext
// The getAdapterForDevice function is part of BluetoothLowEnergyApi
const { writeToDevice, getAdapterForDevice, ... } = useBLE(); // or from props

useControllerDispatch({
  writeToDevice,
  hwSettings,
  points,
  getAdapterForDevice,            // NEW
  primaryDeviceId: primaryMac,    // MAC of this DockedController's device
});
```

**Verify:** `tsc --noEmit` 0 errors at DockedController.tsx.

---

## Step 5 — Fix `PatternEngine.ts:192` static Zengge call

**Source:** `src/protocols/PatternEngine.ts:192`

`buildPatternPayload` is called from `useControllerDispatch`. The static `ZenggeProtocol.setMultiColor()` call here is the same root cause. Fix by accepting an optional `protocol: IControllerProtocol` parameter with Zengge as default:

```typescript
export function buildPatternPayload(
  patternId: number,
  ...existingParams...,
  protocol: IControllerProtocol = ZenggeProtocol,
): number[][] {
  ...
  // Line 192 becomes:
  return protocol.setMultiColor(scaledPixels, hardwareLedPoints ?? numLEDs, speed, direction, transitionType);
}
```

Pass `protocol` from `useControllerDispatch` when calling `buildPatternPayload`.

**Verify:** Existing Zengge-only call sites get default protocol — zero regressions. BanlanX gets adapter-specific encoding.

---

## Step 6 — Verify & Commit

```bash
npm run verify
```
- TSC: 0 errors ✅
- Jest: 126/126 ✅ (all existing protocol tests pass — new parameter is optional with Zengge default)
- Commit: `fix(ble): route pattern dispatch through per-device adapter — fix BanlanX mixed-group`

---

## Out of Scope
- Changing BleWriteDispatcher (already adapter-routed correctly at L143)
- Changing useOptimisticBLE
- Changing BleWriteQueue
- Changing BanlanxAdapter or ZenggeAdapter internals
- DockedController god-object extraction (that's `refactor/god-object-split`)
