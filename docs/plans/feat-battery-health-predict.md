# Plan: `feat/battery-health-predict`

### Design Decisions & Rationale
Battery SoC data is not available directly from the Zengge hardware via any confirmed 0x63 response byte. We therefore implement a **Mathematical Consumption Model** as described in Section 4 of the Master Reference. The model runs as a stateless calculation service derived from elapsed session time, pixel density, brightness level, and the known product capacity. At 20% threshold, we invoke the "Limp Mode" auto-dim via the existing 0x71 command.

---

## Proposed Changes

### [NEW] `src/services/BatteryModelService.ts`
```typescript
interface IBatteryEstimate { estimatedPct: number; isLimp: boolean; estimatedRemainingMins: number; }
function estimate(productType: string, ledPoints: number, brightness: number, sessionStartMs: number): IBatteryEstimate
```
- **Capacity map:** SOULZ=2000mAh, HALOZ=1200mAh, RAILZ=2000mAh.
- **Drain rate formula:** Base draw × (ledPoints/maxPoints) × (brightness/100).
- **Limp Mode threshold:** `estimatedPct <= 20`.

### [MODIFY] `src/components/DockedController.tsx`
- Import `BatteryModelService`.
- On every 60-second `setInterval` inside the docked controller session, call `estimate()` and update a local `batteryPct` state.
- Display a subtle battery indicator pill in the DockedController header.
- When `isLimp === true`, automatically dispatch a brightness-dim command (0x71 to 20%) and show a toast warning.

### [MODIFY] `src/components/DeviceItem.tsx`
- Show the battery % estimate as a small icon badge on each device card.

---

## Open Questions
- **Q:** Should Limp Mode auto-dim happen silently or with a dismissible user confirmation toast?
- **Q:** Do we want a per-hardware override in `DeviceSettingsModal` for a custom capacity value (e.g. user has upgraded battery)?

## Verification Plan
1. Connect a device and open the DockedController.
2. Wait 5 minutes (or mock a 4-hour elapsed time in dev mode).
3. Confirm battery badge decrements and Limp Mode fires at 20%.
