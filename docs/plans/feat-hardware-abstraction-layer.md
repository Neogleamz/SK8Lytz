# Plan: `feat/hardware-abstraction-layer`

### Design Decisions & Rationale
Currently the UI calls `ZenggeProtocol.*` directly throughout `DockedController.tsx`. The Hardware Abstraction Layer (HAL) is a thin adapter interface that decouples the UI from the specific protocol implementation. The UI calls `HardwareController.setColor(rgb)` and the HAL dispatches the correct underlying protocol command. This makes adding RAILZ or a future OEM controller a zero-UI-code change.

---

## ⚠️ User Review Required
> [!IMPORTANT]
> This is an architectural change that touches every BLE command dispatch path. Estimated scope: `DockedController.tsx`, `ZenggeProtocol.ts`, and potentially the Diagnostic Lab. Must be executed on a long-lived feature branch with thorough testing before merge.

---

## Proposed Changes

### [NEW] `src/services/HardwareController.ts`
```typescript
interface IHardwareController {
  setColor(rgb: [number, number, number], targetId?: string): Promise<void>;
  setPattern(patternId: number, speed: number, brightness: number, targetId?: string): Promise<void>;
  setMusicMode(config: IMusicConfig, targetId?: string): Promise<void>;
  setBrightness(pct: number, targetId?: string): Promise<void>;
  setSpeed(pct: number, targetId?: string): Promise<void>;
  setPower(on: boolean, targetId?: string): Promise<void>;
  writeRaw(payload: number[], targetId?: string): Promise<void>;
}
```
- A `ZenggeHardwareController` concrete class implements this interface wrapping `ZenggeProtocol` + `useBLE.writeToDevice`.
- A `MockHardwareController` implements it for sandbox/web mode.
- `HardwareControllerFactory.get(productType)` returns the correct implementation.

### [MODIFY] `src/components/DockedController.tsx`
- Replace all direct `ZenggeProtocol.*` + `writeToDevice()` calls with `HardwareController.*` calls.

### [MODIFY] `src/context/` (whichever context provides BLE write)
- Inject `HardwareController` via context so it's available globally.

---

## Open Questions
- **Q:** Should the HAL be a singleton or injected per-connected-device? (Recommendation: singleton with `targetId` routing, as `writeToDevice` already supports targeting.)
- **Q:** Does the RAILZ hardware use the same Zengge protocol or a different set of commands?

## Verification Plan
1. All existing DockedController modes (color, pattern, music, effects) work identically after refactor.
2. Add a mock `RailzHardwareController` stub and verify `HardwareControllerFactory` can route to it.
