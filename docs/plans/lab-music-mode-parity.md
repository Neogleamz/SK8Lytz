# Music Mode Parity & Protocol Hardening

Achieve 1:1 functional parity for Music Mode (0x73) between the app and hardware, including the missing Matrix Style toggle and improved Diagnostic Lab controls.

## Design Decisions & Rationale
- **Color Remapping**: As confirmed by laboratory testing, the hardware natively handles GRB remapping. We will explicitly remove/deprecate `applyColorSorting` from the 0x73 path to ensure "Pure RGB" delivery.
- **UI Consistency**: We will implement a reusable `QuickColorGrid` (10-preset) for the Diagnostic Lab to replace tedious numeric inputs, matching the `DockedController` experience.
- **Matrix Style**: We will expose the `0x27` (Screen) and `0x26` (Bar) hardware toggle in the main app's Music tab.

## User Review Required
> [!IMPORTANT]
> The **Master Reference** currently contains incorrect information about mandatory pre-swapping. I will be updating section `3.2.1` to reflect that the hardware handles this internally via EEPROM.

## Proposed Changes

---

### [Component] [ZenggeProtocol](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
Update the 0x73 signature to support Matrix Style and ensure pure RGB transmission.

#### [MODIFY] [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- Update `setMusicConfig` signature: `(isDeviceMic, matrixStyle, patternId, color1, color2, sensitivity, brightness)`.
- Map `matrixStyle` to `byte[2]`.

---

### [Component] [Diagnostic Lab](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/Sk8LytzDiagnosticLab.tsx)
Improve usability for Music Mode and Builder tabs.

#### [MODIFY] [Sk8LytzDiagnosticLab.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/Sk8LytzDiagnosticLab.tsx)
- Implementation of a reusable `QuickColorGrid` component.
- Replace `TextInput` fields for `COLOR 1` and `COLOR 2` in the 0x73 section with the grid.
- Update `useEffect` that generates the payload to use the updated `ZenggeProtocol` method.

---

### [Component] [DockedController](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
Add the missing Matrix Style toggle to the user-facing Music mode.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- Add `musicMatrixStyle` state (default `0x27`).
- Add a UI toggle for "Light Screen" vs "Light Bar" in the Music tab.
- Update `handleMusicChange` to propagate the matrix style.
- **Boy Scout**: Refactor the `AnalogGauge` props to use an interface instead of inline types.

---

### [Component] [Master Reference](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md)
Correct the protocol documentation.

#### [MODIFY] [SK8Lytz_App_Master_Reference.md](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md)
- Update section `3.2.1 Color Sorting` to clarify that HALOZ/SOULZ hardware performs internal remapping.

---

## Verification Plan

### Automated Tests
- CLI Verification: `npm run tsc` to ensure protocol signature changes don't break other modes.

### Manual Verification
1. **Lab Test**: Open Diagnostic Lab -> Builder -> 0x73. Tap a color preset. Verify the raw hex payload shows the correct RGB bytes in the correct positions.
2. **Parity Test**: Toggle "Light Bar" (0x26) in the Lab and TX. Verify hardware color application (specifically for Sound Column vs Drop color).
3. **App Test**: Open Music Mode in main app. Change Matrix Style to Light Bar. Verify hardware reacts correctly.
